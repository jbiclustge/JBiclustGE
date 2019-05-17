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
package jbiclustge.enrichmentanalysistools.ontologizer.components;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultsContainer;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import ontologizer.OntologizerCore.Arguments;
import ontologizer.StudySetResultList;
import ontologizer.association.AnnotationContext;
import ontologizer.association.Association;
import ontologizer.association.AssociationContainer;
import ontologizer.association.AssociationParser;
import ontologizer.association.Gene2Associations;
import ontologizer.calculation.AbstractGOTermProperties;
import ontologizer.calculation.CalculationRegistry;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.calculation.ICalculation;
import ontologizer.calculation.b2g.B2GParam;
import ontologizer.calculation.b2g.Bayes2GOCalculation;
import ontologizer.enumeration.TermEnumerator;
import ontologizer.enumeration.TermEnumerator.TermAnnotatedGenes;
import ontologizer.filter.GeneFilter;
import ontologizer.ontology.IParserInput;
import ontologizer.ontology.OBOParser;
import ontologizer.ontology.OBOParserFileInput;
import ontologizer.ontology.Ontology;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermContainer;
import ontologizer.ontology.TermID;
import ontologizer.set.PopulationSet;
import ontologizer.set.StudySet;
import ontologizer.set.StudySetFactory;
import ontologizer.set.StudySetList;
import ontologizer.statistics.AbstractTestCorrection;
import ontologizer.statistics.IResampling;
import ontologizer.statistics.TestCorrectionRegistry;
import ontologizer.types.ByteString;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.MTULogLevel;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class OntologizerProcessor.
 */
public class OntologizerProcessor {
	
	
	/** The listbiclusters. */
	private BiclusterList listbiclusters;
	
	/** The ontologizerarguments. */
	private Arguments ontologizerarguments;
	
	/** The map between bicluster genes and annotation genes. */
	private Map<String, String> mapBetweenBiclusterGenesAndAnnotationGenes;
	
	/** The go terms. */
	private TermContainer goTerms;
	
	/** The go graph. */
	private Ontology goGraph;
	
	/** The populationset. */
	private PopulationSet populationset;
	
	/** The study set list. */
	private StudySetList studySetList;
	
	/** The genefilter. */
	private GeneFilter genefilter;
	
	/** The assocparser. */
	private AssociationParser assocparser;
	
	/** The annotationinput. */
	private IParserInput annotationinput;
	
	/** The go associations. */
	private AssociationContainer goAssociations;
	
	/** The calculation. */
	protected ICalculation calculation;
	
	/** The test correction. */
	protected AbstractTestCorrection testCorrection;
	
	/** The listofresults. */
	private StudySetResultList listofresults;
	
	/** The unannotated gene names. */
	private HashSet<String> unannotatedGeneNames;
	
	/** The genenamevssynonym. */
	private HashMap<String, String> genenamevssynonym=new HashMap<>();
	
	/** The enrichmentanalysislist. */
	//private EnrichmentAnalysisResultsContainer enrichmentanalysis;
	private EnrichmentAnalysisResultList enrichmentanalysislist;
	
	/** The maxpvalue. */
	private double maxpvalue=0.05;
	
	private PropertyChangeSupport changesupport=null;
	
	private boolean stop=false;
	
	/**
	 * Instantiates a new ontologizer processor.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param ontologizerargs the ontologizerargs
	 * @param maxpvalue the maxpvalue
	 */
	public OntologizerProcessor(BiclusterList listbiclusters, Arguments ontologizerargs, double maxpvalue, PropertyChangeSupport changesupport) {
		this.listbiclusters=listbiclusters;
		this.ontologizerarguments=ontologizerargs;
		this.maxpvalue=maxpvalue;
		if(changesupport==null)
			this.changesupport=new PropertyChangeSupport(this);
		else
			this.changesupport=changesupport;
	}
	
	
	/**
	 * Instantiates a new ontologizer processor.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param ontologizerargs the ontologizerargs
	 * @param probsetid2geneids the probsetid 2 geneids
	 * @param maxpvalue the maxpvalue
	 */
	public OntologizerProcessor(BiclusterList listbiclusters, Arguments ontologizerargs, Map<String,String> probsetid2geneids, double maxpvalue, PropertyChangeSupport changesupport) {
		this.listbiclusters=listbiclusters;
		this.ontologizerarguments=ontologizerargs;
		this.mapBetweenBiclusterGenesAndAnnotationGenes=probsetid2geneids;
		this.maxpvalue=maxpvalue;
		if(changesupport==null)
			this.changesupport=new PropertyChangeSupport(this);
		else
			this.changesupport=changesupport;
	}
	
	
	
