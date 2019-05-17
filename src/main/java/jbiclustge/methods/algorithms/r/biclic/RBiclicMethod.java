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
package jbiclustge.methods.algorithms.r.biclic;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class RBiclicMethod.
 */
public class RBiclicMethod extends RBiclustAlgorithmCaller{

	/** The Constant CORRELATIONTHRESHOLD. */
	public static final String CORRELATIONTHRESHOLD="correlation_threshold";
	
	/** The Constant MINROWS. */
	public static final String MINROWS="minimum_number_of_rows_in_biclusters";
	
	/** The Constant MINCOLUMNS. */
	public static final String MINCOLUMNS="minimum_number_of_columns_in_biclusters";
	
	/** The Constant OVERLAPTHRESHOLD. */
	public static final String OVERLAPTHRESHOLD="overlap_threshold";
	
	/** The Constant NAME. */
	public static final String NAME="BICLIC";
	
	/** The tempfolder. */
	private String tempfolder=null;
	
	/** The datafilepath. */
	private String datafilepath=null;
	
	/** The corstand. */
	private double corstand=0.9;
	
	/** The minrow. */
	private int minrow=5;
	
	/** The mincol. */
	private int mincol=5;
	
	/** The overlaplevel. */
	private double overlaplevel=1.0;
	
	
	/**
	 * Instantiates a new r biclic method.
	 */
	public RBiclicMethod() {
		super();
		ismultisession=true;
	}
	
	/**
	 * Instantiates a new r biclic method.
	 *
	 * @param exprs the exprs
	 */
	public RBiclicMethod(ExpressionData exprs) {
		super(exprs);
		ismultisession=true;
	}
	
	/**
	 * Instantiates a new r biclic method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RBiclicMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
		ismultisession=true;
	}
	
	/**
	 * Instantiates a new r biclic method.
	 *
	 * @param props the props
	 */
	public RBiclicMethod(Properties props) {
		super(props);
		ismultisession=true;
	}
	
	/**
	 * Instantiates a new r biclic method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RBiclicMethod(String propertiesfile) {
		super(propertiesfile);
		ismultisession=true;
	}
	
	
	

	/**
	 * Instantiates a new r biclic method.
	 *
	 * @param corstand the corstand
	 * @param minrow the minrow
	 * @param mincol the mincol
	 * @param overlaplevel the overlaplevel
	 */
	private RBiclicMethod(double corstand, int minrow, int mincol, double overlaplevel) {
		super();
		this.corstand = corstand;
		this.minrow = minrow;
		this.mincol = mincol;
		this.overlaplevel = overlaplevel;
		this.ismultisession=true;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() throws IOException {
		ArrayList<String> sources=new ArrayList<>();
		sources.add(SystemFolderTools.getMethodExePath("BICLIC.R"));
		return sources;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#requiredLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> requiredLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("matrixStats"));
		return list;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		// TODO Auto-generated method stub
		return null;
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

	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{
				CORRELATIONTHRESHOLD,
				MINROWS,
				MINCOLUMNS,
				OVERLAPTHRESHOLD
		};
		
		
		String[] defaultvalues=new String[]{"0.9","5","5","1.0"};
		
		String[] comments=new String[] {
				"The correlation threshold to find correlated expression",
				"The minimum number of rows for final bicluster matrix",
				"The minimum number of condition for final bicluster matrix. This should be larger than 2",
				"The threshold to filter out overlapped biclusters, 1 means filtering 100% overlapped biclusters"
		};
		
		String source="source: http://bisyn.kaist.ac.kr/software/biclic.htm";
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,source);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		this.corstand=PropertiesUtilities.getDoublePropertyValueValidLimits(props, CORRELATIONTHRESHOLD, 0.9, 0, false, 1.0, true, getClass());
		this.minrow=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINROWS, 5, 1, true, getClass());
		this.mincol=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINCOLUMNS, 5, 2, false, getClass());
		this.overlaplevel=PropertiesUtilities.getDoublePropertyValueValidLimits(props,OVERLAPTHRESHOLD, 1.0, 0.0, true, 1.0, true, getClass());
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		//LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Starting "+getAlgorithmName()+" biclustering method...please wait");
		configureInputData();

		try {
			
			Instant start = Instant.now();
			
			rsession.silentlyEval(getResultOutputName()+"<- BICLIC(\""+datafilepath+"\", "+String.valueOf(corstand)+", "+String.valueOf(minrow)+", "+String.valueOf(mincol)+", "+String.valueOf(overlaplevel)+")");
			
			saveElapsedTime(start);
			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in executing "+getAlgorithmName()+": ", e);
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Configure input data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void configureInputData() throws IOException{
		tempfolder=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("BICLIC");
		datafilepath=FilenameUtils.concat(tempfolder, UUID.randomUUID().toString()+".txt");
		//expressionset.writeExpressionDatasetToFile(datafilepath);
		expressionset.writeExpressionDatasetLabeledFormatToFile(datafilepath);
		
	}
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		
		listofbiclusters=new BiclusterList();
		
		int totalres=0;
		try {
			totalres=rsession.eval("length(merged_rows)").asInteger();
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error in getting the number of calculated biclusters by "+getAlgorithmName(), e);
			throw e;
			
		}
		
		if(totalres>0){
	
			
			for (int i = 0; i < totalres; i++) {
				
				double[] geneindexes=rsession.eval("merged_rows[["+String.valueOf(i+1)+"]]").asDoubles();
				double[] condindexes=rsession.eval("merged_cols[["+String.valueOf(i+1)+"]]").asDoubles();
				addBiclusterResult(geneindexes, condindexes);
			}
			
		}
	}
	
	
	/**
	 * Adds the bicluster result.
	 *
	 * @param decodedgenes the decodedgenes
	 * @param decodedconditions the decodedconditions
	 */
	private void addBiclusterResult(double[] decodedgenes, double[] decodedconditions){
		
		ArrayList<Integer> genes=new ArrayList<>();
		for (int i = 0; i < decodedgenes.length; i++) {
			genes.add(((int)decodedgenes[i]-1));
		}
		
		ArrayList<Integer> conditions=new ArrayList<>();
		for (int i = 0; i < decodedconditions.length; i++) {
			conditions.add(((int)decodedconditions[i]-1));
		}
		
		BiclusterResult result=new BiclusterResult(expressionset, genes, conditions, true);
		listofbiclusters.add(result);
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeydoubleValue(CORRELATIONTHRESHOLD, corstand);
		parameters.setKeyintValue(MINROWS, minrow);
		parameters.setKeyintValue(MINCOLUMNS, mincol);
		parameters.setKeydoubleValue(OVERLAPTHRESHOLD, overlaplevel);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RBiclicMethod(corstand, minrow, mincol, overlaplevel);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return true;
	}

	

}
