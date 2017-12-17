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
package jbiclustge.graph.biclusters;

import bionetworks.network.AbstractBiologicalNetworkgraph;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterGeneToGeneGraph.
 */
public class BiclusterGeneToGeneGraph extends AbstractBiologicalNetworkgraph{


	
	/**
	 * Instantiates a new bicluster gene to gene graph.
	 *
	 * @param name the name
	 */
	public BiclusterGeneToGeneGraph(String name){
		super(name);
	}
	
	
	/**
	 * Adds the undirected gene 2 gene edge.
	 *
	 * @param gene1 the gene 1
	 * @param gene2 the gene 2
	 */
	public void addUndirectedGene2GeneEdge(String gene1, String gene2){
		addUndirectedEdge(gene1, gene2);
		//addDirectedEdge(gene1, gene2);
	}
	

	
}
