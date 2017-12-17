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
package jbiclustge.analysis.syntheticdata.factory.biclusters;

import java.util.ArrayList;

import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

import jbiclustge.analysis.syntheticdata.SyntheticDataStaticFunctions;
import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;
import jbiclustge.analysis.syntheticdata.distribution.NormalDistributionDataFactory;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;
import pt.ornrocha.arrays.MTUMatrixUtils;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating ShiftBicluster objects.
 */
public class ShiftBiclusterFactory extends AbstractBiclusterFactory{

	/** The bicshiftmeanvalue. */
	private double bicshiftmeanvalue=0;
	
	/** The bicshiftsdvalue. */
	private double bicshiftsdvalue=1.0;
	
	/** The biccolmeanvalue. */
	private double biccolmeanvalue=0.0;
	
	/** The biccolsdvalue. */
	private double biccolsdvalue=1.0;
	
	
	/**
	 * Instantiates a new shift bicluster factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 */
	public ShiftBiclusterFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory){
		super(biclusterdatadistributionfactory);
	}
	
	/**
	 * Instantiates a new shift bicluster factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 * @param bicshiftmeanvalue the bicshiftmeanvalue
	 * @param bicshiftsdvalue the bicshiftsdvalue
	 * @param biccolmeanvalue the biccolmeanvalue
	 * @param biccolsdvalue the biccolsdvalue
	 */
	public ShiftBiclusterFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory,
			double bicshiftmeanvalue,
			double bicshiftsdvalue,
			double biccolmeanvalue,
			double biccolsdvalue) {
		super(biclusterdatadistributionfactory);
		this.bicshiftmeanvalue=bicshiftmeanvalue;
		this.bicshiftsdvalue=bicshiftsdvalue;
		this.biccolmeanvalue=biccolmeanvalue;
		this.biccolsdvalue=biccolsdvalue;
	}

	
	
	
	
	
	
	/**
	 * Sets the shift mean value.
	 *
	 * @param bicshiftmeanvalue the bicshiftmeanvalue
	 * @return the shift bicluster factory
	 */
	public ShiftBiclusterFactory setShiftMeanValue(double bicshiftmeanvalue) {
		this.bicshiftmeanvalue = bicshiftmeanvalue;
		return this;
	}

	/**
	 * Sets the shift SD value.
	 *
	 * @param bicshiftsdvalue the bicshiftsdvalue
	 * @return the shift bicluster factory
	 */
	public ShiftBiclusterFactory setShiftSDValue(double bicshiftsdvalue) {
		this.bicshiftsdvalue = bicshiftsdvalue;
		return this;
	}

	/**
	 * Sets the col mean value.
	 *
	 * @param biccolmeanvalue the biccolmeanvalue
	 * @return the shift bicluster factory
	 */
	public ShiftBiclusterFactory setColMeanValue(double biccolmeanvalue) {
		this.biccolmeanvalue = biccolmeanvalue;
		return this;
	}

	/**
	 * Sets the col SD value.
	 *
	 * @param biccolsdvalue the biccolsdvalue
	 * @return the shift bicluster factory
	 */
	public ShiftBiclusterFactory setColSDValue(double biccolsdvalue) {
		this.biccolsdvalue = biccolsdvalue;
		return this;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#getTypeOfBicluster()
	 */
	@Override
	public TypeBicluster getTypeOfBicluster() {
		return TypeBicluster.SHIFT;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterWithoutBothDimOverlap(int, int)
	 */
	@Override
	public INDArray buildBiclusterWithoutBothDimOverlap(int nrows, int ncols) {
		double[] bicshift =biclusterdatadistributionfactory.generateDoubleDataVector(nrows, bicshiftmeanvalue, bicshiftsdvalue);
		double[] colbase=biclusterdatadistributionfactory.generateDoubleDataVector(ncols, biccolmeanvalue, biccolsdvalue);
		
		INDArray scalevector=SyntheticDataStaticFunctions.makeVectorWithValue(nrows, 1.0);
		INDArray shiftvector=Nd4j.create(bicshift);
		INDArray rowshiftvectorreshape =shiftvector.reshape(shiftvector.length(), 1);
		INDArray basevector=Nd4j.create(colbase);
		
		INDArray bicdist=scalevector.transpose().mmul(basevector);
		return bicdist.addColumnVector(rowshiftvectorreshape);
		
	}



	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithoutBothDimOverlap(java.util.ArrayList, boolean)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithoutBothDimOverlap(ArrayList<Pair<Integer, Integer>> shapebiclusters,boolean onedimoverlap) {

		ArrayList<INDArray> biclusterset=new ArrayList<>();
		
		if(!onedimoverlap){
			for (int i = 0; i < shapebiclusters.size(); i++) {
				Pair<Integer, Integer> shapebic=shapebiclusters.get(i);
				biclusterset.add(buildBiclusterWithoutBothDimOverlap(shapebic.getValue0(), shapebic.getValue1()));
			}
		
			return biclusterset;
		}
		else{
			
			int highrowdim=SyntheticDataStaticFunctions.getRowHighestDimension(shapebiclusters);
			int highcolumndim=SyntheticDataStaticFunctions.getColumnHighestDimension(shapebiclusters);
			
			double[][] bicshift =biclusterdatadistributionfactory.generateDoublesDataMatrix(highrowdim, shapebiclusters.size(), bicshiftmeanvalue, bicshiftsdvalue);
			double[][] colbase=biclusterdatadistributionfactory.generateDoublesDataMatrix(highcolumndim, shapebiclusters.size(), biccolmeanvalue, biccolsdvalue);
			
			
			
			for (int i = 0; i < shapebiclusters.size(); i++) {
				Pair<Integer, Integer> shapebic=shapebiclusters.get(i);
				
				INDArray shiftvector=Nd4j.create(MTUMatrixUtils.getNElementsOfColumnofMatrixofdoubles(bicshift, i, shapebic.getValue0()));
				INDArray rowshiftvectorreshape =shiftvector.reshape(shiftvector.length(), 1);
				INDArray scalevector=SyntheticDataStaticFunctions.makeVectorWithValue(shapebic.getValue0(), 1.0);
				INDArray basevector=Nd4j.create(MTUMatrixUtils.getNElementsOfColumnofMatrixofdoubles(colbase, i, shapebic.getValue1()));
				
				INDArray bicdist=scalevector.transpose().mmul(basevector);
				
				biclusterset.add(bicdist.addColumnVector(rowshiftvectorreshape));
			}

			return biclusterset;
		}
	}



	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithBothDimOverlap(int, int, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithBothDimOverlap(int datanumberrows, int datanumbercolumns,
			ArrayList<Pair<Integer, Integer>> shapebiclusters, ArrayList<Pair<Integer, Integer>> rowranges,
			ArrayList<Pair<Integer, Integer>> colranges) {


		ArrayList<INDArray> biclusters=new ArrayList<>();
		
		double[] bicshift =biclusterdatadistributionfactory.generateDoubleDataVector(datanumberrows, bicshiftmeanvalue, bicshiftsdvalue);
		double[] colbase=biclusterdatadistributionfactory.generateDoubleDataVector(datanumbercolumns, biccolmeanvalue, biccolsdvalue);
		INDArray shiftvector=Nd4j.create(bicshift);
		INDArray rowshiftvectorreshape =shiftvector.reshape(shiftvector.length(), 1);
		INDArray scalevector=SyntheticDataStaticFunctions.makeVectorWithValue(datanumberrows, 1.0);
		INDArray basevector=Nd4j.create(colbase);
		
		INDArray bicdist=scalevector.transpose().mmul(basevector);
		INDArray shiftmatrix =bicdist.addColumnVector(rowshiftvectorreshape);
		
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			
			int initrow=rowranges.get(i).getValue0();
			int endrow=rowranges.get(i).getValue1();
			int initcol=colranges.get(i).getValue0();
			int endcol=colranges.get(i).getValue1();
			
			biclusters.add(shiftmatrix.get(NDArrayIndex.interval(initrow, endrow), NDArrayIndex.interval(initcol,endcol)));
			
		}
		
		
		return biclusters;
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		NormalDistributionDataFactory d=new NormalDistributionDataFactory();
		ShiftBiclusterFactory factory=new ShiftBiclusterFactory(d);
		/*factory.setColMeanValue(200);
		factory.setColSDValue(30);
		factory.setShiftSDValue(20);*/
		
		

		
		//factory.setShiftMeanValue(200);
		INDArray bic=factory.buildBiclusterWithoutBothDimOverlap(10, 10);
		ND4JUtils.printINDArrayMatrix(bic, "\t");
		
	}


}
