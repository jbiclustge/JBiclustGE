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
package jbiclustge.utils.statistics.multithread.chisquare;

import java.util.concurrent.Callable;

import org.javatuples.Triplet;

import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import smile.stat.hypothesis.CorTest;

// TODO: Auto-generated Javadoc
/**
 * The Class ChiSquareTestTask.
 */
public class ChiSquareTestTask implements Callable<Triplet<Integer, Integer, CorTest>>{

	
	/** The thread. */
	private ChiSquareTestThread thread;
	
	
	/**
	 * Instantiates a new chi square test task.
	 *
	 * @param sample1 the sample 1
	 * @param sample2 the sample 2
	 * @param rowpos the rowpos
	 * @param colpos the colpos
	 */
	public ChiSquareTestTask(int[] sample1, int sample2[], int rowpos, int colpos){
		this.thread=new ChiSquareTestThread(sample1, sample2, rowpos, colpos);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Triplet<Integer, Integer, CorTest> call() throws Exception {
		thread.run();
		return thread.getChiSquareResultToMatrixPosition();
	}
	
	
	
	
	
	/**
	 * The Class ChiSquareTestThread.
	 */
	private class ChiSquareTestThread implements Runnable {
		
		
		/** The sample 1. */
		int[] sample1;
		
		/** The sample 2. */
		int[] sample2;
		
		/** The rowpos. */
		int rowpos;
		
		/** The colpos. */
		int colpos;
		
		/** The result. */
		CorTest result;
		
		/**
		 * Instantiates a new chi square test thread.
		 *
		 * @param sample1 the sample 1
		 * @param sample2 the sample 2
		 * @param rowpos the rowpos
		 * @param colpos the colpos
		 */
		public ChiSquareTestThread(int[] sample1, int sample2[], int rowpos, int colpos){
			this.sample1=sample1;
			this.sample2=sample2;
			this.rowpos=rowpos;
			this.colpos=colpos;
		}
		
		
		
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Running ChiSquare correlation between samples: "+rowpos+" and "+colpos);
			int[][] inputmat=new int[2][sample1.length];
			inputmat[0]=sample1;
			inputmat[1]=sample2;
		    result=CorTest.chisq(inputmat);
			
		}
		
		/**
		 * Gets the chi square result to matrix position.
		 *
		 * @return the chi square result to matrix position
		 */
		public Triplet<Integer, Integer, CorTest> getChiSquareResultToMatrixPosition(){
			return new Triplet<Integer, Integer, CorTest>(rowpos, colpos, result);
		}

	}

}
