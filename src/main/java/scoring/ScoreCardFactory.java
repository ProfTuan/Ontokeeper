/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scoring;

import models.EqualMetricScoreCard;

/**
 *
 * @author mac
 */
public class ScoreCardFactory {
    
    
    public ScoreCardFactory(){
        
    }
    
    public EqualMetricScoreCard getEqualWeightedScoreCard(){
        
        return new EqualMetricScoreCard();
        
    }
    
}
