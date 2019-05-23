package builtin;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Node_Blank;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.Builtin;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.util.iterator.ClosableIterator;

public class MergePreferences implements Builtin {

	public String getName() {
		return "mergePref";
	}

	public String getURI() {
		return null;
	}

	public int getArgLength() {
		return 2;
	}

	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		Node_Blank v1 = (Node_Blank) args[0];
	    Node_Blank v2 = (Node_Blank) args[1];
	    
	    List<Triple> toRemove = new ArrayList<Triple>();
			    
	    ClosableIterator<Triple> condPrefIter = context.find(v2, null, null);

	    while(condPrefIter.hasNext()) {
	    	Triple triple = condPrefIter.next();
	    	
	    	ClosableIterator<Triple> defPrefIter = context.find(v1, triple.getMatchPredicate(), null);
	    	while(defPrefIter.hasNext()) {
	    		Triple temp = defPrefIter.next();
	    		if(temp.getObject().isLiteral()) {
		    		toRemove.add(temp);
	    		}
	    	}

	    	Triple newTriple = new Triple(v1, triple.getPredicate(), triple.getObject());
	    	context.silentAdd(newTriple);
	    }
	    
	    //remove old triples 
	    for(int i = 0; i < toRemove.size(); i++) {
	    	context.remove(toRemove.get(i));
	    }
		
		return true;
	}

	public void headAction(Node[] args, int length, RuleContext context) {
		Node_Blank v1 = (Node_Blank) args[0];
	    Node_Blank v2 = (Node_Blank) args[1];
	    
	    List<Triple> toRemove = new ArrayList<Triple>();
			    
	    ClosableIterator<Triple> condPrefIter = context.find(v2, null, null);

	    while(condPrefIter.hasNext()) {
	    	Triple triple = condPrefIter.next();
	    	
	    	ClosableIterator<Triple> defPrefIter = context.find(v1, triple.getMatchPredicate(), null);
	    	while(defPrefIter.hasNext()) {
	    		Triple temp = defPrefIter.next();
	    		if(temp.getObject().isLiteral()) {
		    		toRemove.add(temp);
	    		}
	    	}

	    	Triple newTriple = new Triple(v1, triple.getPredicate(), triple.getObject());
	    	context.silentAdd(newTriple);
	    }
	    
	    //remove old triples 
	    for(int i = 0; i < toRemove.size(); i++) {
	    	context.remove(toRemove.get(i));
	    }

	}

	public boolean isSafe() {
		return false;
	}

	public boolean isMonotonic() {
		return false;
	}
	

}
