/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author tuan
 */
public final class ScoreDial {
    
    private static ScoreDial INSTANCE = null;
    
    
    final private int start_value = -120;
    final private int ending_value = -300;
    
    final private int initial_value = 0;
    final private int final_value = 1;
    
    final private double increments = 0.1;
    
    private ScoreDial(){
        
    }
    
    public static ScoreDial getInstance(){
        
        if(INSTANCE == null){
            INSTANCE = new ScoreDial();
        }
        
        
        return INSTANCE;
    }
    
    public ChartPanel generateScoreDial( double default_value, int height, int width ){
        
        DefaultValueDataset dataset = new DefaultValueDataset(default_value);
        
        DialPlot dial = new DialPlot(dataset);
        dial.setDialFrame(new StandardDialFrame());
        dial.addLayer(new DialValueIndicator(0));
        dial.addLayer(new DialPointer.Pointer());
        
        StandardDialScale scale = new StandardDialScale(initial_value, final_value, -120, -300, increments, 9);
        
        
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.20);
        dial.addScale(0, scale);
        
        ChartPanel chartPanel = new ChartPanel(new JFreeChart(dial));
        
        Dimension d = chartPanel.getPreferredSize();
        //d.height = 178;
        d.height = height;
        //d.width = 178;
        d.width = width;
        
        chartPanel.setPreferredSize(d);
        
        return chartPanel;
    }
    
}
