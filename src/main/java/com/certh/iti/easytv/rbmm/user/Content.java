package com.certh.iti.easytv.rbmm.user;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.user.UserContent;

public class Content implements Ontological {

	private UserContent userContent;
	private boolean faceDetection = false;  
	private boolean textDetection = false;
	private boolean soundDetection = false;
	private boolean characterRecognition = false;
	private String[] subtitileLanguage = null;
	private String[] audioLanguage = null;
    private JSONObject jsonObj = null;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "Content";

	public Content(JSONObject json) {
		setJSONObject(json);
	}
	
	/**
	 * 
	 * @param dataProperty
	 * @return
	 */
	public static String getURI(String dataProperty) {
		String uri = dataProperty.replace(Ontological.NAMESPACE, "http://registry.easytv.eu/").replace("has_", "").replace("_", "/");
		
		if(!UserContent.getAttributes().containsKey(uri))
			throw new IllegalArgumentException("Unknown context uri "+uri);
		
		return uri;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public static String getPredicate(String uri) {
		if(!UserContent.getAttributes().containsKey(uri))
			throw new IllegalArgumentException("Unknown context uri "+uri);
		
		return Ontological.NAMESPACE + uri.replaceAll("http://registry.easytv.eu/", "has_").replace("/","_");
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			
			if(faceDetection) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/detection/face", faceDetection);
			}
			
			if(textDetection) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/detection/text", textDetection);
			}
			
			if(soundDetection) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/detection/sound", soundDetection);
			}
			
			if(characterRecognition) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/detection/character", characterRecognition);
			}
			
			if(subtitileLanguage != null) {
				JSONArray subtitles = new JSONArray();
				
				for(String lang : subtitileLanguage) subtitles.put(lang);
					
				jsonObj.put("http://registry.easytv.eu/application/cs/cc/subtitles/language", subtitles);
			}
			
			
			if(audioLanguage != null) {
				JSONArray audioLangs = new JSONArray();
				
				for(String lang : audioLanguage) audioLangs.put(lang);
					
				jsonObj.put("http://registry.easytv.eu/application/cs/audio/track", audioLanguage);
			}	
		}
		
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) {

		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/face")) {
			faceDetection = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/face");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/text")) {
			textDetection = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/text");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/sound")) {
			soundDetection = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/sound");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/character")) {
			characterRecognition = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/character");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/cc/subtitles/language")) {
			JSONArray subtitles = json.getJSONArray("http://registry.easytv.eu/application/cs/cc/subtitles/language");
			
			subtitileLanguage = new String[subtitles.length()];
			
			for(int i = 0; i < subtitles.length(); i++) subtitileLanguage[i] = subtitles.getString(i);
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/audio/track")) {
			JSONArray audioLanguages = json.getJSONArray("http://registry.easytv.eu/application/cs/audio/track");
			
			audioLanguage = new String[audioLanguages.length()];
			
			for(int i = 0; i < audioLanguages.length(); i++) audioLanguage[i] = audioLanguages.getString(i);
		}
		
		jsonObj = json;
	}
		
	@Override
	public Individual createOntologyInstance(OntModel model) {
		//create the new user in the ontology
		OntClass contextClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual contextInstance = contextClass.createIndividual();
		
		return createOntologyInstance(model, contextInstance);
	}

	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		
		//content always has audio subtitles
		Property hasAudioSubtitilesProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/cc/audio/subtitle"));
		individual.addProperty(hasAudioSubtitilesProperty, model.createTypedLiteral(true));
		
		//add face detection
		if(faceDetection) {
			Property hasFaceDetectionProperty = model.getProperty(getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/face"));
			individual.addProperty(hasFaceDetectionProperty, model.createTypedLiteral(faceDetection));
		}

		//add text detection
		if(textDetection) {
			Property hasTextDetectionProperty = model.getProperty(getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/text"));
			individual.addProperty(hasTextDetectionProperty, model.createTypedLiteral(textDetection));
		}

		//add sound detection
		if(soundDetection) {
			Property hasSoundDetectionProperty = model.getProperty(getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/sound"));
			individual.addProperty(hasSoundDetectionProperty, model.createTypedLiteral(soundDetection));
		}

		//add character recognition
		if(characterRecognition) {
			Property hasCharacterRecognitionProperty = model.getProperty(getPredicate("http://registry.easytv.eu/application/cs/accessibility/detection/character"));
			individual.addProperty(hasCharacterRecognitionProperty, model.createTypedLiteral(characterRecognition));
		}

		//add subtitle language
		if(subtitileLanguage != null) {
			Property hasSubtitleLanguageProperty = model.getProperty(getPredicate("http://registry.easytv.eu/application/cs/cc/subtitles/language"));
			Property hasSignLanguageProperty = model.getProperty(OntPreference.getPredicate("http://registry.easytv.eu/application/cs/accessibility/sign/language"));

			//Available sign lanugages are the same with available subtitles languages
			for(String lang : subtitileLanguage) {
				individual.addProperty(hasSubtitleLanguageProperty, model.createTypedLiteral(lang));
				individual.addProperty(hasSignLanguageProperty, model.createTypedLiteral(lang));
			}
		}
		
		//add audio language
		if(audioLanguage != null) {
			Property hasAudioLanguageProperty = model.getProperty(getPredicate("http://registry.easytv.eu/application/cs/audio/track"));
			
			for(String lang : audioLanguage)
				individual.addProperty(hasAudioLanguageProperty, model.createTypedLiteral(lang));
		}
		
		return individual;
	}

}
