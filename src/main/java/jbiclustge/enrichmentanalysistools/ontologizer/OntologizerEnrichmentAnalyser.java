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
package jbiclustge.enrichmentanalysistools.ontologizer;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import jbiclustge.datatools.goannotation.components.GOAnnotationGAFList;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.GSEAAnalyserType;
import jbiclustge.enrichmentanalysistools.common.StandardAnnotationFile;
import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerAnnotationType;
import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerCalculationMethod;
import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerMTCMethod;
import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerProcessor;
import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerPropertiesContainer;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.JBiGePropertiesManager;
import ontologizer.OntologizerCore.Arguments;
import ontologizer.calculation.AbstractGOTermProperties;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.ontology.TermID;
import pt.ornrocha.compression.MTUDecompressFile;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.systemutils.OSystemUtils;
import pt.ornrocha.webutils.connectionutils.downloaders.HTTPFileDownloader;

// TODO: Auto-generated Javadoc
/**
 * The Class OntologizerEnrichmentAnalyser.
 */
public class OntologizerEnrichmentAnalyser extends EnrichmentAnalyserProcessor{


	/** The calculation. */
	private OntologizerCalculationMethod calculation=OntologizerCalculationMethod.ParentChildUnion;

	/** The mtc. */
	private OntologizerMTCMethod mtc=OntologizerMTCMethod.None;

	/** The annotationtype. */
	private OntologizerAnnotationType annotationtype=OntologizerAnnotationType.ALL;

	/** The forceupdate. */
	private boolean forceupdate=false;

	/** The gotermidstoignore. */
	private ArrayList<String> gotermidstoignore;

	/** The ontologizerinputarguments. */
	private Arguments ontologizerinputarguments;

	/** The resamplingsteps. */
	private int resamplingsteps=-1;

	/** The sizetolerance. */
	private int sizetolerance=-1;

	/** The filtergenesoffile. */
	private String filtergenesoffile;

	/** The savefileindir. */
	private String savefileindir;

	/** The savedotfiles. */
	private boolean savegoannotations, savegotermtable, saveannotatedgenes, savedotfiles=false;

	/** The currentannotationfilepath. */
	private String currentannotationfilepath;

	/** The configupdated. */
	private boolean configupdated=false;

	/** The resultlist. */
	private EnrichmentAnalysisResultList resultlist;

	/** The unannotated gene names. */
	private HashSet<String> unannotatedGeneNames;


	/** The Constant GOONTOLOGYFILE. */
	public static final String GOONTOLOGYFILE="go.obo";

	/** The Constant ONTOLOGYFILEURLKEY. */
	public static final String ONTOLOGYFILEURLKEY="ontologizer.ontology.file.url";


	private OntologizerProcessor processor=null;

	private boolean stop=false;

	/**
	 * Instantiates a new ontologizer enrichment analyser.
	 */
	public OntologizerEnrichmentAnalyser(){};

	/**
	 * Instantiates a new ontologizer enrichment analyser.
	 *
	 * @param annotationfile the annotationfile
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public OntologizerEnrichmentAnalyser(StandardAnnotationFile annotationfile) throws IOException{
		super(getAnnotationFileFromURL(annotationfile.getDownloadURL()));
	}

	/**
	 * Instantiates a new ontologizer enrichment analyser.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param annotationfile the annotationfile
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public OntologizerEnrichmentAnalyser(BiclusterList listbiclusters, StandardAnnotationFile annotationfile) throws IOException {
		this(annotationfile);
		setBiclusteringResultsToAnalyse(listbiclusters);
	}


	/**
	 * Instantiates a new ontologizer enrichment analyser.
	 *
	 * @param ontologyfilepath the ontologyfilepath
	 * @param annotationfilepath the annotationfilepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public OntologizerEnrichmentAnalyser(String ontologyfilepath, String annotationfilepath) throws IOException{
		super(ontologyfilepath,annotationfilepath);
	}

	/**
	 * Instantiates a new ontologizer enrichment analyser.
	 *
	 * @param settings the settings
	 */
	public OntologizerEnrichmentAnalyser(Properties settings){
		super(settings);
		readProperties();
	}




