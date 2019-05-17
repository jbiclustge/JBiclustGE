package jbiclustge.enrichmentanalysistools.clusterprofile.components.props;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.javatuples.Triplet;

import jbiclustge.enrichmentanalysistools.common.OrgDatabaseItem;
import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.propertiesmodules.PropertyLabels;
import pt.ornrocha.propertyutils.EnhancedProperties;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class ClusterProfilerCommonPropertiesContainer extends EnhancedProperties{
	
	

	private static final long serialVersionUID = 1L;
	public static final String KEYTYPE="clusterprofiler_keytype";
	public static final String ADJUSTMETHOD="clusterprofiler_pvalue_adjust_method";
	public static final String MINGSSIZE="clusterprofiler_minimal_size_of_genes_for_testing";
	public static final String MAXGSSIZE="clusterprofiler_maximal_size_of_genes_for_testing";
	public static final String PVALUECUTOFF="clusterprofiler_pvalue_cutoff";
	public static final String QVALUECUTOFF="clusterprofiler_qvalue_cutoff";
	
	
	
	public static final String USEBITRCONVERTER="clusterprofiler_use_bitr_converter";
	public static final String BITRORGANISMDB="clusterprofiler_org_database_name_to_use_in_bitr";
	public static final String CONVERTFROMKEY="clusterprofiler_convert_from_key_identifiers";
	public static final String CONVERTTOKEY="clusterprofiler_convert_to_key_identifiers";
	
	//public static final String QVALUECUTOFF="clusterprofiler_qvalue_cutoff";
	
	protected static final String MAPTXT="File with two columns (column delimiter (;), first column is the gene identifiers of expression dataset and the second column is the gene identifiers used in go annotation file."
			+ " This is an optional file, that must be used if genes described in annotation file have a diferent identifier of genes described in expression dataset.";
	
	
	public void setKeyType(String key){
		addPropertyKey(KEYTYPE, key,"Integer larger or equal to 1. This parameter is used to prune the GO hierarchy from the terms "
				+ "which have less than nodeSize annotated genes (after the true path rule is applied)");
	}
	
	public void setPvalueAdjustMethod(String method){
		addPropertyKey(ADJUSTMETHOD, method,"\"one of \"holm\", \"hochberg\", \"hommel\", \"bonferroni\", \"BH\", \"BY\", \"fdr\", \"none\"");
	}
	
	
	
	public void setPvalueAdjustMethod(pvaluesAdjustMethod pvaladjmethod){
		setPvalueAdjustMethod(pvaladjmethod.toString());
	}
	
	public String getPvalueAdjustMethod() {
		return PropertiesUtilities.getStringPropertyValue(this, ADJUSTMETHOD,pvaluesAdjustMethod.NONE.toString(), 
				new String[] {pvaluesAdjustMethod.BH.toString(),
						pvaluesAdjustMethod.BONFERRONI.toString(),
						pvaluesAdjustMethod.BY.toString(),
						pvaluesAdjustMethod.FDR.toString(),
						pvaluesAdjustMethod.HOCHBERG.toString(),
						pvaluesAdjustMethod.HOLM.toString(),
						pvaluesAdjustMethod.HOMMEL.toString(),
						pvaluesAdjustMethod.NONE.toString()}, getClass());
	}
	
	public void setminGSSize(int size){
		addPropertyKey(MINGSSIZE, String.valueOf(size),"minimal size of genes annotated by Ontology term for testing");
	}
	
	public int getminGSSize() {
		return PropertiesUtilities.getIntegerPropertyValue(this, MINGSSIZE, 10, getClass());
	}
	
	public void setmaxGSSize(int size){
		addPropertyKey(MAXGSSIZE, String.valueOf(size),"maximal size of genes annotated by Ontology term for testing");
	}
	
	public int getmaxGSSize() {
		return PropertiesUtilities.getIntegerPropertyValue(this, MAXGSSIZE, 500, getClass());
	}
	
	public void setPvalueCutOff(double value){
		addPropertyKey(PVALUECUTOFF, String.valueOf(value),"Cutoff value of pvalue");
	}
	
	public double getPvalueCutOff() {
		return PropertiesUtilities.getDoublePropertyValue(this, PVALUECUTOFF, 0.05, getClass());
	}
	
	public void setQvalueCutOff(double value){
		addPropertyKey(QVALUECUTOFF, String.valueOf(value),"Cutoff value of qvalue");
	}
	
	
	public double getQvalueCutOff() {
		return PropertiesUtilities.getDoublePropertyValue(this, QVALUECUTOFF, 0.2, getClass());
	}
	
	public void setBitrConfiguration(String orgdatabase, String fromidtype, String toidtype) {
		
		if(orgdatabase!=null && fromidtype!=null && toidtype!=null) {
			addPropertyKey(USEBITRCONVERTER, Boolean.toString(true), "Use the Biological Id TRanslator present in ClusterProfiler package to convert the gene identifiers, in order to perform the kegg enrichment analysis");
			addPropertyKey(BITRORGANISMDB, orgdatabase, "An annotation database name (usually the annotation package is of type org.XX.XX and can be found in bioconductor website)");
			addPropertyKey(CONVERTFROMKEY, fromidtype, "database map key that match to identifiers of genes of the biclusters or expression dataset (most of the times is the PROBEID)");
			addPropertyKey(CONVERTTOKEY, toidtype, "database map key to convert the identifiers of these genes to be used with kegg database (most of the times is the ENTREZID)");
		}
	}
	
	
	public void setProbidsToAnnotaionIdsFileMap(String filepath) {
		if(filepath!=null && !filepath.isEmpty())
			addPropertyKey(PropertyLabels.MAPPROBEID2GENEID, filepath, MAPTXT);
	}
	
	public static ClusterProfilerCommonPropertiesContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		ClusterProfilerCommonPropertiesContainer props=new ClusterProfilerCommonPropertiesContainer();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	
	
	public static ClusterProfilerCommonPropertiesContainer appendProperties(ClusterProfilerCommonPropertiesContainer props, String[] keys, String[] defaultvalues, String[] comments) throws IOException{
		if(props==null)
			props=new ClusterProfilerCommonPropertiesContainer();
		
		props.addProperties(keys, defaultvalues,comments);
			
		return props;
	}
	
	

	protected static Triplet<String[], String[], String[]> getCommonProperties() throws IOException{
		
		String[] propkeys=new String[]{
				PVALUECUTOFF,
				QVALUECUTOFF,
				ADJUSTMETHOD,
				MINGSSIZE,
				MAXGSSIZE,
				PropertyLabels.MAPPROBEID2GENEID,
				USEBITRCONVERTER,
				BITRORGANISMDB,
				CONVERTFROMKEY,
				CONVERTTOKEY

		};
		String[] defaultvalues=new String[]{"0.05","0.2",pvaluesAdjustMethod.BH.toString(),"10","500","","false","",OrgDatabaseItem.PROBEID.toString(),OrgDatabaseItem.ENTREZID.toString()};
		String[] comments=new String[] {
				"Cutoff value of pvalue",
				"Cutoff value of qvalue",
				"one of \"holm\", \"hochberg\", \"hommel\", \"bonferroni\", \"BH\", \"BY\", \"fdr\", \"none\"",
				"minimal size of genes annotated by Ontology term for testing",
				"maximal size of genes annotated by Ontology term for testing",
				 MAPTXT,
				"Use the Biological Id TRanslator present in ClusterProfiler package to convert the gene identifiers, in order to perform the kegg enrichment analysis",
				"An annotation database name (usually the annotation package is of type org.XX.XX and can be found in bioconductor website)",
				"database map key that match to identifiers of genes of the biclusters or expression dataset (most of the times is the PROBEID)",
				"database map key to convert the identifiers of these genes to be used with kegg database (most of the times is the ENTREZID) "
		};
		
		return new Triplet<String[], String[], String[]>(propkeys, defaultvalues, comments);
		//return ClusterProfilerCommonPropertiesContainer.setupProperties(propkeys, defaultvalues, comments, "Source: http://bioconductor.org/packages/release/bioc/manuals/clusterProfiler/man/clusterProfiler.pdf");
	}
	
	
	public static void writeClusterProfileProperties(ClusterProfilerCommonPropertiesContainer props, String filepath) throws IOException {
		props.store(new FileWriter(new File(filepath)), true);	
	}
	
}
