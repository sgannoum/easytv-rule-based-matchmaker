package com.certh.iti.easytv.rbmm.user.preference;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONArray;
import org.json.JSONObject;


public class ConditionalPreference extends Preference {
	
	private List<Condition> conditions;
	
	public ConditionalPreference(String name, JSONObject json) {
		super(name, json);
	}

	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Condition> getConditions() {
		return conditions;
	}


	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	
	@Override
	public void setJSONObject(JSONObject json) {
		//Add preferences
		super.setJSONObject(json);
		
		//Add conditions
		JSONArray jsonConditions = json.getJSONArray("conditions");
		this.conditions = new ArrayList<Condition>();
		for(int i = 0 ; i < jsonConditions.length(); i++) {
			this.conditions.add(new Condition(jsonConditions.getJSONObject(i)));
		}
		
		this.jsonObj = json;
	}
	
	@Override
	public JSONObject toJSON() {
		if(jsonObj == null) {
			
			//Convert the preference section
			super.toJSON();
			
			//Add condition name
			jsonObj.put("name", name);
			
			//Add condition section
			JSONArray jsonConditions = new JSONArray();
			for(int i = 0; i < conditions.size(); i++) 
				jsonConditions.put(conditions.get(i).toJSON());
			
			//Add condition section to the JSON file
			jsonObj.put("conditions", jsonConditions);
		}
		
		return jsonObj;
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
		for(Condition cond : conditions)
			cond.createOntologyInstance(model, conditionalPrefInstance);

		return conditionalPrefInstance;
	}

}
