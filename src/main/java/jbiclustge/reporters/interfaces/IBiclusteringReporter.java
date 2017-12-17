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
package jbiclustge.reporters.interfaces;

// TODO: Auto-generated Javadoc
/**
 * The Interface IBiclusteringReporter.
 */
public interface IBiclusteringReporter {
	
	
	//void setBiclusteringResultsToWriteReport(ArrayList<BiclusterList> results);
	
	/**
	 * Sets the output directory.
	 *
	 * @param dirpath the dirpath
	 * @param createsubfolder the createsubfolder
	 */
	void setOutputDirectory(String dirpath, boolean createsubfolder);
	
	/**
	 * Gets the output directory path.
	 *
	 * @return the output directory path
	 */
	public String getOutputDirectoryPath();
	
	/**
	 * Writetodirectory.
	 *
	 * @param dirpath the dirpath
	 * @throws Exception the exception
	 */
	void writetodirectory(String dirpath) throws Exception;
	
	/**
	 * Write.
	 *
	 * @throws Exception the exception
	 */
	void write() throws Exception;
	
	/**
	 * Write biclustering coverage parameters.
	 *
	 * @param bol the bol
	 */
	void writeBiclusteringCoverageParameters(boolean bol);
	

}
