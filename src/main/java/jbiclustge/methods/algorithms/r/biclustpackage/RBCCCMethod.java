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

import org.apache.commons.lang3.ArrayUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.processdata.binarization.IDiscretizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.rtools.JavaToRUtils;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.arrays.MTUMatrixUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class RBCCCMethod.
 */
public class RBCCCMethod extends RBiclustAlgorithmCaller{
	
	/** The bcc alpha. */
	public static String BCC_ALPHA="bcc_scaling";
	
	/** The bcc delta. */
	public static String BCC_DELTA="bcc_maximum_of_accepted_score";
	
	/** The bcc nclusters. */
	public static String BCC_NCLUSTERS="bcc_number_of_bicluster_to_be_found";
	
	/** The bcc discretize levels. */
	public static String BCC_DISCRETIZE_LEVELS="bcc_number_of_discretization_levels";
	
	/** The bcc discretize quantile. */
	public static String BCC_DISCRETIZE_QUANTILE="bcc_use_quantiles_in_discretization";

	/** The Constant NAME. */
	public static final String NAME="BCCC";
	
	/** The externalbinmethod. */
	private IDiscretizationMethod externalbinmethod=null;
	

	
	/** The maxscore. */
	private double maxscore=1.0;
	
	/** The scalefactor. */
	private double scalefactor=1.5;
	
	/** The disclevels. */
	private int disclevels=10;
	
	/** The discquantile. */
	private boolean discquantile=false;
	
	/** The nbics. */
	private int nbics=100;
	
	
	
	/**
	 * Instantiates a new RBCCC method.
	 */
	public RBCCCMethod() {
		super();
	}


	/**
	 * Instantiates a new RBCCC method.
	 *
	 * @param exprs the exprs
	 */
	public RBCCCMethod(ExpressionData exprs) {
		super(exprs);
	}
	

	/**
	 * Instantiates a new RBCCC method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RBCCCMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}

	/**
	 * Instantiates a new RBCCC method.
	 *
	 * @param props the props
	 */
	public RBCCCMethod(Properties props){
		super(props);
	}
	
