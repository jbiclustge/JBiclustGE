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
package jbiclustge.enrichmentanalysistools.topgo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.math.R.Rsession;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultsContainer;
import jbiclustge.enrichmentanalysistools.common.GSEAAnalyserType;
import jbiclustge.enrichmentanalysistools.common.RgseaAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGOAlgorithm;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGOAnnotationFunction;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGOMappingType;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGOStatistic;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGoPropertiesContainer;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGopvaluesAdjustMethod;
import jbiclustge.enrichmentanalysistools.topgo.components.TopgoOntology;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.printutils.MTUPrintUtils;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.systemutils.OSystemUtils;


// TODO: Auto-generated Javadoc
/**
 * The Class TopGOEnrichmentAnalyser.
 */
public class TopGOEnrichmentAnalyser extends RgseaAnalyserProcessor{
	
	/** The Constant TOGODATA. */
	private static final String TOGODATA="goindata";
	
	/** The Constant GENEUNIVERSE. */
	private static final String GENEUNIVERSE="totalgenes";
	
	/** The Constant GENENAMELIST. */
	private static final String GENENAMELIST="geneidlist";
	
	/** The Constant GENEID2GO. */
	private static final String GENEID2GO="geneID2GO";
	
	/** The Constant ANALYSEGENES. */
	private static final String ANALYSEGENES="analysegenes";
	
	/** The Constant CURRENTRESULTS. */
	private static final String CURRENTRESULTS="currentresults";
	
	/** The Constant CURRENTRESULTSTABLE. */
	private static final String CURRENTRESULTSTABLE="currentresultstable";
	
	/** The Constant ADJUSTEDPVALUEVECTOR. */
	private static final String ADJUSTEDPVALUEVECTOR="adjustedpvaluevector";
	
	/** The annotationdatabase. */
	private String annotationdatabase;
    
    /** The typeidmapping. */
    private TopGOMappingType typeidmapping;
	
	/** The annfun. */
	private TopGOAnnotationFunction annfun=TopGOAnnotationFunction.annfunGENE2GO;
	
	/** The ontology. */
	private TopgoOntology ontology=TopgoOntology.BP;
	
	/** The algorithm. */
	private TopGOAlgorithm algorithm=TopGOAlgorithm.classic;
	
	/** The statistic. */
	private TopGOStatistic statistic=TopGOStatistic.fisher;
	
	/** The adjustpvaluesmethod. */
	private TopGopvaluesAdjustMethod adjustpvaluesmethod=TopGopvaluesAdjustMethod.NONE;
	
	/** The nodesize. */
	//private double ajustedpvaluesthreshold=0.05;
	private int nodesize=5;
	
	/** The filesep. */
	private String filesep="\n";
	
	/** The idsep. */
	private String idsep=",";
	
	/** The totalgenenamelist. */
	private String[] totalgenenamelist=null;
	
	/** The discardunannotated genes. */
	private boolean discardunannotatedGenes=true;
	
	/** The unannotatedgenes. */
	private HashSet<String> unannotatedgenes;
	
	/** The tempresultlist. */
	private EnrichmentAnalysisResultList tempresultlist=new EnrichmentAnalysisResultList();
	
	/** The objectlabel. */
	private String objectlabel;
	
	/** The usegeneid 2 go. */
	private boolean usegeneid2go=false;
	
	/** The adjustepvalues. */
	private String[] adjustepvalues=null;
	
	/** The initbic. */
	private int initbic=0;
	
	/** The endbic. */
	private int endbic=0;
	
	/** The evalfunction. */
	private String EVALFUNCTION="function(p){ return(p==1)}";
	
