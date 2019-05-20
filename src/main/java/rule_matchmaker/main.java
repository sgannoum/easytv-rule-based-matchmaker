package rule_matchmaker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.jena.rdf.model.Model;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;


public class main {

	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	private static final String INPUT_FILE = "input.txt";
	
	public static void main(String[] args) throws IOException, JSONException {
		
 		//read input.txt file
		File file = new File(System.class.getResource(INPUT_FILE).getFile());
		byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
		String jsonString = new String(encoded, Charset.defaultCharset());
 		
	
		RuleReasoner x = new RuleReasoner();
		//load ontology 
		x.loadModel(ONTOLOGY_NAME);
		//load user profile
		//x.createUserInstance(jsonString);
		
		//run nules and create a new model
		Model infModel = x.runRules(RULES_FILE);
		JsonObject pref = x.getUserPreferences(infModel);
		
		JSONObject userProfile = new JSONObject(jsonString);
		System.out.println(x.updateUserPreferences(userProfile,pref).toString(4));
	}
}
