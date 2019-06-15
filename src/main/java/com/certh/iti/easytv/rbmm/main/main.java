package com.certh.iti.easytv.rbmm.main;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import org.json.JSONException;

import com.certh.iti.easytv.rbmm.reasoner.RuleReasoner;


public class main {

	private static final String ONTOLOGY_NAME = "EasyTV.owl";
	private static final String RULES_FILE = "rules.txt";
	private static final String INPUT_FILE = "userProfile_4.txt";
	
	public static void main(String[] args) throws IOException, JSONException {
		
		RuleReasoner ruleReasoner = new RuleReasoner(ONTOLOGY_NAME, RULES_FILE);
	
		System.out.println(ruleReasoner.infer(INPUT_FILE).toString(4));
	}
}
