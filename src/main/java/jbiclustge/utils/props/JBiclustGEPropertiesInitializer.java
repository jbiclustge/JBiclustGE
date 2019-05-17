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
package jbiclustge.utils.props;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.utils.osystem.SystemFolderTools;
import pt.ornrocha.propertyutils.EnhancedPropertiesWithSubGroups;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class JBiclustGEPropertiesInitializer.
 */
public class JBiclustGEPropertiesInitializer {
	
	
     /** The ruserlibpath. */
     //public static String RUSERLIBPATH="rlibs_path";
    
	
	 /**
 	 * Gets the process properties.
 	 *
 	 * @return the process properties
 	 */
 	private static EnhancedPropertiesWithSubGroups getProcessProperties(){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToGroupCategory("Concurrent processes info", "simultaneous.processes", "1", "Number of algorithms running simultaneously");
		 return props;
	 }
	/*
 	private static EnhancedPropertiesWithSubGroups getRServeProperties(String Rlibpath){
 		return getRServeProperties(Rlibpath,null);
 	}*/
	
	 /**
 	 * Gets the r serve properties.
 	 *
 	 * @param Rlibpath the rlibpath
 	 * @return the r serve properties
 	 */
 	private static EnhancedPropertiesWithSubGroups getRServeProperties(String Rlibpath, String Rpath){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToSubGroupCategory("Rserve properties", "Session type", "rserve_type_session", "", "single or multiple (default=single)");
		 props.addPropertyToSubGroupCategory("Rserve properties", "Rserve confs", "rserve_port", "", "port of Rserve");
		 props.addPropertyToSubGroupCategory("Rserve properties", "Rserve confs", "rserve_host", "", "url to Rserve");
		 String libspath="";
		 if(Rlibpath!=null){
			 if(OSystemUtils.isWindows()){
				 libspath=Rlibpath.replace(File.separator, "/");
			 }
			 else
				 libspath=Rlibpath;
		 }
		 
		 String Ruserpath="";
		 if(OSystemUtils.isLinux()) {
			 if(Rpath!=null)
				 Ruserpath=Rpath;
			 props.addPropertyToSubGroupCategory("Rserve properties", "Use custom R installation", PropertyLabels.RUSERPATH, Ruserpath, "Specify the path to your custom R_Home");
		 }
		 
		 props.addPropertyToSubGroupCategory("Rserve properties", "Directory to install R packages", PropertyLabels.RUSERLIBPATH, libspath, "Specify the directory were the R packages will be installed");
		 return props;
	 }
	
	 
	 /**
 	 * Gets the GSEA properties.
 	 *
 	 * @return the GSEA properties
 	 */
 	private static EnhancedPropertiesWithSubGroups getGSEAProperties(){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToGroupCategory("Functional Analysis Properties", "max_p_value", "0.05", "Maximum allowed p-value to use in functional enrichment Analysis");
		 return props;
	 }
	 
	 /**
 	 * Gets the ontologizer properties.
 	 *
 	 * @return the ontologizer properties
 	 */
 	private static EnhancedPropertiesWithSubGroups getOntologizerProperties(){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToGroupCategory("Ontologizer Properties", "ontologizer.ontology.file.url", "http://purl.obolibrary.org/obo/go.obo", "");
		 return props;
	 }
 	
 	
 	private static EnhancedPropertiesWithSubGroups getLogLevelProperties(){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToGroupCategory("JBiclustGE Log Level", "loglevel", "info", "Log level output: off, error, warning, info, debug, trace");
		 return props;
	 }
	 
	 /**
 	 * Gets the biclustering methods properties.
 	 *
 	 * @return the biclustering methods properties
 	 */
 	private static EnhancedPropertiesWithSubGroups getBiclusteringMethodsProperties(){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "bimineplus", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "bicfinder", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "coalesce", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "cpb", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "cpb_init_bicluster", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "qubic", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "unibic", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "debi", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "bbc", "", "");
		 props.addPropertyToGroupCategory("Alternative biclustering methods executable filepath", "jbimax", "", "");
		 return props;
	 }
	 
	 /**
 	 * Gets the TMP folder.
 	 *
 	 * @return the TMP folder
 	 */
 	private static EnhancedPropertiesWithSubGroups getTMPFolder(){
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.addPropertyToGroupCategory("Folder used to store temporary files of running processes", SystemFolderTools.JBICLUSTUSERTEMPFOLDER, "", "This folder is used to cache the temporary files, produced by the biclustering methods");
		 return props;
	 }
	
	
	 /**
 	 * Write initial configuration.
 	 *
 	 * @param filepath the filepath
 	 * @param Rlibspath the rlibspath
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	public static void writeInitialConfiguration(String filepath,String Rlibspath, String RHomepath) throws IOException{
		 EnhancedPropertiesWithSubGroups props=new EnhancedPropertiesWithSubGroups();
		 props.appendProperties(getProcessProperties());
		 
		 //System.out.println("RSERVEPROPS: "+Rlibspath+" RHOME: "+RHomepath);

		 props.appendProperties(getRServeProperties(Rlibspath, RHomepath));
		 props.appendProperties(getGSEAProperties());
		 props.appendProperties(getOntologizerProperties());
		 props.appendProperties(getBiclusteringMethodsProperties());
		 props.appendProperties(getTMPFolder());
		 props.appendProperties(getLogLevelProperties());
		 props.store(new FileWriter(filepath), true);
	 }
	
	
	

	/*public static void main(String[] args) throws IOException {
		JBiclustGEPropertiesInitializer.writeInitialConfiguration();

	}*/

}
