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
package jbiclustge.results.biclusters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;

import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.BiclusteringMethod;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.props.AlgorithmProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringUtils.
 */
public class BiclusteringUtils {
	
	
	/**
	 * Intersection.
	 *
	 * @param bic1 the bic 1
	 * @param bic2 the bic 2
	 * @return the bicluster result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterResult intersection(BiclusterResult bic1, BiclusterResult bic2) throws IOException{
		
		if(bic1.useSameExpressionMatrix(bic2.getExpressionDataset())){
			
			ArrayList<Integer> newrows=(ArrayList<Integer>) CollectionUtils.intersection(bic1.getGeneIndexes(), bic2.getGeneIndexes());
			ArrayList<Integer> newcolumns=(ArrayList<Integer>) CollectionUtils.intersection(bic1.getConditionIndexes(), bic2.getConditionIndexes());
			
			return new BiclusterResult(bic1.getExpressionDataset(), newrows, newcolumns, true);
			
		}
		else
			throw new IOException("Biclusters that are be compared come from different expression sets");
		
	}
	
	
	/**
	 * Union.
	 *
	 * @param bic1 the bic 1
	 * @param bic2 the bic 2
	 * @return the bicluster result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterResult union(BiclusterResult bic1, BiclusterResult bic2) throws IOException{
		
		if(bic1.useSameExpressionMatrix(bic2.getExpressionDataset())){
			
			ArrayList<Integer> newrows=(ArrayList<Integer>) CollectionUtils.union(bic1.getGeneIndexes(), bic2.getGeneIndexes());
			ArrayList<Integer> newcolumns=(ArrayList<Integer>) CollectionUtils.union(bic1.getConditionIndexes(), bic2.getConditionIndexes());
			
			return new BiclusterResult(bic1.getExpressionDataset(), newrows, newcolumns, true);
			
		}
		else
			throw new IOException("Biclusters that are be compared come from different expression sets");
		
	}
	

	/**
	 * Disjunction.
	 *
	 * @param bic1 the bic 1
	 * @param bic2 the bic 2
	 * @return the bicluster result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterResult disjunction(BiclusterResult bic1, BiclusterResult bic2) throws IOException{
		
		if(bic1.useSameExpressionMatrix(bic2.getExpressionDataset())){
			
			ArrayList<Integer> newrows=(ArrayList<Integer>) CollectionUtils.disjunction(bic1.getGeneIndexes(), bic2.getGeneIndexes());
			ArrayList<Integer> newcolumns=(ArrayList<Integer>) CollectionUtils.disjunction(bic1.getConditionIndexes(), bic2.getConditionIndexes());
			
			return new BiclusterResult(bic1.getExpressionDataset(), newrows, newcolumns, true);
			
		}
		else
			throw new IOException("Biclusters that are be compared come from different expression sets");
		
	}
	
	

	/**
	 * Difference.
	 *
	 * @param bic1 the bic 1
	 * @param bic2 the bic 2
	 * @return the bicluster result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BiclusterResult difference(BiclusterResult bic1, BiclusterResult bic2) throws IOException{
		
		if(bic1.useSameExpressionMatrix(bic2.getExpressionDataset())){
			
			ArrayList<Integer> newrows=(ArrayList<Integer>) CollectionUtils.subtract(bic1.getGeneIndexes(), bic2.getGeneIndexes());
			ArrayList<Integer> newcolumns=(ArrayList<Integer>) CollectionUtils.subtract(bic1.getConditionIndexes(), bic2.getConditionIndexes());
			
			return new BiclusterResult(bic1.getExpressionDataset(), newrows, newcolumns, true);
			
		}
		else
			throw new IOException("Biclusters that are be compared come from different expression sets");
		
	}
	
	
	/**
	 * Overlapratio.
	 *
	 * @param mainbic the mainbic
	 * @param compareto the compareto
	 * @return the double
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static double overlapratio(BiclusterResult mainbic, BiclusterResult compareto) throws IOException{
		BiclusterResult intersectionarea=intersection(mainbic, compareto);
		return intersectionarea.area()/mainbic.area();
	}
	
	
	
	/**
	 * Equal bicluster result.
	 *
	 * @param bic1 the bic 1
	 * @param bic2 the bic 2
	 * @return true, if successful
	 */
	public static boolean equalBiclusterResult(BiclusterResult bic1, BiclusterResult bic2){
		
		if(bic1.getNumberGenes()!=bic2.getNumberGenes() || bic1.getNumberConditions()!=bic2.getNumberConditions() || !bic1.useSameExpressionMatrix(bic2.getExpressionDataset()))
			return false;
		
		for (int i = 0; i < bic1.getNumberGenes(); i++) {
			int index=bic1.getGeneIndexAtVectorPosition(i);
			if(!bic2.getGeneIndexes().contains(index))
				return false;
		}
		
		for (int i = 0; i < bic1.getNumberConditions(); i++) {
			int index=bic1.getConditionIndexAtVectorPosition(i);
			if(!bic2.getConditionIndexes().contains(index))
				return false;
		}
		
		return true;
		
	}
	
	
	/**
	 * Checks if is subset of bicluster result.
	 *
	 * @param largerset the largerset
	 * @param smallerset the smallerset
	 * @return true, if is subset of bicluster result
	 */
	public static boolean isSubsetOfBiclusterResult(BiclusterResult largerset, BiclusterResult smallerset){
		
		if(!CollectionUtils.containsAll(largerset.getGeneIndexes(), smallerset.getGeneIndexes()))
		   return false;
		   
		if(!CollectionUtils.containsAll(largerset.getConditionIndexes(), smallerset.getConditionIndexes()))
			return false;
		
		return true;
		
	}
	
