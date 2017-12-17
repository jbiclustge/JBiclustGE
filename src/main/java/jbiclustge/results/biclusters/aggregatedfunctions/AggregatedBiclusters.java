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
package jbiclustge.results.biclusters.aggregatedfunctions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.json.JSONObject;

import jbiclustge.analysis.overlap.OverlapAnalyser;
import jbiclustge.analysis.similarity.BiclusterResultsPairwiseFunctions;
import jbiclustge.analysis.similarity.components.SimilarityIndexMethod;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.results.biclusters.filters.BiclusterFilteringUtils;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.jsonutils.MTUJsonIOUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import smile.imputation.MissingValueImputationException;

// TODO: Auto-generated Javadoc
/**
 * The Class AggregatedBiclusters.
 */
public class AggregatedBiclusters {
	
	
	/** The aggregatedresultsbymethod. */
	protected LinkedHashMap<String, ArrayList<BiclusterList>> aggregatedresultsbymethod;
	
	/** The orderedinputlist. */
	protected ArrayList<BiclusterList> orderedinputlist;
	
	/** The expressiondataset. */
	private ExpressionData expressiondataset;
	
	/** The genes. */
	protected ArrayList<String> genes;
	
	/** The conditions. */
	protected ArrayList<String> conditions;
	
	/** The biclustermethods. */
	protected static String BICLUSTERMETHODS="Bicluster methods";
	
	/**
	 * Instantiates a new aggregated biclusters.
	 */
	public AggregatedBiclusters(){
		aggregatedresultsbymethod=new LinkedHashMap<>();
		orderedinputlist=new ArrayList<>();
	}
	
	
	/**
	 * Gets the list of biclusters.
	 *
	 * @return the list of biclusters
	 */
	public ArrayList<BiclusterList> getListOfBiclusters(){
		return orderedinputlist;
	}
	
	/**
	 * Gets the list of bicluster by method.
	 *
	 * @return the list of bicluster by method
	 */
	public LinkedHashMap<String, ArrayList<BiclusterList>> getListOfBiclusterByMethod(){
		return aggregatedresultsbymethod;
	}
	
	/**
	 * Sets the expression data.
	 *
	 * @param data the new expression data
	 */
	public void setExpressionData(ExpressionData data){
		this.expressiondataset=data;
		this.genes=expressiondataset.getGeneNamesList();
		this.conditions=expressiondataset.getConditionsList();
	}
	
	/**
	 * Gets the expression dataset.
	 *
	 * @return the expression dataset
	 */
	public ExpressionData getExpressionDataset(){
		return expressiondataset;
	}
	
	/**
	 * Gets the list methods used in bicluster results.
	 *
	 * @return the list methods used in bicluster results
	 */
	public ArrayList<String> getListMethodsUsedInBiclusterResults(){
		ArrayList<String> res=new ArrayList<>();
		
		for (int i = 0; i < orderedinputlist.size(); i++) {
			res.add(orderedinputlist.get(i).getUsedmethod());
		}
		
		return res;
	}
	
	/**
	 * Adds the bicluster result list.
	 *
	 * @param listresults the listresults
	 */
	public void addBiclusterResultList(BiclusterList listresults){
		addBiclusterResultList(listresults.getUsedmethod(), listresults);
	}

	
	/**
	 * Adds the bicluster result list.
	 *
	 * @param method the method
	 * @param listresults the listresults
	 */
	public void addBiclusterResultList(String method, BiclusterList listresults){
		
		if(!aggregatedresultsbymethod.containsKey(method)){
			ArrayList<BiclusterList> setres=new ArrayList<>();
			setres.add(listresults);
			aggregatedresultsbymethod.put(method, setres);
		}
		else{
			aggregatedresultsbymethod.get(method).add(listresults);
		}
		
		if(expressiondataset==null && listresults.getAnalysedDataset()!=null)
			setExpressionData(listresults.getAnalysedDataset());
		else if(expressiondataset!=null && listresults.getAnalysedDataset()==null)
			listresults.setAnalysedDatasetAndVerifySubsets(expressiondataset);
		orderedinputlist.add(listresults);
		
	}
	
	
	/**
	 * Gets the number biclusters.
	 *
	 * @return the number biclusters
	 */
	public int getNumberBiclusters(){
		
		int n=0;
		for (int i = 0; i < orderedinputlist.size(); i++) {
			n+=orderedinputlist.get(i).size();
		}
		
		return n;
	}
	
	

