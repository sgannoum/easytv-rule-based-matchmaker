package com.certh.iti.easytv.rbmm.user;

public interface OntologicalPreference extends OntologicalContent {

	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String HAS_NAME_PROP = NAMESPACE + "hasName";
	public static final String HAS_AUDIO_LANGUAGE_PROP = NAMESPACE + "hasAudioLanguage";
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
	public static final String HAS_VOICE_RECOGNITION_PROP = NAMESPACE + "hasVoiceRecognition";
	
}
