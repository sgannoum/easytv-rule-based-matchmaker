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
//http://localhost:8080/EasyTV_RBMM_Restful_WS/rules
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
	
    @GET
    @Path("personalize/rules")
    public Response getRules() throws IOException, JSONException
    {
    	char[] content = new char[1024 * 3];
		File file = new File(this.getClass().getClassLoader().getResource(RULES_FILE).getFile());
		FileReader reader = new FileReader(file);

		reader.read(content);
		reader.close();
				
		GenericEntity<String> entity = new GenericEntity<String>(new String(content)) {};

        return Response.status(200).entity(entity).build();
    }
	
    @GET
    @Path("personalize/profile")
    public Response getInfor() throws IOException, JSONException
    {
        return Response.status(200).build();
    }
	
    @POST
    @Path("personalize/profile")
    @Consumes("application/json")
    public Response personalizeProfile(Object tmpInput) throws IOException, JSONException
    {
    	JSONObject json = new JSONObject((Map)tmpInput);	
    	
		if(!json.has("user_id")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_id element. }");
			return Response.status(201).entity(err).build();
		}
    	
		if(!json.has("user_profile")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_profile element. }");
			return Response.status(201).entity(err).build();
		}
		
		//Unmarshal inputs
		int user_id = json.getInt("user_id");
		JSONObject user_profile = json.getJSONObject("user_profile");
		
		//infer
		JSONObject personalized_profile = ruleReasoner.infer(user_profile);
		
		//Marshal the results
		JSONObject response = new JSONObject("{user_id: "+String.valueOf(user_id)+", user_profile: "+personalized_profile+"}");
    	
        return Response.status(200).entity(response.toString(4)).build();
    }
    
    @POST
    @Path("personalize/context")
    @Consumes("application/json")
    public Response personalizeContext(Object tmpInput) throws IOException, JSONException
    {
    	JSONObject json = new JSONObject((Map)tmpInput);	
    	
    	//user_id
		if(!json.has("user_id")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_id element. }");
			return Response.status(201).entity(err).build();
		}
    	
		//user_profile
		if(!json.has("user_profile")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_profile element. }");
			return Response.status(201).entity(err).build();
		}
	
		//context
		if(!json.has("user_context")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_context element. }");
			return Response.status(201).entity(err).build();
		}
		
		int user_id = json.getInt("user_id");
		JSONObject user_profile = json.getJSONObject("user_profile");
		JSONObject user_context = json.getJSONObject("user_context");
		
		JSONObject personalized_profile = ruleReasoner.infer(user_profile, user_context);
		
		//Marshal the results
		JSONObject response = new JSONObject("{user_id: "+String.valueOf(user_id)+", user_profile: "+personalized_profile+"}");
    	
        return Response.status(200).entity(response.toString(4)).build();
    }
    
    @POST
    @Path("personalize/content")
    @Consumes("application/json")
    public Response personalizeContent(Object tmpInput) throws IOException, JSONException
    {
    	JSONObject json = new JSONObject((Map)tmpInput);	
    	
    	//user_id
		if(!json.has("user_id")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_id element. }");
			return Response.status(201).entity(err).build();
		}
    	
		//user_profile
		if(!json.has("user_profile")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_profile element. }");
			return Response.status(201).entity(err).build();
		}
	
		//context
		if(!json.has("user_context")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_context element. }");
			return Response.status(201).entity(err).build();
		}
		
		//content
		if(!json.has("user_content")) {
			JSONObject err = new JSONObject("{ code: 401, msg: Missing user_content element. }");
			return Response.status(201).entity(err).build();
		}
		
		int user_id = json.getInt("user_id");
		JSONObject user_profile = json.getJSONObject("user_profile");
		JSONObject user_context = json.getJSONObject("user_context");
		JSONObject user_content = json.getJSONObject("user_content");
		
		JSONObject personalized_profile = ruleReasoner.infer(user_profile, user_context, user_content);
    	
		//Marshal the results
		JSONObject response = new JSONObject("{user_id: "+String.valueOf(user_id)+", user_profile: "+personalized_profile+"}");
    	
        return Response.status(200).entity(response.toString(4)).build();
    }
}
