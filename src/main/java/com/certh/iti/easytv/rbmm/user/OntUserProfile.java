package com.certh.iti.easytv.rbmm.user;

import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;


public class OntUserProfile implements Ontological{
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "User";
	
	//Object properties
	public static final String HAS_PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String HAS_CONTEXT_PROP = NAMESPACE + "hasContext";
	public static final String HAS_SUGGESTION_SET_PROPERTY = NAMESPACE + "hasSuggestionSet";
	public static final String HAS_SUGGESTION_PROPERTY = NAMESPACE + "hasSuggestion";
	public static final String HAS_CONTENT_SUGGESTION_PROP = NAMESPACE + "hasContentSuggestion";

	
	private UserProfile UserProfile = null;
	private OntUserPreferences userPreferences;

	public OntUserProfile(JSONObject userProfile) throws IOException, UserProfileParsingException {
		this.UserProfile = new UserProfile(userProfile);
		this.userPreferences = new OntUserPreferences(UserProfile.getUserPreferences());
	}
	
	public OntUserProfile(UserProfile UserProfile) {
		this.UserProfile = UserProfile;
		this.userPreferences = new OntUserPreferences(UserProfile.getUserPreferences());
	}
	
	public UserProfile getUserProfile(){
		return UserProfile;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model) {
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		return createOntologyInstance(model, userInstance);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual userIndividual) {
		
		//Add user preferences
		Property hasPreferences = model.getProperty(HAS_PREFERENCE_PROP);
		userIndividual.addProperty(hasPreferences, userPreferences.createOntologyInstance(model));	
		
		//Add preferences suggestion set 
		OntClass suggestionSetClass = model.getOntClass(NAMESPACE + "SuggestionSet");
		Property hasSuggestionSetProperty = model.getProperty(HAS_SUGGESTION_SET_PROPERTY);
		userIndividual.addProperty(hasSuggestionSetProperty, suggestionSetClass.createIndividual());	
		
		//add content suggestions
		OntClass suggestionPreferenceClass = model.getOntClass(OntSuggestedPreferences.ONTOLOGY_CLASS_URI);
		Property hasContentSuggestion = model.getProperty(HAS_CONTENT_SUGGESTION_PROP);
		userIndividual.addProperty(hasContentSuggestion, suggestionPreferenceClass.createIndividual());	
		
		return userIndividual;
	}

}
