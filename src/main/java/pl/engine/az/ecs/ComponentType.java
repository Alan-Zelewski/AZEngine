package pl.engine.az.ecs;

public final class ComponentType<T> {

    private final int id;
    private final SparseSet<T> storage;

    public ComponentType(int id, int maxEntities) {
        this.id = id;
        this.storage = new SparseSet<>(maxEntities);
    }

    public int id() {
        return id;
    }

    public SparseSet<T> storage() {
        return storage;
    }
}
