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

// TODO: Auto-generated Javadoc
/**
 * A factory for creating PlaidBiclusters objects.
 */
public class PlaidBiclustersFactory extends AbstractBiclusterFactory{

	/** The biclustersmeaneffect. */
	//private BaseSyntheticDataproperties dataparameters;
	private double[] biclustersmeaneffect=null;
	
	/** The biclusterssdeffect. */
	private double[] biclusterssdeffect=null;
	
	/** The datarowsmeaneffect. */
	private double[] datarowsmeaneffect=null;
	
	/** The datarowssdeffect. */
	private double[] datarowssdeffect=null;
	
	/** The datacolsmeaneffect. */
	private double[] datacolsmeaneffect=null;
	
	/** The datacolssdeffect. */
	private double[] datacolssdeffect=null;
	
	/** The currentbiclustermeaneffect. */
	private double currentbiclustermeaneffect=0.0;
	
	/** The currentbiclusterssdeffect. */
	//private double currentbiclusterssdeffect=0.5;
	private double currentbiclusterssdeffect=1.0;
	
	/** The currentdatarowsmeaneffect. */
	private double currentdatarowsmeaneffect=0.0;
	
	/** The currentdatarowssdeffect. */
	//private double currentdatarowssdeffect=0.5;
	private double currentdatarowssdeffect=1.0;
	
	/** The currentdatacolmeaneffect. */
	private double currentdatacolmeaneffect=0.0;
	
	/** The currentdatacolsdeffect. */
	//private double currentdatacolsdeffect=0.5;
	private double currentdatacolsdeffect=1.0;
	
