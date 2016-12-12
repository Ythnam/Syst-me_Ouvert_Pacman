package openSys;

import java.util.Random;

import model.Map;
import view.Field;

public class RandomMoove extends Position implements IntArt {

	@Override
	public void IARun(int x, int y, Field field) {

		Random rand = new Random();
		this.x = x;
		this.y = y;
		this.field = field;
		
		int choice = rand.nextInt(4);
		switch(choice){
		case 0 : 
			if(this.x<this.field.getXMAX()-1){
				if(Map.getElementOnMap()[y][x+1]!='1'){this.x++;}
			}
			else {
				if(Map.getElementOnMap()[y][0]!='1'){
					this.x=0;
				}
			}
			break;
		case 1 :
			if(this.x>0){
				if(Map.getElementOnMap()[y][x-1]!='1'){this.x--;}
			}
			else {
				if(Map.getElementOnMap()[y][this.field.getXMAX()-1]!='1'){
					this.x = this.field.getXMAX()-1;
				}
			}
			break;
		case 2 :
			if(this.y < this.field.getYMAX()-1){
				if(Map.getElementOnMap()[y+1][x]!='1'){
					this.y++;
				}
			}
			else {
				if(Map.getElementOnMap()[0][x]!='1'){
					this.y=0;
				}
			}
			break;
		case 3 :
			if(this.y> 0){
				if(Map.getElementOnMap()[y-1][x]!='1') this.y--;
			}
			else {
				if(Map.getElementOnMap()[(this.field.getYMAX()-1)][x]!='1'){
					this.y = this.field.getYMAX()-1;
				}
			}
			break;
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

	/*@Override
	public Field getField() {
		return this.field;
	}
*/
}
