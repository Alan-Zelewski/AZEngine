package pl.engine.az.core;

import java.util.Arrays;

public final class Query {
    private final long requiredMask;
    private final int[] sparse;
    private final int[] dense;
    private int size;

    public Query(long requiredMask, int maxEntities) {
        this.requiredMask = requiredMask;
        this.sparse = new int[maxEntities];
        Arrays.fill(sparse, -1);
        this.dense = new int[maxEntities];
    }

    public boolean matches(long signature) {
        return (signature & requiredMask) == requiredMask;
    }

    public void evaluate(int entityId, long entitySignature) {
        if (matches(entitySignature)) {
            add(entityId);
        } else {
            remove(entityId);
        }
    }

    public boolean contains(int entityId) {
        int index = EntityUtils.getIndex(entityId);
        int denseIndex = sparse[index];
        return denseIndex != -1 && dense[denseIndex] == entityId;
    }

    public void add(int entityId) {
        int index = EntityUtils.getIndex(entityId);
        if (contains(entityId))
            return;
        sparse[index] = size;
        dense[size] = entityId;
        size++;
    }

    public void remove(int entityId) {
        int index = EntityUtils.getIndex(entityId);
        if (!contains(entityId)) return;
        int removed = sparse[index];
        int last = size - 1;
        int moved = dense[last];
        dense[removed] = moved;
        sparse[EntityUtils.getIndex(moved)] = removed;
        sparse[index] = -1;
        size--;
    }

    public int size() {
        return size;
    }

    public int entityAt(int index) {
        return dense[index];
    }
}
