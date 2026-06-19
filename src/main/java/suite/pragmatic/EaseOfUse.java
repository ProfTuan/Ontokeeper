/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package suite.pragmatic;

import models.AbstractMetric;
import ontology.OntologyExtractor;

/**
 *
 * @author mac
 */
public class EaseOfUse extends AbstractMetric {
//proportion of annotations to axioms (any annotations)
// if the score is above 0.99, normalize to 1
    
    private double number_annotations;
    private double number_elements;
    
    private static EaseOfUse INSTANCE = null;
    
    public EaseOfUse(){
        
        OntologyExtractor oe = OntologyExtractor.getInstance();
        number_elements = oe.number_of_elements;
        number_annotations =oe.number_of_annotations;
    }
    
    public static EaseOfUse getInstance(){
        
        if(INSTANCE == null){
            INSTANCE = new EaseOfUse();
        }
        
        return INSTANCE;
        
    }
    
    @Override
    public double calculate() {
        
        double score = 0.000;
        
        OntologyExtractor oe = OntologyExtractor.getInstance();
        number_elements = oe.number_of_elements;
        number_annotations =oe.number_of_annotations;
        
        score = number_annotations / number_elements;
        
        if(score > 1) score = 1.000;
        
        return score;
    }
    
}
