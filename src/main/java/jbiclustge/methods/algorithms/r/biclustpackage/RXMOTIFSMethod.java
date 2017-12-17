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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.transformdata.binarization.IDiscretizationMethod;
import jbiclustge.datatools.expressiondata.transformdata.binarization.methods.BiMaxBinarizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.rtools.JavaToRUtils;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.arrays.MTUMatrixUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RXMOTIFSMethod.
 */
public class RXMOTIFSMethod extends RBiclustAlgorithmCaller{

	/** The xmotifs discretize bimax method. */
	public static String XMOTIFS_DISCRETIZE_BIMAX_METHOD="xmotifs_discretize_using_bimax_method";
	
	/** The xmotifs discretize level. */
	public static String XMOTIFS_DISCRETIZE_LEVEL="xmotifs_number_of_discretization_levels";
	
	/** The xmotifs discretize with quantiles. */
	public static String XMOTIFS_DISCRETIZE_WITH_QUANTILES="xmotifs_use_quantiles_in_discretization";
	
	/** The xmotifs ns. */
	public static String XMOTIFS_NS="xmotifs_number_of_columns_choosen";
	
	/** The xmotifs nd. */
	public static String XMOTIFS_ND="xmotifs_number_of_repetitions";
	
	/** The xmotifs sd. */
	public static String XMOTIFS_SD="xmotifs_sample_size_in_repetitions";
	
	/** The xmotifs alpha. */
	public static String XMOTIFS_ALPHA="xmotifs_scaling_factor_for_column_result";
	
	/** The xmotifs nclusters. */
	public static String XMOTIFS_NCLUSTERS="xmotifs_number_of_bicluster_to_be_found";
	
	
	/** The Constant NAME. */
	public static final String NAME="BCXmotifs";

	
	/** The ncolumns. */
	private int ncolumns=10;
	
	/** The nrepetitions. */
	private int nrepetitions=10;
	
	/** The sdrepetitions. */
	private int sdrepetitions=5;
	
	/** The scalefactor. */
	private double scalefactor=0.05;
	
	/** The disclevels. */
	private int disclevels=10;
	
	/** The discquantile. */
	private boolean discquantile=false;
	
	/** The nbics. */
	private int nbics=100;
	
	/** The externalbinmethod. */
	private IDiscretizationMethod externalbinmethod=null;
	
	
	
	/**
	 * Instantiates a new RXMOTIFS method.
	 */
	public RXMOTIFSMethod() {
		super();
	}
	
	/**
	 * Instantiates a new RXMOTIFS method.
	 *
	 * @param exprs the exprs
	 */
	public RXMOTIFSMethod(ExpressionData exprs) {
		super(exprs);
	}
	

	/**
	 * Instantiates a new RXMOTIFS method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RXMOTIFSMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new RXMOTIFS method.
	 *
	 * @param props the props
	 */
	public RXMOTIFSMethod(Properties props){
		super(props);
	}

