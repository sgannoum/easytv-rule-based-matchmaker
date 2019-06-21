package com.certh.iti.easytv.rbmm.user;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

public class SuggestedPreferences extends Ontological {
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "SuggestedPreferences";


	@Override
	public Individual createOntologyInstance(OntModel model) {
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		
		return userInstance;
	}


	@Override
	public Individual createOntologyInstance(OntModel model, Individual userInstance) {
		return userInstance;
	}
	
}
