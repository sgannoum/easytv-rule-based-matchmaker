package comparatorOperand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.builtin.LessThanEquals;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.OntUserPreferences;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.preference.OntCondition;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;

import config.RBMMTestConfig;

public class LessThanEqualRulesTest {
	
	private OntModel model;
	public static final String 	rules = "[Less_than_equals:" + 
		    " (?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#LE)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue ?value)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType ?type)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?pref)" + 
		    ",(?pref ?type ?nodeValue)" + 
		    "->" + 
		    "	LE(?nodeValue, ?value, ?res)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?res)" +
		    "	print('Less than equals', ?nodeValue, ?value, ?res)"+
		    "]" +
		    "[Less_than_equals_context:" + 
		    " (?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#LE)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue ?value)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType ?type)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_CONTEXT_PROP+" ?context)" + 
		    ",(?context ?type ?nodeValue)" + 
		    "->" + 
		    "	LE(?nodeValue, ?value, ?res)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?res)" +
		    "	print('Less than equals', ?nodeValue, ?value, ?res)"+
		    "]"
		    ;
		
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("LE", new LessThanEquals());
		System.out.println("Ontology was loaded");
		
		//user context
		OntClass userContextClass = model.getOntClass(OntUserContext.ONTOLOGY_CLASS_URI);
		Individual  userContextInstance = userContextClass.createIndividual();
		
		Property hasTimeProperty = model.getProperty(OntUserContext.getDataProperty("http://registry.easytv.eu/context/time"));
		userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral("2019-05-30T09:47:47.619Z"));
		
		
		//user
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume"));
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		Property hasContextProperty = model.getProperty(OntUserProfile.HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextProperty, userContextInstance);
		
	}

	@Test
	public void Test_lessThanEqualsTrue1()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "LE");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(7));
		 
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	@Test
	public void Test_lessThanEqualsTrue2()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "LE");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(6));
		 
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	@Test
	public void Test_lessThanEqualsIsFalse()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "LE");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(3));
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), false);
		}
		
	}
	

	@Test
	public void Test_lessThanEqua_Date_true1()  {
		
		//lt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "LE");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntUserContext.getDataProperty("http://registry.easytv.eu/context/time")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral("2019-05-30T09:47:47.619Z" ));
		 
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	@Test
	public void Test_lessThanEqua_Date_true2()  {
		
		//lt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "LE");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntUserContext.getDataProperty("http://registry.easytv.eu/context/time")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral("2019-06-30T09:47:47.619Z" ));
		 
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	@Test
	public void Test_lessThanEquals_Date_False()  {
		
		//lt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "LE");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntUserContext.getDataProperty("http://registry.easytv.eu/context/time")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral("2019-03-30T09:47:47.619Z" ));
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		StmtIterator list = inf.listStatements(null, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), false);
		}
		
	}
}