	/**
	 * Instantiates a new plaid biclusters factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 */
	public PlaidBiclustersFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory) {
		super(biclusterdatadistributionfactory);

	}
	
	
	/**
	 * Instantiates a new plaid biclusters factory.
	 *
	 * @param biclusterdatadistributionfactory the biclusterdatadistributionfactory
	 * @param biclustermeaneffect the biclustermeaneffect
	 * @param biclusterssdeffect the biclusterssdeffect
	 * @param datarowsmeaneffect the datarowsmeaneffect
	 * @param datarowssdeffect the datarowssdeffect
	 * @param datacolsmeaneffect the datacolsmeaneffect
	 * @param datacolssdeffect the datacolssdeffect
	 */
	public PlaidBiclustersFactory(ISyntheticDataDistributionFactory biclusterdatadistributionfactory,
			double[] biclustermeaneffect, 
			double[] biclusterssdeffect,
			double[] datarowsmeaneffect,
			double[] datarowssdeffect,
			double[] datacolsmeaneffect,
			double[] datacolssdeffect) {
		this(biclusterdatadistributionfactory);
        this.biclustersmeaneffect=biclustermeaneffect;
        this.biclusterssdeffect=biclusterssdeffect;
        this.datarowsmeaneffect=datarowsmeaneffect;
        this.datarowssdeffect=datarowssdeffect;
        this.datacolsmeaneffect=datacolsmeaneffect;
        this.datacolssdeffect=datacolssdeffect;
        initdata();

	}
	
	
	
	
	
	
	/**
	 * Sets the bicluster mean effect.
	 *
	 * @param currentbiclustermeaneffect the currentbiclustermeaneffect
	 * @return the plaid biclusters factory
	 */
	public PlaidBiclustersFactory setBiclusterMeanEffect(double currentbiclustermeaneffect) {
		this.currentbiclustermeaneffect = currentbiclustermeaneffect;
		return this;
	}


	/**
	 * Sets the bicluster standard deviation effect.
	 *
	 * @param currentbiclusterssdeffect the currentbiclusterssdeffect
	 * @return the plaid biclusters factory
	 */
	public PlaidBiclustersFactory setBiclusterStandardDeviationEffect(double currentbiclusterssdeffect) {
		this.currentbiclusterssdeffect = currentbiclusterssdeffect;
		return this;
	}


	/**
	 * Sets the bicluster rows mean effect.
	 *
	 * @param currentdatarowsmeaneffect the currentdatarowsmeaneffect
	 * @return the plaid biclusters factory
	 */
	public PlaidBiclustersFactory setBiclusterRowsMeanEffect(double currentdatarowsmeaneffect) {
		this.currentdatarowsmeaneffect = currentdatarowsmeaneffect;
		return this;
	}


	/**
	 * Sets the bicluster rows SD effect.
	 *
	 * @param currentdatarowssdeffect the currentdatarowssdeffect
	 * @return the plaid biclusters factory
	 */
	public PlaidBiclustersFactory setBiclusterRowsSDEffect(double currentdatarowssdeffect) {
		this.currentdatarowssdeffect = currentdatarowssdeffect;
		return this;
	}


	/**
	 * Sets the bicluster column mean effect.
	 *
	 * @param currentdatacolmeaneffect the currentdatacolmeaneffect
	 * @return the plaid biclusters factory
	 */
	public PlaidBiclustersFactory setBiclusterColumnMeanEffect(double currentdatacolmeaneffect) {
		this.currentdatacolmeaneffect = currentdatacolmeaneffect;
		return this;
	}


	/**
	 * Sets the bicluster col SD effect.
	 *
	 * @param currentdatacolsdeffect the currentdatacolsdeffect
	 * @return the plaid biclusters factory
	 */
	public PlaidBiclustersFactory setBiclusterColSDEffect(double currentdatacolsdeffect) {
		this.currentdatacolsdeffect = currentdatacolsdeffect;
		return this;
	}


	/**
	 * Initdata.
	 */
	private void initdata(){
		
		if(biclustersmeaneffect!=null)
			currentbiclustermeaneffect=biclustersmeaneffect[0];
		if(biclusterssdeffect!=null)
			currentbiclusterssdeffect=biclusterssdeffect[0];
		if(datarowsmeaneffect!=null)
			currentdatarowsmeaneffect=datarowsmeaneffect[0];
		if(datarowssdeffect!=null)
			currentdatarowssdeffect=datarowssdeffect[0];
		if(datacolsmeaneffect!=null)
			currentdatacolmeaneffect=datacolsmeaneffect[0];
		if(datacolssdeffect!=null)
			currentdatacolsdeffect=datacolssdeffect[0];
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#getTypeOfBicluster()
	 */
	@Override
	public TypeBicluster getTypeOfBicluster() {
		return TypeBicluster.PLAID;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterWithoutBothDimOverlap(int, int)
	 */
	@Override
	public INDArray buildBiclusterWithoutBothDimOverlap(int nrows, int ncols) {
		
		double biclustereffect=biclusterdatadistributionfactory.generateDoubleDataVector(1, currentbiclustermeaneffect, currentbiclusterssdeffect)[0];
		double[] rowseffect=biclusterdatadistributionfactory.generateDoubleDataVector(nrows, currentdatarowsmeaneffect, currentdatarowssdeffect);
		double[] colseffect=biclusterdatadistributionfactory.generateDoubleDataVector(ncols, currentdatacolmeaneffect, currentdatacolsdeffect);

		
		INDArray rowvector=Nd4j.create(rowseffect);
		rowvector=rowvector.reshape(rowvector.columns(), 1);
		
		INDArray aux=SyntheticDataStaticFunctions.makeVectorWithValue(colseffect.length, 1.0);
		INDArray bicmatrix=rowvector.mmul(aux);
		
		INDArray colvector=Nd4j.create(colseffect);
		INDArray addcoleff=bicmatrix.addRowVector(colvector);
		
		INDArray res=addcoleff.add(biclustereffect);
		
		return res;
	}

	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.biclusters.AbstractBiclusterFactory#buildBiclusterSetWithoutBothDimOverlap(java.util.ArrayList, boolean)
	 */
	@Override
	public ArrayList<INDArray> buildBiclusterSetWithoutBothDimOverlap(ArrayList<Pair<Integer, Integer>> shapebiclusters,boolean onedimoverlap) {
		
		ArrayList<INDArray> biclusterset=new ArrayList<>();
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			
			if(biclustersmeaneffect!=null && biclustersmeaneffect.length==shapebiclusters.size())
				currentbiclustermeaneffect=biclustersmeaneffect[i];
			if(biclusterssdeffect!=null && biclusterssdeffect.length==shapebiclusters.size())
				currentbiclusterssdeffect=biclusterssdeffect[i];
			if(datarowsmeaneffect!=null && datarowsmeaneffect.length==shapebiclusters.size())
				currentdatarowsmeaneffect=datarowsmeaneffect[i];
			if(datarowssdeffect!=null && datarowssdeffect.length==shapebiclusters.size())
				currentdatarowssdeffect=datarowssdeffect[i];
			if(datacolsmeaneffect!=null && datacolsmeaneffect.length==shapebiclusters.size())
				currentdatacolmeaneffect=datacolsmeaneffect[i];
			if(datacolssdeffect!=null && datacolssdeffect.length==shapebiclusters.size())
				currentdatacolsdeffect=datacolssdeffect[i];

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
		
		ArrayList<INDArray> biclusters=new ArrayList<>();
		
		double[] biclustermeaneffect=biclusterdatadistributionfactory.generateDoubleDataVector(shapebiclusters.size(), currentbiclustermeaneffect, currentbiclusterssdeffect);
		double[] rowseffect=biclusterdatadistributionfactory.generateDoubleDataVector(datanumberrows, currentdatarowsmeaneffect, currentdatarowssdeffect);
		double[] colseffect=biclusterdatadistributionfactory.generateDoubleDataVector(datanumbercolumns, currentdatacolmeaneffect, currentdatacolsdeffect);
		
		INDArray rowvector=Nd4j.create(rowseffect);
		rowvector=rowvector.reshape(rowvector.columns(), 1);
		
		INDArray aux=SyntheticDataStaticFunctions.makeVectorWithValue(colseffect.length, 1.0);
		INDArray bicmatrix=rowvector.mmul(aux);
		
		INDArray colvector=Nd4j.create(colseffect);
		INDArray addcoleff=bicmatrix.addRowVector(colvector);
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			
			double biclustereffect=biclustermeaneffect[i];
			int initrow=rowranges.get(i).getValue0();
			int endrow=rowranges.get(i).getValue1();
			int initcol=colranges.get(i).getValue0();
			int endcol=colranges.get(i).getValue1();
			
			addBiclusterEffectToMatrix(addcoleff, initrow, endrow, initcol, endcol, biclustereffect);	
		}
		

		for (int i = 0; i < shapebiclusters.size(); i++) {
			
			int initrow=rowranges.get(i).getValue0();
			int endrow=rowranges.get(i).getValue1();
			int initcol=colranges.get(i).getValue0();
			int endcol=colranges.get(i).getValue1();
			
			biclusters.add(addcoleff.get(NDArrayIndex.interval(initrow, endrow), NDArrayIndex.interval(initcol,endcol)));
			
		}
		
		
		
		return biclusters;
	}
	
	/**
	 * Adds the bicluster effect to matrix.
	 *
	 * @param mat the mat
	 * @param initrow the initrow
	 * @param endrow the endrow
	 * @param initcol the initcol
	 * @param endcol the endcol
	 * @param valuetoadd the valuetoadd
	 */
	private void addBiclusterEffectToMatrix(INDArray mat, int initrow,int endrow, int initcol, int endcol, double valuetoadd){

		for (int i = initrow; i < endrow; i++) {
			for (int j = initcol; j < endcol; j++) {
				double valuein=mat.getDouble(i, j);
				double sum =valuein+valuetoadd;
				mat.putScalar(i, j, sum);
			}
			
		}
		
		
     }
	
	
 
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
    	 
    	 NormalDistributionDataFactory biclusterdatadistributionfactory=new NormalDistributionDataFactory();
    	 PlaidBiclustersFactory factory=new PlaidBiclustersFactory(biclusterdatadistributionfactory);
    	 INDArray bic=factory.buildBiclusterWithoutBothDimOverlap(10, 10);
 		 ND4JUtils.printINDArrayMatrix(bic, "\t");
    	 
    	 
     }

}
