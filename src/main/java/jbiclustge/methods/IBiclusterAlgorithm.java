/************************************************************************** 
 * Copyright 2012 - 2017, Orlando Rocha (ornrocha@gmail.com)
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
 */
package jbiclustge.methods;

import java.io.IOException;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.utils.properties.AlgorithmProperties;

// TODO: Auto-generated Javadoc
/**
 * The Interface IBiclusterAlgorithm.
 */
public interface IBiclusterAlgorithm extends Runnable {
	
	/**
	 * Gets the algorithm name.
	 *
	 * @return the algorithm name
	 */
	//String getAlgorithmName();
	String getAlgorithmName();
	
	/**
	 * Gets the bicluster result list.
	 *
	 * @return the bicluster result list
	 */
	BiclusterList getBiclusterResultList();
	
	/**
	 * Gets the algorithm allowed properties.
	 *
	 * @return the algorithm allowed properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	AlgorithmProperties getAlgorithmAllowedProperties() throws IOException;
  
}
