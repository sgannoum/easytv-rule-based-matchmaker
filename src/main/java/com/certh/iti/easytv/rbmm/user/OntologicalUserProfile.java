package com.certh.iti.easytv.rbmm.user;

public interface OntologicalUserProfile extends Ontological {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "User";
	
	//Object properties
	public static final String HAS_PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String HAS_CONTEXT_PROP = NAMESPACE + "hasContext";
	public static final String HAS_SUGGESTED_PREFERENCES_PROP = NAMESPACE + "hasSuggestedPreferences";

}
