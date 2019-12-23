package com.certh.iti.easytv.rbmm.user.preference;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;

import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.user.preference.Preference;

public class OntPreference implements Ontological {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String HAS_NAME_PROP = 							NAMESPACE + "hasName";
	public static final String hasLanguage = 							NAMESPACE + "hasLanguage";
	public static final String hasContentAudioLanguage = 				NAMESPACE + "hasContentAudioLanguage";
	public static final String hasCursorSize = 							NAMESPACE + "hasCursorSize";
	public static final String hasTTSAudioLanguage =					NAMESPACE + "hasTTSAudioLanguage";
	public static final String hasTTSAudioSpeed = 						NAMESPACE + "hasTTSAudioSpeed";
	public static final String hasTTSAudioVolume = 						NAMESPACE + "hasTTSAudioVolume";
	public static final String hasTTSAudioVoice = 						NAMESPACE + "hasTTSAudioVoice";
	public static final String hasTTSAudioQuality = 					NAMESPACE + "hasTTSAudioQuality";
	public static final String hasCSSubtitlesLanguage = 				NAMESPACE + "hasCSSubtitlesLanguage";
	public static final String hasCSSubtitlesFontSize = 				NAMESPACE + "hasCSSubtitlesFontSize";
	public static final String hasCSSubtitlesFontColor = 				NAMESPACE + "hasCSSubtitlesFontColor";
	public static final String hasCSSubtitlesBackgroundColor =			NAMESPACE + "hasCSSubtitlesBackgroundColor";
	public static final String hasCSCCAudioSubtitle = 					NAMESPACE + "hasCSCCAudioSubtitle";
	public static final String hasCSUIAudioAssistanceBasedOnTTS = 		NAMESPACE + "hasCSUIAudioAssistanceBasedOnTTS";
	public static final String hasCSUITestSize = 						NAMESPACE + "hasCSUITestSize";
	public static final String hasCSUILanguage = 						NAMESPACE + "hasCSUILanguage";
	public static final String hasCSUIVibrationTouch = 					NAMESPACE + "hasCSUIVibrationTouch";
	public static final String hasCSUITextMagnificationScale = 			NAMESPACE + "hasCSUITextMagnificationScale";
	public static final String hasCSAudioEqBass = 						NAMESPACE + "hasCSAudioEqBass";
	public static final String hasCSAudioEqMids = 						NAMESPACE + "hasCSAudioEqMids";
	public static final String hasCSAudioEqHighs = 						NAMESPACE + "hasCSAudioEqHighs";
	public static final String hasCSAudioVolume = 						NAMESPACE + "hasCSAudioVolume";
	public static final String hasCSAudioTrack = 						NAMESPACE + "hasCSAudioTrack";
	public static final String hasControlVoice = 						NAMESPACE + "hasControlVoice";
	public static final String hasControGazeAndGestureControlType = 	NAMESPACE + "hasControGazeAndGestureControlType";
	public static final String hasDisplayCursorColor = 					NAMESPACE + "hasDisplayCursorColor";
	public static final String hasAccessibilityEnhancementImageType = 	NAMESPACE + "hasAccessibilityEnhancementImageType";
	public static final String hasAccessibilityAudioDescription = 		NAMESPACE + "hasAccessibilityAudioDescription";
	public static final String hasAccessibilityDetectionSound = 		NAMESPACE + "hasAccessibilityDetectionSound";
	public static final String hasAccessibilityDetectionText = 			NAMESPACE + "hasAccessibilityDetectionText";
	public static final String hasAccessibilityDetectionCharacter = 	NAMESPACE + "hasAccessibilityDetectionCharacter";
	public static final String hasAccessibilityMagnificationScale = 	NAMESPACE + "hasAccessibilityMagnificationScale";
	public static final String hasAccessibilitySignLanguage = 			NAMESPACE + "hasAccessibilitySignLanguage";
	public static final String hasVolume = 								NAMESPACE + "hasVolume";
	public static final String hasContrast = 							NAMESPACE + "hasContrast";
	
	
	private static final HashMap<String, String> dataPropertyToUri  =  new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
	{
		put(NAMESPACE+ "hasLanguage", 							"http://registry.easytv.eu/common/language");
		put(NAMESPACE+ "hasContentAudioLanguage", 				"http://registry.easytv.eu/common/content/audio/language");
		put(NAMESPACE+ "hasCursorSize", 						"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size");
		put(NAMESPACE+ "hasTTSAudioLanguage", 					"http://registry.easytv.eu/application/tts/audio/language");
		put(NAMESPACE+ "hasTTSAudioSpeed", 						"http://registry.easytv.eu/application/tts/audio/speed");
		put(NAMESPACE+ "hasTTSAudioVolume", 					"http://registry.easytv.eu/application/tts/audio/volume");
		put(NAMESPACE+ "hasTTSAudioVoice", 						"http://registry.easytv.eu/application/tts/audio/voice");
		put(NAMESPACE+ "hasTTSAudioQuality", 					"http://registry.easytv.eu/application/tts/audio/quality");
		put(NAMESPACE+ "hasCSSubtitlesLanguage", 				"http://registry.easytv.eu/application/cs/cc/subtitles/language");
		put(NAMESPACE+ "hasCSSubtitlesFontSize", 				"http://registry.easytv.eu/application/cs/cc/subtitles/font/size");
		put(NAMESPACE+ "hasCSSubtitlesFontColor", 				"http://registry.easytv.eu/application/cs/cc/subtitles/font/color");
		put(NAMESPACE+ "hasCSSubtitlesBackgroundColor", 		"http://registry.easytv.eu/application/cs/cc/subtitles/background/color");
		put(NAMESPACE+ "hasCSCCAudioSubtitle", 					"http://registry.easytv.eu/application/cs/cc/audio/subtitle");
		put(NAMESPACE+ "hasCSUIAudioAssistanceBasedOnTTS", 		"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS");
		put(NAMESPACE+ "hasCSUITestSize", 						"http://registry.easytv.eu/application/cs/ui/text/size");
		put(NAMESPACE+ "hasCSUILanguage", 						"http://registry.easytv.eu/application/cs/ui/language");
		put(NAMESPACE+ "hasCSUIVibrationTouch", 				"http://registry.easytv.eu/application/cs/ui/vibration/touch");
		put(NAMESPACE+ "hasCSUITextMagnificationScale", 		"http://registry.easytv.eu/application/cs/ui/text/magnification/scale");
		put(NAMESPACE+ "hasCSAudioEqBass", 						"http://registry.easytv.eu/application/cs/audio/eq/bass");
		put(NAMESPACE+ "hasCSAudioEqMids", 						"http://registry.easytv.eu/application/cs/audio/eq/mids");
		put(NAMESPACE+ "hasCSAudioEqHighs", 					"http://registry.easytv.eu/application/cs/audio/eq/highs");
		put(NAMESPACE+ "hasCSAudioVolume", 						"http://registry.easytv.eu/application/cs/audio/volume");
		put(NAMESPACE+ "hasCSAudioTrack", 						"http://registry.easytv.eu/application/cs/audio/track");
		put(NAMESPACE+ "hasControlVoice", 						"http://registry.easytv.eu/application/control/voice");
		put(NAMESPACE+ "hasControGazeAndGestureControlType", 	"http://registry.easytv.eu/application/control/csGazeAndGestureControlType");
		put(NAMESPACE+ "hasDisplayCursorColor", 				"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color");
		put(NAMESPACE+ "hasAccessibilityEnhancementImageType", 	"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type");
		put(NAMESPACE+ "hasAccessibilityAudioDescription", 		"http://registry.easytv.eu/application/cs/accessibility/audio/description");
		put(NAMESPACE+ "hasAccessibilityDetectionSound",		"http://registry.easytv.eu/application/cs/accessibility/detection/sound");
		put(NAMESPACE+ "hasAccessibilityDetectionText",   		"http://registry.easytv.eu/application/cs/accessibility/detection/text");
		put(NAMESPACE+ "hasAccessibilityDetectionCharacter", 	"http://registry.easytv.eu/application/cs/accessibility/detection/character");
		put(NAMESPACE+ "hasAccessibilityMagnificationScale", 	"http://registry.easytv.eu/application/cs/accessibility/magnification/scale");
		put(NAMESPACE+ "hasAccessibilitySignLanguage", 			"http://registry.easytv.eu/application/cs/accessibility/sign/language");
		put(NAMESPACE+ "hasVolume", 							"http://registry.easytv.eu/common/volume");
		put(NAMESPACE+ "hasContrast", 							"http://registry.easytv.eu/common/contrast");
		
		put(NAMESPACE + "hasTime", 								"http://registry.easytv.eu/context/time");
		put(NAMESPACE + "hasLocation", 							"http://registry.easytv.eu/context/location");
		
		//	put(NAMESPACE+ "hasAccessibilityDetectionFace", 	"http://registry.easytv.eu/application/cs/accessibility/detection/face");		
	    //	put(NAMESPACE+ "hasCSCCSubtitlesLanguage", 			"http://registry.easytv.eu/application/cs/cc/subtitles/language");
		//	put(NAMESPACE+ "hasAccessibilityDetectionFace", 	"http://registry.easytv.eu/application/cs/accessibility/detection/face");
		// 	put(NAMESPACE+ "", 									"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize");
		// 	put(NAMESPACE+ "", 									"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage");
		//	put(NAMESPACE+ "hasControlGaze", 					"http://registry.easytv.eu/application/control/gaze");
		//	put(NAMESPACE+ "hasFontSize", 						"http://registry.easytv.eu/common/display/screen/enhancement/font/size");
		//	put(NAMESPACE+ "hasFontType", 						"http://registry.easytv.eu/common/display/screen/enhancement/font/type");
		//	put(NAMESPACE+ "hasFontColor", 						"http://registry.easytv.eu/common/display/screen/enhancement/font/color");
		//	put(NAMESPACE+ "hasBackgroundColor", 				"http://registry.easytv.eu/common/display/screen/enhancement/background");
	
    }};
    
    
    private static HashMap<String, String> uriToDataProperty = null;
	protected Preference defaultPreference;
	
	
	/**
	 * 
	 * @param dataProperty
	 * @return
	 */
	public static String getURI(String dataProperty) {
		return dataPropertyToUri.get(dataProperty);
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public static String getDataProperty(String uri) {
	
		//Fill uriToDataProperty
		if(uriToDataProperty == null) {
			uriToDataProperty  =  new HashMap<String, String>();
			
			for(Entry<String, String> entry : dataPropertyToUri.entrySet()) 
				uriToDataProperty.put(entry.getValue(), entry.getKey());
		}
		
		return uriToDataProperty.get(uri);
	}
	
	public OntPreference(Preference defaultPreference) {
		this.defaultPreference = defaultPreference;
	}
	
	public Preference getPreferences(){
		return defaultPreference;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model) {
		
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		Property hasNameProperty = model.getProperty(HAS_NAME_PROP);
		preferenceInstance.addProperty(hasNameProperty, "default") ;
		
		return createOntologyInstance(model, preferenceInstance);
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model, Individual preferenceIndividual) {
			
		String propertyUri = null;
		
		for(Entry<String, Object> entry : defaultPreference.getPreferences().entrySet()) {
			//If known
			if((propertyUri = OntPreference.getDataProperty(entry.getKey())) != null){
				Property property = model.getProperty(propertyUri);
				Literal literal = model.createTypedLiteral(entry.getValue());
				
				//State the preference
				preferenceIndividual.addProperty(property, literal);
			}
		}
		

		return preferenceIndividual;
	}

}
