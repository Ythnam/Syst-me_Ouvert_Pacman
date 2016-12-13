package openSys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyClassLoader extends ClassLoader{

	private String classes = "";
	
	public MyClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class loadClass(String name) throws ClassNotFoundException {
		switch(name){
		case "RandomMoove" :
			this.classes = "RandomMoove";
			break;
		case "SmartRandomMoove" :
			this.classes = "SmartRandomMoove";
			break;
		case "ChasingMoove" :
			this.classes = "ChasingMoove";
			break;
		default :
			return super.loadClass(name);
		}
		/*if(!"RandomMoove".equals(name))
			return super.loadClass(name);*/
		try {
			String url = "file:C:/Users/phild/git3/Syst�me_Ouvert_Pacman/bin/openSys/"+this.classes+".class"; // � revoir
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
			return defineClass("openSys."+this.classes,
					classData, 0, classData.length);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
