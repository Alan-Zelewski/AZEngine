package pl.engine.az.core;

import java.util.EnumSet;

public enum SystemTag {
    CORE,
    GAMEPLAY,
    UI,
    RENDER,
    AUDIO;

    public static long toMask(EnumSet<SystemTag> tags) {
        long mask = 0L;
        for (SystemTag tag : tags) {
            mask |= 1L << tag.ordinal();
        }
        return mask;
    }
}
