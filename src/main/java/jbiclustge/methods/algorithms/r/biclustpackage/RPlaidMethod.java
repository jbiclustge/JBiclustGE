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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.rosuda.REngine.Rserve.RserveException;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.methods.algorithms.r.components.BCPlaidClusterType;
import jbiclustge.rtools.JavaToRUtils;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RPlaidMethod.
 */
public class RPlaidMethod extends RBiclustAlgorithmCaller{
	
	
	/** The plaid ctype. */
	public static String PLAID_CTYPE="plaid_cluster_type";
	
	/** The plaid fitmodel. */
	public static String PLAID_FITMODEL="plaid_model_to_fit_each_layer";
	
	/** The plaid background. */
	public static String PLAID_BACKGROUND="plaid_consider_background_layer";
	
	/** The plaid backgroundlayer. */
	public static String PLAID_BACKGROUNDLAYER="plaid_background_layer_matrix_file";
	
	/** The plaid backgrounddegreesfreedom. */
	public static String PLAID_BACKGROUNDDEGREESFREEDOM="plaid_degrees_of_freedom_of_backround_layer";
	
	/** The plaid suffle. */
	public static String PLAID_SUFFLE="plaid_layer_is_compared_against_number_of_layers";
	
	/** The plaid iterstartup. */
	public static String PLAID_ITERSTARTUP="plaid_number_of_iterations_to_find_starting_values";
	
	/** The plaid iterlayer. */
	public static String PLAID_ITERLAYER="plaid_number_of_iterations_to_find_each_layer";
	
	/** The plaid backfit. */
	public static String PLAID_BACKFIT="plaid_additional_iterations_to_refine_the_fitting_of_the_layer";
	
	/** The plaid rowrelease. */
	public static String PLAID_ROWRELEASE="plaid_threshold_to_prune_rows";
	
	/** The plaid columnrelease. */
	public static String PLAID_COLUMNRELEASE="plaid_threshold_to_prune_columns";
	
	/** The plaid maxlayers. */
	public static String PLAID_MAXLAYERS="plaid_maximum_number_of_layer_to_include_in_the_model";

	
	/** The Constant NAME. */
	public static final String NAME="BCPlaid";
	
	
	
	/** The clustertype. */
	private BCPlaidClusterType clustertype=BCPlaidClusterType.BOTH;
	
	/** The fitmodelformula. */
	private String fitmodelformula="y ~ m + a + b";
	
	/** The background. */
	private boolean background=true;
	
	/** The backgroundmatrix. */
	private double[][] backgroundmatrix=null;
	
	/** The degreesfreedom. */
	private double degreesfreedom=1;
	
	/** The shuffle. */
	private int shuffle=3;
	
	/** The iterstartup. */
	private int iterstartup=5;
	
	/** The iterlayer. */
	private int iterlayer=10;
	
	/** The backfit. */
	private int backfit=0;
	
	/** The rowrelease. */
	private double rowrelease=0.7;
	
	/** The colrelease. */
	private double colrelease=0.7;
	
	/** The maxlayers. */
	private int maxlayers=20;
    
    /** The verbose. */
    private boolean verbose=false; 

    
    
    
	/**
	 * Instantiates a new r plaid method.
	 */
	public RPlaidMethod() {
		super();
		
	}
	
	
	/**
	 * Instantiates a new r plaid method.
	 *
	 * @param exprs the exprs
	 */
	public RPlaidMethod(ExpressionData exprs) {
		super(exprs);
	}

	/**
	 * Instantiates a new r plaid method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RPlaidMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
		
	}
	
	/**
	 * Instantiates a new r plaid method.
	 *
	 * @param props the props
	 */
	public RPlaidMethod(Properties props){
		super(props);
	}

