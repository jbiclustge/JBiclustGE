package jbiclustge.enrichmentanalysistools.clusterprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.BitrConverterInfoContainer;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.props.ClusterProfileEnrichKeggPropsContainer;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.props.ClusterProfilerCommonPropertiesContainer;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.RgseaAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.results.biclusters.containers.BiclusterList;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.systemutils.OSystemUtils;

public abstract class ClusterProfilerEnrichmentAnalyser extends RgseaAnalyserProcessor{

	
	
	protected boolean stopproc=false;
	protected pvaluesAdjustMethod adjustmethod=pvaluesAdjustMethod.NONE;
	protected double pvalcutoff=0.05;
	protected double qvalcutoff=0.2;
	protected int mingsize=10;
	protected int maxgsize=500;
    protected EnrichmentAnalysisResultList tempresultlist=new EnrichmentAnalysisResultList();
    protected BitrConverterInfoContainer geneidsinfoconverter;
    protected String organnotationdatabase;
/*    protected ArrayList<String> cacherobjecttoremove=new ArrayList<>();*/
	
	public ClusterProfilerEnrichmentAnalyser() {}
	
	
	
	public ClusterProfilerEnrichmentAnalyser(BiclusterList listbiclusters) {
		setBiclusteringResultsToAnalyse(listbiclusters);
	}
	
	
	public ClusterProfilerEnrichmentAnalyser(BiclusterList listbiclusters, Properties settings) {
		super(listbiclusters, settings);
	}
	


	@Override
	protected ArrayList<RPackageInfo> getMainRLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("clusterProfiler", true));
		
		return list;
	}

	
	public ClusterProfilerEnrichmentAnalyser setPvalueAdjustMethod(pvaluesAdjustMethod pvaladj) {
		this.adjustmethod=pvaladj;
		return this;
	}
	
	public ClusterProfilerEnrichmentAnalyser setMinGSSize(int value) {
		this.mingsize=value;
		return this;
	}
	
	public ClusterProfilerEnrichmentAnalyser setmaxGSSize(int value) {
		this.maxgsize=value;
		return this;
	}
	
	
	public ClusterProfilerEnrichmentAnalyser setPvalueCutOFF(double value) {
		this.pvalcutoff=value;
		return this;
	}
	
	public ClusterProfilerEnrichmentAnalyser setQvalueCutOFF(double value) {
		this.qvalcutoff=value;
		return this;
	}
	
	
	public ClusterProfilerEnrichmentAnalyser setBitrConvertInformation(BitrConverterInfoContainer convinfo) {
		this.geneidsinfoconverter=convinfo;
		return this;
	}
	

	@Override
	protected ArrayList<String> getRObjectsToRemove() {
		return null;
	}

	@Override
	protected void stopSubProcesses() {
		stopproc=true;
		
	}
	
	
	
	
	protected void readProperties() {
		
		if(props!=null) {
			adjustmethod=pvaluesAdjustMethod.getMTCMethodFromString(PropertiesUtilities.getStringPropertyValue(props, ClusterProfileEnrichKeggPropsContainer.ADJUSTMETHOD, null, getClass()));
	        mingsize=PropertiesUtilities.getIntegerPropertyValue(props, ClusterProfileEnrichKeggPropsContainer.MINGSSIZE, 10, getClass());
			maxgsize=PropertiesUtilities.getIntegerPropertyValue(props, ClusterProfileEnrichKeggPropsContainer.MAXGSSIZE, 500, getClass());
			pvalcutoff=PropertiesUtilities.getDoublePropertyValue(props, ClusterProfileEnrichKeggPropsContainer.PVALUECUTOFF, 0.05, getClass());
			qvalcutoff=PropertiesUtilities.getDoublePropertyValue(props, ClusterProfileEnrichKeggPropsContainer.QVALUECUTOFF, 0.2, getClass());
			
			//System.out.println("PVALUE: "+pvalcutoff);
			//System.out.println("QVALUE: "+qvalcutoff);
			
			String probid2annotationid=PropertiesUtilities.getStringPropertyValue(props, PropertyLabels.MAPPROBEID2GENEID, null, getClass());
	    	if(probid2annotationid!=null){
	    		try {
	    			readMapProbeSetIDToGeneIDFromFile(OSystemUtils.validatePath(probid2annotationid));
	    		} catch (InstantiationException | IllegalAccessException | IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	
	    	boolean usebitr=PropertiesUtilities.getBooleanPropertyValue(props, ClusterProfilerCommonPropertiesContainer.USEBITRCONVERTER, false, getClass());
	    	
	    	System.out.println("USE BITR: "+usebitr);
	    	if(probid2annotationid==null && usebitr) {
	    		
	    		String orgdb=PropertiesUtilities.getStringPropertyValue(props, ClusterProfilerCommonPropertiesContainer.BITRORGANISMDB, null, getClass());
	    		String fromtype=PropertiesUtilities.getStringPropertyValue(props, ClusterProfilerCommonPropertiesContainer.CONVERTFROMKEY, null, getClass());
	    		String totype=PropertiesUtilities.getStringPropertyValue(props, ClusterProfilerCommonPropertiesContainer.CONVERTTOKEY, null, getClass());
	    		
	    		if(orgdb!=null && fromtype!=null && totype!=null) 
	    			geneidsinfoconverter=new BitrConverterInfoContainer(orgdb, fromtype, totype, false);

	    		
	    	}
		}
		
		
	}
	
	
	
	


	@Override
	protected EnrichmentAnalysisResultList buildEnrichmentAnalysisResultList() {
		if(mapofprobset2geneid!=null)
			tempresultlist.setMapofprobset2geneidused(mapofprobset2geneid);
		tempresultlist.setUnannotatedGeneNames(getUnannotatedGeneNames());
		tempresultlist.setBiclusterlistAssociated(listofbiclusters);
		
		tempresultlist.setWasUsedMCTMethod(!adjustmethod.equals(pvaluesAdjustMethod.NONE)?true:false);
		
		return tempresultlist;
	}




}
