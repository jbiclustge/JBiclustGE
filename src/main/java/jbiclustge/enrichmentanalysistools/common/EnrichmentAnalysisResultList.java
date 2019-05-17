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
package jbiclustge.enrichmentanalysistools.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.enriched.EnrichedBiclusterList;
import jbiclustge.results.biclusters.enriched.EnrichedBiclusterResult;
import pt.ornrocha.arrays.MTUArrayUtils;
import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.collections.MTUMapUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class EnrichmentAnalysisResultList.
 */
public class EnrichmentAnalysisResultList extends ArrayList<EnrichmentAnalysisResultsContainer>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	
	/** The mapofprobset 2 geneidused. */
	private Map<String, String> mapofprobset2geneidused;
	
	private Map<String, String> mapofgeneidused2probset=null;
	
	/** The gotermfrequencymap. */
	private Multimap<String, Integer> termidfrequencymap;
	
	/** The gotermpvaluemap. */
	private Multimap<String, Double> termidpvaluemap;
	
	/** The goterm 2 associatedgenes. */
	private LinkedHashMap<Integer, LinkedHashMap<String,ArrayList<String>>> termids2associatedgenes;
	
	/** The significantgottermsperbicluster. */
	private LinkedHashMap<Integer, Integer> significanttermidsperbicluster;
	
	/** The goid 2 goterm. */
	private LinkedHashMap<String, String> termid2termname;
	
	/** The pvaluetreshold. */
	private Double pvaluetreshold=null;
	
	/** The useadjustedpvalues. */
	private boolean useadjustedpvalues=false;
	
	/** The unannotated gene names. */
	private HashSet<String> unannotatedGeneNames;
	
	/** The associatedbiclusterlist. */
	private BiclusterList associatedbiclusterlist;
	
	private boolean wasusedMCTMethod=false;

	
	private EnrichmentAnalysisResultList(Map<String, String> mapofprobset2geneidused,
			Multimap<String, Integer> gotermfrequencymap,
			Multimap<String, Double> gotermpvaluemap,
			LinkedHashMap<Integer, LinkedHashMap<String,ArrayList<String>>> goterm2associatedgenes,
			LinkedHashMap<Integer, Integer> significantgottermsperbicluster,
			LinkedHashMap<String, String> goid2goterm,
			Double pvaluetreshold,
			boolean useadjustedpvalues,
			HashSet<String> unannotatedGeneNames,
			BiclusterList associatedbiclusterlist,
			boolean wasusedMCTMethod) {
		this.mapofprobset2geneidused=mapofprobset2geneidused;
		this.termidfrequencymap=gotermfrequencymap;
		this.termidpvaluemap=gotermpvaluemap;
		this.termids2associatedgenes=goterm2associatedgenes;
		this.significanttermidsperbicluster=significantgottermsperbicluster;
		this.termid2termname=goid2goterm;
		this.pvaluetreshold=pvaluetreshold;
		this.useadjustedpvalues=useadjustedpvalues;
		this.unannotatedGeneNames=unannotatedGeneNames;
		this.associatedbiclusterlist=associatedbiclusterlist;
		this.wasusedMCTMethod=wasusedMCTMethod;
		
	}
	
	public EnrichmentAnalysisResultList() {};
	
	/**
	 * Gets the goterm frequency.
	 *
	 * @return the goterm frequency
	 */
	public LinkedHashMap<String, Double> getTermidsFrequency() {
		LinkedHashMap<String, Double> res=new LinkedHashMap<>();
		for (String goid : termidfrequencymap.keySet()) {
			ArrayList<Integer> n=new ArrayList<>(termidfrequencymap.get(goid));
			double freq=(double)n.size()/this.size();
			res.put(goid, freq);
		}
		
		return (LinkedHashMap<String, Double>) MTUMapUtils.sortMapByValues(res, false);
	}

	/**
	 * Gets the goterm meanp value.
	 *
	 * @return the goterm meanp value
	 */
	public LinkedHashMap<String, Double> gettermidMeanpValue() {
		LinkedHashMap<String, Double> res=new LinkedHashMap<>();
		for (String goid : termidpvaluemap.keySet()) {
			ArrayList<Double> values=new ArrayList<>(termidpvaluemap.get(goid));
			double mean=new DescriptiveStatistics(MTUArrayUtils.convertDoubleListToArrayofdoubles(values)).getMean();
			res.put(goid, mean);
		}
		return res;
	}
	
	
	
	
	

	/**
	 * return a map linking the number of the bicluster and the genes associated to each enriched go term id.
	 *
	 * @return the goterm 2 associatedgenes
	 */
	public LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<String>>> getterm2associatedgenes() {
		return termids2associatedgenes;
	}

	/**
	 * Gets the goid 2 goterm.
	 *
	 * @return the goid 2 goterm
	 */
	public LinkedHashMap<String, String> gettermid2termname() {
		return termid2termname;
	}
	
	/**
	 * Sets the mapofprobset 2 geneidused.
	 *
	 * @param usedprobsetid2geneid the usedprobsetid 2 geneid
	 */
	public void setMapofprobset2geneidused(Map<String, String> usedprobsetid2geneid){
		this.mapofprobset2geneidused=usedprobsetid2geneid;
		try {
			this.mapofgeneidused2probset=MTUMapUtils.invertMap(mapofprobset2geneidused);
		} catch (InstantiationException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		} catch (IllegalAccessException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		}
	}
	
	/**
	 * Gets the mapofprobset 2 geneidused.
	 *
	 * @return the mapofprobset 2 geneidused
	 */
	public Map<String, String> getMapofprobset2geneidused() {
		return mapofprobset2geneidused;
	}
	
	
	

	public boolean wasUsedMCTMethod() {
		return wasusedMCTMethod;
	}

	public void setWasUsedMCTMethod(boolean wasusedMCTMethod) {
		this.wasusedMCTMethod = wasusedMCTMethod;
	}

	/**
	 * Gets the biclusterlist associated.
	 *
	 * @return the biclusterlist associated
	 */
	public BiclusterList getBiclusterlistAssociated() {
		return associatedbiclusterlist;
	}

	/**
	 * Sets the biclusterlist associated.
	 *
	 * @param associatedbiclusterlist the new biclusterlist associated
	 */
	public void setBiclusterlistAssociated(BiclusterList associatedbiclusterlist) {
		this.associatedbiclusterlist = associatedbiclusterlist;
	}

	/**
	 * Gets the unannotated gene names.
	 *
	 * @return the unannotated gene names
	 */
	public HashSet<String> getUnannotatedGeneNames() {
		return unannotatedGeneNames;
	}

	/**
	 * Sets the unannotated gene names.
	 *
	 * @param unannotatedGeneNames the new unannotated gene names
	 */
	public void setUnannotatedGeneNames(HashSet<String> unannotatedGeneNames) {
		this.unannotatedGeneNames = unannotatedGeneNames;
	}

	/**
	 * Gets the percentage enriched biclusters.
	 *
	 * @return the percentage enriched biclusters
	 */
	public double getPercentageEnrichedBiclusters(){
		if(pvaluetreshold!=null){
			if(significanttermidsperbicluster.size()>0){
				
				int positiveterms=0;
				for (int i = 0; i < significanttermidsperbicluster.size(); i++) {
					if(significanttermidsperbicluster.get(i)!=0)
						positiveterms++;
				}
				
				return (double)positiveterms/significanttermidsperbicluster.size()*100;
			}
			else
				return 0;
		}
		else{
			LogMessageCenter.getLogger().addErrorMessage("Please select an p-value treshold to calculate the percentage of enriched biclusters");
			return -1;
		}
			
	}
	
	
	/**
	 * Process results.
	 */
	public void processResults(){
		
		termidfrequencymap=ArrayListMultimap.create();
		termidpvaluemap=ArrayListMultimap.create();
		termid2termname=new LinkedHashMap<>();
		termids2associatedgenes=new LinkedHashMap<>();
		significanttermidsperbicluster=new LinkedHashMap<>();
		
		for (int i = 0; i < size(); i++) {
			EnrichmentAnalysisResultsContainer result=get(i);
			collectEnrichementAnalysisInformation(result, i);
			if(pvaluetreshold!=null)
				collectNumberEnrichedBiclusters(result, i);
		}
	}
	
	

	/**
	 * Filter and process results.
	 *
	 * @param tresholdvalue the tresholdvalue
	 * @param toadjustedpvalues the toadjustedpvalues
	 * @return the enrichment analysis result list
	 */
	public EnrichmentAnalysisResultList filterAndProcessResults(double tresholdvalue, boolean toadjustedpvalues){
		this.pvaluetreshold=tresholdvalue;
		this.useadjustedpvalues=toadjustedpvalues;
		processResults();
		return this;
	}
	
	
	/**
	 * Gets the bicluster list with pvalue cutt off.
	 *
	 * @param tresholdvalue the tresholdvalue
	 * @param toadjustedpvalues the toadjustedpvalues
	 * @return the bicluster list with pvalue cutt off
	 */
	public BiclusterList getBiclusterListWithPvalueCuttOff(double tresholdvalue, boolean toadjustedpvalues){
		
		BiclusterList filteredlist= new BiclusterList();
		filteredlist.setUsedmethod(associatedbiclusterlist.getUsedmethod());
		filteredlist.setAnalysedDataset(associatedbiclusterlist.getAnalysedDataset());
		
		filterAndProcessResults(tresholdvalue, toadjustedpvalues);
		
		for (Map.Entry<Integer, Integer> sig : significanttermidsperbicluster.entrySet()) {
			if(sig.getValue()>0)
				filteredlist.add(associatedbiclusterlist.get(sig.getKey()));
		}
		
		LogMessageCenter.getLogger().addDebugMessage("Number discarded biclusters: "+(associatedbiclusterlist.size()-filteredlist.size()));
		return filteredlist;
	}
	
	
	/**
	 * Gets the enriched bicluster list.
	 *
	 * @return the enriched bicluster list
	 */
	public EnrichedBiclusterList getEnrichedBiclusterList(){
		
		EnrichedBiclusterList filteredlist=new EnrichedBiclusterList();
		
		for (Map.Entry<Integer, Integer> sig : significanttermidsperbicluster.entrySet()) {
			if(sig.getValue()>0){
				//System.out.println(sig.getKey());
				ArrayList<String> enrichedgenes=getEnrichedGeneList(termids2associatedgenes.get(sig.getKey()));
			    ArrayList<String> conditions=associatedbiclusterlist.get(sig.getKey()).getConditionNames();
			    System.out.println(enrichedgenes);
			    //System.out.println(conditions);
				EnrichedBiclusterResult res=new EnrichedBiclusterResult(associatedbiclusterlist.getAnalysedDataset(), enrichedgenes, conditions);
				filteredlist.add(res);
			}	
		}
		
		filteredlist.setUsedmethod(associatedbiclusterlist.getUsedmethod());
		filteredlist.setMethodRunningParameters(associatedbiclusterlist.getMethodRunningParameters());
		filteredlist.setAnalysedDataset(associatedbiclusterlist.getAnalysedDataset());
		
		return filteredlist;
		
	}
	
	
	/**
	 * Gets the enriched gene list.
	 *
	 * @param maplist the maplist
	 * @return the enriched gene list
	 */
	private ArrayList<String> getEnrichedGeneList(LinkedHashMap<String,ArrayList<String>> maplist){
		
		HashSet<String> res=new HashSet<>();
		
		for (String termid : maplist.keySet()) {
			ArrayList<String> associated=maplist.get(termid);
			if(mapofgeneidused2probset!=null) {
				for (int i = 0; i < associated.size(); i++) {
					res.add(mapofgeneidused2probset.get(associated.get(i)));
				}
			}
			else
				res.addAll(associated);
		}
		
		return new ArrayList<>(res);
	}
	
	
	/**
	 * Collect enrichement analysis information.
	 *
	 * @param result the result
	 * @param currentbicluster the currentbicluster
	 */
	private void collectEnrichementAnalysisInformation(EnrichmentAnalysisResultsContainer result, int currentbicluster){
		
		
		LinkedHashMap<String, String> goterm2goname=result.getMapTermIDToTermName();
		LinkedHashMap<String, Double> pvalues=null;
		
		if(pvaluetreshold!=null){
			pvalues=result.getTermsPValueWithCutoff(pvaluetreshold, useadjustedpvalues);
		}
		
		if(goterm2goname!=null)
		for (String goid : goterm2goname.keySet()) {
			
			if(pvalues!=null && !pvalues.containsKey(goid))
				goid=null;

			if(goid!=null){
				if(!termid2termname.containsKey(goid))
						termid2termname.put(goid, goterm2goname.get(goid));
				termidfrequencymap.put(goid, currentbicluster);
				
				if(pvalues!=null)
					termidpvaluemap.put(goid, pvalues.get(goid));
				else
					termidpvaluemap.put(goid, result.getTermIDspvalues().get(goid));
				
				ArrayList<String> assocgenes=result.getGenesAssociatedToTermID(goid);
				if(assocgenes!=null)
					addAssociatedGenesToTermidInCluster(assocgenes, goid, currentbicluster);
			}
		}
	}
	
	/**
	 * Collect number enriched biclusters.
	 *
	 * @param result the result
	 * @param currentbicluster the currentbicluster
	 */
	private void collectNumberEnrichedBiclusters(EnrichmentAnalysisResultsContainer result, int currentbicluster){
		
		ArrayList<String> enrichedgoterms=result.getTermsWithCutoff(pvaluetreshold, useadjustedpvalues);
		if(enrichedgoterms.size()>0)
			significanttermidsperbicluster.put(currentbicluster, enrichedgoterms.size());
		else
			significanttermidsperbicluster.put(currentbicluster, 0);
		
	}
	
	/**
	 * Adds the associated genes to goterm in cluster.
	 *
	 * @param genes the genes
	 * @param id the goid
	 * @param clusterresult the clusterresult
	 */
	private void addAssociatedGenesToTermidInCluster(ArrayList<String> genes, String id, int clusterresult){
		if(termids2associatedgenes.containsKey(clusterresult)){
			termids2associatedgenes.get(clusterresult).put(id, genes);
		}
		else{
			LinkedHashMap<String,ArrayList<String>> genestogoid=new LinkedHashMap<>();
			genestogoid.put(id, genes);
			termids2associatedgenes.put(clusterresult, genestogoid);
		}
	}
	
	
	/**
	 * Sets the P value treshold.
	 *
	 * @param tresholdvalue the tresholdvalue
	 * @param toadjustedpvalues the toadjustedpvalues
	 * @return the enrichment analysis result list
	 */
	public EnrichmentAnalysisResultList setPValueTreshold(double tresholdvalue, boolean toadjustedpvalues){
		this.pvaluetreshold=tresholdvalue;
		this.useadjustedpvalues=toadjustedpvalues;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str =new StringBuilder();
		
		for (int i = 0; i < size(); i++) {
			
			str.append("##############  Enrichment analysis result of bicluster: "+(i+1)+" #######################\n");
			str.append(get(i).toString());
			str.append("\n\n");
		}
		
		return str.toString();
	}
	
	
	/**
	 * Gets the significant enriched terms.
	 *
	 * @param pvaluecutoff the pvaluecutoff
	 * @param useadustedpvalues the useadustedpvalues
	 * @return the significant enriched terms
	 */
	public String getSignificantEnrichedTerms(double pvaluecutoff, boolean useadustedpvalues){
		StringBuilder str =new StringBuilder();
		
		for (int i = 0; i < size(); i++) {
			
			str.append("##############  Significant Enrichment analysis result of bicluster: "+(i+1)+" #######################\n");
			str.append(get(i).getSignificantTermIDsReport(pvaluecutoff, useadustedpvalues));
			str.append("\n\n");
		}
		
		return str.toString();	
	}
	
	
	/**
	 * Prints the.
	 */
	public void print(){
		System.out.println(toString());
	}
	
	/**
	 * Prints the significant terms.
	 *
	 * @param pvaluecutoff the pvaluecutoff
	 * @param useadustedpvalues the useadustedpvalues
	 */
	public void printSignificantTerms(double pvaluecutoff, boolean useadustedpvalues){
		System.out.println(getSignificantEnrichedTerms(pvaluecutoff, useadustedpvalues));
	}
	
	public EnrichmentAnalysisResultList getCloneWithoutExpressionDataAssociation() throws Exception {
		
		EnrichmentAnalysisResultList clone=new EnrichmentAnalysisResultList((Map<String, String>) MTUCollectionsUtils.deepCloneObject(mapofprobset2geneidused), 
				(Multimap<String, Integer>) MTUCollectionsUtils.deepCloneObject(termidfrequencymap), 
				(Multimap<String, Double>) MTUCollectionsUtils.deepCloneObject(termidpvaluemap),
				(LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<String>>>) MTUCollectionsUtils.deepCloneObject(termids2associatedgenes), 
				(LinkedHashMap<Integer, Integer>) MTUCollectionsUtils.deepCloneObject(significanttermidsperbicluster), 
				(LinkedHashMap<String, String>) MTUCollectionsUtils.deepCloneObject(termid2termname),
				pvaluetreshold,
				useadjustedpvalues,
				(HashSet<String>) MTUCollectionsUtils.deepCloneObject(unannotatedGeneNames),
				associatedbiclusterlist.getCloneWithoutExpressionDataAssociation(),
				wasusedMCTMethod);
		
		for (int i = 0; i < size(); i++) {
			clone.add(get(i).getCloneWithoutExpressionDataAssociation());
		}
		
		return clone;
	}
	

}
