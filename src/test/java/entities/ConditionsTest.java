package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.builtin.MergePreferences;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.OntUserPreferences;
import com.certh.iti.easytv.rbmm.user.preference.OntCondition;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.fasterxml.jackson.core.JsonParseException;
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

import junit.framework.Assert;

public class ConditionsTest {
	
	/**
	 * A condition that has conditions which are true => apply the condition's preferences
	 */
	public static final String rules = "[conditional_preference:" + 
			" (?condPref http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#ConditionalPreference)" + 
			",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasConditions ?cond)" + 
			",(?condPref http://www.owl-ontologies.com/OntologyEasyTV.owl#hasName ?name)" +
			",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
			"->" + 			
		    "	print('Conditional preference', ?name,'is true')" + 
		    "	mergePref(?defPref, ?condPref)" + 
			"]"
			;
	
	public static final JSONObject jsonProfile1 = new JSONObject("{" + 
				"      		\"type\": \"gt\"," + 
				"      		\"operands\":[" + 
				"				  \"http://registry.easytv.eu/context/time\"," + 
				"				  \"2019-04-30T09:47:47.619Z\" " + 
				"      		]" + 
				"      }");
	
	public static final JSONObject jsonProfile2 = new JSONObject("{" + 
				"      		\"type\": \"and\"," + 
				"      		\"operands\":[" + 
				"						{" + 
				"							\"type\": \"gt\"," + 
				"							\"operands\":[" + 
				"				  \"http://registry.easytv.eu/context/time\"," + 
				"				  \"2019-04-30T09:47:47.619Z\" " + 
				"							]" + 
				"						}," + 
				"						{" + 
				"							\"type\": \"lt\"," + 
				"							\"operands\":[" + 
				"				  \"http://registry.easytv.eu/context/time\"," + 
				"				  \"2019-10-30T09:47:47.619Z\" " + 
				"							]" + 
				"						}" + 
				"      		]" + 
				"      }");
	
	public static final JSONObject jsonProfile3 = new JSONObject("{" + 
				"		\"type\": \"and\"," + 
				"		\"operands\":[" + 
				"					{" + 
				"						\"type\": \"or\"," + 
				"						\"operand\":[" + 
				"							{" + 
				"								\"type\": \"lt\"," + 
				"								\"operands\":[" + 
				"				  \"http://registry.easytv.eu/context/time\"," + 
				"				  \"2019-10-30T09:47:47.619Z\" " + 
				"								]" + 
				"							}," + 
				"							{" + 
				"								\"type\": \"gt\"," + 
				"								\"operands\":[" + 
				"				  \"http://registry.easytv.eu/context/time\"," + 
				"				  \"2019-04-30T09:47:47.619Z\" " + 
				"								]" + 
				"							}" + 
				"						]" + 
				"					}," + 
				"					{" + 
				"						\"type\": \"eq\"," + 
				"						\"operands\":[" + 
				"				  \"http://registry.easytv.eu/context/location\"," + 
				"				  \"fr\" " + 
				"						]" + 
				"					}" + 
				"		]" + 
				"	}");
	
	
	
	@Test
	public void Test_conditionPreferences_Mapper1() 
	  throws JsonParseException, IOException, UserProfileParsingException {
	 
		OntCondition conditionalPreferences = new OntCondition(new com.certh.iti.easytv.user.preference.Condition(jsonProfile1));
	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}
	
	@Test
	public void Test_conditionPreferences_Mapper2() 
	  throws JsonParseException, IOException, UserProfileParsingException {
	 
		OntCondition conditionalPreferences = new OntCondition(new com.certh.iti.easytv.user.preference.Condition(jsonProfile2));

	 
		System.out.println(conditionalPreferences.toString());
	    Assert.assertNotNull(conditionalPreferences);
	}

	
	@Test
	public void Test_rule_true() 
	  throws JsonParseException, IOException {
	 
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new MergePreferences());
		System.out.println("Ontology was loaded");
		
		
		//user
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume"));
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		
		//Add conditional preferences
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();
		
		Property isTrue = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrue, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(OntCondition.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//and
		OntClass andClass = model.getOntClass(OntCondition.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = model.getProperty(OntCondition.HAS_RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		andInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//conditional
		OntClass conditionalPreferenceClass = model.getOntClass(OntCondition.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(OntCondition.HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, andInstance) ;
		
		Property hasNameProperty = model.getProperty(OntPreference.HAS_NAME_PROP);
		conditionalPreferenceInstance.addProperty(hasNameProperty, "condition_1") ;
	
		Property hasFontSizeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/application/cs/ui/text/size"));
		conditionalPreferenceInstance.addProperty(hasFontSizeProperty, model.createTypedLiteral(500)) ;

		conditionalPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(600));
		
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		StmtIterator list = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getInt(), 600);
		Assert.assertFalse(list.hasNext());

		//check font size
		list = inf.listStatements(userPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getInt(), 500);
		Assert.assertFalse(list.hasNext());
	
	}
	
	
	@Test
	public void Test_rule_false() 
	  throws JsonParseException, IOException {
	 
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register(new MergePreferences());
		System.out.println("Ontology was loaded");
		
		
		//user
		OntClass userPreferenceClass = model.getOntClass(OntUserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/common/volume"));
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		OntClass userClass = model.getOntClass(OntUserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(OntUserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		
		//Add conditional preferences
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "GT");
		Individual gtInstance = gtClass.createIndividual();
		
		Property isTrue = model.getProperty(OntCondition.IS_TURE_PROP);
		gtInstance.addProperty(isTrue, model.createTypedLiteral(true));
		
		//lt
		OntClass ltClass = model.getOntClass(OntCondition.NAMESPACE + "LT");
		Individual ltInstance = ltClass.createIndividual();
		
		ltInstance.addProperty(isTrue, model.createTypedLiteral(true));

		
		//and
		OntClass andClass = model.getOntClass(OntCondition.NAMESPACE + "AND");
		Individual andInstance = andClass.createIndividual();
		
		Property leftOperandProperty = model.getProperty(OntCondition.HAS_LEFT_OPERAND_PROP);
		andInstance.addProperty(leftOperandProperty, ltInstance);
		
		Property rightOperandProperty = model.getProperty(OntCondition.HAS_RIGHT_OPERAND_PROP);
		andInstance.addProperty(rightOperandProperty, gtInstance);		
		
		andInstance.addProperty(isTrue, model.createTypedLiteral(false));

		
		//conditional
		OntClass conditionalPreferenceClass = model.getOntClass(OntCondition.ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(OntCondition.HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, andInstance) ;
		
		Property hasNameProperty = model.getProperty(OntPreference.HAS_NAME_PROP);
		conditionalPreferenceInstance.addProperty(hasNameProperty, "condition_1") ;
	
		Property hasFontSizeProperty = model.getProperty(OntPreference.getDataProperty("http://registry.easytv.eu/application/cs/ui/text/size"));
		conditionalPreferenceInstance.addProperty(hasFontSizeProperty, model.createTypedLiteral(500)) ;

		conditionalPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(600));
		
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
				
		StmtIterator list = inf.listStatements(userPreferenceInstance, hasAudioVolumeProperty, (RDFNode)null);
		Assert.assertEquals(list.next().getObject().asLiteral().getInt(), 6);
		Assert.assertFalse(list.hasNext());
		
		
		//check font size
		list = inf.listStatements(userPreferenceInstance, hasFontSizeProperty, (RDFNode)null);
		Assert.assertFalse(list.hasNext());
	
	}

}
