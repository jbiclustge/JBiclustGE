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
import org.nd4j.linalg.indexing.NDArrayIndex;

import jbiclustge.analysis.syntheticdata.SyntheticDataStaticFunctions;
import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;
import jbiclustge.analysis.syntheticdata.distribution.NormalDistributionDataFactory;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating ConstantBiclusterWithAjustments objects.
 */
public class ConstantBiclusterWithAjustmentsFactory extends ConstantBiclusterFactory{

	
	/** The rowajustment. */
	private boolean rowajustment=false;
	
	/** The typeadjustment. */
	private int typeadjustment=0; // 0 = additive, 1= multiplicative
	
	
	/**
	 * Instantiates a new constant bicluster with ajustments factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 */
	public ConstantBiclusterWithAjustmentsFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory){
		super(biclusterdatadistributionfactory);
	}
	
	
	/**
	 * Sets the columns adjustment.
	 *
	 * @return the constant bicluster with ajustments factory
	 */
	public  ConstantBiclusterWithAjustmentsFactory setColumnsAdjustment(){
		this.rowajustment=false;
		return this;
	}
	
	/**
	 * Sets the rows adjustment.
	 *
	 * @return the constant bicluster with ajustments factory
	 */
	public  ConstantBiclusterWithAjustmentsFactory setRowsAdjustment(){
		this.rowajustment=true;
		return this;
	}
	
	/**
	 * Sets the multiplicative adjustment.
	 */
	public void setMultiplicativeAdjustment(){
		this.typeadjustment=1;
	}
	
	/**
	 * Sets the additive adjustment.
	 */
	public void setAdditiveAdjustment(){
		this.typeadjustment=0;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.ConstantBiclusterFactory#getTypeOfBicluster()
	 */
	@Override
	public TypeBicluster getTypeOfBicluster() {
		return TypeBicluster.CONSTANTADJUSTED;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.ConstantBiclusterFactory#buildBiclusterWithoutBothDimOverlap(int, int)
	 */
	@Override
	public INDArray buildBiclusterWithoutBothDimOverlap(int nrows, int ncols) {
		
		INDArray basevector=SyntheticDataStaticFunctions.makeVectorWithValue(ncols, currentbiclustersignal);
		INDArray scalevector=SyntheticDataStaticFunctions.makeVectorWithValue(nrows, 1.0);
		INDArray bicmatrix=scalevector.transpose().mmul(basevector);
		
		double[] adjustments=null;
		
		if(!rowajustment){
			adjustments=biclusterdatadistributionfactory.generateDoubleDataVector(ncols);
			
			for (int i = 0; i < bicmatrix.columns(); i++) {
				
				double adj=adjustments[i];
				
				for (int j = 0; j < bicmatrix.rows(); j++) {
					double value=0;
					if(typeadjustment==1)
						value=currentbiclustersignal*adj;
					else
					   value=currentbiclustersignal+adj;
					bicmatrix.putScalar(j, i, value);
				}
				
			}
		}
		else{
			adjustments=biclusterdatadistributionfactory.generateDoubleDataVector(nrows);
			
			for (int i = 0; i < bicmatrix.rows(); i++) {
				
				double adj=adjustments[i];
				
				for (int j = 0; j < bicmatrix.columns(); j++) {
					
					double value=0;
					if(typeadjustment==1)
						value=currentbiclustersignal*adj;
					else
					   value=currentbiclustersignal+adj;
					bicmatrix.putScalar(i, j, value);
				}
				
			}
			
		}
		return bicmatrix;
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.ConstantBiclusterFactory#buildBiclusterSetWithBothDimOverlap(int, int, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithBothDimOverlap(int datanumberrows, int datanumbercolumns,
			ArrayList<Pair<Integer, Integer>> shapebiclusters, ArrayList<Pair<Integer, Integer>> rowranges,
			ArrayList<Pair<Integer, Integer>> colranges) {


		ArrayList<INDArray> biclusterset=new ArrayList<>();
		
	/*	if(biclusterssignal!=null && biclusterssignal.size()>0){
					currentbiclustersignal=biclusterssignal.get(0);
					if(currentbiclustersignal==0)
						currentbiclustersignal=1;
				}
				else
					currentbiclustersignal=1;
		*/
		
		int totalrows=0;
		int totalcols=0;
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			Pair<Integer, Integer> shapebic=shapebiclusters.get(i);
			totalrows+=shapebic.getValue0();
			totalcols+=shapebic.getValue1();		
		}

		INDArray bigbicluster=buildBiclusterWithoutBothDimOverlap(totalrows, totalcols);
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			int initrow=rowranges.get(i).getValue0();
			int endrow=rowranges.get(i).getValue1();
			int initcol=colranges.get(i).getValue0();
			int endcol=colranges.get(i).getValue1();
			biclusterset.add(bigbicluster.get(NDArrayIndex.interval(initrow, endrow), NDArrayIndex.interval(initcol,endcol)));
		}
		
		return biclusterset;
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		NormalDistributionDataFactory f=new NormalDistributionDataFactory();
		ConstantBiclusterWithAjustmentsFactory factory=new ConstantBiclusterWithAjustmentsFactory(f);
		factory.setColumnsAdjustment();
        factory.setMultiplicativeAdjustment();
		factory.setBiclusterCommonSignal(5);
		INDArray bic=factory.buildBiclusterWithoutBothDimOverlap(10, 10);
		ND4JUtils.printINDArrayMatrix(bic, "\t");
		
	}
	
}
