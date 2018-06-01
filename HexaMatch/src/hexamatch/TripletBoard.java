package hexamatch;

import hexamatch.hexagons.HexBoard;
import hexamatch.hexagons.HexDirection;
import hexamatch.hexagons.Hexagon;

import java.util.List;
import java.util.stream.Collectors;

public class TripletBoard extends HexBoard<Triplet> {

    public final GameBoard gameBoard;

    public TripletBoard(GameBoard gameBoard) {

        super(gameBoard.settings.tripletBoardSettings);

        this.gameBoard = gameBoard;

    }

    public List<Triplet> getMatchedTriplets() {

        return getAllHexagons()
                .stream()
                .filter(Triplet::isMatch)
                .collect(Collectors.toList());

    }

    public void rebindTriplets() {

        int fallDirectionIndex = gameBoard.jewelBoard.fallDirection.ordinal();
        int negDirectionIndex = (fallDirectionIndex - 1) % HexDirection.length;
        int posDirectionIndex = (fallDirectionIndex + 1) % HexDirection.length;

        negDirectionIndex = negDirectionIndex < 0 ? negDirectionIndex + HexDirection.length : negDirectionIndex;
        posDirectionIndex = posDirectionIndex < 0 ? posDirectionIndex + HexDirection.length : posDirectionIndex;

        for (Jewel currentJewel : gameBoard.jewelBoard.getAllHexagons()) {

            Hexagon fallNeighbor = currentJewel.neighbors.get(fallDirectionIndex);

            if (fallNeighbor != null) {

                Hexagon negNeighbor = currentJewel.neighbors.get(negDirectionIndex);
                Hexagon posNeighbor = currentJewel.neighbors.get(posDirectionIndex);

                if (negNeighbor != null) {

                    Triplet triplet = new Triplet(
                            currentJewel.coordinates,
                            fallNeighbor.coordinates,
                            negNeighbor.coordinates,
                            this
                    );
                    addHex(triplet);

                }

                if (posNeighbor != null) {

                    Triplet triplet = new Triplet(
                            fallNeighbor.coordinates,
                            currentJewel.coordinates,
                            posNeighbor.coordinates,
                            this
                    );
                    addHex(triplet);

                }

            }

        }

    }

}
