package com.certh.iti.easytv.rbmm.user;

import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.exceptions.UserContextParsingException;

public class OntUserContext implements Ontological{
	
	private UserContext userContext;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserContext";
	
	
	public OntUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	
	public OntUserContext(JSONObject userContext) throws UserContextParsingException {
		this.userContext = new UserContext(userContext);
	}
	
	public UserContext getUserContext() {
		return userContext;
	}
	
	/**
	 * Get the contextual uri that corresponds to the given predicate
	 * @param predicate
	 * @return
	 */
	public static String getURI(String predicate) {
		String uri = predicate.replace(Ontological.NAMESPACE, "http://registry.easytv.eu/").replace("has_", "").replace("_", "/");
		
		if(!UserContext.getAttributes().containsKey(uri))
			return null;
		
		return uri;
	}
	
	/**
	 * Get contextual predicate that corresponds to the give uri
	 * @param uri
	 * @return
	 */
	public static String getPredicate(String uri) {
		if(!UserContext.getAttributes().containsKey(uri))
			return null;
		
		return Ontological.NAMESPACE + uri.replaceAll("http://registry.easytv.eu/", "has_").replace("/","_");
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model){
		
		//create the new user in the ontology
		OntClass userContextClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userContextInstance = userContextClass.createIndividual();
		
		return createOntologyInstance(model, userContextInstance);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual userContextInstance) {
		String propertyUri = null;
	
		for(Entry<String, Object> entry : userContext.getContext().entrySet()) {
			if((propertyUri = OntUserContext.getPredicate(entry.getKey())) != null) {
				Property property = model.getProperty(propertyUri);
				Literal literal = model.createTypedLiteral(entry.getValue());
				userContextInstance.addProperty(property, literal);
			}
		}
		
		return userContextInstance;
	}
		
}
