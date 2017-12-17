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

import java.util.ArrayList;
import java.util.Properties;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerEnrichmentAnalyserTask;
import jbiclustge.results.biclusters.containers.BiclusterList;
import pt.ornrocha.threadutils.MTUMultiThreadCallableExecutor;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiThreadOntologizerEnrichmentAnalyser.
 */
public class MultiThreadOntologizerEnrichmentAnalyser extends MultiThreadEnrichmentAnalyserProcessor{
	
	
	/** The threadcache. */
	private ArrayList<OntologizerEnrichmentAnalyserTask> threadcache;

	
	/**
	 * Instantiates a new multi thread ontologizer enrichment analyser.
	 *
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 */
	public MultiThreadOntologizerEnrichmentAnalyser(int simultaneousprocesses,Properties propertiesofprocessor) {
		super(simultaneousprocesses, propertiesofprocessor);
	}

	/**
	 * Instantiates a new multi thread ontologizer enrichment analyser.
	 *
	 * @param setsofbiclusters the setsofbiclusters
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 */
	public MultiThreadOntologizerEnrichmentAnalyser(ArrayList<BiclusterList> setsofbiclusters, int simultaneousprocesses,Properties propertiesofprocessor) {
		super(setsofbiclusters,simultaneousprocesses, propertiesofprocessor);
	}




	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor#configureThreadExecutor()
	 */
	@Override
	protected void configureThreadExecutor() {
		
		OntologizerEnrichmentAnalyser initanalyser=new OntologizerEnrichmentAnalyser(propertiesofprocessor);
		threadcache=new ArrayList<>();
		
		
		for (int i = 0; i < biclustersets.size(); i++) {
			OntologizerEnrichmentAnalyserTask thread=new OntologizerEnrichmentAnalyserTask((OntologizerEnrichmentAnalyser) initanalyser.copyWorkingInstance(), biclustersets.get(i));
			threadcache.add(thread);
		}
		
	}




	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor#executeThreads()
	 */
	@Override
	protected ArrayList<EnrichmentAnalysisResultList> executeThreads() throws Exception {
		return (ArrayList<EnrichmentAnalysisResultList>) MTUMultiThreadCallableExecutor.run(threadcache, numberprocesses);
	}
	
	
	
	
	
	
	
	
	
	

}
