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
 * A factory for creating ScaleBicluster objects.
 */
public class ScaleBiclusterFactory extends AbstractBiclusterFactory{

	
	/** The bicscalemeanvalue. */
	private double bicscalemeanvalue=0;
	
	/** The bicscalesdvalue. */
	private double bicscalesdvalue=1.0;
	
	/** The biccolmeanvalue. */
	private double biccolmeanvalue=0.0;
	
	/** The biccolsdvalue. */
	private double biccolsdvalue=1.0;
	
	

	/**
	 * Instantiates a new scale bicluster factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 * @param bicscalemeanvalue the bicscalemeanvalue
	 * @param bicscalesdvalue the bicscalesdvalue
	 * @param biccolmeanvalue the biccolmeanvalue
	 * @param biccolsdvalue the biccolsdvalue
	 */
	public ScaleBiclusterFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory,
			double bicscalemeanvalue,
			double bicscalesdvalue,
			double biccolmeanvalue,
			double biccolsdvalue) {
		super(biclusterdatadistributionfactory);
		this.bicscalemeanvalue=bicscalemeanvalue;
		this.bicscalesdvalue=bicscalesdvalue;
		this.biccolmeanvalue=biccolmeanvalue;
		this.biccolsdvalue=biccolsdvalue;
	}
	
	/**
	 * Instantiates a new scale bicluster factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 */
	public ScaleBiclusterFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory){
		super(biclusterdatadistributionfactory);
	}
	
	
	

	
	/**
	 * Sets the scale mean value.
	 *
	 * @param bicscalemeanvalue the bicscalemeanvalue
	 * @return the scale bicluster factory
	 */
	public ScaleBiclusterFactory setScaleMeanValue(double bicscalemeanvalue) {
		this.bicscalemeanvalue = bicscalemeanvalue;
		return this;
	}

	/**
	 * Sets the scale SD value.
	 *
	 * @param bicscalesdvalue the bicscalesdvalue
	 * @return the scale bicluster factory
	 */
	public ScaleBiclusterFactory setScaleSDValue(double bicscalesdvalue) {
		this.bicscalesdvalue = bicscalesdvalue;
		return this;
	}

	/**
	 * Sets the col mean value.
	 *
	 * @param biccolmeanvalue the biccolmeanvalue
	 * @return the scale bicluster factory
	 */
	public ScaleBiclusterFactory setColMeanValue(double biccolmeanvalue) {
		this.biccolmeanvalue = biccolmeanvalue;
		return this;
	}

	/**
	 * Sets the col SD value.
	 *
	 * @param biccolsdvalue the biccolsdvalue
	 * @return the scale bicluster factory
	 */
	public ScaleBiclusterFactory setColSDValue(double biccolsdvalue) {
		this.biccolsdvalue = biccolsdvalue;
		return this;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#getTypeOfBicluster()
	 */
	@Override
	public TypeBicluster getTypeOfBicluster() {
		return TypeBicluster.SCALE;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterWithoutBothDimOverlap(int, int)
	 */
	@Override
	public INDArray buildBiclusterWithoutBothDimOverlap(int nrows, int ncols) {
		
		double[] bicscale =biclusterdatadistributionfactory.generateDoubleDataVector(nrows, bicscalemeanvalue, bicscalesdvalue);
		double[] colbase=biclusterdatadistributionfactory.generateDoubleDataVector(ncols, biccolmeanvalue, biccolsdvalue);
		INDArray scalevector=Nd4j.create(bicscale);
		INDArray basevector=Nd4j.create(colbase);

		return scalevector.transpose().mmul(basevector);
	}


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithoutBothDimOverlap(java.util.ArrayList, boolean)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithoutBothDimOverlap(ArrayList<Pair<Integer, Integer>> shapebiclusters, boolean onedimoverlap) {
		
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
			
			double[][] bicscale =biclusterdatadistributionfactory.generateDoublesDataMatrix(highrowdim, shapebiclusters.size(), bicscalemeanvalue, bicscalesdvalue);
			double[][] colbase=biclusterdatadistributionfactory.generateDoublesDataMatrix(highcolumndim, shapebiclusters.size(), biccolmeanvalue, biccolsdvalue);
			
			for (int i = 0; i < shapebiclusters.size(); i++) {
				Pair<Integer, Integer> shapebic=shapebiclusters.get(i);
				
				INDArray scalevector=Nd4j.create(MTUMatrixUtils.getNElementsOfColumnofMatrixofdoubles(bicscale, i, shapebic.getValue0()));
				INDArray basevector=Nd4j.create(MTUMatrixUtils.getNElementsOfColumnofMatrixofdoubles(colbase, i, shapebic.getValue1()));
				
				biclusterset.add(scalevector.transpose().mmul(basevector));
			}

			return biclusterset;
		}
	}


	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithBothDimOverlap(int, int, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithBothDimOverlap(int datanumberrows, int datanumbercolumns, ArrayList<Pair<Integer, Integer>> shapebiclusters,ArrayList<Pair<Integer, Integer>> rowranges,  ArrayList<Pair<Integer, Integer>> colranges) {
		
		ArrayList<INDArray> biclusters=new ArrayList<>();
		
		double[] bicscale =biclusterdatadistributionfactory.generateDoubleDataVector(datanumberrows, bicscalemeanvalue, bicscalesdvalue);
		double[] colbase=biclusterdatadistributionfactory.generateDoubleDataVector(datanumbercolumns, biccolmeanvalue, biccolsdvalue);
		INDArray scalevector=Nd4j.create(bicscale);
		INDArray basevector=Nd4j.create(colbase);
		INDArray scaledmatrix =scalevector.transpose().mmul(basevector);
		
		/*ND4JUtils.printINDArrayMatrix(scaledmatrix, "\t");
		System.out.println("\n\n");*/
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			
			int initrow=rowranges.get(i).getValue0();
			int endrow=rowranges.get(i).getValue1();
			int initcol=colranges.get(i).getValue0();
			int endcol=colranges.get(i).getValue1();
			
			biclusters.add(scaledmatrix.get(NDArrayIndex.interval(initrow, endrow), NDArrayIndex.interval(initcol,endcol)));
			
		}
		
		//INDArray sub=matt.get(NDArrayIndex.interval(1, 3), NDArrayIndex.interval(1, 3));
		
		
		return biclusters;
	}
	
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		NormalDistributionDataFactory biclusterdatadistributionfactory=new NormalDistributionDataFactory();
		
		ScaleBiclusterFactory factory=new ScaleBiclusterFactory(biclusterdatadistributionfactory);
		INDArray bic=factory.buildBiclusterWithoutBothDimOverlap(10, 10);
		ND4JUtils.printINDArrayMatrix(bic, "\t");
		/*ArrayList<Pair<Integer, Integer>> shapebiclusters =new ArrayList<>();
		shapebiclusters.add(new Pair<Integer, Integer>(4, 5));
		shapebiclusters.add(new Pair<Integer, Integer>(6, 4));
		
		ArrayList<Pair<Integer, Integer>> rowranges=new ArrayList<>();
        rowranges.add(new Pair<Integer, Integer>(0, 4));
        rowranges.add(new Pair<Integer, Integer>(2, 8));
        
        ArrayList<Pair<Integer, Integer>> colranges=new ArrayList<>();
        colranges.add(new Pair<Integer, Integer>(0, 5));
        colranges.add(new Pair<Integer, Integer>(3, 7));
	
		
		
		ArrayList<INDArray> bics=fact.buildBiclusterSetWithBothDimOverlap(20, 10, shapebiclusters, rowranges, colranges);
		//ArrayList<INDArray> bics=fact.buildBiclusterSetWithoutBothDimOverlap(shapebiclusters, true);
		
		for (int i = 0; i < bics.size(); i++) {
			INDArray bic=bics.get(i);
			ND4JUtils.printINDArrayMatrix(bic, "\t");
			System.out.println();
		}*/
		
		
		
	}

}
