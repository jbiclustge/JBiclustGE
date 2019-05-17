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
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.methods.algorithms.r.components.BCSpectralNormalizationMethod;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

// TODO: Auto-generated Javadoc	
/**
 * The Class RSpectralMethod.
 */
public class RSpectralMethod extends RBiclustAlgorithmCaller{
	
	
	/** The Spectral NEIGENVALUES. */
	public static String Spectral_NEIGENVALUES="spectral_number_of_eigenValues_considered_to_find_biclusters";
	
	/** The Spectral NORMALIZATION. */
	public static String Spectral_NORMALIZATION="spectral_normalization_method";
	
	/** The Spectral MINR. */
	public static String Spectral_MINR="spectral_minimum_number_of_rows_that_biclusters";
	
	/** The Spectral MINC. */
	public static String Spectral_MINC="spectral_minimum_number_of_columns_that_biclusters";
	
	/** The Spectral WITHINVAR. */
	public static String Spectral_WITHINVAR="spectral_maximum_within_variation_allowed";
	
	/** The Constant NAME. */
	public static final String NAME="BCSpectral";
	
	
	
	/** The normalization. */
	private BCSpectralNormalizationMethod normalization=BCSpectralNormalizationMethod.LOG;
	
	/** The numbereigenvalues. */
	private int numbereigenvalues=3;
	
	/** The minrowsbic. */
	private int minrowsbic=2;
	
	/** The mincolumnsbic. */
	private int mincolumnsbic=2;
	
	/** The withinvariation. */
	private double withinvariation=1.0;
	
	
	/**
	 * Instantiates a new r spectral method.
	 */
	public RSpectralMethod() {
		super();
	}

	/**
	 * Instantiates a new r spectral method.
	 *
	 * @param exprs the exprs
	 */
	public RSpectralMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	
	/**
	 * Instantiates a new r spectral method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RSpectralMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new r spectral method.
	 *
	 * @param props the props
	 */
	public RSpectralMethod(Properties props){
		super(props);
	}
	
