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
import java.util.Map;
import java.util.Properties;

import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiThreadEnrichmentAnalyserProcessor.
 */
public abstract class MultiThreadEnrichmentAnalyserProcessor {
	
	
	/** The biclustersets. */
	protected ArrayList<BiclusterList> biclustersets;
	
	/** The propertiesofprocessor. */
	protected Properties propertiesofprocessor;
	
	/** The mapofprobset 2 geneid. */
	protected Map<String, String> mapofprobset2geneid;
	
	/** The numberprocesses. */
	protected int numberprocesses=1;
	
	/** The bicluster enrichment analysisresults. */
	protected ArrayList<EnrichmentAnalysisResultList> biclusterEnrichmentAnalysisresults;
	
	/**
	 * Instantiates a new multi thread enrichment analyser processor.
	 */
	public MultiThreadEnrichmentAnalyserProcessor(){
		
	}
	
	/**
	 * Instantiates a new multi thread enrichment analyser processor.
	 *
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 */
	public MultiThreadEnrichmentAnalyserProcessor(int simultaneousprocesses, Properties propertiesofprocessor){
		this.numberprocesses=simultaneousprocesses;
		this.propertiesofprocessor=propertiesofprocessor;
	}
	
	/**
	 * Instantiates a new multi thread enrichment analyser processor.
	 *
	 * @param setsofbiclusters the setsofbiclusters
	 * @param simultaneousprocesses the simultaneousprocesses
	 */
	public MultiThreadEnrichmentAnalyserProcessor(ArrayList<BiclusterList> setsofbiclusters, int simultaneousprocesses){
		this.biclustersets=setsofbiclusters;
		this.numberprocesses=simultaneousprocesses;
	}
	
	/**
	 * Instantiates a new multi thread enrichment analyser processor.
	 *
	 * @param setsofbiclusters the setsofbiclusters
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 */
	public MultiThreadEnrichmentAnalyserProcessor(ArrayList<BiclusterList> setsofbiclusters, int simultaneousprocesses, Properties propertiesofprocessor){
		this(setsofbiclusters,simultaneousprocesses);
		this.propertiesofprocessor=propertiesofprocessor;
	}
	
	/**
	 * Sets the bicluster to process.
	 *
	 * @param setsofbiclusters the new bicluster to process
	 */
	public void setBiclusterToProcess(ArrayList<BiclusterList> setsofbiclusters){
		this.biclustersets=setsofbiclusters;
	}
	
	/**
	 * Configure thread executor.
	 */
	protected abstract void configureThreadExecutor();
	
	/**
	 * Execute threads.
	 *
	 * @return the array list
	 * @throws Exception the exception
	 */
	protected abstract ArrayList<EnrichmentAnalysisResultList> executeThreads() throws Exception;
	
	
	/**
	 * Run.
	 *
	 * @throws Exception the exception
	 */
	public void run() throws Exception{
		configureThreadExecutor();
		this.biclusterEnrichmentAnalysisresults=executeThreads();
	}
	
	
	
	/**
	 * Gets the enrichment analysis results.
	 *
	 * @return the enrichment analysis results
	 */
	public ArrayList<EnrichmentAnalysisResultList> getEnrichmentAnalysisResults(){
		return biclusterEnrichmentAnalysisresults;
	}

}
