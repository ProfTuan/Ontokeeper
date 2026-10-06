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
//The average of the ratio of leaf node depth to deepest leaf node depth and the ratio of leaf nodes to total nodes    
    private double number_classes;
    private double number_leaves;
    
    private double average_ancestors_for_leaves;
    private double deepest_leaf;
    
    static private Adaptability INSTANCE = null;
    
    public Adaptability(){
        OntologyExtractor oe = OntologyExtractor.getInstance();
        
        number_classes =oe.getNumberOfClasses();
        number_leaves = oe.number_of_leaves;
        
        average_ancestors_for_leaves = oe.average_ancestor_for_leaves;
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
        //double score = 0.0000;
        
        OntologyExtractor oe = OntologyExtractor.getInstance();
        
        number_classes =oe.getNumberOfClasses();
        number_leaves = oe.number_of_leaves;
        
        average_ancestors_for_leaves = oe.average_ancestor_for_leaves;
        deepest_leaf =oe.deepest_leaf;
        
        System.out.println("*********");
        System.out.println("\tleaves: " +number_leaves);
        System.out.println("\tavg ancestors for leaves: " + average_ancestors_for_leaves);
        System.out.println("\tnumber classes: " + number_classes);
        System.out.println("\tdeepest leaf: " + deepest_leaf);
        System.out.println("\tA :" + ((1- (1/(average_ancestors_for_leaves/deepest_leaf)) )*1));
        System.out.println("\tR :" + ((number_leaves /  number_classes)*1));
        System.out.println("*********");
        
        score = ( (1- (1/(average_ancestors_for_leaves/deepest_leaf)) ) *0.50) + ((number_leaves /  number_classes)*0.50) ;
        
        //if(score>1) score = 1;
        //System.out.println
        
        System.out.println("Adaptability score: " + score);
        
        return score;
    }
    
}
