/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package suite.syntactic;

import models.AbstractMetric;
import ontology.OntologyExtractor;

/**
 *
 * @author mac
 */
public class Structure extends AbstractMetric {
// ratio of subclasses / total number of classes
    
    private double num_classes;
    private double num_subclasses;
    
    private static Structure INSTANCE = null;
    
    public Structure(){
        OntologyExtractor oe = OntologyExtractor.getInstance();
        
        num_subclasses =oe.number_of_subclasses;
        num_classes = oe.getNumberOfClasses();
    }
    
    public static Structure getInstance(){
        
        if(INSTANCE == null){
            INSTANCE = new Structure();
        }
        
        return INSTANCE;
    }
    
    @Override
    public double calculate() {
        
        OntologyExtractor oe = OntologyExtractor.getInstance();
        
        num_subclasses =oe.number_of_subclasses;
        num_classes = oe.getNumberOfClasses();
        
        score = 0.000;
        
        
        score = num_subclasses / num_classes;
        
        return score;
        
    }
    
}