	/**
	 * Gets the number biclusters calculated by method.
	 *
	 * @param methodname the methodname
	 * @return the number biclusters calculated by method
	 */
	public int getNumberBiclustersCalculatedByMethod(String methodname){
		
		for (String name : aggregatedresultsbymethod.keySet()) {
			if(name.toLowerCase().equals(methodname.toLowerCase())){
				ArrayList<BiclusterList> list=aggregatedresultsbymethod.get(name);
				int n=0;
				for (int i = 0; i < list.size(); i++) {
					n+=list.get(i).size();
				}
				return n;
			}
				
		}

		return 0;
			
	}
	
	/**
	 * Gets the aggregated bicluster list.
	 *
	 * @return the aggregated bicluster list
	 */
	public BiclusterList getAggregatedBiclusterList(){
		BiclusterList res=new BiclusterList();
		res.setAnalysedDataset(expressiondataset);
		
		for (int i = 0; i < orderedinputlist.size(); i++) {
			res.addAll(orderedinputlist.get(i));
		}
		
		return res;
	}
	
	
	
	/**
	 * Gets the matrix of gene frequencies in conditions.
	 *
	 * @return the matrix of gene frequencies in conditions
	 */
	public int[][] getMatrixOfGeneFrequenciesInConditions(){
		return getMatrixOfGeneInfluenceFrequencyInConditionsForBiclusterList(orderedinputlist);
	}
	
	
	/**
	 * Save gene frequency matrix.
	 *
	 * @param filepath the filepath
	 * @param delimiter the delimiter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void saveGeneFrequencyMatrix(String filepath, String delimiter) throws IOException{
		
		int[][] matrix=getMatrixOfGeneFrequenciesInConditions();
		MTUWriterUtils.writeIntegerMatrixToFile(filepath, matrix, delimiter, conditions, genes);
	}
	
	/**
	 * Removes the conditionswith percentil cut off.
	 *
	 * @param percentil the percentil
	 * @return the triplet
	 */
	public Triplet<ArrayList<String>,ArrayList<String>,int[][]> removeConditionswithPercentilCutOff(double percentil){
		
		Pair<ArrayList<Integer>, int[][]> filtered=BiclusterFilteringUtils.filterColumnsWithPercentilCutoff(getMatrixOfGeneFrequenciesInConditions(), percentil);
		
		ArrayList<String> newcondsvector=new ArrayList<>();
		ArrayList<Integer> tofilter=filtered.getValue0();
		for (int i = 0; i < conditions.size(); i++) {
			if(tofilter.contains(i))
				newcondsvector.add(conditions.get(i));
		}
		
		return new Triplet<ArrayList<String>, ArrayList<String>, int[][]>(genes, newcondsvector, filtered.getValue1());
	}
	
	
	