	public OntologizerEnrichmentAnalyser(BiclusterList listbiclusters, Properties props) {
		super(listbiclusters, props);
		readProperties();
	}

	/**
	 * Gets the mtc.
	 *
	 * @return the mtc
	 */
	public OntologizerMTCMethod getMTC(){
		return mtc;
	}

	/**
	 * Instantiates a new ontologizer enrichment analyser.
	 *
	 * @param ontologyFilePath the ontology file path
	 * @param annotationFilePath the annotation file path
	 * @param currentannotationfilepath the currentannotationfilepath
	 * @param annotationtype the annotationtype
	 * @param populationFilePath the population file path
	 * @param mapofprobset2geneid the mapofprobset 2 geneid
	 * @param calculation the calculation
	 * @param mtc the mtc
	 * @param resamplingsteps the resamplingsteps
	 * @param sizetolerance the sizetolerance
	 * @param filtergenesoffile the filtergenesoffile
	 * @param savefileindir the savefileindir
	 * @param updatedconfiguration the updatedconfiguration
	 */
	private OntologizerEnrichmentAnalyser(String ontologyFilePath, 
			String annotationFilePath,
			String currentannotationfilepath,
			OntologizerAnnotationType annotationtype,
			String populationFilePath,
			Map<String, String> mapofprobset2geneid,
			OntologizerCalculationMethod calculation,
			OntologizerMTCMethod mtc,
			int resamplingsteps,
			int sizetolerance,
			String filtergenesoffile,
			String savefileindir,
			boolean updatedconfiguration){

		this.ontologyFilePath=ontologyFilePath;
		this.annotationFilePath=annotationFilePath;
		this.annotationtype=annotationtype;
		this.currentannotationfilepath=currentannotationfilepath;
		this.populationFilePath=populationFilePath;
		this.mapofprobset2geneid=mapofprobset2geneid;
		this.calculation=calculation;
		this.mtc=mtc;
		this.resamplingsteps=resamplingsteps;
		this.sizetolerance=sizetolerance;
		this.filtergenesoffile=filtergenesoffile;
		this.savefileindir=savefileindir;
		this.configupdated=updatedconfiguration;

	}



	/**
	 * Sets the calculation method.
	 *
	 * @param calculation the new calculation method
	 */
	public void setCalculationMethod(OntologizerCalculationMethod calculation) {
		this.calculation = calculation;
	}

	/**
	 * Sets the calculation method.
	 *
	 * @param calculation the new calculation method
	 */
	public void setCalculationMethod(String calculation) {
		if(calculation!=null)
			this.calculation = getCalculationmethodFromString(calculation);
	}

	/**
	 * Sets the multiple test correction method.
	 *
	 * @param mtc the new multiple test correction method
	 */
	public void setMultipleTestCorrectionMethod(OntologizerMTCMethod mtc) {
		this.mtc = mtc;
	}

	/**
	 * Sets the multiple test correction method.
	 *
	 * @param mtc the new multiple test correction method
	 */
	public void setMultipleTestCorrectionMethod(String mtc) {
		if(mtc!=null)
			this.mtc = getMTCMethodFromString(mtc);
	} 

	/**
	 * Sets the number resampling steps.
	 *
	 * @param resamplingsteps the new number resampling steps
	 */
	public void setNumberResamplingSteps(int resamplingsteps){
		this.resamplingsteps=resamplingsteps;
	}

	/**
	 * Adds the calculation method.
	 *
	 * @param calculationmethod the calculationmethod
	 * @return the ontologizer enrichment analyser
	 */
	public OntologizerEnrichmentAnalyser addCalculationMethod(OntologizerCalculationMethod calculationmethod) {
		this.calculation=calculationmethod;
		return this;
	}

