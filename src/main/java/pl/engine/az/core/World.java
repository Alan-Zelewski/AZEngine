package pl.engine.az.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private static final int MAX_COMPONENTS = 64;
    private final int maxEntities;
    private int nextEntityIndex = 0;
    private final int[] generations;
    private final int[] freeIndices;
    private int freeCount = 0;
    private final long[] signatures;
    private int nextComponentId = 0;
    private final Map<Class<?>, ComponentType<?>>
            componentRegistry =
            new HashMap<>();
    private final ComponentType<?>[] componentTypes;
    private final List<Query> queries = new ArrayList<>();

    public World(int maxEntities) {
        this.maxEntities = maxEntities;
        this.generations = new int[maxEntities];
        this.freeIndices = new int[maxEntities];
        this.signatures = new long[maxEntities];
        componentTypes =
                new ComponentType[MAX_COMPONENTS];
    }

    // -----------------------
    // COMPONENT REGISTRATION
    // -----------------------

    public <T> ComponentType<T> registerComponent(Class<T> clazz) {
        if (componentRegistry.containsKey(clazz)) {
            throw new IllegalStateException("Component already registered.");
        }
        int id = nextComponentId++;
        ComponentType<T> type = new ComponentType<>(id, maxEntities);
        componentRegistry.put(clazz, type);
        componentTypes[id] = type;
        return type;
    }

    public <T> void addComponent(int entityId, ComponentType<T> type, T component
    ) {
        int index = EntityUtils.getIndex(entityId);
        type.storage()
                .put(index, component);
        signatures[index] |= 1L << type.id();
        updateQueries(entityId, signatures[index]);
    }

    // -----------------------
    // ENTITY CREATION
    // -----------------------
    public int createEntity() {
        int index;
        if (freeCount > 0) {
            freeCount--;
            index = freeIndices[freeCount];
        } else {
            if (nextEntityIndex >= maxEntities) {
                throw new IllegalStateException("Maximum entity count reached!");
            }
            index = nextEntityIndex++;
        }
        return EntityUtils.create(index, generations[index]);
    }

    // -----------------------
    // ENTITY VALIDATION
    // -----------------------
    public boolean isAlive(int entityId) {
        int index = EntityUtils.getIndex(entityId);
        return index >= 0 && index < nextEntityIndex && generations[index] == EntityUtils.getGeneration(entityId);
    }

    // -----------------------
    // DESTROY
    // -----------------------
    public void destroyEntity(int entityId) {
        if (!isAlive(entityId))
            return;
        int index = EntityUtils.getIndex(entityId);
        clearComponents(index);
        updateQueries(entityId, 0);
        generations[index]++;
        freeIndices[freeCount] = index;
        freeCount++;
    }

    public void clearComponents(int entityIndex) {
        long signature = signatures[entityIndex];
        while (signature != 0) {
            int componentId = Long.numberOfTrailingZeros(signature);
            ComponentType<?> type = componentTypes[componentId];
            type.storage().remove(entityIndex);
            signature &= ~(1L << componentId);
        }
        signatures[entityIndex] = 0;
    }

    public <T> void removeComponents(int entityId, ComponentType<T> type) {
        int index = EntityUtils.getIndex(entityId);
        type.storage().remove(index);
        signatures[index] &= ~(1L << type.id());
        updateQueries(
                entityId,
                signatures[index]
        );
    }

    // -----------------------
    // SIGNATURE
    // -----------------------
    public void addSignature(int entity, ComponentType<?> type) {
        int index = EntityUtils.getIndex(entity);
        signatures[index] |= 1L << type.id();
    }

    public long signature(int entity) {
        return signatures[EntityUtils.getIndex(entity)];
    }

    // -----------------------
    // QUERY
    // -----------------------
    public Query createQuery(long requiredMask) {
        Query query = new Query(requiredMask, maxEntities);
        queries.add(query);
        for (int i = 0; i < nextEntityIndex; i++) {
            int entityId = EntityUtils.create(i, generations[i]);
            if (!isAlive(entityId)) continue;
            if ((signatures[i] & requiredMask) == requiredMask) {
                query.add(entityId);
            }
        }
        return query;
    }

    private void updateQueries(int entityId, long signature) {
        for (Query query : queries) {
            query.evaluate(entityId, signature);
        }
    }
}
