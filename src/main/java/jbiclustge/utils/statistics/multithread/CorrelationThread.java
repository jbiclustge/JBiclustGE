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

import org.javatuples.Triplet;

import jbiclustge.utils.statistics.CorrelationTestMethod;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import smile.stat.hypothesis.CorTest;

// TODO: Auto-generated Javadoc
/**
 * The Class CorrelationThread.
 */
public class CorrelationThread implements Runnable {

	/** The sample 1. */
	double[] sample1;
	
	/** The sample 2. */
	double[] sample2;
	
	/** The rowpos. */
	int rowpos;
	
	/** The colpos. */
	int colpos;
	
	/** The result. */
	CorTest result;
	
	/** The stattest. */
	CorrelationTestMethod stattest;
	
	
	/**
	 * Instantiates a new correlation thread.
	 *
	 * @param sample1 the sample 1
	 * @param sample2 the sample 2
	 * @param rowpos the rowpos
	 * @param colpos the colpos
	 * @param stattest the stattest
	 */
	public CorrelationThread(double[] sample1, double[] sample2, int rowpos, int colpos, CorrelationTestMethod stattest){
		this.sample1=sample1;
		this.sample2=sample2;
		this.rowpos=rowpos;
		this.colpos=colpos;
		this.stattest=stattest;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
	
		if(stattest.equals(CorrelationTestMethod.SPEARMAN)){
			LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Running Spearman correlation between samples: "+rowpos+" and "+colpos);
			result=CorTest.spearman(sample1, sample2);
			
		}
		else if(stattest.equals(CorrelationTestMethod.PEARSON)){
			LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Running Pearson correlation between samples: "+rowpos+" and "+colpos);
			result=CorTest.pearson(sample1, sample2);
		}
		else{
			LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Running Kendall correlation between samples: "+rowpos+" and "+colpos);
			result=CorTest.kendall(sample1, sample2);
			
		}

	}
	
	/**
	 * Gets the correlation coeficient.
	 *
	 * @return the correlation coeficient
	 */
	public double getCorrelationCoeficient(){
		return result.cor;
	}
	
	/**
	 * Gets the correlation result to matrix position.
	 *
	 * @return the correlation result to matrix position
	 */
	public Triplet<Integer, Integer, CorTest> getCorrelationResultToMatrixPosition(){
		return new Triplet<Integer, Integer, CorTest>(rowpos, colpos, result);
	}

}
