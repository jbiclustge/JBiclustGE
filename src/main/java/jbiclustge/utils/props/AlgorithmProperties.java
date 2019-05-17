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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import jbiclustge.methods.IBiclusterAlgorithm;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.BiclusteringMethod;
import pt.ornrocha.collections.MTUMapUtils;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.propertyutils.EnhancedProperties;
import pt.ornrocha.propertyutils.PropertiesUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class AlgorithmProperties.
 */
public class AlgorithmProperties extends EnhancedProperties{
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Select property key.
	 *
	 * @param key the key
	 * @return the algorithm properties
	 */
	public synchronized AlgorithmProperties selectPropertyKey(String key){
		this.currentselectedkey=key;
		return this;
	}
	

	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties setupProperties(String[] keys, String[] defaultvalues) throws IOException{
		AlgorithmProperties props=new AlgorithmProperties();
		props.addProperties(keys, defaultvalues);
		return props;
	}
	
	
	/**
	 * Append properties.
	 *
	 * @param props the props
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties appendProperties(AlgorithmProperties props, String[] keys, String[] defaultvalues) throws IOException{
		if(props==null)
			props=new AlgorithmProperties();
		
		props.addProperties(keys, defaultvalues);
			
		return props;
	}
	
	
	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties setupProperties(String[] keys,String[] defaultvalues, String[] comments) throws IOException{
		AlgorithmProperties props=new AlgorithmProperties();
		props.addProperties(keys, defaultvalues, comments);
		return props;
	}
	
	
	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @param commentssource the commentssource
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties setupProperties(String[] keys,String[] defaultvalues, String[] comments, String commentssource) throws IOException{
		AlgorithmProperties props=new AlgorithmProperties();
		props.addProperties(keys, defaultvalues, comments, commentssource);
		return props;
	}
	
	/**
	 * Append properties.
	 *
	 * @param props the props
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties appendProperties(AlgorithmProperties props, String[] keys,String[] defaultvalues, String[] comments) throws IOException{
		if(props==null)
			props=new AlgorithmProperties();
		
		props.addProperties(keys, defaultvalues, comments);
		return props;
	}
	
	
	/**
	 * Setup properties.
	 *
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @param subkeys the subkeys
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties setupProperties(String[] keys,String[] defaultvalues,  String[] comments, String[] subkeys) throws IOException{
		AlgorithmProperties props=new AlgorithmProperties();
		props.addProperties(keys, defaultvalues, comments, subkeys);
		return props;
	}
	
	/**
	 * Append properties.
	 *
	 * @param props the props
	 * @param keys the keys
	 * @param defaultvalues the defaultvalues
	 * @param comments the comments
	 * @param subkeys the subkeys
	 * @return the algorithm properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AlgorithmProperties appendProperties(AlgorithmProperties props, String[] keys,String[] defaultvalues,  String[] comments, String[] subkeys) throws IOException{
		if(props==null)
			props=new AlgorithmProperties();
		
		props.addProperties(keys, defaultvalues, comments, subkeys);
		return props;
	}
	
	
	
	public static void writeDefaultAlgorithmPropertiesToFile(String configurationname, String dirpath,BiclusteringMethod method,AlgorithmProperties props, boolean writecomments) throws IOException{
		if(props!=null){
			String filepath=null;
			
			File dir=new File(dirpath);
			if(!dir.exists())
				dir.mkdirs();
			
			if(configurationname!=null)
				filepath=FilenameUtils.concat(dirpath, method.getAlgorithmID().toLowerCase()+"_"+configurationname+".conf");
			else
				filepath=FilenameUtils.concat(dirpath, method.getAlgorithmID().toLowerCase()+"_configuration.conf");
			
			FileWriter writer =new FileWriter(filepath);
			props.store(writer, writecomments);
		}
	}

	/**
	 * Write default algorithm properties to file.
	 *
	 * @param configurationname the configurationname
	 * @param dirpath the dirpath
	 * @param method the method
	 * @param writecomments the writecomments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeDefaultAlgorithmPropertiesToFile(String configurationname, String dirpath, IBiclusterAlgorithm method, boolean writecomments) throws IOException{
		AlgorithmProperties props=method.getAlgorithmAllowedProperties();
		if(props!=null){
			String filepath=null;
			
			File dir=new File(dirpath);
			if(!dir.exists())
				dir.mkdirs();
			
			if(configurationname!=null)
				filepath=FilenameUtils.concat(dirpath, configurationname+".conf");
			else
				filepath=FilenameUtils.concat(dirpath, BiclusteringMethod.getAlgorithmIDFromMethodInstance(method)+"_config_1.conf");
			
			FileWriter writer =new FileWriter(filepath);
			props.store(writer, writecomments);
		}
	}
	
	/**
	 * Write default algorithm properties to file.
	 *
	 * @param dirpath the dirpath
	 * @param method the method
	 * @param writecomments the writecomments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeDefaultAlgorithmPropertiesToFile(String dirpath, IBiclusterAlgorithm method, boolean writecomments) throws IOException{
		writeDefaultAlgorithmPropertiesToFile(null, dirpath, method, writecomments);
	}
	
	/**
	 * Write default algorithm properties with comments to file.
	 *
	 * @param configurationname the configurationname
	 * @param dirpath the dirpath
	 * @param method the method
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeDefaultAlgorithmPropertiesWithCommentsToFile(String configurationname, String dirpath, IBiclusterAlgorithm method) throws IOException{
		writeDefaultAlgorithmPropertiesToFile(configurationname, dirpath, method, true);
	}
	
	/**
	 * Write default algorithm properties with comments to file.
	 *
	 * @param dirpath the dirpath
	 * @param method the method
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeDefaultAlgorithmPropertiesWithCommentsToFile(String dirpath, IBiclusterAlgorithm method) throws IOException{
		writeDefaultAlgorithmPropertiesWithCommentsToFile(null,dirpath,method);
	}
	
	/**
	 * Load properties.
	 *
	 * @param filepath the filepath
	 * @return the properties
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Properties loadProperties(String filepath) throws FileNotFoundException, IOException{
		return PropertiesUtilities.loadFileProperties(filepath);
	}
	
	
	/**
	 * Load bicluster method with properties.
	 *
	 * @param methodname the methodname
	 * @param configurationfile the configurationfile
	 * @return the abstract biclustering algorithm caller
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AbstractBiclusteringAlgorithmCaller loadBiclusterMethodWithProperties(String methodname, String configurationfile) throws FileNotFoundException, IOException{
		//AbstractBiclusteringAlgorithmCaller method=null;
		
		AbstractBiclusteringAlgorithmCaller method=null;
		for (BiclusteringMethod methodid : BiclusteringMethod.values()) {
			
			if(methodid.getAlgorithmID().toLowerCase().equals(methodname.toLowerCase())){
				method=methodid.getInstance();
			}
			else if(methodid.getNameInMethodClass().toLowerCase().equals(methodname.toLowerCase())){
				method=methodid.getInstance();	
			}
			 
			
			if(method!=null){
				if(configurationfile!=null){
					method.setAlgorithmProperties(configurationfile);
				}
				return method;
			}
		}
				
		return null;
	}
	
	
	
	
	public static Multimap<String, String> loadPropertiesFromDirectory(String directory){
		ArrayList<String> confs=MTUDirUtils.getFilePathsInsideDirectory(directory);
		return loadAlgorithmPropertiesFromDirectory(confs);
	}
	/**
	 * Load properties from directory.
	 *
	 * @param directory the directory
	 * @return the multimap
	 */
	public static Multimap<String, String> loadAlgorithmPropertiesFromDirectory(ArrayList<String> confs){
		
		 Multimap<String,String> res=ArrayListMultimap.create();
		//LinkedHashMap<String, String> res=new LinkedHashMap<>();
		
		//Pattern pat=Pattern.compile("(\\w+)_configuration");
		 Pattern pat=Pattern.compile("(\\w+)_config(.*)");
		
		for (int i = 0; i < confs.size(); i++) {
			String confpath=confs.get(i);
			String filename=FilenameUtils.getBaseName(confpath);
			//System.out.println(filename);
			Matcher m=pat.matcher(filename);
			if(m.find()){
				String algname=m.group(1);
				res.put(algname, confpath);
			}
			
		}
		return res;
	}
	
	
	
	
	
	
	
	
	public static LinkedHashMap<String,AbstractBiclusteringAlgorithmCaller> loadBiclusterMethodsAssociatedToConfigurationsInDirectory(String directory) throws FileNotFoundException, IOException{
		Multimap<String, String> confs=loadPropertiesFromDirectory(directory);

		LinkedHashMap<String,AbstractBiclusteringAlgorithmCaller> res=new LinkedHashMap<>();

		for (String method : confs.keySet()) {
			AbstractBiclusteringAlgorithmCaller alg=null;
			
			Collection<String> algconfs=confs.get(method);
			if(algconfs.size()>0){
				for (String confpath : algconfs) {
					alg=loadBiclusterMethodWithProperties(method, confpath);
					if(alg!=null)
						res.put(FilenameUtils.getBaseName(confpath), alg);
				}
			}
		}
		return res;
	}
	
	
	
	
	
	
	
