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

public class RuleReasonerConditionalTest {
	
	private static final String ONTOLOGY_DIR = "EasyTV.owl";
	private static final String[] RULES_DIR =  new String[] {"ConditionalPreferencesRules.txt", "ContentAdaptationRules.txt"};

	private RuleReasoner ruleReasoner;
	
	@BeforeTest
	public void before_test() throws IOException, JSONException, UserProfileParsingException {
		List<Rule> suggestionRules = new ArrayList<Rule>();
		
		ruleReasoner = new RuleReasoner(ONTOLOGY_DIR, suggestionRules, RULES_DIR);		
	}
	
	@Test
	public void test_infer_conditional_preferences_true() throws IOException, JSONException, UserProfileParsingException {
	
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
				"                        \"http://registry.easytv.eu/common/volume\": 75" + 
				"                    }" + 
				"                }," + 
				"                \"conditional\": [" + 
				"                    {" + 
				"                        \"name\": \"conditional preference 1\"," + 
				"                        \"conditions\": [" + 
				"                            {" + 
				"                                \"type\": \"and\"," + 
				"                                \"operands\": [" + 
				"                                    {" + 
				"                                        \"type\": \"ge\"," + 
				"                                        \"operands\": [" + 
				"                                            \"http://registry.easytv.eu/context/time\"," + 
				"                                            \"09:00:00\"" + 
				"                                        ]" + 
				"                                    }," + 
				"                                    {" + 
				"                                        \"type\": \"le\"," + 
				"                                        \"operands\": [" + 
				"                                            \"http://registry.easytv.eu/context/time\"," + 
				"                                            \"15:00:00\"" + 
				"                                        ]" + 
				"                                    }" + 
				"                                ]" + 
				"                            }" + 
				"                        ]," + 
				"                        \"preferences\": {" + 
				"                            \"http://registry.easytv.eu/common/volume\": 22" + 
				"                        }" + 
				"                    }" + 
				"                ]" + 
				"            }" +
				"  }" + 
				"        }");
		
		JSONObject expected = new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {" + 
				"        \"default\": {\"preferences\": {\"http://registry.easytv.eu/common/volume\": 22}}," + 
				"        \"recommendations\": {\"preferences\": {}}" + 
				"    }}" + 
				"}");
		
		JSONObject actual = ruleReasoner.infer(new OntProfile(profile1));		
		Assert.assertTrue(expected.similar(actual));
	}
	
	@Test
	public void test_infer_conditional_preferences_false() throws IOException, JSONException, UserProfileParsingException {
	
		 JSONObject profile1 = new JSONObject("{"
				+ "\"user_id\":1," +
				"user_context: {" + 
				"    \"http://registry.easytv.eu/context/time\": \"18:00:00\" ," + 
				"    \"http://registry.easytv.eu/context/location\": \"ca\"" + 
				"}, "
				+ "\"user_profile\":{" + 
				"            \"user_preferences\": {" + 
				"                \"default\": {" + 
				"                    \"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 75" + 
				"                    }" + 
				"                }," + 
				"                \"conditional\": [" + 
				"                    {" + 
				"                        \"name\": \"conditional preference 1\"," + 
				"                        \"conditions\": [" + 
				"                            {" + 
				"                                \"type\": \"and\"," + 
				"                                \"operands\": [" + 
				"                                    {" + 
				"                                        \"type\": \"ge\"," + 
				"                                        \"operands\": [" + 
				"                                            \"http://registry.easytv.eu/context/time\"," + 
				"                                            \"09:00:00\"" + 
				"                                        ]" + 
				"                                    }," + 
				"                                    {" + 
				"                                        \"type\": \"le\"," + 
				"                                        \"operands\": [" + 
				"                                            \"http://registry.easytv.eu/context/time\"," + 
				"                                            \"15:00:00\"" + 
				"                                        ]" + 
				"                                    }" + 
				"                                ]" + 
				"                            }" + 
				"                        ]," + 
				"                        \"preferences\": {" + 
				"                            \"http://registry.easytv.eu/common/volume\": 22" + 
				"                        }" + 
				"                    }" + 
				"                ]" + 
				"            }" +
				"  }" + 
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
