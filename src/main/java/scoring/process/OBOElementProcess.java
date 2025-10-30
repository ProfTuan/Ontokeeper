/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scoring.process;

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
import ontology.OntologyExtractor;
import org.semanticweb.owlapi.model.IRI;
import ui.MessageDialog;
import ui.OntokeeperUI;

/**
 *
 * @author mac
 */
public class OBOElementProcess extends Thread {
    
    final private String obo_json = "https://obofoundry.org/registry/ontologies.jsonld";

    
    public long num_elements = 0;
    
    MessageDialog md;
    
    static ArrayList<String> exclusion = new ArrayList<>();
    
    public static final String SP_GENERAL_ENTITY_SIZE_LIMIT = "jdk.xml.maxGeneralEntitySizeLimit";
    public static final String SP_MAX_ELEMENT_DEPTH_LIMIT = "jdk.xml.maxElementDepth";
    public static final String SP_PROPERTY_ENTITY_LIMIT = "jaxp.properties";
    public static final String SP_TOTAL_ENTITY_SIZE_LIMIT = "jdk.xml.totalEntitySizeLimit";
    
    private OntokeeperUI o = null;
    
    public OBOElementProcess(OntokeeperUI parent){
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
        
        o.setElementValue(num_elements);
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
                
                
                if (status.equalsIgnoreCase("active") ) {
                    
                    String title = ja.asJsonObject().get("http://purl.org/dc/elements/1.1/title").asJsonArray().get(0).asJsonObject().getString("@value");
                    
                    String iri_string = ja.asJsonObject().get("http://www.w3.org/ns/dcat#accessURL").asJsonArray().get(0).asJsonObject().getString("@value");
                    
                    System.out.println(iri_string);
                    
                    IRI iri = IRI.create(iri_string);
                    
                    long ontologyTotalElements = OntologyExtractor.getInstance().getOntologyTotalElements(iri);
                    
                    System.out.println("\t" +title +" "+ontologyTotalElements);
                    
                    num_elements += ontologyTotalElements;
                    
                }
                
            }
            
        } catch (MalformedURLException ex) {
            System.getLogger(OBOElementProcess.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(OBOElementProcess.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (JsonLdError ex) {
            System.getLogger(OBOElementProcess.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } finally {
            try {
                inputstream.close();
            } catch (IOException ex) {
                System.getLogger(OBOElementProcess.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        
        System.clearProperty(SP_GENERAL_ENTITY_SIZE_LIMIT);
        System.clearProperty(SP_MAX_ELEMENT_DEPTH_LIMIT);
        System.clearProperty(SP_PROPERTY_ENTITY_LIMIT);
        System.clearProperty(SP_TOTAL_ENTITY_SIZE_LIMIT);
        
    }
    
    public static void main(String[] args) {
        
        OBOElementProcess o = new OBOElementProcess(null);
        
        System.out.println("Fetchin...");
        
        o.fetch();
        
        System.out.println("Total: \t" + o.num_elements);
        
        System.out.println("Done");
        
    }
    
}
