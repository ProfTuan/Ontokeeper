/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package ui;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

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
    
    private Map<IRI, Long> ontologies;
    
    private long total_links = 0;
    
    private AuthorityHelper() {
        
        ontologies = new HashMap<>();
    }
    
    public Map<IRI, Long> getOntologiesList(){
        return ontologies;
    }
    
    public void tallyMatches(IRI iri){
        
        for(var o : ontologies.entrySet()){
            if(iri.toString().contains(o.getKey().toString())){
                Long value = o.getValue();
                value = Long.valueOf(value.longValue() + 1);
            }
        }

        /*
        for(var o : ontologies.entries()){
            if(iri.toString().contains(o.getKey().toString())){
                Long value = o.getValue();
                value = Long.valueOf(value.longValue()+1);
                
                o.setValue(value);
            }
        }
        */
        //long count = ontologies.keys().stream().filter(i->iri.toString().contains(i.toString())).count();
        
    }
    
    public static AuthorityHelper getInstance() {
        return AuthorityHelperHolder.INSTANCE;
    }

    public void recordIRI(IRI ontology_iri) {
        
        ontologies.put(ontology_iri, Long.valueOf(0));
        
    }

    public double generateAuthorityScore(IRI ontology_iri){
        
        //get the denominator
        if(total_links == 0) computeDenominator();
        
        long longValue = ontologies.get(ontology_iri).longValue();
        
        double result = (double)longValue/total_links;
        //Collection<Long> get = ontologies.get(ontology_iri);
        
        return result;
        
    }
    
    public void computeDenominator(){
        /*
        for(var o : ontologies.entries()){
            total_links+=o.getValue().longValue();
        }
        for(var o : ontologies.entrySet()){
            
        }
        */
        for(var v : ontologies.values()){
            total_links +=v.longValue();
        }
        
    }
    
    private static class AuthorityHelperHolder {

        private static final AuthorityHelper INSTANCE = new AuthorityHelper();
    }
}
