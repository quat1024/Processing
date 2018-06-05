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
		size(1280, 720, P2D);
	}
	
	//TODO Factor this out of the main area into something like "Game"
	//the model
	JewelBoard board = null;
	
	//the view
	JewelBoardRenderer boardRenderer = null;
	
	@Override
	public void setup() {
		board = new JewelBoard();
		boardRenderer = new JewelBoardRenderer();
		
		//blah
		board.populate(4);
		
		smooth();
		
		frameRate(120);
	}
	
	@Override
	public void draw() {
		background(0xFF16161D);
		
		boardRenderer.show(this, board, width / 2, height / 2, 40);
	}
}
