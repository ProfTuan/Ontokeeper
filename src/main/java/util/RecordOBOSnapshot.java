/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import suite.GenerateLibraryElementsData;

/**
 *
 * @author mac
 */
public class RecordOBOSnapshot {
    
    private GenerateLibraryElementsData gled;
    
    private String folderPathOBO = "/Users/mac/Desktop/obo_fall";
    
    private long total_elements = 0;
    
    public RecordOBOSnapshot(){
            
         gled = new GenerateLibraryElementsData();
        
    }
    
    public void recordSnapshot(){
        
        if(folderPathOBO.isEmpty()){
            System.out.println("No folder specified. Have the OBO ontologies saved into a folder.");
            return;
        }
        
        gled.run(folderPathOBO);
        
        total_elements = gled.getFinal_Totatl();
        
        System.out.println(total_elements);
        
    }
    
    public void dumpSaveSnapshot(){
        
        
        
    }
    
    public static void main(String[] args) {
        
        RecordOBOSnapshot ro = new RecordOBOSnapshot();
        
        ro.recordSnapshot();
        
    }
    
}
