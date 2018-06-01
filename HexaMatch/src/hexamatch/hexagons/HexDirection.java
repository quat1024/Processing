package hexamatch.hexagons;

public enum HexDirection {

    Dir0(1, 0, -1),
    Dir1(1, -1, 0),
    Dir2(0, -1, 1),
    Dir3(-1, 0, 1),
    Dir4(-1, 1, 0),
    Dir5(0, 1, -1);

    public static final int length = values().length;

    public final int q;
    public final int r;
    public final int s;

    HexDirection(int q, int r, int s) {

        this.q = q;
        this.r = r;
        this.s = s;

    }

    public int get(int index) {

        switch (index) {
            case 0:
                return q;
            case 1:
                return r;
            case 2:
                return s;
            default:
                return -1;
        }

    }

    @Override
    public String toString() {

        return String.format("%d, %d, %d", q, r, s);

    }

}
