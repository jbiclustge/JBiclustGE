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
package jbiclustge.methods.algorithms.r;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.javatuples.Pair;
import org.math.R.Rsession;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.rtools.JavaToRUtils;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.JBiGePropertiesManager;
import jbiclustge.utils.properties.JBiclustGEPropertiesInitializer;
import jrplot.rbinders.functioncallers.AbstractRFunctionCallerSingleDataset;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.rtools.connectors.RConnector;
import pt.ornrocha.rtools.installutils.RInstallTools;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.stringutils.MTUStringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RBiclustAlgorithmCaller.
 */
public abstract class RBiclustAlgorithmCaller extends AbstractBiclusteringAlgorithmCaller{

	/** The writeoriginalresultstofile. */
	//protected int nclusters=100;
	protected String writeoriginalresultstofile=null;
	
	/** The inputmatrixname. */
	protected String inputmatrixname;
	
	/** The resultobjname. */
	protected String resultobjname;
	
	/** The label. */
	protected String label;
	
	/** The rsession. */
	protected Rsession rsession;
	
	/** The ismultisession. */
	protected boolean ismultisession=false;
	
	protected Thread stop;
	
	/**
	 * Instantiates a new r biclust algorithm caller.
	 */
	public RBiclustAlgorithmCaller() {
		super();
		init();
	}
	
	/**
	 * Instantiates a new r biclust algorithm caller.
	 *
	 * @param props the props
	 */
	public RBiclustAlgorithmCaller(Properties props) {
		super(props);
		init();
	}
	
	/**
	 * Instantiates a new r biclust algorithm caller.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RBiclustAlgorithmCaller(String propertiesfile) {
		super(propertiesfile);
		init();
	}

	/**
	 * Instantiates a new r biclust algorithm caller.
	 *
	 * @param exprs the exprs
	 */
	public RBiclustAlgorithmCaller(ExpressionData exprs) {
		super(exprs);
		init();
	}

	/**
	 * Instantiates a new r biclust algorithm caller.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RBiclustAlgorithmCaller(ExpressionData exprs, Properties props) {
		super(exprs, props);
		init();
	}
	
	/**
	 * Sets the writeoriginalresultstofile.
	 *
	 * @param writeoriginalresultstofile the new writeoriginalresultstofile
	 */
	public void setWriteoriginalresultstofile(String writeoriginalresultstofile) {
		this.writeoriginalresultstofile = writeoriginalresultstofile;
	}
	
	/**
	 * Inits the.
	 */
	protected void init(){
		this.label=MTUStringUtils.shortUUID();
		this.inputmatrixname=getAlgorithmName()+"_inmatrix_"+label;
		this.resultobjname="result_"+getAlgorithmName()+"_"+label;
	}
	
	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	protected String getlabel(){
		return label;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
	 */
	@Override
	protected String getRunningTime() {
		return runningtime;
	}
	
	/**
	 * Gets the r session.
	 *
	 * @return the r session
	 */
	protected Rsession getRSession(){
		return rsession;
	}
	
	/**
	 * Required libraries.
	 *
	 * @return the linked hash map
	 */
	/*
	 * set required libraries by name, if it is a bioconductor package must be set Boolean as true, also Boolean is false ;
	 */
	protected abstract ArrayList<RPackageInfo> requiredLibraries();
	
	/**
	 * Load sources.
	 *
	 * @return the array list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract ArrayList<String> loadSources()throws IOException;
	
	/**
	 * Removes the R input objects.
	 *
	 * @return the string[]
	 */
	protected abstract String[] removeRInputObjects();

	
	/**
	 * Inits the rsession.
	 */
	protected void initRsession(){
		if(rsession==null){
			Pair<String, String> rserveparam=JavaToRUtils.getRServeParameters();
			
			String Ruserlib=(String) JBiGePropertiesManager.getManager().getKeyValue(JBiclustGEPropertiesInitializer.RUSERLIBPATH);
			
			
			ismultisession=JavaToRUtils.useMultipleRsession();
			//System.out.println("Multiple Session: "+ismultisession);
			if(ismultisession){
				int port=0;
				try {
					if(rserveparam.getValue1()!=null)
						port=Integer.parseInt(rserveparam.getValue1());
				} catch (Exception e) {
					// TODO: handle exception
				}
				rsession=RConnector.getNewLocalRsession(Ruserlib,rserveparam.getValue0(), port);
			} else {
				RConnector.startInstance(Ruserlib,rserveparam.getValue0(), rserveparam.getValue1());
				rsession=RConnector.getSession();
				
			}
		
		}
	}
	
 	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#run()
	 */
	@Override
	public synchronized void run() {
	      try{
		    
			String Rpath=RInstallTools.getSystemR_HOME();
			LogMessageCenter.getLogger().addTraceMessage("R executable path: "+Rpath);
			if(Rpath!=null && !Rpath.isEmpty()){	
				try{
					
					initRsession();

					if(rsession==null)
						throw new IOException("Rserve is not installed, please install manualy in R console, submiting the following command: install.packages(\"Rserve\")");
					else{
						changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS, null, "Validating if "+getAlgorithmName()+" can execute");
						changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGESUBTASKSTATUS, null, "Loading/installing mandatory packages: "+RConnector.getRequiredPackagesToString(requiredLibraries()));
						loadRequiredLibraries();
						if(loadSources()!=null && loadSources().size()>0)
							changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGESUBTASKSTATUS, null, "Loading mandatory R Sources: "+RConnector.getRequiredSourcesToString(loadSources()));
						loadRequiredRSources();
	
						LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Running "+ getAlgorithmName()+" method, this may take a while, please wait...");
						
						Runtime.getRuntime().addShutdownHook(new Thread() {
							  @Override
							  public void run() {
							    rsession.end();
							    //SystemFolderTools.deleteTempDir(tempfolder);
							  }
							});
						
						stop=new Thread(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("ORDER STOP:::::::::::::::::");
								rsession.end();
								
							}
						});
						
						changesupportlistener.firePropertyChange(FIREPROPERTYALGORITHMCHANGENAME, null, getAlgorithmName());
						changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGETASKSTATUS, null, "Executing...");
						