	/**
	 * Instantiates a new r spectral method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RSpectralMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	
	
	
	/**
	 * Instantiates a new r spectral method.
	 *
	 * @param normalization the normalization
	 * @param numbereigenvalues the numbereigenvalues
	 * @param minrowsbic the minrowsbic
	 * @param mincolumnsbic the mincolumnsbic
	 * @param withinvariation the withinvariation
	 */
	private RSpectralMethod(BCSpectralNormalizationMethod normalization, int numbereigenvalues, int minrowsbic,
			int mincolumnsbic, double withinvariation) {
		super();
		this.normalization = normalization;
		this.numbereigenvalues = numbereigenvalues;
		this.minrowsbic = minrowsbic;
		this.mincolumnsbic = mincolumnsbic;
		this.withinvariation = withinvariation;
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
	 * Sets the normalization method.
	 *
	 * @param normalization the new normalization method
	 */
	public void setNormalizationMethod(BCSpectralNormalizationMethod normalization) {
		this.normalization = normalization;
	}

	/**
	 * Adds the normalization method.
	 *
	 * @param normalization the normalization
	 * @return the r spectral method
	 */
	public RSpectralMethod addNormalizationMethod(BCSpectralNormalizationMethod normalization) {
		this.normalization = normalization;
		return this;
	}




	/**
	 * Sets the number eigen values.
	 *
	 * @param numbereigenvalues the new number eigen values
	 */
	public void setNumberEigenValues(int numbereigenvalues) {
		this.numbereigenvalues = numbereigenvalues;
	}

	
	/**
	 * Adds the number eigen values.
	 *
	 * @param numbereigenvalues the numbereigenvalues
	 * @return the r spectral method
	 */
	public RSpectralMethod addNumberEigenValues(int numbereigenvalues) {
		this.numbereigenvalues = numbereigenvalues;
		return this;
	}


	/**
	 * Sets the min rows bicluster.
	 *
	 * @param minrowsbic the new min rows bicluster
	 */
	public void setMinRowsBicluster(int minrowsbic) {
		this.minrowsbic = minrowsbic;
	}


	/**
	 * Adds the min rows bicluster.
	 *
	 * @param minrowsbic the minrowsbic
	 * @return the r spectral method
	 */
	public RSpectralMethod addMinRowsBicluster(int minrowsbic) {
		this.minrowsbic = minrowsbic;
		return this;
	}



	/**
	 * Sets the min columns bicluster.
	 *
	 * @param mincolumnsbic the new min columns bicluster
	 */
	public void setMinColumnsBicluster(int mincolumnsbic) {
		this.mincolumnsbic = mincolumnsbic;
	}


	/**
	 * Adds the min columns bicluster.
	 *
	 * @param mincolumnsbic the mincolumnsbic
	 * @return the r spectral method
	 */
	public RSpectralMethod addMinColumnsBicluster(int mincolumnsbic) {
		this.mincolumnsbic = mincolumnsbic;
		return this;
	}



	/**
	 * Sets the within variation.
	 *
	 * @param withinvariation the new within variation
	 */
	public void setWithinVariation(double withinvariation) {
		this.withinvariation = withinvariation;
	}


	/**
	 * Adds the within variation.
	 *
	 * @param withinvariation the withinvariation
	 * @return the r spectral method
	 */
	public RSpectralMethod addWithinVariation(double withinvariation) {
		this.withinvariation = withinvariation;
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
		
		this.numbereigenvalues=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, Spectral_NEIGENVALUES, 3, 1,true, this.getClass());
		this.minrowsbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, Spectral_MINR, 2, 2,true, this.getClass());
		this.mincolumnsbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, Spectral_MINC, 2, 2,true, this.getClass());
		this.withinvariation=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, Spectral_WITHINVAR, 1.0, 0, false, this.getClass());
		
		String[] validmethods=new String[]{BCSpectralNormalizationMethod.LOG.toString(), BCSpectralNormalizationMethod.IRRC.toString(),BCSpectralNormalizationMethod.BISTOCHASTIZATION.toString()};
		String normalizationmethod=PropertiesUtilities.getStringPropertyValue(props, Spectral_NORMALIZATION, BCSpectralNormalizationMethod.LOG.toString(), validmethods, this.getClass());
		for (BCSpectralNormalizationMethod method: BCSpectralNormalizationMethod.values()) {
			if(method.toString().equals(normalizationmethod))
				this.normalization=method;
		}
	}

	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				Spectral_NORMALIZATION,
				Spectral_NEIGENVALUES,
				Spectral_MINR,
				Spectral_MINC,
				Spectral_WITHINVAR
		};
		String[] defaultvalues=new String[]{"log","3","2","2","1.0"};
		String[] comments=new String[] {
				"Normalization method, three methods are allowed: log (Logarithmic normalization), irrc (Independent Rescalng of Rows and Columns) and bistochastization",
				"Number of eigenValues considered to find biclusters, high number could increase dramatically time performance",
				"Minimum number of rows that biclusters must have",
				"Minimum number of columns that biclusters must have",
				"Maximum within variation allowed"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: biclust manual, url: https://cran.r-project.org/web/packages/biclust/biclust.pdf");
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {

		try {
			//loadExpressionMatrixInREnvironment();
			loadLabeledExpressionMatrixInREnvironment();
			
			Instant start = Instant.now();
			
			rsession.silentlyEval(getResultOutputName()+" <- biclust("+inputmatrixname+", method=BCSpectral(), normalization=\""+normalization.toString()+"\", numberOfEigenvalues="+String.valueOf(numbereigenvalues)+", minr="+String.valueOf(minrowsbic)+", minc="+String.valueOf(mincolumnsbic)+", withinVar="+String.valueOf(withinvariation)+")",true);
			
			saveElapsedTime(start);
			
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
		return getBaseROjects();
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the r spectral method
	 */
	public static RSpectralMethod newInstance(ExpressionData data){
		return new RSpectralMethod(data);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(Spectral_NEIGENVALUES, numbereigenvalues);
		parameters.setKeyintValue(Spectral_MINR, minrowsbic);
		parameters.setKeyintValue(Spectral_MINC, mincolumnsbic);
		parameters.setKeydoubleValue(Spectral_WITHINVAR, withinvariation);
		parameters.setKeyStringValue(Spectral_NORMALIZATION, normalization.getName());
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RSpectralMethod(normalization, numbereigenvalues, minrowsbic, mincolumnsbic, withinvariation);
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
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		// TODO Auto-generated method stub
		return null;
	}


	

	

}
