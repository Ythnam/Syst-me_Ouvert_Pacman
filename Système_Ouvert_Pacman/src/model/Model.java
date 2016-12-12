package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {

	private Pacman pacman;
	private Map map = new Map();
	private ArrayList<Ghost> alGhost = new ArrayList<>();
	private ArrayList<Items> alItems = new ArrayList<>();
	private RandomItemPop randomPop;
	//	private Items item = null;
	private int lvl = 1;
	private String fichier="texte/lvl"+ getLvl() + ".txt";

	public Model(){
		lecture();
		createstring();
	}

	public void updatefichier(){
		this.fichier = "texte/lvl"+ getLvl() + ".txt";
	}

	public ArrayList<Ghost> getAlGhost() {
		return alGhost;
	}

	public ArrayList<Items> getAlItems() {
		return alItems;
	}

	public Pacman getPacman() {
		return pacman;
	}

	public RandomItemPop getRandomPop() {
		return randomPop;
	}

	/*public Items getItem() {
		return item;
	}

	public void setItem(Items item) {
		this.item = item;
	}**/

	public void setRandomPop(RandomItemPop randomPop) {
		this.randomPop = randomPop;
	}


	public void setAlGhost(ArrayList<Ghost> alGhost) {
		this.alGhost = alGhost;
	}

	/*public void setAlItems(ArrayList<Items> alItems) {
		this.alItems = alItems;
	}*/

	public void setPacman(Pacman pacman) {
		this.pacman = pacman;
	}

	public void addToAlGhost(Ghost g){
		this.alGhost.add(g);
	}

	public void addToAlItems(Items i){
		this.alItems.add(i);
	}

	public Map getMap() {
		return map;
	}

	public void reset(){
		this.alItems = new ArrayList<Items>();
	}
	public void createstring () {
		map = new Map();
		map.setElementOnMap(new char[map.getHauteur()][map.getLongueur()]);
		map.setFoodForPacman(new boolean[map.getHauteur()][map.getLongueur()]);
		int count = 0 ;
		int i =0;
		int j = 0 ;
		try{
			File ips = new File(getFichier());
			FileReader ipsr = new FileReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			Scanner sc = null;
			try {
				sc = new Scanner(new File(getFichier()));
				while (sc.hasNextLine()) {
					for (char c : sc.next().toCharArray()) {
						map.getElementOnMap()[i][j] = c;
						if(c=='0'){
							map.getFoodForPacman()[i][j] = true;
							count++;
						}
						else {

							map.getFoodForPacman()[i][j] = false;
						}


						if(c=='2'){
							this.map.setSpawnGhost(new Point(i,j));
						}
						if(c=='3'){
							this.map.setSpawnPacman(new Point(i,j));
						}
						j++;
					}
					i++;
					j=0;
				}
			} finally {
				if (sc != null)
					sc.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		map.setCounter(count);
	}


	public void lecture () {
		int longueur1 = 0;
		int hauteur1 = 0;

		try{
			File ips = new File(getFichier());
			FileReader ipsr = new FileReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			Scanner sc = null;
			try {
				sc = new Scanner(new File(getFichier()));
				while (sc.hasNextLine()) {
					for (char c : sc.next().toCharArray()) {
						longueur1 += 1;
					}
					hauteur1 += 1 ;
				}
			} finally {
				if (sc != null)
					sc.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		longueur1 = longueur1/hauteur1;
		this.map.setHauteur(hauteur1);
		this.map.setLongueur(longueur1);		
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}



}
