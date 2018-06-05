package redsilencer.hexamatch.hex;

public enum EnumHexDirection {
	POSX(+1, -1, 0),
	NEGX(-1, +1, 0),
	POSY(0, +1, -1),
	NEGY(0, -1, +1),
	POSZ(-1, 0, +1),
	NEGZ(+1, 0, -1);
	
	int xOffset, yOffset, zOffset;
	
	EnumHexDirection(int xOffset, int yOffset, int zOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
	}
	
	public Hex offset(Hex hex) {
		return new Hex(hex.x + xOffset, hex.y + yOffset, hex.z + zOffset);
	}
}
