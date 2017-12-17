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
package jbiclustge.analysis.syntheticdata.distribution;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jbiclustge.datatools.utils.nd4j.ND4JUtils;
import pt.ornrocha.printutils.MTUPrintUtils;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating NormalDistributionData objects.
 */
public class NormalDistributionDataFactory implements ISyntheticDataDistributionFactory{

	
	/** The mean. */
	protected double mean=0;
	
	/** The sd. */
	protected double sd=1;
	

	/**
	 * Instantiates a new normal distribution data factory.
	 */
	public NormalDistributionDataFactory(){};
	

	/**
	 * Instantiates a new normal distribution data factory.
	 *
	 * @param mean the mean
	 * @param sd the sd
	 */
	public NormalDistributionDataFactory(double mean, double sd){
		this.mean=mean;
		this.sd=sd;
	};
	

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#setMean(double)
	 */
	@Override
	public void setMean(double meanvalue) {
		this.mean=meanvalue;	
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#setStandardDeviation(double)
	 */
	@Override
	public void setStandardDeviation(double sdvalue) {
		this.sd=sdvalue;
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateDoublesDataMatrix(int, int)
	 */
	@Override
	public double[][] generateDoublesDataMatrix(int nrows, int ncols) {

		NormalDistribution dist=null;
		
		if(mean==0.0 && sd==1.0)
			dist=new NormalDistribution();
		else
			dist=new NormalDistribution(mean, sd);
		
		double[][] mat=new double[nrows][ncols];
		
		for (int i = 0; i < nrows; i++) {
			double[] row=dist.sample(ncols);
			mat[i]=row;
		}
		
		return mat;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateDoublesDataMatrix(int, int, double, double)
	 */
	@Override
	public double[][] generateDoublesDataMatrix(int nrows, int ncols, double meanvalue, double sdvalue) {
		setMean(meanvalue);
        setStandardDeviation(sdvalue);
		return generateDoublesDataMatrix(nrows, ncols);
	}


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateDataMatrix(int, int)
	 */
	@Override
	public INDArray generateDataMatrix(int nrows, int ncols) {
		return Nd4j.create(generateDoublesDataMatrix(nrows, ncols));
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateDataMatrix(int, int, double, double)
	 */
	@Override
	public INDArray generateDataMatrix(int nrows, int ncols, double meanvalue, double sdvalue) {
        setMean(meanvalue);
        setStandardDeviation(sdvalue);
		return generateDataMatrix(nrows, ncols);
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateDoubleDataVector(int, double, double)
	 */
	@Override
	public double[] generateDoubleDataVector(int size,double meanvalue, double sdvalue) {
		double[][] row=generateDoublesDataMatrix(1, size,meanvalue,sdvalue);
		return row[0];
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateDoubleDataVector(int)
	 */
	@Override
	public double[] generateDoubleDataVector(int size) {
		return generateDoubleDataVector(size, mean, sd);
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateINDArrayDataVector(int, double, double)
	 */
	@Override
	public INDArray generateINDArrayDataVector(int size, double meanvalue, double sdvalue) {
		double[] row=generateDoubleDataVector(size, meanvalue, sdvalue);
		return Nd4j.create(row);
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#generateINDArrayDataVector(int)
	 */
	@Override
	public INDArray generateINDArrayDataVector(int size) {
		return generateINDArrayDataVector(size, mean, sd);
	}
	


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#getDistMeanValue()
	 */
	@Override
	public double getDistMeanValue() {
		return mean;
	}



	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory#getDistStandardDeviationValue()
	 */
	@Override
	public double getDistStandardDeviationValue() {
		return sd;
	}


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		NormalDistributionDataFactory fact=new NormalDistributionDataFactory();
		/*INDArray gener=fact.generateDataMatrix(20, 10);
		ND4JUtils.printINDArrayMatrix(gener, "\t");*/
		
		double[] ar =fact.generateDoubleDataVector(10, 0, 1);
		MTUPrintUtils.printArrayofDoubles(ar);

	}


	



	



}
