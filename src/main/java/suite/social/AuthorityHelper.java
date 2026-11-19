/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package suite.social;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
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
    
    private Map<IRI, Long> authority_ontologies;
    
    private long total_links = 0;
    
    private AuthorityHelper() {
        
        ontologies = new HashMap<>();
        
        authority_ontologies = new HashMap<>();
    }
    
    public void addOntologies(IRI iri_ontology){
        Long intValue = Long.valueOf(0);
        authority_ontologies.put(iri_ontology, intValue);
        
    }
    
    public void print_library_content(){
        
        for(var ao : authority_ontologies.entrySet()){
            
            System.out.println(ao.getKey().toString() + " with " + ao.getValue());
            
        }
        
    }
    
    public void print_ontologies(){
        for(var o : ontologies.entrySet()){
            System.out.println(o.getKey() + " - " + o.getValue().toString());
        }
    }
    
    public Map<String, Long> getOntologiesList(){
        return ontologies;
    }
    
    public void tallyMatches(IRI entity_iri){
        
        String curie = "";
        
        if( entity_iri.toString().lastIndexOf("#")>0  ){
            
            String substring = entity_iri.toString().substring(entity_iri.toString().lastIndexOf("#") +1 );
            
            String subSequence = "";
            
            if(substring.contains("_")){
                subSequence = (String) substring.subSequence(0, substring.lastIndexOf("_"));
            }
            else{
                subSequence = substring;
            }
            
            curie = subSequence;
        }
        
        if( entity_iri.toString().lastIndexOf("/")>0 && entity_iri.toString().lastIndexOf("/") > entity_iri.toString().lastIndexOf("#") ){
            
            String substring = entity_iri.toString().substring(entity_iri.toString().lastIndexOf("/") +1);
            String subSequence = "";
            
            if(substring.contains("_")){
                subSequence = (String)substring.subSequence(0, substring.lastIndexOf("_"));
            }
            else{
                subSequence = substring;
            }
            
            curie = subSequence;
        }
        
        System.out.println("\t\t" + curie);
        
        for (var o : authority_ontologies.entrySet()) {

            if (Pattern.compile(Pattern.quote(curie), Pattern.CASE_INSENSITIVE).matcher(o.getKey().toString()).find()) {
                System.out.println("\t\t\tCURIE: " + curie + " matches IRI: " + o.getKey().toString());
                Long value = o.getValue();
                value = Long.sum(value.longValue(), 1);

                o.setValue(value);
            }

        }


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
        
        String curie = this.deriveCURIE(iri_string).toUpperCase();
        
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
        
        String curie = deriveCURIE(ontology_iri.toString()).toUpperCase();
        
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
