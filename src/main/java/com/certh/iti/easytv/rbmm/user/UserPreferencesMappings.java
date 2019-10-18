package com.certh.iti.easytv.rbmm.user;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.XSDDatatype;

public class UserPreferencesMappings {
	
	private static final String OWL = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	
	public static final HashMap<String, String> dataPropertyToUri  =  new HashMap<String, String>() {{
		put(OWL+ "hasLanguage", "http://registry.easytv.eu/common/language");
		put(OWL+ "hasContentAudioLanguage", "http://registry.easytv.eu/common/content/audio/language");
		put(OWL+ "hasCursorSize", "http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size");
		put(OWL+ "hasTTSAudioLanguage", "http://registry.easytv.eu/application/tts/audio/language");
		put(OWL+ "hasTTSAudioSpeed", "http://registry.easytv.eu/application/tts/audio/speed");
		put(OWL+ "hasTTSAudioVolume", "http://registry.easytv.eu/application/tts/audio/volume");
		put(OWL+ "hasTTSAudioVoice", "http://registry.easytv.eu/application/tts/audio/voice");
		put(OWL+ "hasTTSAudioQuality", "http://registry.easytv.eu/application/tts/audio/quality");
		put(OWL+ "hasCSSubtitlesLanguage", "http://registry.easytv.eu/application/cs/cc/subtitles/language");
		put(OWL+ "hasCSSubtitlesFontSize", "http://registry.easytv.eu/application/cs/cc/subtitles/font/size");
		put(OWL+ "hasCSSubtitlesFontColor", "http://registry.easytv.eu/application/cs/cc/subtitles/font/color");
		put(OWL+ "hasCSSubtitlesBackgroundColor", "http://registry.easytv.eu/application/cs/cc/subtitles/background/color");
		put(OWL+ "hasCSCCAudioSubtitle", "http://registry.easytv.eu/application/cs/cc/audio/subtitle");
		put(OWL+ "hasCSUIAudioAssistanceBasedOnTTS", "http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS");
		put(OWL+ "hasCSUITestSize", "http://registry.easytv.eu/application/cs/ui/text/size");
		put(OWL+ "hasCSUILanguage", "http://registry.easytv.eu/application/cs/ui/language");
		put(OWL+ "hasCSUIVibrationTouch", "http://registry.easytv.eu/application/cs/ui/vibration/touch");
		put(OWL+ "hasCSUITextMagnificationScale", "http://registry.easytv.eu/application/cs/ui/text/magnification/scale");
		put(OWL+ "hasCSAudioEqBass", "http://registry.easytv.eu/application/cs/audio/eq/bass");
		put(OWL+ "hasCSAudioEqMids", "http://registry.easytv.eu/application/cs/audio/eq/mids");
		put(OWL+ "hasCSAudioEqHighs", "http://registry.easytv.eu/application/cs/audio/eq/highs");
		put(OWL+ "hasCSAudioVolume", "http://registry.easytv.eu/application/cs/audio/volume");
		put(OWL+ "hasCSAudioTrack", "http://registry.easytv.eu/application/cs/audio/track");
		put(OWL+ "hasControlVoice", "http://registry.easytv.eu/application/control/voice");
		put(OWL+ "hasControGazeAndGestureControlType", "http://registry.easytv.eu/application/control/csGazeAndGestureControlType");
		put(OWL+ "hasDisplayCursorColor", "http://registry.easytv.eu/common/display/screen/enhancement/cursor/color");
		put(OWL+ "hasAccessibilityEnhancementImageType", "http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type");
		put(OWL+ "hasAccessibilityAudioDescription", "http://registry.easytv.eu/application/cs/accessibility/audio/description");
		put(OWL+ "hasAccessibilityDetectionSound", "http://registry.easytv.eu/application/cs/accessibility/detection/sound");
		put(OWL+ "hasAccessibilityDetectionText",   "http://registry.easytv.eu/application/cs/accessibility/detection/text");
		put(OWL+ "hasAccessibilityDetectionCharacter", "http://registry.easytv.eu/application/cs/accessibility/detection/character");
		put(OWL+ "hasAccessibilityMagnificationScale", "http://registry.easytv.eu/application/cs/accessibility/magnification/scale");
		put(OWL+ "hasAccessibilitySignLanguage", "http://registry.easytv.eu/application/cs/accessibility/sign/language");
		put(OWL+ "hasVolume", "http://registry.easytv.eu/common/volume");
		put(OWL+ "hasContrast", "http://registry.easytv.eu/common/contrast");
		
		put(OWL + "hasTime", "http://registry.easytv.eu/context/time");
		put(OWL + "hasLocation", "http://registry.easytv.eu/context/location");
		
		//	put(OWL+ "hasAccessibilityDetectionFace", "http://registry.easytv.eu/application/cs/accessibility/detection/face");		
	    //	put(OWL+ "hasCSCCSubtitlesLanguage", "http://registry.easytv.eu/application/cs/cc/subtitles/language");
		//	put(OWL+ "hasAccessibilityDetectionFace", "http://registry.easytv.eu/application/cs/accessibility/detection/face");
		// 	put(OWL+ "", "http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize");
		// 	put(OWL+ "", "http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage");
		//	put(OWL+ "hasControlGaze", "http://registry.easytv.eu/application/control/gaze");
		//	put(OWL+ "hasFontSize", "http://registry.easytv.eu/common/display/screen/enhancement/font/size");
		//	put(OWL+ "hasFontType", "http://registry.easytv.eu/common/display/screen/enhancement/font/type");
		//	put(OWL+ "hasFontColor", "http://registry.easytv.eu/common/display/screen/enhancement/font/color");
		//	put(OWL+ "hasBackgroundColor", "http://registry.easytv.eu/common/display/screen/enhancement/background");
	
    }};
    
