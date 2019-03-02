package rule_matchmaker.entities;

public class Visual {
	
	private Integer visual_acuity;
	private Integer contrast_sensitivity;
    private String color_blindness;
    
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "VisualAbility";
	public static final String CONTRAST_SENSIVITY_PROP = NAMESPACE + "hasContrastSensitivity";
	public static final String VISUAL_ACUITY_PROP = NAMESPACE + "hasVisualAcuity";
	public static final String COLOR_BLINDNESS_PROP = NAMESPACE + "hasColorBllindness";

    
	public Integer getVisual_acuity() {
		return visual_acuity;
	}
	public void setVisual_acuity(Integer visual_acuity) {
		this.visual_acuity = visual_acuity;
	}
	public Integer getContrast_sensitivity() {
		return contrast_sensitivity;
	}
	public void setContrast_sensitivity(Integer contrast_sensitivity) {
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
}
