package pl.engine.az.core;

import java.util.Arrays;

public class SparseSet<T> {
    private int[] sparse; // Indeks: EntityID, Wartość: Indeks w dense
    private int[] denseEntities; // Indeks: denseIndex, Wartość: EntityID
    private Object[] denseComponents; // Indeks: denseIndex, Wartość: Komponent
    private int size = 0;

    public SparseSet(int maxEntities) {
        sparse = new int[maxEntities];
        Arrays.fill(sparse, -1); // -1 oznacza, że encja nie ma tego komponentu
        denseEntities = new int[maxEntities];
        denseComponents = new Object[maxEntities];
    }

    public void put(int entityId, T component) {
        if (contains(entityId)) {
            // Nadpisz istniejący komponent
            denseComponents[sparse[entityId]] = component;
            return;
        }

        // Dodaj nowy komponent na końcu tablic gęstych
        int denseIndex = size;
        sparse[entityId] = denseIndex;
        denseEntities[denseIndex] = entityId;
        denseComponents[denseIndex] = component;
        size++;
    }

    public boolean contains(int entityId) {
        return entityId >= 0 && entityId < sparse.length && sparse[entityId] != -1;
    }

    @SuppressWarnings("unchecked")
    public T get(int entityId) {
        if (!contains(entityId))
            return null;
        return (T) denseComponents[sparse[entityId]];
    }

    public void remove(int entityId) {
        if (!contains(entityId))
            return;

        int removedIndex = sparse[entityId];
        int lastIndex = size - 1;
        int movedEntity = denseEntities[lastIndex];

        // Swap and pop
        denseEntities[removedIndex] = movedEntity;
        denseComponents[removedIndex] = denseComponents[lastIndex];
        sparse[movedEntity] = removedIndex;

        sparse[entityId] = -1; // Wyczyść wskaźnik usuniętej encji
        size--;
    }

    public int size() {
        return size;
    }

    public int entityAt(int index) {
        return denseEntities[index];
    }
}
