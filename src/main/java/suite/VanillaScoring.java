package suite;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Stream;
import models.EqualMetricScoreCard;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import ontology.OntologyExtractor;
import scoring.EqualWeightedComputation;
import scoring.ScoreCardFactory;
import suite.pragmatic.Pragmatic;
import suite.semantic.Clarity;
import suite.semantic.Consistency;
import suite.semantic.Interpretability;
import suite.semantic.Semantic;
import suite.syntactic.Lawfulness;
import suite.syntactic.Richness;
import suite.syntactic.Syntactic;
import util.ConfigFlags;

public class VanillaScoring {

    static DecimalFormat df = new DecimalFormat("#.###");
    private ConfigFlags cf = null;
    private String filePath = "";
    private String fileOutput = "";
    long elements = -1;
    private boolean json = false;
    private boolean batch = false;
    
    

    public VanillaScoring() {
        cf = ConfigFlags.getInstance();
    }

    public void configure() {

        this.filePath = "/Users/mac/Downloads/bsso/addicto.owl";
        this.fileOutput = "/Users/mac/Downloads/bsso/addicto.txt";
        this.json = false;
        //cf.setWordNetDict("/Users/mac/Desktop/ok scoring/wn/dict");
        //cf.setPropertyFile("/Users/mac/Desktop/ok scoring/ok.properties");
        //cf.setAverageElements(21820); //nco
        
        //cf.setAverageElements(23379); //khmo (mergeds)
        cf.setAverageElements(23317); //nco -merged

    }
    
    public void configure(long average_elements){
        cf.setAverageElements(average_elements);
    }

    public void setOntologyFilePath(String path) {
        filePath = path;
    }

    public void setOutputFile(String output_path) {
        fileOutput = output_path;
    }

    public void setOutputAsJsont(boolean value) {
        json = value;
    }

    public void setAverageLibraryElements(long value) {
        cf.setAverageElements(value);
    }

    public void configure(String[] args) {
        Options options = new Options();

        Option file = new Option("i", "file", true, "input ontology file (owl or rdf)");
        file.setRequired(true);
        options.addOption(file);

        Option output = new Option("o", "output", true, "output file for results");
        output.setRequired(false);
        options.addOption(output);

        Option wn = new Option("wn", "wordnet", true, "input the location of the WordNet dictionary database folder (i.e., visit https://wordnet.princeton.edu)");
        wn.setRequired(false);
        options.addOption(wn);

        Option prop = new Option("p", "properties", true, "input properties files (i.e. ok.properties)");
        prop.setRequired(false);
        options.addOption(prop);

        Option ele = new Option("e", "elements", true, "Number of elements of a comparable library of ontologies");
        ele.setRequired(false);
        options.addOption(ele);

        Option format = new Option("json", "json", true, "format output file for json. Set to 'true' to activate.");
        format.setRequired(false);
        options.addOption(format);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("help", options);
            System.exit(1);
        }

