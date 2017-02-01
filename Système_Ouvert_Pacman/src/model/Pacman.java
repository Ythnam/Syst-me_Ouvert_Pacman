package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;

import openSys.Context;
import openSys.IntArt;
import openSys.Position;
import openSys.ghost.MyGhostClassLoader;
import openSys.pacman.MyPacmanClassLoader;
import view.Field;

public class Pacman implements Runnable{

	private static Pacman instance; // Pacman est un singleton

	public static BufferedImage img;
	private int x, y;
	private Field field;
	private static boolean isPowerUp = false; // booleen pour le faire entrer ne état ou il bas les fantomes
	private Random rand = new Random(); // Sert juste pour les test tant qu'on n'a pas le clavier
	private ImageIcon imageIcon;
	private static ImageIcon imageIconTop = new ImageIcon("image/pacman-haut.gif");
	private static ImageIcon imageIconBot = new ImageIcon("image/pacman_bas.gif");
	private static ImageIcon imageIconLeft = new ImageIcon("image/pacman_gauche.gif");
	private static ImageIcon imageIconRight = new ImageIcon("image/pacman_droit.gif");

	private static ImageIcon imageIconTopSP = new ImageIcon("image/superpacman_haut.gif");
	private static ImageIcon imageIconBotSP = new ImageIcon("image/superpacman_bas.gif");
	private static ImageIcon imageIconLeftSP = new ImageIcon("image/superpacman_gauche.gif");
	private static ImageIcon imageIconRightSP = new ImageIcon("image/superpacman_droit.gif");

	private boolean onPause = false;
	private long pacmanScore = 0;
	private int pacmanLives = 3;
	private int ghosteaten = 0;
	private boolean right =false, left=false,top = false,down = false;

	//chrono mode PowerUp
	private double chrono = 0;
	private Chrono chron = new Chrono();
	
	private String strategyName = "";

	public void setImageIcon(ImageIcon imageIcon){
		this.imageIcon = imageIcon;
	}


	public ImageIcon getImageIcon() {
		if(right){
			return getImageIconRight();
		}
		else if(left){
			return getImageIconLeft();
		}
		else if(top){
			return getImageIconTop();
		}
		else if(down){
			return getImageIconBot();
		}
		else return imageIcon;
	}

	public static ImageIcon getImageIconTop() {
		if(!isPowerUp) return imageIconTop;
		return imageIconTopSP;
	}

	public static ImageIcon getImageIconBot() {
		if(!isPowerUp) return imageIconBot;
		return imageIconBotSP;
	}

	public static ImageIcon getImageIconLeft() {
		if(!isPowerUp) return imageIconLeft;
		return imageIconLeftSP;
	}

	public static ImageIcon getImageIconRight() {
		if(!isPowerUp) return imageIconRight;
		return imageIconRightSP;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}



	// Ce code pour le singleton a été récupéré sur le site suivant :
	//http://christophej.developpez.com/tutoriel/java/singleton/multithread/
	/**
	 * C'est une méthode nécessaire pour instancier un singleton
	 * @param x la coordonnée x du pacman à l'instanciation
	 * @param y la coordonnée y du pacman à l'instanciation
	 * @param field le field sur lequel il agira
	 * @return
	 */
	public static Pacman getInstance(int x, int y, Field field) {
		if (null == instance) { // Premier appel
			synchronized(Pacman.class) {
				instance = new Pacman(x, y, field);
			}
		}
		return instance;
	}

	private Pacman(int x, int y, Field field){

		this.x = x;
		this.y = y;
		this.field = field;
		this.imageIcon = getImageIconTop(); 

	}	

	public long getPacmanScore() {
		return pacmanScore;
	}

	public void setPacmanScore(long pacmanScore) {
		this.pacmanScore = pacmanScore;
	}



	public int getPacmanLives() {
		return pacmanLives;
	}

	public void setPacmanLives(int pacmanLives) {
		this.pacmanLives = pacmanLives;
	}

	public boolean isOnPause() {
		return onPause;
	}

	public void setOnPause(boolean onPause) {
		this.onPause = onPause;
	}

	public void setX( int x) {
		// TODO Auto-generated method stub
		this.x = x;	
	}

	public void setY(int y){
		this.y = y;
	}

