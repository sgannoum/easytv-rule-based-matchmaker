package com.certh.iti.easytv.rbmm.user;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

public class SuggestedPreferences {
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "SuggestedPreferences";

	
	public static Individual createOntologyInstance(final OntModel model){
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		return userInstance;
	}
	
}
