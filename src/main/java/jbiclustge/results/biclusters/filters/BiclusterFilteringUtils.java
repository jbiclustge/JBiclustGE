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
package jbiclustge.results.biclusters.filters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.javatuples.Pair;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.ontologizer.MultiThreadOntologizerEnrichmentAnalyser;
import jbiclustge.enrichmentanalysistools.ontologizer.OntologizerEnrichmentAnalyser;
import jbiclustge.results.biclusters.aggregatedfunctions.AggregatedBiclusters;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.enriched.EnrichedBiclusterList;
import pt.ornrocha.arrays.MTUMatrixUtils;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.mathutils.MTUStatisticsUtils;
import smile.imputation.MissingValueImputationException;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterFilteringUtils.
 */
public class BiclusterFilteringUtils {
	
	
	

	/**
	 * Filter biclusters by enrichment analysis.
	 *
	 * @param listtofilter the listtofilter
	 * @param processor the processor
	 * @param processorproperties the processorproperties
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @return the array list
	 * @throws Exception 
	 */
	public static ArrayList<BiclusterList> filterBiclustersByEnrichmentAnalysis(ArrayList<BiclusterList> listtofilter,EnrichmentAnalyserProcessor processor, Properties processorproperties, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		
		ArrayList<BiclusterList> res=new ArrayList<>();
		
		if(processorproperties!=null)
			processor.setProperties(processorproperties);
		
		for (int i = 0; i < listtofilter.size(); i++) {
			BiclusterList toprocess=listtofilter.get(i);
			LogMessageCenter.getLogger().toClass(AggregatedBiclusters.class).addDebugMessage("executing enrichment analysis of "+toprocess.size()+" biclusters of "+toprocess.getUsedmethod());
			EnrichmentAnalyserProcessor analyser=processor.copyWorkingInstance();
			analyser.setBiclusteringResultsToAnalyse(toprocess);
			analyser.run();
			EnrichmentAnalysisResultList results=analyser.getEnrichmentAnalysisResults();
			res.add(results.getBiclusterListWithPvalueCuttOff(pvaluecuttoff, useadjustedpvalues));
			LogMessageCenter.getLogger().toClass(AggregatedBiclusters.class).addDebugMessage("The analysis of "+toprocess.size()+" biclusters, were successfully completed ");
		}
		
        return res;
	}
	
	
	/**
	 * Filter biclusters by ontologizer enrichment analysis.
	 *
	 * @param listtofilter the listtofilter
	 * @param processorproperties the processorproperties
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @return the array list
	 * @throws Exception 
	 */
	public static ArrayList<BiclusterList>filterBiclustersByOntologizerEnrichmentAnalysis(ArrayList<BiclusterList> listtofilter, Properties processorproperties, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		EnrichmentAnalyserProcessor processor =new OntologizerEnrichmentAnalyser();
		return filterBiclustersByEnrichmentAnalysis(listtofilter, processor, processorproperties, pvaluecuttoff, useadjustedpvalues);
		
	}
	
	

