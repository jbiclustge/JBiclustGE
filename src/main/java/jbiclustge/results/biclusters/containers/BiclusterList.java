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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import jbiclustge.analysis.overlap.OverlapAnalyser;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.results.biclusters.BiclusterOutputFormat;
import pt.ornrocha.collections.MTUCollectionsUtils;
import pt.ornrocha.collections.MTUListUtils;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.jsonutils.MTUJsonIOUtils;
import pt.ornrocha.jsonutils.MTUJsonUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.timeutils.MTUTimeUtils;
import smile.imputation.MissingValueImputation;
import smile.imputation.MissingValueImputationException;

/**
 * 
 * @startuml
 * 
 * package "jrplot" #DDDDDD {
      
      note "<size:16>Package containing multiple funtions</size>\n<size:16>for the generation of static/interactive plots</size>\n<size:16>using R libraries as background engines</size>" as N1
   }
 * 
 * 
 * package "JBiclustGE" {
 * 
 * abstract class AbstractBiclusteringAlgorithmCaller  {
 * 
 * #ExpressionData expressionset
 * #BiclusterList listofbiclusters
 * +BiclusterList getBiclusterResultList()
 * }
 * 
 * class BiclusterResult{
	-ArrayList<Integer> genesindex
	-ArrayList<String> genenames
	-ArrayList<Integer> conditionsindex
	-ArrayList<String> conditionnames
	-ExpressionData originaldataset

	}
 * 
 * class BiclusterList {
	
	-String usedmethod
	-LinkedHashMap<String,String> methodrunningparameters
	-ExpressionData datasetanalysed
	-LinkedHashMap<String,Object> methodadditionalinfo
	
	+void exportBiclusterList(String dir, String filename, BiclusterOutputFormat exportformat)

}
 * 
 * class "ArrayList<BiclusterResult>" as ArrayList_BiclusterResult_ {
 * }
 * 
 * abstract class EnrichmentAnalyserProcessor  {
 *  #BiclusterList listofbiclusters
 *  #EnrichmentAnalysisResultList biclusterEnrichmentAnalysisList
 *  +void setBiclusteringResultsToAnalyse(BiclusterList listofbiclusters)
 *  +EnrichmentAnalysisResultList getEnrichmentAnalysisResults()
 * }
 * 
 * abstract class AbstractSimilarity {
 * 
 * }
 * 
 * 
 * 
 * ArrayList_BiclusterResult_ <|-- BiclusterList
 * 
 * BiclusterList *-right- BiclusterResult
 * AbstractBiclusteringAlgorithmCaller -right-> BiclusterList
 * 
 * EnrichmentAnalyserProcessor -up-> BiclusterList
 * 
 * EnrichmentAnalyserProcessor <|-- OntologizerEnrichmentAnalyser
 * EnrichmentAnalyserProcessor <|-- TopGOEnrichmentAnalyser
 * 
 * CoherenceAnalyser -up-> BiclusterList
 * CoverageAnalyser -up-> BiclusterList
 * OverlapAnalyser -up-> BiclusterList
 * AbstractSimilarity -up-> BiclusterList
 * }
 * 
 * jrplot -up- JBiclustGE
 * 
 * @enduml
 */

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusterList.
 */
public class BiclusterList extends ArrayList<BiclusterResult> {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The nameoflist. */
	private String nameoflist;
	
	/** The usedmethod. */
	private String usedmethod;
	
	/** The methodrunningtime. */
	private String methodrunningtime;
	
	/** The methodrunningparameters. */
	private LinkedHashMap<String, String> methodrunningparameters;
	
	/** The datasetanalysed. */
	private ExpressionData datasetanalysed;
	
	/** The methodadditionalinfo. */
	private LinkedHashMap<String, Object> methodadditionalinfo=new LinkedHashMap<>();
	
	/** The Constant BICLUSTERLISTTAG. */
	protected static final String BICLUSTERLISTTAG="Biclusters";
	
	/** The Constant BICLUSTERLISTEXPRESSIONDATATAG. */
	public static final String BICLUSTERLISTEXPRESSIONDATATAG="Gene expression dataset";
	
	/** The Constant BICLUSTERINGMETHOD. */
	public static final String BICLUSTERINGMETHOD="Biclustering method";
	
	/** The Constant BICLUSTERINGMETHODPARAMETERS. */
	public static final String BICLUSTERINGMETHODPARAMETERS="Biclustering method parameters";
	
	/** The Constant BICLUSTERINGADDITIONALINFO. */
	public static final String BICLUSTERINGADDITIONALINFO="Biclustering additional information";
	
	/**
	 * Instantiates a new bicluster list.
	 */
	public BiclusterList(){
		super();
	}
	
	/**
	 * Instantiates a new bicluster list.
	 *
	 * @param list the list
	 */
	public BiclusterList(BiclusterList list){
		super(list);
		setUsedmethod(list.getUsedmethod());
		setMethodRunningTime(list.getMethodRunningTime());
		setAnalysedDataset(list.getAnalysedDataset());
		setAditionalBiclusterMethodInformation(list.getAditionalBiclusterMethodInformation());
	}
	
