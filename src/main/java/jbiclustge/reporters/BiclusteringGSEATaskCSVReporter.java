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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.javatuples.Triplet;

import jbiclustge.analysis.coverage.CoverageAnalyser;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultsContainer;
import jbiclustge.reporters.interfaces.IGSEABiclusteringReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.collections.MTUMapUtils;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringGSEATaskCSVReporter.
 */
public class BiclusteringGSEATaskCSVReporter implements IGSEABiclusteringReporter{

	
	/** The enrichmentresults. */
	protected EnrichmentAnalysisResultList enrichmentresults;
	
	/** The bicresults. */
	protected BiclusterList bicresults;
	
	/** The pvaluestoreport. */
	private ArrayList<Double> pvaluestoreport;
	
	/** The useadjustedpvalues. */
	private boolean useadjustedpvalues=false;
	
	/** The writecoverageparameters. */
	private boolean writecoverageparameters=true;
	
	/** The outputdir. */
	private String outputdir;
	
	/** The outputcsvfilesdir. */
	private String outputcsvfilesdir;
	
	/** The createsubfolder. */
	private boolean createsubfolder=true;
	
	/** The globaldelimiter. */
	private String globaldelimiter="\t";

	
	/**
	 * Instantiates a new biclustering GSEA task CSV reporter.
	 */
	public BiclusteringGSEATaskCSVReporter(){}
	
	
	/**
	 * Instantiates a new biclustering GSEA task CSV reporter.
	 *
	 * @param bicresults the bicresults
	 */
	public BiclusteringGSEATaskCSVReporter(BiclusterList bicresults) {
		this.bicresults=bicresults;
	}
	
	
	/**
	 * Instantiates a new biclustering GSEA task CSV reporter.
	 *
	 * @param bicresults the bicresults
	 * @param enrichmentresults the enrichmentresults
	 */
	public BiclusteringGSEATaskCSVReporter(BiclusterList bicresults, EnrichmentAnalysisResultList enrichmentresults) {
        this(bicresults);
		this.enrichmentresults=enrichmentresults;
		
	}
	
