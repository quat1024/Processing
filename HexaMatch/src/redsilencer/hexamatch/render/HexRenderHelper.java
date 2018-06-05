package redsilencer.hexamatch.render;

import processing.core.*;
import redsilencer.hexamatch.hex.Hex;
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
		
		sketch.endShape(CLOSE);
	}
	
	private static final float SQRT_3 = (float) Math.sqrt(3);
	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3) / 2);
	
	public static PVector vectorFromHexCoord(Hex hex) {
		return vectorFromHexCoord(hex.x, hex.y);
	}
	
	public static PVector vectorFromHexCoord(int x, int y) {
		//TODO this is probably flipped or rotated somehow
		float screenX = 1.5f * y;
		float screenY = (SQRT_3 * x + HALF_SQRT_3 * y);
		
		return new PVector(screenX, screenY);
	}
}
