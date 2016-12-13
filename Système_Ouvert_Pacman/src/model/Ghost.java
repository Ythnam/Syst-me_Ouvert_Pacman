package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

import openSys.Context;
import openSys.IntArt;
import openSys.Position;
import openSys.ghost.MyGhostClassLoader;
import openSys.ghost.RandomMoove;
import view.Field;

public class Ghost implements Runnable {

	public static BufferedImage ghostRED;
	public static BufferedImage ghostBLUE;
	public static BufferedImage ghostGREEN;
	public static BufferedImage ghostORANGE;
	public static BufferedImage ghostSCARED;
	private int x, y;
	private double distance; // donnera sa distance par rapport au Pacman et permettra de le faire "chasser" à partir
	// d'une certaine distance
	private Field field;
	private Random rand = new Random();
	private boolean onPause = false;
	private int cache = -10; // pour que les fantomes ne soient pas influencé dès le départ
	private boolean eat = false;
	private int timeSleeping = 500;
	private int shortSleeping = 250;

	static{
		try{
			ghostRED = ImageIO.read(new File("image/ghost_red20x20.png"));
			ghostBLUE = ImageIO.read(new File("image/ghost_blue20x20.png"));
			ghostGREEN = ImageIO.read(new File("image/ghost_green20x20.png"));
			ghostORANGE = ImageIO.read(new File("image/ghost_orange20x20.png"));
			ghostSCARED = ImageIO.read(new File("image/ghost_scared_converted.png"));

		} catch (IOException e){
			e.printStackTrace();
		}
	}




