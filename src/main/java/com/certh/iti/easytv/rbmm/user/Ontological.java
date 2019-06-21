package com.certh.iti.easytv.rbmm.user;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

public abstract class Ontological {
	
	public static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";

	
	/**
	 * Create an ontology instance of the corresponding class 
	 * 
	 * @param model RDF model 
	 * @return instance of the ontology
	 */
	public abstract Individual createOntologyInstance(final OntModel model);
	
	/**
	 * 
	 * @param model RDF model 
	 * @param individual ontology instance
	 * @return instance of the ontology
	 */
	public abstract Individual createOntologyInstance(final OntModel model, Individual individual);

}
