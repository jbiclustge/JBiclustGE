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
package jbiclustge.methods.algorithms.java.bibit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.javatuples.Pair;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.arrays.MTUMatrixUtils;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BibitMethod.
 */
public class BibitMethod extends AbstractBiclusteringAlgorithmCaller{
	
	
	/** The Constant NAME. */
	public static final String NAME="Bibit";
	
	/** The Constant MINIMUMGENES. */
	public static final String MINIMUMGENES="minimum_number_of_genes_allowed_in_a_valid_bicluster";
	
	/** The Constant MINIMUMCONDITIONS. */
	public static final String MINIMUMCONDITIONS="minimum_number_of_conditions_allowed_in_a_valid_bicluster";
	
	/** The Constant MAXBICLUSTERS. */
	public static final String MAXBICLUSTERS="maximum_number_of_biclusters_to_find";
	
	/** The Constant PATTERNSIZE. */
	public static final String PATTERNSIZE="pattern_size_to_use_in_the_encoding_phase";
	
	/** The Constant MAXDISCRETIZEDVALUE. */
	public static final String MAXDISCRETIZEDVALUE="maximum_value_in_the_discretized_dataset";
	
	/** The Constant MAXVALUEDATASETASREFERENCE. */
	public static final String MAXVALUEDATASETASREFERENCE="use_maximum_value_of_datatset_as_reference_in_discretization_step";
	
	
	/** The patternsize. */
	private int patternsize=16;
	
	/** The maxdiscretizedvalue. */
	private int maxdiscretizedvalue=1;
	
	/** The mingenesbic. */
	private int mingenesbic=2;
	
	/** The minconditionsbic. */
	private int minconditionsbic=2;
	
	/** The maxbiclusters. */
	private int maxbiclusters=0;
	
	/** The usemaxvalueasreference. */
	private boolean usemaxvalueasreference=false;
	
	/** The matrix. */
	private BibitMatrix matrix;
	
	boolean run=true;
	
	
	/**
	 * Instantiates a new bibit method.
	 */
	public BibitMethod(){
		super();
	}
	
	/**
	 * Instantiates a new bibit method.
	 *
	 * @param exprs the exprs
	 */
	public BibitMethod(ExpressionData exprs){
		super(exprs);
	}
	
	/**
	 * Instantiates a new bibit method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public BibitMethod(ExpressionData exprs, Properties props){
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new bibit method.
	 *
	 * @param props the props
	 */
	public BibitMethod(Properties props){
		super(props);
	}
	
	/**
	 * Instantiates a new bibit method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public BibitMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	 
	

	/**
	 * Instantiates a new bibit method.
	 *
	 * @param patternsize the patternsize
	 * @param maxdiscretizedvalue the maxdiscretizedvalue
	 * @param mingenesbic the mingenesbic
	 * @param minconditionsbic the minconditionsbic
	 * @param maxbiclusters the maxbiclusters
	 * @param usemaxvalueasreference the usemaxvalueasreference
	 */
	private BibitMethod(int patternsize, int maxdiscretizedvalue, int mingenesbic, int minconditionsbic,
			int maxbiclusters, boolean usemaxvalueasreference) {
		super();
		this.patternsize = patternsize;
		this.maxdiscretizedvalue = maxdiscretizedvalue;
		this.mingenesbic = mingenesbic;
		this.minconditionsbic = minconditionsbic;
		this.maxbiclusters = maxbiclusters;
		this.usemaxvalueasreference = usemaxvalueasreference;
	}

	/**
	 * Sets the pattern size.
	 *
	 * @param patternsize the new pattern size
	 */
	public void setPatternSize(int patternsize) {
		this.patternsize = patternsize;
	}

	/**
	 * Sets the max discretized value.
	 *
	 * @param maxdiscretizedvalue the new max discretized value
	 */
	public void setMaxDiscretizedValue(int maxdiscretizedvalue) {
		this.maxdiscretizedvalue = maxdiscretizedvalue;
	}

	/**
	 * Sets the min genes bicluster.
	 *
	 * @param mingenesbic the new min genes bicluster
	 */
	public void setMinGenesBicluster(int mingenesbic) {
		this.mingenesbic = mingenesbic;
	}

	/**
	 * Sets the min conditions bicluster.
	 *
	 * @param minconditionsbic the new min conditions bicluster
	 */
	public void setMinConditionsBicluster(int minconditionsbic) {
		this.minconditionsbic = minconditionsbic;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param maxbiclusters the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int maxbiclusters) {
		this.maxbiclusters = maxbiclusters;
	}

