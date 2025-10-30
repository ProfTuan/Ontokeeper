package models;

public abstract class AbstractMetric implements Metric {
	protected double weight = 1.00;
	protected double score = 0.00;

	
	
	abstract public double calculate();

	@Override
	public double calculate(double weight_value) {
		// TODO Auto-generated method stub
		setWeight(weight_value);
		return score = (calculate() * weight_value);
	}

    public double getWeight() {
        return weight;
    }

	

	@Override
	public void setWeight(double weight_value) {
		// TODO Auto-generated method stub
		weight = weight_value;

	}

	@Override
	public double getScore() {
		// TODO Auto-generated method stub
		return score;
	}
	
	@Override
	public double getUnWeightedScore() {
		return (score * (1/weight));
	}


}
