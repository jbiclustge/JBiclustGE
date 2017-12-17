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
package jbiclustge.enrichmentanalysistools.topgo;

import java.util.ArrayList;
import java.util.Properties;

import org.javatuples.Pair;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGOEnrichmentAnalyserTask;
import jbiclustge.results.biclusters.containers.BiclusterList;
import pt.ornrocha.threadutils.MTUMultiThreadCallableExecutor;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiThreadTopGOEnrichmentAnalyser.
 */
public class MultiThreadTopGOEnrichmentAnalyser extends MultiThreadEnrichmentAnalyserProcessor{

	
	/** The maxbicsbythread. */
	private int maxbicsbythread=0;
	
	/** The issinglelist. */
	private boolean issinglelist=false;
	
	
	/** The threadcache. */
	private ArrayList<TopGOEnrichmentAnalyserTask> threadcache;
	
	/**
	 * Instantiates a new multi thread top GO enrichment analyser.
	 *
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 */
	public MultiThreadTopGOEnrichmentAnalyser(int simultaneousprocesses,Properties propertiesofprocessor){
		super(simultaneousprocesses, propertiesofprocessor);
	}
	
	/**
	 * Instantiates a new multi thread top GO enrichment analyser.
	 *
	 * @param setsofbiclusters the setsofbiclusters
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 */
	public MultiThreadTopGOEnrichmentAnalyser(ArrayList<BiclusterList> setsofbiclusters, int simultaneousprocesses,Properties propertiesofprocessor) {
		super(setsofbiclusters,simultaneousprocesses, propertiesofprocessor);
	}
	
	/**
	 * Instantiates a new multi thread top GO enrichment analyser.
	 *
	 * @param biclusterlist the biclusterlist
	 * @param simultaneousprocesses the simultaneousprocesses
	 * @param propertiesofprocessor the propertiesofprocessor
	 * @param maxbiclustersbyprocess the maxbiclustersbyprocess
	 */
	public MultiThreadTopGOEnrichmentAnalyser(BiclusterList biclusterlist, int simultaneousprocesses,Properties propertiesofprocessor, int maxbiclustersbyprocess) {
		super(getSingleBicListAsArrayList(biclusterlist),simultaneousprocesses, propertiesofprocessor);
		issinglelist=true;
		this.maxbicsbythread=maxbiclustersbyprocess;
	}
	

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor#configureThreadExecutor()
	 */
	@Override
	protected void configureThreadExecutor() {
		
		threadcache=new ArrayList<>();
		
		TopGOEnrichmentAnalyser initanalyser=new TopGOEnrichmentAnalyser(propertiesofprocessor);
		
		if(issinglelist){
			
			if(maxbicsbythread>0){
				ArrayList<Pair<Integer, Integer>> ranges=splitBiclusterListInVariousSamples();
				
				for (int i = 0; i < ranges.size(); i++) {
					TopGOEnrichmentAnalyser copy=(TopGOEnrichmentAnalyser) initanalyser.copyWorkingInstance();
					copy.setBiclusteringResultsToAnalyse(biclustersets.get(0));
					copy.setRangeBiclustersResultsToAnalyse(ranges.get(i).getValue0(), ranges.get(i).getValue1());
					TopGOEnrichmentAnalyserTask t=new TopGOEnrichmentAnalyserTask(copy);
					threadcache.add(t);
				}
			}
			else{
				initanalyser.setBiclusteringResultsToAnalyse(biclustersets.get(0));
			}
		}
		else{
			
			for (int i = 0; i < biclustersets.size(); i++) {
				TopGOEnrichmentAnalyser copy=(TopGOEnrichmentAnalyser) initanalyser.copyWorkingInstance();
				copy.setBiclusteringResultsToAnalyse(biclustersets.get(i));
				threadcache.add(new TopGOEnrichmentAnalyserTask(copy));
			}
			
			
		}
		
		
	}

	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.MultiThreadEnrichmentAnalyserProcessor#executeThreads()
	 */
	@Override
	protected ArrayList<EnrichmentAnalysisResultList> executeThreads() throws Exception {
		
		ArrayList<EnrichmentAnalysisResultList> results= (ArrayList<EnrichmentAnalysisResultList>) MTUMultiThreadCallableExecutor.run(threadcache, numberprocesses);
		
		if(issinglelist){
			
			EnrichmentAnalysisResultList totallist=new EnrichmentAnalysisResultList();
			for (int i = 0; i < results.size(); i++) {
				EnrichmentAnalysisResultList single=results.get(i);
				totallist.addAll(single);
			}
		   
			ArrayList<EnrichmentAnalysisResultList> totresults=new ArrayList<>();
			totresults.add(totallist);
			return totresults;
		}
		else
		   return results;
	}
	
	
	
	/**
	 * Gets the single bic list as array list.
	 *
	 * @param biclist the biclist
	 * @return the single bic list as array list
	 */
	private static ArrayList<BiclusterList> getSingleBicListAsArrayList(BiclusterList biclist){
		
		ArrayList<BiclusterList> list=new ArrayList<>();
		list.add(biclist);
		return list;
	}
	
	/**
	 * Split bicluster list in various samples.
	 *
	 * @return the array list
	 */
	private ArrayList<Pair<Integer, Integer>> splitBiclusterListInVariousSamples(){
		int totalsize=biclustersets.get(0).getNumberBiclusters();
		
		ArrayList<Pair<Integer, Integer>> ranges=new ArrayList<>();
		
		int nsets=totalsize/maxbicsbythread;
		int moddiv=totalsize%maxbicsbythread;
		if(moddiv!=0)
			nsets+=1;
		
		int init=0;
		int end=0;
		
		for (int i = 0; i < nsets; i++) {
			end=init+maxbicsbythread;
			if(end>=totalsize)
				end=totalsize;
			
			ranges.add(new Pair<Integer, Integer>(init, end));
			init=end;
		}
		return ranges;
	}

}
