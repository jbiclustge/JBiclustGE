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

import org.javatuples.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseSyntheticDataproperties.
 */
public class BaseSyntheticDataproperties {
	
	
	/** The numberrowsdata. */
	protected int numberrowsdata=300;
	
	/** The numbercolumnsdata. */
	protected int numbercolumnsdata=50;
	
	/** The numberbiclusters. */
	protected int numberbiclusters=1;
	
	/** The shapebiclusters. */
	protected ArrayList<Pair<Integer, Integer>> shapebiclusters=null;
	
	/** The overlapbetweenbiclusters. */
	protected ArrayList<Pair<Integer, Integer>> overlapbetweenbiclusters=null;
	
	/** The overlapbicrows. */
	protected int overlapbicrows=0;
	
	/** The overlapbiccolumns. */
	protected int overlapbiccolumns=0;
	
	/** The datadistributionmean. */
	protected double datadistributionmean=0.0;
	
	/** The datadistributionsd. */
	protected double datadistributionsd=1.0;
	
	
	/**
	 * Instantiates a new base synthetic dataproperties.
	 */
	public BaseSyntheticDataproperties(){}
	

	/**
	 * Gets the numberrowsdata.
	 *
	 * @return the numberrowsdata
	 */
	public int getNumberrowsdata() {
		return numberrowsdata;
	}
	
	

	/**
	 * Sets the number rows dataset.
	 *
	 * @param numberrowsdata the numberrowsdata
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setNumberRowsDataset(int numberrowsdata) {
		this.numberrowsdata = numberrowsdata;
		return this;
	}

	/**
	 * Gets the numbercolumnsdata.
	 *
	 * @return the numbercolumnsdata
	 */
	public int getNumbercolumnsdata() {
		return numbercolumnsdata;
	}

	/**
	 * Sets the number columns dataset.
	 *
	 * @param numbercolumnsdata the numbercolumnsdata
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setNumberColumnsDataset(int numbercolumnsdata) {
		this.numbercolumnsdata = numbercolumnsdata;
		return this;
	}


	/**
	 * Gets the numberbiclusters.
	 *
	 * @return the numberbiclusters
	 */
	public int getNumberbiclusters() {
		return numberbiclusters;
	}
	
	/**
	 * Gets the shapebiclusters.
	 *
	 * @return the shapebiclusters
	 */
	public ArrayList<Pair<Integer, Integer>> getShapebiclusters() {
		return shapebiclusters;
	}
	
	/**
	 * Gets the overlap between biclusters.
	 *
	 * @return the overlap between biclusters
	 */
	public ArrayList<Pair<Integer, Integer>> getOverlapBetweenBiclusters() {
		return overlapbetweenbiclusters;
	}
	
	
	/**
	 * Sets the shape of biclusters.
	 *
	 * @param shapebiclusters the shapebiclusters
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setShapeOfBiclusters(ArrayList<Pair<Integer, Integer>> shapebiclusters){
		this.shapebiclusters=shapebiclusters;
		this.numberbiclusters=shapebiclusters.size();
		return this;
	}
	
	/**
	 * Sets the overlap between biclusters.
	 *
	 * @param overlapbetweenbiclusters the overlapbetweenbiclusters
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setOverlapBetweenBiclusters(ArrayList<Pair<Integer, Integer>> overlapbetweenbiclusters){
		this.overlapbetweenbiclusters=overlapbetweenbiclusters;
		return this;
	}
	
	/**
	 * Reset overlap list between two biclusters.
	 */
	public void resetOverlapListBetweenTwoBiclusters(){
		this.overlapbetweenbiclusters=null;
	}
	
	/**
	 * Gets the rows overlap list between two biclusters.
	 *
	 * @return the rows overlap list between two biclusters
	 */
	public ArrayList<Integer> getRowsOverlapListBetweenTwoBiclusters(){
		ArrayList<Integer> res=new ArrayList<>();
		if(overlapbetweenbiclusters!=null){
			for (int i = 0; i < overlapbetweenbiclusters.size(); i++) {
				res.add(overlapbetweenbiclusters.get(i).getValue0());
			}
		}
		else{
			for (int i = 0; i < shapebiclusters.size()-1; i++) {
				res.add(overlapbicrows);
			}
		}
		return res;
	}
	

	/**
	 * Gets the columns overlap list between two biclusters.
	 *
	 * @return the columns overlap list between two biclusters
	 */
	public ArrayList<Integer> getColumnsOverlapListBetweenTwoBiclusters(){
		ArrayList<Integer> res=new ArrayList<>();
		if(overlapbetweenbiclusters!=null){
			for (int i = 0; i < overlapbetweenbiclusters.size(); i++) {
				res.add(overlapbetweenbiclusters.get(i).getValue1());
			}
		}
		else{
			for (int i = 0; i < shapebiclusters.size()-1; i++) {
				res.add(overlapbiccolumns);
			}
		}
		return res;
	}
	
	
	/**
	 * Gets the overlap number bicluster rows.
	 *
	 * @return the overlap number bicluster rows
	 */
	public int getOverlapNumberBiclusterRows() {
		return overlapbicrows;
	}

	/**
	 * Sets the overlap number bicluster rows.
	 *
	 * @param overlapbicrows the overlapbicrows
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setOverlapNumberBiclusterRows(int overlapbicrows) {
		this.overlapbicrows = overlapbicrows;
		return this;
	}

	/**
	 * Gets the overlap number bicluster columns.
	 *
	 * @return the overlap number bicluster columns
	 */
	public int getOverlapNumberBiclusterColumns() {
		return overlapbiccolumns;
	}
	
	

	/**
	 * Sets the overlap number bicluster columns.
	 *
	 * @param overlapbiccolumns the overlapbiccolumns
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setOverlapNumberBiclusterColumns(int overlapbiccolumns) {
		this.overlapbiccolumns = overlapbiccolumns;
		return this;
	}
	
	/**
	 * Gets the data meanvalue.
	 *
	 * @return the data meanvalue
	 */
	public double getDataMeanvalue() {
		return datadistributionmean;
	}

	/**
	 * Sets the data meanvalue.
	 *
	 * @param meandatavalue the meandatavalue
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setDataMeanvalue(double meandatavalue) {
		this.datadistributionmean = meandatavalue;
		return this;
	}

	/**
	 * Gets the data standard deviationvalue.
	 *
	 * @return the data standard deviationvalue
	 */
	public double getDataStandardDeviationvalue() {
		return datadistributionsd;
	}

	/**
	 * Sets the data standard deviationvalue.
	 *
	 * @param sddatavalue the sddatavalue
	 * @return the base synthetic dataproperties
	 */
	public BaseSyntheticDataproperties setDataStandardDeviationvalue(double sddatavalue) {
		this.datadistributionsd = sddatavalue;
		return this;
	}
}
