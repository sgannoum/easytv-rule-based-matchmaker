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

import com.certh.iti.easytv.rbmm.builtin.Equals;
import com.certh.iti.easytv.rbmm.user.SuggestedPreferences;
import com.certh.iti.easytv.rbmm.user.UserProfile;
import com.certh.iti.easytv.rbmm.user.UserContext;
import com.certh.iti.easytv.rbmm.user.UserPreferences;
import com.certh.iti.easytv.rbmm.user.preference.Preference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.RBMMTestConfig;
import junit.framework.Assert;

public class UserContextTest {
	
	private OntModel model;
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"2019-05-30T09:47:47.619Z\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"}");
	
	
	public static final String rules = "[user_rule_1:" + 
			"(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)" + 
			",(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+UserProfile.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+UserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+Preference.AUDIO_VOLUME_PROP+" ?audioVolume)" +
		    ",(?defPref "+Preference.CURSOR_SIZE_PROP+" ?cursorSize)" +
			",equals(?audioVolume, '6'^^http://www.w3.org/2001/XMLSchema#integer, ?res1)" +
			",equals(?cursorSize, '10'^^http://www.w3.org/2001/XMLSchema#integer, ?res2)" +
			"->" + 
			"	(?sugPref "+Preference.BACKGROUND_PROP+" '#ffffff'^^http://www.w3.org/2001/XMLSchema#string)" + 
			"	(?sugPref "+Preference.FONT_COLOR_PROP+" '#000000'^^http://www.w3.org/2001/XMLSchema#string)" + 
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
		OntClass userClass = model.getOntClass(UserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		OntClass suggestedPreferencesClass = model.getOntClass(SuggestedPreferences.ONTOLOGY_CLASS_URI);
		Individual  suggestedPreferencesnstance = suggestedPreferencesClass.createIndividual();
		
		Property hasSuggestedPreferencesnstanceProperty = model.getProperty(UserProfile.HAS_SUGGESTED_PREFERENCES_PROP);
		userInstance.addProperty(hasSuggestedPreferencesnstanceProperty, suggestedPreferencesnstance);
		
		
		OntClass userPreferenceClass = model.getOntClass(UserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(Preference.AUDIO_VOLUME_PROP);
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		Property cursorSizeProperty = model.getProperty(Preference.CURSOR_SIZE_PROP);
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		Property hasPreferenceProperty = model.getProperty(UserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
					
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		Property hasBackgroundColorProperty = model.getProperty(Preference.BACKGROUND_PROP);
		Property hasFontColorProperty = model.getProperty(Preference.FONT_COLOR_PROP);

		StmtIterator list = inf.listStatements(suggestedPreferencesnstance, hasBackgroundColorProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getString(), "#ffffff");
		Assert.assertFalse(list.hasNext());

		list = inf.listStatements(suggestedPreferencesnstance, hasFontColorProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getString(), "#000000");
		Assert.assertFalse(list.hasNext());
		
	}
	
	@Test
	public void Test_user_rule_2()  {
		
		//user	
		OntClass userClass = model.getOntClass(UserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		OntClass suggestedPreferencesClass = model.getOntClass(SuggestedPreferences.ONTOLOGY_CLASS_URI);
		Individual  suggestedPreferencesnstance = suggestedPreferencesClass.createIndividual();
		
		Property hasSuggestedPreferencesnstanceProperty = model.getProperty(UserProfile.HAS_SUGGESTED_PREFERENCES_PROP);
		userInstance.addProperty(hasSuggestedPreferencesnstanceProperty, suggestedPreferencesnstance);
		
		
		OntClass userPreferenceClass = model.getOntClass(UserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(Preference.AUDIO_VOLUME_PROP);
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(5));
		
		Property cursorSizeProperty = model.getProperty(Preference.CURSOR_SIZE_PROP);
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		Property hasPreferenceProperty = model.getProperty(UserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
					
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		Property hasBackgroundColorProperty = model.getProperty(Preference.BACKGROUND_PROP);
		Property hasFontColorProperty = model.getProperty(Preference.FONT_COLOR_PROP);

		StmtIterator list = inf.listStatements(suggestedPreferencesnstance, hasBackgroundColorProperty, (RDFNode)null);
		Assert.assertFalse(list.hasNext());

		list = inf.listStatements(suggestedPreferencesnstance, hasFontColorProperty, (RDFNode)null);
		Assert.assertFalse(list.hasNext());
		
	}
}
