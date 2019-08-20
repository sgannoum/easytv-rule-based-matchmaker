package com.certh.iti.easytv.rbmm.user;

public interface OntologicalUserPreferences extends Ontological {

	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String HAS_ACCESSIBILITY_SERVICE_PROP = NAMESPACE + "hasAccessibilityService";
	public static final String HAS_PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String HAS_CONDITIONAL_PREFERENCE_PROP = NAMESPACE + "hasConditionalPreferences";

}
