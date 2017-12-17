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
package jbiclustge.analysis.syntheticdata.factory.builders;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;

import jbiclustge.analysis.syntheticdata.SyntheticDataStaticFunctions;
import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;
import jbiclustge.analysis.syntheticdata.factory.AbstractSyntheticDataFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.collections.MTUListUtils;
import smile.imputation.MissingValueImputationException;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultDataBuilder.
 */
public class DefaultDataBuilder implements ISyntheticDataBuilder{
	
	/** The modelgenerator. */
	protected AbstractDataModelGenerator modelgenerator;
	
	/** The biclustergenerator. */
	protected AbstractBiclusterFactory biclustergenerator;
	
	/** The datanumberrows. */
	protected int datanumberrows;
	
	/** The datanumbercolumns. */
	protected int datanumbercolumns;
	
	/** The shapebiclusters. */
	protected ArrayList<Pair<Integer, Integer>> shapebiclusters;
	
	/** The datadistributionfactory. */
	protected ISyntheticDataDistributionFactory datadistributionfactory;
	
	/** The datanoise. */
	protected double datanoise=0;
	
	/** The biclustersnoise. */
	protected double[] biclustersnoise;
	
	/** The shuffledata. */
	protected boolean shuffledata=false;
	
	/** The syntheticdataset. */
	protected ExpressionData syntheticdataset;
	
	/** The listofsyntheticbiclusters. */
	protected BiclusterList listofsyntheticbiclusters;
	
	/** The datasetbackground. */
	protected INDArray datasetbackground;
	 

	 /**
 	 * Instantiates a new default data builder.
 	 *
 	 * @param datanumberrows the datanumberrows
 	 * @param datanumbercolumns the datanumbercolumns
 	 * @param shapebiclusters the shapebiclusters
 	 * @param datadistributionfactory the datadistributionfactory
 	 * @param biclustersnoise the biclustersnoise
 	 * @param biclustergenerator the biclustergenerator
 	 */
 	public DefaultDataBuilder(int datanumberrows,
			 int datanumbercolumns,
			 ArrayList<Pair<Integer, Integer>> shapebiclusters,
			 ISyntheticDataDistributionFactory datadistributionfactory,
			 double[] biclustersnoise,
			 AbstractBiclusterFactory biclustergenerator){
		 this.datanumberrows=datanumberrows;
		 this.datanumbercolumns=datanumbercolumns;
		 this.shapebiclusters=shapebiclusters;
		 this.datadistributionfactory=datadistributionfactory;
		 this.biclustersnoise=biclustersnoise;
         this.biclustergenerator=biclustergenerator;
	 }
	
	 
	 /* (non-Javadoc)
 	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#setDataNoise(double)
 	 */
 	public void setDataNoise(double noise){
		 this.datanoise=noise;
	 }
	 
	 /**
 	 * Sets the dataset background.
 	 *
 	 * @param datasetbackground the new dataset background
 	 */
 	public void setDatasetBackground(INDArray datasetbackground){
		 this.datasetbackground=datasetbackground;
	 }
	 
	 
	 /* (non-Javadoc)
 	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#setShuffleData(boolean)
 	 */
 	@Override
	 public void setShuffleData(boolean shuffle) {
        this.shuffledata=shuffle;
			
	 }
	 

	 /* (non-Javadoc)
 	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#setDataModelGenerator(analysis.syntheticdata.model.AbstractDataModelGenerator)
 	 */
 	@Override
	 public void setDataModelGenerator(AbstractDataModelGenerator modelgenerator) {
		this.modelgenerator=modelgenerator;	
	 }


