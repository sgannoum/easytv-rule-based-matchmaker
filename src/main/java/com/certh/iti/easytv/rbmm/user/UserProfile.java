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

public class UserProfile extends Ontological{
    
    @JsonProperty("user_preferences")
	private UserPreferences user_preferences;
    
    @JsonProperty("context")
	private UserContext context;
    
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
	
	public UserPreferences getUser_preferences() {
		return user_preferences;
	}


	public void setUser_preferences(UserPreferences user_preferences) {
		this.user_preferences = user_preferences;
	}
	
	@Override
	public String toString() {
		return "User [" + context + ", " + user_preferences+"]";
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model){
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		return createOntologyInstance(model, userInstance);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		
		//Add Auditory ability
		Property hasContextAbility = model.getProperty(HAS_CONTEXT_PROP);
		individual.addProperty(hasContextAbility, context.createOntologyInstance(model));	
		
		//Add user preferences
		Property hasPreferences = model.getProperty(HAS_PREFERENCE_PROP);
		individual.addProperty(hasPreferences, user_preferences.createOntologyInstance(model));	
		
		//Add suggested preferences
		Property hasSuggestedPreferences = model.getProperty(HAS_SUGGESTED_PREFERENCES_PROP);
		individual.addProperty(hasSuggestedPreferences, new SuggestedPreferences().createOntologyInstance(model));	

		return individual;
	}

	
	public static UserProfile read(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UserProfile user = mapper.readValue(json, UserProfile.class);
		
		return user;
	}
	
	public static UserProfile read(File file) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UserProfile user = mapper.readValue(file, UserProfile.class);
		
		return user;
	}
	
	public static UserProfile read(InputStream in) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UserProfile user = mapper.readValue(in, UserProfile.class);
		
		return user;
	}
	
	public static UserProfile read(JSONObject json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UserProfile user = mapper.readValue(json.toString(), UserProfile.class);
		
		return user;
	}



}
