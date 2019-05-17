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
package jbiclustge.enrichmentanalysistools.topgo.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.propertiesmodules.PropertyLabels;
import pt.ornrocha.propertyutils.EnhancedProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class TopGoPropertiesContainer.
 */
public class TopGoPropertiesContainer extends EnhancedProperties{

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	
	/** The Constant ONTOLOGY. */
	public static final String ONTOLOGY="topgo_ontology";
	
	/** The Constant ALGORITHM. */
	public static final String ALGORITHM="topgo_algorithm";
	
	/** The Constant STATISTIC. */
	public static final String STATISTIC="topgo_statistic";
	
	/** The Constant MCTMETHOD. */
	public static final String MCTMETHOD="topgo_multiple_test_correction_method";
	
	/** The Constant NODESIZE. */
	public static final String NODESIZE="topgo_node_size";
	
	/** The Constant ANNOTATIONDATABASE. */
	public static final String ANNOTATIONDATABASE="topgo_annotation_database";
	
	/** The Constant ANNOTATIONDATABASEORGTYPE. */
	public static final String ANNOTATIONDATABASEORGTYPE="topgo_annotation_database_org_type";
	
	/** The Constant MAPPINGTYPE. */
	public static final String MAPPINGTYPE="topgo_goterm_map_type";
	
	/** The Constant ANNOTATIONFUNCTION. */
	public static final String ANNOTATIONFUNCTION="topgo_annotation_function";
	
	/** The Constant ANNOTATIONFILE. */
	public static final String ANNOTATIONFILE="topgo_annotation_file";
	
	/** The Constant ANNOTATIONFILETERMSDELIMITER. */
	public static final String ANNOTATIONFILETERMSDELIMITER="topgo_annotation_goterms_delimiter";
	
	/** The Constant ANNOTATIONFILECOLUMNDELIMITER. */
	public static final String ANNOTATIONFILECOLUMNDELIMITER="topgo_annotation_column_delimiter";
	
	/** The Constant ANNOTATIONFILEGO2GENEFORMAT. */
	public static final String ANNOTATIONFILEGO2GENEFORMAT="topgo_annotation_go2gene_format";
	
	/** The Constant DISCARDUNANNOTATEDGENES. */
	public static final String DISCARDUNANNOTATEDGENES="discard_unannotated_genes";
	
	/** The Constant MAPPROBEID2GENEID. */
	//public static final String MAPPROBEID2GENEID="file_with_gene_expression_identifiers_to_gene_annotation_identifiers";
	
	
	/**
	 * Instantiates a new top go properties container.
	 */
	public TopGoPropertiesContainer(){}



	/**
	 * Sets the ontology.
	 *
	 * @param ontology the new ontology
	 */
	public void setOntology(String ontology){
		addPropertyKey(ONTOLOGY, ontology,"Ontology of interest (BP, MF or CC)");
	}
	
	/**
	 * Sets the ontology.
	 *
	 * @param ontology the new ontology
	 */
	public void setOntology(TopgoOntology ontology){
		setOntology(ontology.getGOAspect().getGOType().toLowerCase());
	}
	
	
	/**
	 * Gets the ontology.
	 *
	 * @return the ontology
	 */
	public String getOntology(){
		return getProperty(ONTOLOGY, TopgoOntology.BP.getGOAspect().getGOType());
	}
	
	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(String algorithm){
		addPropertyKey(ALGORITHM, algorithm,"Specifies which method for dealing with the GO graph structure. Possible values are: classic, elim, weight, weight01, lea, parentchild");
	}
	
	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(TopGOAlgorithm algorithm){
		setAlgorithm(algorithm.toString());
	}
	
	/**
	 * Gets the algorithm.
	 *
	 * @return the algorithm
	 */
	public String getAlgorithm(){
		return getProperty(ALGORITHM, TopGOAlgorithm.classic.toString());
	}
	