	/**
	 * Filter biclusters by enrichment analysis multi thread.
	 *
	 * @param listtofilter the listtofilter
	 * @param processor the processor
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @return the array list
	 * @throws Exception the exception
	 */
	public static ArrayList<BiclusterList> filterBiclustersByEnrichmentAnalysisMultiThread(ArrayList<BiclusterList> listtofilter,MultiThreadEnrichmentAnalyserProcessor processor, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		
		ArrayList<BiclusterList> res=new ArrayList<>();
		
		processor.setBiclusterToProcess(listtofilter);
		processor.run();
		
		ArrayList<EnrichmentAnalysisResultList> results=processor.getEnrichmentAnalysisResults();
		
		for (int i = 0; i < results.size(); i++) {
			res.add(results.get(i).getBiclusterListWithPvalueCuttOff(pvaluecuttoff, useadjustedpvalues));
			LogMessageCenter.getLogger().toClass(AggregatedBiclusters.class).addDebugMessage("The analysis of "+i+" bicluster results, were successfully completed ");
		}

        return res;
	}
	
	
	/**
	 * Filter biclusters by multi thead ontologizer enrichment analysis.
	 *
	 * @param listtofilter the listtofilter
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param processorproperties the processorproperties
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @return the array list
	 * @throws Exception the exception
	 */
	public static ArrayList<BiclusterList>filterBiclustersByMultiTheadOntologizerEnrichmentAnalysis(ArrayList<BiclusterList> listtofilter,int simultaneousprocesses, Properties processorproperties, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		MultiThreadOntologizerEnrichmentAnalyser processor=new MultiThreadOntologizerEnrichmentAnalyser(simultaneousprocesses, processorproperties);
		return filterBiclustersByEnrichmentAnalysisMultiThread(listtofilter, processor, pvaluecuttoff, useadjustedpvalues);
		
	}
	
	
	
	/**
	 * Gets the enriched biclusters list by by enrichment analysis multi thread.
	 *
	 * @param listtofilter the listtofilter
	 * @param processor the processor
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @return the enriched biclusters list by by enrichment analysis multi thread
	 * @throws Exception the exception
	 */
	public static ArrayList<EnrichedBiclusterList> getEnrichedBiclustersListByByEnrichmentAnalysisMultiThread(ArrayList<BiclusterList> listtofilter,MultiThreadEnrichmentAnalyserProcessor processor, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		
		ArrayList<EnrichedBiclusterList> res=new ArrayList<>();
		
		processor.setBiclusterToProcess(listtofilter);
		processor.run();
		
		ArrayList<EnrichmentAnalysisResultList> results=processor.getEnrichmentAnalysisResults();
		
		for (int i = 0; i < results.size(); i++) {
			EnrichmentAnalysisResultList singleres=results.get(i);
			
			EnrichedBiclusterList enriched=singleres.filterAndProcessResults(pvaluecuttoff, useadjustedpvalues).getEnrichedBiclusterList();
			res.add(enriched);
			
		}
	
		return res;
	}
	
	
	
	/**
	 * Filter biclusters to enriched bicluster list by multi thead ontologizer enrichment analysis.
	 *
	 * @param listtofilter the listtofilter
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param processorproperties the processorproperties
	 * @param pvaluecuttoff the pvaluecuttoff
	 * @param useadjustedpvalues the useadjustedpvalues
	 * @return the array list
	 * @throws Exception the exception
	 */
	public static ArrayList<EnrichedBiclusterList>filterBiclustersToEnrichedBiclusterListByMultiTheadOntologizerEnrichmentAnalysis(ArrayList<BiclusterList> listtofilter,int simultaneousprocesses, Properties processorproperties, double pvaluecuttoff, boolean useadjustedpvalues) throws Exception{
		MultiThreadOntologizerEnrichmentAnalyser processor=new MultiThreadOntologizerEnrichmentAnalyser(simultaneousprocesses, processorproperties);
		return getEnrichedBiclustersListByByEnrichmentAnalysisMultiThread(listtofilter, processor, pvaluecuttoff, useadjustedpvalues);
		
	}
	
	


