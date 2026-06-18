package ontology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wordnet.Lexicon;

public abstract class OntologyDataSource {
	
	public int profile_violations = 0;
	public int number_of_axioms = 0;
	public int number_of_syntatic_used=0;
	public int total_syntatics = 0;
	public long number_of_annotations = 0;
        public long number_of_subclasses =0;
        public long number_of_leaves = 0;
        public long average_ancestor_for_leaves = 0;
        
	public long number_of_elements = 0;
	
	Set<String>classList = new HashSet<String>();
	Set<String>dataPropertyList = new HashSet<String>();
	Set<String>objectPropertyList = new HashSet<String>();
	Set<String>instanceList = new HashSet<String>();
	
	Set<String> terms = new HashSet<String>();
	List<String> raw_terms = new ArrayList<String>(); 
	Set<String> processed_terms = new HashSet<String>();
	
	List<Lexicon> lexicon_terms = new ArrayList<Lexicon>();
	
	public int getTotalTerms() {
		return lexicon_terms.size();
	}
        
        public void emptyElementLists(){
            classList = new HashSet<String>();
            dataPropertyList = new HashSet<String>();
            objectPropertyList = new HashSet<String>();
            instanceList = new HashSet<String>();
            
            terms = new HashSet<String>();
            raw_terms = new ArrayList<String>();
            processed_terms = new HashSet<String>();
            lexicon_terms = new ArrayList<Lexicon>();
        }
	
	public int getDuplicateTermTotal() {
		int dupes = 0;
		
		HashSet<String>dupeList = new HashSet<String>();
		
		for(String elementTerm : raw_terms) {
			if(!dupeList.add(elementTerm)) {
				dupes++;
			}
		}
		
		return dupes;
	}
	
	public int getTotalSenses() {
		int senses_count = 0;
		
		if(lexicon_terms != null) {
			for(Lexicon lexicon_term: lexicon_terms) {
				senses_count += lexicon_term.getSenses();
			}
		}
		
		return senses_count;
	}
	
	public int getTermsWithSenses() {
		int terms_w_senses = 0;
		if(lexicon_terms != null) {
			for(Lexicon lexicon_term: lexicon_terms) {
				if(lexicon_term.getSenses()>0) terms_w_senses++;
			}
		}
		
		return terms_w_senses;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	

	
	

}
