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
package jbiclustge.analysis.overlap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.mathutils.MTUMathUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class OverlapAnalyser.
 */
public class OverlapAnalyser {
	
	
	
	 /**
 	 * Gets the overlap beetwen two biclusters.
 	 *
 	 * @param bic1 the bic 1
 	 * @param bic2 the bic 2
 	 * @return the overlap beetwen two biclusters
 	 */
 	public static double getOverlapBeetwenTwoBiclusters(BiclusterResult bic1, BiclusterResult bic2){
		  int rowsintersect=0;
		  if(bic1.getGeneNames()!=null && bic2.getGeneNames()!=null)
			  rowsintersect=MTUMathUtils.getIntersection(bic1.getGeneNames(),bic2.getGeneNames()).size();
		  else
			  rowsintersect=MTUMathUtils.getIntersection(bic1.getArrayGeneIndexes(),bic2.getArrayGeneIndexes()).size();
		  int columnsintersect=0;
		  if(bic1.getConditionNames()!=null && bic2.getConditionNames()!=null)
			  columnsintersect=MTUMathUtils.getIntersection(bic1.getConditionNames(), bic2.getConditionNames()).size();
		  else
			  columnsintersect=MTUMathUtils.getIntersection(bic1.getArrayConditionIndexes(), bic2.getArrayConditionIndexes()).size();
		  return ((double)rowsintersect/bic1.getNumberGenes())*((double)columnsintersect/bic1.getNumberConditions());
	 }
	 

	 
	 /**
 	 * Gets the overlap biclusters rows.
 	 *
 	 * @param bic1 the bic 1
 	 * @param bic2 the bic 2
 	 * @return the overlap biclusters rows
 	 */
 	public static double getOverlapBiclustersRows(BiclusterResult bic1, BiclusterResult bic2){
		 int rowsintersect=MTUMathUtils.getIntersection(bic1.getGeneNames(),bic2.getGeneNames()).size();
		 return ((double)rowsintersect/bic1.getGeneNames().size());
	 }
	 
	 /**
 	 * Gets the overlap biclusters columns.
 	 *
 	 * @param bic1 the bic 1
 	 * @param bic2 the bic 2
 	 * @return the overlap biclusters columns
 	 */
 	public static double getOverlapBiclustersColumns(BiclusterResult bic1, BiclusterResult bic2){
		 int columnsintersect=MTUMathUtils.getIntersection(bic1.getConditionNames(), bic2.getConditionNames()).size();
		 //return (double)columnsintersect/bic1.getGeneNames().size();
		 return (double)columnsintersect/bic1.getConditionNames().size();
	 }
	
	
	/**
	 * Filter bicluster list with overlap threshold.
	 *
	 * @param biclist the biclist
	 * @param threshold the threshold
	 * @param maxnumberbiclusters the maxnumberbiclusters
	 * @return the bicluster list
	 * @throws Exception the exception
	 */
	public static BiclusterList filterBiclusterListWithOverlapThreshold(BiclusterList biclist, double threshold, int maxnumberbiclusters) throws Exception{
		
		LogMessageCenter.getLogger().toClass(OverlapAnalyser.class).addInfoMessage("Analyzing Overlap of: "+biclist.getUsedmethod());
		if(threshold>=0.0 && threshold<=1.0){
			BiclusterList sortlist=BiclusterList.sortBiclusterList(biclist, false);
            
			int countbic=0;
		
			BiclusterList res=new BiclusterList();
		
			if(biclist.getAnalysedDataset()!=null)
				res.setAnalysedDataset(biclist.getAnalysedDataset());
			if(biclist.getAditionalBiclusterMethodInformation()!=null)
				res.setAditionalBiclusterMethodInformation(biclist.getAditionalBiclusterMethodInformation());
			if(biclist.getMethodRunningParameters()!=null)
				res.setMethodRunningParameters(biclist.getMethodRunningParameters());
			res.setUsedmethod(biclist.getUsedmethod());
			res.setMethodRunningTime(biclist.getMethodRunningTime());
		
			boolean check=true;
			for (int i = 0; i < sortlist.size()&&check; i++) {
                
				if(i==0)
					res.add(sortlist.get(i));
				else{
					BiclusterResult bicres=sortlist.get(i);
					boolean add=true;
					for (int j = 0; j < res.size(); j++) {
						BiclusterResult comp=res.get(j);
					    
						if(bicres.isSubsetOfBicluster(comp)){
							add=false;
						}
						if(add){
							double overlap=getOverlapBeetwenTwoBiclusters(comp, bicres);
							LogMessageCenter.getLogger().toClass(OverlapAnalyser.class).addDebugMessage(biclist.getUsedmethod()+" comparing bic: "+i+" vs bic: "+j+" have an Overlap: "+overlap+" ,treshold is: "+threshold);
							if(overlap>threshold){
								add=false;
							}
						}
					}
					if(add){
						res.add(bicres);
						countbic++;
						if(countbic >=(maxnumberbiclusters-1) && maxnumberbiclusters!=-1)
							check=false;
					}
				
				}	
			}
			return res;
			}
		else 
			throw new Exception("Threshold value must be in [0,1] interval.");
	}
	
	