	/**
	 * Instantiates a new bicluster list.
	 *
	 * @param nameoflist the nameoflist
	 */
	public BiclusterList(String nameoflist){
		super();
		this.nameoflist=nameoflist;
	}
	
	private BiclusterList(String nameoflist,
			String usedmethod,
			String methodrunningtime,
			LinkedHashMap<String, String> methodrunningparameters,
			LinkedHashMap<String, Object> methodadditionalinfo) {
		
		this.nameoflist=nameoflist;
		this.usedmethod=usedmethod;
		this.methodrunningtime=methodrunningtime;
		this.methodrunningparameters=methodrunningparameters;
		this.methodadditionalinfo=methodadditionalinfo;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name){
		this.nameoflist=name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return nameoflist;
	}

	
	
	/**
	 * Gets the usedmethod.
	 *
	 * @return the usedmethod
	 */
	public String getUsedmethod() {
		return usedmethod;
	}

	/**
	 * Sets the usedmethod.
	 *
	 * @param usedmethod the new usedmethod
	 */
	public void setUsedmethod(String usedmethod) {
		this.usedmethod = usedmethod;
	}
	
	
	
	/**
	 * Gets the method running parameters.
	 *
	 * @return the method running parameters
	 */
	public LinkedHashMap<String, String> getMethodRunningParameters() {
		return methodrunningparameters;
	}

	/**
	 * Sets the method running parameters.
	 *
	 * @param methodrunningparameters the methodrunningparameters
	 */
	public void setMethodRunningParameters(LinkedHashMap<String, String> methodrunningparameters) {
		this.methodrunningparameters = methodrunningparameters;
	}

	/**
	 * Gets the method running time.
	 *
	 * @return the method running time
	 */
	public String getMethodRunningTime() {
		return methodrunningtime;
	}

	/**
	 * Sets the method running time.
	 *
	 * @param methodrunningtime the new method running time
	 */
	public void setMethodRunningTime(String methodrunningtime) {
		this.methodrunningtime = methodrunningtime;
	}

	/**
	 * Sets the analysed dataset.
	 *
	 * @param data the new analysed dataset
	 */
	public void setAnalysedDataset(ExpressionData data){
		this.datasetanalysed=data;
	}
	
	/**
	 * Sets the analysed dataset and verify subsets.
	 *
	 * @param data the new analysed dataset and verify subsets
	 */
	public void setAnalysedDatasetAndVerifySubsets(ExpressionData data){
		this.datasetanalysed=data;
		for (int i = 0; i < size(); i++) {
			if(get(i).getAssociatedExpressionData()==null)
				get(i).setAssociatedExpressionData(data);
		}
	}
	
	/**
	 * Gets the analysed dataset.
	 *
	 * @return the analysed dataset
	 */
	public ExpressionData getAnalysedDataset(){
		return datasetanalysed;
	}

	/**
	 * Gets the number biclusters.
	 *
	 * @return the number biclusters
	 */
	public int getNumberBiclusters(){
		return this.size();
	}
	
	/**
	 * Gets the average area biclusters.
	 *
	 * @return the average area biclusters
	 */
	public double getAverageAreaBiclusters(){
		
		double avg=0;
		for (int i = 0; i < size(); i++) {
			BiclusterResult res=get(i);
			avg+=res.area();
		}
		
		return avg/size();
	}
	
