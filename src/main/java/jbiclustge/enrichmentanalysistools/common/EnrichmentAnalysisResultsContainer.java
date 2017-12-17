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
import java.util.LinkedHashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.collections.MTUMapUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class EnrichmentAnalysisResultsContainer.
 */
public class EnrichmentAnalysisResultsContainer {
	
	
	 /** The map GO term 2 genes. */
 	private Multimap<String, String> mapGOTerm2Genes=null;
	 
 	/** The map GO term to go name. */
 	private LinkedHashMap<String, String> mapGOTermToGoName; 
	 
 	/** The GO termspvalues. */
 	private LinkedHashMap<String, Double> GOTermspvalues;
	 
 	/** The GO termsadjustedpvalues. */
 	private LinkedHashMap<String, Double> GOTermsadjustedpvalues;
	 
 	/** The map GO term 2 number annotated study genes. */
 	private LinkedHashMap<String, Integer> mapGOTerm2NumberAnnotatedStudyGenes;
	 
 	/** The map GO term 2 number significant annotated genes. */
 	private LinkedHashMap<String, Integer> mapGOTerm2NumberSignificantAnnotatedGenes;
	 
 	/** The map GO term 2 number annotated population genes. */
 	private LinkedHashMap<String, Integer> mapGOTerm2NumberAnnotatedPopulationGenes;
	 
 	/** The biclusterresult. */
 	private BiclusterResult biclusterresult;
	 
 	
 	private EnrichmentAnalysisResultsContainer(Multimap<String, String> mapGOTerm2Genes,
 			LinkedHashMap<String, String> mapGOTermToGoName,
 			LinkedHashMap<String, Double> GOTermspvalues,
 			LinkedHashMap<String, Double> GOTermsadjustedpvalues,
 			LinkedHashMap<String, Integer> mapGOTerm2NumberAnnotatedStudyGenes,
 			LinkedHashMap<String, Integer> mapGOTerm2NumberSignificantAnnotatedGenes,
 			LinkedHashMap<String, Integer> mapGOTerm2NumberAnnotatedPopulationGenes,
 			BiclusterResult biclusterresult) {
 		this.mapGOTerm2Genes=mapGOTerm2Genes;
 		this.mapGOTermToGoName=mapGOTermToGoName;
 		this.GOTermspvalues=GOTermspvalues;
 		this.GOTermsadjustedpvalues=GOTermsadjustedpvalues;
 		this.mapGOTerm2NumberAnnotatedStudyGenes=mapGOTerm2NumberAnnotatedStudyGenes;
 		this.mapGOTerm2NumberSignificantAnnotatedGenes=mapGOTerm2NumberSignificantAnnotatedGenes;
 		this.mapGOTerm2NumberAnnotatedPopulationGenes=mapGOTerm2NumberAnnotatedPopulationGenes;
 		this.biclusterresult=biclusterresult;
 	}
	 
	 /**
 	 * Instantiates a new enrichment analysis results container.
 	 */
 	public EnrichmentAnalysisResultsContainer(){}
	 
	 /**
 	 * Instantiates a new enrichment analysis results container.
 	 *
 	 * @param biclusterresult the biclusterresult
 	 */
 	public EnrichmentAnalysisResultsContainer(BiclusterResult biclusterresult){
		 this.biclusterresult=biclusterresult;
	 }
	 
	 
	 
	 /**
 	 * Adds the to map term 2 gene association.
 	 *
 	 * @param termid the termid
 	 * @param geneid the geneid
 	 */
 	public void addToMapTerm2GeneAssociation(String termid, String geneid){
		 if(mapGOTerm2Genes==null)
			 mapGOTerm2Genes=ArrayListMultimap.create();
		 mapGOTerm2Genes.put(termid, geneid);
	 }
	 
