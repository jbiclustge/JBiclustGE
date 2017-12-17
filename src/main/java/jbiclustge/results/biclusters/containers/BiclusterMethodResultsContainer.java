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
package jbiclustge.results.biclusters.containers;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterMethodResultsContainer.
 */
public class BiclusterMethodResultsContainer {
	
	/** The resultslist. */
	private BiclusterList resultslist;
	
	/** The enrichmentresults. */
	private EnrichmentAnalysisResultList enrichmentresults;
	
	/**
	 * Instantiates a new bicluster method results container.
	 *
	 * @param resultslist the resultslist
	 */
	public BiclusterMethodResultsContainer(BiclusterList resultslist){
		this.resultslist=resultslist;
	}
	
	
	/**
	 * Instantiates a new bicluster method results container.
	 *
	 * @param resultslist the resultslist
	 * @param enrichmentresults the enrichmentresults
	 */
	public BiclusterMethodResultsContainer(BiclusterList resultslist, EnrichmentAnalysisResultList enrichmentresults){
		this.resultslist=resultslist;
		this.enrichmentresults=enrichmentresults;
	}


	/**
	 * Gets the resultslist.
	 *
	 * @return the resultslist
	 */
	public BiclusterList getResultslist() {
		return resultslist;
	}


	/**
	 * Sets the resultslist.
	 *
	 * @param resultslist the new resultslist
	 */
	public void setResultslist(BiclusterList resultslist) {
		this.resultslist = resultslist;
	}


	/**
	 * Gets the enrichmentresults.
	 *
	 * @return the enrichmentresults
	 */
	public EnrichmentAnalysisResultList getEnrichmentresults() {
		return enrichmentresults;
	}


	/**
	 * Sets the enrichmentresults.
	 *
	 * @param enrichmentresults the new enrichmentresults
	 */
	public void setEnrichmentresults(EnrichmentAnalysisResultList enrichmentresults) {
		this.enrichmentresults = enrichmentresults;
	}
	
	

}
