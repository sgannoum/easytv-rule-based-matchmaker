package com.certh.iti.easytv.rbmm.reasoner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.certh.iti.easytv.rbmm.user.UserProfile;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.UserPreferencesMappings;
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

public class RuleReasoner {

	private OntModel model;
	private Reasoner reasoner;
	
	private String ontologyFile;
	private String rulesFile;
	
	public RuleReasoner(String ontologyFile, String rulesFile) throws IOException {
		this.ontologyFile = ontologyFile;
		this.rulesFile = rulesFile;
		
		//load model
		model = loadModel(ontologyFile);
		
		//load rules
		reasoner = loadRules(rulesFile);
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject infer(String userFile) throws IOException, JSONException {
		
	    String result = "";
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(new File(getClass().getClassLoader().getResource(userFile).getFile())));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        result = sb.toString();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
		
	    JSONObject userProfile = new JSONObject(result);
		
		return infer(userProfile);
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject infer(JSONObject userProfile) throws IOException, JSONException {
		
		//load user file
		UserProfile user = new UserProfile(userProfile);
		
		//load user into ontology
		user.createOntologyInstance(model);
		
		//If content information if available
		if(userProfile.has("content")) {
			Content content = new Content(userProfile.getJSONObject("content"));
			content.createOntologyInstance(model);
		}
		
		//run rules and get inferred model
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);
		
		//update user preferences
		JSONObject newUserprofile = updateUserPreferences(userProfile, getUserPreferences(infModel));
		
		//update user suggested preferences
		newUserprofile = updateSuggestedUserPreferences(newUserprofile,  getSuggestedUserPreferences(infModel));
		
		//add the inferred preferences to the user model
		return newUserprofile;
	}
	
	public OntModel loadModel(String ontologyFile) throws IOException {
		
		File file = new File(getClass().getClassLoader().getResource(ontologyFile).getFile());
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
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
		
		System.out.println("Ontology was loaded");
		
		return model;
	}
	
	public Reasoner loadRules(String[] rulesFile) throws IOException {
		// load files with rules
		List<Rule> rules = new ArrayList<Rule>();
		
		for(String fname : rulesFile) {
			File file = new File(getClass()
								.getClassLoader()
								.getResource(fname)
							.getFile());
			
			rules.addAll(Rule.rulesFromURL(file.getCanonicalPath()));
		}

		//Create generic reasoner 
		Reasoner reasoner = new GenericRuleReasoner(rules);
		System.out.println("Rules was loaded");
		return reasoner;
	}
	
	public Reasoner loadRules(String fname) throws IOException {
		// load file with rules
		File file = new File(getClass()
							.getClassLoader()
							.getResource(fname)
						.getFile());
		
		//Create generic reasoner 
		Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL(file.getCanonicalPath()));
		System.out.println("Rules from file: "+fname+" was loaded");

