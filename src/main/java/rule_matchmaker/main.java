package rule_matchmaker;

import java.io.IOException;
import org.json.JSONException;


public class main {

	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	private static final String INPUT_FILE = "input.txt";
	
	public static void main(String[] args) throws IOException, JSONException {
		
		RuleReasoner ruleReasoner = new RuleReasoner(ONTOLOGY_NAME, RULES_FILE);
		
		System.out.println(ruleReasoner.infer(INPUT_FILE).toString(4));
	}
}
