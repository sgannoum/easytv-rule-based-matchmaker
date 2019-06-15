package com.certh.iti.easytv.rbmm.user;

import java.util.HashMap;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;

public class UserPreferencesMappings {

	/*
	 * 
	 * http://registry.easytv.eu/common/language
	 * http://registry.easytv.eu/common/display/screen/enhancement/background
	 * http://registry.easytv.eu/common/display/screen/enhancement/highlight
	 * http://registry.easytv.eu/common/display/screen/enhancement/cursor/size
	 * http://registry.easytv.eu/common/display/screen/enhancement/cursor/colour
	 * http://registry.easytv.eu/common/display/screen/enhancement/cursor/trails
	 * http://registry.easytv.eu/common/display/screen/enhancement/magnification
	 * http://registry.easytv.eu/common/display/screen/enhancement/font/size
	 * http://registry.easytv.eu/common/display/screen/enhancement/font/type
	 * http://registry.easytv.eu/common/display/screen/enhancement/font/color
	 * 
	 * 
	 * http://registry.easytv.eu/common/display/screen/reader/speech_rate
	 * http://registry.easytv.eu/common/display/screen/reader/pitch
	 * http://registry.easytv.eu/common/display/screen/reader/volume
	 * 
	 * 
	 * http://registry.easytv.eu/common/control/voice_recogition/voice_profile
	 * http://registry.easytv.eu/common/control/voice_recogition/microphone_gain
	 * http://registry.easytv.eu/common/control/voice_recogition/dictation
	 * 
	 * http://registry.easytv.eu/common/control/command/confirmation_feedback
	 * 
	 * http://registry.easytv.eu/common/content/audio/volume
	 * http://registry.easytv.eu/common/content/audio/language
	 * 
	 */
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	private static final String DOMAIN_NAME = "http://registry.easytv.eu/";
	
	private static final String COMMON_NS = "http://registry.easytv.eu/common/";
	private static final String DISPLAY_NS = "display/";
	private static final String CONTROL_NS = "control/";
	private static final String CONTENT_NS = "content/";
	private static final String CONTEXT_NS = "context/";

	
	private static final String SCREEN_ENHANCEMENT_NS = "screen/enhancement/";
	private static final String SCREEN_READER_NS = "screen/reader/";
	private static final String VOICE_RECOGNITION_NS = "command/";
	private static final String COMMAND_CONTROL_NS = "voice_recogition/";
	
