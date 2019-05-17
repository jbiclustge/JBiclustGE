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
package jbiclustge.methods.algorithms.wrappers.bimax;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.analysis.overlap.OverlapAnalyser;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.processdata.binarization.IDiscretizationMethod;
import jbiclustge.datatools.expressiondata.processdata.binarization.methods.BiMaxBinarizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class BimaxMethodCLib.
 */
public class BimaxMethodCLib extends AbstractBiclusteringAlgorithmCaller{
	
	/** The Constant MINGENES. */
	public static final String MINGENES="biclusters_have_at_least_x_genes";
	
	/** The Constant MINCONDITIONS. */
	public static final String MINCONDITIONS="biclusters_have_at_least_x_conditions";
	
	/** The Constant MAXNUMBERBICLUSTERS. */
	public static final String MAXNUMBERBICLUSTERS="max_number_biclusters";
	
	/** The Constant OVERLAPTHRESHOLD. */
	public static final String OVERLAPTHRESHOLD="overlap_threshold";
	
	
	/** The Constant NAME. */
	public static final String NAME="BimaxClib";
	
	/** The binarizationmethod. */
	private IDiscretizationMethod binarizationmethod=BiMaxBinarizationMethod.newInstance();
	
	/** The mingenes. */
	private int mingenes=0;
	
	/** The minconditions. */
	private int minconditions=0;
	
	/** The maxnumberbiclusters. */
	private int maxnumberbiclusters=1;
	
	/** The overlapthreshold. */
	private double overlapthreshold=1.0;
	
	/** The overlapiterations. */
	private double overlapiterations=20;
	
	/** The runinstance. */
	//private BiclusterList templist;
	private RunBimax runinstance;
	
	/**
	 * Instantiates a new bimax method C lib.
	 */
	public BimaxMethodCLib() {
		super();
	}

	/**
	 * Instantiates a new bimax method C lib.
	 *
	 * @param exprs the exprs
	 */
	public BimaxMethodCLib(ExpressionData exprs) {
		super(exprs);
	}
	
	/**
	 * Instantiates a new bimax method C lib.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public BimaxMethodCLib(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new bimax method C lib.
	 *
	 * @param props the props
	 */
	public BimaxMethodCLib(Properties props) {
		super(props);
	}

	/**
	 * Instantiates a new bimax method C lib.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public BimaxMethodCLib(String propertiesfile) {
		super(propertiesfile);
	}
	
	
	
	/**
	 * Instantiates a new bimax method C lib.
	 *
	 * @param binarizationmethod the binarizationmethod
	 * @param mingenes the mingenes
	 * @param minconditions the minconditions
	 * @param maxnumberbiclusters the maxnumberbiclusters
	 * @param overlapthreshold the overlapthreshold
	 * @param overlapiterations the overlapiterations
	 */
	private BimaxMethodCLib(IDiscretizationMethod binarizationmethod, int mingenes, int minconditions,
			int maxnumberbiclusters, double overlapthreshold, double overlapiterations) {
		super();
		this.binarizationmethod = binarizationmethod;
		this.mingenes = mingenes;
		this.minconditions = minconditions;
		this.maxnumberbiclusters = maxnumberbiclusters;
		this.overlapthreshold = overlapthreshold;
		this.overlapiterations = overlapiterations;
	}

	/**
	 * Instantiates a new bimax method C lib.
	 *
	 * @param exprs the exprs
	 * @param binarizationmethod the binarizationmethod
	 */
	public BimaxMethodCLib(ExpressionData exprs, IDiscretizationMethod binarizationmethod) {
		super(exprs);
		this.binarizationmethod=binarizationmethod;
	}
	
	
	/**
	 * Sets the bicluster min genes.
	 *
	 * @param mingenes the new bicluster min genes
	 */
	public void setBiclusterMinGenes(int mingenes) {
		this.mingenes = mingenes;
	}
	
	/**
	 * Adds the bicluster min genes.
	 *
	 * @param mingenes the mingenes
	 * @return the bimax method C lib
	 */
	public BimaxMethodCLib addBiclusterMinGenes(int mingenes) {
		this.mingenes = mingenes;
		return this;
	}

	/**
	 * Sets the bicluster min conditions.
	 *
	 * @param minconditions the new bicluster min conditions
	 */
	public void setBiclusterMinConditions(int minconditions) {
		this.minconditions = minconditions;
	}
	
	/**
	 * Adds the bicluster min conditions.
	 *
	 * @param minconditions the minconditions
	 * @return the bimax method C lib
	 */
	public BimaxMethodCLib addBiclusterMinConditions(int minconditions) {
		this.minconditions = minconditions;
		return this;
	}
	
	/**
	 * Sets the max number biclusters.
	 *
	 * @param nbic the new max number biclusters
	 */
	public void setMaxNumberBiclusters(int nbic){
		this.maxnumberbiclusters=nbic;
	}
	
	/**
	 * Sets the overlap treshold.
	 *
	 * @param value the new overlap treshold
	 */
	public void setOverlapTreshold(double value){
		this.overlapthreshold=value;
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
				MINGENES,
				MINCONDITIONS,
				MAXNUMBERBICLUSTERS,
				OVERLAPTHRESHOLD
		};
		
		
		String[] defaultvalues=new String[]{"0","0","1","1.0"};
		
