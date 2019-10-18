package com.certh.iti.easytv.rbmm.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;


public class main {

	// Arguments
	private static final String _ArgUserProfile = "-u";
	private static final String _ArgUserContext = "-c";
	
	// Profiles
	private static File _UserProfileFile = null;
	private static File _UserContextFile = null;	
	
	//Files 
	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	
	
	public static void main(String[] args) throws IOException, JSONException {
		
		JSONObject userProfile, userContext;
		
		//Parse arguments
		if(args.length != 0) {
			int argn = args.length;
			for(int i = 0; i < argn; i+=2 ) {
				String arg = args[i].trim();
				String value = args[i+1];
				if(arg.equals(_ArgUserProfile)) 
					_UserProfileFile = new File(value.trim());
				else if (arg.equals(_ArgUserContext)) 
					_UserContextFile = new File(value.trim());
			}
			
			if(_UserProfileFile == null || !_UserProfileFile.exists() ) {
				System.err.println("CRITICAL: Profiles directory ('" + _ArgUserProfile + "') does not exist.");
				System.exit(-1);
			}
			
			if(_UserContextFile == null || !_UserContextFile.exists() ) {
				System.err.println("CRITICAL: Profiles directory ('" + _ArgUserContext + "') does not exist.");
				System.exit(-1);
			}
			
			userProfile = readFile(_UserProfileFile);
			userContext = readFile(_UserContextFile);
		
		} else {
			
			InputStreamReader fileReader = new InputStreamReader(System.in);
			JSONObject json = readFile(fileReader);
			
			userProfile = json.getJSONObject("user_profile");
			userContext = json.getJSONObject("user_context");
		}
		
		//initialize
		RuleReasoner ruleReasoner = new RuleReasoner(ONTOLOGY_NAME, RULES_FILE);
		
		System.out.println(ruleReasoner.infer(userProfile, userContext).toString(4));
	}
	

	/**
	 * Read JSON file content from file.
	 * 
	 * @param userFile
	 * @return
	 */
	private static JSONObject readFile(String userFile) {
		try {
			return readFile(new FileReader(new File(userFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Read JSON file content from file.
	 * 
	 * @param userFile
	 * @return
	 */
	private static JSONObject readFile(File userFile) {
		try {
			return readFile(new FileReader(userFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Read JSON file content from file.
	 * 
	 * @param file
	 * @return
	 */
	private static JSONObject readFile(Reader fileReader) {
	    String result = "";
	    try {
	        BufferedReader br = new BufferedReader(fileReader);
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        result = sb.toString();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
		
	    return new JSONObject(result);
	}

}