	/**
	 * Sets the use max datatset value as reference in discretization.
	 *
	 * @param usemaxvalueasreference the new use max datatset value as reference in discretization
	 */
	public void setUseMaxDatatsetValueAsReferenceInDiscretization(boolean usemaxvalueasreference) {
		this.usemaxvalueasreference = usemaxvalueasreference;
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
				MINIMUMGENES,
				MINIMUMCONDITIONS,
				MAXBICLUSTERS,
				PATTERNSIZE,
				MAXDISCRETIZEDVALUE,
				MAXVALUEDATASETASREFERENCE
		};
		
		String[] defaultvalues=new String[]{"2","2","0","16","1","false"};
		
		String[] comments=new String[] {
				"Minimum number of genes allowed in a valid bicluster",
				"Minimum number of conditions allowed in a valid bicluster",
				"Number of bicluster to find. If value is zero the algorithm will return the maximum number of biclusters that can find (default=0)",
				"Number of bits to use at the encoding phase default=16",
				"Maximum value in the discretized dataset. From this value, BiBit will binarize the dataset generating max_value different ones",
				"If true, the maximum value of expression dataset will be used as the maximum value in the discretized dataset (previous parameter)"
				
		};
		
		//String source="source: ";
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		mingenesbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINIMUMGENES, 2, 1, true, getClass());
		minconditionsbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINIMUMCONDITIONS, 2, 1, true, getClass());
		maxbiclusters=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MAXBICLUSTERS, 0, 0, true, getClass());
		patternsize=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, PATTERNSIZE, 16, 1, true, getClass());
		maxdiscretizedvalue=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MAXDISCRETIZEDVALUE, 1, 1, true, getClass());
		usemaxvalueasreference=PropertiesUtilities.getBooleanPropertyValue(props, MAXVALUEDATASETASREFERENCE, false, getClass());
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		double[][] expressionmatrix=expressionset.getexpressionDataMatrix();
		if(usemaxvalueasreference)
			maxdiscretizedvalue=(int)MTUMatrixUtils.maxValueOfMatrix(expressionmatrix);
		//System.out.println(maxdiscretizedvalue);
	    matrix=new BibitMatrix(expressionmatrix, patternsize);
	    int pow=(int) Math.pow(2,patternsize);
		
	  //Creating the array with the number of ones of every decimal value between 0 and 2 raise to patternSize
	   int [] distance=new int[pow];
	   BibitUtilities.numberOfOnes(pow, distance);
	   this.listofbiclusters=new BiclusterList();
	   
	   
	   Date starttime =Calendar.getInstance().getTime();
	   
	   for(int i=maxdiscretizedvalue;i>=1 && run;i--){
	//for(int i=maxdiscretizedvalue;i>=1 ;i--){   
		   matrix.discretizeMatrix(i);
		   Pair<ArrayList<ArrayList<Integer>>, ArrayList<ArrayList<Integer>>> bicluster=BibitUtilities.generateBiclusters(matrix, mingenesbic, minconditionsbic, distance, patternsize);
		   if(bicluster!=null){
			   
			   ArrayList<ArrayList<Integer>> geneindexeslist=bicluster.getValue0();
			   ArrayList<ArrayList<Integer>> condindexeslist=bicluster.getValue1();
			   
			   
			   for (int j = 0; j < geneindexeslist.size(); j++) {
				   ArrayList<Integer> genes=geneindexeslist.get(j);
				   ArrayList<Integer> conds=condindexeslist.get(j);
				   if(genes.size()>0 && conds.size()>0){
					   BiclusterResult bic=new BiclusterResult(expressionset, genes, conds, true);
					   
					   if(maxbiclusters==0){
						   listofbiclusters.add(bic);
					   }
					   else{
						   if(maxbiclusters>0 && listofbiclusters.size()<maxbiclusters){
							   listofbiclusters.add(bic);
						   }
						   else
							   run=false;
					   }
                      bic.appendAdditionalInfo("Discretize level", i);
				   }
				   
				   
			   }   
		   }
	   }
	   
	   Date endtime=Calendar.getInstance().getTime();
	   long runtime=endtime.getTime()-starttime.getTime();	
	   runningtime=MTUTimeUtils.getTimeElapsed(runtime);

		return true;
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
		return null;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(MINIMUMGENES, mingenesbic);
		parameters.setKeyintValue(MINIMUMCONDITIONS, minconditionsbic);
		parameters.setKeyintValue(MAXBICLUSTERS, maxbiclusters);
		parameters.setKeyintValue(PATTERNSIZE, patternsize);
		parameters.setKeyintValue(MAXDISCRETIZEDVALUE, maxdiscretizedvalue);
		parameters.setKeybooleanValue(MAXVALUEDATASETASREFERENCE, usemaxvalueasreference);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new BibitMethod(patternsize, maxdiscretizedvalue, mingenesbic, minconditionsbic, maxbiclusters, usemaxvalueasreference);
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
		run=false;
		
	}

	

	

}