	/**
	 * Instantiates a new biclustering GSEA task CSV reporter.
	 *
	 * @param bicresults the bicresults
	 * @param enrichmentresults the enrichmentresults
	 * @param outputdir the outputdir
	 */
	public BiclusteringGSEATaskCSVReporter(BiclusterList bicresults, EnrichmentAnalysisResultList enrichmentresults, String outputdir) {
        this(bicresults,enrichmentresults);
		this.outputdir=outputdir;
		
	}


	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#write()
	 */
	@Override
	public void write() throws Exception {
		createFilesDirectory();
		writeBiclustersInformation();
	}
	
	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#writetodirectory(java.lang.String)
	 */
	@Override
	public void writetodirectory(String dirpath) throws Exception {
		this.createsubfolder=false;
		this.outputdir=dirpath;
        write();
	}
	
	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#writeBiclusteringCoverageParameters(boolean)
	 */
	@Override
	public void writeBiclusteringCoverageParameters(boolean bol) {
		writecoverageparameters=bol;
		
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IGSEABiclusteringReporter#setGeneEnrichmentAnalyserResults(results.biclusters.containers.BiclusterList, enrichmentanalysistools.common.EnrichmentAnalysisResultList)
	 */
	@Override
	public void setGeneEnrichmentAnalyserResults(BiclusterList analysedlist, EnrichmentAnalysisResultList enrichmentresults) {
		this.bicresults=analysedlist;
		this.enrichmentresults=enrichmentresults;
		
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IGSEABiclusteringReporter#setEnrichmentAnalysisPvalueTresholds(java.util.ArrayList)
	 */
	@Override
	public void setEnrichmentAnalysisPvalueTresholds(ArrayList<Double> values) {
		this.pvaluestoreport=values;
		
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IGSEABiclusteringReporter#useAdjustedpvalues(boolean)
	 */
	@Override
	public void useAdjustedpvalues(boolean useadjusted) {
		this.useadjustedpvalues=useadjusted;
		
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#getOutputDirectoryPath()
	 */
	@Override
	public String getOutputDirectoryPath() {
		return outputcsvfilesdir;
	}


	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#setOutputDirectory(java.lang.String, boolean)
	 */
	@Override
	public void setOutputDirectory(String dirpath, boolean createsubfolder) {
		this.outputdir=dirpath;
		this.createsubfolder=createsubfolder;
	}
	
	
	/**
	 * Creates the files directory.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void createFilesDirectory() throws IOException{
		if(outputdir!=null){
			if(createsubfolder)
				this.outputcsvfilesdir=MTUDirUtils.makeDirectoryWithUniqueIDAndDate(outputdir, bicresults.getUsedmethod());
			else
				this.outputcsvfilesdir=MTUDirUtils.checkandsetDirectories(outputdir);
			
		}
		else 
			throw new IOException("Please define an output folder");
	}
	
	
	/**
	 * Write biclusters information.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void writeBiclustersInformation() throws IOException, InstantiationException, IllegalAccessException{
		//writeBiclustersToBiclustOutputFormatFile();
		writeBiclusterMethodInformation();
		writeBiclusterResults();
		writeBiclusterEnrichmentAnalysis();
	}
	
	
	/**
	 * Write biclusters to biclust output format file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeBiclustersToBiclustOutputFormatFile() throws IOException{
		bicresults.writeBiclusterListToBiclustRPackageFormat(outputcsvfilesdir,"biclust output format","csv");
		try {
			bicresults.writeBiclusterListToJSONFormat(outputcsvfilesdir, "biclusters_json_file", false);
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Cannot save to json file");
		}
		
	}
	
	/**
	 * Write bicluster method information.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeBiclusterMethodInformation() throws IOException{
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter('\t');
		String filepath=FilenameUtils.concat(outputcsvfilesdir, "method information.csv");
		CSVPrinter printer=new CSVPrinter(new FileWriter(filepath), csvFileFormat);
		
		printer.printRecord("Method",bicresults.getUsedmethod());
		printer.printRecord("Running Time",bicresults.getMethodRunningTime());
		printer.println();
		if(writecoverageparameters){
			Triplet<Double, Double, Double> parameters=CoverageAnalyser.getCoverageParameters(bicresults);
			printer.printRecord("Genes coverage percentage", (parameters.getValue0()*100));
			printer.printRecord("Conditions coverage percentage", (parameters.getValue1()*100));
			printer.printRecord("Matrix coverage percentage", (parameters.getValue2()*100));
		}
		printer.flush();
		printer.close();
	}
	
	/**
	 * Write bicluster results.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void  writeBiclusterResults() throws IOException{
		
		StringBuilder str=new StringBuilder();
		
		for (int i = 0; i < bicresults.size(); i++) {
			BiclusterResult bicres=bicresults.get(i);
			str.append(getSingleBiclusterInformation(bicres, i));
			str.append("\n\n");
		}
		String filepath=FilenameUtils.concat(outputcsvfilesdir, "biclusters.csv");
		MTUWriterUtils.writeStringWithFileChannel(str.toString(), filepath, 0);
	}
	
	/**
	 * Gets the single bicluster information.
	 *
	 * @param bicres the bicres
	 * @param bicnumber the bicnumber
	 * @return the single bicluster information
	 */
	private String getSingleBiclusterInformation(BiclusterResult bicres, int bicnumber){
		StringBuilder str=new StringBuilder();
		str.append("Bicluster "+(bicnumber+1)+"\n");
		str.append("Genes= "+bicres.getNumberGenes()+globaldelimiter+"Conditions= "+bicres.getNumberConditions()+"\n");
		str.append("Gene names"+globaldelimiter);
		for (int i = 0; i < bicres.getNumberGenes(); i++) {
			str.append(bicres.getGeneNames().get(i));
			if(i<bicres.getNumberGenes()-1)
				str.append(globaldelimiter);
		}
		str.append("\n");
		
		str.append("Gene indexes"+globaldelimiter);
		for (int i = 0; i < bicres.getNumberGenes(); i++) {
			str.append(bicres.getGeneIndexes().get(i));
			if(i<bicres.getNumberGenes()-1)
				str.append(globaldelimiter);
		}
		str.append("\n");
		
		str.append("Condition names"+globaldelimiter);
		for (int i = 0; i < bicres.getNumberConditions(); i++) {
			str.append(bicres.getConditionNames().get(i));
			if(i<bicres.getNumberConditions()-1)
				str.append(globaldelimiter);
		}
		str.append("\n");
		
		str.append("Condition indexes"+globaldelimiter);
		for (int i = 0; i < bicres.getNumberConditions(); i++) {
			str.append(bicres.getConditionIndexes().get(i));
			if(i<bicres.getNumberConditions()-1)
				str.append(globaldelimiter);
		}
		str.append("\n");
		
		
		return str.toString();
	}
	
	
	/**
	 * Write bicluster enrichment analysis.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void writeBiclusterEnrichmentAnalysis() throws IOException, InstantiationException, IllegalAccessException{
		
	    writeBiclusterEnrichmentAnalysisTotalResults();
		
	    if(pvaluestoreport!=null){
			for (int i = 0; i < pvaluestoreport.size(); i++) {
				double pvalue=pvaluestoreport.get(i);
				writeBiclusterEnrichmentAnalysisWithPvalueCutoff(pvalue);
				writeTermFrequencyForPvalue(pvalue);
			}
		}
    
	}
	
	
	/**
	 * Write bicluster enrichment analysis total results.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeBiclusterEnrichmentAnalysisTotalResults() throws IOException{
		
		StringBuilder str=new StringBuilder();
		
		if(enrichmentresults!=null && enrichmentresults.size()>0){
			writeUnannotatedGeneNames();
			for (int i = 0; i < enrichmentresults.size(); i++) {
				EnrichmentAnalysisResultsContainer bicinfo=enrichmentresults.get(i);
				str.append(getSingleBiclusterEnrichmentAnalysisResult(bicinfo, i));
				str.append("\n\n\n\n");
			}
		
			String filepath=FilenameUtils.concat(outputcsvfilesdir, "Gene Set Enrichment Analysis.csv");
		    MTUWriterUtils.writeStringWithFileChannel(str.toString(), filepath,0);
		}
		
	}
	
	/**
	 * Write unannotated gene names.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeUnannotatedGeneNames() throws IOException{
		HashSet<String> unannotatedgenes=enrichmentresults.getUnannotatedGeneNames();
		if(unannotatedgenes!=null && unannotatedgenes.size()>0){
			String filepath=FilenameUtils.concat(outputcsvfilesdir, "Unannotated Genes.csv");
			MTUWriterUtils.writeListToFile(filepath, unannotatedgenes);
		}
	}
	
	
	/**
	 * Gets the single bicluster enrichment analysis result.
	 *
	 * @param bicinfo the bicinfo
	 * @param bicnumber the bicnumber
	 * @return the single bicluster enrichment analysis result
	 */
	private String getSingleBiclusterEnrichmentAnalysisResult(EnrichmentAnalysisResultsContainer bicinfo, int bicnumber){
		
		StringBuilder str=new StringBuilder();
		str.append("Bicluster "+(bicnumber+1)+"\n");
		str.append(getTotalEnrichmentAnalysisHeader());
		LinkedHashMap<String, String> termidtoGtermname=bicinfo.getMapGOTermToGoName();
		
		
		if(termidtoGtermname!=null){
			for (String termid : termidtoGtermname.keySet()) {
				str.append(termid+globaldelimiter);
				str.append(termidtoGtermname.get(termid)+globaldelimiter);
				if(bicinfo.getMapGOTerm2NumberAnnotatedPopulationGenes()!=null)
					str.append(bicinfo.getMapGOTerm2NumberAnnotatedPopulationGenes().get(termid)+globaldelimiter);
				if(bicinfo.getMapGOTerm2NumberAnnotatedStudyGenes()!=null)
					str.append(bicinfo.getMapGOTerm2NumberAnnotatedStudyGenes().get(termid)+globaldelimiter);
				if(bicinfo.mapMapGOTerm2NumberSignificantAnnotatedGenes()!=null)
					str.append(bicinfo.mapMapGOTerm2NumberSignificantAnnotatedGenes().get(termid)+globaldelimiter);
				if(bicinfo.getGOTermspvalues()!=null)
					str.append(bicinfo.getGOTermspvalues().get(termid)+globaldelimiter);
				if(bicinfo.getGOTermsadjustedpvalues()!=null)
					str.append(bicinfo.getGOTermsadjustedpvalues().get(termid));
				str.append("\n");
			}

		}
		return str.toString();
	}
	
	/**
	 * Gets the total enrichment analysis header.
	 *
	 * @return the total enrichment analysis header
	 */
	private String getTotalEnrichmentAnalysisHeader(){
		return "Term ID"
	             +globaldelimiter+"Term name"
	             +globaldelimiter+"Number population terms"
	             +globaldelimiter+"Number annotated terms"
	             +globaldelimiter+"Number significant terms"
	             +globaldelimiter+"p-values"
	             +globaldelimiter+"Adjusted p-values"+"\n";
	}
	
	
	
	
	/**
	 * Write bicluster enrichment analysis with pvalue cutoff.
	 *
	 * @param pvalue the pvalue
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeBiclusterEnrichmentAnalysisWithPvalueCutoff(double pvalue) throws InstantiationException, IllegalAccessException, IOException{
		
		Map<String, String> mapofprobset2geneidused =enrichmentresults.getMapofprobset2geneidused();
		if(mapofprobset2geneidused!=null)
			mapofprobset2geneidused=MTUMapUtils.invertMap(mapofprobset2geneidused);
		
		
		
		StringBuilder str=new StringBuilder();
		for (int i = 0; i < enrichmentresults.size(); i++) {
			EnrichmentAnalysisResultsContainer bicinfo=enrichmentresults.get(i);
			//System.out.println(bicinfo.toString());
			
			
			ArrayList<String> gotermlowerpvalue=bicinfo.getTermsWithCutoff(pvalue, useadjustedpvalues);
			//////////System.out.println("GOTERM PVALUE: "+gotermlowerpvalue);
			str.append(getSingleBiclusterEnrichmentAnalysiswithpvaluecutoff(bicinfo, gotermlowerpvalue,i, mapofprobset2geneidused));
			str.append("\n\n\n\n");
		}
		
		if(str.length()>0){
			String filepath=FilenameUtils.concat(outputcsvfilesdir, "Gene Set Enrichment Analysis (p-value "+pvalue+").csv");
		    MTUWriterUtils.writeStringWithFileChannel(str.toString(), filepath,0);
		}
	}
	
	/**
	 * Gets the single bicluster enrichment analysiswithpvaluecutoff.
	 *
	 * @param bicinfo the bicinfo
	 * @param gotermlowerpvalue the gotermlowerpvalue
	 * @param bicnumber the bicnumber
	 * @param mapofprobset2geneidused the mapofprobset 2 geneidused
	 * @return the single bicluster enrichment analysiswithpvaluecutoff
	 */
	private String getSingleBiclusterEnrichmentAnalysiswithpvaluecutoff(EnrichmentAnalysisResultsContainer bicinfo, ArrayList<String> gotermlowerpvalue,int bicnumber,Map<String, String> mapofprobset2geneidused){
		
		StringBuilder str=new StringBuilder();
		str.append("Bicluster "+(bicnumber+1)+"\n");
		LinkedHashMap<String, String> termidtoGtermname=bicinfo.getMapGOTermToGoName();
		str.append(getBiclusterEnrichmentAnalysisWithPvalueCutoffHeader(termidtoGtermname));
		
		if(gotermlowerpvalue!=null && gotermlowerpvalue.size()>0){
			for (int i = 0; i < gotermlowerpvalue.size(); i++) {
				String termid=gotermlowerpvalue.get(i);
				str.append(termid+globaldelimiter);
				str.append(termidtoGtermname.get(termid)+globaldelimiter);
				if(bicinfo.getGOTermspvalues()!=null)
					str.append(bicinfo.getGOTermspvalues().get(termid)+globaldelimiter);
				if(bicinfo.getGOTermsadjustedpvalues()!=null)
					str.append(bicinfo.getGOTermsadjustedpvalues().get(termid)+globaldelimiter);
				if(bicinfo.getGenesAssociatedToGoTerm(termid)!=null)
					str.append(bicinfo.getGenesAssociatedToGoTerm(termid)+globaldelimiter);
			    if(bicinfo.getGenesAssociatedToGoTerm(termid)!=null && mapofprobset2geneidused!=null)
			    	str.append(transformgeneidtoprobsetid(bicinfo.getGenesAssociatedToGoTerm(termid),mapofprobset2geneidused));
				str.append("\n");

			}
			
		}
		 return str.toString();
		
	}
	
	/**
	 * Write term frequency for pvalue.
	 *
	 * @param pvalue the pvalue
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeTermFrequencyForPvalue(double pvalue) throws IOException{
		
		EnrichmentAnalysisResultList l=enrichmentresults.filterAndProcessResults(pvalue, useadjustedpvalues);
		writePercentageOfEnrichedBiclustersWithPvalue(pvalue,l);
		
		LinkedHashMap<String, Double> termidfrequency=l.getGotermFrequency();
		
		StringBuilder str =new StringBuilder();
		str.append("Term ID"+globaldelimiter+"Term name"+globaldelimiter+"Frequency (%)"+"\n");
		if(termidfrequency.size()>0)
			  for (String tid : termidfrequency.keySet()) {
				   str.append(tid+globaldelimiter);
				   str.append(enrichmentresults.getGoid2goterm().get(tid)+globaldelimiter);
				   str.append(termidfrequency.get(tid)*100+"\n");
			 }
		
		if(str.length()>0){
			String filepath=FilenameUtils.concat(outputcsvfilesdir, "Terms frequency (p-value "+pvalue+").csv");
		    MTUWriterUtils.writeStringWithFileChannel(str.toString(), filepath,0);
		}
	}
		
	/**
	 * Write percentage of enriched biclusters with pvalue.
	 *
	 * @param pvalue the pvalue
	 * @param l the l
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writePercentageOfEnrichedBiclustersWithPvalue(double pvalue, EnrichmentAnalysisResultList l) throws IOException{
		
		double percentageEnrichedBiclusters=l.getPercentageEnrichedBiclusters();
		
		String appendinfo="Pvalue:\t"+pvalue+"\tEnrichment:\t"+percentageEnrichedBiclusters+"\n";
		
		if(outputcsvfilesdir!=null){
			String filename=FilenameUtils.concat(outputcsvfilesdir, "Percentage_Enriched_Biclusters_per_pvalue.csv");
			File f=new File(filename);
			if(!f.exists()){
				MTUWriterUtils.writeDataTofile(filename, appendinfo);
			}
			else
				MTUWriterUtils.appendDataTofile(filename, appendinfo);
			
		}
		
		
	}
	
	
	/**
	 * Transformgeneidtoprobsetid.
	 *
	 * @param geneid the geneid
	 * @param geneid2probsetid the geneid 2 probsetid
	 * @return the array list
	 */
	private ArrayList<String> transformgeneidtoprobsetid(ArrayList<String> geneid, Map<String,String> geneid2probsetid){
			ArrayList<String> res =new ArrayList<>();
			
			for (int i = 0; i < geneid.size(); i++) {
				res.add(geneid2probsetid.get(geneid.get(i)));
			}
			
			return res;
			
		}
	
	
	/**
	 * Gets the bicluster enrichment analysis with pvalue cutoff header.
	 *
	 * @param mapofprobset2geneidused the mapofprobset 2 geneidused
	 * @return the bicluster enrichment analysis with pvalue cutoff header
	 */
	private String getBiclusterEnrichmentAnalysisWithPvalueCutoffHeader(Map<String, String> mapofprobset2geneidused){
		
		StringBuilder str=new StringBuilder();
		str.append("Term ID"+globaldelimiter);
		str.append("Term name"+globaldelimiter);
		str.append("p-values"+globaldelimiter);
		str.append("Adjusted p-values"+globaldelimiter);
		str.append("Gene names associated to term ID"+globaldelimiter);
		if(mapofprobset2geneidused!=null)
			str.append("Genes probsetID associated to term ID");
		str.append("\n");
		return str.toString();
	}
	
	

	


	

}