	/**
	 * Removes the conditionswith low gene frequencies.
	 *
	 * @param freqvaluethreshold the freqvaluethreshold
	 * @param percentagecutoff the percentagecutoff
	 * @return the triplet
	 */
	public Triplet<ArrayList<String>,ArrayList<String>,int[][]> removeConditionswithLowGeneFrequencies(int freqvaluethreshold, double percentagecutoff){
		
		Pair<ArrayList<Integer>, int[][]> filtered=BiclusterFilteringUtils.filterColumnsWithLowItemsFrequency(getMatrixOfGeneFrequenciesInConditions(), freqvaluethreshold, percentagecutoff);
		
		ArrayList<String> newcondsvector=new ArrayList<>();
		ArrayList<Integer> tofilter=filtered.getValue0();
		for (int i = 0; i < conditions.size(); i++) {
			if(!tofilter.contains(i))
				newcondsvector.add(conditions.get(i));
		}
		
		return new Triplet<ArrayList<String>, ArrayList<String>, int[][]>(genes, newcondsvector, filtered.getValue1());
	}
	 
	
	/**
	 * Removes the geneswith lower frequencies.
	 *
	 * @param freqvaluethreshold the freqvaluethreshold
	 * @param percentagecutoff the percentagecutoff
	 * @return the triplet
	 */
	public Triplet<ArrayList<String>,ArrayList<String>,int[][]> removeGeneswithLowerFrequencies(int freqvaluethreshold, double percentagecutoff){
		
		Pair<ArrayList<Integer>, int[][]> filtered=BiclusterFilteringUtils.filterRowsWithLowItemsFrequency(getMatrixOfGeneFrequenciesInConditions(), freqvaluethreshold, percentagecutoff);
		ArrayList<String> newgenevector=new ArrayList<>();
		ArrayList<Integer> tofilter=filtered.getValue0();
		for (int i = 0; i < genes.size(); i++) {
			if(!tofilter.contains(i))
				newgenevector.add(genes.get(i));
		}
		
		return new Triplet<ArrayList<String>, ArrayList<String>, int[][]>(newgenevector, conditions, filtered.getValue1()); 
	}
	
	
	
    /**
     * Removes the conditions and genes with lower frequencies.
     *
     * @param freqvaluethreshold the freqvaluethreshold
     * @param percentagecutoff the percentagecutoff
     * @return the triplet
     */
    public Triplet<ArrayList<String>,ArrayList<String>,int[][]> removeConditionsAndGenesWithLowerFrequencies(int freqvaluethreshold, double percentagecutoff){
    	return removeConditionsAndGenesWithLowerFrequencies(freqvaluethreshold,percentagecutoff,freqvaluethreshold,percentagecutoff);
    }
	
    /**
     * Removes the conditions and genes with lower frequencies.
     *
     * @param condfreqvaluethreshold the condfreqvaluethreshold
     * @param condpercentagecutoff the condpercentagecutoff
     * @param genefreqvaluethreshold the genefreqvaluethreshold
     * @param genepercentagecutoff the genepercentagecutoff
     * @return the triplet
     */
    public Triplet<ArrayList<String>,ArrayList<String>,int[][]> removeConditionsAndGenesWithLowerFrequencies(int condfreqvaluethreshold, double condpercentagecutoff,int genefreqvaluethreshold, double genepercentagecutoff ){
    	
    	int[][] matrix=getMatrixOfGeneFrequenciesInConditions();
    	Pair<ArrayList<Integer>, int[][]> filteredconditions=BiclusterFilteringUtils.filterColumnsWithLowItemsFrequency(matrix, condfreqvaluethreshold, condpercentagecutoff);
    	
    	ArrayList<String> newcondsvector=new ArrayList<>();
		ArrayList<Integer> condtofilter=filteredconditions.getValue0();
		for (int i = 0; i < conditions.size(); i++) {
			if(!condtofilter.contains(i))
				newcondsvector.add(conditions.get(i));
		}
    	
    	Pair<ArrayList<Integer>, int[][]> filteredgenes=BiclusterFilteringUtils.filterRowsWithLowItemsFrequency(matrix, genefreqvaluethreshold,genepercentagecutoff);
    	
    	ArrayList<String> newgenevector=new ArrayList<>();
		ArrayList<Integer> genestofilter=filteredgenes.getValue0();
		for (int i = 0; i < genes.size(); i++) {
			if(!genestofilter.contains(i))
				newgenevector.add(genes.get(i));
		}
		
		int[][] filteredmatrix=new int[newgenevector.size()][newcondsvector.size()];
		
		
		int r=0;
		for (int i = 0; i < matrix.length; i++) {
			int c=0;
			if(!filteredgenes.getValue0().contains(i)){
			
				for (int j = 0; j < matrix[0].length; j++) {
				   
					if(!filteredconditions.getValue0().contains(j)){
						filteredmatrix[r][c]=matrix[i][j];
						c++;
					}
				}
				r++;
			}
		}
		
		return new Triplet<ArrayList<String>, ArrayList<String>, int[][]>(newgenevector, newcondsvector, filteredmatrix);

    }
    
