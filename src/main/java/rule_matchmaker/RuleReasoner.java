package rule_matchmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import rule_matchmaker.entities.Auditory;
import rule_matchmaker.entities.User;
import rule_matchmaker.entities.Visual;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class RuleReasoner {

	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	private static final String INPUT_FILE = "input.txt";
	private static final HashMap<String, String> map  =  new HashMap<String, String>() {{
		put("hasAudioVolume", "https://easytvproject.eu/registry/common/audioVolume");
		put("hasFontSize", "https://easytvproject.eu/registry/common/fontSize");
		put("hasBackgroundColor", "https://easytvproject.eu/registry/common/backgroundColor");
		put("hasContrast", "https://easytvproject.eu/registry/common/displayContrast");
		put("hasFontColor", "https://easytvproject.eu/registry/common/fontColor");
		put("hasFonts", "https://easytvproject.eu/registry/common/font");
		put("hasLanguageAudio", "https://easytvproject.eu/registry/common/audioLanguage");
		put("hasLanguageSign", "https://easytvproject.eu/registry/common/signLanguage");
		put("hasLanguageSubtitles", "https://easytvproject.eu/registry/common/subtitles");
    }};

	private OntModel model;

	public static void main(String[] args) throws IOException, JSONException {
			
 		RuleReasoner x = new RuleReasoner();
 		
		File file = new File(x.getClass().getResource(INPUT_FILE).getFile());
		byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
		String jsonString = new String(encoded, Charset.defaultCharset());
 		
		x.loadModel();
		x.createUserInstance(jsonString);
		Model infModel = x.runRules();
		JsonObject pref = x.getUserPreferences(infModel);
		
		JSONObject userProfile = new JSONObject(jsonString);
		System.out.println(updateUserPreferences(userProfile,pref).toString(4));
	}

	
	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject infer(String json) throws IOException, JSONException {
		JSONObject userProfile = new JSONObject(json);
		RuleReasoner x = new RuleReasoner();
		x.loadModel();
		x.createUserInstance(json);
		Model infModel = x.runRules();
	//	return x.getUserPreferences(infModel);
		return updateUserPreferences(userProfile,x.getUserPreferences(infModel));
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static JSONObject updateUserPreferences(JSONObject userProfile, JsonObject inferedPref) throws IOException, JSONException {
		JSONObject jsonPreference = userProfile.getJSONObject("user_preferences").getJSONObject("default").getJSONObject("preferences");
		JSONObject pref = new JSONObject(inferedPref.toString());
		String[] fields = JSONObject.getNames(pref);

		for(int i = 0 ; i < fields.length; i++) 
			if(!jsonPreference.has(fields[i])) {
				jsonPreference.put(fields[i], pref.get(fields[i]));
			}
		
		return userProfile;
	}
	
	private void loadModel() throws IOException {
		File file = new File(getClass().getResource(ONTOLOGY_NAME).getFile());
		model = ModelFactory.createOntologyModel();
		InputStream in = new FileInputStream(file);
		model = (OntModel) model.read(in, null, "");
		System.out.println("Ontology was loaded");
	}

	/**
	 * By using a json input this method creates the corresponding user instance in the ontology
	 * @throws IOException
	 */
	private void createUserInstance(String jsonString) throws IOException{
		//load json input from file
/*		
 * 		File file = new File(getClass().getResource(INPUT_FILE).getFile());
		byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
		String jsonString = new String(encoded, Charset.defaultCharset());
*/
		
		//use jackson to parse json and create user instance
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(jsonString, User.class);
		
		//create the new user in the ontology
		OntClass userClass = model.getOntClass(User.ONTOLOGY_CLASS_URI);
		Individual userInstance = userClass.createIndividual();
		if (user.getAge() != null){
			Property p = model.getProperty(User.AGE_PROP);
			userInstance.addProperty(p, model.createTypedLiteral(user.getAge()));
		}
		if (user.getGender() != null){
			Property p = model.getProperty(User.GENDER_PROP);
			userInstance.addProperty(p, user.getGender());
		}
		
		Visual visual = user.getVisual();
		if (visual != null){
			OntClass visualClass = model.getOntClass(Visual.ONTOLOGY_CLASS_URI);
			Individual visualInstance = visualClass.createIndividual();
			
			if (visual.getColor_blindness() != null){
				Property p = model.getProperty(Visual.COLOR_BLINDNESS_PROP);
				visualInstance.addProperty(p, model.createTypedLiteral(visual.getColor_blindness()));
			}
			
			if (visual.getContrast_sensitivity() != null){
				Property p = model.getProperty(Visual.CONTRAST_SENSIVITY_PROP);
				visualInstance.addProperty(p, model.createTypedLiteral(visual.getContrast_sensitivity()));
			}
			
			if (visual.getVisual_acuity() != null){
				Property p = model.getProperty(Visual.VISUAL_ACUITY_PROP);
				visualInstance.addProperty(p, model.createTypedLiteral(visual.getVisual_acuity()));
			}	
			Property p = model.getProperty(User.VISUAL_PROP);
			userInstance.addProperty(p, visualInstance);	
		}		
		Auditory auditory = user.getAuditory();
		if (auditory != null){
			OntClass auditoryClass = model.getOntClass(Auditory.ONTOLOGY_CLASS_URI);
			Individual auditoryInstance = auditoryClass.createIndividual();
			
			if (auditory.getOneK() != null){
				Property p = model.getProperty(Auditory.ONEK_PROP);
				auditoryInstance.addProperty(p, model.createTypedLiteral(auditory.getOneK()));
			}
			
			if (auditory.getTwoK() != null){
				Property p = model.getProperty(Auditory.TWOK_PROP);
				auditoryInstance.addProperty(p, model.createTypedLiteral(auditory.getTwoK()));
			}
			
			if (auditory.getFourK() != null){
				Property p = model.getProperty(Auditory.FOURK_PROP);
				auditoryInstance.addProperty(p, model.createTypedLiteral(auditory.getFourK()));
			}
			
			if (auditory.getEightK() != null){
				Property p = model.getProperty(Auditory.EIGHTK_PROP);
				auditoryInstance.addProperty(p, model.createTypedLiteral(auditory.getEightK()));
			}
			
			if (auditory.getHalfK() != null){
				Property p = model.getProperty(Auditory.HALFK_PROP);
				auditoryInstance.addProperty(p, model.createTypedLiteral(auditory.getHalfK()));
			}
			
			if (auditory.getQuarterK() != null){
				Property p = model.getProperty(Auditory.QUARTERK_PROP);
				auditoryInstance.addProperty(p, model.createTypedLiteral(auditory.getQuarterK()));
			}
			Property p = model.getProperty(User.AUDITORY_PROP);
			userInstance.addProperty(p, auditoryInstance);	
		}
	}

	/**
	 * Loads a rule from a file use the model of the instance and runs the rule
	 * a new model is created that is used for getting the user preferences
	 * 
	 * @throws IOException
	 */
	private InfModel runRules() throws IOException {
		// load files with rules
		File file = new File(getClass().getResource(RULES_FILE).getFile());
		Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL(file
				.getCanonicalPath()));

		// run the rules
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);

//		printModel(infModel);

		return infModel;
	}

	/**
	 * prints one by one all triples of the given ontology
	 * 
	 * @param model
	 */
	private void printModel(Model model) {
		StmtIterator it = model.listStatements();
		while (it.hasNext()) {
			Statement stmt = it.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();
			System.out.println(subject + "  " + predicate + "  " + object);
		}
	}

	/**
	 * Makes SPARQL queries to retrieve one by one all properties related to
	 * user preferences The results are added in a JSON object
	 * 
	 * @param model
	 *            : the model where the SPARQL queries are made
	 * @return
	 */
	private JsonObject getUserPreferences(Model model) {
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
		
		String pref = map.get(propertyName);
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
}
