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
package jbiclustge.utils.statistics;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Interface CorrelationTest.
 */
public interface CorrelationTest{
	
	 /**
 	 * Sets the sample names list.
 	 *
 	 * @param samplenames the new sample names list
 	 */
 	public void setSampleNamesList(ArrayList<String> samplenames);
	 
 	/**
 	 * Gets the sample names.
 	 *
 	 * @return the sample names
 	 */
 	public ArrayList<String> getSampleNames();
	 
 	/**
 	 * Execute.
 	 *
 	 * @throws Exception the exception
 	 */
 	public void execute() throws Exception;
	 
 	/**
 	 * Calculatepvalues.
 	 *
 	 * @return true, if successful
 	 */
 	public boolean calculatepvalues();
	 
 	/**
 	 * Gets the correlation pvalues matrix.
 	 *
 	 * @return the correlation pvalues matrix
 	 * @throws Exception the exception
 	 */
 	public double[][] getCorrelationPvaluesMatrix() throws Exception;
	 
 	/**
 	 * Gets the correlation coeficients matrix.
 	 *
 	 * @return the correlation coeficients matrix
 	 * @throws Exception the exception
 	 */
 	public double[][] getCorrelationCoeficientsMatrix() throws Exception;

}