	/**
	 * Instantiates a new r plaid method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RPlaidMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	

	/**
	 * Instantiates a new r plaid method.
	 *
	 * @param clustertype the clustertype
	 * @param fitmodelformula the fitmodelformula
	 * @param background the background
	 * @param backgroundmatrix the backgroundmatrix
	 * @param degreesfreedom the degreesfreedom
	 * @param shuffle the shuffle
	 * @param iterstartup the iterstartup
	 * @param iterlayer the iterlayer
	 * @param backfit the backfit
	 * @param rowrelease the rowrelease
	 * @param colrelease the colrelease
	 * @param maxlayers the maxlayers
	 * @param verbose the verbose
	 */
	private RPlaidMethod(BCPlaidClusterType clustertype, String fitmodelformula, boolean background,
			double[][] backgroundmatrix, double degreesfreedom, int shuffle, int iterstartup, int iterlayer,
			int backfit, double rowrelease, double colrelease, int maxlayers, boolean verbose) {
		super();
		this.clustertype = clustertype;
		this.fitmodelformula = fitmodelformula;
		this.background = background;
		this.backgroundmatrix = backgroundmatrix;
		this.degreesfreedom = degreesfreedom;
		this.shuffle = shuffle;
		this.iterstartup = iterstartup;
		this.iterlayer = iterlayer;
		this.backfit = backfit;
		this.rowrelease = rowrelease;
		this.colrelease = colrelease;
		this.maxlayers = maxlayers;
		this.verbose = verbose;
	}


	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
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
	 * Sets the cluster type.
	 *
	 * @param clustertype the new cluster type
	 */
	public void setClusterType(BCPlaidClusterType clustertype) {
		this.clustertype = clustertype;
	}
	
	/**
	 * Adds the cluster type.
	 *
	 * @param clustertype the clustertype
	 * @return the r plaid method
	 */
	public RPlaidMethod addClusterType(BCPlaidClusterType clustertype) {
		this.clustertype = clustertype;
		return this;
	}

	/**
	 * Sets the fit model formula.
	 *
	 * @param fitmodelformula the new fit model formula
	 */
	public void setFitModelFormula(String fitmodelformula) {
		this.fitmodelformula = fitmodelformula;
	}
	
	/**
	 * Adds the fit model formula.
	 *
	 * @param fitmodelformula the fitmodelformula
	 * @return the r plaid method
	 */
	public RPlaidMethod addFitModelFormula(String fitmodelformula) {
		this.fitmodelformula = fitmodelformula;
		return this;
	}

	/**
	 * Sets the background.
	 *
	 * @param background the new background
	 */
	public void setBackground(boolean background) {
		this.background = background;
	}
	
	/**
	 * Adds the background.
	 *
	 * @param background the background
	 * @return the r plaid method
	 */
	public RPlaidMethod addBackground(boolean background) {
		this.background = background;
		return this;
	}

	/**
	 * Sets the background matrix.
	 *
	 * @param backgroundmatrix the new background matrix
	 */
	public void setBackgroundMatrix(double[][] backgroundmatrix) {
		this.backgroundmatrix = backgroundmatrix;
	}
	
	/**
	 * Adds the background matrix.
	 *
	 * @param backgroundmatrix the backgroundmatrix
	 * @return the r plaid method
	 */
	public RPlaidMethod addBackgroundMatrix(double[][] backgroundmatrix) {
		this.backgroundmatrix = backgroundmatrix;
		return this;
	}

	/**
	 * Sets the background degreesfreedom.
	 *
	 * @param degreesfreedom the new background degreesfreedom
	 */
	public void setBackgroundDegreesfreedom(double degreesfreedom) {
		this.degreesfreedom = degreesfreedom;
	}
	
	/**
	 * Adds the background degreesfreedom.
	 *
	 * @param degreesfreedom the degreesfreedom
	 * @return the r plaid method
	 */
	public RPlaidMethod  addBackgroundDegreesfreedom(double degreesfreedom) {
		this.degreesfreedom = degreesfreedom;
		return this;
	}

	/**
	 * Sets the shuffle.
	 *
	 * @param shuffle the new shuffle
	 */
	public void setShuffle(int shuffle) {
		this.shuffle = shuffle;
	}
	
	/**
	 * Adds the shuffle.
	 *
	 * @param shuffle the shuffle
	 * @return the r plaid method
	 */
	public RPlaidMethod addShuffle(int shuffle) {
		this.shuffle = shuffle;
		return this;
	}

	/**
	 * Sets the iteration startup.
	 *
	 * @param iterstartup the new iteration startup
	 */
	public void setIterationStartup(int iterstartup) {
		this.iterstartup = iterstartup;
	}
	
	/**
	 * Adds the iteration startup.
	 *
	 * @param iterstartup the iterstartup
	 * @return the r plaid method
	 */
	public RPlaidMethod addIterationStartup(int iterstartup) {
		this.iterstartup = iterstartup;
		return this;
	}

