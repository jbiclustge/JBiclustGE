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
import pt.ornrocha.mathutils.MTUMathUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclustersSorensenMatrix.
 */
public class BiclustersSorensenMatrix extends AbstractSimilarity{

	/** The name. */
	// Sorensen similarity score so for two biclusters A and B is computed: so= (2*|AnB|)/(|A|+|B|)
	public static String NAME="biclustersorensensimilaritymatrix";
	
	/**
	 * Instantiates a new biclusters sorensen matrix.
	 */
	public BiclustersSorensenMatrix() {
		super();
		
	}

	/**
	 * Instantiates a new biclusters sorensen matrix.
	 *
	 * @param dimension the dimension
	 */
	public BiclustersSorensenMatrix(AnalysisTypeDimension dimension) {
		super(dimension);
	}

	/**
	 * Instantiates a new biclusters sorensen matrix.
	 *
	 * @param set1 the set 1
	 * @param set2 the set 2
	 * @param dimension the dimension
	 */
	public BiclustersSorensenMatrix(BiclusterList set1, BiclusterList set2, AnalysisTypeDimension dimension) {
		super(set1, set2, dimension);
	}

	/**
	 * Instantiates a new biclusters sorensen matrix.
	 *
	 * @param set1 the set 1
	 * @param set2 the set 2
	 */
	public BiclustersSorensenMatrix(BiclusterList set1, BiclusterList set2) {
		super(set1, set2);
	}

	/* (non-Javadoc)
	 * @see analysis.similarity.AbstractSimilarity#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	

	/**
	 * Calculate only genes.
	 *
	 * @return the biclusters sorensen matrix
	 */
	public  BiclustersSorensenMatrix calculateOnlyGenes(){
		this.dimension=AnalysisTypeDimension.ROWS;
		return this;
	}
	
	/**
	 * Calculate only conditions.
	 *
	 * @return the biclusters sorensen matrix
	 */
	public  BiclustersSorensenMatrix calculateOnlyConditions(){
		this.dimension=AnalysisTypeDimension.COLUMNS;
		return this;
	}
	
	/**
	 * Calculate both.
	 *
	 * @return the biclusters sorensen matrix
	 */
	public  BiclustersSorensenMatrix calculateBoth(){
		this.dimension=AnalysisTypeDimension.BOTH;
		return this;
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.similarity.AbstractSimilarity#calculateIndexMatrix(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	protected double[][] calculateIndexMatrix(ArrayList<ArrayList<String>> set1, ArrayList<ArrayList<String>> set2) {
		return calculateSorensenMatrix(set1, set2);
	}
	
	 /**
 	 * Calculate sorensen matrix.
 	 *
 	 * @param set1 the set 1
 	 * @param set2 the set 2
 	 * @return the double[][]
 	 */
 	public static double[][] calculateSorensenMatrix(ArrayList<ArrayList<String>> set1, ArrayList<ArrayList<String>> set2){
			
			double[][] outmatrix=new double[set1.size()][set2.size()];
			
			
			for (int i = 0; i < set1.size(); i++) {
				
				ArrayList<String> currentrowset1=set1.get(i);
				
				
				for (int j = 0; j < set2.size(); j++) {
					
					ArrayList<String> currentrowset2=set2.get(j);
					
					int intersectionsize=MTUMathUtils.getIntersection(currentrowset1, currentrowset2).size();
					int bicunionsizes=currentrowset1.size()+currentrowset2.size();
					
					
					double sor=0;

					if(intersectionsize>0){
						sor=(double)2*intersectionsize/bicunionsizes;
					}
						
					outmatrix[i][j]=sor;	
				}

			}
			return outmatrix;

		}

	/* (non-Javadoc)
	 * @see analysis.similarity.AbstractSimilarity#calculateIndexMatrixJoinRowsColumns(java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	protected double[][] calculateIndexMatrixJoinRowsColumns(ArrayList<ArrayList<String>> rowsset1,
			ArrayList<ArrayList<String>> colsset1, ArrayList<ArrayList<String>> rowsset2,
			ArrayList<ArrayList<String>> colsset2) {
		
		return calculateSorensenMatrixConcatenatedRowsAndColumns(rowsset1, colsset1, rowsset2, colsset2);
	}
	
	
	/**
	 * Calculate sorensen matrix concatenated rows and columns.
	 *
	 * @param rowsset1 the rowsset 1
	 * @param colsset1 the colsset 1
	 * @param rowsset2 the rowsset 2
	 * @param colsset2 the colsset 2
	 * @return the double[][]
	 */
	public static double[][] calculateSorensenMatrixConcatenatedRowsAndColumns(ArrayList<ArrayList<String>> rowsset1,
    		ArrayList<ArrayList<String>> colsset1,
    		ArrayList<ArrayList<String>> rowsset2,
    		ArrayList<ArrayList<String>> colsset2){
		
  		double[][] outmatrix=new double[rowsset1.size()][rowsset2.size()];
  		
  		
  		for (int i = 0; i < rowsset1.size(); i++) {
  			
  			ArrayList<String> currentrowset1=rowsset1.get(i);
  			ArrayList<String> currentcolsset1=colsset1.get(i);
  			
  			
  			for (int j = 0; j < rowsset2.size(); j++) {
  				
  				ArrayList<String> currentrowset2=rowsset2.get(j);
  				ArrayList<String> currentcolsset2=colsset2.get(j);
  				
  				int intersectionsize=MTUMathUtils.getIntersection(currentrowset1, currentrowset2).size()
  				      * MTUMathUtils.getIntersection(currentcolsset1, currentcolsset2).size();
  				
  				int bicunionsizes=currentrowset1.size()*currentcolsset1.size()+currentrowset2.size()*currentcolsset2.size();
  			
  				double sor=0;

				if(intersectionsize>0){
					sor=(double)2*intersectionsize/bicunionsizes;
				}
					
				outmatrix[i][j]=sor;	
  			}

  		}
  		return outmatrix;

  	}

	/* (non-Javadoc)
	 * @see analysis.similarity.AbstractSimilarity#calculateSimilarity()
	 */
	@Override
	public Object calculateSimilarity() throws Exception {
		return calculateSimilarityMatrix();
	}

}
