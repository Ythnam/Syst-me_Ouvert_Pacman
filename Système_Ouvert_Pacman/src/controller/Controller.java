package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import view.Field;
import model.Ghost;
import model.Items;
import model.Model;

public class Controller implements KeyListener {

	private Model model;
	private Field view;
	public long[] data;
	public String[] names;
	String  name = "";
	//private static String classement ="";
	private static String classementhtml ="";

	public Controller(Model newModel){
		this.model = newModel;
	}

	public void setView(Field view){
		this.view = view;
	}

	final public Field getView(){
		return this.view;
	}

	public void setModel(Model model){
		this.model = model;
	}

	public Model getModel(){
		return this.model;
	}

	public static String getClassementhtml() {
		return classementhtml;
	}

	public static void setClassementhtml(String classementhtml) {
		Controller.classementhtml = classementhtml;
	}

	public void testitem(){
		for (int k=0;k<this.model.getAlItems().size();k++){
			if(this.model.getAlItems().get(k)!=null){
				if(this.model.getPacman().getX() == this.model.getAlItems().get(k).getX() && this.model.getPacman().getY() == this.model.getAlItems().get(k).getY() ){
					this.model.getPacman().setPacmanScore(this.model.getPacman().getPacmanScore()+this.model.getAlItems().get(k).getScore());
					this.model.getAlItems().set(k, null);
				}
			}
		}


		/*if(this.model.getItem()!=null){
			if(this.model.getPacman().getX() == this.model.getRandomPop().getX() && this.model.getPacman().getY() == this.model.getRandomPop().getY() ){
				//this.model.getRandomPop().starStop();
				//this.model.getRandomPop().setX(-1);
				System.out.println("eat eat eat");
				//this.view.repaint();
				this.model.getPacman().setPacmanScore(this.model.getPacman().getPacmanScore()+this.model.getItem().getScore());
				this.model.setItem(null);
			}
		}*/
	}



	@Override
	public void keyPressed(KeyEvent e) {


		int source = e.getKeyCode();

		if(this.model.getPacman().getPacmanLives()>0){
			if(source==KeyEvent.VK_UP){
				this.model.getPacman().setTop(true);
				this.model.getPacman().setDown(false);
				this.model.getPacman().setRight(false);
				this.model.getPacman().setLeft(false);
				//loose();
				this.model.getPacman().setImageIcon(this.model.getPacman().getImageIconTop());

			}
			else if(source==KeyEvent.VK_DOWN){
				this.model.getPacman().setTop(false);
				this.model.getPacman().setDown(true);
				this.model.getPacman().setRight(false);
				this.model.getPacman().setLeft(false);
				//loose();
				this.model.getPacman().setImageIcon(this.model.getPacman().getImageIconBot());


			}else if(source==KeyEvent.VK_RIGHT){
				this.model.getPacman().setTop(false);
				this.model.getPacman().setDown(false);
				this.model.getPacman().setRight(true);
				this.model.getPacman().setLeft(false);

				this.model.getPacman().setImageIcon(this.model.getPacman().getImageIconRight());
			}else if(source==KeyEvent.VK_LEFT){
				this.model.getPacman().setTop(false);
				this.model.getPacman().setDown(false);
				this.model.getPacman().setRight(false);
				this.model.getPacman().setLeft(true);
				//loose();
				this.model.getPacman().setImageIcon(this.model.getPacman().getImageIconLeft());

			}else if(source==KeyEvent.VK_SPACE){
				gamePause();


				//this.model.getAlGhost().onPause = !this.model.getAlGhost().onPause;
			}
			else if(source==KeyEvent.VK_ENTER){
				if(!this.model.getPacman().isOnPause()){
					gamePause();
				}
				view.nextlvl();
				view.getModel().reset();
				view.repaint();
			} else if(source==KeyEvent.VK_A){
				view.addClassName();
			}
			else{

			}
			this.getView().setModel(this.getModel());
			this.getView().repaint();
		}
	}


	/**
	 * met les thread fantome en pause 
	 * met le pacman en pause
	 */
	public synchronized void gamePause(){
		for (Ghost g : this.model.getAlGhost()){
			g.setOnPause(!g.isOnPause());
		}
		this.model.getPacman().setOnPause(!this.model.getPacman().isOnPause());
		view.getChron().setOnPause(!view.getChron().isOnPause());
		this.model.getRandomPop().setOnPause(!this.model.getRandomPop().isOnPause());
		//view.setDelay(0);
	}


