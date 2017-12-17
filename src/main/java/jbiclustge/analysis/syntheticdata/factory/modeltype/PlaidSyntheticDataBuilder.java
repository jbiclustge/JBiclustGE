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
package jbiclustge.analysis.syntheticdata.factory.modeltype;

import java.io.IOException;

import jbiclustge.analysis.syntheticdata.factory.AbstractSyntheticDataFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.PlaidBiclustersFactory;
import jbiclustge.analysis.syntheticdata.factory.builders.ISyntheticDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.builders.PlaidDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;
import jbiclustge.analysis.syntheticdata.model.DefaultSyntheticModelGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class PlaidSyntheticDataBuilder.
 */
public class PlaidSyntheticDataBuilder extends AbstractSyntheticDataFactory{

	
	/** The biclustersmeaneffect. */
	private double[] biclustersmeaneffect=null;
	
	/** The biclusterssdeffect. */
	private double[] biclusterssdeffect=null;
	
	/** The datarowsmeaneffect. */
	private double[] datarowsmeaneffect=null;
	
	/** The datarowssdeffect. */
	private double[] datarowssdeffect=null;
	
	/** The datacolsmeaneffect. */
	private double[] datacolsmeaneffect=null;
	
	/** The datacolssdeffect. */
	private double[] datacolssdeffect=null;
	
	/** The modelgenerator. */
	private AbstractDataModelGenerator modelgenerator;
	
	private PlaidBiclustersFactory bicfactory;

	/**
	 * Instantiates a new plaid synthetic data builder.
	 *
	 * @param properties the properties
	 */
	public PlaidSyntheticDataBuilder(SyntheticDataProperties properties) {
		super(properties);
		/*if(propertiescontainer.getDataMeanvalue()==0.0 && propertiescontainer.getDataStandardDeviationvalue()==1.0){
			propertiescontainer.setDataStandardDeviationvalue(0.5);
		}*/
			
	}
	
	
	public PlaidSyntheticDataBuilder(SyntheticDataProperties properties,PlaidBiclustersFactory bicfactory) {
		super(properties);
		this.bicfactory=bicfactory;
	}
	
	

	/**
	 * Sets the biclusters mean effects.
	 *
	 * @param biclustersmeaneffect the new biclusters mean effects
	 */
	public void setBiclustersMeanEffects(double...biclustersmeaneffect) {
		this.biclustersmeaneffect = biclustersmeaneffect;
	}

	/**
	 * Sets the biclusters standard deviation effects.
	 *
	 * @param biclusterssdeffect the new biclusters standard deviation effects
	 */
	public void setBiclustersStandardDeviationEffects(double...biclusterssdeffect) {
		this.biclusterssdeffect = biclusterssdeffect;
	}

	/**
	 * Sets the rows mean effects.
	 *
	 * @param datarowsmeaneffect the new rows mean effects
	 */
	public void setRowsMeanEffects(double... datarowsmeaneffect) {
		this.datarowsmeaneffect = datarowsmeaneffect;
	}

	/**
	 * Sets the row standard deviation effects.
	 *
	 * @param datarowssdeffect the new row standard deviation effects
	 */
	public void setRowStandardDeviationEffects(double...datarowssdeffect) {
		this.datarowssdeffect = datarowssdeffect;
	}

	/**
	 * Sets the columns mean effects.
	 *
	 * @param datacolsmeaneffect the new columns mean effects
	 */
	public void setColumnsMeanEffects(double...datacolsmeaneffect) {
		this.datacolsmeaneffect = datacolsmeaneffect;
	}

	/**
	 * Sets the columns standard deviation effects.
	 *
	 * @param datacolssdeffect the new columns standard deviation effects
	 */
	public void setColumnsStandardDeviationEffects(double...datacolssdeffect) {
		this.datacolssdeffect = datacolssdeffect;
	}



	/**
	 * Sets the model generator.
	 *
	 * @param modelgen the new model generator
	 */
	public void setModelGenerator(AbstractDataModelGenerator modelgen){
		this.modelgenerator=modelgen;
	}

	


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.AbstractSyntheticDataFactory#preprocessData()
	 */
	@Override
	protected void preprocessData() {
		
		
		
	}
	
	
    

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.AbstractSyntheticDataFactory#configureModelGenerator()
	 */
	@Override
	protected AbstractDataModelGenerator configureModelGenerator() throws IOException {
        if(this.modelgenerator==null){
        	modelgenerator=new DefaultSyntheticModelGenerator(getCurrentBaseParameters());
        }
        else
        	modelgenerator.setDataParameters(getCurrentBaseParameters());
        	
		return modelgenerator;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.AbstractSyntheticDataFactory#DataAssembler()
	 */
	@Override
	protected ISyntheticDataBuilder DataAssembler() {
		if(bicfactory==null)
			bicfactory=new PlaidBiclustersFactory(biclusterdatadistributionfactory, biclustersmeaneffect, biclusterssdeffect, datarowsmeaneffect, datarowssdeffect, datacolsmeaneffect, datacolssdeffect);
		PlaidDataBuilder builder=new PlaidDataBuilder(datanumberrows, 
				datanumbercolumns, 
				shapebiclusters, 
				biclusterdatadistributionfactory, 
				biclustersmeaneffect, bicfactory);
		builder.setDatasetBackground(datasetbackground);
		return builder;
	}

}
