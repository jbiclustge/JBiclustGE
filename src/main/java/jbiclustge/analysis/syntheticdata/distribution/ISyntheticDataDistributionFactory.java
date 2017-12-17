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

import org.nd4j.linalg.api.ndarray.INDArray;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating ISyntheticDataDistribution objects.
 */
public interface ISyntheticDataDistributionFactory {
	
	
	
	
	/**
	 * Sets the mean.
	 *
	 * @param meanvalue the new mean
	 */
	void setMean(double meanvalue);
	
	/**
	 * Sets the standard deviation.
	 *
	 * @param sdvalue the new standard deviation
	 */
	void setStandardDeviation(double sdvalue);
	
	/**
	 * Gets the dist mean value.
	 *
	 * @return the dist mean value
	 */
	double getDistMeanValue();
	
	/**
	 * Gets the dist standard deviation value.
	 *
	 * @return the dist standard deviation value
	 */
	double getDistStandardDeviationValue();
	
	/**
	 * Generate doubles data matrix.
	 *
	 * @param nrows the nrows
	 * @param ncols the ncols
	 * @return the double[][]
	 */
	double[][] generateDoublesDataMatrix(int nrows, int ncols);
	
	/**
	 * Generate doubles data matrix.
	 *
	 * @param nrows the nrows
	 * @param ncols the ncols
	 * @param meanvalue the meanvalue
	 * @param sdvalue the sdvalue
	 * @return the double[][]
	 */
	double[][] generateDoublesDataMatrix(int nrows, int ncols,double meanvalue, double sdvalue);
	
	/**
	 * Generate data matrix.
	 *
	 * @param nrows the nrows
	 * @param ncols the ncols
	 * @return the IND array
	 */
	INDArray generateDataMatrix(int nrows, int ncols);
	
	/**
	 * Generate data matrix.
	 *
	 * @param nrows the nrows
	 * @param ncols the ncols
	 * @param meanvalue the meanvalue
	 * @param sdvalue the sdvalue
	 * @return the IND array
	 */
	INDArray generateDataMatrix(int nrows, int ncols, double meanvalue, double sdvalue);
	
	/**
	 * Generate double data vector.
	 *
	 * @param size the size
	 * @param meanvalue the meanvalue
	 * @param sdvalue the sdvalue
	 * @return the double[]
	 */
	double[] generateDoubleDataVector(int size,double meanvalue, double sdvalue);
	
	/**
	 * Generate double data vector.
	 *
	 * @param size the size
	 * @return the double[]
	 */
	double[] generateDoubleDataVector(int size);
	
	/**
	 * Generate IND array data vector.
	 *
	 * @param size the size
	 * @param meanvalue the meanvalue
	 * @param sdvalue the sdvalue
	 * @return the IND array
	 */
	INDArray generateINDArrayDataVector(int size, double meanvalue, double sdvalue);
	
	/**
	 * Generate IND array data vector.
	 *
	 * @param size the size
	 * @return the IND array
	 */
	INDArray generateINDArrayDataVector(int size);
	

}
