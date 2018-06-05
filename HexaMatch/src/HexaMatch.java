import hexamatch.*;
import hexamatch.hexagons.HexDirection;
import hexamatch.hexagons.HexType;
import hexamatch.hexagons.Hexagon;
import processing.core.PApplet;

public class HexaMatch extends PApplet {

    private static final GameBoard hexicGameBoard =
            new GameBoard(
                    new GameBoard.Settings(
                            HexDirection.Dir5, HexType.FLAT, 50f, 0.9f, 4
                    )
            );

    public static void main(String[] args) {
        PApplet.main("HexaMatch", args);
    }

    @Override
    public void settings() {
        size(1400, 900);
    }

    @Override
    public void draw() {

        hexicGameBoard.update();

        translate(width / 2f, height / 2f);
        background(0);
        noStroke();

        for (Jewel jewel : hexicGameBoard.jewelBoard.getAllHexagons()) {

            fill(getColor(jewel.jewelType));
            drawHex(jewel);

            fill(50, 50);
            ellipse(
                    (float) jewel.renderer.center.getX(),
                    (float) jewel.renderer.center.getY(),
                    (float) hexicGameBoard.jewelBoard.settings.hexRadius / 2f,
                    (float) hexicGameBoard.jewelBoard.settings.hexRadius / 2f
            );

            /*
            fill(255);
            textAlign(CENTER, CENTER);
            textSize((float) hexicGameBoard.settings.jewelBoardSettings.hexRadius / 3f);
            text(jewel.coordinates.toString(), (float) jewel.renderer.center.getX(), (float) jewel.renderer.center.getY());
            */

        }

        fill(50, 50);
        for (Triplet triplet : hexicGameBoard.tripletBoard.getAllHexagons()) {
            drawHex(triplet);
        }

    }

    @Override
    public void mousePressed() {

        double x = mouseX - width / 2d;
        double y = mouseY - height / 2d;

        switch (mouseButton) {
            case LEFT:
                hexicGameBoard.selectAtPosition(x, y, MouseClick.LEFT);
                break;
            case RIGHT:
                hexicGameBoard.selectAtPosition(x, y, MouseClick.RIGHT);
                break;
        }

    }

    private int getColor(JewelType jewelType) {
        switch (jewelType) {
            case P:
                return color(126, 107, 143);
            case R:
                return color(218, 62, 82);
            case Y:
                return color(242, 233, 78);
            case G:
                return color(50, 230, 179);
            case B:
                return color(163, 217, 255);
            case O:
                return color(250, 163, 129);
            default:
                return color(255, 255, 255);
        }
    }

    private void drawHex(Hexagon hexagon) {

        beginShape();
        for (Veaser corner : hexagon.renderer.corners) {
            vertex(
                    (float) corner.getX(),
                    (float) corner.getY()
            );
        }
        endShape(CLOSE);

    }

}