	 /**
 	 * Adds the to map term 2 gene association.
 	 *
 	 * @param termid the termid
 	 * @param geneids the geneids
 	 */
 	public void addToMapTerm2GeneAssociation(String termid, String[] geneids){
		 for (String id : geneids) {
			addToMapTerm2GeneAssociation(termid, id);
		 }
	 }
	 
	 
	 /**
 	 * Adds the map go term ID with GO term name.
 	 *
 	 * @param termid the termid
 	 * @param termname the termname
 	 */
 	public void addMapGoTermIDWithGOTermName(String termid, String termname){
		 if(mapGOTermToGoName==null)
			 mapGOTermToGoName=new LinkedHashMap<>();
		 mapGOTermToGoName.put(termid, termname);
	 }
	 
	 
	 /**
 	 * Adds the GO term P value.
 	 *
 	 * @param termid the termid
 	 * @param pvalue the pvalue
 	 */
 	public void addGOTermPValue(String termid, double pvalue){
		 if(GOTermspvalues==null)
			 GOTermspvalues=new LinkedHashMap<>();
		 if(pvalue>=0.0)
		   GOTermspvalues.put(termid, pvalue);
	 }
	
	 
	 /**
 	 * Adds the GO term adjusted P value.
 	 *
 	 * @param termid the termid
 	 * @param adjpvalue the adjpvalue
 	 */
 	public void addGOTermAdjustedPValue(String termid, double adjpvalue){
		 if(GOTermsadjustedpvalues==null)
			 GOTermsadjustedpvalues=new LinkedHashMap<>();
		 if(adjpvalue>=0.0)
		   GOTermsadjustedpvalues.put(termid, adjpvalue);
		 
	 }
	 
	 /**
 	 * Adds the P values.
 	 *
 	 * @param termid the termid
 	 * @param pvalue the pvalue
 	 * @param adjustedpavalue the adjustedpavalue
 	 */
 	public void addPValues(String termid, double pvalue, double adjustedpavalue){
		 addGOTermAdjustedPValue(termid, adjustedpavalue);
		 addGOTermPValue(termid, pvalue);
		 LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Adding pvalue:"+pvalue+" Adjusted value: "+adjustedpavalue);
	 }

	 
	 /**
 	 * Adds the number annotated genes to GO term.
 	 *
 	 * @param termid the termid
 	 * @param numbergenes the numbergenes
 	 */
 	public void addNumberAnnotatedGenesToGOTerm(String termid, int numbergenes){
		 if(mapGOTerm2NumberAnnotatedStudyGenes==null)
			 mapGOTerm2NumberAnnotatedStudyGenes=new LinkedHashMap<>();
		 mapGOTerm2NumberAnnotatedStudyGenes.put(termid, numbergenes);
	 }
	 
	 /**
 	 * Adds the number population genes to GO term.
 	 *
 	 * @param termid the termid
 	 * @param numbergenes the numbergenes
 	 */
 	public void addNumberPopulationGenesToGOTerm(String termid, int numbergenes){
		 if(mapGOTerm2NumberAnnotatedPopulationGenes==null)
			 mapGOTerm2NumberAnnotatedPopulationGenes=new LinkedHashMap<>();
		 mapGOTerm2NumberAnnotatedPopulationGenes.put(termid, numbergenes);
	 }
	 
	 /**
 	 * Adds the number significant annotated genes to GO term.
 	 *
 	 * @param termid the termid
 	 * @param numbergenes the numbergenes
 	 */
 	public void addNumberSignificantAnnotatedGenesToGOTerm(String termid, int numbergenes){
		 if(mapGOTerm2NumberSignificantAnnotatedGenes==null)
			 mapGOTerm2NumberSignificantAnnotatedGenes=new LinkedHashMap<>();
		 mapGOTerm2NumberSignificantAnnotatedGenes.put(termid, numbergenes);
	 }


	/**
	 * Gets the map GO term 2 genes.
	 *
	 * @return the map GO term 2 genes
	 */
	public Multimap<String, String> getMapGOTerm2Genes() {
		return mapGOTerm2Genes;
	}

	
	/**
	 * Gets the genes associated to go term.
	 *
	 * @param goterm the goterm
	 * @return the genes associated to go term
	 */
	public ArrayList<String> getGenesAssociatedToGoTerm(String goterm){
		if(mapGOTerm2Genes.containsKey(goterm))
			return new ArrayList<>(mapGOTerm2Genes.get(goterm));
		return null;
	}


	/**
	 * Gets the map GO term to go name.
	 *
	 * @return the map GO term to go name
	 */
	public LinkedHashMap<String, String> getMapGOTermToGoName() {
		return mapGOTermToGoName;
	}



	/**
	 * Gets the GO termspvalues.
	 *
	 * @return the GO termspvalues
	 */
	public LinkedHashMap<String, Double> getGOTermspvalues() {
		return GOTermspvalues;
	}



	/**
	 * Gets the GO termsadjustedpvalues.
	 *
	 * @return the GO termsadjustedpvalues
	 */
	public LinkedHashMap<String, Double> getGOTermsadjustedpvalues() {
		return GOTermsadjustedpvalues;
	}



