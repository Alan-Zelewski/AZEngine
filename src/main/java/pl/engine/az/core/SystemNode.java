package pl.engine.az.core;

import pl.engine.az.system.EngineSystem;

public abstract class SystemNode<T extends EngineSystem> {

    private final T system;
    private final SystemDescriptor descriptor;
    private final long tagMask;

    public SystemNode(T system) {

        this.system = system;
        this.descriptor = system.descriptor();
        this.tagMask = SystemTag.toMask(descriptor.tags());
    }

    public final boolean canRun(long activeMask) {
        return (tagMask & activeMask) != 0;
    }

    public final T getSystem() {
        return system;
    }

    public final SystemDescriptor getDescriptor() {
        return descriptor;
    }
}
