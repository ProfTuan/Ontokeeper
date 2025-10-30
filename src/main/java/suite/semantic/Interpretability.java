package suite.semantic;

import models.AbstractMetric;
import ontology.OntologyExtractor;

public class Interpretability extends AbstractMetric{

	private static Interpretability INSTANCE = null;

        private double terms_with_senses;
        private double total_terms;
        
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Interpretability() {
            OntologyExtractor oe = OntologyExtractor.getInstance();
            terms_with_senses = oe.getTermsWithSenses();
            total_terms = oe.getTotalTerms();
	}

	public static Interpretability getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Interpretability();
		}

		return INSTANCE;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		
                score = terms_with_senses / total_terms;
                
		//OntologyExtractor oe = OntologyExtractor.getInstance();
		
		//score = (double)oe.getTermsWithSenses()/(double)oe.getTotalTerms();
		
		return score;
	}


}
