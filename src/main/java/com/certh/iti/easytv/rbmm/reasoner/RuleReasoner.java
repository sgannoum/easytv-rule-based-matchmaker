package com.certh.iti.easytv.rbmm.reasoner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.builtin.And;
import com.certh.iti.easytv.rbmm.builtin.Equals;
import com.certh.iti.easytv.rbmm.builtin.GreaterThan;
import com.certh.iti.easytv.rbmm.builtin.GreaterThanEquals;
import com.certh.iti.easytv.rbmm.builtin.LessThan;
import com.certh.iti.easytv.rbmm.builtin.LessThanEquals;
import com.certh.iti.easytv.rbmm.builtin.MergePreferences;
import com.certh.iti.easytv.rbmm.builtin.NOT;
import com.certh.iti.easytv.rbmm.builtin.NotEquals;
import com.certh.iti.easytv.rbmm.builtin.OR;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.rbmm.webservice.RBMM_WebService;

public class RuleReasoner {

	private final static Logger logger = java.util.logging.Logger.getLogger(RBMM_WebService.class.getName());

	protected RuleReasoner instance;
	private OntModel model;
	private Reasoner reasoner;
	
	public RuleReasoner(String ontologyFile, String[] rulesFile) throws IOException {
		//load built in
		loadBuiltIn();
		
		//load model
		model = loadModel(ontologyFile);
		
		//load rules
		reasoner = loadRules(rulesFile);
	}
	
	/**
	 * Update rules
	 */
	public void updateRules() {
		//TO-DO 
	}
	
	
	private void loadBuiltIn() {
		//Register builds in functions
		BuiltinRegistry.theRegistry.register(new NotEquals());
		BuiltinRegistry.theRegistry.register(new Equals());
		BuiltinRegistry.theRegistry.register(new GreaterThan());
		BuiltinRegistry.theRegistry.register(new GreaterThanEquals());
		BuiltinRegistry.theRegistry.register(new LessThan());
		BuiltinRegistry.theRegistry.register(new LessThanEquals());
		BuiltinRegistry.theRegistry.register(new And());
		BuiltinRegistry.theRegistry.register(new OR());
		BuiltinRegistry.theRegistry.register(new NOT());
		BuiltinRegistry.theRegistry.register(new MergePreferences());
	}
	
	/**
	 * Load OWL model from the give file.
	 * 
	 * @param ontologyFile
	 * @return
	 * @throws IOException
	 */
	public OntModel loadModel(String ontologyFile) throws IOException {
		
		File file = new File(getClass()
							.getClassLoader()
							.getResource(ontologyFile)
							.getFile());
		
		//Read model
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		
		//reader
		FileReader reader = new FileReader(new File(getClass().getClassLoader().getResource("ContentServiceStatments.txt").getFile()));
		
		//read the model
		model.read(reader, null, "N-TRIPLE");
		
		//close
		reader.close();
		
	
		logger.info("Ontology was loaded");
		
		return model;
	}
	
	/**
	 * Load rules from files.
	 * 
	 * @param rulesFile
	 * @return
	 * @throws IOException
	 */
	public Reasoner loadRules(String[] rulesFile) throws IOException {
		// load files with rules
		List<Rule> rules = new ArrayList<Rule>();
		ClassLoader classLoader = getClass().getClassLoader();
		
		for(String fname : rulesFile) {
			File file = new File(classLoader
								.getResource(fname)
								.getFile());
			
			logger.info("Loadeding rules file..."+file.getName());
			
			rules.addAll(Rule.rulesFromURL(file.getCanonicalPath()));
		}

		//Create generic reasoner 
		Reasoner reasoner = new GenericRuleReasoner(rules);
		
		return reasoner;
	}
	
