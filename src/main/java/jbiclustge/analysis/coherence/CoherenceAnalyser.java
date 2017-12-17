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
package jbiclustge.analysis.coherence;

import java.util.LinkedHashMap;

import org.javatuples.Quartet;
import org.javatuples.Triplet;

import jbiclustge.analysis.similarity.components.AnalysisTypeDimension;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.arrays.MTUMatrixUtils;
import pt.ornrocha.mathutils.MTUDistanceUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class CoherenceAnalyser.
 */
public class CoherenceAnalyser {
	

	/**
	 * Constant variance.
	 *
	 * @param bic the BiclusterResult
	 * @param dimension the dimension
	 * @return constant variance value
	 */
	public static double constantVariance(BiclusterResult bic, AnalysisTypeDimension dimension){
		
		if(dimension.equals(AnalysisTypeDimension.ROWS))
			return oneDimensionConstantVariance(bic, false);
		else if(dimension.equals(AnalysisTypeDimension.COLUMNS))
			return oneDimensionConstantVariance(bic, true);
		else{
		    double rowvar=oneDimensionConstantVariance(bic, false);
		    double colvar=oneDimensionConstantVariance(bic, true);
		    int nrows=bic.getNumberGenes();
		    int ncols=bic.getNumberConditions();
		    return (nrows*rowvar+ncols*colvar)/(nrows+ncols);
		}
	}
	
	
	/**
	 * One dimension constant variance.
	 *
	 * @param bic the BiclusterResult
	 * @param iscolumns the iscolumns
	 * @return the double
	 */
	private static double oneDimensionConstantVariance(BiclusterResult bic, boolean iscolumns){
		
		double[][] datamatrix=null;
		if(iscolumns)
			datamatrix=bic.getBiclusterExpressionDatabyColunmDimension();
		else
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
		
		return matrixVariance(datamatrix);
		
	}
	
	
	
	/**
	 * Additive variance.
	 *
	 * @param bic the BiclusterResult
	 * @param dimension the dimension
	 * @return the double
	 */
	public static double additiveVariance(BiclusterResult bic, AnalysisTypeDimension dimension){
		
		if(dimension.equals(AnalysisTypeDimension.ROWS))
			return oneDimensionAdditiveVariance(bic, false);
		else if(dimension.equals(AnalysisTypeDimension.COLUMNS))
			return oneDimensionAdditiveVariance(bic, true);
		else{
		    double rowvar=oneDimensionAdditiveVariance(bic, false);
		    double colvar=oneDimensionAdditiveVariance(bic, true);
		    int nrows=bic.getNumberGenes();
		    int ncols=bic.getNumberConditions();
		    return (nrows*rowvar+ncols*colvar)/(nrows+ncols);
		}
	}
	
	

	/**
	 * One dimension additive variance.
	 *
	 * @param bic the BiclusterResult
	 * @param iscolumns the iscolumns
	 * @return the double
	 */
	private static double oneDimensionAdditiveVariance(BiclusterResult bic, boolean iscolumns){
		
		double[][] datamatrix=null;
		if(iscolumns){
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
			return matrixVariance(MTUMatrixUtils.transposeMatrix(MTUMatrixUtils.getdoubleMatrixElementsDifferenceRowByRow(datamatrix, true)));
		}
		else{
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
			return matrixVariance(MTUMatrixUtils.getdoubleMatrixElementsDifferenceColumnByRColumn(datamatrix, true));
		}	
	}
	
	

	/**
	 * Multiplicative variance.
	 *
	 * @param bic the BiclusterResult
	 * @param dimension the dimension
	 * @return the double
	 */
	public static double multiplicativeVariance(BiclusterResult bic, AnalysisTypeDimension dimension){
		
		if(dimension.equals(AnalysisTypeDimension.ROWS))
			return oneDimensionMultiplicativeVariance(bic, false);
		else if(dimension.equals(AnalysisTypeDimension.COLUMNS))
			return oneDimensionMultiplicativeVariance(bic, true);
		else{
		    double rowvar=oneDimensionMultiplicativeVariance(bic, false);
		    double colvar=oneDimensionMultiplicativeVariance(bic, true);
		    int nrows=bic.getNumberGenes();
		    int ncols=bic.getNumberConditions();
		    return (nrows*rowvar+ncols*colvar)/(nrows+ncols);
		}
	}
	
	

