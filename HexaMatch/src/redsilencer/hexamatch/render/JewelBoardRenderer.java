package redsilencer.hexamatch.render;

import processing.core.PApplet;
import redsilencer.hexamatch.jewel.Jewel;
import redsilencer.hexamatch.jewel.JewelBoard;

public class JewelBoardRenderer implements IShowable<JewelBoard> {
	JewelRenderer jewelRenderer;
	
	public JewelBoardRenderer(JewelRenderer jewelRenderer) {
		this.jewelRenderer = jewelRenderer;
	}
	
	@Override
	public void show(PApplet sketch, JewelBoard board) {
		for(Jewel jewel : board.getAllJewels()) {
			jewelRenderer.show(sketch, jewel);
		}
	}
}
