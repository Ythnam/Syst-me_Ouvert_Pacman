package openSys;

import view.Field;

public class Context {
	private IntArt intArt;
	
	public Context(IntArt intArt){
	      this.intArt = intArt;
	   }
	
	public void IARun(int x, int y, Field field){
		this.intArt.IARun(x, y, field);
	}
	
	public int getX(){
		return this.intArt.getX();
	}
	
	public int getY(){
		return this.intArt.getY();
	}
	
	/*public Field getField(){
		return this.intArt.getField();
	}*/
	
}
