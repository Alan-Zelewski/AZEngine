package pl.engine.az.world;


public record WorldBounds(double x, double y, double width, double height) {
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMaxX() {
        return x + width;
    }

    public double getMaxY() {
        return y + height;
    }
}
