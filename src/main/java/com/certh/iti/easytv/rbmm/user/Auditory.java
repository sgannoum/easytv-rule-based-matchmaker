package com.certh.iti.easytv.rbmm.user;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Auditory {
	
    @JsonProperty("http://registry.easytv.eu/functionalLimitations/auditory/hearingThresholdAt250Hz")
	private Integer hearing;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "AuditoryAbility";
	public static final String HEARING_PRO = NAMESPACE + "hasHearing";

	public Integer getHearing() {
		return hearing;
	}

	@Override
	public String toString() {
		return "Auditory [hearing=" + hearing + "]";
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass auditoryClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual auditoryInstance = auditoryClass.createIndividual();
		
		if (hearing != null){
			Property p = model.getProperty(HEARING_PRO);
			auditoryInstance.addProperty(p, model.createTypedLiteral(hearing));
		}
		
		return auditoryInstance;
	}

}