	public Ghost(int x, int y, Field field){
		this.x = x;
		this.y = y;
		this.field = field;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public static BufferedImage getGhostBLUE() {
		return ghostBLUE;
	}

	public static BufferedImage getGhostGREEN() {
		return ghostGREEN;
	}

	public static BufferedImage getGhostORANGE() {
		return ghostORANGE;
	}

	public static BufferedImage getGhostRED() {
		return ghostRED;
	}

	public static BufferedImage getGhostSCARED() {
		return ghostSCARED;
	}	

	public boolean isOnPause() {
		return onPause;
	}

	public void setOnPause(boolean onPause) {
		this.onPause = onPause;
	}

	public boolean isEat() {
		return eat;
	}

	public void setEat(boolean eat) {
		this.eat = eat;
	}


	@Override
	public void run(){
		try{

			while(true){
				while(isOnPause()){

					Thread.sleep(500);
				}
				if(isEat()){
					this.x = this.field.getModel().getMap().getSpawnGhost().y;
					this.y = this.field.getModel().getMap().getSpawnGhost().x;
					Thread.sleep(8500);
					setEat(false);
				}

				Thread.sleep(this.timeSleeping);


				//IAGhost();
				//Context context = new Context(new RandomMoove());
				//context.IARun(this.x, this.y, this.field);
				//this.x = context.getX();
				//this.y = context.getY();
				try {
					load();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				comparisonGhostPacman();

				field.repaint();
				if(this.field.getModel().getMap().getCounter()==0){
					Thread.sleep(10);
				}

			}
		}  catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	public synchronized void load() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		ClassLoader parentClassLoader = MyGhostClassLoader.class.getClassLoader();
		MyGhostClassLoader classLoader = new MyGhostClassLoader(parentClassLoader);
		
		String name = "";
		
		if(!this.field.getModel().getPacman().isPowerUp()){
			if(this.isWallBetweenGhostPacmanOnX() == false || this.isWallBetweenGhostPacmanOnY() == false){
				name = "ChasingMoove";
				this.timeSleeping = 250;
			} 
			else{
				name = "SmartRandomMoove";
				this.timeSleeping = 500;
			}
		} else{
			name = "SmartRandomMoove";
			this.timeSleeping = 500;

		}
		
		//name = "UnpossibleMoove";
		Class myObjectClass = classLoader.loadClass(name/*"ChasingMoove"*/);
		IntArt object1 = (IntArt) myObjectClass.newInstance();
		Position object2 = (Position) myObjectClass.newInstance();
		//create new class loader so classes can be reloaded.
		classLoader = new MyGhostClassLoader(parentClassLoader);
		myObjectClass = classLoader.loadClass(name/*"ChasingMoove"*/);
		object1 = (IntArt) myObjectClass.newInstance();
		object2 = (Position) myObjectClass.newInstance();
				
		Context context = new Context(object1);
		context.IARun(this.x, this.y, this.field);
		this.x = context.getX();
		this.y = context.getY();

		
	}
	

	/**
	 * Cette fonction permet de savoir s'il y a des murs entre le Ghost et le Pacman sur la composante X
	 * @return true s'il y a des murs entre eux et false sinon
	 */
	private synchronized boolean isWallBetweenGhostPacmanOnX(){
		char[][] cacheWallMap = this.field.getModel().getMap().getElementOnMap(); // récupère le tableau des chemins et murs
		int distanceX = this.x - this.field.getModel().getPacman().getX();
		if(this.y == this.field.getModel().getPacman().getY()){
			if(distanceX > 0){
				ArrayList<Character> cacheWallMapX = new ArrayList<Character>();
				// on met en mémoire les cases sur l'axe X se trouvant entre le PacMan et le Ghost
				for(int i = this.x-1; i >= this.field.getModel().getPacman().getX(); i--){
					cacheWallMapX.add(cacheWallMap[this.y][i]);
				}

				int counter = 0;
				// on incrémente un counter afin de comparer le counter et la taille de la liste et s'ils sont égaux, alors il n'y a 
				// pas de mur entre les 2
				for(Character c : cacheWallMapX){
					if(c != '1'){
						counter++;
					}
				}

				if(counter == cacheWallMapX.size()){
					return false; // pas de mur sur le chemin
				}
			}
			if(distanceX < 0){
				ArrayList<Character> cacheWallMapX = new ArrayList<Character>();
				// on met en mémoire les cases sur l'axe X se trouvant entre le PacMan et le Ghost
				for(int i = this.x+1; i <= this.field.getModel().getPacman().getX(); i++){
					cacheWallMapX.add(cacheWallMap[this.y][i]);
				}

				int counter = 0;
				// on incrémente un counter afin de comparer le counter et la taille de la liste et s'ils sont égaux, alors il n'y a 
				// pas de mur entre les 2
				for(Character c : cacheWallMapX){
					if(c != '1'){
						counter++;

					}
				}

				if(counter == cacheWallMapX.size()){
					return false; // pas de mur sur le chemin
				}
			}
		}
		return true;
	}

	/**
	 * Cette fonction permet de savoir s'il y a des murs entre le Ghost et le Pacman sur la composante Y
	 * @return true s'il y a des murs entre eux et false sinon
	 */
	private synchronized boolean isWallBetweenGhostPacmanOnY(){
		char[][] cacheWallMap = this.field.getModel().getMap().getElementOnMap(); // récupère le tableau des chemins et murs
		int distanceY = this.y - this.field.getModel().getPacman().getY();
		if(this.x == this.field.getModel().getPacman().getX()){
			if(distanceY > 0){
				ArrayList<Character> cacheWallMapY = new ArrayList<Character>();
				// on met en mémoire les cases se trouvant entre le PacMan et le Ghost
				for(int i = this.y-1; i >= this.field.getModel().getPacman().getY(); i--){
					cacheWallMapY.add(cacheWallMap[i][this.x]);
				}

				int counter = 0;
				// on incrémente un counter afin de comparer le counter et la taille de la liste et s'ils sont égaux, alors il n'y a 
				// pas de mur entre les 2
				for(Character c : cacheWallMapY){
					if(c != '1'){
						counter++;
					}
				}
				if(counter == cacheWallMapY.size()){
					return false;
				}
			}
			if(distanceY < 0){
				ArrayList<Character> cacheWallMapY = new ArrayList<Character>();
				// on met en mémoire les cases se trouvant entre le PacMan et le Ghost
				for(int i = this.y+1; i <= this.field.getModel().getPacman().getY(); i++){
					cacheWallMapY.add(cacheWallMap[i][this.x]);
				}

				int counter = 0;
				// on incrémente un counter afin de comparer le counter et la taille de la liste et s'ils sont égaux, alors il n'y a 
				// pas de mur entre les 2
				for(Character c : cacheWallMapY){
					if(c != '1'){
						counter++;
					}
				}
				if(counter == cacheWallMapY.size()){
					return false;
				}
			}
		}
		return true;
	}

	

	/**
	 * Cette fonction permet de savoir si le fantome et le pacman sont sur la même case et si  c'est le cas, lequel des deux mange
	 * l'autre
	 */
	private synchronized void comparisonGhostPacman(){
		if(this.x == this.field.getModel().getPacman().getX() && this.y == this.field.getModel().getPacman().getY()){// si collision pacman -> fantome
			if(!this.field.getModel().getPacman().isPowerUp()){
				this.field.getModel().getPacman().setPacmanLives(this.field.getModel().getPacman().getPacmanLives()-1);
				this.field.updateScoreAndLife();
				this.field.getController().gamePause();
				this.field.getModel().getPacman().setRight(false);
				this.field.getModel().getPacman().setLeft(false);
				this.field.getModel().getPacman().setTop(false);
				this.field.getModel().getPacman().setDown(false);
				if(this.field.getModel().getPacman().getPacmanLives() != 0){
					this.field.popLooseLife();
				}else{
					this.field.popLooseGame();
				}
			}
			else{
				this.setEat(true);
				if(this.field.getModel().getPacman().getGhosteaten() == 0){
					this.field.getModel().getPacman().setPacmanScore(this.field.getModel().getPacman().getPacmanScore()+200);
					this.field.getModel().getPacman().setGhosteaten(this.field.getModel().getPacman().getGhosteaten()+1);
				}
				else if(this.field.getModel().getPacman().getGhosteaten() == 1){
					this.field.getModel().getPacman().setPacmanScore(this.field.getModel().getPacman().getPacmanScore()+400);
					this.field.getModel().getPacman().setGhosteaten(this.field.getModel().getPacman().getGhosteaten()+1);
				}
				else if(this.field.getModel().getPacman().getGhosteaten() == 2){
					this.field.getModel().getPacman().setPacmanScore(this.field.getModel().getPacman().getPacmanScore()+800);
					this.field.getModel().getPacman().setGhosteaten(this.field.getModel().getPacman().getGhosteaten()+1);
				}
				else {
					this.field.getModel().getPacman().setPacmanScore(this.field.getModel().getPacman().getPacmanScore()+1600);
					this.field.getModel().getPacman().setGhosteaten(this.field.getModel().getPacman().getGhosteaten()+1);
				}

			}

		}		
	}



}