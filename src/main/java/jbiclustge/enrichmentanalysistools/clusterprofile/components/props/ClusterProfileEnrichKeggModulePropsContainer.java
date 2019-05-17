package jbiclustge.enrichmentanalysistools.clusterprofile.components.props;

import java.io.IOException;

import org.javatuples.Triplet;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;


public class ClusterProfileEnrichKeggModulePropsContainer extends ClusterProfileEnrichKeggPropsContainer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static ClusterProfileEnrichKeggModulePropsContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		ClusterProfileEnrichKeggModulePropsContainer props=new ClusterProfileEnrichKeggModulePropsContainer();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	

	public static ClusterProfileEnrichKeggModulePropsContainer getPropertiesInstance() throws IOException{
		
		
		String[] propkeys=new String[]{
				KEGGORGANISMID,
				KEYTYPE,

		};
		String[] defaultvalues=new String[]{"",ClusterProfileKeyType.kegg.toString()};
		String[] comments=new String[] {
				"KEGG supported organism identifier (can be accessed via http://www.genome.jp/kegg/catalog/org_list.html)",
				"one of 'kegg', 'ncbi-geneid', 'ncib-proteinid' and 'uniprot'"
		};
		
		ClusterProfileEnrichKeggModulePropsContainer props=setupProperties(propkeys, defaultvalues, comments, "Source: http://bioconductor.org/packages/release/bioc/manuals/clusterProfiler/man/clusterProfiler.pdf");
		Triplet<String[], String[], String[]>  commonprops=getCommonProperties();
		appendProperties(props,commonprops.getValue0(), commonprops.getValue1(), commonprops.getValue2());
		
		
		return props;
	}
	
	
	public static void writeNewClusterProfileEnrichKeggModuleProperties(String filepath) throws IOException {
		writeClusterProfileProperties(getPropertiesInstance(), filepath);
	}
	
	public static void main(String[] args) throws IOException{
		writeNewClusterProfileEnrichKeggModuleProperties("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/clusterprofile_Kegg_module.conf");
	}
	

}
