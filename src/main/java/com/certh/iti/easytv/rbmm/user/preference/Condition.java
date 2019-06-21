package com.certh.iti.easytv.rbmm.user.preference;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.rbmm.user.UserPreferencesMappings;
import com.google.gson.Gson;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class Condition extends Ontological{
	
	private String type;
	private List<Object> operand;
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";
	
	// Data Properties
	public static final String CONDITIONS_PROP = NAMESPACE + "hasConditions";
	
    // Data Properties
	public static final String LEFT_OPERAND_PROP = NAMESPACE + "hasLeftOperand";
	public static final String RIGHT_OPERAND_PROP = NAMESPACE + "hasRightOperand";
	public static final String HAS_CONDITIONS_PROP = NAMESPACE + "hasConditions";

    
	// Data Properties
	public static final String HAS_TYPE_PROP = NAMESPACE + "hasType";
	public static final String HAS_VALUE_PROP = NAMESPACE + "hasValue";
	public static final String IS_TURE_PROP = NAMESPACE + "isTrue";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Object> getOperand() {
		return operand;
	}
	public void setOperand(List<Object> operand) {
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		String conditionalPreferences = "conditions : [\"type\" : \"" + type + "\", \"operand\": "  ;
		Iterator<Object> iterator = operand.iterator();
		
		conditionalPreferences +=  "[\\n";
		while(iterator.hasNext()) {
			Object value = iterator.next();
			
			if(String.class.isInstance(value)){
				conditionalPreferences += "\"" + value + "\"";
			} else {
				conditionalPreferences += value;
			}
			conditionalPreferences +=  ", ";
		}

		conditionalPreferences +=  "]]";
		return conditionalPreferences;
	}
	
	/**
	 * Create an instance of conditionalPreference 
	 * 
	 * @param model RDF model to enriched with RDF triples of the conditional preference
	 * @return instance of conditional preference
	 */
	@Override
	public Individual createOntologyInstance(final OntModel model){
		OntClass conditionalPreferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		return createOntologyInstance(model, conditionalPreferenceInstance);
	}
	

	/**
	 * Create an instance of conditionalPreference 
	 * 
	 * @param model 
	 * @param conditionalPreferenceInstance an instance of conditional preference class
	 * @return
	 */
	@Override
	public Individual createOntologyInstance(final OntModel model, Individual conditionalPreferenceInstance){
		Deque<Object> nodeStack = new ArrayDeque<Object>();
			
		OntClass operandClass;
		Individual operandInstance;
		
		//Add conditional preferences
		List<Object> fastList = getFlatOperands();
				
		for(int i = fastList.size() - 1; i >= 0; i--) {
			Object element = fastList.get(i);

			if(!String.class.isInstance(element)) {
				nodeStack.push(element);	
				continue;
			}
					
			String elementStr = (String) element;
						
			//logical gate
			if(	elementStr.equalsIgnoreCase("not")) {
					
					Individual operand_1 = (Individual) nodeStack.pop();
				
					operandClass = model.getOntClass(NAMESPACE + elementStr.toUpperCase());
					operandInstance = operandClass.createIndividual();
					
					//set left operand
					Property leftOperandProperty = model.getProperty(LEFT_OPERAND_PROP);
					operandInstance.addProperty(leftOperandProperty, operand_1);
					
					//add second operand to the stack
					nodeStack.push(operandInstance);
					
			} else if(elementStr.equalsIgnoreCase("and") || 
						elementStr.equalsIgnoreCase("or")) {
				
					Individual operand_1 = (Individual) nodeStack.pop();
					Individual operand_2 = (Individual) nodeStack.pop();
				
					operandClass = model.getOntClass(NAMESPACE + elementStr.toUpperCase());
					operandInstance = operandClass.createIndividual();
					
					//set left operand
					Property leftOperandProperty = model.getProperty(LEFT_OPERAND_PROP);
					operandInstance.addProperty(leftOperandProperty, operand_1);
					
					//set right operand
					Property rightOperandProperty = model.getProperty(RIGHT_OPERAND_PROP);
					operandInstance.addProperty(rightOperandProperty, operand_2);		
					
					//add second operand to the stack
					nodeStack.push(operandInstance);
				
			} else if(elementStr.equalsIgnoreCase("gt") || elementStr.equalsIgnoreCase("ge") || 
						elementStr.equalsIgnoreCase("lt") || elementStr.equalsIgnoreCase("le") ||
						 elementStr.equalsIgnoreCase("eq") || elementStr.equalsIgnoreCase("ne") ) {
				
					
					Object uriObj = nodeStack.pop();
					Object value = nodeStack.pop();

					String uri = (String) uriObj;
										
					operandClass = model.getOntClass(NAMESPACE + elementStr.toUpperCase());
					operandInstance = operandClass.createIndividual();
																			
					//set type
					Property hasTypeProperty = model.getProperty(HAS_TYPE_PROP);
					operandInstance.addProperty(hasTypeProperty, model.createProperty(UserPreferencesMappings.getDataProperty(uri)));
					//set value
					Property hasValueProperty = model.getProperty(HAS_VALUE_PROP);
					operandInstance.addProperty(hasValueProperty, model.createTypedLiteral(value));
					
					nodeStack.push(operandInstance);	
			} else {
					nodeStack.push(elementStr);	
			}
		}

		//check that only on operand remains
		if(nodeStack.size() != 1) {
			return null;
		}
		

		Property hasConditionsProperty = model.getProperty(HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, (Individual) nodeStack.pop()) ;
				
		return conditionalPreferenceInstance;
	}
	
	public  List<Object> getFlatOperands(){
		List<Object> nestedOperands = new ArrayList<Object>(operand);
		nestedOperands.add(0, type);
		return flatOutOperands(nestedOperands);
	}
	
	/**
	 * Convert the tree of the conditional preferences into a flat list of operands
	 * 
	 * @return
	 */
	private static List<Object> flatOutOperands(List<Object> nestedOperands) {
		List<Object> flatOperands = new ArrayList<Object>();
		
		int index = 0 ;
				
		while(true) { 
			if(index >= nestedOperands.size()) {
				break;
			}
			
			Object operand1 = nestedOperands.get(index++);

			if(LinkedHashMap.class.isInstance(operand1) || 
					JSONObject.class.isInstance(operand1)) {
				
				JSONObject jsonObj = null;
				
				//handle linkedHashMap and json object
				if(LinkedHashMap.class.isInstance(operand1)) {
					String jsonString = new Gson().toJson(operand1, Map.class);
					
					try {
						jsonObj = new JSONObject(jsonString.toString());
					} catch(JSONException e1) {}
					
				} else {
					jsonObj = (JSONObject) operand1;
				}
					
				//handle first operand					
				flatOperands.add(jsonObj.getString("type"));
				
				//add operand
				int i = index;
				Iterator<Object> iteratorOperands = jsonObj.getJSONArray("operands").iterator();
				while(iteratorOperands.hasNext()) {
					nestedOperands.add(i++, iteratorOperands.next());
				}
				
			} else {
				flatOperands.add(operand1);
			}
		}
		return flatOperands;
	}

}
