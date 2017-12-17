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
package jbiclustge.methods.algorithms.r.fabiapackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.methods.algorithms.r.components.FabiaCenteringMethod;
import jbiclustge.methods.algorithms.r.components.FabiaNormalizationMethod;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RFabiaMethod.
 */
public class RFabiaMethod extends RBiclustAlgorithmCaller{

	/** The fabia nclusters. */
	public static String FABIA_NCLUSTERS="fabia_number_of_biclusters";
	
	/** The fabia alpha. */
	public static String FABIA_ALPHA="fabia_sparseness_loadings";
	
	/** The fabia cyc. */
	public static String FABIA_CYC="fabia_number_of_iterations";
	
	/** The fabia spl. */
	public static String FABIA_SPL="fabia_sparseness_prior_loadings";
	
	/** The fabia spz. */
	public static String FABIA_SPZ="fabia_sparseness_factors";
	
	/** The fabia nneg. */
	public static String FABIA_NNEG="fabia_non-negative_factors_and_loadings";
	
	/** The fabia random. */
	public static String FABIA_RANDOM="fabia_random_initialization_of_loadings";
	
	/** The fabia center. */
	public static String FABIA_CENTER="fabia_data_centering";
	
	/** The fabia norm. */
	public static String FABIA_NORM="fabia_data_normalization";
	
	/** The fabia scale. */
	public static String FABIA_SCALE="fabia_vectors_are_scaled_in_each_iteration_with_variance";
	
	/** The fabia lap. */
	public static String FABIA_LAP="fabia_minimal_value_variational_parameter";
	
	/** The fabia nl. */
	public static String FABIA_NL="fabia_maximal_number_of_biclusters_which_row_can_be_selected";
	
	/** The fabia ll. */
	public static String FABIA_LL="fabia_maximal_number_of_rows_of_a_bicluster";
	
	/** The fabia bl. */
	public static String FABIA_BL="fabia_cycle_at_which_the_nL_or_lL_maximum_starts";
	
	/** The fabia thrz. */
	public static String FABIA_THRZ="fabia_threshold_for_sample_belonging_to_bicluster";
	
	/** The fabia thrl. */
	public static String FABIA_THRL="fabia_threshold_for_loading_belonging_to_bicluster";
	
	
	
	/** The Constant NAME. */
	public static final String NAME="FABIA";
	
	/** The alpha. */
	protected double alpha=0.01;
	
	/** The numberiterations. */
	protected int numberiterations=500;
	
	/** The spartnessprior. */
	protected double spartnessprior=0; // interval [0 - 2.0]
	
	/** The spartnessfactors. */
	protected double spartnessfactors=0.5; // interval [0.5 - 2.0]
	
	/** The centermethod. */
	protected FabiaCenteringMethod centermethod=FabiaCenteringMethod.MEDIAN;
	
	/** The normalization. */
	protected FabiaNormalizationMethod normalization=FabiaNormalizationMethod.QUANTILE;
	
	/** The variationalparameter. */
	protected double variationalparameter=1.0;
	
	/** The nonnegativefactors. */
	protected boolean nonnegativefactors=false;
	
	/** The random. */
	protected double random=1.0;
	
	/** The scale. */
	protected double scale=0.0;
	
	/** The maxbictorow. */
	protected int maxbictorow=0;
	
	/** The maxrowsinbic. */
	protected int maxrowsinbic=0;
	
	/** The cyclestarts. */
	protected int cyclestarts=0;
	
	/** The thresz. */
	protected double thresz=0.5;
	
	/** The thresl. */
	protected Double thresl=null;
    
    /** The nbics. */
    protected int nbics=13;
	
	
	
	
	/**
	 * Instantiates a new r fabia method.
	 */
	public RFabiaMethod() {
		super();
	}


	/**
	 * Instantiates a new r fabia method.
	 *
	 * @param exprs the exprs
	 */
	public RFabiaMethod(ExpressionData exprs) {
		super(exprs);
	}


