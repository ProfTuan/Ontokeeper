package suite.pragmatic;

import models.AbstractMetric;
import models.Metric;

public class Pragmatic extends AbstractMetric{

        private Comprehensiveness c = null;

	private static Pragmatic INSTANCE = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Pragmatic() {
            c = new Comprehensiveness();
	}

	public static Pragmatic getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Pragmatic();
		}

		return INSTANCE;
	}

        public void setEqualWeightedScoring(){
            c.calculate(1);
        }
        
        public double Comprehensiveness(){
            return c.getScore();
        }

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		score = 0.0;
                
                c.calculate();

		score = c.getScore();

		return score;
	}



}
