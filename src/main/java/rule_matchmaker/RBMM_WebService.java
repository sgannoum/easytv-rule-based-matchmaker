package rule_matchmaker;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.json.JSONException;

import com.google.gson.Gson;



@Path("/RBMM")
public class RBMM_WebService
{
	
    //http://localhost:8080/EasyTV_RBMM_Restful_WS/RBMM/match
    @GET
    @Path("/rules")
    public Response getEasyTVRules() throws IOException, JSONException
    {
    	char[] content = new char[1024 * 3];
		File file = new File(this.getClass().getResource("rules.txt").getFile());
		FileReader reader = new FileReader(file);

		reader.read(content);
		reader.close();
				
		GenericEntity<String> entity = new GenericEntity<String>(new String(content)) {};

        return Response.status(200).entity(entity).build();
    }
	
    //http://localhost:8080/EasyTV_RBMM_Restful_WS/RBMM/match
    @GET
    @Path("/match")
    public Response getRunEasyTVRules() throws IOException, JSONException
    {
        return Response.status(200).build();
    }
	
    //http://localhost:8080/EasyTV_RBMM_Restful_WS/RBMM/match
    @POST
    @Path("/match")
    @Consumes("application/json")
    public Response postRunEasyTVRules(Object tmpInput) throws IOException, JSONException
    {
    	RuleReasoner ruleReasoner = new RuleReasoner();
    	Gson gson = new Gson();
    	String json = gson.toJson(tmpInput);
    	System.out.println(json);
        return Response.status(200).entity(ruleReasoner.infer(json).toString()).build();
    }
}
