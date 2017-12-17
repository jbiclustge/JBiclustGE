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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javatuples.Triplet;

import jbiclustge.analysis.coverage.CoverageAnalyser;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultsContainer;
import jbiclustge.reporters.interfaces.IGSEABiclusteringReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.properties.JBiGePropertiesManager;
import pt.ornrocha.collections.MTUMapUtils;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringTaskExcelReporter.
 */
public class BiclusteringTaskExcelReporter implements IGSEABiclusteringReporter{
	
	/** The enrichmentresults. */
	protected EnrichmentAnalysisResultList enrichmentresults;
	
	/** The bicresults. */
	protected BiclusterList bicresults;
	
	/** The wb. */
	private Workbook wb=null;
	
	/** The pvaluestoreport. */
	private ArrayList<Double> pvaluestoreport;
	
	/** The useadjustedpvalues. */
	private boolean useadjustedpvalues=false;
	
	/** The writecoverageparameters. */
	private boolean writecoverageparameters=true;
	
	/** The biclusterpersheet. */
	private int biclusterpersheet=300;
	
	/** The outputdir. */
	private String outputdir;
	
	/** The outputexceldir. */
	private String outputexceldir;
	
	/** The createsubfolder. */
	private boolean createsubfolder=true;
	
	/**
	 * Instantiates a new biclustering task excel reporter.
	 */
	public BiclusteringTaskExcelReporter(){}

	/**
	 * Instantiates a new biclustering task excel reporter.
	 *
	 * @param bicresults the bicresults
	 * @param enrichmentresults the enrichmentresults
	 */
	public BiclusteringTaskExcelReporter(BiclusterList bicresults, EnrichmentAnalysisResultList enrichmentresults) {
		this.bicresults=bicresults;
		this.enrichmentresults=enrichmentresults;
	}

