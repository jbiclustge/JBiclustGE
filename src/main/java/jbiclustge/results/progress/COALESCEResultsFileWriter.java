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
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import pt.ornrocha.logutils.MTULogUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker;

// TODO: Auto-generated Javadoc
/**
 * The Class COALESCEResultsFileWriter.
 */
public class COALESCEResultsFileWriter extends AbstractProcessProgressionChecker{

	
	/** The outfilepath. */
	private String outfilepath;
	
	/** The processedresults. */
	private boolean processedresults=false;
	

	/**
	 * Instantiates a new COALESCE results file writer.
	 *
	 * @param instream the instream
	 * @param saveindir the saveindir
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public COALESCEResultsFileWriter(InputStream instream, String saveindir) throws IOException {
		super(instream);
		this.outfilepath=FilenameUtils.concat(saveindir, UUID.randomUUID().toString()+"_temp_coalesce_results.txt");
	}

	/* (non-Javadoc)
	 * @see pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker#isResultsprocessed()
	 */
	@Override
	public boolean isResultsprocessed() {
		return processedresults;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
		
		MTULogUtils.addDebugMsgToClass(this.getClass(), "Saving results in: {}",outfilepath);
		if(stream!=null){
			
			LogMessageCenter.getLogger().addInfoMessage("Finding the best biclusters... this may take a while");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfilepath)));
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(stream));
			String currentline = null;
	        while((currentline = inputFile.readLine()) != null) {
	        	
	        	if(currentline.matches("Cluster\\s+\\d+"))
		           LogMessageCenter.getLogger().addInfoMessage(currentline+" processed");
		        	   
	        	bw.write(currentline);
	        	bw.newLine();
	            MTULogUtils.addDebugMsgToClass(this.getClass(), currentline);
	        }
	        
	        bw.close();
	        processedresults=true;
		}
		
		return false;
	}

	
	
	/* (non-Javadoc)
	 * @see pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker#getResultsObject()
	 */
	@Override
	public Object getResultsObject() {
		return outfilepath;
	}

}
