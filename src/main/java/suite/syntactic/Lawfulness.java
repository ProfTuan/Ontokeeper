package suite.syntactic;

import models.AbstractMetric;
import models.Metric;
import ontology.OntologyExtractor;

public class Lawfulness extends AbstractMetric {

        private int broken_rules;
        private long total_axioms;
	
	private static Lawfulness INSTANCE = null;
	
	public Lawfulness() {
            OntologyExtractor oe = OntologyExtractor.getInstance();
            broken_rules = oe.profile_violations;
            total_axioms = oe.number_of_axioms;
            
           
	}
	
	public static Lawfulness getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Lawfulness();
		}
		
		return INSTANCE;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		

		OntologyExtractor oe = OntologyExtractor.getInstance();
		double broken_rules = oe.profile_violations;
		double total_axioms = oe.number_of_axioms;

                 System.out.println("\tBroken Rules: " + broken_rules);
            System.out.println("\tTotal Axioms: " + total_axioms);
                
		//score = 1-((double)broken_rules)/(double)total_axioms;

                score = 0.0;
                
                score = 1 - (broken_rules / total_axioms);
                
		return score;

	}
	
	
	

}
