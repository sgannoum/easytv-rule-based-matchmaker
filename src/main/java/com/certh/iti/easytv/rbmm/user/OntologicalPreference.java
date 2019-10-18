package com.certh.iti.easytv.rbmm.user;

public interface OntologicalPreference extends OntologicalContent {

	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String HAS_NAME_PROP = NAMESPACE + "hasName";
/*	public static final String HAS_AUDIO_LANGUAGE_PROP = NAMESPACE + "hasAudioLanguage";
	public static final String HAS_AUDIO_VOLUME_PROP = NAMESPACE + "hasAudioVolume";
	public static final String HAS_BACKGROUND_PROP = NAMESPACE + "hasBackground";
	public static final String HAS_BRIGHTNESS_PROP = NAMESPACE + "hasBrightness";
	public static final String HAS_CONFIRMATION_FEED_BACK_PROP = NAMESPACE + "hasConfirmationFeedback";
	public static final String HAS_CURSOR_COLOR_PROP = NAMESPACE + "hasCursorColor";
	public static final String HAS_CURSOR_SIZE_PROP = NAMESPACE + "hasCursorSize";
	public static final String HAS_CURSOR_TRAILS_PROP = NAMESPACE + "hasCursorTails";
	public static final String HAS_DICTATION_PROP = NAMESPACE + "hasDictation";
	public static final String HAS_FONT_COLOR_PROP = NAMESPACE + "hasFontColor";
	public static final String HAS_FONT_CONTRAST_PROP = NAMESPACE + "hasFontContrast";
	public static final String HAS_FONT_TYPE_PROP = NAMESPACE + "hasFontType";
	public static final String HAS_FONT_SIZE_PROP = NAMESPACE + "hasFontSize";
	public static final String HAS_HIGHLIGHT_PROP = NAMESPACE + "hasHighlight";
	public static final String HAS_LOCATION_ATITUDE_PROP = NAMESPACE + "hasLocationAtitude";
	public static final String HAS_LOCATION_LONGTITUDE_PROP = NAMESPACE + "hasLocationLongtitude";
	public static final String HAS_MAGNIFICATION_PROP = NAMESPACE + "hasMagnification";
	public static final String HAS_MICROPHONE_GAIN_PROP = NAMESPACE + "hasMicrophoneGain";
	public static final String HAS_PITCH_PROP = NAMESPACE + "hasPitch";
	public static final String HAS_READER_RATE_PROP = NAMESPACE + "hasReaderRate";
	public static final String HAS_READER_VOLUME_PROP = NAMESPACE + "hasReaderVolume";
	public static final String HAS_SCREEN_READER_PROP = NAMESPACE + "hasScreenReaderRate";
	public static final String HAS_SPEECH_RATE_PROP = NAMESPACE + "hasSpeechRate";
	public static final String HAS_STYLE_PROP = NAMESPACE + "hasStyle";
	public static final String HAS_VOICE_RECOGNITION_PROP = NAMESPACE + "hasVoiceRecognition";*/
	
	
	public static final String hasLanguage = NAMESPACE + "hasLanguage";
	public static final String hasContentAudioLanguage = NAMESPACE + "hasContentAudioLanguage";
	public static final String hasCursorSize = NAMESPACE + "hasCursorSize";
	public static final String hasTTSAudioLanguage = NAMESPACE + "hasTTSAudioLanguage";
	public static final String hasTTSAudioSpeed = NAMESPACE + "hasTTSAudioSpeed";
	public static final String hasTTSAudioVolume = NAMESPACE + "hasTTSAudioVolume";
	public static final String hasTTSAudioVoice = NAMESPACE + "hasTTSAudioVoice";
	public static final String hasTTSAudioQuality = NAMESPACE + "hasTTSAudioQuality";
	public static final String hasCSSubtitlesLanguage = NAMESPACE + "hasCSSubtitlesLanguage";
	public static final String hasCSSubtitlesFontSize = NAMESPACE + "hasCSSubtitlesFontSize";
	public static final String hasCSSubtitlesFontColor = NAMESPACE + "hasCSSubtitlesFontColor";
	public static final String hasCSSubtitlesBackgroundColor = NAMESPACE + "hasCSSubtitlesBackgroundColor";
	public static final String hasCSCCAudioSubtitle = NAMESPACE + "hasCSCCAudioSubtitle";
	public static final String hasCSUIAudioAssistanceBasedOnTTS = NAMESPACE + "hasCSUIAudioAssistanceBasedOnTTS";
	public static final String hasCSUITestSize = NAMESPACE + "hasCSUITestSize";
	public static final String hasCSUILanguage = NAMESPACE + "hasCSUILanguage";
	public static final String hasCSUIVibrationTouch = NAMESPACE + "hasCSUIVibrationTouch";
	public static final String hasCSUITextMagnificationScale = NAMESPACE + "hasCSUITextMagnificationScale";
	public static final String hasCSAudioEqBass = NAMESPACE + "hasCSAudioEqBass";
	public static final String hasCSAudioEqMids = NAMESPACE + "hasCSAudioEqMids";
	public static final String hasCSAudioEqHighs = NAMESPACE + "hasCSAudioEqHighs";
	public static final String hasCSAudioVolume = NAMESPACE + "hasCSAudioVolume";
	public static final String hasCSAudioTrack = NAMESPACE + "hasCSAudioTrack";
	public static final String hasControlVoice = NAMESPACE + "hasControlVoice";
	public static final String hasControGazeAndGestureControlType = NAMESPACE + "hasControGazeAndGestureControlType";
	public static final String hasDisplayCursorColor = NAMESPACE + "hasDisplayCursorColor";
	public static final String hasAccessibilityEnhancementImageType = NAMESPACE + "hasAccessibilityEnhancementImageType";
	public static final String hasAccessibilityAudioDescription = NAMESPACE + "hasAccessibilityAudioDescription";
	public static final String hasAccessibilityDetectionSound = NAMESPACE + "hasAccessibilityDetectionSound";
	public static final String hasAccessibilityDetectionText = NAMESPACE + "hasAccessibilityDetectionText";
	public static final String hasAccessibilityDetectionCharacter = NAMESPACE + "hasAccessibilityDetectionCharacter";
	public static final String hasAccessibilityMagnificationScale = NAMESPACE + "hasAccessibilityMagnificationScale";
	public static final String hasAccessibilitySignLanguage = NAMESPACE + "hasAccessibilitySignLanguage";
	public static final String hasVolume = NAMESPACE + "hasVolume";
	public static final String hasContrast = NAMESPACE + "hasContrast";
	
	
}