	/* protected INDArray initializeDataset(){
		 
		 INDArray initdata=datadistributionfactory.generateDataMatrix(datanumberrows, datanumbercolumns); 
		 return initdata;
	 }*/
	 
	 
	 /* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#buildDataset()
	 */
	@Override
	 public void buildDataset() throws Exception {
		
		// INDArray initDataset=initializeDataset();
		 if(datasetbackground==null)
			 datasetbackground=AbstractSyntheticDataFactory.generateBackgroundOfDataset(datadistributionfactory, datanumberrows, datanumbercolumns);
		 
		 
		 INDArray biclusters=modelgenerator.generateBiclustersMatrix(biclustergenerator, biclustersnoise);
		 INDArray tmpsyntheticdataset=SyntheticDataStaticFunctions.concatenateDataWithBiclusters(datasetbackground, biclusters);
		 INDArray noisydata=addNoiseToData(tmpsyntheticdataset);
		 ArrayList<Integer> shuffledrowsindexes=null;
		 ArrayList<Integer> shuffledColumnsindexes=null;
		 if(shuffledata){
			 shuffledrowsindexes=MTUListUtils.getListAsShuffleIndexes(datanumberrows);
			 shuffledColumnsindexes=MTUListUtils.getListAsShuffleIndexes(datanumbercolumns); 
		 }
		 
		 syntheticdataset=makeExpressionDataset(noisydata, shuffledrowsindexes, shuffledColumnsindexes);
		 
		 ArrayList<Pair<Integer, Integer>> bicrowsinfo=modelgenerator.getRowMatrixModelInfo().getValue1();
		 ArrayList<Pair<Integer, Integer>> biccolinfo=modelgenerator.getColumnMatrixModelInfo().getValue1();
		 listofsyntheticbiclusters=makeBiclusterList(syntheticdataset,bicrowsinfo,biccolinfo, shuffledrowsindexes, shuffledColumnsindexes);
		 
	 }
	 
	 /**
 	 * Adds the noise to data.
 	 *
 	 * @param datamatrix the datamatrix
 	 * @return the IND array
 	 */
 	protected INDArray addNoiseToData(INDArray datamatrix){
		 if(datanoise>0){
			 datadistributionfactory.setStandardDeviation(datanoise);
			 INDArray noisematrix=datadistributionfactory.generateDataMatrix(datanumberrows, datanumbercolumns);
			return datamatrix.add(noisematrix);
		 }
		 return datamatrix;
	 }
	
	 
	/**
	 * Make expression dataset.
	 *
	 * @param tmpsyntheticdataset the tmpsyntheticdataset
	 * @param shuffledrowsindexes the shuffledrowsindexes
	 * @param shuffledColumnsindexes the shuffled columnsindexes
	 * @return the expression data
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static ExpressionData makeExpressionDataset(INDArray tmpsyntheticdataset,ArrayList<Integer> shuffledrowsindexes, ArrayList<Integer> shuffledColumnsindexes) throws IOException, MissingValueImputationException{
		 
		 double[][] dataset=null;
		 if(shuffledrowsindexes!=null && shuffledColumnsindexes!=null){
			 dataset=new double[tmpsyntheticdataset.rows()][tmpsyntheticdataset.columns()];
			 
			 for (int i = 0; i < tmpsyntheticdataset.rows(); i++) {
				
				 INDArray row=tmpsyntheticdataset.getRow(i);
				 int rowindex=shuffledrowsindexes.get(i);
			     for (int j = 0; j < row.length(); j++) {
					int colindex=shuffledColumnsindexes.get(j); 
					dataset[rowindex][colindex]=tmpsyntheticdataset.getDouble(i, j);
			     }
			 }
			 
			 
		 }
		 else{
			 dataset=ND4JUtils.getDoubleMatrix(tmpsyntheticdataset); 
		 }
		 
		 ExpressionData syntheticdataset=new ExpressionData(getRowLabels(dataset.length), getColumnsLabels(dataset[0].length), dataset);
		 syntheticdataset.load();
		 
		 return syntheticdataset;
		 
	 }
	
	
	 /**
 	 * Make bicluster list.
 	 *
 	 * @param syntheticdataset the syntheticdataset
 	 * @param bicrowsinfo the bicrowsinfo
 	 * @param biccolinfo the biccolinfo
 	 * @param shuffledrowsindexes the shuffledrowsindexes
 	 * @param shuffledColumnsindexes the shuffled columnsindexes
 	 * @return the bicluster list
 	 */
 	public static BiclusterList makeBiclusterList(ExpressionData syntheticdataset, ArrayList<Pair<Integer, Integer>> bicrowsinfo,ArrayList<Pair<Integer, Integer>> biccolinfo,  ArrayList<Integer> shuffledrowsindexes, ArrayList<Integer> shuffledColumnsindexes){
		 
		/* ArrayList<Pair<Integer, Integer>> bicrowsinfo=modelgenerator.getRowMatrixModelInfo().getValue1();
		 ArrayList<Pair<Integer, Integer>> biccolinfo=modelgenerator.getColumnMatrixModelInfo().getValue1();*/
		 BiclusterList listofsyntheticbiclusters=new BiclusterList();
		 listofsyntheticbiclusters.setName("Expected bicluster list with "+bicrowsinfo.size()+" biclusters");
		 
		 for (int i = 0; i < bicrowsinfo.size(); i++) {
			 
			 ArrayList<Integer> bicrowindex=new ArrayList<>();
			 ArrayList<Integer> biccolindex=new ArrayList<>();
			
			 Pair<Integer, Integer> rowrange=bicrowsinfo.get(i);
			   
			 for (int j = rowrange.getValue0(); j < rowrange.getValue1(); j++) {
				 if(shuffledrowsindexes!=null)
					 bicrowindex.add(shuffledrowsindexes.get(j));
				 else
					 bicrowindex.add(j);
		     }
			   
			 Pair<Integer, Integer> colrange=biccolinfo.get(i);   
			 
			 for (int j = colrange.getValue0(); j < colrange.getValue1(); j++) {
				 if(shuffledColumnsindexes!=null)
					 biccolindex.add(shuffledColumnsindexes.get(j));
				 else
					 biccolindex.add(j);
			 }
			   
			 BiclusterResult bic=new BiclusterResult(syntheticdataset, bicrowindex, biccolindex, true);
			 listofsyntheticbiclusters.add(bic);
		 
		 }
		 
		 return listofsyntheticbiclusters;
	 }
	
	
	/**
	 * Gets the row labels.
	 *
	 * @param numberlabels the numberlabels
	 * @return the row labels
	 */
	public static ArrayList<String> getRowLabels(int numberlabels){
		 
		 ArrayList<String> res=new ArrayList<>();
		 for (int i = 0; i < numberlabels; i++) {
			 String label="row"+String.valueOf(i);
			res.add(label);
		 }
		 return res;
	 }
	