        if (cmd.getOptionValue("properties") != null
                || (cmd.getOptionValue("wn") != null || cmd.getOptionValue("elements") != null)) {

            this.filePath = cmd.getOptionValue("file");
            this.fileOutput = cmd.getOptionValue("output");
            this.json = (cmd.getOptionValue("json").equalsIgnoreCase("true")) ? true : false;

            if (cmd.getOptionValue("properties") != null) {
                cf.setPropertyFile(cmd.getOptionValue("properties"));
            } else if (cmd.getOptionValue("wn") != null) {
                cf.setWordNetDict(cmd.getOptionValue("wn"));
            } else if (cmd.getOptionValue("elements") != null) {
                this.elements = Long.parseLong(cmd.getOptionValue("elements"));
                cf.setAverageElements(elements);
            }

        } else {
            formatter.printHelp("help", options);
            System.exit(1);
        }
    }
    
    public void batchRun(String [] args){
        Options options = new Options();
        
        Option dir = new Option("d", "directory", true, "input directory of where the the ontology files resides");
        dir.setRequired(true);
        options.addOption(dir);
        
        Option js = new Option("js", "json", true, "indicate 'true' or 'false' if you want a json export.");
        js.setRequired(false);
        options.addOption(js);
        
        
        Option avg_elements = new Option("avg", "average", true, "provide the average number of elements from the library you are providing. You must use the GenerateLibraryElementsData first to get this value");
        avg_elements.setRequired(true);
        options.addOption(avg_elements);
        
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
        
        if(cmd.getOptionValue("directory") != null || cmd.getOptionValue("average") !=null){
            
           String folderPath = cmd.getOptionValue("directory"); 
//           boolean json =  (cmd.getOptionValue("json").equalsIgnoreCase("true")) ? true : false;
           
           long average_elements = Long.parseLong(cmd.getOptionValue("average"));
           
           batchRun(folderPath, json, average_elements);
           
        }
        else {
            formatter.printHelp("help", options);
            System.exit(1);
        }
    }
    
    
    public void batchRun(String folderPath, boolean json, long average_elements) 
    {
        batch = true;
        cf.setAverageElements(average_elements);
        this.json = json;

        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {

            paths.forEach(p -> {

                if (!Files.isDirectory(p)) {
                    
                    String fileName = p.getFileName().toString();
                    String folder = (folderPath.endsWith("/")) ? folderPath : folderPath + "/";
                    
                    
                    if (fileName.contains(".owl") || fileName.contains(".rdf")) {

                        this.filePath = p.toFile().getAbsolutePath();
                        if(!json)
                        {
                           this.fileOutput = folder + p.getFileName() + ".txt"; 
                        }
                        else{
                           this.fileOutput = folder + p.getFileName(); 
                        }
                        run();

                        
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    public EqualMetricScoreCard scoreOntology(String file_path, long average_elements){
        
        cf.setAverageElements(average_elements);
        
        EqualMetricScoreCard score_card=null;
        
        if (file_path.contains(".owl") || file_path.contains(".rdf") || file_path.contains(".ttl")) {
        
            OntologyExtractor oe = OntologyExtractor.getInstance();
            //oe.reset();
            oe.loadOntologyFile(file_path);
            
            
            score_card = new ScoreCardFactory().getEqualWeightedScoreCard();
            score_card.setOntologyName(file_path);
        }
        
        System.out.println("Scoring...." + score_card.getOverallScore());
        
        return score_card;
    }
    
    public ArrayList<EqualMetricScoreCard> batchRunProcessing(String folderPath, long average_elements){
        
        ArrayList<EqualMetricScoreCard> score_cards = new ArrayList<EqualMetricScoreCard>();
        ScoreCardFactory score_factory = new ScoreCardFactory();
        batch = true;
        cf.setAverageElements(average_elements);
        
        
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {

            paths.forEach(p -> {

                if (!Files.isDirectory(p)) {
                    
                    String fileName = p.getFileName().toString();
                    String folder = (folderPath.endsWith("/")) ? folderPath : folderPath + "/";
                    
                    
                    if (fileName.contains(".owl") || fileName.contains(".rdf") || fileName.contains(".ttl")) {

                        this.filePath = p.toFile().getAbsolutePath();
                        System.out.println("Calculating..." + filePath.toString());
                        //if(!json)
                        //{
                        //   this.fileOutput = folder + p.getFileName() + ".txt"; 
                        //}
                        //else{
                        //   this.fileOutput = folder + p.getFileName(); 
                        //}
                        //run();
                        OntologyExtractor oe = OntologyExtractor.getInstance();
                        //oe.refresh();
                        oe.loadOntologyFile(this.filePath);
                        
                        EqualMetricScoreCard score_card = score_factory.getEqualWeightedScoreCard();
                        score_card.setOntologyName(this.filePath);
                        score_cards.add(score_card);
                        
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        System.out.println("Count of score cords: " + score_cards.size());
        
        return score_cards;
    }

    public void run() {
        OntologyExtractor oe = OntologyExtractor.getInstance();
        EqualWeightedComputation e = EqualWeightedComputation.getInstance();

        //import ontology
        if(batch) oe.refresh();
        
        oe.loadOntologyFile(filePath);

        double final_score = e.computeEqualScoring();
        double Pragmatic_score = Pragmatic.getInstance().getUnWeightedScore();

        double Semantic_score = Semantic.getInstance().getUnWeightedScore();
        double clarity = Clarity.getInstance().getUnWeightedScore();
        double consistency = Consistency.getInstance().getUnWeightedScore();
        double interpretability = Interpretability.getInstance().getUnWeightedScore();

        double Syntactic_score = Syntactic.getInstance().getUnWeightedScore();
        double richness = Richness.getInstance().getUnWeightedScore();
        double lawfulness = Lawfulness.getInstance().getUnWeightedScore();

        StringBuilder content = new StringBuilder();
        content.append("--------------------\n");
        content.append("File: " + filePath + "\n");
        content.append("--------------------\n");
        content.append("Syntactic: " + df.format(Syntactic_score) + "\n");
        content.append("** Richness: " + df.format(richness) + "\n");
        content.append("** Lawfulness: " + df.format(lawfulness) + "\n\n");
        content.append("Semantic: " + df.format(Semantic_score) + "\n");
        content.append("** Clarity: " + df.format(clarity) + "\n");
        content.append("** Consistency: " + df.format(consistency) + "\n");
        content.append("** Interpretability: " + df.format(interpretability) + "\n\n");
        content.append("Pragmatic: " + df.format(Pragmatic_score) + "\n");
        content.append("** Comprehensiveness: " + df.format(Pragmatic_score) + "\n\n");
        content.append("**************************************\n");
        content.append("Overall Score: " + df.format(final_score) + "\n");
        content.append("**************************************\n");
        
        System.out.println("File OUtput: " + fileOutput);

        if (fileOutput == null) {
            System.out.println(content.toString());
        } else {
            try {
                if (json) {
                    JSONObject jsonfile = new JSONObject();
                    jsonfile.put("file", filePath);
                    jsonfile.put("syntactic", df.format(Syntactic_score));
                    jsonfile.put("richness", df.format(richness));
                    jsonfile.put("lawfulness", df.format(lawfulness));
                    jsonfile.put("semantic", df.format(Semantic_score));
                    jsonfile.put("clarity", df.format(clarity));
                    jsonfile.put("consistency", df.format(consistency));
                    jsonfile.put("interpretability", df.format(interpretability));
                    jsonfile.put("pragmatic", df.format(Pragmatic_score));
                    jsonfile.put("comprehensiveness", df.format(Pragmatic_score));
                    jsonfile.put("overall score", df.format(final_score));

                    FileUtils.writeStringToFile(new File(fileOutput.concat(".json")), jsonfile.toString(), Charset.forName("UTF-8"));

                } else {
                    FileUtils.writeStringToFile(new File("output-thing.txt"), content.toString(), Charset.forName("UTF-8"));
                }

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //flags: [ontology file path] [output file for results] [average number of elements] [configuration file] [wn dictionary]
        //VanillaScoring vs = new VanillaScoring();

        //vs.configure(args);
        //vs.configure();
        //vs.run();
        

        //test this
        //vs.batchRun("/Users/mac/Downloads/bsso", false, 23317);
        
        //LIVE VERSION
        VanillaScoring vs = new VanillaScoring();
        vs.batchRun(args);

    }

}
