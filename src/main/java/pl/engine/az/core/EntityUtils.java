package pl.engine.az.core;

public class EntityUtils {
    private static final int INDEX_BITS = 20;
    private static final int INDEX_MASK = (1 << INDEX_BITS) -1;

    public static int create(int index, int generation) {
        return (generation << INDEX_BITS) | index;
    }

    public static int getIndex(int entity) {
        return entity & INDEX_MASK;
    }

    public static int getGeneration(int entity) {
        return entity >>> INDEX_BITS;
    }
}
