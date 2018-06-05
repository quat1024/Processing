package redsilencer.hexamatch.jewel;

import redsilencer.hexamatch.hex.EnumHexDirection;
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
	
	public void populate(int radius) {
		//todo actually place good jewels not this temp crap
		putJewelBlah(new Hex(0, 0, 0));
		putJewelBlah(new Hex(0, 0, 0).offset(EnumHexDirection.POSX));
		putJewelBlah(new Hex(0, 0, 0).offset(EnumHexDirection.POSY));
		putJewelBlah(new Hex(0, 0, 0).offset(EnumHexDirection.POSZ));
		putJewelBlah(new Hex(0, 0, 0).offset(EnumHexDirection.NEGX));
		putJewelBlah(new Hex(0, 0, 0).offset(EnumHexDirection.NEGY));
		putJewelBlah(new Hex(0, 0, 0).offset(EnumHexDirection.NEGZ));
	}
	
	private void putJewelBlah(Hex blah) {
		putJewel(blah, new Jewel(blah, 2));
	}
	
	public Iterable<Jewel> getAllJewels() {
		return board.values();
	}
}
