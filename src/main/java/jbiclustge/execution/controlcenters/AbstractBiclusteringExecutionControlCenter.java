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
package jbiclustge.execution.controlcenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.execution.controlcenters.common.AbstractBiclusteringControlCenter;
import jbiclustge.execution.executors.MultiThreadBiclusteringTaskExecutor;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractBiclusteringExecutionControlCenter.
 *
 * @param <IBiclusteringCallable> the generic type
 * @param <E> the element type
 */
public abstract class AbstractBiclusteringExecutionControlCenter<IBiclusteringCallable, E> extends AbstractBiclusteringControlCenter{
	
	
	/** The simultaneousprocesses. */
	protected Integer simultaneousprocesses=null;
	
	/** The listprocess. */
	protected ArrayList<IBiclusteringCallable> listprocess;
	
	/** The results. */
	protected ArrayList<E> results;
	

	/**
	 * Instantiates a new abstract biclustering execution control center.
	 *
	 * @param data the data
	 */
	public AbstractBiclusteringExecutionControlCenter(ExpressionData data) {
		super(data);
	}
	
	
	/**
	 * Gets the list of tasks.
	 *
	 * @return the list of tasks
	 */
	protected abstract ArrayList<IBiclusteringCallable> getListOfTasks();
	
	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public abstract ArrayList<E> getResults();
	

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public <T> void execute() throws Exception{
		this.listprocess= getListOfTasks();
		results=(ArrayList<E>) MultiThreadBiclusteringTaskExecutor.run(simultaneousprocesses, (Collection<? extends Callable<T>>) listprocess);
	}
	
	/**
	 * Sets the number simultaneous processes.
	 *
	 * @param np the new number simultaneous processes
	 */
	public void setNumberSimultaneousProcesses(int np){
		this.simultaneousprocesses=np;
	}

	

	

}
