package com.certh.iti.easytv.rbmm.user.preference;

import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;

import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.user.preference.Preference;

public class OntPreference implements Ontological {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	// Data Properties
	public static final String HAS_NAME_PROP = NAMESPACE + "hasName";
	
	protected Preference defaultPreference;
	
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public static String getURI(String predicate) {
		String uri = predicate.replace(Ontological.NAMESPACE, "http://registry.easytv.eu/").replace("has_", "").replace("_", "/");
		
		if(!Preference.getAttributes().containsKey(uri))
			return null;
		
		return uri;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public static String getPredicate(String uri) {
		if(!Preference.getAttributes().containsKey(uri))
			return null;
		
		return Ontological.NAMESPACE + uri.replaceAll("http://registry.easytv.eu/", "has_").replace("/","_");
	}
	
	public OntPreference(Preference defaultPreference) {
		this.defaultPreference = defaultPreference;
	}
	
	public Preference getPreferences(){
		return defaultPreference;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model) {
		
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		Property hasNameProperty = model.getProperty(HAS_NAME_PROP);
		preferenceInstance.addProperty(hasNameProperty, "default") ;
		
		return createOntologyInstance(model, preferenceInstance);
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model, Individual preferenceIndividual) {
			
		String propertyUri = null;
		
		for(Entry<String, Object> entry : defaultPreference.getPreferences().entrySet()) {
			//If known
			if((propertyUri = OntPreference.getPredicate(entry.getKey())) != null){
				Property property = model.getProperty(propertyUri);
				Literal literal = model.createTypedLiteral(entry.getValue());
				
				//State the preference
				preferenceIndividual.addProperty(property, literal);
			}
		}
		

		return preferenceIndividual;
	}

}
