package scoring;

import java.text.DecimalFormat;

import ontology.OntologyExtractor;
import suite.pragmatic.Comprehensiveness;
import suite.pragmatic.Pragmatic;
import suite.semantic.Clarity;
import suite.semantic.Consistency;
import suite.semantic.Interpretability;
import suite.semantic.Semantic;
import suite.syntactic.Lawfulness;
import suite.syntactic.Richness;
import suite.syntactic.Syntactic;

public class EqualWeightedComputation {
    
	
	private static EqualWeightedComputation INSTANCE = null;
	
	
	private EqualWeightedComputation() {
		
		
	}
	
	public static EqualWeightedComputation getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EqualWeightedComputation();
		}
		
		return INSTANCE;
	}
        
        public void reset(){
            INSTANCE = new EqualWeightedComputation();
        }
	
	public double computeEqualScoring() {
		
		
		Lawfulness lawfulness = Lawfulness.getInstance();
		Richness richness = Richness.getInstance();
		
		lawfulness.calculate(0.50);
		richness.calculate(0.50);
		
		//syntactic.calculate(0.334);
		Comprehensiveness comp = Comprehensiveness.getInstance();
		comp.calculate(1.00);
		
		Clarity cl = Clarity.getInstance();
		Consistency co = Consistency.getInstance();
		Interpretability i = Interpretability.getInstance();
		
		cl.calculate(0.334);
		co.calculate(0.334);
		i.calculate(0.334);
		
		Syntactic syntactic = Syntactic.getInstance();
		Pragmatic pragmatic = Pragmatic.getInstance();
		Semantic semantic = Semantic.getInstance();
		
		 double final_score = semantic.calculate(0.334) + pragmatic.calculate(0.334) + syntactic.calculate(0.334);
		//System.out.println("Final score is: " + final_score);
		 return final_score;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
