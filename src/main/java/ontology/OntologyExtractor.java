package ontology;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.GCICount;
import org.semanticweb.owlapi.metrics.HiddenGCICount;
import org.semanticweb.owlapi.metrics.ReferencedClassCount;
import org.semanticweb.owlapi.metrics.ReferencedDataPropertyCount;
import org.semanticweb.owlapi.metrics.ReferencedIndividualCount;
import org.semanticweb.owlapi.metrics.ReferencedObjectPropertyCount;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.search.EntitySearcher;

import net.sf.extjwnl.data.POS;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import util.ConfigFlags;
import wordnet.Lexicon;
import wordnet.WordNetService;

public class OntologyExtractor extends OntologyDataSource {

	private static OntologyExtractor INSTANCE;

	private OWLOntologyManager owl_manager;
	private OWLOntology ontology;
	private OWLDataFactory df;

	private WordNetService wordNet = null;
        
        private String file_path;

	private OntologyExtractor() {
		owl_manager = OWLManager.createOWLOntologyManager();
		df = owl_manager.getOWLDataFactory();

		wordNet = WordNetService.getInstance();
	}
	
        

	public static OntologyExtractor getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new OntologyExtractor();
		}

		return INSTANCE; 

	}
        
        /*
        public void reset(){
            INSTANCE = new OntologyExtractor();
        }
        */
        
        
        public void refresh(){
            INSTANCE = new OntologyExtractor();
            //owl_manager= OWLManager.createConcurrentOWLOntologyManager();
            //df = owl_manager.getOWLDataFactory();
        }
        
        
        public long getOntologyTotalElements(String filepath, boolean refresh){
            
            if(refresh){
                refresh();
            }
            
            return getOntologyTotalElements(filepath);
            
        }
        
        public long getOntologyTotalElements(IRI iri){
            
            refresh();
            
            long value = 0;
            
            try {
                
                ontology  = owl_manager.loadOntology(iri);
                
                long num_classes = ontology.classesInSignature().count();
                long num_dp = ontology.dataPropertiesInSignature().count();
                long num_op = ontology.objectPropertiesInSignature().count();
                long num_int = ontology.individualsInSignature().count();
                
                value = (num_classes + num_dp + num_op + num_int);
                
            } catch (OWLOntologyCreationException ex) {
                System.getLogger(OntologyExtractor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
            return value;
            
        }

	public long getOntologyTotalElements(String filepath) {
		File file = new File(filepath);

		if(!file.exists()) {
			System.out.println("ontology file does not exist");
			System.exit(0);
		}

                
                
		try {
			ontology = owl_manager.loadOntologyFromOntologyDocument(file);


		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//get all terms from classes, properties (data and object), and instances
		this.extractAxiomTerms(ontology);

		//System.out.println("DONE EXTRACTING");
                
                long num_classes = ontology.classesInSignature().count();
                long num_dp = ontology.dataPropertiesInSignature().count();
                long num_op = ontology.objectPropertiesInSignature().count();
                long num_int = ontology.individualsInSignature().count();
                
                
                
                System.out.println("OBSERVERD: " + (num_classes + num_dp + num_op + num_int));
		
		//get total number of elements
		//this.number_of_elements = classList.size() + dataPropertyList.size() + objectPropertyList.size() + instanceList.size();
		
                this.number_of_elements = (num_classes + num_dp + num_op + num_int);
                
		//System.out.println("DONE COLLECTING NUMBER OF ELEMENTS");
		
		return this.number_of_elements;
	}
        
        public void loadOntologyFile(String filePath){
            
            
            
            File file = new File(filePath);
            loadOntologyFile(file);
        }

	public void loadOntologyFile(File file) {

                this.emptyElementLists();
		//File file = new File(filePath);
                this.file_path = file.getAbsolutePath();
                
		if(!file.exists()) {
			System.out.println("ontology file does not exist");
			System.exit(0);
		}

		try {
                        owl_manager = OWLManager.createConcurrentOWLOntologyManager();
			ontology = owl_manager.loadOntologyFromOntologyDocument(file);


		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
                    System.out.println("Issue with " + file.getName());
                }

		//get profile violations
		profile_violations = this.getProfileViolationsFromOntology(ontology);

		//get number of axioms
		this.number_of_axioms = this.ontology.getAxiomCount();
                System.out.println("\t\tNumber of Axioms: " +number_of_axioms);

		//get number of syntatic usage
		this.number_of_syntatic_used = this.getNumSyntaticUsage();
		this.total_syntatics = 39;

		//get all terms from classes, properties (data and object), and instances
		this.extractAxiomTerms(ontology);

		//get total number of elements
                long num_classes = ontology.classesInSignature().count();
                long num_dp = ontology.dataPropertiesInSignature().count();
                long num_op = ontology.objectPropertiesInSignature().count();
                long num_int = ontology.individualsInSignature().count();
                
               
                
		this.number_of_elements = (num_classes + num_dp + num_op + num_int);
                
                this.collectClassMetrics();
                this.collectLeafMetrics();
	}
        
        public void collectClassMetrics(){
            //number of subclasses
            
            int count_subclass =0;
            for(var a : ontology.getAxioms(AxiomType.SUBCLASS_OF)){
                
                if(a.getSubClass() instanceof OWLClass){
                    count_subclass++;
                }
                
            }
            //System.out.println(count_subclass);
            this.number_of_subclasses = count_subclass;
        }
        
        public void collectLeafMetrics(){
            //credit: https://stackoverflow.com/questions/58436546/retrieve-just-root-classes-classes-with-no-asserted-subclassof-parent-class
            //Set<OWLClass> collect = ontology.classesInSignature().filter(c->ontology.subClassAxiomsForSuperClass(c).count()==0).collect(Collectors.toSet());
            long count = ontology.classesInSignature().filter(c->ontology.subClassAxiomsForSuperClass(c).count()==0).count();
            
            this.number_of_leaves = count;
        }
        
        public void collectAverageLeafAncestorMetrics(){
            
            long count = 0;
            /*
            Set<OWLClass> collect = ontology.classesInSignature().filter(c->ontology.subClassAxiomsForSuperClass(c).count()==0).collect(Collectors.toSet());
            
            for(var o :collect){
                System.out.println(o.toStringID() + ">>>");
                Set<OWLSubClassOfAxiom> collect1 = ontology.subClassAxiomsForSubClass(o).collect(Collectors.toSet());
                collect1.stream().forEach(System.out::println);
            }
            */
            
            Set<OWLClass> collect = ontology.classesInSignature().filter(c->ontology.subClassAxiomsForSuperClass(c).count()==0).collect(Collectors.toSet());
            
            OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
            OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
            reasoner.precomputeInferences();
            
            
            for(var o : collect){
                count=count+reasoner.getSuperClasses(o,true).getFlattened().size();
            }
            //collect.stream().forEach(o->o);
            System.out.println(count);
            average_ancestor_for_leaves = count;
            //collect.stream().filter(a->ontology.subClassAxiomsForSuperClass(a).filter(predicate))
        }
        
        public void collectAnnotationMetrics(){
            this.number_of_annotations = ontology.annotations().count();
        }

	public int getProfileViolationsFromOntology(OWLOntology o) {
		OWL2DLProfile profile = new OWL2DLProfile();
		OWLProfileReport report = profile.checkOntology(o);

		return report.getViolations().size();

	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

                System.out.println(DateTime.now(DateTimeZone.forID("America/Chicago")));
                OntologyExtractor oe = OntologyExtractor.getInstance();
                
                String maxo = "http://purl.obolibrary.org/obo/maxo.owl";
                String bfo = "http://purl.obolibrary.org/obo/bfo.owl";
                String chembi = "http://purl.obolibrary.org/obo/chebi.owl";
                
                oe.getOntologyTotalElements(IRI.create(chembi));
                //oe.collectAverageLeafAncestorMetrics();
                oe.collectClassMetrics();
                oe.collectLeafMetrics();
                oe.collectAverageLeafAncestorMetrics();
                System.out.println(DateTime.now(DateTimeZone.forID("America/Chicago")));
	}

	private void extractAxiomTerms(OWLOntology o) {

		o.logicalAxioms().forEach(this::processEachTerm);
		
		//System.out.println("finished creating lists");

		raw_terms.addAll(classList);
		raw_terms.addAll(dataPropertyList);
		raw_terms.addAll(objectPropertyList);
		raw_terms.addAll(instanceList);

		terms.addAll(raw_terms);
		
		//System.out.println("finished adding terms");

		//clean up terms

		for(String term :terms) {
			String processed_term = term;

			if(processed_term != null || !processed_term.isEmpty()) {

				if(ConfigFlags.getInstance().isCamelCase()) {
					processed_term = buildStringFromArray(StringUtils.splitByCharacterTypeCamelCase(processed_term));
				}

				if(ConfigFlags.getInstance().isDetermiters()) {
					String pattern = " a | the | an";
					processed_term = processed_term.replaceAll(pattern, "");
				}

				if(ConfigFlags.getInstance().isBrackets()) {
					processed_term = processed_term.replaceAll("(\\(.+?\\))|(\\{.+?\\})", "");
				}

				if(ConfigFlags.getInstance().isUnderscore()) {
					processed_term = processed_term.replaceAll("_", " ");
				}

				if(ConfigFlags.getInstance().isDashes()) {
					processed_term = processed_term.replaceAll("-", " ");
				}

				processed_terms.add(processed_term);

				Lexicon lexicon_term = new Lexicon();
				lexicon_term.setProcessed_term(processed_term);
				lexicon_term.setTerm(term);
				lexicon_terms.add(lexicon_term);

			}

		}
		
		//System.out.println("Finished cleaning terms");

		//generate word senses
		for(Lexicon lex_term : lexicon_terms) {
			int sense_count = 0;
			String words[] = lex_term.getProcessed_term().trim().split("\\s+");
			for(String word : words) {
				if(word !=null && !word.isEmpty()) {
                                    for(POS pos : POS.getAllPOS()){
                                    sense_count = sense_count + wordNet.calculateWordSense(word, pos);
                                }
					//for (POS c : POS.values()) {
					//	sense_count = sense_count + wordNet.calculateWordSense(word, c);
					//}
				}
			}
			lex_term.setSenses(sense_count);

		}

		//System.out.println("Finished creating word senses");
		
		//remove excessive spaces
		for(Lexicon lex_term : lexicon_terms) {
			String term = lex_term.getProcessed_term();

			term = term.trim().replaceAll(" +", " ");
			lex_term.setProcessed_term(term);
		}
		
		//System.out.println("Finished cleaning up remaining terms");

	}

	private void processEachTerm(OWLLogicalAxiom oa) {
            
            
            

            oa.classesInSignature().forEach(owl_class->{
                
                classList.add(this.addClassLabel(owl_class));
                
            });
            
            oa.dataPropertiesInSignature().forEach(data_prop->{
                
                dataPropertyList.add(this.addPropertyLabel(data_prop));
            });
            
            
            oa.objectPropertiesInSignature().forEach(ob_prop->{
                objectPropertyList.add(this.addPropertyLabel(ob_prop));
            });
            
            
            oa.individualsInSignature().forEach(i->{
                instanceList.add(this.addInstanceLabel(i));
            });
            
            
            
		

	}

	private String addClassLabel(OWLClassExpression oce){
		String label = "";


		for(OWLAnnotation a : EntitySearcher.getAnnotations(oce.asOWLClass(), ontology, df.getRDFSLabel()).toArray(OWLAnnotation[]::new))
		{
			OWLAnnotationValue val = a.getValue();

			if(val instanceof OWLLiteral){
				label = ((OWLLiteral)val).getLiteral();

			}
		}
		if(label == ""){
			label = oce.toString();
		}


		return label;

	}

	private String addPropertyLabel(OWLDataPropertyExpression op){
		String label = "";
		for(OWLAnnotation a: EntitySearcher.getAnnotations(op.asOWLDataProperty(),ontology, df.getRDFSLabel()).toArray(OWLAnnotation[]::new))
		{
			OWLAnnotationValue val = a.getValue();
			if(val instanceof OWLLiteral){
				label = ((OWLLiteral)val).getLiteral();


			}
		}
		if(label == ""){
			label = op.toString();
		}

		return label;
	}

	private String addPropertyLabel(OWLObjectPropertyExpression op){
		String label = "";
		for(OWLAnnotation a: EntitySearcher.getAnnotations(op.asOWLObjectProperty(),ontology, df.getRDFSLabel()).toArray(OWLAnnotation[]::new)){
			OWLAnnotationValue val = a.getValue();
			if(val instanceof OWLLiteral){
				label = ((OWLLiteral)val).getLiteral();
				//owldata.insertTerm(label);

			}
		}

		if(label == ""){
			label = op.toString();
		}

		return label;
	}

	private String addInstanceLabel(OWLNamedIndividual i){
		String label = "";
		for(OWLAnnotation a: EntitySearcher.getAnnotations(i.asOWLNamedIndividual(), ontology, df.getRDFSLabel()).toArray(OWLAnnotation[]::new))
		{
			OWLAnnotationValue val = a.getValue();
			if(val instanceof OWLLiteral){
				label = ((OWLLiteral)val).getLiteral();


			}
		} 

		if(label == ""){
			label = i.toString();
		}


		return label;
	}

	private int getNumSyntaticUsage(){
		int count = 0;

		//get class syntax usage
		if(new ReferencedClassCount(ontology).getValue().intValue()>0)count++;

		//get object property usage
		if(new ReferencedObjectPropertyCount(ontology).getValue().intValue()>0)count++;

		//get data property usage
		if(new ReferencedDataPropertyCount(ontology).getValue().intValue()>0)count++;

		//get individual syntax
		if( new ReferencedIndividualCount(ontology).getValue().intValue()>0)count++;

		//get GCI
		if(new GCICount(ontology).getValue().intValue()>0)count++;

		//get hidden gci
		if(new HiddenGCICount(ontology).getValue().intValue()>0)count++;

		//get subclass syntax
		if(ontology.getAxiomCount(AxiomType.SUBCLASS_OF)>0)count++;

		//get equivalentclass syntax
		if(ontology.getAxiomCount(AxiomType.EQUIVALENT_CLASSES)>0)count++;

		//get disjoint syntax
		if(ontology.getAxiomCount(AxiomType.DISJOINT_CLASSES)>0)count++;

		// subobjectproperty syntax
		if(ontology.getAxiomCount(AxiomType.SUB_OBJECT_PROPERTY)>0)count++;

		//equavlient object property syntax
		if(ontology.getAxiomCount(AxiomType.EQUIVALENT_OBJECT_PROPERTIES)>0)count++;

		//inverse object preoperty sytnax
		if(ontology.getAxiomCount(AxiomType.INVERSE_OBJECT_PROPERTIES)>0)count++;

		//disjoint object property syntax
		if(ontology.getAxiomCount(AxiomType.DISJOINT_OBJECT_PROPERTIES)>0)count++;

		//functional object property syntax
		if(ontology.getAxiomCount(AxiomType.FUNCTIONAL_OBJECT_PROPERTY)>0)count++;

		//inverse functional object syntax
		if(ontology.getAxiomCount(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY)>0)count++;

		//transitive object syntax
		if(ontology.getAxiomCount(AxiomType.TRANSITIVE_OBJECT_PROPERTY)>0)count++;

		//symmetric object syntax
		if(ontology.getAxiomCount(AxiomType.SYMMETRIC_OBJECT_PROPERTY)>0)count++;

		//assymettric object syntax
		if(ontology.getAxiomCount(AxiomType.ASYMMETRIC_OBJECT_PROPERTY)>0)count++;

		//reflexisive object property 
		if(ontology.getAxiomCount(AxiomType.REFLEXIVE_OBJECT_PROPERTY)>0)count++;

		//irrflexisive object property
		if(ontology.getAxiomCount(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY)>0)count++;

		//object property domain
		if(ontology.getAxiomCount(AxiomType.OBJECT_PROPERTY_DOMAIN)>0)count++;

		//object property range
		if(ontology.getAxiomCount(AxiomType.OBJECT_PROPERTY_RANGE)>0)count++;

		//subproperty chain of
		if(ontology.getAxiomCount(AxiomType.SUB_PROPERTY_CHAIN_OF)>0)count++;

		//subdataproperty
		if(ontology.getAxiomCount(AxiomType.SUB_DATA_PROPERTY)>0)count++;

		//equivalentdataproperty
		if(ontology.getAxiomCount(AxiomType.EQUIVALENT_DATA_PROPERTIES)>0)count++;

		//disjointdataproperty
		if(ontology.getAxiomCount(AxiomType.DISJOINT_DATA_PROPERTIES)>0)count++;

		//functional data property
		if(ontology.getAxiomCount(AxiomType.FUNCTIONAL_DATA_PROPERTY)>0)count++;

		//data property domain
		if(ontology.getAxiomCount(AxiomType.DATA_PROPERTY_DOMAIN)>0)count++;

		//data property range
		if(ontology.getAxiomCount(AxiomType.DATA_PROPERTY_RANGE)>0)count++;

		//class assertion
		if(ontology.getAxiomCount(AxiomType.CLASS_ASSERTION)>0)count++;

		//object property assertion
		if(ontology.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION)>0)count++;

		//data property assertion
		if(ontology.getAxiomCount(AxiomType.DATA_PROPERTY_ASSERTION)>0)count++;

		//negative data property assertion
		if(ontology.getAxiomCount(AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION)>0)count++;

		if(ontology.getAxiomCount(AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION)>0)count++;

		//same individual
		if(ontology.getAxiomCount(AxiomType.SAME_INDIVIDUAL)>0)count++;

		//different individual
		if(ontology.getAxiomCount(AxiomType.DIFFERENT_INDIVIDUALS)>0)count++;

		//annotation assertion
		if(ontology.getAxiomCount(AxiomType.ANNOTATION_ASSERTION)>0)count++;

		//annotation property domain
		if(ontology.getAxiomCount(AxiomType.ANNOTATION_PROPERTY_DOMAIN)>0)count++;

		//annotation preoprty range
		if(ontology.getAxiomCount(AxiomType.ANNOTATION_PROPERTY_RANGE)>0)count++;



		//count = count + ontology.getAxiomCount(AxiomType.DECLARATION);
		System.out.println("Number: " + count);

		return count;

	}

	private String buildStringFromArray(String [] arrayString){
		StringBuilder builder = new StringBuilder();

		for(String s : arrayString) {
			builder.append(" " + s);
		}

		return builder.toString();
	}

}
