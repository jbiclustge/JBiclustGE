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
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class RQuestmetMethod.
 */
public class RQuestmetMethod extends RQuestMethod{

	
	/** The bcquestmet quant. */
	public static String BCQUESTMET_QUANT="bcquestmet_quantile_to_use_on_metric_data";
	
	/** The bcquestmet vari. */
	public static String BCQUESTMET_VARI="bcquestmet_varianz_to_use_for_metric_data";
	
	/** The Constant NAME. */
	public static final String NAME="BCQuestmet";
	
	
	/** The quantile. */
	private double quantile=0.25;
	
	/** The variance. */
	private double variance=1;
	

	/**
	 * Instantiates a new r questmet method.
	 */
	public RQuestmetMethod() {
		super();
	}

	
	/**
	 * Instantiates a new r questmet method.
	 *
	 * @param exprs the exprs
	 */
	public RQuestmetMethod(ExpressionData exprs) {
		super(exprs);
	}

	/**
	 * Instantiates a new r questmet method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RQuestmetMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	
	

	/**
	 * Instantiates a new r questmet method.
	 *
	 * @param nbics the nbics
	 * @param nquestions the nquestions
	 * @param nrepetitions the nrepetitions
	 * @param samplesizerepetitions the samplesizerepetitions
	 * @param alpha the alpha
	 * @param quantile the quantile
	 * @param variance the variance
	 */
	private RQuestmetMethod(int nbics, int nquestions, int nrepetitions, int samplesizerepetitions, double alpha,double quantile, double variance) {
		super();
		this.nbics=nbics;
		this.nquestions = nquestions;
		this.nrepetitions = nrepetitions;
		this.samplesizerepetitions = samplesizerepetitions;
		this.alpha = alpha;
		this.quantile = quantile;
		this.variance = variance;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addNumberBiclustersToFind(int)
	 */
	public RQuestmetMethod addNumberBiclustersToFind(int nclust){
		setNumberBiclustersToFind(nclust);
		return this;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addNumberQuestions(int)
	 */
	public RQuestmetMethod addNumberQuestions(int nquestions) {
		this.nquestions = nquestions;
		return this;
	}

	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addNumberRepetitions(int)
	 */
	public RQuestmetMethod addNumberRepetitions(int nrepetitions) {
		this.nrepetitions = nrepetitions;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addSampleSizeRepetitions(int)
	 */
	public RQuestmetMethod addSampleSizeRepetitions(int samplesizerepetitions) {
		this.samplesizerepetitions = samplesizerepetitions;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addAlpha(double)
	 */
	public RQuestmetMethod addAlpha(double alpha) {
		this.alpha = alpha;
		return this;
	}
	
	
	
	/**
	 * Sets the quantile.
	 *
	 * @param quantile the new quantile
	 */
	public void setQuantile(double quantile) {
		this.quantile = quantile;
	}
	
	/**
	 * Adds the quantile.
	 *
	 * @param quantile the quantile
	 * @return the r questmet method
	 */
	public RQuestmetMethod addQuantile(double quantile) {
		this.quantile = quantile;
		return this;
	}


	/**
	 * Sets the variance.
	 *
	 * @param variance the new variance
	 */
	public void setVariance(double variance) {
		this.variance = variance;
	}
	
	/**
	 * Adds the variance.
	 *
	 * @param variance the variance
	 * @return the r questmet method
	 */
	public RQuestmetMethod addVariance(double variance) {
		this.variance = variance;
		return this;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getInputParametersString()
	 */
	@Override
	protected String getInputParametersString(){
		return "method=BCQuestmet(), quant="+String.valueOf(quantile)+", vari="+String.valueOf(variance)+", ns="+String.valueOf(nquestions)+", nd="+String.valueOf(nrepetitions)+", sd="+String.valueOf(samplesizerepetitions)+", alpha="+String.valueOf(alpha)+", number="+String.valueOf(nbics)+"";
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		super.setAlgorithmProperties(props);
		
		this.quantile=PropertiesUtilities.getDoublePropertyValueValidLimits(props, BCQUESTMET_QUANT, 0.25, 0, 1, false, this.getClass());
		this.variance=PropertiesUtilities.getDoublePropertyValueValidLimits(props, BCQUESTMET_VARI, 1, 0, 1, true, this.getClass());
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				BCQUEST_NS,
				BCQUEST_ND,
				BCQUEST_SD,
				BCQUEST_ALPHA,
				BCQUEST_NCLUSTERS,
				BCQUESTMET_QUANT,
				BCQUESTMET_VARI
		};
		String[] defaultvalues=new String[]{"10","10","5","0.05","10","0.25","1"};
		String[] comments=new String[] {
				"Number of questions choosen",
				"Number of repetitions",
				"Sample size in repetitions",
				"Scaling factor for column result",
				"Number of bicluster to be found",
				"Which quantile to use on metric data",
				"Which varianz to use for metric data"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the r questmet method
	 */
	public static RQuestmetMethod newInstance(ExpressionData data){
		return new RQuestmetMethod(data);
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RQuestmetMethod(nbics, nquestions, nrepetitions, samplesizerepetitions, quantile, quantile, variance);
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=(RunningParametersReporter) super.getReportRunningParameters();
		parameters.setKeydoubleValue(BCQUESTMET_QUANT, quantile);
		parameters.setKeydoubleValue(BCQUESTMET_VARI, variance);
		return parameters;
	}
}
