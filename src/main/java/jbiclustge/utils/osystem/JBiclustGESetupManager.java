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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.zeroturnaround.zip.ZipUtil;

import com.google.common.collect.ImmutableMap;

import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.utils.osystem.progcheck.JGESetupSettingsContainer;
import jbiclustge.utils.osystem.progcheck.RInstallerProgressionChecker;
import jbiclustge.utils.props.JBiclustGEPropertiesInitializer;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.rtools.installutils.RInstallTools;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.rtools.installutils.components.RPackageInfo.RpackageType;
import pt.ornrocha.rtools.installutils.components.ShellExportFeatures;
import pt.ornrocha.rtools.installutils.components.ShellExportFeatures.CompileTypeFeature;
import pt.ornrocha.systemutils.OSystemUtils;
import pt.ornrocha.systemutils.OSystemUtils.OS;
import pt.ornrocha.systemutils.linuxshell.MTUShellLinux;
import pt.ornrocha.systemutils.linuxshell.listeners.ProgressShellListener;
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
	public static String LIBSEXPORTFEATURES=".Libs_export_features";
	public static String LOCALLINUXLIBSTAG=".locallinuxlibs";
	
	/** The jbiclustgebasezipname. */
	public static String JBICLUSTGEBASEZIPNAME="biclustering_methods";
	
	/** The jbiclustbasedownloadurl. */
	private static String JBICLUSTBASEDOWNLOADURL="https://jbiclustge.github.io/files/";

	public static JGESetupSettingsContainer settingscontainer=new JGESetupSettingsContainer();

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
	
	public static void addLibsExportFeaturesConfig(LinkedHashMap<String, String> propsmap) {
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+LIBSEXPORTFEATURES);
		
		StringBuilder str=new StringBuilder();
		for (String key : propsmap.keySet()) {
			str.append(key+"="+propsmap.get(key)+"\n");
		}
		
		try {
			MTUWriterUtils.writeDataTofile(filestatus.getAbsolutePath(), str.toString());
		} catch (IOException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		}
	}
	
	public static void addLibsExportFeaturesConfig(String renvrootdir) {
		LinkedHashMap<String, String> propsmap =new LinkedHashMap<>();
		propsmap.put(ShellExportFeatures.ROOTDIR, renvrootdir);
		addLibsExportFeaturesConfig(propsmap);
	}
	
	public static void removeLibsExportFeaturesConfig(){
		File filestatus=new File(SystemFolderTools.getCurrentDir()+File.separator+LIBSEXPORTFEATURES);
		if(filestatus.exists())
			filestatus.delete();	
	}
	
	public static boolean usesLocalCompiledLinuxLibs(String dirpath) {
		return MTUDirUtils.containsFileNameInFolder(dirpath, LOCALLINUXLIBSTAG);
	}
	
	public static void addLocalLinuxLibsTag(String dirpath) throws IOException{
		File file=new File(FilenameUtils.concat(dirpath, LOCALLINUXLIBSTAG));
		file.createNewFile();
	}
	
	
	
	
	public static ShellExportFeatures getShellFeatures() {
		File file=new File(SystemFolderTools.getCurrentDir()+File.separator+LIBSEXPORTFEATURES);
		if(file.exists())
			try {
				return ShellExportFeatures.loadInstanceFromFile(file.getAbsolutePath()).setTemporaryShellScriptsFolder(SystemFolderTools.getMainFolderTemporaryProcesses());
			} catch (FileNotFoundException e) {
				LogMessageCenter.getLogger().addCriticalErrorMessage(e);
			} catch (IOException e) {
				LogMessageCenter.getLogger().addCriticalErrorMessage(e);
			}
		
		return null;
	}
	
	public static boolean isShellLinuxExportFeaturesEnabled() {
		File file=new File(SystemFolderTools.getCurrentDir()+File.separator+LIBSEXPORTFEATURES);
		if(file.exists())
			return true;
		return false;
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
	public static void setupJBiclustGEMethodsEnvironment(String ruserlibspath, String RHomePath) throws Exception{
		
		createJBiclustGEStatus();
		setupExecutableBiclusteringMethods();
		if(RHomePath!=null)
			OSystemUtils.setEnvVariable("R_HOME", RHomePath);
		String R_PATH=RInstallTools.getSystemR_PATH();
		String output=setupREnvironmentPackages(R_PATH,ruserlibspath,false);
		setupJBiclustGEProperties(output,RHomePath);
		
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
	public static void setupJBiclustGEProperties(String Rlibspath,String RHomePath) throws IOException{
		
		File propsdir=new File(FilenameUtils.concat(SystemFolderTools.getCurrentDir(), "properties"));
		if(!propsdir.exists())
			propsdir.mkdirs();
		
		String propsfile=FilenameUtils.concat(propsdir.getAbsolutePath(), PropertyLabels.JBICLUSTPROPSFILENAME);
		if(!new File(propsfile).exists())
			JBiclustGEPropertiesInitializer.writeInitialConfiguration(propsfile,Rlibspath,RHomePath);
		
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
	public static String setupREnvironmentPackages(String Rpath, String Rlibspath,boolean installplotpackages) throws Exception{
		
		
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
			installinfolder=checkRLibsInstallPath(Rpath);
		
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
	public static String checkRLibsInstallPath(String Rpath) throws Exception{
		
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
			
			String R_PATH=null;
			String R_version=null;
			
			if(Rpath!=null)
				R_PATH=Rpath;
			else {
				R_PATH=RInstallTools.getSystemR_PATH();
				if(R_PATH.equals(RInstallTools.NONE_R_HOME))
					R_PATH=null;
			}
			
			if(R_PATH!=null)
				R_version=RInstallTools.getRenvVersion(R_PATH);
			
			String currentfolder=SystemFolderTools.getCurrentDir();
			String foldername=PropertyLabels.RDEFAULTLIBSFOLDER;
			if(R_version!=null)
				foldername=foldername+"_"+R_version;
			
			String pathrlibs=FilenameUtils.concat(currentfolder, foldername);
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
			
			
		
			ArrayList<String> installed=getInstalledFiles(RInstallTools.validateToUnixPath(folderbin));

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
			
			if(OSystemUtils.isLinux()) {
				ProgressShellListener listener=new ProgressShellListener().checkForError("unable to resolve host address").validateByFinalOutput("saved [7361389/7361389]");
				boolean valid=MTUShellLinux.executeCMDsAtLinuxShell(listener,"wget","--no-check-certificate","https://jbiclustge.github.io/files/biclustering_methods_linux.zip");
			    
				if(valid && listener.isValidProcessExecution()) {
			    	LogMessageCenter.getLogger().addInfoMessage("Methods were downloaded successfully from: "+url);
					LogMessageCenter.getLogger().addSubTaskStatusMessage("Methods were downloaded successfully from: "+url);
					return setupMethodsViaZipFile();
			    }
			    else {
			    	LogMessageCenter.getLogger().addInfoMessage("Unsuccessful download attempt... please check if your internet connection it is working, or do manual installation of the biclustering methods.");
					LogMessageCenter.getLogger().addSubTaskStatusMessage("Unsuccessful download attempt... please check if your internet connection it is working\n or do manual installation of the biclustering methods.");
					return false;
			    }
			    	
			    	
			}
			else if(downloader.connectionException()!=null && throwconnectionexception)
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
	
	
	public static String getRLocalRootFolderFromPath(String path) {
		
		String name=FilenameUtils.getBaseName(path);
		if(name.startsWith("Rlocal"))
			return path;
		else {
			boolean check=true;
			
			String currentpath=new File(path).getParent();
			
			while(check) {
				
				String currname=FilenameUtils.getBaseName(currentpath);
				if(currname.startsWith("Rlocal"))
					check=false;
				else {
					currentpath=new File(currentpath).getParent();
				}
				
			}
			return currentpath;
		}
	}
	

	public static String getMostLikelyREnvDir(String maindir) {
		
		String lastfolder=FilenameUtils.getBaseName(maindir);
		if(lastfolder.equals("lib64") && new File(FilenameUtils.concat(maindir, "R")).exists())
			return new File(FilenameUtils.concat(maindir, "R")).getAbsolutePath();
		else {
		     
			ArrayList<String> possibledirs=MTUDirUtils.getDirectoryTreeIfReachDirectoryName(maindir, "lib64");
			if(possibledirs.size()>0) {
				for (int i = 0; i < possibledirs.size(); i++) {
					File folderR=new File(FilenameUtils.concat(possibledirs.get(i), "R"));
					if(folderR.exists() && folderR.isDirectory())
						return folderR.getAbsolutePath();	
				}
				return getRenvThroughLibDir(maindir);
			}
			else
				return getRenvThroughLibDir(maindir);
		}
	}
	
	private static String getRenvThroughLibDir(String maindir) {

		ArrayList<String> possibledirs=MTUDirUtils.getDirectoryTreeIfReachDirectoryName(maindir, "lib");

		for (int i = 0; i < possibledirs.size(); i++) {
			File folderR=new File(FilenameUtils.concat(possibledirs.get(i), "R"));
			if(folderR.exists() && folderR.isDirectory() && new File(FilenameUtils.concat(folderR.getAbsolutePath(), "bin"+File.separator+"R")).exists())
				return folderR.getAbsolutePath();
		}

		return getRenvThroughBinDir(maindir);
	}
	
	private static String getRenvThroughBinDir(String maindir) {
		
		ArrayList<String> possibledirs=MTUDirUtils.getDirectoryTreeIfReachDirectoryName(maindir, "bin");
		
		for (int i = 0; i < possibledirs.size(); i++) {
			File parentpath=new File(possibledirs.get(i)).getParentFile();
			String parentname=FilenameUtils.getBaseName(parentpath.getAbsolutePath());
			if(parentname.equals("R") && parentpath.isDirectory())
				return parentpath.getAbsolutePath();
		}
		
		for (int i = 0; i < possibledirs.size(); i++) {
			File rbin=new File(FilenameUtils.concat(possibledirs.get(i), "R"));
			if(rbin.exists() && rbin.isFile())
				return FilenameUtils.getFullPathNoEndSeparator(possibledirs.get(i));
		}
		
		return null;
	}
	
	
	public static String getMostLikelyREnvFolderToRpackages(String maindir) {
		String RHOME=getMostLikelyREnvDir(maindir);
		if(RHOME!=null)
			return FilenameUtils.concat(RHOME, "library");
        return null;
	}
	
	
	

	/*private static ArrayList<RPackageInfo> getBaseRPackages(){
		ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		requiredRpackages.add(RPackageInfo.define("Rserve"));
		requiredRpackages.add(RPackageInfo.define("BicARE", true));
		requiredRpackages.add(RPackageInfo.define("matrixStats"));
		requiredRpackages.add(RPackageInfo.define("biclust",RPackageInfo.define("flexclust")));
		requiredRpackages.add(RPackageInfo.define("fabia", true));
		requiredRpackages.add(RPackageInfo.define("eisa", true));
		requiredRpackages.add(RPackageInfo.define("topGO", true));
		//requiredRpackages.add(RPackageInfo.define("gplots"));
		//requiredRpackages.add(RPackageInfo.define("ggplot2"));
		requiredRpackages.add(RPackageInfo.define("ggplot2", 
				RPackageInfo.define("gtable"),RPackageInfo.define("scales"), RPackageInfo.define("munsell"), RPackageInfo.define("colorspace"), RPackageInfo.define("plyr"), RPackageInfo.define("lazyeval"), RPackageInfo.define("tibble")));
		return requiredRpackages;
	}
	
	private static ArrayList<RPackageInfo> getPlotRPackages(){
		ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		requiredRpackages.add(RPackageInfo.define("plotly", RPackageInfo.define("bindrcpp"), RPackageInfo.define("glue"), RPackageInfo.define("tidyselect"), RPackageInfo.define("tidyr"), RPackageInfo.define("viridisLite")));
		requiredRpackages.add(RPackageInfo.define("viridis", RPackageInfo.define("gridExtra")));
		requiredRpackages.add(RPackageInfo.define("jsonlite"));
		requiredRpackages.add(RPackageInfo.define("RColorBrewer"));
		requiredRpackages.add(RPackageInfo.define("readxl",RPackageInfo.define("cellranger")));
		requiredRpackages.add(RPackageInfo.define("yaml"));
		requiredRpackages.add(RPackageInfo.define("DT"));
		requiredRpackages.add(RPackageInfo.define("xtable"));
		requiredRpackages.add(RPackageInfo.define("htmltools"));
		requiredRpackages.add(RPackageInfo.define("htmlwidgets"));
		requiredRpackages.add(RPackageInfo.define("dplyr"));
		requiredRpackages.add(RPackageInfo.define("shiny", RPackageInfo.define("httpuv"), RPackageInfo.define("later"), RPackageInfo.define("promises"), RPackageInfo.define("mime")));
		requiredRpackages.add(RPackageInfo.define("heatmaply", RPackageInfo.define("trimcluster")));
		requiredRpackages.add(RPackageInfo.define("shinyHeatmaply"));
		requiredRpackages.add(RPackageInfo.define("parcoords", "timelyportfolio/parcoords", RPackageInfo.define("httr"), RPackageInfo.define("R6"), RPackageInfo.define("curl")));
		return requiredRpackages;
	}*/
	
	private static ArrayList<RPackageInfo> getBaseRPackages(){
		ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		
		requiredRpackages.add(RPackageInfo.define("Rserve").setPackageRepositoryURL("http://www.rforge.net/"));
		
		// install critical packages in linux if local Renv
		if(OSystemUtils.isLinux() && isShellLinuxExportFeaturesEnabled()) {
			requiredRpackages.add(RPackageInfo.define("openssl")
				.setConfigureVariables("INCLUDE_DIR", CompileTypeFeature.INCLUDE)
				.setConfigureVariables("LIB_DIR", CompileTypeFeature.LIB));
			
		}
		
		requiredRpackages.add(RPackageInfo.define("BicARE", true));
		requiredRpackages.add(RPackageInfo.define("matrixStats"));// for biclic

		if(OSystemUtils.isWindows())
        	requiredRpackages.add(RPackageInfo.define("dplyr").setPackageType(RpackageType.BINARY));
		
		requiredRpackages.add(RPackageInfo.define("ggplot2"));
		requiredRpackages.add(RPackageInfo.define("biclust"));
		requiredRpackages.add(RPackageInfo.define("fabia", true));
		requiredRpackages.add(RPackageInfo.define("eisa", true));
		requiredRpackages.add(RPackageInfo.define("topGO", true));
		
		if(OSystemUtils.isLinux() && isShellLinuxExportFeaturesEnabled()) {
			requiredRpackages.add(RPackageInfo.define("units")
				.setConfigureArguments("--with-udunits2-lib", CompileTypeFeature.LIB)
				.setConfigureArguments("--with-udunits2-include",CompileTypeFeature.INCLUDE));
		}

		if(OSystemUtils.isLinux()) {
			try {
				String gccversion = getGCCVersion();
				if(gccversion!=null) {
					if(!RInstallTools.isVersionHighThan("4.6.0", gccversion)) {
						requiredRpackages.add(RPackageInfo.define("fgsea").downloadFrom("https://github.com/ctlab/fgsea/archive/v0.99.7.tar.gz", "v0.99.7.tar.gz", "saved [523739]"));
					}
				}
			} catch (Exception e) {
				LogMessageCenter.getLogger().addCriticalErrorMessage(e);
			}
		}
		
		requiredRpackages.add(RPackageInfo.define("clusterProfiler", true));

		return requiredRpackages;
	}
	
	
	private static ArrayList<RPackageInfo> getPlotRPackages(){
		ArrayList<RPackageInfo> requiredRpackages=new ArrayList<>();
		
		requiredRpackages.add(RPackageInfo.define("plotly").setDependencies(RPackageInfo.define("yaml")));
		requiredRpackages.add(RPackageInfo.define("shiny"));
		requiredRpackages.add(RPackageInfo.define("heatmaply"));
		requiredRpackages.add(RPackageInfo.define("shinyHeatmaply"));
		requiredRpackages.add(RPackageInfo.define("parcoords", "timelyportfolio/parcoords").forceInstall());

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

		String Rpath=RInstallTools.getSystemR_PATH();


		if(Rpath!=null && !Rpath.isEmpty() && !Rpath.equals(RInstallTools.NONE_R_HOME)){


			try {

				ArrayList<String> msg=new ArrayList<>();
				LinkedHashMap<String, ArrayList<String>> errorpackages=new LinkedHashMap<>();
				RInstallerProgressionChecker progresscheck=null;


				if(settingscontainer.containsSetting(RInstallerProgressionChecker.RINSTALLERPROGRESSCHECK))
					progresscheck=(RInstallerProgressionChecker) settingscontainer.getSetting(RInstallerProgressionChecker.RINSTALLERPROGRESSCHECK);

				

				for (RPackageInfo packageinfo : requiredRpackages) {

					RInstallerProgressionChecker currentprogresscheck=((progresscheck!=null)?progresscheck.copyInstance():null);
					/*if(currentprogresscheck!=null)
						currentprogresscheck.reset();*/


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


						if(OSystemUtils.isWindows() && packageinfo.getPackageName().equals("parcoords")) {
							
                            installed=tryToinstallWindowsparcoords(Rlibspath,packageinfo,currentprogresscheck);

						}
						else {	
							
							if(packageinfo.isLocalInstallation()) {
								boolean valdown=packageinfo.downloadPackageToLocalInstallation();
								
								//if(valdown) {
									int nsteps=0;
									boolean existfile=false;
									while (!existfile || nsteps<3) {
										existfile=new File(packageinfo.getLocalPackagePath()).exists();
										nsteps++;
										Thread.sleep(5000);
									}
									
									valdown=existfile;
								//}
								
								if(!valdown) {
									LogMessageCenter.getLogger().addCriticalErrorMessage("package: "+FilenameUtils.getName(packageinfo.getLocalPackagePath())+" cannot be downloaded");
								}
							}
							
							

							if(packageinfo.isBioconductorPackage()){
								installed=RInstallTools.installBioconductorPackage(packageinfo, Rlibspath, getShellFeatures(),currentprogresscheck);
								if(currentprogresscheck!=null && currentprogresscheck.errorsFound())
									installed=false;
							}
							else{
								installed=RInstallTools.installPackage(packageinfo, Rlibspath,getShellFeatures(),currentprogresscheck);
								if(currentprogresscheck!=null && currentprogresscheck.errorsFound())
									installed=false;
							}
						}

	
						if(installed) {
							msg.add(packageinfo.getPackageName()+" = installed");
							LogMessageCenter.getLogger().addSubTaskStatusMessage(packageinfo.getPackageName()+" = installed");
						}
						else {
							msg.add(packageinfo.getPackageName()+" = not installed");
							LogMessageCenter.getLogger().addSubTaskStatusMessage(packageinfo.getPackageName()+" = not installed");


							if(currentprogresscheck!=null && currentprogresscheck.getPackagesFailedToInstall().size()>0) {
								String currentpackage=packageinfo.getPackageName();
								ArrayList<String> packagesfail=new ArrayList<>();
								for (int i = 0; i < currentprogresscheck.getPackagesFailedToInstall().size(); i++) {
									String packname=currentprogresscheck.getPackagesFailedToInstall().get(i);
									if(!packname.equals(currentpackage))
										packagesfail.add(packname);
								}
								errorpackages.put(currentpackage, packagesfail);
							}
						}
					}
					
					
					/*if(packageinfo.isLocalInstallation() && packageinfo.getLocalPackagePath()!=null) {
						
						File localpackage=new File(packageinfo.getLocalPackagePath());
						if(localpackage.exists())
							localpackage.delete();
						
					}*/
				}

				if(errorpackages.size()>0)
					settingscontainer.addSetting(RInstallerProgressionChecker.RPACKAGESFAILED, errorpackages);

				if(progresscheck!=null)
					settingscontainer.removeSetting(RInstallerProgressionChecker.RINSTALLERPROGRESSCHECK);

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
	
	
	private static boolean tryToinstallWindowsparcoords(String Rlibspath,RPackageInfo packageinfo, RInstallerProgressionChecker progresscheck) throws Exception {
		

		if(progresscheck==null)
			progresscheck=new RInstallerProgressionChecker().checkForErrors("cannot remove prior installation of package 'Rcpp'");

		boolean installed=RInstallTools.installPackage(packageinfo, Rlibspath,null,progresscheck);
		
		if(progresscheck.errorsFound()) {
			
			ShellExportFeatures exportpath=new ShellExportFeatures();
			RPackageInfo rcppsource=new RPackageInfo("Rcpp").setPackageVersion("1.0.0").setPackageType(RpackageType.SOURCE);
			RInstallerProgressionChecker newprogresscheck=progresscheck.copyInstance().checkForErrors("cannot remove prior installation of package 'Rcpp'");
			newprogresscheck.reset();
			installed=RInstallTools.installPackage(rcppsource, Rlibspath,exportpath,newprogresscheck);
			if(installed) {
				progresscheck.reset();
				installed=RInstallTools.installPackage(packageinfo, Rlibspath,exportpath,progresscheck);
				if(newprogresscheck.errorsFound())
					return false;
				else
					return installed;
			}
			else
				return false;
			
		}
		else
			return installed;

		
	}
	
	
	public static LinkedHashMap<String, Boolean> checkIfMandatoryLibrariesInstalled() throws Exception{
		
		ArrayList<String> mandlibs=new ArrayList<>(Arrays.asList("liblapack","libblas","libopenblas"));
		
		LinkedHashMap<String, Boolean> res=new LinkedHashMap<>();
		
		for (int i = 0; i < mandlibs.size(); i++) {
			
			boolean isvalid=false;
			ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput(mandlibs.get(i)+".so");
			MTUShellLinux.executeCMDsAtLinuxShell(listener,"ldconfig -p | grep "+mandlibs.get(i));
			isvalid=listener.isValidProcessExecution();
			if(mandlibs.get(i).equals("libblas") && isvalid) {
				res.put(mandlibs.get(i), true);
				break;
			}
			else if(!mandlibs.get(i).equals("libblas"))
			     res.put(mandlibs.get(i), isvalid);
		}
		
		return res;
	}
	
	public static LinkedHashMap<String, Boolean> checkIfCompilingToolsInstalled() throws Exception{
		
		LinkedHashMap<String, Boolean> res=new LinkedHashMap<>();

		String bess="build-essential";
		
		ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("install ok installed");
		MTUShellLinux.executeCMDsAtLinuxShell(listener,"dpkg-query","-W","--showformat='${Status}\n'",bess);
		boolean state=listener.isValidProcessExecution();
        if(!state)
        	state=isGCCInstalled();
        res.put(bess, state);	
		
	/*	ArrayList<String> libs=new ArrayList<>(Arrays.asList("checkinstall","cmake"));
		for (int i = 0; i < libs.size(); i++) {
			ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("install ok installed");
			MTUShellLinux.executeCMDsAtLinuxShell(listener,"dpkg-query","-W","--showformat='${Status}\n'",libs.get(i));
			boolean state=listener.isValidProcessExecution();
			res.put(libs.get(i), state);
		}*/
		
		return res;
	}
	
	
	public static boolean isGCCInstalled() throws Exception {
		
		ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("gcc version");
		MTUShellLinux.executeCMDsAtLinuxShell(listener,"gcc","-v");
		
		return listener.isValidProcessExecution();
	}
	
	public static String getGCCVersion() throws Exception {
		
		ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("gcc version");
		MTUShellLinux.executeCMDsAtLinuxShell(listener,"gcc","-v");
		String output=listener.getLastOutput();
		
		Pattern pat=Pattern.compile("gcc version (\\d(.\\d)*)\\s+");
		Matcher match=pat.matcher(output);
		
		if(match.find())
			return match.group(1);
		return null;
	}
	

	public static boolean isFortranInstalled() throws Exception {
		
		ArrayList<String> fortaneng=new ArrayList<>(Arrays.asList("gfortran","f77"));
		
		boolean state=false;
		
		for (int i = 0; i < fortaneng.size(); i++) {
			
			ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("gcc version");
			MTUShellLinux.executeCMDsAtLinuxShell(listener,fortaneng.get(i),"-v");
			state=listener.isValidProcessExecution();
			if(state)
				break;
		}

		return state;
	}
	
	public static boolean checkIfX11HeadersInstalled() throws Exception{
		ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("install ok installed");
		MTUShellLinux.executeCMDsAtLinuxShell(listener,"dpkg-query","-W","--showformat='${Status}\n'","xorg-dev");
		return listener.isValidProcessExecution();
	}
	
	public static boolean checkIfFortranInstalled() throws Exception{
		

		ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("install ok installed");
		ArrayList<String> fortran=new ArrayList<>(Arrays.asList("fort77‍‍‍‍‍‍","gfortran"));
		boolean oneinstalled=false;
		
		for (int i = 0; i < fortran.size(); i++) {
			MTUShellLinux.executeCMDsAtLinuxShell(listener,"dpkg-query","-W","--showformat='${Status}\n'",fortran.get(i));
			boolean state=listener.isValidProcessExecution();
			if(state)
				oneinstalled=true;
		}
		
		if(!oneinstalled)
			oneinstalled=isFortranInstalled();
		
		return oneinstalled;
	}
	
	

	/*public static LinkedHashMap<String, Boolean> checkIfRCompileLibrariesAreInstalled() throws Exception{
		
		ArrayList<String> mandlibs=new ArrayList<>(Arrays.asList("libcurl","libxml2","libssl","liblzma","libpcre","libbz2","libgsl","libcairo","libgit","libudunits2"));
		
		LinkedHashMap<String, Boolean> res=new LinkedHashMap<>();
		
		for (int i = 0; i < mandlibs.size(); i++) {
			
			boolean isvalid=false;
			ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput(mandlibs.get(i)+".so");
			MTUShellLinux.executeCMDsAtLinuxShell(listener,"ldconfig -p | grep "+mandlibs.get(i));
			isvalid=listener.isValidProcessExecution();
			res.put(mandlibs.get(i), isvalid);
		}
		
		return res;
	}*/
	
	public static LinkedHashMap<String, Boolean> checkIfRDEVLibrariesAreInstalled() throws Exception{

		ArrayList<String> mandlibs=new ArrayList<>(Arrays.asList("libcurl4-openssl-dev","libxml2-dev","libssl-dev","liblzma-dev","zlib1g-dev","libpcre3-dev","libbz2-dev","libgsl-dev","libcairo2-dev","libreadline-dev","libudunits2-dev"));
		//"libgit2-dev  -> libcurl4-gnutls-dev"
		LinkedHashMap<String, Boolean> res=new LinkedHashMap<>();

		for (int i = 0; i < mandlibs.size(); i++) {

			boolean isvalid=false;
			ProgressShellListener listener=new ProgressShellListener().validateByPrintedOutput("install ok installed");
			MTUShellLinux.executeCMDsAtLinuxShell(listener,"dpkg-query","-W","--showformat='${Status}\n'",mandlibs.get(i));
			isvalid=listener.isValidProcessExecution();
			res.put(mandlibs.get(i), isvalid);
		}

		return res;
	}
	
	public static ArrayList<String> checkMissingDevLinuxLibs() throws Exception{
		
		ArrayList<String> packagesmissing=new ArrayList<>();
		
		LinkedHashMap<String, Boolean> ndevpackages=checkIfRDEVLibrariesAreInstalled();
		for (String pack : ndevpackages.keySet()) {
			boolean state=ndevpackages.get(pack);
			if(!state)
				packagesmissing.add(pack);
		}
		
		return packagesmissing;
	}
	
	
	public static String getDebSystemRelatedPackage(String lib) {
		
		Map<String, String> libs = ImmutableMap.<String, String>builder()
			    .put("libgfortran", "gfortran")
			    .put("liblapack", "liblapack-dev")
			    .put("libopenblas","libopenblas-dev")
			    .put("libcurl","libcurl4-openssl-dev")
			    .put( "libxml2","libxml2-dev")
			    .put( "libssl","libssl-dev")
			    .put( "liblzma","liblzma-dev")
			    .put( "libreadline","libreadline-dev")
			    .put( "libpcre","libpcre3-dev")
			    .put( "libbz2","libbz2-dev")
			    .put( "libgsl","libgsl-dev")
			    .put( "libcairo","libcairo2-dev")
			    .put("libgit2", "libgit2-dev")
			    .put("libudunits2", "libudunits2-dev")
			    .build();
				

		return libs.get(lib);
	}
	
	
	// update-java-alternatives -l

	
	public static String getJavaHomeForCompileProcedures() {
		
		String javahome=System.getProperty("java.home");
		
		String parent=new File(javahome).getParent();
		String nameparent=FilenameUtils.getBaseName(parent);
		
		
		if(nameparent.startsWith("java-") && new File(FilenameUtils.concat(parent, "bin")).exists())
			return parent;
		else 
			return javahome;
		

	}
	
	public static ArrayList<String> getAlgorithmExecutableNames(){
		return new ArrayList<>(Arrays.asList("BBC","COALESCE","cpb","cpb_init_bicluster","debi","jbimax","qubic","unibic","BicFinder", "BICLIC", "BiMax", "BiMine+", "ubc"));
	}
	
	
	public static boolean checkIfAllExecutablesInstalled() {
		
		ArrayList<String> listexecutables=null;
		
		ArrayList<String> commonname=new ArrayList<>(Arrays.asList("BicFinder.jar", "BICLIC.R", "BiMax.jar", "BiMine+.jar", "ubc.jar"));
		if(OSystemUtils.isLinux())
			listexecutables=new ArrayList<>(Arrays.asList("BBC","COALESCE","cpb","cpb_init_bicluster","debi","jbimax","qubic","unibic"));
		else
			listexecutables=new ArrayList<>(Arrays.asList("BBC.exe","cpb.exe","cpb_init_bicluster.exe","debi.exe","jbimax.exe","qubic.exe","unibic.exe"));
		
		listexecutables.addAll(commonname);
		
		String methodsfolder=SystemFolderTools.getFolderPathOfBinaries();
		
		ArrayList<String> files=MTUDirUtils.getFilePathsInsideDirectory(methodsfolder, true, false);
		ArrayList<String> names=new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			names.add(FilenameUtils.getName(files.get(i)));
		}
		
		for (int i = 0; i < listexecutables.size(); i++) {
			if(!names.contains(listexecutables.get(i)))
				return false;
		}
		
		return true;
	}
	

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		//JBiclustGESetupManager.setupJBiclustGEMethodsEnvironment("/home/orocha/discodados/ApenasTrabalho/Testes/TestRlibs", null);
		//LogMessageCenter.getLogger().setLogLevel(MTULogLevel.TRACE);
		//System.out.println(checkIfRCompileLibrariesAreInstalled());
		//System.out.println(checkIfRCompileLibrariesAreInstalled());
		//System.out.println(checkIfRDEVLibrariesAreInstalled());
		//System.out.println(getRLocalRootFolderFromPath("/home/orocha/JBiclustGE-CLI/Rlocal_3.4.3/lib64/R"));
		//System.out.println(getDebSystemRelatedPackage("libreadline"));
		//System.out.println(getJavaHomeForCompileProcedures());
		//System.out.println(isGCCInstalled());
		//System.out.println(isFortranInstalled());
		//System.out.println(getGCCVersion());
		
		HTTPFileDownloader downloader=new HTTPFileDownloader("https://github.com/ctlab/fgsea/archive/v0.99.7.tar.gz", OSystemUtils.getCurrentDir());
		downloader.run();
		System.out.println(downloader.isSuccessfully());
	}

}
