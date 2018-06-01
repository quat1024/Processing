package hexamatch.animations;

import hexamatch.Combo;
import hexamatch.Jewel;
import hexamatch.Veaser;
import hexamatch.Vector;
import hexamatch.engine.UpdateProcess;
import hexamatch.engine.UpdateSystem;

import java.util.List;

public class ComboAnimationSequence extends UpdateSystem {

    private List<Combo> pendingCombos;
    private int totalCombosInSequence;
    private int completedCombosPopped;

    public ComboAnimationSequence(List<Combo> pendingCombos) {

        this.pendingCombos = pendingCombos;
        this.totalCombosInSequence = pendingCombos.size();
        this.completedCombosPopped = 0;

    }

    @Override
    public void update() {

        super.update();

        pendingCombos
                .stream()
                .filter(Combo::tryPop)
                .forEach(combo -> addToActive(new ComboPopAnimation(combo)));

    }

    @Override
    public boolean isDone() {

        return (completedCombosPopped == totalCombosInSequence);

    }

    public class ComboPopAnimation extends UpdateSystem {

        private int completedJewelPops;
        private Combo combo;

        public ComboPopAnimation(Combo combo) {

            this.combo = combo;

        }

        @Override
        public void setup() {

            this.completedJewelPops = 0;

            for (Jewel shrinkingJewel : combo.connectedJewels) {
                addToActive(new JewelPoppingAnimation(shrinkingJewel));
            }

        }


        @Override
        public boolean isDone() {

            return completedJewelPops == combo.connectedJewels.size();

        }

        @Override
        public void finish() {

            completedCombosPopped++;

        }

        private class JewelPoppingAnimation extends UpdateProcess {

            private Jewel shrinkingJewel;

            public JewelPoppingAnimation(Jewel shrinkingJewel) {

                this.shrinkingJewel = shrinkingJewel;

            }

            @Override
            public void setup() {

                Vector center = shrinkingJewel.renderer.center.getVector();
                for (Veaser corner : shrinkingJewel.renderer.corners) {
                    corner.setTarget(center);
                    corner.setExp(shrinkingJewel.parent.settings.hexRadius / 250f);
                    corner.setThreshold(shrinkingJewel.parent.settings.hexRadius / 25f);
                }

            }

            @Override
            public void update() {

                for (Veaser corner : shrinkingJewel.renderer.corners) {
                    if (!corner.targetReached()) {
                        corner.update();
                    }
                }

            }

            @Override
            public boolean isDone() {

                int cornersReachedTargetCount = 0;

                for (Veaser corner : shrinkingJewel.renderer.corners) {
                    if (corner.targetReached()) {
                        cornersReachedTargetCount++;
                    }
                }

                boolean done = (cornersReachedTargetCount == shrinkingJewel.renderer.corners.length);

                if (done) shrinkingJewel.renderer.resetCorners();

                return done;

            }

            @Override
            public void finish() {

                completedJewelPops++;

            }

        }

    }

}
