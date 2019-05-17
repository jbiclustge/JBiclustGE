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
package jbiclustge.propertiesmodules;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.javatuples.Triplet;

import jbiclustge.methods.algorithms.BiclusteringMethod;
import pt.ornrocha.propertyutils.EnhancedPropertiesWithSubGroups;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertiesModules.
 */
public class PropertiesModules {
	
	
	
	/**
	 * Gets the input expression dataset file module.
	 *
	 * @return the input expression dataset file module
	 */
	public static EnhancedPropertiesWithSubGroups getInputExpressionDatasetFileModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Gene Expression Dataset to Analyse", PropertyLabels.INPUTDATASETFILEPATH, "", "");
		return data;
	}
	
	
	/**
	 * Gets the input expression dataset file module with missingvalues imputation.
	 *
	 * @return the input expression dataset file module with missingvalues imputation
	 */
	public static EnhancedPropertiesWithSubGroups getInputExpressionDatasetFileModuleWithMissingvaluesImputation(){
		EnhancedPropertiesWithSubGroups data=getInputExpressionDatasetFileModule();
		data.addPropertyToSubGroupCategory("Data to Analyse", "Missing Value Imputation", PropertyLabels.MISSINGDATAIMPUTATIONMETHOD, "None", "Methods: AverageImputation, KMeansImputation, KNNImputation, LLSImputation, SVDImputation , ZeroValueImputation or None. Check if the chosen method needs the below configuration");
		data.addPropertyToSubGroupCategory("Data to Analyse", "KMeansImputation parameters", PropertyLabels.KMeansImputation, "4;5","If KMeansImputation, set the following settings: number of clusters;number of runs. Example: KMeansImputation_settings=3;4 -> means 3 clusters and 4 runs.");
		data.addPropertyToSubGroupCategory("Data to Analyse", "KNNImputation parameters", PropertyLabels.KNNImputation, "5","If KNNImputation, set the following settings: number of neighbors used for imputation. Example: KNNImputation_settings=3 -> means 3 neighbors used for imputation.");
		data.addPropertyToSubGroupCategory("Data to Analyse", "LLSImputation parameters", PropertyLabels.LLSImputation, "5","If LLSImputation, set the following settings: number of nearest neighbors used for imputation. Example: LLSImputation_settings=3 -> means 3 nearest neighbors used for imputation.");
		data.addPropertyToSubGroupCategory("Data to Analyse", "SVDImputation parameters", PropertyLabels.SVDImputation, "3","If SVDImputation, set the following settings: number of of eigenvectors used for imputation. Example: SVDImputation_settings=3 -> means 3 eigenvectors used for imputation.");
		return data;
	}

	/**
	 * Gets the concorrent processes module.
	 *
	 * @return the concorrent processes module
	 */
	public static EnhancedPropertiesWithSubGroups getConcorrentProcessesModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Concurrent Processes", PropertyLabels.SIMULTANEOUSPROCESSES, "1", "Number of simultaneous processes running in parallel");
		return data;
	}
	
	
	
	/**
	 * Gets the enrichment analysis module.
	 *
	 * @return the enrichment analysis module
	 */
	public static EnhancedPropertiesWithSubGroups getEnrichmentAnalysisModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Enrichment Analysis",PropertyLabels.GSEAPROCESSOR, "ontologizer", "Perform enrichment analysis with: ontologizer, topgo, clusterprofilerkegg, clusterprofilergo, clusterprofilerkeggmodule (default=ontologizer)");
		data.addPropertyToGroupCategory("Enrichment Analysis", PropertyLabels.GSEACONFIGURATIONFILE, "", "Path to file containing the properties to run with GSEA processor");
		data.addPropertyToGroupCategory("Enrichment Analysis", PropertyLabels.GSEAOUTPVALUES, "0.05;0.01;0.005;0.0001;0.00001", "p-values to use in GSEA processor, if more than one value is defined, these must be separated by semi-colon(;)");
		data.addPropertyToGroupCategory("Enrichment Analysis", PropertyLabels.GSEAUSEADJUSTEDPVALUES, "false", "If it is used adjusted p-values in GSEA analysis, this will depends if the processor supports adjusted p-values or the input configurations to GSEA processor.");
		return data;
	}
	
	

	/**
	 * Gets the results reporter module.
	 *
	 * @return the results reporter module
	 */
	public static EnhancedPropertiesWithSubGroups getResultsReporterModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Report Results",PropertyLabels.OUTPUTDIRECTORY, "", "Directory where results will be stored");
		//data.addPropertyToGroupCategory("Report Results",REPORTFORMAT, "", "Write the results obtained in biclustering analysis to csv files or excel file (Options: csv, excel).\n For big expression dataset files it is advised to use csv format");
		return data;
	}
	

	/**
	 * Gets the biclustering methods path module.
	 *
	 * @return the biclustering methods path module
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EnhancedPropertiesWithSubGroups getBiclusteringMethodsPathModule() throws IOException{
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Biclustering Algorithms", PropertyLabels.ALGORITHMCONFIGURATIONSFOLDER, "", "Set the path of the folder that contains the configurations of the biclustering algorithms");
	    return data;
	}
	

	/**
	 * Gets the biclustering methods module.
	 *
	 * @return the biclustering methods module
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EnhancedPropertiesWithSubGroups getBiclusteringMethodsModule() throws IOException{
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		
		String mainmodelulename="Biclustering Algorithms";
		
		for (BiclusteringMethod method : BiclusteringMethod.values()) {
			Triplet<String[], String[], String[]> parameters=getParametersForBiclusteringMethod(method.getAlgorithmID());
			data.addPropertiesToSubGoupCategory(mainmodelulename, method.getAlgorithmID(), parameters.getValue0(), parameters.getValue1(), parameters.getValue2());	
		 }
		return data;
	}
	
	
	/**
	 * Gets the parameters for biclustering method.
	 *
	 * @param name the name
	 * @return the parameters for biclustering method
	 */
	private static Triplet<String[], String[], String[]> getParametersForBiclusteringMethod(String name){
		
		String[] keys=new String[4];
		String[] values=new String[4];
		String[]comments=new String[4];
		
		keys[0]=name+PropertyLabels.EXECUTETAG; keys[1]=name+PropertyLabels.PRIORITYTAG; keys[2]=name+PropertyLabels.CONFIGURATIONFILETAG; keys[3]=name+PropertyLabels.NUMBERRUNSTAG;
		
		values[0]=String.valueOf(true); values[1]="1"; values[2]=""; values[3]="1"; 
		
		comments[0]="Execute biclustering algorithm "+name+", true or false";
		comments[1]="Priority of execution, higher integer value -> run first";
		comments[2]="Configuration of biclustering algorithm, if none is defined, run using default properties";
		comments[3]="Execute biclustering algorithm n number of times";
		
		return new Triplet<String[], String[], String[]>(keys, values, comments);
	}
	

	/**
	 * Gets the files folder to GSEA analysis.
	 *
	 * @return the files folder to GSEA analysis
	 */
	public static EnhancedPropertiesWithSubGroups getFilesFolderToGSEAAnalysis(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Folder of results",PropertyLabels.ANALYSERESULTINFOLDER , "", "Directory where results are stored");
		return data;
	}
	

	
	/**
	 * Gets the parallel coordinates plot properties.
	 *
	 * @return the parallel coordinates plot properties
	 */
	public static EnhancedPropertiesWithSubGroups getParallelCoordinatesPlotProperties(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToSubGroupCategory("Plot bicluster results","Parallel Coordinates",PropertyLabels.MAKEPARALLELCOORD , "false", "Create the parallel coordinates figures of the resulting biclusters (true or false)");
		return data;
	}
	

	/**
	 * Gets the heat map plot properties.
	 *
	 * @return the heat map plot properties
	 */
	public static EnhancedPropertiesWithSubGroups getHeatMapPlotProperties(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToSubGroupCategory("Plot bicluster results","Heat Maps",PropertyLabels.MAKEHEATMAP , "false", "Create the heat map figures of the resulting biclusters (true or false)");
		return data;
	}
	
	
	public static String getBiclusteringMethodConfigurationName(BiclusteringMethod method, Integer confignumber) {
		if(confignumber==null)
			confignumber=1;
		String confname=method.getAlgorithmID().toLowerCase()+"_config_"+confignumber+".conf";
		return confname;
	}
	

	public static EnhancedPropertiesWithSubGroups initializeBiclusteringMethodsRunningTimesModule(LinkedHashMap<String, Integer> confnametoruntimesmap) {
		
		EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		
		for (String confname : confnametoruntimesmap.keySet()) {
			props.addPropertyToSubGroupCategory("Biclustering Algorithms", "Number of runs for each method configuration", confname, String.valueOf(confnametoruntimesmap.get(confname)), "");
		}
		
		return props;
	}
	
	public static void addBiclusteringMethodsRunningTimesOfConfiguration(EnhancedPropertiesWithSubGroups props, String confname, Integer number_runs) {
		if(number_runs==null)
			number_runs=1;
		props.addPropertyToSubGroupCategory("Biclustering Algorithms", "Number of runs for each method configuration", confname, String.valueOf(number_runs), "");
	}
	
	

	public static EnhancedPropertiesWithSubGroups initializeBiclusteringMethodsPriorityModule(LinkedHashMap<String, Integer> confnametoprioritymap) {
		
		EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		
		for (String confname : confnametoprioritymap.keySet()) {
			props.addPropertyToSubGroupCategory("Biclustering Algorithms", "Priority of each method configuration", PropertyLabels.PRIORITYTAG+confname, String.valueOf(confnametoprioritymap.get(confname)), "");
		}
		
		return props;
	}
	
	public static void addBiclusteringMethodConfigurationPriority(EnhancedPropertiesWithSubGroups props, String confname, Integer priority) {
		if(priority==null)
			priority=0;
		props.addPropertyToSubGroupCategory("Biclustering Algorithms", "Priority of each method configuration", PropertyLabels.PRIORITYTAG+confname, String.valueOf(priority), "");
	}
	
	

}
