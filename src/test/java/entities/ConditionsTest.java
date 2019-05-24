package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import builtin.LessThan;
import builtin.MergePreferences;
import config.RBMMTestConfig;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

import junit.framework.Assert;
import rule_matchmaker.entities.Conditions;
import rule_matchmaker.entities.User;
import rule_matchmaker.entities.UserPreference;

public class ConditionsTest {
	
	/*	public static final String rules = "[conditional_preference:" + 
	" (?condPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ConditionalPreference)" + 
	",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConditions ?cond)" + 
	",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+User.ONTOLOGY_CLASS_URI+")" + 
    ",(?user "+User.PREFERENCE_PROP+" ?defPref)" + 
	"->" + 			
    
	" 	[(?defPref "+UserPreference.AUDIO_VOLUME_PROP+" ?audioVolume) <- (?condPref "+UserPreference.AUDIO_VOLUME_PROP+" ?audioVolume)]" +
	" 	[(?defPref "+UserPreference.AUDIO_LANGUAGE_PROP+" ?audioLanguage) <- (?condPref "+UserPreference.AUDIO_LANGUAGE_PROP+" ?audioLanguage)]" +
	" 	[(?defPref "+UserPreference.BACKGROUND_PROP+" ?background) <- (?condPref "+UserPreference.BACKGROUND_PROP+" ?background)]" + 
	
	" 	[(?defPref "+UserPreference.FONT_CONTRAST_PROP+" ?fontContrast) <- (?condPref "+UserPreference.FONT_CONTRAST_PROP+" ?fontContrast)]" +
	" 	[(?defPref "+UserPreference.FONT_SIZE_PROP+" ?fontSize) <- (?condPref "+UserPreference.FONT_SIZE_PROP+" ?fontSize)]" + 
	
	" 	[(?defPref "+UserPreference.FONT_COLOR_PROP+" ?fontColor) <- (?condPref "+UserPreference.FONT_COLOR_PROP+" ?fontColor)]" + 
	" 	[(?defPref "+UserPreference.FONT_TYPE_PROP+" ?fontType) <- (?condPref "+UserPreference.FONT_TYPE_PROP+" ?fontType)]" +
	
	" 	[(?defPref "+UserPreference.CURSOR_COLOR_PROP+" ?cursorColor) <- (?condPref "+UserPreference.CURSOR_COLOR_PROP+" ?cursorColor)]" +
	" 	[(?defPref "+UserPreference.CURSOR_SIZE_PROP+" ?cursorSize) <- (?condPref "+UserPreference.CURSOR_SIZE_PROP+" ?cursorSize)]" + 
	" 	[(?defPref "+UserPreference.CURSOR_TRAILS_PROP+" ?cursorTrails) <- (?condPref "+UserPreference.CURSOR_TRAILS_PROP+" ?cursorTrails)]" + 
	
	" 	[(?defPref "+UserPreference.DICTATION_PROP+" ?dictation) <- (?condPref "+UserPreference.DICTATION_PROP+" ?dictation)]" + 
	" 	[(?defPref "+UserPreference.BRIGHTNESS_PROP+" ?brightness) <- (?condPref "+UserPreference.BRIGHTNESS_PROP+" ?brightness)]" + 
	" 	[(?defPref "+UserPreference.HIGHLIGHT_PROP+" ?highlight) <- (?condPref "+UserPreference.HIGHLIGHT_PROP+" ?highlight)]" + 
	" 	[(?defPref "+UserPreference.SPEECH_RATE_PROP+" ?speechRate) <- (?condPref "+UserPreference.SPEECH_RATE_PROP+" ?speechRate)]" + 
	" 	[(?defPref "+UserPreference.SCREEN_READER_PROP+" ?screenReader) <- (?condPref "+UserPreference.SCREEN_READER_PROP+" ?screenReader)]" + 
	" 	[(?defPref "+UserPreference.STYLE_PROP+" ?style) <- (?condPref "+UserPreference.STYLE_PROP+" ?style)]" + 

	"]"
	;
*/


/*	public static final String rules = "[conditional_preference:" + 
	" (?condPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ConditionalPreference)" + 
	",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConditions ?cond)" + 
	",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+User.ONTOLOGY_CLASS_URI+")" + 
    ",(?user "+User.PREFERENCE_PROP+" ?defPref)" + 
    ",makeSkolem(?X, ?defPref)" + 
	"->" + 			
	" 	(?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#UserPreferences)" +  
	
	" 	[(?X "+UserPreference.AUDIO_VOLUME_PROP+" ?audioVolume) <- (?condPref "+UserPreference.AUDIO_VOLUME_PROP+" ?audioVolume)]" +
	" 	[(?X "+UserPreference.AUDIO_LANGUAGE_PROP+" ?audioLanguage) <- (?condPref "+UserPreference.AUDIO_LANGUAGE_PROP+" ?audioLanguage)]" +
	" 	[(?X "+UserPreference.BACKGROUND_PROP+" ?background) <- (?condPref "+UserPreference.BACKGROUND_PROP+" ?background)]" + 
	
	" 	[(?X "+UserPreference.FONT_CONTRAST_PROP+" ?fontContrast) <- (?condPref "+UserPreference.FONT_CONTRAST_PROP+" ?fontContrast)]" +
	" 	[(?X "+UserPreference.FONT_SIZE_PROP+" ?fontSize) <- (?condPref "+UserPreference.FONT_SIZE_PROP+" ?fontSize)]" + 
	" 	[(?X "+UserPreference.FONT_COLOR_PROP+" ?fontColor) <- (?condPref "+UserPreference.FONT_COLOR_PROP+" ?fontColor)]" + 
	" 	[(?X "+UserPreference.FONT_TYPE_PROP+" ?fontType) <- (?condPref "+UserPreference.FONT_TYPE_PROP+" ?fontType)]" +
	
	" 	[(?X "+UserPreference.CURSOR_COLOR_PROP+" ?cursorColor) <- (?condPref "+UserPreference.CURSOR_COLOR_PROP+" ?cursorColor)]" +
	" 	[(?X "+UserPreference.CURSOR_SIZE_PROP+" ?cursorSize) <- (?condPref "+UserPreference.CURSOR_SIZE_PROP+" ?cursorSize)]" + 
	" 	[(?X "+UserPreference.CURSOR_TRAILS_PROP+" ?cursorTrails) <- (?condPref "+UserPreference.CURSOR_TRAILS_PROP+" ?cursorTrails)]" + 
	
	" 	[(?X "+UserPreference.DICTATION_PROP+" ?dictation) <- (?condPref "+UserPreference.DICTATION_PROP+" ?dictation)]" + 
	" 	[(?X "+UserPreference.BRIGHTNESS_PROP+" ?brightness) <- (?condPref "+UserPreference.BRIGHTNESS_PROP+" ?brightness)]" + 
	" 	[(?X "+UserPreference.HIGHLIGHT_PROP+" ?highlight) <- (?condPref "+UserPreference.HIGHLIGHT_PROP+" ?highlight)]" + 
	" 	[(?X "+UserPreference.SPEECH_RATE_PROP+" ?speechRate) <- (?condPref "+UserPreference.SPEECH_RATE_PROP+" ?speechRate)]" + 
	" 	[(?X "+UserPreference.SCREEN_READER_PROP+" ?screenReader) <- (?condPref "+UserPreference.SCREEN_READER_PROP+" ?screenReader)]" + 
	" 	[(?X "+UserPreference.STYLE_PROP+" ?style) <- (?condPref "+UserPreference.STYLE_PROP+" ?style)]" + 
	
    "	drop(4)" + 
    "	(?user "+User.PREFERENCE_PROP+" ?X)" + 
	"]"
	;
*/
	
