package rule_matchmaker.entities;

import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Visual {
	
    @JsonProperty("http://registry.easytv.eu/functionalLimitations/visual/visualAcuity")
	private String visual_acuity;
    @JsonProperty("http://registry.easytv.eu/functionalLimitations/visual/contrastSensitivity")
	private String contrast_sensitivity;
    @JsonProperty("http://registry.easytv.eu/functionalLimitations/visual/colorBlindness")
    private String color_blindness;
    
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "VisualAbility";
	public static final String CONTRAST_SENSIVITY_PROP = NAMESPACE + "hasContrastSensitivity";
	public static final String VISUAL_ACUITY_PROP = NAMESPACE + "hasVisualAcuity";
	public static final String COLOR_BLINDNESS_PROP = NAMESPACE + "hasColorBllindness";

    
	public String getVisual_acuity() {
		return visual_acuity;
	}
	public void setVisual_acuity(String visual_acuity) {
		this.visual_acuity = visual_acuity;
	}
	public String getContrast_sensitivity() {
		return contrast_sensitivity;
	}
	public void setContrast_sensitivity(String contrast_sensitivity) {
		this.contrast_sensitivity = contrast_sensitivity;
	}
	public String getColor_blindness() {
		return color_blindness;
	}
	public void setColor_blindness(String color_blindness) {
		this.color_blindness = color_blindness;
	}
	@Override
	public String toString() {
		return "Visual [visual_acuity=" + visual_acuity
				+ ", contrast_sensitivity=" + contrast_sensitivity
				+ ", color_blindness=" + color_blindness + "]";
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public Individual createOntologyInstance(final OntModel model){
		
		OntClass visualClass = model.getOntClass(ONTOLOGY_CLASS_URI);
		Individual visualInstance = visualClass.createIndividual();
		
		if (color_blindness != null) {
			Property p = model.getProperty(COLOR_BLINDNESS_PROP);
			visualInstance.addProperty(p, model.createTypedLiteral(color_blindness));
		}
		
		if (contrast_sensitivity != null){
			Property p = model.getProperty(CONTRAST_SENSIVITY_PROP);
			visualInstance.addProperty(p, model.createTypedLiteral(contrast_sensitivity));
		}
		
		if (visual_acuity != null){
			Property p = model.getProperty(VISUAL_ACUITY_PROP);
			visualInstance.addProperty(p, model.createTypedLiteral(visual_acuity));
		}	
		
		return visualInstance;
	}
	
}
