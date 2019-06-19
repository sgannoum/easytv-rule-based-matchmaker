package com.certh.iti.easytv.rbmm.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.certh.iti.easytv.rbmm.user.preference.ConditionalPreference;
import com.certh.iti.easytv.rbmm.user.preference.Preferences;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPreference {
	
    @JsonProperty("default")
	private Preferences defaultPreferences;
    
    @JsonProperty("conditional")
	List<Preferences> conditional;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String ACCESSIBILITY_SERVICE_PROP = NAMESPACE + "hasAccessibilityService";
	public static final String PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String CONDITIONAL_PREFERENCE_PROP = NAMESPACE + "hasConditionalPreferences";
	


	public Preferences getDefaultUserPreferences() {
		return defaultPreferences;
	}
	
	public void setDefaultUserPreferences(Preferences defaultPreferences) {
		this.defaultPreferences = defaultPreferences;
	}
	
	public List<Preferences> getConditional(){
		return conditional;
	}
	
	public void setConditional(List<Preferences> conditional){
		this.conditional = conditional;
	}
	
	@Override
	public String toString() {
		String userPreferences = "user_preferences [\n";
		
		userPreferences += defaultPreferences.toString();
		if(conditional != null)
			userPreferences +=  "\"conditional\": {\r\n"+conditional.toString()+"\r\n}";

		return userPreferences;
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		//Default preferences
		Individual preferenceInstance = defaultPreferences.createOntologyInstance(model);
		
		//Add conditional preferences
		Property hasConditionalPreferences = model.getProperty(CONDITIONAL_PREFERENCE_PROP);
		for(int i = 0; i < conditional.size();i++) {
			Individual conditionalPreference = conditional.get(i).createOntologyInstance(model);
			preferenceInstance.addProperty(hasConditionalPreferences, conditionalPreference);	
		}
		
		return preferenceInstance;
	}
	
	
    @SuppressWarnings("unchecked")
    @JsonProperty("conditional")
    private void unpackNested(List<Object> conditionals) {
    	
    	conditional = new ArrayList<Preferences>();
    	
    	for(int i = 0; i < conditionals.size(); i++) {	
    		LinkedHashMap<String, Object> inst = (LinkedHashMap<String, Object>) conditionals.get(i);
    		
			ConditionalPreference conditionalPreference = new ConditionalPreference();
    		conditionalPreference.setName((String) inst.get("name"));
    		conditionalPreference.setPreferences((LinkedHashMap<String, Object>) inst.get("preferences"));
    		conditionalPreference.setConditions((List<Object>) inst.get("conditions"));
    		conditional.add(conditionalPreference);	
    	}

    }
}
