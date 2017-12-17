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
package jbiclustge.utils.statistics.multithread;

import java.util.concurrent.Callable;

import org.javatuples.Triplet;

import jbiclustge.utils.statistics.CorrelationTestMethod;
import smile.stat.hypothesis.CorTest;

// TODO: Auto-generated Javadoc
/**
 * The Class CorrelationTask.
 */
public class CorrelationTask implements Callable<Triplet<Integer, Integer, CorTest>>{

	
	/** The thread. */
	private CorrelationThread thread;
	
	
	
	/**
	 * Instantiates a new correlation task.
	 *
	 * @param sample1 the sample 1
	 * @param sample2 the sample 2
	 * @param rowpos the rowpos
	 * @param colpos the colpos
	 * @param stattest the stattest
	 */
	public CorrelationTask(double[] sample1, double[] sample2, int rowpos, int colpos, CorrelationTestMethod stattest){
		this.thread=new CorrelationThread(sample1, sample2, rowpos, colpos, stattest);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Triplet<Integer, Integer, CorTest> call() throws Exception {
		thread.run();
		return thread.getCorrelationResultToMatrixPosition();
	}

}
