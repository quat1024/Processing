package hexamatch.animations;

import hexamatch.Jewel;
import hexamatch.Veaser;
import hexamatch.Vector;
import hexamatch.engine.UpdateSystem;

import java.util.List;

public class FallingJewelSequence extends UpdateSystem {

    private List<Jewel> fallingJewels;

    private boolean done;

    public FallingJewelSequence(List<Jewel> fallingJewels) {

        this.fallingJewels = fallingJewels;

    }

    @Override
    public void setup() {

        done = false;

        for (Jewel jewel : fallingJewels) {

            Vector targetCenter = jewel.parent.renderer.coordinatesToPosition(jewel.coordinates);

            jewel.renderer.center.setTarget(targetCenter);
            jewel.renderer.center.setExp(0.25f);

            for (int c = 0; c < jewel.parent.renderer.baseCorners.length; c++) {

                Vector baseCorner = jewel.parent.renderer.baseCorners[c];
                Veaser corner = jewel.renderer.corners[c];
                corner.setTarget(
                        baseCorner.x + targetCenter.x,
                        baseCorner.y + targetCenter.y
                );
                corner.setExp(0.25f);

            }

        }

    }

    @Override
    public void update() {

        super.update();

        int jewelsDoneMoving = 0;

        for (Jewel jewel : fallingJewels) {

            boolean centerReached = false;

            jewel.renderer.center.update();
            if (jewel.renderer.center.targetReached()) {
                jewel.renderer.center.snapToTarget();
                centerReached = true;
            }

            int cornersReachedCount = 0;

            for (Veaser corner : jewel.renderer.corners) {

                corner.update();
                if (corner.targetReached()) {
                    corner.snapToTarget();
                    cornersReachedCount++;
                }

            }

            if (cornersReachedCount == jewel.renderer.corners.length && centerReached) {
                jewelsDoneMoving++;
            }

        }

        if (jewelsDoneMoving == fallingJewels.size()) {
            done = true;
        }

    }

    @Override
    public boolean isDone() {

        return done;

    }

}