	/**
	 * Personalize user profile 
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject infer(OntProfile profile) throws IOException, JSONException {
		
		profile.createOntologyInstance(model);
		
		//run rules and get inferred model
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);
		
		//retrieve user preferences
		JSONObject inferedPreferences = getConditionalPreferences(infModel);
		
		//retrieve user suggested preferences
		JSONObject suggesteddPreferences = getSuggestedUserPreference(infModel);
		
		JSONObject jsonProfile = profile.getProfile().getJSONObject();
		
		//remove context if exists
		jsonProfile.remove("user_context");
		jsonProfile.remove("user_content");
		
		//remove conditional is exists
		jsonProfile.getJSONObject("user_profile")
				   .getJSONObject("user_preferences")
				   .remove("conditional");
				
		//update preference
		jsonProfile.getJSONObject("user_profile")
				   .getJSONObject("user_preferences")
		   		   .getJSONObject("default")
		   		   .put("preferences", inferedPreferences);
		
		//add recommendations
		jsonProfile.getJSONObject("user_profile")
		   		   .getJSONObject("user_preferences")
		   		   .put("recommendations", new JSONObject().put("preferences", suggesteddPreferences));
		

		
		return jsonProfile;
	}
	
	private JSONObject getConditionalPreferences(Model model) {
		
		JSONObject json = new JSONObject();
		
		String req = ""
				+ "PREFIX easyTV: <http://www.owl-ontologies.com/OntologyEasyTV.owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?has ?result " 
				+ "WHERE {"
					+ "?condPref rdf:type easyTV:ConditionalPreference ."
					+ "?condPref easyTV:hasConditions ?cond ." 
					+ "?cond easyTV:isTrue true ." 
					+ "?condPref ?has ?result ." 
					+ "?has rdf:type  rdf:Property ." 
				+ "}";
		
		Query query = QueryFactory.create(req);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet res = qe.execSelect();

		while (res.hasNext()) {
			QuerySolution s = res.next();
			RDFNode result_node = s.get("result");
			RDFNode has_node = s.get("has");

			if (result_node == null || has_node == null || !result_node.isLiteral())
				continue;
		
			updateJson(result_node.asLiteral(), json, has_node.toString());
		}
		
		return json;
	}
	
	/**
	 * Makes a query to the data model and retrieve the given property from the
	 * UserPreferences class
	 * 
	 * @param model
	 * @param property
	 * @return
	 */
	private JSONObject getSuggestedUserPreference(Model model) {
		
		JSONObject json = new JSONObject();
		
		String req = ""
				+ "PREFIX easyTV: <http://www.owl-ontologies.com/OntologyEasyTV.owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?has ?result " 
				+ "WHERE {"
					+ "?user rdf:type easyTV:User ."
					+ "?user easyTV:hasSuggestedPreferences ?pref ." 
					+ "?pref ?has ?result ." 
					+ "?has rdf:type  rdf:Property ."  
				+ "}";
		
		
		Query query = QueryFactory.create(req);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet res = qe.execSelect();

		while (res.hasNext()) {
			QuerySolution s = res.next();
			RDFNode result_node = s.get("result");
			RDFNode has_node = s.get("has");

			if (result_node == null || has_node == null || !result_node.isLiteral())
				continue;
		
			updateJson(result_node.asLiteral(), json, has_node.toString());
		}
		
		return json;
	}
	
	/**
	 * Gets a specific property from the ontology, makes a SPARQL query and adds
	 * the result to a JSON according to the datatype
	 * 
	 * @param model
	 * @param json
	 * @param propertyName
	 * @return
	 */
	private JSONObject updateJson(Literal result, JSONObject json, String propertyName) {
		if (result == null)
			return json;
		
		String pref = OntPreference.getURI(propertyName);
		
		RDFDatatype datatype = result.getDatatype();
		if (datatype != null) {			
			if (datatype.equals(XSDDatatype.XSDboolean)) {
				json.put(pref, result.getBoolean());
			} else if (datatype.equals(XSDDatatype.XSDint)
					|| datatype.equals(XSDDatatype.XSDinteger)) {
				json.put(pref, result.getInt());
			} else if (datatype.equals(XSDDatatype.XSDlong)) {
				json.put(pref, result.getLong());
			} else if (datatype.equals(XSDDatatype.XSDfloat)) {
				json.put(pref, result.getFloat());
			} else if (datatype.equals(XSDDatatype.XSDdouble)) {
				json.put(pref, result.getDouble());
			} else {
				json.put(pref, result.getString());
			}
		} else {
			json.put(pref, result.getValue().toString());
		}
		return json;
	}
	
	/**
	 * prints one by one all triples of the given ontology
	 * 
	 * @param model
	 */
	public  void printModel() {
		StmtIterator it = model.listStatements();
		while (it.hasNext()) {
			Statement stmt = it.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();
			System.out.println(subject + "  " + predicate + "  " + object);
		}
	}
}
