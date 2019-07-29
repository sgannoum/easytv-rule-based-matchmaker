package com.certh.iti.easytv.rbmm.user;

public interface OntologicalCondition extends Ontological{

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
	
}
