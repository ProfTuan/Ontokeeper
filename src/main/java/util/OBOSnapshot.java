/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.Serializable;
import java.util.ArrayList;
import models.EqualMetricScoreCard;

/**
 *
 * @author mac
 */
public class OBOSnapshot implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long total_elements = 0;
    
    private ArrayList<EqualMetricScoreCard> score_cards;
    
    public OBOSnapshot(){
        score_cards = new ArrayList<EqualMetricScoreCard>();
    }
    
    public void saveCards(ArrayList<EqualMetricScoreCard> cards){
        //score_cards.removeAll(cards)
        
        score_cards.addAll(cards);
    }
    
    public void saveTotal(long total){
        total_elements = total;
    }
    
    public static void main(String[] args) {
        
    }
    
}
