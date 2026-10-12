/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package ui;

import java.util.HashMap;
import java.util.Map;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author mac
 */
public class AuthorityHelper {
    
    /*
    1- collect all of the IRI for each ontology
    2- iteratie through entire library of ontology and do a string match of the IRI (from 1) for each entity
    3- record each match 
    
    alternate: sparql querys
    */
    
    private Map<String, Long> ontologies;
    
    private long total_links = 0;
    
    private AuthorityHelper() {
        
        ontologies = new HashMap<>();
    }
    
    public Map<String, Long> getOntologiesList(){
        return ontologies;
    }
    
    public void tallyMatches(IRI iri){
        
        String iri_string = iri.toString();
        
        for(var o :ontologies.entrySet()){
            if(iri_string.contains(o.getKey().toString())){
                Long value = o.getValue();
                value = Long.valueOf(value.longValue() + 1);
                
                o.setValue(value);
                //total_links += value;
            }
        }
        
        /*
        for(var o : ontologies.entrySet()){
            //System.out.println("\ttally iri: " + iri.toString());
            //System.out.println("\tcompared to: " + o.getKey().toString());
            if(iri.toString().contains(o.getKey().toString())){
                Long value = o.getValue();
                value = Long.valueOf(value.longValue() + 1);
            }
        }
        */
        
    }
    
    public static AuthorityHelper getInstance() {
        return AuthorityHelperHolder.INSTANCE;
    }
    
    private String deriveCURIE(String iri_string){
        String curie = "";
        
        int b = iri_string.lastIndexOf("/");
        iri_string = iri_string.toString().substring(b + 1);
        
        if (iri_string.contains(".") == true) {
            iri_string = iri_string.substring(0, iri_string.lastIndexOf("."));
        }
        
        curie = iri_string;
        return curie;
    }

    public void recordIRI(String iri_string) {
        
        String curie = this.deriveCURIE(iri_string);
        
        System.out.println(curie.toString() + " has been added.");
        ontologies.put(curie, Long.valueOf(0));
        
    }

    public double generateAuthorityScore(IRI ontology_iri){
        long demo=0;
        //get the denominator
        //if(total_links == 0) computeDenominator();
        for(var d : ontologies.entrySet()){
            demo = demo + d.getValue();
        }
        
        System.out.println("Denominator: " +demo);
        
        System.out.println("generate authorith score...");
        System.out.println("\tSize of the storage: " + ontologies.size());
        System.out.println("\tNumber of links: " + total_links);
        
        String curie = deriveCURIE(ontology_iri.toString());
        
        long longValue = ontologies.get(curie).longValue();
        
        System.out.println("\tValue number for onto: " + longValue);
        
        double result = (double)longValue/total_links;
        //Collection<Long> get = ontologies.get(ontology_iri);
        
        return result;
        
    }
    
    public void computeDenominator(){
        total_links = 0;
        for(var v : ontologies.values()){
            total_links +=v.longValue();
        }
        
    }
    
    private static class AuthorityHelperHolder {

        private static final AuthorityHelper INSTANCE = new AuthorityHelper();
    }
}
