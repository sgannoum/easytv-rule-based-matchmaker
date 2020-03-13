package com.certh.iti.easytv.rbmm.reasoner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntClass;
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
import org.json.JSONArray;
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
import com.certh.iti.easytv.rbmm.rules.RuleUtils;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.OntProfile;
import com.certh.iti.easytv.rbmm.user.OntUserContext;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.rbmm.webservice.RBMM_WebService;
import com.certh.iti.easytv.user.UserContent;
import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

public class RuleReasoner {

	private final static Logger logger = java.util.logging.Logger.getLogger(RBMM_WebService.class.getName());

	protected RuleReasoner instance;
	private String ontologyFile;
	private OntModel model;
	private Reasoner reasoner;
	private List<Rule> otherRules;
	private List<Rule> suggestionRules;
	
	public RuleReasoner(String ontologyFile, List<Rule> suggestionRules, String[] rulesFile) throws IOException {
		//load built in
		loadBuiltIn();
		
		//load model
		this.model = loadModel(ontologyFile);
		
		//load predicates
		this.model = loadPredicates(model);
		
		//load other rules
		this.otherRules = loadRules(rulesFile);
		
		//suggestions rules
		this.suggestionRules = suggestionRules;
		
		List<Rule> allRules = new ArrayList<Rule>();
		allRules.addAll(otherRules);
		allRules.addAll(suggestionRules);
		
		//Create generic reasoner 
		this.reasoner = new GenericRuleReasoner(allRules);
		
		this.ontologyFile = ontologyFile;
	}
	
	
	public RuleReasoner(String ontologyFile, String[] rulesFile) throws IOException {
		//load built in
		loadBuiltIn();
		
		//load model
		this.model = loadModel(ontologyFile);
		
		//load predicates
		this.model = loadPredicates(model);
		
		//load other rules
		this.otherRules = loadRules(rulesFile);
		
		//load suggestions rules
		this.suggestionRules = loadRules("SuggestionsRules.txt");
		
		List<Rule> allRules = new ArrayList<Rule>();
		allRules.addAll(otherRules);
		allRules.addAll(suggestionRules);
		
		//Create generic reasoner 
		this.reasoner = new GenericRuleReasoner(allRules);
		
		this.ontologyFile = ontologyFile;

	}
	
	public RuleReasoner(String ontologyFile, String suggestionFile, String[] rulesFile) throws IOException {
		//load built in
		loadBuiltIn();
		
		//load model
		this.model = loadModel(ontologyFile);
		
		//load predicates
		this.model = loadPredicates(model);
		
		//load other rules
		this.otherRules = loadRules(rulesFile);
		
		//load suggestions rules
		this.suggestionRules = loadRules(suggestionFile);
		
		List<Rule> allRules = new ArrayList<Rule>();
		allRules.addAll(otherRules);
		allRules.addAll(suggestionRules);
		
		//Create generic reasoner 
		this.reasoner = new GenericRuleReasoner(allRules);
		
		this.ontologyFile = ontologyFile;
	}
	

	public OntModel getOntModel() {
		return model;
	}
	
	private void loadBuiltIn() {
		//Register builds in functions
		BuiltinRegistry.theRegistry.register("NE", new NotEquals());
		BuiltinRegistry.theRegistry.register("EQ", new Equals());
		BuiltinRegistry.theRegistry.register("GT", new GreaterThan());
		BuiltinRegistry.theRegistry.register("GE", new GreaterThanEquals());
		BuiltinRegistry.theRegistry.register("LT", new LessThan());
		BuiltinRegistry.theRegistry.register("LE", new LessThanEquals());
		BuiltinRegistry.theRegistry.register("and", new And());
		BuiltinRegistry.theRegistry.register("or", new OR());
		BuiltinRegistry.theRegistry.register("not", new NOT());
		BuiltinRegistry.theRegistry.register(new MergePreferences());
	}
	
	/**
	 * Get current loaded rules
	 * @return
	 */
	public JSONArray getRules() {
		return RuleUtils.convert(this.suggestionRules);
	}
	