	/**
	 * Gets the matrix of gene influence frequency in conditions for bicluster list.
	 *
	 * @param listbiclusters the listbiclusters
	 * @return the matrix of gene influence frequency in conditions for bicluster list
	 */
	public int[][] getMatrixOfGeneInfluenceFrequencyInConditionsForBiclusterList(ArrayList<BiclusterList> listbiclusters){
		ArrayList<BiclusterResult> totallist=new ArrayList<>();
		for (int i = 0; i < listbiclusters.size(); i++) {
			totallist.addAll(listbiclusters.get(i));
		}
		return getMatrixOfGeneInfluenceFrequencyInConditions(totallist);
	}
	

	/**
	 * Gets the matrix of gene influence frequency in conditions.
	 *
	 * @param resultset the resultset
	 * @return the matrix of gene influence frequency in conditions
	 */
	private int[][] getMatrixOfGeneInfluenceFrequencyInConditions(ArrayList<BiclusterResult> resultset){
		
		int [][] geneconditionfrequency=new int[genes.size()][conditions.size()];

		//System.out.println("Number genes: "+genes.size());
		//System.out.println("Number conditions: "+conditions.size());
		for (int i = 0; i < resultset.size(); i++) {
			BiclusterResult bicres=resultset.get(i);
			ArrayList<Integer>bicgenes=bicres.getGeneIndexes();
			ArrayList<Integer>bicconditions=bicres.getConditionIndexes();
	
			for (int j = 0; j < bicgenes.size(); j++) {
				int geneindex=bicgenes.get(j);
				for (int k = 0; k < bicconditions.size(); k++) {
					int condindex=bicconditions.get(k);
					if(geneindex>-1 && condindex>-1){
						//System.out.println(geneindex+" -> "+condindex);
						geneconditionfrequency[geneindex][condindex]++;
					}
				}
			}
		}
		return geneconditionfrequency;
	}
	
	
	/**
	 * Summary.
	 *
	 * @return the string
	 * @throws Exception the exception
	 */
	public String summary() throws Exception{
		StringBuilder str=new StringBuilder();
		str.append("Total biclusters: "+getNumberBiclusters()+"\n");
		
		for (String method : aggregatedresultsbymethod.keySet()) {
			str.append("Biclusters calculated by "+method+": "+getNumberBiclustersCalculatedByMethod(method)+"\n");
		}
        
		
		String jaccardindex=getJaccardPairwiseSimilarityIndexSummary();
		if(jaccardindex!=null){
			str.append("\n\n");
			str.append(jaccardindex);
		}
		
		return str.toString();
	}
	
	/**
	 * Gets the filtered bicluster list by overlap threshold.
	 *
	 * @param threshold the threshold
	 * @return the filtered bicluster list by overlap threshold
	 * @throws Exception the exception
	 */
	public BiclusterList getFilteredBiclusterListByOverlapThreshold(double threshold) throws Exception{
		
		BiclusterList alllist=new BiclusterList();
		alllist.setAnalysedDataset(expressiondataset);
		for (int i = 0; i < orderedinputlist.size(); i++) {
			alllist.addAll(orderedinputlist.get(i));
		}
		return OverlapAnalyser.filterBiclusterListWithOverlapThreshold(alllist, threshold, -1);
	}
	