	 public void setPropertyChangeSupport(PropertyChangeSupport changesupport) {
		this.changesupport = changesupport;
	}


	/**
 	 * Sets the map of bicluster ids and ontology ids.
 	 *
 	 * @param map the map
 	 */
 	public void setMapOfBiclusterIdsAndOntologyIds(Map<String,String> map){
	    	this.mapBetweenBiclusterGenesAndAnnotationGenes=map;
	    }
	 
	
	 
	 /**
 	 * Gets the enrichment analysis result list.
 	 *
 	 * @return the enrichment analysis result list
 	 */
 	public EnrichmentAnalysisResultList getEnrichmentAnalysisResultList() {
		return enrichmentanalysislist;
	}
	 
	 
    

	public PropertyChangeSupport getPropertyChangesupport() {
		return changesupport;
	}


	/**
	 * Gets the unannotated gene names.
	 *
	 * @return the unannotated gene names
	 */
	public HashSet<String> getUnannotatedGeneNames() {
		return unannotatedGeneNames;
	}

	

	public void setStop(boolean stop) {
		this.stop = stop;
	}


	/**
	 * Execute.
	 *
	 * @throws Exception the exception
	 */
	public synchronized void execute() throws Exception{
		
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Annotation File: "+ontologizerarguments.associationFile);
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Correction Method: "+ontologizerarguments.correctionName);
		changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGETASKSTATUS, null, "Configuring Ontologizer Process");
		 
