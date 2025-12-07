/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import no.hasmac.jsonld.JsonLd;
import no.hasmac.jsonld.JsonLdError;
import no.hasmac.jsonld.document.Document;
import no.hasmac.jsonld.document.JsonDocument;
import ui.MessageDialog;
import ui.OntokeeperUI;

/**
 *
 * @author mac
 */
public class DownloadOBOLibraryTask extends Thread{
    
    final private String obo_json = "https://obofoundry.org/registry/ontologies.jsonld";
    
    MessageDialog md;
    
    private ArrayList<String> exclusion = new ArrayList<>();
    
    public static final String SP_GENERAL_ENTITY_SIZE_LIMIT = "jdk.xml.maxGeneralEntitySizeLimit";
    public static final String SP_MAX_ELEMENT_DEPTH_LIMIT = "jdk.xml.maxElementDepth";
    public static final String SP_PROPERTY_ENTITY_LIMIT = "jaxp.properties";
    public static final String SP_TOTAL_ENTITY_SIZE_LIMIT = "jdk.xml.totalEntitySizeLimit";
    
    private OntokeeperUI o = null;
    
    public DownloadOBOLibraryTask(OntokeeperUI parent){
        o = parent;
    }
    
    @Override
    public void run() {
        
        md = new MessageDialog(null, false);
        
        md.setMessage("Retrieving data from OBO Foundry. This may take awhile. Please wait.");
        
        md.setLocationRelativeTo(null);
        
        try{
            md.setVisible(true);
            fetch();

        }
        finally{
            md.dispose();
        }
        

    }
    
    public void fetch(){
        
        System.setProperty(SP_GENERAL_ENTITY_SIZE_LIMIT, "0"); //set no limit
        System.setProperty(SP_MAX_ELEMENT_DEPTH_LIMIT, "0");
        System.setProperty(SP_PROPERTY_ENTITY_LIMIT, "0");
        System.setProperty(SP_TOTAL_ENTITY_SIZE_LIMIT, "0");
        
        InputStream inputstream = null;
        
        try {
            inputstream = URI.create(obo_json).toURL().openStream();
            Document document = JsonDocument.of(inputstream);
            
            JsonArray json_array = JsonLd.expand(obo_json).ordered().get();
            
            for (JsonValue ja : json_array) {
                
                String status = ja.asJsonObject().get("http://obofoundry.github.io/vocabulary/activity_status").asJsonArray().get(0)
                        .asJsonObject().getString("@value");
                
                String id = ja.asJsonObject().get("http://obofoundry.github.io/vocabulary/activity_status").asJsonArray().get(0)
                        .asJsonObject().getString("@value");
                
            }
            
        } catch (MalformedURLException ex) {
            System.getLogger(DownloadOBOLibraryTask.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(DownloadOBOLibraryTask.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (JsonLdError ex) {
            System.getLogger(DownloadOBOLibraryTask.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
        
        
        System.clearProperty(SP_GENERAL_ENTITY_SIZE_LIMIT);
        System.clearProperty(SP_MAX_ELEMENT_DEPTH_LIMIT);
        System.clearProperty(SP_PROPERTY_ENTITY_LIMIT);
        System.clearProperty(SP_TOTAL_ENTITY_SIZE_LIMIT);
        
    }
    
}
