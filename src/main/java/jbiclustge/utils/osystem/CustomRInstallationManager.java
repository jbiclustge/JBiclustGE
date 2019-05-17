package jbiclustge.utils.osystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;

import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.systemutils.linuxshell.listeners.ProgressShellListener;

public class CustomRInstallationManager{
	
	public enum RenvVersion{
		
		R_3_4_1{
			@Override
			public String getVersion() {
				return "3.4.1";
			}
		},
		R_3_4_3{
			@Override
			public String getVersion() {
				return "3.4.3";
			}
		},
		R_3_4_4{
			@Override
			public String getVersion() {
				return "3.4.4";
			}
		},
		R_3_5_1{
			@Override
			public String getVersion() {
				return "3.5.1";
			}
		};
		
		public String getVersion() {
			return getVersion();
		}
		
		@Override
		public String toString() {
			return getVersion();
		}
		
		public static RenvVersion getREnvVersionFromString(String version) {
			
			for (RenvVersion elem : RenvVersion.values()) {
				if(version.equals(elem.getVersion()))
					return elem;
			}
			
			return RenvVersion.R_3_4_3;
		}
	}
	

	private static String INSTALLERNAME="install_R_locally.sh";
	public static String RHomeName="Rlocal_";
	private String RVersion="3.4.3";
	private int ncompileproc=2;
	private boolean notsupportsxorg=false;
	private boolean installonlyR=false;
	private ProgressShellListener proglistener=null;
	
	public CustomRInstallationManager() {
		
	}
	
	public CustomRInstallationManager(RenvVersion version) {
		this.RVersion=version.getVersion();
	}
	
	public CustomRInstallationManager(int concurrentcompileprocesses) {
		this.ncompileproc=concurrentcompileprocesses;
	}
	
	public CustomRInstallationManager(RenvVersion version, int concurrentcompileprocesses) {
		this.RVersion=version.getVersion();
		this.ncompileproc=concurrentcompileprocesses;
	}
	
	public CustomRInstallationManager X11NotSupported(boolean supportsx11) {
		this.notsupportsxorg=supportsx11;
		return this;
	}
	
	public CustomRInstallationManager installOnlyR() {
		installonlyR=true;
		return this;
	}
	
	public CustomRInstallationManager addProgressListener(ProgressShellListener proglistener) {
		this.proglistener=proglistener;
		return this;
	}

	public boolean install() {

		//String rhome=SystemFolderTools.getCurrentDir();

		try {
			ArrayList<String> cmds=new ArrayList<>();
			cmds.add("/bin/sh");
			cmds.add("-c");
			cmds.add(getInstallerFile()+getInputParameters());
			Runtime.getRuntime().exec("chmod 777 "+getInstallerFile());
			

			ProcessBuilder build= new ProcessBuilder(cmds);
			build.redirectErrorStream(true);
			final Process p =build.start();
			InputStream inputstr =p.getInputStream();

			if(proglistener==null)
				proglistener=new ProgressShellListener(inputstr).showInfoOutput(true).checkForErrors("No rule to make target 'install'.  Stop.");
			else
				proglistener.setInputStream(inputstr);
			//Thread stdout=new Thread(new RInstallerProgressionChecker(inputstr));

			Thread stdout=new Thread(proglistener);
			stdout.run();
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				  @Override
				  public void run() {
				    p.destroy();
				   /* try {
						MTUDirUtils.deleteDirectory(FilenameUtils.concat(SystemFolderTools.getCurrentDir(), RHomeName+RVersion));
					} catch (IOException e) {
						LogMessageCenter.getLogger().addErrorMessage(e);
					}*/
				  }
				});
			int exitval=p.waitFor();
			if(!proglistener.isValidProcessExecution())
				exitval=1;

			MTUFileUtils.deleteFile(getInstallerFile());
			if(!installonlyR) {
				String builddir=FilenameUtils.concat(getRootDir(), "build_dir");
				if(new File(builddir).exists())
					MTUDirUtils.deleteDirectory(builddir);
			}
			 
