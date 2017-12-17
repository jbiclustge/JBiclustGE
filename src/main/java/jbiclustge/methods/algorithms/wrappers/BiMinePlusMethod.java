/*
 * Copyright 2016
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
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
 * Created inside the BIOSYSTEMS Research Group (http://www.ceb.uminho.pt/biosystems)
 * 
 * Author: Orlando Rocha
 */
package jbiclustge.methods.algorithms.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.AlgorithmProperties;
import jbiclustge.utils.properties.CommandsProcessList;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiMinePlusWrapper.
 */
public class BiMinePlusMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	
	/** The Constant MINCONDITIONS. */
	public static final String MINCONDITIONS="minimum_number_conditions_threshold";
	
	/** The Constant ASR. */
	public static final String ASR="average_spearmans_rho";
	
	/** The Constant JAVAXMX. */
	public static final String JAVAXMX="java_xmx_parameter";
	
	/** The Constant NAME. */
	public static final String NAME="BiMineplus";
	
	/** The datafilepath. */
	protected String datafilepath=null;
	
	/** The tmpfolder. */
	protected String tmpfolder=null;
	
	/** The resultsfilepath. */
	protected String resultsfilepath=null;
	
	/** The cmds. */
	protected CommandsProcessList cmds;
	
	/** The minconds. */
	private int minconds=2;
	
	/** The asr. */
	protected double asr=0.6; // Average Spearman’s Rho [-1..1]
	
	/** The javaxmx. */
	protected int javaxmx=1024;
	
	protected Thread stop;
	
	
	/**
	 * Instantiates a new bi mine plus wrapper.
	 */
	public BiMinePlusMethod() {
		super();
	}

	/**
	 * Instantiates a new bi mine plus wrapper.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public BiMinePlusMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}


	
	/**
	 * Instantiates a new BiMine+.
	 *
	 * @param exprs the exprs
	 */
	public BiMinePlusMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	 /**
 	 * Instantiates a new bi mine plus method.
 	 *
 	 * @param props the props
 	 */
 	public BiMinePlusMethod(Properties props){
	    	super(props);
	  }
	 
	 
	 /**
 	 * Instantiates a new bi mine plus method.
 	 *
 	 * @param propertiesfile the propertiesfile
 	 */
 	public BiMinePlusMethod(String propertiesfile){
	    	super(propertiesfile);
	  }
	 
	 
	
	/**
	 * Instantiates a new bi mine plus method.
	 *
	 * @param minconds the minconds
	 * @param asr the asr
	 * @param javaxmx the javaxmx
	 */
	private BiMinePlusMethod(int minconds, double asr, int javaxmx) {
		super();
		this.minconds = minconds;
		this.asr = asr;
		this.javaxmx = javaxmx;
	}

	/**
	 * Is the threshold of minimum number of conditions.
	 * It can be used only in the BiMine algorithm.
	 *
	 * @param minconds the new threshold of minimum number of conditions
	 */
	public void setMinimumConditions(int minconds) {
		this.minconds = minconds;
	}
	
	/**
	 * Adds the minimum conditions.
	 *
	 * @param minconds the minconds
	 * @return the bi mine plus method
	 */
	public BiMinePlusMethod addMinimumConditions(int minconds){
		setMinimumConditions(minconds);
		return this;
	}
	

	/**
	 * Sets the threshold of the Average Spearman’s Rho [-1...1]
	 *
	 * @param asr the new Average Spearman’s Rho value
	 */
	public void setASR(double asr) {
		this.asr = asr;
	}
	
	/**
	 * Adds the ASR.
	 *
	 * @param asr the asr
	 * @return the bi mine plus method
	 */
	public BiMinePlusMethod addASR(double asr){
		setASR(asr);
		return this;
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return "BiMine+.jar";
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.IBiclustWrapper#getBinaryExecutablePath()
	 */
	@Override
	public String getBinaryExecutablePath() throws IOException {
		String path=SystemFolderTools.getBinaryFilePathFromPropertiesFile(getAlgorithmName());
        if(path==null)
        	path=SystemFolderTools.getMethodExePath(getBinaryName());
		return path;
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.IBiclustWrapper#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{MINCONDITIONS,ASR,JAVAXMX};
		String[] propdefaultvals=new String[]{"2","0.6","1024"};
		String[] propcomments=new String[]{
				"Threshold of minimum number of conditions >2",
				"Threshold of the average spearmans rho [-1..1]",
				"Java maximum size memory allocation pool"
			};
		return AlgorithmProperties.setupProperties(propkeys, propdefaultvals,propcomments);
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
	
		this.minconds=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINCONDITIONS, 2, 2, true, this.getClass());
		this.asr=PropertiesUtilities.getDoublePropertyValueValidLimits(props, ASR, 0.6, -1.0, 1.0, true, this.getClass());
		this.javaxmx=PropertiesUtilities.getIntegerPropertyValue(props, JAVAXMX, 1024, getClass());
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@SuppressWarnings("resource")
	@Override
	protected boolean runAlgorithm() throws Exception {
			
			preconfigureAlgorithm();
		 
	        ProcessBuilder build= new ProcessBuilder(cmds);
		
	        Date starttime =Calendar.getInstance().getTime();
			final Process p =build.start();
			InputStream inputstr =p.getInputStream();
			ReusableInputStream errorstr =new ReusableInputStream(p.getErrorStream());
			
			
			Thread stdout=new Thread(new GeneralProcessProgressionChecker(inputstr));
			stdout.run();
			
			Thread stderrorout=new Thread(new GeneralProcessProgressionChecker(errorstr));
			stderrorout.run();
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				  @Override
				  public void run() {
				    p.destroy();
				    SystemFolderTools.deleteTempDir(tmpfolder);
				  }
				});
			
			stop=new Thread(new Runnable() {
				
				@Override
				public void run() {
					 p.destroy();
					 SystemFolderTools.deleteTempDir(tmpfolder);
					
				}
			});
			
			/*stop=new Thread() {
				@Override
				public void run() {
					 p.destroy();
					 SystemFolderTools.deleteTempDir(tmpfolder);
				}
			};*/
			

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
	 * @see executors.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		
		this.listofbiclusters=new BiclusterList();
		ArrayList<String> lines=(ArrayList<String>) MTUReadUtils.readFileLines(resultsfilepath);
		for (int i = 2; i < lines.size(); i++) {
			listofbiclusters.add(parseLine(lines.get(i)));
		}
		
		SystemFolderTools.deleteTempDir(tmpfolder);
		//SystemFolderTools.deleteTempFile(datafilepath);
		//SystemFolderTools.deleteTempFile(resultsfilepath);
		 
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.AbstractBiclusteringAlgorithmCaller#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}
	
	
	

	/**
	 * Configure the temporary files which the algorithm will use.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void configureInputData() throws IOException{
		String provname=MTUStringUtils.shortUUID();
		tmpfolder=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix(getAlgorithmName());
		datafilepath=FilenameUtils.concat(tmpfolder, provname+".txt");
		resultsfilepath=FilenameUtils.concat(tmpfolder, provname+"_results"+".txt");
		expressionset.writeExpressionDatasetToFile(datafilepath, true, false, "\t");
	}
	
	/**
	 * Preconfigure algorithm.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void preconfigureAlgorithm() throws IOException{
		cmds=new CommandsProcessList();
		cmds.add("java");
		cmds.add("-Xmx"+javaxmx+"m");
		cmds.add("-jar");
		cmds.add(getBinaryExecutablePath());
		configureInputData();
		cmds.add(datafilepath);
		cmds.add(String.valueOf(minconds));
		cmds.add(String.valueOf(asr));
		cmds.add(resultsfilepath);
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	
	/**
	 * Parses the results collected from the output of the biclustering algorithm.
	 *
	 * @param line the line
	 * @return the bicluster result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private BiclusterResult parseLine(String line) throws IOException{

		//String regex="Genes\\s+\\[(\\w+(,\\s+\\w+)*)\\]\\s+Conditions\\s+\\[(\\w+(,\\s+\\w+)*)\\]\\s+ASR:\\s+((-)*\\d+.\\d+)";
		String regex="Genes\\s+\\[(.*)\\]\\s+Conditions\\s+\\[(.*)\\]\\s+ASR:\\s+((-)*\\d+.\\d+)";

		Pattern pat=Pattern.compile(regex);
		
		Matcher m =pat.matcher(line);
		
		ArrayList<String> genenames=new ArrayList<>();
		ArrayList<Integer> condindexes=new ArrayList<>();
		String asr=null;
		
		if(m.find()){
			//System.out.println("Genes#: "+m.group(1));
			String[] genes=m.group(1).trim().split(",");
			for (int i = 0; i < genes.length; i++) {
				genenames.add(genes[i].trim());
			}
			
			//System.out.println("Conditions#: "+m.group(2));
			String[] conds=m.group(2).trim().split(",");
			
			for (int j = 0; j < conds.length; j++) {
				String val=conds[j].trim();
				condindexes.add(Integer.parseInt(val));		
			}
			//System.out.println("AA#: "+m.group(3));
			asr=m.group(3);
		}
		
		BiclusterResult res =new BiclusterResult(expressionset, genenames,null, null, condindexes);
		if(asr!=null)
		   res.appendAdditionalInfo("Average Spearman’s Rho",Double.parseDouble(asr));
		
		return res;
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the bi mine plus method
	 */
	public static BiMinePlusMethod newInstance(ExpressionData data){
		return new BiMinePlusMethod(data);
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @param props the props
	 * @return the bi mine plus method
	 */
	public static BiMinePlusMethod newInstance(ExpressionData data, Properties props){
		return new BiMinePlusMethod(data,props);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
	 */
	@Override
	protected String getRunningTime() {
		return runningtime;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return tmpfolder;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(MINCONDITIONS, minconds);
		parameters.setKeydoubleValue(ASR, asr);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new BiMinePlusMethod(minconds, asr, javaxmx);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportDefineNumberBiclustersToFind()
	 */
	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		return false;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#changeNumberBiclusterToFind(int)
	 */
	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		// TODO Auto-generated method stub
		
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
