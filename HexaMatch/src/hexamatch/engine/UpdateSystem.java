package hexamatch.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class UpdateSystem extends UpdateProcess {

    private List<UpdateProcess> pendingIn;
    private List<UpdateProcess> active;
    private List<UpdateProcess> pendingOut;

    public UpdateSystem() {

        this.pendingIn = new ArrayList<>();
        this.active = new ArrayList<>();
        this.pendingOut = new ArrayList<>();

    }

    @Override
    public void setup() {

        pendingIn.clear();
        active.clear();
        pendingOut.clear();

    }

    @Override
    public void update() {

        for (UpdateProcess in : pendingIn) {
            in.setup();
            active.add(in);
        }
        pendingIn.clear();

        active.forEach(
                current -> {
                    current.update();
                    if (current.isDone()) {
                        pendingOut.add(current);
                    }
                }
        );

        for (UpdateProcess out : pendingOut) {
            out.finish();
            active.remove(out);
        }
        pendingOut.clear();

    }

    @Override
    public boolean isDone() {

        return active.size() <= 0;

    }

    @Override
    public void finish() {

    }

    public void addToActive(IProcess process, IFinalizer finalizer) {

        pendingIn.add(new ProcessFinalizerCouple(process, finalizer));

    }

    public void addToActive(UpdateProcess updateProcess) {

        pendingIn.add(updateProcess);

    }

    private class ProcessFinalizerCouple extends UpdateProcess {

        private IProcess process;
        private IFinalizer finalizer;

        public ProcessFinalizerCouple(IProcess process, IFinalizer finalizer) {

            this.process = process;
            this.finalizer = finalizer;

        }

        @Override
        public void setup() {
            process.setup();
        }

        @Override
        public void update() {
            process.update();
        }

        @Override
        public boolean isDone() {
            return process.isDone();
        }

        @Override
        public void finish() {
            finalizer.finish();
        }

    }

}
