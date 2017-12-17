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
package jbiclustge.rtools.plotutils;

import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.rtools.plotutils.components.HeatMapUtils;
import jbiclustge.rtools.plotutils.components.ParallelCoordinatesUtils;
import jrplot.plotpackages.ggplot.parallelcoord.BiclusterParallelCoordinates;
import jrplot.plotpackages.heatmap.Heatmap2Caller;
import jrplot.rbinders.components.interfaces.IDataFrameDataLoader;
import jrplot.rbinders.components.matrix.DefaultMatrixContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterPlotUtils.
 */
public class BiclusterPlotUtils {
	
	
	/////////////////////////////  Parallel Coordinates ///////////////////////////////////////
	
	 /**
	 * Make and save bicluster plot parallel coordinates.
	 *
	 * @param bicluster the bicluster
	 * @param showgenesincolumns the showgenesincolumns
	 * @param orderelements the orderelements
	 * @param indexesasnames the indexesasnames
	 * @param filepath the filepath
	 */
	public static void makeAndSaveBiclusterPlotParallelCoordinates(BiclusterResult bicluster,boolean showgenesincolumns,boolean orderelements, boolean indexesasnames, String filepath){
		 IDataFrameDataLoader dataframe=ParallelCoordinatesUtils.makeBiclusterDataFrame(bicluster, showgenesincolumns, orderelements, indexesasnames);
		 BiclusterParallelCoordinates maker=new BiclusterParallelCoordinates(dataframe, "id");
		 maker.addWidth(1600).addHeight(1200);
		 maker.saveFigureFilepath(filepath);
		 maker.run();
			
	  }
	
	
	 /**
 	 * Make and save bicluster plot parallel coordinates.
 	 *
 	 * @param bicluster the bicluster
 	 * @param showgenesincolumns the showgenesincolumns
 	 * @param filepath the filepath
 	 */
 	public static void makeAndSaveBiclusterPlotParallelCoordinates(BiclusterResult bicluster,boolean showgenesincolumns, String filepath){
		    makeAndSaveBiclusterPlotParallelCoordinates(bicluster, showgenesincolumns, true, false, filepath);
	
	 }
	  
	 
	 /**
 	 * Make and save bicluster plot parallel coordinates.
 	 *
 	 * @param listresults the listresults
 	 * @param indexbicluster the indexbicluster
 	 * @param showgenesincolumns the showgenesincolumns
 	 * @param orderelements the orderelements
 	 * @param indexesasnames the indexesasnames
 	 * @param filepath the filepath
 	 */
 	public static void makeAndSaveBiclusterPlotParallelCoordinates(BiclusterList listresults, int indexbicluster, boolean showgenesincolumns, boolean orderelements, boolean indexesasnames, String filepath){   
		  makeAndSaveBiclusterPlotParallelCoordinates(listresults.get(indexbicluster), showgenesincolumns, orderelements, indexesasnames, filepath);
	  }
	
	
	 
	 /**
 	 * Make and save list biclusters plot parallel coordinates.
 	 *
 	 * @param listresults the listresults
 	 * @param showgenesincolumns the showgenesincolumns
 	 * @param orderelements the orderelements
 	 * @param indexesasnames the indexesasnames
 	 * @param dirpath the dirpath
 	 */
 	public static void makeAndSaveListBiclustersPlotParallelCoordinates(BiclusterList listresults, boolean showgenesincolumns, boolean orderelements, boolean indexesasnames, String dirpath){   
		 
		for (int i = 0; i < listresults.size(); i++) {
			BiclusterResult res=listresults.get(i);
			String filepath=FilenameUtils.concat(dirpath, "Bicluster_"+(i+1));
			makeAndSaveBiclusterPlotParallelCoordinates(res, showgenesincolumns, orderelements, indexesasnames, filepath);
		}
		 
		 
	 }
	  
	
	
	 /////////////////////////////  Heat maps ///////////////////////////////////////
	
	
	 /**
 	 * Make and save list biclusters plot heatmap.
 	 *
 	 * @param listresults the listresults
 	 * @param orderelements the orderelements
 	 * @param indexesasnames the indexesasnames
 	 * @param saveindir the saveindir
 	 */
 	public static void makeAndSaveListBiclustersPlotHeatmap(BiclusterList listresults, boolean orderelements, boolean indexesasnames, String saveindir){
		 
		 ArrayList<DefaultMatrixContainer> matrices=HeatMapUtils.makeBiclustersMatrices(listresults, orderelements, indexesasnames);
		 
		 for (int i = 0; i < matrices.size(); i++) {
			 Heatmap2Caller maker= Heatmap2Caller.load(matrices.get(i));
			 maker.addWidth(1600).addHeight(1200);
			 String filepath=FilenameUtils.concat(saveindir, "Bicluster_"+(i+1));
			 maker.saveFigureFilepath(filepath);
			 maker.run();
		 }
	 }
	 
	 
	 /**
 	 * Initialize bicluster heat map.
 	 *
 	 * @param listresults the listresults
 	 * @param orderelements the orderelements
 	 * @param indexesasnames the indexesasnames
 	 * @return the array list
 	 */
 	public static ArrayList<Heatmap2Caller> initializeBiclusterHeatMap(BiclusterList listresults, boolean orderelements, boolean indexesasnames){
		 ArrayList<DefaultMatrixContainer> matrices=HeatMapUtils.makeBiclustersMatrices(listresults, orderelements, indexesasnames);
		 ArrayList<Heatmap2Caller> heatmaps=new ArrayList<>();
		 for (int i = 0; i < matrices.size(); i++) {
			 Heatmap2Caller maker= Heatmap2Caller.load(matrices.get(i));
			 heatmaps.add(maker);
		 }	 
		 return heatmaps;
	 }
	 
	 

}
