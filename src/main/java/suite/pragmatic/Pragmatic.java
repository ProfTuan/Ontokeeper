package suite.pragmatic;

import models.AbstractMetric;
import models.Metric;

public class Pragmatic extends AbstractMetric{

        private Comprehensiveness c = null;
        private Adaptability a = null;
        private EaseOfUse e = null;
	private static Pragmatic INSTANCE = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Pragmatic() {
            c = new Comprehensiveness();
            a = new Adaptability();
            e = new EaseOfUse();
	}

	public static Pragmatic getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Pragmatic();
		}

		return INSTANCE;
	}

        public void setEqualWeightedScoring(){
            //c.calculate(1);
            
            c.setWeight(0.333);
            a.setWeight(0.333);
            e.setWeight(0.333);
        }
        
        public double getComprehensiveness(){
            return c.getScore();
        }
        
        public double getAdaptablity(){
            return a.getScore();
        }
        
        public double getEaseOfUse(){
            return e.getScore();
        }

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		score = 0.0;
                
                c.calculate();
                a.calculate();
                e.calculate();

		//score = c.getScore();

                score = (c.getScore() * c.getWeight()) + (a.getScore() * a.getWeight()) + (e.getScore() * e.getWeight());
                
		return score;
	}



}
