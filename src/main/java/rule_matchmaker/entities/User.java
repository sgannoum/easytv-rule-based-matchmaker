package rule_matchmaker.entities;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
    @JsonProperty("visual")
	private Visual visual;
    
    @JsonProperty("auditory")
	private Auditory auditory;
    
    @JsonProperty("user_preferences")
	private UserPreference user_preferences;
    
    @JsonProperty("context")
	private UserContext context;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "User";
	
	//Object properties
	public static final String HAS_VISUAL_PROP = NAMESPACE + "hasVisualAbility";
	public static final String HAS_AUDITORY_PROP = NAMESPACE + "hasAuditoryAbility";
	public static final String HAS_PREFERENCE_PROP = NAMESPACE + "hasPreference";
	public static final String HAS_CONTEXT_PROP = NAMESPACE + "hasContext";

	
	public UserContext getContext() {
		return context;
	}


	public void setContext(UserContext context) {
		this.context = context;
	}
	
	public Visual getVisual() {
		return visual;
	}


	public void setVisual(Visual visual) {
		this.visual = visual;
	}


	public Auditory getAuditory() {
		return auditory;
	}


	public void setAuditory(Auditory auditory) {
		this.auditory = auditory;
	}
	
	
	public UserPreference getUser_preferences() {
		return user_preferences;
	}


	public void setUser_preferences(UserPreference user_preferences) {
		this.user_preferences = user_preferences;
	}
	
	@Override
	public String toString() {
		return "User [" + visual + ", " +
							auditory + ", " +
								context + ", " +
									user_preferences+"]";
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		//Add visual acuity
		Property hasVisualAbility = model.getProperty(HAS_VISUAL_PROP);
		userInstance.addProperty(hasVisualAbility, visual.createOntologyInstance(model));	
		
		//Add Auditory ability
		Property hasAuditoryAbility = model.getProperty(HAS_AUDITORY_PROP);
		userInstance.addProperty(hasAuditoryAbility, auditory.createOntologyInstance(model));	
		
		//Add Auditory ability
		Property hasContextAbility = model.getProperty(HAS_CONTEXT_PROP);
		userInstance.addProperty(hasContextAbility, context.createOntologyInstance(model));	
		
		//Add user preferences
		Property hasPreferences = model.getProperty(HAS_PREFERENCE_PROP);
		userInstance.addProperty(hasPreferences, user_preferences.createOntologyInstance(model));	
		
		return userInstance;
	}

}
