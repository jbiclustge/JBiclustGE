/************************************************************************** 
 * Copyright 2012 - 2017, Orlando Rocha (ornrocha@gmail.com)
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
 */
package jbiclustge.propertiesmodules.components;

import java.util.ArrayList;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class GSEAInfoContainer.
 */
public class GSEAInfoContainer {
	
	
	/** The enrichmentanalyser. */
	private EnrichmentAnalyserProcessor enrichmentanalyser;
	
	/** The pvalues. */
	private ArrayList<Double> pvalues;
	
	/** The useadjustedpvalues. */
	private boolean useadjustedpvalues=false;
	
	
	
	/**
	 * Instantiates a new GSEA info container.
	 *
	 * @param enrichmentanalyser the enrichmentanalyser
	 * @param pvalues the pvalues
	 * @param useadjustedpvalues the useadjustedpvalues
	 */
	public GSEAInfoContainer(EnrichmentAnalyserProcessor enrichmentanalyser, ArrayList<Double> pvalues,
			boolean useadjustedpvalues) {
		this.enrichmentanalyser = enrichmentanalyser;
		this.pvalues = pvalues;
		this.useadjustedpvalues = useadjustedpvalues;
	}



	/**
	 * Gets the enrichmentanalyser.
	 *
	 * @return the enrichmentanalyser
	 */
	public EnrichmentAnalyserProcessor getEnrichmentanalyser() {
		return enrichmentanalyser;
	}



	/**
	 * Gets the pvalues.
	 *
	 * @return the pvalues
	 */
	public ArrayList<Double> getPvalues() {
		return pvalues;
	}



	/**
	 * Checks if is useadjustedpvalues.
	 *
	 * @return true, if is useadjustedpvalues
	 */
	public boolean isUseadjustedpvalues() {
		return useadjustedpvalues;
	}
	
	
	
	

}
