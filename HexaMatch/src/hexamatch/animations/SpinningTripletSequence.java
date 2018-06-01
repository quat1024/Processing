package hexamatch.animations;

import hexamatch.GameBoard;
import hexamatch.Jewel;
import hexamatch.Triplet;
import hexamatch.Veaser;
import hexamatch.engine.UpdateProcess;
import hexamatch.engine.UpdateSystem;
import hexamatch.hexagons.HexCoord;
import hexamatch.hexagons.RadialIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpinningTripletSequence extends UpdateSystem {

    private Triplet spinningTriplet;
    private int direction;
    private GameBoard gameBoard;

    private int completedTurns;
    private boolean sequenceComplete;
    private List<Triplet> affectedNeighborhood;
    private List<Jewel> spinningJewels;

    public SpinningTripletSequence(Triplet spinningTriplet, int direction, GameBoard gameBoard) {

        this.spinningTriplet = spinningTriplet;
        this.direction = direction;
        this.gameBoard = gameBoard;

    }

    @Override
    public void setup() {

        this.completedTurns = 0;
        this.sequenceComplete = false;
        this.affectedNeighborhood = new ArrayList<>();
        this.spinningJewels = spinningTriplet.getJewels();

        for (HexCoord nearbyCoordinates : new RadialIterator(spinningTriplet.coordinates, 2)) {
            Triplet nearbyTriplet = gameBoard.tripletBoard.getHex(nearbyCoordinates);
            if (nearbyTriplet != null) {
                affectedNeighborhood.add(nearbyTriplet);
            }
        }

        addToActive(new Rotator());

    }

    @Override
    public boolean isDone() {

        return sequenceComplete;

    }

    private class Rotator extends UpdateProcess {

        private double currentAngle;
        private double targetAngle;
        private double angleStep;
        private int waitFrames;
        private boolean done;

        @Override
        public void setup() {

            this.currentAngle = 0d;
            this.targetAngle = Math.toRadians(120d) * direction;
            this.angleStep = Math.toRadians(5d) * Math.signum(direction);
            this.waitFrames = 2;
            this.done = false;

        }

        @Override
        public void update() {

            if (waitFrames > 0) {

                waitFrames--;

            } else if (!done) {

                if (Math.abs(currentAngle) < Math.abs(targetAngle - angleStep)) {

                    double cos = Math.cos(angleStep);
                    double sin = Math.sin(angleStep);

                    double tripletCenterX = spinningTriplet.renderer.center.getX();
                    double tripletCenterY = spinningTriplet.renderer.center.getY();

                    for (int c = 0; c < spinningTriplet.renderer.corners.length; c++) {

                        Veaser corner = spinningTriplet.renderer.corners[c];

                        double cornerX = corner.getX();
                        double cornerY = corner.getY();

                        corner.snapToTarget(
                                (cornerX - tripletCenterX) * cos - (cornerY - tripletCenterY) * sin + tripletCenterX,
                                (cornerY - tripletCenterY) * cos + (cornerX - tripletCenterX) * sin + tripletCenterY
                        );

                    }

                    for (Jewel jewel : spinningJewels) {

                        Veaser center = jewel.renderer.center;

                        double adjustedCenterX = center.getX() - tripletCenterX;
                        double adjustedCenterY = center.getY() - tripletCenterY;

                        center.snapToTarget(
                                adjustedCenterX * cos - adjustedCenterY * sin + tripletCenterX,
                                adjustedCenterY * cos + adjustedCenterX * sin + tripletCenterY
                        );

                        for (int c = 0; c < jewel.renderer.corners.length; c++) {

                            Veaser corner = jewel.renderer.corners[c];

                            double adjustedCornerX = corner.getX() - tripletCenterX;
                            double adjustedCornerY = corner.getY() - tripletCenterY;

                            corner.snapToTarget(
                                    adjustedCornerX * cos - adjustedCornerY * sin + tripletCenterX,
                                    adjustedCornerY * cos + adjustedCornerX * sin + tripletCenterY
                            );

                        }

                    }

                    currentAngle += angleStep;

                } else {

                    done = true;

                }

            }

        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public void finish() {

            List<HexCoord> spinningJewelCoordinates =
                    spinningJewels
                            .stream()
                            .map(w -> w.coordinates)
                            .collect(Collectors.toList());
            Collections.rotate(spinningJewelCoordinates, -direction);

            spinningJewels.forEach(gameBoard.jewelBoard::removeHex);
            for (int c = 0; c < 3; c++) {
                spinningJewels.get(c).coordinates = spinningJewelCoordinates.get(c);
            }
            spinningJewels.forEach(gameBoard.jewelBoard::addHex);

            for (Jewel jewel : spinningJewels) {
                jewel.renderer.resetCenter();
                jewel.renderer.resetCorners();
            }

            spinningTriplet.renderer.resetCenter();
            spinningTriplet.renderer.resetCorners();

            boolean foundMatch =
                    affectedNeighborhood
                            .stream()
                            .anyMatch(Triplet::isMatch);

            if (++completedTurns < 3 && !foundMatch) {
                addToActive(this);
            } else {
                sequenceComplete = true;
            }

        }

    }

}
