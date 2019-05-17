package jbiclustge.enrichmentanalysistools.clusterprofile.components.props;

import java.io.IOException;
import java.util.Properties;

import org.javatuples.Triplet;

import jbiclustge.annotation.goannotation.components.GOAspect;
import jbiclustge.enrichmentanalysistools.common.OrgDatabaseItem;
import pt.ornrocha.arrays.MTUEnumUtils;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class ClusterProfileEnrichGOPropsContainer extends ClusterProfilerCommonPropertiesContainer{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String GOANOTORGDATABASE="clusterprofiler_organism_annotation_database";
	public static final String GOASPECT="clusterprofiler_GO_aspect";
	
	
	
	
	public void setAnnotationDatabase(String id){
		addPropertyKey(GOANOTORGDATABASE, id,"Name of the annotation package to be used, usually type of org.XX.XX (search at bioconductor website for the organism annotation database)");
	}
	
	public String getAnnotationDatabase() {
		return PropertiesUtilities.getStringPropertyValue(this, GOANOTORGDATABASE, null, getClass());
	}
	
	
	public void setGOAspect(GOAspect id){
		addPropertyKey(GOASPECT, id.getGOType(),"Ontology of interest (BP, MF or CC)");
	}
	
	public void setKeyType(String key){
		addPropertyKey(KEYTYPE, key,"keytype of how the gene identifiers are mapped, usually one of these: "+MTUEnumUtils.getEnumNamesString(OrgDatabaseItem.class, ", "));
	}
	
	public void setKeyType(OrgDatabaseItem key) {
		setKeyType(key.toString());
	}
	
	
	public String getKeyType() {
		return PropertiesUtilities.getStringPropertyValue(this, KEYTYPE, OrgDatabaseItem.PROBEID.toString(), getClass());
	}
	
	
	
	public static ClusterProfileEnrichGOPropsContainer loadFromOtherProperties(Properties props) {
		
		ClusterProfileEnrichGOPropsContainer newprops=new ClusterProfileEnrichGOPropsContainer();
		newprops.setAnnotationDatabase(PropertiesUtilities.getStringPropertyValue(props, GOANOTORGDATABASE, null, ClusterProfileEnrichGOPropsContainer.class));
		newprops.setGOAspect(GOAspect.getAspectFromString(PropertiesUtilities.getStringPropertyValue(props, GOASPECT, GOAspect.BIOLOGICALPROCESS.getGOType(), ClusterProfileEnrichGOPropsContainer.class)));
		newprops.setKeyType(PropertiesUtilities.getStringPropertyValue(props, KEYTYPE, null, ClusterProfileEnrichGOPropsContainer.class));
		newprops.setPvalueAdjustMethod(PropertiesUtilities.getStringPropertyValue(props, ADJUSTMETHOD, null, ClusterProfileEnrichGOPropsContainer.class));
        newprops.setminGSSize(PropertiesUtilities.getIntegerPropertyValue(props, MINGSSIZE, 10, ClusterProfileEnrichGOPropsContainer.class));
		newprops.setmaxGSSize(PropertiesUtilities.getIntegerPropertyValue(props, MAXGSSIZE, 500, ClusterProfileEnrichGOPropsContainer.class));
		newprops.setPvalueCutOff(PropertiesUtilities.getDoublePropertyValue(props, PVALUECUTOFF, 0.05, ClusterProfileEnrichGOPropsContainer.class));
		newprops.setQvalueCutOff(PropertiesUtilities.getDoublePropertyValue(props, QVALUECUTOFF, 0.2, ClusterProfileEnrichGOPropsContainer.class));
		
		return newprops;
	}
	
	
	
	public static ClusterProfileEnrichGOPropsContainer setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		ClusterProfileEnrichGOPropsContainer props=new ClusterProfileEnrichGOPropsContainer();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	
	
	

	public static ClusterProfileEnrichGOPropsContainer getPropertiesInstance() throws IOException{
		
		
		String[] propkeys=new String[]{
				GOANOTORGDATABASE,
				GOASPECT,
				KEYTYPE

		};
		String[] defaultvalues=new String[]{"",GOAspect.BIOLOGICALPROCESS.getGOType(),OrgDatabaseItem.PROBEID.toString()};
		String[] comments=new String[] {
				"Name of the annotation package to be used, usually type of org.XX.XX (search at bioconductor website for the organism annotation database)",
				"Ontology of interest (BP, MF or CC)",
				"keytype of how the gene identifiers are mapped, usually one of these: "+MTUEnumUtils.getEnumNamesString(OrgDatabaseItem.class, ", ")
		};
		
		ClusterProfileEnrichGOPropsContainer props=setupProperties(propkeys, defaultvalues, comments, "Source: http://bioconductor.org/packages/release/bioc/manuals/clusterProfiler/man/clusterProfiler.pdf");
		Triplet<String[], String[], String[]>  commonprops=getCommonProperties();
		appendProperties(props,commonprops.getValue0(), commonprops.getValue1(), commonprops.getValue2());
		
		
		return (ClusterProfileEnrichGOPropsContainer) props;
	}
	
	
	
	
	public static void writeNewClusterProfileEnrichGOPropsContainer(String filepath) throws IOException {
		writeClusterProfileProperties(getPropertiesInstance(), filepath);
	}
	
	
	public static void main(String[] args) throws IOException{
		
		ClusterProfileEnrichGOPropsContainer.writeNewClusterProfileEnrichGOPropsContainer("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/clusterprofile_GO.conf");
	}
	

}
