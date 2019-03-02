package rule_matchmaker.entities;

public class User {
	
	private Integer age;
	private String gender;
	private Visual visual;
	private Auditory auditory;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "User";
	public static final String AGE_PROP = NAMESPACE + "hasAge";
	public static final String GENDER_PROP = NAMESPACE + "hasGender";
	public static final String VISUAL_PROP = NAMESPACE + "hasVisualAbility";
	public static final String AUDITORY_PROP = NAMESPACE + "hasAuditoryAbility";
	
	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Visual getVisual() {
		return visual;
	}


	public void setVisual(Visual visual) {
		this.visual = visual;
	}


	public Auditory getAuditory() {
		return auditory;
	}


	public void setAuditory(Auditory auditory) {
		this.auditory = auditory;
	}


	@Override
	public String toString() {
		return "User [age=" + age + ", gender=" + gender + ", visual=" + visual
				+ ", auditory=" + auditory + "]";
	}
	
	


}
