package hexamatch.hexagons;

public enum HexType {

    POINTY, FLAT;

    public HexType opposite() {

        switch (this) {
            case POINTY:
                return FLAT;
            case FLAT:
                return POINTY;
            default:
                return null;
        }

    }

}
