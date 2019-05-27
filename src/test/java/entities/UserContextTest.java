package entities;

import java.io.IOException;
import java.util.Date;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import rule_matchmaker.entities.UserContext;

public class UserContextTest {
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"http://registry.easytv.eu/context/time\": \"1558700176286\" ,\r\n" + 
			"    \"http://registry.easytv.eu/context/location\": \"fr\"\r\n" + 
			"}");
	
	@BeforeTest
	public void beforeTests() {
	    Assert.assertNotNull(jsonProfile1);
	}
	
	@Test
	public void TestAuditoyMapper() 
	  throws JsonParseException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		UserContext userContext = mapper.readValue(jsonProfile1.toString(), UserContext.class);
	 
		System.out.println(userContext.toString());
	    Assert.assertNotNull(userContext);
	}
}
