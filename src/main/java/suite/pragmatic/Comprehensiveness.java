package suite.pragmatic;

import models.AbstractMetric;
import ontology.OntologyExtractor;
import util.ConfigFlags;

public class Comprehensiveness extends AbstractMetric{

	private static Comprehensiveness INSTANCE = null;
	
        private double number_of_elements =0;
        private double average_elements =0;
        
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
        public Comprehensiveness(double num_elements, double avg_elements){
            
            this.number_of_elements = num_elements;
            this.average_elements = avg_elements;
        }
        
	public Comprehensiveness() {
		
	}
	
	public static Comprehensiveness getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Comprehensiveness();
		}
		
		return INSTANCE;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		
		OntologyExtractor oe = OntologyExtractor.getInstance();
                
                number_of_elements = (double)oe.number_of_elements;
                average_elements = (double)ConfigFlags.getInstance().getAverageElements();
                
                if(ConfigFlags.getInstance().isSingleScoreMode()){
                    score = number_of_elements/ (average_elements + number_of_elements);
                }
                else{
                    score = number_of_elements/ average_elements;
                }
                
                
		
		//score = (double)oe.number_of_elements/(double)ConfigFlags.getInstance().getAverageElements();
		
		return score;
	}

}
