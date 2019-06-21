package com.certh.iti.easytv.rbmm.user.preference;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConditionalPreference extends Preference {
	
    @JsonProperty("name")
	private String name;
    
    
    @JsonProperty("conditions")
    @JsonDeserialize(as=Condition.class)
	private Condition conditions;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Condition getConditions() {
		return conditions;
	}


	public void setConditions(Condition conditions) {
		this.conditions = conditions;
	}
	
	public void setConditions(List<Object> conditions) {
		this.conditions = new Condition();
		LinkedHashMap<String, Object> inst = (LinkedHashMap<String, Object>) conditions.remove(0);
		this.conditions.setType((String) inst.get("type"));
		this.conditions.setOperand((List<Object>) inst.get("operands"));
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass conditionalPrefClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual conditionalPrefInstance = conditionalPrefClass.createIndividual();		

		return createOntologyInstance(model, conditionalPrefInstance);
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual conditionalPrefInstance) {
		
		Property hasNameProperty = model.getProperty(HAS_NAME_PROP);
		conditionalPrefInstance.addProperty(hasNameProperty,model.createTypedLiteral(name));
		
		//add the preference properties 
		super.createOntologyInstance(model, conditionalPrefInstance);
		
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
