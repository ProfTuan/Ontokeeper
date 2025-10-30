package suite.semantic;

import models.AbstractMetric;
import ontology.OntologyExtractor;

public class Clarity extends AbstractMetric{

	private static Clarity INSTANCE = null;
        
        private double total_senses;
        private double total_terms;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Clarity() {
            OntologyExtractor oe = OntologyExtractor.getInstance();
            
            total_senses = oe.getTotalSenses();
            total_terms = oe.getTotalTerms();
            
	}

	public static Clarity getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Clarity();
		}

		return INSTANCE;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		
		
		
                score = total_senses / total_terms;
                score = 1 - ( score / total_terms);
                
		//score = (double)oe.getTotalSenses()/(double)oe.getTotalTerms();
		//score = 1.00 - (score/(double)oe.getTotalTerms());
		
		return score;
	}

}
