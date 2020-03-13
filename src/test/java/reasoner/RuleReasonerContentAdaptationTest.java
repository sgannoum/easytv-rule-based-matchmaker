package reasoner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class RuleReasonerContentAdaptationTest {
	
	private static final String ONTOLOGY_DIR = "EasyTV.owl";
	private static final String[] RULES_DIR =  new String[] {"ConditionalPreferencesRules.txt", "ContentAdaptationRules.txt"};

	private RuleReasoner ruleReasoner;
	Rule flat_match = Rule.parseRule( 
			"[flat_match:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
			"	(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    " 	(?pref ?perdicate 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    "	(?content ?perdicate 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"	->" + 
			"	(?sugPref ?perdicate 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	Rule subtitution_match = Rule.parseRule( 
			"[flat_match:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
			"	(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    " 	(?pref ?perdicate 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    "	(?content ?perdicate 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" +

			"	->" + 
			"	(?sugPref ?perdicate 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	
	@BeforeTest
	public void before_test() throws IOException, JSONException, UserProfileParsingException {
		List<Rule> suggestionRules = new ArrayList<Rule>();
		suggestionRules.add(flat_match);
		suggestionRules.add(subtitution_match);
		
		ruleReasoner = new RuleReasoner(ONTOLOGY_DIR, suggestionRules, RULES_DIR);		
	}
	
	
	@Test
	public void test_infer_content_adaptation_flat_match() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": false," + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": false," + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": false," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"ca\"]" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"    					\"http://registry.easytv.eu/application/cs/accessibility/detection/face\": false," + 
				"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true" + 
				"                    }" + 
				"                }" + 
				"            }" +
				"  		   }" + 
				"        }");
		
		JSONObject expected = new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {" + 
				"        \"default\": {\"preferences\": {}}," + 
				"        \"recommendations\": {\"preferences\": {}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.infer(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
	
}