	/**
	 * Sets the iteration layer.
	 *
	 * @param iterlayer the new iteration layer
	 */
	public void setIterationLayer(int iterlayer) {
		this.iterlayer = iterlayer;
	}
	
	/**
	 * Adds the iteration layer.
	 *
	 * @param iterlayer the iterlayer
	 * @return the r plaid method
	 */
	public RPlaidMethod addIterationLayer(int iterlayer) {
		this.iterlayer = iterlayer;
		return this;
	}

	/**
	 * Sets the back fit.
	 *
	 * @param backfit the new back fit
	 */
	public void setBackFit(int backfit) {
		this.backfit = backfit;
	}
	
	/**
	 * Adds the back fit.
	 *
	 * @param backfit the backfit
	 * @return the r plaid method
	 */
	public RPlaidMethod addBackFit(int backfit) {
		this.backfit = backfit;
		return this;
	}

	/**
	 * Sets the row release.
	 *
	 * @param rowrelease the new row release
	 */
	public void setRowRelease(double rowrelease) {
		this.rowrelease = rowrelease;
	}
	
	/**
	 * Adds the row release.
	 *
	 * @param rowrelease the rowrelease
	 * @return the r plaid method
	 */
	public RPlaidMethod addRowRelease(double rowrelease) {
		this.rowrelease = rowrelease;
		return this;
	}

	/**
	 * Sets the column release.
	 *
	 * @param colrelease the new column release
	 */
	public void setColumnRelease(double colrelease) {
		this.colrelease = colrelease;
	}
	
