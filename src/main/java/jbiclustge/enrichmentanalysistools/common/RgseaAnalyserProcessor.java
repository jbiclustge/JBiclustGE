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
package jbiclustge.enrichmentanalysistools.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.javatuples.Pair;
import org.math.R.Rsession;

import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.rtools.JavaToRUtils;
import jbiclustge.utils.properties.JBiGePropertiesManager;
import jbiclustge.utils.properties.JBiclustGEPropertiesInitializer;
import jrplot.rbinders.functioncallers.AbstractRFunctionCallerSingleDataset;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.rtools.connectors.RConnector;
import pt.ornrocha.rtools.installutils.RInstallTools;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class RgseaAnalyserProcessor.
 */
public abstract class RgseaAnalyserProcessor extends EnrichmentAnalyserProcessor{
	
	
	
	/** The rsession. */
	protected Rsession rsession;
	//protected boolean stop=false;
	protected Thread stop;
	/**
	 * Instantiates a new rgsea analyser processor.
	 */
	public RgseaAnalyserProcessor(){}
	
	/**
	 * Instantiates a new rgsea analyser processor.
	 *
	 * @param annotationfilepath the annotationfilepath
	 */
	public RgseaAnalyserProcessor(String annotationfilepath){
		super(annotationfilepath);
	}
	
	/**
	 * Instantiates a new rgsea analyser processor.
	 *
	 * @param ontologyfilepath the ontologyfilepath
	 * @param annotationfilepath the annotationfilepath
	 */
	public RgseaAnalyserProcessor(String ontologyfilepath, String annotationfilepath){
		super(ontologyfilepath,annotationfilepath);
	}
	
	/**
	 * Instantiates a new rgsea analyser processor.
	 *
	 * @param props the props
	 */
	public RgseaAnalyserProcessor(Properties props){
		super(props);
	}
	
	
	public RgseaAnalyserProcessor(BiclusterList listbiclusters, Properties props){
		super(listbiclusters,props);
	}

	
	/**
	 * Gets the main R libraries.
	 *
	 * @return the main R libraries
	 */
	protected abstract ArrayList<RPackageInfo> getMainRLibraries();
	
	/**
	 * Valid to load extra packages.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean validToLoadExtraPackages();
	
	/**
	 * Required extra libraries.
	 *
	 * @return the linked hash map
	 */
	protected abstract ArrayList<RPackageInfo> requiredExtraLibraries();
	
	/**
	 * Execute rgsea process.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean executeRgseaProcess() throws Exception;
	
	/**
	 * Gets the r objects to remove.
	 *
	 * @return the r objects to remove
	 */
	protected abstract ArrayList<String> getRObjectsToRemove();
	
	
	protected abstract void stopSubProcesses();
	
	@Override
	public void stopProcess() {
		stopSubProcesses();
		rsession.end();
	}
	
	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#validForRunning()
	 */
	@Override
	protected boolean validForRunning() throws Exception {
		
		boolean valid=false;
		changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGETASKSTATUS, null, "Validating if "+getTypeAnalyserProcessor().toString()+" can execute");
		String Rpath=RInstallTools.getSystemR_HOME();
		if(Rpath!=null && !Rpath.isEmpty()){
			
			Pair<String, String> rserveparam=JavaToRUtils.getRServeParameters();
			
			String Ruserlib=(String) JBiGePropertiesManager.getManager().getKeyValue(JBiclustGEPropertiesInitializer.RUSERLIBPATH);
			
			int port=0;
			try {
				if(rserveparam.getValue1()!=null)
					port=Integer.parseInt(rserveparam.getValue1());
			} catch (Exception e) {
				// TODO: handle exception
			}
			rsession =RConnector.getExclusiveRsession(Ruserlib,rserveparam.getValue0(), port);
			
			if(rsession!=null){
				changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Loading/installing mandatory packages: "+RConnector.getRequiredPackagesToString(getMainRLibraries()));
				valid=loadRequiredLibraries(getMainRLibraries(), Ruserlib);
				
				if(validToLoadExtraPackages() && valid){
					changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Loading/installing required packages: "+RConnector.getRequiredPackagesToString(requiredExtraLibraries()));
					
					valid=loadRequiredLibraries(requiredExtraLibraries(), Ruserlib);
					
				}
				
			}
			
			/*if(rsession!=null){
				
				String userRLibPath=JavaToRUtils.checkUserRLibsPath(rsession);
				if(userRLibPath==null && !OSystemUtils.isWindows())
					userRLibPath=RInstallTools.getUserSystemRLibs();
				
				
				valid=loadRequiredLibraries(getMainRLibraries(), userRLibPath);
				
				if(validToLoadExtraPackages() && valid){
					
					valid=loadRequiredLibraries(requiredExtraLibraries(), userRLibPath);
					
				}

			}*/
			
		}
		return valid;
	}
	
	
/*	private String getRequiredPackagesToString(ArrayList<RPackageInfo> reqpackages) {
		StringBuilder str=new StringBuilder();
		for (int i = 0; i < reqpackages.size(); i++) {
			str.append(reqpackages.get(i).getPackageName()+"\n");
		}
		return str.toString();
	}*/
	
	/* (non-Javadoc)
	 * @see enrichmentanalysistools.common.EnrichmentAnalyserProcessor#runAnalysis()
	 */
	@Override
	protected synchronized void runAnalysis() throws Exception {
		changesupport.firePropertyChange(EnrichmentAnalyserProcessor.FIREPROPERTYGSEACHANGETASKSTATUS, null, "Configuring "+getTypeAnalyserProcessor().toString());
		boolean r=executeRgseaProcess();
		if(getRObjectsToRemove()!=null){
			rsession.unset(getRObjectsToRemove());
			LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Objects were removed from R workspace:\n", getRObjectsToRemove(), "\n");
		}
		rsession.end();
		if(!r) {
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("An error has occured during the Gene enrichment analysis, please try again");
			throw new GSEANullResultsException();
		}
	}
	
	/**
	 * Load required libraries.
	 *
	 * @param libs the libs
	 * @param RLibPath the r lib path
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	protected boolean loadRequiredLibraries(ArrayList<RPackageInfo> libs, String RLibPath) throws Exception{

		if(libs!=null && RLibPath!=null)
			return RConnector.loadRequiredLibraries(rsession, libs, RLibPath);
		else if(libs!=null && RLibPath==null)
			return RConnector.loadRequiredLibraries(rsession,libs);
		else
			return false;
	}
	
	
	
}
