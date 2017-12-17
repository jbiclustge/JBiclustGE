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
import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;

import jbiclustge.analysis.syntheticdata.SyntheticDataStaticFunctions;
import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.TypeBicluster;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGeneratorMultiBiclustersType;
import jbiclustge.analysis.syntheticdata.model.UnsupportedDataModelGenerator;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.containers.BiclusterList;
import pt.ornrocha.collections.MTUListUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class MixDataBuilder.
 */
public class MixDataBuilder implements ISyntheticDataBuilder{

	
	
	/** The modelgenerator. */
	protected AbstractDataModelGenerator modelgenerator;
	
	/** The biclustergenerators. */
	protected ArrayList<AbstractBiclusterFactory> biclustergenerators;
	
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
	
	
	/**
	 * Instantiates a new mix data builder.
	 *
	 * @param datanumberrows the datanumberrows
	 * @param datanumbercolumns the datanumbercolumns
	 * @param shapebiclusters the shapebiclusters
	 * @param datadistributionfactory the datadistributionfactory
	 * @param biclustersnoise the biclustersnoise
	 * @param biclustergenerator the biclustergenerator
	 */
	public MixDataBuilder(int datanumberrows,
			 int datanumbercolumns,
			 ArrayList<Pair<Integer, Integer>> shapebiclusters,
			 ISyntheticDataDistributionFactory datadistributionfactory,
			 double[] biclustersnoise,
			 ArrayList<AbstractBiclusterFactory> biclustergenerator){
		 this.datanumberrows=datanumberrows;
		 this.datanumbercolumns=datanumbercolumns;
		 this.shapebiclusters=shapebiclusters;
		 this.datadistributionfactory=datadistributionfactory;
		 this.biclustersnoise=biclustersnoise;
         this.biclustergenerators=biclustergenerator;
	}
	

	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#setDataModelGenerator(analysis.syntheticdata.model.AbstractDataModelGenerator)
	 */
	@Override
	public void setDataModelGenerator(AbstractDataModelGenerator modelgenerator) throws UnsupportedDataModelGenerator {
		if(modelgenerator instanceof AbstractDataModelGeneratorMultiBiclustersType)
			this.modelgenerator=modelgenerator;
		else
			throw new UnsupportedDataModelGenerator();
		
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#setDataNoise(double)
	 */
	@Override
	public void setDataNoise(double noise) {
		this.datanoise=noise;
		
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#setShuffleData(boolean)
	 */
	@Override
	public void setShuffleData(boolean shuffle) {
		this.shuffledata=shuffle;
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.builders.ISyntheticDataBuilder#buildDataset()
	 */
	@Override
	public void buildDataset() throws Exception {
		
		AbstractDataModelGeneratorMultiBiclustersType modelgeneratorcast=(AbstractDataModelGeneratorMultiBiclustersType) modelgenerator;
		INDArray initdata=datadistributionfactory.generateDataMatrix(datanumberrows, datanumbercolumns); 
		INDArray biclusters=modelgeneratorcast.generateBiclustersMatrix(biclustergenerators, biclustersnoise);
		
		ArrayList<Integer> nplaid=new ArrayList<>();
		for (int i = 0; i <biclustergenerators.size(); i++) {
			if(biclustergenerators.get(i).getTypeOfBicluster().equals(TypeBicluster.PLAID))
				nplaid.add(i);
		}
		
		int[] plaidpos=null;
		if(nplaid.size()>0)
			plaidpos=ArrayUtils.toPrimitive(nplaid.toArray(new Integer[nplaid.size()]));
		
		
		//INDArray tmpsyntheticdataset=SyntheticDataStaticFunctions.concatenateDataWithBiclusters(initdata, biclusters);
		INDArray tmpsyntheticdataset=SyntheticDataStaticFunctions.concatenateDataWithBiclusters(initdata, biclusters, modelgeneratorcast, plaidpos);
		INDArray noisydata=addNoiseToData(tmpsyntheticdataset);
		 ArrayList<Integer> shuffledrowsindexes=null;
		 ArrayList<Integer> shuffledColumnsindexes=null;
		 if(shuffledata){
			 shuffledrowsindexes=MTUListUtils.getListAsShuffleIndexes(datanumberrows);
			 shuffledColumnsindexes=MTUListUtils.getListAsShuffleIndexes(datanumbercolumns); 
		 }
		 
		 syntheticdataset=DefaultDataBuilder.makeExpressionDataset(tmpsyntheticdataset, shuffledrowsindexes, shuffledColumnsindexes);
		 
		 ArrayList<Pair<Integer, Integer>> bicrowsinfo=modelgenerator.getRowMatrixModelInfo().getValue1();
		 ArrayList<Pair<Integer, Integer>> biccolinfo=modelgenerator.getColumnMatrixModelInfo().getValue1();
		 listofsyntheticbiclusters=DefaultDataBuilder.makeBiclusterList(syntheticdataset,bicrowsinfo,biccolinfo, shuffledrowsindexes, shuffledColumnsindexes);
		
		
		
		
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

	

}