	public static ArrayList<Pair<AbstractBiclusteringAlgorithmCaller, String>> loadBiclusterMethodsAssociatedToConfigurationsInDirectoryByNumberRuns(String directory, HashMap<String, Integer> mapmethodnumberruns) throws FileNotFoundException, IOException{
		Multimap<String, String> confs=loadPropertiesFromDirectory(directory);

		ArrayList<Pair<AbstractBiclusteringAlgorithmCaller, String>> res=new ArrayList<>();

		for (String method : confs.keySet()) {
			
			ArrayList<String> methodconfs=new ArrayList<>(confs.get(method));

			for (int i = 0; i < methodconfs.size(); i++) {
				
				String basename=FilenameUtils.getBaseName(methodconfs.get(i));
				if(mapmethodnumberruns.containsKey(basename)) {
					
					Integer nproc=mapmethodnumberruns.get(basename);
					
					for (int j = 0; j < nproc; j++) {
						AbstractBiclusteringAlgorithmCaller alg=loadBiclusterMethodWithProperties(method, methodconfs.get(i));
						res.add(new Pair<AbstractBiclusteringAlgorithmCaller, String>(alg, basename));
					}
					
			
				}
				else {
					AbstractBiclusteringAlgorithmCaller alg=loadBiclusterMethodWithProperties(method, methodconfs.get(i));
					res.add(new Pair<AbstractBiclusteringAlgorithmCaller, String>(alg, basename));
				}
			}
		}
		return res;
	}
	
	
	public static ArrayList<Pair<AbstractBiclusteringAlgorithmCaller, String>> loadBiclusterMethodsAssociatedToConfigurationsInDirectoryByNumberRunsAndPriority(String directory, HashMap<String, Integer> mapmethodnumberruns, LinkedHashMap<String, Integer> mapconfigvspriority) throws FileNotFoundException, IOException{
		
		Multimap<String, String> confs=loadPropertiesFromDirectory(directory);
		
		
		LinkedHashMap<String, String> mapconfnametoconffilepath=new LinkedHashMap<>();
		LinkedHashMap<String, String> mapconfnametomethodname=new LinkedHashMap<>();
		LinkedHashMap<String, Integer> mapconfnametoprio=new LinkedHashMap<>();
		
		for (String method : confs.keySet()) {
			ArrayList<String> methodconfs=new ArrayList<>(confs.get(method));
			
			for (int i = 0; i < methodconfs.size(); i++) {
				String basename=FilenameUtils.getBaseName(methodconfs.get(i));
				mapconfnametoconffilepath.put(basename, methodconfs.get(i));
				mapconfnametomethodname.put(basename, method);
			}
		}
		
		
		for (String configname : mapconfnametoconffilepath.keySet()) {
			if(mapconfigvspriority.containsKey(configname))
				mapconfnametoprio.put(configname, mapconfigvspriority.get(configname));
			else
				mapconfnametoprio.put(configname, 0);
		}
		
		mapconfnametoprio=(LinkedHashMap<String, Integer>) MTUMapUtils.sortMapByValues(mapconfnametoprio, false);
		
		ArrayList<Pair<AbstractBiclusteringAlgorithmCaller, String>> res=new ArrayList<>();
		
		for (String confname : mapconfnametoprio.keySet()) {
			
			if(mapmethodnumberruns.containsKey(confname)) {
				
				Integer nproc=mapmethodnumberruns.get(confname);
				
				for (int j = 0; j < nproc; j++) {
					AbstractBiclusteringAlgorithmCaller alg=loadBiclusterMethodWithProperties(mapconfnametomethodname.get(confname), mapconfnametoconffilepath.get(confname));
					res.add(new Pair<AbstractBiclusteringAlgorithmCaller, String>(alg, confname));
				}
				
		
			}
			else {
				AbstractBiclusteringAlgorithmCaller alg=loadBiclusterMethodWithProperties(mapconfnametomethodname.get(confname),  mapconfnametoconffilepath.get(confname));
				res.add(new Pair<AbstractBiclusteringAlgorithmCaller, String>(alg, confname));
			}
			
		}

		return res;
	}
	
	
	