	/**
	 * Gets the filtered bicluster list by overlap treshold for each method results.
	 *
	 * @param threshold the threshold
	 * @return the filtered bicluster list by overlap treshold for each method results
	 * @throws Exception the exception
	 */
	public LinkedHashMap<String, BiclusterList> getFilteredBiclusterListByOverlapTresholdForEachMethodResults(double threshold) throws Exception{
		
		LinkedHashMap<String, BiclusterList> res=new LinkedHashMap<>();
		
	    for (Map.Entry<String, ArrayList<BiclusterList>> saved: aggregatedresultsbymethod.entrySet()) {
			ArrayList<BiclusterList> listbiclusters=saved.getValue();
			if(listbiclusters.size()>1){
				BiclusterList all=new BiclusterList();
				all.setAnalysedDataset(expressiondataset);
				all.setUsedmethod(saved.getKey()+"_filteredbyoverlap");
				for (int i = 0; i < listbiclusters.size(); i++) {
					all.addAll(listbiclusters.get(i));
				}
				res.put(saved.getKey(), OverlapAnalyser.filterBiclusterListWithOverlapThreshold(all, threshold, -1));
			}
			else{
				if(listbiclusters.get(0).getUsedmethod()==null)
					listbiclusters.get(0).setUsedmethod(saved.getKey());
				res.put(saved.getKey(), OverlapAnalyser.filterBiclusterListWithOverlapThreshold(listbiclusters.get(0), threshold, -1));
			}
			
		}
	   return res;		
	}
	
	
	//public LinkedHashMap<String, BiclusterList> reduceBicluster
	

	/**
	 * Gets the pairwise jaccard similarity index.
	 *
	 * @return the pairwise jaccard similarity index
	 * @throws Exception the exception
	 */
	public double[][] getPairwiseJaccardSimilarityIndex() throws Exception{
		if(orderedinputlist.size()>1){
			BiclusterList[] arrayresults=orderedinputlist.toArray(new BiclusterList[orderedinputlist.size()]);
			return BiclusterResultsPairwiseFunctions.getPairwiseBiclusterResultsSimilarityIndex(SimilarityIndexMethod.Jaccard, arrayresults);
		}
		return null;
	}
	
	/**
	 * Gets the pairwise similarity index.
	 *
	 * @param similaritymethod the similaritymethod
	 * @return the pairwise similarity index
	 * @throws Exception the exception
	 */
	public double[][] getPairwiseSimilarityIndex(SimilarityIndexMethod similaritymethod) throws Exception{
		if(orderedinputlist.size()>1){
			BiclusterList[] arrayresults=orderedinputlist.toArray(new BiclusterList[orderedinputlist.size()]);
			return BiclusterResultsPairwiseFunctions.getPairwiseBiclusterResultsSimilarityIndex(similaritymethod, arrayresults);
		}
		return null;
	}
	
	/**
	 * Gets the jaccard pairwise similarity index summary.
	 *
	 * @return the jaccard pairwise similarity index summary
	 * @throws Exception the exception
	 */
	public String getJaccardPairwiseSimilarityIndexSummary() throws Exception{
		return getPairwiseSimilarityIndexSummary(getPairwiseJaccardSimilarityIndex());
	}
	
