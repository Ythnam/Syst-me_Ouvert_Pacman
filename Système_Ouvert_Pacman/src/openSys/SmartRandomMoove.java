package openSys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import model.Map;
import view.Field;

public class SmartRandomMoove extends Position implements IntArt {

	private int cache = -10;
	private Random rand = new Random();
	
	@Override
	public void IARun(int x, int y, Field field) {
		this.x = x;
		this.y = y;
		this.field = field;
		IAGhostRandomMoove();
	}
	
	
	// IA random des ghost   

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

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

}