	/**
	 * Adds the multiple testcorrection method.
	 *
	 * @param mtcmethod the mtcmethod
	 * @return the ontologizer enrichment analyser
	 */
	public OntologizerEnrichmentAnalyser addMultipleTestcorrectionMethod(OntologizerMTCMethod mtcmethod) {
		this.mtc=mtcmethod;
		return this;
	}

	/**
	 * Sets the annotation ontology type.
	 *
	 * @param type the new annotation ontology type
	 */
	public void setAnnotationOntologyType(OntologizerAnnotationType type){
		this.annotationtype=type;
	}

	/**
	 * Force update.
	 *
	 * @param forceupdate the forceupdate
	 */
	public void forceUpdate(boolean forceupdate){
		this.forceupdate=forceupdate;
	}

	/**
	 * Append GO term ID to ignore.
	 *
	 * @param gotermid the gotermid
	 */
	public void appendGOTermIDToIgnore(String gotermid){
		if(gotermidstoignore==null)
			gotermidstoignore=new ArrayList<>();
		gotermidstoignore.add(gotermid);
	}

	/**
	 * Sets the list GO term I ds to ignore.
	 *
	 * @param list the new list GO term I ds to ignore
	 */
	public void setListGOTermIDsToIgnore(ArrayList<String> list){
		this.gotermidstoignore=list;
	}

	/**
	 * Sets the list GO term I ds to ignore.
	 *
	 * @param list the new list GO term I ds to ignore
	 */
	public void setListGOTermIDsToIgnore(String...list){
		this.gotermidstoignore=new ArrayList<>(Arrays.asList(list));
	}

