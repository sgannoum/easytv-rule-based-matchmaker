package rule_matchmaker.entities;

public class Auditory {
	
	private Integer quarterK;
	private Integer halfK;
	private Integer oneK;
	private Integer twoK;
	private Integer fourK;
	private Integer eightK;
	
	private static final String NAMESPACE = "http://www.owl-ontologies.com/OntologyEasyTV.owl#";
	public static final String ONTOLOGY_CLASS_URI = NAMESPACE + "AuditoryAbility";
	public static final String ONEK_PROP = NAMESPACE + "hasOneK";
	public static final String TWOK_PROP = NAMESPACE + "hasTwoK";
	public static final String FOURK_PROP = NAMESPACE + "hasFourK";
	public static final String EIGHTK_PROP = NAMESPACE + "hasEightK";
	public static final String HALFK_PROP = NAMESPACE + "hasHalfK";
	public static final String QUARTERK_PROP = NAMESPACE + "hasQuarterQ";


	public Integer getQuarterK() {
		return quarterK;
	}

	public void setQuarterK(Integer quarterK) {
		this.quarterK = quarterK;
	}

	public Integer getHalfK() {
		return halfK;
	}
	
	public void setHalfK(Integer halfK) {
		this.halfK = halfK;
	}

	public Integer getOneK() {
		return oneK;
	}

	public void setOneK(Integer oneK) {
		this.oneK = oneK;
	}

	public Integer getTwoK() {
		return twoK;
	}

	public void setTwoK(Integer twoK) {
		this.twoK = twoK;
	}

	public Integer getFourK() {
		return fourK;
	}

	public void setFourK(Integer fourK) {
		this.fourK = fourK;
	}

	public Integer getEightK() {
		return eightK;
	}

	public void setEightK(Integer eightK) {
		this.eightK = eightK;
	}

	@Override
	public String toString() {
		return "Auditory [quarterK=" + quarterK + ", halfK=" + halfK
				+ ", oneK=" + oneK + ", twoK=" + twoK + ", fourK=" + fourK
				+ ", eightK=" + eightK + "]";
	}

}
