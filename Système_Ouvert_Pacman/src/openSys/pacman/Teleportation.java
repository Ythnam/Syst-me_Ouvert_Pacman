package openSys.pacman;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import openSys.IntArt;
import openSys.Position;
import view.Field;

public class Teleportation extends Position implements IntArt{

	@Override
	public void IARun(int x, int y, Field field) {
		this.x = x;
		this.y = y;
		this.field = field;
		teleportation();
		
	}
	
	public ArrayList<Point> getCoordOfFood(){
		boolean[][] cacheWallMap = this.field.getModel().getMap().getFoodForPacman();
		int numL = 0;
		int numC = 0;
		ArrayList<Point> alP = new ArrayList<Point>();
		for(boolean[] columnBool : cacheWallMap){	
			for(boolean lineBool : columnBool){
				if(lineBool)
					alP.add(new Point(numL, numC));
				numL++;
			}
			numC++;
			numL = 0;
		}
		return alP;
	}
	
	public void teleportation(){
		ArrayList<Point> alP = this.getCoordOfFood();
		/*for(Point p : alP){
			System.out.println("x : "+p.getX()+"      y : "+p.getY());
		}*/

		Random rand = new Random();
		int randOnPoint = rand.nextInt(alP.size());
		Point point = alP.get(randOnPoint);
		
		this.x = (int) point.getX();
		this.y = (int) point.getY();

		
		
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
