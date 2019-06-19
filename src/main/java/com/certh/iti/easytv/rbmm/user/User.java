package com.certh.iti.easytv.rbmm.user;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {
    
    @JsonProperty("user_preferences")
	private UserPreference user_preferences;
    
    @JsonProperty("context")
	private UserContext context;
    
    public static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "User";
	
	//Object properties
	public static final String HAS_PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String HAS_CONTEXT_PROP = NAMESPACE + "hasContext";
	public static final String HAS_SUGGESTED_PREFERENCES_PROP = NAMESPACE + "hasSuggestedPreferences";


	
	public UserContext getContext() {
		return context;
	}


	public void setContext(UserContext context) {
		this.context = context;
	}
	
	public UserPreference getUser_preferences() {
		return user_preferences;
	}


	public void setUser_preferences(UserPreference user_preferences) {
		this.user_preferences = user_preferences;
	}
	
	@Override
	public String toString() {
		return "User [" + context + ", " + user_preferences+"]";
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		//Add Auditory ability
		Property hasContextAbility = model.getProperty(HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextAbility, context.createOntologyInstance(model));	
		
		//Add user preferences
		Property hasPreferences = model.getProperty(HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferences, user_preferences.createOntologyInstance(model));	
		
		//Add suggested preferences
		Property hasSuggestedPreferences = model.getProperty(HAS_SUGGESTED_PREFERENCES_PROP);
		userInstance.addProperty(hasSuggestedPreferences, SuggestedPreferences.createOntologyInstance(model));	

		return userInstance;
	}
	
	public static User read(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(json, User.class);
		
		return user;
	}
	
	public static User read(File file) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(file, User.class);
		
		return user;
	}
	
	public static User read(InputStream in) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(in, User.class);
		
		return user;
	}
	
	public static User read(JSONObject json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(json.toString(), User.class);
		
		return user;
	}

}
