

package model;

import java.awt.Point;


public class Map {
	private static  int longueur =10;
	private static  int hauteur =10;
	protected static  char[][] elementOnMap; //protected car utilisé dans la fonction power() du pacman
	/*Desctiption de la map
	 * 0 : emplacement libre
	 * 1 : mur
	 * 2 : spawn fantome
	 * 3 : spawn pacman
	 * 5 : superPacman
	 * 7 : void : emplacement inacessible
	 */
	private static  boolean[][] foodForPacman;
	/*Indique s'il y a une piece ou non sur la case
	 */
	private static  int counter;
	private  long now = 0;
	private Point spawnGhost = new Point(0,0);
	private Point spawnPacman = new Point(0,0);
	public static int getLongueur() {
		return longueur;
	}

	public Map(){

	}

	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}
	public static int getHauteur() {
		return hauteur;
	}
	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}
	public static char[][] getElementOnMap() {
		return elementOnMap;
	}
	public void setElementOnMap(char[][] elementOnMap) {
		this.elementOnMap = elementOnMap;
	}
	public static boolean[][] getFoodForPacman() {
		return foodForPacman;
	}
	public void setFoodForPacman(boolean[][] foodForPacman) {
		this.foodForPacman = foodForPacman;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public long getNow() {
		return now;
	}
	public void setNow(long now) {
		this.now = now;
	}

	public Point getSpawnGhost() {
		return spawnGhost;
	}

	public void setSpawnGhost(Point spawn) {
		this.spawnGhost = spawn;
	}

	public Point getSpawnPacman() {
		return spawnPacman;
	}

	public void setSpawnPacman(Point spawn) {
		this.spawnPacman = spawn;
	}


}
