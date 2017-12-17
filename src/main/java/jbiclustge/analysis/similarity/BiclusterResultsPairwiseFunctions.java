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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import jbiclustge.analysis.similarity.components.AnalysisTypeDimension;
import jbiclustge.analysis.similarity.components.SimilarityIndexMethod;
import jbiclustge.results.biclusters.containers.BiclusterList;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.printutils.MTUPrintUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterResultsPairwiseFunctions.
 */
public class BiclusterResultsPairwiseFunctions {
	
	 /*public static double getPairwiseBiclusterResultsSimilarityIndex(BiclusterList results1, BiclusterList results2, SimilarityIndexMethod similaritymethod) throws Exception{
		 
		 double[][] indexmatrix=null;
		 
		 if(similaritymethod.equals(SimilarityIndexMethod.Jaccard))
			 indexmatrix=new BiclustersJaccardMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 else if(similaritymethod.equals(SimilarityIndexMethod.Kulczynski))
			 indexmatrix=new BiclustersKulczynskiMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 else if(similaritymethod.equals(SimilarityIndexMethod.Ochiai))
			 indexmatrix=new BiclustersOchiaiMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 else
			 indexmatrix=new BiclustersSorensenMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 
		 
		 double index=0;
		 
		 for (int i = 0; i < indexmatrix.length; i++) {
			 double[] row=indexmatrix[i];
			 double sumrow=0;
			 for (int j = 0; j < row.length; j++) {
				 System.out.println(row[j]);
				 sumrow+=row[j];
			  }
			 index+=sumrow;
		 }
		 
		 ArrayList<Integer> bicsize=new ArrayList<>(2);
		 bicsize.add(results1.size());
		 bicsize.add(results2.size());
		 
		 int max=Collections.max(bicsize);
		 
		 if(index>0)
		   index=index/max;
		 
		 return index;
		 
	 }*/
	
	
	/**
 	 * Gets the pairwise bicluster similarity index.
 	 *
 	 * @param results1 the results 1
 	 * @param results2 the results 2
 	 * @param similaritymethod the similaritymethod
 	 * @return the pairwise bicluster similarity index
 	 * @throws Exception the exception
 	 */
 	//public static double get
	public static double getPairwiseBiclusterSimilarityIndex(BiclusterList results1, BiclusterList results2, SimilarityIndexMethod similaritymethod) throws Exception{
		 double indexboth=getBiclustersSimilarityIndex(results1, results2, similaritymethod);
		 double indexbic1=getBiclustersSimilarityIndex(results1, results1,similaritymethod);
		 double indexbic2=getBiclustersSimilarityIndex(results2, results2,similaritymethod);
		 
		 return indexboth/Math.max(indexbic1, indexbic2);
		
	}
	
	
	/**
	 * Gets the biclusters similarity index.
	 *
	 * @param results1 the results 1
	 * @param results2 the results 2
	 * @param similaritymethod the similaritymethod
	 * @return the biclusters similarity index
	 * @throws Exception the exception
	 */
	public static double getBiclustersSimilarityIndex(BiclusterList results1, BiclusterList results2, SimilarityIndexMethod similaritymethod) throws Exception{
		 
		 double[][] indexmatrix=null;
		 
		 if(similaritymethod.equals(SimilarityIndexMethod.Jaccard))
			 indexmatrix=new BiclustersJaccardMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 else if(similaritymethod.equals(SimilarityIndexMethod.Kulczynski))
			 indexmatrix=new BiclustersKulczynskiMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 else if(similaritymethod.equals(SimilarityIndexMethod.Ochiai))
			 indexmatrix=new BiclustersOchiaiMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 else
			 indexmatrix=new BiclustersSorensenMatrix(results1, results2, AnalysisTypeDimension.BOTH).calculateSimilarityMatrix();
		 
		 
		 double index=0;
		 
		 for (int i = 0; i < indexmatrix.length; i++) {
			 double[] row=indexmatrix[i];
			 double sumrow=0;
			 for (int j = 0; j < row.length; j++) {
				 sumrow+=row[j];
			  }
			 index+=sumrow;
		 }
		 
		 ArrayList<Integer> bicsize=new ArrayList<>(2);
		 bicsize.add(results1.size());
		 bicsize.add(results2.size());
		 
		 int max=Collections.max(bicsize);
		 
		 if(index>0)
		   index=index/max;
		 
		 return index; 
	 }
	 
	 
	 /**
 	 * Gets the pairwise bicluster results similarity index.
 	 *
 	 * @param similaritymethod the similaritymethod
 	 * @param biclustersresultList the biclustersresult list
 	 * @return the pairwise bicluster results similarity index
 	 * @throws Exception the exception
 	 */
 	public static double[][] getPairwiseBiclusterResultsSimilarityIndex(SimilarityIndexMethod similaritymethod, BiclusterList...biclustersresultList) throws Exception{
		 
		 if(biclustersresultList.length<2)
			 throw new IOException("It must be assigned at least two bicluster result sets");
		 else{
			 double[][] res = new double[biclustersresultList.length][biclustersresultList.length];
			 
			 
			 for (int i = 0; i < biclustersresultList.length; i++) {
				 
				 BiclusterList set1=biclustersresultList[i];
				 for (int j = 0; j < biclustersresultList.length; j++) {
					 
					 BiclusterList set2=biclustersresultList[j];
					 if(i!=j && i>j){
						 LogMessageCenter.getLogger().toClass(BiclusterResultsPairwiseFunctions.class).addTraceMessage("Pairwise of bic: "+i+" vs bic: "+j);
						 double value=getPairwiseBiclusterSimilarityIndex(set1, set2,similaritymethod);
						 res[i][j]= value;
						 res[j][i]=value;
					 }
					 else if(i==j)
						 res[i][j]=1.0;
				 }
			  }
	        return res;
		 }
	 }
	 
	 
	 /**
 	 * Gets the jaccard index pairwise bicluster results.
 	 *
 	 * @param results1 the results 1
 	 * @param results2 the results 2
 	 * @return the jaccard index pairwise bicluster results
 	 * @throws Exception the exception
 	 */
 	public static double getJaccardIndexPairwiseBiclusterResults(BiclusterList results1, BiclusterList results2) throws Exception{
		 return getPairwiseBiclusterSimilarityIndex(results1, results2, SimilarityIndexMethod.Jaccard);
	 }
	 
	 
	 /**
 	 * Gets the jaccard index pairwise bicluster results.
 	 *
 	 * @param biclustersresultList the biclustersresult list
 	 * @return the jaccard index pairwise bicluster results
 	 * @throws Exception the exception
 	 */
 	public static double[][] getJaccardIndexPairwiseBiclusterResults(BiclusterList...biclustersresultList) throws Exception{
		 return  getPairwiseBiclusterResultsSimilarityIndex(SimilarityIndexMethod.Jaccard, biclustersresultList);
	 }
	 
