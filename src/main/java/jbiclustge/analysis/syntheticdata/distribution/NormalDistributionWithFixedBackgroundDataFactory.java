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

// TODO: Auto-generated Javadoc
/**
 * A factory for creating NormalDistributionWithFixedBackgroundData objects.
 */
public class NormalDistributionWithFixedBackgroundDataFactory extends NormalDistributionDataFactory{
	
	
	/** The fixedbackground. */
	private int fixedbackground=10;
	
	
	/**
	 * Instantiates a new normal distribution with fixed background data factory.
	 */
	public NormalDistributionWithFixedBackgroundDataFactory(){};
	
	
	/**
	 * Instantiates a new normal distribution with fixed background data factory.
	 *
	 * @param mean the mean
	 * @param sd the sd
	 * @param background the background
	 */
	public NormalDistributionWithFixedBackgroundDataFactory(double mean, double sd, int background){
		super(mean, sd);
		this.fixedbackground=background;
	}
	
	
	/**
	 * Sets the fixed background.
	 *
	 * @param value the new fixed background
	 */
	public void setFixedBackground(int value){
		this.fixedbackground=value;
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.distribution.NormalDistributionDataFactory#generateDoublesDataMatrix(int, int)
	 */
	@Override
	public double[][] generateDoublesDataMatrix(int nrows, int ncols) {
		
		double[][] tmpmat=super.generateDoublesDataMatrix(nrows, ncols);
		
		double[][] res=new double[tmpmat.length][tmpmat[0].length];
		
		for (int i = 0; i < tmpmat.length; i++) {
			
			double[] row=tmpmat[i];
			for (int j = 0; j < row.length; j++) {
				res[i][j]=fixedbackground+row[j];
			}
			
		}
		return res;
	}
	
	
	

}