	/**
	 * Sets the statistic method.
	 *
	 * @param stats the new statistic method
	 */
	public void setStatisticMethod(String stats){
		addPropertyKey(STATISTIC, stats,"Specifies which group test statistic will be used. Possible values are:  fisher, ks, t, globaltest, sum");
	}
	
	/**
	 * Sets the statistic method.
	 *
	 * @param stats the new statistic method
	 */
	public void setStatisticMethod(TopGOStatistic stats){
		setStatisticMethod(stats.toString());
	}
	
	/**
	 * Gets the statistic method.
	 *
	 * @return the statistic method
	 */
	public String getStatisticMethod(){
		return getProperty(STATISTIC, TopGOStatistic.fisher.toString());
	}

	
	/**
	 * Sets the node size.
	 *
	 * @param nodesize the new node size
	 */
	public void setNodeSize(int nodesize){
		addPropertyKey(NODESIZE, String.valueOf(nodesize),"Integer larger or equal to 1. This parameter is used to prune the GO hierarchy from the terms "
				+ "which have less than nodeSize annotated genes (after the true path rule is applied)");
	}
	
	/**
	 * Gets the node size.
	 *
	 * @return the node size
	 */
	public int getNodeSize(){
		try {
			return Integer.parseInt(getProperty(NODESIZE));
		} catch (Exception e) {
			return 5;
		}
	}
	
	/**
	 * Sets the annotation database.
	 *
	 * @param name the new annotation database
	 */
	public void setAnnotationDatabase(String name){
		addPropertyKey(ANNOTATIONDATABASE, name,"Name of the annotation package to be used.");
	}
	
	/**
	 * Gets the annotation database.
	 *
	 * @return the annotation database
	 */
	public String getAnnotationDatabase(){
		if(containsKey(ANNOTATIONDATABASE) && !getProperty(ANNOTATIONDATABASE).isEmpty())
			return getProperty(ANNOTATIONDATABASE);
		return null;
	}
	
	/**
	 * Sets the annotation file.
	 *
	 * @param filepath the new annotation file
	 */
	public void setAnnotationFile(String filepath){
		addPropertyKey(ANNOTATIONFILE, filepath,"Annotation user file to be used.");
	}
	
	/**
	 * Gets the annotation file.
	 *
	 * @return the annotation file
	 */
	public String getAnnotationFile(){
		if(containsKey(ANNOTATIONFILE) && !getProperty(ANNOTATIONFILE).isEmpty())
			return getProperty(ANNOTATIONFILE);
		return null;
	}
	
	/**
	 * Sets the annotation column delimiter.
	 *
	 * @param delimiter the new annotation column delimiter
	 */
	public void setAnnotationColumnDelimiter(String delimiter){
		addPropertyKey(ANNOTATIONFILECOLUMNDELIMITER, delimiter,"Column delimiter used in annotation file");
	}
	
	/**
	 * Sets the annotation GO terms delimiter.
	 *
	 * @param delimiter the new annotation GO terms delimiter
	 */
	public void setAnnotationGOTermsDelimiter(String delimiter){
		addPropertyKey(ANNOTATIONFILETERMSDELIMITER, delimiter,"GO Terms delimiter used in annotation file");
	}
	
	
	/**
	 * Sets the GO term mapping type.
	 *
	 * @param type the new GO term mapping type
	 */
	public void setGOTermMappingType(String type){
		addPropertyKey(MAPPINGTYPE, type,"If annotation package is of type org.XX.XX, choose how the gene identifiers are mapped. Possible values:"
				+ " Entrez, GenBank, Alias, Ensembl, Gene, Symbol, GeneName and UniGene");
	}
	
	/**
	 * Gets the GO term mapping type.
	 *
	 * @return the GO term mapping type
	 */
	public String getGOTermMappingType(){
		if(containsKey(MAPPINGTYPE) && !getProperty(MAPPINGTYPE).isEmpty())
				return getProperty(MAPPINGTYPE);
		return null;
	}
	
