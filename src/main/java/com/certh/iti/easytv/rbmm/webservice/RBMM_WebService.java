package com.certh.iti.easytv.rbmm.webservice;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Property;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;
import com.certh.iti.easytv.rbmm.user.Content;
import com.certh.iti.easytv.rbmm.user.UserContext;
import com.certh.iti.easytv.rbmm.user.UserProfile;

@Path("/")
public class RBMM_WebService
{
	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	private RuleReasoner ruleReasoner;
	
	public RBMM_WebService() {
		try {
			ruleReasoner = new RuleReasoner(ONTOLOGY_NAME, RULES_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    //http://localhost:8080/EasyTV_RBMM_Restful_WS/rules
    @GET
    @Path("rules")
    public Response getEasyTVRules() throws IOException, JSONException
    {
    	char[] content = new char[1024 * 3];
		File file = new File(this.getClass().getClassLoader().getResource(RULES_FILE).getFile());
		FileReader reader = new FileReader(file);

		reader.read(content);
		reader.close();
				
		GenericEntity<String> entity = new GenericEntity<String>(new String(content)) {};

        return Response.status(200).entity(entity).build();
    }
	
    //http://localhost:8080/EasyTV_RBMM_Restful_WS/match
    @GET
    @Path("match")
    public Response getRunEasyTVRules() throws IOException, JSONException
    {
        return Response.status(200).build();
    }
	
    //http://localhost:8080/EasyTV_RBMM_Restful_WS/match
    @POST
    @Path("match")
    @Consumes("application/json")
    public Response postRunEasyTVRules(Object tmpInput) throws IOException, JSONException
    {
    	JSONObject json = new JSONObject((Map)tmpInput);	
    	
		if(!json.has("user_profile")) {
			//TO-DO handle the case of non-existing profile 
		}
	
		//context
		if(!json.has("user_context")) {
			//TO-DO handle the case of non-existing user_context 
		}
		
		JSONObject userProfile = json.getJSONObject("user_profile");
		JSONObject userContext = json.getJSONObject("user_context");
		
		JSONObject personalizedProfile = ruleReasoner.infer(userProfile, userContext);
    	
        return Response.status(200).entity(personalizedProfile.toString(4)).build();
    }
}