	/**
	 * Gets the method from name.
	 *
	 * @param name the name
	 * @return the method from name
	 */
	public static AbstractBiclusteringAlgorithmCaller getMethodFromAlgorithmID(String name){
		if(name!=null)
			for (BiclusteringMethod method : BiclusteringMethod.values()) {
			   if(name.toLowerCase().equals(method.getAlgorithmID().toLowerCase()))
				   return method.getInstance();
			}
		return null;
	}
	
	
	/**
	 * Write biclustering methods configuration template.
	 *
	 * @param outputdir the outputdir
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeBiclusteringMethodsConfigurationTemplate(String outputdir) throws IOException{
		if(outputdir!=null){
			File d=new File(outputdir);
			if(!d.exists())
				d.mkdirs();
			
			for (BiclusteringMethod method : BiclusteringMethod.values()) {
				if(method.isSupported())
					AlgorithmProperties.writeDefaultAlgorithmPropertiesToFile(outputdir, method.getInstance(), true);
			}
		}
	}
	
	/**
	 * Write biclustering methods configuration template for CMD.
	 *
	 * @param outputdir the outputdir
	 * @return the linked hash map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static LinkedHashMap<String, String> writeBiclusteringMethodsConfigurationTemplateForCMD(String outputdir) throws IOException{
		LinkedHashMap<String, String> algpaths=new LinkedHashMap<>();
		if(outputdir!=null){
			
			String algmainpath=FilenameUtils.concat(outputdir, "biclustering_methods_configurations");
			
			File d=new File(algmainpath);
			if(!d.exists())
				d.mkdirs();
			
			for (BiclusteringMethod method : BiclusteringMethod.values()) {
				//
				AbstractBiclusteringAlgorithmCaller methodinstance=method.getInstance();
				String filepath=FilenameUtils.concat(algmainpath, methodinstance.getAlgorithmName().toLowerCase()+"_configuration.conf");
				if(method.getAlgorithmID().toLowerCase()!=methodinstance.getAlgorithmName().toLowerCase())
					algpaths.put(method.getAlgorithmID().toLowerCase()+"_configuration_file", filepath);
				else
					algpaths.put(methodinstance.getAlgorithmName().toLowerCase()+"_configuration_file", filepath);
				AlgorithmProperties.writeDefaultAlgorithmPropertiesToFile(algmainpath, methodinstance, true);
			}
		}
		return algpaths;
	}
	
/*	public static AbstractBiclusteringAlgorithmCaller getMethodFromName(String name){
		switch (name.toLowerCase()) {
		case "bimaxclib":
			return new BimaxMethodCLib();
		case "bimax":
			return new BimaxMethod();
		case "opsm":
			return new OPSMMethod();
		case "cc":
			return new RBCCCMethod();
		case "plaid":
			return new RPlaidMethod();
		case "quest":
			return new RQuestMethod();
		case "questord":
			return new RQuestordMethod();
		case "questmet":
			return new RQuestmetMethod();
		case "spectral":
			return new RSpectralMethod();
		case "xmotifs":
			return new RXMOTIFSMethod();
		case "fabia":
			return new RFabiaMethod();
		case "fabiap":
			return new RFabiaPMethod();
		case "fabias":
			return new RFabiaSMethod();
		case "isa":
			return new RIsaMethod();
		case "bicfinder":
			return new BicFinderMethod();
		case "bimineplus":
			return new BiMinePlusMethod();
		case "coalesce":
			return new COALESCEMethod();
		case "cpb":
			return new CPBMethod();
		case "qubic":
			return new QuBicMethod();
		case "unibic":
			return new UnibicMethod();
		case "ubclust":
			return new UBClustMethod();
		case "penalizedplaid":	
		    return new PenalizedPlaidMethod();
		
		default:
			return null;
		}
	}*/
	

}
