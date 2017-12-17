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
package jbiclustge.analysis.syntheticdata.factory.builders;

import java.io.IOException;

import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;
import jbiclustge.analysis.syntheticdata.model.UnsupportedDataModelGenerator;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Interface ISyntheticDataBuilder.
 */
public interface ISyntheticDataBuilder {
	
	
	
	/**
	 * Sets the data model generator.
	 *
	 * @param modelgenerator the new data model generator
	 * @throws UnsupportedDataModelGenerator the unsupported data model generator
	 */
	public void setDataModelGenerator(AbstractDataModelGenerator modelgenerator) throws UnsupportedDataModelGenerator;
	
	/**
	 * Builds the dataset.
	 *
	 * @throws Exception the exception
	 */
	public void buildDataset() throws Exception;
	
	/**
	 * Gets the synthetic dataset.
	 *
	 * @return the synthetic dataset
	 */
	public ExpressionData getSyntheticDataset();
	
	/**
	 * Write expression dataset to file.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeExpressionDatasetToFile(String filepath)throws IOException;
	
	/**
	 * Write expected biclusters to file.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeExpectedBiclustersToFile(String filepath)throws IOException;
	
	/**
	 * Gets the expected biclusters.
	 *
	 * @return the expected biclusters
	 */
	public BiclusterList getExpectedBiclusters();
	
	/**
	 * Sets the data noise.
	 *
	 * @param noise the new data noise
	 */
	public void setDataNoise(double noise);
	
	/**
	 * Sets the shuffle data.
	 *
	 * @param shuffle the new shuffle data
	 */
	public void setShuffleData(boolean shuffle);

	

}
