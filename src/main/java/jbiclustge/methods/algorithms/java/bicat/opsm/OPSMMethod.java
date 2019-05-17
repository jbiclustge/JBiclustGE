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
package jbiclustge.methods.algorithms.java.bicat.opsm;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.java.bicat.auxtools.Bicluster;
import jbiclustge.methods.algorithms.java.bicat.opsm.components.BendorReloadedAlgorithm;
import jbiclustge.methods.algorithms.java.bicat.opsm.components.OPSMDataset;
import jbiclustge.methods.algorithms.wrappers.components.KillerProcess;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class OPSMMethod extends AbstractBiclusteringAlgorithmCaller{
	
	public static final String NAME="OPSM";
	public static final String NUMBERBESTMODELS="number_best_partial_models_using_next_iteration";
	
	private int numberbestpartialmodels=10; // Pick the n best partial models for next iteration
	private LinkedList<Bicluster> outputBiclusters;
    private KillerProcess killer=new KillerProcess();

	public OPSMMethod() {
		super();
	}

	
	public OPSMMethod(ExpressionData exprs) {
		super(exprs);
		
	}

	public OPSMMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	public OPSMMethod(Properties props) {
		super(props);
	}
	
	public OPSMMethod(String propertiesfile) {
		super(propertiesfile);
	}
	
	

	
	private OPSMMethod(ExpressionData data, int numberbestpartialmodels) {
		super(data);
		this.numberbestpartialmodels = numberbestpartialmodels;
	}


	public void setNumberBestPartialModels(int numberbestpartialmodels) {
		this.numberbestpartialmodels = numberbestpartialmodels;
	}

	public OPSMMethod addNumberBestPartialModels(int numberbestpartialmodels) {
		this.numberbestpartialmodels = numberbestpartialmodels;
		return this;
	}
	
	

	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	@Override
	protected synchronized boolean runAlgorithm() {
		
		Instant start = Instant.now();
		final BendorReloadedAlgorithm opsm = new BendorReloadedAlgorithm();
		OPSMDataset indata = new OPSMDataset(expressionset.getexpressionDataMatrixFloatValues()); 
		outputBiclusters=opsm.run(indata, numberbestpartialmodels,killer);
		
		if(!killer.isToKill()) {
			saveElapsedTime(start);

			if(outputBiclusters!=null && outputBiclusters.size()>0)
				return true;
		}
		
		return false;
	}
	
	@Override
	protected void processResults() {
		listofbiclusters=new BiclusterList();
		for (Bicluster bic : outputBiclusters) {
			listofbiclusters.add(convertToInternalBiclusterFormat(bic));
		}
		
	}
	
	
	protected BiclusterResult convertToInternalBiclusterFormat(Bicluster opsmbic){
		return new BiclusterResult(expressionset, opsmbic.getGenes(), opsmbic.getChips(), true);

	}
	
	

	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.numberbestpartialmodels=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, NUMBERBESTMODELS, 10, 1, true, this.getClass());
	}


	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{NUMBERBESTMODELS};
		String[] defaultvalues=new String[]{"10"};
		String[] comments=new String[] {"Pick the n best partial models for next iteration"};
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments);
	}

    public static OPSMMethod newInstance(ExpressionData data){
    	return new OPSMMethod(data);
    }


	@Override
	protected String getTemporaryWorkingDirectory() {
		return null;
	}


	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(NUMBERBESTMODELS, numberbestpartialmodels);
		return parameters;
	}


	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new OPSMMethod(expressionset, numberbestpartialmodels);
	}


	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		return false;
	}


	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected boolean supportedinoperatingsystem() {
		return true;
	}


	@Override
	public void stopProcess() {
		Thread t=new Thread(killer);
		t.start();
	}
	

}
