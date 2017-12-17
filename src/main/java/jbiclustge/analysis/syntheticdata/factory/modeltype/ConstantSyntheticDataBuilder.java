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
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import jbiclustge.analysis.syntheticdata.factory.AbstractSyntheticDataFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.ConstantBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.builders.DefaultDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.builders.ISyntheticDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGenerator;
import jbiclustge.analysis.syntheticdata.model.DefaultSyntheticModelGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class ConstantSyntheticDataBuilder.
 */
public class ConstantSyntheticDataBuilder extends AbstractSyntheticDataFactory{

	
	/** The biclusterssignal. */
	protected ArrayList<Integer> biclusterssignal=null;
	
	/** The modelgenerator. */
	protected AbstractDataModelGenerator modelgenerator;
	
	protected ConstantBiclusterFactory bicfactory;
	
	/**
	 * Instantiates a new constant synthetic data builder.
	 */
	public ConstantSyntheticDataBuilder(){
		super(null);
	}
	
	
	
	/**
	 * Instantiates a new constant synthetic data builder.
	 *
	 * @param properties the properties
	 */
	public ConstantSyntheticDataBuilder(SyntheticDataProperties properties) {
		super(properties);
	}
	
	
	public ConstantSyntheticDataBuilder(SyntheticDataProperties properties,ConstantBiclusterFactory bicfactory ) {
		super(properties);
		this.bicfactory=bicfactory;
	}
	
	/**
	 * Instantiates a new constant synthetic data builder.
	 *
	 * @param properties the properties
	 * @param biclustersignals the biclustersignals
	 */
	public ConstantSyntheticDataBuilder(SyntheticDataProperties properties, int...biclustersignals) {
		super(properties);
		addBiclusterSignals(biclustersignals);
	}
	
	/**
	 * Sets the model generator.
	 *
	 * @param modelgen the new model generator
	 */
	public void setModelGenerator(AbstractDataModelGenerator modelgen){
		this.modelgenerator=modelgen;
	}
	
	
	/**
	 * Adds the bicluster signals.
	 *
	 * @param signals the signals
	 * @return the constant synthetic data builder
	 */
	public ConstantSyntheticDataBuilder addBiclusterSignals(int...signals){
		this.biclusterssignal=new ArrayList<>(Arrays.asList(ArrayUtils.toObject(signals)));
		return this;
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
	 * @see analysis.syntheticdata.factory.AbstractSyntheticDataFactory#preprocessData()
	 */
	@Override
	protected void preprocessData() {
		
		if(biclusterssignal!=null && biclusterssignal.size()<numberbiclusters){
			ArrayList<Integer> tmpsignals=new ArrayList<>(numberbiclusters);
			for (int i = 0; i < numberbiclusters; i++) {
				if(i<biclusterssignal.size())
					tmpsignals.add(biclusterssignal.get(i));
				else
					tmpsignals.add(biclusterssignal.get(0));
			}
			biclusterssignal=tmpsignals;
		}
		
		
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.AbstractSyntheticDataFactory#DataAssembler()
	 */
	@Override
	protected ISyntheticDataBuilder DataAssembler() {
		
		if(bicfactory==null)
			bicfactory=new ConstantBiclusterFactory();
		if(biclusterssignal!=null){
			
			if(biclusterssignal.size()==1)
				bicfactory.setBiclusterCommonSignal(biclusterssignal.get(0));
			else
				bicfactory.setBiclustersSignal(biclusterssignal);
			
		}
			
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
