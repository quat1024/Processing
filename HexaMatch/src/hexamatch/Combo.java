package hexamatch;

import java.util.List;

public class Combo {

    public List<Jewel> connectedJewels;

    private boolean isPopped;

    public Combo(List<Jewel> connectedJewels) {

        this.connectedJewels = connectedJewels;

    }

    public boolean tryPop() {

        if (isPopped) return false;

        for (Jewel jewel : connectedJewels) {
            if (jewel.renderer.isMoving()) return false;
        }

        return isPopped = true;

    }

}
