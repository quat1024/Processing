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
	
	public EnumHexDirection rotateClockwise() {
		switch(this) {
			case POSX: return NEGY;
			case POSY: return NEGZ;
			case POSZ: return NEGX;
			case NEGX: return POSY;
			case NEGY: return POSZ;
			default:   return POSX; /*case NEGZ*/
		}
	}
	
	public EnumHexDirection rotateCounterclockwise() {
		switch(this) {
			case POSX: return NEGZ;
			case POSY: return NEGX;
			case POSZ: return NEGY;
			case NEGX: return POSZ;
			case NEGY: return POSX;
			default:   return POSY; /*case NEGZ*/
		}
	}
}
