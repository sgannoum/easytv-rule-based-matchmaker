package rules;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

import junit.framework.Assert;

public class InferenceTests {
	
	private static final String ONTOLOGY_DIR = "EasyTV.owl";
	private static final String STATMENETS_DIR = "statmenets";
	private static final String[] RULES_DIR =  new String[] {"ConditionalPreferencesRules.txt", "ContentAdaptationRules.txt", "SuggestionsRules.txt"};

	private RuleReasoner ruleReasoner;
	

	public static final JSONObject userProfile2 = new JSONObject("{"
			+ "\"user_id\":1," +
			"user_context: {" + 
			"    \"http://registry.easytv.eu/context/time\": \"10:00:00\" ," + 
			"    \"http://registry.easytv.eu/context/location\": \"ca\"" + 
			"}, "
			+ "\"user_profile\":{" + 
			"            \"user_preferences\": {" + 
			"                \"default\": {" + 
			"                    \"preferences\": {" + 
			"                        \"http://registry.easytv.eu/common/volume\": 75," + 
			"                        \"http://registry.easytv.eu/common/contrast\": 100," + 
			"                        \"http://registry.easytv.eu/application/control/voice\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 10," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 1," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"male\"," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/shelf/frequency\": 35," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/shelf/gain\": 15," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/pass/frequency\": 80," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.5," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/high/pass/frequency\": 900," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/high/pass/qFactor\": 0.8," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/high/shelf/frequency\": 2200," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/high/shelf/gain\": 15," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 80," + 
			"                        \"http://registry.easytv.eu/common/content/audio/language\": \"ca\"," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": false," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": false," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": false," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/size\": 2.0," + 
			"       				 \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 75,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false," + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\"," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": false," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"image-magnification\"," +
			"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2.0," + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1" + 
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
	
	
	@BeforeMethod
	public void beforeMethod() throws IOException {
		ruleReasoner = new RuleReasoner(ONTOLOGY_DIR, RULES_DIR);
	}
	
	@Test
	public void Test_inference() throws JSONException, IOException, UserProfileParsingException {
		//before inference
		JSONObject preferences = userProfile2.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default").getJSONObject("preferences");
		//Assert.assertEquals(33, preferences.getInt("http://registry.easytv.eu/common/volume"));

		
		OntProfile profile = new OntProfile(userProfile2);

 
		//after inference
		JSONObject actual = ruleReasoner.infer(profile);
		System.out.println(actual.toString(4));
		preferences = actual.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default").getJSONObject("preferences");

		Assert.assertEquals(22, preferences.getInt("http://registry.easytv.eu/common/volume"));
	}
	
	
}
