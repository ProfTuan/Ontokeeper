package wordnet;

import java.io.Serializable;

public class Lexicon implements Serializable{

	private String term;
	private String processed_term;
	private int senses = 0;
	private int list_index;
	
	public int getList_index() {
		return list_index;
	}

	public void setList_index(int list_index) {
		this.list_index = list_index;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getProcessed_term() {
		return processed_term;
	}

	public void setProcessed_term(String processed_term) {
		this.processed_term = processed_term;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public int getSenses() {
		return senses;
	}

	public void setSenses(int senses) {
		this.senses = senses;
	}
	
	

}

