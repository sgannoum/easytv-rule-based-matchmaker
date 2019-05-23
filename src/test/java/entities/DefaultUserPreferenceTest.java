package entities;

import java.io.IOException;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import rule_matchmaker.entities.UserPreference;
import rule_matchmaker.entities.DefaultUserPreferences;

public class DefaultUserPreferenceTest {
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"https://easytvproject.eu/registry/application/cs/audio/audioDescription\": true,\r\n" + 
			"        \"https://easytvproject.eu/registry/application/cs/audio/track\": \"es\",\r\n" + 
			"        \"https://easytvproject.eu/registry/common/audioVolume\": 77,\r\n" + 
			"        \"https://easytvproject.eu/registry/application/cs/cc/subtitles/fontColor\": \"#ffffff\",\r\n" + 
			"        \"https://easytvproject.eu/registry/application/cs/cc/subtitles/fontSize\": 17,\r\n" + 
			"        \"https://easytvproject.eu/registry/application/cs/cc/subtitles/language\": \"en\",\r\n" + 
			"        \"https://easytvproject.eu/registry/application/cs/audio/volume\": 30\r\n" + 
			"      }" + 
			"}");
	
	@BeforeTest
	public void beforeTests() {
	    Assert.assertNotNull(jsonProfile1);
	}
	
	@Test
	public void TestVisualMapper() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		DefaultUserPreferences userPreference = mapper.readValue(jsonProfile1.toString(), DefaultUserPreferences.class);
	 
		System.out.println(userPreference.toString());
	    Assert.assertNotNull(userPreference);
	}

}
