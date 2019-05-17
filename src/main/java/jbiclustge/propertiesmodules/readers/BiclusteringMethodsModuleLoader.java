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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;

import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.propertiesmodules.PropertiesModules;
import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.collections.MTUMapUtils;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringMethodsModuleLoader.
 */
public class BiclusteringMethodsModuleLoader extends PropertyModuleLoader{
	
	/** The methodconfigurationsfolder. */
	private String methodconfigurationsfolder;
	
	/** The methodsvsruns. */
	private LinkedHashMap<AbstractBiclusteringAlgorithmCaller, Pair<Integer, String>> methodsvsruns;
	private LinkedHashMap<String, Integer> configpriorities;
	
	private String algorithmsconfigtype=PropertyLabels.ALGORITHMSCONFTYPESINGLE;

	/**
	 * Instantiates a new biclustering methods module loader.
	 *
	 * @param props the props
	 * @throws Exception the exception
	 */
	public BiclusteringMethodsModuleLoader(Properties props) throws Exception {
		super(props);
	}
	
	
	
	/* (non-Javadoc)
	 * @see propertiesmodules.PropertyModuleLoader#loadProperties()
	 */
	@Override
	public void loadProperties() throws Exception {

		//if(props.containsKey(PropertyLabels.ALGORITHMCONFIGURATIONSFOLDER) && !props.getProperty(PropertiesModules.ALGORITHMCONFIGURATIONSFOLDER).isEmpty()){
          if(PropertiesUtilities.isValidProperty(props, PropertyLabels.ALGORITHMCONFIGURATIONSFOLDER)) {
			methodconfigurationsfolder=props.getProperty(PropertyLabels.ALGORITHMCONFIGURATIONSFOLDER);
			if(OSystemUtils.isWindows())
				methodconfigurationsfolder=OSystemUtils.validatePath(methodconfigurationsfolder);

			String typemethodsconfig=props.getProperty(PropertyLabels.ALGORITHMSCONFTYPE);

			HashMap<String, Integer> mapmethodnumberruns=new HashMap<>();
			
			
			if(PropertiesUtilities.isValidProperty(props, PropertyLabels.ALGORITHMCONFIGURATIONSFOLDERBYRUNTIME)) {
				String foldername=props.getProperty(PropertyLabels.ALGORITHMCONFIGURATIONSFOLDERBYRUNTIME);
				
				String tmpfolder=FilenameUtils.concat(methodconfigurationsfolder, foldername);
				if(new File(tmpfolder).exists() && new File(tmpfolder).isDirectory())
					methodconfigurationsfolder=tmpfolder;
			}
			

			ArrayList<String> methodsconfigpaths=MTUDirUtils.getFilePathsInsideDirectory(methodconfigurationsfolder);

			for (String path : methodsconfigpaths) {
				String configname=FilenameUtils.getBaseName(path);
				if(props.containsKey(configname))
					mapmethodnumberruns.put(configname,Integer.parseInt(props.getProperty(configname)));
			}


			LinkedHashMap<String, Integer> mapconfigvspriority=new LinkedHashMap<>();

			int prioconfigs=0;

			for (String path : methodsconfigpaths) {
				String configname=FilenameUtils.getBaseName(path);
				if(props.containsKey(PropertyLabels.PRIORITYTAG+configname)) {
					String prio=props.getProperty(PropertyLabels.PRIORITYTAG+configname);
					if(prio!=null && !prio.isEmpty()) {
						int prionumb=0;
						try {
							prionumb=Integer.parseInt(prio);	
						} catch (Exception e) {
							prionumb=0;
						}
						if(prionumb>0)
							prioconfigs++;
						mapconfigvspriority.put(configname, prionumb);
					}
				}
				else {
					mapconfigvspriority.put(configname, 0);
				}
			}

			/*	LinkedHashMap<String, Integer> orderedmapconfigvspriority=null;

			if(prioconfigs>0)
				orderedmapconfigvspriority=(LinkedHashMap<String, Integer>) MTUMapUtils.sortMapByValues(mapconfigvspriority, false);
			else
				orderedmapconfigvspriority=mapconfigvspriority;*/


			if(typemethodsconfig.equals(PropertyLabels.ALGORITHMSCONFTYPEMULTI)) {

				algorithmsconfigtype=PropertyLabels.ALGORITHMSCONFTYPEMULTI;

				methodsvsruns=new LinkedHashMap<>();

				ArrayList<Pair<AbstractBiclusteringAlgorithmCaller, String>> methods=null;

				if(prioconfigs>0)
					methods=AlgorithmProperties.loadBiclusterMethodsAssociatedToConfigurationsInDirectoryByNumberRunsAndPriority(methodconfigurationsfolder, mapmethodnumberruns, mapconfigvspriority);
				else
					methods=AlgorithmProperties.loadBiclusterMethodsAssociatedToConfigurationsInDirectoryByNumberRuns(methodconfigurationsfolder, mapmethodnumberruns);

				for (int i = 0; i < methods.size(); i++) {
					methodsvsruns.put(methods.get(i).getValue0(), new Pair<Integer, String>(1,methods.get(i).getValue1()));
				}
			}
			else {

				LinkedHashMap<String,AbstractBiclusteringAlgorithmCaller> methods=AlgorithmProperties.loadBiclusterMethodsAssociatedToConfigurationsInDirectory(methodconfigurationsfolder);

				LinkedHashMap<String, AbstractBiclusteringAlgorithmCaller> orderedmethods=null;

				if(prioconfigs>0) {
					orderedmethods=new LinkedHashMap<>();
					LinkedHashMap<String, Integer> orderedmapconfigvspriority=new LinkedHashMap<>();
					//mapconfigvspriority=(LinkedHashMap<String, Integer>) MTUMapUtils.sortMapByValues(mapconfigvspriority, false);
					
					for (String configname : methods.keySet()) {
                        if(mapconfigvspriority.containsKey(configname))
                        	orderedmapconfigvspriority.put(configname, mapconfigvspriority.get(configname));
                        else
                        	orderedmapconfigvspriority.put(configname, 0);
					}
					
					orderedmapconfigvspriority=(LinkedHashMap<String, Integer>) MTUMapUtils.sortMapByValues(orderedmapconfigvspriority, false);
					
					for (String config : orderedmapconfigvspriority.keySet()) {
						orderedmethods.put(config, methods.get(config));
					}
					
				}
				else
					orderedmethods=methods;



				if(orderedmethods.size()>0){
					methodsvsruns=new LinkedHashMap<>();

					for (String id : orderedmethods.keySet()) {
						if(mapmethodnumberruns.containsKey(id))
							methodsvsruns.put(methods.get(id), new Pair<Integer, String>(mapmethodnumberruns.get(id),id));
						else
							methodsvsruns.put(methods.get(id), new Pair<Integer, String>(1,id));

					}
					
					//System.out.println(methodsvsruns);
				}
			}
		}
	}
				
		

	/**
	 * Gets the method configurationsfolder.
	 *
	 * @return the method configurationsfolder
	 */
	public String getMethodConfigurationsfolder() {
		return methodconfigurationsfolder;
	}
	

	public String getAlgorithmsConfigurationType() {
		return algorithmsconfigtype;
	}

	/**
	 * Gets the methods to run.
	 *
	 * @return the methods to run
	 */
	public LinkedHashMap<AbstractBiclusteringAlgorithmCaller, Pair<Integer, String>> getMethodsToRun() {
		return methodsvsruns;
	}

	
	
	/**
	 * Load.
	 *
	 * @param props the props
	 * @return the biclustering methods module loader
	 * @throws Exception the exception
	 */
	public static BiclusteringMethodsModuleLoader load(Properties props) throws Exception{
		BiclusteringMethodsModuleLoader loader=new BiclusteringMethodsModuleLoader(props);
		loader.loadProperties();
		return loader;
	}


	

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	@Override
	public HashMap<String, Object> getMapOfProperties() {
		// TODO Auto-generated method stub
		return null;
	}










	

}
