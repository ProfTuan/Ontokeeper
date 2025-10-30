package suite.semantic;

import models.AbstractMetric;

public class Semantic extends AbstractMetric{
    
        private Clarity clarity = null;
        private Consistency consistency = null;
        private Interpretability interpertability = null;
	
	private static Semantic INSTANCE = null;
	
	public Semantic() {
	
            clarity = new Clarity();
            consistency = new Consistency();
            interpertability = new Interpretability();
	}
	
	public static Semantic getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Semantic();
		}
		
		return INSTANCE;
	}
        
        public double getClarity(){
            return clarity.getScore();
        }
        
        public double getConsistency(){
            return consistency.getScore();
        }
        
        public double getInterpertability(){
            
            return interpertability.getScore();
        }
        
        public void setEqualWeightedScoring(){
            
            clarity.setWeight(0.334);
            consistency.setWeight(0.334);
            interpertability.setWeight(0.334);
        }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		score = 0.0;
		//clarity = Clarity.getInstance();
		//consistency = Consistency.getInstance();
		//intepertability = Interpretability.getInstance();
		
                clarity.calculate();
                interpertability.calculate();
                consistency.calculate();
                
		score = (clarity.getScore() * clarity.getWeight()) + (interpertability.getScore() * interpertability.getWeight()) + (consistency.getScore() * consistency.getWeight());
		
		return score;
	}

}
