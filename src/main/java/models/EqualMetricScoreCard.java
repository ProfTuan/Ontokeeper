/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import ontology.OntologyExtractor;

/**
 *
 * @author mac
 */
public class EqualMetricScoreCard extends MetricModel {
    
    private String id = "";
    private double overall_score = 0.000;
    
    public EqualMetricScoreCard(){
        
        initEqualWeightScoring();
        calculateSubScores();
        //id  = OntologyExtractor.getInstance().getOntologyName();
    }

    public double getStructureScore(){
        return syntactic.getStructure();
    }
    
    public double getRichnessScore(){
        
        
        return syntactic.getRichness();
    }
    
    public double getLawfulnessScore(){
        
        return syntactic.getLawfulness();
    }
    
    
    public double getClarityScore(){
        return semantic.getClarity();
    }
    
    public double getConsistencyScore(){
        return semantic.getConsistency();
    }
    
    public double getInterpertabilityScore(){
        return semantic.getInterpertability();
    }
    
    public double getAdaptabilityScore(){
        return pragmatic.getAdaptablity();
    }
    
    public double getEaseOfUseScore(){
        return pragmatic.getEaseOfUse();
    }
    
    public double getComprehensivenessCore(){
        return pragmatic.getComprehensiveness();
    }
    
    public double getOverallScore(){
        
        //calculateSubScores();
        
        overall_score = (pragmatic.getScore() + semantic.getScore() + syntactic.getScore()) / 3;
        
        
        return overall_score;
        //pragmatic.score + sematnic
    }
    
    public void initEqualWeightScoring(){
        
        pragmatic.setEqualWeightedScoring();
        
        semantic.setEqualWeightedScoring();
        
        syntactic.setEqualWeightedScoring();
        
        
    }
    
    
    public double getPragmaticScore(){
        double pragmatic_socre = pragmatic.getScore();
        
        return pragmatic_socre;
    }
    
    public double getSemanticScore(){
        
        double semantic_score = semantic.getScore();
        
        return semantic_score;
    }
    
    public double getSyntacticScore(){
        double syntactic_score = syntactic.getScore();
        
        return syntactic_score;
    }
    
    private void calculateSubScores(){
        
        pragmatic.calculate();
        semantic.calculate();
        syntactic.calculate();
    }
        
}
