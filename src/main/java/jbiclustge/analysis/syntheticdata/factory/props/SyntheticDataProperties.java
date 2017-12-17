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
package jbiclustge.analysis.syntheticdata.factory.props;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Class SyntheticDataProperties.
 */
public class SyntheticDataProperties extends BaseSyntheticDataproperties{
	
	
	/** The datanoise. */
	private double datanoise=0.0;
	
	/** The shuffledata. */
	private boolean shuffledata=false;
	
	/** The biclustersautoshape. */
	private boolean biclustersautoshape=false;
	
	/** The biclustersnoise. */
	private ArrayList<Double> biclustersnoise=null;


	/**
	 * Instantiates a new synthetic data properties.
	 */
	public SyntheticDataProperties(){
		super();
	}

	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setNumberRowsDataset(int)
	 */
	public SyntheticDataProperties setNumberRowsDataset(int numberrowsdata) {
		this.numberrowsdata = numberrowsdata;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setOverlapNumberBiclusterRows(int)
	 */
	public SyntheticDataProperties setOverlapNumberBiclusterRows(int overlapbicrows) {
		this.overlapbicrows = overlapbicrows;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setOverlapNumberBiclusterColumns(int)
	 */
	public SyntheticDataProperties setOverlapNumberBiclusterColumns(int overlapbiccolumns) {
		this.overlapbiccolumns = overlapbiccolumns;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setDataMeanvalue(double)
	 */
	public SyntheticDataProperties setDataMeanvalue(double meandatavalue) {
		this.datadistributionmean = meandatavalue;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setDataStandardDeviationvalue(double)
	 */
	public SyntheticDataProperties setDataStandardDeviationvalue(double sddatavalue) {
		this.datadistributionsd = sddatavalue;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setNumberColumnsDataset(int)
	 */
	public SyntheticDataProperties setNumberColumnsDataset(int numbercolumnsdata) {
		this.numbercolumnsdata = numbercolumnsdata;
		return this;
	}

	/**
	 * Append bicluster shape.
	 *
	 * @param nrows the nrows
	 * @param ncolumns the ncolumns
	 * @return the synthetic data properties
	 */
	public SyntheticDataProperties appendBiclusterShape(int nrows, int ncolumns){
		if(shapebiclusters==null)
			shapebiclusters=new ArrayList<>();
		shapebiclusters.add(new Pair<Integer, Integer>(nrows, ncolumns));
		numberbiclusters++;
		return this;
	}
	
	
	@Override
	public SyntheticDataProperties setShapeOfBiclusters(ArrayList<Pair<Integer, Integer>> shapebiclusters){
		this.shapebiclusters=shapebiclusters;
		this.numberbiclusters=shapebiclusters.size();
		return this;
	}
	
	
	/**
	 * Append overlap between two biclusters.
	 *
	 * @param noverlrows the noverlrows
	 * @param noverlcols the noverlcols
	 * @return the synthetic data properties
	 */
	public SyntheticDataProperties appendOverlapBetweenTwoBiclusters(int noverlrows, int noverlcols){
		if(overlapbetweenbiclusters==null)
			overlapbetweenbiclusters=new ArrayList<>();
		overlapbetweenbiclusters.add(new Pair<Integer, Integer>(noverlrows, noverlcols));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see analysis.syntheticdata.factory.properties.BaseSyntheticDataproperties#setOverlapBetweenBiclusters(java.util.ArrayList)
	 */
	public SyntheticDataProperties setOverlapBetweenBiclusters(ArrayList<Pair<Integer, Integer>> overlapbetweenbiclusters){
		this.overlapbetweenbiclusters=overlapbetweenbiclusters;
		return this;
	}
	
	/**
	 * Sets the number biclusters same shape.
	 *
	 * @param numberbiclusters the numberbiclusters
	 * @return the synthetic data properties
	 */
	public SyntheticDataProperties setNumberBiclustersSameShape(int numberbiclusters) {
		this.numberbiclusters = numberbiclusters;
		this.biclustersautoshape=true;
		return this;
	}
	
	/**
	 * Sets the number biclusters same shape.
	 *
	 * @param numberbiclusters the numberbiclusters
	 * @param numberbicrows the numberbicrows
	 * @param numbercolrows the numbercolrows
	 * @return the synthetic data properties
	 */
	public SyntheticDataProperties setNumberBiclustersSameShape(int numberbiclusters, int numberbicrows, int numbercolrows) {
		for (int i = 0; i < numberbiclusters; i++) {
			appendBiclusterShape(numberbicrows, numbercolrows);
		}
		return this;
	}


    /**
     * Sets the biclusters noise.
     *
     * @param noiseeachbicluster the noiseeachbicluster
     * @return the synthetic data properties
     */
    public SyntheticDataProperties setBiclustersNoise(double... noiseeachbicluster){
    	Double[] elems=ArrayUtils.toObject(noiseeachbicluster);
    	this.biclustersnoise=new ArrayList<>(Arrays.asList(elems));
    	return this;
    }
    
    
    /**
     * Sets the biclusters noise.
     *
     * @param noiseeachbicluster the noiseeachbicluster
     * @return the synthetic data properties
     */
    public SyntheticDataProperties setBiclustersNoise(ArrayList<Double> noiseeachbicluster){
    	this.biclustersnoise=noiseeachbicluster;
    	return this;
    }
    
    

	/**
	 * Gets the biclusters noise.
	 *
	 * @return the biclusters noise
	 */
	public ArrayList<Double> getBiclustersNoise() {
		return biclustersnoise;
	}

   

	/**
	 * Gets the data noise.
	 *
	 * @return the data noise
	 */
	public double getDataNoise() {
		return datanoise;
	}



	/**
	 * Sets the data noise.
	 *
	 * @param datanoise the datanoise
	 * @return the synthetic data properties
	 */
	public SyntheticDataProperties setDataNoise(double datanoise) {
		this.datanoise = datanoise;
		return this;
	}



	/**
	 * Checks if is sufflebics.
	 *
	 * @return true, if is sufflebics
	 */
	public boolean isSufflebics() {
		return shuffledata;
	}


	/**
	 * Sets the shuffle data.
	 *
	 * @param shuffledata the shuffledata
	 * @return the synthetic data properties
	 */
	public SyntheticDataProperties setShuffleData(boolean shuffledata) {
		this.shuffledata = shuffledata;
		return this;
	}



	/**
	 * Checks if is biclustersautoshape.
	 *
	 * @return true, if is biclustersautoshape
	 */
	public boolean isBiclustersautoshape() {
		return biclustersautoshape;
	}

	
	
	
	

}
