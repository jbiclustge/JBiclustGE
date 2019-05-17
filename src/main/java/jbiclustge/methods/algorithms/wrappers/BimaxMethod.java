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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import jbiclustge.analysis.overlap.OverlapAnalyser;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.processdata.binarization.IDiscretizationMethod;
import jbiclustge.datatools.expressiondata.processdata.binarization.methods.BiMaxBinarizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.props.AlgorithmProperties;
import jbiclustge.utils.props.CommandsProcessList;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;

// TODO: Auto-generated Javadoc
/**
 * The Class BimaxMethod.
 */
public class BimaxMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{
	
	/** The Constant MINGENES. */
	public static final String MINGENES="biclusters_have_at_least_x_genes";
	
	/** The Constant MINCONDITIONS. */
	public static final String MINCONDITIONS="biclusters_have_at_least_x_conditions";
	
	/** The Constant MAXNUMBERBICLUSTERS. */
	public static final String MAXNUMBERBICLUSTERS="max_number_biclusters";
	
	/** The Constant OVERLAPTHRESHOLD. */
	public static final String OVERLAPTHRESHOLD="overlap_threshold";
	
	
	public static final String BIMAXBINARIZATIONTHRESHOLD="binarization_threshold";
	
	/** The Constant NAME. */
	public static final String NAME="Bimax";
	
	/** The binarizationmethod. */
	private IDiscretizationMethod binarizationmethod=BiMaxBinarizationMethod.newInstance();
	
	/** The mingenes. */
	private int mingenes=0;
	
	/** The minconditions. */
	private int minconditions=0;
	
	/** The maxnumberbiclusters. */
	private int maxnumberbiclusters=0;
	
	private double binarizationthreshold=0;
	
	/** The overlapthreshold. */
	private double overlapthreshold=1.0;
	
	/** The workingpath. */
	private String workingpath;

	/** The datafilepath. */
	private String datafilepath=null;
	
	/** The outputfilepath. */
	private String outputfilepath=null;
	
	/** The resultsfilepath. */

	private CommandsProcessList cmds;
	
	
	private Thread stop;
	
	/**
	 * Instantiates a new bimax method.
	 */
	public BimaxMethod() {
		super();
	}

	/**
	 * Instantiates a new bimax method.
	 *
	 * @param exprs the exprs
	 */
	public BimaxMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	/**
	 * Instantiates a new bimax method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public BimaxMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new bimax method.
	 *
	 * @param props the props
	 */
	public BimaxMethod(Properties props) {
		super(props);
	}

	/**
	 * Instantiates a new bimax method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public BimaxMethod(String propertiesfile) {
		super(propertiesfile);
	}
	
	/**
	 * Instantiates a new bimax method.
	 *
	 * @param exprs the exprs
	 * @param binarizationmethod the binarizationmethod
	 */
	public BimaxMethod(ExpressionData exprs, IDiscretizationMethod binarizationmethod) {
		super(exprs);
		this.binarizationmethod=binarizationmethod;
	}
	
	
	
	/**
	 * Instantiates a new bimax method.
	 *
	 * @param binarizationmethod the binarizationmethod
	 * @param mingenes the mingenes
	 * @param minconditions the minconditions
	 * @param maxnumberbiclusters the maxnumberbiclusters
	 * @param overlapthreshold the overlapthreshold
	 */
	private BimaxMethod(IDiscretizationMethod binarizationmethod, int mingenes, int minconditions,
			int maxnumberbiclusters, double overlapthreshold) {
		super();
		this.binarizationmethod = binarizationmethod;
		this.mingenes = mingenes;
		this.minconditions = minconditions;
		this.maxnumberbiclusters = maxnumberbiclusters;
		this.overlapthreshold = overlapthreshold;
	}

	/**
	 * Sets the bicluster min genes.
	 *
	 * @param mingenes the new bicluster min genes
	 */
	public void setBiclusterMinGenes(int mingenes) {
		this.mingenes = mingenes;
	}
	
