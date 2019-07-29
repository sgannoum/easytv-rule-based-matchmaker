package com.certh.iti.easytv.rbmm.user;

public interface OntologicalPreference extends Ontological {

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
	
}
