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
import jrplot.rbinders.components.dataframe.DefaultDataframeContainer;
import jrplot.rbinders.components.dataframe.datatype.DoubleDataColumn;
import jrplot.rbinders.components.dataframe.datatype.StringDataColumn;
import jrplot.rbinders.components.interfaces.IDataFrameDataLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class ParallelCoordinatesUtils.
 */
public class ParallelCoordinatesUtils {
	
	
	   /**
   	 * Make data frame.
   	 *
   	 * @param matrixvalues the matrixvalues
   	 * @param columnsnames the columnsnames
   	 * @param vectornames the vectornames
   	 * @return the i data frame data loader
   	 */
   	public static IDataFrameDataLoader makeDataFrame(double [][] matrixvalues, ArrayList<String> columnsnames, ArrayList<String> vectornames){
		   
		   IDataFrameDataLoader dataframe=new DefaultDataframeContainer();

		   for (int i = 0; i < matrixvalues[0].length; i++) {
			   
			   ArrayList<Double> columnvalues=new ArrayList<>();
			   
			   for (int j = 0; j < matrixvalues.length; j++) {
				  columnvalues.add(matrixvalues[j][i]);
			   }

			   DoubleDataColumn valcolumn=new DoubleDataColumn(columnsnames.get(i), columnvalues);
			   dataframe.appendDataColumnToDataframe(valcolumn);
		   }
		   
		   StringDataColumn ids =new StringDataColumn("id", vectornames);
		   
		   dataframe.appendDataColumnToDataframe(ids);
		   
		   return dataframe;
	   }
	
	
	   /**
   	 * Make bicluster data frame.
   	 *
   	 * @param bicluster the bicluster
   	 * @param presentgenesascolumns the presentgenesascolumns
   	 * @param orderelements the orderelements
   	 * @param indexesascolumnsnames the indexesascolumnsnames
   	 * @return the i data frame data loader
   	 */
   	public static IDataFrameDataLoader makeBiclusterDataFrame(BiclusterResult bicluster, boolean presentgenesascolumns, boolean orderelements, boolean indexesascolumnsnames){
		   
	
		   double [][] matrixvalues=null;
		   ArrayList<String> columnsnames=null;
		   ArrayList<String> vectornames=null;
		   
		   if(presentgenesascolumns){
			   
			   if(orderelements){
				   matrixvalues=bicluster.getOrderedBiclusterExpressionDatabyColunmDimension();
				   columnsnames=bicluster.getGeneNamesOrderedAsExpressionDataset();
				   vectornames=bicluster.getConditionNamesOrderedAsExpressionDataset();
			   }
			   else{
				   matrixvalues=bicluster.getBiclusterExpressionDatabyColunmDimension();
				   columnsnames=bicluster.getGeneNames();
				   vectornames=bicluster.getConditionNames();
			   }
			   
			   if(indexesascolumnsnames)
				   columnsnames=BiclusterResult.getStringIndexesOfGeneNames(bicluster, columnsnames);
			   
			   
			   
		   }
		   else{
			   
			   if(orderelements){
				   matrixvalues=bicluster.getOrderedBiclusterExpressionDatabyRowDimension();
				   columnsnames=bicluster.getConditionNamesOrderedAsExpressionDataset();
				   vectornames=bicluster.getGeneNamesOrderedAsExpressionDataset();
			   }
			   else{
				   matrixvalues=bicluster.getBiclusterExpressionDatabyRowDimension();
				   columnsnames=bicluster.getConditionNames();
				   vectornames=bicluster.getGeneNames();
			   }
			   
			   if(indexesascolumnsnames){
				   columnsnames=BiclusterResult.getStringIndexesOfConditionNames(bicluster, columnsnames);
				   
			   }

		   }
		   
		   return makeDataFrame(matrixvalues, columnsnames, vectornames);
 
	   }
	
	
	   /**
   	 * Make bicluster data frame.
   	 *
   	 * @param listresults the listresults
   	 * @param indexbicluster the indexbicluster
   	 * @param presentgenesascolumns the presentgenesascolumns
   	 * @param orderelements the orderelements
   	 * @param indexesascolumnsnames the indexesascolumnsnames
   	 * @return the i data frame data loader
   	 */
   	public static IDataFrameDataLoader makeBiclusterDataFrame(BiclusterList listresults, int indexbicluster, boolean presentgenesascolumns, boolean orderelements, boolean indexesascolumnsnames){
		   return makeBiclusterDataFrame(listresults.get(indexbicluster), presentgenesascolumns, orderelements, indexesascolumnsnames);
	   }
	   
	
	   
	   /**
   	 * Make bicluster data frame.
   	 *
   	 * @param bicluster the bicluster
   	 * @param initindex the initindex
   	 * @param endindex the endindex
   	 * @param presentgenesascolumns the presentgenesascolumns
   	 * @param indexesascolumnsnames the indexesascolumnsnames
   	 * @return the i data frame data loader
   	 */
   	public static IDataFrameDataLoader makeBiclusterDataFrame(BiclusterResult bicluster,int initindex, int endindex, boolean presentgenesascolumns, boolean indexesascolumnsnames){
		   
		   ArrayList<String> genenames=null;
		   ArrayList<String> condnames=null;
		   
		   if(presentgenesascolumns){
			      ArrayList<String> origgenenames=bicluster.getGeneNamesOrderedAsExpressionDataset();
			      genenames=new ArrayList<>();
			      for (int i = initindex; i < endindex; i++) {
			    	  genenames.add(origgenenames.get(i));
				  }
				  
			      condnames=bicluster.getConditionNamesOrderedAsExpressionDataset();
		   }
		   else{
			     ArrayList<String> origcondnames=bicluster.getConditionNamesOrderedAsExpressionDataset();
			     condnames=new ArrayList<>();
			      for (int i = initindex; i < endindex; i++) {
			    	  condnames.add(origcondnames.get(i));
				  }
				  
			      genenames=bicluster.getGeneNamesOrderedAsExpressionDataset(); 
		   }
		   
		   BiclusterResult reducedbicluster=new BiclusterResult(bicluster.getAssociatedExpressionData(), genenames, condnames);
		   return makeBiclusterDataFrame(reducedbicluster, presentgenesascolumns, true,indexesascolumnsnames);
	   }
	   
	   
	   
	   
	 /*  public static void saveBiclusterParallelCoordinates(BiclusterList listresults, int indexbicluster, boolean presentgenesascolumns, boolean orderelements, boolean indexesascolumnsnames, String filepath){
		   
		   IDataFrameDataLoader dataframe=makeBiclusterDataFrame(listresults, indexbicluster, presentgenesascolumns, orderelements, indexesascolumnsnames);
		   BiclusterParallelCoordinates maker=new BiclusterParallelCoordinates(dataframe, "id");
		   maker.addWidth(1200).addHeight(800);
		   maker.saveFigureFilepath(filepath);
		   maker.run();
		   
	   }*/
	   
	   
	
	  
	

}
