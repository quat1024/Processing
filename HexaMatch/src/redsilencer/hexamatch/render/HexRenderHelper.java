package redsilencer.hexamatch.render;

import processing.core.PApplet;
import processing.core.PConstants;
import redsilencer.hexamatch.util.MathUtil;

public class HexRenderHelper implements PConstants {
	public static void drawHex(PApplet sketch, float radius) {
		sketch.beginShape(POLYGON);
		
		for(int corner = 0; corner < 6; corner++) {
			float angle = corner * TAU * 1/6f; 
			float x = MathUtil.cos(angle) * radius;
			float y = MathUtil.sin(angle) * radius;
			sketch.vertex(x, y);
		}
	}
}
