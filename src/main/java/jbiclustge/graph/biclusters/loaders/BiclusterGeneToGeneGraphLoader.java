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
package jbiclustge.graph.biclusters.loaders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import jbiclustge.graph.biclusters.BiclusterGeneToGeneGraph;
import jbiclustge.results.biclusters.aggregatedfunctions.AggregatedBiclusters;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterGeneToGeneGraphLoader.
 */
public class BiclusterGeneToGeneGraphLoader {
	
	
	
/*	public static void loadBiclusterResultIntoGeneToGeneGraph(BiclusterGeneToGeneGraph grh, BiclusterResult result){
		ArrayList<String> genes=result.getGeneNames();
		System.out.println("Connecting "+genes.size()+" genes");
		grh.addVertexList(genes);
		Queue<String> queue=new LinkedList<>();
		queue.addAll(genes);
		while (!queue.isEmpty()) {
			String topgenename= queue.poll();
			Iterator<String> g=queue.iterator();
			while (g.hasNext()) {
				String nextgenename= g.next();
				System.out.println("Adding: "+topgenename+" and "+nextgenename);
				grh.addUndirectedGene2GeneEdge(topgenename, nextgenename);
			}	
		}
	}*/
	
	/**
 * Load bicluster result into gene to gene graph.
 *
 * @param grh the grh
 * @param result the result
 */
public static void loadBiclusterResultIntoGeneToGeneGraph(BiclusterGeneToGeneGraph grh, BiclusterResult result){
		ArrayList<String> genes=result.getGeneNames();
		System.out.println("Connecting "+genes.size()+" genes");
		
		for (int i = 0; i < genes.size()-1; i++) {
			String gene1id=genes.get(i);
			for (int j = 1; j < genes.size(); j++) {
				String gene2id=genes.get(j);
				//System.out.println("Adding: "+gene1id+" and "+gene2id);
				grh.addUndirectedGene2GeneEdge(gene1id, gene2id);
			}
			
		}

	}
	
	
	/**
	 * Load gene to gene graph from bicluster list.
	 *
	 * @param graphname the graphname
	 * @param biclist the biclist
	 * @return the bicluster gene to gene graph
	 */
	public static BiclusterGeneToGeneGraph loadGeneToGeneGraphFromBiclusterList(String graphname, BiclusterList biclist){
		BiclusterGeneToGeneGraph graph=new BiclusterGeneToGeneGraph(graphname);
		for (int i = 0; i < biclist.size(); i++) {
			BiclusterResult bic=biclist.get(i);
			loadBiclusterResultIntoGeneToGeneGraph(graph, bic);
		}
		return graph;
	}
	
	
	/**
	 * Load gene to gene graph from aggregated bicluster.
	 *
	 * @param graphname the graphname
	 * @param list the list
	 * @return the bicluster gene to gene graph
	 */
	public static BiclusterGeneToGeneGraph loadGeneToGeneGraphFromAggregatedBicluster(String graphname, AggregatedBiclusters list){
		
		BiclusterGeneToGeneGraph graph=new BiclusterGeneToGeneGraph(graphname);
		
		ArrayList<BiclusterList> biclist=list.getListOfBiclusters();
		
		for (int i = 0; i < biclist.size(); i++) {
			BiclusterList bl=biclist.get(i);
			for (int j = 0; j < bl.size(); j++) {
				loadBiclusterResultIntoGeneToGeneGraph(graph, bl.get(j));
				System.out.println("Loading bicluster: "+j+" of list: "+i+" of total: "+biclist.size());
			}
		}
		System.out.println("Loading FINISHED ");
		return graph;
		
	}
	
	/**
	 * Load gene to gene graph from correlation linked map.
	 *
	 * @param map the map
	 * @return the bicluster gene to gene graph
	 */
	public static BiclusterGeneToGeneGraph loadGeneToGeneGraphFromCorrelationLinkedMap(LinkedHashMap<String, ArrayList<String>> map){
		
		BiclusterGeneToGeneGraph graph=new BiclusterGeneToGeneGraph("Gene To Gene correlation graph");
		for (Map.Entry<String, ArrayList<String>> elems : map.entrySet()) {
			String maingene=elems.getKey();
			ArrayList<String> correlatedgenes=elems.getValue();
			
			for (int i = 0; i < correlatedgenes.size(); i++) {
				graph.addUndirectedGene2GeneEdge(maingene, correlatedgenes.get(i));
			}
		}
		
		return graph;
	}
	

}
