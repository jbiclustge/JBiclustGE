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

import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;

import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.props.BaseSyntheticDataproperties;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractDataModelGenerator.
 */
public abstract class AbstractDataModelGenerator {
	
	  
	  /** The dataparameters. */
  	protected BaseSyntheticDataproperties dataparameters;
	  
  	/** The initmodelmatrix. */
  	protected INDArray initmodelmatrix;
	  
  	/** The isbothdimensionoverlap. */
  	protected boolean isbothdimensionoverlap=false;
	  
	  
	  /**
  	 * Instantiates a new abstract data model generator.
  	 */
  	public AbstractDataModelGenerator(){}
	
	  /**
  	 * Instantiates a new abstract data model generator.
  	 *
  	 * @param dataparameters the dataparameters
  	 * @throws IOException Signals that an I/O exception has occurred.
  	 */
  	public AbstractDataModelGenerator(BaseSyntheticDataproperties dataparameters) throws IOException{
		  this.dataparameters=dataparameters;
		  if(dataparameters.getOverlapNumberBiclusterRows()>0 && dataparameters.getOverlapNumberBiclusterColumns()>0)
			  isbothdimensionoverlap=true;
		  this.initmodelmatrix=initializeBiclustersModel();
		  
	  }
	

	  /**
  	 * Initialize biclusters model.
  	 *
  	 * @return the IND array
  	 * @throws IOException Signals that an I/O exception has occurred.
  	 */
  	protected abstract INDArray initializeBiclustersModel() throws IOException;
	  
	  /**
  	 * Gets the row matrix model info.
  	 *
  	 * @return the row matrix model info
  	 */
  	public abstract Pair<INDArray, ArrayList<Pair<Integer, Integer>>> getRowMatrixModelInfo();
	  
  	/**
  	 * Gets the column matrix model info.
  	 *
  	 * @return the column matrix model info
  	 */
  	public abstract Pair<INDArray, ArrayList<Pair<Integer, Integer>>> getColumnMatrixModelInfo();
	  
  	/**
  	 * Generate biclusters matrix.
  	 *
  	 * @param biclusterfactory the biclusterfactory
  	 * @param biclustersnoise the biclustersnoise
  	 * @return the IND array
  	 */
  	public abstract INDArray generateBiclustersMatrix(AbstractBiclusterFactory biclusterfactory, double[] biclustersnoise);
	  
	  /**
  	 * Sets the data parameters.
  	 *
  	 * @param dataparameters the new data parameters
  	 * @throws IOException Signals that an I/O exception has occurred.
  	 */
  	public void setDataParameters(BaseSyntheticDataproperties dataparameters) throws IOException{
		  this.dataparameters=dataparameters;
		  if(dataparameters.getOverlapNumberBiclusterRows()>0 && dataparameters.getOverlapNumberBiclusterColumns()>0)
			  isbothdimensionoverlap=true;
		  this.initmodelmatrix=initializeBiclustersModel();
	  }
	  
	  /**
  	 * Gets the initial bicluster model matrix.
  	 *
  	 * @return the initial bicluster model matrix
  	 */
  	public INDArray getInitialBiclusterModelMatrix(){
		  return initmodelmatrix;
	  }

}
