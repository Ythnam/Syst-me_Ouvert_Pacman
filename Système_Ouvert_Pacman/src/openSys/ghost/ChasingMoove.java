package openSys.ghost;

import java.util.ArrayList;

import model.Map;
import openSys.IntArt;
import openSys.Position;
import view.Field;

public class ChasingMoove extends Position implements IntArt {

	private int cache = -10;

	@Override
	public void IARun(int x, int y, Field field) {
		this.x = x;
		this.y = y;
		this.field = field;
		IAGhostChasingPacman();

	}

	// chasing IA ghost

	/**
	 * Cette fonction permet de savoir ce qui se trouve autour du ghost
	 * @return un tableau de 4 éléments permettant de savoir s'il y a un mur ou non autour de lui
	 */
	public synchronized char[] getWallAroundGhost(){
		char[][] cacheWallMap = this.field.getModel().getMap().getElementOnMap(); // récupère le tableau des chemins et murs
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

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

}
