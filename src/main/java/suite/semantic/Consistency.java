package suite.semantic;

import models.AbstractMetric;
import ontology.OntologyExtractor;

public class Consistency extends AbstractMetric{

	private static Consistency INSTANCE = null;

        private double duplicate_term_totals;
        private double total_terms;
        
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Consistency() {
            OntologyExtractor oe = OntologyExtractor.getInstance();
            duplicate_term_totals = oe.getDuplicateTermTotal();
            total_terms = oe.getTotalTerms();
	}

	public static Consistency getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Consistency();
		}

		return INSTANCE;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		
		//OntologyExtractor oe = OntologyExtractor.getInstance();
		
                score = 1 - (duplicate_term_totals / total_terms);
                
		//score = 1-(double)oe.getDuplicateTermTotal()/(double)oe.getTotalTerms();
		
		return score;
	}

}
