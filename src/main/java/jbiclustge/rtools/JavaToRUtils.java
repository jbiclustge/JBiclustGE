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
package jbiclustge.rtools;

import org.javatuples.Pair;

import jbiclustge.utils.properties.JBiGePropertiesManager;
import pt.ornrocha.rtools.connectors.RConnector;

// TODO: Auto-generated Javadoc
/**
 * The Class JavaToRUtils.
 */
public class JavaToRUtils {
	
	
	/**
	 * Convert boolean to R.
	 *
	 * @param state the state
	 * @return the string
	 */
	public static String convertBooleanToR(boolean state){
		if(state)
			return "TRUE";
		else
			return "FALSE";
	}
	
	
	/**
	 * Use multiple rsession.
	 *
	 * @return true, if successful
	 */
	public static boolean useMultipleRsession(){
		String sessiontype=(String) JBiGePropertiesManager.getManager().getKeyValue("rserve_type_session");
		if(sessiontype!=null && !sessiontype.isEmpty()){
			if(sessiontype.toLowerCase().equals("multiple"))
				return true;
		}
		return false;
	}
	
	
	/*public static String checkUserRLibsPath(Rsession rsession){
		String RLibPath=(String) JBiGePropertiesManager.getManager().getKeyValue(JBiclustGEPropertiesInitializer.RUSERLIBPATH);

		//if(OSystemUtils.isWindows())
			//RLibPath=OSystemUtils.convertPathToWindows(RLibPath);
		if(RLibPath!=null && !RLibPath.isEmpty()){
			File d=new File(RLibPath);
			if(!d.exists())
				d.mkdirs();
			RConnector.setRLibPath(rsession,RLibPath);	
			return RLibPath;
		}
		else
			return null;
	}*/
	
	/**
	 * Gets the r serve parameters.
	 *
	 * @return the r serve parameters
	 */
	public static Pair<String, String> getRServeParameters(){
		
		String host=(String) JBiGePropertiesManager.getManager().getKeyValue(RConnector.RSERVEHOST);
		String port=(String) JBiGePropertiesManager.getManager().getKeyValue(RConnector.RSERVEPORT);
		if(host!=null && host.isEmpty())
			host=null;
		if(port!=null && port.isEmpty())
		    port=null;
		
		return new Pair<String, String>(host, port);
	}

}
