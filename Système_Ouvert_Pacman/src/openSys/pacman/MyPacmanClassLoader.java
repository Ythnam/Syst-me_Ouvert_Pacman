package openSys.pacman;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MyPacmanClassLoader extends ClassLoader {

private String classes = "";
	
	public MyPacmanClassLoader(ClassLoader parent){
		super(parent);
	}

	public synchronized Class loadClass(String name) throws ClassNotFoundException {
	
		ListingFile lf = new ListingFile();
		lf.listingFile();
		ArrayList<String> alS = lf.getAlS();
		if(alS.contains(name+".class") && !name.equals("MyPacmanClassLoader.class") && !name.equals("ListingFile.class")){
			this.classes = name;
		} else{
			return super.loadClass(name);
		}
		try {
			this.classes = name;
			File file = new File("bin/openSys/pacman/"+this.classes+".class");
			String classPath = file.getCanonicalPath();
			String url = "file:"+classPath;
			System.out.println(url);
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while(data != -1){
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			System.out.println(this.classes);
			return defineClass("openSys.pacman."+this.classes,
					classData, 0, classData.length);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