						boolean successfully=runAlgorithm();
						
						
		   
						//long endTime = System.currentTimeMillis();
						//runningtime=MTUTimeUtils.getTimeElapsed(endTime-startTime);
						if(successfully){
							changesupportlistener.firePropertyChange(FIREBICLUSTERINGPROPERTYCHANGESUBTASKSTATUS, null, "Processing Biclusters...");
							LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Processing results of "+ getAlgorithmName()+" method..."); 
							try {
								processResults();
							} catch (Exception e) {
								LogMessageCenter.getLogger().addCriticalErrorMessage("Error in processing of "+ getAlgorithmName()+" results: ", e);
						
							}
							LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Results of "+ getAlgorithmName()+" were successfully processed");
		      
						}
					}
				}
				catch (Exception e){
					LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error: ", e);
				}
		
				if(removeRInputObjects()!=null && removeRInputObjects().length>0){
					boolean r=rsession.unset(removeRInputObjects());
					if(r)
						LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Objects were removed from R workspace:\n", removeRInputObjects(), "\n");
				}
				
				if(ismultisession)
					rsession.end();
				
			}
			else 
				throw new IOException("No installation of R was detected, please install R to use "+getAlgorithmName());
	      }
		catch (Exception e){
				LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error: ",e);
		}

	      
	}
	
	
	
	/**
	 * Load required libraries.
	 *
	 * @throws Exception the exception
	 */
	protected void loadRequiredLibraries() throws Exception{
		ArrayList<RPackageInfo> libs=requiredLibraries();
		if(libs!=null){
			//String userRlibs=JavaToRUtils.checkUserRLibsPath(getRSession());
			String userRlibs=(String) JBiGePropertiesManager.getManager().getKeyValue(JBiclustGEPropertiesInitializer.RUSERLIBPATH);
			if(userRlibs!=null)
				RConnector.loadRequiredLibraries(rsession, libs, userRlibs);
			else
				RConnector.loadRequiredLibraries(rsession,libs);
		}
	}
	
	/**
	 * Load required R sources.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void loadRequiredRSources() throws IOException{
		if(loadSources()!=null){
			ArrayList<String> inputsources=loadSources();
			for (int i = 0; i < inputsources.size(); i++) {
				rsession.source(new File(inputsources.get(i)));
			}
		}
	}
	
	
	
	
	/**
	 * Gets the result output name.
	 *
	 * @return the result output name
	 */
	protected String getResultOutputName(){
		return resultobjname;
	}
	
	/*public void setNumberBiclustersBeFound(int nclust){
		this.nclusters=nclust;
	}*/
	
	
	/**
	 * Gets the list of biclusters.
	 *
	 * @param nameresultslot the nameresultslot
	 * @return the list of biclusters
	 * @throws RserveException the rserve exception
	 * @throws REXPMismatchException the REXP mismatch exception
	 */
	protected BiclusterList getListOfBiclusters(String nameresultslot) throws RserveException, REXPMismatchException{
		

			int nbicluters=rsession.eval(nameresultslot+"@Number").asInteger();
		    LogMessageCenter.getLogger().addInfoMessage(getAlgorithmName()+" Number Output Biclusters: "+nbicluters);
		    if(nbicluters>0)
			  listofbiclusters=new BiclusterList();

		    for (int i = 1; i <= nbicluters; i++) {
		    	String pos=String.valueOf(i);
		    	int[] genes=rsession.eval("row(matrix("+nameresultslot+"@RowxNumber[,"+pos+"]))["+nameresultslot+"@RowxNumber[,"+pos+"]==T]").asIntegers();
		    	int[] conditions=rsession.eval("row(matrix("+nameresultslot+"@NumberxCol["+pos+",]))["+nameresultslot+"@NumberxCol["+pos+",]==T]").asIntegers();
		    	
		    	BiclusterResult result=new BiclusterResult(expressionset, genes, conditions, true,1);
		    	listofbiclusters.add(result);

		    }

		return listofbiclusters;
	}
	
	
	/*protected void loadExpressionMatrixInREnvironment(Rsession s){
		synchronized (s) {
			s.set(inputmatrixname, expressionset.getexpressionDataMatrix());
		    s.set((inputmatrixname+"_rownames"), expressionset.getGeneids());
		    s.set((inputmatrixname+"_colnames"), expressionset.getConditions());
		    s.silentlyEval("rownames("+inputmatrixname+")="+(inputmatrixname+"_rownames"));
		    s.silentlyEval("colnames("+inputmatrixname+")="+(inputmatrixname+"_colnames"));
		}
	}*/
	
	/**
	 * Load expression matrix in R environment.
	 */
	protected void loadExpressionMatrixInREnvironment(){

		   ArrayList<String> s=new ArrayList<>();
		   
		   for (int i = 0; i < expressionset.getGeneids().length; i++) {
			    String d=expressionset.getGeneids()[i];
			    if(s.contains(d))
			    	System.out.println("DUPLICATE: "+d);
			    else
			    	s.add(d);
		      }
		   
		   
			rsession.set(inputmatrixname, expressionset.getexpressionDataMatrix());
			rsession.set((inputmatrixname+"_rownames"), expressionset.getGeneids());
			rsession.set((inputmatrixname+"_colnames"), expressionset.getConditions());
			rsession.silentlyEval("rownames("+inputmatrixname+")="+(inputmatrixname+"_rownames"));
			rsession.silentlyEval("colnames("+inputmatrixname+")="+(inputmatrixname+"_colnames"));
		
	}
	
	/**
	 * Gets the base R ojects.
	 *
	 * @return the base R ojects
	 */
	protected String[] getBaseROjects(){
		return new String[]{inputmatrixname,
				(inputmatrixname+"_rownames"),
				(inputmatrixname+"_colnames"),
				resultobjname};
	}
	
	/**
	 * Gets the base R ojects list.
	 *
	 * @return the base R ojects list
	 */
	protected ArrayList<String> getBaseROjectsList(){
		return new ArrayList<>(Arrays.asList(getBaseROjects()));
	}
	
	/**
	 * Write bicluster results to file with original algorithm method.
	 */
	protected void writeBiclusterResultsToFileWithOriginalAlgorithmMethod(){
		writeBiclusterResultsToFileWithOriginalAlgorithmMethod(getResultOutputName());	
	}
	
	/**
	 * Write bicluster results to file with original algorithm method.
	 *
	 * @param nameofbiclusterobject the nameofbiclusterobject
	 */
	protected void writeBiclusterResultsToFileWithOriginalAlgorithmMethod(String nameofbiclusterobject){
		if(writeoriginalresultstofile!=null){
			rsession.silentlyVoidEval("writeBiclusterResults(\""+writeoriginalresultstofile+"\","+nameofbiclusterobject+",\"output_"+getAlgorithmName()+"\",rownames("+inputmatrixname+"),colnames("+inputmatrixname+"))", true);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return null;
	}
	
	@Override
	public void stopProcess() {
		stop.start();
		
	}
	
	

}
