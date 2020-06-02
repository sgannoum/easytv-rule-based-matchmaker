package rules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
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


public class RulesUtilsTests {
	
	Rule rule1 = Rule.parseRule(
			"[rule_1:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasContext ?cnxt)"+
			"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestionSet ?sugSet)" + 
			"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?var0)" + 
			"	GE(?var0, '0'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	LE(?var0, '4'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_character 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"   noValue(?pref <http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound> 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"   noValue(?pref <http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_text> 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"	makeTemp(?ruleSug)" + 
			"	makeTemp(?sugPref)" + 
			"	->	" + 
			"   (?sugSet http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestion ?ruleSug)" +
			"   (?ruleSug http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#RuleSuggestion)" +
			"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConfidence '0.5'^^http://www.w3.org/2001/XMLSchema#double)" +
			"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
			"   (?sugPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#SuggestedPreferences)" +
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_text 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	JSONObject jsonRule1 = new JSONObject("{" + 
			" \"name\": \"rule_1\"," + 
			" \"confidence\": 0.5," + 
			" \"head\": [{" + 
			"  \"args\": [{" + 
			"   \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"   \"value\": true" + 
			"  }]," + 
			"  \"preference\": \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\"," + 
			"  \"builtin\": \"EQ\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"    \"value\": true" + 
			"   }]," + 
			"   \"preference\": \"http://registry.easytv.eu/application/cs/accessibility/detection/text\"," + 
			"   \"builtin\": \"EQ\"" + 
			" }]," + 
			" \"body\": [" + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 0" + 
			"   }]," + 
			"   \"preference\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"builtin\": \"GE\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 4" + 
			"   }]," + 
			"   \"preference\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"builtin\": \"LE\"" + 
			"  }," +
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"    \"value\": true" + 
			"   }]," + 
			"   \"preference\": \"http://registry.easytv.eu/application/cs/accessibility/detection/character\"," + 
			"   \"builtin\": \"EQ\"" + 
			"  }" +
			" ]" + 
			"}" );
	
	
	Rule rule2 = Rule.parseRule(
			"[rule_1:" + 
			" 	(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			" 	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)" + 
			"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasContext ?cnxt)"+
			"	(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestionSet ?sugSet)" + 
			"	(?pref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_common_volume ?var0)" + 
			"	GE(?var0, '0'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	LE(?var0, '4'^^http://www.w3.org/2001/XMLSchema#integer)" + 
			"	(?cnxt http://www.owl-ontologies.com/OntologyEasyTV.owl#has_context_light '2'^^http://www.w3.org/2001/XMLSchema#integer)"+	
			"   noValue(?pref <http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound> 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"	makeTemp(?ruleSug)" + 
			"	makeTemp(?sugPref)" + 
			"	->	" + 
			"   (?sugSet http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestion ?ruleSug)" +
			"   (?ruleSug http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#RuleSuggestion)" +
			"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConfidence '0.5'^^http://www.w3.org/2001/XMLSchema#double)" +
			"   (?ruleSug http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" +
			"   (?sugPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#SuggestedPreferences)" +
			"	(?sugPref http://www.owl-ontologies.com/OntologyEasyTV.owl#has_application_cs_accessibility_detection_sound 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" + 
			"]	");
	
	JSONObject jsonRule2 = new JSONObject("{" + 
			" \"name\": \"rule_1\"," +
			" \"confidence\": 0.5," + 
			" \"head\": [{" + 
			"  \"args\": [{" + 
			"   \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
			"   \"value\": true" + 
			"  }]," + 
			"  \"preference\": \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\"," + 
			"  \"builtin\": \"EQ\"" + 
			" }]," + 
			" \"body\": [" + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 0" + 
			"   }]," + 
			"   \"preference\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"builtin\": \"GE\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 4" + 
			"   }]," + 
			"   \"preference\": \"http://registry.easytv.eu/common/volume\"," + 
			"   \"builtin\": \"LE\"" + 
			"  }," + 
			"  {" + 
			"   \"args\": [{" + 
			"    \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
			"    \"value\": 2" + 
			"   }]," + 
			"   \"builtin\": \"EQ\"," + 
			"   \"preference\": \"http://registry.easytv.eu/context/light\"" + 
			"  }"+
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
	public void test_convert_rule_equality1() {
		Rule actual = RuleUtils.convert(jsonRule2);
		System.out.println(actual.toString());
		Assert.assertEquals(actual, rule2);
	}
	
	@Test
	public void test_convert_json_equality1() {
		JSONObject actual = RuleUtils.convert(rule1);
		Assert.assertTrue(jsonRule1.similar(actual));
	}
	
	@Test
	public void test_convert_rule_equality2() {
		Rule actual = RuleUtils.convert(jsonRule1);
		Assert.assertEquals(actual, rule1);
	}
	
	@Test
	public void test_convert_json_equality2() {
		JSONObject actual = RuleUtils.convert(rule2);
		Assert.assertTrue(jsonRule2.similar(actual));
	}
	
	@Test
	public void test_convert_all_suggestions_rules() throws IOException {
		
		List<Rule> rules = new ArrayList<Rule>();		
		rules.addAll(Rule.rulesFromURL(ClassLoader.getSystemResource("SuggestionsRules.txt").toString()));
		JSONArray actual = RuleUtils.convert(rules);
		System.out.println(actual.toString(4));
	}

}
