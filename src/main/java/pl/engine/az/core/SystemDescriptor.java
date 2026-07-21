package pl.engine.az.core;

import pl.engine.az.system.phase.SystemPhase;

import java.util.EnumSet;

public record SystemDescriptor(String name, SystemPhase phase, EnumSet<SystemTag> tags) {
}
