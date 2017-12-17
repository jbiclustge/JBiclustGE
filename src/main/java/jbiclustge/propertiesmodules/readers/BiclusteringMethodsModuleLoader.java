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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;

import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.BiclusteringMethod;
import jbiclustge.propertiesmodules.PropertiesModules;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringMethodsModuleLoader.
 */
public class BiclusteringMethodsModuleLoader extends PropertyModuleLoader{
	
	/** The methodconfigurationsfolder. */
	private String methodconfigurationsfolder;
	
	/** The methodsvsruns. */
	private LinkedHashMap<AbstractBiclusteringAlgorithmCaller, Integer> methodsvsruns;

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

		if(props.containsKey(PropertiesModules.ALGORITHMCONFIGURATIONSFOLDER) && !props.getProperty(PropertiesModules.ALGORITHMCONFIGURATIONSFOLDER).isEmpty()){

			methodconfigurationsfolder=props.getProperty(PropertiesModules.ALGORITHMCONFIGURATIONSFOLDER);
			if(OSystemUtils.isWindows())
				methodconfigurationsfolder=OSystemUtils.validatePath(methodconfigurationsfolder);
			//methodconfigurationsfolder.replace("\\", "/");


			String typemethodsconfig=props.getProperty(PropertiesModules.ALGORITHMSCONFTYPE);

			HashMap<String, Integer> mapmethodnumberruns=new HashMap<>();

			if(typemethodsconfig.equals(PropertiesModules.ALGORITHMSCONFTYPEMULTI)) {


				ArrayList<String> methodsconfigpaths=MTUDirUtils.getFilePathsInsideDirectory(methodconfigurationsfolder);

				for (String path : methodsconfigpaths) {
					String configname=FilenameUtils.getBaseName(path);
					if(props.containsKey(configname))
						mapmethodnumberruns.put(configname,Integer.parseInt(props.getProperty(configname)));
				}

				LinkedHashMap<String,AbstractBiclusteringAlgorithmCaller> methods=AlgorithmProperties.loadBiclusterMethodsAssociatedToConfigurationsInDirectory(methodconfigurationsfolder);

				if(methods.size()>0){
					methodsvsruns=new LinkedHashMap<>();

					for (String id : methods.keySet()) {

						if(mapmethodnumberruns.containsKey(id))
							methodsvsruns.put(methods.get(id), mapmethodnumberruns.get(id));
						else
							methodsvsruns.put(methods.get(id), 1);

					}

				}
			}
			else {
				for (BiclusteringMethod method : BiclusteringMethod.values()) {
					if(props.containsKey(PropertiesModules.NUMBERRUNSPREFIX+method.getName()))
						mapmethodnumberruns.put(method.getNameInMethodClass(), Integer.parseInt(props.getProperty(PropertiesModules.NUMBERRUNSPREFIX+method.getName())));
				}


				ArrayList<AbstractBiclusteringAlgorithmCaller> methods=AlgorithmProperties.loadBiclusterMethodsFromConfigurationsInDirectory(methodconfigurationsfolder);

				if(methods.size()>0){
					methodsvsruns=new LinkedHashMap<>();

					for (int i = 0; i < methods.size(); i++) {
						String namemethod=methods.get(i).getAlgorithmName();
						if(mapmethodnumberruns.containsKey(namemethod))
							methodsvsruns.put(methods.get(i), mapmethodnumberruns.get(namemethod));
						else
							methodsvsruns.put(methods.get(i), 1);
					}
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



	/**
	 * Gets the methods to run.
	 *
	 * @return the methods to run
	 */
	public LinkedHashMap<AbstractBiclusteringAlgorithmCaller, Integer> getMethodsToRun() {
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










	

}
