package com.certh.iti.easytv.rbmm.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import com.certh.iti.easytv.rbmm.user.preference.OntConditionalPreference;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.user.UserPreferences;
import com.certh.iti.easytv.user.preference.ConditionalPreference;
import com.certh.iti.easytv.user.preference.Preference;

public class OntUserPreferences implements Ontological {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String HAS_ACCESSIBILITY_SERVICE_PROP = NAMESPACE + "hasAccessibilityService";
	public static final String HAS_CONDITIONAL_PREFERENCE_PROP = NAMESPACE + "hasConditionalPreferences";
	
	
	private UserPreferences userPreferences;
	private OntPreference defaultPreference;
	private List<OntPreference> conditionalPreferences = new ArrayList<OntPreference>();
	
	public OntUserPreferences(UserPreferences UserPreferences) {
		this.userPreferences = UserPreferences;
		this.defaultPreference = new OntPreference(UserPreferences.getDefaultPreference());
		
		//Convert conditional preferences
		for(Preference conditionalPreference : UserPreferences.getConditionalPreferences()) 
			conditionalPreferences.add(new OntConditionalPreference((ConditionalPreference) conditionalPreference));
		
	}
	/**
	 * Get associated user preferences
	 * @return
	 */
	public UserPreferences getUserPreferences(){
		return userPreferences;
	}

	
	@Override
	public Individual createOntologyInstance(final OntModel model){
		
		//Create individual
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		//Add conditional preferences
		return createOntologyInstance(model, preferenceInstance);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual preferenceInstance) {
		
		//Add default preferences
		defaultPreference.createOntologyInstance(model, preferenceInstance);
		
		//Add conditional preferences
		Property hasConditionalPreferences = model.getProperty(HAS_CONDITIONAL_PREFERENCE_PROP);
		for(OntPreference condPreference : conditionalPreferences) {
			Individual condIndividual = condPreference.createOntologyInstance(model);
			preferenceInstance.addProperty(hasConditionalPreferences, condIndividual);	
		}
		
		return preferenceInstance;
	}

}
