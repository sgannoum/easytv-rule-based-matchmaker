package rule_matchmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import rule_matchmaker.entities.User;
import rule_matchmaker.entities.UserPreferencesMappings;

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
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

public class RuleReasoner {

	private OntModel model;

	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static JSONObject infer(String ontologyFile, String ruleFile, String json) throws IOException, JSONException {
		JSONObject userProfile = new JSONObject(json);
		RuleReasoner x = new RuleReasoner();
		
		x.loadModel(ontologyFile);
		x.loadUser(json);
		
		//run rules and get inferred model
		Model infModel = x.runRules(ruleFile);
		
		//add the inferred preferences to the user model
		return updateUserPreferences(userProfile,x.getUserPreferences(infModel));
	}
	
	public void loadUser(String userProfile) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(userProfile.toString(), User.class);
		
		//load user into ontology
		user.createOntologyInstance(model);
	}
	
	public void loadModel(String ontologyFile) throws IOException {
		File file = new File(getClass().getResource(ontologyFile).getFile());
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		System.out.println("Ontology was loaded");
	}
	
	/**
	 * Loads a rule from a file use the model of the instance and runs the rule
	 * a new model is created that is used for getting the user preferences
	 * 
	 * @throws IOException
	 */
	public InfModel runRules(String rulesFile) throws IOException {
		// load files with rules
		File file = new File(getClass().getResource(rulesFile).getFile());
		Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL(file
				.getCanonicalPath()));

		// run the rules
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);

		//printModel(infModel);

		return infModel;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static JSONObject updateUserPreferences(JSONObject userProfile, JsonObject inferedPref) throws IOException, JSONException {
		JSONObject jsonPreference = userProfile.getJSONObject("user_preferences")
											   .getJSONObject("default")
											   .getJSONObject("preferences");
		
		JSONObject pref = new JSONObject(inferedPref.toString());
		String[] fields = JSONObject.getNames(pref);

		for(int i = 0 ; i < fields.length; i++) 
			if(!jsonPreference.has(fields[i])) {
				jsonPreference.put(fields[i], pref.get(fields[i]));
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
	public JsonObject getUserPreferences(Model model) {
		JsonObject userPreferences = new JsonObject();
		userPreferences = updateJson(model, userPreferences, "hasAudioVolume");
		userPreferences = updateJson(model, userPreferences, "hasFontSize");
		userPreferences = updateJson(model, userPreferences, "hasBackgroundColor");
		userPreferences = updateJson(model, userPreferences, "hasContrast");
		userPreferences = updateJson(model, userPreferences, "hasFontColor");
		userPreferences = updateJson(model, userPreferences, "hasFonts");
		userPreferences = updateJson(model, userPreferences, "hasLanguageAudio");
		userPreferences = updateJson(model, userPreferences, "hasLanguageSign");
		userPreferences = updateJson(model, userPreferences, "hasLanguageSubtitles");
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
	private JsonObject updateJson(Model model, JsonObject json, String propertyName) {
		Literal result = getUserPreference(model, propertyName);
		if (result == null)
			return json;
		
		String pref = UserPreferencesMappings.dataPropertyToUri.get(propertyName);
		RDFDatatype datatype = result.getDatatype();
		if (datatype != null) {			
			if (datatype.equals(XSDDatatype.XSDboolean)) {
				json.addProperty(pref, result.getBoolean());
			} else if (datatype.equals(XSDDatatype.XSDint)
					|| datatype.equals(XSDDatatype.XSDinteger)) {
				json.addProperty(pref, result.getInt());
			} else if (datatype.equals(XSDDatatype.XSDlong)) {
				json.addProperty(pref, result.getLong());
			} else if (datatype.equals(XSDDatatype.XSDfloat)) {
				json.addProperty(pref, result.getFloat());
			} else if (datatype.equals(XSDDatatype.XSDdouble)) {
				json.addProperty(pref, result.getDouble());
			} else {
				json.addProperty(pref, result.getString());
			}
		} else {
			json.addProperty(pref, result.getValue().toString());
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
				+ "PREFIX : <http://www.owl-ontologies.com/OntologyEasyTV.owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?result " + "WHERE{"
				+ "?pref  rdf:type :UserPreferences ." + "?pref  :" + property
				+ " ?result." + "}";
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
