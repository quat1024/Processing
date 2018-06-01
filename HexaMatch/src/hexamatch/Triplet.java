package hexamatch;

import hexamatch.hexagons.HexCoord;
import hexamatch.hexagons.HexType;
import hexamatch.hexagons.Hexagon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Triplet extends Hexagon<Triplet> {

    public final TripletBoard tripletBoard;
    private final HexCoord[] jewelCoordinates;

    public Triplet(HexCoord a, HexCoord b, HexCoord c, TripletBoard tripletBoard) {

        super(convertToTripletCoordinates(a, b, c, tripletBoard.settings.hexType), tripletBoard);

        this.tripletBoard = tripletBoard;
        this.jewelCoordinates = new HexCoord[]{a, b, c};

    }

    private static HexCoord convertToTripletCoordinates(HexCoord a, HexCoord b, HexCoord c, HexType hexType) {

        int rotationDirection = 0;

        switch (hexType) {
            case POINTY:
                rotationDirection = 1;
                break;
            case FLAT:
                rotationDirection = -1;
                break;
        }

        HexCoord offsetA = HexCoord.rotate(new HexCoord(a.q * 2, a.r * 2), a, rotationDirection);
        HexCoord offsetB = HexCoord.rotate(new HexCoord(b.q * 2, b.r * 2), b, rotationDirection);
        HexCoord offsetC = HexCoord.rotate(new HexCoord(c.q * 2, c.r * 2), c, rotationDirection);

        return new HexCoord(
                (offsetA.q + offsetB.q + offsetC.q) / 3,
                (offsetA.r + offsetB.r + offsetC.r) / 3
        );

    }

    public boolean isMatch() {

        List<Jewel> jewels = getJewels();

        if (jewels.contains(null)) {
            return false;
        }

        JewelType a = jewels.get(0).jewelType;
        JewelType b = jewels.get(1).jewelType;
        JewelType c = jewels.get(2).jewelType;

        return a == b && b == c;

    }

    public List<Jewel> getJewels() {

        return Arrays
                .stream(jewelCoordinates)
                .map(tripletBoard.gameBoard.jewelBoard::getHex)
                .collect(
                        Collectors.toList()
                );

    }

}
