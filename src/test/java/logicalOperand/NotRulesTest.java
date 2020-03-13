package logicalOperand;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.builtin.NOT;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.OntUserPreferences;
import com.certh.iti.easytv.rbmm.user.preference.OntCondition;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;

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

public class NotRulesTest {
	
	private OntModel model;
	public static final  String rules = "[Not_rule:" + 
			"(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#NOT)" + 
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasLeftOperand ?leftOp)" + 
			",(?leftOp http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v1)" +
			"->" + 
			"	not(?v1, ?v2)"+
			"	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?v2)" +
			"	print('NOT', ?v1, ?v2)"+
			"]"
			;
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		model = ModelFactory.createOntologyModel();
		InputStream in = ClassLoader.getSystemResourceAsStream(RBMMTestConfig.ONTOLOGY_File);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new NOT());
		System.out.println("Ontology was loaded");
		
		//user
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.hasVolume);
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		Property cursorSizeProperty = model.getProperty(OntPreference.hasCursorSize);
		userPreferenceInstance.addProperty(cursorSizeProperty, model.createTypedLiteral(10));
		
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
	}

	@Test
	public void Test_Not_True_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(true));
		 
		//not
		OntClass notClass = model.getOntClass(OntCondition.NAMESPACE + "NOT");
		Individual notInstance = notClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		notInstance.addProperty(hasLeftOperandProperty, gtInstance);			
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
			
  		StmtIterator list = inf.listStatements(notInstance, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), false);
		}
		
	}
	
	@Test
	public void Test_Not_False_input()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();

		Property isTrueProperty = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrueProperty, model.createTypedLiteral(false));
		 
		//not
		OntClass notClass = model.getOntClass(OntCondition.NAMESPACE + "NOT");
		Individual notInstance = notClass.createIndividual();

		Property hasLeftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		notInstance.addProperty(hasLeftOperandProperty, gtInstance);			
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
			
  		StmtIterator list = inf.listStatements(notInstance, isTrueProperty, (RDFNode)null);
		Assert.assertTrue(list.hasNext(), "No such statement "+isTrueProperty.getLocalName());
		while (list.hasNext()) {
			Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}
		
	}
	
	
}
