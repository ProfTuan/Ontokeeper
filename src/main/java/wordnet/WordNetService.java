package wordnet;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

public class WordNetService {

    //private IDictionary dict = null;
    private Dictionary dictionary = null;

    private static WordNetService INSTANCE;

    private WordNetService() {

        try {
            //this.init();
            dictionary = Dictionary.getDefaultResourceInstance();
        } catch (JWNLException ex) {
            Logger.getLogger(WordNetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO Auto-generated catch block

    }

    public static WordNetService getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new WordNetService();
        }

        return INSTANCE;

    }

    public int calculateWordSense(String word, POS c) {
        int sense_count = 0;

        try {
            IndexWord indexWord = dictionary.getIndexWord(c, word);

            if (indexWord != null) {
                sense_count = sense_count + indexWord.getSenses().size();
            }

        } catch (JWNLException ex) {
            Logger.getLogger(WordNetService.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
		IIndexWord idxWord = dict.getIndexWord(word,c);
		if(idxWord != null) {
			sense_count = sense_count + idxWord.getWordIDs().size();
		}
         */
        return sense_count;
    }

    public void init() throws MalformedURLException {

        /*
		URL url=null;
		try {


			
			url = new URL("file", null, ConfigFlags.getInstance().getWordNetDict());
			dict = new Dictionary(url); 
			dict.open();
			if(dict.isOpen()){
				//System.out.println("WordNet library is active");
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         */
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WordNetService instance = WordNetService.getInstance();
        
        int calculateWordSense = instance.calculateWordSense("entity", POS.NOUN);
        
        System.out.println(calculateWordSense);

    }

}
