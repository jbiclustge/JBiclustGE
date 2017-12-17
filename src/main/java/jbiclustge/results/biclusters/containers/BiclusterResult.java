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
package jbiclustge.results.biclusters.containers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javatuples.Triplet;
import org.json.JSONObject;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jrplot.rbinders.components.dataframe.DefaultDataframeContainer;
import jrplot.rbinders.components.dataframe.datatype.DoubleDataColumn;
import jrplot.rbinders.components.dataframe.datatype.StringDataColumn;
import jrplot.rbinders.components.dataframe.datatype.rownames.StringColumnRowNames;
import jrplot.rbinders.components.interfaces.IDataFrameDataLoader;
import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.jsonutils.MTUJsonUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.mathutils.MTUMathUtils;


// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterResult.
 */
public class BiclusterResult implements Comparable<BiclusterResult>{


	/** The indexinresults. */
	private int indexinresults=-1;

	/** The genesindex. */
	private ArrayList<Integer> genesindex;

	/** The genenames. */
	private ArrayList<String> genenames;

	/** The conditionsindex. */
	private ArrayList<Integer> conditionsindex;

	/** The conditionnames. */
	private ArrayList<String> conditionnames;

	/** The originaldataset. */
	private ExpressionData originaldataset;

	/** The additionalresultmatrix. */
	private Object[][] additionalresultmatrix;

	/** The additionalinfo. */
	private HashMap<String, Object> additionalinfo=new HashMap<>();

	/** The needsconfiguration. */
	protected boolean needsconfiguration=false;

	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param genenameslist the genenameslist
	 * @param conditionslist the conditionslist
	 */
	private BiclusterResult(ArrayList<String> genenameslist, ArrayList<String> conditionslist){
		this.genenames=genenameslist;
		this.conditionnames=conditionslist;
		needsconfiguration=true;
	}

	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param geneindexes the geneindexes
	 * @param condindexes the condindexes
	 */
	private BiclusterResult(Integer[] geneindexes, Integer[] condindexes){
		this.genesindex=new ArrayList<>(Arrays.asList(geneindexes));
		this.conditionsindex=new ArrayList<>(Arrays.asList(condindexes));
		needsconfiguration=true;
	}

	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param originaldataset the originaldataset
	 */
	public BiclusterResult(ExpressionData originaldataset){
		this.originaldataset=originaldataset;
		this.genesindex=new ArrayList<>();
		this.conditionsindex=new ArrayList<>();
	}

	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param originaldataset the originaldataset
	 * @param geneindexes the geneindexes
	 * @param condindexes the condindexes
	 * @param mapnames the mapnames
	 */
	public BiclusterResult(ExpressionData originaldataset, ArrayList<Integer> geneindexes, ArrayList<Integer> condindexes, boolean mapnames){
		this.originaldataset=originaldataset;
		this.genesindex=geneindexes;
		this.conditionsindex=condindexes;
		if(mapnames){
			this.genenames=getGeneNames();
			this.conditionnames=getConditionNames();
		}
	}

	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param originaldataset the originaldataset
	 * @param geneindexes the geneindexes
	 * @param condindexes the condindexes
	 * @param mapnames the mapnames
	 */
	public BiclusterResult(ExpressionData originaldataset, int[] geneindexes, int[] condindexes, boolean mapnames){
		this.originaldataset=originaldataset;
		this.genesindex=new ArrayList<>();
		addGeneIndexes(geneindexes);
		this.conditionsindex=new ArrayList<>();
		addConditionIndexes(condindexes);
		if(mapnames){
			this.genenames=getGeneNames();
			this.conditionnames=getConditionNames();
		}
	}



	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param originaldataset the originaldataset
	 * @param geneindexes the geneindexes
	 * @param condindexes the condindexes
	 * @param mapnames the mapnames
	 * @param subtractindex the subtractindex
	 */
	public BiclusterResult(ExpressionData originaldataset, int[] geneindexes, int[] condindexes, boolean mapnames, int subtractindex){
		this.originaldataset=originaldataset;
		this.genesindex=new ArrayList<>();
		addGeneIndexesWithindexSubtraction(geneindexes, subtractindex);
		this.conditionsindex=new ArrayList<>();
		addConditonIndexesWithindexSubtraction(condindexes, subtractindex);
		if(mapnames){
			this.genenames=getGeneNames();
			this.conditionnames=getConditionNames();
		}
	}


	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param originaldataset the originaldataset
	 * @param genenameslist the genenameslist
	 * @param conditionslist the conditionslist
	 */
	public BiclusterResult(ExpressionData originaldataset, ArrayList<String> genenameslist, ArrayList<String> conditionslist){
		this.originaldataset=originaldataset;
		this.genesindex=setIndexListFromNames(genenameslist, originaldataset.getGeneNamesList());
		this.conditionsindex=setIndexListFromNames(conditionslist, originaldataset.getConditionsList());
		this.genenames=genenameslist;
		this.conditionnames=conditionslist;
	}

	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param genenameslist the genenameslist
	 * @param conditionslist the conditionslist
	 * @param geneindexes the geneindexes
	 * @param condindexes the condindexes
	 */
	public BiclusterResult(ArrayList<String> genenameslist, ArrayList<String> conditionslist, ArrayList<Integer> geneindexes, ArrayList<Integer> condindexes){
		this.genenames=genenameslist;
		this.conditionnames=conditionslist;
		this.genesindex=geneindexes;
		this.conditionsindex=condindexes;
	}


