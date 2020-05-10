package reasoner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class RuleReasonerContentAdaptationTest {
	
	private static final String ONTOLOGY_DIR = "EasyTV.owl";
	private static final String[] RULES_DIR =  new String[] {"ConditionalPreferencesRules.txt", "ContentAdaptationRules.txt"};

	private RuleReasoner ruleReasoner;
	
	@BeforeClass
	public void before_test() throws IOException, JSONException, UserProfileParsingException {
		List<Rule> suggestionRules = new ArrayList<Rule>();
		
		ruleReasoner = new RuleReasoner(ONTOLOGY_DIR, suggestionRules, RULES_DIR);		
	}
	
	
	@Test
	public void test_infer_content_adaptation_flat_match() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{" +
				"user_id:1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"ca\"]" + 
				"}, "+
				"user_profile:{" + 
				"            user_preferences: {" + 
				"                default: {" + 
				"                    preferences: {" + 
				"                       \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true" + 
				" }}}}}");
		
		JSONObject expected = new JSONObject("{" + 
				"    user_id: 1," + 
				"    user_profile: {user_preferences: {" + 
				"        default: {preferences: {}}," + 
				"        recommendations: {preferences: {\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.inferContentSuggestions(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_infer_content_adaptation_substitution_match() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"+
				"user_id:1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": false," + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"ca\"]" + 
				"}, " +
				" user_profile:{" + 
				"            user_preferences: {" + 
				"                default: {" + 
				"                    preferences: {" + 
				"    					\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true" + 
				" }}}}}");
		
		JSONObject expected = new JSONObject("{" + 
				"    user_id: 1," + 
				"    user_profile: {user_preferences: {" + 
				"        default: {preferences: {}}," + 
				"        recommendations: {preferences: {\"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.inferContentSuggestions(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_content_adaptation_language_flat_match() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"+
				"user_id: 1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\",\"es\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"ca\",\"es\"]" + 
				"}, " +
				"user_profile:{" + 
				"            user_preferences: {" + 
				"                default: {" + 
				"                    preferences: {" + 
				"    					\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"" + 
				" }}}}}");
		
		JSONObject expected = new JSONObject("{" + 
				"    user_id: 1," + 
				"    user_profile: {user_preferences: {" + 
				"        default: {preferences: {}}," + 
				"        recommendations: {preferences: {\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.inferContentSuggestions(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_content_adaptation_language_substitution_match() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"+
				"user_id: 1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\",\"es\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"es\"]" + 
				"}, "
				+ "user_profile:{" + 
				"            user_preferences: {" + 
				"                default: {" + 
				"                    preferences: {" + 
				"    					\"http://registry.easytv.eu/application/cs/audio/track\": \"ca\"" + 
				" }}}}}");
		
		JSONObject expected = new JSONObject("{" + 
				"    user_id: 1," + 
				"    user_profile: {user_preferences: {" + 
				"        default: {preferences: {}}," + 
				"        recommendations: {preferences: {" +
				"    			\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"," + 
				"    			\"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": true" + 
				"}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.inferContentSuggestions(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_content_adaptation_no_match() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"+
				"user_id: 1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\",\"es\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"es\"]" + 
				"}, "+
				"user_profile:{" + 
				"            user_preferences: {" + 
				"                default: {" + 
				"                    preferences: {" + 
				"    					\"http://registry.easytv.eu/application/cs/audio/track\": \"gr\"," + 
				"        				\"http://registry.easytv.eu/application/control/voice\": true,\r\n" + 
				" }}}}}");
		
		JSONObject expected = new JSONObject("{" + 
				"    user_id: 1," + 
				"    user_profile: {user_preferences: {" + 
				"        default: {preferences: {}}," + 
				"        recommendations: {preferences: {}}" + 
				"    }}}");
		
		JSONObject actual = ruleReasoner.inferContentSuggestions(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_content_adaptation_complex_example() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"+
				"user_id: 1," +
				"user_content: {" + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": false," + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," +
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
				"    \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
				"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"es\"]," + 
				"    \"http://registry.easytv.eu/application/cs/audio/track\": [\"ca\",\"es\"]" + 
				"}, "+
				"user_profile:{" + 
				"            user_preferences: {" + 
				"                default: {" + 
				"                    preferences: {" + 
				"    					\"http://registry.easytv.eu/application/cs/audio/track\": \"gr\"," + 
				"    					\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"," + 
				"    					\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 	
				"                       \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
				"    					\"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true" + 
				" }}}}}");
		
		JSONObject expected = new JSONObject("{" + 
				"    user_id: 1," + 
				"    user_profile: { user_preferences: {" + 
				"        default: { preferences: {}}," + 
				"        recommendations: { preferences: {" +
				"    			\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," +
				"    			\"http://registry.easytv.eu/application/cs/audio/track\": \"ca\"" + 
				"    }}}}}");
		
		JSONObject actual = ruleReasoner.inferContentSuggestions(new OntProfile(profile1));				
		Assert.assertTrue(expected.similar(actual));
	}
	
}
