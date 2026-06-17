/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioportal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author mac
 */
public class NCBOConfig {
    
    //add ignore list
    //  "https://data.bioontology.org/ontologies/PDUMDV" (issue with pagnation of instances)
    
    //add 
    
    private static NCBOConfig INSTANCE;
    
    final String prop_name = "apis.properties";
    
    private Properties api_property = new Properties();
    
    private NCBOConfig(){
        
        Path api_file = Paths.get(prop_name);
        if(Files.notExists(api_file)){
            this.createDefaultPropertiesFile();
        }
        
        try {
            api_property.load(new FileInputStream(prop_name));
        } catch (FileNotFoundException ex) {
            System.getLogger(NCBOConfig.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NCBOConfig.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public synchronized static NCBOConfig getInstance() {
        
        if(INSTANCE == null) {
            INSTANCE = new NCBOConfig();
        }
        
        return INSTANCE;
        
    }
    
    public String getNCBOAPIKey(){
        String response = (String) api_property.get("ncbo");
        
        return response;
         
    }
    
    private void createDefaultPropertiesFile(){
        try {
            
            Path newFilePath = Paths.get(prop_name);
            Files.createFile(newFilePath);
        } catch (IOException ex) {
            System.getLogger(NCBOConfig.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    public static void main(String[] args) {
        NCBOConfig n = new NCBOConfig();
        n.createDefaultPropertiesFile();
    }
}
