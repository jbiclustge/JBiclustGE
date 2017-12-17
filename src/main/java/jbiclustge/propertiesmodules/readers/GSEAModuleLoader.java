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
import java.util.Properties;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.ontologizer.OntologizerEnrichmentAnalyser;
import jbiclustge.enrichmentanalysistools.topgo.TopGOEnrichmentAnalyser;
import jbiclustge.propertiesmodules.PropertiesModules;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import jbiclustge.propertiesmodules.components.GSEAInfoContainer;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class GSEAModuleLoader.
 */
public class GSEAModuleLoader extends PropertyModuleLoader{
	
	/** The gseaprocessor. */
	private EnrichmentAnalyserProcessor gseaprocessor=null;
	
	/** The pvalues. */
	private ArrayList<Double> pvalues=null;
	
	/** The useadjustedpvalues. */
	private boolean useadjustedpvalues=false;

	/**
	 * Instantiates a new GSEA module loader.
	 *
	 * @param props the props
	 * @throws Exception the exception
	 */
	public GSEAModuleLoader(Properties props) throws Exception {
		super(props);
	}

	/* (non-Javadoc)
	 * @see propertiesmodules.PropertyModuleLoader#loadProperties()
	 */
	@Override
	public void loadProperties() throws Exception {
		
		if(props.containsKey(PropertiesModules.GSEAPROCESSOR)){
			String processor=props.getProperty(PropertiesModules.GSEAPROCESSOR);
			
			if(processor!=null && !processor.isEmpty() && (processor.toLowerCase().equals("ontologizer") || processor.toLowerCase().equals("topgo"))){
				
				Properties processorprops=null;
				
				if(props.containsKey(PropertiesModules.GSEACONFIGURATIONFILE) && !props.getProperty(PropertiesModules.GSEACONFIGURATIONFILE).isEmpty())
				      processorprops=AlgorithmProperties.loadProperties(props.getProperty(PropertiesModules.GSEACONFIGURATIONFILE));
				
				
				if(processor.toLowerCase().equals("ontologizer"))
					gseaprocessor=new OntologizerEnrichmentAnalyser();
				else
					gseaprocessor=new TopGOEnrichmentAnalyser();
				
				if(processorprops!=null)
					gseaprocessor.setProperties(processorprops);
				
				
				if(props.containsKey(PropertiesModules.GSEAOUTPVALUES)&& !props.getProperty(PropertiesModules.GSEAOUTPVALUES).isEmpty()){
					
					String pvaluesstr=props.getProperty(PropertiesModules.GSEAOUTPVALUES);
					
					String[] tmpval=pvaluesstr.split(";");
					
					pvalues=new ArrayList<>();
					for (int i = 0; i < tmpval.length; i++) {
						
						try {
							Double v=Double.parseDouble(tmpval[i].trim());
							if(v!=null)
								pvalues.add(v);
                          
						} catch (Exception e) {
							LogMessageCenter.getLogger().addCriticalErrorMessage("Error in parsing value ["+tmpval[i]+"] ", e);
						}
					}
					
					if(pvalues.size()==0)
						pvalues.add(0.05);
					
					
				}
				else{
					pvalues=new ArrayList<>();
					pvalues.add(0.05);
				}
				
				if(props.containsKey(PropertiesModules.GSEAUSEADJUSTEDPVALUES)&& !props.getProperty(PropertiesModules.GSEAUSEADJUSTEDPVALUES).isEmpty()){
					if(props.getProperty(PropertiesModules.GSEAUSEADJUSTEDPVALUES).toLowerCase().equals("true"))
						useadjustedpvalues=true;
				}
				
			}
			else
				LogMessageCenter.getLogger().addCriticalErrorMessage("Undefined processor for the gene enrichment analysis, only ontologizer or topgo are accepted!!");
		}
		
	}

	/**
	 * Gets the gseaprocessor.
	 *
	 * @return the gseaprocessor
	 */
	public EnrichmentAnalyserProcessor getGseaprocessor() {
		return gseaprocessor;
	}

	/**
	 * Gets the pvalues.
	 *
	 * @return the pvalues
	 */
	public ArrayList<Double> getPvalues() {
		return pvalues;
	}

	/**
	 * Checks if is useadjustedpvalues.
	 *
	 * @return true, if is useadjustedpvalues
	 */
	public boolean isUseadjustedpvalues() {
		return useadjustedpvalues;
	}
	
	/**
	 * Gets the info container.
	 *
	 * @return the info container
	 */
	public GSEAInfoContainer getInfoContainer(){
		return new GSEAInfoContainer(gseaprocessor, pvalues, useadjustedpvalues);
	}
	
	
	/**
	 * Load.
	 *
	 * @param props the props
	 * @return the GSEA module loader
	 * @throws Exception the exception
	 */
	public static GSEAModuleLoader load(Properties props) throws Exception{
		GSEAModuleLoader loader=new GSEAModuleLoader(props);
		loader.loadProperties();
		return loader;
	}
	

}
