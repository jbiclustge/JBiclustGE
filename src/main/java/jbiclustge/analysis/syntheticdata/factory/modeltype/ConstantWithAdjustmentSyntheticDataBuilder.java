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

import jbiclustge.analysis.syntheticdata.distribution.NormalDistributionDataFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.ConstantBiclusterWithAjustmentsFactory;
import jbiclustge.analysis.syntheticdata.factory.builders.DefaultDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.builders.ISyntheticDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class ConstantWithAdjustmentSyntheticDataBuilder.
 */
public class ConstantWithAdjustmentSyntheticDataBuilder extends ConstantSyntheticDataBuilder{
	
	
	/** The bicfactory. */
	private ConstantBiclusterWithAjustmentsFactory bicfactory;
	
	/**
	 * Instantiates a new constant with adjustment synthetic data builder.
	 *
	 * @param properties the properties
	 */
	public ConstantWithAdjustmentSyntheticDataBuilder(SyntheticDataProperties properties){
		super(properties);
	}
	
	/**
	 * Instantiates a new constant with adjustment synthetic data builder.
	 *
	 * @param properties the properties
	 * @param biclustersignals the biclustersignals
	 */
	public ConstantWithAdjustmentSyntheticDataBuilder(SyntheticDataProperties properties, int...biclustersignals) {
		super(properties, biclustersignals);

	}
	
	/**
	 * Sets the constant bicluster factory.
	 *
	 * @param bicfactory the bicfactory
	 * @return the constant with adjustment synthetic data builder
	 */
	public ConstantWithAdjustmentSyntheticDataBuilder setConstantBiclusterFactory(ConstantBiclusterWithAjustmentsFactory bicfactory){
		this.bicfactory=bicfactory;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.modeltype.ConstantSyntheticDataBuilder#DataAssembler()
	 */
	@Override
	protected ISyntheticDataBuilder DataAssembler() {
		
		if(bicfactory==null)
			bicfactory=new ConstantBiclusterWithAjustmentsFactory(new NormalDistributionDataFactory());
		
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
