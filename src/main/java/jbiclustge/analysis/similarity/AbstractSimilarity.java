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
package jbiclustge.analysis.similarity;

import java.util.ArrayList;

import jbiclustge.analysis.similarity.components.AnalysisTypeDimension;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractSimilarity.
 */
public abstract class AbstractSimilarity {
	
	
	/** The dimension. */
	protected AnalysisTypeDimension dimension=AnalysisTypeDimension.BOTH;
	
	/** The resultset 1. */
	protected BiclusterList resultset1;
	
	/** The resultset 2. */
	protected BiclusterList resultset2;
	
	
	/**
	 * Instantiates a new abstract similarity.
	 */
	public AbstractSimilarity(){}
	
	/**
	 * Instantiates a new abstract similarity.
	 *
	 * @param dimension the dimension
	 */
	public AbstractSimilarity(AnalysisTypeDimension dimension){
		this.dimension=dimension;
	}
	
	/**
	 * Instantiates a new abstract similarity.
	 *
	 * @param set1 the set 1
	 * @param set2 the set 2
	 */
	public AbstractSimilarity(BiclusterList set1, BiclusterList set2){
		resultset1=set1;
		resultset2=set2;
	}
	

	/**
	 * Instantiates a new abstract similarity.
	 *
	 * @param set1 the set 1
	 * @param set2 the set 2
	 * @param dimension the dimension
	 */
	public AbstractSimilarity(BiclusterList set1, BiclusterList set2, AnalysisTypeDimension dimension){
		resultset1=set1;
		resultset2=set2;
		this.dimension=dimension;
		
	}
	
	
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public abstract String getName();
	
	/**
	 * Calculate index matrix.
	 *
	 * @param set1 the set 1
	 * @param set2 the set 2
	 * @return the double[][]
	 */
	protected abstract double[][] calculateIndexMatrix(ArrayList<ArrayList<String>>  set1,ArrayList<ArrayList<String>> set2);
	
	/**
	 * Calculate index matrix join rows columns.
	 *
	 * @param rowsset1 the rowsset 1
	 * @param colsset1 the colsset 1
	 * @param rowsset2 the rowsset 2
	 * @param colsset2 the colsset 2
	 * @return the double[][]
	 */
	protected abstract double[][] calculateIndexMatrixJoinRowsColumns(ArrayList<ArrayList<String>> rowsset1,
    		ArrayList<ArrayList<String>> colsset1,
    		ArrayList<ArrayList<String>> rowsset2,
    		ArrayList<ArrayList<String>> colsset2);
	
	/**
	 * Calculate similarity.
	 *
	 * @return the object
	 * @throws Exception the exception
	 */
	public abstract Object calculateSimilarity() throws Exception;
	
	
	 /**
 	 * Calculate similarity matrix.
 	 *
 	 * @param list1 the list 1
 	 * @param list2 the list 2
 	 * @return the double[][]
 	 * @throws Exception the exception
 	 */
 	public double[][] calculateSimilarityMatrix(BiclusterList list1, BiclusterList list2) throws Exception{
	    	this.resultset1=list1;
	    	this.resultset2=list2;
			return calculateSimilarityMatrix();
		 }


	/**
	 * Calculate similarity matrix.
	 *
	 * @return the double[][]
	 * @throws Exception the exception
	 */
	public double[][] calculateSimilarityMatrix() throws Exception{
		
		if(dimension.equals(AnalysisTypeDimension.ROWS))
			return getRowsSimilarityMatrix();
		else if(dimension.equals(AnalysisTypeDimension.COLUMNS))
			return getColumnsSimilarityMatrix();
		else 
		    return  getRowsplusColumnsSimilarityMatrix();
	}
	
	
	/**
	 * Gets the rows similarity matrix.
	 *
	 * @return the rows similarity matrix
	 * @throws Exception the exception
	 */
	protected double[][] getRowsSimilarityMatrix() throws Exception{
		
		if(resultset1.size()>0 && resultset2.size()>0){

			ArrayList<ArrayList<String>> genesSet1 =resultset1.getBiclustersGeneNameList();
			ArrayList<ArrayList<String>> genesSet2 =resultset2.getBiclustersGeneNameList();
			return calculateIndexMatrix(genesSet1, genesSet2);
	
		}
		else
			throw new Exception("One list of bicluster is null,  must have at least 1 or more bicluster elements");

	}
	
	
	/**
	 * Gets the columns similarity matrix.
	 *
	 * @return the columns similarity matrix
	 * @throws Exception the exception
	 */
	protected double[][] getColumnsSimilarityMatrix() throws Exception{
		
		if(resultset1.size()>0 && resultset2.size()>0){
			
			ArrayList<ArrayList<String>> conditionsSet1 =resultset1.getBiclustersConditionsNameList();
			ArrayList<ArrayList<String>> conditionsSet2 =resultset2.getBiclustersConditionsNameList();
			return calculateIndexMatrix(conditionsSet1, conditionsSet2);
	
		}
		else
			throw new Exception("One list of bicluster is null,  must have at least 1 or more bicluster elements");

	}
	

	/**
	 * Gets the rowsplus columns similarity matrix.
	 *
	 * @return the rowsplus columns similarity matrix
	 * @throws Exception the exception
	 */
	protected double[][] getRowsplusColumnsSimilarityMatrix()throws Exception{
		
		
		ArrayList<ArrayList<String>> genesSet1 =resultset1.getBiclustersGeneNameList();
		ArrayList<ArrayList<String>> conditionsSet1 =resultset1.getBiclustersConditionsNameList();
		
		
		ArrayList<ArrayList<String>> genesSet2 =resultset2.getBiclustersGeneNameList();
		ArrayList<ArrayList<String>> conditionsSet2 =resultset2.getBiclustersConditionsNameList();
		
		
		return calculateIndexMatrixJoinRowsColumns(genesSet1, conditionsSet1, genesSet2, conditionsSet2);
		
	}
	

	


	
	

}
