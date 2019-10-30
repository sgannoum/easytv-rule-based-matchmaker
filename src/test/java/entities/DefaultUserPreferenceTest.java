package entities;

import java.io.IOException;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.user.OntUserPreferences;
import com.certh.iti.easytv.rbmm.user.OntUserProfile;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.fasterxml.jackson.core.JsonParseException;
import junit.framework.Assert;

public class DefaultUserPreferenceTest {
	
	JSONObject jsonProfile1 = new JSONObject("{" + 
			"      \"preferences\": {" + 
			"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\"," + 
			"        \"http://registry.easytv.eu/common/volume\": 77," + 
			"        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"" + 
			"      }" + 
			"}");
	
	@BeforeTest
	public void beforeTests() {
	    Assert.assertNotNull(jsonProfile1);
	}
	
	@Test
	public void TestVisualMapper() 
	  throws JsonParseException, IOException, UserProfileParsingException {

		OntPreference userPreference = new OntPreference(new com.certh.iti.easytv.user.preference.Preference("default", jsonProfile1));
	 
		System.out.println(userPreference.toString());
	    Assert.assertNotNull(userPreference);
	}

}
