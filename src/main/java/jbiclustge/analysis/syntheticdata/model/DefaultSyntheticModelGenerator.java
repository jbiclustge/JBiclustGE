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
import jbiclustge.analysis.syntheticdata.factory.props.SyntheticDataProperties;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultSyntheticModelGenerator.
 */
public class DefaultSyntheticModelGenerator extends AbstractDataModelGenerator{

	/** The rowmatinfo. */
	protected Pair<INDArray, ArrayList<Pair<Integer, Integer>>> rowmatinfo;
	
	/** The colmatinfo. */
	protected Pair<INDArray, ArrayList<Pair<Integer, Integer>>> colmatinfo;
	
	
	/**
	 * Instantiates a new default synthetic model generator.
	 *
	 * @param dataparameters the dataparameters
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public DefaultSyntheticModelGenerator(BaseSyntheticDataproperties dataparameters) throws IOException {
		super(dataparameters);
	}


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.model.AbstractDataModelGenerator#initializeBiclustersModel()
	 */
	@Override
	protected INDArray initializeBiclustersModel() throws IOException {
		
		INDArray identmat=Nd4j.eye(dataparameters.getNumberbiclusters());
		
		if(dataparameters.getOverlapBetweenBiclusters()==null){
			rowmatinfo=SyntheticDataStaticFunctions.makeRowMatrix(dataparameters.getNumberrowsdata(), dataparameters.getShapebiclusters(), dataparameters.getOverlapNumberBiclusterRows());
			colmatinfo=SyntheticDataStaticFunctions.makeColumnMatrix(dataparameters.getNumbercolumnsdata(), dataparameters.getShapebiclusters(), dataparameters.getOverlapNumberBiclusterColumns());
		}
		else{
			rowmatinfo=SyntheticDataStaticFunctions.makeRowMatrixAsymmetricOverlap(dataparameters.getNumberrowsdata(), dataparameters.getShapebiclusters(), dataparameters.getRowsOverlapListBetweenTwoBiclusters());
			colmatinfo=SyntheticDataStaticFunctions.makeColumnMatrixAsymmetricOverlap(dataparameters.getNumbercolumnsdata(), dataparameters.getShapebiclusters(), dataparameters.getColumnsOverlapListBetweenTwoBiclusters());
		}
		
		
		INDArray matonlyrow=rowmatinfo.getValue0().mmul(identmat);
		return matonlyrow.mmul(colmatinfo.getValue0().transpose());
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
		
		INDArray biclustersmatrix=null;
		ArrayList<INDArray> biclusters=null;
		
		ArrayList<Pair<Integer, Integer>> rowranges=rowmatinfo.getValue1();
		ArrayList<Pair<Integer, Integer>> colranges=colmatinfo.getValue1();
		ArrayList<Pair<Integer, Integer>> shapebiclusters=dataparameters.getShapebiclusters();
		
		if(!isbothdimensionoverlap){
			boolean onedimoverlap=false;
			if(dataparameters.getOverlapNumberBiclusterRows()>0 || dataparameters.getOverlapNumberBiclusterColumns()>0)
				onedimoverlap=true;
			
			biclusters=biclusterfactory.buildBiclusterSetWithoutBothDimOverlap(dataparameters.getShapebiclusters(), onedimoverlap);
		}
		else{
			biclusters=biclusterfactory.buildBiclusterSetWithBothDimOverlap(dataparameters.getNumberrowsdata(), dataparameters.getNumbercolumnsdata(), shapebiclusters, rowranges, colranges);
		}
			
			biclustersmatrix=initmodelmatrix.dup();
			
			
			
			for (int i = 0; i < biclusters.size(); i++) {
				INDArray bicluster=biclusters.get(i);
				//Pair<Integer, Integer> shape=shapebiclusters.get(i);
				//System.out.println(biclustersnoise[i]);
				if(biclustersnoise!=null && i<biclustersnoise.length && biclustersnoise[i]>0.0){	
					bicluster=SyntheticDataStaticFunctions.addNoiseToData(bicluster, biclustersnoise[i]);
				}
				
				Pair<Integer, Integer> rowdim=rowranges.get(i);
				Pair<Integer, Integer> coldim=colranges.get(i);

				SyntheticDataStaticFunctions.replaceBiclusterValuesInMatrix(biclustersmatrix, bicluster, rowdim.getValue0(), rowdim.getValue1(), coldim.getValue0(), coldim.getValue1());
			}

		
		return biclustersmatrix;
	}
	
 
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		//BaseSyntheticDataproperties props=new BaseSyntheticDataproperties();
		SyntheticDataProperties props=new SyntheticDataProperties();
		props.setNumberRowsDataset(30);
		props.setNumberColumnsDataset(30);
		ArrayList<Pair<Integer, Integer>> shapebics=new ArrayList<>();
		shapebics.add(new Pair<Integer, Integer>(10, 10));
		shapebics.add(new Pair<Integer, Integer>(10,10));
		shapebics.add(new Pair<Integer, Integer>(10,10));
		
		props.setShapeOfBiclusters(shapebics);
		
		props.appendOverlapBetweenTwoBiclusters(3, 6);
		props.appendOverlapBetweenTwoBiclusters(2, 4);
		
		/*props.setOverlapNumberBiclusterRows(5);
		props.setOverlapNumberBiclusterColumns(3)*/;
		
		DefaultSyntheticModelGenerator gener=new DefaultSyntheticModelGenerator(props);
    	 
 		 ND4JUtils.printINDArrayMatrix(gener.initializeBiclustersModel(), "\t");
    	 
    	 
     }
	

}
