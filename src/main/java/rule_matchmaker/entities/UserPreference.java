package rule_matchmaker.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class UserPreference {
	
	private Map<String, Object> preferences;
	private ConditionalPreferences conditional_preferences;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	private static final String DOMAIN_NAME = "https://easytvproject.eu/registry/";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String ACCESSIBILITY_SERVICE_PROP = NAMESPACE + "hasAccessibilityService";
	public static final String PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String CONDITIONAL_PREFERENCE_PROP = NAMESPACE + "hasConditionalPreferences";
	
	// Data Properties
	public static final String AUDIO_LANGUAGE_PROP = NAMESPACE + "hasAudioLanguage";
	public static final String AUDIO_VOLUME_PROP = NAMESPACE + "hasAudioVolume";
	public static final String BACKGROUND_PROP = NAMESPACE + "hasBackground";
	public static final String BRIGHTNESS_PROP = NAMESPACE + "hasBrightness";
	public static final String CONFIRMATION_FEED_BACK_PROP = NAMESPACE + "hasConfirmationFeedback";
	public static final String CURSOR_COLOR_PROP = NAMESPACE + "hasCursorColor";
	public static final String CURSOR_SIZE_PROP = NAMESPACE + "hasCursorSize";
	public static final String CURSOR_TRAILS_PROP = NAMESPACE + "hasCursorTails";
	public static final String DICTATION_PROP = NAMESPACE + "hasDictation";
	public static final String FONT_COLOR_PROP = NAMESPACE + "hasFontContrast";
	public static final String FONT_CONTRAST_PROP = NAMESPACE + "hasFontContrast";
	public static final String FONT_TYPE_PROP = NAMESPACE + "hasFontType";
	public static final String FONT_SIZE_PROP = NAMESPACE + "hasFontSize";
	public static final String HIGHLIGHT_PROP = NAMESPACE + "hasHighlight";
	public static final String LOCATION_ATITUDE_PROP = NAMESPACE + "hasLocationAtitude";
	public static final String LOCATION_LONGTITUDE_PROP = NAMESPACE + "hasLocationLongtitude";
	public static final String MAGNIFICATION_PROP = NAMESPACE + "hasMagnification";
	public static final String MICROPHONE_GAIN_PROP = NAMESPACE + "hasMicrophoneGain";
	public static final String PITCH_PROP = NAMESPACE + "hasPitch";
	public static final String READER_RATE_PROP = NAMESPACE + "hasReaderRate";
	public static final String READER_VOLUME_PROP = NAMESPACE + "hasReaderVolume";
	public static final String SCREEN_READER_PROP = NAMESPACE + "hasScreenReaderRate";
	public static final String SPEECH_RATE_PROP = NAMESPACE + "hasSpeechRate";
	public static final String STYLE_PROP = NAMESPACE + "hasStyle";
	public static final String VOICE_RECOGNITION_PROP = NAMESPACE + "hasVoiceRecognition";

	
	
	public Map<String, Object> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}
	
	public ConditionalPreferences getConditional_preferences() {
		return conditional_preferences;
	}
	public void setConditional_preferences(ConditionalPreferences conditional_preferences) {
		this.conditional_preferences = conditional_preferences;
	}
	
	@Override
	public String toString() {
		String userPreferences = "user_preferences [\n";
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			userPreferences += entry.getKey();
			userPreferences += ": ";
			userPreferences += entry.getValue().toString();
			userPreferences += ",\n";
		}
		userPreferences += "]";
		
		if(conditional_preferences != null)
		userPreferences += conditional_preferences.toString();
		
		return userPreferences;
	}

	public static String getDataProperty(String uri) {
		return UserPreferencesMappings.uriToDataProperty.get(uri);
	}
	
	public static String getURI(String dataProperty) {
		return UserPreferencesMappings.dataPropertyToUri.get(dataProperty);
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Property p = model.getProperty(getDataProperty(entry.getKey()));
			
			preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
		}
		
		//Add conditional preferences
		Property hasConditionalPreferences = model.getProperty(CONDITIONAL_PREFERENCE_PROP);
		preferenceInstance.addProperty(hasConditionalPreferences, conditional_preferences.createOntologyInstance(model));	
		
		return preferenceInstance;
	}
}