	/**
	 * Load aggregated bicluster list from biclust R package output format.
	 *
	 * @param filewithfilespaths the filewithfilespaths
	 * @param originalexpressiondatasetfilepath the originalexpressiondatasetfilepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(String filewithfilespaths, String originalexpressiondatasetfilepath) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		ArrayList<String> filepaths=(ArrayList<String>) MTUReadUtils.readFileLines(filewithfilespaths);
		return BiclusterFilteringUtils.loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(filepaths, originalexpressiondatasetfilepath);
	}
	


	/**
	 * Load aggregated bicluster from biclust R package output format.
	 *
	 * @param filepath the filepath
	 * @param originalexpressiondatasetfilepath the originalexpressiondatasetfilepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterFromBiclustRPackageOutputFormat(String filepath, String originalexpressiondatasetfilepath) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		ArrayList<String> filepaths=new ArrayList<>();
		filepaths.add(filepath);
		return BiclusterFilteringUtils.loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(filepaths, originalexpressiondatasetfilepath);
	}


	/**
	 * Load aggregated bicluster list from multiple files.
	 *
	 * @param filespath the filespath
	 * @param isjsonfiles the isjsonfiles
	 * @param originalexpressiondatasetfilepath the originalexpressiondatasetfilepath
	 * @param delimiter the delimiter
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromMultipleFiles(ArrayList<String> filespath,boolean isjsonfiles, String originalexpressiondatasetfilepath,String delimiter) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		
		ExpressionData originaldata=null;
		
		if(originalexpressiondatasetfilepath!=null){
			
			if(delimiter!=null)
				originaldata=ExpressionData.loadDatasetWithDelimiter(originalexpressiondatasetfilepath, delimiter, null);
			else
				originaldata=ExpressionData.loadDataset(originalexpressiondatasetfilepath, null);
		}
		if(isjsonfiles)
			return loadAggregatedBiclusterListFromJSONFilesFormat(filespath, originaldata);
		else
			return loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(filespath, originaldata);
	}


	/**
	 * Load aggregated bicluster list from biclust R package output format.
	 *
	 * @param filespath the filespath
	 * @param originalexpressiondatasetfilepath the originalexpressiondatasetfilepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(ArrayList<String> filespath, String originalexpressiondatasetfilepath) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		return loadAggregatedBiclusterListFromMultipleFiles(filespath,false, originalexpressiondatasetfilepath, null);
	}


	/**
	 * Load aggregated bicluster list from biclust R package output format.
	 *
	 * @param filespath the filespath
	 * @param data the data
	 * @return the aggregated biclusters
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromBiclustRPackageOutputFormat(ArrayList<String> filespath, ExpressionData data) throws IOException, ParseException, MissingValueImputationException{
		
		AggregatedBiclusters aggbiclusters=new AggregatedBiclusters();
		for (int i = 0; i < filespath.size(); i++) {
			LogMessageCenter.getLogger().toClass(BiclusterFilteringUtils.class).addInfoMessage("Loading biclust format file "+i+" of "+filespath.size());
			if(filespath.get(i)!=null && !filespath.get(i).isEmpty()){
				BiclusterList listbicres=BiclusterList.importBiclustersFromBiclustRPackageOutputFormat(filespath.get(i), data);
				aggbiclusters.addBiclusterResultList(listbicres);
			}
		}
		return aggbiclusters;
	}
	

	/**
	 * Load aggregated bicluster list from JSON files format.
	 *
	 * @param filespath the filespath
	 * @param data the data
	 * @return the aggregated biclusters
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromJSONFilesFormat(ArrayList<String> filespath, ExpressionData data) throws IOException, ParseException, MissingValueImputationException{
		
		AggregatedBiclusters aggbiclusters=new AggregatedBiclusters();
		for (int i = 0; i < filespath.size(); i++) {
			LogMessageCenter.getLogger().toClass(BiclusterFilteringUtils.class).addInfoMessage("Loading json file "+i+" of "+filespath.size());
			if(filespath.get(i)!=null && !filespath.get(i).isEmpty()){
				BiclusterList listbicres=BiclusterList.imporBiclusterListFromJBiclustGEJSONFile(filespath.get(i), data);
				aggbiclusters.addBiclusterResultList(listbicres);
			}
		}
		return aggbiclusters;
	}
	
	/**
	 * Load aggregated bicluster list from multiple JSON files.
	 *
	 * @param filewithfilespaths the filewithfilespaths
	 * @param originalexpressiondatasetfilepath the originalexpressiondatasetfilepath
	 * @return the aggregated biclusters
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromMultipleJSONFiles(String filewithfilespaths, String originalexpressiondatasetfilepath) throws IOException, ParseException, MissingValueImputationException{
		ArrayList<String> filepaths=(ArrayList<String>) MTUReadUtils.readFileLines(filewithfilespaths);
		return loadAggregatedBiclusterListFromMultipleJSONFiles(filepaths, originalexpressiondatasetfilepath);
	}
	
	/**
	 * Load aggregated bicluster list from multiple JSON files.
	 *
	 * @param filepaths the filepaths
	 * @param originalexpressiondatasetfilepath the originalexpressiondatasetfilepath
	 * @return the aggregated biclusters
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static AggregatedBiclusters loadAggregatedBiclusterListFromMultipleJSONFiles(ArrayList<String> filepaths, String originalexpressiondatasetfilepath) throws FileNotFoundException, IOException, ParseException, MissingValueImputationException{
		   return loadAggregatedBiclusterListFromMultipleFiles(filepaths, true, originalexpressiondatasetfilepath, null);
	}
	
	
	
	/**
	 * Filter columns with percentil cutoff.
	 *
	 * @param freqmatrix the freqmatrix
	 * @param percentil the percentil
	 * @return the pair
	 */
	public static Pair<ArrayList<Integer>, int[][]> filterColumnsWithPercentilCutoff(int[][] freqmatrix,double percentil){
		
		double[][] matd=MTUMatrixUtils.convertIntToDoubleMatrix(freqmatrix);
		
		double[] columnmeans=MTUStatisticsUtils.getMatrixColumnsMean(matd);
		
		double perccutoff=new DescriptiveStatistics(columnmeans).getPercentile(percentil);
		
		ArrayList<Integer> goodconditionindexes=new ArrayList<>();
		
		for (int i = 0; i < columnmeans.length; i++) {
			if(columnmeans[i]>=perccutoff)
				goodconditionindexes.add(i);
			
		}
		
		int[][] resized=new int[freqmatrix.length][goodconditionindexes.size()];
		
		for (int i = 0; i < freqmatrix.length; i++) {
			int[] row=freqmatrix[i];
			int n=0;
			for (int j = 0; j < row.length; j++) {
				if(goodconditionindexes.contains(j)){
					resized[i][n]=row[j];
					n++;
				}
			}
		}
		
		return new Pair<ArrayList<Integer>, int[][]>(goodconditionindexes, resized);
		
	}
	
	
	
	
	