	/**
	 * Instantiates a new RBCCC method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RBCCCMethod(String propertiesfile){
		super(propertiesfile);
	}

	
	
	/**
	 * Instantiates a new RBCCC method.
	 *
	 * @param externalbinmethod the externalbinmethod
	 * @param maxscore the maxscore
	 * @param scalefactor the scalefactor
	 * @param disclevels the disclevels
	 * @param discquantile the discquantile
	 * @param nbics the nbics
	 */
	private RBCCCMethod(IDiscretizationMethod externalbinmethod, double maxscore, double scalefactor, int disclevels,
			boolean discquantile, int nbics) {
		super();
		this.externalbinmethod = externalbinmethod;
		this.maxscore = maxscore;
		this.scalefactor = scalefactor;
		this.disclevels = disclevels;
		this.discquantile = discquantile;
		this.nbics=nbics;
		
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


	
	/**
	 * Sets the number biclusters to find.
	 *
	 * @param nbics the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int nbics) {
		this.nbics = nbics;
	}


	/**
	 * Sets the scalingfactor columns.
	 *
	 * @param scalefactor the new scalingfactor columns
	 */
	public void setScalingfactorColumns(double scalefactor) {
		this.scalefactor = scalefactor;
	}

	/**
	 * Adds the scalingfactor columns.
	 *
	 * @param scalefactor the scalefactor
	 * @return the RBCCC method
	 */
	public RBCCCMethod addScalingfactorColumns(double scalefactor) {
		this.scalefactor = scalefactor;
		return this;
	}


	/**
	 * Sets the maximum score.
	 *
	 * @param maxscore the new maximum score
	 */
	public void setMaximumScore(double maxscore) {
		this.maxscore = maxscore;
	}
	
	/**
	 * Adds the maximum score.
	 *
	 * @param maxscore the maxscore
	 * @return the RBCCC method
	 */
	public RBCCCMethod addMaximumScore(double maxscore) {
		this.maxscore = maxscore;
		return this;
	}
	
	/**
	 * Sets the external binarization method.
	 *
	 * @param method the new external binarization method
	 */
	public void setExternalBinarizationMethod(IDiscretizationMethod method){
		this.externalbinmethod=method;
	}

	/**
	 * Adds the external binarization method.
	 *
	 * @param method the method
	 * @return the RBCCC method
	 */
	public RBCCCMethod addExternalBinarizationMethod(IDiscretizationMethod method){
		this.externalbinmethod=method;
		return this;
	}
	
	/**
	 * Sets the discretize levels.
	 *
	 * @param levels the new discretize levels
	 */
	public void setDiscretizeLevels(int levels){
		this.disclevels=levels;
	}
	
	/**
	 * Adds the discretize levels.
	 *
	 * @param levels the levels
	 * @return the RBCCC method
	 */
	public RBCCCMethod addDiscretizeLevels(int levels){
		this.disclevels=levels;
		return this;
	}
	
	
	/**
	 * Sets the use quantiles in data discretization.
	 *
	 * @param bol the new use quantiles in data discretization
	 */
	public void setuseQuantilesInDataDiscretization(boolean bol){
		this.discquantile=bol;
	}
	
	/**
	 * Use quantiles in data discretization.
	 *
	 * @param bol the bol
	 * @return the RBCCC method
	 */
	public RBCCCMethod useQuantilesInDataDiscretization(boolean bol){
		this.discquantile=bol;
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
	

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.nbics=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BCC_NCLUSTERS, 100, 2,true, this.getClass());
		this.maxscore=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props,BCC_DELTA, 1.0, 0, false, this.getClass());
		this.scalefactor=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, BCC_ALPHA, 1.5, 0, false, this.getClass());
		this.disclevels=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BCC_DISCRETIZE_LEVELS, 10, 1, true, getClass());
		this.discquantile=PropertiesUtilities.getBooleanPropertyValue(props, BCC_DISCRETIZE_QUANTILE, false, getClass());
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				BCC_NCLUSTERS,
				BCC_DELTA,
				BCC_ALPHA,
				BCC_DISCRETIZE_LEVELS,
				BCC_DISCRETIZE_QUANTILE,
		};
		String[] defaultvalues=new String[]{"100","1.0","1.5","","false"};
		String[] comments=new String[] {
				"Number of bicluster to be found",
				"Maximum of accepted score (delta)",
				"Scaling factor (alpha)",
				"Number of discretization levels, no value = default value = 10",
				"Use the quantiles, else uses equally spaced levels"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		try {
			
		/*	if(externalbinmethod==null && !discquantile){
				externalbinmethod=new DiscretizeByLevelsMethod(disclevels);
			}*/
			
			
			if(externalbinmethod!=null){
				
				double[][] binmatrix=MTUMatrixUtils.convertIntToDoubleMatrix(expressionset.getBinarizedMatrix(externalbinmethod));
				
				rsession.set(inputmatrixname, binmatrix);
				rsession.silentlyVoidEval("disc_"+inputmatrixname+" <- "+inputmatrixname+"");
			}
			else
				loadLabeledExpressionMatrixInREnvironment();
				//loadExpressionMatrixInREnvironment();
			
				

			
			if(externalbinmethod==null){
				if(disclevels!=10 || discquantile)
					rsession.silentlyVoidEval("disc_"+inputmatrixname+"<-discretize("+inputmatrixname+", nof="+String.valueOf(disclevels)+", quant="+JavaToRUtils.convertBooleanToR(discquantile)+")");
				else
					rsession.silentlyVoidEval("disc_"+inputmatrixname+"<-discretize("+inputmatrixname+")");
					
			}
			
			
			Instant start = Instant.now();
			
			rsession.silentlyEval(getResultOutputName()+" <- biclust("+"disc_"+inputmatrixname+", method=BCCC(), number="+String.valueOf(nbics)+", alpha="+String.valueOf(scalefactor)+", delta="+String.valueOf(maxscore)+")",true);
			
			saveElapsedTime(start);
			
	        writeBiclusterResultsToFileWithOriginalAlgorithmMethod();
			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": ", e);
			return false;
		}
		
		
		return true;
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
	 * @return the RBCCC method
	 */
	public static RBCCCMethod newInstance(ExpressionData data){
		return new RBCCCMethod(data);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		
		String[] ccRobjects=new String[]{("disc_"+inputmatrixname)};
		
		return ArrayUtils.addAll(getBaseROjects(), ccRobjects);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(BCC_NCLUSTERS, nbics);
		parameters.setKeydoubleValue(BCC_DELTA, maxscore);
		parameters.setKeydoubleValue(BCC_ALPHA, scalefactor);
		parameters.setKeyintValue(BCC_DISCRETIZE_LEVELS, disclevels);
		parameters.setKeybooleanValue(BCC_DISCRETIZE_QUANTILE, discquantile);
		return parameters;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RBCCCMethod(externalbinmethod, maxscore, scalefactor, disclevels, discquantile,nbics);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		return null;
	}



	
	

	
	

}
