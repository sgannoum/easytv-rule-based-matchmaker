package com.certh.iti.easytv.rbmm.builtin;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.RuleContext;

public class GreaterThan extends ComparatorBuiltin {

	@Override
	public String getName() {
		return "greaterThan";
	}

	public String getURI() {
		return null;
	}

	@Override
	public int getArgLength() {
		return 3;
	}

	@Override
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		Node_Literal v1 = (Node_Literal) args[0];
		Node_Literal v2 = (Node_Literal) args[1];
		Node_RuleVariable v3 = (Node_RuleVariable) args[2];
		
		BindingEnvironment env = context.getEnv();	

		if(compareTo(v1, v2) > 0) {
			env.bind(v3,NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
			return true;
		} else {		
			env.bind(v3,NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
			return false;
		}	
	}

	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		Node_Literal v1 = (Node_Literal) args[0];
		Node_Literal v2 = (Node_Literal) args[1];
		Node_RuleVariable v3 = (Node_RuleVariable) args[2];
		
		BindingEnvironment env = context.getEnv();	

		if(compareTo(v1, v2) > 0) {
			env.bind(v3,NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
		} else {		
			env.bind(v3,NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
		}	
	}

}