	/**
	 * Filter columns with low items frequency.
	 *
	 * @param freqmatrix the freqmatrix
	 * @param freqvaluethreshold the freqvaluethreshold
	 * @param percentagecutoff the percentagecutoff
	 * @return the pair
	 */
	public static Pair<ArrayList<Integer>, int[][]> filterColumnsWithLowItemsFrequency(int[][] freqmatrix, int freqvaluethreshold, double percentagecutoff){
		
		if(percentagecutoff>1)
			percentagecutoff=percentagecutoff/100;
		
		ArrayList<Integer> columnstoremove=new ArrayList<>();
		
		for (int i = 0; i < freqmatrix[0].length; i++) {
			ArrayList<Integer> outcolvalues=new ArrayList<>();
			for (int j = 0; j < freqmatrix.length; j++) {
				int value=freqmatrix[j][i];
				if(value<freqvaluethreshold)
					outcolvalues.add(value);
			}
			
			double outpercentage=(double)outcolvalues.size()/freqmatrix.length;
			LogMessageCenter.getLogger().toClass(BiclusterFilteringUtils.class).addTraceMessage("Percentage zeros: "+outpercentage+"  max percentage: "+percentagecutoff);
			if(outpercentage>percentagecutoff)
				columnstoremove.add(i);
		}
		
		int[][] res=new int[freqmatrix.length][(freqmatrix[0].length-columnstoremove.size())]; 

		for (int i = 0; i < freqmatrix.length; i++) {
			int[] row=freqmatrix[i];
			int n=0;
			for (int j = 0; j < row.length; j++) {
				if(!columnstoremove.contains(j)){
					res[i][n]=row[j];
					n++;
				}
			}
			
		}
		return new Pair<ArrayList<Integer>, int[][]>(columnstoremove, res);
	}
	
	

