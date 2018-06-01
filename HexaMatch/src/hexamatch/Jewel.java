package hexamatch;

import hexamatch.hexagons.HexCoord;
import hexamatch.hexagons.Hexagon;

public class Jewel extends Hexagon<Jewel> {

    public JewelType jewelType;

    public Jewel(HexCoord coordinates, JewelBoard jewelBoard) {

        super(coordinates, jewelBoard);

        this.jewelType = JewelType.P;

    }

}