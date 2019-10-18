package com.certh.iti.easytv.rbmm.user;

import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;


public class UserProfile implements OntologicalUserProfile{
    
	private UserPreferences userPreferences;
	private JSONObject jsonObj = null;

	public UserProfile(JSONObject json) throws IOException {
		jsonObj = null;
		setJSONObject(json);
	}
	
	public UserPreferences getUser_preferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences user_preferences) {
		this.userPreferences = user_preferences;
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("user_preferences", userPreferences.getJSONObject());
		}
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) {	
		
		//if(!json.has("user_preferences")) TO-DO send an error messag
		userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		
		jsonObj = json;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model) {
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		return createOntologyInstance(model, userInstance);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		
		//Add user preferences
		Property hasPreferences = model.getProperty(HAS_PREFERENCE_PROP);
		individual.addProperty(hasPreferences, userPreferences.createOntologyInstance(model));	
		
		//Add suggested preferences
		Property hasSuggestedPreferences = model.getProperty(HAS_SUGGESTED_PREFERENCES_PROP);
		individual.addProperty(hasSuggestedPreferences, new SuggestedPreferences().createOntologyInstance(model));	

		return individual;
	}

}
