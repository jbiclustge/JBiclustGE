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
package jbiclustge.utils.props;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.io.FilenameUtils;

import jbiclustge.utils.osystem.SystemFolderTools;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class JBiGePropertiesManager.
 */
public class JBiGePropertiesManager {
	
	/** The propsfile. */
	public static String propsfile=FilenameUtils.concat(SystemFolderTools.getCurrentDir(), "properties"+File.separator+"jbiclustge.properties");
	
	public static String jbiclustgestatusfile=FilenameUtils.concat(SystemFolderTools.getCurrentDir(), ".jbiclustgestatus");
	
	/** The config. */
	private PropertiesConfiguration config;
	
	/** The mapproperties. */
	private HashMap<String, Object> mapproperties;
	
	private Properties mapinstalledstate;
	
	/** The propmanager. */
	static JBiGePropertiesManager propmanager=null;
	
	
	/**
	 * Gets the manager.
	 *
	 * @return the manager
	 */
	synchronized public static JBiGePropertiesManager getManager(){
		if(propmanager==null)
			propmanager=new JBiGePropertiesManager();
		
		return propmanager;
	}
	
	
	/**
	 * Instantiates a new j bi ge properties manager.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private JBiGePropertiesManager(){
		Configurations configs = new Configurations();
		try {
			if(new File(propsfile).exists()) {
				this.config=configs.properties(new File(propsfile));
				readProperties();
				
				if(new File(jbiclustgestatusfile).exists()) {
					mapinstalledstate=PropertiesUtilities.loadFileProperties(jbiclustgestatusfile);
			    }
			}
			else {
				mapproperties=new HashMap<>();
				LogMessageCenter.getLogger().addCriticalErrorMessage("The file containing the properties of JBiclustGE was not found in the system \"jbiclustge.properties\". Starting an empty properties  \"JBiGePropertiesManager\"");
				/*try {
					throw new IOException();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
	/**
	 * Read properties.
	 */
	private void readProperties(){
		mapproperties=new HashMap<>();
		Iterator<String> key=config.getKeys();
		while (key.hasNext()) {
			String keyname = (String) key.next();
			Object value=config.getProperty(keyname);
			if(value!=null){
				mapproperties.put(keyname, value);
			}
		}
	}
	
	
	public void reload() {
		try {

			if(new File(propsfile).exists()) {
				this.config=new Configurations().properties(new File(propsfile));
				readProperties();
				
				if(new File(jbiclustgestatusfile).exists()) {
					mapinstalledstate=PropertiesUtilities.loadFileProperties(jbiclustgestatusfile);
			    }
			}
			else {
				mapproperties=new HashMap<>();
				LogMessageCenter.getLogger().addCriticalErrorMessage("The file containing the properties of JBiclustGE was not found in the system \"jbiclustge.properties\". Starting an empty properties  \"JBiGePropertiesManager\"");
			}
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		}
	}
	
	/**
	 * Gets the keyset.
	 *
	 * @return the keyset
	 */
	public HashSet<String> getKeyset(){
		return new HashSet<>(mapproperties.keySet());
	}
	
	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @return the key value
	 */
	public Object getKeyValue(String key){
		if(mapproperties!=null && mapproperties.containsKey(key)){
			Object value=mapproperties.get(key);
			if((value instanceof String) && ((String)value).isEmpty())
				return null;
			else
				return mapproperties.get(key);
		}
			
		return null;
	}
	
	public void addProperty(String key, Object property) {
		if(property!=null){
			if(mapproperties==null)
				mapproperties=new HashMap<>();
			mapproperties.put(key, property);
		}
	}
	
	/**
	 * Checks for key.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean hasKey(String key){
		if(mapproperties!=null)
			return mapproperties.containsKey(key);
		return false;
	}
	
	public boolean isFeatureInstalled(String featurename) {
		if(mapinstalledstate!=null) {
			if(mapinstalledstate.containsKey(featurename) && 
					mapinstalledstate.getProperty(featurename).trim().equals("installed"))
				return true;
				
		}
		return false;
	}
	
	public boolean areFeaturesInstalled(String...featurenames) {
		
		for (int i = 0; i < featurenames.length; i++) {
			if(!isFeatureInstalled(featurenames[i]))
				return false;
		}
		return true;
	}
}
