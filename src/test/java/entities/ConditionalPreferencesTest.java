package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.RBMMTestConfig;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import junit.framework.Assert;
import rule_matchmaker.RuleReasoner;
import rule_matchmaker.entities.ConditionalPreferences;
import rule_matchmaker.entities.UserPreference;

public class ConditionalPreferencesTest {
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"      		\"type\": \"gt\",\r\n" + 
				"      		\"operand\":[\r\n" + 
				"      		 \"http://registry.easytv.eu/common/content/audio/volume\",\r\n" + 
				"      		  77\r\n" + 
				"      		]\r\n" + 
				"      }");
	
	JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
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
	
	JSONObject jsonProfile3 = new JSONObject("{\r\n" + 
				"		\"type\": \"and\",\r\n" + 
				"		\"operand\":[\r\n" + 
				"					{\r\n" + 
				"						\"type\": \"gt\",\r\n" + 
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
	
	@BeforeTest
	public void beforeTests() {
	    Assert.assertNotNull(jsonProfile1);
	}
	
	@Test
	public void TestVisualMapper1() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ConditionalPreferences conditionalPreferences = mapper.readValue(jsonProfile1.toString(), ConditionalPreferences.class);
	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}
	
	@Test
	public void TestVisualMapper2() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ConditionalPreferences conditionalPreferences = mapper.readValue(jsonProfile2.toString(), ConditionalPreferences.class);
	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}

	@Test
	public void Test_handleOperands() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ConditionalPreferences conditionalPreferences = mapper.readValue(jsonProfile3.toString(), ConditionalPreferences.class);
	 
		
		List<Object> flatlist = conditionalPreferences.handleOperands1();
		Iterator<Object> iter = flatlist.iterator();
		
		while(iter.hasNext()) {
			Object obj = iter.next();
			System.out.print(obj.toString());
			System.out.print(",");
			System.out.print(obj.getClass().getName());
			System.out.println();
		}
		
	    Assert.assertNotNull(conditionalPreferences);
	}
	
	@Test
	public void Test_handleOperands_1() 
	  throws JsonParseException, IOException {
	 
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		System.out.println("Ontology was loaded");
		
		file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel Expected_model = ModelFactory.createOntologyModel();
		in = new FileInputStream(file);
		Expected_model = (OntModel) Expected_model.read(in, null, "");
		System.out.println("Ontology was loaded");
		
		
		//gt
		OntClass gtClass = Expected_model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = Expected_model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, Expected_model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = Expected_model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, Expected_model.createTypedLiteral(5));
		
		//lt
		OntClass ltClass = Expected_model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();

		hasTypeProperty = Expected_model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		ltInstance.addProperty(hasTypeProperty, Expected_model.createTypedLiteral(UserPreference.getDataProperty("http://registry.easytv.eu/common/display/screen/enhancement/font/size")));
		
		hasValueProperty = Expected_model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		ltInstance.addProperty(hasValueProperty, Expected_model.createTypedLiteral(5));
		
		
		//and
		OntClass andClass = Expected_model.getOntClass(ConditionalPreferences.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = Expected_model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = Expected_model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		//conditional
		OntClass conditionalPreferenceClass = Expected_model.getOntClass(ConditionalPreferences.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = Expected_model.getProperty(ConditionalPreferences.HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, andInstance) ;
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ConditionalPreferences conditionalPreferences = mapper.readValue(jsonProfile2.toString(), ConditionalPreferences.class);
		
		//Assert.assertNotNull(conditionalPreferences.createOntologyInstance(model));
		 
		RuleReasoner.printModel(Expected_model); 
	}

}
