package pl.engine.az.core;

import pl.engine.az.system.phase.SystemPhase;

public record SystemDescriptor(String name, SystemPhase phase, long tags) {
}
