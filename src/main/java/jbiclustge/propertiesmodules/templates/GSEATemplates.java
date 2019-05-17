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

import jbiclustge.enrichmentanalysistools.ontologizer.components.OntologizerPropertiesContainer;
import jbiclustge.enrichmentanalysistools.topgo.components.TopGoPropertiesContainer;
import jbiclustge.propertiesmodules.PropertiesModules;
import jbiclustge.propertiesmodules.PropertyLabels;
import pt.ornrocha.propertyutils.EnhancedPropertiesWithSubGroups;

// TODO: Auto-generated Javadoc
/**
 * The Class GSEATemplates.
 */
public class GSEATemplates {
	
	
	
	/*public static EnhancedPropertiesWithSubGroups getGSEABaseTemplateProperties() throws IOException{
		EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		props.appendProperties(PropertiesModules.getInputExpressionDatasetFileModule());
		props.appendProperties(PropertiesModules.getEnrichmentAnalysisModule());
		props.appendProperties(PropertiesModules.getResultsReporterModule());
		return props;
	}*/
	
	
	/**
	 * Gets the GSEA base template properties.
	 *
	 * @return the GSEA base template properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EnhancedPropertiesWithSubGroups getGSEABaseTemplateProperties() throws IOException{
		return PropertiesModules.getEnrichmentAnalysisModule();
	}
	
	
	
	/*public static EnhancedPropertiesWithSubGroups getGSEABaseTemplateProperties() throws IOException{
		EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		props.appendProperties(PropertiesModules.getFilesFolderToGSEAAnalysis());
		props.appendProperties(PropertiesModules.getInputExpressionDatasetFileModule());
		props.appendProperties(PropertiesModules.getEnrichmentAnalysisModule());
		props.appendProperties(PropertiesModules.getResultsReporterModule());
		return props;
	}*/
	
	
	
	
	
	
	
	
	/**
	 * Write GSEA with top go template.
	 *
	 * @param dirpath the dirpath
	 * @param useannotationdatabase the useannotationdatabase
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeGSEAWithTopGoTemplate(String dirpath, boolean useannotationdatabase) throws IOException{
		EnhancedPropertiesWithSubGroups props=getGSEABaseTemplateProperties();
		
		props.setProperty(PropertyLabels.GSEAPROCESSOR, "topgo");
		String gseafilepath=FilenameUtils.concat(dirpath, "topGO_configuration.conf");
		
		if(useannotationdatabase)
			TopGoPropertiesContainer.writePropertiesFileToAnnotationDatabase(gseafilepath);
		else
			TopGoPropertiesContainer.writePropertiesFileToAnnotationFile(gseafilepath);
		
		props.setProperty(PropertyLabels.GSEACONFIGURATIONFILE, gseafilepath);
		
		String filename=FilenameUtils.concat(dirpath, "Biclustering_GSEA_topGO_Profile.conf");
		props.store(new FileWriter(filename), true);
	}
	
	/**
	 * Write GSEA with ontologizer template.
	 *
	 * @param dirpath the dirpath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeGSEAWithOntologizerTemplate(String dirpath) throws IOException{
		EnhancedPropertiesWithSubGroups props=getGSEABaseTemplateProperties();
		
		props.setProperty(PropertyLabels.GSEAPROCESSOR, "ontologizer");
		String gseafilepath=FilenameUtils.concat(dirpath, "Ontologizer_configuration.conf");
		OntologizerPropertiesContainer.writeCompletePropertiesFileTemplate(gseafilepath);
		
		props.setProperty(PropertyLabels.GSEACONFIGURATIONFILE, gseafilepath);
		
		String filename=FilenameUtils.concat(dirpath, "Biclustering_GSEA_Ontologizer_Profile.conf");
		props.store(new FileWriter(filename), true);
	}

	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		GSEATemplates.writeGSEAWithTopGoTemplate("/home/orocha/discodados/ApenasTrabalho/testes2", true);
	}

}
