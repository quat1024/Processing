package redsilencer.hexamatch.render;

import processing.core.*;
import redsilencer.hexamatch.jewel.Jewel;
import redsilencer.hexamatch.jewel.JewelBoard;

public class JewelBoardRenderer implements IShowable<JewelBoard> {
	JewelRenderer jewelRenderer;
	float hexSize = 1;
	float hexSpacing = .3f;
	
	public JewelBoardRenderer() {
		this.jewelRenderer = new JewelRenderer();
	}
	
	public void setHexSize(float hexSize) {
		this.hexSize = hexSize;
	}
	
	public void setHexSpacing(float hexSpacing) {
		this.hexSpacing = hexSpacing;
	}
	
	@Override
	public void show(PApplet sketch, JewelBoard board, float x, float y, float scale) {
		sketch.pushMatrix();
		sketch.translate(x, y);
		sketch.scale(scale);
		
		for(Jewel jewel : board.getAllJewels()) {
			//find out where to draw
			PVector hexOffset = HexRenderHelper.vectorFromHexCoord(jewel.getHex());
			hexOffset.mult(hexSize + hexSpacing);
			
			//and draw it there
			jewelRenderer.show(sketch, jewel, hexOffset.x, hexOffset.y, hexSize);
		}
		
		sketch.popMatrix();
	}
}
