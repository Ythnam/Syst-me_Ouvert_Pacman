package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import view.Field;

public class Bell extends Items{

	private static final String NAME = "bell";
	private static final int SCORE = 3000;
	public static BufferedImage img;
	//private int x, y;
	//private boolean isTaken = false; //permet de savoir si le PACMAN l'a mang� ou non

	static{
		try{
			img = ImageIO.read(new File("image/bell20x20.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public Bell(int x, int y, Field field){
		super.x = x;
		super.y = y;
		super.field = field;
	}

	public String getName(){
		return this.NAME;
	}

	public int getScore(){
		return this.SCORE;
	}

	public static BufferedImage getImg() {
		return img;
	}

	/*
	public Items generateItems(int x, int y){
		Bell b = new Bell(x,y);
		return b;
	}*/
}
