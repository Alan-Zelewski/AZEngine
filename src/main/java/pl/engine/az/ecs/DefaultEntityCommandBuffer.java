package pl.engine.az.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultEntityCommandBuffer implements EntityCommandBuffer {
    private int nextVirtualId = -1;
    private int[] virtualToReal = new int[128];

    private interface Command {
        void execute(World world, int[] mapping);
    }

    private final List<Command> commands = new ArrayList<>();

    private int resolve(int entity, int[] mapping) {
        return entity < 0 ? mapping[-entity] : entity;
    }

    @Override
    public int createEntity() {
        final int vId = nextVirtualId--;
        if (-vId >= virtualToReal.length) {
            virtualToReal = Arrays.copyOf(virtualToReal, virtualToReal.length * 2);
        }
        commands.add((world, mapping) -> {
            int realId = world.createEntity();
            mapping[-vId] = realId;
        });
        return vId;
    }

    @Override
    public void destroyEntity(int entity) {
        commands.add(((world, mapping) -> world.destroyEntity(resolve(entity, mapping))));
    }

    @Override
    public <T> void addComponent(int entity, ComponentType<T> type, T component) {
        commands.add(((world, mapping) -> world.addComponent(resolve(entity, mapping), type, component)));
    }

    @Override
    public <T> void removeComponent(int entity, ComponentType<T> type) {
        commands.add(((world, mapping) -> world.removeComponents(resolve(entity, mapping), type)));
    }

    @Override
    public <T> void setComponent(int entity, ComponentType<T> type, T component) {
        commands.add(((world, mapping) -> world.addComponent(resolve(entity, mapping), type, component)));
    }

    @Override
    public void playback(World world) {
        for (Command command : commands) {
            command.execute(world, virtualToReal);
        }
        commands.clear();
        nextVirtualId = -1;
    }
}