	/**
	 * Sets the GO term mapping type.
	 *
	 * @param type the new GO term mapping type
	 */
	public void setGOTermMappingType(TopGOMappingType type){
		setGOTermMappingType(type.toString());
	}
	
	/**
	 * Sets the checks if is annotation GO 2 gene format.
	 *
	 * @param bool the new checks if is annotation GO 2 gene format
	 */
	public void setIsAnnotationGO2GeneFormat(boolean bool){
		addPropertyKey(ANNOTATIONFILEGO2GENEFORMAT, String.valueOf(bool),"Specifies if the annotations are provided as a GO-to-genes mapping (true),"
				+ " else it is assumed that the annotations are provided as a gene-to-GOs mapping (false)");
	}
	
	/**
	 * Sets the checks if is annotation database ORG type.
	 *
	 * @param bool the new checks if is annotation database ORG type
	 */
	public void setIsAnnotationDatabaseORGType(boolean bool){
		addPropertyKey(ANNOTATIONDATABASEORGTYPE, String.valueOf(bool),"Specifies if the annotation package uses mappings of the type org.XX.XX, possible values: true or false");
	}
	
	/**
	 * Checks if is annotation database ORG type.
	 *
	 * @return true, if is annotation database ORG type
	 */
	public boolean isAnnotationDatabaseORGType(){
		if(containsKey(ANNOTATIONDATABASEORGTYPE) && !getProperty(ANNOTATIONDATABASEORGTYPE).isEmpty())
			if(getProperty(ANNOTATIONDATABASEORGTYPE).toLowerCase().equals("true"))
				return true;
		return false;
	}
	
	/**
	 * Sets the MTC method.
	 *
	 * @param method the new MTC method
	 */
	public void setMTCMethod(pvaluesAdjustMethod method){
		addPropertyKey(MCTMETHOD, method.toString(),"Specifies the multiple test correction method for p-values. Possible values: holm, hochberg, bonferroni, BH, BY, fdr and none (default=none)."
				+ " This functionality do not take part of topGo, it is an external function.");
	}
	
	/**
	 * Gets the MTC method.
	 *
	 * @return the MTC method
	 */
	public String getMTCMethod(){
		if(containsKey(MCTMETHOD) && !getProperty(MCTMETHOD).isEmpty())
			return getProperty(MCTMETHOD);
		return null;
	}
	
	
	public void setDiscardUnannotatedGenes(boolean bool){
		addPropertyKey(DISCARDUNANNOTATEDGENES, String.valueOf(bool),"Discard genes that are not in annotation file");
	}
	
	
	
	public static void writeTopGOProperties(TopGoPropertiesContainer props, String filepath) throws IOException {
		props.store(new FileWriter(new File(filepath)), true);	
	}
	
	
	
