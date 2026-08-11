/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package suite;

import models.EqualMetricScoreCard;
import org.json.JSONObject;

/**
 *
 * @author mac
 */
public class GitHubScore {
    
    private long total_elements = 7521788;
    
    private String filePath = "";
    
    private EqualMetricScoreCard score_card;
    
    public GitHubScore(String _filePath){
        
        filePath = _filePath;
    }
    
    public void runScoring(){
        
        VanillaScoring vs = new VanillaScoring();
        
         score_card = vs.scoreOntology(filePath, total_elements);
    }
    
    public JSONObject exportJSONResult(){
        
        System.out.println(score_card.toString());
        
        return score_card.toJson();
    }
    
    public static void main(String[] args) {
        
        if(args.length<0){
            System.out.println("Specificy the file path");
        }
        
        GitHubScore gs = new GitHubScore("/Users/mac/Documents/GitHub/NCF/nco.owl");
        gs.runScoring();
        gs.exportJSONResult();
    }
    
}