	/**
	 * calcul du score 
	 * ajout du score dans le fichier
	 * lecture du fichier
	 * lancement de la popUp
	 * @throws IOException
	 */
	public void savescore() throws IOException{

		//maj du score en fonction du nombre de vie et du temps 
		this.model.getPacman().setPacmanScore((int)(this.model.getPacman().getPacmanScore() + this.model.getPacman().getPacmanLives()*5000 - view.getChrono()*100));

		//recuperation du nom du joueur
		name = JOptionPane.showInputDialog(view,"Entrez votre pseudo", null);
		//setClassement(getClassement() + "Fin du niveau en "+this.view.getChrono()+"s\nTotal "+this.model.getPacman().getPacmanScore()+"points\n\n\n\n");

		//premiere ligne de la fenetre classement
		setClassementhtml("<html>" + "Fin du niveau en "+this.view.getChrono()+"s<br>Total "+this.model.getPacman().getPacmanScore()+"<br><br><br><br>");

		int z =1;
		//recuperation des scores precedant
		data = new long[11];
		names = new String[11];
		File f = new File ("texte/classement.txt");
		if(f.exists()){

			try {
				FileReader c = new FileReader(f); 
				BufferedReader r = new BufferedReader(c);

				String line = r.readLine();

				int i =0;
				while (line != null) {
					String[] decompose = line.split(";");
					//    

					String bla = decompose[0];
					String[] mots = bla.split (" " );
					data[i]= Integer.parseInt(mots[1]);
					names[i]= mots[0];
					i++;

					line = r.readLine();
				}

				r.close();

			} catch (Exception exception) {
				System.out.println ("Erreur lors de la recuperation : " + exception.getMessage());
			}

		}else{
			FileWriter fw = new FileWriter (f);
			fw.write ("something");
			fw.close();
		}

		/*insertion nouveau score dans fichier texte
		 * + cration du tableau de score dans un string a afficher
		 */

		try
		{
			FileWriter fw = new FileWriter (f);
			boolean bool = true;
			int l =0;
			for (int i=0;i<10;i++)
			{

				if(this.model.getPacman().getPacmanScore()>data[l] && bool){
					fw.write (name+" " + this.model.getPacman().getPacmanScore());
					// setClassement(getClassement() + z + " - "+ name+" " + this.model.getPacman().getPacmanScore() + "\n");
					setClassementhtml(getClassementhtml() + z + " - "+ name+" " + this.model.getPacman().getPacmanScore() + "<br>");
					z++;
					fw.write ("\r\n");
					bool = false;
				}
				if(data[l]!=0.0 ){
					fw.write (String.valueOf(names[l]) +" " +  String.valueOf (data[l]));
					//setClassement(getClassement() + z + " - "+ String.valueOf(names[l]) + " " + String.valueOf(data[l]) + "\n");
					setClassementhtml(getClassementhtml() + z + " - "+ String.valueOf(names[l]) + " " + String.valueOf(data[l]) + "<br>");
					z++;
					fw.write ("\r\n");
				}

				l++;
			}
			setClassementhtml(getClassementhtml() + "</html>");
			fw.close();
		}
		catch (IOException exception)
		{
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
		//lacement de la popup de classement
		view.popClassement();	
	}
	@Override
	public void keyReleased(KeyEvent e) {
		//view.pop();
		// TODO Auto-generated method stub		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * detection du mode powerUp de Pacman
	 * detection des fantome
	 * si mode normal :
	 *  * décremente counter pacman life 
	 * 	* mets le jeu en pause
	 * 	* appartition de pop up "perte de vie " ou "game over "
	 * 
	 * sinon 
	 * fantome au spawn et sleep time
	 */
	public synchronized void loose(){
		for (Ghost g:this.model.getAlGhost()){
			if(this.model.getPacman().getX() == g.getX() && this.model.getPacman().getY() == g.getY()){// si collision pacman -> fantome
				if(!this.model.getPacman().isPowerUp()){
					this.model.getPacman().setPacmanLives(this.model.getPacman().getPacmanLives()-1);
					view.updateScoreAndLife();
					gamePause();
					this.model.getPacman().setRight(false);
					this.model.getPacman().setLeft(false);
					this.model.getPacman().setTop(false);
					this.model.getPacman().setDown(false);
					if(this.model.getPacman().getPacmanLives() != 0){
						view.popLooseLife();
					}else{
						view.popLooseGame();
					}
				}
				else{
					g.setEat(true);
					if(this.model.getPacman().getGhosteaten() == 0){
						this.model.getPacman().setPacmanScore(this.model.getPacman().getPacmanScore()+200);
						this.model.getPacman().setGhosteaten(this.model.getPacman().getGhosteaten()+1);
					}
					else if(this.model.getPacman().getGhosteaten() == 1){
						this.model.getPacman().setPacmanScore(this.model.getPacman().getPacmanScore()+400);
						this.model.getPacman().setGhosteaten(this.model.getPacman().getGhosteaten()+1);
					}
					else if(this.model.getPacman().getGhosteaten() == 2){
						this.model.getPacman().setPacmanScore(this.model.getPacman().getPacmanScore()+800);
						this.model.getPacman().setGhosteaten(this.model.getPacman().getGhosteaten()+1);
					}
					else {
						this.model.getPacman().setPacmanScore(this.model.getPacman().getPacmanScore()+1600);
						this.model.getPacman().setGhosteaten(this.model.getPacman().getGhosteaten()+1);
					}

				}

			}

		}

	}


}
