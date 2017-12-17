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

import java.util.ArrayList;

import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;
import jbiclustge.analysis.syntheticdata.factory.AbstractSyntheticDataFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import pt.ornrocha.collections.MTUListUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class PlaidDataBuilder.
 */
public class PlaidDataBuilder extends DefaultDataBuilder{

	/**
	 * Instantiates a new plaid data builder.
	 *
	 * @param datanumberrows the datanumberrows
	 * @param datanumbercolumns the datanumbercolumns
	 * @param shapebiclusters the shapebiclusters
	 * @param datadistributionfactory the datadistributionfactory
	 * @param biclustersnoise the biclustersnoise
	 * @param biclustergenerator the biclustergenerator
	 */
	public PlaidDataBuilder(int datanumberrows, int datanumbercolumns,
			ArrayList<Pair<Integer, Integer>> shapebiclusters,
			ISyntheticDataDistributionFactory datadistributionfactory, double[] biclustersnoise,
			AbstractBiclusterFactory biclustergenerator) {
		super(datanumberrows, datanumbercolumns, shapebiclusters, datadistributionfactory, biclustersnoise, biclustergenerator);

	}
	
	

	/**
	 * Initialize dataset.
	 *
	 * @return the IND array
	 */
	protected INDArray initializeDataset(){
    	
    	
    	 /*INDArray initdata=Nd4j.zeros(datanumberrows, datanumbercolumns);
    	 double value=datadistributionfactory.generateDoubleDataVector(1)[0]; 
		 
		 initdata=initdata.add(value);*/ 
		
		INDArray initdata=AbstractSyntheticDataFactory.generatePlaidBackgroundOfDataset(datadistributionfactory, datanumberrows, datanumbercolumns);
		 
		 /*if(datanoise>0){
			 datadistributionfactory.setStandardDeviation(datanoise);
			 INDArray noisematrix=datadistributionfactory.generateDataMatrix(datanumberrows, datanumbercolumns);
			 INDArray datawithnoise=initdata.add(noisematrix);
			 return datawithnoise;
		 }*/
		 
		 return initdata;
	 }
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.DefaultDataBuilder#buildDataset()
	 */
	@Override
	 public void buildDataset() throws Exception {
		
		if(datasetbackground==null)
			datasetbackground=initializeDataset();
		 //INDArray initDataset=initializeDataset();
		
		 INDArray biclusters=modelgenerator.generateBiclustersMatrix(biclustergenerator, biclustersnoise);
		 INDArray tmpsyntheticdataset=datasetbackground.add(biclusters);
		 ArrayList<Integer> shuffledrowsindexes=null;
		 ArrayList<Integer> shuffledColumnsindexes=null;
		 if(shuffledata){
			 shuffledrowsindexes=MTUListUtils.getListAsShuffleIndexes(datanumberrows);
			 shuffledColumnsindexes=MTUListUtils.getListAsShuffleIndexes(datanumbercolumns); 
		 }
		 
		 syntheticdataset=makeExpressionDataset(tmpsyntheticdataset, shuffledrowsindexes, shuffledColumnsindexes);
		 
		 ArrayList<Pair<Integer, Integer>> bicrowsinfo=modelgenerator.getRowMatrixModelInfo().getValue1();
		 ArrayList<Pair<Integer, Integer>> biccolinfo=modelgenerator.getColumnMatrixModelInfo().getValue1();
		 listofsyntheticbiclusters=makeBiclusterList(syntheticdataset,bicrowsinfo,biccolinfo, shuffledrowsindexes, shuffledColumnsindexes);
		 

	 }
	
	

}
