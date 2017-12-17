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
package jbiclustge.execution.threadconfig;

import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterMethodThread.
 */
public class BiclusterMethodThread extends AbstractBiclusteringThread<BiclusterList>{

	/**
	 * Instantiates a new bicluster method thread.
	 *
	 * @param method the method
	 */
	public BiclusterMethodThread(AbstractBiclusteringAlgorithmCaller method) {
		super(method);
	}

	
	
	/* (non-Javadoc)
	 * @see execution.threadconfig.ITaskThread#getResults()
	 */
	@Override
	public BiclusterList getResults() {
		return results;
	}

	/* (non-Javadoc)
	 * @see execution.threadconfig.AbstractBiclusteringThread#execute()
	 */
	@Override
	protected BiclusterList execute() throws Exception {
		method.run();
		BiclusterList res=method.getBiclusterResultList();
		return res;
	}

}