	private boolean stopproc=false;
	/**
	 * Instantiates a new top GO enrichment analyser.
	 */
	public TopGOEnrichmentAnalyser(){
		setlabel();
	}
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param annotationfilepath the annotationfilepath
	 */
	public TopGOEnrichmentAnalyser(String annotationfilepath) {
		super(annotationfilepath);
		setlabel();
	}
	

	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param annotationfilepath the annotationfilepath
	 */
	public TopGOEnrichmentAnalyser(BiclusterList listbiclusters, String annotationfilepath) {
		super(annotationfilepath);
		setBiclusteringResultsToAnalyse(listbiclusters);
		setlabel();
	}
	
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param annotationdatabasename the annotationdatabasename
	 * @param typeidsmapping the typeidsmapping
	 */
	public TopGOEnrichmentAnalyser(String annotationdatabasename, TopGOMappingType typeidsmapping){
		this.annotationdatabase=annotationdatabasename;
		this.typeidmapping=typeidsmapping;
		this.annfun=TopGOAnnotationFunction.annfunORG;
		setlabel();
	}
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param annotationdatabasename the annotationdatabasename
	 * @param typeidsmapping the typeidsmapping
	 */
	public TopGOEnrichmentAnalyser(BiclusterList listbiclusters,String annotationdatabasename, TopGOMappingType typeidsmapping){
		this(annotationdatabasename,typeidsmapping);
		setBiclusteringResultsToAnalyse(listbiclusters);
		setlabel();
	}
	
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param annotationdatabasename the annotationdatabasename
	 * @param isORGdatabase the is OR gdatabase
	 * @param typeidsmapping the typeidsmapping
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public TopGOEnrichmentAnalyser(String annotationdatabasename, boolean isORGdatabase, TopGOMappingType typeidsmapping) throws IOException{
		this.annotationdatabase=annotationdatabasename;
		if(isORGdatabase){
			this.annfun=TopGOAnnotationFunction.annfunORG;
			if(typeidsmapping==null)
				throw new IOException("The type of mapping between gene identifiers and Go terms cannot be null");
			else
				this.typeidmapping=typeidsmapping;
		}
		else
			this.annfun=TopGOAnnotationFunction.annfunDB;
		setlabel();
	}
	
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param annotationdatabasename the annotationdatabasename
	 * @param isORGdatabase the is OR gdatabase
	 * @param typeidsmapping the typeidsmapping
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public TopGOEnrichmentAnalyser(BiclusterList listbiclusters, String annotationdatabasename, boolean isORGdatabase, TopGOMappingType typeidsmapping) throws IOException{
		this(annotationdatabasename,isORGdatabase,typeidsmapping);
		setBiclusteringResultsToAnalyse(listbiclusters);
		setlabel();
		
	}
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param settings the settings
	 */
	public TopGOEnrichmentAnalyser(Properties settings){
		super(settings);
		readProperties();
		setlabel();
	}
	
	public TopGOEnrichmentAnalyser(BiclusterList listbiclusters, Properties settings){
		super(listbiclusters, settings);
		readProperties();
		setlabel();
		
	}
	
	
	/**
	 * Instantiates a new top GO enrichment analyser.
	 *
	 * @param annotationdatabase the annotationdatabase
	 * @param annotationFilePath the annotation file path
	 * @param typeidmapping the typeidmapping
	 * @param annfun the annfun
	 * @param ontology the ontology
	 * @param algorithm the algorithm
	 * @param statistic the statistic
	 * @param nodesize the nodesize
	 * @param mtcmethod the mtcmethod
	 * @param filesep the filesep
	 * @param idsep the idsep
	 * @param mapofprobset2geneid the mapofprobset 2 geneid
	 */
	private TopGOEnrichmentAnalyser(String annotationdatabase, 
			String annotationFilePath,
			TopGOMappingType typeidmapping,
			TopGOAnnotationFunction annfun,
			TopgoOntology ontology,
			TopGOAlgorithm algorithm,
			TopGOStatistic statistic,
			int nodesize,
			TopGopvaluesAdjustMethod mtcmethod,
			String filesep,
			String idsep,
			Map<String, String> mapofprobset2geneid){
		
		this.annotationdatabase=annotationdatabase;
		this.annotationFilePath=annotationFilePath;
		this.typeidmapping=typeidmapping;
		this.annfun=annfun;
		this.ontology=ontology;
		this.algorithm=algorithm;
		this.statistic=statistic;
		this.nodesize=nodesize;
		this.adjustpvaluesmethod=mtcmethod;
		this.filesep=filesep;
		this.idsep=idsep;
		this.mapofprobset2geneid=mapofprobset2geneid;
		setlabel();
	}
	
	/**
	 * Setlabel.
	 */
	private void setlabel(){
		this.objectlabel=MTUStringUtils.shortUUID();
	}
	
	
	
	
	

	/**
	 * Sets the ontology type.
	 *
	 * @param ontology the new ontology type
	 */
	public void setOntologyType(TopgoOntology ontology){
		this.ontology=ontology;
	}
	
	/**
	 * Sets the ontology type.
	 *
	 * @param ontology the new ontology type
	 */
	public void setOntologyType(String ontology){
		if(ontology!=null)
		  this.ontology=getOntologyFromString(ontology);
	}
	
	/**
	 * Adds the ontology type.
	 *
	 * @param ontology the ontology
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser addOntologyType(TopgoOntology ontology){
		setOntologyType(ontology);
		return this;
	}
	
	
	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(TopGOAlgorithm algorithm){
		this.algorithm=algorithm;
	}
	
	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(String algorithm){
		if(algorithm!=null)
			this.algorithm=getAlgorithmFromString(algorithm);
	}
	
	/**
	 * Adds the algorithm.
	 *
	 * @param algorithm the algorithm
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser addAlgorithm(TopGOAlgorithm algorithm){
		setAlgorithm(algorithm);
		return this;
	}
	
	
	/**
	 * Sets the statistic method.
	 *
	 * @param stats the new statistic method
	 */
	public void setStatisticMethod(TopGOStatistic stats){
		this.statistic=stats;
	}
	
	/**
	 * Sets the statistic method.
	 *
	 * @param stats the new statistic method
	 */
	public void setStatisticMethod(String stats){
		if(stats!=null)
			this.statistic=getStatisticFromString(stats);
	}
	