	/**
	 * Save results to directory.
	 *
	 * @param dir the dir
	 * @param gotermtable the gotermtable
	 * @param goannotations the goannotations
	 * @param annotatedgenes the annotatedgenes
	 * @param savedotfiles the savedotfiles
	 */
	public void saveResultsToDirectory(String dir, boolean gotermtable, boolean goannotations, boolean annotatedgenes, boolean savedotfiles){
		this.savegoannotations=goannotations;
		this.savegotermtable=gotermtable;
		this.saveannotatedgenes=annotatedgenes;
		this.savedotfiles=savedotfiles;
		this.savefileindir=dir;
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#getUnannotatedGeneNames()
	 */
	public HashSet<String> getUnannotatedGeneNames() {
		return unannotatedGeneNames;
	}


	/**
	 * Update configurations.
	 */
	private void updateConfigurations(){
		configureCurrentAnnotationFile();
		configupdated=true;
	}



	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#runAnalysis()
	 */
	@Override
	protected  void runAnalysis() {

		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Starting ontologizer gene enrichment analysis for results obtained by "+ listofbiclusters.getUsedmethod()+" method, this may take a while, please wait...");
		if(!stop) {
			if(!configupdated)
				updateConfigurations();
			initArguments();

			if(mapofprobset2geneid!=null)
				processor=new OntologizerProcessor(listofbiclusters, ontologizerinputarguments, mapofprobset2geneid,maxpvalue,changesupport);
			else
				processor=new OntologizerProcessor(listofbiclusters, ontologizerinputarguments,maxpvalue,changesupport);

			try {

				processor.execute();

			} catch (Exception e) {
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("An error has occured in the ontologizer Gene enrichment analysis: ", e);
			}
		}

		if(!stop) {
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Gene enrichment analysis were successfully performed to "+ listofbiclusters.getUsedmethod()+" method");

			this.resultlist=processor.getEnrichmentAnalysisResultList();
			this.unannotatedGeneNames=processor.getUnannotatedGeneNames();
			resultlist.setUnannotatedGeneNames(unannotatedGeneNames);
			resultlist.setBiclusterlistAssociated(listofbiclusters);
		}

	}

	/**
	 * Configure current annotation file.
	 */
	protected void configureCurrentAnnotationFile() {
		if(annotationtype.equals(OntologizerAnnotationType.ALL) && gotermidstoignore==null)
			currentannotationfilepath=annotationFilePath;
		else{
			String savepath=FilenameUtils.getFullPath(annotationFilePath);
			String namefile=FilenameUtils.getBaseName(annotationFilePath);
			String ext=FilenameUtils.getExtension(annotationFilePath);
			String newname=null;
			String newfilepath=null;
			if(annotationtype.equals(OntologizerAnnotationType.ALL) && gotermidstoignore!=null){
				String excludedgoterms=null;
				if(ext!=null && !ext.isEmpty())
					newname=namefile+"_excludedgoterms."+ext;
				else
					newname=namefile+"_excludedgoterms";
				newfilepath=FilenameUtils.concat(savepath, newname);

				try {
					GOAnnotationGAFList filter=new GOAnnotationGAFList(annotationFilePath);
					excludedgoterms=filter.getFilteredAnnotationByExcludeGOTermIds(gotermidstoignore.toArray(new String[gotermidstoignore.size()]));
					MTUWriterUtils.writeStringWithFileChannel(excludedgoterms, newfilepath, 0);
					currentannotationfilepath=newfilepath;
				} catch (Exception e) {
					LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error in exclusion of GO terms from annotation file. ", e);
					currentannotationfilepath=annotationFilePath;
				}
			}
			else{

				if(ext!=null && !ext.isEmpty()){
					if(gotermidstoignore!=null)
						newname=namefile+"_"+annotationtype.getGOAspect().getGOType()+"_excludedgoterms."+ext;
					else
						newname=namefile+"_"+annotationtype.getGOAspect().getGOType()+"."+ext;	
				}
				else{
					if(gotermidstoignore!=null)
						newname=namefile+"_"+annotationtype.getGOAspect().getGOType()+"_excludedgoterms";
					else
						newname=namefile+"_"+annotationtype.getGOAspect().getGOType();
				}


				newfilepath=FilenameUtils.concat(savepath, newname);

				File f=new File(newfilepath);
				if(!forceupdate && gotermidstoignore==null && f.exists())
					currentannotationfilepath=newfilepath;
				else if(gotermidstoignore!=null){
					try {
						GOAnnotationGAFList filterlist=new GOAnnotationGAFList(annotationFilePath);
						String filteredexcludedgoterms=filterlist.getFilteredAnnotationByGOAspectExcludeGOTermIds(annotationtype.getGOAspect(), gotermidstoignore.toArray(new String[gotermidstoignore.size()]));
						MTUWriterUtils.writeStringWithFileChannel(filteredexcludedgoterms, newfilepath, 0);	
						currentannotationfilepath=newfilepath;
					} catch (Exception e) {
						LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error in filtering and exclude GO term ids from annotation file. ", e);
						currentannotationfilepath=annotationFilePath;
					}
				}
				else{
					try {
						GOAnnotationGAFList filterlist=new GOAnnotationGAFList(annotationFilePath);
						String filteredgoterms=filterlist.getFilteredAnnotationByGOAspectNoExclusions(annotationtype.getGOAspect());
						MTUWriterUtils.writeStringWithFileChannel(filteredgoterms, newfilepath, 0);
						currentannotationfilepath=newfilepath;

					} catch (Exception e) {
						LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error in filtering GO terms from annotation file. ", e);
						currentannotationfilepath=annotationFilePath;
					}

				}
			}
		}
	}

	/**
	 * Inits the arguments.
	 */
	protected void initArguments(){

		if(ontologizerinputarguments==null){
			ontologizerinputarguments=new Arguments();
			ontologizerinputarguments.goTermsOBOFile=ontologyFilePath;

			ontologizerinputarguments.associationFile=currentannotationfilepath;

			ontologizerinputarguments.calculationName=calculation.toString();

			if(!mtc.equals(OntologizerMTCMethod.None))
				ontologizerinputarguments.correctionName=mtc.toString();
			ontologizerinputarguments.populationFile=populationFilePath;

			if(resamplingsteps>=100 && resamplingsteps<=100000)
				ontologizerinputarguments.resamplingSteps=resamplingsteps;
			if(sizetolerance>=1 && sizetolerance<=100)
				ontologizerinputarguments.sizeTolerance=sizetolerance;
			if(filtergenesoffile!=null)
				ontologizerinputarguments.filterFile=filtergenesoffile;
		}
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#validForRunning()
	 */
	@Override
	protected boolean validForRunning() throws Exception {
		return true;
	}


	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#readProperties()
	 */
	@Override
	protected void readProperties() {
		if(props!=null){
			String ontologyfilepath=PropertiesUtilities.getStringPropertyValue(props,OntologizerPropertiesContainer.ONTOLOGYFILE, null, null);
			if(ontologyfilepath!=null && !ontologyfilepath.isEmpty()){

				setOntologyFilePath(OSystemUtils.validatePath(ontologyfilepath));
			}
			else{
				ontologyfilepath=PropertiesUtilities.getStringPropertyValue(props,OntologizerPropertiesContainer.URLONTOLOGYFILE, null, null);
				if(ontologyfilepath!=null && !ontologyfilepath.isEmpty())
					try {
						setOntologyFilePath(getOntologyFileFromURL(GOONTOLOGYFILE,ontologyfilepath));
					} catch (IOException e) {
						LogMessageCenter.getLogger().addCriticalErrorMessage("Error in loading ontology file from url+["+ontologyfilepath+"]", e);
					}
			}

			String annotationfilepath=PropertiesUtilities.getStringPropertyValue(props,OntologizerPropertiesContainer.ANNOTATIONFILE, null, null);
			if(annotationfilepath!=null && !annotationfilepath.isEmpty()){
				setAnnotationFilePath(OSystemUtils.validatePath(annotationfilepath));
			}
			else{
				annotationfilepath=PropertiesUtilities.getStringPropertyValue(props,OntologizerPropertiesContainer.ORGANISMANNOTATIONFILE, null, null);
				if(annotationfilepath!=null)
					try {
						setAnnotationFilePath(getAnnotationFileFromURL(annotationfilepath));
					} catch (IOException e) {
						LogMessageCenter.getLogger().addCriticalErrorMessage("Error in loading annotation file from url+["+annotationfilepath+"]", e);
					}
				else
					LogMessageCenter.getLogger().addCriticalErrorMessage("Please set an annotation file for gene enrichment analysis ");
			}


			setPopulationFilePath(OSystemUtils.validatePath(PropertiesUtilities.getStringPropertyValue(props,OntologizerPropertiesContainer.POPULATIONFILE, null, null)));

			setCalculationMethod(PropertiesUtilities.getStringPropertyValue(props, OntologizerPropertiesContainer.CALCULATIONMETHOD, null, null));
			setMultipleTestCorrectionMethod(PropertiesUtilities.getStringPropertyValue(props, OntologizerPropertiesContainer.MCTMETHOD, null, null));


			this.resamplingsteps=PropertiesUtilities.getIntegerPropertyValueValidLimits(props, OntologizerPropertiesContainer.RESAMPLINGSTEPS, -1, 100, 100000, true, this.getClass());
			this.sizetolerance=PropertiesUtilities.getIntegerPropertyValueValidLimits(props, OntologizerPropertiesContainer.SIZETOLERANCE, -1, 1, 100, true, this.getClass());
			String annottype=PropertiesUtilities.getStringPropertyValue(props, OntologizerPropertiesContainer.ANNOTATIONTYPE, "all", getClass());
			this.annotationtype=OntologizerAnnotationType.getAnnotationTypeFromString(annottype);

			if(!annotationtype.equals(OntologizerAnnotationType.ALL))
				this.forceupdate=PropertiesUtilities.getBooleanPropertyValue(props, OntologizerPropertiesContainer.ANNOTATIONUPDATE, false, getClass());

			String ignoreterms=PropertiesUtilities.getStringPropertyValue(props, OntologizerPropertiesContainer.IGNOREGOTERMIDS, null, getClass());
			if(ignoreterms!=null && !ignoreterms.isEmpty()){
				String[] elems=ignoreterms.split(";");
				if(elems.length>0)
					gotermidstoignore=new ArrayList<>(Arrays.asList(elems));

			}


			String probid2annotationid=PropertiesUtilities.getStringPropertyValue(props, OntologizerPropertiesContainer.MAPPROBEID2GENEID, null, getClass());
			if(probid2annotationid!=null){
				try {
					readMapProbeSetIDToGeneIDFromFile(OSystemUtils.validatePath(probid2annotationid));
				} catch (InstantiationException | IllegalAccessException | IOException e) {
					e.printStackTrace();
				}
			}
			updateConfigurations();
		}

	}



	/**
	 * Gets the good terms corrected P value.
	 *
	 * @param result the result
	 * @param pvalue the pvalue
	 * @return the good terms corrected P value
	 */
	public static HashSet<TermID> getGoodTermsCorrectedPValue(EnrichedGOTermsResult result, double pvalue){

		HashSet<TermID> out=new HashSet<>();
		Iterator<AbstractGOTermProperties> gotermpropres=result.iterator();

		while (gotermpropres.hasNext()) {
			AbstractGOTermProperties currentGoTerm = (AbstractGOTermProperties) gotermpropres.next();
			TermID currentterm=currentGoTerm.goTerm.getID();
			if(currentGoTerm.p_adjusted<pvalue)
				out.add(currentterm);
		}

		return out;

	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#buildEnrichmentAnalysisResultList()
	 */
	@Override
	protected EnrichmentAnalysisResultList buildEnrichmentAnalysisResultList() {
		return resultlist;
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#getDefaultOntologyFilePath()
	 */
	@Override
	protected String getDefaultOntologyFilePath() {
		try {
			return getOntologyFileFromURL(GOONTOLOGYFILE,getOntologyDefaultURL());
		} catch (IOException e){
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error getting the default Ontology File", e);
		}
		return null;
	}


	/**
	 * Gets the ontology default URL.
	 *
	 * @return the ontology default URL
	 */
	protected String getOntologyDefaultURL() {
		String ontologyurl=(String) JBiGePropertiesManager.getManager().getKeyValue(ONTOLOGYFILEURLKEY);
		if(ontologyurl!=null && !ontologyurl.isEmpty())
			return ontologyurl;
		else
			return "http://purl.obolibrary.org/obo/go.obo";
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#mandatoryontologyfile()
	 */
	@Override
	protected boolean mandatoryontologyfile() {
		return true;
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#mandatoryannotationfile()
	 */
	@Override
	protected boolean mandatoryannotationfile() {
		return true;
	}

	/**
	 * Gets the calculationmethod from string.
	 *
	 * @param name the name
	 * @return the calculationmethod from string
	 */
	protected OntologizerCalculationMethod getCalculationmethodFromString(String name){
		if(name!=null){
			for (OntologizerCalculationMethod method : OntologizerCalculationMethod.values()) {
				if(name.toLowerCase().equals(method.toString().toLowerCase()))
					return method;
			}
		}
		return OntologizerCalculationMethod.ParentChildUnion;
	}

	/**
	 * Gets the MTC method from string.
	 *
	 * @param name the name
	 * @return the MTC method from string
	 */
	protected OntologizerMTCMethod getMTCMethodFromString(String name){
		if(name!=null)
			for (OntologizerMTCMethod method : OntologizerMTCMethod.values()) {
				if(name.toLowerCase().equals(method.toString().toLowerCase()))
					return method;
			}
		return OntologizerMTCMethod.None;
	}


	/**
	 * Gets the annotation file from URL.
	 *
	 * @param url the url
	 * @return the annotation file from URL
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getAnnotationFileFromURL(String url) throws IOException{
		String filepath=FilenameUtils.concat(SystemFolderTools.getEnrichmentAnalysisDirectory(), FilenameUtils.getBaseName(url));
		if(new File(filepath).exists())
			return filepath;
		else{
			String compressedname=FilenameUtils.getName(url);
			String tmpfile=FilenameUtils.concat(SystemFolderTools.getEnrichmentAnalysisDirectory(), compressedname);
			HTTPFileDownloader downloader=new HTTPFileDownloader(url, SystemFolderTools.getEnrichmentAnalysisDirectory());
			downloader.run();
			if(downloader.isSuccessfully()){
				String decompressedfile=MTUDecompressFile.GZip(tmpfile, SystemFolderTools.getEnrichmentAnalysisDirectory());
				File f=new File(tmpfile);
				f.delete();
				if(decompressedfile==null)
					throw new IOException("An error was detected when trying to decompress the Ontology file");
				else
					filepath=decompressedfile;
			}
			else
				throw new IOException("An error was detected when trying to download the Standard Annotation file,  please check if your internet connection it's ON");

		}
		return filepath;
	}

	/**
	 * Gets the ontology file from URL.
	 *
	 * @param ontologyfilename the ontologyfilename
	 * @param ontologyfileurl the ontologyfileurl
	 * @return the ontology file from URL
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getOntologyFileFromURL(String ontologyfilename, String ontologyfileurl) throws IOException{
		String filepath=FilenameUtils.concat(SystemFolderTools.getEnrichmentAnalysisDirectory(),ontologyfilename);
		if(new File(filepath).exists()){
			return filepath;
		}
		else{

			HTTPFileDownloader downloader=new HTTPFileDownloader(ontologyfileurl, SystemFolderTools.getEnrichmentAnalysisDirectory());
			downloader.run();
			if(!new File(downloader.getFilePath()).exists())
				throw new IOException("An error was detected when trying to download the Ontology file,  please check if your internet connection it is ON or if Ontology URL is well defined in the JBiGe.properties file");
			else	
				return downloader.getFilePath();
		}
	}



	/**
	 * New instance.
	 *
	 * @param list the list
	 * @param properties the properties
	 * @return the ontologizer enrichment analyser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static OntologizerEnrichmentAnalyser newInstance(BiclusterList list, OntologizerPropertiesContainer properties) throws IOException{
		OntologizerEnrichmentAnalyser ontenr=new OntologizerEnrichmentAnalyser();
		ontenr.setProperties(properties);
		return ontenr;

	}


	/*	private ArrayList<BiclusterList> splitBiclusterListToSmallBiclusterLists(BiclusterList list){

		ArrayList<BiclusterList> res=new ArrayList<>();
		int n=0;
		BiclusterList splitted=new BiclusterList();
		splitted.setUsedmethod(list.getUsedmethod());
		for (int i = 0; i < list.size(); i++) {
			BiclusterResult singleres=list.get(i);
			if(n==300){
				res.add(splitted);
				splitted=new BiclusterList();
				splitted.setUsedmethod(list.getUsedmethod());
				n=0;
			}
			splitted.add(singleres);
			n++;
		}
		if(splitted.size()>0)
			res.add(splitted);


		return res;
	}*/



	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#copyWorkingInstance()
	 */
	@Override
	public EnrichmentAnalyserProcessor copyWorkingInstance() {
		return new OntologizerEnrichmentAnalyser(this.ontologyFilePath, 
				this.annotationFilePath,
				this.currentannotationfilepath,
				this.annotationtype,
				this.populationFilePath, 
				this.mapofprobset2geneid, 
				this.calculation, 
				this.mtc, 
				this.resamplingsteps, 
				this.sizetolerance, 
				this.filtergenesoffile, 
				this.savefileindir,
				this.configupdated);
	}

	@Override
	public GSEAAnalyserType getTypeAnalyserProcessor() {
		return GSEAAnalyserType.Ontologizer;
	}

	@Override
	public void stopProcess() {
		this.stop=true;
		processor.setStop(true);
	}



}
