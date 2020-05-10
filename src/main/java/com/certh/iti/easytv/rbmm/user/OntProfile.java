package com.certh.iti.easytv.rbmm.user;

import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.UserContent;
import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class OntProfile implements Ontological{
	
	public static final String HAS_CONTEXT_PROP = NAMESPACE + "hasContext";
	
	private Profile profile;
	private OntUserProfile OntUserProfile = null;
	private OntUserContext OntUserContext = null;
	private Content OntUserContent = null;
	
	//TODO
	//private OntUserContent ontUserContent;

	public OntProfile(JSONObject json) throws IOException, UserProfileParsingException {
		
		this.profile = new Profile(json);
		OntUserProfile = new OntUserProfile(profile.getUserProfile());
		
		if(profile.getUserContext() != null)
			OntUserContext = new OntUserContext(profile.getUserContext());
		
		if(profile.getUserContent() != null)
			OntUserContent = new Content(profile.getUserContent().getJSONObject());
	}
	
	public OntProfile(int userId, UserProfile userProfile) throws IOException, UserProfileParsingException {
		
		if(userProfile == null)
			throw new NullPointerException("No userprofile");
			
		this.profile = new Profile(userId, userProfile, null,  null);
		OntUserProfile = new OntUserProfile(profile.getUserProfile());
	}
	
	public OntProfile(int userId, UserProfile userProfile, UserContext userContext) throws IOException, UserProfileParsingException {
		
		this.profile = new Profile(userId, userProfile, userContext, null);
		OntUserProfile = new OntUserProfile(profile.getUserProfile());
		OntUserContext = new OntUserContext(profile.getUserContext());
	}
	
	public OntProfile(int userId, UserProfile userProfile, UserContext userContext, UserContent userContent) throws IOException, UserProfileParsingException {
		this.profile = new Profile(userId, userProfile, userContext,  userContent);
	}

	
	public Profile getProfile() {
		return this.profile;
	}
	
	public UserProfile getUserProfile() {
		return this.profile.getUserProfile();
	}
	
	public UserContext getUserContext() {
		return this.profile.getUserContext();
	}
	
	public UserContent getUserContent() {
		return this.profile.getUserContent();
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model) {
		Individual contextIndividual = null;
		Individual userPRofileIndividual = null;
		
		userPRofileIndividual = OntUserProfile.createOntologyInstance(model);
		
		//Add context
		if(OntUserContext != null) {
			contextIndividual = OntUserContext.createOntologyInstance(model);
			
			Property hasContextAbility = model.getProperty(HAS_CONTEXT_PROP);
			userPRofileIndividual.addProperty(hasContextAbility, contextIndividual);	
		}
		
		//Add content 
		if(OntUserContent != null) {			
			OntUserContent.createOntologyInstance(model);
		}
		
		return userPRofileIndividual;
	}

	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		throw new IllegalStateException("Unimplemented method");
	}

}
