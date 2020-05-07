package entities;

import org.apache.jena.ontology.OntModel;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;

public class ContentText {
	
	private OntModel model;
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"2019-05-30T09:47:47.619Z\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"}");
	
	
	public static final String rules = 
	"[face_detection_preference:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    " (?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?content "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"->" + 
		    " (?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face")+" ?faceDetection)" +
			"]" +
			
	"[text_detection_preference:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/text")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?content "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/text")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"->" + 
		    ",(?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/text")+" ?textDetection)" +
			"]" +
			
	"[sound_detection_preference:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/sound")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?content "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/sound")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"->" + 
		    " (?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/sound")+" ?soundDetection)" +
			"]" +
			
	"[character_recognition_preference:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/character")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?content "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/character")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"->" + 
		    " (?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/character")+" ?characterRecognition)" +
			"]" +
		    
	"[suggestions_preference:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/character")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?content "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/character")+" 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"->" + 
		    " (?defPref "+OntPreference.getPredicate("http://registry.easytv.eu/application/cs/ui/text/size")+" '30'^^http://www.w3.org/2001/XMLSchema#integer)" +
			"]"	+
		    
	"[suggest_face_detection_preference:" + 
			" (?user http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+OntUserProfile.ONTOLOGY_CLASS_URI+")" + 
			",(?content  http://www.w3.org/1999/02/22-rdf-syntax-ns#type "+Content.ONTOLOGY_CLASS_URI+")" + 
		    ",(?user "+OntUserProfile.HAS_PREFERENCE_PROP+" ?defPref)" +
		    ",(?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face")+" 'false'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?content "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
		    ",(?defPref "+OntPreference.getPredicate("http://registry.easytv.eu/application/cs/ui/text/size")+" ?magnification)" +
		    ",greaterThen(?magnification, 60, ?res)" +
			"->" + 
		    " (?defPref "+Content.getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face")+" 'true'^^http://www.w3.org/2001/XMLSchema#boolean)" +
			"]"	  
			;
	
	@Test
	public void Test_user_rule_1()  {
		
		
	}
	

}
