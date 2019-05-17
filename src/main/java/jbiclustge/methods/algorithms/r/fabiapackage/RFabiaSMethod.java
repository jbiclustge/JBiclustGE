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
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.r.components.FabiaCenteringMethod;
import jbiclustge.methods.algorithms.r.components.FabiaNormalizationMethod;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class RFabiaSMethod.
 */
public class RFabiaSMethod extends RFabiaMethod{
	
	
	/** The Constant NAME. */
	public static final String NAME="FABIAS";

	
	
	
	/**
	 * Instantiates a new r fabia S method.
	 */
	public RFabiaSMethod() {
		super();
	}

	/**
	 * Instantiates a new r fabia S method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RFabiaSMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new r fabia S method.
	 *
	 * @param props the props
	 */
	public RFabiaSMethod(Properties props) {
		super(props);
	}
	
	/**
	 * Instantiates a new r fabia S method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RFabiaSMethod(String propertiesfile) {
		super(propertiesfile);
	}

	/**
	 * Instantiates a new r fabia S method.
	 *
	 * @param exprs the exprs
	 */
	public RFabiaSMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNumberBiclustersToFind(int)
	 */
	public RFabiaSMethod addNumberBiclustersToFind(int nclust){
		setNumberBiclustersToFind(nclust);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addAlpha(double)
	 */
	public RFabiaSMethod addAlpha(double alpha) {
		this.alpha = alpha;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNumberIterations(int)
	 */
	public RFabiaSMethod addNumberIterations(int numberiterations) {
		this.numberiterations = numberiterations;
		return this;
	}

	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addSpartnessFactors(double)
	 */
	public RFabiaSMethod addSpartnessFactors(double spartnessfactors) throws IOException {
		setSpartnessFactors(spartnessfactors);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addCenterMethod(methods.algorithms.r.components.FabiaCenteringMethod)
	 */
	public RFabiaSMethod addCenterMethod(FabiaCenteringMethod centermethod) {
		this.centermethod = centermethod;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNormalizationMethod(methods.algorithms.r.components.FabiaNormalizationMethod)
	 */
	public RFabiaSMethod addNormalizationMethod(FabiaNormalizationMethod normalization) {
		this.normalization = normalization;
		return this;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addVariationalParameter(double)
	 */
	public RFabiaSMethod addVariationalParameter(double variationalparameter) {
		this.variationalparameter = variationalparameter;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addRandom(double)
	 */
	public RFabiaSMethod addRandom(double random) {
		this.random = random;
		return this;
	}

	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addNonNegativeFactors(boolean)
	 */
	public RFabiaSMethod addNonNegativeFactors(boolean nonnegativefactors) {
		this.nonnegativefactors = nonnegativefactors;
		return this;
	}
	

	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addMaxbiclustersRowCanBeAccepted(int)
	 */
	public RFabiaSMethod addMaxbiclustersRowCanBeAccepted(int maxbictorow) {
		this.maxbictorow = maxbictorow;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addMaxRowsOfBicluster(int)
	 */
	public RFabiaSMethod addMaxRowsOfBicluster(int maxrowsinbic) {
		this.maxrowsinbic = maxrowsinbic;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addCyclenLorlLMaxStarts(int)
	 */
	public RFabiaSMethod addCyclenLorlLMaxStarts(int cyclestarts) {
		this.cyclestarts = cyclestarts;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addThresholdSampleInBiclusterResults(double)
	 */
	public RFabiaSMethod addThresholdSampleInBiclusterResults(double thresz) {
		this.thresz = thresz;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#addThresholdLoadingInBiclusterResults(java.lang.Double)
	 */
	public RFabiaSMethod addThresholdLoadingInBiclusterResults(Double thresl) {
		this.thresl = thresl;
		return this;
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.fabiapackage.RFabiaMethod#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
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
				FABIA_SPZ,
				FABIA_NNEG,
				FABIA_RANDOM,
				FABIA_CENTER,
				FABIA_NORM,
				FABIA_LAP,
				FABIA_NL,
				FABIA_LL,
				FABIA_BL,
				FABIA_THRZ,
				FABIA_THRL
		};
		String[] defaultvalues=new String[]{"13","0.01","500","0.5","false","1.0","2","1","1.0","0","0","0","0.5",""};
		String[] comments=new String[] {
				"Number of bicluster to be found",
				"Sparseness loadings",
				"Number of iterations",
				"Sparseness factors (0.5 - 2.0)",
				"Non-negative factors and loadings if non_negative > 0",
				"Random initialization of loadings in [-random,random] if >0 or <=0 SVD",
				"Data centering: 1 (mean), 2 (median), 3 (mode), 0 (none)",
				"Data normalization: 1 (0.75-0.25 quantile), 2 (var=1), 0 (none)",
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
			
			rsession.silentlyEval(getResultOutputName()+" <- fabias("+inputmatrixname+", p="+String.valueOf(nbics)+","
					+ " alpha="+String.valueOf(alpha)+", cyc="+String.valueOf(numberiterations)+","
							+ " spz="+String.valueOf(spartnessfactors)+", non_negative="+getNonNegativeFactorsState()+","
									+ " random="+String.valueOf(random)+", center="+centermethod.toString()+", norm="+normalization.toString()+","
											+ " lap="+String.valueOf(variationalparameter)+", nL="+String.valueOf(maxbictorow)+","
													+ " lL="+String.valueOf(maxrowsinbic)+", bL="+String.valueOf(cyclestarts)+")");
           
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
 	 * @return the r fabia S method
 	 */
 	public static RFabiaSMethod newInstance(ExpressionData data){
			return new RFabiaSMethod(data);
		}
	 

	 
}
