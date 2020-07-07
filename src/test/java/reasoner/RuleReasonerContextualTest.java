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
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class RuleReasonerContextualTest {
	
	private static final String ONTOLOGY_DIR = "EasyTV.owl";
	private static final String[] RULES_DIR =  new String[] {"ConditionalPreferencesRules.txt", "ContentAdaptationRules.txt"};

	private RuleReasoner ruleReasoner;
	
	List<Rule> suggestionRules = new ArrayList<Rule>() {
		private static final long serialVersionUID = 1L;
		{
			add(Rule.parseRule( 
					"[soun_detection_suggestion_rule:" + 
							" 	(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
							" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
							"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasContext ?cnxt)"+
							" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestionSet ?sugSet)" + 
							" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
							" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?volume)" + 
							" 	GE(?volume, '15'^^http://www.w3.org/2001/XMLSchema#integer)" + 
							" 	LE(?volume, '20'^^http://www.w3.org/2001/XMLSchema#integer)" + 
							"	(?cnxt http://www.owl-ontologies.com/OntologyEasyTV.owl#has_context_light 'dark'^^http://www.w3.org/2001/XMLSchema#string)"+	
							"	makeTemp(?ruleSug)" + 
							"	makeTemp(?sugPref)" + 
							"	->	" + 
							"   (?sugSet http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestion ?ruleSug)" +
							"   (?ruleSug http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#RuleSuggestion)" +
							"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConfidence '0.7'^^http://www.w3.org/2001/XMLSchema#double)" +
							"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
							"   (?sugPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#SuggestedPreferences)" +
							"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
							"]	"));
			
			add(Rule.parseRule( 
							"[Ambient_sound_breathing_suggestions:" + 
							" 	(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
							" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
							"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasContext ?cnxt)"+
							" 	(?cnxt http://www.owl-ontologies.com/OntologyEasyTV.owl#has_context_device_soundMeter ?ambientSound)" + 
							" 	EQ(?ambientSound, 'Breathing'^^http://www.w3.org/2001/XMLSchema#string)" + 
							" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestionSet ?sugSet)" +
							"	makeTemp(?ruleSug)" + 
							"	makeTemp(?sugPref)" + 
							"	->" + 
							"   (?sugSet http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestion ?ruleSug)" +
							"   (?ruleSug http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#RuleSuggestion)" +
							"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConfidence '0.8'^^http://www.w3.org/2001/XMLSchema#double)" +
							"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
							"   (?sugPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#SuggestedPreferences)" +
							"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume '10'^^http://www.w3.org/2001/XMLSchema#integer)" +  
							"	print('Suggest volume level', 10, 'with confidence', 0.8)" + 
							"]		"));
			
			add(Rule.parseRule( 
							"[Ambient_sound_breathing_suggestions_higher:" + 
							" 	(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
							" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
							"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasContext ?cnxt)"+
							" 	(?cnxt http://www.owl-ontologies.com/OntologyEasyTV.owl#has_context_device_soundMeter ?ambientSound)" + 
							" 	EQ(?ambientSound, 'Breathing'^^http://www.w3.org/2001/XMLSchema#string)" + 
							" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestionSet ?sugSet)" +
							"	makeTemp(?ruleSug)" + 
							"	makeTemp(?sugPref)" + 
							"	->" + 
							"   (?sugSet http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestion ?ruleSug)" +
							"   (?ruleSug http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#RuleSuggestion)" +
							"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConfidence '0.9'^^http://www.w3.org/2001/XMLSchema#double)" +
							"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
							"   (?sugPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#SuggestedPreferences)" +
							"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume '20'^^http://www.w3.org/2001/XMLSchema#integer)" +  
							"	print('Suggest volume level', 20, 'with confidence', 0.9)" + 
							"]		"));
		
		}
		
	};
	
	@BeforeTest
	public void before_test() throws IOException, JSONException, UserProfileParsingException {
		ruleReasoner = new RuleReasoner(ONTOLOGY_DIR, suggestionRules, RULES_DIR);		
	}
	
	@Test
	public void test_contextual_infer_ambinetLight() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/device/soundMeter\": \"Breathing\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false" + 
				"                    }" + 
				"                }" + 
				"            }" +
				"  		   }" + 
				"        }");
		
		JSONObject expected = new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {" + 
				"        \"default\": {\"preferences\": {}}," + 
				"        \"recommendations\": {\"preferences\": {\"http://registry.easytv.eu/common/volume\": 20}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.infer(new OntProfile(profile1));	
		Assert.assertTrue(expected.similar(actual), actual.toString(4));
	}
	
	@Test
	public void test_contextual_infer_sound_detection_true() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/light\": \"dark\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 15," + 
				"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false" + 
				"                    }" + 
				"                }" + 
				"            }" +
				"  		   }" + 
				"        }");
		
		JSONObject expected = new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {" + 
				"        \"default\": {\"preferences\": {}}," + 
				"        \"recommendations\": {\"preferences\": {\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.infer(new OntProfile(profile1));	
		Assert.assertTrue(expected.similar(actual), actual.toString(4));
	}
	
	@Test
	public void test_contextual_infer_sound_detection_false() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/light\": \"home\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 15," + 
				"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false" + 
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
		Assert.assertTrue(expected.similar(actual), actual.toString(4));
	}
	
	@Test
	public void test_contextual_infer_sound_detection_false_1() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/light\": \"theater\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 5," + 
				"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false" + 
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
		Assert.assertTrue(expected.similar(actual), actual.toString(4));
	}
	
}
