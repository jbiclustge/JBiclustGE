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
package jbiclustge.execution.tasks;

import java.util.ArrayList;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.execution.threadconfig.BiclusteringThreadWithGeneEnrichmentAnalysis;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.reporters.BiclusteringGSEAReporterType;
import jbiclustge.results.biclusters.containers.BiclusterMethodResultsContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringTaskWithEnrichmentAnalysis.
 */
public class BiclusteringTaskWithEnrichmentAnalysis implements IBiclusteringCallable<BiclusterMethodResultsContainer>{

	/** The thread. */
	private BiclusteringThreadWithGeneEnrichmentAnalysis thread;
	
	/**
	 * Instantiates a new biclustering task with enrichment analysis.
	 *
	 * @param thread the thread
	 */
	public BiclusteringTaskWithEnrichmentAnalysis(BiclusteringThreadWithGeneEnrichmentAnalysis thread){
		this.thread=thread;
	}
	
	/**
	 * Instantiates a new biclustering task with enrichment analysis.
	 *
	 * @param method the method
	 * @param processor the processor
	 * @param typereporter the typereporter
	 * @param writeresulstofolder the writeresulstofolder
	 */
	public BiclusteringTaskWithEnrichmentAnalysis(AbstractBiclusteringAlgorithmCaller method, EnrichmentAnalyserProcessor processor,BiclusteringGSEAReporterType typereporter,String writeresulstofolder){
		thread=new BiclusteringThreadWithGeneEnrichmentAnalysis(method, processor,typereporter, writeresulstofolder);
	}
	
	/**
	 * Instantiates a new biclustering task with enrichment analysis.
	 *
	 * @param method the method
	 * @param processor the processor
	 * @param typereporter the typereporter
	 * @param writeresulstofolder the writeresulstofolder
	 * @param adjustedpvalues the adjustedpvalues
	 * @param pvalues the pvalues
	 */
	public BiclusteringTaskWithEnrichmentAnalysis(AbstractBiclusteringAlgorithmCaller method, EnrichmentAnalyserProcessor processor,BiclusteringGSEAReporterType typereporter,String writeresulstofolder,boolean adjustedpvalues, double...pvalues) {
		this(method,processor,typereporter,writeresulstofolder);
		if(pvalues!=null){
			thread.setEnrichmentAnalysisPvalueTresholds(pvalues);
			thread.useAdjustedpvalues(adjustedpvalues);
		}
		
	}
	
	
	/**
	 * Instantiates a new biclustering task with enrichment analysis.
	 *
	 * @param method the method
	 * @param processor the processor
	 * @param typereporter the typereporter
	 * @param writeresulstofolder the writeresulstofolder
	 * @param pvalues the pvalues
	 * @param adjustedpvalues the adjustedpvalues
	 */
	public BiclusteringTaskWithEnrichmentAnalysis(AbstractBiclusteringAlgorithmCaller method, EnrichmentAnalyserProcessor processor,BiclusteringGSEAReporterType typereporter,String writeresulstofolder,ArrayList<Double> pvalues, boolean adjustedpvalues) {
		this(method,processor,typereporter,writeresulstofolder);
		if(pvalues!=null){
			thread.setEnrichmentAnalysisPvalueTresholds(pvalues);
			thread.useAdjustedpvalues(adjustedpvalues);
		}
		
	}
	
	
	/**
	 * Instantiates a new biclustering task with enrichment analysis.
	 *
	 * @param method the method
	 * @param processor the processor
	 * @param typereporter the typereporter
	 * @param writeresulstofolder the writeresulstofolder
	 * @param pvalues the pvalues
	 * @param adjustedpvalues the adjustedpvalues
	 * @param compressresults the compressresults
	 */
	public BiclusteringTaskWithEnrichmentAnalysis(AbstractBiclusteringAlgorithmCaller method, 
			EnrichmentAnalyserProcessor processor,
			BiclusteringGSEAReporterType typereporter,
			String writeresulstofolder,
			ArrayList<Double> pvalues, 
			boolean adjustedpvalues,
			boolean compressresults) {
		this(method, processor, typereporter, writeresulstofolder, pvalues, adjustedpvalues);
		thread.setCompressfiles(compressresults);
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public BiclusterMethodResultsContainer call() throws Exception {
		return thread.runandgetresults();
	}

}
