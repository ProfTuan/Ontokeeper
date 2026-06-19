package suite.syntactic;

import java.util.List;

import models.AbstractMetric;
import models.Metric;

public class Syntactic extends AbstractMetric {
	
	private static Syntactic INSTANCE = null;
	
        private Richness richness;
        private Lawfulness lawfulness;
        private Structure structure;
	
	public Syntactic() {
	
            richness = new Richness ();
            lawfulness = new Lawfulness ();
            structure = new Structure();
	}
	
	public static Syntactic getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Syntactic();
		}
		
		return INSTANCE;
	}

        public double getRichness(){
            
            return richness.getScore();
        }
        
        public double getLawfulness(){
            return lawfulness.getScore();
        }
        
        public double getStructure(){
            return structure.getScore();
        }
        
        public void setEqualWeightedScoring(){
            
            richness.setWeight(0.333);
            lawfulness.setWeight(0.333);
            structure.setWeight(0.333);
        }
        
	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		//Richness richness = Richness.getInstance();
		//Lawfulness lawfulness = Lawfulness.getInstance();
                richness.calculate();
                
                lawfulness.calculate();
		
                structure.calculate();
                
		score = (richness.getScore() * richness.getWeight()) + (lawfulness.getScore() * lawfulness.getWeight() + (structure.getScore() * structure.getWeight())  ) ;
		
		return score;
	}
	
	

}
