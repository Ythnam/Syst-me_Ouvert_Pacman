package model;

public class Chrono {

	private long tempsDepart=0;
	private long tempsFin=0;
	long pauseDepart=0;
	private long pauseFin=0;
	private double dureed=0;
	private long duree = 0 ;
	private boolean onPause = false;
	private long gamePause = 0;

	public void start()
	{
		tempsDepart=System.currentTimeMillis();
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		duree=0;
	}

	public void pause()
	{
		pauseDepart=System.currentTimeMillis();
		duree += pauseDepart - tempsDepart;
	}

	public void resume()
	{
		if(tempsDepart==0) {return;}
		if(pauseDepart==0) {return;}
		tempsDepart=System.currentTimeMillis();
		// tempsDepart=tempsDepart+pauseFin-pauseDepart;
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		//duree=0;
	}

	public void gamepause(){
		tempsDepart=System.currentTimeMillis();
	}
	public void restart(){
		tempsDepart=System.currentTimeMillis();
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		duree=0;
		dureed =0;
		onPause = false;
	}

	public double getDureeSec()
	{
		dureed = duree;
		return dureed/1000;
	}

	public double getDureeMs()
	{
		dureed = duree;
		return dureed;
	}        

	public String getDureeTxt()
	{
		return timeToHMS(duree/100);
	}

	public static String timeToHMS(long tempsS) {

		// IN : (long) temps en secondes
		// OUT : (String) temps au format texte : "1 h 26 min 3 s"

		int h = (int) (tempsS / 3600);
		int m = (int) ((tempsS % 3600) / 60);
		int s = (int) (tempsS % 60);

		String r="";

		if(h>0) {r+=h+" h ";}
		if(m>0) {r+=m+" min ";}
		if(s>0) {r+=s+" s";}
		if(h<=0 && m<=0 && s<=0) {r="0 s";}

		return r;
	}
	//ref timer : https://fr.jeffprod.com/blog/2015/un-chronometre-en-java.html

	public boolean isOnPause() {
		return onPause;
	}

	public void setOnPause(boolean onPause) {
		this.onPause = onPause;
	}
} 
