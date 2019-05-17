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
package jbiclustge.enrichmentanalysistools.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.utils.props.AlgorithmProperties;
import jbiclustge.utils.props.JBiGePropertiesManager;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class EnrichmentAnalyserProcessor.
 */
public abstract class EnrichmentAnalyserProcessor {
	

	/** The ontology file path. */
	protected String ontologyFilePath; // 
	
	/** The annotation file path. */
	protected String annotationFilePath;
	
	/** The population file path. */
	protected String populationFilePath;
	
	/** The listofbiclusters. */
	protected BiclusterList listofbiclusters;
	
	/** The props. */
	protected Properties props;
	
	/** The bicluster enrichment analysis list. */
	protected EnrichmentAnalysisResultList biclusterEnrichmentAnalysisList;
	
	/** The mapofprobset 2 geneid. */
	protected Map<String, String> mapofprobset2geneid;
	
	/** The maxpvalue. */
	protected double maxpvalue=0.05;
	
	protected PropertyChangeSupport changesupport=new PropertyChangeSupport(this);
	
	
	public static String FIREPROPERTYGSEACHANGEPROGRESS="FIREPROPERTYCHANGEPROGRESS";
	public static String FIREPROPERTYGSEACHANGETASKSTATUS="FIREPROPERTYCHANGETASKSTATUS";
	public static String FIREPROPERTYGSEACHANGESUBTASKSTATUS="FIREPROPERTYCHANGESUBTASKSTATUS";
	public static String FIREPROPERTYGSEAANALYSERCHANGENAME="FIREPROPERTYANALYSERCHANGENAME";
	public static String FIREPROPERTYGSEAANALYSERERROR="FIREPROPERTYGSEAANALYSERERROR";
	
	/**
	 * Instantiates a new enrichment analyser processor.
	 */
	public EnrichmentAnalyserProcessor(){}
	
	/**
	 * Instantiates a new enrichment analyser processor.
	 *
	 * @param annotationfilepath the annotationfilepath
	 */
	public EnrichmentAnalyserProcessor(String annotationfilepath){
		this.annotationFilePath=annotationfilepath;
	}
	
	/**
	 * Instantiates a new enrichment analyser processor.
	 *
	 * @param ontologyfilepath the ontologyfilepath
	 * @param annotationfilepath the annotationfilepath
	 */
	public EnrichmentAnalyserProcessor(String ontologyfilepath, String annotationfilepath){
		this.ontologyFilePath=ontologyfilepath;
		this.annotationFilePath=annotationfilepath;
	}
	
	/**
	 * Instantiates a new enrichment analyser processor.
	 *
	 * @param props the props
	 */
	public EnrichmentAnalyserProcessor(Properties props){
		this.props=props;
	}
	

	public EnrichmentAnalyserProcessor(BiclusterList listbiclusters, Properties props) {
		this(props);
		setBiclusteringResultsToAnalyse(listbiclusters);
	}
	
	
	/**
	 * Gets the max allowed P value.
	 *
	 * @return the max allowed P value
	 */
	private double getMaxAllowedPValue(){
		String val=(String) JBiGePropertiesManager.getManager().getKeyValue("max_p_value");
		double maxval=0.05;
		if(val!=null){
			try {
				maxval=Double.parseDouble(val);
			} catch (Exception e) {
				return 0.05;
			}
		}
		return maxval;
	}
	

	
	/**
	 * Mandatoryontologyfile.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean mandatoryontologyfile();
	
	/**
	 * Mandatoryannotationfile.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean mandatoryannotationfile();
	
	/**
	 * Gets the default ontology file path.
	 *
	 * @return the default ontology file path
	 */
	protected abstract String getDefaultOntologyFilePath();
	
	/**
	 * Valid for running.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	protected abstract boolean validForRunning() throws Exception;
	
	/**
	 * Run analysis.
	 */
	protected abstract void runAnalysis() throws Exception;
	
	/**
	 * Read properties.
	 */
	protected abstract void readProperties();
	
	/**
	 * Gets the unannotated gene names.
	 *
	 * @return the unannotated gene names
	 */
	public abstract HashSet<String> getUnannotatedGeneNames();
	
	/**
	 * Builds the enrichment analysis result list.
	 *
	 * @return the enrichment analysis result list
	 */
	protected abstract EnrichmentAnalysisResultList buildEnrichmentAnalysisResultList();
	
	/**
	 * Copy working instance.
	 *
	 * @return the enrichment analyser processor
	 */
	public abstract EnrichmentAnalyserProcessor copyWorkingInstance();
	
	public abstract GSEAAnalyserType getTypeAnalyserProcessor();
	
	public abstract void stopProcess();
	
	public void addAnalyserPropertyChangeListener(PropertyChangeListener listener) {
		changesupport.addPropertyChangeListener(listener);
	}
	

	
	/**
	 * Sets the biclustering results to analyse.
	 *
	 * @param listofbiclusters the new biclustering results to analyse
	 */
	public void setBiclusteringResultsToAnalyse(BiclusterList listofbiclusters){
		this.listofbiclusters=listofbiclusters;
	}
	
	/**
	 * Sets the annotation file path.
	 *
	 * @param annotationFilePath the new annotation file path
	 */
	public void setAnnotationFilePath(String annotationFilePath){
		this.annotationFilePath=annotationFilePath;
	}
	
