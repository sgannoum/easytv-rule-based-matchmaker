package com.certh.iti.easytv.rbmm.builtin;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.Builtin;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.RuleContext;

public class NOT implements Builtin {

	public String getName() {
		return "not";
	}

	public String getURI() {
		return null;
	}

	public int getArgLength() {
		return 2;
	}

	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		Node_Literal v1 = (Node_Literal) args[0];
		Node_RuleVariable v2 = (Node_RuleVariable) args[1];
		
		BindingEnvironment env = context.getEnv();	

		if(v1.getLiteralValue().equals(Boolean.TRUE)) {
			env.bind(v2,NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
		} else {		
			env.bind(v2,NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
		}	
		return true;
	}

	public void headAction(Node[] args, int length, RuleContext context) {
		Node_Literal v1 = (Node_Literal) args[0];
		Node_RuleVariable v2 = (Node_RuleVariable) args[1];
		
		BindingEnvironment env = context.getEnv();	

		if(v1.getLiteralValue().equals(Boolean.TRUE)) {
			env.bind(v2,NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
		} else {		
			env.bind(v2,NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
		}	
	}

	public boolean isSafe() {
		return false;
	}

	public boolean isMonotonic() {
		return false;
	}
	

}
