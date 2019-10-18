package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.builtin.And;
import com.certh.iti.easytv.rbmm.builtin.Equals;
import com.certh.iti.easytv.rbmm.builtin.GreaterThan;
import com.certh.iti.easytv.rbmm.builtin.GreaterThanEquals;
import com.certh.iti.easytv.rbmm.builtin.LessThan;
import com.certh.iti.easytv.rbmm.builtin.LessThanEquals;
import com.certh.iti.easytv.rbmm.builtin.MergePreferences;
import com.certh.iti.easytv.rbmm.builtin.NOT;
import com.certh.iti.easytv.rbmm.builtin.NotEquals;
import com.certh.iti.easytv.rbmm.builtin.OR;
import com.certh.iti.easytv.rbmm.user.UserContext;
import com.certh.iti.easytv.rbmm.user.UserProfile;
import com.certh.iti.easytv.rbmm.user.preference.Preference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import comparatorOperand.EqualsRulesTest;
import comparatorOperand.GreaterThanEqualRulesTest;
import comparatorOperand.GreaterThanRulesTest;
import comparatorOperand.LessThanEqualRulesTest;
import comparatorOperand.LessThanRulesTest;
import comparatorOperand.NotEqualsRulesTest;
import config.RBMMTestConfig;
import junit.framework.Assert;
import logicalOperand.AndRulesTest;
import logicalOperand.NotRulesTest;
import logicalOperand.OrRulesTest;

public class UserTest {
	
