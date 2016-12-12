package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import view.Field;

public abstract class Items {

	protected int x, y;
	protected boolean isTaken = false; //permet de savoir si le PACMAN l'a mangé ou non
	protected Field field;
	protected int SCORE;


	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public abstract String getName();
	public abstract int getScore();
	//public abstract Items generateItems();
}
