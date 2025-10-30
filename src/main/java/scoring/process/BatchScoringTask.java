/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scoring.process;

import java.util.ArrayList;
import javax.swing.JFrame;
import models.EqualMetricScoreCard;
import suite.GenerateLibraryElementsData;
import suite.VanillaScoring;
import ui.MessageDialog;
import ui.OntokeeperUI;

/**
 *
 * @author mac
 */
public class BatchScoringTask extends Thread{
    
    private OntokeeperUI parent = null;
    
    private MessageDialog md = null;
    
    private String folderPath = null;
    
    private long element_count_avg = 0;
    
    private boolean json = false;
    
    private ArrayList<EqualMetricScoreCard> score_cards;
    
    public BatchScoringTask(String folderPath, long element_count){
        this.folderPath = folderPath;
        
        this.element_count_avg = element_count;
    }
    
    public BatchScoringTask(String folderPath, OntokeeperUI parent){
        
        this.folderPath = folderPath;
        
        this.parent = parent;
        
    }
    
    public void setAvgElements(long elements_total){
        this.element_count_avg = elements_total;
    }
    
    public void setToJsonExpoert(boolean value){
        json = value;
    }

    @Override
    public void run() {
        
        score_cards = new ArrayList<EqualMetricScoreCard>();
        
        md = new MessageDialog(null, false);

        md.setMessage("Calculating scores. Please wait....");
        md.setLocationRelativeTo(null);
        
        try{
            
            md.setVisible(true);
            
            GenerateLibraryElementsData gled = new GenerateLibraryElementsData();

            gled.run(this.folderPath);

            this.element_count_avg = gled.getFinal_Totatl();
            
            
            VanillaScoring vs = new VanillaScoring();
            
            score_cards.addAll( vs.batchRunProcessing(folderPath,element_count_avg) );
            
            parent.setScoreCards(score_cards);
            
            parent.populateScoreTable();
            
            
        }
        finally{
            md.dispose();
        }
        
        
    }

    public ArrayList<EqualMetricScoreCard> getScore_cards() {
        return score_cards;
    }
    
    
    
    
    public static void main(String[] args) {
        
    }
    
}
