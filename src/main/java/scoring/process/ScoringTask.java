/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scoring.process;

import models.EqualMetricScoreCard;
import suite.VanillaScoring;
import ui.MessageDialog;
import ui.OntokeeperUI;

/**
 *
 * @author mac
 */
public class ScoringTask extends Thread{
    
    private OntokeeperUI parent = null;
    
    private MessageDialog md = null;
    
    private boolean json = false;
    
    
    
    public ScoringTask(OntokeeperUI ui){
        
        parent = ui;
        
        //this.file_path = file_path;
        
    }

    @Override
    public void run() {
        md = new MessageDialog(null, false);

        md.setMessage("Calculating scores. Please wait....");
        md.setLocationRelativeTo(null);
        
        try{
            
            md.setVisible(true);
            
            
            VanillaScoring vs = new VanillaScoring();
            
            
            long element_value = parent.getElementValue();
            
            String file_path = parent.getTxtOntologyFile();
            
            EqualMetricScoreCard score_card = vs.scoreOntology(file_path, element_value);
            
            parent.setScoresForOntology(score_card);
            
        }
        
        finally{
            
            md.dispose();
            
        }
    }
    
          
    
    
}
