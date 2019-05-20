package comparatorOperand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import config.RBMMTestConfig;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import rule_matchmaker.entities.ConditionalPreferences;
import rule_matchmaker.entities.User;
import rule_matchmaker.entities.UserPreference;

public class EqualsRulesTest {
	
	private OntModel model;
	public static final String 	rules = "[Equals_true:" + 
		    "(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#EqualityOperand)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue ?value)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType ?type)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+User.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+User.PREFERENCE_PROP+" ?pref)" + 
		    ",(?pref ?type ?nodeValue)" + 
			",equal(?nodeValue, ?value)"+
		    "->" + 
		    "	print('Equals', ?nodeValue, ?value)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    "]"+
	
		    "[Equals_false:" + 
		    "(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#EQ)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue ?value)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType ?type)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+User.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+User.PREFERENCE_PROP+" ?pref)" + 
		    ",(?pref ?type ?nodeValue)" + 
		    ",notEqual(?value, ?nodeValue)" + 
		    "->" + 
		    "	print('Not equals', ?value, ?nodeValue)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    "]";
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
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
		
	}

	@Test
	public void Test_greaterThanIsTrue()  {
		
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "EQ");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(6));
		 
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	
	@Test
	public void Test_greaterThanIsFalse()  {
		
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "EQ");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(ConditionalPreferences.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(UserPreference.getDataProperty("http://registry.easytv.eu/common/content/audio/volume")));
				
		Property hasValueProperty = model.getProperty(ConditionalPreferences.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(7));
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), false);
		}
		
	}
}
