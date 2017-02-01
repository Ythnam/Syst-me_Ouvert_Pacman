package openSys.pacman;

import openSys.IntArt;
import openSys.Position;
import view.Field;

public class XPP extends Position implements IntArt{

	@Override
	public void IARun(int x, int y, Field field) {
		this.x = x;
		this.y = y;
		this.field = field;
		teleportation();
		
	}
	
	public void teleportation(){
		this.x = this.x + 1;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

}
