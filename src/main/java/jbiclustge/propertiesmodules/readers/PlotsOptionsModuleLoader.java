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
package jbiclustge.propertiesmodules.readers;

import java.util.HashMap;
import java.util.Properties;

import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import pt.ornrocha.propertyutils.PropertiesUtilities;



// TODO: Auto-generated Javadoc
/**
 * The Class PlotsOptionsModuleLoader.
 */
public class PlotsOptionsModuleLoader extends PropertyModuleLoader{
	
	/** The createparallelcord. */
	private boolean createparallelcord=false;
	
	/** The createheatmaps. */
	private boolean createheatmaps=false;

	/**
	 * Instantiates a new plots options module loader.
	 *
	 * @param props the props
	 * @throws Exception the exception
	 */
	public PlotsOptionsModuleLoader(Properties props) throws Exception {
		super(props);
	}

	
	
	
	
	/* (non-Javadoc)
	 * @see propertiesmodules.PropertyModuleLoader#loadProperties()
	 */
	@Override
	public void loadProperties() throws Exception {
	
		createparallelcord=PropertiesUtilities.getBooleanPropertyValue(props, PropertyLabels.MAKEPARALLELCOORD, false, getClass());
		createheatmaps=PropertiesUtilities.getBooleanPropertyValue(props, PropertyLabels.MAKEHEATMAP, false, getClass());
		
	}





	/**
	 * Checks if is creates the parallel coordinates.
	 *
	 * @return true, if is creates the parallel coordinates
	 */
	public boolean isCreateParallelCoordinates() {
		return createparallelcord;
	}





	/**
	 * Checks if is creates the heat maps.
	 *
	 * @return true, if is creates the heat maps
	 */
	public boolean isCreateHeatMaps() {
		return createheatmaps;
	}
	
	
	/**
	 * Load.
	 *
	 * @param props the props
	 * @return the plots options module loader
	 * @throws Exception the exception
	 */
	public static PlotsOptionsModuleLoader load(Properties props) throws Exception{
		PlotsOptionsModuleLoader loader=new PlotsOptionsModuleLoader(props);
		loader.loadProperties();
		return loader;
	}





	@Override
	public HashMap<String, Object> getMapOfProperties() {
		HashMap<String, Object> param=new HashMap<>();
		param.put(PropertyLabels.MAKEPARALLELCOORD, createparallelcord);
		param.put(PropertyLabels.MAKEHEATMAP, createheatmaps);
		return param;
	}

}