	public static final HashMap<String, String> uriToDataProperty  =  new HashMap<String, String>() {{
		
		put("http://registry.easytv.eu/common/language", OWL+ "hasLanguage");
	    put("http://registry.easytv.eu/common/content/audio/language", OWL+ "hasContentAudioLanguage"); 
	    put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size", OWL+ "hasCursorSize"); 
	    put("http://registry.easytv.eu/application/tts/audio/language", OWL+ "hasTTSAudioLanguage"); 
	    put("http://registry.easytv.eu/application/tts/audio/speed", OWL+ "hasTTSAudioSpeed"); 
	    put("http://registry.easytv.eu/application/tts/audio/volume", OWL+ "hasTTSAudioVolume"); 
	    put("http://registry.easytv.eu/application/tts/audio/voice", OWL+ "hasTTSAudioVoice"); 
		put("http://registry.easytv.eu/application/tts/audio/quality", OWL+ "hasTTSAudioQuality"); 
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/language", OWL+ "hasCSSubtitlesLanguage"); 
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/font/size", OWL+ "hasCSSubtitlesFontSize"); 
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/font/color", OWL+ "hasCSSubtitlesFontColor"); 
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/background/color", OWL+ "hasCSSubtitlesBackgroundColor"); 
	    put("http://registry.easytv.eu/application/cs/cc/audio/subtitle", OWL+ "hasCSCCAudioSubtitle");  
	    put("http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS", OWL+ "hasCSUIAudioAssistanceBasedOnTTS");  
	    put("http://registry.easytv.eu/application/cs/ui/text/size", OWL+ "hasCSUITestSize"); 
	    put("http://registry.easytv.eu/application/cs/ui/language", OWL+ "hasCSUILanguage"); 	
	    put("http://registry.easytv.eu/application/cs/ui/vibration/touch", OWL+ "hasCSUIVibrationTouch"); 
	    put("http://registry.easytv.eu/application/cs/ui/text/magnification/scale", OWL+ "hasCSUITextMagnificationScale");  
	    put("http://registry.easytv.eu/application/cs/audio/eq/bass", OWL+ "hasCSAudioEqBass"); 
	    put("http://registry.easytv.eu/application/cs/audio/eq/mids", OWL+ "hasCSAudioEqMids"); 
	    put("http://registry.easytv.eu/application/cs/audio/eq/highs", OWL+ "hasCSAudioEqHighs"); 
	  	put("http://registry.easytv.eu/application/cs/audio/volume", OWL+ "hasCSAudioVolume"); 
	    put("http://registry.easytv.eu/application/cs/audio/track", OWL+ "hasCSAudioTrack"); 
	    put("http://registry.easytv.eu/application/control/voice", OWL+ "hasControlVoice"); 
	    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlType", OWL+ "hasControGazeAndGestureControlType"); 
	    put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/color", OWL+ "hasDisplayCursorColor"); 
	    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize", OWL+ "");  
	    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage", OWL+ ""); 
	    put("http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type", OWL+ "hasAccessibilityEnhancementImageType");  
	    put("http://registry.easytv.eu/application/cs/accessibility/audio/description", OWL+ "hasAccessibilityAudioDescription");  
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/sound", OWL+ "hasAccessibilityDetectionSound");  
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/text", OWL+ "hasAccessibilityDetectionText");  
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/character", OWL+ "hasAccessibilityDetectionCharacter");  
	    put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale", OWL+ "hasAccessibilityMagnificationScale"); 
	    put("http://registry.easytv.eu/application/cs/accessibility/sign/language", OWL+ "hasAccessibilitySignLanguage"); 
		put("http://registry.easytv.eu/common/volume", OWL+ "hasVolume"); 
		put("http://registry.easytv.eu/common/contrast", OWL+ "hasContrast"); 
		
		put("http://registry.easytv.eu/context/time", OWL + "hasTime");
		put("http://registry.easytv.eu/context/location", OWL + "hasLocation");
		
		//	put("http://registry.easytv.eu/common/display/screen/enhancement/font/size", OWL+ "hasFontSize"); 
		//  put("http://registry.easytv.eu/common/display/screen/enhancement/font/type", OWL+ "hasFontType"); 
		//  put("http://registry.easytv.eu/common/display/screen/enhancement/font/color", OWL+ "hasFontColor"); 
		//  put("http://registry.easytv.eu/common/display/screen/enhancement/background", OWL+ "hasBackgroundColor"); 
		//  put("http://registry.easytv.eu/application/cs/cc/subtitles/language", OWL+ "hasCSCCSubtitlesLanguage"); 
		//	put("http://registry.easytv.eu/application/control/gaze", OWL+ "hasControlGaze");  
		//  put("http://registry.easytv.eu/application/cs/accessibility/detection/face", OWL+ "hasAccessibilityDetectionFace");  
		
    }};
    
    
    public static final HashMap<String, XSDDatatype> uriToRDFDataType  =  new HashMap<String, XSDDatatype>() {{
				
    	put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage",	XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize",XSDDatatype.XSDdouble);
		put("http://registry.easytv.eu/application/control/csGazeAndGestureControlType",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/control/voice",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/accessibility/audio/description",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/accessibility/detection/character",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/accessibility/detection/text",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale",XSDDatatype.XSDdouble);
		put("http://registry.easytv.eu/application/cs/accessibility/sign/language",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/audio/eq/bass",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/cs/audio/eq/highs",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/cs/audio/eq/mids",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/cs/audio/track",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/audio/volume",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/cs/cc/audio/subtitle",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/cc/subtitles/background/color",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/cc/subtitles/font/color",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/cc/subtitles/font/size",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/cs/cc/subtitles/language",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/ui/language",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/ui/text/magnification/scale",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/cs/ui/text/size",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/cs/ui/vibration/touch",XSDDatatype.XSDboolean);
		put("http://registry.easytv.eu/application/tts/audio/language",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/tts/audio/quality",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/tts/audio/speed",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/application/tts/audio/voice",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/application/tts/audio/volume",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/common/content/audio/language",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/common/contrast",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/color",XSDDatatype.XSDdouble);
		put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size",XSDDatatype.XSDinteger);
		put("http://registry.easytv.eu/common/language",XSDDatatype.XSDstring);
		put("http://registry.easytv.eu/common/volume",XSDDatatype.XSDinteger);
		
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
