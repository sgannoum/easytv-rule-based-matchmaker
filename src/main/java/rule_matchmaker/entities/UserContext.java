package rule_matchmaker.entities;

import java.util.Date;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserContext {
	
    @JsonProperty("http://registry.easytv.eu/context/time")
	private String time;
    
    @JsonProperty("http://registry.easytv.eu/context/location")
	private String location;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserContext";
	
	//Object properties
	public static final String HAS_TIME_PROP = NAMESPACE + "hasTime";
	public static final String HAS_LOCATION_PROP = NAMESPACE + "hasLocation";
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public Individual createOntologyInstance(final OntModel model){
		
		//create the new user in the ontology
		OntClass userContextClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual userContextInstance = userContextClass.createIndividual();
		
		Property hasTimeProperty = model.getProperty(HAS_TIME_PROP);
		userContextInstance.addProperty(hasTimeProperty, model.createTypedLiteral(time));
		
		Property hasLocationProperty = model.getProperty(HAS_LOCATION_PROP);
		userContextInstance.addProperty(hasLocationProperty, model.createTypedLiteral(location));
		
		return userContextInstance;
	}
	
	@Override
	public String toString() {
		return "userContext [ time: " + time + " location: "+ location + " ]";
	}
	
}