	public boolean isTop() {
		return top;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public double getChronoP() {
		return chrono;
	}

	public void setChronop(double chrono) {
		this.chrono = chrono;
	}

	public boolean isPowerUp() {
		return isPowerUp;
	}

	public void setPowerUp(boolean isPowerUp) {
		this.isPowerUp = isPowerUp;
	}

	public int getGhosteaten() {
		return ghosteaten;
	}

	public void setGhosteaten(int ghosteaten) {
		this.ghosteaten = ghosteaten;
	}

	@Override
	public void run() {
		try{

			while(true){
				while(isOnPause()){
					Thread.sleep(500);
				}
				Thread.sleep(200);

				this.strategyName = this.field.getStrategie(); // Stratégie est le nom de la classe qu'on veut charger
				//System.out.println(this.strategyName);
				File file = new File("bin/openSys/pacman/"+this.strategyName+".class");
				if(file.exists()){
					try {
						loading();
						this.field.getController().testitem();
					} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					if(right){
						goRight();
						this.field.getController().testitem();
					}
					else if (left){
						goLeft();
						this.field.getController().testitem();
					}
					else if (top){
						goTop();
						this.field.getController().testitem();
					}
					else if (down){
						goBot();
						this.field.getController().testitem();
					}
				}

				field.repaint();
				if(this.field.getModel().getMap().getCounter()==0){
					Thread.sleep(10);
				}


				// on gere le superMode 
				if(this.isPowerUp()){

					this.chron.pause();
					chrono = chron.getDureeSec();
					this.chron.resume();
					if(chrono>10){
						this.setPowerUp(false);
						setGhosteaten(0);
						chrono = -1;

					}
				}

			}
		}  catch (InterruptedException e){
			e.printStackTrace();
		}

	}
	
	/**
	 * This methode is used to load class and add points
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public synchronized void loading() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		load();
		//
		if(Map.getElementOnMap()[y][x]!='1' && Map.getElementOnMap()[y][x]!='7'){ //pas un mur ni le spawn 
			if(Map.getFoodForPacman()[y][x] == true){
				pacmanScore+=1000 ;
				this.field.updateScoreAndLife();
				this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);
				Map.getFoodForPacman()[y][x]=false;
				this.saver();
			}
		}
		//
		
	}
	
	
	public synchronized void load() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		ClassLoader parentClassLoader = MyPacmanClassLoader.class.getClassLoader();
		MyPacmanClassLoader classLoader = new MyPacmanClassLoader(parentClassLoader);
		Class myObjectClass = classLoader.loadClass(this.strategyName);

		IntArt object1 = (IntArt) myObjectClass.newInstance();
		Position object2 = (Position) myObjectClass.newInstance();
		//create new class loader so classes can be reloaded.
		classLoader = new MyPacmanClassLoader(parentClassLoader);
		myObjectClass = classLoader.loadClass(this.strategyName);
		object1 = (IntArt) myObjectClass.newInstance();
		object2 = (Position) myObjectClass.newInstance();
				
		Context context = new Context(object1);
		context.IARun(this.x, this.y, this.field);
		this.x = context.getX();
		this.y = context.getY();

		
	}

	/**
	 * tester la colision du pacman
	 * si plus de vie, lancemement de savescore
	 */
	public void saver(){
		this.field.getController().loose();
		if(this.field.getModel().getMap().getCounter() == 0){
			this.field.getController().gamePause();
			if(this.field.getModel().getLvl() == 3){
				try {
					this.field.getController().savescore();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				this.field.popClassement();
			}
		}
	}


	private synchronized void goLeft(){
		if(!isOnPause()){
			if(this.x>0){
				if(Map.getElementOnMap()[y][x-1]!='1' && Map.getElementOnMap()[y][x-1]!='7'){ //pas un mur ni le spawn 
					if(Map.getFoodForPacman()[y][x-1] == true){ pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[y][x-1]=false;
					this.x--;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}

			}
			else {
				if(Map.getElementOnMap()[y][this.field.getXMAX()-1]!='1' && Map.getElementOnMap()[y][this.field.getXMAX()-1]!='7'){
					if(Map.getFoodForPacman()[y][this.field.getXMAX()-1] == true){ pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[y][this.field.getXMAX()-1]=false;
					this.x = this.field.getXMAX()-1;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}
			}
		}
	}


	private synchronized void goRight(){
		if(!isOnPause()){
			if(this.x<this.field.getXMAX()-1){
				if(Map.getElementOnMap()[y][x+1]!='1' && Map.getElementOnMap()[y][x+1]!='7' ){
					if(Map.getFoodForPacman()[y][x+1] == true){ pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[y][x+1]=false;
					this.x++;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}
			}
			else {
				if(Map.getElementOnMap()[y][0]!='1' && Map.getElementOnMap()[y][0]!='7'){
					if(Map.getFoodForPacman()[y][0] == true){ pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[y][0]=false;
					this.x=0;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}
			}
		}
	}

	private synchronized void goBot(){
		if(!isOnPause()){
			if(this.y < this.field.getYMAX()-1){
				if(Map.getElementOnMap()[y+1][x]!='1' && Map.getElementOnMap()[y+1][x]!='7' ){
					if(Map.getFoodForPacman()[y+1][x] == true) { pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[y+1][x]= false;
					this.y++;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}

			}
			else {
				if(Map.getElementOnMap()[0][x]!='1' && Map.getElementOnMap()[0][x]!='7'){
					if(Map.getFoodForPacman()[0][x] == true) { pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[0][x]=false;
					this.y=0;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}
			}
		}
	}

	private synchronized void goTop(){
		if(!isOnPause()){
			if(this.y> 0){
				if(Map.getElementOnMap()[y-1][x]!='1' && Map.getElementOnMap()[y-1][x]!='7'){
					if(Map.getFoodForPacman()[y-1][x] == true){ pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[y-1][x]=false;
					this.y--;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}
			}
			else {
				if(Map.getElementOnMap()[(this.field.getYMAX())-1][x]!='1' && Map.getElementOnMap()[(this.field.getYMAX())-1][x+1]!='7'){
					if(Map.getFoodForPacman()[this.field.getYMAX()-1][x] == true) { pacmanScore+=1000 ;this.field.updateScoreAndLife();this.field.getModel().getMap().setCounter(this.field.getModel().getMap().getCounter() - 1);}
					Map.getFoodForPacman()[this.field.getYMAX()-1][x]=false;
					this.y = this.field.getYMAX()-1;
					saver();
					if(Map.getElementOnMap()[y][x]=='5'){
						power(x,y);
					}
				}
			}
		}
	}


	/**
	 * gestion du mode PowerUp
	 * changement boolean 
	 * changement du tableau pour dire si item mangé ou non
	 * maj de l'icon du pacman
	 * @param _x position x de la case ou se trouver l'item qui rends puissant
	 * @param _y position x de la case ou se trouver l'item qui rends puissant
	 */
	public void power(int _x, int _y){
		this.setPowerUp(true);
		chron.start();
		this.field.getModel().getMap().elementOnMap[_y][_x]='0';
		setImageIcon(getImageIcon());
	}


}