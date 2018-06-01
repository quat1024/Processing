package hexamatch.hexagons;

import hexamatch.Veaser;
import hexamatch.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class Hexagon<H extends Hexagon<H>> {

    public final HexBoard<H> parent;
    public final List<H> neighbors;
    public final HexRenderer renderer;
    public HexCoord coordinates;

    public Hexagon(HexCoord coordinates, HexBoard<H> parent) {

        this.coordinates = coordinates;

        this.parent = parent;
        this.neighbors = new ArrayList<>();
        this.renderer = new HexRenderer();

    }

    public void updateNeighbors() {

        neighbors.clear();

        for (HexDirection direction : HexDirection.values()) {

            HexCoord neighborCoordinates = new HexCoord(
                    coordinates.q + direction.q,
                    coordinates.r + direction.r
            );

            neighbors.add(parent.getHex(neighborCoordinates));

        }

    }

    public class HexRenderer {

        public final static double lerpRate = 0.01d;

        public final Veaser center;
        public final Veaser[] corners;

        public HexRenderer() {

            this.center = new Veaser(parent.renderer.coordinatesToPosition(coordinates), lerpRate);
            this.corners = new Veaser[parent.renderer.baseCorners.length];

            for (int c = 0; c < parent.renderer.baseCorners.length; c++) {

                Vector baseCorner = parent.renderer.baseCorners[c];

                corners[c] = new Veaser(
                        baseCorner.x + center.getX(),
                        baseCorner.y + center.getY(),
                        lerpRate
                );

            }

        }

        public void resetCenter() {

            center.snapToTarget(
                    parent.renderer.coordinatesToPosition(coordinates)
            );

        }

        public void resetCorners() {

            for (int c = 0; c < corners.length; c++) {

                Vector baseCorner = parent.renderer.baseCorners[c];

                corners[c].snapToTarget(
                        new Vector(
                                baseCorner.x + center.getX(),
                                baseCorner.y + center.getY()
                        )
                );

            }

        }

        public boolean isMoving() {

            if (!center.targetReached()) {
                return true;
            }

            for (Veaser corner : corners) {
                if (!corner.targetReached()) return true;
            }

            return false;

        }

    }

}
