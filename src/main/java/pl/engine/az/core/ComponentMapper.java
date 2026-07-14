package pl.engine.az.core;

public final class ComponentMapper<T> {
    private final ComponentType<T> type;

    public ComponentMapper(ComponentType<T> type) {
        this.type = type;
    }

    public T get(int entity) {
        return type.storage().get(EntityUtils.getIndex(entity));
    }

    public void remove(int entity) {
        type.storage().remove(EntityUtils.getIndex(entity));
    }
}
