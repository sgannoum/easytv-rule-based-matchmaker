package entities;

import java.io.IOException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import rule_matchmaker.entities.User;

public class UserTest {
	
	public static final JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"  \"visual\": {\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/visual/visualAcuity\": \"20/400\",\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/visual/contrastSensitivity\": \"21:1\",\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/visual/colorBlindness\": \"normal\"\r\n" + 
			"  },\r\n" + 
			"  \"auditory\": {\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/auditory/hearingThresholdAt250Hz\": 0,\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/auditory/hearingThresholdAt500Hz\": 0,\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/auditory/hearingThresholdAt1000Hz\": 0,\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/auditory/hearingThresholdAt2000Hz\": 0,\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/auditory/hearingThresholdAt4000Hz\": 0,\r\n" + 
			"    \"https://easytvproject.eu/registry/functionalLimitations/auditory/hearingThresholdAt8000Hz\": 0\r\n" + 
			"  },\r\n" + 
			"  \"user_preferences\": {\r\n" + 
			"    \"default\": {\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"https://easytvproject.eu/registry/common/fontSize\": 9,\r\n" + 
			"        \"https://easytvproject.eu/registry/common/font\": \"sans-serif\",\r\n" + 
			"        \"https://easytvproject.eu/registry/common/fontColor\": \"#000000\",\r\n" + 
			"        \"https://easytvproject.eu/registry/common/fontBackgroundColor\": \"#ffffff\",\r\n" + 
			"        \"https://easytvproject.eu/registry/common/audioVolume\": 50,\r\n" + 
			"        \"https://easytvproject.eu/registry/common/audiolanguage\": \"en\",\r\n" + 
			"        \"https://easytvproject.eu/registry/common/contrast\": 100,\r\n" + 
			"        \"https://easytvproject.eu/registry/app/cs/textDetection\": false,\r\n" + 
			"        \"https://easytvproject.eu/registry/app/cs/audioVolume\": 50,\r\n" + 
			"        \"https://easytvproject.eu/registry/app/cs/audioLanguage\": \"en\",\r\n" + 
			"        \"https://easytvproject.eu/registry/app/cs/audioDescription\": false\r\n" + 
			"      }\r\n" + 
			"    },\r\n" + 
			"    \"conditional\": [\r\n" + 
			"      {\r\n" + 
			"        \"name\": null,\r\n" + 
			"        \"preferences\": {\r\n" + 
			"          \"https://easytvproject.eu/registry/common/audioVolume\": 10\r\n" + 
			"        },\r\n" + 
			"        \"conditions\": [\r\n" + 
			"          {\r\n" + 
			"            \"type\": \"le\",\r\n" + 
			"            \"operands\": [\r\n" + 
			"              \"https://easytvproject.eu/registry/context/time\",\r\n" + 
			"              0\r\n" + 
			"            ]\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      }\r\n" + 
			"    ]\r\n" + 
			"  }\r\n" + 
			"}");
	
	@BeforeTest
	public void beforeTests() {
	    Assert.assertNotNull(jsonProfile1);
	}
	
	@Test
	public void TestUserMapper() 
	  throws JsonParseException, IOException {
	 
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		User user = mapper.readValue(jsonProfile1.toString(), User.class);
	 
		System.out.println(user.toString());
	    Assert.assertNotNull(user);
	}

}
