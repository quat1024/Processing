package redsilencer.hexamatch.jewel;

import redsilencer.hexamatch.hex.Hex;

public class Jewel {
	final Hex hex;
	final int color;
	
	public Jewel(Hex hex, int color) {
		this.hex = hex;
		this.color = color;
	}
	
	public Hex getHex() {
		return hex;
	}
	
	public int getColor() {
		return color;
	}
}
