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

import org.javatuples.Triplet;

import jbiclustge.methods.algorithms.BiclusteringMethod;
import pt.ornrocha.propertyutils.EnhancedPropertiesWithSubGroups;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertiesModules.
 */
public class PropertiesModules {
	
	
	/** The Constant INPUTDATASETFILEPATH. */
	public static final String INPUTDATASETFILEPATH="dataset_filepath";
	
	/** The Constant MISSINGDATAIMPUTATIONMETHOD. */
	public static final String MISSINGDATAIMPUTATIONMETHOD="Use_missing_imputation_method";
	
	/** The Constant KMeansImputation. */
	public static final String KMeansImputation="KMeansImputation_settings";
	
	/** The Constant KNNImputation. */
	public static final String KNNImputation="KNNImputation_settings";
	
	/** The Constant LLSImputation. */
	public static final String LLSImputation="LLSImputation_settings";
	
	/** The Constant SVDImputation. */
	public static final String SVDImputation="SVDImputation_settings";
	
	
	public static final String PROFILEFILENAME="profile.conf";
	
	/**
	 * Gets the input expression dataset file module.
	 *
	 * @return the input expression dataset file module
	 */
	public static EnhancedPropertiesWithSubGroups getInputExpressionDatasetFileModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Gene Expression Dataset to Analyse", INPUTDATASETFILEPATH, "", "");
		return data;
	}
	
	
	/**
	 * Gets the input expression dataset file module with missingvalues imputation.
	 *
	 * @return the input expression dataset file module with missingvalues imputation
	 */
	public static EnhancedPropertiesWithSubGroups getInputExpressionDatasetFileModuleWithMissingvaluesImputation(){
		EnhancedPropertiesWithSubGroups data=getInputExpressionDatasetFileModule();
		data.addPropertyToSubGroupCategory("Data to Analyse", "Missing Value Imputation", MISSINGDATAIMPUTATIONMETHOD, "None", "Methods: AverageImputation, KMeansImputation, KNNImputation, LLSImputation, SVDImputation , ZeroValueImputation or None. Check if the chosen method needs the below configuration");
		data.addPropertyToSubGroupCategory("Data to Analyse", "KMeansImputation parameters", KMeansImputation, "4;5","If KMeansImputation, set the following settings: number of clusters;number of runs. Example: KMeansImputation_settings=3;4 -> means 3 clusters and 4 runs.");
		data.addPropertyToSubGroupCategory("Data to Analyse", "KNNImputation parameters", KNNImputation, "5","If KNNImputation, set the following settings: number of neighbors used for imputation. Example: KNNImputation_settings=3 -> means 3 neighbors used for imputation.");
		data.addPropertyToSubGroupCategory("Data to Analyse", "LLSImputation parameters", LLSImputation, "5","If LLSImputation, set the following settings: number of nearest neighbors used for imputation. Example: LLSImputation_settings=3 -> means 3 nearest neighbors used for imputation.");
		data.addPropertyToSubGroupCategory("Data to Analyse", "SVDImputation parameters", SVDImputation, "3","If SVDImputation, set the following settings: number of of eigenvectors used for imputation. Example: SVDImputation_settings=3 -> means 3 eigenvectors used for imputation.");
		return data;
	}
	
	
	/** The Constant SIMULTANEOUSPROCESSES. */
	public static final String SIMULTANEOUSPROCESSES="simultaneous_running_processes";
	
	/**
	 * Gets the concorrent processes module.
	 *
	 * @return the concorrent processes module
	 */
	public static EnhancedPropertiesWithSubGroups getConcorrentProcessesModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Concurrent Processes", SIMULTANEOUSPROCESSES, "1", "Number of simultaneous processes running in parallel");
		return data;
	}
	
	
	/** The Constant GSEAPROCESSOR. */
	public static final String GSEAPROCESSOR="gsea_processor";
	
	/** The Constant GSEACONFIGURATIONFILE. */
	public static final String GSEACONFIGURATIONFILE="gsea_configuration_file";
	
	/** The Constant GSEAOUTPVALUES. */
	public static final String GSEAOUTPVALUES="gsea_output_pvalues";
	
	/** The Constant GSEAUSEADJUSTEDPVALUES. */
	public static final String GSEAUSEADJUSTEDPVALUES="gsea_use_adjusted_pvalues";
	
	public static final String GSEAPROCESSORTOPGOCONFIGNAME="topGO_configuration.conf";
	public static final String GSEAPROCESSORONTOLOGIZERCONFIGNAME="Ontologizer_configuration.conf";
	
	
	/**
	 * Gets the enrichment analysis module.
	 *
	 * @return the enrichment analysis module
	 */
	public static EnhancedPropertiesWithSubGroups getEnrichmentAnalysisModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Enrichment Analysis",GSEAPROCESSOR, "ontologizer", "Perform enrichment analysis with: ontologizer or topgo (default=ontologizer)");
		data.addPropertyToGroupCategory("Enrichment Analysis", GSEACONFIGURATIONFILE, "", "Path to file containing the properties to run with GSEA processor");
		data.addPropertyToGroupCategory("Enrichment Analysis", GSEAOUTPVALUES, "0.05;0.01", "p-values to use in GSEA processor, if more than one value is defined, these must be separated by semi-colon(;)");
		data.addPropertyToGroupCategory("Enrichment Analysis", GSEAUSEADJUSTEDPVALUES, "false", "If it is used adjusted p-values in GSEA analysis, this will depends if the processor supports adjusted p-values or the input configurations to GSEA processor.");
		return data;
	}
	
	
	/** The Constant OUTPUTDIRECTORY. */
	public static final String OUTPUTDIRECTORY="output_directory";
	
	/** The Constant REPORTFORMAT. */
	public static final String REPORTFORMAT="report_output_format";
	
	/**
	 * Gets the results reporter module.
	 *
	 * @return the results reporter module
	 */
	public static EnhancedPropertiesWithSubGroups getResultsReporterModule(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Report Results",OUTPUTDIRECTORY, "", "Directory where results will be stored");
		//data.addPropertyToGroupCategory("Report Results",REPORTFORMAT, "", "Write the results obtained in biclustering analysis to csv files or excel file (Options: csv, excel).\n For big expression dataset files it is advised to use csv format");
		return data;
	}
	
	/** The Constant ALGORITHMCONFIGURATIONSFOLDER. */
	public static final String ALGORITHMCONFIGURATIONSFOLDER="algorithm_configurations_folder";
	public static final String ALGORITHMSCONFTYPE="algorithms_configuration_type";
	public static final String ALGORITHMSCONFTYPESINGLE="unique";
	public static final String ALGORITHMSCONFTYPEMULTI="multi";
	public static final String ALGORITHMSCONFFOLDERNAME="algorithms";
	/**
	 * Gets the biclustering methods path module.
	 *
	 * @return the biclustering methods path module
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EnhancedPropertiesWithSubGroups getBiclusteringMethodsPathModule() throws IOException{
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Biclustering Algorithms", ALGORITHMCONFIGURATIONSFOLDER, "", "Set the path of the folder that contains the configurations of the biclustering algorithms");
	    return data;
	}
	
	
	
	/** The Constant EXECUTETAG. */
	public static final String EXECUTETAG="_execute";
	
	/** The Constant PRIORITYTAG. */
	public static final String PRIORITYTAG="_priority";
	
	/** The Constant CONFIGURATIONFILETAG. */
	public static final String CONFIGURATIONFILETAG="_configuration_file";
	
	/** The Constant NUMBERRUNSTAG. */
	public static final String NUMBERRUNSTAG="_number_runs";
	
	/** The Constant NUMBERRUNSPREFIX. */
	public static final String NUMBERRUNSPREFIX="Run_";
	
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
			Triplet<String[], String[], String[]> parameters=getParametersForBiclusteringMethod(method.getName());
			data.addPropertiesToSubGoupCategory(mainmodelulename, method.getName(), parameters.getValue0(), parameters.getValue1(), parameters.getValue2());	
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
		
		keys[0]=name+EXECUTETAG; keys[1]=name+PRIORITYTAG; keys[2]=name+CONFIGURATIONFILETAG; keys[3]=name+NUMBERRUNSTAG;
		
		values[0]=String.valueOf(true); values[1]="1"; values[2]=""; values[3]="1"; 
		
		comments[0]="Execute biclustering algorithm "+name+", true or false";
		comments[1]="Priority of execution, higher integer value -> run first";
		comments[2]="Configuration of biclustering algorithm, if none is defined, run using default properties";
		comments[3]="Execute biclustering algorithm n number of times";
		
		return new Triplet<String[], String[], String[]>(keys, values, comments);
	}
	
	/** The Constant ANALYSERESULTINFOLDER. */
	public static final String ANALYSERESULTINFOLDER="folder_containing_biclustering_results";
	
	/**
	 * Gets the files folder to GSEA analysis.
	 *
	 * @return the files folder to GSEA analysis
	 */
	public static EnhancedPropertiesWithSubGroups getFilesFolderToGSEAAnalysis(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToGroupCategory("Folder of results",ANALYSERESULTINFOLDER , "", "Directory where results are stored");
		return data;
	}
	
	/** The Constant MAKEPARALLELCOORD. */
	public static final String MAKEPARALLELCOORD="make_parallel_coordinates_plots";
	
	/**
	 * Gets the parallel coordinates plot properties.
	 *
	 * @return the parallel coordinates plot properties
	 */
	public static EnhancedPropertiesWithSubGroups getParallelCoordinatesPlotProperties(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToSubGroupCategory("Plot bicluster results","Parallel Coordinates",MAKEPARALLELCOORD , "false", "Create the parallel coordinates figures of the resulting biclusters (true or false)");
		return data;
	}
	
	/** The Constant MAKEHEATMAP. */
	public static final String MAKEHEATMAP="make_heat_map_plots";
	
	/**
	 * Gets the heat map plot properties.
	 *
	 * @return the heat map plot properties
	 */
	public static EnhancedPropertiesWithSubGroups getHeatMapPlotProperties(){
		EnhancedPropertiesWithSubGroups data=new EnhancedPropertiesWithSubGroups();
		data.addPropertyToSubGroupCategory("Plot bicluster results","Heat Maps",MAKEHEATMAP , "false", "Create the heat map figures of the resulting biclusters (true or false)");
		return data;
	}
	

}
