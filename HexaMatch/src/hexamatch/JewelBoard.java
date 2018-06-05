package hexamatch;

import hexamatch.hexagons.HexBoard;
import hexamatch.hexagons.HexCoord;
import hexamatch.hexagons.HexDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JewelBoard extends HexBoard<Jewel> {

    public HexDirection fallDirection;
    public final HashMap<Integer, List<HexCoord>> fallLines;

    public JewelBoard(GameBoard gameBoard) {

        super(gameBoard.settings.jewelBoardSettings);

        this.fallDirection = gameBoard.settings.fallDirection;
        this.fallLines = new HashMap<>();

    }

    public void rebindFallLines() {

        fallLines.clear();

        int directionZeroIndex =
                (fallDirection.q == 0) ? 0 :
                        (fallDirection.r == 0) ? 1 :
                                (fallDirection.s == 0) ? 2 : -1;

        for (Jewel jewel : getAllHexagons()) {

            int fallLineIndex = jewel.coordinates.get(directionZeroIndex);

            if (!fallLines.containsKey(fallLineIndex)) {
                fallLines.put(fallLineIndex, new ArrayList<>());
            }

            fallLines.get(fallLineIndex).add(jewel.coordinates);

        }

        int sortAxisIndex =
                (fallDirection.q == 1) ? 0 :
                        (fallDirection.r == 1) ? 1 :
                                (fallDirection.s == 1) ? 2 : -1;

        for (List<HexCoord> line : fallLines.values()) {
            line.sort(
                    (coordA, coordB) -> // Ascending order
                            coordA.get(sortAxisIndex) > coordB.get(sortAxisIndex) ? 1 : -1
            );
        }

        /*
        System.out.println();
        System.out.println(String.format("Fall Direction  = %s", fallDirection.toString()));
        System.out.println(String.format("Fall Axis Index = %d", directionZeroIndex));
        System.out.println(String.format("Sort Axis Index = %d", sortAxisIndex));
        System.out.println();
        for (Map.Entry<Integer, List<HexCoord>> entry : fallLines.entrySet()) {
            System.out.println(String.format("Line %d, Length %d", entry.getKey(), entry.getValue().size()));
            for (HexCoord coordinates : entry.getValue()) {
                System.out.println("\t" + coordinates.toString());
            }
        }
        */

    }

}
