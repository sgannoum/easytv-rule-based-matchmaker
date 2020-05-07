package reasoner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.rbmm.webservice.RBMM_config;
import com.certh.iti.easytv.user.UserContent;
import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

public class RuleReasonerTest {
	
	private static final String ONTOLOGY_DIR = "EasyTV.owl";
	private static final String[] RULES_DIR =  new String[] {"ConditionalPreferencesRules.txt", "ContentAdaptationRules.txt"};

	private RuleReasoner ruleReasoner;
	Rule soun_detection_suggestion_rule = Rule.parseRule( 
			"[soun_detection_suggestion_rule:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
			" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?volume)" + 
			" 	GE(?volume, '0'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			" 	LE(?volume, '10'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	->" + 
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	
	@BeforeTest
	public void before_test() throws IOException, JSONException, UserProfileParsingException {
		List<Rule> suggestionRules = new ArrayList<Rule>();
		suggestionRules.add(soun_detection_suggestion_rule);
		
		ruleReasoner = new RuleReasoner(ONTOLOGY_DIR, suggestionRules, RULES_DIR);		
	}
	
	@Test
	public void test_infer_sound_detection_true() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/time\": \"10:00:00\" ," + 
				"    \"http://registry.easytv.eu/context/location\": \"ca\"" + 
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
				"        \"recommendations\": {\"preferences\": {\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.infer(new OntProfile(profile1));	
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_infer_sound_detection_false_1() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/time\": \"10:00:00\" ," + 
				"    \"http://registry.easytv.eu/context/location\": \"ca\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 11," + 
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
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_infer_sound_detection_false_2() throws IOException, JSONException, UserProfileParsingException {
	
		JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/time\": \"10:00:00\" ," + 
				"    \"http://registry.easytv.eu/context/location\": \"ca\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 5," + 
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
	
	
	//@Test
	public void test_redefine_ontology() throws IOException {
		
		ruleReasoner = new RuleReasoner(RBMM_config.ONTOLOGY_NAME, RBMM_config.RULES_FILE);
		OntModel ontModel = ruleReasoner.getOntModel();
		OntClass userPreferences = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#UserPreferences");
		OntClass userContext = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#UserContext");
		OntClass content = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#Content");

		//add preference predicates
		for(Entry<String, Attribute> entry :  Preference.getAttributes().entrySet()) {
			Attribute value = entry.getValue();
			String key = entry.getKey();
			String uri =  OntPreference.getPredicate(key);
			OntProperty propertyImple =  ontModel.createOntProperty(uri);
			Resource range = ontModel.getResource(value.getXMLDataTypeURI());
			
			propertyImple.addDomain(userPreferences);
			propertyImple.setRange(range);
			
			System.out.println(propertyImple);
		}
		
		//add context predicates
		for(Entry<String, Attribute> entry :  UserContext.getAttributes().entrySet()) {
			Attribute value = entry.getValue();
			String key = entry.getKey();
			String uri =  OntUserContext.getPredicate(key);
			OntProperty propertyImple =  ontModel.createOntProperty(uri);
			Resource range = ontModel.getResource(value.getXMLDataTypeURI());
			
			propertyImple.addDomain(userContext);
			propertyImple.setRange(range);
			
			System.out.println(propertyImple);
		}
		
		//add content
		for(Entry<String, Attribute> entry :  UserContent.getAttributes().entrySet()) {
			Attribute value = entry.getValue();
			String key = entry.getKey();
			String uri =  Content.getPredicate(key);
			OntProperty propertyImple =  ontModel.createOntProperty(uri);
			Resource range = ontModel.getResource(value.getXMLDataTypeURI());
			
			propertyImple.addDomain(content);
			propertyImple.setRange(range);
			
			System.out.println(propertyImple);
		}
		
		File file = new File("C:\\Users\\salgan\\Desktop\\EasyTV.owl");
		FileOutputStream out = new FileOutputStream(file);
		ontModel.write(out);
	}
	
	
}
