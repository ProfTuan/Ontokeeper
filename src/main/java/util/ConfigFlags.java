package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFlags {

	Properties properties = new Properties();

	private static ConfigFlags INSTANCE = null;
	
	private boolean camel_case;
	private boolean determiters;
	private boolean brackets;
	private boolean underscore;
	private boolean dashes;
	private long element_number;
        
        private String location_ontology_file = "";
	
	private String wn_dict = "";

	private ConfigFlags() {

		//String pathConfig = getClass().getResource("ok.properties").getPath().toString();
		try {
			properties.load(getClass().getResourceAsStream("/ok.properties"));
			//properties.load(new FileInputStream(pathConfig));
                        
                        camel_case = true;
                        determiters = true;
                        brackets = true;
                        underscore = true;
                        dashes = true;
                        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ConfigFlags getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ConfigFlags();
		}

		return INSTANCE;

	}
        
        public void setSafeInternalConfigs(){
            
        }
	
	public void setPropertyFile(String pathFile) {
		
		properties = new Properties();
		
		try {
			properties.load(new FileInputStream(pathFile));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean isCamelCase() {
		String value = (String) properties.get("camelcases");

		return Boolean.parseBoolean(value);
	}

	public boolean isDetermiters() {
		String value = (String) properties.get("determiters");

		return Boolean.parseBoolean(value);
	}

	public boolean isBrackets() {
		String value = (String) properties.get("brackets");

		return Boolean.parseBoolean(value);
	}
	
	public boolean isUnderscore() {
		String value = (String) properties.get("underscores");

		return Boolean.parseBoolean(value);
	}
	
	public boolean isDashes() {
		String value = (String) properties.get("dashes");

		return Boolean.parseBoolean(value);
	}
	
	public long getAverageElements() {
		
            if(element_number == 0){
                String value = (String)properties.getProperty("average_elements");
		return Long.parseLong(value);
            }
            else{
                return element_number;
            }
            
                
                
                
	}
        
        
	
	public String getWordNetDict() {
		if(this.wn_dict.isBlank()) {
			this.wn_dict = (String)properties.getProperty("jwi_dict");
		}
		
		return this.wn_dict;
	}
	
	public void setWordNetDict(String value) {
		this.wn_dict = value;
	}
	
	public void setAverageElements(long value) {
		this.element_number = value;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(ConfigFlags.getInstance().isBrackets()) {
			System.out.println("brackets is true");
		}
		
		System.out.println(ConfigFlags.getInstance().getWordNetDict());

	}

}
