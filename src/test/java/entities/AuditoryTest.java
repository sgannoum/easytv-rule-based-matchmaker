package entities;

import java.io.IOException;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import rule_matchmaker.entities.Auditory;

public class AuditoryTest {
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"hearing\": 81\r\n" + 
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
		Auditory auditory = mapper.readValue(jsonProfile1.toString(), Auditory.class);
	 
		System.out.println(auditory.toString());
	    Assert.assertNotNull(auditory);
	}
}
