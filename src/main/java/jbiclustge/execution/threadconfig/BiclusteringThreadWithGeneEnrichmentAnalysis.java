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

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.zeroturnaround.zip.ZipUtil;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.reporters.BiclusteringGSEAReporterType;
import jbiclustge.reporters.interfaces.IGSEABiclusteringReporter;
import jbiclustge.results.biclusters.containers.BiclusterMethodResultsContainer;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringThreadWithGeneEnrichmentAnalysis.
 */
public class BiclusteringThreadWithGeneEnrichmentAnalysis extends BiclusteringThread {

	/** The processor. */
	private EnrichmentAnalyserProcessor processor;
	
	/** The pvaluestoreport. */
	private ArrayList<Double> pvaluestoreport;
	
	/** The useadjustedpvalues. */
	private boolean useadjustedpvalues=false;
	
	/** The outputreporter. */
	private BiclusteringGSEAReporterType outputreporter;
	
	/** The compressfiles. */
	private boolean compressfiles=false;
    
    /** The createonlyreportfiles. */
    private boolean createonlyreportfiles=false;
	
	

	
	/**
	 * Instantiates a new biclustering thread with gene enrichment analysis.
	 *
	 * @param method the method
	 * @param processor the processor
	 */
	public BiclusteringThreadWithGeneEnrichmentAnalysis(AbstractBiclusteringAlgorithmCaller method, EnrichmentAnalyserProcessor processor) {
		super(method);
	    this.processor=processor.copyWorkingInstance();
	}
	
	/**
	 * Instantiates a new biclustering thread with gene enrichment analysis.
	 *
	 * @param method the method
	 * @param processor the processor
	 * @param typereporter the typereporter
	 * @param writeresulstofolder the writeresulstofolder
	 */
	public BiclusteringThreadWithGeneEnrichmentAnalysis(AbstractBiclusteringAlgorithmCaller method, EnrichmentAnalyserProcessor processor, BiclusteringGSEAReporterType typereporter,String writeresulstofolder) {
		super(method, writeresulstofolder);
		if(processor!=null)
			this.processor=processor.copyWorkingInstance();
        this.outputreporter=typereporter;
	}

	
	
	/* (non-Javadoc)
	 * @see execution.threadconfig.BiclusteringThread#getResults()
	 */
	@Override
	public BiclusterMethodResultsContainer getResults() {
		return results;
	}

	
	/**
	 * Sets the compressfiles.
	 *
	 * @param compressfiles the new compressfiles
	 */
	public void setCompressfiles(boolean compressfiles) {
		this.compressfiles = compressfiles;
	}

	/**
	 * Creates the report files and discard results.
	 */
	public void createReportFilesAndDiscardResults(){
		this.createonlyreportfiles=true;
	}
	
	/**
	 * Sets the enrichment analysis pvalue tresholds.
	 *
	 * @param values the new enrichment analysis pvalue tresholds
	 */
	public void setEnrichmentAnalysisPvalueTresholds(double...values){
		
		for (double d : values) {
			addEnrichmentAnalysisPvalueTreshold(d);
		}
	}
	
	/**
	 * Sets the enrichment analysis pvalue tresholds.
	 *
	 * @param values the new enrichment analysis pvalue tresholds
	 */
	public void setEnrichmentAnalysisPvalueTresholds(ArrayList<Double> values){
		
		for (double d : values) {
			addEnrichmentAnalysisPvalueTreshold(d);
		}
	}
	
	/**
	 * Adds the enrichment analysis pvalue treshold.
	 *
	 * @param value the value
	 */
	public void addEnrichmentAnalysisPvalueTreshold(double value){
		if(pvaluestoreport==null)
			pvaluestoreport=new ArrayList<>();
		pvaluestoreport.add(value);
	}
	
	/**
	 * Use adjustedpvalues.
	 *
	 * @param adjustedpvalues the adjustedpvalues
	 */
	public void useAdjustedpvalues(boolean adjustedpvalues){
		this.useadjustedpvalues=adjustedpvalues;
	}

	/* (non-Javadoc)
	 * @see execution.threadconfig.BiclusteringThread#execute()
	 */
	@Override
	protected BiclusterMethodResultsContainer execute() throws Exception {
	    
		BiclusterMethodResultsContainer bicres=super.execute();
		EnrichmentAnalysisResultList listenranl=null;
		
		if(processor!=null){
			
			try {
				processor.setBiclusteringResultsToAnalyse(bicres.getResultslist());
				processor.run();
				listenranl=processor.getEnrichmentAnalysisResults();	
			} catch (Exception e) {
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error in the execution of Functional Enrichment Analysis: ", e);
				return bicres;
			}
			
			if(outputreporter!=null && outputdir!=null){
				
				 
				try {
					IGSEABiclusteringReporter reporter=outputreporter.getReporterInstance();
					reporter.setGeneEnrichmentAnalyserResults(bicres.getResultslist(), listenranl);
					if(pvaluestoreport!=null){
						reporter.setEnrichmentAnalysisPvalueTresholds(pvaluestoreport);
						reporter.useAdjustedpvalues(useadjustedpvalues);
					}
					
					reporter.writetodirectory(outputdir);	
				} catch (Exception e) {
					LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error at creating the csv output files report: ", e);
				}
				
				String zipname=null;
				if(compressfiles){
					String dirtocompress=outputdir;
					String basename=FilenameUtils.getBaseName(dirtocompress);
					zipname=FilenameUtils.concat(outputdir, basename+".zip");
					ZipUtil.pack(new File(dirtocompress), new File(zipname));
					try {
						MTUDirUtils.deleteDirectory(dirtocompress);
					} catch (Exception e) {
						LogMessageCenter.getLogger().addCriticalErrorMessage("Error in deleting compressed directory: ",e);
					}
				}
				
			}
			
			// for performance purpose, reducing memory consumption if the aim is to create only the files with analysis information
			if(createonlyreportfiles)
				return null;
			
			bicres.setEnrichmentresults(listenranl);
		}
		
		return bicres;
	}
	
	

}
