package com.certh.iti.easytv.rbmm.user;

public interface OntologicalContent extends Ontological {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "Content";
	
	//Object properties
	public static final String HAS_FACE_DETECTION_PROP = NAMESPACE + "hasFaceDetection";
	public static final String HAS_TEXT_DETECTION_PROP = NAMESPACE + "hasTextDetectuib";
	public static final String HAS_SOUND_DETECTION_PROP = NAMESPACE + "hasSoundDetection";
	public static final String HAS_CHARACTER_RECOGNITION_PROP = NAMESPACE + "hasCharacterRecognition";
	public static final String HAS_SUBTITLE_LANGUAGE_PROP = NAMESPACE + "hasSubtitleLanguage";
	public static final String HAS_AUDIO_LANGUAGE_PROP = NAMESPACE + "hasAudioLanguage";

}
