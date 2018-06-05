package hexamatch;

import hexamatch.animations.ComboAnimationSequence;
import hexamatch.animations.FallingJewelSequence;
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
            jewelBoard.addHex(new Jewel(startingCoordinates, jewelBoard, getRandomJewelType()));
        }

        jewelBoard.rebindFallLines();
        tripletBoard.rebindTriplets();

        JewelType[] jewelTypes = JewelType.values();
        List<Triplet> matches = tripletBoard.getMatchedTriplets();

        while (matches.size() > 0) {

            for (Triplet triplet : matches) {

                List<Jewel> jewels = triplet.getJewels();

                Jewel chosenJewel = jewels.get(
                        (int) Math.floor(random.nextDouble() * jewels.size())
                );

                int currentIndex = chosenJewel.jewelType.ordinal();
                int randomOffset = (int) Math.ceil(random.nextDouble() * (jewelTypes.length - 1));
                chosenJewel.jewelType = jewelTypes[(currentIndex + randomOffset) % jewelTypes.length];

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
                        addToActive(new SpinningTripletSequence(selectedTriplet, -1, this), new ComboPop());
                        break;
                    case RIGHT:
                        addToActive(new SpinningTripletSequence(selectedTriplet, 1, this), new ComboPop());
                        break;
                }

            }

        }

    }

    private JewelType getRandomJewelType() {
        return JewelType.values()[(int) Math.floor(random.nextDouble() * JewelType.length)];
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

    private class ComboPop implements IFinalizer {

        @Override
        public void finish() {

            jewelBoard.fallDirection = HexDirection.values()[(jewelBoard.fallDirection.ordinal() + 1) % HexDirection.length];
            jewelBoard.rebindFallLines();

            List<Combo> allCombos = getCombos();

            if (allCombos.size() > 0) {
                addToActive(new ComboAnimationSequence(allCombos), new PostComboPop(allCombos));
            } else {
                ignoreInput = false;
            }

        }

    }

    private class PostComboPop implements IFinalizer {

        private List<Combo> comboList;

        public PostComboPop(List<Combo> comboList) {

            this.comboList = comboList;

        }

        @Override
        public void finish() {

            if (comboList.size() > 0) {

                List<Jewel> deadJewels =
                        comboList
                                .stream()
                                .flatMap(
                                        combo -> combo.connectedJewels.stream()
                                )
                                .collect(Collectors.toList());

                deadJewels.forEach(jewelBoard::removeHex);

                List<Jewel> fallingJewels = new ArrayList<>();

                for (List<HexCoord> fallLine : jewelBoard.fallLines.values()) {

                    List<Jewel> jewelsInLine =
                            fallLine
                                    .stream()
                                    .map(jewelBoard::getHex)
                                    .collect(Collectors.toList());

                    for (Jewel jewel : jewelsInLine) {
                        if (jewel != null) {
                            jewelBoard.removeHex(jewel);
                        }
                    }

                    for (int i = fallLine.size() - 1; i >= 0; i--) {

                        Jewel current = jewelsInLine.get(i);

                        if (current != null) {

                            boolean moved = false;

                            for (int currentI = i; currentI < fallLine.size() - 1; currentI++) {

                                int nextI = currentI + 1;
                                if (jewelsInLine.get(nextI) == null) {

                                    current.coordinates = fallLine.get(nextI);

                                    moved = true;

                                    jewelsInLine.set(nextI, current);
                                    jewelsInLine.set(currentI, null);

                                } else break;

                            }

                            if (moved) {
                                fallingJewels.add(current);
                            }

                        }

                    }

                    for (int i = 0; i < jewelsInLine.size(); i++) {

                        Jewel jewel = jewelsInLine.get(i);

                        if (jewel == null) {

                            jewel = new Jewel(fallLine.get(i), jewelBoard, getRandomJewelType());

                            fallingJewels.add(jewel);

                            jewel.renderer.center.snapToTarget(
                                    jewel.parent.renderer.coordinatesToPosition(
                                            new HexCoord(
                                                    jewel.coordinates.q + (-jewelBoard.fallDirection.q * fallLine.size()),
                                                    jewel.coordinates.r + (-jewelBoard.fallDirection.r * fallLine.size())
                                            )
                                    )
                            );

                            for (int c = 0; c < jewel.parent.renderer.baseCorners.length; c++) {
                                jewel.renderer.corners[c].snapToTarget(
                                        jewel.parent.renderer.baseCorners[c].x + jewel.renderer.center.getX(),
                                        jewel.parent.renderer.baseCorners[c].y + jewel.renderer.center.getY()
                                );
                            }

                        }

                        jewelBoard.addHex(jewel);

                    }

                }

                addToActive(new FallingJewelSequence(fallingJewels), new ComboPop());

            } else {

                ignoreInput = false;

            }

        }

    }

}
