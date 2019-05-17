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
package jbiclustge.methods.algorithms.wrappers.debi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.javatuples.Pair;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.wrappers.components.RegulationPattern;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.props.CommandsProcessList;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;

// TODO: Auto-generated Javadoc
/**
 * The Class DebiTask.
 */
public class DebiTask implements Callable<Pair<Boolean,BiclusterList>>{

	/** The task. */
	private DebiThread task;
	
	/**
	 * Instantiates a new debi task.
	 *
	 * @param expressiondata the expressiondata
	 * @param executablepath the executablepath
	 * @param inputdatafile the inputdatafile
	 * @param outputdatafile the outputdatafile
	 * @param pattern the pattern
	 * @param maxoverlap the maxoverlap
	 * @param binlevel the binlevel
	 * @param mincondsbic the mincondsbic
	 * @param binary the binary
	 */
	public DebiTask(ExpressionData expressiondata,String executablepath, String inputdatafile, String outputdatafile, RegulationPattern pattern, double maxoverlap, double binlevel, int mincondsbic, boolean binary){
		task=new DebiThread(expressiondata,executablepath, inputdatafile, outputdatafile, pattern, maxoverlap, binlevel, mincondsbic, binary);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Pair<Boolean,BiclusterList> call() throws Exception {
		Runtime.getRuntime().addShutdownHook(new KillDebi(task)); 
		task.run();
		Pair<Boolean, BiclusterList> res=new Pair<Boolean, BiclusterList>(task.getState(), task.getResults());
		return res;
	}
	
	public void killtask() {
		task.kill();
	}
	
	
	/**
	 * The Class DebiThread.
	 */
	private class DebiThread implements Runnable{

		/** The executablepath. */
		private String executablepath;
		
		/** The inputdata. */
		private String inputdata;
		
		/** The outputfile. */
		private String outputfile;
		
		/** The pattern. */
		private RegulationPattern pattern;
		
		/** The maxoverlap. */
		private double maxoverlap;
		
		/** The binlevel. */
		private double binlevel;
		
		/** The minconds. */
		private int minconds;
		
		/** The isbinarydata. */
		private boolean isbinarydata=false;
		
		/** The runok. */
		private boolean runok=false;
		
		/** The results. */
		private BiclusterList results;
		
		/** The cmds. */
		protected CommandsProcessList cmds;
		
		/** The expressiondata. */
		protected ExpressionData expressiondata;
		
		/** The parentdir. */
		protected String parentdir=null;
		
		/** The p. */
		private Process p=null;
		
		/**
		 * Instantiates a new debi thread.
		 *
		 * @param expressiondata the expressiondata
		 * @param executablepath the executablepath
		 * @param inputdatafile the inputdatafile
		 * @param outputdatafile the outputdatafile
		 * @param pattern the pattern
		 * @param maxoverlap the maxoverlap
		 * @param binlevel the binlevel
		 * @param mincondsbic the mincondsbic
		 * @param binary the binary
		 */
		public DebiThread(ExpressionData expressiondata,String executablepath, String inputdatafile, String outputdatafile, RegulationPattern pattern, double maxoverlap, double binlevel, int mincondsbic, boolean binary){
			this.executablepath=executablepath;
			inputdata=inputdatafile;
			setOutputFileNames(outputdatafile,pattern);
            this.maxoverlap=maxoverlap;
            this.binlevel=binlevel;
            this.minconds=mincondsbic;
            this.isbinarydata=binary;
            this.pattern=pattern;
            this.expressiondata=expressiondata;
		}
		
		
		/**
		 * Sets the output file names.
		 *
		 * @param outputname the outputname
		 * @param pattern the pattern
		 */
		private void setOutputFileNames(String outputname, RegulationPattern pattern){
			String path=FilenameUtils.getFullPath(outputname);
			String basename=FilenameUtils.getBaseName(outputname);
			String ext=FilenameUtils.getExtension(outputname);
			String newfilename=basename+"_"+pattern.getName()+"."+ext;
			outputfile=FilenameUtils.concat(path, newfilename);
			
			parentdir=FilenameUtils.getFullPathNoEndSeparator(outputname);
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			try {
				preconfigurealgorithm();
				
				ProcessBuilder build= new ProcessBuilder(cmds);
				//build.directory(new File(tempfolder));
				//long startTime = System.currentTimeMillis();
				//final Process p =build.start();
				p =build.start();
				InputStream inputstr =p.getInputStream();
				ReusableInputStream errorstr =new ReusableInputStream(p.getErrorStream());
				
				
				Thread stdout=new Thread(new GeneralProcessProgressionChecker(inputstr));
				stdout.run();
				
				//System.out.println("Parent: "+parentdir);
				/*Runtime.getRuntime().addShutdownHook(new Thread() {
					  @Override
					  public void run() {
					    p.destroy();
					    System.out.println("Stop processs");
					    SystemFolderTools.deleteTempDir(parentdir);
					  }
					});*/

				int exitval=p.waitFor();
				if(exitval==0){
					try {
						processResults();
					} catch (Exception e) {
						e.printStackTrace();
						
						LogMessageCenter.getLogger().addCriticalErrorMessage("Error in processing of DeBi results: "+e.getMessage());
						runok=false;
					}
					runok=true;
				}
				else{
					String out =IOUtils.toString(errorstr);
					LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of DeBi: "+out);
					runok=false;
				}
				
				
				
			} catch (Exception e) {
				LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of DeBi: "+e.getMessage());
				runok=false;
			}
			
			
		}
		
		
		/**
		 * Kill.
		 */
		public void kill(){
			p.destroy();
			SystemFolderTools.deleteTempDir(parentdir);
		}
		
		/**
		 * Preconfigurealgorithm.
		 *
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		protected void preconfigurealgorithm() throws IOException{
			
			cmds=new CommandsProcessList();
			//cmds.add(getBinaryExecutablePath());
			cmds.add(executablepath);
			cmds.add(inputdata);
			cmds.add(outputfile);
			if(!isbinarydata){
				cmds.add("-b"+String.valueOf(binlevel));
			}
			if(minconds!=0 && minconds>0){
				cmds.add("-s"+String.valueOf(minconds));
			}
			if(maxoverlap>0 && maxoverlap<=1.0){
				cmds.add("-o"+String.valueOf(maxoverlap));
			}
			else
				cmds.add("-o"+String.valueOf(0.5));
            
			if(pattern.equals(RegulationPattern.UP) || pattern.equals(RegulationPattern.DOWN)){
				cmds.add("-p"+pattern.getShortForm());
			}
			else
				cmds.add("-p"+RegulationPattern.UP.getShortForm());
			
			LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs DeBi: "+cmds);
		}
		
		
		/**
		 * Process results.
		 *
		 * @throws Exception the exception
		 */
		protected void processResults() throws Exception{
			ArrayList<String> lines=(ArrayList<String>) MTUReadUtils.readFileLines(outputfile);
			results=new BiclusterList();
			ArrayList<String> bicgenes=new ArrayList<>();
			ArrayList<String> bicconds=new ArrayList<>();
			String auxinfo=null;
			int bicstart=0;
			for (int i = 0; i < lines.size(); i++) {;
				if(bicstart==0){
					auxinfo=lines.get(i);
				}
				else if(bicstart==1){
					String[] elems=lines.get(i).split(" ");
					for (int j = 0; j < elems.length; j++) {
						bicgenes.add(elems[j]);
					}
				}
				else{
					String[] elems=lines.get(i).split(" ");
					for (int j = 0; j < elems.length; j++) {
						bicconds.add(elems[j]);
					}
					
					
					//BiclusterResult res=new BiclusterResult(expressiondata, bicgenes, bicconds);
					BiclusterResult res=new BiclusterResult(expressiondata,true, bicgenes, bicconds);
					res.appendAdditionalInfo("Regulation Pattern", pattern.getName());
					
					if(auxinfo!=null){
						String[]extrainfo=auxinfo.split(" ");
						res.appendAdditionalInfo("Score", extrainfo[2]);
						res.appendAdditionalInfo("Normalized score", extrainfo[3]);
					}
					
					results.add(res);
					bicgenes=new ArrayList<>();
					bicconds=new ArrayList<>();
					auxinfo=null;
					bicstart=-1;
				}
				
				bicstart++;
			}
		}
		
		
		/**
		 * Gets the results.
		 *
		 * @return the results
		 */
		public BiclusterList getResults(){
			return results;
		}
		
		/**
		 * Gets the state.
		 *
		 * @return the state
		 */
		public boolean getState(){
			return runok;
		}
		
	}
	
	
	/**
	 * The Class KillDebi.
	 */
	private class KillDebi extends Thread{
		
		/** The t. */
		DebiThread t=null;
		
		/**
		 * Instantiates a new kill debi.
		 *
		 * @param t the t
		 */
		public KillDebi(DebiThread t) {
			this.t=t;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			t.kill();
		}
		

	}

}
