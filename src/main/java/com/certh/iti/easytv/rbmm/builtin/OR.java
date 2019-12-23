package com.certh.iti.easytv.rbmm.builtin;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.Builtin;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.RuleContext;

public class OR implements Builtin {

	public String getName() {
		return "or";
	}

	public String getURI() {
		return null;
	}

	public int getArgLength() {
		return 3;
	}

	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		Node variable = args[args.length - 1];
		BindingEnvironment env = context.getEnv();	
		
		boolean or_value = evaluate(args);

		if(or_value) {
			env.bind(variable, NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
		} else {		
			env.bind(variable, NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
		}	
		return true;
	}

	public void headAction(Node[] args, int length, RuleContext context) {
		Node_Literal v1 = (Node_Literal) args[0];
		Node_Literal v2 = (Node_Literal) args[1];
		Node_RuleVariable v3 = (Node_RuleVariable) args[2];
		
		BindingEnvironment env = context.getEnv();	

		if(v1.getLiteralValue().equals(Boolean.TRUE) || v2.getLiteralValue().equals(Boolean.TRUE)) {
			env.bind(v3, NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
		} else {		
			env.bind(v3, NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
		}	
	}

	public boolean isSafe() {
		return false;
	}

	public boolean isMonotonic() {
		return false;
	}
	
	private boolean evaluate(Node[] args) {
		boolean or_value = ((Node_Literal) args[0]).getLiteralValue().equals(Boolean.TRUE) ? true : false;
		
		for(int i = 1; i < args.length - 1 ; i++ ) 
			or_value |= ((Node_Literal) args[i]).getLiteralValue().equals(Boolean.TRUE) ? true : false;
		
		
		return or_value;
	}
	

}
