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

import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import jbiclustge.utils.statistics.multithread.MultiThreadCorrelationTest;
import pt.ornrocha.threadutils.MTUMultiThreadCallableExecutor;
import smile.stat.hypothesis.CorTest;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiThreadChiSquareTest.
 */
public class MultiThreadChiSquareTest implements MultiThreadCorrelationTest{

	
	/** The inputdata. */
	private int[][] inputdata;
	
	/** The simultaneousprocesses. */
	private int simultaneousprocesses=Runtime.getRuntime().availableProcessors();
	
	/** The toprocess. */
	private ArrayList<ChiSquareTestTask> toprocess;
	
	/** The independencepvalues. */
	private double[][] independencepvalues;
	
	/** The correlationcoeficientsmatrix. */
	private double[][] correlationcoeficientsmatrix;
	
	/** The samplenames. */
	private ArrayList<String> samplenames;
	
	/**
	 * Instantiates a new multi thread chi square test.
	 *
	 * @param inputdata the inputdata
	 */
	public MultiThreadChiSquareTest(int[][] inputdata){
		this.inputdata=inputdata;
		setup();
	}
	
	/**
	 * Instantiates a new multi thread chi square test.
	 *
	 * @param inputdata the inputdata
	 * @param samplenames the samplenames
	 */
	public MultiThreadChiSquareTest(int[][] inputdata,ArrayList<String> samplenames ){
		this.samplenames=samplenames;
	}
	
	/* (non-Javadoc)
	 * @see utils.statistics.multithread.MultiThreadCorrelationTest#setNumberSimultaneousProcesses(int)
	 */
	public void setNumberSimultaneousProcesses(int np){
		this.simultaneousprocesses=np;
	}
	
	/**
	 * Setup.
	 */
	private void setup(){
		toprocess=new ArrayList<>();
		for (int i = 0; i < inputdata.length-1; i++) {
			int[] sample1=inputdata[i];
			for (int j = i+1; j < inputdata.length; j++) {
			     int[] sample2=inputdata[j];
			     ChiSquareTestTask cht=new ChiSquareTestTask(sample1, sample2, i, j);
			     toprocess.add(cht);
		     }
			
		}
	}
	
	

	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#execute()
	 */
	public void execute() throws Exception{
		if(simultaneousprocesses==-1)
			simultaneousprocesses=Runtime.getRuntime().availableProcessors();
	
		ArrayList<Triplet<Integer, Integer, CorTest>> results=(ArrayList<Triplet<Integer, Integer, CorTest>>) MTUMultiThreadCallableExecutor.run(toprocess, simultaneousprocesses);
		
		this.independencepvalues=new double[inputdata.length][inputdata.length];
		correlationcoeficientsmatrix=new double[inputdata.length][inputdata.length];
		
		for (int i = 0; i < results.size(); i++) {
			Triplet<Integer, Integer, CorTest> simpleres=results.get(i);
			double pvalue=simpleres.getValue2().pvalue;
			if(Double.isNaN(pvalue) || Double.isInfinite(pvalue))
				pvalue=0.0;
			double corr=simpleres.getValue2().cor;
			if(Double.isNaN(corr)|| Double.isInfinite(corr))
				corr=0.0;
			independencepvalues[simpleres.getValue0()][simpleres.getValue1()]=pvalue;
			independencepvalues[simpleres.getValue1()][simpleres.getValue0()]=pvalue;
			correlationcoeficientsmatrix[simpleres.getValue0()][simpleres.getValue1()]=corr;
			correlationcoeficientsmatrix[simpleres.getValue1()][simpleres.getValue0()]=corr;
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#getCorrelationPvaluesMatrix()
	 */
	@Override
	public double[][] getCorrelationPvaluesMatrix() throws Exception{
		if(independencepvalues==null)
			execute();
		return independencepvalues;
	}
	
	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#getCorrelationCoeficientsMatrix()
	 */
	@Override
	public double[][] getCorrelationCoeficientsMatrix() throws Exception {
		return correlationcoeficientsmatrix;
	}
	
	
	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#getSampleNames()
	 */
	@Override
	public ArrayList<String> getSampleNames() {
		return samplenames;
	}
	

	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#setSampleNamesList(java.util.ArrayList)
	 */
	@Override
	public void setSampleNamesList(ArrayList<String> samplenames) {
		this.samplenames=samplenames;
		
	}


	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#calculatepvalues()
	 */
	@Override
	public boolean calculatepvalues() {
		return true;
	}


	
	/**
	 * Perform correlation test.
	 *
	 * @param matrix the matrix
	 * @param nprocesses the nprocesses
	 * @return the pair
	 * @throws Exception the exception
	 */
	public static Pair<double[][], double[][]> performCorrelationTest(int[][] matrix, int nprocesses) throws Exception{
		MultiThreadChiSquareTest t=new MultiThreadChiSquareTest(matrix);
		t.setNumberSimultaneousProcesses(nprocesses);
		t.execute();
		return new Pair<double[][], double[][]>(t.getCorrelationCoeficientsMatrix(), t.getCorrelationPvaluesMatrix());
	}
	
	
	
	/**
	 * Execute chi square independence test.
	 *
	 * @param matrix the matrix
	 * @param nprocesses the nprocesses
	 * @return the double[][]
	 * @throws Exception the exception
	 */
	public static double[][] executeChiSquareIndependenceTest(int[][] matrix, int nprocesses) throws Exception{
		MultiThreadChiSquareTest t=new MultiThreadChiSquareTest(matrix);
		t.setNumberSimultaneousProcesses(nprocesses);
		t.execute();
		return t.getCorrelationPvaluesMatrix();
	}


	
	
}
