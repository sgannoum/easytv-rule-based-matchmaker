package comparatorOperand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;

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

import com.certh.iti.easytv.rbmm.builtin.Equals;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.preference.OntCondition;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;

import config.RBMMTestConfig;

public class EqualsRulesTest {
	
	private OntModel model;
	public static final String 	rules = "[Equals_pref:" + 
		    "(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#EQ)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue ?value)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType ?type)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?pref)" + 
		    ",(?pref ?type ?nodeValue)" + 
		    "->" + 
			"	EQ(?nodeValue, ?value, ?res)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?res)" +
		    "	print('Equals', ?nodeValue, ?value, ?res)"+
		    "]" + 
		    "[Equals_context:" + 
		    "(?cond http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#EQ)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue ?value)" + 
		    ",(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType ?type)" + 
		    ",(?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_CONTEXT_PROP+" ?context)" + 
		    ",(?context ?type ?nodeValue)" + 
		    "->" + 
			"	EQ(?nodeValue, ?value, ?res)"+
		    "	(?cond http://www.owl-ontologies.com/OntologyEasyTV.owl#isTrue ?res)" +
		    "	print('Equals', ?type, ?nodeValue, ?value, ?res)"+
		    "]"
		    ;
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("EQ", new Equals());
		System.out.println("Ontology was loaded");
		
		//user context
/*		OntClass userContextClass = model.getOntClass(UserContext.ONTOLOGY_CLASS_URI);
		Individual  userContextInstance = userContextClass.createIndividual();
		
		Property hasTimeProperty = model.getProperty(UserContext.HAS_TIME_PROP);
		userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral("2019-05-30T09:47:47.619Z"));
		
		//user
		OntClass userPreferenceClass = model.getOntClass(UserPreferences.ONTOLOGY_CLASS_URI);
		Individual  userPreferenceInstance = userPreferenceClass.createIndividual();
		
		Property hasAudioVolumeProperty = model.getProperty(Preference.HAS_AUDIO_VOLUME_PROP);
		userPreferenceInstance.addProperty(hasAudioVolumeProperty, model.createTypedLiteral(6));
		
		OntClass userClass = model.getOntClass(UserProfile.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		Property hasPreferenceProperty = model.getProperty(UserProfile.HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferenceProperty, userPreferenceInstance);
		
		Property hasContextProperty = model.getProperty(UserProfile.HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextProperty, userContextInstance);
		
		model.write(System.out, "N-TRIPLE");
*/
		
		//These statements replace the above ones
		StringReader  str = new StringReader(
				"_:userContextInstance <"+OntUserContext.getPredicate("http://registry.easytv.eu/context/time")+"> \"2019-05-30T09:47:47.619Z\" .\r\n" + 
				"_:userContextInstance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.owl-ontologies.com/OntologyEasyTV.owl#UserContext> .\r\n" + 
				"_:userPreferenceInstance <"+OntPreference.getPredicate("http://registry.easytv.eu/common/volume")+"> \"6\"^^<http://www.w3.org/2001/XMLSchema#int> .\r\n" + 
				"_:userPreferenceInstance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.owl-ontologies.com/OntologyEasyTV.owl#UserPreferences> .\r\n" + 
				"_:userInstance <http://www.owl-ontologies.com/OntologyEasyTV.owl#hasContext> _:userContextInstance .\r\n" + 
				"_:userInstance <http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference> _:userPreferenceInstance .\r\n" + 
				"_:userInstance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.owl-ontologies.com/OntologyEasyTV.owl#User> .");
		model.read(str, null,"N-TRIPLE");
		
	}

	@Test
	public void Test_greaterThanIsTrue()  {
		
		//gt
/*		OntClass gtClass = model.getOntClass(Condition.NAMESPACE + "EQ");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(Condition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(UserPreferencesMappings.getDataProperty("http://registry.easytv.eu/common/volume")));
				
		Property hasValueProperty = model.getProperty(Condition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(6));
*/
		
		StringReader  str = new StringReader(
				"_:EqInstance <http://www.owl-ontologies.com/OntologyEasyTV.owl#hasType> <"+OntPreference.getPredicate("http://registry.easytv.eu/common/volume")+"> .\r\n" + 
				"_:EqInstance <http://www.owl-ontologies.com/OntologyEasyTV.owl#hasValue> \"6\"^^<http://www.w3.org/2001/XMLSchema#int> .\r\n" + 
				"_:EqInstance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.owl-ontologies.com/OntologyEasyTV.owl#EQ> .\r\n");
		model.read(str, null,"N-TRIPLE");

		
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
	public void Test_greaterThanIsFalse()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "EQ");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntPreference.getPredicate("http://registry.easytv.eu/common/volume")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral(7));
		
		
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
	public void Test_greaterThan_UserContext_Date_IsTrue()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "EQ");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntUserContext.getPredicate("http://registry.easytv.eu/context/time")));
				
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
	public void Test_greaterThan_UserContext_Date_IsFalse()  {
		
		//gt
		OntClass gtClass = model.getOntClass(OntCondition.NAMESPACE + "EQ");
		Individual gtInstance = gtClass.createIndividual();

		Property hasTypeProperty = model.getProperty(OntCondition.HAS_TYPE_PROP);
		gtInstance.addProperty(hasTypeProperty, model.createProperty(OntUserContext.getPredicate("http://registry.easytv.eu/context/time")));
				
		Property hasValueProperty = model.getProperty(OntCondition.HAS_VALUE_PROP);
		gtInstance.addProperty(hasValueProperty, model.createTypedLiteral("2014-12-12T10:39:40Z" ));
		 
		
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
