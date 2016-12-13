package openSys.ghost;

import openSys.IntArt;
import openSys.Position;
import view.Field;

public class UnpossibleMoove extends Position implements IntArt{

	@Override
	public void IARun(int x, int y, Field field) {
		this.x = x;
		this.y = y;
		this.field = field;
		IAUnpossible();
	}
	
	public void IAUnpossible(){
		int dx, dy; // distance selon x et y
		dx = this.field.getModel().getPacman().getX() - this.x;
		dy = this.field.getModel().getPacman().getY() - this.y;
		
		if(dx < 0){
			this.x = this.x - 1;
		}else{
			if(dx > 0)
				this.x = this.x + 1;	
		}
		
		if(dy < 0){
			this.y = this.y - 1;
		}else{
			if(dy > 0)
				this.y = this.y + 1;
		}
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
