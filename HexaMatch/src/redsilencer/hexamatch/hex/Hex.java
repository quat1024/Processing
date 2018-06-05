package redsilencer.hexamatch.hex;

public class Hex {
	// "cube coordinates"
	public final int x, y, z;
	
	public Hex(int x, int y, int z) {
		validateCoordinates(x, y, z);
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	void validateCoordinates(int x, int y, int z) {
		if(x + y + z != 0) throw new IllegalArgumentException(String.format("Invalid hex cube coordinates: %s %s %s", x, y, z));
	}
	
	Hex offset(EnumHexDirection dir) {
		return dir.offset(this);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Hex)) return false;
		Hex otherHex = (Hex) other;
		
		return otherHex.x == x && otherHex.y == y && otherHex.z == z;
	}
}