	/**
	 * Filter rows with low items frequency.
	 *
	 * @param freqmatrix the freqmatrix
	 * @param freqvaluethreshold the freqvaluethreshold
	 * @param percentagecutoff the percentagecutoff
	 * @return the pair
	 */
	public static  Pair<ArrayList<Integer>, int[][]> filterRowsWithLowItemsFrequency(int[][] freqmatrix, int freqvaluethreshold, double percentagecutoff){
		
		if(percentagecutoff>1)
			percentagecutoff=percentagecutoff/100;
		
		ArrayList<Integer> rowstoremove=new ArrayList<>();
		
		for (int i = 0; i < freqmatrix.length; i++) {
			ArrayList<Integer> outrowvalues=new ArrayList<>();
			for (int j = 0; j < freqmatrix[0].length; j++) {
				int value=freqmatrix[i][j];
				if(value<freqvaluethreshold)
					outrowvalues.add(value);
			}
			
			double outpercentage=(double)outrowvalues.size()/freqmatrix[0].length;
			LogMessageCenter.getLogger().toClass(BiclusterFilteringUtils.class).addTraceMessage("Percentage less value: "+outpercentage+"  max percentage: "+percentagecutoff);
			if(outpercentage>percentagecutoff)
				rowstoremove.add(i);
			
		}

		int[][] res=new int[(freqmatrix.length-rowstoremove.size())][freqmatrix[0].length]; 

		int n=0;
		for (int i = 0; i < freqmatrix.length; i++) {
			int[] row=freqmatrix[i];
			if(!rowstoremove.contains(i)){
				res[n]=row;
				n++;
			}
				
		}
		return new Pair<ArrayList<Integer>, int[][]>(rowstoremove, res);
	}
	
	
	
	/**
	 * Gets the frequency matrix print format.
	 *
	 * @param freq the freq
	 * @param genenames the genenames
	 * @param conditionnames the conditionnames
	 * @return the frequency matrix print format
	 */
	public static String getFrequencyMatrixPrintFormat(int[][] freq, ArrayList<String> genenames, ArrayList<String> conditionnames){
		
		StringBuilder str=new StringBuilder();
		
		if(genenames!=null && conditionnames!=null)
			str.append("ids"+"\t");
		if(conditionnames!=null){
			for (int i = 0; i < freq[0].length; i++) {
				str.append(conditionnames.get(i));
				if(i<freq[0].length-1)
					str.append("\t");
			}
			str.append("\n");
		}
		
		
		for (int i = 0; i < freq.length; i++) {
			if(genenames!=null){
				String gene=genenames.get(i);
				str.append(gene+"\t");
			}
			int[] rowgene=freq[i];
			for (int j = 0; j < rowgene.length; j++) {
				str.append(rowgene[j]);
				if(j<rowgene.length-1)
					str.append("\t");
			}
			str.append("\n");
		}
		
		return str.toString();
	}
	
	/**
	 * Save frequency matrix to file.
	 *
	 * @param filepath the filepath
	 * @param freq the freq
	 * @param genenames the genenames
	 * @param conditionnames the conditionnames
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void saveFrequencyMatrixToFile(String filepath, int[][] freq, ArrayList<String> genenames, ArrayList<String> conditionnames) throws IOException{
		String outprint=getFrequencyMatrixPrintFormat(freq, genenames, conditionnames);
		MTUWriterUtils.writeStringWithFileChannel(outprint, filepath, 0);
	}

}
