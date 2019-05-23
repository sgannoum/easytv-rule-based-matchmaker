package rule_matchmaker.entities;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class DefaultUserPreferences {
	
	private Map<String, Object> preferences;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	private static final String DOMAIN_NAME = "https://easytvproject.eu/registry/";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserPreferences";
	
	
	public Map<String, Object> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}
	
	public static String getDataProperty(String uri) {
		return UserPreferencesMappings.uriToDataProperty.get(uri);
	}
	
	public static String getURI(String dataProperty) {
		return UserPreferencesMappings.dataPropertyToUri.get(dataProperty);
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Property p = model.getProperty(getDataProperty(entry.getKey()));
			
			preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
		}
		
		return preferenceInstance;
	}
	
	
	public Individual createOntologyInstance(final OntModel model, Individual preferenceInstance){
				
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Property p = model.getProperty(getDataProperty(entry.getKey()));
			
			preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
		}
		
		return preferenceInstance;
	}
	
	
	@Override
	public String toString() {
		String userPreferences = "preferences [\n";
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			userPreferences += entry.getKey();
			userPreferences += ": ";
			userPreferences += entry.getValue().toString();
			userPreferences += ",\n";
		}
		userPreferences += "]";
		
		return userPreferences;
	}

}