		String[] comments=new String[] {
				"biclusters output need to have at least that many genes, 0= no limits",
				"biclusters output need to have at least that many conditions, 0= no limits",
				"number of bicluster to find, default=1",
				"overlap threshold, value between [0,1] interval. If value is equal to 1.0, the algorithm will return the maximum number of biclusters defined by user (default=1.0)\n.Otherwise only return the biclusters which respect overlap threshold"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		this.mingenes=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINCONDITIONS, 0,0,true,this.getClass());
		this.mingenes=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props,MINCONDITIONS, 0,0,true,this.getClass());
		this.maxnumberbiclusters=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props,MAXNUMBERBICLUSTERS, 1,1,true,this.getClass());
		this.overlapthreshold=PropertiesUtilities.getDoublePropertyValueValidLimits(props, OVERLAPTHRESHOLD, 1.0, 0.0, 1.0, true, getClass());
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		Instant start = Instant.now();
		try {
			int[][] data=expressionset.getBinarizedMatrix(binarizationmethod);
			if(overlapthreshold==1.0){
				runinstance= RunBimax.setAndExecute(data, mingenes, minconditions, maxnumberbiclusters);
				this.listofbiclusters=getBiclusters();
			}
			else{
				runWithOverlapping(data);
			}
				

			saveElapsedTime(start);
			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error executing "+getAlgorithmName()+": ", e);
			return false;
		}
		
		return true;
		
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		//this.listofbiclusters=templist;	
	}
	
	/**
	 * Gets the biclusters.
	 *
	 * @return the biclusters
	 */
	private BiclusterList getBiclusters(){
		BiclusterList templist=new BiclusterList();
		ArrayList<ArrayList<Integer>> geneids=getBiclustersGenes();
		ArrayList<ArrayList<Integer>> condids=getBiclustersConditions();
		
		for (int i = 0; i < geneids.size(); i++) {
			if(geneids.get(i).size()>0 && condids.get(i).size()>0){
				BiclusterResult bicres=new BiclusterResult(expressionset, geneids.get(i),condids.get(i), true);
				templist.add(bicres);
			}
		}
		return templist;
	}
	
	
	/**
	 * Run with overlapping.
	 *
	 * @param data the data
	 * @throws Exception the exception
	 */
	protected void runWithOverlapping(int[][] data) throws Exception{
		
		LogMessageCenter.getLogger().addInfoMessage("Running biclusters overlap threshold selection...");
		int initmaxbic=maxnumberbiclusters;
		int iter=0;
		//int repeatedsize=0;
		int lastsize=0;
		boolean run=true;
		
		while (run) {
			//System.out.println("Iter: "+iter+" MAx Bic: "+initmaxbic);
			runinstance=RunBimax.setAndExecute(data, mingenes, minconditions, initmaxbic);
			
			BiclusterList filtered=OverlapAnalyser.filterBiclusterListWithOverlapThreshold(getBiclusters(), overlapthreshold, maxnumberbiclusters);
			
			

			if(filtered.size()==maxnumberbiclusters || iter==overlapiterations){	
				run=false;
				this.listofbiclusters=filtered;
			}
			else{
				if(lastsize!=0 && filtered.size()!=lastsize){
					iter=0;
				}
				else
					iter++;
				
				lastsize=filtered.size();
				initmaxbic+=300;
				
			}

		}
		//System.out.println("Iterations: "+iter);
	}
	
	
	
	
	
	
	
	
	

/*	@Override
	protected void processResults() throws Exception {
		int[][] bicrows=runinstance.getRowsinbiclusters();
		int[][] biccols=runinstance.getColumnsinbiclusters();
		this.listofbiclusters=new BiclusterList();
		
		for (int i = 0; i < bicrows[0].length; i++) {
			ArrayList<Integer> geneindex=new ArrayList<>();
			ArrayList<Integer> condindex=new ArrayList<>();
			for (int j = 0; j < bicrows.length; j++) {
				int value=bicrows[j][i];
				if(value==1)
					geneindex.add(j);
			}
			
			if(geneindex.size()==0)
				continue;
			else{
				for (int j = 0; j < biccols.length; j++) {
					int value=biccols[j][i];
					if(value==1)
						condindex.add(j);
				}
				if(condindex.size()>0){
					BiclusterResult res=new BiclusterResult(expressionset, geneindex, condindex, true);
					listofbiclusters.add(res);
				}
			}
			
			
		}
		
	}*/

	/* (non-Javadoc)
 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
 */

	/**
	 * Gets the biclusters genes.
	 *
	 * @return the biclusters genes
	 */
	private ArrayList<ArrayList<Integer>> getBiclustersGenes(){
		int[][] bicrows=runinstance.getRowsinbiclusters();
		ArrayList<ArrayList<Integer>> res=new ArrayList<>();
		for (int i = 0; i < bicrows[0].length; i++) {
			ArrayList<Integer> geneindexes=new ArrayList<>();
			for (int j = 0; j < bicrows.length; j++) {
				int value=bicrows[j][i];
				if(value!=0){
					//System.out.println(value-1);
					geneindexes.add((value-1));
				}
			}
			res.add(geneindexes);
		}
		return res;
		
	}
	
	/**
	 * Gets the biclusters conditions.
	 *
	 * @return the biclusters conditions
	 */
	private ArrayList<ArrayList<Integer>> getBiclustersConditions(){
		int[][] biccols=runinstance.getColumnsinbiclusters();
		ArrayList<ArrayList<Integer>> res=new ArrayList<>();
		for (int i = 0; i < biccols[0].length; i++) {
			ArrayList<Integer> condindexes=new ArrayList<>();
			for (int j = 0; j < biccols.length; j++) {
				int value=biccols[j][i];
				if(value!=0){
					condindexes.add((value-1));
				}
			}
			res.add(condindexes);
		}
		return res;
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return false;
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new BimaxMethodCLib(binarizationmethod, mingenes, minconditions, maxnumberbiclusters, overlapthreshold, overlapiterations);
	}

	@Override
	public void stopProcess() {
		// TODO Auto-generated method stub
		
	}

	
	

	
	
	
	
	
	
	
	

}
