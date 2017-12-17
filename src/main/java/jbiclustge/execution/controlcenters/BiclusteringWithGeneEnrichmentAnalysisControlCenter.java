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
package jbiclustge.execution.controlcenters;

import java.util.ArrayList;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.execution.tasks.BiclusteringTaskWithEnrichmentAnalysis;
import jbiclustge.reporters.BiclusteringGSEAReporterType;
import jbiclustge.results.biclusters.containers.BiclusterMethodResultsContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringWithGeneEnrichmentAnalysisControlCenter.
 */
public class BiclusteringWithGeneEnrichmentAnalysisControlCenter extends AbstractBiclusteringExecutionControlCenter<BiclusteringTaskWithEnrichmentAnalysis, BiclusterMethodResultsContainer>{

	
	/** The geneenrichmentanalyser. */
	protected EnrichmentAnalyserProcessor geneenrichmentanalyser;
	
	/** The pvalues. */
	protected ArrayList<Double> pvalues;
	
	/** The useadjustedpvalues. */
	protected boolean useadjustedpvalues=false;
	
	/** The typereporter. */
	protected BiclusteringGSEAReporterType typereporter;
	
	/** The outputdir. */
	protected String outputdir;
	
	/** The compressresults. */
	protected boolean compressresults=false;
	

	/**
	 * Instantiates a new biclustering with gene enrichment analysis control center.
	 *
	 * @param dataset the dataset
	 */
	public BiclusteringWithGeneEnrichmentAnalysisControlCenter(ExpressionData dataset){
		super(dataset);
	}
	
	
/*	public BiclusteringWithGeneEnrichmentAnalysisControlCenter(String datafile) throws Exception {
		 data=ExpressionData.loadDataset(datafile,null);
	 }
	
	 public BiclusteringWithGeneEnrichmentAnalysisControlCenter(String datafile, MissingValueImputation imputationmethod) throws Exception {
		 data=ExpressionData.loadDataset(datafile,imputationmethod);
	 }
	
	 public BiclusteringWithGeneEnrichmentAnalysisControlCenter(String datafile,String datadelimiter, ArrayList<String> rowsheaderids, ArrayList<String> columnsheaderids) throws Exception{
			data=ExpressionData.loadDatasetFileWithoutHeaders(datafile, null, rowsheaderids, columnsheaderids, datadelimiter);
		}
	
	
	 public BiclusteringWithGeneEnrichmentAnalysisControlCenter(String datafile,String datadelimiter,String rowsheaderfile, String columnsheaderfile) throws Exception{
			this(datafile,datadelimiter,(ArrayList<String>) MTUReadUtils.readFileLines(rowsheaderfile), (ArrayList<String>) MTUReadUtils.readFileLines(columnsheaderfile));
		}
	 
	 
	 public BiclusteringWithGeneEnrichmentAnalysisControlCenter(String datafile,String datadelimiter, String rowsheaderfile,int namerowsindexcolumn,String rownamefiledelimiter, String columnsheaderfile,int namecolumnindexcolumn,String columnnamefiledelimiter) throws Exception{
			this(datafile,datadelimiter,MTUReadUtils.columnFileLines(rowsheaderfile, namerowsindexcolumn, rownamefiledelimiter),MTUReadUtils.columnFileLines(columnsheaderfile, namecolumnindexcolumn, columnnamefiledelimiter));
		}*/
	 
	 /**
 * Sets the gene enrichment analyser processor.
 *
 * @param geneenrichmentprocessor the new gene enrichment analyser processor
 */
public void setGeneEnrichmentAnalyserProcessor(EnrichmentAnalyserProcessor geneenrichmentprocessor){
		 this.geneenrichmentanalyser=geneenrichmentprocessor;
	 }
	 
	 /**
 	 * Sets the reporter type.
 	 *
 	 * @param typereporter the new reporter type
 	 */
 	public void setReporterType(BiclusteringGSEAReporterType typereporter){
		 this.typereporter=typereporter;
	 }
	 
	 /**
 	 * Sets the output results directory.
 	 *
 	 * @param outputdir the new output results directory
 	 */
 	public void setOutputResultsDirectory(String outputdir){
		 this.outputdir=outputdir;
	 }
	 
	 /**
 	 * Sets the p values to analyse in enrichment analysis.
 	 *
 	 * @param pvalues the new p values to analyse in enrichment analysis
 	 */
 	public void setpValuesToAnalyseInEnrichmentAnalysis(ArrayList<Double> pvalues){
		 this.pvalues=pvalues;
	 }
	 
	 /**
 	 * Sets the p values to analyse in enrichment analysis.
 	 *
 	 * @param values the new p values to analyse in enrichment analysis
 	 */
 	public void setpValuesToAnalyseInEnrichmentAnalysis(double...values){
		 if(pvalues==null)
			 pvalues=new ArrayList<>();
		 for (double d : values) {
			pvalues.add(d);
		 }
	 }
	 
	 /**
 	 * Use adjustedpvalues.
 	 *
 	 * @param adjustedpvalues the adjustedpvalues
 	 */
 	public void useAdjustedpvalues(boolean adjustedpvalues){
			this.useadjustedpvalues=adjustedpvalues;
		}

	 
	
	/**
	 * Sets the compress results.
	 *
	 * @param compressresults the new compress results
	 */
	public void setCompressResults(boolean compressresults) {
		this.compressresults = compressresults;
	}


	/* (non-Javadoc)
	 * @see execution.controlcenters.AbstractBiclusteringExecutionControlCenter#getListOfTasks()
	 */
	@Override
	protected ArrayList<BiclusteringTaskWithEnrichmentAnalysis> getListOfTasks() {
		ArrayList<BiclusteringTaskWithEnrichmentAnalysis> tasks=new ArrayList<>();
	
		for (int i = 0; i < biclustmethods.size(); i++) {
			
			tasks.add(new BiclusteringTaskWithEnrichmentAnalysis(biclustmethods.get(i), geneenrichmentanalyser,typereporter, outputdir, pvalues, useadjustedpvalues,compressresults));
		}
		
		return tasks;
	}

	/* (non-Javadoc)
	 * @see execution.controlcenters.AbstractBiclusteringExecutionControlCenter#getResults()
	 */
	@Override
	public ArrayList<BiclusterMethodResultsContainer> getResults() {
		return results;
	}


}
