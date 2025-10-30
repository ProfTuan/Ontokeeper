package suite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ontology.OntologyExtractor;
import util.ConfigFlags;

public class GenerateLibraryElementsData {

    private long final_total = 0;
    private ConfigFlags cf = null;
    private String folderPath = "";

    public GenerateLibraryElementsData() {
        cf = ConfigFlags.getInstance();
    }

    public void setFolderPath(String value) {
        this.folderPath = value;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    
    public long getFinal_Totatl(){
        
        return final_total;
        
    }
    
    
    public void configure(String[] args) {
        Options options = new Options();

        Option folder = new Option("fd", "folder", true, "input file folder");
        folder.setRequired(true);
        options.addOption(folder);

        Option wn = new Option("wn", "wordnet", true, "input the location of the WordNet dictionary database folder (i.e., visit https://wordnet.princeton.edu)");
        wn.setRequired(true);
        options.addOption(wn);

        Option prop = new Option("p", "properties", true, "OPTIONAL: input properties files");
        prop.setRequired(false);
        options.addOption(prop);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            formatter.printHelp("help", options);
            System.exit(1);
        }

        this.folderPath = cmd.getOptionValue("folder");

        if (cmd.getOptionValue("properties") != null) {
            cf.setPropertyFile(cmd.getOptionValue("properties"));
        } else {
            cf.setWordNetDict(cmd.getOptionValue("wordnet"));
        }

    }

    public void run(String[] args) {

        Options options = new Options();

        Option folder = new Option("fd", "folder", true, "input file folder");
        folder.setRequired(true);
        options.addOption(folder);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            formatter.printHelp("help", options);
            System.exit(1);
        }

        this.folderPath = cmd.getOptionValue("folder");

        run();

    }
    
    public void run(String folderPath){
        
        this.folderPath = folderPath;
        
        run();
    }

    public void run() {
        
        final_total = 0;
        
        try (Stream<Path> paths = Files.walk(Paths.get(this.folderPath))) {

            paths.forEach(p -> {

                if (!Files.isDirectory(p)) {
                    String fileName = p.getFileName().toString();
                    if (fileName.contains(".owl") || fileName.contains(".rdf") || fileName.contains(".ttl")) {

                        OntologyExtractor oe = OntologyExtractor.getInstance();

                        final_total += oe.getOntologyTotalElements(p.toString(), true);

                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nLibrary element size is >> " + final_total);
        System.out.println("\n*** Use this number to update total elements to calculate the comphresnivenss sub-score ***\n");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // [file folder] 
        //String folderPath = "";
        //ConfigFlags cf = ConfigFlags.getInstance();
        GenerateLibraryElementsData gled = new GenerateLibraryElementsData();

        //gled.configure(args);
        /*
		if(args.length >= 1) {

			gled.configure(args);
			//folderPath = args[0];
			//cf.setPropertyFile(args[1]);
		}
		else if (args.length == 0){
			gled.setFolderPath("/Users/mac/Desktop/test");
		}*/
        //String 
        //gled.setFolderPath("/Users/mac/Downloads/bsso");
        //gled.run();
        //LIVE VERSION
        gled.run(args);

    }

}