	/**
	 * Sets the ontology file path.
	 *
	 * @param ontologyFilePath the new ontology file path
	 */
	public void setOntologyFilePath(String ontologyFilePath) {
		this.ontologyFilePath = ontologyFilePath;
	}

	/**
	 * Sets the population file path.
	 *
	 * @param populationFilePath the new population file path
	 */
	public void setPopulationFilePath(String populationFilePath) {
		this.populationFilePath = populationFilePath;
	}

	/**
	 * Sets the properties.
	 *
	 * @param props the new properties
	 */
	public void setProperties(Properties props){
		this.props=props;
		readProperties();
	}
	
	/**
	 * Load property file.
	 *
	 * @param properties the properties
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void loadPropertyFile(String properties) throws FileNotFoundException, IOException{
		setProperties(PropertiesUtilities.loadFileProperties(properties));
	}
	
	/**
	 * Sets the map of probe set to gene ID.
	 *
	 * @param map the map
	 */
	public void setMapOfProbeSetToGeneID(Map<String, String> map){
		this.mapofprobset2geneid=map;
	}
	
	/**
	 * Read map probe set ID to gene ID from file.
	 *
	 * @param filepath the filepath
	 * @param delimiter the delimiter
	 * @param ignoreheader the ignoreheader
	 * @param keyColumn the key column
	 * @param valueColumn the value column
	 * @param ignoreelemsregexp the ignoreelemsregexp
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void readMapProbeSetIDToGeneIDFromFile(String filepath, String delimiter, boolean ignoreheader, int keyColumn, int valueColumn,String ignoreelemsregexp) throws InstantiationException, IllegalAccessException, IOException{
		mapofprobset2geneid=MTUReadUtils.mapElementListFromFile(filepath, delimiter, keyColumn, valueColumn, ignoreheader, new LinkedHashMap<String, String>(),ignoreelemsregexp);
		
	}
	
	
	/**
	 * Read map probe set ID to gene ID from file.
	 *
	 * @param filepath the filepath
	 * @param delimiter the delimiter
	 * @param ignoreheader the ignoreheader
	 * @param keyColumn the key column
	 * @param valueColumn the value column
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void readMapProbeSetIDToGeneIDFromFile(String filepath, String delimiter, boolean ignoreheader, int keyColumn, int valueColumn) throws InstantiationException, IllegalAccessException, IOException{
		readMapProbeSetIDToGeneIDFromFile(filepath, delimiter,ignoreheader, keyColumn, valueColumn,null);
		
	}
	
	/**
	 * Read map probe set ID to gene ID from file.
	 *
	 * @param filepath the filepath
	 * @param delimiter the delimiter
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void readMapProbeSetIDToGeneIDFromFile(String filepath, String delimiter) throws InstantiationException, IllegalAccessException, IOException{
		readMapProbeSetIDToGeneIDFromFile(filepath, delimiter,false,0, 1);	
	}
	
	/**
	 * Read map probe set ID to gene ID from file.
	 *
	 * @param filepath the filepath
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void readMapProbeSetIDToGeneIDFromFile(String filepath) throws InstantiationException, IllegalAccessException, IOException{
		readMapProbeSetIDToGeneIDFromFile(filepath, ";",false,0, 1);
		
	}
	
	
	/**
	 * Gets the enrichment analysis results.
	 *
	 * @return the enrichment analysis results
	 */
	public EnrichmentAnalysisResultList getEnrichmentAnalysisResults(){
		return biclusterEnrichmentAnalysisList;
	}
	
	
	
	
	
	/**
	 * Run.
	 */
	public void run() throws Exception{
        
		if(listofbiclusters.size()>0){
			changesupport.firePropertyChange(FIREPROPERTYGSEAANALYSERCHANGENAME, null, getTypeAnalyserProcessor().toString());
			
			if(valid()){
				this.maxpvalue=getMaxAllowedPValue();
				runAnalysis();
				this.biclusterEnrichmentAnalysisList=buildEnrichmentAnalysisResultList();
			}
		}
		else{
			LogMessageCenter.getLogger().addCriticalErrorMessage("The bicluster algorithm ["+listofbiclusters.getUsedmethod()+"] returned an empty list of biclusters, please try to change algorithm parameters and run it again");
		}
	}
	
	
	/**
	 * Valid.
	 *
	 * @return true, if successful
	 */
	protected boolean valid(){
		boolean isvalid=true;
		try {
			checkOntologyFile();
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error checking ontology file: ", e);
			
		}
		
		try {
			if(mandatoryontologyfile() && ontologyFilePath==null)
				throw new IOException("The Ontology file must be specified");
			if(mandatoryannotationfile() && annotationFilePath==null)
				throw new IOException("The Annotation file must be specified");
			
			isvalid=validForRunning();
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Invalid ontology file: ", e);
			isvalid=false;
		}
		return isvalid;
	}
	
	
	/**
	 * Check ontology file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void checkOntologyFile() throws IOException{
		if(mandatoryontologyfile() && ontologyFilePath==null)
			this.ontologyFilePath=getDefaultOntologyFilePath();
		
	}
	

	
}
