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
package jbiclustge.methods.algorithms;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.IBiclusterAlgorithm;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.timeutils.MTUTimeUtils;
import smile.imputation.MissingValueImputationException;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractBiclusteringAlgorithmCaller.
 */
public abstract class AbstractBiclusteringAlgorithmCaller implements IBiclusterAlgorithm{
	
	
	/** The expressionset. */
	protected ExpressionData expressionset;
	
	/** The listofbiclusters. */
	protected BiclusterList listofbiclusters;
	
	/** The algorithmproperties. */
	protected Properties algorithmproperties;
    
    /** The runningtime. */
    protected String runningtime=null;
	
	/**
	 * Support define number biclusters to find.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean supportDefineNumberBiclustersToFind();
	
	/**
	 * Change number bicluster to find.
	 *
	 * @param numberbics the numberbics
	 */
	protected abstract void changeNumberBiclusterToFind(int numberbics);
	
	/**
	 * Sets the algorithm properties.
	 *
	 * @param props the new algorithm properties
	 */
	public abstract void setAlgorithmProperties(Properties props);
	
	/**
	 * Run algorithm.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	protected abstract boolean runAlgorithm() throws Exception;
	
	/**
	 * Process results.
	 *
	 * @throws Exception the exception
	 */
	protected abstract void processResults() throws Exception;
	
	/**
	 * Gets the running time.
	 *
	 * @return the running time
	 */
	//protected abstract String getRunningTime();
	
	/**
	 * Gets the report running parameters.
	 *
	 * @return the report running parameters
	 */
	protected abstract LinkedHashMap<String, String> getReportRunningParameters();
	
	/**
	 * Copy intance.
	 *
	 * @return the abstract biclustering algorithm caller
	 */
	public abstract AbstractBiclusteringAlgorithmCaller copyIntance();
	//public abstract String getAlgorithmName();
	
	
	public abstract void stopProcess();
	
	protected PropertyChangeSupport changesupportlistener=new PropertyChangeSupport(this);

	public static String FIREBICLUSTERINGPROPERTYCHANGEPROGRESS="FIREBICLUSTERINGPROPERTYCHANGEPROGRESS";
	public static String FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS="FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS";
	public static String FIREBICLUSTERINGPROPERTYCHANGESUBTASKSTATUS="FIREBICLUSTERINGPROPERTYCHANGESUBTASKSTATUS";
	public static String FIREPROPERTYALGORITHMCHANGENAME="FIREPROPERTYALGORITHMCHANGENAME";
	public static String FIREPROPERTYCRITICALERROR="FIREPROPERTYCRITICALERROR";
	
	/**
	 * Instantiates a new abstract biclustering algorithm caller.
	 */
	public AbstractBiclusteringAlgorithmCaller(){}
	
	
	/**
	 * Instantiates a new abstract biclustering algorithm caller.
	 *
	 * @param exprs the exprs
	 */
	public AbstractBiclusteringAlgorithmCaller(ExpressionData exprs){
		this.expressionset=exprs;
		if(expressionset!=null && !expressionset.isDataLoaded())
			try {
				expressionset.loadData();
			} catch (IOException | MissingValueImputationException e) {
				LogMessageCenter.getLogger().addCriticalErrorMessage("Error loading expression dataset: ", e);
			}
	}
	
	/**
	 * Instantiates a new abstract biclustering algorithm caller.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public AbstractBiclusteringAlgorithmCaller(ExpressionData exprs, Properties props){
		this(exprs);
		this.algorithmproperties=props;
		
	}
	
	/**
	 * Instantiates a new abstract biclustering algorithm caller.
	 *
	 * @param props the props
	 */
	public AbstractBiclusteringAlgorithmCaller(Properties props){
		this.algorithmproperties=props;
		
	}
	
	/**
	 * Instantiates a new abstract biclustering algorithm caller.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public AbstractBiclusteringAlgorithmCaller(String propertiesfile){
		try {
			this.algorithmproperties=PropertiesUtilities.loadFileProperties(propertiesfile);
		} catch (IOException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error reading properties file: ", e);
		}

	}
	
	/**
	 * Sets the algorithm properties.
	 *
	 * @param propertiesfile the new algorithm properties
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void setAlgorithmProperties(String propertiesfile) throws FileNotFoundException, IOException{
		setAlgorithmProperties(AlgorithmProperties.loadProperties(propertiesfile));
	}
	
	
	public void addBiclusteringPropertyChangeListener(PropertyChangeListener listener) {
		changesupportlistener.addPropertyChangeListener(listener);
	}
	
	/**
	 * Gets the run time.
	 *
	 * @return the run time
	 */
	/*public String getRunTime(){
		return getRunningTime();
	}*/
	
