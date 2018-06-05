package redsilencer.hexamatch.render;

import processing.core.PApplet;
import redsilencer.hexamatch.jewel.Jewel;

public class JewelRenderer implements IShowable<Jewel> {
	int[] colors = new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00};
	
	public void show(PApplet sketch, Jewel jewel, float x, float y, float scale) {
		int jewelRenderColor = getColor(jewel);
		
		sketch.fill(jewelRenderColor);
		sketch.stroke(sketch.lerpColor(0xFF000000, jewelRenderColor, 0.3f));
		sketch.strokeWeight(.3f);
		
		sketch.translate(x, y);
		HexRenderHelper.drawHex(sketch, scale);
		sketch.translate(-x, -y);
	}
	
	int getColor(Jewel jewel) {
		if(jewel.getColor() >= colors.length) {
			//tbh jewels shouldn't have to care about how many colors exist in the renderer
			//todo this sucks
			throw new IllegalArgumentException("Invalid jewel color ID");
		} else return colors[jewel.getColor()];
	}
}