	/**
	 * Adds the statistic method.
	 *
	 * @param stats the stats
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser addStatisticMethod(TopGOStatistic stats){
		setStatisticMethod(stats);
		return this;
	}
	
	/**
	 * Sets the GO termsid mapping type.
	 *
	 * @param maptype the new GO termsid mapping type
	 */
	public void setGOTermsidMappingType(TopGOMappingType maptype){
		this.typeidmapping=maptype;
	}
	
	/**
	 * Sets the GO termsid mapping type.
	 *
	 * @param maptype the new GO termsid mapping type
	 */
	public void setGOTermsidMappingType(String maptype){
		if(maptype!=null)
			this.typeidmapping=getMappingTypeFromString(maptype);
	}
	
	/**
	 * Adds the GO termsid mapping type.
	 *
	 * @param maptype the maptype
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser addGOTermsidMappingType(TopGOMappingType maptype){
		setGOTermsidMappingType(maptype);
		return this;
	}
	
	/**
	 * Sets the node size.
	 *
	 * @param size the new node size
	 */
	public void setNodeSize(int size){
		if(size>0)
			this.nodesize=size;
	}
	
	/**
	 * Adds the node size.
	 *
	 * @param size the size
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser addNodeSize(int size){
		this.nodesize=size;
		return this;
	}
	
	/**
	 * Sets the range biclusters results to analyse.
	 *
	 * @param initbic the initbic
	 * @param endbic the endbic
	 */
	public void setRangeBiclustersResultsToAnalyse(int initbic, int endbic){
		this.initbic=initbic;
		this.endbic=endbic;
	}
	
	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#getDefaultOntologyFilePath()
	 */
	@Override
	protected String getDefaultOntologyFilePath() {
		return null;
	}

	/**
	 * Discard unannotated genes.
	 *
	 * @param discard the discard
	 */
	public void discardUnannotatedGenes(boolean discard){
		this.discardunannotatedGenes=discard;
	}
	
	/**
	 * Use unannotated genes.
	 *
	 * @param discard the discard
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser useUnannotatedGenes(boolean discard){
		this.discardunannotatedGenes=discard;
		return this;
	}
	
	/**
	 * Sets the MTC method.
	 *
	 * @param mtc the new MTC method
	 */
	public void setMTCMethod(TopGopvaluesAdjustMethod mtc){
		this.adjustpvaluesmethod=mtc;
	}
	
	/**
	 * Adds the MTC method.
	 *
	 * @param mtc the mtc
	 * @return the top GO enrichment analyser
	 */
	public TopGOEnrichmentAnalyser addMTCMethod(TopGopvaluesAdjustMethod mtc){
		this.adjustpvaluesmethod=mtc;
		return this;
	}
	
	
	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.RgseaAnalyserProcessor#getMainRLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> getMainRLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("DBI"));
		list.add(RPackageInfo.define("topGO", true));
		
		return list;
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.RgseaAnalyserProcessor#validToLoadExtraPackages()
	 */
	@Override
	protected boolean validToLoadExtraPackages() {
        if(annotationFilePath==null && annotationdatabase==null)
		   return false;
        else
        	return true;
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.RgseaAnalyserProcessor#requiredExtraLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> requiredExtraLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		if(annotationdatabase!=null)
			list.add(RPackageInfo.define(annotationdatabase, true));
		return list;
	}
	
	
	
	

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.RgseaAnalyserProcessor#executeRgseaProcess()
	 */
	@Override
	protected boolean executeRgseaProcess() throws Exception {

		changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Configuring Population...");

		if(!stopproc) {
			configureTotalGeneNameList();
			try {
				initTopGOObject();
			} catch (Exception e1) {
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("An error has occured in the topGO Gene enrichment analysis: ", e1);
				changesupport.firePropertyChange(FIREPROPERTYGSEAANALYSERERROR,null,e1.getMessage());
				throw e1;
				//return false;
			}

		}

		if(listofbiclusters!=null && listofbiclusters.size()>0 && !stopproc){
			changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGETASKSTATUS, null, "Analysing Biclusters");
			
			setupRangeBiclustersToAnalyse();
			int n=0;
			int total=endbic-initbic;
			for (int i = initbic; i < endbic && !stopproc; i++) {
				LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Analyzing bicluster "+(i+1)+" of "+listofbiclusters.size());
				changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Processing result "+n+" of "+total);
				
				BiclusterResult res =listofbiclusters.get(i);
				try {
					runGOEnrichment(res);
				} catch (REXPMismatchException e) {
					LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("An error has occured in the topGO Gene enrichment analysis: ", e);
					return false;
				}
				
				float progress = (float)n/total;
	            changesupport.firePropertyChange(FIREPROPERTYGSEACHANGEPROGRESS, null, progress);
				n++;
			}
		}

