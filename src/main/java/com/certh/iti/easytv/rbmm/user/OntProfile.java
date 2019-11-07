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
	
	private Profile contextualUserProfile;
	private OntUserProfile OntUserProfile = null;
	private OntUserContext OntUserContext = null;
	
	//TODO
	//private OntUserContent ontUserContent;

	public OntProfile(JSONObject json) throws IOException, UserProfileParsingException {
		
		this.contextualUserProfile = new Profile(json);
		OntUserProfile = new OntUserProfile(contextualUserProfile.getUserProfile());
		
		if(contextualUserProfile.getUserContext() != null)
			OntUserContext = new OntUserContext(contextualUserProfile.getUserContext());
		
		//TODO
/*		if(contextualUserProfile.getUserContent() != null)
			OntUserContext = new OntUserContent(contextualUserProfile.getUserContent());
*/

	}
	
	public OntProfile(int userId, UserProfile userProfile) throws IOException, UserProfileParsingException {
		
		if(userProfile == null)
			throw new NullPointerException("No userprofile");
			
		this.contextualUserProfile = new Profile(userId, userProfile, null,  null);
		OntUserProfile = new OntUserProfile(contextualUserProfile.getUserProfile());
	}
	
	public OntProfile(int userId, UserProfile userProfile, UserContext userContext) throws IOException, UserProfileParsingException {
		
		this.contextualUserProfile = new Profile(userId, userProfile, userContext, null);
		OntUserProfile = new OntUserProfile(contextualUserProfile.getUserProfile());
		OntUserContext = new OntUserContext(contextualUserProfile.getUserContext());
	}
	
	public OntProfile(int userId, UserProfile userProfile, UserContext userContext, UserContent userContent) throws IOException, UserProfileParsingException {
		this.contextualUserProfile = new Profile(userId, userProfile, userContext,  userContent);
	}

	
	public Profile getProfile() {
		return this.contextualUserProfile;
	}
	
	public UserProfile getUserProfile() {
		return this.contextualUserProfile.getUserProfile();
	}
	
	public UserContext getUserContext() {
		return this.contextualUserProfile.getUserContext();
	}
	
	public UserContent getUserContent() {
		return this.contextualUserProfile.getUserContent();
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model) {
		Individual contextIndividual = null;
		Individual userPRofileIndividual = null;
		
		userPRofileIndividual = OntUserProfile.createOntologyInstance(model);
		
		if(OntUserContext != null) {
			contextIndividual = OntUserContext.createOntologyInstance(model);
			
			Property hasContextAbility = model.getProperty(HAS_CONTEXT_PROP);
			userPRofileIndividual.addProperty(hasContextAbility, contextIndividual);	
		}
		
		//TODO
		//Add content 
		
		return userPRofileIndividual;
	}

	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		throw new IllegalStateException("Unimplemented method");
	}

}
