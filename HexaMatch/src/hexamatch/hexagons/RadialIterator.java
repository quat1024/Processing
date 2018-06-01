package hexamatch.hexagons;

import java.util.Iterator;

public class RadialIterator implements Iterable<HexCoord> {

    private final HexCoord center;
    private final int range;

    public RadialIterator(int range) {

        this(new HexCoord(0, 0), range);

    }

    public RadialIterator(HexCoord center, int range) {

        this.center = center;
        this.range = range;

    }

    public Iterator<HexCoord> iterator() {

        return new Iterator<HexCoord>() {

            private int q = -range;
            private int r = 0;

            private boolean hasNext = true;

            @Override
            public boolean hasNext() {

                return hasNext;

            }

            @Override
            public HexCoord next() {

                HexCoord response = new HexCoord(
                        q + center.q,
                        r + center.r
                );

                if (r < Math.min(range, -q + range)) {

                    r++;

                } else {

                    if (q < range) {
                        q++;
                    } else {
                        hasNext = false; //q = -range;
                    }

                    r = Math.max(-range, -q - range);

                }

                return response;

            }

        };

    }


}
