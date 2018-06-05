package hexamatch;

import hexamatch.hexagons.HexCoord;
import hexamatch.hexagons.Hexagon;

public class Jewel extends Hexagon<Jewel> {

    public JewelType jewelType;

    public Jewel(HexCoord coordinates, JewelBoard jewelBoard, JewelType jewelType) {

        super(coordinates, jewelBoard);

        this.jewelType = jewelType;

    }

}