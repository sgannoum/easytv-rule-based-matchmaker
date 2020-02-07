package com.certh.iti.easytv.rbmm.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.reasoner.TriplePattern;
import org.apache.jena.reasoner.rulesys.ClauseEntry;
import org.apache.jena.reasoner.rulesys.Functor;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.rbmm.webservice.RBMM_WebService;

/**
 * A set of utils to convert a jena rule from and to JSON format
 *
 */
public class RuleUtils {
	
	private final static Logger logger = java.util.logging.Logger.getLogger(RuleUtils.class.getName());

	/**
	 * Convert JSON given rules to jena rules
	 * @param jsonRules
	 * @return
	 */
	public static List<Rule> convert(JSONArray jsonRules) {
		
		List<Rule> rules = new ArrayList<Rule>();
		
		for(int i = 0; i < jsonRules.length(); i++) {
			JSONObject operand = (JSONObject) jsonRules.get(i);
			Rule rule = convert(operand);
			rules.add(rule);
		}

		return rules;
	}
	
	/**
	 * Convert from JSON to Jena rule
	 * @param rule
	 * @return
	 */
	public static Rule convert(JSONObject rule) {
			
		Map<String, String> vars = new HashMap<String, String>();
		StringBuffer buff = new StringBuffer();
		int var = 0;
	
		buff.append("[");
		if(rule.has("name")) buff.append(rule.getString("name")+":");
		buff.append("(?user rdf:type http://www.owl-ontologies.com/OntologyEasyTV.owl#User)");
		buff.append("(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasPreference ?pref)");
		buff.append("(?user http://www.owl-ontologies.com/OntologyEasyTV.owl#hasSuggestedPreferences ?sugPref)");
		
		//Handle head
		JSONArray body = rule.getJSONArray("body");
		for(int i = 0; i < body.length(); i++) {
			JSONObject statement = body.getJSONObject(i);
			
			String preference = statement.getString("prefence");
			String predicate = OntPreference.getDataProperty(preference);
			String functor = statement.getString("type");
			JSONArray args = statement.getJSONArray("args");
			for(int j = 0; j < args.length(); j++) {
				JSONObject arg = args.getJSONObject(j);			
	
				if(functor.equalsIgnoreCase("eq"))
					buff.append(String.format("(?pref %s '%s'^^%s)\n", predicate, arg.get("value"), arg.getString("xml-type")));
				else {
					//look for variable
					String prefVar;
					
					if((prefVar = vars.get(predicate)) == null) {
						prefVar = String.format("?var%d", var++);
						buff.append(String.format("(?pref %s %s)\n", predicate, prefVar));						
						vars.put(predicate, prefVar);
					} 
					
					buff.append(String.format("%S(%s '%s'^^%s)\n", functor, prefVar, arg.get("value"),  arg.getString("xml-type")));
				}
			}
		}
		
		buff.append("->");

		//handle body
		JSONArray head = rule.getJSONArray("head");
		for(int i = 0; i < head.length(); i++) {
			JSONObject statement = head.getJSONObject(i);
			
			String preference = statement.getString("prefence");
			String predicate =  OntPreference.getDataProperty(preference);
			JSONArray args = statement.getJSONArray("args");
			for(int j = 0; j < args.length(); j++) {
				JSONObject arg = args.getJSONObject(j);				
				buff.append(String.format("(?sugPref %s '%s'^^%s)\n", predicate, arg.get("value"), arg.getString("xml-type")));
			}
		}

		buff.append("]\n");

		return Rule.parseRule(buff.toString());
	}
	
	
	public static JSONArray convert(List<Rule> rules) {
		
		JSONArray jsonRules = new JSONArray();
		for(Rule rule: rules)
			jsonRules.put(convert(rule));

		return jsonRules;
	}
	
	
	public static JSONObject convert(Rule rule) {
		
		System.out.println(rule.getName());
		
		HashMap<String, String> variableMpper = new HashMap<String, String>();
		JSONObject jsonRule = new JSONObject();
		if(rule.getName() != null) jsonRule.put("name", rule.getName());
		
		JSONArray bodyOperands = convert(rule.getBody(), variableMpper);
		jsonRule.put("body", bodyOperands);
		
		JSONArray headOperands = convert(rule.getHead(), variableMpper);
		jsonRule.put("head", headOperands);

		
		return jsonRule;
	}
	
	
	private static JSONArray convert(ClauseEntry[] entries, HashMap<String, String> variableMpper) {
		JSONArray bodyOperands = new JSONArray();

		
		for(ClauseEntry entry : entries) {
			String preference = null;
			
			//TripelPattern case
			if(TriplePattern.class.isInstance(entry)) {
				TriplePattern t = (TriplePattern) entry;
				
				Node_RuleVariable subject = (Node_RuleVariable) t.getSubject();
				Node_URI predicate = (Node_URI) t.getPredicate();
				Node object = t.getObject();

				//check that the predicate is a preference
				if(!predicate.getURI().startsWith(Ontological.NAMESPACE) || 
						!predicate.getURI().contains("has_"))
					continue;
					
				//Convert to preference URI
				preference = OntPreference.getURI(predicate.getURI());
				
				if(object.isLiteral()) {
					Node_Literal literal = (Node_Literal) object;
					JSONObject operand = new JSONObject()
											.put("type", "EQ")
											.put("prefence", preference)
											.put("args", new JSONArray().put(new JSONObject()
																			.put("value", literal.getLiteralValue())
																			.put("xml-type", literal.getLiteralDatatypeURI())));
					
					bodyOperands.put(operand);
					
				} else if(object.isVariable()) {
					Node_RuleVariable variable = (Node_RuleVariable) object;
					//System.out.println("add: "+variable.getName());
					variableMpper.put(variable.getName(), preference);
				}
				
				
			} else if(Functor.class.isInstance(entry)) {
				
				//TODO implement handling more than one argument
				
				Functor functor = (Functor) entry;
				JSONArray args = new JSONArray();
						
				for(Node arg: functor.getArgs())
					if(arg.isVariable()) {
						Node_RuleVariable variable = (Node_RuleVariable) arg;
												
						if((preference = variableMpper.get(variable.getName())) == null)
							throw new IllegalArgumentException("Unknwon variable name: "+variable.getName());
						
					} else if(arg.isLiteral()) {
						Node_Literal var = (Node_Literal) arg;
						
						args.put(new JSONObject()
										.put("value", var.getLiteralValue())
										.put("xml-type", var.getLiteralDatatypeURI()));
					}
				
				
				JSONObject operand = new JSONObject()
										.put("type", functor.getImplementor().toString())
										.put("prefence", preference)
										.put("args", args);
								
				bodyOperands.put(operand);
			}
		}

		return bodyOperands;
	}

}
