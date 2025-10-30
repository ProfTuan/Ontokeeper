/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scoring.process;

import javax.swing.JOptionPane;
import suite.GenerateLibraryElementsData;
import ui.MessageDialog;

/**
 *
 * @author mac
 */
public class LibraryValueGeneration extends Thread{
    
    private String folderPath = "";
    
    private long total_library_elements = 0;
    
    private MessageDialog md = null;
    
    public LibraryValueGeneration(String folderPath){
        
        this.folderPath = folderPath;
        
    }
    
    @Override
    public void run() {

        md = new MessageDialog(null, false);

        md.setMessage("Processing library element count. Please wait....");
        md.setLocationRelativeTo(null);

        try {
            md.setVisible(true);
            
            GenerateLibraryElementsData gled = new GenerateLibraryElementsData();

            gled.run(this.folderPath);

            total_library_elements = gled.getFinal_Totatl();

        } finally {

            md.dispose();

        }

    }


    
    
    
    public long getTotal_library_elements() {
        return total_library_elements;
    }
    
    
    
    public static void main(String[] args) {
        
    }
    
}
