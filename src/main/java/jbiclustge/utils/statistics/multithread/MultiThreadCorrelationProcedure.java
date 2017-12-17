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

import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import jbiclustge.utils.statistics.CorrelationTestMethod;
import pt.ornrocha.threadutils.MTUMultiThreadCallableExecutor;
import smile.stat.hypothesis.CorTest;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiThreadCorrelationProcedure.
 */
public class MultiThreadCorrelationProcedure implements MultiThreadCorrelationTest{
	
	/** The inputdata. */
	private double[][] inputdata;
	
	/** The simultaneousprocesses. */
	private int simultaneousprocesses=Runtime.getRuntime().availableProcessors();
	
	/** The correlationcoeficientsmatrix. */
	private double[][] correlationcoeficientsmatrix;
	
	/** The correlationpvaluesmatrix. */
	private double[][] correlationpvaluesmatrix;
	
	/** The toprocess. */
	private ArrayList<CorrelationTask> toprocess;
	
	/** The samplenames. */
	private ArrayList<String> samplenames;
	
	/** The statisticmethod. */
	private CorrelationTestMethod statisticmethod;
	
	
	/**
	 * Instantiates a new multi thread correlation procedure.
	 *
	 * @param inputdata the inputdata
	 * @param correlationtest the correlationtest
	 */
	public MultiThreadCorrelationProcedure(double[][] inputdata, CorrelationTestMethod correlationtest){
		this.inputdata=inputdata;
		this.statisticmethod=correlationtest;
		setup();
	}
	
	/**
	 * Instantiates a new multi thread correlation procedure.
	 *
	 * @param inputdata the inputdata
	 * @param correlationtest the correlationtest
	 * @param samplenames the samplenames
	 */
	public MultiThreadCorrelationProcedure(double[][] inputdata, CorrelationTestMethod correlationtest, ArrayList<String> samplenames){
		this(inputdata,correlationtest);
		this.samplenames=samplenames;
	}
	
	/**
	 * Instantiates a new multi thread correlation procedure.
	 *
	 * @param inputdata the inputdata
	 * @param correlationtest the correlationtest
	 */
	public MultiThreadCorrelationProcedure(int[][] inputdata, CorrelationTestMethod correlationtest){
		this.inputdata=convertMatrix(inputdata);
		this.statisticmethod=correlationtest;
		setup();
	}
	
	/**
	 * Instantiates a new multi thread correlation procedure.
	 *
	 * @param inputdata the inputdata
	 * @param correlationtest the correlationtest
	 * @param samplenames the samplenames
	 */
	public MultiThreadCorrelationProcedure(int[][] inputdata, CorrelationTestMethod correlationtest,ArrayList<String> samplenames){
		this(inputdata,correlationtest);
		this.samplenames=samplenames;
	}
	
