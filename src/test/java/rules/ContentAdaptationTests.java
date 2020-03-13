package rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

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
		
	public static final String 	contains_rule = 
			"[ ApplyAdaptationOnContent:" + 
			"	(?service http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?type)" +
			"	(?type http://www.w3.org/2000/01/rdf-schema#subClassOf http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService)" +
			"	(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing)" + 
			"	(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?object)" + 
			"	(?object http://www.owl-ontologies.com/OntologyEasyTV.owl#contains ?otherObject)" + 
			"	noValue(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?otherObject)" + 
			"	->  " + 
		//	"	print('inferrence ', ?service, ' applies ', ?somthing, ' onContent ', ?otherObject)" + 
			"	(?service http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?otherObject)" + 
			"]";
	
	
	//substitution rule: if there is another content accessibility service that does the same enhancement on the same object, then it is considered a substitution service
	public static final String 	substitutions_rule = 
			"[ SubstitutionCreation1:" + 
			"	(?service1 http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?type1)" +
			"	(?type1 http://www.w3.org/2000/01/rdf-schema#subClassOf http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService)" +
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing)" + 
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?object)" + 
			
			"	(?service2 http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?type2)" +
			"	(?type2 http://www.w3.org/2000/01/rdf-schema#subClassOf http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService)" +
			"	(?service2 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing)" + 
			"	(?service2 http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent ?object)" + 

			"	strConcat(?service1, ?service1t)" + 
			"	strConcat(?service2, ?service2t)" + 
			"	NE(?service1t, ?service2t, ?res)" +
			"	noValue(?object http://www.owl-ontologies.com/OntologyEasyTV.owl#contains)" +  						//allows for minimum common dominator
			"	noValue(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#substitutePartOf ?service2)" + 
			"	makeTemp(?sub)" + 
			"	->  " + 
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#substitutePartOf ?service2)" +
			"	(?sub http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution)" +
			"	(?sub http://www.owl-ontologies.com/OntologyEasyTV.owl#ofContentAdaptationService ?service1)" + 
			"	(?sub http://www.owl-ontologies.com/OntologyEasyTV.owl#byContentAdaptationService ?service2)" + 
			"	print('New substitution ', ?service1, 'by', ?service2, 'where both applies', ?somthing, 'on', ?object)" + 
			"]" +
			
			
			//add new substitution to the substitution set
			"[ SubstitutionConnection:" + 
			"	(?sub1 http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution)" +
			"	(?sub2 http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.owl-ontologies.com/OntologyEasyTV.owl#Substitution)" +
			"	strConcat(?sub1, ?sub1t)" + 
			"	strConcat(?sub2, ?sub2t)" + 
			"	NE(?sub1t, ?sub2t, ?res)" + 
			"	(?sub1 http://www.owl-ontologies.com/OntologyEasyTV.owl#ofContentAdaptationService ?service)" + 
			"	(?sub2 http://www.owl-ontologies.com/OntologyEasyTV.owl#ofContentAdaptationService ?service)" + 
			"	(?sub1 http://www.owl-ontologies.com/OntologyEasyTV.owl#byContentAdaptationService ?service1)" + 
			"	(?sub2 http://www.owl-ontologies.com/OntologyEasyTV.owl#byContentAdaptationService ?service2)" + 
			"	strConcat(?service1, ?service1t)" + 
			"	strConcat(?service2, ?service2t)" + 
			"	NE(?service1t, ?service2t, ?res1)" + 
			"	(?service1 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing1)" + 
			"	(?service2 http://www.owl-ontologies.com/OntologyEasyTV.owl#applies ?somthing2)" + 
			"	(?somthing1 http://www.owl-ontologies.com/OntologyEasyTV.owl#excecutedBefore ?somthing2)" + 
			"	->  " + 
			"	(?sub1 http://www.owl-ontologies.com/OntologyEasyTV.owl#nextSubstitution ?sub2)" +
			"	print(?somthing1, 'by service', ?service1, 'is before', ?somthing2, 'by service', ?service2, 'for service', ?service)" + 
			"]" 
			
			;
	
	public static final String 	rules = contains_rule + substitutions_rule;


	
	@Test
	public void content_adpatation_contains_rule_Test() throws IOException {
		
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("NE", new NotEquals());
		System.out.println("Ontology was loaded");
		
		//read the model
		FileReader reader = new FileReader(new File(getClass().getClassLoader().getResource("ContentServiceStatments.txt").getFile()));
		model.read(reader, null, "N3");
		
		//close
		reader.close();
		
		
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
	public void content_adpatation_rule_test() throws IOException {
		File file = new File(RBMMTestConfig.ONTOLOGY_File);
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		BuiltinRegistry.theRegistry.register("NE", new NotEquals());
		System.out.println("Ontology was loaded");
		
		//read the model
		StringReader reader = new StringReader(""
				+ "<http://www.owl-ontologies.com/OntologyEasyTV.owl#FaceDetection> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.owl-ontologies.com/OntologyEasyTV.owl#TransformalContentAccessibilityService> ;"
				+ "															      	<http://www.owl-ontologies.com/OntologyEasyTV.owl#applies> <http://www.owl-ontologies.com/OntologyEasyTV.owl#Detection>, <http://www.owl-ontologies.com/OntologyEasyTV.owl#Magnification> ;"
				+ "															  	  	<http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent> <http://www.owl-ontologies.com/OntologyEasyTV.owl#Face> ."
				+ ""
				+ ""
				+ "<http://www.owl-ontologies.com/OntologyEasyTV.owl#CustomMagnification> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.owl-ontologies.com/OntologyEasyTV.owl#ContentAccessibilityService> ;"
				+ "															  			 <http://www.owl-ontologies.com/OntologyEasyTV.owl#applies> <http://www.owl-ontologies.com/OntologyEasyTV.owl#Magnification> ;"
				+ "															 		 	 <http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent> <http://www.owl-ontologies.com/OntologyEasyTV.owl#Video> ."
				+ "");
		
		model.read(reader, null, "N3");
		
		//close
		reader.close();
		
		
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(contains_rule));
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		
		//check that custom magnification magnifies face
		Resource customeMagnification = model.getResource("http://www.owl-ontologies.com/OntologyEasyTV.owl#CustomMagnification");
		Property on = model.getProperty("http://www.owl-ontologies.com/OntologyEasyTV.owl#onContent");
		Resource resource = model.getResource("http://www.owl-ontologies.com/OntologyEasyTV.owl#Face");
		
		//Check that customMagnification magnifies faces
		Assert.assertTrue(inf.contains(customeMagnification, on, resource));
	}

}
