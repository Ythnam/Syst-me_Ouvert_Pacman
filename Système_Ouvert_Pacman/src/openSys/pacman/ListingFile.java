package openSys.pacman;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class ListingFile {

	private ArrayList<String> alS = new ArrayList<String>();
	
	public ListingFile(){
		
	}
	
	public ArrayList<String> getAlS() {
		return alS;
	}

	// http://stackoverflow.com/questions/4852531/find-files-in-a-folder-using-java
	public void listingFile(){
		File f = new File("bin/openSys/pacman/");
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("class");
		    }
		});
		for(File file : matchingFiles){
			this.alS.add(file.getName());
		}
	}
}