	/**
	 * Instantiates a new biclustering task excel reporter.
	 *
	 * @param bicresults the bicresults
	 * @param enrichmentresults the enrichmentresults
	 * @param outputdir the outputdir
	 */
	public BiclusteringTaskExcelReporter(BiclusterList bicresults, EnrichmentAnalysisResultList enrichmentresults, String outputdir) {
		this(bicresults, enrichmentresults);
		this.outputdir=outputdir;
	}
	
	
	/**
	 * Gets the max biclusterpersheet.
	 *
	 * @return the max biclusterpersheet
	 */
	private void getMaxBiclusterpersheet(){
		if(JBiGePropertiesManager.getManager().hasKey("max.biclusters.per.excel.sheet")){
			
			try {
				biclusterpersheet=Integer.valueOf((String) JBiGePropertiesManager.getManager().getKeyValue("max.biclusters.per.excel.sheet")); 
			} catch (Exception e) {
				biclusterpersheet=300;
			}
	
		}
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#writeBiclusteringCoverageParameters(boolean)
	 */
	@Override
	public void writeBiclusteringCoverageParameters(boolean bol) {
		this.writecoverageparameters=bol;
		
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
	
	/* (non-Javadoc)
	 * @see reporters.interfaces.IGSEABiclusteringReporter#setEnrichmentAnalysisPvalueTresholds(java.util.ArrayList)
	 */
	public void setEnrichmentAnalysisPvalueTresholds(ArrayList<Double> values){
		
		for (double d : values) {
			addEnrichmentAnalysisPvalueTreshold(d);
		}
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
		return outputexceldir;
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IGSEABiclusteringReporter#setGeneEnrichmentAnalyserResults(results.biclusters.containers.BiclusterList, enrichmentanalysistools.common.EnrichmentAnalysisResultList)
	 */
	@Override
	public void setGeneEnrichmentAnalyserResults(BiclusterList analysedlist,
			EnrichmentAnalysisResultList enrichmentresults) {
		this.enrichmentresults=enrichmentresults;
		this.bicresults=analysedlist;
		
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#setOutputDirectory(java.lang.String, boolean)
	 */
	@Override
	public void setOutputDirectory(String dirpath, boolean createsubfolder) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#writetodirectory(java.lang.String)
	 */
	@Override
	public void writetodirectory(String dirpath) throws Exception {
		this.outputdir=dirpath;
		createsubfolder=false;
		write();
		
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
	
	/* (non-Javadoc)
	 * @see reporters.interfaces.IBiclusteringReporter#write()
	 */
	@Override
	public void write() throws Exception {
		
		if(outputdir!=null){
			
			if(createsubfolder)
				outputexceldir=MTUDirUtils.makeDirectoryWithUniqueIDAndDate(outputdir, bicresults.getUsedmethod());
			else 
				outputexceldir=MTUDirUtils.checkandsetDirectories(outputdir);
		
		if(bicresults.size()>0){
			getMaxBiclusterpersheet();
			String filepath=getFileNamePath(bicresults.getUsedmethod());
			wb=new XSSFWorkbook();
			writeBiclusteringResultsToWorksheet();
			if(enrichmentresults!=null)
				writeBiclusterEnrichmentAnalysis();
		
			FileOutputStream fos = new FileOutputStream(filepath);
			wb.write(fos);
			fos.close();
			wb.close();
			
			//writeBiclusterstoFile();
		}
		else
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Empty biclusters list, nothing to do");
		}
	}
	
	
	/*private void writeBiclusterstoFile() throws IOException{
		if(outputexceldir!=null){
			bicresults.writeBiclusterListToBiclustRPackageFormat(outputexceldir,"biclust output format","csv");
			bicresults.writeBiclusterListToJSONFormat(outputexceldir, "biclusters_json_file", false);
		}
	}*/

	
	/**
	 * Write biclustering results to worksheet.
	 */
	protected void writeBiclusteringResultsToWorksheet(){
		Sheet sh=null;
		if(bicresults.size()<=biclusterpersheet)
			sh=wb.createSheet("Biclustering results");
		else
			sh=wb.createSheet("Biclustering results ("+String.valueOf(1)+" to "+String.valueOf(biclusterpersheet)+")");
		int currentrownumber=0;
		
		Row r0=sh.createRow(currentrownumber);
		Cell cb0=r0.createCell(0);
		cb0.setCellValue("Biclustering method used");
		cb0.setCellStyle(getCellNameStyle(false,IndexedColors.DARK_TEAL));
		r0.createCell(1).setCellValue(bicresults.getUsedmethod());
		currentrownumber++;
		
		if(bicresults.getMethodRunningTime()!=null){
			Row rtime=sh.createRow(currentrownumber);
			Cell ctime=rtime.createCell(0);
			ctime.setCellValue("Running Time");
			ctime.setCellStyle(getCellNameStyle(false,IndexedColors.BLUE));
			rtime.createCell(1).setCellValue(bicresults.getMethodRunningTime());
			currentrownumber++;
		}
		setRowSpace(sh, currentrownumber);
		currentrownumber++;
		
		if(writecoverageparameters){
			XSSFCellStyle stylecv = getCellNameStyle(true,IndexedColors.ORANGE);
			
			Triplet<Double, Double, Double> parameters=CoverageAnalyser.getCoverageParameters(bicresults);
			
			Row r1=sh.createRow(currentrownumber);
			Cell cb=r1.createCell(0);
			cb.setCellValue("Biclustering Coverage Parameters");
			cb.setCellStyle(stylecv);
			sh.addMergedRegion(new CellRangeAddress(currentrownumber,currentrownumber,0,2));
			currentrownumber++;
			Row r2=sh.createRow(currentrownumber);
			r2.createCell(0).setCellValue("Genes coverage percentage");
			r2.createCell(1).setCellValue(parameters.getValue0()*100);
			currentrownumber++;
			Row r3=sh.createRow(currentrownumber);
			r3.createCell(0).setCellValue("Conditions coverage percentage");
			r3.createCell(1).setCellValue(parameters.getValue1()*100);
			currentrownumber++;
			Row r4=sh.createRow(currentrownumber);
			r4.createCell(0).setCellValue("Matrix coverage percentage");
			r4.createCell(1).setCellValue(parameters.getValue2()*100);
			currentrownumber++;
			setRowSpace(sh, currentrownumber);
			currentrownumber++;
			setRowSpace(sh, currentrownumber);
			currentrownumber++;
			
		}
		
		XSSFCellStyle style = getCellNameStyle(false,IndexedColors.GREEN);
		
		int processedbicluster=0;
		
		for (int i = 0; i < bicresults.size(); i++) {
			BiclusterResult bicluster=bicresults.get(i);
			
			if(processedbicluster==biclusterpersheet){
				int interval=bicresults.size()-i;
				if(interval>biclusterpersheet)
					interval=i+biclusterpersheet;
				else
					interval=bicresults.size();
					
				sh=wb.createSheet("Biclustering results ("+String.valueOf(i+1)+" to "+String.valueOf(interval)+")");
				processedbicluster=0;
				currentrownumber=0;
			}
			
			currentrownumber=writeBicluster(bicluster, currentrownumber, sh, i, style);
			processedbicluster++;
	
		}
		
	}
	
	/**
	 * Write bicluster.
	 *
	 * @param bicluster the bicluster
	 * @param currentrownumber the currentrownumber
	 * @param sh the sh
	 * @param biclustnumber the biclustnumber
	 * @param style the style
	 * @return the int
	 */
	private int writeBicluster(BiclusterResult bicluster, int currentrownumber, Sheet sh, int biclustnumber ,XSSFCellStyle style){
		
		if(currentrownumber>0){
			setRowSpace(sh, currentrownumber);
			currentrownumber++;
		}
		
		Row row =setBiclusterNumberHeader(biclustnumber, currentrownumber, sh, style);
		String numbergenes= "Genes= "+bicluster.getNumberGenes();
		String numbercond="Conditions= "+bicluster.getNumberConditions();
		row.createCell(1).setCellValue(numbergenes);
		row.createCell(2).setCellValue(numbercond);
		currentrownumber++;
		
		writeConditionsInfo(currentrownumber, biclustnumber, sh, bicluster,false);
		currentrownumber++;
		writeConditionsInfo(currentrownumber, biclustnumber, sh, bicluster,true);
		currentrownumber++;
		writeGenesInfo(currentrownumber, biclustnumber, sh, bicluster,false);
		currentrownumber++;
		writeGenesInfo(currentrownumber, biclustnumber, sh, bicluster,true);
		currentrownumber++;
		
		return currentrownumber;
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
			}
		}
    
	}
	
	/**
	 * Write bicluster enrichment analysis total results.
	 */
	private void writeBiclusterEnrichmentAnalysisTotalResults(){
		Sheet sh=null;
		if(enrichmentresults.size()<=biclusterpersheet)
			sh=wb.createSheet("Gene Set Enrichment Analysis");
		else
			sh=wb.createSheet("GSEA ("+String.valueOf(1)+" to "+String.valueOf(biclusterpersheet)+")");

		XSSFCellStyle style =getCellNameStyle(true, IndexedColors.AQUA);
	
		int currentrownumber=0;
		writeEnrichmentAnalysisTotalResultsHeader(sh,currentrownumber);
		currentrownumber++;
		
		int processedenranalysis=0;
		
		for (int i = 0; i < enrichmentresults.size(); i++) {
			EnrichmentAnalysisResultsContainer bicinfo=enrichmentresults.get(i);
			
			if(processedenranalysis==biclusterpersheet){
				int interval=enrichmentresults.size()-i;
				if(interval>biclusterpersheet)
					interval=i+biclusterpersheet;
				else
					interval=enrichmentresults.size();
					
				sh=wb.createSheet("GSEA ("+String.valueOf(i+1)+" to "+String.valueOf(interval)+")");
				processedenranalysis=0;
				currentrownumber=0;
				writeEnrichmentAnalysisTotalResultsHeader(sh, currentrownumber);
				currentrownumber++;
			}

			
			int initrow=currentrownumber;

			int passrows=writeSingleBiclusterEnrichmentAnalysis(sh, bicinfo, currentrownumber,i,style);
			currentrownumber=currentrownumber+passrows;
			sh.addMergedRegion(new CellRangeAddress(initrow,currentrownumber,0,0));
			currentrownumber++;
			setRowSpace(sh, currentrownumber);
			currentrownumber++;
			processedenranalysis++;
		}

	}
	
	/**
	 * Write enrichment analysis total results header.
	 *
	 * @param sh the sh
	 * @param currentrownumber the currentrownumber
	 */
	private void writeEnrichmentAnalysisTotalResultsHeader(Sheet sh, int currentrownumber){
		Row r1=sh.createRow(currentrownumber);
		r1.createCell(0).setCellValue("");
		r1.createCell(1).setCellValue("Term ID");
		r1.createCell(2).setCellValue("Term name");
		r1.createCell(3).setCellValue("Number population terms");
		r1.createCell(4).setCellValue("Number annotated terms");
		r1.createCell(5).setCellValue("Number significant terms");
		r1.createCell(6).setCellValue("p-values");
		r1.createCell(7).setCellValue("Adjusted p-values");
	}
	
	/**
	 * Write single bicluster enrichment analysis.
	 *
	 * @param sh the sh
	 * @param bicinfo the bicinfo
	 * @param currentrow the currentrow
	 * @param currentbicnumber the currentbicnumber
	 * @param style the style
	 * @return the int
	 */
	private int writeSingleBiclusterEnrichmentAnalysis(Sheet sh, EnrichmentAnalysisResultsContainer bicinfo, int currentrow,int currentbicnumber, XSSFCellStyle style){
		
		LinkedHashMap<String, String> termidtoGtermname=bicinfo.getMapGOTermToGoName();
		int cr=currentrow;
		int nrows=0;
		
		//Row row=sh.createRow(cr);
		Row row=null;
		if(termidtoGtermname!=null){
			for (String termid : termidtoGtermname.keySet()) {

				if(nrows==0)
					row=setBiclusterNumberHeader(currentbicnumber, currentrow, sh, style);
				else
					row=sh.createRow(cr);
			//r1.createCell(0).setCellValue("");
				row.createCell(1).setCellValue(termid);
				row.createCell(2).setCellValue(termidtoGtermname.get(termid));
				if(bicinfo.getMapGOTerm2NumberAnnotatedPopulationGenes()!=null)
					row.createCell(3).setCellValue(bicinfo.getMapGOTerm2NumberAnnotatedPopulationGenes().get(termid));
				if(bicinfo.getMapGOTerm2NumberAnnotatedStudyGenes()!=null)
					row.createCell(4).setCellValue(bicinfo.getMapGOTerm2NumberAnnotatedStudyGenes().get(termid));
				if(bicinfo.mapMapGOTerm2NumberSignificantAnnotatedGenes()!=null)
					row.createCell(5).setCellValue(bicinfo.mapMapGOTerm2NumberSignificantAnnotatedGenes().get(termid));
				if(bicinfo.getGOTermspvalues()!=null)
					row.createCell(6).setCellValue(bicinfo.getGOTermspvalues().get(termid));
				if(bicinfo.getGOTermsadjustedpvalues()!=null)
					row.createCell(7).setCellValue(bicinfo.getGOTermsadjustedpvalues().get(termid));
				nrows++;
				cr++;
			}
			return nrows-1;
		}
		return currentrow;
	}
	
	
	/**
	 * Write bicluster enrichment analysis with pvalue cutoff.
	 *
	 * @param pvalue the pvalue
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void writeBiclusterEnrichmentAnalysisWithPvalueCutoff(double pvalue) throws InstantiationException, IllegalAccessException{
		
		String sheetname=null;
		if(enrichmentresults.size()<=biclusterpersheet)
			sheetname="GSEA with p-value "+String.valueOf(pvalue);
		else{
			sheetname="GSEA p-value "+String.valueOf(pvalue)+" ("+String.valueOf(1)+" to "+String.valueOf(biclusterpersheet)+")";
		}

		Sheet sh=wb.createSheet(sheetname);
		XSSFCellStyle style =getCellNameStyle(true,IndexedColors.GREY_40_PERCENT);
		
		Map<String, String> mapofprobset2geneidused =enrichmentresults.getMapofprobset2geneidused();
		if(mapofprobset2geneidused!=null)
			mapofprobset2geneidused=MTUMapUtils.invertMap(mapofprobset2geneidused);
		
		int currentrownumber=0;
		writeBiclusterEnrichmentAnalysisWithPvalueCutoffHeader(sh, currentrownumber, mapofprobset2geneidused);
	/*	Row r1=sh.createRow(currentrownumber);
		r1.createCell(0).setCellValue("");
		r1.createCell(1).setCellValue("Term ID");
		r1.createCell(2).setCellValue("Term name");
		r1.createCell(3).setCellValue("p-values");
		r1.createCell(4).setCellValue("Adjusted p-values");
		r1.createCell(5).setCellValue("Gene names associated to term ID");
		if(mapofprobset2geneidused!=null)
			r1.createCell(6).setCellValue("Genes probsetID associated to term ID");*/
		currentrownumber++;
		
		
		int processedenranalysis=0;
		
		for (int i = 0; i < enrichmentresults.size(); i++) {
			EnrichmentAnalysisResultsContainer bicinfo=enrichmentresults.get(i);
			
			if(processedenranalysis==biclusterpersheet){
				int interval=enrichmentresults.size()-i;
				if(interval>biclusterpersheet)
					interval=i+biclusterpersheet;
				else
					interval=enrichmentresults.size();
				
				sheetname="GSEA p-value "+String.valueOf(pvalue)+" ("+String.valueOf(i+1)+" to "+String.valueOf(interval)+")";
				sh=wb.createSheet(sheetname);
				processedenranalysis=0;
				currentrownumber=0;
				writeEnrichmentAnalysisTotalResultsHeader(sh, currentrownumber);
				currentrownumber++;
			}
			
			ArrayList<String> gotermlowerpvalue=bicinfo.getTermsWithCutoff(pvalue, useadjustedpvalues);
			int initrow=currentrownumber;
			int passrows=writeSingleBiclusterEnrichmentAnalysiswithpvaluecutoff(sh, bicinfo, gotermlowerpvalue, mapofprobset2geneidused, currentrownumber, i, style);
			currentrownumber=currentrownumber+passrows;
			sh.addMergedRegion(new CellRangeAddress(initrow,currentrownumber,0,0));
			currentrownumber++;
			setRowSpace(sh, currentrownumber);
			currentrownumber++;
			processedenranalysis++;
		}
		
		
		
		Row rf=sh.createRow(currentrownumber);
		Cell cb=rf.createCell(0);
		cb.setCellValue("Additional Information");
		cb.setCellStyle(getCellNameStyle(true, IndexedColors.LIGHT_CORNFLOWER_BLUE));
		sh.addMergedRegion(new CellRangeAddress(currentrownumber,currentrownumber,0,3));
		currentrownumber++;
		setRowSpace(sh, currentrownumber);
		currentrownumber++;	
		
		EnrichmentAnalysisResultList l=enrichmentresults.filterAndProcessResults(pvalue, useadjustedpvalues);
		
		LinkedHashMap<String, Double> termidfrequency=l.getGotermFrequency();
		
		Row rfreq=sh.createRow(currentrownumber);
		Cell cbf=rfreq.createCell(0);
		cbf.setCellValue("Terms frequency (%)");
		cbf.setCellStyle(getCellNameStyle(true, IndexedColors.BLUE));
		sh.addMergedRegion(new CellRangeAddress(currentrownumber,currentrownumber,0,3));
		currentrownumber++;
		
		Row rt=sh.createRow(currentrownumber);
		rt.createCell(0).setCellValue("Term ID");
		rt.createCell(1).setCellValue("Term name");
		rt.createCell(2).setCellValue("Frequency (%)");
		currentrownumber++;
		
		if(termidfrequency.size()>0)
		  for (String tid : termidfrequency.keySet()) {
			   Row ro=sh.createRow(currentrownumber);
				ro.createCell(0).setCellValue(tid);
				ro.createCell(1).setCellValue(enrichmentresults.getGoid2goterm().get(tid));
				ro.createCell(2).setCellValue(termidfrequency.get(tid)*100);
				currentrownumber++;
		 }
		
	}
	
	/**
	 * Write bicluster enrichment analysis with pvalue cutoff header.
	 *
	 * @param sh the sh
	 * @param currentrownumber the currentrownumber
	 * @param mapofprobset2geneidused the mapofprobset 2 geneidused
	 */
	private void writeBiclusterEnrichmentAnalysisWithPvalueCutoffHeader(Sheet sh, int currentrownumber,Map<String, String> mapofprobset2geneidused){
		Row r1=sh.createRow(currentrownumber);
		r1.createCell(0).setCellValue("");
		r1.createCell(1).setCellValue("Term ID");
		r1.createCell(2).setCellValue("Term name");
		r1.createCell(3).setCellValue("p-values");
		r1.createCell(4).setCellValue("Adjusted p-values");
		r1.createCell(5).setCellValue("Gene names associated to term ID");
		if(mapofprobset2geneidused!=null)
			r1.createCell(6).setCellValue("Genes probsetID associated to term ID");
	}
	
	
	/**
	 * Write single bicluster enrichment analysiswithpvaluecutoff.
	 *
	 * @param sh the sh
	 * @param bicinfo the bicinfo
	 * @param gotermlowerpvalue the gotermlowerpvalue
	 * @param mapofprobset2geneidused the mapofprobset 2 geneidused
	 * @param currentrow the currentrow
	 * @param currentbicnumber the currentbicnumber
	 * @param style the style
	 * @return the int
	 */
	private int writeSingleBiclusterEnrichmentAnalysiswithpvaluecutoff(Sheet sh, EnrichmentAnalysisResultsContainer bicinfo, ArrayList<String> gotermlowerpvalue,Map<String, String> mapofprobset2geneidused,int currentrow,int currentbicnumber, XSSFCellStyle style){
		
		LinkedHashMap<String, String> termidtoGtermname=bicinfo.getMapGOTermToGoName();
		int cr=currentrow;
		int nrows=0;
		Row row=null;
		
		if(gotermlowerpvalue!=null && gotermlowerpvalue.size()>0){
			for (int i = 0; i < gotermlowerpvalue.size(); i++) {
				String termid=gotermlowerpvalue.get(i);
				if(nrows==0)
					row=setBiclusterNumberHeader(currentbicnumber, currentrow, sh, style);
				else
					row=sh.createRow(cr);
            
				row.createCell(1).setCellValue(termid);
			    row.createCell(2).setCellValue(termidtoGtermname.get(termid));
			    
			    if(bicinfo.getGOTermspvalues()!=null)
			    	row.createCell(3).setCellValue(bicinfo.getGOTermspvalues().get(termid));
			    if(bicinfo.getGOTermsadjustedpvalues()!=null)
			    	row.createCell(4).setCellValue(bicinfo.getGOTermsadjustedpvalues().get(termid));
			    
			    if(bicinfo.getGenesAssociatedToGoTerm(termid)!=null)
			    	row.createCell(5).setCellValue(String.valueOf(bicinfo.getGenesAssociatedToGoTerm(termid)));
			    if(bicinfo.getGenesAssociatedToGoTerm(termid)!=null && mapofprobset2geneidused!=null){
			    	row.createCell(6).setCellValue(String.valueOf(transformgeneidtoprobsetid(bicinfo.getGenesAssociatedToGoTerm(termid),mapofprobset2geneidused)));
			    }
			    
			    nrows++;
			    cr++;
			    
			}
			return nrows-1;
		}
		else{
			
			setBiclusterNumberHeader(currentbicnumber, currentrow, sh, style);
			return nrows;
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
	 * Sets the bicluster number header.
	 *
	 * @param nbic the nbic
	 * @param currentrownumber the currentrownumber
	 * @param sh the sh
	 * @param style the style
	 * @return the row
	 */
	private Row setBiclusterNumberHeader(int nbic, int currentrownumber,Sheet sh, XSSFCellStyle style){
		Row row =sh.createRow(currentrownumber);
		String clusterid="Bicluster "+(nbic+1);
		Cell cb=row.createCell(0);
		cb.setCellValue(clusterid);
		if(style!=null)
			cb.setCellStyle(style);
		
		return row;
	}
	
	/**
	 * Sets the row space.
	 *
	 * @param sh the sh
	 * @param currentrownumber the currentrownumber
	 */
	private void setRowSpace(Sheet sh, int currentrownumber){
		sh.createRow(currentrownumber).createCell(0).setCellValue("");
	}
	
	/**
	 * Write genes info.
	 *
	 * @param currentrow the currentrow
	 * @param numbercluster the numbercluster
	 * @param sh the sh
	 * @param bicluster the bicluster
	 * @param isindex the isindex
	 */
	private void writeGenesInfo(int currentrow, int numbercluster, Sheet sh, BiclusterResult bicluster,boolean isindex){
		
		Row row =sh.createRow(currentrow);

		if(isindex){
			row.createCell(0).setCellValue("Gene indexes: ");
			ArrayList<Integer>geneinfo=bicluster.getGeneIndexes();
			for (int i = 0; i < geneinfo.size(); i++) {
				row.createCell(i+1).setCellValue(geneinfo.get(i));
			}
			
		}
		else{
			row.createCell(0).setCellValue("Gene names: ");
			ArrayList<String> geneids=bicluster.getGeneNames();
			for (int i = 0; i < geneids.size(); i++) {
			row.createCell(i+1).setCellValue(geneids.get(i));
			}
		}
	
	}
	
	/**
	 * Write conditions info.
	 *
	 * @param currentrow the currentrow
	 * @param numbercluster the numbercluster
	 * @param sh the sh
	 * @param bicluster the bicluster
	 * @param isindex the isindex
	 */
	private void writeConditionsInfo(int currentrow, int numbercluster, Sheet sh, BiclusterResult bicluster,boolean isindex){
		
		Row row =sh.createRow(currentrow);
		
		if(isindex){
			row.createCell(0).setCellValue("Condition indexes: ");
			ArrayList<Integer> conditionsindexes=bicluster.getConditionIndexes();
			
			
			for (int i = 0; i < conditionsindexes.size(); i++) {
				row.createCell(i+1).setCellValue(conditionsindexes.get(i));
			}
			
		}
		else{
			
			row.createCell(0).setCellValue("Condition names: ");
			ArrayList<String> conditionsids=bicluster.getConditionNames();
			for (int i = 0; i < conditionsids.size(); i++) {
				row.createCell(i+1).setCellValue(conditionsids.get(i));
			}
		}
	}
	
	/**
	 * Gets the cell name style.
	 *
	 * @param centeralignment the centeralignment
	 * @param color the color
	 * @return the cell name style
	 */
	private XSSFCellStyle getCellNameStyle(boolean centeralignment, IndexedColors color){
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillBackgroundColor(color.getIndex());
		style.setFillPattern(CellStyle.ALIGN_FILL);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		if(centeralignment){
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		}
		return style;
	}
	
	 /**
 	 * Gets the file name path.
 	 *
 	 * @param name the name
 	 * @return the file name path
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	protected String getFileNamePath(String name) throws IOException{
		  return FilenameUtils.concat(outputexceldir, "Results of "+name+" "+MTUTimeUtils.getCurrentDateAndTime("yyyy-MM-dd_HH.mm.ss")+".xlsx");
	   }




	

}
