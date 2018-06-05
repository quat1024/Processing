package redsilencer.hexamatch.render;

import processing.core.PApplet;
import redsilencer.hexamatch.jewel.Jewel;

public class JewelRenderer implements IShowable<Jewel> {
	int[] colors = new int[]{0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00};
	
	public void show(PApplet sketch, Jewel jewel) {
		//draw a hexagon
	}
	
	int getColor(Jewel jewel) {
		if(jewel.getColor() >= colors.length) {
			//tbh jewels shouldn't have to care about how many colors exist in the renderer
			//todo this sucks
			throw new IllegalArgumentException("Invalid jewel color ID");
		} else return colors[jewel.getColor()];
	}
}
