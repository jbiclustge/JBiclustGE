/************************************************************************** 
 * Copyright 2012 - 2017, Orlando Rocha (ornrocha@gmail.com)
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
 */
package jbiclustge.execution.controlcenters.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import smile.imputation.MissingValueImputationException;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractBiclusteringControlCenter.
 */
public abstract class AbstractBiclusteringControlCenter {
	
	
	
	/** The biclustmethods. */
	protected ArrayList<AbstractBiclusteringAlgorithmCaller> biclustmethods;
	protected ArrayList<String> confignamelist;
	protected Map<String, Object> props=null;
	
	/** The data. */
	protected ExpressionData data;

	/**
	 * Instantiates a new abstract biclustering control center.
	 *
	 * @param data the data
	 */
	public AbstractBiclusteringControlCenter(ExpressionData data) {
		   this.data=data;
	}
	
	public void setMapOfProperties(Map<String, Object> props) {
		this.props=props;
	}
	
	
	public void addPropertytoMap(String key, Object value) {
		if(props==null)
			props=new HashMap<>();
		props.put(key, value);
	}
	
	/**
	 * Adds the biclustering method use properties.
	 *
	 * @param method the method
	 * @param methodproperties the methodproperties
	 */
	public void addBiclusteringMethodUseProperties(AbstractBiclusteringAlgorithmCaller method, Properties methodproperties){
		if(biclustmethods==null)
			biclustmethods=new ArrayList<>();
		if(data!=null){
			if(!data.isDataLoaded())
				try {
					data.load();
				} catch (IOException | MissingValueImputationException e) {
					LogMessageCenter.getLogger().addCriticalErrorMessage("Error loading expression dataset", e);
				}
		
			method.setExpressionData(data);
			if(methodproperties!=null)
				method.setAlgorithmProperties(methodproperties);
			biclustmethods.add(method);
		}
	}
	
	
	/**
	 * Adds the biclustering method.
	 *
	 * @param method the method
	 */
	public void addBiclusteringMethod(AbstractBiclusteringAlgorithmCaller method){
		addBiclusteringMethodUseProperties(method,null);
	}
	
	public void addBiclusteringMethod(AbstractBiclusteringAlgorithmCaller method, String configname){
		addBiclusteringMethodUseProperties(method,null);
		if(confignamelist==null)
			confignamelist=new ArrayList<>();
		confignamelist.add(configname);
	}
	
	/**
	 * Adds the biclustering method use file properties.
	 *
	 * @param method the method
	 * @param methodproperties the methodproperties
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addBiclusteringMethodUseFileProperties(AbstractBiclusteringAlgorithmCaller method, String methodproperties) throws FileNotFoundException, IOException{
		addBiclusteringMethodUseProperties(method, AlgorithmProperties.loadProperties(methodproperties));
	}
	
	/**
	 * Sets the biclustering methods list.
	 *
	 * @param methods the new biclustering methods list
	 */
	public void setBiclusteringMethodsList(AbstractBiclusteringAlgorithmCaller[] methods){
		if(methods!=null)
			for (int i = 0; i < methods.length; i++) {
				addBiclusteringMethod(methods[i]);
			}
	}
	
	
	/**
	 * Adds the biclustering method list.
	 *
	 * @param methods the methods
	 */
	public void addBiclusteringMethodList(AbstractBiclusteringAlgorithmCaller...methods){
		setBiclusteringMethodsList(methods);
	}
	
	
	/**
	 * Gets the expression data.
	 *
	 * @return the expression data
	 */
	public ExpressionData getExpressionData() {
		return data;
	}

}
