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
import openSys.MyClassLoader;
import openSys.Position;
import openSys.RandomMoove;
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
		ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
		MyClassLoader classLoader = new MyClassLoader(parentClassLoader);
		Class myObjectClass = classLoader.loadClass("SmartRandomMoove");
		IntArt object1 = (IntArt) myObjectClass.newInstance();
		Position object2 = (Position) myObjectClass.newInstance();
		//create new class loader so classes can be reloaded.
		classLoader = new MyClassLoader(parentClassLoader);
		myObjectClass = classLoader.loadClass("SmartRandomMoove");
		object1 = (IntArt) myObjectClass.newInstance();
		object2 = (Position) myObjectClass.newInstance();
				
		Context context = new Context(object1);
		context.IARun(this.x, this.y, this.field);
		this.x = context.getX();
		this.y = context.getY();

		
	}
	

	// IA random des ghost   

	/**
	 * Cette fonction permet de savoir ce qui se trouve autour du ghost
	 * @return un tableau de 4 éléments permettant de savoir s'il y a un mur ou non autour de lui
	 */
	public synchronized char[] getWallAroundGhost(){
		char[][] cacheWallMap = this.field.getModel().getMap().getElementOnMap(); // récupère le tableau des chemins et murs
		/*for(int i=0;i<=9;i++){
							System.out.println(cacheWallMap[i][0]+""+cacheWallMap[i][1]+""+cacheWallMap[i][2]+""+cacheWallMap[i][3]+""+cacheWallMap[i][4]+""+cacheWallMap[i][5]+""+cacheWallMap[i][6]+""+cacheWallMap[i][7]+""+cacheWallMap[i][8]+""+cacheWallMap[i][9]);

					}*/
		char[] aroundAvailable = new char[4];
		if(this.x == this.field.getXMAX()-1){
			aroundAvailable[0] = cacheWallMap[this.y][0]; // droite
		}else{
			aroundAvailable[0] = cacheWallMap[this.y][this.x+1]; //droite
		}
		if(this.x == 0){
			aroundAvailable[1] = cacheWallMap[this.y][this.field.getXMAX()-1]; //gauche
		}else{
			aroundAvailable[1] = cacheWallMap[this.y][this.x-1]; //gauche
		}
		if(this.y == this.field.getYMAX()-1){
			aroundAvailable[2] = cacheWallMap[0][this.x]; //bas
		}else{
			aroundAvailable[2] = cacheWallMap[this.y+1][this.x]; //bas
		}
		if(this.y == 0){
			aroundAvailable[3] = cacheWallMap[this.field.getYMAX()-1][this.x]; //haut
		}else{
			aroundAvailable[3] = cacheWallMap[this.y-1][this.x]; //haut
		}

		//System.out.println("position"+this.x+" "+this.y+"droite:"+aroundAvailable[0]+" gauche:"+aroundAvailable[1]+" bas:"+aroundAvailable[2]+" haut"+aroundAvailable[3]);
		return aroundAvailable;
	}

	/**
	 * Cette fonction permet de choisir une direction en fonction de la valeur d'entrée
	 * @param i représente la direction du Pacman 1:droite, 2:gauche, 3:bas, 4:gauche
	 */
	private synchronized void mySwitch(int i){
		switch(i){
		case 0 :
			this.cache = 1;
			if(this.x  <this.field.getXMAX()-1){
				if(Map.getElementOnMap()[y][x+1] != '1'){
					this.x++;
				}
			}
			else {
				if(Map.getElementOnMap()[y][0] != '1'){
					this.x = 0;
				}
			}
			break;
		case 1 :
			this.cache = 0;
			if(this.x > 0){
				if(Map.getElementOnMap()[y][x-1] != '1'){
					this.x--;
				}
			}
			else {
				if(Map.getElementOnMap()[y][this.field.getXMAX()-1 ]!= '1'){
					this.x = this.field.getXMAX()-1;
				}
			}
			break;
		case 2 :
			this.cache = 3;
			if(this.y < this.field.getYMAX() - 1){
				if(Map.getElementOnMap()[y+1][x] != '1'){
					this.y++;
				}
			}
			else {
				if(Map.getElementOnMap()[0][x] != '1'){
					this.y=0;
				}
			}
			break;
		case 3 :
			this.cache = 2;
			if(this.y > 0){
				if(Map.getElementOnMap()[y-1][x] != '1') this.y--;
			}
			else {
				if(Map.getElementOnMap()[(this.field.getYMAX()-1)][x] != '1'){
					this.y = this.field.getYMAX()-1;
				}
			}
			break;
		}
	}

	/**
	 * Cette méthode est l'algorithme de mouvement des fantomes : ils ne se cognent pas contre les murs et à chaque intersection,
	 * ils choisiront une des entrées possible
	 */
	private synchronized void IAGhostRandomMoove(){

		char[] wallAround = this.getWallAroundGhost();
		ArrayList<Integer> alI = new ArrayList();
		int count = 0;		
		for(char wall : wallAround){
			if(wall !='1'){ //wall =1 -> wall est un mur
				if (Map.getElementOnMap()[this.y][this.x]=='0' && wall =='7'){

				}
				else {
					alI.add(count); // je récupère le numéro des cases ou les ghosts peuvent passer (1=top, 2=bot, 3=droite, 4=gauche);
				}
			}
			count++;
		}

		Iterator itI = alI.iterator();

		switch(alI.size()){
		case 1 :
			int position1 = (int) itI.next();
			mySwitch(position1);
			break;
		case 2 :
			int position2 = alI.get(rand.nextInt(alI.size()));
			while(position2 == this.cache) position2 = alI.get(this.rand.nextInt(alI.size()));
			mySwitch(position2);
			break;
		case 3 :
			int position3 = alI.get(rand.nextInt(alI.size()));
			while(position3 == this.cache) position3 = alI.get(rand.nextInt(alI.size()));
			mySwitch(position3);
			break;
		case 4 :
			int position4 = alI.get(rand.nextInt(alI.size()));
			while(position4 == this.cache) position4 = alI.get(rand.nextInt(alI.size()));
			mySwitch(position4);
			break;
		}

	}

	//  fin IA random des ghost

	// chasing IA ghost

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
	 * Cette fonction permet aux Ghost de se diriger vers le Pacman s'il n'y a pas de mur entre eux
	 */
	private synchronized void IAGhostChasingPacman(){
		char[] around = getWallAroundGhost();	
		if(!isWallBetweenGhostPacmanOnY()/*this.field.getModel().getPacman().getX() == this.x && this.field.getModel().getPacman().getY() != this.y*/){
			int distance = this.y - this.field.getModel().getPacman().getY();
			if(distance < 0 && Map.getElementOnMap()[this.y+1][this.x] != '1'){
				this.cache = 3; // Permet au Pacman de ne pas revenir en arriève lors du changement d'algorithme de mouvement
				if(this.y == this.field.getYMAX()-1){
					this.y = 0;
				}else{
					this.y = this.y + 1;
				}
			} 
			if(distance > 0 && Map.getElementOnMap()[this.y-1][this.x] != '1'){
				this.cache = 2; // Permet au Pacman de ne pas revenir en arriève lors du changement d'algorithme de mouvement
				if(this.y == 0){
					this.y = this.field.getYMAX()-1;
				}else{
					this.y = this.y - 1;
				}

			}
		}

		if(!isWallBetweenGhostPacmanOnX()/*this.field.getModel().getPacman().getY() == this.y && this.field.getModel().getPacman().getX() != this.x*/){
			int distance = this.x - this.field.getModel().getPacman().getX();
			if(distance < 0 && Map.getElementOnMap()[this.y][this.x+1] != '1'){
				this.cache = 1; // Permet au Pacman de ne pas revenir en arriève lors du changement d'algorithme de mouvement
				if(this.x == 0){
					this.x = this.field.getXMAX()-1;
				}else{
					this.x = this.x + 1;
				}
			} 
			if(distance > 0 && Map.getElementOnMap()[this.y][this.x-1] != '1'){
				this.cache = 0; // Permet au Pacman de ne pas revenir en arriève lors du changement d'algorithme de mouvement
				if(this.x == this.field.getXMAX()-1){
					this.x = 0;
				}else{
					this.x = this.x - 1;
				}
			}
		}
	}
	// fin chasing IA Ghost

	/**
	 * IA des Ghost qui permet soit d'avoir un fantome de se déplacer normalement ou d'aller chasser le pacman s'il a la vision
	 * sur lui (pas de mur entre eux)
	 */
	private synchronized void IAGhost(){
		if(!this.field.getModel().getPacman().isPowerUp()){
			if(this.isWallBetweenGhostPacmanOnX() == false || this.isWallBetweenGhostPacmanOnY() == false){
				IAGhostChasingPacman();
				//System.out.println("IA chasing");
				this.timeSleeping = 250;
			} 
			else{
				IAGhostRandomMoove();
				//System.out.println("IA nm");
				this.timeSleeping = 500;
			}
		} else{
			IAGhostRandomMoove();
			//System.out.println("IA nm !");
			this.timeSleeping = 500;

		}

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