	/**
	 * Instantiates a new bicluster result.
	 *
	 * @param originaldataset the originaldataset
	 * @param genenameslist the genenameslist
	 * @param geneindexes the geneindexes
	 * @param conditionslist the conditionslist
	 * @param condindexes the condindexes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BiclusterResult(ExpressionData originaldataset, ArrayList<String> genenameslist, ArrayList<Integer> geneindexes, ArrayList<String> conditionslist, ArrayList<Integer> condindexes) throws IOException{
		this.originaldataset=originaldataset;
		if(genenameslist!=null && geneindexes!=null && genenameslist.size()!=geneindexes.size())
			throw new IOException("The list of gene names and gene indexes have diferent sizes");
		else if(conditionslist!=null && condindexes!=null && conditionslist.size()!=condindexes.size())
			throw new IOException("The list of condition names and conditon indexes have diferent sizes");
		else{

			if(genenameslist!=null && geneindexes==null){
				this.genenames=genenameslist;
				this.genesindex=setIndexListFromNames(genenameslist,originaldataset.getGeneNamesList());
			}
			else if(genenameslist==null && geneindexes!=null){
				this.genesindex=geneindexes;
				this.genenames=getGeneNames();
			}
			else 
				throw new IOException("Both gene names and gene indexes have null values");

			if(conditionslist!=null && condindexes==null){
				this.conditionnames=conditionslist;
				this.conditionsindex=setIndexListFromNames(conditionslist,originaldataset.getConditionsList());
			}
			else if(conditionslist==null && condindexes!=null){
				this.conditionsindex=condindexes;
				this.conditionnames=getConditionNames();
			}
			else 
				throw new IOException("Both condition names and conditon indexes have null values");


		}

	}

	private BiclusterResult(int indexinresults,
			ArrayList<Integer> genesindex,
			ArrayList<String> genenames,
			ArrayList<Integer> conditionsindex,
			ArrayList<String> conditionnames,
			Object[][] additionalresultmatrix,
			HashMap<String, Object> additionalinfo) {

		this.indexinresults=indexinresults;
		this.genesindex=genesindex;
		this.genenames=genenames;
		this.conditionsindex=conditionsindex;
		this.conditionnames=conditionnames;
		this.additionalresultmatrix=additionalresultmatrix;
		this.additionalinfo=additionalinfo;
		needsconfiguration=true;
	}


	/**
	 * Parses the gene and chip tags to names.
	 */
	public void parseGeneAndChipTagsToNames(){
		if(originaldataset!=null){
			String tag="gene";
			this.genesindex=new ArrayList<>();
			for (int i = 0; i < genenames.size(); i++) {
				Integer index=getParsedIndex(tag, genenames.get(i));
				if(index!=null)
					genesindex.add(index);
			}
			this.genenames=null;
			this.genenames=getGeneNames();

			tag="chip";
			this.conditionsindex=new ArrayList<>();
			for (int i = 0; i < conditionnames.size(); i++) {
				Integer index=getParsedIndex(tag, conditionnames.get(i));
				if(index!=null)
					conditionsindex.add(index);
			}
			this.conditionnames=null;
			this.conditionnames=getConditionNames();
		}
	}


