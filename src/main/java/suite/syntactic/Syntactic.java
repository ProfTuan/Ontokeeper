package suite.syntactic;

import java.util.List;

import models.AbstractMetric;
import models.Metric;

public class Syntactic extends AbstractMetric {
	
	private static Syntactic INSTANCE = null;
	
        private Richness richness;
        private Lawfulness lawfulness;
	
	public Syntactic() {
	
            richness = new Richness ();
            lawfulness = new Lawfulness ();
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
        
        
        public void setEqualWeightedScoring(){
            
            richness.setWeight(0.50);
            lawfulness.setWeight(0.50);
            
        }
        
	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		//Richness richness = Richness.getInstance();
		//Lawfulness lawfulness = Lawfulness.getInstance();
                richness.calculate();
                
                lawfulness.calculate();
		
                
                
		score = (richness.getScore() * richness.getWeight()) + (lawfulness.getScore() * lawfulness.getWeight()) ;
		
		return score;
	}
	
	

}
