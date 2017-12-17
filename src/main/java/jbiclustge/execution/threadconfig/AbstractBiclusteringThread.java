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
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractBiclusteringThread.
 *
 * @param <T> the generic type
 */
public abstract class AbstractBiclusteringThread<T> implements ITaskThread<T>{

	
	/** The method. */
	protected AbstractBiclusteringAlgorithmCaller method;
	
	/** The results. */
	protected T results;
	
	
	/**
	 * Instantiates a new abstract biclustering thread.
	 *
	 * @param method the method
	 */
	public AbstractBiclusteringThread(AbstractBiclusteringAlgorithmCaller method) {
		this.method=method;
	}
	
	
	/**
	 * Execute.
	 *
	 * @return the t
	 * @throws Exception the exception
	 */
	protected abstract T execute() throws Exception;
	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		 try {
			results=execute();
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error: ", e);
		}
	}
	
	/**
	 * Runandgetresults.
	 *
	 * @return the t
	 */
	public T runandgetresults(){
		run();
		return results;
	}

/*	@Override
	public T getResults() {
		if(results==null)
			run();
		return results;
	}*/
	
	
	/**
 * Gets the method.
 *
 * @return the method
 */
public AbstractBiclusteringAlgorithmCaller getMethod(){
		return method;
	}
	


}
