package com.certh.iti.easytv.rbmm.user;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONArray;
import org.json.JSONObject;

public class Content implements Ontological {

	private boolean faceDetection = false;  
	private boolean textDetection = false;
	private boolean soundDetection = false;
	private boolean characterRecognition = false;
	private String[] subtitileLanguage = null;
	private String[] audioLanguage = null;
    private JSONObject jsonObj = null;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "Content";
	
	//Object properties
	public static final String HAS_FACE_DETECTION_PROP = NAMESPACE + "hasFaceDetection";
	public static final String HAS_TEXT_DETECTION_PROP = NAMESPACE + "hasTextDetectuib";
	public static final String HAS_SOUND_DETECTION_PROP = NAMESPACE + "hasSoundDetection";
	public static final String HAS_CHARACTER_RECOGNITION_PROP = NAMESPACE + "hasCharacterRecognition";
	public static final String HAS_SUBTITLE_LANGUAGE_PROP = NAMESPACE + "hasSubtitleLanguage";
	public static final String HAS_AUDIO_LANGUAGE_PROP = NAMESPACE + "hasAudioLanguage";

	
	public Content(JSONObject json) {
		setJSONObject(json);
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			
			if(faceDetection) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/faceDetection", faceDetection);
			}
			
			if(textDetection) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/textDetection", textDetection);
			}
			
			if(soundDetection) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/soundDetection", soundDetection);
			}
			
			if(characterRecognition) {
				jsonObj.put("http://registry.easytv.eu/application/cs/accessibility/characterRecognition", characterRecognition);
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

		if(json.has("http://registry.easytv.eu/application/cs/accessibility/faceDetection")) {
			faceDetection = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/faceDetection");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/textDetection")) {
			textDetection = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/textDetection");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/soundDetection")) {
			soundDetection = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/soundDetection");
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/characterRecognition")) {
			characterRecognition = json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/characterRecognition");
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
	
	public boolean isFaceDetection() {
		return faceDetection;
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
		
		//add face detection
		if(faceDetection) {
			Property hasFaceDetectionProperty = model.getProperty(HAS_FACE_DETECTION_PROP);
			individual.addProperty(hasFaceDetectionProperty, model.createTypedLiteral(faceDetection));
		}
		
		//add text detection
		if(textDetection) {
			Property hasTextDetectionProperty = model.getProperty(HAS_TEXT_DETECTION_PROP);
			individual.addProperty(hasTextDetectionProperty, model.createTypedLiteral(textDetection));
		}
		
		//add sound detection
		if(soundDetection) {
			Property hasSoundDetectionProperty = model.getProperty(HAS_SOUND_DETECTION_PROP);
			individual.addProperty(hasSoundDetectionProperty, model.createTypedLiteral(soundDetection));
		}
		
		//add character recognition
		if(characterRecognition) {
			Property hasCharacterRecognitionProperty = model.getProperty(HAS_CHARACTER_RECOGNITION_PROP);
			individual.addProperty(hasCharacterRecognitionProperty, model.createTypedLiteral(characterRecognition));
		}
		
		//add subtitle language
		if(subtitileLanguage != null) {
			Property hasSubtitleLanguageProperty = model.getProperty(HAS_SUBTITLE_LANGUAGE_PROP);
			
			for(String lang : subtitileLanguage)
				individual.addProperty(hasSubtitleLanguageProperty, model.createTypedLiteral(lang));
		}
		
		//add audio language
		if(audioLanguage != null) {
			Property hasAudioLanguageProperty = model.getProperty(HAS_AUDIO_LANGUAGE_PROP);
			
			for(String lang : audioLanguage)
				individual.addProperty(hasAudioLanguageProperty, model.createTypedLiteral(lang));
		}
		
		return individual;
	}



}
