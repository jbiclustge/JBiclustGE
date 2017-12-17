/*
 * Copyright 2016
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
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
 * Created inside the BIOSYSTEMS Research Group (http://www.ceb.uminho.pt/biosystems)
 * 
 * Author: Orlando Rocha
 */

package jbiclustge.methods.algorithms.wrappers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.utils.properties.AlgorithmProperties;
import jbiclustge.utils.properties.CommandsProcessList;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class BicFinderWrapper.
 */
public class BicFinderMethod extends BiMinePlusMethod{

	/** The Constant ACSI. */
	public static final String ACSI="average_correspondence_similarity_index_threshold";
	/** The Constant NAME. */
	public static final String NAME="BicFinder";
	
	/** The acsi. */
	private double acsi=0.90;
	
	/**
	 * Instantiates a new bic finder wrapper.
	 */
	public BicFinderMethod() {
		super();
	}

	/**
	 * Instantiates a new bic finder wrapper.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public BicFinderMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}

	/**
	 * Instantiates a new bic finder wrapper.
	 *
	 * @param exprs the exprs
	 */
	public BicFinderMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	/**
	 * Instantiates a new bic finder method.
	 *
	 * @param props the props
	 */
	public BicFinderMethod(Properties props){
    	super(props);
    }
	
	/**
	 * Instantiates a new bic finder method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public BicFinderMethod(String propertiesfile){
    	super(propertiesfile);
    }
	
	
	
	/**
	 * Instantiates a new bic finder method.
	 *
	 * @param data the data
	 * @param acsi the acsi
	 * @param asr the asr
	 * @param javaxmx the javaxmx
	 */
	private BicFinderMethod(ExpressionData data, double acsi, double asr, int javaxmx) {
		super(data);
		this.acsi = acsi;
		this.asr=asr;
		this.javaxmx=javaxmx;
	}

	/**
	 * Sets the threshold of the average correspondence similarity index ACSI [0...1]
	 *
	 * @param acsi the new ACSI value
	 */
	public void setAcsi(double acsi) {
		this.acsi = acsi;
	}
	
	/**
	 * Adds the ACSI.
	 *
	 * @param acsi the acsi
	 * @return the bic finder method
	 */
	public BicFinderMethod addACSI(double acsi){
		setAcsi(acsi);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.BiMinePlusMethod#addASR(double)
	 */
	public  BicFinderMethod addASR(double asr){
		setASR(asr);
		return this;
	}

	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.BiMinePlusWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return "BicFinder.jar";
	}
	

	
	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.BiMinePlusWrapper#preconfigureAlgorithm()
	 */
	protected void preconfigureAlgorithm() throws IOException{
		cmds=new CommandsProcessList();
		cmds.add("java");
		cmds.add("-Xmx"+javaxmx+"m");
		cmds.add("-jar");
		cmds.add(getBinaryExecutablePath());
		configureInputData();
		cmds.add(datafilepath);
		cmds.add(String.valueOf(acsi));
		cmds.add(String.valueOf(asr));
		cmds.add(resultsfilepath);
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.BiMinePlusWrapper#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}
	
	/* (non-Javadoc)
	 * @see executors.algorithms.wrappers.IBiclustWrapper#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{ACSI,ASR,JAVAXMX};
		String[] propdefaultvals=new String[]{"0.9","0.6","1024"};
		String[] propcomments=new String[]{
			"ACSI is the threshold of the average correspondence similarity index [0..1]",
			"ASR is the threshold of the average spearmans rho [-1..1]",
			"Java maximum size memory allocation pool"
		};
		return AlgorithmProperties.setupProperties(propkeys, propdefaultvals,propcomments);
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.BiMinePlusMethod#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
	
		this.acsi=PropertiesUtilities.getDoublePropertyValueValidLimits(props, ACSI, 0.9, 0, 1.0, true, this.getClass());
		this.asr=PropertiesUtilities.getDoublePropertyValueValidLimits(props, ASR, 0.6, -1.0, 1.0, true, this.getClass());
		this.javaxmx=PropertiesUtilities.getIntegerPropertyValue(props, JAVAXMX, 1024, getClass());
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.BiMinePlusMethod#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeydoubleValue(ACSI, acsi);
		parameters.setKeydoubleValue(ASR, asr);
		return parameters;
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @return the bic finder method
	 */
	public static BicFinderMethod newInstance(ExpressionData data){
		return new BicFinderMethod(data);
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @param props the props
	 * @return the bic finder method
	 */
	public static BicFinderMethod newInstance(ExpressionData data, Properties props){
		return new BicFinderMethod(data,props);
	}
	
	/**
	 * New instance.
	 *
	 * @param data the data
	 * @param props the props
	 * @return the bic finder method
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BicFinderMethod newInstance(ExpressionData data, String props) throws FileNotFoundException, IOException{
		Properties propz=PropertiesUtilities.loadFileProperties(props);
		return new BicFinderMethod(data,propz);
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.BiMinePlusMethod#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new BicFinderMethod(expressionset, acsi, asr,javaxmx);
	}

}
