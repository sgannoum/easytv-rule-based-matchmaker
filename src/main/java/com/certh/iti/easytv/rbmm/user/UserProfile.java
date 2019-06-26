package com.certh.iti.easytv.rbmm.user;

import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;


public class UserProfile implements OntologicalUserProfile{
    
	private UserPreferences userPreferences;
    
	private UserContext context;
	private JSONObject jsonObj = null;

	public UserProfile(JSONObject json) throws IOException {
		jsonObj = null;
		setJSONObject(json);
	}
	
	public UserContext getContext() {
		return context;
	}


	public void setContext(UserContext context) {
		this.context = context;
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
			jsonObj.put("context", context.getJSONObject());

		}
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) {		
		userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		context = new UserContext(json.getJSONObject("context"));
		jsonObj = json;
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
		individual.addProperty(hasPreferences, userPreferences.createOntologyInstance(model));	
		
		//Add suggested preferences
		Property hasSuggestedPreferences = model.getProperty(HAS_SUGGESTED_PREFERENCES_PROP);
		individual.addProperty(hasSuggestedPreferences, new SuggestedPreferences().createOntologyInstance(model));	

		return individual;
	}

}
