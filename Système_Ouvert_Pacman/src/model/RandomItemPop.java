package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import view.Field;

public class RandomItemPop implements Runnable{

	ArrayList<Point> alP = new ArrayList<Point>();

	private Field field;
	private int x;
	private int y;
	private boolean onPause = false;



	public RandomItemPop(Field field){
		this.field = field;
	}

	public RandomItemPop(int x, int y, Field field){
		this.x = x;
		this.y = y;
		this.field = field;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


	@Override
	public void run() {

		boolean repop = true;
		boolean restart = true;
		while(true){	

			if((this.field.getChrono()%11>=10) && (this.field.getChrono()%11<11)&& repop){

				restart = true;
				repop = false;
				this.field.getModel().reset();


			}
			if((this.field.getChrono()%11>=5) && (this.field.getChrono()%11<8) && restart){

				repop = true;
				restart = false;
				this.typeOfItemsSelected();
			}


			/*
			this.typeOfItemsSelected();

			this.field.repaint();
			Thread.sleep(5000);

			for (int k=0;k<this.field.getModel().getAlItems().size();k++){
				this.field.getModel().getAlItems().set(k, null);
			}
			this.field.repaint();
			Thread.sleep(5000);
			 */
		}

	}


	private void pointItemPop(){
		String line="";
		alP = new ArrayList<Point>();
		char[][] WallMap = this.field.getModel().getMap().getElementOnMap(); // récupère le tableau des chemins et murs


		for(int i = 0; i < this.field.getXMAX(); i++){
			for(int j = 0; j < this.field.getYMAX(); j++){
				line+=(WallMap[j][i]);
				if(WallMap[j][i] != '1' && WallMap[j][i] !='7' && WallMap[j][i] !='2'){
					alP.add(new Point(i, j));
				}

			}
		}

		// J'ai récupéré les points où les Items peuvent pop




	}

	public void creaPoint(){
		Random rand = new Random();
		int randomIntPop = rand.nextInt(alP.size());

		this.x = (int) alP.get(randomIntPop).getX();
		this.y = (int) alP.get(randomIntPop).getY();
	}

	private void typeOfItemsSelected(){
		int level = this.field.getModel().getLvl();
		Items items = new Apple(0,0,this.field);

		this.pointItemPop();
		this.creaPoint();

		switch(level){
		case 1 :
			items = new Cherry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			break;
		case 2 :
			items = new Cherry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			this.creaPoint();
			items = new Strawberry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			break;
		case 3 :
			items = new Cherry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			this.creaPoint();
			items = new Strawberry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			this.creaPoint();
			items = new Orange(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			break;
		case 4 :
			items = new Cherry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			this.creaPoint();
			items = new Strawberry(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			this.creaPoint();
			items = new Orange(this.x, this.y, this.field);
			this.field.getModel().addToAlItems(items);
			this.creaPoint();
			break;
		case 5 :
			items = new Apple(this.x, this.y, this.field);
			break;
		case 6 :
			items = new Apple(this.x, this.y, this.field);
			break;
		case 7 :
			items = new Melon(this.x, this.y, this.field);
			break;
		case 8 :
			items = new Melon(this.x, this.y, this.field);
			break;
		case 9 : 
			items = new Galboss(this.x, this.y, this.field);
			break;
		case 10 :
			items = new Galboss(this.x, this.y, this.field);
			break;
		case 11 :
			items = new Bell(this.x, this.y, this.field);
			break;
		case 12 :
			items = new Bell(this.x, this.y, this.field);
			break;
		default :
			items = new Key(this.x, this.y, this.field);
			break;

		}
		//this.field.getModel().setItem(items);
	}

	public void setX(int i) {
		// TODO Auto-generated method stub
		this.x = i;
	}

	public boolean isOnPause() {
		return onPause;
	}

	public void setOnPause(boolean onPause) {
		this.onPause = onPause;
	}

	public void stop(){

	}

}