	/**
	 * Gets the parsed index.
	 *
	 * @param tag the tag
	 * @param currentname the currentname
	 * @return the parsed index
	 */
	private Integer getParsedIndex(String tag, String currentname){
		String patin=tag+"(\\d+)";
		Pattern pat=Pattern.compile(patin);
		Matcher mt=pat.matcher(currentname);
		if(mt.find()){
			try {
				return Integer.parseInt(mt.group(1));
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}



	/**
	 * Sets the index of algorithm result.
	 *
	 * @param ind the new index of algorithm result
	 */
	public void setIndexOfAlgorithmResult(int ind){
		this.indexinresults=ind;
	}

	/**
	 * Gets the bicluster index.
	 *
	 * @return the bicluster index
	 */
	public int getBiclusterIndex(){
		return indexinresults;
	}



	/**
	 * Adds the gene index.
	 *
	 * @param index the index
	 */
	public void addGeneIndex(int index){
		if(genesindex==null)
			genesindex=new ArrayList<>();
		this.genesindex.add(index);
	}


	/**
	 * Adds the gene indexes.
	 *
	 * @param indexes the indexes
	 */
	public void addGeneIndexes(int[] indexes){
		for (int i : indexes) {
			addGeneIndex(i);
		}
	}


	/**
	 * Adds the gene indexes withindex subtraction.
	 *
	 * @param indexes the indexes
	 * @param subtract the subtract
	 */
	public void addGeneIndexesWithindexSubtraction(int[] indexes, int subtract){

		for (int i : indexes) {
			addGeneIndex(i-subtract);
		}

	}


	/**
	 * Gets the gene index at vector position.
	 *
	 * @param pos the pos
	 * @return the gene index at vector position
	 */
	public int getGeneIndexAtVectorPosition(int pos){
		if(pos<getGeneIndexes().size())
			return genesindex.get(pos);
		return -1;
	}

	/**
	 * Adds the condition index.
	 *
	 * @param index the index
	 */
	public void addConditionIndex(int index){
		this.conditionsindex.add(index);
	}

	/**
	 * Adds the condition indexes.
	 *
	 * @param indexes the indexes
	 */
	public void addConditionIndexes(int[] indexes){
		for (int i : indexes) {
			addConditionIndex(i);
		}
	}


	/**
	 * Adds the conditon indexes withindex subtraction.
	 *
	 * @param indexes the indexes
	 * @param subtract the subtract
	 */
	public void addConditonIndexesWithindexSubtraction(int[] indexes, int subtract){

		for (int i : indexes) {
			addConditionIndex(i-subtract);
		}

	}


	/**
	 * Gets the condition index at vector position.
	 *
	 * @param pos the pos
	 * @return the condition index at vector position
	 */
	public int getConditionIndexAtVectorPosition(int pos){
		if(pos<getConditionIndexes().size())
			return conditionsindex.get(pos);
		return -1;
	}


	/**
	 * Gets the number genes.
	 *
	 * @return the number genes
	 */
	public int getNumberGenes(){
		if(genenames!=null)
			return genenames.size();
		else
			return genesindex.size();
	}

	/**
	 * Gets the number conditions.
	 *
	 * @return the number conditions
	 */
	public int getNumberConditions(){
		if(conditionnames!=null)
			return conditionnames.size();
		else
			return conditionsindex.size();
	}

	/**
	 * Gets the shape.
	 *
	 * @return the shape
	 */
	public int[] getShape(){
		return new int[]{getNumberGenes(), getNumberConditions()};
	}

	/**
	 * Area.
	 *
	 * @return the int
	 */
	public int area(){
		return getNumberGenes()*getNumberConditions();
	}

	/**
	 * Gets the gene indexes.
	 *
	 * @return the gene indexes
	 */
	public ArrayList<Integer> getGeneIndexes(){

		if(genesindex==null){
			loadGeneIndexesFromGeneNames();
		}

		return genesindex;
	}



	/**
	 * Gets the array gene indexes.
	 *
	 * @return the array gene indexes
	 */
	public Integer[] getArrayGeneIndexes(){
		return getGeneIndexes().toArray(new Integer[getGeneIndexes().size()]);
	}


	/**
	 * Gets the condition indexes.
	 *
	 * @return the condition indexes
	 */
	public ArrayList<Integer> getConditionIndexes(){
		if(conditionsindex==null)
			loadConditionIndexesFromConditionsNames();
		return conditionsindex;
	}

	/**
	 * Gets the array condition indexes.
	 *
	 * @return the array condition indexes
	 */
	public Integer[] getArrayConditionIndexes(){
		return getConditionIndexes().toArray(new Integer[getConditionIndexes().size()]);
	}


	/**
	 * Load gene indexes from gene names.
	 */
	public void loadGeneIndexesFromGeneNames(){
		if(originaldataset!=null && genenames!=null){
			genesindex=new ArrayList<>();
			for (int i = 0; i <genenames.size(); i++) {
				String geneid=genenames.get(i);
				genesindex.add(originaldataset.getGeneNamesList().indexOf(geneid));
			}
		}
	}


	/**
	 * Load condition indexes from conditions names.
	 */
	public void loadConditionIndexesFromConditionsNames(){
		if(originaldataset!=null && conditionnames!=null){
			conditionsindex=new ArrayList<>();
			for (int i = 0; i <conditionnames.size(); i++) {
				String condid=conditionnames.get(i);
				conditionsindex.add(originaldataset.getConditionsList().indexOf(condid));
			}
		}
	}


	/**
	 * Gets the bicluster expression databy row dimension.
	 *
	 * @return the bicluster expression databy row dimension
	 */
	public double[][] getBiclusterExpressionDatabyRowDimension(){
		double[][] res=new double[getNumberGenes()][getNumberConditions()];
		for (int i = 0; i < getNumberGenes(); i++) {
			int rowindex=getGeneIndexAtVectorPosition(i);
			for (int j = 0; j < getNumberConditions(); j++) {
				int colindex=getConditionIndexAtVectorPosition(j);
				res[i][j]=originaldataset.getValueAt(rowindex, colindex);
			}
		}
		return res;
	}
	

	/**
	 * Gets the bicluster expression databy colunm dimension.
	 *
	 * @return the bicluster expression databy colunm dimension
	 */
	public double[][] getBiclusterExpressionDatabyColunmDimension(){
		double[][] res=new double[getNumberConditions()][getNumberGenes()];
		for (int i = 0; i < getNumberConditions(); i++) {
			int colindex=getConditionIndexAtVectorPosition(i);
			for (int j = 0; j < getNumberGenes(); j++) {
				int rowindex=getGeneIndexAtVectorPosition(j);
				res[i][j]=originaldataset.getValueAt(rowindex, colindex);
			}
		}
		return res;
	}


	/**
	 * Gets the ordered bicluster expression databy row dimension.
	 *
	 * @return the ordered bicluster expression databy row dimension
	 */
	public double[][] getOrderedBiclusterExpressionDatabyRowDimension(){
		double[][] res=new double[getNumberGenes()][getNumberConditions()];

		if(genesindex==null && originaldataset!=null)
			loadGeneIndexesFromGeneNames();
		if(conditionsindex==null && originaldataset!=null)
			loadConditionIndexesFromConditionsNames();

		ArrayList<Integer> copygeneindexes=new ArrayList<>(genesindex);
		ArrayList<Integer> copycondindexes=new ArrayList<>(conditionsindex);

		Collections.sort(copygeneindexes);
		Collections.sort(copycondindexes);

		for (int i = 0; i < copygeneindexes.size(); i++) {
			int rowindex=copygeneindexes.get(i);
			for (int j = 0; j < copycondindexes.size(); j++) {
				int colindex=copycondindexes.get(j);
				res[i][j]=originaldataset.getValueAt(rowindex, colindex);
			}
		}
		return res;
	}
	
	
	public Triplet<ArrayList<String>, ArrayList<String>, double[][]> getOrderedBiclusterbyExpressionDatabyRowDimension(){
		return new Triplet<ArrayList<String>, ArrayList<String>, double[][]>(getGeneNamesOrderedAsExpressionDataset(), 
																			getConditionNamesOrderedAsExpressionDataset(), 
																			getOrderedBiclusterExpressionDatabyRowDimension());
	}
	

	/**
	 * Gets the ordered bicluster expression databy colunm dimension.
	 *
	 * @return the ordered bicluster expression databy colunm dimension
	 */
	public double[][] getOrderedBiclusterExpressionDatabyColunmDimension(){
		double[][] res=new double[getNumberConditions()][getNumberGenes()];

		if(genesindex==null && originaldataset!=null)
			loadGeneIndexesFromGeneNames();
		if(conditionsindex==null && originaldataset!=null)
			loadConditionIndexesFromConditionsNames();

		ArrayList<Integer> copygeneindexes=new ArrayList<>(genesindex);
		ArrayList<Integer> copycondindexes=new ArrayList<>(conditionsindex);
		Collections.sort(copygeneindexes);
		Collections.sort(copycondindexes);



		for (int i = 0; i < copycondindexes.size(); i++) {
			int colindex=copycondindexes.get(i);
			for (int j = 0; j < copygeneindexes.size(); j++) {
				int rowindex=copygeneindexes.get(j);
				res[i][j]=originaldataset.getValueAt(rowindex, colindex);
			}
		}
		return res;
	}
	
	
	
	public Triplet<ArrayList<String>, ArrayList<String>, double[][]> getOrderedBiclusterbyExpressionDatabyColunmDimension(){
		return new Triplet<ArrayList<String>, ArrayList<String>, double[][]>(getGeneNamesOrderedAsExpressionDataset(), 
																			getConditionNamesOrderedAsExpressionDataset(), 
																			getOrderedBiclusterExpressionDatabyColunmDimension());
	}



	/**
	 * Append additional info.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void appendAdditionalInfo(String key, Object value){
		this.additionalinfo.put(key, value);
	}

	/**
	 * Gets the additional info.
	 *
	 * @param key the key
	 * @return the additional info
	 */
	public Object getAdditionalInfo(String key){
		if(additionalinfo!=null && additionalinfo.containsKey(key))
			return additionalinfo.get(key);
		return null;
	}

	/**
	 * Gets the map of additional info.
	 *
	 * @return the map of additional info
	 */
	public HashMap<String, Object> getMapOfAdditionalInfo(){
		return additionalinfo;
	}

	/**
	 * Sets the additional information.
	 *
	 * @param info the info
	 */
	public void setAdditionalInformation(HashMap<String, Object> info){
		this.additionalinfo=info;
	}

	/**
	 * Gets the additional info keyset.
	 *
	 * @return the additional info keyset
	 */
	public ArrayList<String> getAdditionalInfoKeyset(){
		if(additionalinfo!=null && additionalinfo.size()>0)
			return new ArrayList<>(additionalinfo.keySet());
		return null;
	}



	/**
	 * Gets the gene names.
	 *
	 * @return the gene names
	 */
	public ArrayList<String> getGeneNames(){
		if(genenames==null)
			loadGeneNamesFromIndexes();
		return genenames;
	}
	
	public String[] getGeneNamesArray(){
		if(getGeneNames()!=null)
			return getGeneNames().toArray(new String[getGeneNames().size()]);
		return null;
	}

	/**
	 * Gets the gene names ordered as expression dataset.
	 *
	 * @return the gene names ordered as expression dataset
	 */
	public ArrayList<String> getGeneNamesOrderedAsExpressionDataset(){
		ArrayList<String> genesnames=getGeneNames();
		ArrayList<Integer> geneindexes=getGeneIndexes();

		ArrayList<Integer> copygeneindexes=new ArrayList<>(geneindexes);
		Collections.sort(copygeneindexes);

		ArrayList<String> res=new ArrayList<>();
		for (int i = 0; i < copygeneindexes.size(); i++) {
			int orderedindex=copygeneindexes.get(i);
			res.add(genesnames.get(geneindexes.indexOf(orderedindex)));
		}
		return res;
	}


	public String[] getOrderedGeneNamesArray(){
		ArrayList<String> order=getGeneNamesOrderedAsExpressionDataset();
		return order.toArray(new String[order.size()]);
	}


	/**
	 * Load gene names from indexes.
	 */
	protected void loadGeneNamesFromIndexes(){
		genenames=new ArrayList<>(genesindex.size());
		for (int i = 0; i < genesindex.size(); i++) {
			genenames.add(originaldataset.getGeneID(genesindex.get(i)));
		}
	}




	/**
	 * Gets the transformed gene names to mapped GO annotations.
	 *
	 * @param mappedgenes the mappedgenes
	 * @return the transformed gene names to mapped GO annotations
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ArrayList<String> getTransformedGeneNamesToMappedGOAnnotations(Map<String, String> mappedgenes) throws IOException{
		ArrayList<String> names=getGeneNames();
		ArrayList<String> res=new ArrayList<>();
		for (int i = 0; i < names.size(); i++) {
			String geneid=names.get(i);
			if(!mappedgenes.containsKey(geneid))
				throw new IOException("Gene ID ["+geneid+"] do not have a GO annotation associated");
			else
				res.add(mappedgenes.get(geneid));
		}
		return res;
	}



	/**
	 * Sets the associated expression data.
	 *
	 * @param data the new associated expression data
	 */
	public void setAssociatedExpressionData(ExpressionData data){
		this.originaldataset=data;
		if(needsconfiguration){
			if(genesindex==null && conditionsindex==null){
				loadGeneIndexesFromGeneNames();
				loadConditionIndexesFromConditionsNames();
			}
			else if(genenames==null && conditionnames==null){
				loadConditionNamesFromIndexes();
				loadGeneNamesFromIndexes();
			}

		}
	}

	/**
	 * Gets the associated expression data.
	 *
	 * @return the associated expression data
	 */
	public ExpressionData getAssociatedExpressionData(){
		return originaldataset;
	}


	/**
	 * Gets the condition names.
	 *
	 * @return the condition names
	 */
	public ArrayList<String> getConditionNames(){
		if(conditionnames==null)
			loadConditionNamesFromIndexes();
		return conditionnames;
	}
	
	public String[] getConditionNamesArray(){
		if(getConditionNames()!=null)
			return getConditionNames().toArray(new String[getConditionNames().size()]);
		return null;
	}


	/**
	 * Gets the condition names ordered as expression dataset.
	 *
	 * @return the condition names ordered as expression dataset
	 */
	public ArrayList<String> getConditionNamesOrderedAsExpressionDataset(){
		ArrayList<String> condnames=getConditionNames();
		ArrayList<Integer> condindexes=getConditionIndexes();

		ArrayList<Integer> copycondindexes=new ArrayList<>(condindexes);
		Collections.sort(copycondindexes);

		ArrayList<String> res=new ArrayList<>();
		for (int i = 0; i < copycondindexes.size(); i++) {
			int orderedindex=copycondindexes.get(i);
			res.add(condnames.get(condindexes.indexOf(orderedindex)));
		}
		return res;
	}

	public String[] getOrderedConditionNamesArray(){
		ArrayList<String> order=getConditionNamesOrderedAsExpressionDataset();
		return order.toArray(new String[order.size()]);
	}


	/**
	 * Load condition names from indexes.
	 */
	protected void loadConditionNamesFromIndexes(){
		conditionnames=new ArrayList<>(conditionsindex.size());
		for (int i = 0; i < conditionsindex.size(); i++) {
			conditionnames.add(originaldataset.getConditionName(conditionsindex.get(i)));
		}
	}

	/*	protected ArrayList<Integer> setIndexListFromNames(ArrayList<String> idlist, String[] listofindexedids){
		ArrayList<Integer> res=new ArrayList<>();

		for (int i = 0; i < listofindexedids.length; i++) {
			String id=listofindexedids[i];
			if(idlist.contains(id))
				res.add(i);
		}

		return res;
	}*/

	/**
	 * Sets the index list from names.
	 *
	 * @param idlist the idlist
	 * @param datasetgenevector the datasetgenevector
	 * @return the array list
	 */
	protected ArrayList<Integer> setIndexListFromNames(ArrayList<String> idlist, ArrayList<String> datasetgenevector){
		ArrayList<Integer> res=new ArrayList<>();

		for (int i = 0; i < idlist.size(); i++) {
			if(datasetgenevector.contains(idlist.get(i)))
				res.add(datasetgenevector.indexOf(idlist.get(i)));
			else
				try {
					throw new Exception("Element ["+idlist.get(i)+"]  do not exist in original gene expression dataset");
				} catch (Exception e) {
					LogMessageCenter.getLogger().addCriticalErrorMessage("Error :", e);
				}
		}

		return res;
	}

	/**
	 * Sets the additionalresultmatrix.
	 *
	 * @param extrainfomatrix the new additionalresultmatrix
	 */
	public void setadditionalresultmatrix(Object[][] extrainfomatrix){
		this.additionalresultmatrix=extrainfomatrix;
	}


	/**
	 * Gets the additional result matrix.
	 *
	 * @return the additional result matrix
	 */
	public Object[][] getAdditionalResultMatrix() {
		return additionalresultmatrix;
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BiclusterResult o) { 
		return area()-o.area();
	}

	/**
	 * Checks if is subset of bicluster.
	 *
	 * @param tocompare the tocompare
	 * @return true, if is subset of bicluster
	 */
	public boolean isSubsetOfBicluster(BiclusterResult tocompare){
		boolean issubset=true;
		Set<String> geneintersection=MTUMathUtils.getIntersection(genenames, tocompare.getGeneNames());
		if(geneintersection.size()!=genenames.size())
			issubset=false;

		if(issubset){
			Set<String> condintersection=MTUMathUtils.getIntersection(conditionnames, tocompare.getConditionNames());
			if(condintersection.size()!=conditionnames.size())
				issubset=false;
		}
		return issubset;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder str=new StringBuilder();

		ArrayList<String> genenames=getGeneNames();
		str.append("Genes: "+genenames.size()+"\n");
		for (int i = 0; i < genenames.size() ; i++) {
			if(genesindex!=null)
				str.append("[Name: "+genenames.get(i)+", Index: "+genesindex.get(i)+"]");
			else
				str.append("[Name: "+genenames.get(i));
			if(i<(genenames.size()-1))
				str.append("\t");
		}
		str.append("\n\n");


		ArrayList<String> condnames=getConditionNames();
		str.append("Conditions: "+condnames.size()+"\n");
		for (int i = 0; i < condnames.size() ; i++) {
			if(conditionsindex!=null)
				str.append("[Name: "+condnames.get(i)+", Index: "+conditionsindex.get(i)+"]");
			else
				str.append("[Name: "+condnames.get(i));
			if(i<(condnames.size()-1))
				str.append("\t");
		}
		str.append("\n\n");
		str.append("Additional Information: "+"\n");
		if(additionalinfo.size()>0){
			ArrayList<String> keys=getAdditionalInfoKeyset();
			for (int i = 0; i < keys.size(); i++) {
				str.append(keys.get(i)+": "+additionalinfo.get(keys.get(i))+"\n");
			}
		}


		return str.toString();

	}

	/**
	 * Copy.
	 *
	 * @return the bicluster result
	 * @throws Exception the exception
	 */
	public BiclusterResult copy() throws Exception{
		BiclusterResult copyinstance=null;
		if(this.genenames!=null && this.conditionnames!=null)
			copyinstance=new BiclusterResult((ArrayList<String>)MTUCollectionsUtils.deepCloneObject(this.genenames), (ArrayList<String>)MTUCollectionsUtils.deepCloneObject(this.conditionnames));
		else
			copyinstance=new BiclusterResult(getArrayGeneIndexes(), getArrayConditionIndexes());
		if(originaldataset!=null)
			copyinstance.setAssociatedExpressionData(originaldataset);
		if(getMapOfAdditionalInfo()!=null)
			copyinstance.setAdditionalInformation(getMapOfAdditionalInfo());
		if(getAdditionalResultMatrix()!=null)
			copyinstance.setadditionalresultmatrix(getAdditionalResultMatrix());
		return copyinstance;
	}

	/**
	 * Gets the biclust package outpu format.
	 *
	 * @return the biclust package outpu format
	 */
	public String getBiclustPackageOutpuFormat(){
		StringBuilder str =new StringBuilder();
		ArrayList<String> genenames=getGeneNames();
		ArrayList<String> conditionnames=getConditionNames();

		str.append(genenames.size()+" "+conditionnames.size()+"\n");
		for (int i = 0; i < genenames.size() ; i++) {
			str.append(genenames.get(i));
			if(i<(genenames.size()-1))
				str.append(" ");
		}
		str.append("\n");
		for (int i = 0; i < conditionnames.size(); i++) {
			str.append(conditionnames.get(i));
			if(i<(conditionnames.size()-1))
				str.append(" ");
		}
		str.append("\n");
		return str.toString();
	}

	/**
	 * Gets the bic overlapper output format.
	 *
	 * @return the bic overlapper output format
	 */
	public String getBicOverlapperOutputFormat(){
		StringBuilder str =new StringBuilder();
		ArrayList<String> genenames=getGeneNames();
		ArrayList<String> conditionnames=getConditionNames();

		for (int i = 0; i < genenames.size() ; i++) {
			str.append(genenames.get(i));
			if(i<(genenames.size()-1))
				str.append(" ");
		}
		str.append("\n");
		for (int i = 0; i < conditionnames.size(); i++) {
			str.append(conditionnames.get(i));
			if(i<(conditionnames.size()-1))
				str.append(" ");
		}
		str.append("\n");
		return str.toString();
	}


	/** The Constant GENESTAG. */
	public static final String GENESTAG="Genes";

	/** The Constant CONDITIONSTAG. */
	public static final String CONDITIONSTAG="Conditions";

	/** The Constant GENENAMESTAG. */
	public static final String GENENAMESTAG="Gene names";

	/** The Constant CONDITIONNAMESTAG. */
	public static final String CONDITIONNAMESTAG="Condition names";

	/** The Constant GENEINDEXESTAG. */
	public static final String GENEINDEXESTAG="Gene indexes";

	/** The Constant CONDITIONINDEXESTAG. */
	public static final String CONDITIONINDEXESTAG="Condition indexes";

	/** The Constant BICLUSTERADDITIONALINFO. */
	public static final String BICLUSTERADDITIONALINFO="Bicluster additional information";

	/**
	 * Gets the JSON information.
	 *
	 * @return the JSON information
	 */
	public JSONObject getJSONInformation(){
		JSONObject obj = new JSONObject();

		obj.put(GENESTAG, getNumberGenes());
		obj.put(CONDITIONSTAG, getNumberConditions());
		obj.put(GENENAMESTAG, MTUJsonUtils.assembleJSONArrayFromList(genenames));
		obj.put(GENEINDEXESTAG, MTUJsonUtils.assembleJSONArrayFromList(genesindex));
		obj.put(CONDITIONNAMESTAG, MTUJsonUtils.assembleJSONArrayFromList(conditionnames));
		obj.put(CONDITIONINDEXESTAG, MTUJsonUtils.assembleJSONArrayFromList(conditionsindex));


		if(additionalinfo!=null){
			JSONObject otherinfo=new JSONObject();
			for (Map.Entry<String, Object> info : additionalinfo.entrySet()) {
				otherinfo.put(info.getKey(), info.getValue());

			}
			obj.put(BICLUSTERADDITIONALINFO, otherinfo);
		}


		return obj;
	}

	/**
	 * Load bicluster result from JSON object.
	 *
	 * @param bicobj the bicobj
	 * @return the bicluster result
	 */
	public static BiclusterResult loadBiclusterResultFromJSONObject(JSONObject bicobj){

		ArrayList<String> genenames=MTUJsonUtils.getStringElemsFromJSONArray(bicobj.getJSONArray(GENENAMESTAG));
		ArrayList<String> condnames=MTUJsonUtils.getStringElemsFromJSONArray(bicobj.getJSONArray(CONDITIONNAMESTAG));

		ArrayList<Integer> geneindexes=null;
		ArrayList<Integer> condindexes=null;

		if(bicobj.has(GENEINDEXESTAG))
			geneindexes=MTUJsonUtils.getIntegerElemsFromJSONArray(bicobj.getJSONArray(GENEINDEXESTAG));
		if(bicobj.has(CONDITIONINDEXESTAG))
			condindexes=MTUJsonUtils.getIntegerElemsFromJSONArray(bicobj.getJSONArray(CONDITIONINDEXESTAG));

		BiclusterResult result=null;

		if(geneindexes!=null && condindexes!=null)
			result=new BiclusterResult(genenames, condnames, geneindexes, condindexes);
		else
			result=new BiclusterResult(genenames, condnames);

		if(bicobj.has(BICLUSTERADDITIONALINFO)){
			JSONObject otherinfo=bicobj.getJSONObject(BICLUSTERADDITIONALINFO);

			for (String key : otherinfo.keySet()) {
				result.appendAdditionalInfo(key, otherinfo.get(key));
			}

		}

		return result;
	}


	public BiclusterResult getCloneWithoutExpressionDataAssociation() throws Exception {
		
		return new BiclusterResult(indexinresults, 
				(ArrayList<Integer>) MTUCollectionsUtils.deepCloneObject(genesindex), 
				(ArrayList<String>) MTUCollectionsUtils.deepCloneObject(genenames), 
				(ArrayList<Integer>) MTUCollectionsUtils.deepCloneObject(conditionsindex), 
				(ArrayList<String>) MTUCollectionsUtils.deepCloneObject(conditionnames), 
				(Object[][]) MTUCollectionsUtils.deepCloneObject(additionalresultmatrix), 
				(HashMap<String, Object>) MTUCollectionsUtils.deepCloneObject(additionalinfo));
	}


	/**
	 * Gets the expression dataset.
	 *
	 * @return the expression dataset
	 */
	public ExpressionData getExpressionDataset(){
		return originaldataset;
	}

	/**
	 * Use same expression matrix.
	 *
	 * @param data the data
	 * @return true, if successful
	 */
	public boolean useSameExpressionMatrix(ExpressionData data){
		if(data==originaldataset)
			return true;
		return false;
	}

	/**
	 * Creates the bicluster result container.
	 *
	 * @param genes the genes
	 * @param conditions the conditions
	 * @return the bicluster result
	 */
	public static BiclusterResult createBiclusterResultContainer(ArrayList<String> genes, ArrayList<String> conditions){
		return new BiclusterResult(genes, conditions);
	}


	/**
	 * Gets the string indexes of gene names.
	 *
	 * @param bicluster the bicluster
	 * @param genenames the genenames
	 * @return the string indexes of gene names
	 */
	public static ArrayList<String> getStringIndexesOfGeneNames(BiclusterResult bicluster, ArrayList<String> genenames){

		ArrayList<String> res= new ArrayList<>();

		ArrayList<String> bicgenenames=bicluster.getGeneNames();
		ArrayList<Integer> bicgeneindexes=bicluster.getGeneIndexes();

		for (int i = 0; i < genenames.size(); i++) {
			if(bicgenenames.contains(genenames.get(i))){
				res.add("G"+String.valueOf(bicgeneindexes.get(bicgenenames.indexOf(genenames.get(i)))));
			}
		}

		return res;
	}


	/**
	 * Gets the string indexes of condition names.
	 *
	 * @param bicluster the bicluster
	 * @param condnames the condnames
	 * @return the string indexes of condition names
	 */
	public static ArrayList<String> getStringIndexesOfConditionNames(BiclusterResult bicluster, ArrayList<String> condnames){

		ArrayList<String> res= new ArrayList<>();

		ArrayList<String> biccondnames=bicluster.getConditionNames();
		ArrayList<Integer> biccondindexes=bicluster.getConditionIndexes();

		for (int i = 0; i < condnames.size(); i++) {
			if(biccondnames.contains(condnames.get(i))){
				res.add("C"+String.valueOf(biccondindexes.get(biccondnames.indexOf(condnames.get(i)))));
			}
		}

		return res;

	}
	
	
	public IDataFrameDataLoader getResultsToDataFrame(boolean rowisconditions) {
		
		DefaultDataframeContainer dataframe=new DefaultDataframeContainer();
		double[][] matrixvalues=null;
		if(!rowisconditions) {
			
			Triplet<ArrayList<String>, ArrayList<String>, double[][]> orderedelems=getOrderedBiclusterbyExpressionDatabyColunmDimension();
			
			ArrayList<String> conditionnames=orderedelems.getValue1();
			
			StringColumnRowNames genes=new StringColumnRowNames(orderedelems.getValue0());
			dataframe.setRowNames(genes);
			
			matrixvalues=orderedelems.getValue2();
			
			
			for (int i = 0; i < conditionnames.size(); i++) {
				String condid=conditionnames.get(i);
				ArrayList<Double> colvalues=new ArrayList<>(orderedelems.getValue0().size());
				double[] row=matrixvalues[i];
				for (int j = 0; j < row.length; j++) {
					colvalues.add(row[j]);
				}
				DoubleDataColumn condcol=new DoubleDataColumn(condid, colvalues);
				dataframe.appendDataColumnToDataframe(condcol);
			}
	
		}
		else {
			
			Triplet<ArrayList<String>, ArrayList<String>, double[][]> orderedelems=getOrderedBiclusterbyExpressionDatabyRowDimension();
			
			ArrayList<String> genenames=orderedelems.getValue0();
			
			StringColumnRowNames conditions=new StringColumnRowNames(orderedelems.getValue1());
			dataframe.setRowNames(conditions);
			
			matrixvalues=orderedelems.getValue2();
			
			for (int i = 0; i < genenames.size(); i++) {
				String geneid=genenames.get(i);
				ArrayList<Double> colvalues=new ArrayList<>(orderedelems.getValue1().size());
				double[] row=matrixvalues[i];
				for (int j = 0; j < row.length; j++) {
					colvalues.add(row[j]);
				}
				DoubleDataColumn condcol=new DoubleDataColumn(geneid, colvalues);
				dataframe.appendDataColumnToDataframe(condcol);
			}	
		}
		
		return dataframe;
	}

}
