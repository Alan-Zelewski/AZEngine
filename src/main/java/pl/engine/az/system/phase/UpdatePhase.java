package pl.engine.az.system.phase;

public enum UpdatePhase implements SystemPhase{
    INPUT,
    MOVEMENT,
    PHYSICS_PRE,
    PHYSICS_STEP,
    PHYSICS_POST,
    GAMEPLAY,
    ANIMATION
}
