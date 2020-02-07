package rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.builtin.And;
import com.certh.iti.easytv.rbmm.builtin.Equals;
import com.certh.iti.easytv.rbmm.builtin.GreaterThan;
import com.certh.iti.easytv.rbmm.builtin.GreaterThanEquals;
import com.certh.iti.easytv.rbmm.builtin.LessThan;
import com.certh.iti.easytv.rbmm.builtin.LessThanEquals;
import com.certh.iti.easytv.rbmm.builtin.NOT;
import com.certh.iti.easytv.rbmm.builtin.NotEquals;
import com.certh.iti.easytv.rbmm.builtin.OR;
import com.certh.iti.easytv.rbmm.rules.RuleUtils;

import junit.framework.Assert;


public class RulesUtilsTests {
	
	Rule rule1 = Rule.parseRule(
			"[rule_1:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
			"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?var0)" + 
			"	GE(?var0, '0'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	LE(?var0, '4'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_character 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"	->	" + 
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_text 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	JSONObject jsonRule1 = new JSONObject("{" + 
			" \"name\": \"rule_1\"," + 
			" \"head\": [{" + 
			"  \"args\": [{" + 
			"   \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"   \"value\": true" + 
			"  }]," + 
			"  \"prefence\": \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\"," + 
			"  \"type\": \"EQ\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"    \"value\": true" + 
			"   }]," + 
			"   \"prefence\": \"http://registry.easytv.eu/application/cs/accessibility/detection/text\"," + 
			"   \"type\": \"EQ\"" + 
			" }]," + 
			" \"body\": [" + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 0" + 
			"   }]," + 
			"   \"prefence\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"type\": \"GE\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 4" + 
			"   }]," + 
			"   \"prefence\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"type\": \"LE\"" + 
			"  }," +
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"    \"value\": true" + 
			"   }]," + 
			"   \"prefence\": \"http://registry.easytv.eu/application/cs/accessibility/detection/character\"," + 
			"   \"type\": \"EQ\"" + 
			"  }" +
			" ]" + 
			"}" );
	
	
	Rule rule2 = Rule.parseRule(
			"[rule_1:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
			"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?var0)" + 
			"	GE(?var0, '0'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	LE(?var0, '4'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	->	" + 
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	JSONObject jsonRule2 = new JSONObject("{" + 
			" \"name\": \"rule_1\"," + 
			" \"head\": [{" + 
			"  \"args\": [{" + 
			"   \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"   \"value\": true" + 
			"  }]," + 
			"  \"prefence\": \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\"," + 
			"  \"type\": \"EQ\"" + 
			" }]," + 
			" \"body\": [" + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 0" + 
			"   }]," + 
			"   \"prefence\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"type\": \"GE\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 4" + 
			"   }]," + 
			"   \"prefence\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"type\": \"LE\"" + 
			"  }" + 
			" ]" + 
			"}" );
	
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		BuiltinRegistry.theRegistry.register("NE", new NotEquals());
		BuiltinRegistry.theRegistry.register("EQ", new Equals());
		BuiltinRegistry.theRegistry.register("GT", new GreaterThan());
		BuiltinRegistry.theRegistry.register("GE", new GreaterThanEquals());
		BuiltinRegistry.theRegistry.register("LT", new LessThan());
		BuiltinRegistry.theRegistry.register("LE", new LessThanEquals());
		BuiltinRegistry.theRegistry.register("and", new And());
		BuiltinRegistry.theRegistry.register("or", new OR());
		BuiltinRegistry.theRegistry.register("not", new NOT());
				
		System.out.println("Ontology was loaded");
	}
	
	@Test
	public void test_convert_toJson_1() {
		
		Rule rule_1 = Rule.parseRule(
				"[Font_size_suggestion_rule_3:" + 
				" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
				" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
				" 	" + 
				"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_enhancement_image_type 'image-magnification'^^http://www.w3.org/2001/XMLSchema#string)" + 
				"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_magnification_scale ?scale)" + 
				"	NE(?scale, '1.5'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	EQ(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	GT(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	GE(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	LT(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	LE(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	and(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	or(?scale, '2.0'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_ui_text_size ?ui_text_size)" + 
				"	NE(?ui_text_size,  '1.5'^^http://www.w3.org/2001/XMLSchema#double)" + 
				"" + 
				" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
				"" + 
				"	->	" + 
				"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_cc_subtitles_font_size '15'^^http://www.w3.org/2001/XMLSchema#integer)	" + 
				"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_ui_text_size '15'^^http://www.w3.org/2001/XMLSchema#string)	" + 
				"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_ui_text_size ?var)	" +
				"]	");
		
		
		JSONObject jsonRule_1 = RuleUtils.convert(rule_1);
		System.out.println(jsonRule_1.toString(1));
	}
	
	@Test
	public void test_convert_toJson_2() {	
		
		Rule rule_2 = Rule.parseRule(
				"[Avatar_suggestion_rule:" + 
				" 	(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
				" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
				" 		" + 
				" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_sign_language 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
				" 	" + 
				" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?volume)" + 
				" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_tts_audio_volume ?tts_volume)" + 
				" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_cc_audio_subtitle 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
				" 	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_ui_audioAssistanceBasedOnTTS 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
				"  	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_audio_description 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
				" " + 
				" 	GE(?volume, '75'^^http://www.w3.org/2001/XMLSchema#integer)" + 
				" 	GE(?tts_volume, '75'^^http://www.w3.org/2001/XMLSchema#integer) 	 " + 
				"" + 
				" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
				" 	" + 
				"	->" + 
				"	" + 
				"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_sign_language 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
				"]	");
		
		JSONObject jsonRule_2 = RuleUtils.convert(rule_2);
		System.out.println(jsonRule_2.toString(1));
	}
	
	@Test
	public void test_convert_rule_equality1() {
		Rule actual = RuleUtils.convert(jsonRule2);
		Assert.assertEquals(rule2, actual);
	}
	
	@Test
	public void test_convert_json_equality1() {
		JSONObject actual = RuleUtils.convert(rule1);
		Assert.assertTrue(jsonRule1.similar(actual));
	}
	
	@Test
	public void test_convert_rule_equality2() {
		Rule actual = RuleUtils.convert(jsonRule1);
		Assert.assertEquals(rule1, actual);
	}
	
	@Test
	public void test_convert_json_equality2() {
		JSONObject actual = RuleUtils.convert(rule2);
		Assert.assertTrue(jsonRule2.similar(actual));
	}
	
	@Test
	public void test_convert_suggestions_rules() throws IOException {
		
		List<Rule> rules = new ArrayList<Rule>();
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader
							.getResource("SuggestionsRules.txt")
							.getFile());
		
		rules.addAll(Rule.rulesFromURL(file.getCanonicalPath()));
		JSONArray actual = RuleUtils.convert(rules);
		System.out.println(actual.toString(4));

	}

}
