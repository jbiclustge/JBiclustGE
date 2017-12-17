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
package jbiclustge.enrichmentanalysistools.analysis;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.reporters.interfaces.IGSEABiclusteringReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterMethodResultsContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterListGeneEnrichmentAnalyser.
 */
public class BiclusterListGeneEnrichmentAnalyser {
	
	
	/** The listbiclusters. */
	private BiclusterList listbiclusters;
	
	/** The gseaprocessor. */
	private EnrichmentAnalyserProcessor gseaprocessor;
	
	/** The results. */
	private BiclusterMethodResultsContainer results;

	
	/**
	 * Instantiates a new bicluster list gene enrichment analyser.
	 *
	 * @param listbiclusters the listbiclusters
	 * @param gseaprocessor the gseaprocessor
	 */
	public BiclusterListGeneEnrichmentAnalyser(BiclusterList listbiclusters, EnrichmentAnalyserProcessor gseaprocessor){
		this.listbiclusters=listbiclusters;
		this.gseaprocessor=gseaprocessor;
	}

	
	/**
	 * Run.
	 *
	 * @return the bicluster list gene enrichment analyser
	 */
	public BiclusterListGeneEnrichmentAnalyser run() throws Exception{
		
		if(gseaprocessor!=null && listbiclusters!=null){
			gseaprocessor.setBiclusteringResultsToAnalyse(listbiclusters);
			gseaprocessor.run();
			
			EnrichmentAnalysisResultList gsearesults=gseaprocessor.getEnrichmentAnalysisResults();
			results=new BiclusterMethodResultsContainer(listbiclusters, gsearesults);
		}
		return this;
	}
	
	
	
	/**
	 * Write report.
	 *
	 * @param reporter the reporter
	 * @param outputdir the outputdir
	 * @throws Exception the exception
	 */
	public void writeReport(IGSEABiclusteringReporter reporter, String outputdir) throws Exception{
		
		if(results==null)
			run();
		reporter.setGeneEnrichmentAnalyserResults(listbiclusters,results.getEnrichmentresults());
		reporter.writetodirectory(outputdir);
	}
	
	

}
