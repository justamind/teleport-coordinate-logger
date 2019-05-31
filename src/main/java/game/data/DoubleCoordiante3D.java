package game.data;

public class DoubleCoordiante3D {
    public double x;
    public double y;
    public double z;

    public DoubleCoordiante3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isInRange(DoubleCoordiante3D oldPosition, int i) {
        return Math.max(Math.max(Math.abs(this.x - oldPosition.x), Math.abs(this.y - oldPosition.y)), Math.abs(this.z - oldPosition.z)) <= i;
    }

    @Override
    public String toString() {
        return "(" +
            "" + Math.round(x) +
            ", " + Math.round(y) +
            ", " + Math.round(z) +
            ')';
    }
}