	public static final String rules = "[conditional_preference:" + 
			" (?condPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ConditionalPreference)" + 
			",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConditions ?cond)" + 
			",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasName ?name)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+User.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+User.HAS_PREFERENCE_PROP+" ?defPref)" +
			"->" + 			
		    "	print('Conditional preference', ?name,'is true')" + 
		    "	mergePref(?defPref, ?condPref)" + 
			"]"
			;
	
	public static final JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"      		\"type\": \"gt\",\r\n" + 
				"      		\"operand\":[\r\n" + 
				"      		 \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
				"      		  77\r\n" + 
				"      		]\r\n" + 
				"      }");
	
	public static final JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"      		\"type\": \"and\",\r\n" + 
				"      		\"operand\":[\r\n" + 
				"						{\r\n" + 
				"							\"type\": \"gt\",\r\n" + 
				"							\"operand\":[\r\n" + 
				"								 \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
				"								 5\r\n" + 
				"							]\r\n" + 
				"						},\r\n" + 
				"						{\r\n" + 
				"							\"type\": \"lt\",\r\n" + 
				"							\"operand\":[\r\n" + 
				"								 \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\",\r\n" + 
				"								 20\r\n" + 
				"							]\r\n" + 
				"						}\r\n" + 
				"      		]\r\n" + 
				"      }");
	
	public static final JSONObject jsonProfile3 = new JSONObject("{\r\n" + 
				"		\"type\": \"and\",\r\n" + 
				"		\"operand\":[\r\n" + 
				"					{\r\n" + 
				"						\"type\": \"or\",\r\n" + 
				"						\"operand\":[\r\n" + 
				"							{\r\n" + 
				"								\"type\": \"lt\",\r\n" + 
				"								\"operand\":[\r\n" + 
				"								 \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
				"								 5\r\n" + 
				"								]\r\n" + 
				"							},\r\n" + 
				"							{\r\n" + 
				"								\"type\": \"gt\",\r\n" + 
				"								\"operand\":[\r\n" + 
				"								 \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\",\r\n" + 
				"								 20\r\n" + 
				"								]\r\n" + 
				"							}\r\n" + 
				"						]\r\n" + 
				"					},\r\n" + 
				"					{\r\n" + 
				"						\"type\": \"eq\",\r\n" + 
				"						\"operand\":[\r\n" + 
				"						 \"https://easytvproject.eu/registry/common/signLanguage\",\r\n" + 
				"						 true\r\n" + 
				"						]\r\n" + 
				"					}\r\n" + 
				"		]\r\n" + 
				"	}");
	
	
	
	@Test
	public void Test_conditionPreferences_Mapper1() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Conditions conditionalPreferences = mapper.readValue(jsonProfile1.toString(), Conditions.class);
	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}
	
	@Test
	public void Test_conditionPreferences_Mapper2() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Conditions conditionalPreferences = mapper.readValue(jsonProfile2.toString(), Conditions.class);
	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}

	
	@Test
	public void Test_rule_true() 
	  throws JsonParseException, IOException {
	 
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new MergePreferences());
		System.out.println("Ontology was loaded");
		
		
		//user
		OntClass userPreferenceClass = model.getOntClass(UserPreference.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(UserPreference.AUDIO_VOLUME_PROP);
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		OntClass userClass = model.getOntClass(User.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(User.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		
		//Add conditional preferences
		//gt
		OntClass gtClass = model.getOntClass(Conditions.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(Conditions.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = model.getProperty(Conditions.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		Property isTrue = model.getProperty(Conditions.IS_TURE_PROP);
		gtInstance.addProperty(isTrue, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(Conditions.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();

		hasTypeProperty = model.getProperty(Conditions.HAS_TYPE_PROP);
		ltInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/display/screen/enhancement/font/size")));
		
		hasValueProperty = model.getProperty(Conditions.HAS_VALUE_PROP);
		ltInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		ltInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//and
		OntClass andClass = model.getOntClass(Conditions.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = model.getProperty(Conditions.LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = model.getProperty(Conditions.RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		andInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//conditional
		OntClass conditionalPreferenceClass = model.getOntClass(Conditions.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(Conditions.HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, andInstance) ;
	
		Property hasFontSizeProperty = model.getProperty(UserPreference.FONT_SIZE_PROP);
		conditionalPreferenceInstance.addProperty(hasFontSizeProperty, model.createTypedLiteral(500)) ;

		conditionalPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(600));
		
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		StmtIterator list = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getInt(), 600);
		Assert.assertFalse(list.hasNext());

		//check font size
		list = inf.listStatements(userPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getInt(), 500);
		Assert.assertFalse(list.hasNext());
	
	}
	
	
	@Test
	public void Test_rule_false() 
	  throws JsonParseException, IOException {
	 
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new MergePreferences());
		System.out.println("Ontology was loaded");
		
		
		//user
		OntClass userPreferenceClass = model.getOntClass(UserPreference.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(UserPreference.AUDIO_VOLUME_PROP);
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		OntClass userClass = model.getOntClass(User.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(User.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		
		//Add conditional preferences
		//gt
		OntClass gtClass = model.getOntClass(Conditions.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(Conditions.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = model.getProperty(Conditions.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		Property isTrue = model.getProperty(Conditions.IS_TURE_PROP);
		gtInstance.addProperty(isTrue, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(Conditions.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();

		hasTypeProperty = model.getProperty(Conditions.HAS_TYPE_PROP);
		ltInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/display/screen/enhancement/font/size")));
		
		hasValueProperty = model.getProperty(Conditions.HAS_VALUE_PROP);
		ltInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		ltInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//and
		OntClass andClass = model.getOntClass(Conditions.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = model.getProperty(Conditions.LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = model.getProperty(Conditions.RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		andInstance.addProperty(isTrue, model.createTypedLiteral(false));

		
		//conditional
		OntClass conditionalPreferenceClass = model.getOntClass(Conditions.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(Conditions.HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, andInstance) ;
	
		Property hasFontSizeProperty = model.getProperty(UserPreference.FONT_SIZE_PROP);
		conditionalPreferenceInstance.addProperty(hasFontSizeProperty, model.createTypedLiteral(500)) ;

		conditionalPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(600));
		
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		StmtIterator list = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getInt(), 6);
		Assert.assertFalse(list.hasNext());
		
		
		//check font size
		list = inf.listStatements(userPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertFalse(list.hasNext());
	
	}

}
