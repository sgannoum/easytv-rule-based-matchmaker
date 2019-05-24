package rule_matchmaker.entities;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "preferences", "conditions" })
public class ConditionalPreference {
	
    @JsonProperty("name")
	private String name;
    
    @JsonProperty("preferences")
    @JsonDeserialize(as=DefaultUserPreferences.class)
	private DefaultUserPreferences preferences;
    
    @JsonProperty("conditions")
    @JsonDeserialize(as=Conditions.class)
	private Conditions conditions;
	
	public static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";

	// Data Properties
	public static final String ACCESSIBILITY_SERVICE_PROP = NAMESPACE + "hasAccessibilityService";
	public static final String PREFERENCE_PROP = NAMESPACE + "hasPreference";


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public DefaultUserPreferences getPreferences() {
		return preferences;
	}


	public void setPreferences(DefaultUserPreferences preferences) {
		this.preferences = preferences;
	}
	
	public void setPreferences(LinkedHashMap<String, Object> preferences) {
		this.preferences = new DefaultUserPreferences();
		this.preferences.setPreferences(preferences);
	}



	public Conditions getConditions() {
		return conditions;
	}


	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}
	
	public void setConditions(List<Object> conditions) {
		this.conditions = new Conditions();
		LinkedHashMap<String, Object> inst = (LinkedHashMap<String, Object>) conditions.remove(0);
		this.conditions.setType((String) inst.get("type"));
		this.conditions.setOperand((List<Object>) inst.get("operands"));
	}
	
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass conditionalPrefClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual conditionalPrefInstance = conditionalPrefClass.createIndividual();		
		
		Property hasNameProperty = model.getProperty(UserPreference.HAS_NAME_PROP);
		conditionalPrefInstance.addProperty(hasNameProperty,model.createTypedLiteral(name));
		
		//add the preference properties 
		preferences.createOntologyInstance(model, conditionalPrefInstance);
		
		//add conditional preferences properties
		conditions.createOntologyInstance(model, conditionalPrefInstance);

		return conditionalPrefInstance;
	}
	
	@Override
	public String toString() {
		return "{\r\n"+
				 "\"name\": \"" + name +"\", \r\n" +
				 preferences.toString() +", \r\n" +
				 conditions.toString() + ", \r\n" +
				 "}";
	}

}