		if(!stop)
			 configureProcess();
		listofresults=new StudySetResultList();
		 
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Total number biclusters to Processing: "+listbiclusters.size()+" for "+listbiclusters.getUsedmethod()+" method");
		changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGEPROGRESS, null, (float)0);
		 changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGETASKSTATUS, null, "Analysing Biclusters"); 
		
		 int n=1;
		 if(!stop)
		 for (StudySet studySet : studySetList){
			 if(stop)
				 break;
			
			 LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Processing results of bicluster "+n+" of "+listbiclusters.getUsedmethod()+" method");	 
			 changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Processing result "+n+" of "+studySetList.size());
			 
			 listofresults.addStudySetResult(calculation.calculateStudySet(goGraph,goAssociations,populationset,studySet,testCorrection));
             studySet.resetCounterAndEnumerator();
             
             float progress = (float)n/(float)studySetList.size();
             changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGEPROGRESS, null, progress);
             
             n++;
			}
		 if(!stop)
			 processResults();
	 }
	 
	 
	 /**
 	 * Configure process.
 	 *
 	 * @throws Exception the exception
 	 */
 	protected void configureProcess() throws Exception{
		 
		 setOntologizerParameters();
		 
		 OBOParser oboParser = new OBOParser(new OBOParserFileInput(ontologizerarguments.goTermsOBOFile));
		 String parsetag =oboParser.doParse();
		 LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage(parsetag);
		 
		 goTerms=new TermContainer(oboParser.getTermMap(), oboParser.getFormatVersion(), oboParser.getDate());
		 LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage(goTerms.toString());
		 goGraph = Ontology.create(goTerms);
		 annotationinput=new OBOParserFileInput(ontologizerarguments.associationFile);
		 
		 
		 if(ontologizerarguments.populationFile!=null && !ontologizerarguments.populationFile.isEmpty())
			   this.populationset=(PopulationSet)StudySetFactory.createFromFile(new File(ontologizerarguments.populationFile), true);
			else{
				if(this.populationset==null)
					setPopulationFromBicluster();
			}
		 
		 
		 convertBiclusterResultToStudyList();
		 
		 if (ontologizerarguments.filterFile != null){
				genefilter = new GeneFilter(new File(ontologizerarguments.filterFile));	
				
				populationset.applyFilter(genefilter);

				for (StudySet studySet : studySetList)
					studySet.applyFilter(genefilter);
		  }
		 

		 for (ByteString geneName : studySetList.getGeneSet())
			{
				if (!populationset.contains(geneName))
					populationset.addGene(geneName,"");
			}

		 checkAssociation();
		// System.out.println(assocparser.getAssociations());
		// System.out.println(assocparser.getAnnotationMapping().getDbObjectID2Symbol());
		 AnnotationContext ac=assocparser.getAnnotationMapping();
		 if(ac==null)
			 ac=new AnnotationContext(assocparser.getListOfObjectSymbols(), new HashMap<ByteString,ByteString>(), new HashMap<ByteString,ByteString>());
		 this.goAssociations=new AssociationContainer(assocparser.getAssociations(), ac);
		 checkPopulationUnannotatedGeneNames();
		 findDuplicates();
	 }
	 
	 
	 
	 
	 /**
 	 * Sets the ontologizer parameters.
 	 */
 	private void setOntologizerParameters(){
		 calculation=getCalculationMethodInstance(ontologizerarguments.calculationName);
		 
		 Integer resamplingsteps=null;
		 Integer sizetolerance=null;
		 if(ontologizerarguments.resamplingSteps>-1)
				resamplingsteps=ontologizerarguments.resamplingSteps;
			if(ontologizerarguments.sizeTolerance>-1)
				sizetolerance=ontologizerarguments.sizeTolerance;

		 testCorrection=getCorrectionMethodInstance(ontologizerarguments.correctionName,resamplingsteps, sizetolerance);
	 }
	 
	 
	 
	 /**
 	 * Sets the population from bicluster.
 	 *
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	protected void setPopulationFromBicluster() throws IOException{

			ArrayList<String> allgenes=listbiclusters.getAnalysedDataset().getGeneNamesList();
			ArrayList<String> populationgenes=null;
			
			if(mapBetweenBiclusterGenesAndAnnotationGenes!=null){
				populationgenes=new ArrayList<>();
				for (String geneid : allgenes) {
					if(mapBetweenBiclusterGenesAndAnnotationGenes.containsKey(geneid))
						populationgenes.add(mapBetweenBiclusterGenesAndAnnotationGenes.get(geneid));
					else
						populationgenes.add(geneid);
				}
			}
			else
				populationgenes=allgenes;
			
			String[] items=new String[populationgenes.size()];
			items=populationgenes.toArray(items);
			
			this.populationset=(PopulationSet) StudySetFactory.createFromArray(items, true);
			//MTUWriterUtils.writeListToFile("allgenespop.txt", allgenes);
		}
	 
	 
	 
	 /**
 	 * Convert bicluster result to study list.
 	 *
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	protected void convertBiclusterResultToStudyList() throws IOException{
			
			studySetList=new StudySetList("Study_Bicluster");
			
			for (int i = 0; i < listbiclusters.size(); i++) {
				
				BiclusterResult bicres=listbiclusters.get(i);
				ArrayList<String> geneids=null;
				if(mapBetweenBiclusterGenesAndAnnotationGenes!=null)
					geneids=bicres.getTransformedGeneNamesToMappedGOAnnotations(mapBetweenBiclusterGenesAndAnnotationGenes);
				else
					geneids=bicres.getGeneNames();
				
				StudySet geneset=new StudySet("geneSet_of_bicluster_"+i);
				
				if(geneids!=null && geneids.size()>0){
					
					for (int j = 0; j < geneids.size(); j++) {
						geneset.addGene(new ByteString(geneids.get(j)), "gene name "+geneids.get(j));
					}
					if(LogMessageCenter.getLogger().getLogLevel().equals(MTULogLevel.TRACE))
						LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage(getGeneStudySet(geneset, i));
				}
				
				studySetList.addStudySet(geneset);
			}
			
		}
	 
	 
	 /**
 	 * Check association.
 	 *
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	protected void checkAssociation() throws IOException{
		 this.assocparser=new AssociationParser(annotationinput, goTerms, populationset.getAllGeneNames());
	 }
	 
	 
	 /**
 	 * Find duplicates.
 	 */
 	protected void findDuplicates(){
		   populationset.filterOutDuplicateGenes(goAssociations);
			for (StudySet study : studySetList)
				study.filterOutDuplicateGenes(goAssociations);

			if (ontologizerarguments.filterOutUnannotatedGenes)
			{
				/* Filter out genes within the study without any annotations */
				for (StudySet study : studySetList)
					study.filterOutAssociationlessGenes(goAssociations);

				/* Filter out genes within the population which doesn't have an annotation */
				populationset.filterOutAssociationlessGenes(goAssociations);
			}
	 }
	 
	 /**
 	 * Gets the gene study set.
 	 *
 	 * @param set the set
 	 * @param numberbicl the numberbicl
 	 * @return the gene study set
 	 */
 	protected String getGeneStudySet(StudySet set, int numberbicl){
			
			ByteString genes[]=set.getGenes();
			StringBuilder str=new StringBuilder();
			str.append("Bicluster "+numberbicl+"\t");
			
			for (int i = 0; i < genes.length; i++) {
				str.append(genes[i]);
				if(i<(genes.length)-1)
					str.append(";");
					
			}
			str.append("\n");
			return str.toString();
		}
	 
	 /**
 	 * Process results.
 	 */
 	private void processResults(){
 		 changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGETASKSTATUS, null, "Processing Ontologizer Results");
		 this.enrichmentanalysislist=new EnrichmentAnalysisResultList();
		 
		 enrichmentanalysislist.setWasUsedMCTMethod((ontologizerarguments.correctionName==null)?false:true);
			 
		 enrichmentanalysislist.setMapofprobset2geneidused(mapBetweenBiclusterGenesAndAnnotationGenes);
		 ArrayList<EnrichedGOTermsResult> ontologresults=listofresults.getStudySetResults();
		 
		 for (int i = 0; i < ontologresults.size(); i++) {
			LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Processing results of bicluster "+i);
			changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Processing results of bicluster "+(i+1));
			
			EnrichedGOTermsResult ontres=ontologresults.get(i);
			enrichmentanalysislist.add(extractGOTermsInformation(i, ontres)); 
		 }
		 ontologresults=null;
	 }
	 
	 /**
 	 * Reset processor.
 	 */
 	public void resetProcessor(){
		 listofresults=null;
		 goAssociations=null;
		 goGraph=null;
		 listbiclusters=null;
		 genenamevssynonym=new HashMap<>();
	 }
	 
	 /**
 	 * Extract GO terms information.
 	 *
 	 * @param ncluster the ncluster
 	 * @param ontres the ontres
 	 * @return the enrichment analysis results container
 	 */
 	private EnrichmentAnalysisResultsContainer extractGOTermsInformation(int ncluster, EnrichedGOTermsResult ontres){
		 
		 EnrichmentAnalysisResultsContainer container=new EnrichmentAnalysisResultsContainer(listbiclusters.get(ncluster));
		// System.out.println("SYNO: "+genenamevssynonym);
		 
		 Iterator<AbstractGOTermProperties> termprops=ontres.iterator();
		 
		 StudySet stuset=ontres.getStudySet();
		 TermEnumerator enumerator=stuset.enumerateTerms(goGraph, goAssociations);
		 
		 while (termprops.hasNext()) {
			 
			 
			 
			 AbstractGOTermProperties termproperty =termprops.next();
			 Term term=termproperty.goTerm;
			 
			 double pvalue=termproperty.p;
			 
			 if(pvalue<=maxpvalue){
			 
				 TermID termid=term.getID();
				 String termname=new String(term.getName().toString());
				 String gotermid=new String(term.getIDAsString());
			 
				 container.addMapTermIDWithTermName(gotermid, termname);
				 TermAnnotatedGenes genes = enumerator.getAnnotatedGenes(termid);
				 

				 for (ByteString gene : genes.totalAnnotated) {
					     String genename=null;
					     if(genenamevssynonym.size()>0 && genenamevssynonym.containsKey(gene.toString())){
					    	genename=genenamevssynonym.get(gene.toString());
					     }
					     else
					    	 genename=gene.toString();
					     
						 container.addToMapTerm2GeneAssociation(gotermid, genename);
				 }
			 
				 int annotatedstudygenes=termproperty.annotatedStudyGenes;
				 container.addNumberAnnotatedGenesToGOTerm(gotermid, annotatedstudygenes);
			 
				 int annotatedpopulationgenes=termproperty.annotatedPopulationGenes;
				 container.addNumberPopulationGenesToTermID(gotermid,  annotatedpopulationgenes);
			
			 
				 double p_adjusted=termproperty.p_adjusted;
				 container.addPValues(gotermid, pvalue, p_adjusted); 
			 }
		 }
		 
		 //set to null in order to allow the garbage collector to free the enumerator
		 stuset.resetCounterAndEnumerator();
		// enumerator=null;
		 
		 return container;
	 }
	 
	 
	 
	 
	 /**
 	 * Check population unannotated gene names.
 	 */
 	private void checkPopulationUnannotatedGeneNames(){
		 HashSet<ByteString> geneinpopulation=populationset.getAllGeneNames();
		
		 unannotatedGeneNames=new HashSet<>();

		 for (ByteString geneid : geneinpopulation) {
			 Gene2Associations gene2Association = goAssociations.get(geneid);
			 /*Iterator<Association> ass=gene2Association.iterator();
			 
			 for (Association association : gene2Association) {
				System.out.println("Synm: "+ geneid.toString() +" GENE NAME: "+ association.getObjectSymbol());
			 }*/
			 if(gene2Association==null)
				 unannotatedGeneNames.add(geneid.toString());
			 else
				 mapIfSynonymOfGene(gene2Association, geneid.toString());
		}
	   LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Unannotated genes: ", unannotatedGeneNames);
	 }
	 
	 
	 /**
 	 * Map if synonym of gene.
 	 *
 	 * @param gene2Association the gene 2 association
 	 * @param genename the genename
 	 */
 	private void mapIfSynonymOfGene(Gene2Associations gene2Association, String genename){
		 
		// Iterator<Association> associations=gene2Association.iterator();
		 
		 String annotationgenename=null;
		 
		/* while (associations.hasNext()) {
	    	 Association a=associations.next();
	    	 if(a.getObjectSymbol())
		 }
		 */
		 for (Association association : gene2Association) {
			 annotationgenename=association.getObjectSymbol().toString();
		 }
		 
		 if(!annotationgenename.equals(genename))
			 genenamevssynonym.put(annotationgenename, genename);
		 
	 }
	 
	 
	 /**
 	 * Gets the calculation method instance.
 	 *
 	 * @param name the name
 	 * @return the calculation method instance
 	 */
 	public static ICalculation getCalculationMethodInstance(String name){
			ICalculation calculation=null;
			calculation = CalculationRegistry.getCalculationByName(name);
			if (calculation == null)
				calculation = CalculationRegistry.getDefault();
			if (calculation instanceof Bayes2GOCalculation) {
				Bayes2GOCalculation b2g = (Bayes2GOCalculation) calculation;
				b2g.setAlpha(B2GParam.Type.MCMC);
				b2g.setBeta(B2GParam.Type.MCMC);
				b2g.setExpectedNumber(B2GParam.Type.MCMC);
				b2g.setMcmcSteps(1000000);
			}
			return calculation;
		}
	 
	 /**
 	 * Gets the correction method instance.
 	 *
 	 * @param name the name
 	 * @param resamplesteps the resamplesteps
 	 * @param tolerance the tolerance
 	 * @return the correction method instance
 	 */
 	public static AbstractTestCorrection getCorrectionMethodInstance(String name, Integer resamplesteps, Integer tolerance){
			AbstractTestCorrection testCorrection=null;
			testCorrection = TestCorrectionRegistry.getCorrectionByName(name);
			if (testCorrection == null)
				testCorrection = TestCorrectionRegistry.getDefault();
			if (testCorrection instanceof IResampling) {
				IResampling resampling = (IResampling) testCorrection;
				resampling.resetCache();
				if (resamplesteps!=null && resamplesteps>0) {
					resampling.setNumberOfResamplingSteps(resamplesteps);
				}
				if (tolerance!=null && tolerance > 0) {
					resampling.setSizeTolerance(tolerance);
				}
			}
			
			return testCorrection;
			
		}

}