			if(exitval!=0){
				return false;
			}
			
		} catch (IOException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		} catch (InterruptedException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		}

        
		return true;
	}
	
	private String getInputParameters() {
		StringBuilder str=new StringBuilder();
		str.append(" -i ");
		str.append(SystemFolderTools.getCurrentDir());
		str.append(" -v ");
		str.append(RVersion);
		str.append(" -n ");
		str.append(getNumberProcesses());
		str.append(" -j ");
		str.append(JBiclustGESetupManager.getJavaHomeForCompileProcedures());
		if(notsupportsxorg)
			str.append(" -nx");
		if(installonlyR)
			str.append(" -ro");
		
		
		//System.out.println(str.toString());
		return str.toString();
	}
	
	private int getNumberProcesses() {
		
		int n=Runtime.getRuntime().availableProcessors();
		if(n<=4 && ncompileproc==2)
			return 2;
		if(n<=4 && ncompileproc!=2)
			return ncompileproc;
		else if(n>4 && ncompileproc!=2)
			return ncompileproc;
		else
			return n/2;
	}
	
	
	
	public String getR_Path() {

		String mainfolder=FilenameUtils.concat(SystemFolderTools.getCurrentDir(), RHomeName+RVersion);

		File mf=new File(mainfolder);

		if(mf.exists())
             return JBiclustGESetupManager.getMostLikelyREnvDir(mainfolder);
		return null;
	}
	
	public String getRootDir() {
		return FilenameUtils.concat(SystemFolderTools.getCurrentDir(), RHomeName+RVersion);
	}
	


	public String getFolderToRpackages() {
		
		String rpath=getR_Path();
		if(rpath!=null) {
			String rlibrary=FilenameUtils.concat(rpath, "library");
				if(!new File(rlibrary).exists())
					new File(rlibrary).mkdirs();
			return rlibrary;
		}
		
		return null;
	}
	
	
	
	private String getInstallerFile() throws IOException {
		
		String installerpath=FilenameUtils.concat(SystemFolderTools.getCurrentDir(),INSTALLERNAME);
		
		if(!new File(installerpath).exists()) {

			InputStream in = ClassLoader.getSystemResourceAsStream(INSTALLERNAME);
			
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
		
			String line;
			try {

				br = new BufferedReader(new InputStreamReader(in));
				while ((line = br.readLine()) != null) {
					sb.append(line+"\n");
				}

			} catch (IOException e) {
				LogMessageCenter.getLogger().addErrorMessage(e);
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						LogMessageCenter.getLogger().addErrorMessage(e);
					}
				}
			}

			MTUWriterUtils.writeStringWithFileChannel(sb.toString(), installerpath, 0);
			
		}
		
		return installerpath;
	}
	
	
	public static void main(String[] args) {
		//LogMessageCenter.getLogger().setLogLevel(MTULogLevel.TRACE);
		CustomRInstallationManager installer=new CustomRInstallationManager(RenvVersion.R_3_4_3);
		installer.install();
	}
	
	
	public static ArrayList<Pair<RenvVersion, String>> checkIfAnyCustomVersionItsInstalled() {
		
		ArrayList<Pair<RenvVersion, String>> versions=null;
		
		for (RenvVersion vers : RenvVersion.values()) {
			String currversion=checkIfVersionIsInstalled(vers);
			if(currversion!=null) {
				if(versions==null)
					versions=new ArrayList<>();
				versions.add(new Pair<CustomRInstallationManager.RenvVersion, String>(vers, currversion));
			}
		}
		return versions;
	}
	
	
	public static String checkIfVersionIsInstalled(RenvVersion version) {
		
		String option1=FilenameUtils.concat(SystemFolderTools.getCurrentDir(), RHomeName+version.getVersion()+File.separator+"lib64"+File.separator+"R");
		File fopt1=new File(option1);
		
		String option2=FilenameUtils.concat(SystemFolderTools.getCurrentDir(), RHomeName+version.getVersion()+File.separator+"lib"+File.separator+"R");
		File fopt2=new File(option2);
		
		if(fopt1.exists() && fopt1.isDirectory())
			return option1;
		else if(fopt2.exists() && fopt2.isDirectory())
			return option2;
		
		
		return null;
	}
	

}
