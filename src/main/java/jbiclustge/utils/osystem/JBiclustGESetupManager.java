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
package jbiclustge.utils.osystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.zeroturnaround.zip.ZipUtil;

import jbiclustge.utils.properties.JBiclustGEPropertiesInitializer;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.rtools.installutils.RInstallTools;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.systemutils.OSystemUtils;
import pt.ornrocha.systemutils.OSystemUtils.OS;
import pt.ornrocha.webutils.connectionutils.downloaders.HTTPFileDownloader;

// TODO: Auto-generated Javadoc
/**
 * The Class JBiclustGESetupManager.
 */
public class JBiclustGESetupManager {
	
	
	/** The jbiclustgesetupstatus. */
	public static String JBICLUSTGESETUPSTATUS=".jbiclustgestatus";
	public static String JBICLUSTGECONFIGURED=".configured";
	public static String INSTALLDONE=".installdone";
	
	/** The jbiclustgebasezipname. */
	public static String JBICLUSTGEBASEZIPNAME="biclustering_methods";
	
	/** The jbiclustbasedownloadurl. */
	private static String JBICLUSTBASEDOWNLOADURL="https://jbiclustge.github.io/files/";


	/**
	 * Checks if is jbiclust GE configured.
	 *
	 * @return true, if is jbiclust GE configured
	 */
	public static boolean isJbiclustGEConfigured(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGECONFIGURED);
		if(filestatus.exists())
			return true;
		return false;
	}
	
	
	public static boolean isinstallationdone(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+INSTALLDONE);
		if(filestatus.exists())
			return true;
		return false;
	}
	
	public static void removeInstallationDoneTag(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+INSTALLDONE);
		if(filestatus.exists())
			filestatus.delete();	
	}
	
	public static void addInstallationDoneTag() throws IOException{
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+INSTALLDONE);
		filestatus.createNewFile();
	}
	
	
	public static boolean isConfigured(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGECONFIGURED);
		if(filestatus.exists())
			return true;
		return false;
	}
	
	public static void removeConfiguredTag(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGECONFIGURED);
		if(filestatus.exists())
			filestatus.delete();	
	}
	
	public static void addConfiguredTag() throws IOException{
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGECONFIGURED);
		filestatus.createNewFile();
	}
	
	/**
	 * Removes the J biclust GE config lock.
	 */
	public static void removeJBiclustGEConfigLock(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGESETUPSTATUS);
		if(filestatus.exists())
			filestatus.delete();	
	}
	
	
	/**
	 * Reset previous configuration.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void resetPreviousConfiguration() throws IOException{
		removeJBiclustGEConfigLock();
		removeConfiguredTag();
		File propsdir=new File(FilenameUtils.concat(SystemFolderTools.getCurrentDir(), "properties"));
		if(propsdir.exists()){
			MTUDirUtils.deleteDirectory(propsdir.getAbsolutePath());
		}
	}
	
	
	/**
	 * Setup JbiclustGE methods environment.
	 *
	 * @param ruserlibspath the new up J biclust GE methods environment
	 * @throws Exception the exception
	 */
	public static void setupJBiclustGEMethodsEnvironment(String ruserlibspath) throws Exception{
		
		createJBiclustGEStatus();
		setupExecutableBiclusteringMethods();
		String output=setupREnvBiclusteringMethods(ruserlibspath,false);
		setupJBiclustGEProperties(output);
		
		addConfiguredTag();
		addInstallationDoneTag();
	}
	
	/**
	 * Locksetup biclust setup.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createJBiclustGEStatus() throws IOException{
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGESETUPSTATUS);
		filestatus.createNewFile();
	}
	
	
	

	
	/**
	 * Gets the lock file.
	 *
	 * @return the lock file
	 */
	public static File getStatusFile(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+JBICLUSTGESETUPSTATUS);
		if(filestatus.exists())
			return filestatus;
		return null;
	}
	
	/**
	 * Sets the up J biclust GE properties.
	 *
	 * @param Rlibspath the new up J biclust GE properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void setupJBiclustGEProperties(String Rlibspath) throws IOException{
		
		File propsdir=new File(FilenameUtils.concat(SystemFolderTools.getCurrentDir(), "properties"));
		if(!propsdir.exists())
			propsdir.mkdirs();
		
		String propsfile=FilenameUtils.concat(propsdir.getAbsolutePath(), "jbiclustge.properties");
		if(!new File(propsfile).exists())
			JBiclustGEPropertiesInitializer.writeInitialConfiguration(propsfile,Rlibspath);
		
	}
	
	
	
	public static boolean setupExecutableBiclusteringMethods() throws Exception {
		boolean sucess=false;
		sucess=setupMethodsViaZipFile();
		if(!sucess)
			sucess=setupMethodsViaWeb(true);
		
		return sucess;
		
	}
	
	
	/**
	 * Setup biclustering methods.
	 *
	 * @param Rlibspath the rlibspath
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String setupREnvBiclusteringMethods(String Rlibspath, boolean installplotpackages) throws Exception{
		
		String installinfolder=null;
		
		if(Rlibspath!=null){
			File uf=new File(Rlibspath);
			if(uf.exists() && uf.isDirectory())
				installinfolder=Rlibspath;
			else if(!uf.exists()){
				uf.mkdirs();
				installinfolder=Rlibspath;
			}
		}
		
		if(installinfolder==null)
			installinfolder=configureRLibsInstallPath();
		
		installRequiredRPackages(installinfolder,getBaseRPackages());
		if(installplotpackages)
			installRequiredRPackages(installinfolder,getPlotRPackages());
		
		return installinfolder;
	}
	
	
	/**
	 * Configure R libs install path.
	 *
	 * @return the string
	 * @throws Exception the exception
	 */
	private static String configureRLibsInstallPath() throws Exception{
		String installon=null;
		if(OSystemUtils.isLinux()){
			ArrayList<String> Rlibs=RInstallTools.checkRLibsFoldersInR(null);
			for (String path : Rlibs) {
				if(MTUDirUtils.isFolderWritable(path)){
					installon=path;
					break;
				}
			}
		}
		
		if(installon==null){
			String currentfolder=SystemFolderTools.getCurrentDir();
			String pathrlibs=FilenameUtils.concat(currentfolder, "JBiclustGE_RLibs");
			File f=new File(pathrlibs);
			if(!f.exists())
				f.mkdirs();
			
			installon=pathrlibs;
			
			if(OSystemUtils.isWindows())
				installon=installon.replace(File.separator, "/");
		}
		
		return installon;
	}
	
	
	/**
	 * Setup methods via zip file.
	 *
	 * @return true, if successful
	 */
	private static boolean setupMethodsViaZipFile(){
		
		LogMessageCenter.getLogger().addTaskStatusMessage("Installing Biclustering Methods");
		String currentfolder=SystemFolderTools.getCurrentDir();
		
		ArrayList<String> filesin=MTUDirUtils.getFilePathsInsideDirectory(currentfolder, true, false);
		
		String reqfile=null;
		try {
			reqfile = getRequiredZipFileName();
		} catch (IOException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error: ", e);
			return false;
		}
		String zipfilepath=null;
		
		for (String filepath : filesin) {
			
			String namefile=FilenameUtils.getBaseName(filepath);
			if(namefile.equals(reqfile))
				zipfilepath=filepath;
		}
		
		if(zipfilepath!=null){
			LogMessageCenter.getLogger().addInfoMessage("Installing biclustering methods from zip file: "+zipfilepath);
			LogMessageCenter.getLogger().addSubTaskStatusMessage("Installing biclustering methods from zip file: "+zipfilepath);
			String folderbin=SystemFolderTools.checkAndSetFolderOfBinaries();

			ZipUtil.unpack(new File(zipfilepath), new File(folderbin));
			LogMessageCenter.getLogger().addInfoMessage("Methods were installed successfully in: "+folderbin);
			LogMessageCenter.getLogger().addSubTaskStatusMessage("Methods were installed successfully in: "+folderbin);
			
			
		
			ArrayList<String> installed=getInstalledFiles(RInstallTools.validatePath(folderbin));

			if(OSystemUtils.isLinux())
				setFilePermissions(installed);
		
			ArrayList<String> msg=new ArrayList<>();
			for (int i = 0; i < installed.size(); i++) {
				String namefile=FilenameUtils.getBaseName(installed.get(i));
				msg.add(namefile+" = installed");
				LogMessageCenter.getLogger().addSubTaskStatusMessage(namefile+" = installed");
				LogMessageCenter.getLogger().toClass(JBiclustGESetupManager.class).addInfoMessage(namefile+" = installed");
			}
			
			try {
				MTUFileUtils.SaveorAppendArrayLisToFile(getStatusFile().getAbsolutePath(), msg, true);
			} catch (IOException e) {
                LogMessageCenter.getLogger().toClass(JBiclustGESetupManager.class).addCriticalErrorMessage(e);
			}
			
		
			
			new File(zipfilepath).delete();
			
			return true;
		}
		LogMessageCenter.getLogger().addInfoMessage("The required zip file: "+reqfile+".zip was not found" );
		LogMessageCenter.getLogger().addTaskStatusMessage("The required zip file: "+reqfile+".zip was not found");
		LogMessageCenter.getLogger().addSubTaskStatusMessage("");
		return false;
	}
	
	
	
	/**
	 * Setup methods via web.
	 *
	 * @return true, if successful
	 * @throws Exception 
	 */
	private static boolean setupMethodsViaWeb(boolean throwconnectionexception) throws Exception{

		LogMessageCenter.getLogger().addTaskStatusMessage("Downloading Biclustering Methods");
		String requiredfilename=getRequiredZipFileName();
		if(requiredfilename!=null)
			requiredfilename=requiredfilename+".zip";
		else
			return false;

		String url=JBICLUSTBASEDOWNLOADURL+requiredfilename;
		LogMessageCenter.getLogger().addInfoMessage("Downloading methods from: "+url);
		LogMessageCenter.getLogger().addSubTaskStatusMessage("Downloading methods from: "+url);

		HTTPFileDownloader downloader=new HTTPFileDownloader(url, SystemFolderTools.getCurrentDir());
		downloader.run();


		if(downloader.isSuccessfully()){
			LogMessageCenter.getLogger().addInfoMessage("Methods were downloaded successfully from: "+url);
			LogMessageCenter.getLogger().addSubTaskStatusMessage("Methods were downloaded successfully from: "+url);
			return setupMethodsViaZipFile();
		}
		else {
            if(downloader.connectionException()!=null && throwconnectionexception)
            	throw downloader.connectionException();
			LogMessageCenter.getLogger().addInfoMessage("Unsuccessful download attempt... please check if your internet connection it is working, or do manual installation of the biclustering methods.");
			LogMessageCenter.getLogger().addSubTaskStatusMessage("Unsuccessful download attempt... please check if your internet connection it is working\n or do manual installation of the biclustering methods.");
			return false;
		}
	}
	
	/**
	 * Sets the file permissions.
	 *
	 * @param files the new file permissions
	 */
	private static void setFilePermissions(ArrayList<String> files){
		
		//ArrayList<String> files=getInstalledFiles(folder);
		for (String f : files) {
			File fexe=new File(f);
			fexe.setExecutable(true);
	        LogMessageCenter.getLogger().addInfoMessage("Permission of file "+FilenameUtils.getName(f)+ " was changed to executable");
		}
		
	}
	
	
	/**
	 * Gets the installed files.
	 *
	 * @param folder the folder
	 * @return the installed files
	 */
	private static ArrayList<String> getInstalledFiles(String folder){
		ArrayList<String> files=MTUDirUtils.getFilePathsInsideDirectory(folder, true, false);
		for (String f : files) {
			LogMessageCenter.getLogger().addInfoMessage(FilenameUtils.getName(f)+" was successfully installed.");
			LogMessageCenter.getLogger().addInfoMessage("Path: "+f+"\n");
		}
		return files;
	}
	
	
	/**
	 * Gets the required file name.
	 *
	 * @return the required file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getRequiredZipFileName() throws IOException{
		
		String reqname=JBICLUSTGEBASEZIPNAME;
		
		if(OSystemUtils.isLinux()){
			
			if(OSystemUtils.getOperationSystem().equals(OS.LINUX32))
			   throw new IOException("Unsupported operating system, only supported on 64 bits linux systems");
				reqname=reqname+"_linux";		
		}
		else if(OSystemUtils.isWindows()){
			
			if(OSystemUtils.getOperationSystem().equals(OS.WIN32))
				throw new IOException("Unsupported operating system, only supported on 64 bits windows systems");
			else
				reqname=reqname+"_win";
		}
		
		if(reqname.equals(JBICLUSTGEBASEZIPNAME))
			return null;
		return reqname;
	}
	
	

	private static ArrayList<RPackageInfo> getBaseRPackages(){
		ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		requiredRpackages.add(RPackageInfo.define("Rserve"));
		requiredRpackages.add(RPackageInfo.define("BicARE", true));
		requiredRpackages.add(RPackageInfo.define("matrixStats"));
		requiredRpackages.add(RPackageInfo.define("biclust",RPackageInfo.define("flexclust")));
		requiredRpackages.add(RPackageInfo.define("fabia", true));
		requiredRpackages.add(RPackageInfo.define("eisa", true));
		requiredRpackages.add(RPackageInfo.define("topGO", true));
		//requiredRpackages.add(RPackageInfo.define("gplots"));
		requiredRpackages.add(RPackageInfo.define("ggplot2"));
		return requiredRpackages;
	}
	
	private static ArrayList<RPackageInfo> getPlotRPackages(){
		ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		requiredRpackages.add(RPackageInfo.define("plotly"));
		requiredRpackages.add(RPackageInfo.define("viridis"));
		requiredRpackages.add(RPackageInfo.define("jsonlite"));
		requiredRpackages.add(RPackageInfo.define("RColorBrewer"));
		requiredRpackages.add(RPackageInfo.define("readxl",RPackageInfo.define("cellranger")));
		requiredRpackages.add(RPackageInfo.define("DT"));
		requiredRpackages.add(RPackageInfo.define("xtable"));
		requiredRpackages.add(RPackageInfo.define("htmltools"));
		requiredRpackages.add(RPackageInfo.define("htmlwidgets"));
		requiredRpackages.add(RPackageInfo.define("dplyr"));
		requiredRpackages.add(RPackageInfo.define("shiny"));
		requiredRpackages.add(RPackageInfo.define("heatmaply", RPackageInfo.define("trimcluster")));
		requiredRpackages.add(RPackageInfo.define("shinyHeatmaply"));
		requiredRpackages.add(RPackageInfo.define("parcoords", "timelyportfolio/parcoords"));
		return requiredRpackages;
	}
	
	
	
	/**
	 * Install required R packages.
	 *
	 * @param Rlibspath the rlibspath
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private static boolean installRequiredRPackages(String Rlibspath, ArrayList<RPackageInfo> requiredRpackages) throws Exception{
		
		String Rpath=RInstallTools.getSystemR_HOME();
		
		/*ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		requiredRpackages.add(RPackageInfo.define("Rserve"));
		requiredRpackages.add(RPackageInfo.define("BicARE", true));
		requiredRpackages.add(RPackageInfo.define("matrixStats"));
		requiredRpackages.add(RPackageInfo.define("biclust",RPackageInfo.define("flexclust")));
		requiredRpackages.add(RPackageInfo.define("fabia", true));
		requiredRpackages.add(RPackageInfo.define("eisa", true));
		requiredRpackages.add(RPackageInfo.define("topGO", true));
		//requiredRpackages.add(RPackageInfo.define("gplots"));
		requiredRpackages.add(RPackageInfo.define("ggplot2"));
		//requiredRpackages.add(RPackageInfo.define("GGally"));
		requiredRpackages.add(RPackageInfo.define("plotly"));
		requiredRpackages.add(RPackageInfo.define("viridis"));
		requiredRpackages.add(RPackageInfo.define("jsonlite"));
		requiredRpackages.add(RPackageInfo.define("RColorBrewer"));
		requiredRpackages.add(RPackageInfo.define("readxl",RPackageInfo.define("cellranger")));
		requiredRpackages.add(RPackageInfo.define("DT"));
		requiredRpackages.add(RPackageInfo.define("xtable"));
		requiredRpackages.add(RPackageInfo.define("htmltools"));
		requiredRpackages.add(RPackageInfo.define("htmlwidgets"));
		requiredRpackages.add(RPackageInfo.define("dplyr"));
		requiredRpackages.add(RPackageInfo.define("shiny"));
		requiredRpackages.add(RPackageInfo.define("heatmaply", RPackageInfo.define("trimcluster")));
		requiredRpackages.add(RPackageInfo.define("shinyHeatmaply"));
		requiredRpackages.add(RPackageInfo.define("parcoords", "timelyportfolio/parcoords"));*/
		
		
		if(Rpath!=null && !Rpath.isEmpty()){
			
			
			try {
				
				ArrayList<String> msg=new ArrayList<>();
				
				for (RPackageInfo packageinfo : requiredRpackages) {
					
					boolean installed=false;
					
					if(Rlibspath!=null)
						installed=RInstallTools.checkIfPackageIsInstalled(packageinfo, Rpath, Rlibspath);
					else
						installed=RInstallTools.checkIfPackageIsInstalled(packageinfo, Rpath, null);
					
					if(installed){
						msg.add(packageinfo.getPackageName()+" = installed");
						LogMessageCenter.getLogger().addTaskStatusMessage("Package "+packageinfo.getPackageName()+" already installed, skip installation");
						LogMessageCenter.getLogger().toClass(JBiclustGESetupManager.class).addInfoMessage("Package "+packageinfo.getPackageName()+" already installed, skip installation");
					}
					
					if(!installed){
						
						LogMessageCenter.getLogger().toClass(JBiclustGESetupManager.class).addInfoMessage("Package "+packageinfo.getPackageName()+" not installed, doing installation");
						LogMessageCenter.getLogger().addTaskStatusMessage("Package "+packageinfo.getPackageName()+" is not installed, doing installation please wait...");
						
						if(packageinfo.isBioconductorPackage()){
							if(Rlibspath!=null)
								installed=RInstallTools.installBioconductorPackage(packageinfo, Rlibspath);
							else
								installed=RInstallTools.installBioconductorPackage(packageinfo);
						}
						else{
							if(Rlibspath!=null)
								installed=RInstallTools.installPackage(packageinfo, Rlibspath);
							else
								installed=RInstallTools.installPackage(packageinfo);
						}
						
						if(installed) {
							msg.add(packageinfo.getPackageName()+" = installed");
							LogMessageCenter.getLogger().addSubTaskStatusMessage(packageinfo.getPackageName()+" = installed");
						}
						else {
							msg.add(packageinfo.getPackageName()+" = not installed");
							LogMessageCenter.getLogger().addSubTaskStatusMessage(packageinfo.getPackageName()+" = not installed");
						}
					}
				}
				
				MTUFileUtils.SaveorAppendArrayLisToFile(getStatusFile().getAbsolutePath(), msg, true);
				
			}
			catch (Exception e){
					LogMessageCenter.getLogger().addCriticalErrorMessage("Error installing R packages: ",e);
			}
			
			
			return true;
		}
		else{
		  LogMessageCenter.getLogger().addCriticalErrorMessage("Not was detected a R environment in this computer, please install R from: https://www.r-project.org/ ");
		  
		  ArrayList<String> msg=new ArrayList<>();
		  for (RPackageInfo packageinfo : requiredRpackages) {
			  msg.add(packageinfo.getPackageName()+" = not installed");
		  }
		  try {
			MTUFileUtils.SaveorAppendArrayLisToFile(getStatusFile().getAbsolutePath(), msg, true);
		  	} catch (Exception e) {
			// TODO: handle exception
		  }
		  
		  return false;
		}
		
		
		
	}


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		JBiclustGESetupManager.setupJBiclustGEMethodsEnvironment("/home/orocha/discodados/ApenasTrabalho/Testes/TestRlibs");

	}

}
