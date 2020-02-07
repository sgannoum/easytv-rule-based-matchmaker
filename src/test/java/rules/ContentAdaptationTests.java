package rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.builtin.NotEquals;

import config.RBMMTestConfig;

public class ContentAdaptationTests {
	
	private OntModel model; //http://www.w3.org/2000/01/rdf-schema#subClassOf
	
	public static final String 	contains_rule = 
			"[ ApplyAdaptationOnContent:" + 
			"	(?service http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?type)\r\n" +
			"	(?type http://www.w3.org/2000/01/rdf-schema#subClassOf http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService)\r\n" +
			"	(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing)\r\n" + 
			"	(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?object)\r\n" + 
			"	(?object http://www.owl-ontologies.com/OntologyEasyTV.owl#contains ?otherObject)\r\n" + 
			"	noValue(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?otherObject)\r\n" + 
			"	->  " + 
			"	print('inferrence ', ?service, ' applies ', ?somthing, ' onContent ', ?otherObject)\r\n" + 
			"	(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?otherObject)\r\n" + 
			"]";
	
	
	//substitution rule: if there is another content accessibility service that does the same enhancement on the same object, then it is considered a substitution service
	public static final String 	substitutions_rule = 
			"[ SubstitutionCreation1:" + 
			"	(?service1 http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?type1)\r\n" +
			"	(?type1 http://www.w3.org/2000/01/rdf-schema#subClassOf http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService)\r\n" +
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing)\r\n" + 
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?object)\r\n" + 
			
			"	(?service2 http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?type2)\r\n" +
			"	(?type2 http://www.w3.org/2000/01/rdf-schema#subClassOf http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService)\r\n" +
			"	(?service2 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing)\r\n" + 
			"	(?service2 http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?object)\r\n" + 

			"	strConcat(?service1, ?service1t)\r\n" + 
			"	strConcat(?service2, ?service2t)\r\n" + 
			"	NE(?service1t, ?service2t, ?res)\r\n" +
			"	noValue(?object http://www.owl-ontologies.com/OntologyEasyTV.owl#contains)\r\n" +  						//allows for minimum common dominator
			"	noValue(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#substitutePartOf ?service2)\r\n" + 
			"	makeTemp(?sub)\r\n" + 
			"	->  " + 
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#substitutePartOf ?service2)\r\n" +
			"	(?sub http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution)\r\n" +
			"	(?sub http://www.owl-ontologies.com/OntologyEasyTV.owl#ofContentAdaptationService ?service1)\r\n" + 
			"	(?sub http://www.owl-ontologies.com/OntologyEasyTV.owl#byContentAdaptationService ?service2)\r\n" + 
			"	print('New substitution ', ?service1, 'by', ?service2, 'where both applies', ?somthing, 'on', ?object)\r\n" + 
			"]" +
			
			
			//add new substitution to the substitution set
			"[ SubstitutionConnection:" + 
			"	(?sub1 http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution)\r\n" +
			"	(?sub2 http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution)\r\n" +
			"	strConcat(?sub1, ?sub1t)\r\n" + 
			"	strConcat(?sub2, ?sub2t)\r\n" + 
			"	NE(?sub1t, ?sub2t, ?res)\r\n" + 
			"	(?sub1 http://www.owl-ontologies.com/OntologyEasyTV.owl#ofContentAdaptationService ?service)\r\n" + 
			"	(?sub2 http://www.owl-ontologies.com/OntologyEasyTV.owl#ofContentAdaptationService ?service)\r\n" + 
			"	(?sub1 http://www.owl-ontologies.com/OntologyEasyTV.owl#byContentAdaptationService ?service1)\r\n" + 
			"	(?sub2 http://www.owl-ontologies.com/OntologyEasyTV.owl#byContentAdaptationService ?service2)\r\n" + 
			"	strConcat(?service1, ?service1t)\r\n" + 
			"	strConcat(?service2, ?service2t)\r\n" + 
			"	NE(?service1t, ?service2t, ?res1)\r\n" + 
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing1)\r\n" + 
			"	(?service2 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing2)\r\n" + 
			"	(?somthing1 http://www.owl-ontologies.com/OntologyEasyTV.owl#excecutedBefore ?somthing2)\r\n" + 
			"	->  " + 
			"	(?sub1 http://www.owl-ontologies.com/OntologyEasyTV.owl#nextSubstitution ?sub2)\r\n" +
			"	print(?somthing1, 'by service', ?service1, 'is before', ?somthing2, 'by service', ?service2, 'for service', ?service)\r\n" + 
			"]" 
			
			;
	
	public static final String 	rules = contains_rule + substitutions_rule;

	
	@BeforeMethod
	public void beforeMethod() throws IOException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("NE", new NotEquals());
		System.out.println("Ontology was loaded");
		
		FileReader reader = new FileReader(new File(getClass().getClassLoader().getResource("ContentServiceStatments.txt").getFile()));
		//read the model
		model.read(reader, null, "N-TRIPLE");
		
		//close
		reader.close();
		
		//print out the model
		//model.write(System.out, "N-TRIPLE");
	}
	
	@Test
	public void containsRuleTest() {
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(contains_rule));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		//check that custom magnification magnifies face
		Resource customeMagnification = model.getResource("http://www.owl-ontologies.com/OntologyEasyTV.owl#CustomMagnification");
		Property on = model.getProperty("http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent");
		Resource resource = model.getResource("http://www.owl-ontologies.com/OntologyEasyTV.owl#Face");
		
		//Check that customMagnification magnifies faces
		Assert.assertTrue(inf.contains(customeMagnification, on, resource));
		
	}
	
	@Test
	public void substitutionRuleTest() {
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		//check that custom magnification magnifies face
		Resource customeMagnification = model.getResource("http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution");
	//	Property hasSubstitutionSet = model.getProperty("http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSubstitutionSet");

		StmtIterator list = inf.listStatements();
	//	StmtIterator list = inf.listStatements(customeMagnification, null, (RDFNode)null);
		//Assert.assertTrue(list.hasNext(), "No such statement "+hasSubstitutionSet.getLocalName());
/*		while (list.hasNext()) {
			System.out.println(list.next());
			//Assert.assertEquals(list.next().getObject().asLiteral().getBoolean(), true);
		}*/
	}

}