	/**
	 * Gets the pairwise similarity index summary.
	 *
	 * @param res the res
	 * @return the pairwise similarity index summary
	 * @throws Exception the exception
	 */
	public String getPairwiseSimilarityIndexSummary(double[][] res) throws Exception{
		
		//double[][] res=getPairwiseJaccardSimilarityIndex();
		
		if(res!=null){
			ArrayList<String> sortedmethods=getListMethodsUsedInBiclusterResults();
			
			StringBuilder str=new StringBuilder();
			str.append(""+"\t");
			for (int i = 0; i < sortedmethods.size(); i++) {
				str.append(sortedmethods.get(i));
				if(i<sortedmethods.size()-1)
					str.append("\t");
			}
			str.append("\n");
			
			for (int i = 0; i < res.length; i++) {
				str.append(sortedmethods.get(i)+"\t");
				double[] vector=res[i];
				for (int j = 0; j < vector.length; j++) {
					str.append(vector[j]);
					if(j<vector.length-1)
						str.append("\t");
				}
				str.append("\n");
			}
			
			return str.toString();
		}
		
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		try {
			return summary();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Prints the summary.
	 *
	 * @throws Exception the exception
	 */
	public void printSummary() throws Exception{
		System.out.println(summary());		
	}
	
	/**
	 * Save biclusters with ontologizer enrichment analysis cutt off.
	 *
	 * @param dirpath the dirpath
	 * @param processorproperties the processorproperties
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @throws Exception 
	 */
	public void saveBiclustersWithOntologizerEnrichmentAnalysisCuttOff(String dirpath,Properties processorproperties, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		ArrayList<BiclusterList> bicfiltered=BiclusterFilteringUtils.filterBiclustersByOntologizerEnrichmentAnalysis(orderedinputlist, processorproperties, pvaluecuttoff, useadjustedpvalues);
		
		BiclusterList totalList=new BiclusterList();
		totalList.setUsedmethod("Several_methods_ontologizerEA_pvalue_"+pvaluecuttoff+"_adjustedpvalue_"+useadjustedpvalues);
		
		for (int i = 0; i < bicfiltered.size(); i++) {
			totalList.addAll(bicfiltered.get(i));
		}
		
		totalList.writeBiclusterListToBiclustRPackageFormat(dirpath);	
	}
	
	
	
	/**
	 * Convert aggregated biclusters to JSON format.
	 *
	 * @param saveexpressiondataset the saveexpressiondataset
	 * @return the JSON object
	 */
	public JSONObject convertAggregatedBiclustersToJSONFormat(boolean saveexpressiondataset){
		
		JSONObject obj = new JSONObject();
		obj.put("Number of Biclusters", orderedinputlist.size());
		
		JSONObject biclustermethods=new JSONObject();
		
		for (Map.Entry<String, ArrayList<BiclusterList>> methodsbic: aggregatedresultsbymethod.entrySet()) {
			
			String methodname=methodsbic.getKey();
			ArrayList<BiclusterList> results=methodsbic.getValue();
			JSONObject resultsbiclustermethod=new JSONObject();
			
			for (int i = 0; i < results.size(); i++) {
				JSONObject biclistobj=results.get(i).convertBiclusterListToJSONFormat();
				resultsbiclustermethod.put(String.valueOf(i+1), biclistobj);
			}
			
			biclustermethods.put(methodname, resultsbiclustermethod);	
		}
		
		obj.put(BICLUSTERMETHODS, biclustermethods);
		
		if(saveexpressiondataset)
			obj.put(BiclusterList.BICLUSTERLISTEXPRESSIONDATATAG, expressiondataset.getDatasetJSONFormat());
		
		
		return obj;
	}
	
	/**
	 * Convert aggregated biclusters to JSON format.
	 *
	 * @return the JSON object
	 */
	public JSONObject convertAggregatedBiclustersToJSONFormat(){
		return convertAggregatedBiclustersToJSONFormat(false);
	}
	

	
	/**
	 * Write aggregated biclusters to JSON format.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeAggregatedBiclustersToJSONFormat(String filepath) throws IOException{
		JSONObject towrite=convertAggregatedBiclustersToJSONFormat();
		MTUWriterUtils.writeStringWithFileChannel(towrite.toString(),MTUFileUtils.buildFilePathWithExtension(filepath, "json"),0);
	}
	
	/**
	 * Write aggregated biclusters to JSON format.
	 *
	 * @param filepath the filepath
	 * @param appendexpressiondataset the appendexpressiondataset
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeAggregatedBiclustersToJSONFormat(String filepath,boolean appendexpressiondataset) throws IOException{
		JSONObject towrite=convertAggregatedBiclustersToJSONFormat(appendexpressiondataset);
		MTUWriterUtils.writeStringWithFileChannel(towrite.toString(),MTUFileUtils.buildFilePathWithExtension(filepath, "json"),0);
	}
	

	/**
	 * Write aggregated biclusters to JSON format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeAggregatedBiclustersToJSONFormat(String directory, String filename) throws IOException{
		JSONObject towrite=convertAggregatedBiclustersToJSONFormat();
		MTUWriterUtils.writeStringWithFileChannel(towrite.toString(),MTUFileUtils.buildFilePathWithExtension(directory, filename, "json"),0);
	}
	
	/**
	 * Write aggregated biclusters to JSON format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @param appendexpressiondataset the appendexpressiondataset
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeAggregatedBiclustersToJSONFormat(String directory, String filename, boolean appendexpressiondataset) throws IOException{
		JSONObject towrite=convertAggregatedBiclustersToJSONFormat(appendexpressiondataset);
		MTUWriterUtils.writeStringWithFileChannel(towrite.toString(),MTUFileUtils.buildFilePathWithExtension(directory, filename, "json"),0);
	}
	
	
	/**
	 * Load aggregated bicluster list from JSON object.
	 *
	 * @param obj the obj
	 * @return the aggregated biclusters
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromJSONObject(JSONObject obj){
		
		AggregatedBiclusters agbic=new AggregatedBiclusters();
		
	
		if(obj.has(BICLUSTERMETHODS)){
			
			if(obj.has(BiclusterList.BICLUSTERLISTEXPRESSIONDATATAG)){
				LogMessageCenter.getLogger().addInfoMessage("loading gene expression data.");
				ExpressionData data=ExpressionData.getExpressionDatasetFromJSONOject(obj.getJSONObject(BiclusterList.BICLUSTERLISTEXPRESSIONDATATAG));
				agbic.setExpressionData(data);
			}
			
			
			LogMessageCenter.getLogger().addInfoMessage("The json file contains a aggregated list of biclusters, loading file...");
			JSONObject biclustermethodresults=obj.getJSONObject(BICLUSTERMETHODS);
			for (String methodname : biclustermethodresults.keySet()) {
				JSONObject methodresults=biclustermethodresults.getJSONObject(methodname);
			
				TreeSet<Integer> orderedkeys=new TreeSet<>();
				for (String key : methodresults.keySet()) {
					orderedkeys.add(Integer.valueOf(key));
				}
			
				for (Integer intkey : orderedkeys) {
					JSONObject bicobj=methodresults.getJSONObject(String.valueOf(intkey));
					BiclusterList bic=BiclusterList.loadBiclusterListFromJSONObject(bicobj);
					agbic.addBiclusterResultList(methodname, bic);
				}
			
			}
			
			LogMessageCenter.getLogger().addInfoMessage("Successfully loading");
		}
		else{
			LogMessageCenter.getLogger().addInfoMessage("The json file contains a list of biclusters, loading file...");
			BiclusterList list=BiclusterList.loadBiclusterListFromJSONObject(obj);
			agbic.addBiclusterResultList(list);
			if(agbic.getExpressionDataset()==null && list.getAnalysedDataset()!=null)
				agbic.setExpressionData(list.getAnalysedDataset());
			LogMessageCenter.getLogger().addInfoMessage("Successfully loading, total biclusters: "+agbic.getNumberBiclusters());
		}
		
		return agbic;
	}
	
	
	/**
	 * Load aggregated bicluster list from JSON file.
	 *
	 * @param filepath the filepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromJSONFile(String filepath) throws FileNotFoundException{
		return loadAggregatedBiclusterListFromJSONObject(MTUJsonIOUtils.readJsonFile(filepath));
	}
	

	/**
	 * Load aggregate bicluster list from multi files biclust R package output format.
	 *
	 * @param filewithfilepaths the filewithfilepaths
	 * @param originalexpressionsetfilepath the originalexpressionsetfilepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregateBiclusterListFromMultiFilesBiclustRPackageOutputFormat(String filewithfilepaths, String originalexpressionsetfilepath) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		return BiclusterFilteringUtils.loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(filewithfilepaths, originalexpressionsetfilepath);
	}
	
	/**
	 * Load aggregate bicluster list from single file biclust R package output format.
	 *
	 * @param filepath the filepath
	 * @param originalexpressionsetfilepath the originalexpressionsetfilepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregateBiclusterListFromSingleFileBiclustRPackageOutputFormat(String filepath, String originalexpressionsetfilepath) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		return BiclusterFilteringUtils.loadAggregatedBiclusterFromBiclustRPackageOutputFormat(filepath, originalexpressionsetfilepath);
	}
	



}
