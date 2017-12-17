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
package jbiclustge.propertiesmodules.templates;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import jbiclustge.propertiesmodules.PropertiesModules;
import jbiclustge.results.biclusters.BiclusteringUtils;
import pt.ornrocha.propertyutils.EnhancedPropertiesWithSubGroups;

// TODO: Auto-generated Javadoc
/**
 * The Class RunMethodsTemplates.
 */
public class RunMethodsTemplates {
	
	/** The configfilename. */
	public static String CONFIGFILENAME="Additional_Configuration.conf";
	
	/**
	 * Gets the base template properties.
	 *
	 * @return the base template properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static EnhancedPropertiesWithSubGroups getBaseTemplateProperties() throws IOException{
		EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		props.appendProperties(PropertiesModules.getInputExpressionDatasetFileModuleWithMissingvaluesImputation());
		props.appendProperties(PropertiesModules.getConcorrentProcessesModule());
		return props;
	}

	
	/**
	 * Write configuration without GSEA to folder.
	 *
	 * @param directory the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeConfigurationWithoutGSEAToFolder(String directory) throws IOException{
		
		String datasetconffile=FilenameUtils.concat(directory, CONFIGFILENAME);
		EnhancedPropertiesWithSubGroups props=getBaseTemplateProperties();
		props.store(new FileWriter(datasetconffile), true);
		
		BiclusteringUtils.writeBiclusteringMethodsConfigurationTemplate(directory);
		
		System.out.println("Algorithm configurations were stored in directory: "+directory);
		System.out.println("\n");
		System.out.println("1) Delete the configuration templates that you don't want to execute");
		System.out.println("2) Change the parameters of the algorithm configurations (if necessary)");
		System.out.println("3) Set the filepath of gene expression dataset in "+CONFIGFILENAME+" inside the above directory");
		System.out.println("4) Set the Missing Value Imputation method in this file, if needed.");

	}
	

	/**
	 * Write configuration with GSEA to folder.
	 *
	 * @param directory the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeConfigurationWithGSEAToFolder(String directory) throws IOException{
		
		String datasetconffile=FilenameUtils.concat(directory, CONFIGFILENAME);
		EnhancedPropertiesWithSubGroups props=getBaseTemplateProperties();
		props.store(new FileWriter(datasetconffile), true);
		
		BiclusteringUtils.writeBiclusteringMethodsConfigurationTemplate(directory);
		
		System.out.println("Algorithm configurations were stored in directory: "+directory);
		System.out.println("\n");
		System.out.println("1) Delete the configuration templates that you don't want to execute");
		System.out.println("2) Change the parameters of the algorithm configurations (if necessary)");
		System.out.println("3) Set the filepath of gene expression dataset in "+CONFIGFILENAME+" inside the above directory");
		System.out.println("4) Set the Missing Value Imputation method in this file, if needed.");

	}
	

}
