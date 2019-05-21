package builtin;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.Builtin;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.RuleContext;

public class GreaterThan implements Builtin {

	public String getName() {
		return "greaterThan";
	}

	public String getURI() {
		return null;
	}

	public int getArgLength() {
		return 3;
	}

	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		Integer v1 = (Integer) ((Node_Literal) args[0]).getLiteralValue();
		Integer v2 = (Integer) ((Node_Literal) args[1]).getLiteralValue();
		Node_RuleVariable v3 = (Node_RuleVariable) args[2];
		
		BindingEnvironment env = context.getEnv();	

		if(v1 > v2) {
			env.bind(v3,NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
			return true;
		} else {		
			env.bind(v3,NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
			return false;
		}	
	}

	public void headAction(Node[] args, int length, RuleContext context) {
		Integer v1 = (Integer) ((Node_Literal) args[0]).getLiteralValue();
		Integer v2 = (Integer) ((Node_Literal) args[1]).getLiteralValue();
		Node_RuleVariable v3 = (Node_RuleVariable) args[2];
		
		BindingEnvironment env = context.getEnv();	

		
		if(v1 > v2) {
			env.bind(v3,NodeFactory.createLiteralByValue(true, XSDDatatype.XSDboolean));
		} else {		
			env.bind(v3,NodeFactory.createLiteralByValue(false, XSDDatatype.XSDboolean));
		}	
	}

	public boolean isSafe() {
		return false;
	}

	public boolean isMonotonic() {
		return false;
	}
	

}
