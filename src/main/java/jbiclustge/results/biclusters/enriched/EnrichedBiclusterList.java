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
package jbiclustge.results.biclusters.enriched;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.jsonutils.MTUJsonUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import smile.imputation.MissingValueImputationException;

// TODO: Auto-generated Javadoc
/**
 * The Class EnrichedBiclusterList.
 */
public class EnrichedBiclusterList extends BiclusterList{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	
	
	 /** The totalgenes. */
 	private HashSet<String> totalgenes;
	 
 	/** The totalconditions. */
 	private HashSet<String> totalconditions;
	 
	 

	
	 /**
 	 * Instantiates a new enriched bicluster list.
 	 */
 	public EnrichedBiclusterList(){
		 totalgenes=new HashSet<>();
		 totalconditions=new HashSet<>();
	 }

	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(BiclusterResult e) {
	    totalgenes.addAll(e.getGeneNames());
	    totalconditions.addAll(e.getConditionNames());
		return super.add(e);
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends BiclusterResult> c) {
		
		appendGenesAndConditions(c);
		return super.addAll(c);
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, BiclusterResult e) {
		 totalgenes.addAll(e.getGeneNames());
		 totalconditions.addAll(e.getConditionNames());
		super.add(index, e);
	}
	
	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends BiclusterResult> c) {
		appendGenesAndConditions(c);
		return super.addAll(index, c);
	}
	
	/**
	 * Append genes and conditions.
	 *
	 * @param c the c
	 */
	private void appendGenesAndConditions(Collection<? extends BiclusterResult> c){
		for (BiclusterResult enrichedBiclusterResult : c) {
			totalgenes.addAll(enrichedBiclusterResult.getGeneNames());
			totalconditions.addAll(enrichedBiclusterResult.getConditionNames());
		}
	}



	/*public HashSet<String> getTotalGenes() {
		return totalgenes;
	}
	
	public HashSet<String> getTotalConditions() {
		return totalconditions;
	}*/
	
	
	
	

	/** The Constant GENES. */
	private static final String GENES="EnrichedGenes";
	
	/** The Constant CONDITIONS. */
	private static final String CONDITIONS="FilteredConditions";
	
	
    /* (non-Javadoc)
     * @see results.biclusters.containers.BiclusterList#convertBiclusterListToJSONFormat(boolean)
     */
    @Override
	public JSONObject convertBiclusterListToJSONFormat(boolean saveexpressiondataset){
		
		JSONObject obj = new JSONObject();
		obj.put("Total", size());
		obj.put(BICLUSTERINGMETHOD, getUsedmethod());
		
		
		if(getMethodRunningParameters()!=null){
			obj.put(BICLUSTERINGMETHODPARAMETERS, MTUJsonUtils.assembleJSONObjectFromStringLinkedHashMap(getMethodRunningParameters()));
		}
		
		if(getAditionalBiclusterMethodInformation()!=null){
			obj.put(BICLUSTERINGADDITIONALINFO, MTUJsonUtils.assembleJSONObjectFromKStringVObjectLinkedHashMap(getAditionalBiclusterMethodInformation()));
		}
		
		JSONObject biclustersobj=new JSONObject();
		
		for (int i = 0; i < size(); i++) {
			biclustersobj.put(String.valueOf(i), get(i).getJSONInformation());
		}
		obj.put(BICLUSTERLISTTAG, biclustersobj);
		

		if(saveexpressiondataset && getAnalysedDataset()!=null)
			try {
		
				ExpressionData reduceddata=getAnalysedDataset().getSubsetOfOriginalExpressionData(new ArrayList<>(totalgenes), new ArrayList<>(totalconditions));
				LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Number enriched genes: "+reduceddata.getGeneNamesList().size());
				LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Number conditions associated to enriched genes: "+reduceddata.getConditionsList().size());
				obj.put(BICLUSTERLISTEXPRESSIONDATATAG, reduceddata.getDatasetJSONFormat());
			} catch (JSONException | IOException | MissingValueImputationException e) {
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Gene expression data could not be saved: ", e);
			}
		
		return obj;
	}
	
	
	

}
