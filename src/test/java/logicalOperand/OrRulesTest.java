package logicalOperand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import rule_matchmaker.entities.ConditionalPreferences;
import rule_matchmaker.entities.User;
import rule_matchmaker.entities.UserPreference;

public class OrRulesTest {
	
	private OntModel model;
	public final String rules = "[AND_OR_Similar_Inputs:" + 
		    "(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#DualOperand)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasLeftOperand ?leftOp)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasRightOperand ?rightOp)" +   
		//  ",(?leftOp http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ComparatorOperand)" + 
		//  ",(?rightOp http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ComparatorOperand)" + 
		  	",(?leftOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v1)" +
		  	",(?rightOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v2)" +
			",equal(?v1, ?v2)"+
		    "->" + 
		    "	print('Equals', ?v1, ?v2)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v1)" +
		    "]"+
	
			"[AND_Different_Inputs:" + 
			"(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#AND)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasLeftOperand ?leftOp)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasRightOperand ?rightOp)" +
		//	",(?leftOp http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ComparatorOperand)" + 
		//	",(?rightOp http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ComparatorOperand)" + 	
			",(?leftOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v1)" +
			",(?rightOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v2)" +
			",notEqual(?v1, ?v2)"+
			"->" + 
			"	print('AND', ?v1, ?v2, 'false')"+
			"	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"]"+

			
			"[OR_Different_Inputs:" + 
			"(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#OR)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasLeftOperand ?leftOp)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasRightOperand ?rightOp)" +
		//	",(?leftOp http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ComparatorOperand)" + 
		//	",(?rightOp http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ComparatorOperand)" + 	
			",(?leftOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v1)" +
			",(?rightOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v2)" +
			",notEqual(?v1, ?v2)"+
			"->" + 
			"	print('OR', ?v1, ?v2, 'true')"+
			"	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"]"
			;
	
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
		
		Property cursorSizeProperty = model.getProperty(UserPreference.CURSOR_SIZE_PROP);
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		OntClass userClass = model.getOntClass(User.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(User.PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
	}

	@Test
	public void Test_And_True_True_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		 
		//and
		OntClass orClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "OR");
		Individual orInstance = orClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		orInstance.addProperty(hasRightOperandProperty, ltInstance);
				
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
			
  		StmtIterator list = inf.listStatements(orInstance, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
/*		StmtIterator list = inf.listStatements(orInstance, null, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			System.out.println(list.next());
		}
*/
		
	}
	
	
	@Test
	public void Test_And_False_True_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		
		//lt
		OntClass ltClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		 
		//or
		OntClass andClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "OR");
		Individual orInstance = andClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		orInstance.addProperty(hasRightOperandProperty, ltInstance);
				
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		
		StmtIterator list = inf.listStatements(orInstance, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	@Test
	public void Test_And_True_False_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		 
		//or
		OntClass andClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "OR");
		Individual orInstance = andClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		orInstance.addProperty(hasRightOperandProperty, ltInstance);
				
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		
		StmtIterator list = inf.listStatements(orInstance, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	@Test
	public void Test_And_False_False_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(ConditionalPreferences.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		
		//lt
		OntClass ltClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		 
		//or
		OntClass andClass = model.getOntClass(ConditionalPreferences.NAMESPACE + "OR");
		Individual orInstance = andClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(ConditionalPreferences.LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(ConditionalPreferences.RIGHT_OPERAND_PROP);
		orInstance.addProperty(hasRightOperandProperty, ltInstance);
				
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
		
		StmtIterator list = inf.listStatements(orInstance, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), false);
		}
		
	}
}