	/**
	 * One dimension multiplicative variance.
	 *
	 * @param bic the BiclusterResult
	 * @param iscolumns the iscolumns
	 * @return the double
	 */
	private static double oneDimensionMultiplicativeVariance(BiclusterResult bic, boolean iscolumns){
		
		double[][] datamatrix=null;
		if(iscolumns){
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
			return matrixVariance(MTUMatrixUtils.transposeMatrix(MTUMatrixUtils.getdoubleMatrixElementsRatioRowByRow(datamatrix, true)));
		}
		else{
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
			return matrixVariance(MTUMatrixUtils.getdoubleMatrixElementsRatioColumnByRColumn(datamatrix, true));
		}	
	}
	
	
	/**
	 * Sign variance.
	 *
	 * @param bic the BiclusterResult
	 * @param dimension the dimension
	 * @return the double
	 */
	public static double signVariance(BiclusterResult bic, AnalysisTypeDimension dimension){
		
		if(dimension.equals(AnalysisTypeDimension.ROWS))
			return oneDimensionSignVariance(bic, false);
		else if(dimension.equals(AnalysisTypeDimension.COLUMNS))
			return oneDimensionSignVariance(bic, true);
		else{
		    double rowvar=oneDimensionSignVariance(bic, false);
		    double colvar=oneDimensionSignVariance(bic, true);
		    int nrows=bic.getNumberGenes();
		    int ncols=bic.getNumberConditions();
		    return (nrows*rowvar+ncols*colvar)/(nrows+ncols);
		}
	}
	

	/**
	 * One dimension sign variance.
	 *
	 * @param bic the BiclusterResult
	 * @param iscolumns the iscolumns
	 * @return the double
	 */
	private static double oneDimensionSignVariance(BiclusterResult bic, boolean iscolumns){
		
		double[][] datamatrix=null;
		if(iscolumns){
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
			return matrixVariance(MTUMatrixUtils.transposeMatrix(getMatrixElementsSignProfileRowByRow(datamatrix)));
		}
		else{
			datamatrix=bic.getBiclusterExpressionDatabyRowDimension();
			return matrixVariance(getMatrixElementsSignProfileColumnByColumn(datamatrix));
		}	
	}
	

	/**
	 * Matrix variance.
	 *
	 * @param matrix the matrix
	 * @return the double
	 */
	private static double matrixVariance(double[][] matrix){
		double dist=MTUDistanceUtils.sumEuclideanDistanceRowMatrix(matrix);
		int nrows=matrix.length;
		return dist/(nrows*(nrows-1));
	}
	
	
	
	

	/**
	 * Gets the matrix elements sign profile row by row.
	 *
	 * @param matrix the matrix
	 * @return the matrix elements sign profile row by row
	 */
	public static double[][] getMatrixElementsSignProfileRowByRow(double[][] matrix){
		
		double[][] res=new double[matrix.length][matrix[0].length];
		
		for (int i = 1; i < res.length; i++) {
			double[] previousindex=matrix[i-1];
			double[] currindex=matrix[i];
			double[] store=new double[currindex.length];
			
			for (int j = 0; j < currindex.length; j++) {
				double currval=currindex[j];
				double previousval=previousindex[j];
				if(currval>previousval)
					store[j]=1.0;
				else if(currval<previousval)
					store[j]=-1.0;
				else
					store[j]=0.0;
			}
			res[i]=store;
		}
		
		return res;
	}
	
	/**
	 * Gets the matrix elements sign profile column by column.
	 *
	 * @param matrix the matrix
	 * @return the matrix elements sign profile column by column
	 */
	public static double[][] getMatrixElementsSignProfileColumnByColumn(double[][] matrix){
		double[][] trans=MTUMatrixUtils.transposeMatrix(matrix);
		double[][] res=getMatrixElementsSignProfileRowByRow(trans);
		return MTUMatrixUtils.transposeMatrix(res);
	}
	
	
	/**
	 * Gets the constant variance for bicluster list.
	 *
	 * @param biclist the biclist
	 * @param dimension the dimension
	 * @return the constant variance for bicluster list
	 */
	public static LinkedHashMap<Integer, Double> getConstantVarianceForBiclusterList(BiclusterList biclist, AnalysisTypeDimension dimension){
		LinkedHashMap<Integer, Double> res=new LinkedHashMap<>();
		for (int i = 0; i < biclist.size(); i++) {
			res.put(i, constantVariance(biclist.get(i), dimension));
		}
		return res;
	}
	
	/**
	 * Gets the additive variance for bicluster list.
	 *
	 * @param biclist the BiclusterList
	 * @param dimension the dimension
	 * @return the additive variance for bicluster list
	 */
	public static LinkedHashMap<Integer, Double> getAdditiveVarianceForBiclusterList(BiclusterList biclist, AnalysisTypeDimension dimension){
		LinkedHashMap<Integer, Double> res=new LinkedHashMap<>();
		for (int i = 0; i < biclist.size(); i++) {
			res.put(i, additiveVariance(biclist.get(i), dimension));
		}
		return res;
	}
	
