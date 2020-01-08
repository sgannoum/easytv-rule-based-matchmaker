package com.certh.iti.easytv.rbmm.user;

import java.util.Map;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.exceptions.UserContextParsingException;

public class OntUserContext implements Ontological{
	
	private UserContext userContext;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserContext";
	
	//Object properties
	public static final String HAS_TIME_PROP = NAMESPACE + "hasTime";
	public static final String HAS_LOCATION_PROP = NAMESPACE + "hasLocation";
	
	
	public OntUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	
	public OntUserContext(JSONObject userContext) throws UserContextParsingException {
		this.userContext = new UserContext(userContext);
	}
	
	public UserContext getUserContext() {
		return userContext;
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
		Map<String, Object> context = userContext.getContext();
		
		//TODO add all other preferences
		
/*		if(context.containsKey("http://registry.easytv.eu/context/device")) {
			Property hasTimeProperty = model.getProperty(HAS_TIME_PROP);
			userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral(context.get("http://registry.easytv.eu/context/time")));
		}
*/
		
/*		if(context.containsKey("http://registry.easytv.eu/context/light")) {
			Property hasTimeProperty = model.getProperty(HAS_TIME_PROP);
			userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral(context.get("http://registry.easytv.eu/context/time")));
		}
*/
		
/*		if(context.containsKey("http://registry.easytv.eu/context/proximity")) {
			Property hasTimeProperty = model.getProperty(HAS_TIME_PROP);
			userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral(context.get("http://registry.easytv.eu/context/time")));
		}
*/
		
		if(context.containsKey("http://registry.easytv.eu/context/time")) {
			Property hasTimeProperty = model.getProperty(HAS_TIME_PROP);
			userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral(context.get("http://registry.easytv.eu/context/time")));
		}
		
		if(context.containsKey("http://registry.easytv.eu/context/location")) {
			Property hasLocationProperty = model.getProperty(HAS_LOCATION_PROP);
			userContextInstance.addProperty(hasLocationProperty, model.createTypedLiteral(context.get("http://registry.easytv.eu/context/location")));
		}
		
		return userContextInstance;
	}
		
}