	/**
	 * Gets the biclusterresult.
	 *
	 * @return the biclusterresult
	 */
	public BiclusterResult getBiclusterresult() {
		return biclusterresult;
	}



	/**
	 * Gets the map GO term 2 number annotated study genes.
	 *
	 * @return the map GO term 2 number annotated study genes
	 */
	public LinkedHashMap<String, Integer> getMapGOTerm2NumberAnnotatedStudyGenes() {
		return mapGOTerm2NumberAnnotatedStudyGenes;
	}


	/**
	 * Map map GO term 2 number significant annotated genes.
	 *
	 * @return the linked hash map
	 */
	public LinkedHashMap<String, Integer> mapMapGOTerm2NumberSignificantAnnotatedGenes() {
		return mapGOTerm2NumberSignificantAnnotatedGenes;
	}

	/**
	 * Gets the map GO term 2 number annotated population genes.
	 *
	 * @return the map GO term 2 number annotated population genes
	 */
	public LinkedHashMap<String, Integer> getMapGOTerm2NumberAnnotatedPopulationGenes() {
		return mapGOTerm2NumberAnnotatedPopulationGenes;
	}
	 
	 
	/**
	 * Gets the terms P value with cutoff.
	 *
	 * @param pvaluecutoff the pvaluecutoff
	 * @param adustedpvalues the adustedpvalues
	 * @return the terms P value with cutoff
	 */
	public LinkedHashMap<String, Double> getTermsPValueWithCutoff(double pvaluecutoff, boolean adustedpvalues){
		
		LinkedHashMap<String, Double> pvalues=null;
		if(adustedpvalues && GOTermsadjustedpvalues!=null)
			pvalues=GOTermsadjustedpvalues;
		else
			pvalues=GOTermspvalues;
		
		
	/*	if(adustedpvalues && pvalues==null){
			//pvalues=GOTermspvalues;
			//LogMessageCenter.getLogger().toClass(getClass()).addErrorMessage("This method does not support adjusted p-values, thus the values shown were replaced by p-values without adjustment");
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("It was selected the use of adjusted p-values but the enrichment analysis did not calculated these values");
		}*/
			
		
		LinkedHashMap<String, Double> res=new LinkedHashMap<>();
        //System.out.println("Using adjusted p-values: "+adustedpvalues);
		if(pvalues!=null)
			for (String key : pvalues.keySet()) {
				double val=pvalues.get(key);
				if(val<pvaluecutoff){
					res.put(key, val);
					//System.out.println("Key: "+key+" value: "+val);
				}
			}

		
		return (LinkedHashMap<String, Double>) MTUMapUtils.sortMapByValues(res, true);
		
	}
	
	/**
	 * Gets the terms with cutoff.
	 *
	 * @param pvaluecutoff the pvaluecutoff
	 * @param adustedpvalues the adustedpvalues
	 * @return the terms with cutoff
	 */
	public ArrayList<String> getTermsWithCutoff(double pvaluecutoff, boolean adustedpvalues){
		
		LinkedHashMap<String, Double> res=getTermsPValueWithCutoff(pvaluecutoff, adustedpvalues);
		return new ArrayList<>(res.keySet());	
	}
	
	
	/**
	 * Gets the all go term.
	 *
	 * @return the all go term
	 */
	public ArrayList<String> getAllGoTerm(){
		if(mapGOTermToGoName!=null)
			return new ArrayList<>(mapGOTermToGoName.keySet());
		return new ArrayList<>();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder str=new StringBuilder();
		
		str.append("GO_ID"+"\t"+"Term"+"\t");
		if(mapGOTerm2NumberAnnotatedPopulationGenes!=null)
			str.append("number_population_terms"+"\t");
		if(mapGOTerm2NumberAnnotatedStudyGenes!=null)
			str.append("number_annotated_terms\t");
		if(mapGOTerm2NumberSignificantAnnotatedGenes!=null)
			str.append("number_significant_terms\t");
		if(GOTermspvalues!=null)
			str.append("p-values\t");
		if(GOTermsadjustedpvalues!=null)
			str.append("adjusted p-values\t");
		str.append("associated genes");
		str.append("\n");
		
		
		for (String goid : mapGOTermToGoName.keySet()) {
			str.append(goid+"\t"+mapGOTermToGoName.get(goid)+"\t");
			if(mapGOTerm2NumberAnnotatedPopulationGenes!=null)
				str.append(mapGOTerm2NumberAnnotatedPopulationGenes.get(goid)+"\t");
			if(mapGOTerm2NumberAnnotatedStudyGenes!=null)
				str.append(mapGOTerm2NumberAnnotatedStudyGenes.get(goid)+"\t");
			if(mapGOTerm2NumberSignificantAnnotatedGenes!=null)
				str.append(mapGOTerm2NumberSignificantAnnotatedGenes.get(goid)+"\t");
			if(GOTermspvalues!=null)
				str.append(GOTermspvalues.get(goid)+"\t");
			if(GOTermsadjustedpvalues!=null)
				str.append(GOTermsadjustedpvalues.get(goid));
			str.append(mapGOTerm2Genes.get(goid));
			str.append("\n");
		}
		
		return str.toString();
	}
	
