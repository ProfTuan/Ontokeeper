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
public class Adaptability extends AbstractMetric {
//average number of ancestor for each leaf (def: a class with no subclass) - average number of ancestors to deepest node
//leaf / #number of classest
    
    private double number_classes;
    private double number_leaves;
    
    private double average_ancestors;
    private double deepest_leaf;
    
    static private Adaptability INSTANCE = null;
    
    public Adaptability(){
        OntologyExtractor oe = OntologyExtractor.getInstance();
        
        number_classes =oe.getNumberOfClasses();
        number_leaves = oe.number_of_leaves;
        
        average_ancestors = oe.average_ancestor_for_leaves;
        deepest_leaf =oe.deepest_leaf;
    }
    
    static public Adaptability getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Adaptability();
        }
        
        return INSTANCE;
    }
    
    @Override
    public double calculate() {
        double score = 0.0000;
        
        OntologyExtractor oe = OntologyExtractor.getInstance();
        
        number_classes =oe.getNumberOfClasses();
        number_leaves = oe.number_of_leaves;
        
        average_ancestors = oe.average_ancestor_for_leaves;
        deepest_leaf =oe.deepest_leaf;
        
        score = ((number_leaves /  number_classes)*0.50) + ((average_ancestors/deepest_leaf)*0.50);
        
        return score;
    }
    
}
