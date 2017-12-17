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
package jbiclustge.analysis.syntheticdata.factory;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;
import jbiclustge.analysis.syntheticdata.distribution.NormalDistributionDataFactory;
import jbiclustge.analysis.syntheticdata.factory.builders.ISyntheticDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.props.BaseSyntheticDataproperties;
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbstractSyntheticData objects.
 */
public abstract class AbstractSyntheticDataFactory {
	
	
	/** The propertiescontainer. */
	protected SyntheticDataProperties propertiescontainer;
	
	/** The datanumberrows. */
	protected int datanumberrows=300;
	
	/** The datanumbercolumns. */
	protected int datanumbercolumns=50;
	
	/** The numberbiclusters. */
	protected int numberbiclusters=1;
	
	/** The shapebiclusters. */
	protected ArrayList<Pair<Integer, Integer>> shapebiclusters;
	
	/** The numberbicoverlaprows. */
	protected int numberbicoverlaprows=0;
	
	/** The numberbicovelapcolums. */
	protected int numberbicovelapcolums=0;
	
	/** The biclustersnoise. */
	protected double[] biclustersnoise=null;

	/** The datadistributionfactory. */
	protected ISyntheticDataDistributionFactory datadistributionfactory;
	
	/** The biclusterdatadistributionfactory. */
	protected ISyntheticDataDistributionFactory biclusterdatadistributionfactory;
	
	/** The datasetbuilder. */
	private ISyntheticDataBuilder datasetbuilder;
	
	/** The datasetbackground. */
	protected INDArray datasetbackground;

	
	
	
	
	/**
	 * Instantiates a new abstract synthetic data factory.
	 *
	 * @param properties the properties
	 */
	public AbstractSyntheticDataFactory(SyntheticDataProperties properties){
		this.propertiescontainer=properties;

	}
	
	/**
	 * Sets the data distribution generator.
	 *
	 * @param datadistribution the new data distribution generator
	 */
	public void setDataDistributionGenerator(ISyntheticDataDistributionFactory datadistribution){
		this.datadistributionfactory=datadistribution;
	}
	
	
	/**
	 * Sets the biclusters data distribution generator.
	 *
	 * @param datadistribution the new biclusters data distribution generator
	 */
	public void setBiclustersDataDistributionGenerator(ISyntheticDataDistributionFactory datadistribution){
		this.biclusterdatadistributionfactory=datadistribution;
	}
	
	
	/**
	 * Sets the background of dataset.
	 *
	 * @param datasetbackground the new background of dataset
	 */
	public void setBackgroundOfDataset(INDArray datasetbackground){
		this.datasetbackground=datasetbackground;
	}
	
	
	/*public void setModelGenerator(AbstractDataModelGenerator modelgen){
		this.modelgenerator=modelgen;
	}*/
	
	
	public SyntheticDataProperties getSyntheticDataPropertiescontainer() {
		return propertiescontainer;
	}
	
	/**
	 * Preprocess data.
	 */
	protected abstract void preprocessData();
	
	

	/**
	 * Configure model generator.
	 *
	 * @return the abstract data model generator
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract AbstractDataModelGenerator configureModelGenerator() throws IOException;
	
	/**
	 * Data assembler.
	 *
	 * @return the i synthetic data builder
	 */
	protected abstract ISyntheticDataBuilder DataAssembler();
	
	
	/**
	 * Read properties.
	 */
	protected void readProperties(){
		if(propertiescontainer!=null){
			this.datanumberrows=propertiescontainer.getNumberrowsdata();
			this.datanumbercolumns=propertiescontainer.getNumbercolumnsdata();
			this.numberbiclusters=propertiescontainer.getNumberbiclusters();
			if(!propertiescontainer.isBiclustersautoshape()){
				this.shapebiclusters=propertiescontainer.getShapebiclusters();		
			}
			else
				initShapeBiclusters();
			
			
			this.numberbicoverlaprows=propertiescontainer.getOverlapNumberBiclusterRows();
			this.numberbicovelapcolums=propertiescontainer.getOverlapNumberBiclusterColumns();
			
			
			
			if(propertiescontainer.getBiclustersNoise()!=null){
				ArrayList<Double> bicnoise=propertiescontainer.getBiclustersNoise();
				if(bicnoise.size()==numberbiclusters)
					biclustersnoise=ArrayUtils.toPrimitive(bicnoise.toArray(new Double[bicnoise.size()]));
				else{
					biclustersnoise=new double[numberbiclusters];
					for (int i = 0; i < numberbiclusters; i++) {
						if(i<bicnoise.size())
							biclustersnoise[i]=bicnoise.get(i);
						else
							biclustersnoise[i]=0;
					}
				}
			}
			else
				initBiclustersNoise();
			
			
			if(datadistributionfactory==null){
				this.datadistributionfactory=new NormalDistributionDataFactory();
				datadistributionfactory.setMean(propertiescontainer.getDataMeanvalue());
				datadistributionfactory.setStandardDeviation(propertiescontainer.getDataStandardDeviationvalue());
			}
			
			if(biclusterdatadistributionfactory==null){
				this.biclusterdatadistributionfactory=new NormalDistributionDataFactory();
			}
			
			
		}
	}
	
	
	/**
	 * Initialize.
	 */
	protected void initialize(){
		
		if(propertiescontainer!=null)
			readProperties();
		else{
			initShapeBiclusters();
			initBiclustersNoise();
			this.datadistributionfactory=new NormalDistributionDataFactory();
			this.biclusterdatadistributionfactory=new NormalDistributionDataFactory();
		}
		
	}
	
