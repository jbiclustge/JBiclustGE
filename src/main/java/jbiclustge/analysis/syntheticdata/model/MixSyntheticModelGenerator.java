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
package jbiclustge.analysis.syntheticdata.model;

import java.io.IOException;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jbiclustge.analysis.syntheticdata.SyntheticDataStaticFunctions;
import jbiclustge.analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory;
import jbiclustge.analysis.syntheticdata.factory.props.BaseSyntheticDataproperties;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class MixSyntheticModelGenerator.
 */
public class MixSyntheticModelGenerator extends AbstractDataModelGeneratorMultiBiclustersType{

	/** The rowmatinfo. */
	protected Pair<INDArray, ArrayList<Pair<Integer, Integer>>> rowmatinfo;
	
	/** The colmatinfo. */
	protected Pair<INDArray, ArrayList<Pair<Integer, Integer>>> colmatinfo;
	
	
	
	
	/**
	 * Instantiates a new mix synthetic model generator.
	 *
	 * @param dataparameters the dataparameters
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public MixSyntheticModelGenerator(BaseSyntheticDataproperties dataparameters) throws IOException {
		super(dataparameters);
	}

	

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.model.AbstractDataModelGenerator#initializeBiclustersModel()
	 */
	@Override
	protected INDArray initializeBiclustersModel() {
		
		INDArray identmat=Nd4j.eye(dataparameters.getNumberbiclusters());
		
		if(!isbothdimensionoverlap){
			rowmatinfo=SyntheticDataStaticFunctions.makeRowMatrix(dataparameters.getNumberrowsdata(), dataparameters.getShapebiclusters(), dataparameters.getOverlapNumberBiclusterRows());
			colmatinfo=SyntheticDataStaticFunctions.makeColumnMatrix(dataparameters.getNumbercolumnsdata(), dataparameters.getShapebiclusters(), dataparameters.getOverlapNumberBiclusterColumns());
		
		
			INDArray matonlyrow=rowmatinfo.getValue0().mmul(identmat);
			INDArray out=matonlyrow.mmul(colmatinfo.getValue0().transpose());
		//ND4JUtils.printINDArrayMatrix(out, "\t");
			return out;
		}
		else
			throw new UnsupportedOperationException("Only supports one dimension overlap");
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.model.AbstractDataModelGenerator#getRowMatrixModelInfo()
	 */
	@Override
	public Pair<INDArray, ArrayList<Pair<Integer, Integer>>> getRowMatrixModelInfo() {
		return rowmatinfo;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.model.AbstractDataModelGenerator#getColumnMatrixModelInfo()
	 */
	@Override
	public Pair<INDArray, ArrayList<Pair<Integer, Integer>>> getColumnMatrixModelInfo() {
		return colmatinfo;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.model.AbstractDataModelGenerator#generateBiclustersMatrix(analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory, double[])
	 */
	@Override
	public INDArray generateBiclustersMatrix(AbstractBiclusterFactory biclusterfactory, double[] biclustersnoise) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.model.AbstractDataModelGeneratorMultiBiclustersType#generateBiclustersMatrix(java.util.ArrayList, double[])
	 */
	@Override
	public INDArray generateBiclustersMatrix(ArrayList<AbstractBiclusterFactory> biclusterfactory, double[] biclustersnoise) {
		
		
		INDArray biclustersmatrix=null;
		ArrayList<INDArray> biclusters=new ArrayList<>();
		
		ArrayList<Pair<Integer, Integer>> rowranges=rowmatinfo.getValue1();
		ArrayList<Pair<Integer, Integer>> colranges=colmatinfo.getValue1();	

		ArrayList<Pair<Integer, Integer>> biclustersshape=dataparameters.getShapebiclusters();
		
		

		
		for (int i = 0; i < biclustersshape.size(); i++) {
			
			Pair<Integer, Integer> bicshape=biclustersshape.get(i);
			AbstractBiclusterFactory factory=null;

			if(i<=biclusterfactory.size())
				factory=biclusterfactory.get(i);
			else
				factory=biclusterfactory.get(0);
			
			
            biclusters.add(factory.buildBiclusterWithoutBothDimOverlap(bicshape.getValue0(), bicshape.getValue1()));
		}
		
		biclustersmatrix=initmodelmatrix.dup();
	    //ND4JUtils.printINDArrayMatrix(biclustersmatrix, "\t");
		for (int i = 0; i < biclusters.size(); i++) {
			INDArray bicluster=biclusters.get(i);
			
			if(biclustersnoise!=null && i<=biclustersnoise.length && biclustersnoise[i]>0.0){	
				bicluster=SyntheticDataStaticFunctions.addNoiseToData(bicluster, biclustersnoise[i]);
			}
				
			Pair<Integer, Integer> rowdim=rowranges.get(i);
			Pair<Integer, Integer> coldim=colranges.get(i);

			SyntheticDataStaticFunctions.replaceBiclusterValuesInMatrix(biclustersmatrix, bicluster, rowdim.getValue0(), rowdim.getValue1(), coldim.getValue0(), coldim.getValue1());
			//ND4JUtils.printINDArrayMatrix(biclustersmatrix, "\t");
			//System.out.println("\n\n");
		}

		
		return biclustersmatrix;
		
	}
	
	
	

}
