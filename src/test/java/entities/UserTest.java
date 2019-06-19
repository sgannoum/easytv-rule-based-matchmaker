package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import com.certh.iti.easytv.rbmm.user.User;
import com.certh.iti.easytv.rbmm.user.preference.Preferences;
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
	private String rules =  AndRulesTest.rules + OrRulesTest.rules + NotRulesTest.rules +
			EqualsRulesTest.rules + NotEqualsRulesTest.rules +
			GreaterThanRulesTest.rules + GreaterThanEqualRulesTest.rules +
			LessThanRulesTest.rules + LessThanEqualRulesTest.rules +
			ConditionsTest.rules
			;
	
	
	public static final JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			" \"context\":{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"2019-05-30T09:47:47.619Z\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"	},\r\n"	+
			"  \"user_preferences\": {\r\n" + 
			"    \"default\": {\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 3,\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"sans-serif\",\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#000000\",\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#ffffff\",\r\n" + 
			"        \"http://registry.easytv.eu/common/content/audio/volume\": 6,\r\n" + 
			"        \"http://registry.easytv.eu/common/content/audio/language\": \"en\",\r\n" + 
			"      }\r\n" + 
			"    },\r\n" + 
			"    \"conditional\": [\r\n" + 
			"      {\r\n" + 
			"        \"name\": \"condition_1\",\r\n" + 
			"        \"preferences\": {\r\n" + 
			"          \"http://registry.easytv.eu/common/content/audio/volume\": 10\r\n" + 
			"        },\r\n" + 
			"        \"conditions\": [\r\n" + 
			"          {\r\n" + 
			"            \"type\": \"and\",\r\n" + 
			"            \"operands\": [\r\n" + 
			"			  {\r\n" + 
			"				\"type\": \"gt\",\r\n" + 
			"				\"operands\": [\r\n" + 
			"				  \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
			"				  1\r\n" + 
			"				]\r\n" + 
			"			  },\r\n" + 
			"				{\r\n" + 
			"					\"type\": \"lt\",\r\n" + 
			"					\"operands\": [\r\n" + 
			"					  \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\",\r\n" + 
			"					  7\r\n" + 
			"					]\r\n" + 
			"				}\r\n" + 
			"            ]\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      }\r\n" + 
			"    ]"+
			"  }\r\n" + 
			"}");
	
	public static final JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
			" \"context\":{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"2019-05-30T09:47:47.619Z\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"	},\r\n"	+
			"  \"user_preferences\": {\r\n" + 
			"    \"default\": {\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 3,\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"sans-serif\",\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#000000\",\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#ffffff\",\r\n" + 
			"        \"http://registry.easytv.eu/common/content/audio/volume\": 6,\r\n" + 
			"        \"http://registry.easytv.eu/common/content/audio/language\": \"en\",\r\n" + 
			"      }\r\n" + 
			"    },\r\n" + 
			"    \"conditional\": [\r\n" + 
			"      {\r\n" + 
			"        \"name\": \"condition_1\",\r\n" + 
			"        \"preferences\": {\r\n" + 
			"          \"http://registry.easytv.eu/common/content/audio/volume\": 10\r\n" + 
			"        },\r\n" + 
			"        \"conditions\": [\r\n" + 
			"			  {\r\n" + 
			"				\"type\": \"gt\",\r\n" + 
			"				\"operands\": [\r\n" + 
			"				  \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
			"				  1\r\n" + 
			"				]\r\n" + 
			"			  },\r\n" + 
			"        ]\r\n" + 
			"      }\r\n" + 
			"    ]"+
			"  }\r\n" + 
			"}");
	
	public static final JSONObject jsonProfile3 = new JSONObject("{\r\n" + 
			" \"context\":{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"2019-05-30T09:47:47.619Z\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"	},\r\n"	+
			"  \"user_preferences\": {\r\n" + 
			"    \"default\": {\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 3,\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"sans-serif\",\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#000000\",\r\n" + 
			"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#ffffff\",\r\n" + 
			"        \"http://registry.easytv.eu/common/content/audio/volume\": 6,\r\n" + 
			"        \"http://registry.easytv.eu/common/content/audio/language\": \"en\",\r\n" + 
			" 		 \"https://easytvproject.eu/registry/common/audiolanguage\": \"en\","+
			"      }\r\n" + 
			"    },\r\n" + 
			"    \"conditional\": [\r\n" + 
			"      {\r\n" + 
			"        \"name\": \"condition_1\",\r\n" + 
			"        \"preferences\": {\r\n" + 
			"          \"http://registry.easytv.eu/common/content/audio/volume\": 10\r\n" + 
			"        },\r\n" + 
			"        \"conditions\": [\r\n" + 
			"          {\r\n" + 
			"            \"type\": \"and\",\r\n" + 
			"            \"operands\": [\r\n" + 
			"			  {\r\n" + 
			"				\"type\": \"gt\",\r\n" + 
			"				\"operands\": [\r\n" + 
			"				  \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
			"				  1\r\n" + 
			"				]\r\n" + 
			"			  },\r\n" + 
			"				{\r\n" + 
			"					\"type\": \"lt\",\r\n" + 
			"					\"operands\": [\r\n" + 
			"					  \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\",\r\n" + 
			"					  7\r\n" + 
			"					]\r\n" + 
			"				}\r\n" + 
			"            ]\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      },\r\n" + 
			"      {\r\n" + 
			"        \"name\": \"condition_2\",\r\n" + 
			"        \"preferences\": {\r\n" + 
			"          \"http://registry.easytv.eu/common/display/screen/enhancement/background\":  \"#222222\"\r\n" + 
			"        },\r\n" + 
			"        \"conditions\": [\r\n" + 
			"          {\r\n" + 
			"            \"type\": \"and\",\r\n" + 
			"            \"operands\": [\r\n" + 
			"			  {\r\n" + 
			"				\"type\": \"eq\",\r\n" + 
			"				\"operands\": [\r\n" + 
			"				  \"http://registry.easytv.eu/context/time\",\r\n" + 
			"				  \"2019-05-30T09:47:47.619Z\"\r\n" + 
			"				]\r\n" + 
			"			  },\r\n" + 
			"				{\r\n" + 
			"					\"type\": \"eq\",\r\n" + 
			"					\"operands\": [\r\n" + 
			"					  \"http://registry.easytv.eu/common/display/screen/enhancement/background\",\r\n" + 
			"				  \"#ffffff\"\r\n" + 
			"					]\r\n" + 
			"				}\r\n" + 
			"            ]\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      }\r\n" +
			"    ]"+
			"  }\r\n" + 
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
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(jsonProfile1.toString(), User.class);
	 
		System.out.println(user.toString());
	    Assert.assertNotNull(user);
	}
	
	@Test
	public void TestUserInference1() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(jsonProfile1.toString(), User.class);
		
		
		Individual userInstance = user.createOntologyInstance(model);
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasPreferenceProperty = model.getProperty(User.HAS_PREFERENCE_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasPreferenceProperty, (RDFNode)null);
		Resource userPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userPreferenceInstance, null, (RDFNode)null);
		Property hasAudioVolumeProperty = model.getProperty(Preferences.AUDIO_VOLUME_PROP);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(10, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());
		
		Property hasFontSizeProperty = model.getProperty(Preferences.FONT_SIZE_PROP);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertEquals(3, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());
		
		Property hasBackgroundProperty = model.getProperty(Preferences.BACKGROUND_PROP);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasBackgroundProperty, (RDFNode)null);
		Assert.assertEquals("#ffffff", userPreferenceList.next().getObject().asLiteral().getString());
		Assert.assertFalse(userPreferenceList.hasNext());

	}
	
	@Test
	public void TestUserInference2() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(jsonProfile2.toString(), User.class);
		
		Individual userInstance = user.createOntologyInstance(model);
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasPreferenceProperty = model.getProperty(User.HAS_PREFERENCE_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasPreferenceProperty, (RDFNode)null);
		Resource userPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userPreferenceInstance, null, (RDFNode)null);
		Property hasAudioVolumeProperty = model.getProperty(Preferences.AUDIO_VOLUME_PROP);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(10, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());

	}
	
	@Test
	public void TestUserInference3() 
	  throws JsonParseException, IOException {
			
		User user = User.read(jsonProfile3.toString());
		Individual userInstance = user.createOntologyInstance(model);
				
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		Property hasPreferenceProperty = model.getProperty(User.HAS_PREFERENCE_PROP);
		StmtIterator userList = inf.listStatements(userInstance, hasPreferenceProperty, (RDFNode)null);
		Resource userPreferenceInstance = userList.next().getObject().asResource();
		
		StmtIterator userPreferenceList = inf.listStatements(userPreferenceInstance, null, (RDFNode)null);
		Property hasAudioVolumeProperty = model.getProperty(Preferences.AUDIO_VOLUME_PROP);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(10, userPreferenceList.next().getObject().asLiteral().getInt());
		Assert.assertFalse(userPreferenceList.hasNext());
		
		Property hasBackgroundProperty = model.getProperty(Preferences.BACKGROUND_PROP);
		userPreferenceList = inf.listStatements(userPreferenceInstance, hasBackgroundProperty, (RDFNode)null);
		Assert.assertEquals("#222222", userPreferenceList.next().getObject().asLiteral().getString());
		Assert.assertFalse(userPreferenceList.hasNext());
	}

}