	/**
	 * Filter bicluster list with rows and columns overlap threshold.
	 *
	 * @param biclist the biclist
	 * @param rowsthreshold the rowsthreshold
	 * @param columnsthreshold the columnsthreshold
	 * @param maxnumberbiclusters the maxnumberbiclusters
	 * @return the bicluster list
	 * @throws Exception the exception
	 */
/*	public static BiclusterList filterBiclusterListWithRowsAndColumnsOverlapThreshold(BiclusterList biclist, double rowsthreshold,double columnsthreshold,int maxnumberbiclusters) throws Exception{
		
		
		if(rowsthreshold>=0.0 && rowsthreshold<=1.0 && columnsthreshold>=0.0 && columnsthreshold<=1.0){
			BiclusterList sortlist=BiclusterList.sortBiclusterList(biclist, false);

			int countbic=0;
		
			BiclusterList res=new BiclusterList();
		
			if(biclist.getAnalysedDataset()!=null)
				res.setAnalysedDataset(biclist.getAnalysedDataset());
			if(biclist.getAditionalBiclusterMethodInformation()!=null)
				res.setAditionalBiclusterMethodInformation(biclist.getAditionalBiclusterMethodInformation());
			res.setUsedmethod(biclist.getUsedmethod());
			res.setMethodRunningTime(biclist.getMethodRunningTime());
		
			boolean check=true;
			for (int i = 0; i < sortlist.size()&&check; i++) {

				if(i==0)
					res.add(sortlist.get(i));
				else{
					BiclusterResult bicres=sortlist.get(i);
					boolean add=true;
					for (int j = 0; j < res.size(); j++) {
						BiclusterResult comp=res.get(j);
					    
						double rowoverlap=getOverlapBiclustersRows(comp, bicres);
						double coloverlap=getOverlapBiclustersColumns(comp, bicres);

						if(rowoverlap>=rowsthreshold || coloverlap>=columnsthreshold){
							add=false;
						}
					
					}
					if(add){
						res.add(bicres);
						countbic++;
						if(countbic >=(maxnumberbiclusters-1) && maxnumberbiclusters!=-1)
							check=false;
					}
				
				}	
			}
			return res;
			}
		else{
			if(rowsthreshold<0.0 || rowsthreshold>1.0)
				throw new Exception("Rows threshold value must be a value in [0,1] interval.");
			else 
				throw new Exception("Columns threshold value must be a value in [0,1] interval.");
		}
			
	}*/
	
	
	/**
	 * Average overlap.
	 *
	 * @param biclist the biclist
	 * @return the double
	 */
	public static double AverageOverlap(BiclusterList biclist){
		BiclusterList sortlist=BiclusterList.sortBiclusterList(biclist, false);
		
		Queue<BiclusterResult> queue=new LinkedList<>();
		queue.addAll(sortlist);
		int n=0;
		double sum=0.0;
		while (!queue.isEmpty()) {
			BiclusterResult top= queue.poll();
			Iterator<BiclusterResult> g=queue.iterator();
			while (g.hasNext()) {
				BiclusterResult next= g.next();
				double overlap=getOverlapBeetwenTwoBiclusters(top, next);
				sum+=overlap;
				n++;
			}	
		}
		return sum/n;
	}
	
	
	
	
	
	

}
