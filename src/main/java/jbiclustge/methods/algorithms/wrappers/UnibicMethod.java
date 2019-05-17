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
package jbiclustge.methods.algorithms.wrappers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.processdata.binarization.IDiscretizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.props.AlgorithmProperties;
import jbiclustge.utils.props.CommandsProcessList;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class UnibicMethod.
 */
public class UnibicMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	
	/** The Constant NAME. */
	public static final String NAME="UniBic";
	
	/** The Constant UNIBICPVALUE. */
	public static final String UNIBICPVALUE="unibic_pvalue"; 
	
	
	/** The Constant QUANTILEDISCRETIZATION. */
	/*
	 * input parameters
	 */
	public static final String QUANTILEDISCRETIZATION="quantile_discretization_percentage";
	
	/** The Constant RANKLEVELS. */
	public static final String RANKLEVELS="number_rank_levels";
	
	/** The Constant DISCRETEDATA. */
	public static final String DISCRETEDATA="discrete_data";
	
	/** The Constant TFNAMETOSEARCH. */
	public static final String TFNAMETOSEARCH="tf_name_to_be_searched";
	
	/** The Constant ENLARGEBICLUSTERFLAG. */
	public static final String ENLARGEBICLUSTERFLAG="flag_to_enlarge_bicluster_by_pvalue_constrain";
	
	/** The Constant STOPBICLUSTERBYAREAFLAG. */
	public static final String STOPBICLUSTERBYAREAFLAG="flag_using_area_of_bicluster_to_determine_when_stop";
	
	/** The Constant LOWERBOUNDCONDITIONFLAG. */
	public static final String LOWERBOUNDCONDITIONFLAG="flag_using_the_lower_bound_of_condition_number";
	
	/** The Constant BLOCKSTOREPORT. */
	public static final String BLOCKSTOREPORT="number_of_blocks_to_report";
	
	/** The Constant FILTEROVERLAPBLOCKS. */
	public static final String FILTEROVERLAPBLOCKS="filtering_overlapping_blocks";
	
	/** The Constant MINCOLUMNWITH. */
	public static final String MINCOLUMNWITH="minimum_column_width_block";
	
	/** The Constant CONSISTENCYLEVELBLOCK. */
	public static final String CONSISTENCYLEVELBLOCK="consistency_level_of_the_block";
	
	/** The Constant EXPANSIONFLAG. */
	public static final String EXPANSIONFLAG="expansion_flag";
	
	/** The binarizationmethod. */
	private IDiscretizationMethod binarizationmethod;
	
	/** The datafilepath. */
	protected String datafilepath=null;
	
	/** The cmds. */
	protected CommandsProcessList cmds;
	
	/** The quantilediscretization. */
	// input parameters
	private double quantilediscretization=0.5; // (0-0.5]
	
	/** The numberranks. */
	private int numberranks=15;
	
	/** The discretedata. */
	private boolean discretedata=false;
	
	/** The filetoexpand. */
	private String filetoexpand=null;
	
	/** The T ftosearch. */
	private String TFtosearch=null;
	
	/** The enlargebicluster. */
	private boolean enlargebicluster=false;
	
	/** The useareatostop. */
	private boolean useareatostop=false;
	
	/** The uselowerboundofconditions. */
	private boolean uselowerboundofconditions=false;
	
	/** The numberblocks. */
	// output parameters
	private int numberblocks=100;
	
	/** The filteroverlapblocks. */
	private double filteroverlapblocks=1.0; // [0-1.0]
	
	/** The mincolumnwith. */
	private int mincolumnwith=4; // >=2
	
	/** The consistencylevel. */
	private double consistencylevel=0.85; // (0.5-1.0]
	
	/** The expand. */
	private boolean expand=false;
	
	/** The executablefilepath. */
	private String executablefilepath=null;
	
	/** The tmpdir. */
	private String tmpdir=null;
	
	private Thread stop;
	
	/**
	 * Instantiates a new unibic method.
	 */
	public UnibicMethod() {
		super();
	}


	/**
	 * Instantiates a new unibic method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public UnibicMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}


	/**
	 * Instantiates a new unibic method.
	 *
	 * @param exprs the exprs
	 */
	public UnibicMethod(ExpressionData exprs) {
		super(exprs);
	}
	
    /**
     * Instantiates a new unibic method.
     *
     * @param properties the properties
     */
    public UnibicMethod(Properties properties){
	    	super(properties);
	    }
    
    /**
     * Instantiates a new unibic method.
     *
     * @param propertiesfile the propertiesfile
     */
    public UnibicMethod(String propertiesfile){
    	super(propertiesfile);
    }
	
	/**
	 * Instantiates a new unibic method.
	 *
	 * @param exprs the exprs
	 * @param discretemethod the discretemethod
	 */
	public UnibicMethod(ExpressionData exprs, IDiscretizationMethod discretemethod) {
		this(exprs);
		this.binarizationmethod=discretemethod;
		this.discretedata=true;
	}
	
	
	
	/**
	 * Instantiates a new unibic method.
	 *
	 * @param binarizationmethod the binarizationmethod
	 * @param quantilediscretization the quantilediscretization
	 * @param numberranks the numberranks
	 * @param discretedata the discretedata
	 * @param filetoexpand the filetoexpand
	 * @param tFtosearch the t ftosearch
	 * @param enlargebicluster the enlargebicluster
	 * @param useareatostop the useareatostop
	 * @param uselowerboundofconditions the uselowerboundofconditions
	 * @param numberblocks the numberblocks
	 * @param filteroverlapblocks the filteroverlapblocks
	 * @param mincolumnwith the mincolumnwith
	 * @param consistencylevel the consistencylevel
	 * @param expand the expand
	 */
	private UnibicMethod(IDiscretizationMethod binarizationmethod, double quantilediscretization, int numberranks,
			boolean discretedata, String filetoexpand, String tFtosearch, boolean enlargebicluster,
			boolean useareatostop, boolean uselowerboundofconditions, int numberblocks, double filteroverlapblocks,
			int mincolumnwith, double consistencylevel, boolean expand) {
		super();
		this.binarizationmethod = binarizationmethod;
		this.quantilediscretization = quantilediscretization;
		this.numberranks = numberranks;
		this.discretedata = discretedata;
		this.filetoexpand = filetoexpand;
		TFtosearch = tFtosearch;
		this.enlargebicluster = enlargebicluster;
		this.useareatostop = useareatostop;
		this.uselowerboundofconditions = uselowerboundofconditions;
		this.numberblocks = numberblocks;
		this.filteroverlapblocks = filteroverlapblocks;
		this.mincolumnwith = mincolumnwith;
		this.consistencylevel = consistencylevel;
		this.expand = expand;
	}


	/**
	 * Sets the discretization method.
	 *
	 * @param discretemethod the new discretization method
	 */
	public void setDiscretizationMethod(IDiscretizationMethod discretemethod){
		this.binarizationmethod=discretemethod;
		this.discretedata=true;
	}
	
	/**
	 * Adds the discretization method.
	 *
	 * @param discretemethod the discretemethod
	 * @return the unibic method
	 */
	public UnibicMethod addDiscretizationMethod(IDiscretizationMethod discretemethod){
		this.binarizationmethod=discretemethod;
		this.discretedata=true;
		return this;
	}
	
	/**
	 * Sets the number biclusters to find.
	 *
	 * @param numberblocks the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int numberblocks) {
		this.numberblocks = numberblocks;
	}
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param numberblocks the numberblocks
	 * @return the unibic method
	 */
	public UnibicMethod addNumberBiclustersToFind(int numberblocks) {
		this.numberblocks = numberblocks;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportDefineNumberBiclustersToFind()
	 */
	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		return true;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#changeNumberBiclusterToFind(int)
	 */
	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		setNumberBiclustersToFind(numberbics);
	}

	/**
	 * Sets the filter overlap blocks.
	 *
	 * @param filteroverlapblocks the new filter overlap blocks
	 */
	public void setFilterOverlapBlocks(double filteroverlapblocks) {
		this.filteroverlapblocks = filteroverlapblocks;
	}
	
	/**
	 * Adds the filter overlap blocks.
	 *
	 * @param filteroverlapblocks the filteroverlapblocks
	 * @return the unibic method
	 */
	public UnibicMethod addFilterOverlapBlocks(double filteroverlapblocks) {
		this.filteroverlapblocks = filteroverlapblocks;
		return this;
	}


	/**
	 * Sets the min column with.
	 *
	 * @param mincolumnwith the new min column with
	 */
	public void setMinColumnWith(int mincolumnwith) {
		this.mincolumnwith = mincolumnwith;
	}

	/**
	 * Adds the min column with.
	 *
	 * @param mincolumnwith the mincolumnwith
	 * @return the unibic method
	 */
	public UnibicMethod addMinColumnWith(int mincolumnwith) {
		this.mincolumnwith = mincolumnwith;
		return this;
	}

	/**
	 * Sets the consistency level.
	 *
	 * @param consistencylevel the new consistency level
	 */
	public void setConsistencyLevel(double consistencylevel) {
		this.consistencylevel = consistencylevel;
	}
	
	/**
	 * Adds the consistency level.
	 *
	 * @param consistencylevel the consistencylevel
	 * @return the unibic method
	 */
	public UnibicMethod addConsistencyLevel(double consistencylevel) {
		this.consistencylevel = consistencylevel;
		return this;
	}


	/**
	 * Sets the quantile discretization.
	 *
	 * @param quantilediscretization the new quantile discretization
	 */
	public void setQuantileDiscretization(double quantilediscretization) {
		this.quantilediscretization = quantilediscretization;
	}

	/**
	 * Adds the quantile discretization.
	 *
	 * @param quantilediscretization the quantilediscretization
	 * @return the unibic method
	 */
	public UnibicMethod addQuantileDiscretization(double quantilediscretization) {
		this.quantilediscretization = quantilediscretization;
		return this;
	}

	/**
	 * Sets the number ranks.
	 *
	 * @param numberranks the new number ranks
	 */
	public void setNumberRanks(int numberranks) {
		this.numberranks = numberranks;
	}
	
	/**
	 * Adds the number ranks.
	 *
	 * @param numberranks the numberranks
	 * @return the unibic method
	 */
	public UnibicMethod addNumberRanks(int numberranks) {
		this.numberranks = numberranks;
		return this;
	}


	/**
	 * Sets the t fto search.
	 *
	 * @param tFtosearch the new t fto search
	 */
	public void setTFtoSearch(String tFtosearch) {
		TFtosearch = tFtosearch;
	}
	
	/**
	 * Adds the T fto search.
	 *
	 * @param tFtosearch the t ftosearch
	 * @return the unibic method
	 */
	public UnibicMethod addTFtoSearch(String tFtosearch) {
		TFtosearch = tFtosearch;
		return this;
	}


	/**
	 * Sets the enlarge bicluster.
	 *
	 * @param enlargebicluster the new enlarge bicluster
	 */
	public void setEnlargeBicluster(boolean enlargebicluster) {
		this.enlargebicluster = enlargebicluster;
	}
	
	/**
	 * Adds the enlarge bicluster.
	 *
	 * @param enlargebicluster the enlargebicluster
	 * @return the unibic method
	 */
	public UnibicMethod addEnlargeBicluster(boolean enlargebicluster) {
		this.enlargebicluster = enlargebicluster;
		return this;
	}


	/**
	 * Sets the use areato stop.
	 *
	 * @param useareatostop the new use areato stop
	 */
	public void setUseAreatoStop(boolean useareatostop) {
		this.useareatostop = useareatostop;
	}

	/**
	 * Adds the use areato stop.
	 *
	 * @param useareatostop the useareatostop
	 * @return the unibic method
	 */
	public UnibicMethod addUseAreatoStop(boolean useareatostop) {
		this.useareatostop = useareatostop;
		return this;
	}

	/**
	 * Sets the use lower boundof conditions.
	 *
	 * @param uselowerboundofconditions the new use lower boundof conditions
	 */
	public void setUseLowerBoundofConditions(boolean uselowerboundofconditions) {
		this.uselowerboundofconditions = uselowerboundofconditions;
	}
	
	/**
	 * Adds the use lower boundof conditions.
	 *
	 * @param uselowerboundofconditions the uselowerboundofconditions
	 * @return the unibic method
	 */
	public UnibicMethod addUseLowerBoundofConditions(boolean uselowerboundofconditions) {
		this.uselowerboundofconditions = uselowerboundofconditions;
		return this;
	}
	
	/**
	 * Sets the expand.
	 *
	 * @param useexpansionflag the new expand
	 */
	public void setExpand(boolean useexpansionflag) {
		this.expand = useexpansionflag;
	}
	
	/**
	 * Adds the expand.
	 *
	 * @param useexpansionflag the useexpansionflag
	 * @return the unibic method
	 */
	public UnibicMethod addExpand(boolean useexpansionflag) {
		this.expand = useexpansionflag;
		return this;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return NAME.toLowerCase();
	}
	


	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryExecutablePath()
	 */
	@Override
	public String getBinaryExecutablePath() throws IOException {
		String path=SystemFolderTools.getBinaryFilePathFromPropertiesFile(getBinaryName());
        if(path==null)
        	path=SystemFolderTools.getMethodExePath(getBinaryName());
		return path;
	}

	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@SuppressWarnings("resource")
	@Override
	protected  boolean runAlgorithm() throws Exception {
		
		 
		    preconfigurealgorithm();
		    ProcessBuilder build= new ProcessBuilder(cmds);
		    build.directory(new File(tmpdir));
		    Instant start = Instant.now();
			final Process p =build.start();
			InputStream inputstr =p.getInputStream();
			ReusableInputStream errorstr =new ReusableInputStream(p.getErrorStream());
			
			
			Thread stdout=new Thread(new GeneralProcessProgressionChecker(inputstr,getClass()));
			stdout.start();
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				  @Override
				  public void run() {
				    p.destroy();
				    SystemFolderTools.deleteTempDir(tmpdir);
				  }
				});
			
			stop=new Thread(new Runnable() {
				
				@Override
				public void run() {
					 p.destroy();
					 SystemFolderTools.deleteTempDir(tmpdir);
					
				}
			});
			

			int exitval=p.waitFor();
			if(exitval==0){
				saveElapsedTime(start);
				return true;
				
			}
			else{
				String out =IOUtils.toString(errorstr);
				LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": "+out);
				return false;
			}
	
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected  void processResults() throws Exception {

		this.listofbiclusters=new BiclusterList();
		
		
		LinkedHashMap<String, ArrayList<String>> unibicclusters=filterClusters(datafilepath);
		
		for (Map.Entry<String, ArrayList<String>> biclustermap : unibicclusters.entrySet()) {
			BiclusterResult biclustelem=getBiclusterResultData(biclustermap.getKey(), biclustermap.getValue());
			listofbiclusters.add(biclustelem);
			
		}
		
		SystemFolderTools.deleteTempDir(tmpdir);
		//deleteTempFiles();
		
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}
	
	
	/**
	 * Configure input data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void configureInputData() throws IOException{
		tmpdir=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("UNIBIC");
		executablefilepath=FilenameUtils.concat(tmpdir, (OSystemUtils.isWindows() ? getBinaryName()+".exe" : getBinaryName()));

		MTUFileUtils.copyFile(getBinaryExecutablePath(), executablefilepath);
		datafilepath=FilenameUtils.concat(tmpdir, MTUStringUtils.shortUUID()+".txt");
		//datafilepath=FilenameUtils.concat(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()+".txt");
		
		if(binarizationmethod!=null)
			expressionset.writeExpressionDatasetLabeledFormatToFile(datafilepath, binarizationmethod);
			//expressionset.writeExpressionDatasetToFile(datafilepath, binarizationmethod);
		else
			expressionset.writeExpressionDatasetLabeledFormatToFile(datafilepath);
			//expressionset.writeExpressionDatasetToFile(datafilepath);
	}
	
	/**
	 * Preconfigurealgorithm.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void preconfigurealgorithm() throws IOException{
		configureInputData();
		cmds=new CommandsProcessList();
		cmds.add(executablefilepath);
		
		String inputfile=null;
		if(OSystemUtils.isWindows())
			inputfile=datafilepath;
		else
			inputfile=FilenameUtils.getName(datafilepath);
		cmds.insertStringParameter("-i", inputfile);
		
		
		if(mincolumnwith>=2){
			cmds.insertIntegerParameter("-k", mincolumnwith);
		}
		if(filteroverlapblocks>=0.0 && filteroverlapblocks<1.0){
			cmds.insertDoubleParameter("-f", filteroverlapblocks);
		}
		if(consistencylevel>0.5 && consistencylevel!=0.85 && consistencylevel<=1.0){
			cmds.insertDoubleParameter("-c", consistencylevel);
		}
		if(numberblocks>0 && numberblocks!=100){
			cmds.insertIntegerParameter("-o", numberblocks);
		}
		if(quantilediscretization>0 && quantilediscretization!=0.5 && quantilediscretization<0.5 && !discretedata){
			cmds.insertDoubleParameter("-q", quantilediscretization);
		}
		if(numberranks>0 && numberranks!=15 && !discretedata){
			cmds.insertIntegerParameter("-r", numberranks);
		}
		if(discretedata){
			cmds.add("-d");
		}
		if(TFtosearch!=null){
			cmds.insertStringParameter("-T", getLabeledTFtoSearch());
		}
		if(enlargebicluster){
			cmds.add("-P");
		}
		if(useareatostop){
			cmds.add("-S");
		}
		if(uselowerboundofconditions){
			cmds.add("-C");
		}
		if(expand)
			cmds.add("-s");
		if(filetoexpand!=null)
			cmds.insertStringParameter("-b", filetoexpand);
		
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	private String getLabeledTFtoSearch() {
		return expressionset.transformToGeneLabel(TFtosearch);
	}
	
	
	/**
	 * Filter clusters.
	 *
	 * @param datafilepath the datafilepath
	 * @return the linked hash map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static LinkedHashMap<String, ArrayList<String>> filterClusters(String datafilepath) throws IOException{
		ArrayList<String> cachedoc=(ArrayList<String>) FileUtils.readLines(new File(datafilepath+".blocks"));

		LinkedHashMap<String, ArrayList<String>> filteredclusters=new LinkedHashMap<>();
		
		ArrayList<String> currentcluster=null;
		String clusterheader=null;
		
		for (int i = 0; i < cachedoc.size(); i++) {
			 String currentline=cachedoc.get(i);
		     if(currentline.matches("BC\\d+\\tS=(.*)") || (i==(cachedoc.size()-1))){
				if(currentcluster!=null && clusterheader!=null){
					filteredclusters.put(clusterheader, currentcluster);
				}
				currentcluster=new ArrayList<>();
				clusterheader=currentline;
			 }
		     if(clusterheader!=null && currentline!=clusterheader && !currentline.isEmpty() )
		    	 currentcluster.add(currentline);
		}
		
		return filteredclusters;
	}
	
	
	/**
	 * Gets the bicluster result data.
	 *
	 * @param header the header
	 * @param results the results
	 * @return the bicluster result data
	 * @throws IOException 
	 */
	protected BiclusterResult getBiclusterResultData(String header, ArrayList<String> results) throws IOException{
		
		ArrayList<String> geneids=getObjectList(results.get(0),2);
		ArrayList<String> condids=getObjectList(results.get(1),2);
		
		//BiclusterResult resultscontainer=new BiclusterResult(expressionset, geneids, condids);
		BiclusterResult resultscontainer=new BiclusterResult(expressionset,true,geneids, condids);
		String pval=filterpvalue(header);
		if(pval!=null)
			resultscontainer.appendAdditionalInfo(UNIBICPVALUE, pval);
		
		Object[][] auxinfo=new Object[geneids.size()][condids.size()];
		
		for (int i = 2; i < results.size(); i++) {
			ArrayList<String> matrixreg=getObjectList(results.get(i), 1);
			for (int j = 0; j < matrixreg.size(); j++) {
				auxinfo[i-2][j]=Integer.parseInt(matrixreg.get(j));
			}
		}
		
		resultscontainer.setadditionalresultmatrix(auxinfo);
		return resultscontainer;
	}
	
	/**
	 * Gets the object list.
	 *
	 * @param line the line
	 * @param ignorefirst the ignorefirst
	 * @return the object list
	 */
	protected ArrayList<String> getObjectList(String line, int ignorefirst){
       
		ArrayList<String> res=new ArrayList<>();
		
		String[] elems=line.trim().split("\\s+");
		for (int i = ignorefirst; i < elems.length; i++) {
			
			res.add(elems[i]);
		}
		
        return res;
	}
	
	/**
	 * Filterpvalue.
	 *
	 * @param header the header
	 * @return the string
	 */
	protected String filterpvalue(String header){
		String pat="Pvalue:(\\d+([\\.-]\\w+)*)";
		Pattern tomatch=Pattern.compile(pat);
		
		Matcher m=tomatch.matcher(header);
		if(m.find()){
			String match=m.group(1);
			return match;
		}
		
		return null;
	}


	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.quantilediscretization=PropertiesUtilities.getDoublePropertyValueValidLimits(props, QUANTILEDISCRETIZATION, 0.5, 0, false, 1.0, true, this.getClass());
		this.numberranks=PropertiesUtilities.getIntegerPropertyValue(props, RANKLEVELS, 15, this.getClass());
		this.discretedata=PropertiesUtilities.getBooleanPropertyValue(props, DISCRETEDATA, false, this.getClass());
        this.TFtosearch=PropertiesUtilities.getStringPropertyValue(props, TFNAMETOSEARCH, null, getClass());
        this.enlargebicluster=PropertiesUtilities.getBooleanPropertyValue(props, ENLARGEBICLUSTERFLAG, false, this.getClass());
		this.useareatostop=PropertiesUtilities.getBooleanPropertyValue(props, STOPBICLUSTERBYAREAFLAG, false, this.getClass());
		this.uselowerboundofconditions=PropertiesUtilities.getBooleanPropertyValue(props, LOWERBOUNDCONDITIONFLAG, false, this.getClass());
		this.numberblocks=PropertiesUtilities.getIntegerPropertyValue(props,BLOCKSTOREPORT, 100, this.getClass());
		this.filteroverlapblocks=PropertiesUtilities.getDoublePropertyValueValidLimits(props, FILTEROVERLAPBLOCKS, 1.0, 0, 1.0, true, this.getClass());
		this.mincolumnwith=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINCOLUMNWITH, 4,2,true, this.getClass());
		this.consistencylevel=PropertiesUtilities.getDoublePropertyValueValidLimits(props, CONSISTENCYLEVELBLOCK, 0.85, 0.5, false, 1.0, true, this.getClass());
		this.expand=PropertiesUtilities.getBooleanPropertyValue(props, EXPANSIONFLAG, false, this.getClass());
		
	}


	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{
				QUANTILEDISCRETIZATION,
				RANKLEVELS,
				DISCRETEDATA,
				TFNAMETOSEARCH,
				ENLARGEBICLUSTERFLAG,
				STOPBICLUSTERBYAREAFLAG,
				LOWERBOUNDCONDITIONFLAG,
				BLOCKSTOREPORT,
				FILTEROVERLAPBLOCKS,
				MINCOLUMNWITH,
				CONSISTENCYLEVELBLOCK,
				EXPANSIONFLAG
		};
		
		String[] defaultvalues=new String[]{"0.5","15","false","","false","false","false","100","1.0","4","0.85","false"};
		
		String[] comments=new String[] {
				"Use quantile discretization for continuous data",
				"The number of ranks as which we treat the up(down)-regulated value when discretization",
				"Discrete data (where user should send their processed data to different value classes)",
				"To-be-searched TF name, just consider the seeds containing current TF default format: B1234",
				"Enlarge current bicluster by the p-value constraint (false or true)",
				"Use area as the value of bicluster to determine when stop (false or true)",
				"Use the lower bound of conditions number (5 percent of the gene number, false or true)",
				"Number of blocks to report",
				"Filter overlapping blocks (1 do not remove any blocks)",
				"Minimum column width of the block",
				"Consistency level of the block (0.5-1.0], the minimum ratio between the number of identical valid symbols in a column and the total number of rows in the output",
				"Expansion"
		};
		
		String[] propsubkeys=new String[]{"-q","-r","-d","-T","-P","-S","-C","-o","-f","-k","-c","-s"};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments, propsubkeys);
		
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		//return tmpdir;
		return null;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeydoubleValue(QUANTILEDISCRETIZATION, quantilediscretization);
		parameters.setKeyintValue(RANKLEVELS, numberranks);
		parameters.setKeyintValue(BLOCKSTOREPORT, numberblocks);
		parameters.setKeydoubleValue(FILTEROVERLAPBLOCKS, filteroverlapblocks);
		parameters.setKeyintValue(MINCOLUMNWITH, mincolumnwith);
		parameters.setKeydoubleValue(CONSISTENCYLEVELBLOCK, consistencylevel);
		parameters.setKeybooleanValue(EXPANSIONFLAG, expand);
		parameters.setKeybooleanValue(DISCRETEDATA, discretedata);
		parameters.setKeyStringValue(TFNAMETOSEARCH, TFtosearch);
		parameters.setKeybooleanValue(ENLARGEBICLUSTERFLAG, enlargebicluster);
		parameters.setKeybooleanValue(STOPBICLUSTERBYAREAFLAG, useareatostop);
		parameters.setKeybooleanValue(LOWERBOUNDCONDITIONFLAG, uselowerboundofconditions);
		return parameters;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new UnibicMethod(binarizationmethod, quantilediscretization, numberranks, discretedata, filetoexpand, TFtosearch, enlargebicluster, useareatostop, uselowerboundofconditions, numberblocks, filteroverlapblocks, mincolumnwith, consistencylevel, expand);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return true;
	}


	@Override
	public void stopProcess() {
		stop.start();
		
	}

}