	/**
	 * Inits the shape biclusters.
	 */
	private void initShapeBiclusters(){
		shapebiclusters=new ArrayList<>();
		
		int bicrowsize=getNumberBiclusterDimensionElementSize(datanumberrows);
		int biccolsize=getNumberBiclusterDimensionElementSize(datanumbercolumns);
		
		for (int i = 0; i < numberbiclusters; i++) {
			shapebiclusters.add(new Pair<Integer, Integer>(bicrowsize, biccolsize));
		}
	}
	
	
	/**
	 * Inits the biclusters noise.
	 */
	private void initBiclustersNoise(){
		this.biclustersnoise=new double[numberbiclusters];
	}
	
	/**
	 * Gets the number bicluster dimension element size.
	 *
	 * @param dimensionsize the dimensionsize
	 * @return the number bicluster dimension element size
	 */
	private int getNumberBiclusterDimensionElementSize(int dimensionsize){
		int numberelements=Math.floorDiv(dimensionsize, numberbiclusters);
		if(numberelements==dimensionsize)
			numberelements=(int)dimensionsize/2;
		return numberelements;
	}
	
	/**
	 * Checks if is overlap two dimensions.
	 *
	 * @return true, if is overlap two dimensions
	 */
	protected boolean isOverlapTwoDimensions(){
		if(numberbicoverlaprows>0 && numberbicovelapcolums>0)
			return true;
		return false;
	}
	
	
	/**
	 * Gets the current base parameters.
	 *
	 * @return the current base parameters
	 */
	protected BaseSyntheticDataproperties getCurrentBaseParameters(){
		BaseSyntheticDataproperties parameters=new BaseSyntheticDataproperties();
		parameters.setNumberRowsDataset(datanumberrows)
		.setNumberColumnsDataset(datanumbercolumns)
		.setShapeOfBiclusters(shapebiclusters)
		.setOverlapNumberBiclusterRows(numberbicoverlaprows)
		.setOverlapNumberBiclusterColumns(numberbicovelapcolums);
        if(propertiescontainer.getOverlapBetweenBiclusters()!=null)
        	parameters.setOverlapBetweenBiclusters(propertiescontainer.getOverlapBetweenBiclusters());
		return parameters;
	}
	
	
	/**
	 * Make dataset.
	 *
	 * @throws Exception the exception
	 */
	public void makeDataset() throws Exception{
		initialize();
		preprocessData();
		AbstractDataModelGenerator modelgenerator=configureModelGenerator();
		this.datasetbuilder=DataAssembler();
		
		if(propertiescontainer.getDataNoise()!=0.0)
			datasetbuilder.setDataNoise(propertiescontainer.getDataNoise());
		datasetbuilder.setShuffleData(propertiescontainer.isSufflebics());
		
		
		datasetbuilder.setDataModelGenerator(modelgenerator);
		datasetbuilder.buildDataset();
		
	}
	
	

	/**
	 * Write expression dataset to file.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeExpressionDatasetToFile(String filepath) throws IOException {
		datasetbuilder.writeExpressionDatasetToFile(filepath);
	}



	/**
	 * Write expected biclusters to file.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeExpectedBiclustersToFile(String filepath) throws IOException {
		datasetbuilder.writeExpectedBiclustersToFile(filepath);
	}
	
	/**
	 * Generate background of dataset.
	 *
	 * @param distfactory the distfactory
	 * @param datanumberrows the datanumberrows
	 * @param datanumbercolumns the datanumbercolumns
	 * @return the IND array
	 */
	public static INDArray generateBackgroundOfDataset(ISyntheticDataDistributionFactory distfactory, int datanumberrows, int datanumbercolumns){
		 INDArray initdata=distfactory.generateDataMatrix(datanumberrows, datanumbercolumns); 
		 return initdata;
	}
	
	/**
	 * Generate plaid background of dataset.
	 *
	 * @param distfactory the distfactory
	 * @param datanumberrows the datanumberrows
	 * @param datanumbercolumns the datanumbercolumns
	 * @return the IND array
	 */
	public static INDArray generatePlaidBackgroundOfDataset(ISyntheticDataDistributionFactory distfactory, int datanumberrows, int datanumbercolumns){
		 INDArray initdata=Nd4j.zeros(datanumberrows, datanumbercolumns);
    	 double value=distfactory.generateDoubleDataVector(1)[0]; 
		 initdata=initdata.add(value);
		 return initdata;
	}
	

}