	/**
	 * Gets the biclusters gene name list.
	 *
	 * @return the biclusters gene name list
	 */
	public ArrayList<ArrayList<String>> getBiclustersGeneNameList(){
		ArrayList<ArrayList<String>> res=new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			res.add(new ArrayList<>(get(i).getGeneNames()));
		}
		return res;
	}
	
	/**
	 * Gets the biclusters conditions name list.
	 *
	 * @return the biclusters conditions name list
	 */
	public ArrayList<ArrayList<String>> getBiclustersConditionsNameList(){
		ArrayList<ArrayList<String>> res=new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			res.add(new ArrayList<>(get(i).getConditionNames()));
		}
		return res;
	}
	
	/**
	 * Gets the biclusters gene index list.
	 *
	 * @return the biclusters gene index list
	 */
	public ArrayList<Integer[]> getBiclustersGeneIndexList(){
		ArrayList<Integer[]> res=new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			res.add(get(i).getArrayGeneIndexes());
		}
		return res;
	}
	
	/**
	 * Gets the biclusters condition index list.
	 *
	 * @return the biclusters condition index list
	 */
	public ArrayList<Integer[]> getBiclustersConditionIndexList(){
		ArrayList<Integer[]> res=new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			res.add(get(i).getArrayConditionIndexes());
		}
		return res;
	}
	
	/**
	 * Adds the aditional bicluster method information.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void addAditionalBiclusterMethodInformation(String key, Object value){
		if(methodadditionalinfo==null)
			methodadditionalinfo=new LinkedHashMap<>();
		
		methodadditionalinfo.put(key, value);
	}
	
	/**
	 * Gets the aditional bicluster method information.
	 *
	 * @return the aditional bicluster method information
	 */
	public LinkedHashMap<String, Object> getAditionalBiclusterMethodInformation(){
		return methodadditionalinfo;
	}
	
	/**
	 * Sets the aditional bicluster method information.
	 *
	 * @param info the info
	 */
	public void setAditionalBiclusterMethodInformation(LinkedHashMap<String, Object> info){
		this.methodadditionalinfo=info;
	}
	
	/**
	 * Filter by overlap treshold.
	 *
	 * @param threshold the threshold
	 * @return the bicluster list
	 * @throws Exception the exception
	 */
	public BiclusterList filterByOverlapTreshold(double threshold) throws Exception{
		return OverlapAnalyser.filterBiclusterListWithOverlapThreshold(this, threshold, -1);
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString(){
		StringBuilder str=new StringBuilder();
		if(usedmethod!=null)
			str.append("Method used: "+usedmethod+"\n");
		if(nameoflist!=null)
		   str.append("Name of the biclusters set: "+nameoflist+"\n");
		for (int i = 0; i < this.size(); i++) {
			str.append("Bicluster: "+i+"\n");
			str.append(get(i).toString());
			str.append("\n\n");
		}
		if(str!=null)
			return str.toString();
		
	    return "Empty List!";
	}
	
	/**
	 * Prints the results.
	 */
	public void printResults(){
		System.out.println(toString());
	}
	
	
	/**
	 * Import biclusters from biclust R package output format.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclustersFromBiclustRPackageOutputFormat(String biclustoutputfile) throws IOException, ParseException, MissingValueImputationException{
		return importBiclustersFromBiclustRPackageOutputFormat(biclustoutputfile,null, null,null);
	}
	
	/**
	 * Import biclusters from biclust R package output format.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param originaldataset the originaldataset
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclustersFromBiclustRPackageOutputFormat(String biclustoutputfile, ExpressionData originaldataset) throws IOException, ParseException, MissingValueImputationException{
		return importBiclustersFromBiclustRPackageOutputFormat(biclustoutputfile,null, originaldataset);
	}
	
	
	/**
	 * Import biclusters from biclust R package output format.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param delimiter the delimiter
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclustersFromBiclustRPackageOutputFormat(String biclustoutputfile, String delimiter) throws IOException, ParseException, MissingValueImputationException{
		return importBiclustersFromBiclustRPackageOutputFormat(biclustoutputfile, delimiter, null,null);
	}
	
	
	/**
	 * Import biclusters from biclust R package output format.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param biclustfiledelimiter the biclustfiledelimiter
	 * @param expressiondatafilepath the expressiondatafilepath
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclustersFromBiclustRPackageOutputFormat(String biclustoutputfile, String biclustfiledelimiter, String expressiondatafilepath) throws IOException, ParseException, MissingValueImputationException{
		return importBiclustersFromBiclustRPackageOutputFormat(biclustoutputfile, biclustfiledelimiter, expressiondatafilepath, null);
	}
	
	
	/**
	 * Import biclusters from txt file.
	 *
	 * @param data the data
	 * @param biclustersfilepath the biclustersfilepath
	 * @param delimiter the delimiter
	 * @param areindexes the areindexes
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterList importBiclustersFromTxtFile(ExpressionData data, String biclustersfilepath, String delimiter, boolean areindexes) throws IOException {
		
		if(delimiter==null)
			 delimiter=" ";
		 
		 ArrayList<String> lines;

		lines = MTUReadUtils.readFileLinesRemoveEmptyLines(biclustersfilepath);
	
		 
		 BiclusterList res=new BiclusterList();
				 
		 String geneline=null;
		 String condline=null;
		 
		 int bicl=1;
		 for (int i = 0; i <lines.size(); i++) {
			 if(bicl==1)
				 geneline=lines.get(i);
			 else{
				 condline=lines.get(i);
				 
				 BiclusterResult bicluster=null;
				 try {
				 if(areindexes){
					 ArrayList<Integer> geneindexes=MTUListUtils.getIntegerElementsFromStringArrayNumber(geneline, delimiter);
					 ArrayList<Integer> condindexes=MTUListUtils.getIntegerElementsFromStringArrayNumber(condline, delimiter);
					 bicluster=new BiclusterResult(data, geneindexes, condindexes, true);
				 }
				 else{
					 ArrayList<String> genenames=MTUListUtils.getElementsFromString(geneline, delimiter);
					 ArrayList<String> condnames=MTUListUtils.getElementsFromString(condline, delimiter);
					 bicluster=new BiclusterResult(data, genenames, condnames);
				 }
				 } catch (Exception e) {
					 throw new NumberFormatException();
				}
				 res.add(bicluster);

				 bicl=0;
			 }
			 bicl++;
			
		 }
		 
		 return res;
	}
	
	/**
	 * Import biclusters from txt file.
	 *
	 * @param expressiondatafile the expressiondatafile
	 * @param biclustersfilepath the biclustersfilepath
	 * @param delimiter the delimiter
	 * @param areindexes the areindexes
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclustersFromTxtFile(String expressiondatafile, String biclustersfilepath, String delimiter, boolean areindexes) throws IOException, ParseException, MissingValueImputationException{
		ExpressionData data=ExpressionData.importFromTXTFormat(expressiondatafile).load();
		return importBiclustersFromTxtFile(data, biclustersfilepath, delimiter, areindexes);
	}
	


	/**
	 * Import biclusters from biclust R package output format.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param biclustfiledelimiter the biclustfiledelimiter
	 * @param expressiondatafilepath the expressiondatafilepath
	 * @param expressionfiledelimiter the expressionfiledelimiter
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclustersFromBiclustRPackageOutputFormat(String biclustoutputfile, String biclustfiledelimiter, String expressiondatafilepath, String expressionfiledelimiter) throws IOException, ParseException, MissingValueImputationException{
		
		ExpressionData originaldata=null;
		
		if(expressiondatafilepath!=null){
			
			if(expressionfiledelimiter!=null)
				originaldata=ExpressionData.loadDatasetWithDelimiter(expressiondatafilepath, expressionfiledelimiter, null);
			else
				originaldata=ExpressionData.loadDataset(expressiondatafilepath, null);
			
		}
		return importBiclustersFromBiclustRPackageOutputFormat(biclustoutputfile, biclustfiledelimiter, originaldata);
	}	

	
	
	
	/**
	 * Import biclusters from biclust R package output format.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param delimiter the delimiter
	 * @param originaldatatset the originaldatatset
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterList importBiclustersFromBiclustRPackageOutputFormat(String biclustoutputfile, String delimiter,ExpressionData originaldatatset) throws IOException{
		
		
		if(delimiter==null)
			delimiter=" ";
	
		
		ArrayList<String> lines=(ArrayList<String>) MTUReadUtils.readFileLines(biclustoutputfile);
		if(lines.size()<3){
			throw new IOException("Invalid biclust results file");
		}
		else{
			String method="unknown";
			int b=1;
			String header=lines.get(0);
			String pattern="Bicluster results using (\\w+) saved";
			Pattern pat=Pattern.compile(pattern);
			Matcher m=pat.matcher(header);
			if(m.find()){
				method=m.group(1);	
			}
			
			if(header.matches("\\d+\\s+\\d+"))
				b=0;
			
			BiclusterList res=new BiclusterList("Imported Biclusters");
			
			for (int i = b; i < lines.size(); i+=3) {
				
				ArrayList<String> genes=(ArrayList<String>) MTUStringUtils.getElementsListFromString(lines.get(i+1), delimiter, new ArrayList<String>());
				ArrayList<String> conds=(ArrayList<String>) MTUStringUtils.getElementsListFromString(lines.get(i+2), delimiter, new ArrayList<String>());
				BiclusterResult bic=BiclusterResult.createBiclusterResultContainer(genes, conds);
				res.add(bic);
				if(originaldatatset!=null){
					bic.setAssociatedExpressionData(originaldatatset);
					bic.loadGeneIndexesFromGeneNames();
					bic.loadConditionIndexesFromConditionsNames();
				}
			}
			
			if(originaldatatset!=null)
				res.setAnalysedDataset(originaldatatset);
			
			res.setUsedmethod(method);

			return res;
		}	
		
	}
	
	
	/**
	 * Read biclusters with R package output format and gene vs chip tags.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param data the data
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterList readBiclustersWithRPackageOutputFormatAndGeneVsChipTags(String biclustoutputfile, ExpressionData data) throws IOException{
		return readBiclustersWithRPackageOutputFormatAndGeneVsChipTags(biclustoutputfile, null, data);
	}
	
	/**
	 * Read biclusters with R package output format and gene vs chip tags.
	 *
	 * @param biclustoutputfile the biclustoutputfile
	 * @param delimiter the delimiter
	 * @param data the data
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterList readBiclustersWithRPackageOutputFormatAndGeneVsChipTags(String biclustoutputfile, String delimiter,ExpressionData data) throws IOException{
		if(data!=null){
			
			if(delimiter==null)
				delimiter=" ";
			
			ArrayList<String> lines=(ArrayList<String>) MTUReadUtils.readFileLines(biclustoutputfile);
			if(lines.size()<3){
				throw new IOException("Invalid biclust results file");
			}
			else{
				
				int b=1;
				String header=lines.get(0);
				if(header.matches("\\d+\\s+\\d+"))
					b=0;
				
				BiclusterList res=new BiclusterList("Imported Biclusters");
				
				for (int i = b; i < lines.size(); i+=3) {
					
					ArrayList<String> genes=(ArrayList<String>) MTUStringUtils.getElementsListFromString(lines.get(i+1), delimiter, new ArrayList<String>());
					ArrayList<String> conds=(ArrayList<String>) MTUStringUtils.getElementsListFromString(lines.get(i+2), delimiter, new ArrayList<String>());
					BiclusterResult bic=BiclusterResult.createBiclusterResultContainer(genes, conds);
					bic.setAssociatedExpressionData(data);
					bic.parseGeneAndChipTagsToNames();
					res.add(bic);	
				}
				
				res.setAnalysedDataset(data);

				return res;
			}
			
		}
		else
			throw new IOException("Expression dataset is [null] and it is necessary To import biclusters from file");
	}
	
	
	
	/**
	 * Write bicluster list to biclust R package format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @param extension the extension
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public synchronized void writeBiclusterListToBiclustRPackageFormat(String directory, String filename,String extension) throws IOException{
		StringBuilder str=new StringBuilder();
		//str.append("Bicluster results using "+usedmethod+" saved at: "+MTUTimeUtils.getCurrentDateAndTime()+"\n");
		if(nameoflist!=null && usedmethod==null)
			str.append(nameoflist+"\n");
		else
			str.append("Bicluster results using "+usedmethod+"\n");
		
		for (int i = 0; i < size(); i++) {
			str.append(get(i).getBiclustPackageOutpuFormat());
		}
		
		String filepath=null;
		if(filename!=null){
			if(extension==null)
				filepath=MTUFileUtils.addExtensionToFilepathIfNotExists(directory, filename, "txt");
			else
				filepath=FilenameUtils.concat(directory, filename+"."+extension);
			
		}
		else
			filepath=FilenameUtils.concat(directory,"results_"+usedmethod+"_"+MTUTimeUtils.getCurrentDateAndTime("yyyy-MM-dd_HH:mm:ss"));
		
		MTUDirUtils.checkDirectory(directory);
		MTUWriterUtils.writeStringWithFileChannel(str.toString(), filepath, 0);
	}
	
	
	/**
	 * Write bicluster list to biclust R package format.
	 *
	 * @param directory the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToBiclustRPackageFormat(String directory) throws IOException{
		writeBiclusterListToBiclustRPackageFormat(directory, null,null);
	}
	
	
	/**
	 * Write bicluster list to bic overlapper output format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToBicOverlapperOutputFormat(String directory, String filename) throws IOException{
		StringBuilder str=new StringBuilder();
		str.append(size()+"\n");
		str.append("Bicluster results using "+usedmethod+"\n");
		for (int i = 0; i < size(); i++) {
			str.append("B"+(i+1)+":"+get(i).getNumberGenes()+"\t"+get(i).getNumberConditions()+"\n");
			str.append(get(i).getBicOverlapperOutputFormat());
		}
		
		String filepath=null;
		if(filename!=null)
				filepath=FilenameUtils.concat(directory, filename+"."+"bic");
		else
			filepath=FilenameUtils.concat(directory,"results_"+usedmethod+"_"+MTUTimeUtils.getCurrentDateAndTime("yyyy-MM-dd_HH:mm:ss"+".bic"));
		
		MTUDirUtils.checkDirectory(directory);
		MTUWriterUtils.writeStringWithFileChannel(str.toString(), filepath, 0);
	}
	
	
	/**
	 * Write bicluster list to bic overlapper output format.
	 *
	 * @param directory the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToBicOverlapperOutputFormat(String directory) throws IOException{
		writeBiclusterListToBicOverlapperOutputFormat(directory, null);
	}
	
	
	/**
	 * Write bicluster list to J biclust GE output format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJBiclustGEOutputFormat(String directory, String filename) throws IOException{
		
		String bicsinfo=getBiclusterListAsString();
		
		String filepath=null;
		if(filename!=null){
			filepath=MTUFileUtils.forceFilePathExtension(directory, filename, "bicge");
		}
		else
			filepath=FilenameUtils.concat(directory,"Biclusters_"+usedmethod+"_"+MTUTimeUtils.getCurrentDateAndTime("yyyy-MM-dd_HH-mm-ss")+".bicge");
		
		MTUDirUtils.checkDirectory(directory);
		MTUWriterUtils.writeStringWithFileChannel(bicsinfo, filepath, 0);
	}
	

	/**
	 * Write bicluster list to J biclust GE output format.
	 *
	 * @param directory the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJBiclustGEOutputFormat(String directory) throws IOException{
		writeBiclusterListToJBiclustGEOutputFormat(directory, BiclusterOutputFormat.JBiclustGE_CSV.getName());
	}
	
	
	/**
	 * Gets the bicluster list as string.
	 *
	 * @return the bicluster list as string
	 */
	public String getBiclusterListAsString(){
		StringBuilder str=new StringBuilder();
		str.append("Total_Biclusters:"+size()+"\n");
		if(usedmethod!=null)
			str.append("Method_used:"+usedmethod+"\n");
		if(getMethodRunningTime()!=null)
			str.append("Running_Time:"+getMethodRunningTime()+"\n");
	
		str.append("\n");
		for (int i = 0; i < size(); i++) {
			str.append(getSingleBiclusterInformation(get(i), i));
			if(i<(size()-1))
				str.append("\n\n");
		}
		return str.toString();
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
		str.append("Number_of_Genes:"+bicres.getNumberGenes()+"\n");
		str.append("Number_of_Conditions:"+bicres.getNumberConditions()+"\n");
		str.append("Gene_names:\t");
		for (int i = 0; i < bicres.getNumberGenes(); i++) {
			str.append(bicres.getGeneNames().get(i));
			if(i<bicres.getNumberGenes()-1)
				str.append("\t");
		}
		str.append("\n");
		
		str.append("Gene_indexes:\t");
		for (int i = 0; i < bicres.getNumberGenes(); i++) {
			str.append(bicres.getGeneIndexes().get(i));
			if(i<bicres.getNumberGenes()-1)
				str.append("\t");
		}
		str.append("\n");
		
		str.append("Condition_names:\t");
		for (int i = 0; i < bicres.getNumberConditions(); i++) {
			str.append(bicres.getConditionNames().get(i));
			if(i<bicres.getNumberConditions()-1)
				str.append("\t");
		}
		str.append("\n");
		
		str.append("Condition_indexes:\t");
		for (int i = 0; i < bicres.getNumberConditions(); i++) {
			str.append(bicres.getConditionIndexes().get(i));
			if(i<bicres.getNumberConditions()-1)
				str.append("\t");
		}
		str.append("\n");
		
		
		return str.toString();
	}
	
	
	/**
	 * Load bicluster list from J biclust GE format.
	 *
	 * @param filepath the filepath
	 * @param originaldata the originaldata
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterList loadBiclusterListFromJBiclustGEFormat(String filepath, ExpressionData originaldata) throws IOException{
		
		
		ArrayList<String> lines=MTUReadUtils.readFileLinesRemoveEmptyLines(filepath);
		BiclusterList listres=new BiclusterList();
		
		ArrayList<String> genenames=null;
		ArrayList<String> condnames=null;
		ArrayList<Integer> geneindexes=null;
		ArrayList<Integer> condindexes=null;
		
		String method=null;
		
		for (int i = 0; i < lines.size(); i++) {
			String line=lines.get(i);
			if(line.contains("Bicluster")){
				genenames=new ArrayList<>();
				condnames=new ArrayList<>();
				geneindexes=new ArrayList<>();
				condindexes=new ArrayList<>();
			}
			else if(line.contains("Method_used:")){
				String[] m=line.split(":");
				if(m.length==2)
					method=m[1];
			}
			else if(line.contains("Gene_names:")){
				String[] gelems=line.split("\t");
				for (int j = 1; j < gelems.length; j++) {
					genenames.add(gelems[j]);
				}
			}
			else if(line.contains("Gene_indexes:")){
				String[] gindexes=line.split("\t");
				for (int j = 1; j < gindexes.length; j++) {
					Integer ind=Integer.parseInt(gindexes[j]);
					geneindexes.add(ind);
				}
			}
			else if(line.contains("Condition_names:")){
				String[] celems=line.split("\t");
				for (int j = 1; j < celems.length; j++) {
					condnames.add(celems[j]);
				}
			}
			else if(line.contains("Condition_indexes:")){
				String[] cindexes=line.split("\t");
				for (int j = 1; j < cindexes.length; j++) {
					Integer ind=Integer.parseInt(cindexes[j]);
					condindexes.add(ind);
				}
				
				BiclusterResult res=new BiclusterResult(genenames, condnames, geneindexes, condindexes);
				if(originaldata!=null)
					res.setAssociatedExpressionData(originaldata);
				listres.add(res);
			}
			
		}
		
		if(method!=null)
			listres.setUsedmethod(method);
		if(originaldata!=null)
			listres.setAnalysedDataset(originaldata);
		
		return listres;
	}
	
	/**
	 * Import bicluster list from J biclust GE format.
	 *
	 * @param filepath the filepath
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterList importBiclusterListFromJBiclustGEFormat(String filepath) throws IOException{
		return loadBiclusterListFromJBiclustGEFormat(filepath,null);
	}
	
	/**
	 * Import bicluster list from J biclust GE format.
	 *
	 * @param filepath the filepath
	 * @param originaldata the originaldata
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclusterListFromJBiclustGEFormat(String filepath, ExpressionData originaldata) throws IOException, ParseException, MissingValueImputationException{
		return loadBiclusterListFromJBiclustGEFormat(filepath, originaldata);
		
	}
	
	
	/**
	 * Import bicluster list from J biclust GE format.
	 *
	 * @param filepath the filepath
	 * @param originaldata the originaldata
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclusterListFromJBiclustGEFormat(String filepath, String originaldata) throws IOException, ParseException, MissingValueImputationException{
		ExpressionData data=null;
		if(originaldata!=null)
		   data=ExpressionData.loadDataset(originaldata, null);
		return loadBiclusterListFromJBiclustGEFormat(filepath, data);
		
	}
	
	/**
	 * Import bicluster list from J biclust GE format.
	 *
	 * @param filepath the filepath
	 * @param originaldata the originaldata
	 * @param imputationmethod the imputationmethod
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 * @throws MissingValueImputationException the missing value imputation exception
	 */
	public static BiclusterList importBiclusterListFromJBiclustGEFormat(String filepath, String originaldata, MissingValueImputation imputationmethod) throws IOException, ParseException, MissingValueImputationException{
		ExpressionData data=null;
		if(originaldata!=null)
			data=ExpressionData.loadDataset(originaldata, imputationmethod);
		return loadBiclusterListFromJBiclustGEFormat(filepath, data);
		
		
	}
	
	
	
	
	/**
	 * Sort bicluster list.
	 *
	 * @param biclist the biclist
	 * @param ascendingorder the ascendingorder
	 * @return the bicluster list
	 */
	public static BiclusterList sortBiclusterList(BiclusterList biclist, final boolean ascendingorder)
    {

       BiclusterList newlist = new BiclusterList(biclist);

        // Sorting the list based on values
        Collections.sort(newlist, new Comparator<BiclusterResult>()
        {
            public int compare(BiclusterResult o1,
            		BiclusterResult o2)
            {
                if (ascendingorder)
                {
                    return o1.compareTo(o2);
                }
                else
                {
                    return o2.compareTo(o1);

                }
            }
        });
        return newlist;
    }
	
	
	/**
	 * Load bicluster list from JSON file.
	 *
	 * @param filepath the filepath
	 * @return the bicluster list
	 * @throws FileNotFoundException the file not found exception
	 */
	public static BiclusterList imporBiclusterListFromJBiclustGEJSONFile(String filepath) throws FileNotFoundException{
		return loadBiclusterListFromJSONObject(MTUJsonIOUtils.readJsonFile(filepath));
		
	}
	
	/**
	 * Load bicluster list from JSON file.
	 *
	 * @param filepath the filepath
	 * @param data the data
	 * @return the bicluster list
	 * @throws FileNotFoundException the file not found exception
	 */
	public static BiclusterList imporBiclusterListFromJBiclustGEJSONFile(String filepath, ExpressionData data) throws FileNotFoundException{
		return loadBiclusterListFromJSONObject(MTUJsonIOUtils.readJsonFile(filepath), data);
		
	}
	
	
	/**
	 * Load bicluster list from JSON object.
	 *
	 * @param obj the obj
	 * @return the bicluster list
	 */
	public static BiclusterList loadBiclusterListFromJSONObject(JSONObject obj){
		return loadBiclusterListFromJSONObject(obj, null);
	}

	/**
	 * Load bicluster list from JSON object.
	 *
	 * @param obj the obj
	 * @param data the data
	 * @return the bicluster list
	 */
	public static BiclusterList loadBiclusterListFromJSONObject(JSONObject obj, ExpressionData data){
		BiclusterList biclist=new BiclusterList();
		JSONObject bics =(JSONObject) obj.get(BICLUSTERLISTTAG);
		
		if(obj.has(BICLUSTERINGMETHOD)){
		     String method=obj.getString(BICLUSTERINGMETHOD);
		     biclist.setUsedmethod(method);    
		}
		
		if(obj.has(BICLUSTERINGMETHODPARAMETERS)){
			LinkedHashMap<String, String> param=MTUJsonUtils.getJSonStringElemsLinkedHashMap(obj.getJSONObject(BICLUSTERINGMETHODPARAMETERS));
			biclist.setMethodRunningParameters(param);
		}
		
		if(obj.has(BICLUSTERINGADDITIONALINFO)){
			LinkedHashMap<String, Object> extrainfo=new LinkedHashMap<>(MTUJsonUtils.getJSONObjectElementsToMap(obj.getJSONObject(BICLUSTERINGADDITIONALINFO)));
			if(extrainfo!=null)
				biclist.setAditionalBiclusterMethodInformation(extrainfo);
		}
		

		if(data==null){
			if(obj.has(BICLUSTERLISTEXPRESSIONDATATAG)){
				LogMessageCenter.getLogger().addInfoMessage("loading gene expression data.");
				data=ExpressionData.getExpressionDatasetFromJSONOject(obj.getJSONObject(BICLUSTERLISTEXPRESSIONDATATAG));
				biclist.setAnalysedDataset(data);
			}
		}
		else
			biclist.setAnalysedDataset(data);
		
		TreeSet<Integer> orderedkeys=new TreeSet<>();
		for (String key : bics.keySet()) {
			orderedkeys.add(Integer.valueOf(key));
		}
	
		for (Integer intkey : orderedkeys) {
			JSONObject bicobj=bics.getJSONObject(String.valueOf(intkey));
			BiclusterResult bic=BiclusterResult.loadBiclusterResultFromJSONObject(bicobj);
			if(data!=null)
				bic.setAssociatedExpressionData(data);
			biclist.add(bic);
		}
		return biclist;
	}
	
	
	
	
	
	
	/**
	 * Convert bicluster list to JSON format.
	 *
	 * @param saveexpressiondataset the saveexpressiondataset
	 * @return the JSON object
	 */
	public JSONObject convertBiclusterListToJSONFormat(boolean saveexpressiondataset){
		
		JSONObject obj = new JSONObject();
		obj.put("Total", size());
		obj.put(BICLUSTERINGMETHOD, usedmethod);
		
		if(methodrunningparameters!=null){
			obj.put(BICLUSTERINGMETHODPARAMETERS, MTUJsonUtils.assembleJSONObjectFromStringLinkedHashMap(methodrunningparameters));
		}
		
		if(methodadditionalinfo!=null){
			obj.put(BICLUSTERINGADDITIONALINFO, MTUJsonUtils.assembleJSONObjectFromKStringVObjectLinkedHashMap(methodadditionalinfo));
		}
		
		JSONObject biclustersobj=new JSONObject();
		
		for (int i = 0; i < size(); i++) {
			biclustersobj.put(String.valueOf(i), get(i).getJSONInformation());
		}
		obj.put(BICLUSTERLISTTAG, biclustersobj);
		

		if(saveexpressiondataset && datasetanalysed!=null)
		   obj.put(BICLUSTERLISTEXPRESSIONDATATAG, datasetanalysed.getDatasetJSONFormat());
		
		return obj;
	}
	
	/**
	 * Convert bicluster list to JSON format.
	 *
	 * @return the JSON object
	 */
	public JSONObject convertBiclusterListToJSONFormat(){
		return convertBiclusterListToJSONFormat(false);
	}
	
	/**
	 * Write bicluster list to JSON format.
	 *
	 * @param filepath the filepath
	 * @param indentation the indentation
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJSONFormat(String filepath, int indentation) throws IOException{
		JSONObject towrite=convertBiclusterListToJSONFormat();
		MTUJsonIOUtils.writeJSONObjectToFilePrettyFormat(towrite, filepath, indentation);
	}
	
	/**
	 * Write bicluster list to JSON format.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJSONFormat(String filepath) throws IOException{
		JSONObject towrite=convertBiclusterListToJSONFormat();
		MTUJsonIOUtils.writeJSONObjectToFilePrettyFormat(towrite, filepath, 5);
	}
	
	/**
	 * Write bicluster list to JSON format.
	 *
	 * @param filepath the filepath
	 * @param appendexpressiondataset the appendexpressiondataset
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJSONFormat(String filepath,boolean appendexpressiondataset) throws IOException{
		JSONObject towrite=convertBiclusterListToJSONFormat(appendexpressiondataset);
		MTUJsonIOUtils.writeJSONObjectToFilePrettyFormat(towrite, filepath, 5);
	}
	
	/**
	 * Write bicluster list to JSON format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @param indentation the indentation
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJSONFormat(String directory, String filename, int indentation) throws IOException{
		JSONObject towrite=convertBiclusterListToJSONFormat();
		MTUJsonIOUtils.writeJSONObjectToFilePrettyFormat(towrite, directory, filename, indentation);
	}
	

	/**
	 * Write bicluster list to JSON format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJSONFormat(String directory, String filename) throws IOException{
		JSONObject towrite=convertBiclusterListToJSONFormat();
		MTUJsonIOUtils.writeJSONObjectToFilePrettyFormat(towrite, directory, filename, 5);
	}
	
	/**
	 * Write bicluster list to JSON format.
	 *
	 * @param directory the directory
	 * @param filename the filename
	 * @param appendexpressiondataset the appendexpressiondataset
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBiclusterListToJSONFormat(String directory, String filename, boolean appendexpressiondataset) throws IOException{
		JSONObject towrite=convertBiclusterListToJSONFormat(appendexpressiondataset);
		if(towrite!=null && directory!=null && filename!=null){
			String filepath= MTUFileUtils.buildFilePathWithExtension(directory, filename, "json");
			FileWriter w =new FileWriter(filepath);
			towrite.write(w);
			//MTUJsonIOUtils.writeJSONObjectToFilePrettyFormat(towrite, directory, filename, 5);
		}
	}
	
	
	/**
	 * Export bicluster list.
	 *
	 * @param dir the dir
	 * @param filename the filename
	 * @param exportformat the exportformat
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void exportBiclusterList(String dir, String filename, BiclusterOutputFormat exportformat) throws IOException{
		
		if(exportformat.equals(BiclusterOutputFormat.JBiclustGE_CSV)){
			writeBiclusterListToJBiclustGEOutputFormat(dir, filename);
		}
		else if(exportformat.equals(BiclusterOutputFormat.JBiclustGE_JSON)){
			writeBiclusterListToJSONFormat(dir, filename);
		}
		else if(exportformat.equals(BiclusterOutputFormat.BicOverlapper)){
			writeBiclusterListToBicOverlapperOutputFormat(dir, filename);
		}
		else
			writeBiclusterListToBiclustRPackageFormat(dir, filename, "txt");
	}
	
	
	public void removeExpressionDatasetDependencies() {
		setAnalysedDataset(null);
		
		for (int i = 0; i < size(); i++) {
			BiclusterResult res=get(i);
			res.setAssociatedExpressionData(null);
		}
	}
	
	public void addExpressionDatasetDependencies(ExpressionData dataset) {
		setAnalysedDataset(dataset);
		
		for (int i = 0; i < size(); i++) {
			BiclusterResult res=get(i);
			res.setAssociatedExpressionData(dataset);
		}
	}
	
	public BiclusterList getCloneWithoutExpressionDataAssociation() throws Exception {
		
		BiclusterList clone=new BiclusterList(nameoflist, 
											usedmethod, 
											methodrunningtime, 
											(LinkedHashMap<String, String>) MTUCollectionsUtils.deepCloneObject(methodrunningparameters), 
											(LinkedHashMap<String, Object>) MTUCollectionsUtils.deepCloneObject(methodadditionalinfo));
		
		for (int i = 0; i < size(); i++) {
			clone.add(get(i).getCloneWithoutExpressionDataAssociation());
		}
		
		return clone;
	}
	
	public ArrayList<String> getAllGenesOfBiclusters(){
		HashSet<String> currentgenes=new HashSet<>();
		
		for (int i = 0; i < size(); i++) {
			currentgenes.addAll(get(i).getGeneNames());
		}
		return new ArrayList<>(currentgenes);
	}
	
	public ArrayList<String> getAllConditionsOfBiclusters(){
		HashSet<String> currentconditions=new HashSet<>();
		
		for (int i = 0; i < size(); i++) {
			currentconditions.addAll(get(i).getConditionNames());
		}
		return new ArrayList<>(currentconditions);
	}

}
