package entities;

import org.testng.annotations.Test;

import com.certh.iti.easytv.rbmm.user.Ontological;
import com.certh.iti.easytv.rbmm.user.preference.OntPreference;

import junit.framework.Assert;

public class OntPreferenceTest {
	
	@Test
	public void test_getURI_1() {
		String dataProperty = Ontological.NAMESPACE + "has_common_volume";
		String excpected = "http://registry.easytv.eu/common/volume";
		String actual = OntPreference.getURI(dataProperty);
	
		Assert.assertEquals(excpected, actual);
	}
	
	@Test
	public void test_getURI_2() {
		String dataProperty = Ontological.NAMESPACE + "has_application_cs_audio_eq_low_shelf_frequency";
		String excpected = "http://registry.easytv.eu/application/cs/audio/eq/low/shelf/frequency";
		String actual = OntPreference.getURI(dataProperty);
	
		Assert.assertEquals(excpected, actual);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_getURI_3() {
		String dataProperty = Ontological.NAMESPACE + "has_nothing";
		String excpected = "http://registry.easytv.eu/common/volume";
		String actual = OntPreference.getURI(dataProperty);
	
		Assert.assertEquals(excpected, actual);
	}
	
	@Test
	public void test_getDataProperty_1() {
		
		String prefernceURI = "http://registry.easytv.eu/common/volume";
		String excpected = Ontological.NAMESPACE + "has_common_volume";
		String actual = OntPreference.getPredicate(prefernceURI);
	
		Assert.assertEquals(excpected, actual);
	}

	@Test
	public void test_getDataProperty_2() {
		
		String prefernceURI = "http://registry.easytv.eu/application/cs/audio/eq/low/shelf/frequency";
		String excpected = Ontological.NAMESPACE + "has_application_cs_audio_eq_low_shelf_frequency";
		String actual = OntPreference.getPredicate(prefernceURI);
	
		Assert.assertEquals(excpected, actual);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_getDataProperty_3() {
		
		String prefernceURI = "http://registry.easytv.eu/nothing";
		String excpected = Ontological.NAMESPACE + "has_application_cs_audio_eq_low_shelf_frequency";
		String actual = OntPreference.getPredicate(prefernceURI);
	
		Assert.assertEquals(excpected, actual);
	}

}
