package entities;

import java.io.IOException;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import rule_matchmaker.entities.Visual;

public class VisualTest {
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"    \"visual_acuity\": 8,\r\n" + 
			"    \"contrast_sensitivity\": 24,\r\n" + 
			"    \"color_blindness\": \"normal\"\r\n" + 
			"  }");
	
	@BeforeTest
	public void beforeTests() {
	    Assert.assertNotNull(jsonProfile1);
	}
	
	@Test
	public void TestVisualMapper() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Visual visual = mapper.readValue(jsonProfile1.toString(), Visual.class);
	 
		System.out.println(visual.toString());
	    Assert.assertNotNull(visual);
	}

}
