package jbiclustge.propertiesmodules;

public class PropertyLabels {
	
	
	
	/*
	 * overlap
	 */
	public static final String OVERLAPFILTERING="filter_list_of_bicluster_by_overlap_value";
	public static final String FILTEROVERLAPNUMBERBICS="number_bicluster_To_filter_by_overlap";
	
	/*
	 * Dataset
	 */
	public static final String INPUTDATASETFILEPATH="dataset_filepath";
	public static final String DATASETBYFILENAME="dataset_name";
	
    /*
     * missing values imputation
     */
	public static final String MISSINGDATAIMPUTATIONMETHOD="Use_missing_imputation_method";
	public static final String KMeansImputation="KMeansImputation_settings";
	public static final String KNNImputation="KNNImputation_settings";
	public static final String LLSImputation="LLSImputation_settings";
	public static final String SVDImputation="SVDImputation_settings";
	
	
	
	/*
	 * Configuration
	 */
	public static final String PROFILEFILENAME="profile.conf";
	public static final String ALGORITHMCONFIGURATIONSFOLDER="algorithm_configurations_folder";
	public static final String ALGORITHMSCONFTYPE="algorithms_configuration_type";
	public static final String ALGORITHMSCONFFOLDERNAME="algorithms";
	public static final String EXECUTETAG="_execute";
	public static final String PRIORITYTAG="priority_";
	public static final String CONFIGURATIONFILETAG="_configuration_file";
	public static final String NUMBERRUNSTAG="_number_runs";
	public static final String NUMBERRUNSPREFIX="Run_";
	public static final String ANALYSERESULTINFOLDER="folder_containing_biclustering_results";
	public static final String ALGORITHMCONFIGURATIONSFOLDERBYRUNTIME="load_algorithms_by_run_time_type_folder";
	public static final String RCONCURRENTRUNTYPE="multiple";
	
	
	
	public static final String RUSERLIBPATH="rlibs_path";
	public static final String RUSERPATH="R_path";
	public static final String JBICLUSTPROPSFILENAME="jbiclustge.properties";
	public static final String RDEFAULTLIBSFOLDER="Local_R_packages";
	
	
	
	/*
	 * multithread
	 */
	public static final String SIMULTANEOUSPROCESSES="simultaneous_running_processes";
	public static final String ALGORITHMSCONFTYPESINGLE="single_run";
	public static final String ALGORITHMSCONFTYPEMULTI="multi_run";
	
	/*
	 * GSEA
	 */
	public static final String GSEAPROCESSOR="gsea_processor";
	public static final String GSEACONFIGURATIONFILE="gsea_configuration_file";
	public static final String GSEAOUTPVALUES="gsea_output_pvalues";
	public static final String GSEAUSEADJUSTEDPVALUES="gsea_use_adjusted_pvalues";
	public static final String ENRICHMENTANALYSISCONF="enrichment_analysis.conf";
	public static final String GSEAPROCESSORTOPGOCONFIGNAME="topGO_configuration.conf";
	public static final String GSEAPROCESSORONTOLOGIZERCONFIGNAME="Ontologizer_configuration.conf";
	public static final String GSEAPROCESSORCLUSTERPRLOFILERKEGGCONFIGNAME="Clusterprofiler_kegg_configuration.conf";
	public static final String GSEAPROCESSORCLUSTERPRLOFILERKEGGMODULECONFIGNAME="Clusterprofiler_kegg_module_configuration.conf";
	public static final String GSEAPROCESSORCLUSTERPRLOFILERGOCONFIGNAME="Clusterprofiler_GO_configuration.conf";
	
    /*
     * output
     */
	public static final String OUTPUTDIRECTORY="output_directory";
	public static final String REPORTFORMAT="report_output_format";
	
	/*
	 * plots
	 */
	public static final String MAKEPARALLELCOORD="make_parallel_coordinates_plots";
	public static final String MAKEHEATMAP="make_heat_map_plots";
	
	
	
	// GSEA engines
	
	public static final String ONTOLOGIZER="ontologizer";
	public static final String TOPGO="topgo";
	public static final String CLUSTERPROFILERKEGG="clusterprofilerkegg";
	public static final String CLUSTERPROFILERKEGGMODULE="clusterprofilerkeggmodule";
	public static final String CLUSTERPROFILERGO="clusterprofilergo";
	
	
	public static final String MAPPROBEID2GENEID="file_with_gene_expression_identifiers_to_gene_annotation_identifiers";
	

}
