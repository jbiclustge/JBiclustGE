package jbiclustge.enrichmentanalysistools.clusterprofile.components.props;

import java.io.IOException;
import java.util.Properties;

import org.javatuples.Triplet;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class ClusterProfileEnrichKeggPropsContainer extends ClusterProfilerCommonPropertiesContainer{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEGGORGANISMID="clusterprofiler_kegg_organismID";

	
	
	
	public void setKeggOrganismID(String id){
		addPropertyKey(KEGGORGANISMID, id,"KEGG supported organism identifier (can be accessed via http://www.genome.jp/kegg/catalog/org_list.html)");
	}
	
	public String getKeggOrganism() {
		return PropertiesUtilities.getStringPropertyValue(this, KEGGORGANISMID, null, getClass());
	}
	
	
	public void setKeyType(String key){
		addPropertyKey(KEYTYPE, key,"one of 'kegg', 'ncbi-geneid', 'ncib-proteinid' and 'uniprot'");
	}
	
	public void setKeyType(ClusterProfileKeyType key) {
		setKeyType(key.toString());
	}
	
	
	public String getKeyType() {
		return PropertiesUtilities.getStringPropertyValue(this, KEYTYPE, "kegg",new String[] {ClusterProfileKeyType.kegg.getName(),ClusterProfileKeyType.ncbigeneid.getName(),ClusterProfileKeyType.ncibproteinid.getName(),ClusterProfileKeyType.uniprot.getName()}, getClass());
	}
	
	
	
	public static ClusterProfileEnrichKeggPropsContainer loadFromOtherProperties(Properties props) {
		
		ClusterProfileEnrichKeggPropsContainer newprops=new ClusterProfileEnrichKeggPropsContainer();
		newprops.setKeggOrganismID(PropertiesUtilities.getStringPropertyValue(props, KEGGORGANISMID, null, ClusterProfileEnrichKeggPropsContainer.class));
		newprops.setKeyType(PropertiesUtilities.getStringPropertyValue(props, KEYTYPE, null, ClusterProfileEnrichKeggPropsContainer.class));
		newprops.setPvalueAdjustMethod(PropertiesUtilities.getStringPropertyValue(props, ADJUSTMETHOD, null, ClusterProfileEnrichKeggPropsContainer.class));
        newprops.setminGSSize(PropertiesUtilities.getIntegerPropertyValue(props, MINGSSIZE, 10, ClusterProfileEnrichKeggPropsContainer.class));
		newprops.setmaxGSSize(PropertiesUtilities.getIntegerPropertyValue(props, MAXGSSIZE, 500, ClusterProfileEnrichKeggPropsContainer.class));
		newprops.setPvalueCutOff(PropertiesUtilities.getDoublePropertyValue(props, PVALUECUTOFF, 0.05, ClusterProfileEnrichKeggPropsContainer.class));
		newprops.setQvalueCutOff(PropertiesUtilities.getDoublePropertyValue(props, QVALUECUTOFF, 0.2, ClusterProfileEnrichKeggPropsContainer.class));
		
		return newprops;
	}
	
	
	
	public static ClusterProfileEnrichKeggPropsContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		ClusterProfileEnrichKeggPropsContainer props=new ClusterProfileEnrichKeggPropsContainer();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	
	
	

	public static ClusterProfileEnrichKeggPropsContainer getPropertiesInstance() throws IOException{
		
		
		String[] propkeys=new String[]{
				KEGGORGANISMID,
				KEYTYPE,

		};
		String[] defaultvalues=new String[]{"",ClusterProfileKeyType.kegg.toString()};
		String[] comments=new String[] {
				"KEGG supported organism identifier (can be accessed via http://www.genome.jp/kegg/catalog/org_list.html)",
				"one of 'kegg', 'ncbi-geneid', 'ncib-proteinid' and 'uniprot'"
		};
		
		ClusterProfileEnrichKeggPropsContainer props=setupProperties(propkeys, defaultvalues, comments, "Source: http://bioconductor.org/packages/release/bioc/manuals/clusterProfiler/man/clusterProfiler.pdf");
		Triplet<String[], String[], String[]>  commonprops=getCommonProperties();
		appendProperties(props,commonprops.getValue0(), commonprops.getValue1(), commonprops.getValue2());
		
		
		return props;
	}
	
	
	
	
	public static void writeNewClusterProfileEnrichKeggProperties(String filepath) throws IOException {
		writeClusterProfileProperties(getPropertiesInstance(), filepath);
	}
	
	
	public static void main(String[] args) throws IOException{
		
		ClusterProfileEnrichKeggPropsContainer.writeNewClusterProfileEnrichKeggProperties("/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/saccharomyces/Ambroset_results/clusterprofile_kegg_2.conf");
	}
	

}
