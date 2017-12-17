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
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;

import jbiclustge.analysis.syntheticdata.SyntheticDataStaticFunctions;
import jbiclustge.analysis.syntheticdata.distribution.ISyntheticDataDistributionFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating ConstantBicluster objects.
 */
public class ConstantBiclusterFactory extends AbstractBiclusterFactory{

	
	/** The biclusterssignal. */
	protected ArrayList<Integer> biclusterssignal=null;
	
	/** The currentbiclustersignal. */
	protected int currentbiclustersignal=1;
	
	/**
	 * Instantiates a new constant bicluster factory.
	 */
	public ConstantBiclusterFactory() {
        super(null);
	}
	
	/**
	 * Instantiates a new constant bicluster factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 */
	public ConstantBiclusterFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory) {
        super(biclusterdatadistributionfactory);
	}

	
	/**
	 * Sets the biclusters signal.
	 *
	 * @param bicsignal the new biclusters signal
	 */
	public void setBiclustersSignal(ArrayList<Integer> bicsignal){
		biclusterssignal=bicsignal;
	}
	
	/**
	 * Sets the biclusters signal.
	 *
	 * @param bicsignals the new biclusters signal
	 */
	public void setBiclustersSignal(int...bicsignals){
		biclusterssignal=new ArrayList<>(Arrays.asList(ArrayUtils.toObject(bicsignals)));
	}
	
	/**
	 * Sets the bicluster common signal.
	 *
	 * @param signal the new bicluster common signal
	 */
	public void setBiclusterCommonSignal(int signal){
		currentbiclustersignal=signal;
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#getTypeOfBicluster()
	 */
	@Override
	public TypeBicluster getTypeOfBicluster() {
		return TypeBicluster.CONSTANT;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterWithoutBothDimOverlap(int, int)
	 */
	@Override
	public INDArray buildBiclusterWithoutBothDimOverlap(int nrows, int ncols) {
		
		INDArray basevector=SyntheticDataStaticFunctions.makeVectorWithValue(ncols, currentbiclustersignal);
		INDArray scalevector=SyntheticDataStaticFunctions.makeVectorWithValue(nrows, 1.0);
		return scalevector.transpose().mmul(basevector);
	}



	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithoutBothDimOverlap(java.util.ArrayList, boolean)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithoutBothDimOverlap(ArrayList<Pair<Integer, Integer>> shapebiclusters,boolean onedimoverlap) {

		ArrayList<INDArray> biclusterset=new ArrayList<>();
		

		for (int i = 0; i < shapebiclusters.size(); i++) {
				
				if(biclusterssignal!=null && biclusterssignal.size()>=shapebiclusters.size()){
					currentbiclustersignal=biclusterssignal.get(i);
					if(currentbiclustersignal==0)
						currentbiclustersignal=1;
				}
				else
					currentbiclustersignal=1;
				
				Pair<Integer, Integer> shapebic=shapebiclusters.get(i);
				biclusterset.add(buildBiclusterWithoutBothDimOverlap(shapebic.getValue0(), shapebic.getValue1()));
			}
		
			return biclusterset;

	}



	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithBothDimOverlap(int, int, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithBothDimOverlap(int datanumberrows, int datanumbercolumns,
			ArrayList<Pair<Integer, Integer>> shapebiclusters, ArrayList<Pair<Integer, Integer>> rowranges,
			ArrayList<Pair<Integer, Integer>> colranges) {


		ArrayList<INDArray> biclusterset=new ArrayList<>();
		
		if(biclusterssignal!=null && biclusterssignal.size()>0){
					currentbiclustersignal=biclusterssignal.get(0);
					if(currentbiclustersignal==0)
						currentbiclustersignal=1;
				}
				else
					currentbiclustersignal=1;
		

		for (int i = 0; i < shapebiclusters.size(); i++) {

				Pair<Integer, Integer> shapebic=shapebiclusters.get(i);
				biclusterset.add(buildBiclusterWithoutBothDimOverlap(shapebic.getValue0(), shapebic.getValue1()));
		}
		
		return biclusterset;
	}


	

}
