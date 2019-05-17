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
import java.util.Properties;

import jbiclustge.enrichmentanalysistools.clusterprofile.ClusterProfilerGOEnrichmentAnalyser;
import jbiclustge.enrichmentanalysistools.clusterprofile.ClusterProfilerKeggModuleEnrichmentAnalyser;
import jbiclustge.enrichmentanalysistools.clusterprofile.ClusterProfilerKeggPathwayEnrichmentAnalyser;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.ontologizer.OntologizerEnrichmentAnalyser;
import jbiclustge.enrichmentanalysistools.topgo.TopGOEnrichmentAnalyser;
import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import jbiclustge.propertiesmodules.components.GSEAInfoContainer;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;

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
		
		if(PropertiesUtilities.isValidProperty(props, PropertyLabels.GSEAPROCESSOR)){
			
			String processor=props.getProperty(PropertyLabels.GSEAPROCESSOR);
			
			if(processor.toLowerCase().equals(PropertyLabels.ONTOLOGIZER) || 
					processor.toLowerCase().equals(PropertyLabels.TOPGO)  ||
					processor.toLowerCase().equals(PropertyLabels.CLUSTERPROFILERKEGG) ||
					processor.toLowerCase().equals(PropertyLabels.CLUSTERPROFILERGO) ||
					processor.toLowerCase().equals(PropertyLabels.CLUSTERPROFILERKEGGMODULE)){
				
				Properties processorprops=null;
				
				/*if(props.containsKey(PropertyLabels.GSEACONFIGURATIONFILE) && !props.getProperty(PropertyLabels.GSEACONFIGURATIONFILE).isEmpty()) {*/
				if(PropertiesUtilities.isValidProperty(props, PropertyLabels.GSEACONFIGURATIONFILE)) {
					  //System.out.println(props.getProperty(PropertyLabels.GSEACONFIGURATIONFILE));
					processorprops=PropertiesUtilities.loadFileProperties(props.getProperty(PropertyLabels.GSEACONFIGURATIONFILE));
				}
				

				if(processor.toLowerCase().equals(PropertyLabels.ONTOLOGIZER))
					gseaprocessor=new OntologizerEnrichmentAnalyser();
				else if(processor.toLowerCase().equals(PropertyLabels.CLUSTERPROFILERKEGG))
					gseaprocessor=new ClusterProfilerKeggPathwayEnrichmentAnalyser();
				else if(processor.toLowerCase().equals(PropertyLabels.CLUSTERPROFILERKEGGMODULE))
					gseaprocessor= new ClusterProfilerKeggModuleEnrichmentAnalyser();
				else if(processor.toLowerCase().equals(PropertyLabels.CLUSTERPROFILERGO))
					gseaprocessor= new ClusterProfilerGOEnrichmentAnalyser();
				else
					gseaprocessor=new TopGOEnrichmentAnalyser();
				
				if(processorprops!=null)
					gseaprocessor.setProperties(processorprops);
				
				if(PropertiesUtilities.isValidProperty(props, PropertyLabels.GSEAOUTPVALUES)) {

					String pvaluesstr=props.getProperty(PropertyLabels.GSEAOUTPVALUES);
					
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
				
				if(PropertiesUtilities.isValidProperty(props,PropertyLabels.GSEAUSEADJUSTEDPVALUES)) {
					useadjustedpvalues=PropertiesUtilities.getBooleanPropertyValue(props, PropertyLabels.GSEAUSEADJUSTEDPVALUES, false, getClass());
				}
				
			}
			else
				LogMessageCenter.getLogger().addCriticalErrorMessage("Undefined processor for the gene enrichment analysis, please choose one of these:"+PropertyLabels.ONTOLOGIZER+", "+PropertyLabels.TOPGO+", "+PropertyLabels.CLUSTERPROFILERKEGG+" are accepted!!");
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

	@Override
	public HashMap<String, Object> getMapOfProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