		return reasoner;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static JSONObject updateUserPreferences(JSONObject userProfile, JSONObject inferedPref) throws IOException, JSONException {
		JSONObject jsonPreference = userProfile.getJSONObject("user_preferences")
											   .getJSONObject("default")
											   .getJSONObject("preferences");
		
		JSONObject pref = new JSONObject(inferedPref.toString());
		String[] fields = JSONObject.getNames(pref);

		for(int i = 0 ; i < fields.length; i++) 
			jsonPreference.put(fields[i], pref.get(fields[i]));
		
		return userProfile;
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static JSONObject updateSuggestedUserPreferences(JSONObject userProfile, JSONObject inferedSuggestedPref) throws IOException, JSONException {		
		JSONObject userSuggestedPref =  new JSONObject();
		JSONObject pref = new JSONObject(inferedSuggestedPref.toString());
		String[] fields = JSONObject.getNames(pref);

		if(fields != null) {
			for(int i = 0 ; i < fields.length; i++) 
				userSuggestedPref.put(fields[i], pref.get(fields[i]));
			
			userProfile.put("recommanded_preferences", userSuggestedPref);
		}
		
		return userProfile;
	}
	
	/**
	 * Makes SPARQL queries to retrieve one by one all properties related to
	 * user preferences The results are added in a JSON object
	 * 
	 * @param model
	 *            : the model where the SPARQL queries are made
	 * @return
	 */
	public JSONObject getUserPreferences(Model model) {
		JSONObject userPreferences = new JSONObject();
		userPreferences = updateJson(model, userPreferences, "hasBackground");
		userPreferences = updateJson(model, userPreferences, "hasHighlight");
		userPreferences = updateJson(model, userPreferences, "hasCursorSize");
		userPreferences = updateJson(model, userPreferences, "hasCursorColour");
		userPreferences = updateJson(model, userPreferences, "hasCursorTrails");
		userPreferences = updateJson(model, userPreferences, "hasMagnification");
		userPreferences = updateJson(model, userPreferences, "hasFontSize");
		userPreferences = updateJson(model, userPreferences, "hasFontType");
		userPreferences = updateJson(model, userPreferences, "hasFontColor");
		userPreferences = updateJson(model, userPreferences, "hasSpeechRate");
		userPreferences = updateJson(model, userPreferences, "hasPitch");
		userPreferences = updateJson(model, userPreferences, "hasVolume");
		userPreferences = updateJson(model, userPreferences, "hasVoiceProfile");
		userPreferences = updateJson(model, userPreferences, "hasMicrophoneGain");
		userPreferences = updateJson(model, userPreferences, "hasDictation");
		userPreferences = updateJson(model, userPreferences, "hasConfirmationFeedBack");
		userPreferences = updateJson(model, userPreferences, "hasAudioVolume");
		userPreferences = updateJson(model, userPreferences, "hasAudioLanguage");

		System.out.println(userPreferences);
		return userPreferences;
	}
	
	/**
	 * Makes SPARQL queries to retrieve one by one all properties related to
	 * user preferences The results are added in a JSON object
	 * 
	 * @param model
	 *            : the model where the SPARQL queries are made
	 * @return
	 */
	public JSONObject getSuggestedUserPreferences(Model model) {
		JSONObject userPreferences = new JSONObject();
		userPreferences = updateSuggestedJson(model, userPreferences, "hasBackground");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasHighlight");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasCursorSize");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasCursorColour");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasCursorTrails");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasMagnification");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasFontSize");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasFontType");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasFontColor");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasSpeechRate");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasPitch");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasVolume");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasVoiceProfile");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasMicrophoneGain");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasDictation");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasConfirmationFeedBack");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasAudioVolume");
		userPreferences = updateSuggestedJson(model, userPreferences, "hasAudioLanguage");

		System.out.println(userPreferences);
		return userPreferences;
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
	private JSONObject updateJson(Model model, JSONObject json, String propertyName) {
		Literal result = getUserPreference(model, propertyName);
		if (result == null)
			return json;
		
		String pref = UserPreferencesMappings.dataPropertyToUri.get(UserProfile.NAMESPACE + propertyName);
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
	 * Gets a specific property from the ontology, makes a SPARQL query and adds
	 * the result to a JSON according to the datatype
	 * 
	 * @param model
	 * @param json
	 * @param propertyName
	 * @return
	 */
	private JSONObject updateSuggestedJson(Model model, JSONObject json, String propertyName) {
		Literal result = getSuggestedUserPreference(model, propertyName);
		if (result == null)
			return json;
		
		
		String pref = UserPreferencesMappings.dataPropertyToUri.get(UserProfile.NAMESPACE + propertyName);
		
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
	 * Makes a query to the data model and retrieve the given property from the
	 * UserPreferences class
	 * 
	 * @param model
	 * @param property
	 * @return
	 */
	private Literal getUserPreference(Model model, String property) {
		String query = ""
				+ "PREFIX easyTV: <http://www.owl-ontologies.com/OntologyEasyTV.owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?result " 
				+ "WHERE {"
				+ "?pref  rdf:type easyTV:UserPreferences ." 
				+ "?pref  easyTV:" + property+ " ?result ." 
				+ "}";
		return executeSparqlQuerytSparqlResult(query, model);
	}
	
	/**
	 * Makes a query to the data model and retrieve the given property from the
	 * UserPreferences class
	 * 
	 * @param model
	 * @param property
	 * @return
	 */
	private Literal getSuggestedUserPreference(Model model, String property) {
		String query = ""
				+ "PREFIX easyTV: <http://www.owl-ontologies.com/OntologyEasyTV.owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?result " 
				+ "WHERE {"
				+ "?pref  rdf:type easyTV:SuggestedPreferences ." 
				+ "?pref  easyTV:" + property+ " ?result ." 
				+ "}";
		return executeSparqlQuerytSparqlResult(query, model);
	}

	/**
	 * Method for executing a SPARQL query It returns the variable named
	 * "result" IT DOES NOT WORK FOR MULTIPLE RESULTS (it will send the first
	 * that it finds)
	 * 
	 * @param req
	 *            : the SPARQL query to be executed
	 * @param model
	 *            : the model where the query is made
	 * @return
	 */
	private Literal executeSparqlQuerytSparqlResult(String req, Model model) {
		Query query = QueryFactory.create(req);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet res = qe.execSelect();

		Literal result = null;
		while (res.hasNext()) {
			QuerySolution s = res.next();
			RDFNode node = s.get("result");
			if (node == null)
				continue;
			return node.asLiteral();
		}
		return result;
	}
	
	/**
	 * prints one by one all triples of the given ontology
	 * 
	 * @param model
	 */
	public static void printModel(Model model) {
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
