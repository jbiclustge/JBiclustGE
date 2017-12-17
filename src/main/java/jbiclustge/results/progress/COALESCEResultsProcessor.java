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
package jbiclustge.results.progress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker;

// TODO: Auto-generated Javadoc
/**
 * The Class COALESCEResultsProcessor.
 */
public class COALESCEResultsProcessor extends AbstractProcessProgressionChecker{
	
	

	/** The listbiclusters. */
	private BiclusterList listbiclusters=null;
	
	/** The originaldataset. */
	private ExpressionData originaldataset;
	
	/** The resultsprocessed. */
	private boolean resultsprocessed=false;
	
	
	
	/**
	 * Instantiates a new COALESCE results processor.
	 *
	 * @param instream the instream
	 * @param originaldata the originaldata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public COALESCEResultsProcessor(InputStream instream, ExpressionData originaldata) throws IOException {
		super(instream);	
		this.originaldataset=originaldata;
	}


	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
		
        if(stream!=null){
			
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
			
			ArrayList<String> currentgenelist=null;
			ArrayList<String> currentconditionlist=null;
			ArrayList<String> motiflist=null;
			
			listbiclusters=new BiclusterList();
			BiclusterResult currentbicluster=null;
			
			
			
	        while((currentline = inputFile.readLine()) != null) {
	        	
	           if(currentline.matches("Cluster\\s+\\d+")){
	        	  
	        	   LogMessageCenter.getLogger().addInfoMessage(currentline+" processed");
	        	   
	        	   if(currentbicluster!=null){
	        		   listbiclusters.add(currentbicluster);
	        	   }
	        	   currentgenelist=new ArrayList<>();
	        	   currentconditionlist=new ArrayList<>();
	        	   motiflist=new ArrayList<>();
	           }
	           else if(currentline.matches("Genes\\s+\\w+.*")){
	              setListIDs(currentgenelist,currentline);
	           }
	           else if(currentline.matches("Conditions\\s+\\w+.*")){
	        	  setListIDs(currentconditionlist, currentline);
	        	  if(currentgenelist!=null && currentconditionlist!=null)
	        		  currentbicluster=new BiclusterResult(originaldataset, currentgenelist, currentconditionlist);
	           }
	           else if(currentline.matches("Motifs\\s+\\w+.*")){
	        	   setListIDs(motiflist,currentline);
	        	   System.out.println(currentline);
	           }
	          // MTULogUtils.addDebugMsgToClass(this.getClass(), currentline);
	        }
	        
	        if(currentbicluster!=null)
	        	listbiclusters.add(currentbicluster);
	        
	        LogMessageCenter.getLogger().addInfoMessage("Processing COALESCE results, please wait...");
			
	        this.resultsprocessed=true;
			return true;
		}
		
		
		return false;
	}
	
	
	/**
	 * Sets the list I ds.
	 *
	 * @param container the container
	 * @param line the line
	 */
	private void setListIDs(ArrayList<String> container, String line){
		
		String[] elems=line.split("\\s+");
		for (int i = 1; i < elems.length; i++) {
			container.add(elems[i]);
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker#isResultsprocessed()
	 */
	@Override
	public boolean isResultsprocessed() {
		return resultsprocessed;
	}


	/* (non-Javadoc)
	 * @see pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker#getResultsObject()
	 */
	@Override
	public Object getResultsObject() {
		return listbiclusters;
	}
	
	

}