	/**
	 * Adds the bicluster min genes.
	 *
	 * @param mingenes the mingenes
	 * @return the bimax method
	 */
	public BimaxMethod addBiclusterMinGenes(int mingenes) {
		this.mingenes = mingenes;
		return this;
	}

	/**
	 * Sets the bicluster min conditions.
	 *
	 * @param minconditions the new bicluster min conditions
	 */
	public void setBiclusterMinConditions(int minconditions) {
		this.minconditions = minconditions;
	}
	
	/**
	 * Adds the bicluster min conditions.
	 *
	 * @param minconditions the minconditions
	 * @return the bimax method
	 */
	public BimaxMethod addBiclusterMinConditions(int minconditions) {
		this.minconditions = minconditions;
		return this;
	}
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param nbic the nbic
	 * @return the bimax method
	 */
	public BimaxMethod addNumberBiclustersToFind(int nbic){
		this.maxnumberbiclusters=nbic;
		return this;
	}
	
	/**
	 * Sets the overlap treshold.
	 *
	 * @param value the new overlap treshold
	 */
	public void setOverlapTreshold(double value){
		this.overlapthreshold=value;
	}
	
	
	
	public void setBinarizationThreshold(double value) {
		this.binarizationthreshold=value;
	}
	/**
	 * Sets the number biclusters to find.
	 *
	 * @param nbic the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int nbic){
		this.maxnumberbiclusters=nbic;
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
				MINGENES,
				MINCONDITIONS,
				MAXNUMBERBICLUSTERS,
				BIMAXBINARIZATIONTHRESHOLD,
				OVERLAPTHRESHOLD
		};
		
		
		String[] defaultvalues=new String[]{"0","0","0","0","1.0"};
		
		String[] comments=new String[] {
				"biclusters output need to have at least that many genes, 0= no limits",
				"biclusters output need to have at least that many conditions, 0= no limits",
				"number of bicluster to find, (default=0 find maximum number of biclusters)",
				"threshold to be applied in the binarization procedure (default=0,  find a threshold automatically)",
				"overlap threshold, value between [0,1] interval. If value is equal to 1.0, the algorithm will return the maximum number of biclusters defined by user (default=1.0)\n.Otherwise only return the biclusters which respect overlap threshold"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return "jbimax";
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryExecutablePath()
	 */
	@Override
	public String getBinaryExecutablePath() throws IOException {
		String path=SystemFolderTools.getBinaryFilePathFromPropertiesFile(getAlgorithmName().toLowerCase());
        if(path==null)
        	path=SystemFolderTools.getMethodExePath(getBinaryName());
		return path;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		this.mingenes=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINGENES, 0,0,true,this.getClass());
		this.minconditions=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props,MINCONDITIONS, 0,0,true,this.getClass());
		this.maxnumberbiclusters=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props,MAXNUMBERBICLUSTERS, 0,0,true,this.getClass());
		this.binarizationthreshold=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, BIMAXBINARIZATIONTHRESHOLD, 0.0, 0.0, true, this.getClass());
		this.overlapthreshold=PropertiesUtilities.getDoublePropertyValueValidLimits(props, OVERLAPTHRESHOLD, 1.0, 0.0, 1.0, true, getClass());

	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@SuppressWarnings("resource")
	@Override
	protected boolean runAlgorithm() throws Exception {
		configureInputData();
		preconfigureAlgorithm();
		
		ProcessBuilder build= new ProcessBuilder(cmds);
		build.directory(new File(workingpath));
		
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
			    SystemFolderTools.deleteTempDir(workingpath);
			  }
			});
		
		stop=new Thread(new Runnable() {
			
			@Override
			public void run() {
				 p.destroy();
				 SystemFolderTools.deleteTempDir(workingpath);
				 LogMessageCenter.getLogger().addInfoMessage(NAME+" was forced to stop");
				
			}
		});

		int exitval=p.waitFor();
		//System.out.println("EXIT: "+exitval);
		
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
	protected void processResults() throws Exception {
		parseResultsFile();
		
		if(overlapthreshold<1.0 && overlapthreshold>=0)
			listofbiclusters=OverlapAnalyser.filterBiclusterListWithOverlapThreshold(listofbiclusters, overlapthreshold, maxnumberbiclusters);
		SystemFolderTools.deleteTempDir(workingpath);
	
	}


	
	/**
	 * Parses the results file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void parseResultsFile() throws IOException{
		this.listofbiclusters=new BiclusterList();
		ArrayList<String> lines=(ArrayList<String>) MTUReadUtils.readFileLines(outputfilepath);
		
		ArrayList<Integer> geneindexes=null;
		ArrayList<Integer> condindexed=null;
		int n=0;
		for (int i = 0; i < lines.size(); i++) {
			
			String line=lines.get(i);
			if(line!=null && !line.isEmpty()){
				if(line.contains("bic")){
					if(geneindexes!=null && condindexed!=null){
						BiclusterResult res=new BiclusterResult(expressionset, geneindexes, condindexed, true);
						listofbiclusters.add(res);
					}
					geneindexes=new ArrayList<>();
					condindexed=new ArrayList<>();
					n=0;
				}
				else{
					String[] elems=line.trim().split("\t");
					ArrayList<Integer> tmp=new ArrayList<>();
					for (int j = 0; j < elems.length; j++) {
						tmp.add(Integer.parseInt(elems[j])-1);	
					}
					
					Collections.sort(tmp);
					
					if(n==1){
						geneindexes=tmp;
					}
					else if(n==2){
						condindexed=tmp;
					}
				}
				n++;
			}
			
		}
		BiclusterResult res=new BiclusterResult(expressionset, geneindexes, condindexed, true);
		listofbiclusters.add(res);

	}
	
	
	/**
	 * Configure input data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void configureInputData() throws IOException{
		workingpath=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("BIMAX");
		datafilepath=FilenameUtils.concat(workingpath, "matrix.txt");
		outputfilepath=FilenameUtils.concat(workingpath, "results.txt");
		String header=expressionset.numberGenes()+" "+expressionset.numberConditions()+" "+mingenes+" "+minconditions+" "+maxnumberbiclusters;
		ArrayList<String> first=new ArrayList<>();
		first.add(header);
		
		if(binarizationthreshold>0)
			binarizationmethod=new BiMaxBinarizationMethod().setThreshold(binarizationthreshold);
			
		expressionset.writeBinarizedExpressionMatrixtoFile(datafilepath, binarizationmethod, " ",first);
	}
	
	
	/**
	 * Preconfigure algorithm.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void preconfigureAlgorithm() throws IOException{
		cmds=new CommandsProcessList();
		cmds.add(getBinaryExecutablePath());
		cmds.add(datafilepath);
		cmds.add(outputfilepath);

	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the bimax method
	 */
	public static BimaxMethod newInstance(ExpressionData data){
    	return new BimaxMethod(data);
    }
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @param props the props
	 * @return the bimax method
	 */
	public static BimaxMethod newInstance(ExpressionData data, Properties props){
    	return new BimaxMethod(data,props);
    }
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @param props the props
	 * @return the bimax method
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BimaxMethod newInstance(ExpressionData data, String props) throws FileNotFoundException, IOException{
    	return new BimaxMethod(data,PropertiesUtilities.loadFileProperties(props));
    }

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		//return workingpath;
		return null;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		LinkedHashMap<String, String> param=new LinkedHashMap<>();
		param.put("Min genes", String.valueOf(mingenes));
		param.put("Min conditions", String.valueOf(minconditions));
		param.put("Max number biclusters", String.valueOf(maxnumberbiclusters));
		param.put(OVERLAPTHRESHOLD, String.valueOf(overlapthreshold));
		return param;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new BimaxMethod(binarizationmethod, mingenes, minconditions, maxnumberbiclusters,overlapthreshold);
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
