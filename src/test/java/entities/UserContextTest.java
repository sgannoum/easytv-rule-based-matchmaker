package entities;

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
import com.certh.iti.easytv.rbmm.user.OntSuggestedPreferences;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.OntUserPreferences;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.user.exceptions.UserContextParsingException;
import com.fasterxml.jackson.core.JsonParseException;
import config.RBMMTestConfig;
import junit.framework.Assert;

public class UserContextTest {
	
	private OntModel model;
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"09:47:47\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"ca\"\r\n" + 
			"}");
	
	
	public final String rules = "[user_rule_1:" + 
			"(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+OntPreference.getPredicate("http://registry.easytv.eu/application/cs/ui/text/size")+" ?audioVolume)" +
		    ",(?defPref "+OntPreference.getPredicate("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size")+" ?cursorSize)" +
			",EQ(?audioVolume, '6'^^http://www.w3.org/2001/XMLSchema#integer, ?res1)" +
			",EQ(?cursorSize, '10'^^http://www.w3.org/2001/XMLSchema#integer, ?res2)" +
			"->" + 
			"	(?sugPref "+OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/background/color")+" '#ffffff'^^http://www.w3.org/2001/XMLSchema#string)" + 
			"	(?sugPref "+OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/font/color")+" '#000000'^^http://www.w3.org/2001/XMLSchema#string)" + 
			"	print('Suggested preferences')"+
			"]"
			;
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		model = ModelFactory.createOntologyModel();
		InputStream in = ClassLoader.getSystemResourceAsStream(RBMMTestConfig.ONTOLOGY_File);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("EQ", new Equals());
		System.out.println("Ontology was loaded");
		
	}
	
	@Test
	public void TestAuditoyMapper() 
	  throws JsonParseException, IOException, UserContextParsingException {
		
		OntUserContext userContext = new OntUserContext(jsonProfile1);
	 
		System.out.println(userContext.toString());
	    Assert.assertNotNull(userContext);
	}
	
	
	@Test
	public void Test_user_rule_1()  {
		
		//user	
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		OntClass suggestedPreferencesClass = model.getOntClass(OntSuggestedPreferences.ONTOLOGY_CLASS_URI);
		Individual  suggestedPreferencesnstance = suggestedPreferencesClass.createIndividual();
		
		Property hasSuggestedPreferencesnstanceProperty = model.getProperty(OntUserProfile.HAS_SUGGESTED_PREFERENCES_PROP);
		userInstance.addProperty(hasSuggestedPreferencesnstanceProperty, suggestedPreferencesnstance);
		
		
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/ui/text/size"));
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		Property cursorSizeProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size"));
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
					
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		Property hasBackgroundColorProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/background/color"));
		Property hasFontColorProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/font/color"));

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
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		OntClass suggestedPreferencesClass = model.getOntClass(OntSuggestedPreferences.ONTOLOGY_CLASS_URI);
		Individual  suggestedPreferencesnstance = suggestedPreferencesClass.createIndividual();
		
		Property hasSuggestedPreferencesnstanceProperty = model.getProperty(OntUserProfile.HAS_SUGGESTED_PREFERENCES_PROP);
		userInstance.addProperty(hasSuggestedPreferencesnstanceProperty, suggestedPreferencesnstance);
		
		
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/ui/text/size"));
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(5));
		
		Property cursorSizeProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size"));
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
					
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		Property hasBackgroundColorProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/background/color"));
		Property hasFontColorProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/font/color"));

		StmtIterator list = inf.listStatements(suggestedPreferencesnstance, hasBackgroundColorProperty, (RDFNode)null);
		Assert.assertFalse(list.hasNext());

		list = inf.listStatements(suggestedPreferencesnstance, hasFontColorProperty, (RDFNode)null);
		Assert.assertFalse(list.hasNext());
		
	}
}