	/**
	 * Load bicluster methods from configurations in directory.
	 *
	 * @param directory the directory
	 * @return the array list
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ArrayList<AbstractBiclusteringAlgorithmCaller> loadBiclusterMethodsFromConfigurationsInDirectory(String directory) throws FileNotFoundException, IOException{
		Multimap<String, String> confs=loadPropertiesFromDirectory(directory);
		
		ArrayList<AbstractBiclusteringAlgorithmCaller> res=new ArrayList<>();
		for (String method : confs.keySet()) {
			AbstractBiclusteringAlgorithmCaller alg=null;
			
			Collection<String> algconfs=confs.get(method);
			if(algconfs.size()>0){
				for (String confpath : algconfs) {
					alg=loadBiclusterMethodWithProperties(method, confpath);
					if(alg!=null)
						res.add(alg);
				}
			}
		}
		return res;
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException{
		
		LinkedHashMap<String, Integer> mapconfigvspriority=new LinkedHashMap<>();
		mapconfigvspriority.put("unibic_config_1", 3);
		mapconfigvspriority.put("fabia_config_1", 1);
		
		
		HashMap<String, Integer> mapmethodnumberruns=new HashMap<>();
		/*mapmethodnumberruns.put("unibic_config_1", 2);
		mapmethodnumberruns.put("fabia_config_1", 5);*/
		
		loadBiclusterMethodsAssociatedToConfigurationsInDirectoryByNumberRunsAndPriority("/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/souto/armstrongv1/algorithms", mapmethodnumberruns, mapconfigvspriority);
		//System.out.println(loadPropertiesFromDirectory("/home/orocha/discodados/ApenasTrabalho/TesteCLI2/algorithms"));
		//System.out.println(loadBiclusterMethodsFromConfigurationsInDirectory("/home/orocha/discodados/Biclustering/datasets/teste/constant_up/row500/new_confs"));
	}
}