	/**
	 * Update rules
	 */
	public void updateRules(JSONArray jsonRules) {
		
		this.suggestionRules.clear();
		this.suggestionRules = RuleUtils.convert(jsonRules);
		
		List<Rule> allRules = new ArrayList<Rule>();
		allRules.addAll(otherRules);
		allRules.addAll(suggestionRules);
		
		//Create generic reasoner 
		this.reasoner = new GenericRuleReasoner(allRules);
	}
	
	
	/**
	 * Load OWL model from the give file.
	 * 
	 * @param ontologyFile
	 * @return
	 * @throws IOException
	 */
	public OntModel loadModel(String ontologyFile) throws IOException {
		
		//Read model
		OntModel model = ModelFactory.createOntologyModel();
		
		//load ontology model
		model.read(ClassLoader.getSystemResourceAsStream(ontologyFile), null, "");
	
		//load content adaptation service statements
		model.read(ClassLoader.getSystemResourceAsStream("ContentServiceStatments.txt"), null, "N3");
		
		logger.info("Ontology was loaded");
		
		return model;
	}
	
	
	/**
	 * Define predicate for preferences, context and content
	 * @param ontModel
	 */
	public OntModel loadPredicates(OntModel ontModel) {
		
		OntClass userPreferences = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#UserPreferences");
		OntClass userContext = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#UserContext");
		OntClass content = ontModel.getOntClass("http://www.owl-ontologies.com/OntologyEasyTV.owl#Content");
		
		Attribute value;
		String key, uri; 
		DatatypeProperty datatypeProperty;
		Resource range;
		
		//add preference predicates
		for(Entry<String, Attribute> entry :  Preference.getAttributes().entrySet()) {
			value = entry.getValue();
			key = entry.getKey();
			uri =  OntPreference.getPredicate(key);
			datatypeProperty =  ontModel.createDatatypeProperty(uri);
			range = ontModel.getResource(value.getXMLDataTypeURI());
			
			datatypeProperty.addDomain(userPreferences);
			datatypeProperty.setRange(range);
		}
		
		//add context predicates
		for(Entry<String, Attribute> entry :  UserContext.getAttributes().entrySet()) {
			value = entry.getValue();
			key = entry.getKey();
			uri =  OntUserContext.getPredicate(key);
			datatypeProperty =  ontModel.createDatatypeProperty(uri);
			range = ontModel.getResource(value.getXMLDataTypeURI());
			
			datatypeProperty.addDomain(userContext);
			datatypeProperty.setRange(range);
		}
		
		//add content
		for(Entry<String, Attribute> entry :  UserContent.getAttributes().entrySet()) {
			value = entry.getValue();
			key = entry.getKey();
			uri =  Content.getDataProperty(key);
			datatypeProperty =  ontModel.createDatatypeProperty(uri);
			range = ontModel.getResource(value.getXMLDataTypeURI());
			
			datatypeProperty.addDomain(content);
			datatypeProperty.setRange(range);
		}
		
		return ontModel;
		
	}
	
	/**
	 * Load rules from files.
	 * 
	 * @param rulesFile
	 * @return
	 * @throws IOException
	 */
	public List<Rule> loadRules(String[] rulesFile) throws IOException {
		// load files with rules
		List<Rule> rules = new ArrayList<Rule>();
		
		for(String fname : rulesFile) 
			rules.addAll(loadRules(fname));
		
		return rules;
	}
	
	/**
	 * Load rules from files.
	 * 
	 * @param rulesFile
	 * @return
	 * @throws IOException
	 */
	public List<Rule> loadRules(String fname) throws IOException {

		List<Rule> rules = new ArrayList<Rule>();
		URL url = ClassLoader.getSystemResource(fname);
		logger.info("Loadeding rules file..."+url.getFile());
		rules.addAll(Rule.rulesFromURL(url.toString()));
		return rules;
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
		
/*		File file = new File("C:\\Users\\salgan\\Desktop\\model.owl");
		FileOutputStream out = new FileOutputStream(file);
		model.write(out);
*/
		
		//Get inferre model
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);
		
/*		file = new File("C:\\Users\\salgan\\Desktop\\infModel.owl");
		out = new FileOutputStream(file);
		infModel.write(out);
*/
		
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
