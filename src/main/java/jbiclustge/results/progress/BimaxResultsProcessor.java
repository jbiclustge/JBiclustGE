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
 * The Class BimaxResultsProcessor.
 */
public class BimaxResultsProcessor extends AbstractProcessProgressionChecker{

	
	/** The listbiclusters. */
	private BiclusterList listbiclusters=null;
	
	/** The originaldataset. */
	private ExpressionData originaldataset;
	
	/** The resultsprocessed. */
	private boolean resultsprocessed=false;
	
	
	/**
	 * Instantiates a new bimax results processor.
	 *
	 * @param instream the instream
	 * @param originaldata the originaldata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BimaxResultsProcessor(InputStream instream, ExpressionData originaldata) throws IOException{
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
			
			ArrayList<Integer> currentgenelist=null;
			ArrayList<Integer> currentconditionlist=null;
			listbiclusters=new BiclusterList();
			
			
			BiclusterResult res=null;
			
			int bicline=0;
			while((currentline = inputFile.readLine()) != null) {
				//LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Bimax output: "+currentline);
				if(currentline.isEmpty()){
					if(currentgenelist!=null && currentconditionlist!=null){
						res=new BiclusterResult(originaldataset, currentgenelist, currentconditionlist, true);
						listbiclusters.add(res);
						currentgenelist=null;
						currentconditionlist=null;
					}
					bicline=0;
				}
				else if(bicline==2){
					currentgenelist=parseLine(currentline);
				}
				else if(bicline==3){
					currentconditionlist=parseLine(currentline);
				}	
				bicline++;	
			}
			res=new BiclusterResult(originaldataset, currentgenelist, currentconditionlist, true);
			listbiclusters.add(res);
			this.resultsprocessed=true;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Parses the line.
	 *
	 * @param line the line
	 * @return the array list
	 */
	private ArrayList<Integer> parseLine(String line){
		ArrayList<Integer> res=new ArrayList<>();
		
		String[] elems=line.split("\\s+");
		for (int i = 0; i < elems.length; i++) {
			try {
				int value=Integer.parseInt(elems[i]);
				res.add(value-1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return res;
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