	/**
	 * Write properties file to annotation database.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writePropertiesFileToAnnotationDatabase(String filepath) throws IOException{
		TopGoPropertiesContainer props=getCommonProperties();
		
		String[] propkeys=new String[]{
				ANNOTATIONDATABASE,
				ANNOTATIONDATABASEORGTYPE,
				MAPPINGTYPE
				
		};
		String[] defaultvalues=new String[]{"",String.valueOf(true),TopGOMappingType.Symbol.toString()};
		String[] comments=new String[] {
				"Name of the annotation package to be used.",
				"Specifies if the annotation package uses mappings of the type org.XX.XX, possible values: true or false",
				"If annotation package is of type org.XX.XX, choose how the gene identifiers are mapped. Possible values:"
				+ " Entrez, GenBank, Alias, Ensembl, Gene, Symbol, GeneName and UniGene"
					
		};
		
		props.addProperties(propkeys, defaultvalues, comments);
		props.store(new FileWriter(new File(filepath)), true);
	}
	
	/**
	 * Write properties file to annotation file.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writePropertiesFileToAnnotationFile(String filepath) throws IOException{

		TopGoPropertiesContainer props=getCommonProperties();
		
		String[] propkeys=new String[]{
				ANNOTATIONFILE,
				ANNOTATIONFILEGO2GENEFORMAT,
				ANNOTATIONFILECOLUMNDELIMITER,
				ANNOTATIONFILETERMSDELIMITER
				
		};
		String[] defaultvalues=new String[]{"",String.valueOf(false),"\t",","};
		String[] comments=new String[] {
				"Annotation user file to be used.",
				"Specifies if the annotations are provided as a GO-to-genes mapping (true), else it is assumed that the annotations are provided as a gene-to-GOs mapping (false)",
				"Column delimiter used in annotation file",
				"GO Terms delimiter used in annotation file"
					
		};
		
		props.addProperties(propkeys, defaultvalues, comments);
		props.store(new FileWriter(new File(filepath)), true);

	}
	
	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @return the top go properties container
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TopGoPropertiesContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments) throws IOException{
		TopGoPropertiesContainer props=new TopGoPropertiesContainer();
		props.addProperties(keys, defaultvalues, comments);
		return props;
	}
	
	/**
	 * Append properties.
	 *
	 * @param props the props
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @return the top go properties container
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TopGoPropertiesContainer appendProperties(TopGoPropertiesContainer props, String[] keys, String[] defaultvalues) throws IOException{
		if(props==null)
			props=new TopGoPropertiesContainer();
		
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
	 * @return the top go properties container
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TopGoPropertiesContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		TopGoPropertiesContainer props=new TopGoPropertiesContainer();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	
	
	/**
	 * Gets the common properties.
	 *
	 * @return the common properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static TopGoPropertiesContainer getCommonProperties() throws IOException{
		
		String[] propkeys=new String[]{
				ONTOLOGY,
				ALGORITHM,
				STATISTIC,
				NODESIZE,
				MCTMETHOD,
				DISCARDUNANNOTATEDGENES,
				PropertyLabels.MAPPROBEID2GENEID
		};
		String[] defaultvalues=new String[]{TopgoOntology.BP.getGOAspect().getGOType(),TopGOAlgorithm.classic.toString(),TopGOStatistic.fisher.toString(),"5","none",String.valueOf(true),""};
		String[] comments=new String[] {
				"Ontology of interest (BP, MF or CC)",
				"Specifies which method for dealing with the GO graph structure. Possible values are: classic, elim, weight, weight01, lea, parentchild",
				"Specifies which group test statistic will be used. Possible values are:  fisher, ks, t, globaltest, sum",
				"Integer larger or equal to 1. This parameter is used to prune the GO hierarchy from the terms "
				+ "which have less than nodeSize annotated genes (after the true path rule is applied)",
				"Specifies the multiple test correction method for p-values. Possible values: holm, hochberg, bonferroni, BH, BY, fdr and none (default=none). This functionality do not take part of topGo, it is an external function.",
				"Discard genes that are not in annotation file",
				"File with two columns (column delimiter (;), first column is the gene identifiers of expression dataset and the second column is the gene identifiers used in go annotation file."
						+ " This is an optional file, that must be used if genes described in annotation file have a diferent identifier of genes described in expression dataset."
					
		};
		
		
		return TopGoPropertiesContainer.setupProperties(propkeys, defaultvalues, comments, "Source: https://bioconductor.org/packages/release/bioc/html/topGO.html");
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException{
		TopGoPropertiesContainer.writePropertiesFileToAnnotationDatabase("/home/orocha/discodados/ApenasTrabalho/Resultados_biclustering/Evaluation/Mydata/yeast_real_dataset/topgo.props");
		//TopGoPropertiesContainer.writePropertiesFileToAnnotationFile("/home/orocha/topGOFileProps.props");
	}
}