	  /**
  	 * Recovery score.
  	 *
  	 * @param expected the expected
  	 * @param result the result
  	 * @param dimension the dimension
  	 * @return the double
  	 * @throws Exception the exception
  	 */
  	protected static double recoveryScore(BiclusterList expected, BiclusterList result, AnalysisTypeDimension dimension) throws Exception{
		  
		  double[][] indexmatrix=new BiclustersJaccardMatrix(expected, result, dimension).calculateSimilarityMatrix();
		  //MTUPrintUtils.printDoubleMatrix(indexmatrix);
		  double score=0;
			 
			 for (int i = 0; i < indexmatrix.length; i++) {
				 double[] row=indexmatrix[i];
				 double maxscore=0;
				 ArrayList<Double> rowscores=new ArrayList<>();
				 for (int j = 0; j < row.length; j++) {
					 rowscores.add(row[j]);
				  }
				 maxscore=Collections.max(rowscores);
				 score+=maxscore;
			 }
			 if(score>0)
				 score=score/expected.size();
			// System.out.println("\n\n");
			 return score;
	  }
	  
	 /**
 	 * Gets the prelics recovery score.
 	 *
 	 * @param expected the expected
 	 * @param result the result
 	 * @return the prelics recovery score
 	 * @throws Exception the exception
 	 */
 	//recovery= SG(A,B)= (1/A)*sum( max(|Arows n Brows|/|Arows u Brows|))
	  public static double getPrelicsRecoveryScore(BiclusterList expected, BiclusterList result) throws Exception{
		  return recoveryScore(expected, result, AnalysisTypeDimension.ROWS);
	  }
	
	 
	 /**
 	 * Gets the prelics relevance score.
 	 *
 	 * @param expected the expected
 	 * @param result the result
 	 * @return the prelics relevance score
 	 * @throws Exception the exception
 	 */
 	//relevance= SG(B,A)
	 public static double getPrelicsRelevanceScore(BiclusterList expected, BiclusterList result) throws Exception{
		 return getPrelicsRecoveryScore(result, expected);
	 }
	 
	 
	 /**
 	 * Gets the recovery score.
 	 *
 	 * @param expected the expected
 	 * @param result the result
 	 * @return the recovery score
 	 * @throws Exception the exception
 	 */
 	// From Robust Biclustering of High dimensional Molecular Data by Sparse Singular Value Decomposition Incorporating Stability Selection
	 public static double getRecoveryScore(BiclusterList expected, BiclusterList result) throws Exception{
		 return recoveryScore(expected, result, AnalysisTypeDimension.BOTH);
	 }
	 
	 /**
 	 * Gets the relevance score.
 	 *
 	 * @param expected the expected
 	 * @param result the result
 	 * @return the relevance score
 	 * @throws Exception the exception
 	 */
 	public static double getRelevanceScore(BiclusterList expected, BiclusterList result) throws Exception{
		 return getRecoveryScore(result, expected);
	 }
	

	 
}
