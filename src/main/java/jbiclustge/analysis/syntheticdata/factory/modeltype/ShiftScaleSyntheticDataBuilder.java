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
import jbiclustge.analysis.syntheticdata.factory.biclusters.ShiftScaleBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.builders.DefaultDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.builders.ISyntheticDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;
import jbiclustge.analysis.syntheticdata.model.DefaultSyntheticModelGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class ShiftScaleSyntheticDataBuilder.
 */
public class ShiftScaleSyntheticDataBuilder extends AbstractSyntheticDataFactory{

	/** The bicscalemeanvalue. */
	private double bicscalemeanvalue=0;
	
	/** The bicscalesdvalue. */
	private double bicscalesdvalue=1.0;
	
	/** The bicshiftmeanvalue. */
	private double bicshiftmeanvalue=0;
	
	/** The bicshiftsdvalue. */
	private double bicshiftsdvalue=1.0;
	
	/** The biccolmeanvalue. */
	private double biccolmeanvalue=0.0;
	
	/** The biccolsdvalue. */
	private double biccolsdvalue=1.0;
	
	/** The modelgenerator. */
	private AbstractDataModelGenerator modelgenerator;
	
	
	private ShiftScaleBiclusterFactory bicfactory;
	/**
	 * Instantiates a new shift scale synthetic data builder.
	 *
	 * @param properties the properties
	 */
	public ShiftScaleSyntheticDataBuilder(SyntheticDataProperties properties){
		super(properties);
	}
	
	
	public ShiftScaleSyntheticDataBuilder(SyntheticDataProperties properties,ShiftScaleBiclusterFactory bicfactory){
		super(properties);
		this.bicfactory=bicfactory;
	}
	
	/**
	 * Instantiates a new shift scale synthetic data builder.
	 *
	 * @param properties the properties
	 * @param bicshiftmeanvalue the bicshiftmeanvalue
	 * @param bicshiftsdvalue the bicshiftsdvalue
	 * @param bicscalemeanvalue the bicscalemeanvalue
	 * @param bicscalesdvalue the bicscalesdvalue
	 * @param biccolmeanvalue the biccolmeanvalue
	 * @param biccolsdvalue the biccolsdvalue
	 */
	public ShiftScaleSyntheticDataBuilder(SyntheticDataProperties properties,
			double bicshiftmeanvalue,
			double bicshiftsdvalue,
			double bicscalemeanvalue, 
			double bicscalesdvalue,
			double biccolmeanvalue,
			double biccolsdvalue) {
		super(properties);
		this.bicshiftmeanvalue=bicshiftmeanvalue;
		this.bicshiftsdvalue=bicshiftsdvalue;
		this.bicscalemeanvalue=bicscalemeanvalue;
		this.bicscalesdvalue=bicscalesdvalue;
		this.biccolmeanvalue=biccolmeanvalue;
		this.biccolsdvalue=biccolsdvalue;
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
		// TODO Auto-generated method stub
		
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
			bicfactory=new ShiftScaleBiclusterFactory(biclusterdatadistributionfactory, bicshiftmeanvalue, bicshiftsdvalue, bicscalemeanvalue, bicscalesdvalue, biccolmeanvalue, biccolsdvalue);
		DefaultDataBuilder builder=new DefaultDataBuilder(datanumberrows, 
				datanumbercolumns, 
				shapebiclusters, 
				biclusterdatadistributionfactory, 
				biclustersnoise,
				bicfactory);
		builder.setDatasetBackground(datasetbackground);
		return builder;
	}



}
