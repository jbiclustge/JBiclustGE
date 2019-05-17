/************************************************************************** 
 * Copyright 2015 - 2017
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 * Created by Orlando Rocha (ornrocha@gmail.com) inside BIOSYSTEMS Group (https://www.ceb.uminho.pt/BIOSYSTEMS)
 */
package jbiclustge.enrichmentanalysistools.ontologizer.components;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jbiclustge.annotation.goannotation.components.GOAspect;
import pt.ornrocha.propertyutils.EnhancedProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class OntologizerPropertiesContainer.
 */
public class OntologizerPropertiesContainer extends EnhancedProperties{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant CALCULATIONMETHOD. */
	public static final String CALCULATIONMETHOD="ontologizer_calculation_method";
	
	/** The Constant MCTMETHOD. */
	public static final String MCTMETHOD="ontologizer_multiple_test_correction_method";
	
	/** The Constant RESAMPLINGSTEPS. */
	public static final String RESAMPLINGSTEPS="ontologizer_resampling_steps";
	
	/** The Constant SIZETOLERANCE. */
	public static final String SIZETOLERANCE="ontologizer_size_tolerance";
	
	/** The Constant POPULATIONFILE. */
	public static final String POPULATIONFILE="ontologizer_population_file";
	
	/** The Constant ONTOLOGYFILE. */
	public static final String ONTOLOGYFILE="ontologizer_ontology_file";
	
	/** The Constant URLONTOLOGYFILE. */
	public static final String URLONTOLOGYFILE="url_ontology_file";
	
	/** The Constant ANNOTATIONFILE. */
	public static final String ANNOTATIONFILE="ontologizer_annotation_file";
	
	/** The Constant ANNOTATIONTYPE. */
	public static final String ANNOTATIONTYPE="ontologizer_type_ontology";
	
	/** The Constant IGNOREGOTERMIDS. */
	public static final String IGNOREGOTERMIDS="ontologizer_ignore_GO_terms_ids";
	
	/** The Constant ANNOTATIONUPDATE. */
	public static final String ANNOTATIONUPDATE="force_update_type_ontology";
	
	/** The Constant ORGANISMANNOTATIONFILE. */
	public static final String ORGANISMANNOTATIONFILE="standard_organism_ontologizer_annotation_file_url";
	
	/** The Constant MAPPROBEID2GENEID. */
	public static final String MAPPROBEID2GENEID="file_with_gene_expression_identifiers_to_gene_annotation_identifiers";
	
	
    
	/**
	 * Sets the calculation method.
	 *
	 * @param calculationmethod the new calculation method
	 */
	public void setCalculationMethod(String calculationmethod){
		addPropertyKey(CALCULATIONMETHOD, calculationmethod,"Specifies the calculation method to use. Possible values are: MGSA, Parent-Child-Intersection,"
				+ " Parent-Child-Union (default), Term-For-Term, Topology-Elim, Topology-Weighted");
	}
	
	public void setCalculationMethod(OntologizerCalculationMethod calculationmethod){
		setCalculationMethod(calculationmethod.toString());
	}
	
	
	/**
	 * Sets the multiple test correction method.
	 *
	 * @param mct the new multiple test correction method
	 */
	public void setMultipleTestCorrectionMethod(String mtc){
		addPropertyKey(MCTMETHOD, mtc,"Specifies the MTC method to use. Possible values"
				+ " are: Benjamini-Hochberg, Benjamini-Yekutieli, "
				+ "Bonferroni, Bonferroni-Holm, None (default), Westfall-Young-Single-Step,"
				+ " Westfall-Young-Step-Down");
	}
	
	public void setMultipleTestCorrectionMethod(OntologizerMTCMethod mtc){
		setMultipleTestCorrectionMethod(mtc.toString());
	}
	
	/**
	 * Sets the resampling steps.
	 *
	 * @param nsteps the new resampling steps
	 */
	public void setResamplingSteps(int nsteps){
		addPropertyKey(RESAMPLINGSTEPS, String.valueOf(nsteps),"Specifies the number of steps used in resampling based MTCs (use default=-1)");
	}
	
	
	/**
	 * Sets the size tolerance.
	 *
	 * @param size the new size tolerance
	 */
	public void setSizeTolerance(int size){
		addPropertyKey(SIZETOLERANCE, String.valueOf(size),"Specifies the percentage at which the actual study "
				+ "set size and the size of the resampled study sets"
				+ "are allowed to differ (use default=-1)");
	}
	
	
	/**
	 * Sets the population file path.
	 *
	 * @param filepath the new population file path
	 */
	public void setPopulationFilePath(String filepath){
		addPropertyKey(POPULATIONFILE, filepath);
	}
	
