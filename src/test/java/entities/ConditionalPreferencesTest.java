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
import rule_matchmaker.entities.ConditionalPreferences;
import rule_matchmaker.entities.User;
import rule_matchmaker.entities.UserPreference;

public class ConditionalPreferencesTest {
	
	public static final String rules = "[conditional_preference:" + 
			" (?condPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ConditionalPreference)" + 
			",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConditions ?cond)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+User.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+User.PREFERENCE_PROP+" ?defPref)" + 
			"->" + 			
		    "	print('A conditional preference has benn satisfied')" + 
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
		ConditionalPreferences conditionalPreferences = mapper.readValue(jsonProfile1.toString(), ConditionalPreferences.class);
	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}
	
	@Test
	public void Test_conditionPreferences_Mapper2() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ConditionalPreferences conditionalPreferences = mapper.readValue(jsonProfile2.toString(), ConditionalPreferences.class);
	 
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
		
		Property hasPreferenceProperty = model.getProperty(User.PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		
		//Add conditional preferences
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		Property isTrue = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		gtInstance.addProperty(isTrue, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();

		hasTypeProperty = model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		ltInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/display/screen/enhancement/font/size")));
		
		hasValueProperty = model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		ltInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		ltInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//and
		OntClass andClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		andInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//conditional
		OntClass conditionalPreferenceClass = model.getOntClass(ConditionalPreferences.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(ConditionalPreferences.HAS_CONDITIONS_PROP);
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
		
		Property hasPreferenceProperty = model.getProperty(User.PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		
		//Add conditional preferences
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		Property isTrue = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		gtInstance.addProperty(isTrue, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();

		hasTypeProperty = model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		ltInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/display/screen/enhancement/font/size")));
		
		hasValueProperty = model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		ltInstance.addProperty(hasValueProperty, model.createTypedLiteral(5));
		
		ltInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//and
		OntClass andClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		andInstance.addProperty(isTrue, model.createTypedLiteral(false));

		
		//conditional
		OntClass conditionalPreferenceClass = model.getOntClass(ConditionalPreferences.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(ConditionalPreferences.HAS_CONDITIONS_PROP);
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
