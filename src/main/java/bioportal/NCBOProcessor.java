/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioportal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mac
 * 
 * reuse of https://github.com/ncbo/ncbo_rest_sample_code/tree/master/java
 * 
 */

public class NCBOProcessor extends Thread {
    
    private Set<NCBOModel> ontologies;
    
    private final String REST_URL = "https://data.bioontology.org";
    private String API_KEY;
    private ObjectMapper mapper = new ObjectMapper();
    
    public NCBOProcessor(){
        API_KEY = NCBOConfig.getInstance().getNCBOAPIKey();
    }
     

    public void getAllOntologies(){
        
        ontologies = new HashSet<>();
        
        
        String resourceString = REST_URL + "/ontologies";
        String authorization = API_KEY;
        resourceString = resourceString +"?" +authorization;
        
        System.out.println(resourceString);
        
        
        try {
            URL url = new URI(resourceString).toURL();
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            
            
            conn.connect();

            
            JsonNode jn = mapper.readTree(url.openStream());
            
      
            for(JsonNode j : jn){
                
                NCBOModel nm = new NCBOModel();
                
                String id = j.get("@id").asText();
                String name = j.get("name").asText();
                String properties_link = j.get("links").get("properties").asText();
                String classes_link = j.get("links").get("classes").asText();
                String instances_link = j.get("links").get("instances").asText();
  
                nm.setId(id);
                nm.setName(name);
                nm.setProperty_link(properties_link);
                nm.setClass_link(classes_link);
                nm.setInstance_link(instances_link);
                
                ontologies.add(nm);
            }
    
        } catch (MalformedURLException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (URISyntaxException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        finally{
            
        }
        
        System.out.println("*** getting the metrics ***");
        
        for(NCBOModel nm : ontologies){
            nm.retrieveMetrics(authorization);
        }

        System.out.println("*** getting the entities ***");
        
        for(NCBOModel nm: ontologies){
            
            if(nm.getTotalNumberElements()<41){ // to minimize processing time
            
            System.out.println("\t" + "class info...");
            
            String class_link = nm.getClass_link();
            Map<String, String> class_labels = this.getLabels(class_link);
            nm.addClassLabels(class_labels);
            
            System.out.println("\t" + "property info...");
            
            String property_link = nm.getProperty_link();
            Map<String, String> prop_labels = this.getLabels(property_link);
            nm.addPropertyLabels(prop_labels);
            
            
            System.out.println("\t" + "instance info...");
            
            String instance_link = nm.getInstance_link();
            Map<String, String> instance_labels = this.getLabels(instance_link);
            nm.addInstanceLabels(instance_labels);
               
            }
            
            
        }
        
    }
    
    public void printLabelResults(){
        
        StringBuilder sb = new StringBuilder();
        
        ontologies.forEach(nb->{
            
            sb.append(nb.getId()).append("\n");
            
            sb.append("CLASSES").append("\n\n");
            for(var cl :nb.getClassLabels().entrySet()){
                sb.append(cl.getKey()).append("\t");
                sb.append(cl.getValue()).append("\n");
            }
            
            sb.append("PROPERTIES").append("\n\n");
            
            for(var pl : nb.getPropertyLabels().entrySet()){
                sb.append(pl.getKey()).append("\t");
                sb.append(pl.getValue()).append("\n");
            }
            
            sb.append("INSTANCES").append("\n\n");
            
            for(var il: nb.getInstanceLabels().entrySet()){
                sb.append(il.getKey()).append("\t");
                sb.append(il.getValue()).append("\n");
            }
            
        });
        
        try {
            FileUtils.writeStringToFile(new File("output.txt"), sb.toString(), Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    private Map <String,String> getLabels(String resourceString) {
        
        Map<String,String> entity_labels = new HashMap<String, String>();
        System.out.println("resourcestring: \t" + resourceString);
        try {
           
            String page_resource = resourceString + "?apikey=" + API_KEY;
            System.out.println(page_resource);
            String next_page = "";
            do {

                URL url = new URI(page_resource).toURL();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                conn.connect();

                JsonNode jn = mapper.readTree(url.openStream());
                
               //System.out.println(jn.toPrettyString());

                JsonNode jc = jn.get("collection"); //classes and instances have collections

                if (jc != null ) {
                    for (JsonNode j : jc) {
                        String asText = j.get("prefLabel").asText();
                        String id_text = j.get("@id").asText();
                        System.out.println("preflabel: \t" + asText);
                        
                        entity_labels.put(id_text, asText);
                    }

                    if (jn.get("links") != null && jn.get("links").get("nextPage") != null ) {
                        page_resource = jn.get("links").get("nextPage").asText();
                    }
           
                    else {
                        page_resource = "null";
                    }
                    if(jc.isEmpty()) System.out.println("yes it is empty");
                    
                    if(jc.isEmpty()) page_resource = "null";
                    
                    //System.out.println(jn.toPrettyString());
                    //if(jn.get("nextPage") ==null) page_resource = "null";
                    //System.out.println("going through jc \n");
                    //System.out.println(jn.get("nextPage").asText());
                }
                
                else{
                    jn.forEach(j->{
                        
                        System.out.println(j.get("label").asText() + "\t" + j.get("@id").asText() + "\n**************\n");
                        entity_labels.put(j.get("@id").asText(), j.get("label").asText());
                        //System.out.println( j.toPrettyString() + "\n**************\n");
                        
                    });
                    
                    page_resource = "null";
                }

               
                
                /*if(jn.get("nextPage").asText() == "null"){ //hack to fix the instance nextPage issue 
                    page_resource = jn.get("nextPage").asText();
                }*/
                
            } while (page_resource != "null");
            

        } catch (URISyntaxException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ProtocolException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NCBOProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return entity_labels;
    }
    
    public static void main(String[] args) {
        
        NCBOProcessor p = new NCBOProcessor();
        //p.getLabels("https://data.bioontology.org/ontologies/PDUMDV/instances");
        //p.getLabels("https://data.bioontology.org/ontologies/DRANPTO/classes");
        p.getAllOntologies();
        p.printLabelResults();
    }

  

    
    
}
