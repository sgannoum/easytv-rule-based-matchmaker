package reasoner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Resource;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.rbmm.webservice.RBMM_config;
import com.certh.iti.easytv.user.UserContent;
import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

public class RuleReasonerTest {

	private RuleReasoner ruleReasoner;
	
	@Test
	public void test1() throws IOException {
		
		ruleReasoner = new RuleReasoner(RBMM_config.ONTOLOGY_NAME, RBMM_config.RULES_FILE);
		OntModel ontModel = ruleReasoner.getOntModel();
		OntClass userPreferences = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#UserPreferences");
		OntClass userContext = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#UserContext");
		OntClass content = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#Content");

		//add preference predicates
		for(Entry<String, Attribute> entry :  Preference.preferencesAttributes.entrySet()) {
			Attribute value = entry.getValue();
			String key = entry.getKey();
			String uri =  OntPreference.getDataProperty(key);
			OntProperty propertyImple =  ontModel.createOntProperty(uri);
			Resource range = ontModel.getResource(value.getXMLDataTypeURI());
			
			propertyImple.addDomain(userPreferences);
			propertyImple.setRange(range);
			
			System.out.println(propertyImple);
		}
		
		//add context predicates
		for(Entry<String, Attribute> entry :  UserContext.contextAttributes.entrySet()) {
			Attribute value = entry.getValue();
			String key = entry.getKey();
			String uri =  OntUserContext.getDataProperty(key);
			OntProperty propertyImple =  ontModel.createOntProperty(uri);
			Resource range = ontModel.getResource(value.getXMLDataTypeURI());
			
			propertyImple.addDomain(userContext);
			propertyImple.setRange(range);
			
			System.out.println(propertyImple);
		}
		
		//add content
		for(Entry<String, Attribute> entry :  UserContent.content_attributes.entrySet()) {
			Attribute value = entry.getValue();
			String key = entry.getKey();
			String uri =  Content.getDataProperty(key);
			OntProperty propertyImple =  ontModel.createOntProperty(uri);
			Resource range = ontModel.getResource(value.getXMLDataTypeURI());
			
			propertyImple.addDomain(content);
			propertyImple.setRange(range);
			
			System.out.println(propertyImple);
		}
		
		File file = new File("C:\\Users\\salgan\\Desktop\\EasyTV.owl");
		FileOutputStream out = new FileOutputStream(file);
		ontModel.write(out);
	}
	
	
}
