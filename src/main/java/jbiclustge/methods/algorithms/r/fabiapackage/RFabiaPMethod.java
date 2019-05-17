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
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.components.FabiaCenteringMethod;
import jbiclustge.methods.algorithms.r.components.FabiaNormalizationMethod;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class RFabiaPMethod.
 */
public class RFabiaPMethod extends RFabiaMethod{
	
	/** The Constant NAME. */
	public static final String NAME="FABIAP";
	
	/** The fabia sl. */
	public static String FABIA_SL="fabia_final_sparseness_loadings";
	
	/** The fabia sz. */
	public static String FABIA_SZ="fabia_final_sparseness_factors ";
	
	/** The finalspartnessloadings. */
	protected double finalspartnessloadings=0.6; 
	
	/** The finalspartnessfactors. */
	protected double finalspartnessfactors=0.6;

	
	
	/**
	 * Instantiates a new r fabia P method.
	 */
	public RFabiaPMethod() {
		super();
	}

	
	/**
	 * Instantiates a new r fabia P method.
	 *
	 * @param exprs the exprs
	 */
	public RFabiaPMethod(ExpressionData exprs) {
		super(exprs);
	}

	/**
	 * Instantiates a new r fabia P method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RFabiaPMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new r fabia P method.
	 *
	 * @param props the props
	 */
	public RFabiaPMethod(Properties props){
		super(props);
	}
	
