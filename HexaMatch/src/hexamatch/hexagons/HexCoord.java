package hexamatch.hexagons;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HexCoord {

    public final int q;
    public final int r;
    public final int s;

    private final int hashCode;

    public HexCoord(int q, int r) {

        this.q = q;
        this.r = r;
        this.s = -q - r;

        final int A = (q >= 0 ? 2 * q : -2 * q - 1);
        final int B = (r >= 0 ? 2 * r : -2 * r - 1);
        final int C = (A >= B ? A * A + A + B : A + B * B) / 2;

        this.hashCode = q < 0 && r < 0 || q >= 0 && r >= 0 ? C : -C - 1;

    }

    public static HexCoord round(double q, double r) {

        double cubeX = q;
        double cubeY = -q - r;
        double cubeZ = r;

        int i = (int) Math.round(cubeX);
        int j = (int) Math.round(cubeY);
        int k = (int) Math.round(cubeZ);

        double xDiff = Math.abs(i - cubeX);
        double yDiff = Math.abs(j - cubeY);
        double zDiff = Math.abs(k - cubeZ);

        if (xDiff > yDiff && xDiff > zDiff) {
            i = -j - k;
        } else if (yDiff > zDiff) {
            j = -i - k;
        } else {
            k = -i - j;
        }

        return new HexCoord(i, k);

    }

    public static HexCoord rotate(HexCoord point, HexCoord center, int steps) {

        int sign = (steps % 2 != 0) ? -1 : 1;

        List<Integer> rotation = Arrays.asList(
                (point.q - center.q) * sign,
                (point.s - center.s) * sign,
                (point.r - center.r) * sign
        );

        Collections.rotate(rotation, steps);

        return new HexCoord(
                rotation.get(0) + center.q,
                rotation.get(2) + center.r
        );

    }

    public static int distanceBetween(HexCoord a, HexCoord b) {
        return Math.max(
                Math.abs(a.q - b.q),
                Math.max(
                        Math.abs(a.r - b.r),
                        Math.abs(a.s - b.s)
                )
        );
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
    public int hashCode() {

        return hashCode;

    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        } else if (obj instanceof HexCoord) {
            HexCoord other = (HexCoord) obj;
            return other.q == q && other.r == r;
        }
        return false;

    }

    @Override
    public String toString() {

        return String.format("%d, %d, %d", q, r, s);

    }

}
