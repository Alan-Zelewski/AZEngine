package pl.engine.az.ecs;

public interface EntityCommandBuffer {
    int createEntity();
    void destroyEntity(int entity);
    <T> void addComponent(int entity, ComponentType<T> type, T component);
    <T> void removeComponent(int entity, ComponentType<T> type);
    <T> void setComponent(int entity, ComponentType<T> type, T component);
    void playback(World world);
}
