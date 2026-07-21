package pl.engine.az.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.engine.az.system.EngineSystem;
import pl.engine.az.system.UpdateSystem;

@AllArgsConstructor
@Getter
public class SystemNode<T extends EngineSystem> {
    private final T system;
    private final SystemDescriptor descriptor;
}