	/**
	 * Instantiates a new r fabia P method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RFabiaPMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	
	
	
	/**
	 * Instantiates a new r fabia P method.
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
	 * @param finalspartnessloadings the finalspartnessloadings
	 * @param finalspartnessfactors the finalspartnessfactors
	 */
	private RFabiaPMethod(int nbics, double alpha, int numberiterations, double spartnessprior, double spartnessfactors,
			FabiaCenteringMethod centermethod, FabiaNormalizationMethod normalization, double variationalparameter,
			boolean nonnegativefactors, double random, double scale, int maxbictorow, int maxrowsinbic, int cyclestarts,
			double thresz, Double thresl,double finalspartnessloadings, double finalspartnessfactors) {
		super(nbics, alpha, numberiterations, spartnessprior, spartnessfactors, centermethod, normalization,
				variationalparameter, nonnegativefactors, random, scale, maxbictorow, maxrowsinbic, cyclestarts, thresz,
				thresl);
		this.finalspartnessloadings = finalspartnessloadings;
		this.finalspartnessfactors = finalspartnessfactors;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNumberBiclustersToFind(int)
	 */
	public RFabiaPMethod addNumberBiclustersToFind(int nclust){
		setNumberBiclustersToFind(nclust);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addAlpha(double)
	 */
	public RFabiaPMethod addAlpha(double alpha) {
		this.alpha = alpha;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNumberIterations(int)
	 */
	public RFabiaPMethod addNumberIterations(int numberiterations) {
		this.numberiterations = numberiterations;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addSpartnessPrior(double)
	 */
	public RFabiaPMethod addSpartnessPrior(double spartnessprior) throws IOException {
		setSpartnessPrior(spartnessprior);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addSpartnessFactors(double)
	 */
	public RFabiaPMethod addSpartnessFactors(double spartnessfactors) throws IOException {
		setSpartnessFactors(spartnessfactors);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addCenterMethod(methods.algorithms.r.components.FabiaCenteringMethod)
	 */
	public RFabiaPMethod addCenterMethod(FabiaCenteringMethod centermethod) {
		this.centermethod = centermethod;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNormalizationMethod(methods.algorithms.r.components.FabiaNormalizationMethod)
	 */
	public RFabiaPMethod addNormalizationMethod(FabiaNormalizationMethod normalization) {
		this.normalization = normalization;
		return this;
	}


	/**
	 * Adds the final spartness loadings.
	 *
	 * @param finalspartnessloadings the finalspartnessloadings
	 * @return the r fabia P method
	 */
	public RFabiaPMethod addFinalSpartnessLoadings(double finalspartnessloadings) {
		this.finalspartnessloadings = finalspartnessloadings;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addVariationalParameter(double)
	 */
	public RFabiaPMethod addVariationalParameter(double variationalparameter) {
		this.variationalparameter = variationalparameter;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addRandom(double)
	 */
	public RFabiaPMethod addRandom(double random) {
		this.random = random;
		return this;
	}

	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNonNegativeFactors(boolean)
	 */
	public RFabiaPMethod addNonNegativeFactors(boolean nonnegativefactors) {
		this.nonnegativefactors = nonnegativefactors;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addScale(double)
	 */
	public RFabiaPMethod addScale(double scale) {
		this.scale = scale;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addMaxbiclustersRowCanBeAccepted(int)
	 */
	public RFabiaPMethod addMaxbiclustersRowCanBeAccepted(int maxbictorow) {
		this.maxbictorow = maxbictorow;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addMaxRowsOfBicluster(int)
	 */
	public RFabiaPMethod addMaxRowsOfBicluster(int maxrowsinbic) {
		this.maxrowsinbic = maxrowsinbic;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addCyclenLorlLMaxStarts(int)
	 */
	public RFabiaPMethod addCyclenLorlLMaxStarts(int cyclestarts) {
		this.cyclestarts = cyclestarts;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addThresholdSampleInBiclusterResults(double)
	 */
	public RFabiaPMethod addThresholdSampleInBiclusterResults(double thresz) {
		this.thresz = thresz;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addThresholdLoadingInBiclusterResults(java.lang.Double)
	 */
	public RFabiaPMethod addThresholdLoadingInBiclusterResults(Double thresl) {
		this.thresl = thresl;
		return this;
	}
	
	/**
	 * Sets the final spartness loadings.
	 *
	 * @param finalspartnessloadings the new final spartness loadings
	 */
	public void setFinalSpartnessLoadings(double finalspartnessloadings) {
		this.finalspartnessloadings = finalspartnessloadings;
	}


	/**
	 * Adds the final spartness factors.
	 *
	 * @param finalspartnessfactors the finalspartnessfactors
	 * @return the r fabia P method
	 */
	public RFabiaPMethod addFinalSpartnessFactors(double finalspartnessfactors) {
		this.finalspartnessfactors = finalspartnessfactors;
		return this;
	}
	
	/**
	 * Sets the final spartness factors.
	 *
	 * @param finalspartnessfactors the new final spartness factors
	 */
	public void setFinalSpartnessFactors(double finalspartnessfactors) {
		this.finalspartnessfactors = finalspartnessfactors;
	}

	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		super.setAlgorithmProperties(props);
		
		this.finalspartnessloadings=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, FABIA_SL, 0.6, 0, true, this.getClass());
		this.finalspartnessfactors=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, FABIA_SZ, 0.6, 0, true, this.getClass());
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{
				FABIA_NCLUSTERS,
				FABIA_ALPHA,
				FABIA_CYC,
				FABIA_SPL,
				FABIA_SPZ,
				FABIA_SL,
				FABIA_SZ,
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
		String[] defaultvalues=new String[]{"13","0.01","500","0","0.5","0.6","0.6","false","1.0","2","1","0","1.0","0","0","0","0.5",""};
		String[] comments=new String[] {
				"Number of bicluster to be found",
				"Sparseness loadings",
				"Number of iterations",
				"Sparseness prior loadings (0 - 2.0)",
				"Sparseness factors (0.5 - 2.0)",
				"final sparseness loadings",
				"final sparseness factors",
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
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		
		try {
			//loadExpressionMatrixInREnvironment();
			loadLabeledExpressionMatrixInREnvironment();
			
			if(nbics>expressionset.numberConditions()) // because if the number of biclusters is higher than the number of the conditions in dataset, fabia will not return any results 
				nbics=expressionset.numberConditions();
         
			Instant start = Instant.now();
			rsession.silentlyEval(getResultOutputName()+" <- fabiap("+inputmatrixname+", p="+String.valueOf(nbics)+","
					+ " alpha="+String.valueOf(alpha)+", cyc="+String.valueOf(numberiterations)+", spl="+String.valueOf(spartnessprior)+","
							+ " spz="+String.valueOf(spartnessfactors)+", sL="+String.valueOf(finalspartnessloadings)+", sZ="+String.valueOf(finalspartnessfactors)+","
									+ " non_negative="+getNonNegativeFactorsState()+", random="+String.valueOf(random)+", center="+centermethod.toString()+", "
											+ "norm="+normalization.toString()+", scale="+String.valueOf(scale)+", lap="+String.valueOf(variationalparameter)+","
													+ " nL="+String.valueOf(maxbictorow)+", lL="+String.valueOf(maxrowsinbic)+", bL="+String.valueOf(cyclestarts)+")");
           
			//System.out.println(s.asString("summary("+getResultOutputName()+")"));

			saveElapsedTime(start);
			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": ", e);
			return false;
		}
		
		
		return true;
	}
	
	 /**
 	 * New instance.
 	 *
 	 * @param data the data
 	 * @return the r fabia P method
 	 */
 	public static RFabiaPMethod newInstance(ExpressionData data){
			return new RFabiaPMethod(data);
		}
	 
	 /* (non-Javadoc)
 	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#copyIntance()
 	 */
 	@Override
	   public AbstractBiclusteringAlgorithmCaller copyIntance() {
		 return new RFabiaPMethod(nbics, finalspartnessfactors, numberiterations, finalspartnessfactors, finalspartnessfactors, centermethod, normalization, finalspartnessfactors, nonnegativefactors, finalspartnessfactors, finalspartnessfactors, maxbictorow, maxrowsinbic, cyclestarts, finalspartnessfactors, thresl, finalspartnessloadings, finalspartnessfactors);
	 }
	 
	  /* (non-Javadoc)
  	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#getReportRunningParameters()
  	 */
  	@Override
	   protected LinkedHashMap<String, String> getReportRunningParameters() {
		   RunningParametersReporter parameters=(RunningParametersReporter) super.getReportRunningParameters();
		   parameters.setKeydoubleValue(FABIA_SL, finalspartnessloadings);
		   parameters.setKeydoubleValue(FABIA_SZ, finalspartnessfactors);
		   return parameters;
	  }

}