	//@Override
	public String getRunTime(){
		return runningtime;
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getBiclusterResultList()
	 */
	public BiclusterList getBiclusterResultList(){
		if(listofbiclusters!=null){
			listofbiclusters.setUsedmethod(getAlgorithmName());
			listofbiclusters.setAnalysedDataset(expressionset);
			listofbiclusters.setMethodRunningTime(getRunTime());
			if(getReportRunningParameters()!=null)
				listofbiclusters.setMethodRunningParameters(getReportRunningParameters());
		}
		/*else
			listofbiclusters=new BiclusterList();*/
		return listofbiclusters;
	}

    /**
     * Sets the expression data.
     *
     * @param data the new expression data
     */
    public void setExpressionData(ExpressionData data){
    	this.expressionset=data;
    }
    
    /**
     * Gets the temporary working directory.
     *
     * @return the temporary working directory
     */
    protected abstract String getTemporaryWorkingDirectory();
    
    /**
     * Supportedinoperatingsystem.
     *
     * @return true, if successful
     */
    protected abstract boolean supportedinoperatingsystem();
	
    
    /**
     * Change number bicluster tobe found.
     *
     * @param nmuberbiclusters the nmuberbiclusters
     */
    public void changeNumberBiclusterTobeFound(int nmuberbiclusters){
    	if(supportDefineNumberBiclustersToFind())
    		changeNumberBiclusterToFind(nmuberbiclusters);
    	else
    		LogMessageCenter.getLogger().toClass(getClass()).addWarnMessage("This method do not supports to define the number of biclusters to be found");
    }
    
    public void reset() {
    	this.listofbiclusters=null;
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
    @Override
    public synchronized void run() {
    	try {
    		changesupportlistener.firePropertyChange(FIREPROPERTYALGORITHMCHANGENAME, null, getAlgorithmName());

    		if(algorithmproperties!=null) {
    			setAlgorithmProperties(algorithmproperties);
    		}
    		if(expressionset!=null){

    			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Running "+ getAlgorithmName()+" method, this may take a while, please wait...");
    			changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS, null, "executing...");

    			boolean successfully=runAlgorithm();

    			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Processing results of "+ getAlgorithmName()+" method..."); 

    			if(successfully){
    				try {
    					changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS, null, "Processing results...");

    					processResults();
    					if(listofbiclusters==null || listofbiclusters.size()==0){
    						successfully=false;
    						LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("The results of "+ getAlgorithmName()+" were null, please change the parameters of the method and try again");
    					}
    				} catch (Exception e){
    					LogMessageCenter.getLogger().addCriticalErrorMessage("Error in processing of "+ getAlgorithmName()+" results: ", e);
    					changesupportlistener.firePropertyChange(FIREPROPERTYCRITICALERROR, null, "Error in processing of "+ getAlgorithmName()+" results: "+e.getMessage());
    				}
    			}
    			if(successfully) {
    				LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("The results of "+ getAlgorithmName()+" were successfully processed");
    				changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS, null, "Results were successfully processed");
    			}
    		}
    		else
    			throw new IOException("Null input for Expression data, please set the  gene expression data to be analysed, using one of the following formats: "
    					+ "Text file format for expression dataset (.txt, .csv),  Stanford cDNA file format (.pcl), Gene Cluster Text file format (.gct) or ExpRESsion (with P and A calls) file format (.res)\n");
    	} catch (Exception e){
    		LogMessageCenter.getLogger().addCriticalErrorMessage("An error was raised when running "+getAlgorithmName()+": ", e);
    		changesupportlistener.firePropertyChange(FIREPROPERTYCRITICALERROR, null, "An error was raised when running "+getAlgorithmName()+": "+e.getMessage());
    	}
    	finally {
    		if(getTemporaryWorkingDirectory()!=null)
    			SystemFolderTools.deleteTempDir(getTemporaryWorkingDirectory());
    	}
    }
	
    
    protected void saveElapsedTime(Instant start) {
    	Instant finish = Instant.now();
		long runtime = Duration.between(start, finish).toMillis();
		runningtime=MTUTimeUtils.getTimeElapsed(runtime);
    }
    
	/**
	 * Gets the date signature.
	 *
	 * @return the date signature
	 */
	protected synchronized String getDateSignature(){
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	}
}
