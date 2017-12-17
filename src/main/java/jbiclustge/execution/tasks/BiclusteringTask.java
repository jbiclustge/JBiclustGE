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

import jbiclustge.execution.threadconfig.BiclusterMethodThread;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringTask.
 */
public class BiclusteringTask implements IBiclusteringCallable<BiclusterList>{

	/** The thread. */
	private BiclusterMethodThread thread;
	
	/**
	 * Instantiates a new biclustering task.
	 *
	 * @param method the method
	 */
	public BiclusteringTask(AbstractBiclusteringAlgorithmCaller method) {
	       this.thread=new BiclusterMethodThread(method);
		}
	
	
	/**
	 * Instantiates a new biclustering task.
	 *
	 * @param thread the thread
	 */
	public BiclusteringTask(BiclusterMethodThread thread) {
       this.thread=thread;
	}
	

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public BiclusterList call() throws Exception {
		thread.run();
		return thread.getResults();
	}

}
