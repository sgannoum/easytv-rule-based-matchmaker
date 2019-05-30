package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import builtin.Equals;
import config.RBMMTestConfig;
import junit.framework.Assert;
import rule_matchmaker.entities.SuggestedPreferences;
import rule_matchmaker.entities.User;
import rule_matchmaker.entities.UserContext;
import rule_matchmaker.entities.UserPreference;
import rule_matchmaker.entities.Visual;

public class UserContextTest {
	
	private OntModel model;
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"1558700176286\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"}");
	
	
	public static final String rules = "[user_rule_1:" + 
			"(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			",(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
			",(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasVisualAbility ?visual)" + 
			",(?visual http://www.owl-ontologies.com/OntologyEasyTV.owl#hasColorBlindness ?blindness)" +	
			",equals(?blindness, 'protanopia'^^http://www.w3.org/2001/XMLSchema#string, ?res)" +
			"->" + 
			"	(?sugPref "+UserPreference.BACKGROUND_PROP+" '#ffffff'^^http://www.w3.org/2001/XMLSchema#string)" + 
			"	(?sugPref "+UserPreference.FONT_COLOR_PROP+" '#000000'^^http://www.w3.org/2001/XMLSchema#string)" + 
			"	print('Suggested preferences')"+
			"]"
			;
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new Equals());
		System.out.println("Ontology was loaded");
		
		System.out.println(rules);

				
	}
	
	@Test
	public void TestAuditoyMapper() 
	  throws JsonParseException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UserContext userContext = mapper.readValue(jsonProfile1.toString(), UserContext.class);
	 
		System.out.println(userContext.toString());
	    Assert.assertNotNull(userContext);
	}
	
	
	@Test
	public void Test_user_rule_1()  {
		
		//user
		OntClass userViualClass = model.getOntClass(Visual.ONTOLOGY_CLASS_URI);
		Individual  visualInstance = userViualClass.createIndividual();
		
		Property hasColorBlindnessProperty = model.getProperty(Visual.COLOR_BLINDNESS_PROP);
		visualInstance.addProperty(hasColorBlindnessProperty, model.createTypedLiteral("protanopia"));
		
		OntClass suggestedPreferencesClass = model.getOntClass(SuggestedPreferences.ONTOLOGY_CLASS_URI);
		Individual  suggestedPreferencesnstance = suggestedPreferencesClass.createIndividual();
		
		
		OntClass userClass = model.getOntClass(User.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasVisualAbilityProperty = model.getProperty(User.HAS_VISUAL_PROP);
		userInstance.addProperty(hasVisualAbilityProperty, visualInstance);
		
		Property hasSuggestedPreferencesnstanceProperty = model.getProperty(User.HAS_SUGGESTED_PREFERENCES_PROP);
		userInstance.addProperty(hasSuggestedPreferencesnstanceProperty, suggestedPreferencesnstance);
					
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		
		Property hasBackgroundColorProperty = model.getProperty(UserPreference.BACKGROUND_PROP);
		Property hasFontColorProperty = model.getProperty(UserPreference.FONT_COLOR_PROP);

		StmtIterator list = inf.listStatements(suggestedPreferencesnstance, hasBackgroundColorProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getString(), "#ffffff");
		Assert.assertFalse(list.hasNext());

		list = inf.listStatements(suggestedPreferencesnstance, hasFontColorProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getString(), "#000000");
		Assert.assertFalse(list.hasNext());
		
	}
}
