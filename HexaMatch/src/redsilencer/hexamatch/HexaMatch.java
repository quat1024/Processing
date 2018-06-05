package redsilencer.hexamatch;

import processing.core.PApplet;
import redsilencer.hexamatch.jewel.JewelBoard;
import redsilencer.hexamatch.render.JewelBoardRenderer;
import redsilencer.hexamatch.render.JewelRenderer;

public class HexaMatch extends PApplet {
	public static void main(String[] args) {
		PApplet.main("redsilencer.hexamatch.HexaMatch", args);
	}
	
	@Override
	public void settings() {
		size(1280, 720);
	}
	
	//TODO Factor this out of the main area into something like "Game"
	//the model
	JewelBoard board = null;
	
	//the view
	JewelBoardRenderer boardRenderer = null;
	
	@Override
	public void setup() {
		board = new JewelBoard();
		boardRenderer = new JewelBoardRenderer(new JewelRenderer());
	}
	
	@Override
	public void draw() {
		boardRenderer.show(this, board);
	}
}
