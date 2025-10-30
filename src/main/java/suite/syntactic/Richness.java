package suite.syntactic;

import models.AbstractMetric;
import models.Metric;
import ontology.OntologyExtractor;

public class Richness extends AbstractMetric {
	
	private static Richness INSTANCE = null;
        
        private double num_syntatic_used;
        private double total_syntactics;
	
	public Richness() {
	
            OntologyExtractor oe = OntologyExtractor.getInstance();
            
            num_syntatic_used = oe.number_of_syntatic_used;
            total_syntactics = oe.total_syntatics;
            //score = (double)oe.number_of_syntatic_used/(double)oe.total_syntatics;
            
	}
	
	public static Richness getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Richness();
		}
		
		return INSTANCE;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		
		//OntologyExtractor oe = OntologyExtractor.getInstance();
		//score = (double)oe.number_of_syntatic_used/(double)oe.total_syntatics;
                
                OntologyExtractor oe = OntologyExtractor.getInstance();
            
            num_syntatic_used = oe.number_of_syntatic_used;
            total_syntactics = oe.total_syntatics;
		
                score = 0.000;
                
                System.out.println("\tNum Syntactic Used: " + num_syntatic_used);
                System.out.println("\tTotal Syntactics: " + total_syntactics);
                        
                score = num_syntatic_used / total_syntactics;        
                
                System.out.println("\tRichness: " + score);
                
		return score;
	}
	
	

	

}
