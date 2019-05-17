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
package jbiclustge.utils.osystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import jbiclustge.utils.props.JBiGePropertiesManager;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SystemFolderTools.
 */
public class SystemFolderTools {
	
	/** The Constant binariesfolder. */
	public static final String binariesfolder="methods_exe";
	
	/** The Constant enrichmentanalysisfolder. */
	public static final String enrichmentanalysisfolder="enrichmentanalysis";
	
	/** The Constant LINUXFOLDER. */
	public static final String LINUXFOLDER="linux";
	
	/** The Constant WINDOWSFOLDER. */
	public static final String WINDOWSFOLDER="windows";
	
	/** The Constant BOTHOSSYSTEMS. */
	public static final String BOTHOSSYSTEMS="windows";
	
	/** The Constant JBICLUSTUSERTEMPFOLDER. */
	public static final String JBICLUSTUSERTEMPFOLDER="jbicge_temp_files";
	
	public static final String JBICLUSTLOGDIR="jbiclustge_logs";
	
	/**
	 * Gets the current dir.
	 *
	 * @return the current dir
	 */
	public static String getCurrentDir(){
		 String currentpath = new File(".").getAbsolutePath();
		 Path p = Paths.get(currentpath);
		 return p.getParent().toString();
	 }
	
	
	/**
	 * Gets the folder path of binaries.
	 *
	 * @return the folder path of binaries
	 */
	public static String getFolderPathOfBinaries(){
		return getCurrentDir()+File.separator+binariesfolder+File.separator;
	}
	
	/**
	 * Check and set folder of binaries.
	 *
	 * @return the string
	 */
	public static String checkAndSetFolderOfBinaries(){
		File folderpath= new File(getFolderPathOfBinaries());
		if(!folderpath.exists())
			folderpath.mkdirs();
		
		return folderpath.getAbsolutePath();
	}
	
	
	public static String getLogFolder() {
		String folderpath=FilenameUtils.concat(getCurrentDir(), JBICLUSTLOGDIR);
		MTUDirUtils.checkDirectory(folderpath);
		return folderpath;
	}

	
	/**
	 * Gets the method exe path.
	 *
	 * @param name the name
	 * @return the method exe path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getMethodExePath(String name) throws IOException{
		String path=null;
		if(OSystemUtils.isLinux() || OSystemUtils.isWindows()){
			if(OSystemUtils.isWindows() && !name.endsWith(".jar"))
				name=name+".exe";
			path=FilenameUtils.concat(getFolderPathOfBinaries(), name);
		}
		else
			throw new IOException("Unsupported Operating System");
		

		if(path!=null && new File(path).exists())
			return path;
		else
			throw new IOException("The Binary "+name+"is missing in binaries folder");
	}
	
	

	
	/**
	 * Gets the linux executables path.
	 *
	 * @return the linux executables path
	 */
	public static String getLinuxExecutablesPath(){
		return getFolderPathOfBinaries()+LINUXFOLDER;
	}
	
	
	/**
	 * Gets the methods folder name.
	 *
	 * @return the methods folder name
	 */
	public static String getMethodsFolderName(){
		return binariesfolder;
	}
	
	
	/**
	 * Creates the temp process folder.
	 *
	 * @param maindir the maindir
	 * @param foldernamenosufix the foldernamenosufix
	 * @return the string
	 */
	public static String createTempProcessFolder(String maindir,String foldernamenosufix){
		//String name=foldernamenosufix+"_temp_process_folder";
		String name=foldernamenosufix;
		String folderpath=FilenameUtils.concat(maindir, name);
		File folder=new File(folderpath);
		if(!folder.exists())
			folder.mkdirs();
		return folderpath;
	}
	
	/**
	 * Creates the random temporary process folder.
	 *
	 * @return the string
	 */
	public static String createRandomTemporaryProcessFolder(){
		String foldername=MTUStringUtils.shortUUID();
		return createTempProcessFolder(getMainFolderTemporaryProcesses(), foldername);
	}
	
	/**
	 * Creates the random temporary process folder with name prefix.
	 *
	 * @param prefixname the prefixname
	 * @return the string
	 */
	public static String createRandomTemporaryProcessFolderWithNamePrefix(String prefixname){
		String foldername=MTUStringUtils.shortUUID();
		//System.out.println("Main Folder: "+getMainFolderTemporaryProcesses());
		return createTempProcessFolder(getMainFolderTemporaryProcesses(), prefixname+"_"+foldername);
	}
	
	

	
	/**
	 * Creates the folder.
	 *
	 * @param folderpath the folderpath
	 * @return the string
	 */
	public static String createFolder(String folderpath){
		File folder=new File(folderpath);
		if(!folder.exists())
			folder.mkdirs();
		return folderpath;
	}
	
	/**
	 * Gets the main folder temporary processes.
	 *
	 * @return the main folder temporary processes
	 */
	public static String getMainFolderTemporaryProcesses(){
		String userdir=(String) JBiGePropertiesManager.getManager().getKeyValue(JBICLUSTUSERTEMPFOLDER);
		if(userdir!=null)
			return createFolder(userdir);
		else {
			return createFolder(FilenameUtils.concat(System.getProperty("java.io.tmpdir"),JBICLUSTUSERTEMPFOLDER));
		}
	}
	

	/**
	 * Delete temp file.
	 *
	 * @param filepath the filepath
	 * @return true, if successful
	 */
	public static boolean deleteTempFile(String filepath){
		File tempfile=new File(filepath);
		if(tempfile.exists())
			return tempfile.delete();
		return false;
	}
	
	/**
	 * Delete temp dir.
	 *
	 * @param dirpath the dirpath
	 * @return true, if successful
	 */
	public static boolean deleteTempDir(String dirpath){
		File tmpdir=new File(dirpath);
		if(tmpdir.exists())
			try {
				FileUtils.deleteDirectory(tmpdir);
			} catch (IOException e) {
				LogMessageCenter.getLogger().addErrorMessage(e.getMessage());
				return false;
			}
		return true;
	}


	
	/**
	 * Gets the enrichment analysis directory.
	 *
	 * @return the enrichment analysis directory
	 */
	public static String getEnrichmentAnalysisDirectory(){
		String dir=FilenameUtils.concat(getCurrentDir(), enrichmentanalysisfolder);
		File f =new File(dir);
		if(!f.exists())
			f.mkdirs();
		return dir;
	}
	
	/**
	 * Gets the binary file path from properties file.
	 *
	 * @param namebinary the namebinary
	 * @return the binary file path from properties file
	 */
	public static String getBinaryFilePathFromPropertiesFile(String namebinary){
		String path=null;
		path=(String) JBiGePropertiesManager.getManager().getKeyValue(namebinary);
		if(path==null || path.isEmpty())
			path=(String) JBiGePropertiesManager.getManager().getKeyValue(namebinary.toLowerCase());
		
		if(path!=null && path.isEmpty())
			path=null;
		return path;
	}
	
	
	public static String getOrgDatabaseTmpFilepath() {
		return FilenameUtils.concat(SystemFolderTools.getCurrentDir(), "properties"+File.separator+"saved_org_databases.txt");
	}
	
	/**
	 * Check and set J bicluste GE working dir.
	 *
	 * @return the string
	 */
	public static String checkAndSetJBiclusteGEWorkingDir(){
		String userhome=System.getProperty("user.home");
		String jbiclustgeworkdir=FilenameUtils.concat(userhome, "jbiclustGE_workspace");
		File f=new File(jbiclustgeworkdir);
		if(!f.exists())
			f.mkdirs();
		return jbiclustgeworkdir;
	}
	
}
