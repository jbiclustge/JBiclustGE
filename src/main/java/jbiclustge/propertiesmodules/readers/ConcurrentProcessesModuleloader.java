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
 * The Class ConcurrentProcessesModuleloader.
 */
public class ConcurrentProcessesModuleloader extends PropertyModuleLoader{

	
	/** The nprocesses. */
	private int nprocesses=1;
	
	
	/**
	 * Instantiates a new concurrent processes moduleloader.
	 *
	 * @param props the props
	 * @throws Exception the exception
	 */
	public ConcurrentProcessesModuleloader(Properties props) throws Exception {
		super(props);
		loadProperties();
	}

	/* (non-Javadoc)
	 * @see propertiesmodules.PropertyModuleLoader#loadProperties()
	 */
	@Override
	public void loadProperties() throws Exception {
		
		if(PropertiesUtilities.isValidProperty(props, PropertyLabels.SIMULTANEOUSPROCESSES)) {
			nprocesses=PropertiesUtilities.getIntegerPropertyValue(props, PropertyLabels.SIMULTANEOUSPROCESSES, 1, getClass());
		}
	}
	
	/**
	 * Gets the number concurrent processes.
	 *
	 * @return the number concurrent processes
	 */
	public int getNumberConcurrentProcesses(){
		return nprocesses;
	}

	
	/**
	 * Gets the number concurrent processes from loader.
	 *
	 * @param props the props
	 * @return the number concurrent processes from loader
	 */
	public static Integer getNumberConcurrentProcessesFromLoader(Properties props){
		ConcurrentProcessesModuleloader reader = null;
		try {
			reader = new ConcurrentProcessesModuleloader(props);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reader.getNumberConcurrentProcesses();
	}
	
	/**
	 * Load.
	 *
	 * @param props the props
	 * @return the concurrent processes moduleloader
	 * @throws Exception the exception
	 */
	public static ConcurrentProcessesModuleloader load(Properties props) throws Exception{
		return new ConcurrentProcessesModuleloader(props);
	}

	@Override
	public HashMap<String, Object> getMapOfProperties() {
		HashMap<String, Object> param=new HashMap<>();
		param.put(PropertyLabels.SIMULTANEOUSPROCESSES, nprocesses);
		return param;
	}
}