	/* (non-Javadoc)
	 * @see utils.statistics.multithread.MultiThreadCorrelationTest#setNumberSimultaneousProcesses(int)
	 */
	public void setNumberSimultaneousProcesses(int np){
		this.simultaneousprocesses=np;
	}
	
	
	/**
	 * Convert matrix.
	 *
	 * @param data the data
	 * @return the double[][]
	 */
	private double[][] convertMatrix(int[][] data){
		
		double[][] converted=new double[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			double[] cv=Doubles.toArray(Ints.asList(data[i]));
			converted[i]=cv;
		}
		return converted;
	}
	
	
	/**
	 * Setup.
	 */
	private void setup(){
		this.toprocess=new ArrayList<>();
		
		for (int i = 0; i < inputdata.length-1; i++) {
			double[] sample1=inputdata[i];
			for (int j = i+1; j < inputdata.length; j++) {
				double[]sample2=inputdata[j];
			    CorrelationTask task=new CorrelationTask(sample1, sample2, i, j, statisticmethod);
			    toprocess.add(task);
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
		correlationcoeficientsmatrix=new double[inputdata.length][inputdata.length];
		correlationpvaluesmatrix=new double[inputdata.length][inputdata.length];
		
		for (int i = 0; i < results.size(); i++) {
			Triplet<Integer, Integer, CorTest> simpleres=results.get(i);
			double pvalue=simpleres.getValue2().pvalue;
			if(Double.isNaN(pvalue) || Double.isInfinite(pvalue))
				pvalue=0.0;
			double corr=simpleres.getValue2().cor;
			if(Double.isNaN(corr)|| Double.isInfinite(corr))
				corr=0.0;
			correlationcoeficientsmatrix[simpleres.getValue0()][simpleres.getValue1()]=corr;
			correlationpvaluesmatrix[simpleres.getValue0()][simpleres.getValue1()]=pvalue;
			
			correlationcoeficientsmatrix[simpleres.getValue1()][simpleres.getValue0()]=corr;
			correlationpvaluesmatrix[simpleres.getValue1()][simpleres.getValue0()]=pvalue;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#getCorrelationCoeficientsMatrix()
	 */
	public double[][] getCorrelationCoeficientsMatrix() throws Exception{
		if(correlationcoeficientsmatrix==null)
			execute();
		return correlationcoeficientsmatrix;
	}
	
	/* (non-Javadoc)
	 * @see utils.statistics.CorrelationTest#getCorrelationPvaluesMatrix()
	 */
	public double[][] getCorrelationPvaluesMatrix() throws Exception{
		if(correlationpvaluesmatrix==null)
			execute();
		return correlationpvaluesmatrix;
	}
	
	/**
	 * Perform correlation test.
	 *
	 * @param matrix the matrix
	 * @param correlationtest the correlationtest
	 * @param nprocesses the nprocesses
	 * @return the pair
	 * @throws Exception the exception
	 */
	public static Pair<double[][], double[][]> performCorrelationTest(int[][] matrix, CorrelationTestMethod correlationtest, int nprocesses) throws Exception{
		MultiThreadCorrelationProcedure ct=new MultiThreadCorrelationProcedure(matrix, correlationtest);
		ct.setNumberSimultaneousProcesses(nprocesses);
		ct.execute();
		return new Pair<double[][], double[][]>(ct.getCorrelationCoeficientsMatrix(), ct.getCorrelationPvaluesMatrix());
	}
	
	/**
	 * Perform correlation test.
	 *
	 * @param matrix the matrix
	 * @param correlationtest the correlationtest
	 * @param nprocesses the nprocesses
	 * @return the pair
	 * @throws Exception the exception
	 */
	public static Pair<double[][], double[][]> performCorrelationTest(double[][] matrix, CorrelationTestMethod correlationtest, int nprocesses) throws Exception{
		MultiThreadCorrelationProcedure ct=new MultiThreadCorrelationProcedure(matrix, correlationtest);
		ct.setNumberSimultaneousProcesses(nprocesses);
		ct.execute();
		return new Pair<double[][], double[][]>(ct.getCorrelationCoeficientsMatrix(), ct.getCorrelationPvaluesMatrix());
	}
	
	/**
	 * Execute correlation test.
	 *
	 * @param matrix the matrix
	 * @param correlationtest the correlationtest
	 * @param nprocesses the nprocesses
	 * @return the multi thread correlation procedure
	 * @throws Exception the exception
	 */
	public static MultiThreadCorrelationProcedure executeCorrelationTest(int[][] matrix, CorrelationTestMethod correlationtest, int nprocesses) throws Exception{
		MultiThreadCorrelationProcedure ct=new MultiThreadCorrelationProcedure(matrix, correlationtest);
		ct.setNumberSimultaneousProcesses(nprocesses);
		ct.execute();
		return ct;
	}
	
	/**
	 * Execute correlation test.
	 *
	 * @param matrix the matrix
	 * @param correlationtest the correlationtest
	 * @param nprocesses the nprocesses
	 * @return the multi thread correlation procedure
	 * @throws Exception the exception
	 */
	public static MultiThreadCorrelationProcedure executeCorrelationTest(double[][] matrix, CorrelationTestMethod correlationtest, int nprocesses) throws Exception{
		MultiThreadCorrelationProcedure ct=new MultiThreadCorrelationProcedure(matrix, correlationtest);
		ct.setNumberSimultaneousProcesses(nprocesses);
		ct.execute();
		return ct;
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


	

}