		if(!stopproc) {
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Gene enrichment analysis were successfully performed to "+ listofbiclusters.getUsedmethod()+" method");
			return true;
		}
		return false;

	}
	
	/**
	 * Setup range biclusters to analyse.
	 */
	private void setupRangeBiclustersToAnalyse(){
		if(endbic==0)
			endbic=listofbiclusters.size();
		else if(endbic>listofbiclusters.size())
			endbic=listofbiclusters.size();
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#readProperties()
	 */
	@Override
	protected void readProperties() {
		
		if(props!=null){
			setOntologyType(PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.ONTOLOGY, null, null));
			setAlgorithm(PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.ALGORITHM, null, null));	
			setStatisticMethod(PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.STATISTIC, null, null));	
			setNodeSize(PropertiesUtilities.getIntegerPropertyValue(props, TopGoPropertiesContainer.NODESIZE, 0, null));
		
			String annotfile=PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.ANNOTATIONFILE, null, null);
			if(annotfile!=null){
		
				this.annotationFilePath=OSystemUtils.validatePath(annotfile);
			
				String columdelim=PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.ANNOTATIONFILECOLUMNDELIMITER, null, null);
				if(columdelim!=null)
					this.filesep=columdelim;
			
				String termdelim=PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.ANNOTATIONFILETERMSDELIMITER, null, null);
				if(termdelim!=null)
					this.idsep=termdelim;
			
				boolean isgo2geneformat=PropertiesUtilities.getBooleanPropertyValue(props, TopGoPropertiesContainer.ANNOTATIONFILEGO2GENEFORMAT, false, null);
				if(isgo2geneformat)
					annfun=TopGOAnnotationFunction.annfunGO2GENE;
				else
					annfun=TopGOAnnotationFunction.annfunGENE2GO;
			}
			
			String anndatabase=PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.ANNOTATIONDATABASE, null, null);
			if(anndatabase!=null && annotfile==null){
				this.annotationdatabase=anndatabase;
			
				boolean isorgdatabase=PropertiesUtilities.getBooleanPropertyValue(props, TopGoPropertiesContainer.ANNOTATIONDATABASEORGTYPE, false, null);
				if(isorgdatabase){
					setGOTermsidMappingType(PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.MAPPINGTYPE, null, null));
					annfun=TopGOAnnotationFunction.annfunORG;
				}
				else
					annfun=TopGOAnnotationFunction.annfunDB;

			}
			String probid2annotationid=PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.MAPPROBEID2GENEID, null, getClass());
		    	if(probid2annotationid!=null){
		    		try {
		    			readMapProbeSetIDToGeneIDFromFile(OSystemUtils.validatePath(probid2annotationid));
		    		} catch (InstantiationException | IllegalAccessException | IOException e) {
		    			e.printStackTrace();
		    		}
		    	}
		 
		    	String mtcmethod=PropertiesUtilities.getStringPropertyValue(props, TopGoPropertiesContainer.MCTMETHOD, "none", getClass());
		    	adjustpvaluesmethod=TopGopvaluesAdjustMethod.getMTCMethodFromString(mtcmethod);
			
		}
		
		
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#buildEnrichmentAnalysisResultList()
	 */
	@Override
	protected EnrichmentAnalysisResultList buildEnrichmentAnalysisResultList() {
		if(mapofprobset2geneid!=null)
			tempresultlist.setMapofprobset2geneidused(mapofprobset2geneid);
		tempresultlist.setUnannotatedGeneNames(unannotatedgenes);
		tempresultlist.setBiclusterlistAssociated(listofbiclusters);
		
		tempresultlist.setWasUsedMCTMethod(!adjustpvaluesmethod.equals(TopGopvaluesAdjustMethod.NONE)?true:false);
		
		return tempresultlist;
	}
	
	/**
	 * Configure total gene name list.
	 * @throws Exception 
	 */
	protected void configureTotalGeneNameList() throws Exception{
		ArrayList<String> tempgenenamelist=listofbiclusters.getAnalysedDataset().getGeneNamesList();
		
		ArrayList<String> temptotalgenelist=new ArrayList<>();
		
		LogMessageCenter.getLogger().addTraceMessage("Input gene List: "+tempgenenamelist);
		LogMessageCenter.getLogger().addTraceMessage("Map probsetids to gene name ids: "+mapofprobset2geneid);
		if(mapofprobset2geneid!=null){
			totalgenenamelist=new String[tempgenenamelist.size()];
			for (int i = 0; i < tempgenenamelist.size(); i++) {
				String id=tempgenenamelist.get(i);
				if(mapofprobset2geneid.containsKey(id))
					temptotalgenelist.add(mapofprobset2geneid.get(id));
				else
					temptotalgenelist.add(id);
			}
		}
		else
			temptotalgenelist.addAll(tempgenenamelist);

		
		ArrayList<String> allowedids=getAllowedGenesInAnnotation();
	    
		if(allowedids.size()>0)
			temptotalgenelist.retainAll(allowedids);

		if(temptotalgenelist.size()>0) {
			
			totalgenenamelist=temptotalgenelist.toArray(new String[tempgenenamelist.size()]);
			setInputGeneList();
		}
		else
			throw new Exception("None of the gene identifiers that are in the annotation that you are using, matches with the  gene identifiers present in the biclusters.\nPlease change the configurations of topGO");

	}
	

	

	protected ArrayList<String> getAllowedGenesInAnnotation(){
		ArrayList<String> allowed=new ArrayList<>();
		String annotgenes="annotationgenes";
		String[] genesinannotation=null;
		if(annfun.equals(TopGOAnnotationFunction.annfunORG) || annfun.equals(TopGOAnnotationFunction.annfunORG2)) {
			rsession.silentlyEval(annotgenes+" <- annFUN.org(\""+ontology.getGOAspect().getGOType()+"\", mapping = \""+annotationdatabase+"\", ID = \""+typeidmapping.toString()+"\")");
			try {
				genesinannotation= (String[]) rsession.cast(rsession.eval("unique(unlist("+annotgenes+"))"));
			} catch (REXPMismatchException e) {
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage(e);
			}
		}
		else if(annfun.equals(TopGOAnnotationFunction.annfunDB) || annfun.equals(TopGOAnnotationFunction.annfunDB2)) {
			rsession.silentlyEval(annotgenes+" <- annFUN.db(\""+ontology.getGOAspect().getGOType()+"\", affyLib=\""+annotationdatabase+"\")");
			try {
				genesinannotation= (String[]) rsession.cast(rsession.eval("unique(unlist("+annotgenes+"))"));
			} catch (REXPMismatchException e) {
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage(e);
			}
		}
		
		if(genesinannotation!=null)
			allowed.addAll(Arrays.asList(genesinannotation));
		
		return allowed;
	}
	
	
	/**
	 * Sets the input gene list.
	 */
	protected void setInputGeneList(){
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Loading Gene name list...");
	
		rsession.set((GENENAMELIST+objectlabel), totalgenenamelist);
		rsession.silentlyEval((GENEUNIVERSE+objectlabel)+"<- rep(0, length("+(GENENAMELIST+objectlabel)+"))");
		rsession.silentlyEval("names("+(GENEUNIVERSE+objectlabel)+") <- "+(GENENAMELIST+objectlabel));
	}
	
	
	/**
	 * Inits the top GO object.
	 *
	 * @throws REXPMismatchException the REXP mismatch exception
	 */
	protected void initTopGOObject() throws Exception{

		setEvalFunction();

		String rcmd=(TOGODATA+objectlabel)+" <- new(\"topGOdata\", description = \"topGO session\", ontology = \""+ontology.getGOAspect().getGOType()+"\", allGenes ="+(GENEUNIVERSE+objectlabel)+", "
				+ "geneSel = evalfunct"+objectlabel+", nodeSize ="+String.valueOf(nodesize)+", "+getInputAnnotationParameters()+")";
		REXP out=rsession.silentlyEval(rcmd);


		if(out==null) {
			if(annfun.equals(TopGOAnnotationFunction.annfunORG) || annfun.equals(TopGOAnnotationFunction.annfunDB)) {

				if(annfun.equals(TopGOAnnotationFunction.annfunORG)) {
					LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Retrying to building topGO object with an alternative function: "+TopGOAnnotationFunction.annfunORG2.toString());
					annfun=TopGOAnnotationFunction.annfunORG2;
				}
				else if(annfun.equals(TopGOAnnotationFunction.annfunDB)) {
					LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Retrying to building topGO object with an alternative function: "+TopGOAnnotationFunction.annfunDB2.toString());
					annfun=TopGOAnnotationFunction.annfunDB2;
				}

				rcmd=(TOGODATA+objectlabel)+" <- new(\"topGOdata\", description = \"topGO session\", ontology = \""+ontology.getGOAspect().getGOType()+"\", allGenes ="+(GENEUNIVERSE+objectlabel)+", "
						+ "geneSel = evalfunct"+objectlabel+", nodeSize ="+String.valueOf(nodesize)+", "+getInputAnnotationParameters()+")";
				out=rsession.silentlyEval(rcmd);

			}
		} 

       if(out!=null) {
		@SuppressWarnings("static-access")
		String[] annotatedgenes= (String[]) rsession.cast(rsession.silentlyEval("genes("+(TOGODATA+objectlabel)+")",false));


		checkUnannotatedGenes(annotatedgenes);
		if(discardunannotatedGenes){
			this.totalgenenamelist=annotatedgenes;
			setInputGeneList();
			rebuildTopGOObject();

		}
       }
       else
    	   throw new Exception("Unable to initialize the topGOdata object that contains all information necessary for the GO analysis. Please try to change the initial configurations of topGO.");

	}
	
	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.RgseaAnalyserProcessor#getRObjectsToRemove()
	 */
	@Override
	protected ArrayList<String> getRObjectsToRemove(){
		
		String[] baseremove = new String[]{(GENENAMELIST+objectlabel),
				(GENEUNIVERSE+objectlabel),
				(TOGODATA+objectlabel),
				("evalfunct"+objectlabel),
				(ANALYSEGENES+objectlabel),
				(CURRENTRESULTS+objectlabel),
				(CURRENTRESULTSTABLE+objectlabel),
				("cgoid"+objectlabel)
		};
		ArrayList<String> remove=new ArrayList<>(Arrays.asList(baseremove));
		
		if(usegeneid2go)
			remove.add(GENEID2GO+objectlabel);
		if(adjustepvalues!=null)
			remove.add(ADJUSTEDPVALUEVECTOR+objectlabel);
		
		return remove;
		//return remove.toArray(new String[remove.size()]);
	}
	
	/**
	 * Check unannotated genes.
	 *
	 * @param annotatedgenes the annotatedgenes
	 */
	private void checkUnannotatedGenes(String[] annotatedgenes){
		//MTUPrintUtils.printArrayofStrings(annotatedgenes);
		unannotatedgenes=new HashSet<>();
		if(annotatedgenes!=null){
			
			ArrayList<String> listannotatedgenes=new ArrayList<String>(Arrays.asList(annotatedgenes));
			for (int i = 0; i < totalgenenamelist.length; i++) {
				String geneid=totalgenenamelist[i];
				if(!listannotatedgenes.contains(geneid))
					unannotatedgenes.add(geneid);
			}
		}
		
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Unannotated Genes: ", unannotatedgenes);
	}
	
	/**
	 * Rebuild top GO object.
	 */
	protected void rebuildTopGOObject(){
		//Rsession s=RConnector.getSession();
		setEvalFunction();
		rsession.silentlyEval((TOGODATA+objectlabel)+" <- new(\"topGOdata\", description = \"topGO session\", ontology = \""+ontology.getGOAspect().getGOType()+"\", allGenes ="+(GENEUNIVERSE+objectlabel)+", "
	     		+ "geneSel = evalfunct"+objectlabel+", nodeSize ="+String.valueOf(nodesize)+", "+getInputAnnotationParameters()+")");
	}
	
	/**
	 * Sets the eval function.
	 */
	protected void setEvalFunction(){
		rsession.silentlyEval(("evalfunct"+objectlabel)+" <- "+EVALFUNCTION);
	
	}
	
	/**
	 * Gets the input annotation parameters.
	 *
	 * @return the input annotation parameters
	 */
	protected String getInputAnnotationParameters(){
		
		StringBuilder str =new StringBuilder();
		
		
		if(annfun.equals(TopGOAnnotationFunction.annfunORG)){
			str.append("annot = "+TopGOAnnotationFunction.annfunORG.toString());
			str.append(",");
			str.append("mapping = \""+annotationdatabase+"\"");
			str.append(", ");
			str.append("ID = \""+typeidmapping.toString()+"\"");
		}
		else if(annfun.equals(TopGOAnnotationFunction.annfunORG2)) {
			rsession.silentlyVoidEval(TopGOAnnotationFunction.annfunORG2.getFunction());
			
			str.append("annot = "+TopGOAnnotationFunction.annfunORG2.toString());
			str.append(",");
			str.append("mapping = \""+annotationdatabase+"\"");
			str.append(", ");
			str.append("ID = \""+typeidmapping.toString()+"\"");
		}
		else if(annfun.equals(TopGOAnnotationFunction.annfunDB)){
			str.append("annot ="+TopGOAnnotationFunction.annfunDB.toString());
			str.append(",");
			str.append("affyLib =\""+annotationdatabase+"\"");
		}
		else if(annfun.equals(TopGOAnnotationFunction.annfunDB2)){
			rsession.silentlyVoidEval(TopGOAnnotationFunction.annfunDB2.getFunction());
			str.append("annot ="+TopGOAnnotationFunction.annfunDB2.toString());
			str.append(",");
			str.append("affyLib =\""+annotationdatabase+"\"");
		}
		else{
			    //if(annfun.equals(TopGOAnnotationFunction.annfunGENE2GO))
			    rsession.silentlyEval((GENEID2GO+objectlabel)+" <- readMappings(file = \""+annotationFilePath+"\", sep = \""+filesep+"\", IDsep = \""+idsep+"\")");
			    if(annfun.equals(TopGOAnnotationFunction.annfunGO2GENE))
					rsession.silentlyEval((GENEID2GO+objectlabel)+" <- inverseList("+(GENEID2GO+objectlabel)+")");
				str.append("annot ="+TopGOAnnotationFunction.annfunGENE2GO.toString());
				str.append(",");
				str.append("gene2GO ="+(GENEID2GO+objectlabel));
				usegeneid2go=true;
		}
		
		
		
		return str.toString();
	}
	
	
	/**
	 * Run GO enrichment.
	 *
	 * @param res the res
	 * @throws REXPMismatchException the REXP mismatch exception
	 */
	@SuppressWarnings("static-access")
	protected void runGOEnrichment(BiclusterResult res) throws REXPMismatchException{
		
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Running topGo GO-term enrichment analysis...");
		ArrayList<String> geneids =res.getGeneNames();
		
		String[] ids=null;
		if(mapofprobset2geneid!=null){
			ids=new String[geneids.size()];
			
			for (int i = 0; i < ids.length; i++) {
				String id=geneids.get(i);
				if(mapofprobset2geneid.containsKey(id))
					ids[i]=mapofprobset2geneid.get(id);
				else
					ids[i]=id;
			}
		}
		else
			ids=geneids.toArray(new String[geneids.size()]);
		
		
		setInputGeneList();

		rsession.set((ANALYSEGENES+objectlabel), ids);
		
		rsession.silentlyEval((GENEUNIVERSE+objectlabel)+"[which(names("+(GENEUNIVERSE+objectlabel)+") %in% "+(ANALYSEGENES+objectlabel)+")]=1");
		
		rsession.silentlyEval((TOGODATA+objectlabel)+" <- updateGenes("+(TOGODATA+objectlabel)+", "+(GENEUNIVERSE+objectlabel)+", evalfunct"+objectlabel+")");
		rsession.silentlyEval((CURRENTRESULTS+objectlabel)+" <- runTest("+(TOGODATA+objectlabel)+", algorithm=\""+algorithm.toString()+"\", statistic=\""+statistic.toString()+"\") ");
		rsession.silentlyEval((CURRENTRESULTSTABLE+objectlabel)+" <- GenTable("+(TOGODATA+objectlabel)+", statspval"+objectlabel+" = "+(CURRENTRESULTS+objectlabel)+", orderBy = \"statspval"+objectlabel+"\", ranksOf = \"statspval"+objectlabel+"\", topNodes = length(score("+(CURRENTRESULTS+objectlabel)+")))");
		
		
		
		String[] goids= (String[]) rsession.cast(rsession.eval((CURRENTRESULTSTABLE+objectlabel)+"[, \"GO.ID\"]"));
		String[] goterms= (String[]) rsession.cast(rsession.eval((CURRENTRESULTSTABLE+objectlabel)+"[, \"Term\"]"));
		int[] genesannotated= rsession.eval((CURRENTRESULTSTABLE+objectlabel)+"[, \"Annotated\"]").asIntegers();
		int[] genessignificant= rsession.eval((CURRENTRESULTSTABLE+objectlabel)+"[, \"Significant\"]").asIntegers();
		String[] pvalues=rsession.eval((CURRENTRESULTSTABLE+objectlabel)+"[, \"statspval"+objectlabel+"\"]").asStrings();
		

        adjustepvalues=null;
    
		if(!adjustpvaluesmethod.equals(TopGopvaluesAdjustMethod.NONE)){	
			adjustepvalues=rsession.eval((ADJUSTEDPVALUEVECTOR+objectlabel)+" <- p.adjust("+(CURRENTRESULTSTABLE+objectlabel)+"[,\"statspval"+objectlabel+"\"], method=\""+adjustpvaluesmethod.toString()+"\")").asStrings();
			
		}
		
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Saving results of GO-term enrichment analysis...");
		EnrichmentAnalysisResultsContainer results=new EnrichmentAnalysisResultsContainer(res);
		for (int i = 0; i < goids.length; i++) {
			String goid=goids[i];
			
			double pvalue=convertPvalueOutput(pvalues[i]);
			if(pvalue<=maxpvalue){
				results.addMapGoTermIDWithGOTermName(goid, goterms[i]);
				results.addGOTermPValue(goid, convertPvalueOutput(pvalues[i]));
				if(adjustepvalues!=null){
					results.addGOTermAdjustedPValue(goid, convertPvalueOutput(adjustepvalues[i]));
				}
				results.addNumberSignificantAnnotatedGenesToGOTerm(goid, genessignificant[i]);
				results.addNumberAnnotatedGenesToGOTerm(goid, genesannotated[i]);
				results.addToMapTerm2GeneAssociation(goid, getGenesInGoTermID(goid, rsession));
			}
		}
		
		tempresultlist.add(results);
	}
	
	
	/**
	 * Convert pvalue output.
	 *
	 * @param pvaluestring the pvaluestring
	 * @return the double
	 */
	private double convertPvalueOutput(String pvaluestring){
		//System.out.println(pvaluestring);
		double pvalue=1.0;
		try {
				pvalue=Double.parseDouble(pvaluestring);
		} catch (Exception e) {
			if(pvaluestring.matches(".*[<>].*")){
				String[] elems=pvaluestring.split("[<>]");
				if(elems[0].isEmpty() && !elems[1].isEmpty())
					pvalue=Double.parseDouble(elems[1].trim());
				else
					pvalue=Double.parseDouble(elems[0]+"e"+elems[1]);
				
				//System.out.println(pvalue);
			}
			else
				LogMessageCenter.getLogger().addCriticalErrorMessage("Error in converting topGO pvalue: "+pvaluestring+". "+e.getMessage());
		}
		
		return pvalue;
	}
	
	/**
	 * Gets the genes in go term ID.
	 *
	 * @param goid the goid
	 * @param s the s
	 * @return the genes in go term ID
	 * @throws REXPMismatchException the REXP mismatch exception
	 */
	@SuppressWarnings("static-access")
	protected String[] getGenesInGoTermID(String goid, Rsession s) throws REXPMismatchException{
		s.set(("cgoid"+objectlabel), goid);
		String[] mapgenes= (String[]) s.cast(s.eval("genesInTerm("+(TOGODATA+objectlabel)+", cgoid"+objectlabel+")[[1]]"));
        if(mapgenes!=null)
        	return mapgenes;
        else
        	return new String[0];
		
	}


	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#mandatoryontologyfile()
	 */
	@Override
	protected boolean mandatoryontologyfile() {
		return false;
	}


	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#mandatoryannotationfile()
	 */
	@Override
	protected boolean mandatoryannotationfile() {
		return false;
	}
	
	/**
	 * Gets the ontology from string.
	 *
	 * @param name the name
	 * @return the ontology from string
	 */
	protected TopgoOntology getOntologyFromString(String name){
		if(name!=null){
			for (TopgoOntology ont : TopgoOntology.values()) {
				if(ont.getGOAspect().getGOType().toLowerCase().equals(name.toLowerCase()))
					return ont;
			}
		}
	   return TopgoOntology.BP;
	}
	
	/**
	 * Gets the algorithm from string.
	 *
	 * @param name the name
	 * @return the algorithm from string
	 */
	protected TopGOAlgorithm getAlgorithmFromString(String name){
		if(name!=null){
			for (TopGOAlgorithm alg : TopGOAlgorithm.values()) {
				if(alg.toString().toLowerCase().equals(name.toLowerCase()))
					return alg;
			}
		}
	   return TopGOAlgorithm.classic;
	}
	
	/**
	 * Gets the statistic from string.
	 *
	 * @param name the name
	 * @return the statistic from string
	 */
	protected TopGOStatistic getStatisticFromString(String name){
		if(name!=null){
			for (TopGOStatistic stats : TopGOStatistic.values()) {
				if(stats.toString().toLowerCase().equals(name.toLowerCase()))
					return stats;
			}
		}
	   return TopGOStatistic.fisher;
	}
	
	/**
	 * Gets the mapping type from string.
	 *
	 * @param name the name
	 * @return the mapping type from string
	 */
	protected static TopGOMappingType getMappingTypeFromString(String name){
		if(name!=null){
			for (TopGOMappingType map : TopGOMappingType.values()) {
				if(map.toString().toLowerCase().equals(name.toLowerCase()))
					return map;
			}
		}
	   return TopGOMappingType.Symbol;
	}
	
	
	/**
	 * New instance.
	 *
	 * @param listbic the listbic
	 * @param properties the properties
	 * @return the top GO enrichment analyser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TopGOEnrichmentAnalyser newInstance(BiclusterList listbic, TopGoPropertiesContainer properties) throws IOException{
		if(properties.getAnnotationDatabase()==null && properties.getAnnotationFile()==null)
			throw new IOException("Please define one of this parameters in properties file: annotation database or user annotation file path");
		else{
			TopGOEnrichmentAnalyser newobj=null;
			if(properties.getAnnotationFile()!=null){
				newobj=new TopGOEnrichmentAnalyser(listbic, properties.getAnnotationFile());
			}
			else {
				if(properties.isAnnotationDatabaseORGType()){
					if(properties.getGOTermMappingType()==null)
						throw new IOException("Please define how the gene identifiers are mapped in the "+TopGoPropertiesContainer.MAPPINGTYPE+" field.");
					else
						newobj=new TopGOEnrichmentAnalyser(listbic, properties.getAnnotationDatabase(), true, getMappingTypeFromString(properties.getGOTermMappingType()));
				}
				else
					newobj=new TopGOEnrichmentAnalyser(listbic, properties.getAnnotationDatabase(), false, null);
					
			}
			
			newobj.setProperties(properties);
			return newobj;
		}
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#copyWorkingInstance()
	 */
	@Override
	public EnrichmentAnalyserProcessor copyWorkingInstance() {
		return new TopGOEnrichmentAnalyser(annotationdatabase, annotationFilePath, typeidmapping, annfun, ontology, algorithm, statistic, nodesize,adjustpvaluesmethod, filesep, idsep,mapofprobset2geneid);
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#getUnannotatedGeneNames()
	 */
	@Override
	public HashSet<String> getUnannotatedGeneNames() {
		return unannotatedgenes;
	}

	@Override
	public GSEAAnalyserType getTypeAnalyserProcessor() {
		return GSEAAnalyserType.TopGO;
	}

	@Override
	protected void stopSubProcesses() {
        stopproc=true;
	}

	

	

}
