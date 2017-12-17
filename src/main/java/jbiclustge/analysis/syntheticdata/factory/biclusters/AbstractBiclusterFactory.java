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
package jbiclustge.analysis.syntheticdata.factory.biclusters;

import java.util.ArrayList;

import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;

import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbstractBicluster objects.
 */
public abstract class AbstractBiclusterFactory {
	
	/** The biclusterdatadistributionfactory. */
	/*protected int nrows;
	protected int ncols;*/
	protected ISyntheticDataDistributionFactory biclusterdatadistributionfactory;
	
	/** The bicluster. */
	protected INDArray bicluster;
	
	/**
	 * Instantiates a new abstract bicluster factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 */
	public AbstractBiclusterFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory){
		this.biclusterdatadistributionfactory=biclusterdatadistributionfactory;
	}
	
	/**
	 * Gets the type of bicluster.
	 *
	 * @return the type of bicluster
	 */
	public abstract TypeBicluster getTypeOfBicluster();
	
	/**
	 * Builds the bicluster without both dim overlap.
	 *
	 * @param nrows the nrows
	 * @param ncols the ncols
	 * @return the IND array
	 */
	public abstract INDArray buildBiclusterWithoutBothDimOverlap(int nrows, int ncols);
	
	/**
	 * Builds the bicluster set without both dim overlap.
	 *
	 * @param shapebiclusters the shapebiclusters
	 * @param onedimoverlap the onedimoverlap
	 * @return the array list
	 */
	public abstract ArrayList<INDArray> buildBiclusterSetWithoutBothDimOverlap(ArrayList<Pair<Integer, Integer>> shapebiclusters, boolean onedimoverlap);
	
	/**
	 * Builds the bicluster set with both dim overlap.
	 *
	 * @param datanumberrows the datanumberrows
	 * @param datanumbercolumns the datanumbercolumns
	 * @param shapebiclusters the shapebiclusters
	 * @param rowranges the rowranges
	 * @param colranges the colranges
	 * @return the array list
	 */
	public abstract ArrayList<INDArray> buildBiclusterSetWithBothDimOverlap(int datanumberrows, int datanumbercolumns, ArrayList<Pair<Integer, Integer>> shapebiclusters, ArrayList<Pair<Integer, Integer>> rowranges, ArrayList<Pair<Integer, Integer>> colranges);
	
	
	/*public INDArray getBicluster(){
		if(bicluster==null)
			buildBicluster();
		return bicluster;
	}
	
	
	public double[][] getBiclusterDoubleMatrix(){
		INDArray bic=getBicluster();
		return ND4JUtils.getDoubleMatrix(bic);
	}*/
	
	

}