	/**
	 * Instantiates a new RXMOTIFS method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RXMOTIFSMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	
	
	/**
	 * Instantiates a new RXMOTIFS method.
	 *
	 * @param ncolumns the ncolumns
	 * @param nrepetitions the nrepetitions
	 * @param sdrepetitions the sdrepetitions
	 * @param scalefactor the scalefactor
	 * @param disclevels the disclevels
	 * @param discquantile the discquantile
	 * @param externalbinmethod the externalbinmethod
	 */
	private RXMOTIFSMethod(int ncolumns, int nrepetitions, int sdrepetitions, double scalefactor, int disclevels,
			boolean discquantile, IDiscretizationMethod externalbinmethod) {
		super();
		this.ncolumns = ncolumns;
		this.nrepetitions = nrepetitions;
		this.sdrepetitions = sdrepetitions;
		this.scalefactor = scalefactor;
		this.disclevels = disclevels;
		this.discquantile = discquantile;
		this.externalbinmethod = externalbinmethod;
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
	 * @param number the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int number){
		nbics=number;
	}
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param nclust the nclust
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addNumberBiclustersToFind(int nclust){
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
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addNumberRepetitions(int nrepetitions) {
		this.nrepetitions = nrepetitions;
		return this;
	}


	/**
	 * Sets the number columns.
	 *
	 * @param ncolumns the new number columns
	 */
	public void setNumberColumns(int ncolumns) {
		this.ncolumns = ncolumns;
	}

	/**
	 * Adds the number columns.
	 *
	 * @param ncolumns the ncolumns
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addNumberColumns(int ncolumns) {
		this.ncolumns = ncolumns;
		return this;
	}
	
	
	/**
	 * Sets the samplesize repetitions.
	 *
	 * @param sdrepetitions the new samplesize repetitions
	 */
	public void setSamplesizeRepetitions(int sdrepetitions) {
		this.sdrepetitions = sdrepetitions;
	}

	/**
	 * Adds the samplesize repetitions.
	 *
	 * @param sdrepetitions the sdrepetitions
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addSamplesizeRepetitions(int sdrepetitions) {
		this.sdrepetitions = sdrepetitions;
		return this;
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
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addScalingfactorColumns(double scalefactor) {
		this.scalefactor = scalefactor;
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
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addDiscretizeLevels(int levels){
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
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod useQuantilesInDataDiscretization(boolean bol){
		this.discquantile=bol;
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
	 * @return the RXMOTIFS method
	 */
	public RXMOTIFSMethod addExternalBinarizationMethod(IDiscretizationMethod method){
		this.externalbinmethod=method;
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
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		this.disclevels=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, XMOTIFS_DISCRETIZE_LEVEL, 10, 1, true, getClass());
		this.discquantile=PropertiesUtilities.getBooleanPropertyValue(props, XMOTIFS_DISCRETIZE_WITH_QUANTILES, false, getClass());
		this.ncolumns=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, XMOTIFS_NS, 10, 2,true, this.getClass());
		this.nrepetitions=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, XMOTIFS_ND, 10, 1,true, this.getClass());
		this.sdrepetitions=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, XMOTIFS_SD, 5, 2,true, this.getClass());
		this.scalefactor=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, XMOTIFS_ALPHA, 0.05, 0, false, this.getClass());
		this.nbics=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, XMOTIFS_NCLUSTERS, 100, 2,true, this.getClass());
		boolean usebimaxmethod=PropertiesUtilities.getBooleanPropertyValue(props, XMOTIFS_DISCRETIZE_BIMAX_METHOD, false,getClass());
		if(usebimaxmethod)
			this.externalbinmethod=new BiMaxBinarizationMethod();
		
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {

		String[] propkeys=new String[]{
				XMOTIFS_DISCRETIZE_LEVEL,
				XMOTIFS_DISCRETIZE_WITH_QUANTILES,
				XMOTIFS_DISCRETIZE_BIMAX_METHOD,
				XMOTIFS_NS,
				XMOTIFS_ND,
				XMOTIFS_SD,
				XMOTIFS_ALPHA,
				XMOTIFS_NCLUSTERS
		};
		String[] defaultvalues=new String[]{"10","false","false","10","10","5","0.05","100"};
		String[] comments=new String[] {
				"Discretize levels (default=10)",
				"Use quantiles in discretization process, otherwise uses equally spaced levels",
				"Use bimax discretization method, instead of discretize levels",
				"Number of columns to be chosen (ns)",
				"Number of repetitions (nd)",
				"Sample size in repetitions (sd)",
				"Scaling factor for column result (alpha)",
				"Number of biclusters to be found"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		try {
			

			if(externalbinmethod!=null){
				
				double[][] binmatrix=MTUMatrixUtils.convertIntToDoubleMatrix(expressionset.getBinarizedMatrix(externalbinmethod));
				rsession.set(inputmatrixname, binmatrix);
				rsession.silentlyVoidEval("disc_"+inputmatrixname+" <- "+inputmatrixname+"");
			}
			else
				loadExpressionMatrixInREnvironment();
			
			if(externalbinmethod==null){
				if(disclevels!=10 || discquantile)
					rsession.silentlyVoidEval("disc_"+inputmatrixname+"<-discretize("+inputmatrixname+", nof="+String.valueOf(disclevels)+", quant="+JavaToRUtils.convertBooleanToR(discquantile)+")");
				else{
					rsession.silentlyVoidEval("disc_"+inputmatrixname+"<-discretize("+inputmatrixname+")");
				}
			}
			
			Date starttime =Calendar.getInstance().getTime();
			
			rsession.silentlyEval(getResultOutputName()+" <- biclust("+"disc_"+inputmatrixname+", method=BCXmotifs(), number="+String.valueOf(nbics)+", alpha="+String.valueOf(scalefactor)+", nd="+String.valueOf(nrepetitions)+", ns="+String.valueOf(ncolumns)+", sd="+String.valueOf(sdrepetitions)+")",true);
			
			Date endtime=Calendar.getInstance().getTime();
			long runtime=endtime.getTime()-starttime.getTime();	
			runningtime=MTUTimeUtils.getTimeElapsed(runtime);
			
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
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		
		String[] ccRobjects=new String[]{("disc_"+inputmatrixname)};
		
		return ArrayUtils.addAll(getBaseROjects(), ccRobjects);
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the RXMOTIFS method
	 */
	public static RXMOTIFSMethod newInstance(ExpressionData data){
		return new RXMOTIFSMethod(data);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(XMOTIFS_NCLUSTERS, nbics);
		parameters.setKeyintValue(XMOTIFS_DISCRETIZE_LEVEL, disclevels);
		parameters.setKeybooleanValue(XMOTIFS_DISCRETIZE_WITH_QUANTILES, discquantile);
		parameters.setKeyintValue(XMOTIFS_NS, ncolumns);
		parameters.setKeyintValue(XMOTIFS_ND, nrepetitions);
		parameters.setKeyintValue(XMOTIFS_SD, sdrepetitions);
		parameters.setKeydoubleValue(XMOTIFS_ALPHA, scalefactor);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RXMOTIFSMethod(ncolumns, nrepetitions, sdrepetitions, scalefactor, disclevels, discquantile, externalbinmethod);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		return null;
	}

	

	
}