package com.certh.iti.easytv.rbmm.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.json.JSONObject;

public class UserContext implements Ontological{
	
	private Date time = null;  
	private String location = null;
    private JSONObject jsonObj = null;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "UserContext";
	
	//Object properties
	public static final String HAS_TIME_PROP = NAMESPACE + "hasTime";
	public static final String HAS_LOCATION_PROP = NAMESPACE + "hasLocation";
	
	
	public UserContext(JSONObject json) {
		setJSONObject(json);
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			
			if(time != null) {
				jsonObj.put("http://registry.easytv.eu/context/time", time.toString());
			}
			
			if(location != null) {
				jsonObj.put("http://registry.easytv.eu/context/location", location);
			}
		}
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) {		

		//check for context time 
		if(json.has("http://registry.easytv.eu/context/time")) {
			String timeStr = json.getString("http://registry.easytv.eu/context/time");
			
			//Known format
			Pattern iso_8601 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z");  
			Pattern simpleFormat = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");  
			Pattern miniSimpleFormat = Pattern.compile("\\d{2}:\\d{2}");  
			
			if(iso_8601.matcher(timeStr).matches()) {
				time = Date.from( Instant.parse(timeStr));
			} else {
				
				if(miniSimpleFormat.matcher(timeStr).matches())  
					timeStr +=":00";
				
				if(simpleFormat.matcher(timeStr).matches()) {
					
					try {
						time = new SimpleDateFormat("HH:mm:ss").parse(timeStr);
					} catch (ParseException e) {}
					
				} else {
					throw new IllegalArgumentException("Wrong time format: "+ timeStr);
				}
			}
		}
		
		
		//check for context location
		if(json.has("http://registry.easytv.eu/context/location")) {
			location = json.getString("http://registry.easytv.eu/context/location");
		}
		
		
		
		jsonObj = json;
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
