package redsilencer.hexamatch.jewel;

import redsilencer.hexamatch.hex.Hex;

import java.util.HashMap;

public class JewelBoard {
	HashMap<Hex, Jewel> board = new HashMap<>();
	
	public Jewel getJewel(Hex hex) {
		return board.get(hex);
	}
	
	public void putJewel(Hex hex, Jewel jewel) {
		board.put(hex, jewel);
	}
	
	public Iterable<Jewel> getAllJewels() {
		return board.values();
	}
}
