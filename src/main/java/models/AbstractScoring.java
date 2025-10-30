/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.File;
import java.text.DecimalFormat;
import ontology.OntologyExtractor;
import org.json.JSONObject;
import scoring.EqualWeightedComputation;
import suite.pragmatic.Pragmatic;
import suite.semantic.Clarity;
import suite.semantic.Consistency;
import suite.semantic.Interpretability;
import suite.semantic.Semantic;
import suite.syntactic.Lawfulness;
import suite.syntactic.Richness;
import suite.syntactic.Syntactic;
import util.ConfigFlags;

/**
 *
 * @author mac
 */
public abstract class AbstractScoring {
    
    protected DecimalFormat df = new DecimalFormat("#.###");
    
    protected ConfigFlags cf = null;
    
    protected OntologyExtractor ontology_extractor = null;
    
    protected EqualWeightedComputation equal_weighted_computation = null;
    
    protected long average_total_elements = 0;
    
    protected File target_ontology_file = null;
    
    protected File target_folder = null;
    
    public AbstractScoring(){
        cf = ConfigFlags.getInstance();
    }
    
    public void computeScoring(File ontology_file){
        
        target_ontology_file = ontology_file;
        
        ontology_extractor = OntologyExtractor.getInstance();
        equal_weighted_computation = EqualWeightedComputation.getInstance();
        
        ontology_extractor.refresh();
        ontology_extractor.loadOntologyFile(target_ontology_file);
        
        
        
    }
    
    protected void getStringRenderingScore(){
        
        double final_score = equal_weighted_computation.computeEqualScoring();
        double Pragmatic_score = Pragmatic.getInstance().getUnWeightedScore();

        double Semantic_score = Semantic.getInstance().getUnWeightedScore();
        double clarity = Clarity.getInstance().getUnWeightedScore();
        double consistency = Consistency.getInstance().getUnWeightedScore();
        double interpretability = Interpretability.getInstance().getUnWeightedScore();

        double Syntactic_score = Syntactic.getInstance().getUnWeightedScore();
        double richness = Richness.getInstance().getUnWeightedScore();
        double lawfulness = Lawfulness.getInstance().getUnWeightedScore();

        StringBuilder content = new StringBuilder();
        content.append("--------------------\n");
        content.append("File: " + target_ontology_file.getAbsolutePath() + "\n");
        content.append("--------------------\n");
        content.append("Syntactic: " + df.format(Syntactic_score) + "\n");
        content.append("** Richness: " + df.format(richness) + "\n");
        content.append("** Lawfulness: " + df.format(lawfulness) + "\n\n");
        content.append("Semantic: " + df.format(Semantic_score) + "\n");
        content.append("** Clarity: " + df.format(clarity) + "\n");
        content.append("** Consistency: " + df.format(consistency) + "\n");
        content.append("** Interpretability: " + df.format(interpretability) + "\n\n");
        content.append("Pragmatic: " + df.format(Pragmatic_score) + "\n");
        content.append("** Comprehensiveness: " + df.format(Pragmatic_score) + "\n\n");
        content.append("**************************************\n");
        content.append("Overall Score: " + df.format(final_score) + "\n");
        content.append("**************************************\n");
        
        
    }
}
