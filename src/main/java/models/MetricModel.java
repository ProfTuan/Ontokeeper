/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import org.json.JSONObject;
import suite.pragmatic.Pragmatic;
import suite.semantic.Semantic;
import suite.syntactic.Syntactic;

/**
 *
 * @author mac
 */
public abstract class MetricModel {
    
    private String id = "";
    
    protected Pragmatic pragmatic = null;
    protected Semantic semantic = null;
    protected Syntactic syntactic = null;
    
    public MetricModel(){
        pragmatic = new Pragmatic();
        semantic = new Semantic();
        syntactic = new Syntactic();
    }
    
    public void setOntologyName(String value){
        
        id = value;
        
    }
    
    public String getOntologyId(){
        return id;
    }
    
   public double getOverallScore(){
       
       double overall_score = 0.00;
       
       overall_score = pragmatic.calculate(0.334) + semantic.calculate(0.334) + syntactic.calculate(0.334);
       
       return overall_score;
       
   }

    @Override
    public String toString() {
        
       
        StringBuilder string_builder = new StringBuilder();
        
        string_builder.append("SYNTACTIC \t" + syntactic.getScore() + "\n");
        string_builder.append("\tRichness: \t" + syntactic.getRichness() + "\n");
        string_builder.append("\tLawfulness: \t " + syntactic.getLawfulness() + "\n");
        string_builder.append("SEMANTIC \t" + semantic.getScore() + "\n");
        string_builder.append("\tClarity: \t " + semantic.getClarity() + "\n");
        string_builder.append("\tConsistency: \t " + semantic.getConsistency() + "\n");
        string_builder.append("\tInterpertability: \t " + semantic.getInterpertability() + "\n");
        string_builder.append("Pragmatic: \t " + pragmatic.getScore() + "\n");
        string_builder.append("\tComprehensiveness : \t " + pragmatic.getScore() + "\n" );
        string_builder.append("*** Social is not supported (yet)\n");
        string_builder.append("\n----------------------------\n");
        string_builder.append("OVERALL SCORE: \t" + this.getOverallScore());
        string_builder.append("\n----------------------------\n");
        
        return string_builder.toString();
        
    }
    
    public JSONObject toJson(){
        JSONObject jsonfile = new JSONObject();
        jsonfile.put("file", this.id);
        jsonfile.put("syntactic", syntactic.getScore());
        jsonfile.put("richness", syntactic.getRichness());
        jsonfile.put("lawfulness", syntactic.getLawfulness());
        jsonfile.put("semantic", semantic.getScore());
        jsonfile.put("clarity", semantic.getClarity());
        jsonfile.put("consistency", semantic.getConsistency());
        jsonfile.put("interpretability", semantic.getInterpertability());
        jsonfile.put("pragmatic", pragmatic.getScore());
        jsonfile.put("comprehensiveness", pragmatic.getScore());
        jsonfile.put("overall score", this.getOverallScore());
        
        return jsonfile;
    }
   
   
    
}
