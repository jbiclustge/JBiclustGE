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
package jbiclustge.methods.algorithms.r.biclustpackage;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class RQuestMethod.
 */
public class RQuestMethod extends RBiclustAlgorithmCaller{
	
	/** The bcquest ns. */
	public static String BCQUEST_NS="bcquest_number_of_questions_choosen";
	
	/** The bcquest nd. */
	public static String BCQUEST_ND="bcquest_number_of_repetitions";
	
	/** The bcquest sd. */
	public static String BCQUEST_SD="bcquest_sample_size_in_repetitions";
	
	/** The bcquest alpha. */
	public static String BCQUEST_ALPHA="bcquest_scaling_factor_for_column_result";
	
	/** The bcquest nclusters. */
	public static String BCQUEST_NCLUSTERS="bcquest_number_of_bicluster_to_be_found";
	
	/** The Constant NAME. */
	public static final String NAME="BCQuest";
	
	
	/** The nquestions. */
	protected  int nquestions=10;
	
	/** The nrepetitions. */
	protected int nrepetitions=10;
	
	/** The samplesizerepetitions. */
	protected  int samplesizerepetitions=5;
	
	/** The alpha. */
	protected  double alpha=0.05;
    
    /** The nbics. */
    protected int nbics=10;
	
	
	

	/**
	 * Instantiates a new r quest method.
	 */
	public RQuestMethod() {
		super();
	}

	
	/**
	 * Instantiates a new r quest method.
	 *
	 * @param props the props
	 */
	public RQuestMethod(Properties props) {
		super(props);
	}
	
	/**
	 * Instantiates a new r quest method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RQuestMethod(String propertiesfile) {
		super(propertiesfile);
	}
	
	/**
	 * Instantiates a new r quest method.
	 *
	 * @param exprs the exprs
	 */
	public RQuestMethod(ExpressionData exprs) {
		super(exprs);
	}

	/**
	 * Instantiates a new r quest method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RQuestMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	


	/**
	 * Instantiates a new r quest method.
	 *
	 * @param nbics the nbics
	 * @param nquestions the nquestions
	 * @param nrepetitions the nrepetitions
	 * @param samplesizerepetitions the samplesizerepetitions
	 * @param alpha the alpha
	 */
	private RQuestMethod(int nbics, int nquestions, int nrepetitions, int samplesizerepetitions, double alpha) {
		super();
		this.nbics=nbics;
		this.nquestions = nquestions;
		this.nrepetitions = nrepetitions;
		this.samplesizerepetitions = samplesizerepetitions;
		this.alpha = alpha;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param number the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int number){
		nbics=number;
	}
	
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param nclust the nclust
	 * @return the r quest method
	 */
	public RQuestMethod addNumberBiclustersToFind(int nclust){
		setNumberBiclustersToFind(nclust);
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
	 * Sets the number questions.
	 *
	 * @param nquestions the new number questions
	 */
	public void setNumberQuestions(int nquestions) {
		this.nquestions = nquestions;
	}
	
	/**
	 * Adds the number questions.
	 *
	 * @param nquestions the nquestions
	 * @return the r quest method
	 */
	public RQuestMethod addNumberQuestions(int nquestions) {
		this.nquestions = nquestions;
		return this;
	}

	/**
	 * Sets the number repetitions.
	 *
	 * @param nrepetitions the new number repetitions
	 */
	public void setNumberRepetitions(int nrepetitions) {
		this.nrepetitions = nrepetitions;
	}
	
	/**
	 * Adds the number repetitions.
	 *
	 * @param nrepetitions the nrepetitions
	 * @return the r quest method
	 */
	public RQuestMethod addNumberRepetitions(int nrepetitions) {
		this.nrepetitions = nrepetitions;
		return this;
	}


	/**
	 * Sets the sample size repetitions.
	 *
	 * @param samplesizerepetitions the new sample size repetitions
	 */
	public void setSampleSizeRepetitions(int samplesizerepetitions) {
		this.samplesizerepetitions = samplesizerepetitions;
	}
	
	/**
	 * Adds the sample size repetitions.
	 *
	 * @param samplesizerepetitions the samplesizerepetitions
	 * @return the r quest method
	 */
	public RQuestMethod addSampleSizeRepetitions(int samplesizerepetitions) {
		this.samplesizerepetitions = samplesizerepetitions;
		return this;
	}


	/**
	 * Sets the alpha.
	 *
	 * @param alpha the new alpha
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * Adds the alpha.
	 *
	 * @param alpha the alpha
	 * @return the r quest method
	 */
	public RQuestMethod addAlpha(double alpha) {
		this.alpha = alpha;
		return this;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#requiredLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> requiredLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("biclust",RPackageInfo.define("flexclust")));
		return list;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.nquestions=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BCQUEST_NS, 10, 1,true, this.getClass());
		this.nrepetitions=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BCQUEST_ND, 10, 1,true, this.getClass());
		this.samplesizerepetitions=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BCQUEST_SD, 5, 1,true, this.getClass());
		this.alpha=PropertiesUtilities.getDoublePropertyValueValidLimits(props, BCQUEST_ALPHA, 0.05, 0, 1, true, this.getClass());
		this.nbics=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BCQUEST_NCLUSTERS, 10, 2,true, this.getClass());
		
	}

	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				BCQUEST_NS,
				BCQUEST_ND,
				BCQUEST_SD,
				BCQUEST_ALPHA,
				BCQUEST_NCLUSTERS
		};
		String[] defaultvalues=new String[]{"10","10","5","0.05","10"};
		String[] comments=new String[] {
				"Number of questions choosen",
				"Number of repetitions",
				"Sample size in repetitions",
				"Scaling factor for column result",
				"Number of bicluster to be found"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		try {
			loadExpressionMatrixInREnvironment();

			Instant start = Instant.now();
			rsession.silentlyEval(""+getResultOutputName()+" <- biclust("+inputmatrixname+", "+getInputParametersString()+")",true);
			
			saveElapsedTime(start);
			
	        writeBiclusterResultsToFileWithOriginalAlgorithmMethod();
			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": ", e);
			return false;
		}
		
		
		return true;
	}
	
	/**
	 * Gets the input parameters string.
	 *
	 * @return the input parameters string
	 */
	protected String getInputParametersString(){
		return "method=BCQuest(), ns="+String.valueOf(nquestions)+", nd="+String.valueOf(nrepetitions)+", sd="+String.valueOf(samplesizerepetitions)+", alpha="+String.valueOf(alpha)+", number="+String.valueOf(nbics)+"";
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		return getBaseROjects();
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		getListOfBiclusters(getResultOutputName());
		
	}

    /**
     * New instance.
     *
     * @param data the data
     * @return the r quest method
     */
    public static RQuestMethod newInstance(ExpressionData data){
    	return new RQuestMethod(data);
    }


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(BCQUEST_NS, nquestions);
		parameters.setKeyintValue(BCQUEST_ND, nrepetitions);
		parameters.setKeyintValue(BCQUEST_SD, samplesizerepetitions);
		parameters.setKeydoubleValue(BCQUEST_ALPHA, alpha);
		parameters.setKeyintValue(BCQUEST_NCLUSTERS, nbics);
		return parameters;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RQuestMethod(nbics, nquestions, nrepetitions, samplesizerepetitions, alpha);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		return null;
	}


	
	




}
