package com.certh.iti.easytv.rbmm.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.user.preference.ConditionalPreference;
import com.certh.iti.easytv.rbmm.user.preference.Preference;

public class UserPreferences implements OntologicalUserPreferences {
	
	private Preference defaultPreference;
	private List<Preference> conditionalPreferences;
	private JSONObject jsonObj;

	public UserPreferences(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public Preference getDefaultUserPreferences() {
		return defaultPreference;
	}
	
	public void setDefaultUserPreferences(Preference defaultPreferences) {
		this.defaultPreference = defaultPreferences;
	}
	
	public List<Preference> getConditional(){
		return conditionalPreferences;
	}
	
	public void setConditional(List<Preference> conditional){
		this.conditionalPreferences = conditional;
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			toJSON();
		}
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) {
		
		defaultPreference = new Preference("default", json.getJSONObject("default"));
		conditionalPreferences = new ArrayList<Preference>();

		if(json.has("conditional")) {
			
			//Get conditional preferences
			JSONArray conditional = json.getJSONArray("conditional");
			
			//Add conditional preferences
			for(int i = 0 ; i < conditional.length(); i++) {
				JSONObject condition =  conditional.getJSONObject(i);
				conditionalPreferences.add(new ConditionalPreference(condition.getString("name"), condition));
			}
		}
		
		this.jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {

			jsonObj = new JSONObject();
			
			//add default preferences
			jsonObj.put("default", defaultPreference.toJSON());
			
			
			if(!conditionalPreferences.isEmpty()) {
				//prepare the conditional preference array
				JSONArray conditional = new JSONArray();
				
				for (Preference conditionalPreference : conditionalPreferences) 
					conditional.put(conditionalPreference.getJSONObject());
				
				//add conditional preferences
				jsonObj.put("conditional", conditional);
			}
		}
		return jsonObj;
	}
	
	@Override
	public Individual createOntologyInstance(final OntModel model){
		
		//Default preferences
		Individual preferenceInstance = defaultPreference.createOntologyInstance(model);
		
		//Add conditional preferences
		Property hasConditionalPreferences = model.getProperty(CONDITIONAL_PREFERENCE_PROP);
		for(Preference conditionalPreference : conditionalPreferences) {
			Individual conditionalPreferenceInstance = conditionalPreference.createOntologyInstance(model);
			preferenceInstance.addProperty(hasConditionalPreferences, conditionalPreferenceInstance);	
		}
		
		return preferenceInstance;
	}
	
	@Override
	public Individual createOntologyInstance(OntModel model, Individual individual) {
		// TODO Auto-generated method stub
		return null;
	}

}
