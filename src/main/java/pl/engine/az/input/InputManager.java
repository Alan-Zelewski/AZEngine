package pl.engine.az.input;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputManager {

    /*
    Action -> klawisze

    MOVE_LEFT:
        A
        LEFT
    */
    private final EnumMap<Action, Set<InputKey>> actionBindings = new EnumMap<>(Action.class);

    /*
        Klawisz -> akcje

        A:
        MOVE_LEFT
    */
    private final Map<InputKey, EnumSet<Action>> keyBindings = new HashMap<>();

    /*
    Fizycznie wciśnięte klawisze
    */
    private final Set<InputKey> activeKeys = new HashSet<>();

    /*
    Logicznie aktywne akcje
 */
    private final EnumSet<Action> activeActions = EnumSet.noneOf(Action.class);

    /*
Akcje, ktore w tej klatce przeszly nieaktywna -> aktywna
(wykrywane w momencie przetwarzania eventu, a nie przez diff)
*/
    private final EnumSet<Action> justPressedActions = EnumSet.noneOf(Action.class);

    /*
Akcje, ktore w tej klatce przeszly aktywna -> nieaktywna
*/
    private final EnumSet<Action> justReleasedActions = EnumSet.noneOf(Action.class);


    /*
    Stan poprzedniej klatki
 */
    private final EnumSet<Action> previousActions = EnumSet.noneOf(Action.class);

    /*
       Eventy z AWT
    */
    private final Queue<RawInputEvent> events = new ConcurrentLinkedQueue<>();

    public void postEvent(RawInputEvent event) {
        events.offer(event);
    }

    public void bind(Action action, InputKey key) {
        actionBindings.computeIfAbsent(action, a -> new HashSet<>()).add(key);
        keyBindings.computeIfAbsent(key, k -> EnumSet.noneOf(Action.class)).add(action);
    }

    public void beginFrame() {
        justPressedActions.clear();
        justReleasedActions.clear();

        RawInputEvent event;

        while ((event = events.poll()) != null) {
            switch (event.type()) {
                case PRESSED -> keyPressed(event.key());
                case RELEASED -> keyReleased(event.key());
            }
        }
    }

    private void keyPressed(InputKey key) {
        boolean alreadyPhysicallyDown = !activeKeys.add(key);
        if (alreadyPhysicallyDown) {
            // auto-repeat z AWT - klawisz juz byl wcisniety, nic sie logicznie nie zmienia
            return;
        }
        EnumSet<Action> actions = keyBindings.get(key);
        if (actions == null) {
            return;
        }
        for(Action action : actions) {
            if (activeActions.add(action)) {
                // akcja faktycznie przeszla z nieaktywnej w aktywna w tej klatce
                justPressedActions.add(action);
                // jesli w tej samej klatce zdazyla sie juz zwolnic wczesniej, to
                // ten nowy press ja "odswieza" - usuwamy stare just-released
                justReleasedActions.remove(action);
            }
        }
    }

    private void keyReleased(InputKey key) {
        activeKeys.remove(key);
        EnumSet<Action> actions = keyBindings.get(key);
        if (actions == null) {
            return;
        }
        for (Action action : actions) {
            if (!isAnyBindingActive(action) && activeActions.remove(action)) {
                // akcja faktycznie przeszla z aktywnej w nieaktywna w tej klatce
                justReleasedActions.add(action);
            }
        }
    }

    private boolean isAnyBindingActive(Action action) {
        Set<InputKey> keys = actionBindings.get(action);
        if (keys == null) {
            return false;
        }
        for (InputKey key : keys) {
            if (activeKeys.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActionActive(Action action) {
        return activeActions.contains(action);
    }

    public boolean wasPressed(Action action) {
        return activeActions.contains(action) && !previousActions.contains(action);
    }

    public boolean wasReleased(Action action) {
        return !activeActions.contains(action) && previousActions.contains(action);
    }

    public void loadConfig(InputConfig config) {
        config.getBindings().forEach((action, keys) -> {
            for (Integer key : keys) {
                bind(action, new InputKey(key));
            }
        });
    }
}