	/**
	 * Gets the significant go terms report.
	 *
	 * @param pvaluecutoff the pvaluecutoff
	 * @param useadustedpvalues the useadustedpvalues
	 * @return the significant go terms report
	 */
	public String getSignificantGoTermsReport(double pvaluecutoff, boolean useadustedpvalues){
		StringBuilder str=new StringBuilder();
		
		LinkedHashMap<String, Double> sortedsignificantterms=getTermsPValueWithCutoff(pvaluecutoff, useadustedpvalues);
		
		if(sortedsignificantterms.size()>0){
			
			
			str.append("GO_ID"+"\t"+"Term"+"\t");
				if(mapGOTerm2NumberAnnotatedPopulationGenes!=null)
					str.append("number_population_terms"+"\t");
				if(mapGOTerm2NumberAnnotatedStudyGenes!=null)
					str.append("number_annotated_terms\t");
				if(mapGOTerm2NumberSignificantAnnotatedGenes!=null)
					str.append("number_significant_terms\t");
				if(GOTermspvalues!=null)
					str.append("p-values\t");
				if(GOTermsadjustedpvalues!=null)
					str.append("adjusted p-values\t");
				str.append("associated genes");
				str.append("\n");
			
			
			for (String goid : sortedsignificantterms.keySet()) {
				   
					str.append(goid+"\t"+mapGOTermToGoName.get(goid)+"\t");
					if(mapGOTerm2NumberAnnotatedPopulationGenes!=null)
						str.append(mapGOTerm2NumberAnnotatedPopulationGenes.get(goid)+"\t");
					if(mapGOTerm2NumberAnnotatedStudyGenes!=null)
						str.append(mapGOTerm2NumberAnnotatedStudyGenes.get(goid)+"\t");
					if(mapGOTerm2NumberSignificantAnnotatedGenes!=null)
						str.append(mapGOTerm2NumberSignificantAnnotatedGenes.get(goid)+"\t");
					if(GOTermspvalues!=null)
						str.append(GOTermspvalues.get(goid)+"\t");
					if(GOTermsadjustedpvalues!=null)
						str.append(GOTermsadjustedpvalues.get(goid)+"\t");
					str.append(mapGOTerm2Genes.get(goid));
					str.append("\n");	
			}
		}
		
		return str.toString();
		
	}
	
	public EnrichmentAnalysisResultsContainer getCloneWithoutExpressionDataAssociation() throws Exception {
		
		return new EnrichmentAnalysisResultsContainer((Multimap<String, String>) MTUCollectionsUtils.deepCloneObject(mapGOTerm2Genes),
				(LinkedHashMap<String, String>) MTUCollectionsUtils.deepCloneObject(mapGOTermToGoName), 
				(LinkedHashMap<String, Double>) MTUCollectionsUtils.deepCloneObject(GOTermspvalues), 
				(LinkedHashMap<String, Double>) MTUCollectionsUtils.deepCloneObject(GOTermsadjustedpvalues),
				(LinkedHashMap<String, Integer>) MTUCollectionsUtils.deepCloneObject(mapGOTerm2NumberAnnotatedStudyGenes), 
				(LinkedHashMap<String, Integer>) MTUCollectionsUtils.deepCloneObject(mapGOTerm2NumberSignificantAnnotatedGenes), 
				(LinkedHashMap<String, Integer>) MTUCollectionsUtils.deepCloneObject(mapGOTerm2NumberAnnotatedPopulationGenes), 
				biclusterresult.getCloneWithoutExpressionDataAssociation());
	}
	 

}
