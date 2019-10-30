package com.certh.iti.easytv.rbmm.user.preference;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.certh.iti.easytv.user.preference.Condition;
import com.certh.iti.easytv.user.preference.ConditionalPreference;


public class OntConditionalPreference extends OntPreference {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";
	
	protected ConditionalPreference conditional_preference;
	protected List<OntCondition> Conditions = new ArrayList<OntCondition>();
	
	public OntConditionalPreference(ConditionalPreference conditional_preference) {
		super(conditional_preference);
		
		this.conditional_preference = conditional_preference;
		
		//Create OntConditions
		for(Condition cond : conditional_preference.getConditions()) {
			Conditions.add(new OntCondition((Condition) cond));
		}
	}

	public ConditionalPreference getConditionalPreferences(){
		return conditional_preference;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass conditionalPrefClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual individual = conditionalPrefClass.createIndividual();		

		return createOntologyInstance(model, individual);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		
		Property hasNameProperty = model.getProperty(HAS_NAME_PROP);
		individual.addProperty(hasNameProperty,model.createTypedLiteral(conditional_preference.getName()));
		
		//add the preference properties 
		super.createOntologyInstance(model, individual);
		
		//add conditional preferences properties
		for(OntCondition cond : Conditions) 
			cond.createOntologyInstance(model, individual);
		

		return individual;
	}

}