	/**
	 * Gets the multiplicative variance for bicluster list.
	 *
	 * @param biclist the BiclusterList
	 * @param dimension the dimension
	 * @return the multiplicative variance for bicluster list
	 */
	public static LinkedHashMap<Integer, Double> getMultiplicativeVarianceForBiclusterList(BiclusterList biclist, AnalysisTypeDimension dimension){
		LinkedHashMap<Integer, Double> res=new LinkedHashMap<>();
		for (int i = 0; i < biclist.size(); i++) {
			res.put(i, multiplicativeVariance(biclist.get(i), dimension));
		}
		return res;
	}
	
	/**
	 * Gets the sign variance for bicluster list.
	 *
	 * @param biclist the BiclusterList
	 * @param dimension the dimension
	 * @return the sign variance for bicluster list
	 */
	public static LinkedHashMap<Integer, Double> getSignVarianceForBiclusterList(BiclusterList biclist, AnalysisTypeDimension dimension){
		LinkedHashMap<Integer, Double> res=new LinkedHashMap<>();
		for (int i = 0; i < biclist.size(); i++) {
			res.put(i, signVariance(biclist.get(i), dimension));
		}
		return res;
	}
	
	/**
	 * Gets the constant variance.
	 *
	 * @param bic the BiclusterList
	 * @return the constant variance
	 */
	public static Triplet<Double, Double, Double> getConstantVariance(BiclusterResult bic){
		double constantrows=constantVariance(bic, AnalysisTypeDimension.ROWS);
		double constantcolumns=constantVariance(bic, AnalysisTypeDimension.COLUMNS);
		double constantboth=constantVariance(bic, AnalysisTypeDimension.BOTH);
		return new Triplet<Double, Double, Double>(constantrows, constantcolumns, constantboth);
	}
	
	/**
	 * Gets the additive variance.
	 *
	 * @param bic the BiclusterList
	 * @return the additive variance
	 */
	public static Triplet<Double, Double, Double> getAdditiveVariance(BiclusterResult bic){
		double additiverows=additiveVariance(bic, AnalysisTypeDimension.ROWS);
		double additivecolumns=additiveVariance(bic, AnalysisTypeDimension.COLUMNS);
		double additiveboth=additiveVariance(bic, AnalysisTypeDimension.BOTH);
		return new Triplet<Double, Double, Double>(additiverows,additivecolumns, additiveboth);
	}
	
	/**
	 * Gets the multiplicative variance.
	 *
	 * @param bic the BiclusterList
	 * @return the multiplicative variance
	 */
	public static Triplet<Double, Double, Double> getMultiplicativeVariance(BiclusterResult bic){
		double rows=multiplicativeVariance(bic, AnalysisTypeDimension.ROWS);
		double columns=multiplicativeVariance(bic, AnalysisTypeDimension.COLUMNS);
		double both=multiplicativeVariance(bic, AnalysisTypeDimension.BOTH);
		return new Triplet<Double, Double, Double>(rows,columns, both);
	}
	
	/**
	 * Gets the sign variance.
	 *
	 * @param bic the BiclusterList
	 * @return the sign variance
	 */
	public static Triplet<Double, Double, Double> getSignVariance(BiclusterResult bic){
		double rows=signVariance(bic, AnalysisTypeDimension.ROWS);
		double columns=signVariance(bic, AnalysisTypeDimension.COLUMNS);
		double both=signVariance(bic, AnalysisTypeDimension.BOTH);
		return new Triplet<Double, Double, Double>(rows,columns, both);
	}
	
	
	
	/**
	 * Gets the coherence measures for bicluster.
	 *
	 * @param bic the BiclusterList
	 * @param dimension the dimension
	 * @return the coherence measures for bicluster
	 */
	public static Quartet<Double, Double, Double, Double> getCoherenceMeasuresForBicluster(BiclusterResult bic, AnalysisTypeDimension dimension){
		double constant=constantVariance(bic, dimension);
		double additive=additiveVariance(bic, dimension);
		double multiplicative=multiplicativeVariance(bic, dimension);
		double sign=signVariance(bic, dimension);
		return new Quartet<Double, Double, Double, Double>(constant, additive, multiplicative, sign);
	}
	
	
	
	
	/**
	 * Gets the coherence measures for bicluster list.
	 *
	 * @param biclist the BiclusterList
	 * @param dimension the dimension
	 * @return the coherence measures for bicluster list
	 */
	public static LinkedHashMap<Integer, Quartet<Double, Double, Double, Double>> getCoherenceMeasuresForBiclusterList(BiclusterList biclist, AnalysisTypeDimension dimension){
		LinkedHashMap<Integer, Quartet<Double, Double, Double, Double>> res=new LinkedHashMap<>();
		for (int i = 0; i < biclist.size(); i++) {
			res.put(i,getCoherenceMeasuresForBicluster(biclist.get(i), dimension));
		}
		return res;
	}
	

	


}
