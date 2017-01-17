package openSys.pacman;

import openSys.IntArt;
import openSys.Position;
import view.Field;

public class Teleportation extends Position implements IntArt{

	@Override
	public void IARun(int x, int y, Field field) {
		this.x = x;
		this.y = y;
		this.field = field;
		
	}
	
	public void teleportation(){
		
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

}
