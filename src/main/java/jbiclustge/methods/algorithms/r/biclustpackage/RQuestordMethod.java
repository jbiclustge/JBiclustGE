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
 * The Class RQuestordMethod.
 */
public class RQuestordMethod extends RQuestMethod{
	
	
	/** The bcquestord d. */
	public static String BCQUESTORD_D="bcquest_half_margin_of_intervall_question_values_should_be_in";
	
	/** The Constant NAME. */
	public static final String NAME="BCQuestord";
	
	/** The questionintervalmargin. */
	protected double questionintervalmargin=1;

	
	/**
	 * Instantiates a new r questord method.
	 */
	public RQuestordMethod() {
		super();
	}
	
	/**
	 * Instantiates a new r questord method.
	 *
	 * @param exprs the exprs
	 */
	public RQuestordMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	
	/**
	 * Instantiates a new r questord method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RQuestordMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new r questord method.
	 *
	 * @param props the props
	 */
	public RQuestordMethod(Properties props){
		super(props);
	}

	
	/**
	 * Instantiates a new r questord method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RQuestordMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	

	/**
	 * Instantiates a new r questord method.
	 *
	 * @param nbics the nbics
	 * @param nquestions the nquestions
	 * @param nrepetitions the nrepetitions
	 * @param samplesizerepetitions the samplesizerepetitions
	 * @param alpha the alpha
	 * @param questionintervalmargin the questionintervalmargin
	 */
	private RQuestordMethod(int nbics, int nquestions, int nrepetitions, int samplesizerepetitions, double alpha,double questionintervalmargin) {
		super();
		this.nbics=nbics;
		this.nquestions = nquestions;
		this.nrepetitions = nrepetitions;
		this.samplesizerepetitions = samplesizerepetitions;
		this.alpha = alpha;
		this.questionintervalmargin = questionintervalmargin;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addNumberBiclustersToFind(int)
	 */
	public RQuestordMethod addNumberBiclustersToFind(int nclust){
		setNumberBiclustersToFind(nclust);
		return this;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addNumberQuestions(int)
	 */
	public RQuestordMethod addNumberQuestions(int nquestions) {
		this.nquestions = nquestions;
		return this;
	}

	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addNumberRepetitions(int)
	 */
	public RQuestordMethod addNumberRepetitions(int nrepetitions) {
		this.nrepetitions = nrepetitions;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addSampleSizeRepetitions(int)
	 */
	public RQuestordMethod addSampleSizeRepetitions(int samplesizerepetitions) {
		this.samplesizerepetitions = samplesizerepetitions;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#addAlpha(double)
	 */
	public RQuestordMethod addAlpha(double alpha) {
		this.alpha = alpha;
		return this;
	}
	
	
	/**
	 * Sets the question interval margin.
	 *
	 * @param questionintervalmargin the new question interval margin
	 */
	public void setQuestionIntervalMargin(double questionintervalmargin) {
		this.questionintervalmargin = questionintervalmargin;
	}
	
	/**
	 * Adds the question interval margin.
	 *
	 * @param questionintervalmargin the questionintervalmargin
	 * @return the r questord method
	 */
	public RQuestordMethod addQuestionIntervalMargin(double questionintervalmargin) {
		this.questionintervalmargin = questionintervalmargin;
		return this;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getInputParametersString()
	 */
	@Override
	protected String getInputParametersString(){
		return "method=BCQuestord(), d="+String.valueOf(questionintervalmargin)+", ns="+String.valueOf(nquestions)+", nd="+String.valueOf(nrepetitions)+", sd="+String.valueOf(samplesizerepetitions)+", alpha="+String.valueOf(alpha)+", number="+String.valueOf(nbics)+"";
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		super.setAlgorithmProperties(props);
		
		this.questionintervalmargin=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, BCQUESTORD_D, 1, 0, false, this.getClass());
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
				BCQUESTORD_D
		};
		String[] defaultvalues=new String[]{"10","10","5","0.05","10","1"};
		String[] comments=new String[] {
				"Number of questions choosen",
				"Number of repetitions",
				"Sample size in repetitions",
				"Scaling factor for column result",
				"Number of bicluster to be found",
				"Half margin of intervall question values should be in (Intervall is mean-d,mean+d)"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the r questord method
	 */
	public static RQuestordMethod newInstance(ExpressionData data){
		return new RQuestordMethod(data);
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RQuestordMethod(nbics, nquestions, nrepetitions, samplesizerepetitions, questionintervalmargin, questionintervalmargin);
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.biclustpackage.RQuestMethod#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=(RunningParametersReporter) super.getReportRunningParameters();
		parameters.setKeydoubleValue(BCQUESTORD_D, questionintervalmargin);
		return parameters;
	}
}
