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
package jbiclustge.analysis.syntheticdata.factory.modeltype.mix;

import java.io.IOException;
import java.util.ArrayList;

import jbiclustge.analysis.syntheticdata.factory.AbstractSyntheticDataFactory;
import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.builders.ISyntheticDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.builders.MixDataBuilder;
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGeneratorMultiBiclustersType;
import jbiclustge.analysis.syntheticdata.model.MixSyntheticModelGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class MixSyntheticDataBuilder.
 */
public class MixSyntheticDataBuilder extends AbstractSyntheticDataFactory{

	
	/** The modelgenerator. */
	private AbstractDataModelGeneratorMultiBiclustersType modelgenerator;
	
	/** The biclustergenerators. */
	private ArrayList<AbstractBiclusterFactory> biclustergenerators;
	

	/**
	 * Instantiates a new mix synthetic data builder.
	 *
	 * @param properties the properties
	 * @param listtypebiclusters the listtypebiclusters
	 */
	public MixSyntheticDataBuilder(SyntheticDataProperties properties, ArrayList<AbstractBiclusterFactory> listtypebiclusters) {
		super(properties);
		this.biclustergenerators=listtypebiclusters;
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
	protected AbstractDataModelGeneratorMultiBiclustersType configureModelGenerator() throws IOException {
		 if(modelgenerator==null){
			 this.modelgenerator=new MixSyntheticModelGenerator(getCurrentBaseParameters());
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
		
		MixDataBuilder builder=new MixDataBuilder(datanumberrows, 
				datanumbercolumns, 
				shapebiclusters, 
				biclusterdatadistributionfactory, 
				biclustersnoise, biclustergenerators);

		return builder;
	}

}