	/**
	 * Instantiates a new r fabia method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RFabiaMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}

	/**
	 * Instantiates a new r fabia method.
	 *
	 * @param props the props
	 */
	public RFabiaMethod(Properties props){
		super(props);
	}

	/**
	 * Instantiates a new r fabia method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RFabiaMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	
	
	/**
	 * Instantiates a new r fabia method.
	 *
	 * @param nbics the nbics
	 * @param alpha the alpha
	 * @param numberiterations the numberiterations
	 * @param spartnessprior the spartnessprior
	 * @param spartnessfactors the spartnessfactors
	 * @param centermethod the centermethod
	 * @param normalization the normalization
	 * @param variationalparameter the variationalparameter
	 * @param nonnegativefactors the nonnegativefactors
	 * @param random the random
	 * @param scale the scale
	 * @param maxbictorow the maxbictorow
	 * @param maxrowsinbic the maxrowsinbic
	 * @param cyclestarts the cyclestarts
	 * @param thresz the thresz
	 * @param thresl the thresl
	 */
	protected RFabiaMethod(int nbics, double alpha, int numberiterations, double spartnessprior, double spartnessfactors,
			FabiaCenteringMethod centermethod, FabiaNormalizationMethod normalization, double variationalparameter,
			boolean nonnegativefactors, double random, double scale, int maxbictorow, int maxrowsinbic, int cyclestarts,
			double thresz, Double thresl) {
		super();
		this.nbics=nbics;
		this.alpha = alpha;
		this.numberiterations = numberiterations;
		this.spartnessprior = spartnessprior;
		this.spartnessfactors = spartnessfactors;
		this.centermethod = centermethod;
		this.normalization = normalization;
		this.variationalparameter = variationalparameter;
		this.nonnegativefactors = nonnegativefactors;
		this.random = random;
		this.scale = scale;
		this.maxbictorow = maxbictorow;
		this.maxrowsinbic = maxrowsinbic;
		this.cyclestarts = cyclestarts;
		this.thresz = thresz;
		this.thresl = thresl;
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
	 * @return the r fabia method
	 */
	public RFabiaMethod addAlpha(double alpha) {
		this.alpha = alpha;
		return this;
	}



	/**
	 * Sets the number iterations.
	 *
	 * @param numberiterations the new number iterations
	 */
	public void setNumberIterations(int numberiterations) {
		this.numberiterations = numberiterations;
	}

	/**
	 * Adds the number iterations.
	 *
	 * @param numberiterations the numberiterations
	 * @return the r fabia method
	 */
	public RFabiaMethod addNumberIterations(int numberiterations) {
		this.numberiterations = numberiterations;
		return this;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param number the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int number){
		this.nbics=number;
	}

	/**
	 * Adds the number biclusters to find.
	 *
	 * @param nclust the nclust
	 * @return the r fabia method
	 */
	public RFabiaMethod addNumberBiclustersToFind(int nclust){
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
	 * Sets the spartness prior.
	 *
	 * @param spartnessprior the new spartness prior
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void setSpartnessPrior(double spartnessprior) throws IOException {
		if(spartnessprior>=0 && spartnessprior<=2.0)
		  this.spartnessprior = spartnessprior;
		else
			throw new IOException("The input sparseness prior value["+spartnessprior+"] is out of the allowed range [0 - 2.0]");
			
	}

	/**
	 * Adds the spartness prior.
	 *
	 * @param spartnessprior the spartnessprior
	 * @return the r fabia method
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public RFabiaMethod addSpartnessPrior(double spartnessprior) throws IOException {
		setSpartnessPrior(spartnessprior);
		return this;
	}



	/**
	 * Sets the spartness factors.
	 *
	 * @param spartnessfactors the new spartness factors
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void setSpartnessFactors(double spartnessfactors) throws IOException {
		if(spartnessfactors>=0.5 && spartnessfactors<=2.0)
		  this.spartnessfactors = spartnessfactors;
		else
			throw new IOException("The input sparseness factors value["+spartnessfactors+"] is out of the allowed range [0.5 - 2.0]");
	}

	/**
	 * Adds the spartness factors.
	 *
	 * @param spartnessfactors the spartnessfactors
	 * @return the r fabia method
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public RFabiaMethod addSpartnessFactors(double spartnessfactors) throws IOException {
		setSpartnessFactors(spartnessfactors);
		return this;
	}



	/**
	 * Sets the center method.
	 *
	 * @param centermethod the new center method
	 */
	public void setCenterMethod(FabiaCenteringMethod centermethod) {
		this.centermethod = centermethod;
	}

	/**
	 * Adds the center method.
	 *
	 * @param centermethod the centermethod
	 * @return the r fabia method
	 */
	public RFabiaMethod addCenterMethod(FabiaCenteringMethod centermethod) {
		this.centermethod = centermethod;
		return this;
	}



	/**
	 * Sets the normalization method.
	 *
	 * @param normalization the new normalization method
	 */
	public void setNormalizationMethod(FabiaNormalizationMethod normalization) {
		this.normalization = normalization;
	}

	/**
	 * Adds the normalization method.
	 *
	 * @param normalization the normalization
	 * @return the r fabia method
	 */
	public RFabiaMethod addNormalizationMethod(FabiaNormalizationMethod normalization) {
		this.normalization = normalization;
		return this;
	}



	/**
	 * Sets the variational parameter.
	 *
	 * @param variationalparameter the new variational parameter
	 */
	public void setVariationalParameter(double variationalparameter) {
		this.variationalparameter = variationalparameter;
	}

	/**
	 * Adds the variational parameter.
	 *
	 * @param variationalparameter the variationalparameter
	 * @return the r fabia method
	 */
	public RFabiaMethod addVariationalParameter(double variationalparameter) {
		this.variationalparameter = variationalparameter;
		return this;
	}




	/**
	 * Sets the non negative factors.
	 *
	 * @param nonnegativefactors the new non negative factors
	 */
	public void setNonNegativeFactors(boolean nonnegativefactors) {
		this.nonnegativefactors = nonnegativefactors;
	}

	/**
	 * Adds the non negative factors.
	 *
	 * @param nonnegativefactors the nonnegativefactors
	 * @return the r fabia method
	 */
	public RFabiaMethod addNonNegativeFactors(boolean nonnegativefactors) {
		this.nonnegativefactors = nonnegativefactors;
		return this;
	}



	/**
	 * Sets the random.
	 *
	 * @param random the new random
	 */
	public void setRandom(double random) {
		this.random = random;
	}

	/**
	 * Adds the random.
	 *
	 * @param random the random
	 * @return the r fabia method
	 */
	public RFabiaMethod addRandom(double random) {
		this.random = random;
		return this;
	}



	/**
	 * Sets the scale.
	 *
	 * @param scale the new scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Adds the scale.
	 *
	 * @param scale the scale
	 * @return the r fabia method
	 */
	public RFabiaMethod addScale(double scale) {
		this.scale = scale;
		return this;
	}



	/**
	 * Sets the maxbiclusters row can be accepted.
	 *
	 * @param maxbictorow the new maxbiclusters row can be accepted
	 */
	public void setMaxbiclustersRowCanBeAccepted(int maxbictorow) {
		this.maxbictorow = maxbictorow;
	}

	/**
	 * Adds the maxbiclusters row can be accepted.
	 *
	 * @param maxbictorow the maxbictorow
	 * @return the r fabia method
	 */
	public RFabiaMethod addMaxbiclustersRowCanBeAccepted(int maxbictorow) {
		this.maxbictorow = maxbictorow;
		return this;
	}



	/**
	 * Sets the max rows of bicluster.
	 *
	 * @param maxrowsinbic the new max rows of bicluster
	 */
	public void setMaxRowsOfBicluster(int maxrowsinbic) {
		this.maxrowsinbic = maxrowsinbic;
	}

	/**
	 * Adds the max rows of bicluster.
	 *
	 * @param maxrowsinbic the maxrowsinbic
	 * @return the r fabia method
	 */
	public RFabiaMethod addMaxRowsOfBicluster(int maxrowsinbic) {
		this.maxrowsinbic = maxrowsinbic;
		return this;
	}



	/**
	 * Sets the cyclen lorl L max starts.
	 *
	 * @param cyclestarts the new cyclen lorl L max starts
	 */
	public void setCyclenLorlLMaxStarts(int cyclestarts) {
		this.cyclestarts = cyclestarts;
	}

	/**
	 * Adds the cyclen lorl L max starts.
	 *
	 * @param cyclestarts the cyclestarts
	 * @return the r fabia method
	 */
	public RFabiaMethod addCyclenLorlLMaxStarts(int cyclestarts) {
		this.cyclestarts = cyclestarts;
		return this;
	}



	/**
	 * Sets the threshold sample in bicluster results.
	 *
	 * @param thresz the new threshold sample in bicluster results
	 */
	public void setThresholdSampleInBiclusterResults(double thresz) {
		this.thresz = thresz;
	}
	
	/**
	 * Adds the threshold sample in bicluster results.
	 *
	 * @param thresz the thresz
	 * @return the r fabia method
	 */
	public RFabiaMethod addThresholdSampleInBiclusterResults(double thresz) {
		this.thresz = thresz;
		return this;
	}

	/**
	 * Sets the threshold loading in bicluster results.
	 *
	 * @param thresl the new threshold loading in bicluster results
	 */
	public void setThresholdLoadingInBiclusterResults(Double thresl) {
		this.thresl = thresl;
	}

	/**
	 * Adds the threshold loading in bicluster results.
	 *
	 * @param thresl the thresl
	 * @return the r fabia method
	 */
	public RFabiaMethod addThresholdLoadingInBiclusterResults(Double thresl) {
		this.thresl = thresl;
		return this;
	}




	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#requiredLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> requiredLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("fabia", true));
		return list;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		this.nbics=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, FABIA_NCLUSTERS, 13, 2,true, this.getClass());
		this.alpha=PropertiesUtilities.getDoublePropertyValueValidLimits(props, FABIA_ALPHA, 0.01, 0,1.0, true, this.getClass());
		this.numberiterations=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, FABIA_CYC, 500, 10,true, this.getClass());
		this.spartnessprior=PropertiesUtilities.getDoublePropertyValueValidLimits(props, FABIA_SPL, 0, 0, 2.0, true, this.getClass());
		this.spartnessfactors=PropertiesUtilities.getDoublePropertyValueValidLimits(props, FABIA_SPZ, 0.5, 0.5, 2.0, true, this.getClass());
		this.nonnegativefactors=PropertiesUtilities.getBooleanPropertyValue(props, FABIA_NNEG, false, this.getClass());
		this.variationalparameter=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, FABIA_LAP, 1.0, 0, false, this.getClass());
		this.random=PropertiesUtilities.getDoublePropertyValue(props, FABIA_RANDOM, 1.0, this.getClass());
		this.scale=PropertiesUtilities.getDoublePropertyValue(props, FABIA_SCALE, 0, this.getClass());
		this.maxbictorow=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, FABIA_NL, 0, 0,true, this.getClass());
		this.maxrowsinbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, FABIA_LL, 0, 0,true, this.getClass());
		this.cyclestarts=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, FABIA_BL, 0, 0,true, this.getClass());
		this.thresz=PropertiesUtilities.getDoublePropertyValueValidLimits(props, FABIA_THRZ, 0.5, 0, 1, true, this.getClass());
		
		
		String[] allowedcenterinput=new String[]{"none","0","mean","1","median","2","mode","3"};
		String centermethod=PropertiesUtilities.getStringPropertyValue(props, FABIA_CENTER, "median", allowedcenterinput, this.getClass());
		this.centermethod=getCenterMethodFromString(centermethod);
		
		
		String[] allowednormalization=new String[]{"none", "0", "quantile", "1", "variance", "2"};
		String normmethod=PropertiesUtilities.getStringPropertyValue(props, FABIA_NORM, "quantile", allowednormalization, this.getClass());
		this.normalization=getNormalizationMethodFromString(normmethod);
		
		String threslinput=PropertiesUtilities.getStringPropertyValue(props, FABIA_THRL, null, this.getClass());
		
		if(threslinput==null || threslinput.isEmpty())
			   this.thresl=null;
		else{
			Double tmpn=null;
		   try {
		       tmpn=Double.parseDouble(threslinput);
		    } catch (Exception e) {
			  this.thresl=null;
		   }
		   try {
		      if(tmpn!=null && tmpn<0 && tmpn>1)
				throw new IOException("The input thresl value["+tmpn+"] is out of the allowed range [0 - 1] or null");
		      else
			     this.thresl=tmpn;
		   } catch (IOException e) {
			   LogMessageCenter.getLogger().addCriticalErrorMessage("Error in loading properties of "+getAlgorithmName()+": ", e);
				this.thresl=null;
			}
		
		}
		
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{
				FABIA_NCLUSTERS,
				FABIA_ALPHA,
				FABIA_CYC,
				FABIA_SPL,
				FABIA_SPZ,
				FABIA_NNEG,
				FABIA_RANDOM,
				FABIA_CENTER,
				FABIA_NORM,
				FABIA_SCALE,
				FABIA_LAP,
				FABIA_NL,
				FABIA_LL,
				FABIA_BL,
				FABIA_THRZ,
				FABIA_THRL
		};
		String[] defaultvalues=new String[]{"13","0.01","500","0","0.5","false","1.0","2","1","0","1.0","0","0","0","0.5",""};
		String[] comments=new String[] {
				"Number of bicluster to be found",
				"Sparseness loadings",
				"Number of iterations",
				"Sparseness prior loadings (0 - 2.0)",
				"Sparseness factors (0.5 - 2.0)",
				"Non-negative factors and loadings if non_negative > 0",
				"Random initialization of loadings in [-random,random] if >0 or <=0 SVD",
				"Data centering: 1 (mean), 2 (median), 3 (mode), 0 (none)",
				"Data normalization: 1 (0.75-0.25 quantile), 2 (var=1), 0 (none)",
				"Loading vectors are scaled in each iteration to the given variance 0=non-scaling",
				"Minimal value of the variational parameter",
				"Maximal number of biclusters at which a row element can participate",
				"Maximal number of row elements per bicluster",
				"Cycle at which the nL or lL maximum starts",
				"Threshold for sample belonging to bicluster",
				"Threshold for loading belonging to bicluster (if not given it is estimated)"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: fabia manual, url: http://www.bioconductor.org/packages/devel/bioc/vignettes/fabia/inst/doc/fabia.pdf");
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		
		try {
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Starting Fabia clustering method...please wait");
			
			loadExpressionMatrixInREnvironment();
         
			Date starttime =Calendar.getInstance().getTime();
			rsession.silentlyEval(getResultOutputName()+" <- fabia("+inputmatrixname+", p="+String.valueOf(nbics)+","
					+ " alpha="+String.valueOf(alpha)+", cyc="+String.valueOf(numberiterations)+", spl="+String.valueOf(spartnessprior)+","
							+ " spz="+String.valueOf(spartnessfactors)+", non_negative="+getNonNegativeFactorsState()+","
									+ " random="+String.valueOf(random)+", center="+centermethod.toString()+", norm="+normalization.toString()+","
											+ " scale="+String.valueOf(scale)+", lap="+String.valueOf(variationalparameter)+", nL="+String.valueOf(maxbictorow)+","
													+ " lL="+String.valueOf(maxrowsinbic)+", bL="+String.valueOf(cyclestarts)+")");
           

			Date endtime=Calendar.getInstance().getTime();
			long runtime=endtime.getTime()-starttime.getTime();	
			runningtime=MTUTimeUtils.getTimeElapsed(runtime);

			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": ", e);
			return false;
		}
		
		
		return true;
	}
	
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void processResults() throws Exception {
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Saving Fabia results...please wait");

		String extractedname="extracted_"+getResultOutputName();
		if(thresz!=0.5 || thresl!=null)
			rsession.eval(extractedname+" <- extractBic("+getResultOutputName()+", thresZ="+String.valueOf(thresz)+", thresL="+getRThresholdZ()+")");
		else
			rsession.eval(extractedname+" <- extractBic("+getResultOutputName()+")");
		
	
		
		RList genevalueslist =(RList) rsession.cast(rsession.eval(getResultOutputName()+"_genevalues <-"+extractedname+"$bic[,2]"));
		RList genenameslist =(RList) rsession.cast(rsession.eval(getResultOutputName()+"_genenames <-"+extractedname+"$bic[,3]"));
		RList colunmsvalueslist =(RList) rsession.cast(rsession.eval(getResultOutputName()+"_colvalues <-"+extractedname+"$bic[,4]"));
		RList colunmsnameslist =(RList) rsession.cast(rsession.eval(getResultOutputName()+"_colnames <-"+extractedname+"$bic[,5]"));
		
		//System.out.println(genevalueslist);
		
		ArrayList<ArrayList<Double>> genescores =convertRListofDoubles(genevalueslist);
		ArrayList<ArrayList<Double>> conditionscores=convertRListofDoubles(colunmsvalueslist);
		
		ArrayList<ArrayList<String>> genenames =convertRListofStrings(genenameslist);
		ArrayList<ArrayList<String>> conditionnames=convertRListofStrings(colunmsnameslist);
		
		//System.out.println("Genes: "+genenames);
		
		listofbiclusters=new BiclusterList();
		for (int i = 0; i < genenames.size(); i++) {
			ArrayList<String> genesids=genenames.get(i);
			ArrayList<String> condids=conditionnames.get(i);
			if((genesids!=null && genesids.size()>0) && (condids!=null && condids.size()>0)){
				BiclusterResult res =new BiclusterResult(expressionset, genesids, condids);
				res.appendAdditionalInfo("Gene_Scores", genescores.get(i));
				res.appendAdditionalInfo("Condition_Scores", conditionscores.get(i));
				listofbiclusters.add(res);
			}
		}
        if(listofbiclusters.size()>0)
        	LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Fabia results were successfully saved");
        else
        	LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Fabia returned a null result list");
        
        
		
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		
		String[] baseobjects=getBaseROjects();
		
		String[] fabiaobjects=new String[]{("extracted_"+getResultOutputName()),
				(getResultOutputName()+"_genevalues"),
				(getResultOutputName()+"_genenames"),
				(getResultOutputName()+"_colvalues"),
				(getResultOutputName()+"_colnames")};
		return ArrayUtils.addAll(baseobjects, fabiaobjects);
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}
	
	
	
	
	/**
	 * Convert R listof doubles.
	 *
	 * @param lstdoubles the lstdoubles
	 * @return the array list
	 * @throws REXPMismatchException the REXP mismatch exception
	 */
	protected ArrayList<ArrayList<Double>> convertRListofDoubles(RList lstdoubles) throws REXPMismatchException{
		ArrayList<ArrayList<Double>> res=new ArrayList<>();
		for (int i = 0; i < lstdoubles.size(); i++) {
			double[] gv = ((REXP) lstdoubles.get(i)).asDoubles();
			ArrayList<Double> ngv=new ArrayList<>();
			for (int j = 0; j < gv.length; j++) {
				ngv.add(gv[j]);
			}
			res.add(ngv);
		}
		return res;
	}
	
   /**
    * Convert R listof strings.
    *
    * @param lststring the lststring
    * @return the array list
    * @throws REXPMismatchException the REXP mismatch exception
    */
   protected ArrayList<ArrayList<String>> convertRListofStrings(RList lststring) throws REXPMismatchException{
    	ArrayList<ArrayList<String>> res =new ArrayList<>();
    	for (int i = 0; i < lststring.size(); i++) {
			String[] gn = ((REXP) lststring.get(i)).asStrings();
			/*System.out.println("GENES: \n");
			MTUPrintUtils.printArrayofStrings(gn);
			System.out.println("\n");*/
			ArrayList<String> ngn=new ArrayList<>();
			for (int j = 0; j < gn.length; j++) {
				ngn.add(gn[j]);
			}
			res.add(ngn);
		}
    	return res;
	}
    
   /**
    * Gets the r threshold Z.
    *
    * @return the r threshold Z
    */
   protected String getRThresholdZ(){
    	if(thresl==null)
    		return "NULL";
    	else
    		return String.valueOf(thresl);
    }
    
   /**
    * Gets the non negative factors state.
    *
    * @return the non negative factors state
    */
   protected String getNonNegativeFactorsState(){
    	if(nonnegativefactors)
    		return "1";
    	else
    		return "0";
    }
    
   /**
    * Gets the center method from string.
    *
    * @param val the val
    * @return the center method from string
    */
   protected FabiaCenteringMethod getCenterMethodFromString(String val){
    	
    	for (FabiaCenteringMethod cm : FabiaCenteringMethod.values()) {
			if(cm.getName().equals(val) || cm.toString().equals(val))
				return cm;
		}
    	return FabiaCenteringMethod.MEDIAN;
    }
    

   /**
    * Gets the normalization method from string.
    *
    * @param val the val
    * @return the normalization method from string
    */
   protected FabiaNormalizationMethod getNormalizationMethodFromString(String val){
    	
    	for (FabiaNormalizationMethod nm : FabiaNormalizationMethod.values()) {
			if(nm.getName().equals(val) || nm.toString().equals(val))
				return nm;
		}
    	return FabiaNormalizationMethod.QUANTILE;
    }

   /**
    * New instance.
    *
    * @param data the data
    * @return the r fabia method
    */
   public static RFabiaMethod newInstance(ExpressionData data){
		return new RFabiaMethod(data);
	}



   /* (non-Javadoc)
    * @see methods.algorithms.r.RBiclustAlgorithmCaller#getTemporaryWorkingDirectory()
    */
   @Override
   protected String getTemporaryWorkingDirectory() {
	   return null;
   }


   /* (non-Javadoc)
    * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
    */
   @Override
   protected LinkedHashMap<String, String> getReportRunningParameters() {
	   RunningParametersReporter parameters=new RunningParametersReporter();
	   parameters.setKeyintValue(FABIA_NCLUSTERS, nbics);
	   parameters.setKeydoubleValue(FABIA_ALPHA, alpha);
	   parameters.setKeyintValue(FABIA_CYC, numberiterations);
	   parameters.setKeydoubleValue(FABIA_SPL, spartnessprior);
	   parameters.setKeydoubleValue(FABIA_SPZ, spartnessfactors);
	   parameters.setKeybooleanValue(FABIA_NNEG, nonnegativefactors);
	   parameters.setKeydoubleValue(FABIA_LAP, variationalparameter);
	   parameters.setKeydoubleValue(FABIA_RANDOM, random);
	   parameters.setKeydoubleValue(FABIA_SCALE, scale);
	   parameters.setKeyintValue(FABIA_NL, maxbictorow);
	   parameters.setKeyintValue(FABIA_LL, maxrowsinbic);
	   parameters.setKeyintValue(FABIA_BL, cyclestarts);
	   parameters.setKeydoubleValue(FABIA_THRZ, thresz);
	   parameters.setKeyStringValue(FABIA_CENTER, centermethod.getName());
	   parameters.setKeyStringValue(FABIA_NORM, normalization.getName());
	   parameters.setKeyStringValue(FABIA_THRL, String.valueOf(thresl));

	   
	   return parameters;
   }


   /* (non-Javadoc)
    * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
    */
   @Override
   public AbstractBiclusteringAlgorithmCaller copyIntance() {
	   return new RFabiaMethod(nbics, alpha, numberiterations, spartnessprior, spartnessfactors, centermethod, normalization, variationalparameter, nonnegativefactors, random, scale, maxbictorow, maxrowsinbic, cyclestarts, thresz, thresl);
   }


/* (non-Javadoc)
 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
 */
@Override
protected ArrayList<String> loadSources() {
	return null;
}


  





}
