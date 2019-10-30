package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class RBMMTestConfig {
		
	public static final String ONTOLOGY_File = RBMMTestConfig.class.getClassLoader().getResource("EasyTV.owl").getFile();


	public static JSONObject getProfile(String fname) throws IOException {
		String line;
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(loader.getResource(fname).getFile())));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
				buff.append(line);
		
		
		JSONObject json = new JSONObject(buff.toString());		
		reader.close();
		
		return json;
		
	}
	
}
