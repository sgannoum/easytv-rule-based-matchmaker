package com.certh.iti.easytv.rbmm.user.preference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.user.OntologicalPreference;
import com.certh.iti.easytv.rbmm.user.UserPreferencesMappings;

public class Preference implements OntologicalPreference {
	
	protected String name;
	protected Map<String, Object> preferences;
	protected JSONObject jsonObj;

	public Preference(String name, JSONObject json) {
		this.name = name;
		this.preferences = new HashMap<String, Object>();
		setJSONObject(json);
	}
	
	public Map<String, Object> getPreferences() {
		return preferences;
	}
	
	public JSONObject getJSONObject() {
		return toJSON();
	}
	
	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}
	

	public void setJSONObject(JSONObject json) {
		JSONObject jsonPreference = json.getJSONObject("preferences");
		String[] fields = JSONObject.getNames(jsonPreference);
		
		for(int i = 0 ; i < fields.length; i++) {
			String preferenceUri = fields[i];
			
			preferences.put(preferenceUri, jsonPreference.get(preferenceUri));
		}
		
		this.jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			JSONObject jsonPreferences = new JSONObject();
			
			for(Entry<String, Object> entry : preferences.entrySet()) 
				jsonPreferences.put(entry.getKey(), entry.getValue());
			
			jsonObj.put("preferences", jsonPreferences);
		}
		return jsonObj;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model) {
		
		OntClass preferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual preferenceInstance = preferenceClass.createIndividual();
		
		Property hasNameProperty = model.getProperty(HAS_NAME_PROP);
		preferenceInstance.addProperty(hasNameProperty, "default") ;
		
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String propertyUri = null;
			
			//only add known properties
			if((propertyUri = UserPreferencesMappings.getDataProperty(entry.getKey())) != null){
				Property p = model.getProperty(propertyUri);
				preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
			}
		}
		
		return preferenceInstance;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model, Individual preferenceInstance) {
				
		//Add user preferences
		Iterator<Entry<String, Object>> iterator = preferences.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Property p = model.getProperty(UserPreferencesMappings.getDataProperty(entry.getKey()));
			
			preferenceInstance.addProperty(p, model.createTypedLiteral(entry.getValue()));
		}
		
		return preferenceInstance;
	}

}
