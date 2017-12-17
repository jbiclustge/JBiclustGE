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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.wrappers.components.KolmogorovEstimator;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.AlgorithmProperties;
import jbiclustge.utils.properties.CommandsProcessList;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class UBClustMethod.
 */
public class UBClustMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	
	/** The Constant NAME. */
	public static final String NAME="UBClust";
	
	/** The Constant DISCRETIZATIONLEVAL. */
	public static final String DISCRETIZATIONLEVAL="discretization_level";
	
	/** The Constant INITIALTEMPERATURE. */
	public static final String INITIALTEMPERATURE="initial_temperature";
	
	/** The Constant TEMPERATUREFACTOR. */
	public static final String TEMPERATUREFACTOR="temperature_factor";
	
	/** The Constant ESTIMATOR. */
	public static final String ESTIMATOR="estimator";
	
	/** The Constant NUMBERBICLUSTERSESTIMATE. */
	public static final String NUMBERBICLUSTERSESTIMATE="number_biclusters_to_estimate";
	
	/** The Constant MINJAVAHEAPSIZE. */
	public static final String MINJAVAHEAPSIZE="initial_java_heap_size";
	
	
	/** The workingpath. */
	private String workingpath;
	
	/** The executablefilepath. */
	private String executablefilepath;
	
	/** The rowsresultfilepath. */
	private String rowsresultfilepath;
	
	/** The columnsresultfilepath. */
	private String columnsresultfilepath;
	
	/** The matrixfilepath. */
	private String matrixfilepath;
	
	/** The datamatrixfilepath. */
	private String datamatrixfilepath;
	
	/** The initialjavaheapsizemg. */
	private int initialjavaheapsizemg=1024;
	
	/** The discretizationlevel. */
	private int discretizationlevel=128;
	
	/** The initialtemp. */
	private double initialtemp=0.00001;
	
	/** The temperaturefactor. */
	private double temperaturefactor=0.9;
	
	/** The estimator. */
	private int estimator=0;
	
	/** The numberbiclusters. */
	private int numberbiclusters=1;

	
	/** The cmds. */
	protected CommandsProcessList cmds;
	
	private Thread stop;
	/**
	 * Instantiates a new UB clust method.
	 */
	public UBClustMethod(){
		super();
	}
	
	/**
	 * Instantiates a new UB clust method.
	 *
	 * @param exprs the exprs
	 */
	public UBClustMethod(ExpressionData exprs){
		super(exprs);
	}
	
	/**
	 * Instantiates a new UB clust method.
	 *
	 * @param properties the properties
	 */
	public UBClustMethod(Properties properties){
		super(properties);
	}
	
	/**
	 * Instantiates a new UB clust method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public UBClustMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	/**
	 * Instantiates a new UB clust method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public UBClustMethod(ExpressionData exprs, Properties props){
		super(exprs, props);
	}
	
	
	
	
	/**
	 * Instantiates a new UB clust method.
	 *
	 * @param initialjavaheapsizemg the initialjavaheapsizemg
	 * @param discretizationlevel the discretizationlevel
	 * @param initialtemp the initialtemp
	 * @param temperaturefactor the temperaturefactor
	 * @param estimator the estimator
	 * @param numberbiclusters the numberbiclusters
	 */
	private UBClustMethod(int initialjavaheapsizemg, int discretizationlevel, double initialtemp,
			double temperaturefactor, int estimator, int numberbiclusters) {
		super();
		this.initialjavaheapsizemg = initialjavaheapsizemg;
		this.discretizationlevel = discretizationlevel;
		this.initialtemp = initialtemp;
		this.temperaturefactor = temperaturefactor;
		this.estimator = estimator;
		this.numberbiclusters = numberbiclusters;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param nbic the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int nbic){
		this.numberbiclusters=nbic;
	}
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param nbic the nbic
	 * @return the UB clust method
	 */
	public UBClustMethod addNumberBiclustersToFind(int nbic){
		setNumberBiclustersToFind(nbic);
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
	 * Sets the initial temperature.
	 *
	 * @param temperature the new initial temperature
	 */
	public void setInitialTemperature(double temperature){
		this.initialtemp=temperature;
	}
	
	/**
	 * Adds the initial temperature.
	 *
	 * @param temperature the temperature
	 * @return the UB clust method
	 */
	public UBClustMethod addInitialTemperature(double temperature){
		this.initialtemp=temperature;
		return this;
	}
	
	/**
	 * Sets the temperature factor.
	 *
	 * @param temperaturefactor the new temperature factor
	 */
	public void setTemperatureFactor(double temperaturefactor){
		this.temperaturefactor=temperaturefactor;
	}
	
	/**
	 * Adds the temperature factor.
	 *
	 * @param temperaturefactor the temperaturefactor
	 * @return the UB clust method
	 */
	public UBClustMethod addTemperatureFactor(double temperaturefactor){
		this.temperaturefactor=temperaturefactor;
		return this;
	}
	
	/**
	 * Sets the estimator.
	 *
	 * @param estimator the new estimator
	 */
	public void setEstimator(int estimator){
		this.estimator=estimator;
	}
	
	public void setEstimator(KolmogorovEstimator estimator) {
		this.estimator=Integer.parseInt(estimator.getCodeTag());
	}
	
	/**
	 * Sets the discretization level.
	 *
	 * @param levels the new discretization level
	 */
	public void setDiscretizationLevel(int levels){
		this.discretizationlevel=levels;
	}
	
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{
				NUMBERBICLUSTERSESTIMATE,
				DISCRETIZATIONLEVAL,
				INITIALTEMPERATURE,
				TEMPERATUREFACTOR,
				ESTIMATOR,
				MINJAVAHEAPSIZE
		};
		
		
		String[] defaultvalues=new String[]{"1","128","0.00001","0.9","0","1024"};
		
		String[] comments=new String[] {
				"Number of biclusters to estimate",
				"Discretization levels (default 128)",
				"Initial temperature (default 0.00001)",
				"Temperature factor (default 0.9)",
				"Kolmogorov complexity estimator: \n"
				+ "  0 = Uniform Model (default)\n"
				+ "  1 = Constant Rows Model\n"
				+ "  2 = Additive Model\n"
				+ "  3 = Relaxed OPSM",
				"Initial Java heap size (MB), default=1024"
		};
		
		String source="source: http://alumni.cs.ucr.edu/~hli/ubc/";
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,source);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return "ubc.jar";
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
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.numberbiclusters=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, NUMBERBICLUSTERSESTIMATE, 1, 1, true, getClass());
		this.discretizationlevel=PropertiesUtilities.getIntegerPropertyValue(props, DISCRETIZATIONLEVAL, 128, getClass());
		this.initialtemp=PropertiesUtilities.getDoublePropertyValue(props, INITIALTEMPERATURE, 0.00001, getClass());
		this.temperaturefactor=PropertiesUtilities.getDoublePropertyValue(props, TEMPERATUREFACTOR, 0.9, getClass());
		this.estimator=PropertiesUtilities.getIntegerPropertyValueValidLimits(props, ESTIMATOR, 0, 0, 3, true, getClass());
		this.initialjavaheapsizemg=PropertiesUtilities.getIntegerPropertyValue(props, MINJAVAHEAPSIZE, 1024, getClass());
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@SuppressWarnings("resource")
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		configurePaths();
		preconfigurealgorithm();
		
		ProcessBuilder build= new ProcessBuilder(cmds);
		build.directory(new File(workingpath));
		Date starttime =Calendar.getInstance().getTime();
		final Process p =build.start();
		InputStream inputstr =p.getInputStream();
		ReusableInputStream errorstr =new ReusableInputStream(p.getErrorStream());
		
		
		Thread stdout=new Thread(new GeneralProcessProgressionChecker(inputstr,getClass()));
		stdout.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			  @Override
			  public void run() {
			    p.destroy();
			    SystemFolderTools.deleteTempDir(workingpath);
			  }
			});
		
		stop=new Thread(new Runnable() {
			
			@Override
			public void run() {
				 p.destroy();
				 SystemFolderTools.deleteTempDir(workingpath);
				
			}
		});
		

		int exitval=p.waitFor();
	
		if(exitval==0){
			Date endtime=Calendar.getInstance().getTime();
			long runtime=endtime.getTime()-starttime.getTime();	
			runningtime=MTUTimeUtils.getTimeElapsed(runtime);
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
	protected void processResults() throws Exception {
		
		this.listofbiclusters=new BiclusterList();
		
		ArrayList<ArrayList<Integer>> genesinbiclusters=getGeneBiclusters();
		ArrayList<ArrayList<Integer>> conditionsinbiclusters=getConditionBiclusters();
		
		for (int i = 0; i < genesinbiclusters.size(); i++) {
			BiclusterResult bicres=new BiclusterResult(expressionset, genesinbiclusters.get(i), conditionsinbiclusters.get(i), true);
			listofbiclusters.add(bicres);
		}
		
		SystemFolderTools.deleteTempDir(workingpath);
		//FileUtils.deleteDirectory(new File(workingpath));
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
	 */
	@Override
	protected String getRunningTime() {
		return runningtime;
	}
	
	/**
	 * Configure paths.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void configurePaths() throws IOException{
		
		workingpath=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("UBC");
		executablefilepath=FilenameUtils.concat(workingpath, getBinaryName());
		MTUFileUtils.copyFile(getBinaryExecutablePath(), executablefilepath);
		rowsresultfilepath=FilenameUtils.concat(workingpath, "row.txt");
		columnsresultfilepath=FilenameUtils.concat(workingpath, "col.txt");
		matrixfilepath=FilenameUtils.concat(workingpath, "matrix.txt");
		datamatrixfilepath=FilenameUtils.concat(workingpath, "data.txt");
		
	}
	
	/**
	 * Preconfigurealgorithm.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void preconfigurealgorithm() throws IOException{
		
		expressionset.writeExpressionDatasetToFile(datamatrixfilepath, "\t");
		
		cmds=new CommandsProcessList();
		cmds.add("java");
		cmds.add("-Xms"+String.valueOf(initialjavaheapsizemg)+"m");
		cmds.add("-jar");
		cmds.add(executablefilepath);
		if(discretizationlevel!=128)
			cmds.insertIntegerParameter("-l", discretizationlevel);
		if(initialtemp!=0.00001)
			cmds.insertDoubleParameter("-t", initialtemp);
		if(temperaturefactor!=0.9)
			cmds.insertDoubleParameter("-f", temperaturefactor);
		if(estimator!=0 && estimator>0 && estimator<4)
			cmds.insertIntegerParameter("-e", estimator);
		if(numberbiclusters>=1)
			cmds.insertIntegerParameter("-k", numberbiclusters);
		cmds.add(datamatrixfilepath);
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	/**
	 * Gets the gene biclusters.
	 *
	 * @return the gene biclusters
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArrayList<ArrayList<Integer>> getGeneBiclusters() throws IOException{
		ArrayList<String> generesultlines=(ArrayList<String>) MTUReadUtils.readFileLines(rowsresultfilepath);
		
		ArrayList<ArrayList<Integer>> res=new ArrayList<>();
		
		for (int i = 0; i < generesultlines.size(); i++) {
			ArrayList<Integer> genebiclust=new ArrayList<>();
			
			String[] elems=generesultlines.get(i).trim().split("\\s+");
		
			for (int j = 1; j < elems.length; j++) {
				int val=0;
				try {
					val=Integer.valueOf(elems[j]);
					if(val==1)
						genebiclust.add(j-1);
				} catch (Exception e) {
				   LogMessageCenter.getLogger().addCriticalErrorMessage("Error in processing gene results: ", e);
				   throw new IOException("Error converting value ["+elems[j]+" to Integer value");
				}
			}
			res.add(genebiclust);
		}
		
		return res;
	}
	
	/**
	 * Gets the condition biclusters.
	 *
	 * @return the condition biclusters
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArrayList<ArrayList<Integer>> getConditionBiclusters() throws IOException{
		ArrayList<String> condresultlines=(ArrayList<String>) MTUReadUtils.readFileLines(columnsresultfilepath);
		
		ArrayList<ArrayList<Integer>> res=new ArrayList<>();
		
		for (int i = 0; i < condresultlines.size(); i++) {
			ArrayList<Integer> condbiclust=new ArrayList<>();
			
			String[] elems=condresultlines.get(i).trim().split("\\s+");
			for (int j = 1; j < elems.length; j++) {
				int val=0;
				try {
					val=Integer.valueOf(elems[j]);
					if(val==1)
						condbiclust.add(j-1);
				} catch (Exception e) {
				   LogMessageCenter.getLogger().addCriticalErrorMessage("Error in processing conditions results: ", e);
				   throw new IOException("Error converting value ["+elems[j]+" to Integer value");
				}
			}
			res.add(condbiclust);
		}
		
		return res;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return workingpath;

	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(NUMBERBICLUSTERSESTIMATE, numberbiclusters);
		parameters.setKeyintValue(DISCRETIZATIONLEVAL, discretizationlevel);
		parameters.setKeydoubleValue(INITIALTEMPERATURE, initialtemp);
		parameters.setKeydoubleValue(TEMPERATUREFACTOR, temperaturefactor);
		parameters.setKeyintValue(ESTIMATOR, estimator);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new UBClustMethod(initialjavaheapsizemg, discretizationlevel, initialtemp, temperaturefactor, estimator, numberbiclusters);
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
