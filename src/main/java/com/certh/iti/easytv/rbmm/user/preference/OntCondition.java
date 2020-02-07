package com.certh.iti.easytv.rbmm.user.preference;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.user.preference.Condition;

public class OntCondition implements Ontological {
	
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "ConditionalPreference";
		
    // Data Properties
	public static final String HAS_LEFT_OPERAND_PROP = NAMESPACE + "hasLeftOperand";
	public static final String HAS_RIGHT_OPERAND_PROP = NAMESPACE + "hasRightOperand";
	public static final String HAS_CONDITIONS_PROP = NAMESPACE + "hasConditions";

    
	// Data Properties
	public static final String HAS_TYPE_PROP = NAMESPACE + "hasType";
	public static final String HAS_VALUE_PROP = NAMESPACE + "hasValue";
	public static final String IS_TURE_PROP = NAMESPACE + "isTrue";
	
	
	private Condition condition;
	
	public OntCondition(Condition condition) {
		this.condition = condition;
	}
	
	public Condition getCondition() {
		return condition;
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
		Individual individual = createOntologyInstance(model, conditionalPreferenceInstance, condition);
		
		Property hasConditionsProperty = model.getProperty(HAS_CONDITIONS_PROP);
		conditionalPreferenceInstance.addProperty(hasConditionsProperty, individual) ;
		
		return conditionalPreferenceInstance;
	}

	

	/**
	 * Handle the operand of a condition
	 * 
	 * @param model
	 * @param conditionalPreferenceInstance
	 * @param condition
	 * @return
	 */
	private Individual createOntologyInstance(final OntModel model, Individual conditionalPreferenceInstance, Condition condition){
					
			String conditionType = (String) condition.getType();
			List<Object> conditionOprands = condition.getOperands();
			
			 if(conditionType.equalsIgnoreCase("gt") || conditionType.equalsIgnoreCase("ge") || 
					conditionType.equalsIgnoreCase("lt") || conditionType.equalsIgnoreCase("le") ||
						 conditionType.equalsIgnoreCase("eq") || conditionType.equalsIgnoreCase("ne") ) {
				
					
					Object uriObj = conditionOprands.get(0);
					Object value = conditionOprands.get(1);
	
					String uri = (String) uriObj;
										
					OntClass operandClass = model.getOntClass(NAMESPACE + conditionType.toUpperCase());
					Individual operandInstance = operandClass.createIndividual();
																			
					//set type
					Property hasTypeProperty = model.getProperty(HAS_TYPE_PROP);
					operandInstance.addProperty(hasTypeProperty, model.createProperty(OntUserContext.getDataProperty(uri)));
					//set value
					Property hasValueProperty = model.getProperty(HAS_VALUE_PROP);
					operandInstance.addProperty(hasValueProperty, model.createTypedLiteral(value));
					
					return operandInstance;
					
			} else {
				
					Individual last = null;
					List<Individual> individuals = new ArrayList<Individual>();
					
					//handle operands
					for(Object obj : conditionOprands) {
						OntCondition tmp = new OntCondition((com.certh.iti.easytv.user.preference.Condition) obj);
						individuals.add(tmp.createOntologyInstance(model, conditionalPreferenceInstance, (Condition) obj));
					}
					
					
					if(conditionType.equalsIgnoreCase("and") || 
							conditionType.equalsIgnoreCase("or")) {
					
						while(!individuals.isEmpty()) {
							Individual operand_1 = (Individual) individuals.remove(0);
							Individual operand_2 = (Individual) individuals.remove(0);
						
							OntClass operandClass = model.getOntClass(NAMESPACE + conditionType.toUpperCase());
							last = operandClass.createIndividual();
							
							//set left operand
							Property leftOperandProperty = model.getProperty(HAS_LEFT_OPERAND_PROP);
							last.addProperty(leftOperandProperty, operand_1);
							
							//set right operand
							Property rightOperandProperty = model.getProperty(HAS_RIGHT_OPERAND_PROP);
							last.addProperty(rightOperandProperty, operand_2);		
						}
						
					} if(conditionType.equalsIgnoreCase("not")) {
						
						while(!individuals.isEmpty()) {
							Individual operand_1 = (Individual) individuals.remove(0);
						
							OntClass operandClass = model.getOntClass(NAMESPACE + conditionType.toUpperCase());
							last = operandClass.createIndividual();
							
							//set left operand
							Property leftOperandProperty = model.getProperty(HAS_LEFT_OPERAND_PROP);
							last.addProperty(leftOperandProperty, operand_1);	
						}
					}
							
					return last;
		}
	}

}
