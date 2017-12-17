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
package jbiclustge.methods.algorithms.wrappers.debi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.transformdata.binarization.IDiscretizationMethod;
import jbiclustge.datatools.expressiondata.transformdata.binarization.methods.BiMaxBinarizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.wrappers.IBiclustWrapper;
import jbiclustge.methods.algorithms.wrappers.components.RegulationPattern;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.threadutils.MTUMultiThreadCallableExecutor;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DebiMethod.
 */
public class DebiMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	
	/** The Constant NAME. */
	public static final String NAME="DeBi";
	
	/** The Constant MAXOVERLAP. */
	public static final String MAXOVERLAP="maximum_overlap_level";
	
	/** The Constant BINARIZATIONLEVEL. */
	public static final String BINARIZATIONLEVEL="binarization_level";
	
	/** The Constant REGULATIONPATTERN. */
	public static final String REGULATIONPATTERN="pattern_of_regulation";
	
	/** The Constant MINCONDSBIC. */
	public static final String MINCONDSBIC="minimum_conditions_per_bicluster";
	
	/** The Constant BINARIZEWITHBIMAXMETHOD. */
	public static final String BINARIZEWITHBIMAXMETHOD="binarize_using_bimax_method";
	
	/** The pattern. */
	private RegulationPattern pattern=RegulationPattern.BOTH;
	
	/** The maxoverlap. */
	private double maxoverlap=0.5;
	
	/** The binarizationlevel. */
	private double binarizationlevel=1.0;
	
	/** The mincondsbic. */
	private int mincondsbic=0;
	
	/** The discretedata. */
	//private double initalpha=0.01;
	protected boolean discretedata=false;
	
	/** The binarizationmethod. */
	private IDiscretizationMethod binarizationmethod;
	
	/** The workingpath. */
	private String workingpath;
	
	/** The outputfilepath. */
	private String outputfilepath;
	
	/** The datamatrixfilepath. */
	protected String datamatrixfilepath=null;
	
	private ArrayList<DebiTask> tasks;
	
	/**
	 * Instantiates a new debi method.
	 */
	public DebiMethod(){
		super();
	}
	
	/**
	 * Instantiates a new debi method.
	 *
	 * @param exprs the exprs
	 */
	public DebiMethod(ExpressionData exprs){
		super(exprs);
	}
	
	/**
	 * Instantiates a new debi method.
	 *
	 * @param properties the properties
	 */
	public DebiMethod(Properties properties){
		super(properties);
	}
	
	/**
	 * Instantiates a new debi method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public DebiMethod(ExpressionData exprs, Properties props){
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new debi method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public DebiMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	/**
	 * Instantiates a new debi method.
	 *
	 * @param exprs the exprs
	 * @param discretemethod the discretemethod
	 */
	public DebiMethod(ExpressionData exprs, IDiscretizationMethod discretemethod){
		super(exprs);
		this.binarizationmethod=discretemethod;
		this.discretedata=true;
	}
	
	
	

	/**
	 * Instantiates a new debi method.
	 *
	 * @param pattern the pattern
	 * @param maxoverlap the maxoverlap
	 * @param binarizationlevel the binarizationlevel
	 * @param mincondsbic the mincondsbic
	 * @param discretedata the discretedata
	 * @param binarizationmethod the binarizationmethod
	 */
	private DebiMethod(RegulationPattern pattern, double maxoverlap, double binarizationlevel, int mincondsbic,
			boolean discretedata, IDiscretizationMethod binarizationmethod) {
		super();
		this.pattern = pattern;
		this.maxoverlap = maxoverlap;
		this.binarizationlevel = binarizationlevel;
		this.mincondsbic = mincondsbic;
		this.discretedata = discretedata;
		this.binarizationmethod = binarizationmethod;
	}

	/**
	 * Sets the regulation pattern.
	 *
	 * @param pattern the new regulation pattern
	 */
	public void setRegulationPattern(RegulationPattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * Sets the max overlap.
	 *
	 * @param maxoverlap the new max overlap
	 */
	public void setMaxOverlap(double maxoverlap) {
		this.maxoverlap = maxoverlap;
	}

	/**
	 * Sets the binarization level.
	 *
	 * @param binarizationlevel the new binarization level
	 */
	public void setBinarizationLevel(double binarizationlevel) {
		this.binarizationlevel = binarizationlevel;
	}

	/**
	 * Sets the minconditions bicluster.
	 *
	 * @param mincondsbic the new minconditions bicluster
	 */
	public void setMinconditionsBicluster(int mincondsbic) {
		this.mincondsbic = mincondsbic;
	}

	/*public void setInitialAlpha(double initalpha) {
		this.initalpha = initalpha;
	}*/

	/**
	 * Sets the discretization method.
	 *
	 * @param discretemethod the new discretization method
	 */
	public void setDiscretizationMethod(IDiscretizationMethod discretemethod){
		this.binarizationmethod=discretemethod;
		this.discretedata=true;
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
				MAXOVERLAP,
				BINARIZATIONLEVEL,
				REGULATIONPATTERN,
				MINCONDSBIC,
				BINARIZEWITHBIMAXMETHOD
		};
		
		
		String[] defaultvalues=new String[]{"0.5","1.0","both","0","false"};
		
		String[] comments=new String[] {
				"Maximum overlap level of the biclusters default=0.5 (overlap=1: no overlap is allowed, overlap=0: all the biclusters are taken, no filtering applied)",
				"binarization level (for binary data not needed otherwise it must be defined)",
				"Pattern of regulation up, down or both (up, down, both) default=both",
				"Minimum number of conditions in biclusters, if 0, it is automatically calculated, but the minimum value will be allways 5,  default=0",
				"Use same binarization method of bimax"
			
		};
		
		String source="source: https://owww.molgen.mpg.de/~serin/debi/main.html";
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,source);
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.mincondsbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINCONDSBIC, 0, 0, true, getClass());
		this.maxoverlap=PropertiesUtilities.getDoublePropertyValueValidLimits(props, MAXOVERLAP, 0.5, 0.0, true, 1, false, getClass());
		this.binarizationlevel=PropertiesUtilities.getDoublePropertyValue(props, BINARIZATIONLEVEL, 1.0, getClass());
		String regpat=PropertiesUtilities.getStringPropertyValue(props, REGULATIONPATTERN, "both", getClass());
		if(regpat!=null)
			pattern=RegulationPattern.getRegulationPatternFromString(regpat);
		this.discretedata=PropertiesUtilities.getBooleanPropertyValue(props, BINARIZEWITHBIMAXMETHOD, false, getClass());
		if(discretedata)
			this.binarizationmethod=BiMaxBinarizationMethod.newInstance();
			
	}
	

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return "debi";
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
	@Override
	protected boolean runAlgorithm() throws Exception {
		boolean valid=false;
		configurePaths();
		Date starttime =Calendar.getInstance().getTime();
		tasks=new ArrayList<>();
		if(pattern.equals(RegulationPattern.BOTH)){
			
			tasks.add(new DebiTask(expressionset,getBinaryExecutablePath(), datamatrixfilepath, outputfilepath, RegulationPattern.UP, maxoverlap, binarizationlevel, mincondsbic, discretedata));
			tasks.add(new DebiTask(expressionset,getBinaryExecutablePath(), datamatrixfilepath,outputfilepath, RegulationPattern.DOWN, maxoverlap, binarizationlevel, mincondsbic,discretedata));
			
			ArrayList<Pair<Boolean,BiclusterList>> results=(ArrayList<Pair<Boolean, BiclusterList>>) MTUMultiThreadCallableExecutor.run(tasks, 2);
			
			valid=results.get(0).getValue0()&&results.get(1).getValue0();
			if(valid){
				listofbiclusters=new BiclusterList();
				listofbiclusters.addAll(results.get(0).getValue1());
				listofbiclusters.addAll(results.get(1).getValue1());
			}
			Date endtime=Calendar.getInstance().getTime();
			long runtime=endtime.getTime()-starttime.getTime();	
			runningtime=MTUTimeUtils.getTimeElapsed(runtime);
			return valid;	
		}
		else{
			
			DebiTask task=new DebiTask(expressionset,getBinaryExecutablePath(), datamatrixfilepath, outputfilepath, pattern, maxoverlap, binarizationlevel, mincondsbic, discretedata);
			tasks.add(task);
			Pair<Boolean,BiclusterList> result=task.call();
			valid=result.getValue0();
			if(valid)
				listofbiclusters=result.getValue1();
			
			Date endtime=Calendar.getInstance().getTime();
			long runtime=endtime.getTime()-starttime.getTime();	
			runningtime=MTUTimeUtils.getTimeElapsed(runtime);
			return valid;
		}

	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		// TODO Auto-generated method stub
		
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
		return workingpath;
	}
	
    /**
     * Configure paths.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void configurePaths() throws IOException{
    	workingpath=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix(getAlgorithmName());
    	outputfilepath=FilenameUtils.concat(workingpath, getAlgorithmName()+"_output.txt");
    	datamatrixfilepath=FilenameUtils.concat(workingpath, UUID.randomUUID().toString()+".txt");
    	
    	if(binarizationmethod!=null)
			expressionset.writeExpressionDatasetToFile(datamatrixfilepath, binarizationmethod);
		else
			expressionset.writeExpressionDatasetToFile(datamatrixfilepath);
    }

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		LinkedHashMap<String, String> param=new LinkedHashMap<>();
		param.put("Max overlap level", String.valueOf(maxoverlap));
		param.put("Binarization level", String.valueOf(binarizationlevel));
		param.put("Min conditions bicluster", String.valueOf(mincondsbic));
		param.put("Regulation pattern", pattern.getName());
		param.put("Use Bimax binarization method", String.valueOf(discretedata));
		return param;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new DebiMethod(pattern, maxoverlap, binarizationlevel, mincondsbic, discretedata, binarizationmethod);
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
		for (DebiTask t : tasks) {
			t.killtask();
		}
		
	}
	
	

}
