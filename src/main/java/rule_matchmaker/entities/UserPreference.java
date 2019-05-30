package rule_matchmaker.entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPreference {
	
    @JsonProperty("default")
	private DefaultUserPreferences defaultPreferences;
    
    @JsonProperty("conditional")
	List<ConditionalPreference> conditional;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	private static final String DOMAIN_NAME = "https://easytvproject.eu/registry/";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String ACCESSIBILITY_SERVICE_PROP = NAMESPACE + "hasAccessibilityService";
	public static final String PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String CONDITIONAL_PREFERENCE_PROP = NAMESPACE + "hasConditionalPreferences";
	
	// Data Properties
	public static final String HAS_NAME_PROP = NAMESPACE + "hasName";
	public static final String AUDIO_LANGUAGE_PROP = NAMESPACE + "hasAudioLanguage";
	public static final String AUDIO_VOLUME_PROP = NAMESPACE + "hasAudioVolume";
	public static final String BACKGROUND_PROP = NAMESPACE + "hasBackground";
	public static final String BRIGHTNESS_PROP = NAMESPACE + "hasBrightness";
	public static final String CONFIRMATION_FEED_BACK_PROP = NAMESPACE + "hasConfirmationFeedback";
	public static final String CURSOR_COLOR_PROP = NAMESPACE + "hasCursorColor";
	public static final String CURSOR_SIZE_PROP = NAMESPACE + "hasCursorSize";
	public static final String CURSOR_TRAILS_PROP = NAMESPACE + "hasCursorTails";
	public static final String DICTATION_PROP = NAMESPACE + "hasDictation";
	public static final String FONT_COLOR_PROP = NAMESPACE + "hasFontColor";
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

	public DefaultUserPreferences getDefaultUserPreferences() {
		return defaultPreferences;
	}
	
	public void setDefaultUserPreferences(DefaultUserPreferences defaultPreferences) {
		this.defaultPreferences = defaultPreferences;
	}
	
	public List<ConditionalPreference> getConditional(){
		return conditional;
	}
	
	public void setConditional(List<ConditionalPreference> conditional){
		this.conditional = conditional;
	}
	
	@Override
	public String toString() {
		String userPreferences = "user_preferences [\n";
		
		userPreferences += defaultPreferences.toString();
		if(conditional != null)
			userPreferences +=  "\"conditional\": {\r\n"+conditional.toString()+"\r\n}";

		return userPreferences;
	}

	public static String getDataProperty(String uri) {
		return UserPreferencesMappings.uriToDataProperty.get(uri);
	}
	
	public static String getURI(String dataProperty) {
		return UserPreferencesMappings.dataPropertyToUri.get(dataProperty);
	}
	
	public static XSDDatatype getXSDDatatype(String dataProperty) {
		return UserPreferencesMappings.uriToRDFDataType.get(dataProperty);
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		//Default preferences
		Individual preferenceInstance = defaultPreferences.createOntologyInstance(model);
		
		//Add conditional preferences
		Property hasConditionalPreferences = model.getProperty(CONDITIONAL_PREFERENCE_PROP);
		for(int i = 0; i < conditional.size();i++) {
			Individual conditionalPreference = conditional.get(i).createOntologyInstance(model);
			preferenceInstance.addProperty(hasConditionalPreferences, conditionalPreference);	
		}
		
		return preferenceInstance;
	}
	
	
    @SuppressWarnings("unchecked")
    @JsonProperty("conditional")
    private void unpackNested(List<Object> conditionals) {
    	
    	conditional = new ArrayList<ConditionalPreference>();
    	
    	for(int i = 0; i < conditionals.size(); i++) {	
    		LinkedHashMap<String, Object> inst = (LinkedHashMap<String, Object>) conditionals.get(i);
    		
			ConditionalPreference conditionalPreference = new ConditionalPreference();
    		conditionalPreference.setName((String) inst.get("name"));
    		conditionalPreference.setPreferences((LinkedHashMap<String, Object>) inst.get("preferences"));
    		conditionalPreference.setConditions((List<Object>) inst.get("conditions"));
    		conditional.add(conditionalPreference);	
    	}

    }
}
