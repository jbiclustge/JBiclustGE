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
package jbiclustge.execution.threadconfig;

import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.results.biclusters.BiclusterOutputFormat;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterMethodResultsContainer;
import pt.ornrocha.fileutils.MTUDirUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringThread.
 */
public class BiclusteringThread extends AbstractBiclusteringThread<BiclusterMethodResultsContainer> {

	/** The outputdir. */
	protected String outputdir;
	
	/** The onlysavetofiles. */
	private boolean onlysavetofiles=false;
	
	/** The outformat. */
	private BiclusterOutputFormat outformat=BiclusterOutputFormat.JBiclustGE_CSV;
	
	
	
	/**
	 * Instantiates a new biclustering thread.
	 *
	 * @param method the method
	 */
	public BiclusteringThread(AbstractBiclusteringAlgorithmCaller method) {
		super(method);
	}

	
	/**
	 * Instantiates a new biclustering thread.
	 *
	 * @param method the method
	 * @param saveindir the saveindir
	 */
	public BiclusteringThread(AbstractBiclusteringAlgorithmCaller method, String saveindir) {
		super(method);
		this.outputdir=saveindir;
	}

	/* (non-Javadoc)
	 * @see execution.threadconfig.ITaskThread#getResults()
	 */
	@Override
	public BiclusterMethodResultsContainer getResults() {
		return results;
	}
	
	/**
	 * Write biclusters to files and discard results.
	 */
	public void writeBiclustersToFilesAndDiscardResults(){
		this.onlysavetofiles=true;
	}

	/* (non-Javadoc)
	 * @see execution.threadconfig.AbstractBiclusteringThread#execute()
	 */
	@Override
	protected BiclusterMethodResultsContainer execute() throws Exception {
		
		method.run();
		BiclusterList listres=method.getBiclusterResultList();
		if(outputdir!=null){
			this.outputdir=MTUDirUtils.makeDirectoryWithUniqueIDAndDate(outputdir, method.getAlgorithmName());
			listres.exportBiclusterList(outputdir, outformat.getName(), outformat);
			if(onlysavetofiles)
				return null;
		}

		return new BiclusterMethodResultsContainer(listres);
	}
	
	
	
	
	
	

}
