package hexamatch.hexagons;

import hexamatch.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class HexBoard<H extends Hexagon<H>> {

    public final Settings settings;
    public final HexGridRenderer renderer;

    private final HashMap<HexCoord, H> grid;

    public HexBoard(Settings settings) {

        this.settings = settings;
        this.renderer = new HexGridRenderer();

        this.grid = new HashMap<>();

    }

    public H addHex(H hexagon) {

        if (!grid.containsKey(hexagon.coordinates)) {

            H hex = grid.put(hexagon.coordinates, hexagon);

            hexagon.updateNeighbors();
            for (H neighbor : hexagon.neighbors) {
                if (neighbor != null) neighbor.updateNeighbors();
            }

            return hex;

        }

        return null;

    }

    public H removeHex(H hexagon) {

        if (grid.containsKey(hexagon.coordinates)) {

            H hex = grid.remove(hexagon.coordinates);

            hexagon.updateNeighbors();
            for (H neighbor : hexagon.neighbors) {
                if (neighbor != null) neighbor.updateNeighbors();
            }

            return hex;

        }

        return null;

    }

    public H getHex(HexCoord coordinates) {

        return grid.get(coordinates);

    }

    public List<H> getAllHexagons() {
        return new ArrayList<>(grid.values());
    }

    public static class Settings {

        public final HexType hexType;
        public final double hexRadius;
        public final double hexPadding;

        public Settings(HexType hexType, double hexRadius, double hexPadding) {

            this.hexType = hexType;
            this.hexRadius = hexRadius;
            this.hexPadding = hexPadding;

        }

    }

    public class HexGridRenderer {

        public final Vector hexSize;
        public final Vector[] baseCorners;

        public HexGridRenderer() {

            this.hexSize = getHexSizeByRadius(settings.hexType, settings.hexRadius);
            this.baseCorners = new Vector[6];

            for (int c = 0; c < 6; c++) {
                double angle = Math.toRadians(60d * c);
                if (settings.hexType == HexType.POINTY) angle += Math.toRadians(30d);
                baseCorners[c] = new Vector(
                        settings.hexRadius * settings.hexPadding * Math.cos(angle),
                        settings.hexRadius * settings.hexPadding * Math.sin(angle)
                );
            }

        }

        public Vector coordinatesToPosition(HexCoord coordinates) {

            return coordinatesToPosition(coordinates.q, coordinates.r);

        }

        public Vector coordinatesToPosition(int q, int r) {

            switch (settings.hexType) {
                case POINTY:
                    return new Vector(
                            settings.hexRadius * Math.sqrt(3d) * (q + r / 2d),
                            settings.hexRadius * 3d / 2d * r
                    );
                case FLAT:
                    return new Vector(
                            settings.hexRadius * 3d / 2d * q,
                            settings.hexRadius * Math.sqrt(3d) * (r + q / 2d)
                    );
                default:
                    return null;
            }

        }

        public HexCoord positionToCoordinates(Vector position) {

            return positionToCoordinates(position.x, position.y);

        }

        public HexCoord positionToCoordinates(double x, double y) {

            switch (settings.hexType) {
                case POINTY:
                    return HexCoord.round(
                            (x * Math.sqrt(3d) / 3d - y / 3d) / settings.hexRadius,
                            y * 2d / 3d / settings.hexRadius
                    );
                case FLAT:
                    return HexCoord.round(
                            x * 2d / 3d / settings.hexRadius,
                            (y * Math.sqrt(3d) / 3d - x / 3d) / settings.hexRadius
                    );
                default:
                    return null;
            }

        }

        public Vector getHexSizeByRadius(HexType hexType, double hexRadius) {

            switch (hexType) {
                case POINTY:
                    return new Vector(
                            hexRadius * 2d * (Math.sqrt(3d) / 2d),
                            hexRadius * 2d
                    );
                case FLAT:
                    return new Vector(
                            hexRadius * 2d,
                            hexRadius * 2d * (Math.sqrt(3d) / 2d)
                    );
                default:
                    return null;
            }

        }

    }

}
