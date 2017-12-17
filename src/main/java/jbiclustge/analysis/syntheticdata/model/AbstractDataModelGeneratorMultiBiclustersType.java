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
package jbiclustge.analysis.syntheticdata.model;

import java.io.IOException;
import java.util.ArrayList;

import org.nd4j.linalg.api.ndarray.INDArray;

import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.TypeBicluster;
import jbiclustge.analysis.syntheticdata.factory.props.BaseSyntheticDataproperties;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractDataModelGeneratorMultiBiclustersType.
 */
public abstract class AbstractDataModelGeneratorMultiBiclustersType extends AbstractDataModelGenerator{

	
	/**
	 * Instantiates a new abstract data model generator multi biclusters type.
	 *
	 * @param dataparameters the dataparameters
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public AbstractDataModelGeneratorMultiBiclustersType(BaseSyntheticDataproperties dataparameters) throws IOException{
		super(dataparameters);
	}
	
	
	/**
	 * Generate biclusters matrix.
	 *
	 * @param biclusterfactory the biclusterfactory
	 * @param biclustersnoise the biclustersnoise
	 * @return the IND array
	 */
	public abstract INDArray generateBiclustersMatrix(ArrayList<AbstractBiclusterFactory> biclusterfactory, double[] biclustersnoise);

	
	
	
	/**
	 * Gets the factory type form factories list.
	 *
	 * @param list the list
	 * @param typefactory the typefactory
	 * @return the factory type form factories list
	 */
	public static AbstractBiclusterFactory getFactoryTypeFormFactoriesList(ArrayList<AbstractBiclusterFactory> list, TypeBicluster typefactory){
		
		for (AbstractBiclusterFactory factory : list) {
			if(factory.getTypeOfBicluster().equals(typefactory))
				return factory;
		}
		return null;	
	}

}
