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
package jbiclustge.methods.algorithms.wrappers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jbiclustge.analysis.overlap.OverlapAnalyser;
import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;
import jbiclustge.datatools.utils.nd4j.Nd4jExtended;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class CPBMethod.
 */
public class CPBMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	/** The Constant NAME. */
	public static final String NAME="CPB";
	
	/** The Constant AUXILIARCLUSTERINGBINARY. */
	public static final String AUXILIARCLUSTERINGBINARY="cpb_init_bicluster";
	
	/** The Constant NUMBERBICLUSTERS. */
	public static final String NUMBERBICLUSTERS="number_biclusters_to_seed";
	
	/** The Constant ROWTOCOLUMNRATION. */
	public static final String ROWTOCOLUMNRATION="row_to_column_ratio";
	
	/** The Constant PCCTHRESHOLD. */
	public static final String PCCTHRESHOLD="pcc_threshold";
	
	/** The Constant MINROWSINSEED. */
	public static final String MINROWSINSEED="min_rows_seed";
	
	/** The Constant MAXROWSINSEED. */
	public static final String MAXROWSINSEED="max_rows_seed";
	
	/** The Constant REQUIREDROWSINSEED. */
	public static final String REQUIREDROWSINSEED="required_row_in_the_seed";
	
	/** The Constant REQUIREDCOLUMNSINSEED. */
	public static final String REQUIREDCOLUMNSINSEED="required_columns_in_the_seed";
	
	/** The Constant MAXOVERLAPRATIO. */
	public static final String MAXOVERLAPRATIO="max_overlap_ratio";
	
	/** The Constant RANDOMINIT. */
	public static final String RANDOMINIT="use_random_initialization";
	
	/*
	 * Parameters
	 */

	/** The numberbiclusttoseed. */
	protected int numberbiclusttoseed=100;
	
	/** The rowtocolratio. */
	protected double rowtocolratio=1.0;
	
	/** The minrowseed. */
	protected int minrowseed=2;
	
	/** The maxrowseed. */
	protected int maxrowseed=-1;
	
	/** The requiredrowseed. */
	protected int requiredrowseed=-1; // fixed_row: The row that is required to be in the seed. -1 for random choice
	
	/** The requiredcolumnseed. */
	protected int requiredcolumnseed=-1;
	
	/** The maxoverlapratio. */
	protected double maxoverlapratio=0.75; // target PCC
	
	/** The pcc. */
	protected double pcc=0.9;
	
	/** The randominitialization. */
	protected boolean randominitialization=false;
	
	/** The rowpartition. */
	protected Integer rowpartition=null;
	
	/** The columnpartition. */
	protected Integer columnpartition=null;
	
	/** The cmds. */
	protected ArrayList<String> cmds;
	
	/** The minrows. */
	protected int minrows=2;
	
	/** The mincolumns. */
	protected int mincolumns=2;
	
	/** The removesubsets. */
	protected boolean removesubsets=true;
	
	/** The initshuffle seeddata. */
	protected String initshuffleSeeddata=null;
	
	/** The pcbinitfile. */
	protected String pcbinitfile;
	
	/** The currentdatamatrixfilepath. */
	protected String currentdatamatrixfilepath;
	
	/** The mainworkingdir. */
	protected String mainworkingdir=null;
	
	/** The workingdir. */
	protected String workingdir;
	
	/** The starttime. */
	private Date starttime ;
	
	private Thread stop;
	
	
	/**
	 * Instantiates a new CPB method.
	 */
	public CPBMethod() {
		super();

	}

	/**
	 * Instantiates a new CPB method.
	 *
	 * @param exprs the exprs
	 */
	public CPBMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	/**
	 * Instantiates a new CPB method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public CPBMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
		
	}
	
	/**
	 * Instantiates a new CPB method.
	 *
	 * @param properties the properties
	 */
	public CPBMethod(Properties properties){
		super(properties);
	}

	/**
	 * Instantiates a new CPB method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public CPBMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	

	/**
	 * Instantiates a new CPB method.
	 *
	 * @param numberbiclusttoseed the numberbiclusttoseed
	 * @param rowtocolratio the rowtocolratio
	 * @param minrowseed the minrowseed
	 * @param maxrowseed the maxrowseed
	 * @param requiredrowseed the requiredrowseed
	 * @param requiredcolumnseed the requiredcolumnseed
	 * @param maxoverlapratio the maxoverlapratio
	 * @param pcc the pcc
	 * @param randominitialization the randominitialization
	 * @param rowpartition the rowpartition
	 * @param columnpartition the columnpartition
	 * @param minrows the minrows
	 * @param mincolumns the mincolumns
	 * @param removesubsets the removesubsets
	 */
	private CPBMethod(int numberbiclusttoseed, double rowtocolratio, int minrowseed, int maxrowseed,
			int requiredrowseed, int requiredcolumnseed, double maxoverlapratio, double pcc,
			boolean randominitialization, Integer rowpartition, Integer columnpartition, int minrows, int mincolumns,
			boolean removesubsets) {
		super();
		this.numberbiclusttoseed = numberbiclusttoseed;
		this.rowtocolratio = rowtocolratio;
		this.minrowseed = minrowseed;
		this.maxrowseed = maxrowseed;
		this.requiredrowseed = requiredrowseed;
		this.requiredcolumnseed = requiredcolumnseed;
		this.maxoverlapratio = maxoverlapratio;
		this.pcc = pcc;
		this.randominitialization = randominitialization;
		this.rowpartition = rowpartition;
		this.columnpartition = columnpartition;
		this.minrows = minrows;
		this.mincolumns = mincolumns;
		this.removesubsets = removesubsets;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}


	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return NAME.toLowerCase();
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryExecutablePath()
	 */
	@Override
	public String getBinaryExecutablePath() throws IOException {
		
		
		 String path=SystemFolderTools.getBinaryFilePathFromPropertiesFile(getBinaryName());
	        
		 if(path==null)
	        	path=SystemFolderTools.getMethodExePath(getBinaryName());
			return path;
	}
	
	/**
	 * Gets the auxiliar binary path.
	 *
	 * @return the auxiliar binary path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected String getAuxiliarBinaryPath() throws IOException{
		String path=SystemFolderTools.getBinaryFilePathFromPropertiesFile(AUXILIARCLUSTERINGBINARY);
        if(path==null)
        	path=SystemFolderTools.getMethodExePath(AUXILIARCLUSTERINGBINARY);

		return path;
	}
	

	
	/**
	 * Sets the number biclusters to seed.
	 *
	 * @param numberbiclusttofind the new number biclusters to seed
	 */
	public void setNumberBiclustersToSeed(int numberbiclusttofind) {
		if(numberbiclusttofind<1)
			throw new NumberIsTooSmallException(numberbiclusttofind, 1, false);
		else
		  this.numberbiclusttoseed = numberbiclusttofind;
	}
	
	/**
	 * Adds the number biclusters to seed.
	 *
	 * @param numberbiclusttofind the numberbiclusttofind
	 * @return the CPB method
	 */
	public CPBMethod addNumberBiclustersToSeed(int numberbiclusttofind) {
		setNumberBiclustersToSeed(numberbiclusttofind);
		return this;
	}

	/**
	 * Sets the minrowseed.
	 *
	 * @param minrowseed the new minrowseed
	 */
	public void setMinrowseed(int minrowseed) {
		this.minrowseed = minrowseed;
	}
	
	/**
	 * Adds the minrowseed.
	 *
	 * @param minrowseed the minrowseed
	 * @return the CPB method
	 */
	public CPBMethod addMinrowseed(int minrowseed) {
		this.minrowseed = minrowseed;
		return this;
	}

	/**
	 * Sets the max row seed.
	 *
	 * @param maxrowseed the new max row seed
	 */
	public void setMaxRowSeed(int maxrowseed) {
		this.maxrowseed = maxrowseed;
	}

	/**
	 * Adds the max row seed.
	 *
	 * @param maxrowseed the maxrowseed
	 * @return the CPB method
	 */
	public CPBMethod addMaxRowSeed(int maxrowseed) {
		this.maxrowseed = maxrowseed;
		return this;
	}

	
	/**
	 * Sets the required row seed.
	 *
	 * @param requiredrowseed the new required row seed
	 */
	public void setRequiredRowSeed(int requiredrowseed) {
		if(requiredrowseed<-1)
			throw new NumberIsTooSmallException(requiredrowseed, -1, false);
		else
		  this.requiredrowseed = requiredrowseed;
	}
	
	/**
	 * Adds the required row seed.
	 *
	 * @param requiredrowseed the requiredrowseed
	 * @return the CPB method
	 */
	public CPBMethod addRequiredRowSeed(int requiredrowseed) {
		setRequiredRowSeed(requiredrowseed);
		return this;
	}
	
	/**
	 * Sets the required column seed.
	 *
	 * @param requiredcolumnseed the new required column seed
	 */
	public void setRequiredColumnSeed(int requiredcolumnseed) {
		if(requiredcolumnseed<-1)
			throw new NumberIsTooSmallException(requiredcolumnseed, -1, false);
		else
		  this.requiredcolumnseed = requiredcolumnseed;
	}
	
	/**
	 * Adds the required column seed.
	 *
	 * @param requiredcolumnseed the requiredcolumnseed
	 * @return the CPB method
	 */
	public CPBMethod addRequiredColumnSeed(int requiredcolumnseed) {
		setRequiredColumnSeed(requiredcolumnseed);
		return this;
	}

	/**
	 * Sets the maxoverlapratio.
	 *
	 * @param maxoverlapratio the new maxoverlapratio
	 */
	public void setMaxoverlapratio(double maxoverlapratio) {
		this.maxoverlapratio = maxoverlapratio;
	}
	
	/**
	 * Adds the maxoverlapratio.
	 *
	 * @param maxoverlapratio the maxoverlapratio
	 * @return the CPB method
	 */
	public CPBMethod addMaxoverlapratio(double maxoverlapratio) {
		this.maxoverlapratio = maxoverlapratio;
		return this;
	}

	/**
	 * Sets the randominitialization.
	 *
	 * @param randominitialization the new randominitialization
	 */
	public void setRandominitialization(boolean randominitialization) {
		this.randominitialization = randominitialization;
	}

	
	/**
	 * Adds the randominitialization.
	 *
	 * @param randominitialization the randominitialization
	 * @return the CPB method
	 */
	public CPBMethod addRandominitialization(boolean randominitialization) {
		this.randominitialization = randominitialization;
		return this;
	}
	
    /**
     * Sets the rowto column ratio.
     *
     * @param rowtocolratio the new rowto column ratio
     */
    public void setRowtoColumnRatio(double rowtocolratio) {
		this.rowtocolratio = rowtocolratio;
	}
    
    /**
     * Sets the pcc.
     *
     * @param value the new pcc
     */
    public void setPCC(double value){
    	this.pcc=value;
    }
    
    /**
     * Adds the PCC.
     *
     * @param value the value
     * @return the CPB method
     */
    public CPBMethod addPCC(double value){
    	setPCC(value);
    	return this;
    }
    
    /* (non-Javadoc)
     * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
     */
    @Override
	protected String getRunningTime() {
		return runningtime;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {

			starttime =Calendar.getInstance().getTime();
		    initConfig();
		    initPCBSeedData(initshuffleSeeddata);
		    initPCBSeedData(pcbinitfile);
			minrows=findminbic();
			
			configureInputPCBMainRun();
		
			return runProcess();
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		BiclusterList biclusterstofilter=filterBiclusterFileResults();
		filterBiclusters(biclusterstofilter);
		SystemFolderTools.deleteTempDir(mainworkingdir);
		
	}
	
	
	
    /**
     * Run process.
     *
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    protected boolean runProcess() throws IOException, InterruptedException{
    	
 
    	ProcessBuilder build= new ProcessBuilder(cmds);
    	build.directory(new File(mainworkingdir));
    	

		final Process p =build.start();
   
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			  @Override
			  public void run() {
			    p.destroy();
			    SystemFolderTools.deleteTempDir(mainworkingdir);
			  }
			});
		
		stop=new Thread(new Runnable() {
			
			@Override
			public void run() {
				 p.destroy();
				 SystemFolderTools.deleteTempDir(mainworkingdir);
				 LogMessageCenter.getLogger().addInfoMessage(NAME+" was forced to stop");
				
			}
		});

		
		int exitval=p.waitFor();
		
		if(exitval==0){
			Date endtime=Calendar.getInstance().getTime();
			long runtime=endtime.getTime()-starttime.getTime();	
			runningtime=MTUTimeUtils.getTimeElapsed(runtime);
			return true;
		}
		else{
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+", please change the input parameters and try again.");
			return false;
		}
    }
	
    /**
     * Inits the config.
     */
    private void initConfig(){
    	mainworkingdir=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("CPB");
    	initshuffleSeeddata=FilenameUtils.concat(mainworkingdir, MTUStringUtils.shortUUID()+".txt");
    	pcbinitfile=FilenameUtils.concat(mainworkingdir, MTUStringUtils.shortUUID()+".txt");

    }
	
	
	/**
	 * Inits the PCB seed data.
	 *
	 * @param writeto the writeto
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	protected void initPCBSeedData(String writeto) throws IOException, InterruptedException{
	
		ArrayList<String> initcmds=new ArrayList<>();

		initcmds.add(getAuxiliarBinaryPath());
		
		initcmds.add(String.valueOf(expressionset.numberGenes()));
		initcmds.add(String.valueOf(expressionset.numberConditions()));
		initcmds.add(String.valueOf(numberbiclusttoseed));
		
		if(randominitialization){
			if(maxrowseed==-1)
				maxrowseed=expressionset.numberGenes();
			else if(maxrowseed<minrowseed){
				LogMessageCenter.getLogger().toClass(getClass()).addErrorMessage(MAXROWSINSEED+" cannot be lower than "+MINROWSINSEED+", changing to the number of genes: "+expressionset.numberGenes());
				maxrowseed=expressionset.numberGenes();
			}
			
			initcmds.add(String.valueOf(minrowseed));
			initcmds.add(String.valueOf(maxrowseed));
			initcmds.add(String.valueOf(requiredrowseed));
			initcmds.add(String.valueOf(requiredcolumnseed));
			initcmds.add(writeto);
			LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Initial input CMDs "+getAlgorithmName()+": "+initcmds);
			runInitBiclustersBinary(initcmds);
			
		}
		else{
			initGridBiclusters(writeto);
		}
		
		LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Init cmds: "+initcmds);
	}
	
	/**
	 * Run init biclusters binary.
	 *
	 * @param cmds the cmds
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	protected boolean runInitBiclustersBinary(ArrayList<String> cmds) throws IOException, InterruptedException{
		
	
		ProcessBuilder build= new ProcessBuilder(cmds);

		final Process p =build.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			  @Override
			  public void run() {
			    p.destroy();
			    SystemFolderTools.deleteTempDir(mainworkingdir);
			  }
			});
		
		
		int exitval=p.waitFor();
		if(exitval!=0){
			LogMessageCenter.getLogger().addCriticalErrorMessage(AUXILIARCLUSTERINGBINARY+" was unable to produce results, the process cannot be executed. Please check your input parameters");
			return false;
		}
		else
			return true;
		
	}
	
	/**
	 * Inits the grid biclusters.
	 *
	 * @param writeto the writeto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void initGridBiclusters(String writeto) throws IOException{
		
		if(rowpartition==null)
			rowpartition=10;
		if(columnpartition==null)
			columnpartition=2;
		
		int gridrows=(int)(expressionset.numberGenes()/rowpartition);
		int gridcolumns=(int)(expressionset.numberConditions()/columnpartition);
		
		int bicnoinpass=rowpartition*columnpartition;
		int numbicvsbicnoinpass=(int)(numberbiclusttoseed/bicnoinpass);
		
		INDArray rowindexes=Nd4jExtended.createIntegerVectorRange(expressionset.numberGenes());
		INDArray columnindexes=Nd4jExtended.createIntegerVectorRange(expressionset.numberConditions());
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(writeto,true));
		
		for (int i = 0; i < numbicvsbicnoinpass; i++) {
			
			INDArray sufflerows=Nd4jExtended.shuffleVector(rowindexes);
			INDArray sufflecolumns=Nd4jExtended.shuffleVector(columnindexes);
			
			for (int j = 0; j < rowpartition; j++) {
				
				int rowstart=j*gridrows;
				int rowend=0;
				if(j==(rowpartition-1))
					rowend=-1;
				else
					rowend=(j+1)*gridrows;
				
				for (int k = 0; k < columnpartition; k++) {
					
					int colstart=k*gridcolumns;
					int colend=0;
					if(j==(columnpartition-1))
						colend=-1;
					else
						colend=(k+1)*gridcolumns;
					
					int nbiclust=i*bicnoinpass+j*columnpartition+k;
					bw.write("BICLUSTER "+nbiclust+"\n");
					bw.write(String.valueOf(requiredrowseed)+"\n");
					for (int l = rowstart; l < rowend; l++) {
						int rowval=sufflerows.getInt(l);
						bw.write(String.valueOf(rowval));
						if(l<(rowend-1))
							bw.write(" ");
					}
					bw.write("\n");
					
					bw.write(String.valueOf(requiredcolumnseed)+"\n");
					for (int m = colstart; m < colend; m++) {
						int colval=sufflecolumns.getInt(m);
						
						bw.write(String.valueOf(colval));
						if(m<(colend-1))
							bw.write(" ");
						
					}
					bw.write("\n");
					
				}
			}
		}
		bw.close();
		
	}
	
	
	/**
	 * Findminbic.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	protected int findminbic() throws IOException, InterruptedException{
		LogMessageCenter.getLogger().addInfoMessage("Finding minimal rows, this may take a while, please wait...");
		
		int minbic=2;
		configureShuffleMatrix();
		preConfigurePCBAlgorithmInputParameters(initshuffleSeeddata);
		
		boolean success=runProcess();

		if(success){
			BiclusterList results=filterBiclusterFileResults();
			for (int i = 0; i < results.size(); i++) {
				BiclusterResult bic=results.get(i);
				if(bic.getNumberGenes()>minbic)
					minbic=bic.getNumberGenes();
			}
		}
		
		deleteTMPFiles();
		SystemFolderTools.deleteTempFile(currentdatamatrixfilepath);
		
		return minbic;
	}
	
	/**
	 * Delete TMP files.
	 */
	private void deleteTMPFiles(){
		
		ArrayList<String> allfiles=MTUDirUtils.getFilePathsInsideDirectory(mainworkingdir, true, false);
		
		for (int i = 0; i < allfiles.size(); i++) {
			String ext=FilenameUtils.getExtension(allfiles.get(i));
			if(ext.equals("out") || ext.equals("smx"))
				new File(allfiles.get(i)).delete();
		}
		
	}
	
	
	
	
	
	/**
	 * Configure shuffle matrix.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void configureShuffleMatrix() throws IOException{
		
		INDArray copydatamatrix=Nd4j.create(expressionset.numberGenes(), expressionset.numberConditions());
		Nd4j.copy(expressionset.getExpressionDataMatrix(),copydatamatrix);
		
		INDArray vector=copydatamatrix.reshape(expressionset.numberGenes()*expressionset.numberConditions(), 1);
		Nd4j.shuffle(vector, 1);
		INDArray shufflematrix=vector.reshape(expressionset.numberGenes(),expressionset.numberConditions());
		
		String filename=MTUStringUtils.shortUUID();
		currentdatamatrixfilepath=FilenameUtils.concat(mainworkingdir, (filename+".txt"));
		
		String datashape=expressionset.numberGenes()+" "+expressionset.numberConditions();
		ArrayList<String> extra=new ArrayList<>();
		extra.add(datashape);
		String data=MTUStringUtils.convertDoubleMatrixToString(ND4JUtils.getDoubleMatrix(shufflematrix), " ", extra);
		MTUWriterUtils.writeStringWithFileChannel(data, currentdatamatrixfilepath, 0);
		
	}


	
	/**
	 * Configure input PCB main run.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void configureInputPCBMainRun() throws IOException{
		String filename=MTUStringUtils.shortUUID();
	
		currentdatamatrixfilepath=FilenameUtils.concat(mainworkingdir, (filename+".txt"));
		String datashape=expressionset.numberGenes()+" "+expressionset.numberConditions();
		ArrayList<String> extra=new ArrayList<>();
		extra.add(datashape);
		expressionset.writeExpressionMatrixToFile(currentdatamatrixfilepath," ", extra);
		
		preConfigurePCBAlgorithmInputParameters(pcbinitfile);
	}
	
	/**
	 * Pre configure PCB algorithm input parameters.
	 *
	 * @param workwithfile the workwithfile
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void preConfigurePCBAlgorithmInputParameters(String workwithfile) throws IOException{
		cmds=new ArrayList<>();
		cmds.add(getBinaryExecutablePath());
		cmds.add(currentdatamatrixfilepath);
		cmds.add(workwithfile);
		cmds.add(String.valueOf(rowtocolratio));
		cmds.add(String.valueOf(pcc));
		LogMessageCenter.getLogger().toClass(getClass()).addTraceMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	/**
	 * Filter bicluster out files.
	 *
	 * @return the array list
	 */
	protected ArrayList<String> filterBiclusterOutFiles(){
		ArrayList<String> tempres=new ArrayList<>();
		File dir=new File(mainworkingdir);
		File[] listoffiles=dir.listFiles();
		for (File file : listoffiles) {
			String ext=FilenameUtils.getExtension(file.getAbsolutePath());
			if(file.isFile() && ext.equals("out"))
				tempres.add(file.getAbsolutePath());
		}
		
		ArrayList<String> res=new ArrayList<>();
		
		for (int i = 0; i < tempres.size(); i++) {
			String tagname=".ran"+i+".";
			
			for (String linename : tempres) {
				if(linename.contains(tagname))
					res.add(linename);
			}
		}
		return res;
	}
	
	
	/**
	 * Filter bicluster file results.
	 *
	 * @return the bicluster list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected BiclusterList filterBiclusterFileResults() throws IOException{
		ArrayList<String> listresults=filterBiclusterOutFiles();
		
		BiclusterList res=new BiclusterList();
		
		for (int i = 0; i < listresults.size(); i++) {
			BiclusterResult bic=parseBicluster(listresults.get(i));
			if(bic!=null)
				res.add(bic);
		}
		
		return res;
	}
	
	/**
	 * Parses the bicluster.
	 *
	 * @param filepath the filepath
	 * @return the bicluster result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private BiclusterResult parseBicluster(String filepath) throws IOException{
		
		ArrayList<String> lines=(ArrayList<String>) MTUReadUtils.readFileLines(filepath);
		ArrayList<Integer> rowindexes=new ArrayList<>();
		ArrayList<Integer> colindexes=new ArrayList<>();
		String currenttag=null;
		
		for (int i = 0; i < lines.size(); i++) {
			String line=lines.get(i);
			if(line.contains("ROWS"))
				currenttag="R";
			else if(line.contains("COLS"))
				currenttag="C";
			else{
				if(!line.isEmpty()){
				  String[] elems=line.split("\\s+");
				  int index=Integer.parseInt(elems[0].trim());
				  String value=elems[1];
				 
				  //if(!value.contains("nan")){
				  if(value.contains("nan"))
					  return null;
				  else{
					  if(currenttag.equals("R"))
						  rowindexes.add(index);
					  else
						  colindexes.add(index);
				  }
				}
				
			}
		}
		
		Collections.sort(rowindexes);
		Collections.sort(colindexes);
		
		if(rowindexes.size()>0 && colindexes.size()>0)
			return new BiclusterResult(expressionset, rowindexes, colindexes,true);
		return null;
	}
	
	/**
	 * Filter biclusters.
	 *
	 * @param biclusterstofilter the biclusterstofilter
	 * @throws Exception the exception
	 */
	protected void filterBiclusters(BiclusterList biclusterstofilter) throws Exception{
		this.listofbiclusters=new BiclusterList();
		
		
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Processing Results...");
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Total Biclusters: "+biclusterstofilter.size());
        
		BiclusterList filtersize=filterBySize(biclusterstofilter);
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Number of biclusters after size reduction: "+filtersize.size());
		
		filtersize.setUsedmethod(getAlgorithmName());
		BiclusterList filteredoverlap=OverlapAnalyser.filterBiclusterListWithOverlapThreshold(filtersize, maxoverlapratio, filtersize.size());
		listofbiclusters=filteredoverlap;
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Number of biclusters after checking the overlap between them : "+filteredoverlap.size());
		/*BiclusterList sortarea=sortByArea(filtersize);
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Number of biclusters after sort by area: "+sortarea.size());
		
		
		BiclusterList filteredoverlap=filterbyoverlaping(sortarea);
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Number of biclusters after checking the overlap between clusters : "+filteredoverlap.size());
	
		if(filteredoverlap.size()>0){
			listofbiclusters.addAll(filteredoverlap);
		}*/
		
		
	}
	
	/**
	 * Filter by size.
	 *
	 * @param list the list
	 * @return the bicluster list
	 */
	protected BiclusterList filterBySize(BiclusterList list){
		
		BiclusterList filteredsize=new BiclusterList();
		
		//System.out.println("LIST BEFORE SIZE REDUCTION: "+list.size());
		
		for (int i = 0; i < list.size(); i++) {
			BiclusterResult result=list.get(i);
			
			/*System.out.println("MINGenes: "+minrows+"  Genes: "+result.getNumberGenes()+" Min Conds: "+mincolumns+"  Conditions: "+result.getNumberConditions());
			if(result.getNumberGenes()>minrows && result.getNumberConditions()>mincolumns){
				
				filteredsize.add(result);
				
			}*/
			
			if(result.getNumberGenes()>minrows && 
			   result.getNumberGenes()<result.getExpressionDataset().numberGenes() &&
			   result.getNumberConditions()>mincolumns &&
			   result.getNumberConditions()<result.getExpressionDataset().numberConditions())
			   
			   filteredsize.add(result);
			  
			   
				
				
		}
		
		return filteredsize;
	}
	
	
/*	protected BiclusterList sortByArea(BiclusterList list){
		
		INDArray areas=Nd4j.create(list.size());
		
		for (int i = 0; i < list.size(); i++) {
			areas.putScalar(i, list.get(i).area());
		}
		
		INDArray[] sortedareas=Nd4j.sortWithIndices(areas, 1, false);
		
		INDArray sortedindexes=sortedareas[0];
		
		BiclusterList sortedlist=new BiclusterList();
		for (int i = 0; i < sortedindexes.length(); i++) {
			int pos=sortedindexes.getInt(i);
			sortedlist.add(list.get(pos));
		}
		
		return sortedlist;
		
	}*/
	
/*	protected BiclusterList filterbyoverlaping(BiclusterList listbiclust) throws IOException{
		
		BiclusterList accepted=new BiclusterList();
		
		for (int i = 0; i < listbiclust.size(); i++) {
			
			BiclusterResult currentresult=listbiclust.get(i);
			
			if(accepted.size()<1){
					accepted.add(currentresult);
			}
			else{
			   for (int j = 0; j < accepted.size(); j++) {
			        BiclusterResult compareto=accepted.get(j);
					if(addCluster(currentresult, compareto) && !accepted.contains(currentresult))
						accepted.add(currentresult); 
			   }
			}
			
		}
		
		return accepted;
		
	}*/
	
	/*protected boolean addCluster(BiclusterResult toadd, BiclusterResult comparewith) throws IOException{
		
		boolean add=true;
		
		if(BiclusteringUtils.equalBiclusterResult(comparewith, toadd))
			add=false;
		
		else if(BiclusteringUtils.overlapratio(toadd, comparewith) > maxoverlapratio)
			add=false;
		
		else if(removesubsets && BiclusteringUtils.isSubsetOfBiclusterResult(comparewith, toadd))
			add =false;
		

		return add;
	}*/
	
	/*protected void deleteTempFiles() throws IOException{
		FileUtils.deleteDirectory(new File(workingdir));
		SystemFolderTools.deleteTempFile(initfilepath);
		SystemFolderTools.deleteTempFile(datafilepath);
	}*/

	/* (non-Javadoc)
 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
 */
@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				NUMBERBICLUSTERS,
				PCCTHRESHOLD,
				MINROWSINSEED,
				MAXROWSINSEED,
				REQUIREDROWSINSEED,
				REQUIREDCOLUMNSINSEED,
				MAXOVERLAPRATIO,
				RANDOMINIT,
				ROWTOCOLUMNRATION
		};
		
		String[] defaultvalues=new String[]{"100","0.9","2","-1","-1","-1","0.75","false","1"};
		
		String[] comments=new String[] {
				"Number of biclusters that cpb will be seeded",
				"PCC threshold for each row",
				"Min rows in the seed",
				"Max rows in the seed",
				"Required row in the seed. -1 for random choice(reference row)",
				"Required columns in the seed. -1 for random choice",
				"Max overlap ratio. Between [0-1]",
				"Use grid initialization (false) or use random initialization (true)",
				"Row to column ratio"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments);
	}
	
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		numberbiclusttoseed=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, NUMBERBICLUSTERS, 100, 2, true, this.getClass());
		minrowseed=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MINROWSINSEED, 2, 1, true, this.getClass());
		maxrowseed=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, MAXROWSINSEED, -1, -1, true, this.getClass());
		requiredrowseed=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, REQUIREDROWSINSEED, -1, -1, true, this.getClass());
		requiredcolumnseed=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, REQUIREDCOLUMNSINSEED, -1, -1, true, this.getClass());
		maxoverlapratio=PropertiesUtilities.getDoublePropertyValueValidLimits(props, MAXOVERLAPRATIO, 0.75, 0, 1.0, true, this.getClass());
		pcc=PropertiesUtilities.getDoublePropertyValueValidLimits(props,PCCTHRESHOLD, 0.95, 0, 1.0, true, this.getClass());
		randominitialization=PropertiesUtilities.getBooleanPropertyValue(props, RANDOMINIT, false, this.getClass());
		rowtocolratio=PropertiesUtilities.getDoublePropertyValueValidLimits(props, ROWTOCOLUMNRATION, 1.0, 0, 1.0, true, this.getClass());
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
	    return mainworkingdir;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(NUMBERBICLUSTERS, numberbiclusttoseed);
		parameters.setKeyintValue(MINROWSINSEED, minrowseed);
		parameters.setKeyintValue(MAXROWSINSEED, maxrowseed);
		parameters.setKeyintValue(REQUIREDROWSINSEED, requiredrowseed);
		parameters.setKeyintValue(REQUIREDCOLUMNSINSEED, requiredcolumnseed);
		parameters.setKeydoubleValue(MAXOVERLAPRATIO, maxoverlapratio);
		parameters.setKeydoubleValue(PCCTHRESHOLD, pcc);
		parameters.setKeybooleanValue(RANDOMINIT, randominitialization);
		parameters.setKeydoubleValue(ROWTOCOLUMNRATION, rowtocolratio);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new CPBMethod(numberbiclusttoseed, rowtocolratio, minrowseed, maxrowseed, requiredrowseed, requiredcolumnseed, maxoverlapratio, pcc, randominitialization, rowpartition, columnpartition, minrows, mincolumns, removesubsets);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportDefineNumberBiclustersToFind()
	 */
	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		return false;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#changeNumberBiclusterToFind(int)
	 */
	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return true;
	}

	@Override
	public void stopProcess() {
		stop.start();
		
	}
	
}
