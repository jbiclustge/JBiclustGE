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
package jbiclustge.reporters;

import jbiclustge.reporters.interfaces.IBiclusteringReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringTaskReporter.
 */
public abstract class BiclusteringTaskReporter implements IBiclusteringReporter{
	
	
	/** The bicresults. */
	protected BiclusterList bicresults;
	
	/** The outputdir. */
	protected String outputdir;
	
	/** The createsubfolder. */
	protected boolean createsubfolder=true;
	
	
	/**
	 * Instantiates a new biclustering task reporter.
	 *
	 * @param bicresults the bicresults
	 */
	public BiclusteringTaskReporter(BiclusterList bicresults) {
        this.bicresults=bicresults;
	}
	
	/**
	 * Instantiates a new biclustering task reporter.
	 *
	 * @param bicresults the bicresults
	 * @param outputdir the outputdir
	 */
	public BiclusteringTaskReporter(BiclusterList bicresults, String outputdir) {
        this.bicresults=bicresults;
        this.outputdir=outputdir;
	}
	

	/**
	 * Sets the output directory.
	 *
	 * @param dirpath the new output directory
	 */
	public void setOutputDirectory(String dirpath) {
		this.outputdir=dirpath;
	}
	
	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#getOutputDirectoryPath()
	 */
	public abstract String getOutputDirectoryPath();
	
	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#writetodirectory(java.lang.String)
	 */
	public void writetodirectory(String filepath) throws Exception {
		setOutputDirectory(filepath);
		createsubfolder=false;
		write();
	}

}
