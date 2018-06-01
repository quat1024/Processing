package hexamatch;

import hexamatch.animations.ComboAnimationSequence;
import hexamatch.animations.SpinningTripletSequence;
import hexamatch.engine.IFinalizer;
import hexamatch.engine.UpdateSystem;
import hexamatch.hexagons.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameBoard extends UpdateSystem {

    public final Settings settings;
    public final Random random;
    public final JewelBoard jewelBoard;
    public final TripletBoard tripletBoard;

    private boolean ignoreInput;

    public GameBoard(Settings gameSettings) {

        this.settings = gameSettings;
        this.random = new Random();
        this.jewelBoard = new JewelBoard(this);
        this.tripletBoard = new TripletBoard(this);

        for (HexCoord startingCoordinates : new RadialIterator(settings.boardSize)) {
            jewelBoard.addHex(new Jewel(startingCoordinates, jewelBoard));
        }

        jewelBoard.rebindFallLines();
        tripletBoard.rebindTriplets();

        JewelType[] jewelTypes = JewelType.values();
        List<Triplet> matches = tripletBoard.getMatchedTriplets();

        while (matches.size() > 0) {

            for (Triplet triplet : matches) {
                for (Jewel jewel : triplet.getJewels()) {

                    int currentIndex = jewel.jewelType.ordinal();
                    int randomOffset = (int) Math.ceil(random.nextDouble() * (jewelTypes.length - 1));
                    jewel.jewelType = jewelTypes[(currentIndex + randomOffset) % jewelTypes.length];

                }
            }

            matches = tripletBoard.getMatchedTriplets();

        }

    }

    public void selectAtPosition(double x, double y, MouseClick mouseClick) {

        if (!ignoreInput) {

            HexCoord tripletCoordinates = tripletBoard.renderer.positionToCoordinates(x, y);
            Triplet selectedTriplet = tripletBoard.getHex(tripletCoordinates);

            if (selectedTriplet != null) {

                ignoreInput = true;

                switch (mouseClick) {
                    case LEFT:
                        addToActive(new SpinningTripletSequence(selectedTriplet, -1, this), new TryCascade());
                        break;
                    case RIGHT:
                        addToActive(new SpinningTripletSequence(selectedTriplet, 1, this), new TryCascade());
                        break;
                }

            }

        }

    }

    private int[][] getBounds() {

        int minQ = Integer.MAX_VALUE;
        int minR = Integer.MAX_VALUE;
        int minS = Integer.MAX_VALUE;
        int maxQ = Integer.MIN_VALUE;
        int maxR = Integer.MIN_VALUE;
        int maxS = Integer.MIN_VALUE;

        for (Jewel jewel : jewelBoard.getAllHexagons()) {

            minQ = Math.min(jewel.coordinates.q, minQ);
            minR = Math.min(jewel.coordinates.r, minR);
            minS = Math.min(jewel.coordinates.s, minS);
            maxQ = Math.max(jewel.coordinates.q, maxQ);
            maxR = Math.max(jewel.coordinates.r, maxR);
            maxS = Math.max(jewel.coordinates.s, maxS);

        }

        int[] lower = {minQ, minR, minS};
        int[] upper = {maxQ, maxR, maxS};

        return new int[][]{lower, upper};

    }

    private List<Combo> getCombos() {

        List<Triplet> matchedTriplets = tripletBoard.getMatchedTriplets();
        List<Combo> allCombos = new ArrayList<>();

        if (matchedTriplets.size() > 0) {

            Queue<Jewel> jewelsInMatchedTriplets = new LinkedList<>(
                    matchedTriplets
                            .stream()
                            .flatMap(
                                    triplet -> triplet
                                            .getJewels()
                                            .stream()
                            )
                            .collect(
                                    Collectors.toSet()
                            )
            );

            Set<HexCoord> visited = new HashSet<>();

            while (jewelsInMatchedTriplets.size() > 0) {

                Jewel current = jewelsInMatchedTriplets.remove();

                if (!visited.contains(current.coordinates)) {

                    visited.add(current.coordinates);

                    JewelType referenceType = current.jewelType;

                    List<Jewel> connectedJewels = new ArrayList<>();
                    connectedJewels.add(current);

                    Stack<Jewel> pending = new Stack<>();
                    pending.push(current);

                    while (pending.size() > 0) {

                        Jewel next = pending.pop();

                        for (Jewel neighbor : next.neighbors) {

                            if (neighbor != null && jewelsInMatchedTriplets.contains(neighbor)) {
                                if (neighbor.jewelType == referenceType && !visited.contains(neighbor.coordinates)) {

                                    visited.add(neighbor.coordinates);
                                    pending.push(neighbor);
                                    connectedJewels.add(neighbor);

                                }
                            }

                        }

                    }

                    if (connectedJewels.size() > 0) {
                        allCombos.add(new Combo(connectedJewels));
                    }

                }

            }

        }

        return allCombos;

    }

    public static class Settings {

        public final int boardSize;
        public final HexDirection fallDirection;
        public final HexBoard.Settings jewelBoardSettings;
        public final HexBoard.Settings tripletBoardSettings;

        public Settings(HexDirection fallDirection, HexType hexType, double hexRadius, double hexPadding, int boardSize) {

            this.boardSize = boardSize;
            this.fallDirection = fallDirection;
            this.jewelBoardSettings = new HexBoard.Settings(hexType, hexRadius, hexPadding);
            this.tripletBoardSettings = new HexBoard.Settings(
                    jewelBoardSettings.hexType.opposite(),
                    jewelBoardSettings.hexRadius * Math.sqrt(3d) / 3d,
                    jewelBoardSettings.hexPadding - (jewelBoardSettings.hexPadding / 5d)
            );

        }

    }

    private class TryCascade implements IFinalizer {

        //  check for combos
        //  if there are combos
        //      remove the jewels in the combos from the game board
        //      slide existing jewels to the end of the fall lines
        //      add new jewels to the top of the fall lines
        //      start a combo animation sequence
        //      start a falling jewel animation sequence
        //      repeat

        @Override
        public void finish() {

            List<Combo> allCombos = getCombos();

            if (allCombos.size() > 0) {

                addToActive(new ComboAnimationSequence(allCombos), new PostCascade());

            } else {

                ignoreInput = false;

            }

        }

    }

    private class PostCascade implements IFinalizer {

        @Override
        public void finish() {
            ignoreInput = false;
        }

    }

}
