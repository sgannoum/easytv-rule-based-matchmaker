package logicalOperand;

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

import com.certh.iti.easytv.rbmm.builtin.OR;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.OntUserPreferences;
import com.certh.iti.easytv.rbmm.user.preference.OntCondition;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;

import config.RBMMTestConfig;

public class OrRulesTest {
	
	private OntModel model;
	public static final  String rules = "[or_rule:" + 
			"(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#OR)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasLeftOperand ?leftOp)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasRightOperand ?rightOp)" +	
			",(?leftOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v1)" +
			",(?rightOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v2)" +
			"->" + 
			"	or(?v1, ?v2, ?v3)"+
			"	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v3)" +
			"	print('OR', ?v1, ?v2, ?v3)"+
			"]"
			;
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("or", new OR());
		System.out.println("Ontology was loaded");
		
		//user
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume"));
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		Property cursorSizeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/display/screen/enhancement/cursor/size"));
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
	}

	@Test
	public void Test_And_True_True_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(OntCondition.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		 
		//and
		OntClass orClass = model.getOntClass(OntCondition.NAMESPACE + "OR");
		Individual orInstance = orClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(OntCondition.HAS_RIGHT_OPERAND_PROP);
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
	public void Test_And_False_True_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		
		//lt
		OntClass ltClass = model.getOntClass(OntCondition.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		 
		//or
		OntClass andClass = model.getOntClass(OntCondition.NAMESPACE + "OR");
		Individual orInstance = andClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(OntCondition.HAS_RIGHT_OPERAND_PROP);
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
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(OntCondition.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		 
		//or
		OntClass andClass = model.getOntClass(OntCondition.NAMESPACE + "OR");
		Individual orInstance = andClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(OntCondition.HAS_RIGHT_OPERAND_PROP);
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
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		
		//lt
		OntClass ltClass = model.getOntClass(OntCondition.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		 
		//or
		OntClass andClass = model.getOntClass(OntCondition.NAMESPACE + "OR");
		Individual orInstance = andClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		orInstance.addProperty(hasLeftOperandProperty, gtInstance);
		
		Property hasRightOperandProperty = model.getProperty(OntCondition.HAS_RIGHT_OPERAND_PROP);
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
