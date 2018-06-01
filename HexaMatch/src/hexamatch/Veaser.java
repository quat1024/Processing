package hexamatch;

public class Veaser {

    private final Easer x;
    private final Easer y;
    private double exp;
    private double threshold = 0.01d;

    public Veaser(Vector vector, double exp) {
        this(vector.x, vector.y, exp);
    }

    public Veaser(double x, double y, double exp) {
        this.x = new Easer(x, exp);
        this.y = new Easer(y, exp);
        this.exp = exp;
    }

    public void update() {
        x.update();
        y.update();
    }

    public void setTarget(Vector target) {
        setTargetX(target.x);
        setTargetY(target.y);
    }

    public void setTarget(double targetX, double targetY) {
        setTargetX(targetX);
        setTargetY(targetY);
    }

    public void setTargetX(double targetX) {
        x.setTarget(targetX);
    }

    public void setTargetY(double targetY) {
        y.setTarget(targetY);
    }

    public void snapToTarget(Vector target) {
        snapToTarget(
                target.x,
                target.y
        );
    }

    public void snapToTarget(double targetX, double targetY) {
        x.snapToTarget(targetX);
        y.snapToTarget(targetY);
    }

    public Vector getVector() {
        return new Vector(getX(), getY());
    }

    public double getX() {
        return x.value();
    }

    public double getY() {
        return y.value();
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double newExp) {
        exp = newExp;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double newThreshold) {
        threshold = newThreshold;
    }

    public boolean targetReached() {
        return x.targetReached() && y.targetReached();
    }

    private class Easer {

        private double value;
        private double target;

        public Easer(double initial, double exp) {
            this.value = initial;
            this.target = initial;
        }

        public void update() {
            value += (target - value) * exp;
        }

        public void setValue(double newValue) {
            this.value = newValue;
        }

        public void setTarget(double newTarget) {
            this.target = newTarget;
        }

        public void snapToTarget() {
            value = target;
        }

        public void snapToTarget(double newTarget) {
            value = newTarget;
            target = newTarget;
        }

        public double value() {
            return value;
        }

        public boolean targetReached() {
            return Math.abs(value - target) < threshold;
        }

    }

}
