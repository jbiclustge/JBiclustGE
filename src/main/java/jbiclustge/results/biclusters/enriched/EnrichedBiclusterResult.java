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

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.jsonutils.MTUJsonUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class EnrichedBiclusterResult.
 */
public class EnrichedBiclusterResult extends BiclusterResult{


	  /**
  	 * Instantiates a new enriched bicluster result.
  	 *
  	 * @param originaldataset the originaldataset
  	 * @param genenameslist the genenameslist
  	 * @param conditionslist the conditionslist
  	 */
  	public EnrichedBiclusterResult(ExpressionData originaldataset, ArrayList<String> genenameslist,ArrayList<String> conditionslist) {
		super(originaldataset, genenameslist, conditionslist);
	}
	 
	 
	  /* (non-Javadoc)
  	 * @see results.biclusters.containers.BiclusterResult#getJSONInformation()
  	 */
  	@Override
	  public JSONObject getJSONInformation(){
			JSONObject obj = new JSONObject();
			
			obj.put(GENESTAG, getNumberGenes());
			obj.put(CONDITIONSTAG, getNumberConditions());
			obj.put(GENENAMESTAG, MTUJsonUtils.assembleJSONArrayFromList(getGeneNames()));
			//obj.put(GENEINDEXESTAG, MTUJsonUtils.assembleJSONArrayFromList(genesindex));
			obj.put(CONDITIONNAMESTAG, MTUJsonUtils.assembleJSONArrayFromList(getConditionNames()));
			//obj.put(CONDITIONINDEXESTAG, MTUJsonUtils.assembleJSONArrayFromList(conditionsindex));
			
			
			if(getMapOfAdditionalInfo()!=null){
				JSONObject otherinfo=new JSONObject();
				for (Map.Entry<String, Object> info : getMapOfAdditionalInfo().entrySet()) {
					otherinfo.put(info.getKey(), info.getValue());
					
				}
				obj.put(BICLUSTERADDITIONALINFO, otherinfo);
			}
			
			
			return obj;
		}
		
	 
	 
	
}
