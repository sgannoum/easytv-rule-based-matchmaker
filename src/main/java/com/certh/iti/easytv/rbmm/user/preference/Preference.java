package com.certh.iti.easytv.rbmm.user.preference;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.rbmm.user.UserPreferencesMappings;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Preference implements Ontological {
	
    @JsonProperty("preferences")
	protected Map<String, Object> preferences;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
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
	
	
	public Map<String, Object> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model) {
		
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		Property hasNameProperty = model.getProperty(HAS_NAME_PROP);
		preferenceInstance.addProperty(hasNameProperty, "default") ;
		
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String propertyUri = null;
			
			//only add known properties
			if((propertyUri = UserPreferencesMappings.getDataProperty(entry.getKey())) != null){
				Property p = model.getProperty(propertyUri);
				preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
			}
		}
		
		return preferenceInstance;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model, Individual preferenceInstance) {
				
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Property p = model.getProperty(UserPreferencesMappings.getDataProperty(entry.getKey()));
			
			preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
		}
		
		return preferenceInstance;
	}
	
	
	@Override
	public String toString() {
		String userPreferences = "preferences [\n";
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			userPreferences += entry.getKey();
			userPreferences += ": ";
			userPreferences += entry.getValue().toString();
			userPreferences += ",\n";
		}
		userPreferences += "]";
		
		return userPreferences;
	}

}
