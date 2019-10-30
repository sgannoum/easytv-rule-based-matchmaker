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
	public static final String HAS_SUGGESTED_PREFERENCES_PROP = NAMESPACE + "hasSuggestedPreferences";
	

	
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
		
		//Add suggested preferences
		Property hasSuggestedPreferences = model.getProperty(HAS_SUGGESTED_PREFERENCES_PROP);
		userIndividual.addProperty(hasSuggestedPreferences, new SuggestedPreferences().createOntologyInstance(model));	

		return userIndividual;
	}

}
