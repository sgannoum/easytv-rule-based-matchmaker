package rule_matchmaker.entities;

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

import com.google.gson.Gson;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class ConditionalPreferences {
	
	private String type;
	private List<Object> operand;
	
	public static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";
	
	// Data Properties
	public static final String CONDITIONS_PROP = NAMESPACE + "hasConditions";
	
	private static final HashMap<String, String> gateToUri  =  new HashMap<String, String>() {{
		put("and", NAMESPACE + "AND");
		put("or", NAMESPACE + "OR");
		put("not", NAMESPACE + "NOT");
		put("gt", NAMESPACE + "GT");
		put("ge", NAMESPACE + "GE");
		put("lt", NAMESPACE + "LT");
		put("le", NAMESPACE + "LE");
		put("ne", NAMESPACE + "NE");
		put("eq", NAMESPACE + "EQ");
		put("ap", NAMESPACE + "AP");
    }};
	
    // Data Properties
	public static final String LEFT_OPERAND_PROP = NAMESPACE + "hasLeftOperand";
	public static final String RIGHT_OPERAND_PROP = NAMESPACE + "hasRightOperand";
	public static final String HAS_CONDITIONS_PROP = NAMESPACE + "hasConditions";

    
	// Data Properties
	public static final String HAS_RESULT_PROP = NAMESPACE + "hasResult";
	public static final String HAS_TYPE_PROP = NAMESPACE + "hasType";
	public static final String HAS_VALUE_PROP = NAMESPACE + "hasValue";
	public static final String HAS_URI_PROP = NAMESPACE + "hasURI";
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
		String conditionalPreferences = "conditional_preferences : [\"type\" : \"" + type + "\", \"operand\": "  ;
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
	public Individual createOntologyInstance(final OntModel model){
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
			
			if(elementStr.contains("http")) {
				nodeStack.push(elementStr);	
				continue;
			}
			
			elementStr = elementStr.toUpperCase();
			
			//logical gate
			if(	elementStr.equalsIgnoreCase("not")) {
					
					Object operand_1 = nodeStack.pop();
				
					operandClass = model.getOntClass(NAMESPACE + elementStr);
					operandInstance = operandClass.createIndividual();
					
					//set left operand
					Property leftOperandProperty = model.getProperty(LEFT_OPERAND_PROP);
					operandInstance.addProperty(leftOperandProperty, model.createTypedLiteral(operand_1));
					
					//add second operand to the stack
					nodeStack.push(operandInstance);
					
			} else if(elementStr.equalsIgnoreCase("and") || 
						elementStr.equalsIgnoreCase("or")) {
				
					Object operand_1 = nodeStack.pop();
					Object operand_2 = nodeStack.pop();
				
					operandClass = model.getOntClass(NAMESPACE + elementStr);
					operandInstance = operandClass.createIndividual();
					
					//set left operand
					Property leftOperandProperty = model.getProperty(LEFT_OPERAND_PROP);
					operandInstance.addProperty(leftOperandProperty, model.createTypedLiteral(operand_1));
					
					//set right operand
					Property rightOperandProperty = model.getProperty(RIGHT_OPERAND_PROP);
					operandInstance.addProperty(rightOperandProperty, model.createTypedLiteral(operand_2));		
					
					//add second operand to the stack
					nodeStack.push(operandInstance);
				
			} else if(elementStr.equalsIgnoreCase("gt") || type.equalsIgnoreCase("ge") || 
						elementStr.equalsIgnoreCase("lt") || type.equalsIgnoreCase("le") ||
						 elementStr.equalsIgnoreCase("eq") || type.equalsIgnoreCase("nq") ) {
				
					Object value = nodeStack.pop();
					Object uriObj = nodeStack.pop();
					
					System.out.println(value);
					String uri;
					if(String.class.isInstance(uriObj)) {
						uri = (String) uriObj;
					} else {
						uri = (String) value;
						value = uriObj;
					}
					
					operandClass = model.getOntClass(NAMESPACE + elementStr);
					operandInstance = operandClass.createIndividual();
														
					//set type
					Property hasTypeProperty = model.getProperty(HAS_TYPE_PROP);
					operandInstance.addProperty(hasTypeProperty, model.createTypedLiteral(UserPreference.getDataProperty(uri)));
					
					//set value
					Property hasValueProperty = model.getProperty(HAS_VALUE_PROP);
					operandInstance.addProperty(hasValueProperty, model.createTypedLiteral(value));
					
					nodeStack.push(operandInstance);	
			} 
		}

		//check that only on operand remains
		if(nodeStack.size() != 1) {
			return null;
		}
		
		OntClass conditionalPreferenceClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual conditionalPreferenceInstance = conditionalPreferenceClass.createIndividual();
		
		Property hasConditionsProperty = model.getProperty(HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty,model.createTypedLiteral(nodeStack.pop())) ;
				
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
		
//		flatList.add(type);
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
				Iterator<Object> iteratorOperands = jsonObj.getJSONArray("operand").iterator();
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
