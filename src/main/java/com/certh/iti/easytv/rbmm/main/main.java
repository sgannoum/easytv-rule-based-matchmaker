package com.certh.iti.easytv.rbmm.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;


public class main {

	// Arguments
	private static final String _ArgUserProfile = "-u";
	
	// Profiles
	private static File _UserProfileFile = null;
	
	//Files 
	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	
	
	public static void main(String[] args) throws IOException, JSONException, UserProfileParsingException {
		
		JSONObject json = null, userProfile = null, userContext = null, userContent = null;
		
		//Parse arguments
		if(args.length != 0) {
			int argn = args.length;
			for(int i = 0; i < argn; i+=2 ) {
				String arg = args[i].trim();
				String value = args[i+1];
				if(arg.equals(_ArgUserProfile)) 
					_UserProfileFile = new File(value.trim());
			}
			
			if(_UserProfileFile == null || !_UserProfileFile.exists() ) {
				System.err.println("CRITICAL: User profiles ('" + _ArgUserProfile + "') does not exist.");
				System.exit(-1);
			}
			
			//read user profile
			json = readFile(_UserProfileFile);

		
		} else {
			System.out.println("Enter profile: ");
			InputStreamReader fileReader = new InputStreamReader(System.in);
			json = readFile(fileReader);
		}
		
		//parse json
		if(!json.has("user_profile")) {
			System.err.println("CRITICAL: User profile ('" + json + "') does not exist.");
			System.exit(-1);
		}
		
		//read user profile
		userProfile = json.getJSONObject("user_profile");
		OntProfile profile = new OntProfile(userProfile);
		
		//initialize
		RuleReasoner ruleReasoner = new RuleReasoner(ONTOLOGY_NAME, RULES_FILE);
		
		System.out.println(ruleReasoner.infer(profile));

		
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