	public static final HashMap<String, String> dataPropertyToUri  =  new HashMap<String, String>() {{
		
		put("hasLanguage", COMMON_NS +"language");
		
		//Display
		put(NAMESPACE + "hasBackground", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"background");
		put(NAMESPACE + "hasHighlight", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"highlight");
		put(NAMESPACE + "hasCursorSize", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/size");
		put(NAMESPACE + "hasCursorColour", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/colour");
		put(NAMESPACE + "hasCursorTrails", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/trails");
		put(NAMESPACE + "hasMagnification", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"magnification");
		put(NAMESPACE + "hasFontSize", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/size");
		put(NAMESPACE + "hasFontType", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/type");
		put(NAMESPACE + "hasFontColor", COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/color");

		put(NAMESPACE + "hasSpeechRate", COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"speech_rate");
		put(NAMESPACE + "hasPitch", COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"pitch");
		put(NAMESPACE + "hasVolume", COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"volume");
		
		//Control
		put(NAMESPACE + "hasVoiceProfile", COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"voice_profile");
		put(NAMESPACE + "hasMicrophoneGain", COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"microphone_gain");
		put(NAMESPACE + "hasDictation", COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"dictation");
		
		put(NAMESPACE + "hasConfirmationFeedBack", COMMON_NS+ CONTROL_NS + COMMAND_CONTROL_NS+"confirmation_feedback");

		//Content
		put(NAMESPACE + "hasAudioVolume", COMMON_NS + CONTENT_NS + "audio/volume");
		put(NAMESPACE + "hasAudioLanguage", COMMON_NS + CONTENT_NS + "audio/language");
		
		//context
		put(NAMESPACE + "hasTime", DOMAIN_NAME + CONTEXT_NS + "time");
		put(NAMESPACE + "hasLocation", DOMAIN_NAME + CONTEXT_NS + "location");
		
    }};
    
    public static final HashMap<String, String> uriToDataProperty  =  new HashMap<String, String>() {{
		
		put(COMMON_NS +"language", "hasLanguage");
		
		//Display
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"background", NAMESPACE + "hasBackground");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"highlight", NAMESPACE + "hasHighlight");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/size", NAMESPACE + "hasCursorSize");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/colour", NAMESPACE + "hasCursorColour");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/trails", NAMESPACE + "hasCursorTrails");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"magnification", NAMESPACE + "hasMagnification");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/size", NAMESPACE + "hasFontSize");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/type", NAMESPACE + "hasFontType");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/color", NAMESPACE + "hasFontColor");

		put(COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"speech_rate", NAMESPACE + "hasSpeechRate");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"pitch", NAMESPACE + "hasPitch");
		put(COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"volume", NAMESPACE + "hasVolume");
		
		//Control
		put(COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"voice_profile", NAMESPACE + "hasVoiceProfile");
		put(COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"microphone_gain", NAMESPACE + "hasMicrophoneGain");
		put(COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"dictation", NAMESPACE + "hasDictation");
		
		put(COMMON_NS+ CONTROL_NS + COMMAND_CONTROL_NS+"confirmation_feedback", NAMESPACE + "hasConfirmationFeedBack");

		//Content
		put(COMMON_NS + CONTENT_NS + "audio/volume", NAMESPACE + "hasAudioVolume");
		put(COMMON_NS + CONTENT_NS + "audio/language", NAMESPACE + "hasAudioLanguage");
		
		//context
		put(DOMAIN_NAME + CONTEXT_NS + "time", NAMESPACE + "hasTime");
		put(DOMAIN_NAME + CONTEXT_NS + "location", NAMESPACE + "hasLocation");
		
    }};
    
    
    public static final HashMap<String, XSDDatatype> uriToRDFDataType  =  new HashMap<String, XSDDatatype>() {{
				
		//Display
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"background", XSDDatatype.XSDstring);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"highlight", XSDDatatype.XSDstring);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/size", XSDDatatype.XSDinteger);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/colour", XSDDatatype.XSDstring);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"cursor/trails", XSDDatatype.XSDinteger);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"magnification", XSDDatatype.XSDinteger);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/size", XSDDatatype.XSDinteger);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/type", XSDDatatype.XSDstring);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_ENHANCEMENT_NS+"font/color", XSDDatatype.XSDstring);

		put(COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"speech_rate", XSDDatatype.XSDinteger);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"pitch", XSDDatatype.XSDinteger);
		put(COMMON_NS+ DISPLAY_NS + SCREEN_READER_NS+"volume", XSDDatatype.XSDinteger);
		
		//Control
		put(COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"voice_profile", XSDDatatype.XSDboolean);
		put(COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"microphone_gain", XSDDatatype.XSDinteger);
		put(COMMON_NS+ CONTROL_NS + VOICE_RECOGNITION_NS+"dictation", XSDDatatype.XSDinteger);
		
		put(COMMON_NS+ CONTROL_NS + COMMAND_CONTROL_NS+"confirmation_feedback", XSDDatatype.XSDboolean);

		//Content
		put(COMMON_NS + CONTENT_NS + "audio/volume", XSDDatatype.XSDinteger);
		put(COMMON_NS + CONTENT_NS + "audio/language", XSDDatatype.XSDstring);
		
		//context
		put(DOMAIN_NAME + CONTEXT_NS + "time", XSDDatatype.XSDdateTime);
		put(DOMAIN_NAME + CONTEXT_NS + "location", XSDDatatype.XSDstring);
		
    }};
    
    
	public static String getDataProperty(String uri) {
		return UserPreferencesMappings.uriToDataProperty.get(uri);
	}
	
	public static String getURI(String dataProperty) {
		return UserPreferencesMappings.dataPropertyToUri.get(dataProperty);
	}
	
	public static XSDDatatype getXSDDatatype(String dataProperty) {
		return UserPreferencesMappings.uriToRDFDataType.get(dataProperty);
	}
    

}
