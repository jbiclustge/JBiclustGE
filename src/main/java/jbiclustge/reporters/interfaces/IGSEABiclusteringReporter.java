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

import java.util.ArrayList;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Interface IGSEABiclusteringReporter.
 */
public interface IGSEABiclusteringReporter extends IBiclusteringReporter{
	
	
	/**
	 * Sets the gene enrichment analyser results.
	 *
	 * @param analysedlist the analysedlist
	 * @param enrichmentresults the enrichmentresults
	 */
	void setGeneEnrichmentAnalyserResults(BiclusterList analysedlist, EnrichmentAnalysisResultList enrichmentresults);
	
	/**
	 * Sets the enrichment analysis pvalue tresholds.
	 *
	 * @param values the new enrichment analysis pvalue tresholds
	 */
	void setEnrichmentAnalysisPvalueTresholds(ArrayList<Double> values);
	
	/**
	 * Use adjustedpvalues.
	 *
	 * @param useadjusted the useadjusted
	 */
	void useAdjustedpvalues(boolean useadjusted);

}
