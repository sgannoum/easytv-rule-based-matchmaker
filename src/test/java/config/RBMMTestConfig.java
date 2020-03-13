package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class RBMMTestConfig {
		
	public static final String ONTOLOGY_File = "EasyTV.owl";


	public static JSONObject getProfile(String fname) throws IOException {
		String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(fname)));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
				buff.append(line);
		
		
		JSONObject json = new JSONObject(buff.toString());		
		reader.close();
		
		return json;
		
	}
	
}
