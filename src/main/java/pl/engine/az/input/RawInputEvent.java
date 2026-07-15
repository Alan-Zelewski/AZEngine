package pl.engine.az.input;

public record RawInputEvent(Type type, InputKey key) {
    public enum Type {
        PRESSED,
        RELEASED
    }
}
