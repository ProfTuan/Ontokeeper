/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioportal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mac
 */
public class NCBOModel {
    
    private String id;
    private String name;
    private Map<String, String> instances;
    private Map<String, String> classes;
    private Map<String, String> properties;
    private NCBO_Metric metrics;
    
    private String instance_link;
    private String class_link;
    private String property_link;
    
    public static void main(String[] args) {
        
        NCBOModel nm = new NCBOModel();
        String extracted = nm.extractLabelFromId("http://purl.obolibrary.org/obo/pdumdv/granular_stage");
        
        System.out.println(extracted);
        
    }
    
    
    
    public void addPropertyLabels(Map<String,String>collection){
        properties.putAll(collection);
    }
    
    public Map<String, String> getPropertyLabels(){
        return properties;
    }
    
    public void addClassLabels(Map<String,String> collection){
        classes.putAll(collection);
    }
    
    public Map<String, String> getClassLabels(){
        return classes;
    }
    
    public void addInstanceLabels(Map<String,String> collection){
        instances.putAll(collection);
    }
    
    public Map<String, String> getInstanceLabels(){
        return instances;
    }
    
    public NCBOModel(){
        instances = new HashMap<>();
        classes = new HashMap<>();
        properties = new HashMap<>();
    }

    public void retrieveMetrics(String authorization){
        
        ObjectMapper mapper = new ObjectMapper();
        
        long properties_num =0;
        long individuals_num =0;
        long classes_num= 0;
        
        try {
            
            String rest_call = id + "/metrics?" + authorization;
            //System.out.println(rest_call);
            URL url = new URI(rest_call).toURL();
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");

            conn.connect();

            JsonNode jn = mapper.readTree(url.openStream());
            //System.out.println("********\n"+jn.toPrettyString());
            if(jn !=null){
                
                if (jn.get("properties") !=null) properties_num = jn.get("properties").asLong();
                if (jn.get("individuals") !=null) individuals_num = jn.get("individuals").asLong();
                if (jn.get("classes") !=null) classes_num = jn.get("classes").asLong();
            }

            this.metrics = new NCBO_Metric();
            
            metrics.classes = classes_num;
            metrics.individuals = individuals_num;
            metrics.properties = properties_num;
            
            metrics.total_elements = classes_num + individuals_num + properties_num;
            
        } catch (MalformedURLException ex) {
            System.getLogger(NCBOModel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (URISyntaxException ex) {
            System.getLogger(NCBOModel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NCBOModel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    class NCBO_Metric{
        
        public long properties;
        public long individuals;
        public long classes;
        public long total_elements;
        
        
    }
    
    //TODO: rectify accurate elements
    
    public void rectifyMissingInformation(){
        this.imputeLabelInformation();
        this.recountElementsFromCollection();
    }
    
    
    private void recountElementsFromCollection(){

        this.metrics.individuals = instances.size();
        this.metrics.properties = properties.size();
        this.metrics.classes = classes.size();
        
        metrics.total_elements = metrics.individuals + metrics.properties + metrics.classes;
    }
    
    //TODO: generate labels from URI
    
    private String extractLabelFromId(String uri){
        StringBuilder sb = new StringBuilder();
        int idx = uri.indexOf("#")+1;
        if(idx>0){
            sb.append(uri.substring(idx));
        }
        
        
        
        return sb.toString();
    }
    
    private void imputeLabelInformation(){
        
 
        deriveLabelFromURI(properties);
        
        deriveLabelFromURI(classes);
        
        deriveLabelFromURI(instances);
        
       
        
    }
    
    private void deriveLabelFromURI(Map<String,String> collection){
        Map<String, String> temp = new HashMap<>();
        
        collection.forEach((id, label)->{
            
            if(label == null || label.isEmpty() || label.equals("null")){
                if(id.contains("#")){
                    temp.put(id, extractLabelFromId(id));
                }
                
            }
            
        });
        
        for(var t: temp.entrySet()){
            collection.replace(t.getKey(), t.getValue());
        }
        
        
    }
    
    public long getTotalNumberElements(){
        if(this.metrics !=null){
            return metrics.total_elements;
        }
        else return 0;
    }
    
    public long getNumberIndividuals(){
        if(this.metrics !=null){
            return metrics.individuals;
        }
        else return 0;
    }
    
    public long getNumberProperties(){
        if(this.metrics !=null){
            return metrics.properties;
        }
        else return 0;
    }
    
    public long getNumberClasses(){
        if(this.metrics !=null){
            return metrics.classes;
        }
        else return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstance_link() {
        return instance_link;
    }

    public void setInstance_link(String instance_link) {
        this.instance_link = instance_link;
    }

    public String getClass_link() {
        return class_link;
    }

    public void setClass_link(String class_link) {
        this.class_link = class_link;
    }

    public String getProperty_link() {
        return property_link;
    }

    public void setProperty_link(String property_link) {
        this.property_link = property_link;
    }
    
    @Override
    public String toString(){
        
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.id);
        sb.append("\nNAME: ").append(this.name);
        sb.append("\nINSTANCE LINK: ").append(this.instance_link);
        sb.append("\nPROPERTY LINK: ").append(this.instance_link);
        sb.append("\nCLASS LINK: ").append(this.instance_link);
        sb.append("\nclasses: ").append(this.metrics.classes);
        sb.append("\nproperties: ").append(this.metrics.properties);
        sb.append("\ninstances: ").append(this.metrics.individuals);
        
        sb.append("\n****************\n");
        
        return sb.toString();
        
    }
    
    
}