	/**
	 * Sets the ontology file path.
	 *
	 * @param filepath the new ontology file path
	 */
	public void setOntologyFilePath(String filepath){
		addPropertyKey(ONTOLOGYFILE,filepath,"File containing information about GO terms");
	}
	
	
	public void setOntologyURLFile(String url){
		addPropertyKey(URLONTOLOGYFILE,url,"URL of file containing information about GO terms, this file will be download from the provided URL (example: http://purl.obolibrary.org/obo/go.obo)");
	}
	
	/**
	 * Sets the annotation file path.
	 *
	 * @param filepath the new annotation file path
	 */
	public void setAnnotationFilePath(String filepath){
		addPropertyKey(ANNOTATIONFILE, filepath,"File which contains the annotations");
	}
	
	
	public void setAnnotationOrganismURLFile(String url){
		addPropertyKey(ORGANISMANNOTATIONFILE, url,"Instead of using ontologizer annotation file, an standard annotation file of an organism can be used."
				+ " Define the url of file (ex: http://geneontology.org/gene-associations/gene_association.ecocyc.gz)");
	}
	
	
	
	public void setListTermIDSToIgnore(ArrayList<String> listids) {
		if(listids!=null && listids.size()>0) {
			StringBuilder str=new StringBuilder();
			for (int i = 0; i < listids.size(); i++) {
				str.append(listids.get(i));
				if(i<(listids.size()-1))
					str.append(";");
			}
		   addPropertyKey(IGNOREGOTERMIDS, str.toString(),"Ignore GO term id or list of GO terms ids in gene enrichment analysis. Input GO terms ids must be separated by \";\"");
		}
	}
	
	
	public void setAnnotationGOAspectType(GOAspect aspect) {
		String aspectstr="all";
		if(aspect!=null)
			aspectstr=aspect.getGOType().toLowerCase();
		addPropertyKey(ANNOTATIONTYPE, aspectstr,"Ontology of interest, biological process (BP), molecular function (MF), cellular component (CC) or all (all) default=all");	
	}
	
	public void setAnnotationGOAspectType(OntologizerAnnotationType aspect) {
		setAnnotationGOAspectType(aspect.getGOAspect());
	}
	
	
	public void forceUpdateOfOntologyFileConcerningToGOAspect(boolean force) {
		addPropertyKey(ANNOTATIONUPDATE, String.valueOf(force),"Force update of ontology file when an ontology of interest is selected, true or false, default=false");
	}
	
	/**
	 * Gets the population file.
	 *
	 * @return the population file
	 */
	public String getPopulationFile(){
		if(containsKey(POPULATIONFILE) && !getProperty(POPULATIONFILE).isEmpty())
			return getProperty(POPULATIONFILE);
		return null;
			
	}
	
	/**
	 * Gets the ontology file path.
	 *
	 * @return the ontology file path
	 */
	public String getOntologyFilePath(){
		if(containsKey(ONTOLOGYFILE) && !getProperty(ONTOLOGYFILE).isEmpty())
			return getProperty(ONTOLOGYFILE);
		return null;
	}
	