	/**
	 * Adds the column release.
	 *
	 * @param colrelease the colrelease
	 * @return the r plaid method
	 */
	public RPlaidMethod addColumnRelease(double colrelease) {
		this.colrelease = colrelease;
		return this;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param maxlayers the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int maxlayers) {
		this.maxlayers = maxlayers;
	}
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param maxlayers the maxlayers
	 * @return the r plaid method
	 */
	public RPlaidMethod addNumberBiclustersToFind(int maxlayers) {
		this.maxlayers = maxlayers;
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
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.fitmodelformula=PropertiesUtilities.getStringPropertyValue(props, PLAID_FITMODEL, "y ~ m + a + b", this.getClass());
		this.background=PropertiesUtilities.getBooleanPropertyValue(props, PLAID_BACKGROUND, true, this.getClass());
	    this.degreesfreedom=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, PLAID_BACKGROUNDDEGREESFREEDOM, 1, 0, false,this.getClass());
	    this.shuffle=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, PLAID_SUFFLE, 3, 1, true, this.getClass());
	    this.iterstartup=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, PLAID_ITERSTARTUP, 5, 1, true, this.getClass());
	    this.iterlayer=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, PLAID_ITERLAYER, 10, 1, true, this.getClass());
	    this.backfit=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, PLAID_BACKFIT, 0, 0, true, this.getClass());
	    this.rowrelease=PropertiesUtilities.getDoublePropertyValueValidLimits(props, PLAID_ROWRELEASE, 0.7, 0, 1, true, this.getClass());
	    this.colrelease=PropertiesUtilities.getDoublePropertyValueValidLimits(props, PLAID_COLUMNRELEASE, 0.7, 0, 1, true, this.getClass());
	    this.maxlayers=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, PLAID_MAXLAYERS, 20, 2, true, this.getClass());
	    
	    String typecluster=PropertiesUtilities.getStringPropertyValue(props, PLAID_CTYPE, "b", new String[]{"b","r","c"}, this.getClass());
	    for (BCPlaidClusterType type : BCPlaidClusterType.values()) {
			if(typecluster.toLowerCase().equals(type.toString()))
				this.clustertype=type;
		}
	    
	    String filebackgroundmatrix=PropertiesUtilities.getStringPropertyValue(props, PLAID_BACKGROUNDLAYER, null, this.getClass());
	    if(filebackgroundmatrix!=null){
	    	File f=new File(filebackgroundmatrix);
	    	if(f.exists())
				try {
					backgroundmatrix=MTUReadUtils.readDoubleMatrixFromFile(filebackgroundmatrix, "\t");
				} catch (IOException e) {
					backgroundmatrix=null;
				}
	    }
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				PLAID_CTYPE,
				PLAID_FITMODEL,
				PLAID_BACKGROUND,
				PLAID_BACKGROUNDLAYER,
				PLAID_BACKGROUNDDEGREESFREEDOM,
				PLAID_SUFFLE,
				PLAID_ITERSTARTUP,
				PLAID_ITERLAYER,
				PLAID_BACKFIT,
				PLAID_ROWRELEASE,
				PLAID_COLUMNRELEASE,
				PLAID_MAXLAYERS
		};
		String[] defaultvalues=new String[]{"b","y ~ m + a + b","true","","1","3","5","10","0","0.7","0.7","20"};
		String[] comments=new String[] {
				"r, c or b, to cluster rows, columns or both",
				"Model (formula) to fit each layer",
				"If true the method will consider that a background layer, [true, false]",
				"A background layer can be specified in a file containing a numeric matrix with dimension of data matrix",
				"Degrees of Freedom of backround layer if background.layer is specified",
				"Before a layer is added, it is statistical significance is compared against a x number of layers obtained by random defined by this parameter",
				"Number of iterations to find starting values",
				"Number of iterations to find each layer",
				"After a layer is added, additional iterations can be done to refine the fitting of the layer",
				"Threshold to prune rows in the layers depending on row homogeneity [0-1]",
				"Threshold to prune columns in the layers depending on column homogeneity [0-1]",
				"Maximum number of layer to include in the model (max number biclusters to be found) "
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		try {
			loadExpressionMatrixInREnvironment();

			Date starttime =Calendar.getInstance().getTime();
			rsession.silentlyEval(getResultOutputName()+" <- biclust("+inputmatrixname+", method=BCPlaid(),"
					+ " cluster=\""+clustertype.toString()+"\", fit.model ="+fitmodelformula+","
							+ " background ="+JavaToRUtils.convertBooleanToR(background)+", background.layer ="+setBackgroundLayer()+","
									+ " background.df ="+String.valueOf(degreesfreedom)+", row.release ="+String.valueOf(rowrelease)+","
											+ " col.release ="+String.valueOf(colrelease)+", shuffle ="+String.valueOf(shuffle)+", back.fit ="+String.valueOf(backfit)+","
													+ " max.layers ="+String.valueOf(maxlayers)+", iter.startup ="+String.valueOf(iterstartup)+", iter.layer ="+String.valueOf(iterlayer)+","
															+ " verbose ="+JavaToRUtils.convertBooleanToR(verbose)+")",true);
			
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
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		return getBaseROjects();
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
	 * @return the r plaid method
	 */
	public static RPlaidMethod newInstance(ExpressionData data){
		return new RPlaidMethod(data);
	}
	
	
	/**
	 * Sets the background layer.
	 *
	 * @return the string
	 * @throws RserveException the rserve exception
	 */
	protected String setBackgroundLayer() throws RserveException{
		if(backgroundmatrix==null)
			return "NA";
		else{
			String matname="plaidbackgroudmatrix";
			rsession.set(matname, backgroundmatrix);
			
			return matname;
		}
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(PLAID_MAXLAYERS, maxlayers);
		parameters.setKeydoubleValue(PLAID_ROWRELEASE, rowrelease);
		parameters.setKeydoubleValue(PLAID_COLUMNRELEASE, colrelease);
		parameters.setKeyStringValue(PLAID_FITMODEL, fitmodelformula);
		parameters.setKeybooleanValue(PLAID_BACKGROUND, background);
		parameters.setKeydoubleValue(PLAID_BACKGROUNDDEGREESFREEDOM, degreesfreedom);
		parameters.setKeyintValue(PLAID_SUFFLE, shuffle);
		parameters.setKeyintValue(PLAID_ITERSTARTUP, iterstartup);
		parameters.setKeyintValue(PLAID_ITERLAYER, iterlayer);
		parameters.setKeyintValue(PLAID_BACKFIT, backfit);
		return parameters;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RPlaidMethod(clustertype, fitmodelformula, background, backgroundmatrix, degreesfreedom, shuffle, iterstartup, iterlayer, backfit, rowrelease, colrelease, maxlayers, verbose);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		return null;
	}





	
	
	
	
	
	

}