	private OntModel model;
	public static final String 	suggestionRule =  
	"[font_size_suggestion:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User) " + 
			" (?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			" (?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasAccessibilityMagnificationScale ?mg)" + 
			" (?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
			" greaterThan(?mg, 1, ?res)	" + 
			"->" + 
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasCSUITestSize 60)" + 
	 "]";
	
	private String rules =  AndRulesTest.rules + OrRulesTest.rules + NotRulesTest.rules +
			EqualsRulesTest.rules + NotEqualsRulesTest.rules +
			GreaterThanRulesTest.rules + GreaterThanEqualRulesTest.rules +
			LessThanRulesTest.rules + LessThanEqualRulesTest.rules +
			ConditionsTest.rules + suggestionRule
			;
	
	
	public static final JSONObject UserContext1 = new JSONObject("{" + 
			"    \"http://registry.easytv.eu/context/time\": \"2019-05-30T09:47:47.619Z\" ," + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"" + 
			" }");
	
	public static final JSONObject UserContext2 = new JSONObject("{" + 
			"    \"http://registry.easytv.eu/context/time\": \"09:47:00\" ," + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"" +
			" }");
	
	public static final JSONObject UserContext3 = new JSONObject("{" + 
			"    \"http://registry.easytv.eu/context/time\": \"09:00:00\" ," + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"" + 
			" }");
	
	public static final JSONObject UserContext4 = new JSONObject("{" + 
			"    \"http://registry.easytv.eu/context/time\": \"09:47:00\" ," + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"" + 
			" }");
	
	public static final JSONObject userProfile1 = new JSONObject("{" + 
			"  \"user_preferences\": {" + 
			"    \"default\": {" + 
			"      \"preferences\": {" + 
"                        \"http://registry.easytv.eu/common/volume\": 36," + 
"                        \"http://registry.easytv.eu/common/contrast\": 100," + 
"                        \"http://registry.easytv.eu/application/control/voice\": true," + 
"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 27," + 
"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"," + 
"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 3," + 
"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/bass\": 10," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/mids\": -8," + 
"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 21," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/highs\": 5," + 
"                        \"http://registry.easytv.eu/common/content/audio/language\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": true," + 
"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": true," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"es\"," + 
"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1.5," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": true," + 
"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gaze_control\"," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"face-detection\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1.5"+
			"      }" + 
			"    }" + 
			"  }" + 
			"}");
	
	public static final JSONObject userProfile2 = new JSONObject("{" + 
			"  \"user_preferences\": {" + 
			"    \"default\": {" + 
			"      \"preferences\": {" + 
"                        \"http://registry.easytv.eu/common/volume\": 36," + 
"                        \"http://registry.easytv.eu/common/contrast\": 100," + 
"                        \"http://registry.easytv.eu/application/control/voice\": true," + 
"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 27," + 
"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"," + 
"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 3," + 
"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/bass\": 10," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/mids\": -8," + 
"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 21," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/highs\": 5," + 
"                        \"http://registry.easytv.eu/common/content/audio/language\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": true," + 
"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": true," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"es\"," + 
"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1.5," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": true," + 
"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gaze_control\"," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"face-detection\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1.5"+
			"      }" + 
			"    }," + 
			"    \"conditional\": [" + 
			"      {" + 
			"        \"name\": \"condition_1\"," + 
			"        \"preferences\": {" + 
			"          \"http://registry.easytv.eu/application/cs/audio/volume\": 10" + 
			"        }," + 
			"        \"conditions\": [" + 
			"          {" + 
			"            \"type\": \"and\"," + 
			"            \"operands\": [" + 
			"			  {" + 
			"				\"type\": \"gt\"," + 
			"				\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/time\"," + 
			"				  \"09:00:00\" " + 
			"				]" + 
			"			  }," + 
			"				{" + 
			"					\"type\": \"lt\"," + 
			"					\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/time\"," + 
			"				  \"12:47:00\" " + 
			"					]" + 
			"				}" + 
			"            ]" + 
			"          }" + 
			"        ]" + 
			"      }" + 
			"    ]"+
			"  }" + 
			"}");
	
	public static final JSONObject userProfile3 = new JSONObject("{" + 
			"  \"user_preferences\": {" + 
			"    \"default\": {" + 
			"      \"preferences\": {" + 
"                        \"http://registry.easytv.eu/common/volume\": 36," + 
"                        \"http://registry.easytv.eu/common/contrast\": 100," + 
"                        \"http://registry.easytv.eu/application/control/voice\": true," + 
"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 27," + 
"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"," + 
"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 3," + 
"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/bass\": 10," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/mids\": -8," + 
"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 21," + 
"                        \"http://registry.easytv.eu/application/cs/audio/eq/highs\": 5," + 
"                        \"http://registry.easytv.eu/common/content/audio/language\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
"                        \"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": true," + 
"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": true," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"es\"," + 
"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1.5," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": true," + 
"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gaze_control\"," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"face-detection\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"ca\"," + 
"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1.5"+
			"      }" + 
			"    }," + 
			"    \"conditional\": [" + 
			"      {" + 
			"        \"name\": \"condition_1\"," + 
			"        \"preferences\": {" + 
			"          \"http://registry.easytv.eu/common/volume\": 10" + 
			"        }," + 
			"        \"conditions\": [" + 
			"			  {" + 
			"				\"type\": \"gt\"," + 
			"				\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/time\"," + 
			"				  \"08:00:00\" " + 
			"				]" + 
			"			  }," + 
			"        ]" + 
			"      }" + 
			"    ]"+
			"  }" + 
			"}");
	
	public static final JSONObject userProfile4 = new JSONObject("{" + 
			"  \"user_preferences\": {" + 
			"    \"default\": {" + 
			"      \"preferences\": {" + 
			"                        \"http://registry.easytv.eu/common/volume\": 36," + 
			"                        \"http://registry.easytv.eu/common/contrast\": 100," + 
			"                        \"http://registry.easytv.eu/application/control/voice\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 27," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 3," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/bass\": 10," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/mids\": -8," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 21," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/highs\": 5," + 
			"                        \"http://registry.easytv.eu/common/content/audio/language\": \"ca\"," + 
			"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
			"                        \"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"ca\"," + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"es\"," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1.5," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": true," + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gaze_control\"," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"face-detection\"," + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"ca\"," + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1.5"+
			"      }" + 
			"    }," + 
			"    \"conditional\": [" + 
			"      {" + 
			"        \"name\": \"condition_1\"," + 
			"        \"preferences\": {" + 
			"          \"http://registry.easytv.eu/common/volume\": 10" + 
			"        }," + 
			"        \"conditions\": [" + 
			"          {" + 
			"            \"type\": \"and\"," + 
			"            \"operands\": [" + 
			"			  {" + 
			"				\"type\": \"gt\"," + 
			"				\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/time\"," + 
			"				  \"09:47:47\" " + 
			"				]" + 
			"			  }," + 
			"				{" + 
			"					\"type\": \"lt\"," + 
			"					\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/time\"," + 
			"				  \"09:47:47\" " + 
			"					]" + 
			"				}" + 
			"            ]" + 
			"          }" + 
			"        ]" + 
			"      }," + 
			"      {" + 
			"        \"name\": \"condition_2\"," + 
			"        \"preferences\": {" + 
			"          \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\":  \"#222222\"" + 
			"        }," + 
			"        \"conditions\": [" + 
			"          {" + 
			"            \"type\": \"and\"," + 
			"            \"operands\": [" + 
			"			  {" + 
			"				\"type\": \"eq\"," + 
			"				\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/time\"," + 
			"				  \"09:47:00\"" + 
			"				]" + 
			"			  }," + 
			"				{" + 
			"					\"type\": \"eq\"," + 
			"					\"operands\": [" + 
			"				  \"http://registry.easytv.eu/context/location\"," + 
			"				  \"fr\" " +  
			"					]" + 
			"				}" + 
			"            ]" + 
			"          }" + 
			"        ]" + 
			"      }" +
			"    ]"+
			"  }" + 
			"}");
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new NotEquals());
		BuiltinRegistry.theRegistry.register(new Equals());
		BuiltinRegistry.theRegistry.register(new GreaterThan());
		BuiltinRegistry.theRegistry.register(new GreaterThanEquals());
		BuiltinRegistry.theRegistry.register(new LessThan());
		BuiltinRegistry.theRegistry.register(new LessThanEquals());
		BuiltinRegistry.theRegistry.register(new And());
		BuiltinRegistry.theRegistry.register(new OR());
		BuiltinRegistry.theRegistry.register(new NOT());
		BuiltinRegistry.theRegistry.register(new MergePreferences());

		System.out.println("Ontology was loaded");

	}
	
	@Test
	public void TestUserMapper() 
	  throws JsonParseException, IOException {
	 
		UserProfile user = new UserProfile(userProfile2);
	 
		//System.out.println(user.getJSONObject().toString(4));
	    Assert.assertNotNull(user);
	}
	
	@Test
	public void TestUserInference() 
	  throws JsonParseException, IOException {
	 
		UserProfile user = new UserProfile(userProfile1);
		Individual userInstance = user.createOntologyInstance(model);
		
		//Add context to the model
		UserContext context = new UserContext(UserContext1);
		Individual contextIndividual = context.createOntologyInstance(model);
			
		Property hasContextAbility = model.getProperty(UserProfile.HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextAbility, contextIndividual);	
		
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasSuggestedPreferenceProperty = model.getProperty(UserProfile.HAS_SUGGESTED_PREFERENCES_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasSuggestedPreferenceProperty, (RDFNode)null);
		Resource userSuggestedPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userSuggestedPreferenceInstance, null, (RDFNode)null);
		Property hasFontSizeProperty = model.getProperty(Preference.hasCSUITestSize);
		 userPreferenceList = inf.listStatements(userSuggestedPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertEquals(60, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());

	}
	
	@Test
	public void TestUserInference1() 
	  throws JsonParseException, IOException {
	 
		//Add user profile to the model
		UserProfile user = new UserProfile(userProfile2);
		Individual userInstance = user.createOntologyInstance(model);
		
		//Add context to the model
		UserContext context = new UserContext(UserContext2);
		Individual contextIndividual = context.createOntologyInstance(model);
			
		Property hasContextAbility = model.getProperty(UserProfile.HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextAbility, contextIndividual);	
		
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasPreferenceProperty = model.getProperty(UserProfile.HAS_PREFERENCE_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasPreferenceProperty, (RDFNode)null);
		Resource userPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userPreferenceInstance, null, (RDFNode)null);
		Property hasAudioVolumeProperty = model.getProperty(Preference.hasCSAudioVolume);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(10, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());
		
		Property hasFontSizeProperty = model.getProperty(Preference.hasCSUITestSize);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertEquals(20, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());

	}

	
	@Test
	public void TestUserInference2() 
	  throws JsonParseException, IOException {
	 
		UserProfile user = new UserProfile(userProfile3);
		Individual userInstance = user.createOntologyInstance(model);
		
		//Add context to the model
		UserContext context = new UserContext(UserContext3);
		Individual contextIndividual = context.createOntologyInstance(model);
			
		Property hasContextAbility = model.getProperty(UserProfile.HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextAbility, contextIndividual);	
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasPreferenceProperty = model.getProperty(UserProfile.HAS_PREFERENCE_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasPreferenceProperty, (RDFNode)null);
		Resource userPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userPreferenceInstance, null, (RDFNode)null);
		Property hasAudioVolumeProperty = model.getProperty(Preference.hasVolume);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(10, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());

	}
	
	@Test
	public void TestUserInference3() 
	  throws JsonParseException, IOException {
			
		UserProfile user = new UserProfile(userProfile4);
		Individual userInstance = user.createOntologyInstance(model);

		//Add context to the model
		UserContext context = new UserContext(UserContext4);
		Individual contextIndividual = context.createOntologyInstance(model);
			
		Property hasContextAbility = model.getProperty(UserProfile.HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextAbility, contextIndividual);	
		
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasPreferenceProperty = model.getProperty(UserProfile.HAS_PREFERENCE_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasPreferenceProperty, (RDFNode)null);
		Resource userPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userPreferenceInstance, null, (RDFNode)null);
		Property hasAudioVolumeProperty = model.getProperty(Preference.hasVolume);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(36, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());
		
		Property hasBackgroundProperty = model.getProperty(Preference.hasDisplayCursorColor);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasBackgroundProperty, (RDFNode)null);
		Assert.assertEquals("#222222", userPreferenceList.next().getObject().asLiteral().getString());
		Assert.assertFalse(userPreferenceList.hasNext());
	}

}