	/**
	 * Gets the columns labels.
	 *
	 * @param numberlabels the numberlabels
	 * @return the columns labels
	 */
	public static ArrayList<String> getColumnsLabels(int numberlabels){
		 
		 ArrayList<String> res=new ArrayList<>();
		 for (int i = 0; i < numberlabels; i++) {
			 String label="col"+String.valueOf(i);
			res.add(label);
		 }
		 return res;
	 }
	
	

	 /* (non-Javadoc)
 	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#getSyntheticDataset()
 	 */
 	@Override
	 public ExpressionData getSyntheticDataset() {
		return syntheticdataset;
	 }


	 /* (non-Javadoc)
 	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#getExpectedBiclusters()
 	 */
 	@Override
	 public BiclusterList getExpectedBiclusters() {
		return listofsyntheticbiclusters;
	 }


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#writeExpressionDatasetToFile(java.lang.String)
	 */
	@Override
	public void writeExpressionDatasetToFile(String filepath) throws IOException {
		if(syntheticdataset!=null){
			syntheticdataset.writeExpressionDatasetToFile(filepath);
		}
	}


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#writeExpectedBiclustersToFile(java.lang.String)
	 */
	@Override
	public void writeExpectedBiclustersToFile(String filepath) throws IOException {
		if(listofsyntheticbiclusters!=null){
			String dir=FilenameUtils.getFullPath(filepath);
			String filename=FilenameUtils.getBaseName(filepath);
			listofsyntheticbiclusters.writeBiclusterListToBiclustRPackageFormat(dir, filename, "txt");
		}
		
	}


	 
	 
	



	/* public static void main(String[] args) throws Exception {
			
			
			double[][] mat1=new double[3][5];
			mat1[0]=new double[]{1,2,3,4,5};
			mat1[1]=new double[]{6,7,8,9,10};
			mat1[2]=new double[]{11,12,13,14,15};
			
	
	        INDArray m1=Nd4j.create(mat1);
            
	        ArrayList<Integer> shuffledrowsindexes=MTUListUtils.getListAsShuffleIndexes(3);
			ArrayList<Integer> shuffledColumnsindexes=MTUListUtils.getListAsShuffleIndexes(5);

			
			double[][] dataset=new double[m1.rows()][m1.columns()];
			 
			 for (int i = 0; i < m1.rows(); i++) {
				
				 INDArray row=m1.getRow(i);
				 int rowindex=shuffledrowsindexes.get(i);
			     for (int j = 0; j < row.length(); j++) {
			    	double value=m1.getDouble(i, j);
					int colindex=shuffledColumnsindexes.get(j); 
					System.out.println("("+i+","+j+") --> ("+rowindex+","+colindex+") -->"+value); 
					dataset[rowindex][colindex]=value;
			     }
			 }
			

			ND4JUtils.printINDArrayMatrix(m1, "\t");
			System.out.println("\n");
			System.out.println(shuffledrowsindexes);
			System.out.println(shuffledColumnsindexes);
			System.out.println("\n");
			MTUPrintUtils.printDoubleMatrix(dataset);
			

		}*/


	


















	

}
