package models;

public interface Metric  {
	
	
	//public double calculate();
	public double calculate(double weight_value);
	public void setWeight(double weight_value);
	//public double getWeight();
	public double getScore();
	public double getUnWeightedScore();

}
