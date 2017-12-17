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
package jbiclustge.rtools.plotutils.components;

import java.util.ArrayList;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jrplot.rbinders.components.matrix.DefaultMatrixContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class HeatMapUtils.
 */
public class HeatMapUtils {
	
	
	/**
	 * Make bicluster matrix.
	 *
	 * @param bicluster the bicluster
	 * @param orderelements the orderelements
	 * @param indexesasnames the indexesasnames
	 * @return the default matrix container
	 */
	public static DefaultMatrixContainer makeBiclusterMatrix(BiclusterResult bicluster, boolean orderelements, boolean indexesasnames){
		
		
		  double [][] matrixvalues=null;
		   ArrayList<String> rownames=null;
		   ArrayList<String> colnames=null;
		   
		
			   if(orderelements){
				   matrixvalues=bicluster.getOrderedBiclusterExpressionDatabyRowDimension();
				   rownames=bicluster.getGeneNamesOrderedAsExpressionDataset();
				   colnames=bicluster.getConditionNamesOrderedAsExpressionDataset();
				   
			   }
			   else{
				   matrixvalues=bicluster.getBiclusterExpressionDatabyRowDimension();
				   rownames=bicluster.getGeneNames();
				   colnames=bicluster.getConditionNames();
				   
			   }
			   
			   if(indexesasnames){
				   rownames=BiclusterResult.getStringIndexesOfGeneNames(bicluster, rownames);
				   colnames=BiclusterResult.getStringIndexesOfConditionNames(bicluster, colnames);
				   
			   }

		 return DefaultMatrixContainer.create(matrixvalues, rownames, colnames);
	}
	
	/**
	 * Make bicluster matrix.
	 *
	 * @param biclusters the biclusters
	 * @param indexbicluster the indexbicluster
	 * @param orderelements the orderelements
	 * @param indexesasnames the indexesasnames
	 * @return the default matrix container
	 */
	public static DefaultMatrixContainer makeBiclusterMatrix(BiclusterList biclusters,int indexbicluster,  boolean orderelements, boolean indexesasnames){
		return makeBiclusterMatrix(biclusters.get(indexbicluster), orderelements, indexesasnames);
	}
	
	
	/**
	 * Make biclusters matrices.
	 *
	 * @param biclusters the biclusters
	 * @param orderelements the orderelements
	 * @param indexesasnames the indexesasnames
	 * @return the array list
	 */
	public static ArrayList<DefaultMatrixContainer> makeBiclustersMatrices(BiclusterList biclusters, boolean orderelements, boolean indexesasnames){
		ArrayList<DefaultMatrixContainer> matrices=new ArrayList<>();
		for (int i = 0; i < biclusters.size(); i++) {
			matrices.add(makeBiclusterMatrix(biclusters, i, orderelements, indexesasnames));
		}
		return matrices;
	}
	

}