	/**
	 * Gets the annotation file path.
	 *
	 * @return the annotation file path
	 */
	public String getAnnotationFilePath(){
		if(containsKey(ANNOTATIONFILE) && !getProperty(ANNOTATIONFILE).isEmpty())
			return getProperty(ANNOTATIONFILE);
		return null;
	}
	
	
	
	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @return the ontologizer properties container
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static OntologizerPropertiesContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments) throws IOException{
		OntologizerPropertiesContainer props=new OntologizerPropertiesContainer();
		props.addProperties(keys, defaultvalues, comments);
		return props;
	}
	
	/**
	 * Append properties.
	 *
	 * @param props the props
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @return the ontologizer properties container
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static OntologizerPropertiesContainer appendProperties(OntologizerPropertiesContainer props, String[] keys, String[] defaultvalues) throws IOException{
		if(props==null)
			props=new OntologizerPropertiesContainer();
		
		props.addProperties(keys, defaultvalues);
			
		return props;
	}
	
	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @param commentssource the commentssource
	 * @return the ontologizer properties container
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static OntologizerPropertiesContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		OntologizerPropertiesContainer props=new OntologizerPropertiesContainer();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	
	
	/*public static void writeSimplePropertiesFileTemplate(String filepath) throws IOException{
		getbaseProperties().store(new FileWriter(filepath), true);
	}*/
	
	public static void writeOntologizerProperties(OntologizerPropertiesContainer props,String filepath) throws IOException{
		props.store(new FileWriter(filepath), true);
	}
	
	/**
	 * Write complete properties file template.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeCompletePropertiesFileTemplate(String filepath) throws IOException{
		OntologizerPropertiesContainer props=getbaseProperties();
		String[] propkeys=new String[]{
				ONTOLOGYFILE,
				URLONTOLOGYFILE,
				POPULATIONFILE,
				ANNOTATIONFILE,
				ANNOTATIONTYPE,
				ANNOTATIONUPDATE,
				IGNOREGOTERMIDS,
				ORGANISMANNOTATIONFILE
				
		};
		String[] defaultvalues=new String[]{"","","","","all","false","",""};
		String[] comments=new String[] {
				"File containing information about GO terms",
				"URL of file containing information about GO terms, this file will be download from the provided URL (example: http://purl.obolibrary.org/obo/go.obo)",
				"File containing genes within the population",
				"File which contains the annotations",
				"Ontology of interest, biological process (BP), molecular function (MF), cellular component (CC) or all (all) default=all",
				"Force update of ontology file when an ontology of interest is selected, true or false, default=false",
				"Ignore GO term id or list of GO terms ids in gene enrichment analysis. Input GO terms ids must be separated by \";\"",
				"Instead of using ontologizer annotation file, an standard annotation file of an organism can be used. Define the url of file (ex: http://geneontology.org/gene-associations/gene_association.ecocyc.gz)"
		};
		props.addProperties(propkeys, defaultvalues, comments);
		props.store(new FileWriter(filepath), true);
		
	}
	
	
	/**
	 * Gets the base properties.
	 *
	 * @return the base properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static OntologizerPropertiesContainer getbaseProperties() throws IOException{
		String[] propkeys=new String[]{
				CALCULATIONMETHOD,
				MCTMETHOD,
				RESAMPLINGSTEPS,
				SIZETOLERANCE,
				MAPPROBEID2GENEID
		};
		String[] defaultvalues=new String[]{OntologizerCalculationMethod.ParentChildUnion.toString(),OntologizerMTCMethod.None.toString(),"-1","-1",""};
		String[] comments=new String[] {
				"Specifies the calculation method to use. Possible values are: MGSA, Parent-Child-Intersection,"
				+ " Parent-Child-Union (default), Term-For-Term, Topology-Elim, Topology-Weighted",
				"Specifies the MTC method to use. Possible values"
				+ " are: Benjamini-Hochberg, Benjamini-Yekutieli, "
				+ "Bonferroni, Bonferroni-Holm, None (default), Westfall-Young-Single-Step,"
				+ " Westfall-Young-Step-Down",
				"Specifies the number of steps used in resampling based MTCs (use default=-1)",
				"Specifies the percentage at which the actual study "
				+ "set size and the size of the resampled study sets"
				+ "are allowed to differ (use default=-1)",
				"File with two columns (column delimiter (;), first column is the gene identifiers of expression dataset and the second column is the gene identifiers used in go annotation file."
				+ " This is an optional file, that must be used if genes described in annotation file have a diferent identifier of genes described in expression dataset."
				
		};
		return  OntologizerPropertiesContainer.setupProperties(propkeys, defaultvalues, comments,"Source: http://ontologizer.de/commandline/");
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException{
		//OntologizerPropertiesContainer.writeCompletePropertiesFileTemplate("/home/orocha/discodados/Biclustering/files_test/ontologizer.props");
		OntologizerPropertiesContainer t=new OntologizerPropertiesContainer();
		t.setAnnotationFilePath("http://geneontology.org/gene-associations/gene_association.ecocyc.gz");
		t.store(new FileWriter("/home/orocha/discodados/ApenasTrabalho/Testes/propstest.conf"), true);
		
	}
	
}
