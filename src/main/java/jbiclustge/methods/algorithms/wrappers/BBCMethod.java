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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.wrappers.components.BBCNormalizationMethod;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.properties.AlgorithmProperties;
import jbiclustge.utils.properties.CommandsProcessList;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;
import pt.ornrocha.systemutils.OSystemUtils;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class BBCMethod.
 */
public class BBCMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	
	
	/** The Constant NAME. */
	public static final String NAME="BBC";
	
	
	/** The Constant NUMBERBICLUSTERS. */
	public static final String NUMBERBICLUSTERS="Number_biclusters_to_be_found";
	
	/** The Constant NORMALIZATIONMETHOD. */
	public static final String NORMALIZATIONMETHOD="Normalization_method";
	
	/** The Constant NORMALIZATIONALPHAVALUE. */
	public static final String NORMALIZATIONALPHAVALUE="Normalization_alpha_value";
	
	/** The nbics. */
	private int nbics=2;
	
	/** The normmethod. */
	private BBCNormalizationMethod normmethod=BBCNormalizationMethod.IQRN;
	
	/** The normalpha. */
	private double normalpha=90;
	
	
	/** The tempfolder. */
	private String tempfolder=null;
	
	/** The cmds. */
	private CommandsProcessList cmds;
	
	/** The datafilepath. */
	private String datafilepath=null;
	
	/** The outputfilepath. */
	private String outputfilepath=null;
	
	private Thread stop;
	
	/**
	 * Instantiates a new BBC method.
	 */
	public BBCMethod() {
		super();

	}

	/**
	 * Instantiates a new BBC method.
	 *
	 * @param exprs the exprs
	 */
	public BBCMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	/**
	 * Instantiates a new BBC method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public BBCMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
		
	}
	
	/**
	 * Instantiates a new BBC method.
	 *
	 * @param properties the properties
	 */
	public BBCMethod(Properties properties){
		super(properties);
	}

	/**
	 * Instantiates a new BBC method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public BBCMethod(String propertiesfile){
		super(propertiesfile);
	}
	

	/**
	 * Instantiates a new BBC method.
	 *
	 * @param nbics the nbics
	 * @param normmethod the normmethod
	 * @param normalpha the normalpha
	 */
	private BBCMethod(int nbics, BBCNormalizationMethod normmethod, double normalpha) {
		super();
		this.nbics = nbics;
		this.normmethod = normmethod;
		this.normalpha = normalpha;
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}


	/**
	 * Sets the number biclusters to find.
	 *
	 * @param nbic the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int nbic){
		this.nbics=nbic;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportDefineNumberBiclustersToFind()
	 */
	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		return true;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#changeNumberBiclusterToFind(int)
	 */
	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		// bbc only runs if minimum of biclusters to find is higher than 1
		if(numberbics<2)
			numberbics=2;
		setNumberBiclustersToFind(numberbics);
	}
	
	
	/**
	 * Sets the normalization method.
	 *
	 * @param normmethod the new normalization method
	 */
	public void setNormalizationMethod(BBCNormalizationMethod normmethod){
	    this.normmethod=normmethod;
	}
	
	
	/**
	 * Must be a value between >5 and <=100.
	 *
	 * @param value the new normalization alpha
	 */
	public void setNormalizationAlpha(double value){
		this.normalpha=value;
	}
	
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{NUMBERBICLUSTERS,NORMALIZATIONMETHOD,NORMALIZATIONALPHAVALUE};
		String[] propdefaultvals=new String[]{"2",BBCNormalizationMethod.IQRN.getName(),"90"};
		String[] propcomments=new String[]{
				"Number of biclusters that user hopes to find in the datasets",
				"Normalization method to be used on the microarray data.\n"
				+ "none (No normalization will be done by the BBC program)\n"
				+ "csn (Column-wise standardization)\n"
				+ "rsn (Row-wise standardization)\n"
				+ "iqrn (Inter-quartile range normalization, alpha value for the normalization needs to be specified)\n"
				+ "sqrn (The smallest range quartile normalization, alpha value for the normalization needs to be specified)\n",
				"alpha value for the normalization method, needs to be set if iqrn or sqrn is used (value between (5,100].\n"
				+ "alpha value means the normalization is done based on the alpha% quartile of data in each column"
			};
		
		
		
		String source="http://www.people.fas.harvard.edu/~junliu/BBC/BBC_manual.pdf";
		return AlgorithmProperties.setupProperties(propkeys, propdefaultvals,propcomments,source);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return "BBC";
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


	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		nbics=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, NUMBERBICLUSTERS, 2, 2, true, this.getClass());
		String normalizationmethod=PropertiesUtilities.getStringPropertyValue(props, NORMALIZATIONMETHOD, "iqrn", getClass());
		normmethod=BBCNormalizationMethod.getBBCNormalizationMethodFromString(normalizationmethod);
		
		String alphavalue=PropertiesUtilities.getStringPropertyValue(props, NORMALIZATIONALPHAVALUE, null, getClass());
		if(normmethod.equals(BBCNormalizationMethod.IQRN) || normmethod.equals(BBCNormalizationMethod.SQRN)){
			if(alphavalue==null)
				normalpha=90.0;
			else{
				try {
					normalpha=Double.parseDouble(alphavalue);
				} catch (Exception e) {
					normalpha=90.0;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@SuppressWarnings("resource")
	@Override
	protected boolean runAlgorithm() throws Exception {
		preconfigurealgorithm();
		
		ProcessBuilder build= new ProcessBuilder(cmds);
		build.directory(new File(tempfolder));
		Date starttime =Calendar.getInstance().getTime();
		final Process p =build.start();
		InputStream inputstr =p.getInputStream();
		ReusableInputStream errorstr =new ReusableInputStream(p.getErrorStream());
		
		
		Thread stdout=new Thread(new GeneralProcessProgressionChecker(inputstr));
		stdout.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			  @Override
			  public void run() {
			    p.destroy();
			    SystemFolderTools.deleteTempDir(tempfolder);
			  }
			});
		
		stop=new Thread(new Runnable() {
			
			@Override
			public void run() {
				 p.destroy();
				 SystemFolderTools.deleteTempDir(tempfolder);
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
			String out =IOUtils.toString(errorstr);
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": "+out);
			return false;
		}

	
	}
	
	/**
	 * Preconfigurealgorithm.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void preconfigurealgorithm() throws IOException{
		configureInputData();
		cmds=new CommandsProcessList();
		cmds.add(getBinaryExecutablePath());
		cmds.insertStringParameter("-i", datafilepath);
		cmds.insertIntegerParameter("-k", nbics);
		cmds.insertStringParameter("-o", outputfilepath);
		cmds.insertStringParameter("-n", normmethod.getName());
		
		if(normmethod.equals(BBCNormalizationMethod.IQRN) || normmethod.equals(BBCNormalizationMethod.SQRN)){
			if(normalpha>5 && normalpha<=100)
			  cmds.insertDoubleParameter("-r", normalpha);
		}
		
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	
	/**
	 * Configure input data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void configureInputData() throws IOException{
		tempfolder=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("BBC");
		datafilepath=FilenameUtils.concat(tempfolder, MTUStringUtils.shortUUID()+".txt");
		expressionset.writeExpressionDatasetToFile(datafilepath);
		outputfilepath=FilenameUtils.concat(tempfolder, "bbc_results.txt");
	}
	
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		
		listofbiclusters=new BiclusterList();
		ArrayList<String> lines=MTUReadUtils.readFileLinesRemoveEmptyLines(outputfilepath);
		
		String lineinfo=lines.get(0);
		Pattern pat=Pattern.compile("K=(\\d+)\\s+Number of stable clusters:\\s+(\\d+)\\s+likelihood:\\s+((-)*\\d+(.\\d+)*)\\s+Number of parameters:\\s+(\\d+(.\\d+)*)\\s+BIC:\\s+(\\d+(.\\d+))");
		Matcher m=pat.matcher(lineinfo);
		if(m.find()){
            try {
            	listofbiclusters.addAditionalBiclusterMethodInformation("K",m.group(1));
            	listofbiclusters.addAditionalBiclusterMethodInformation("Number of stable clusters", m.group(2));
            	listofbiclusters.addAditionalBiclusterMethodInformation("likelihood", m.group(3));
            	listofbiclusters.addAditionalBiclusterMethodInformation("Number of parameters",m.group(6));
            	listofbiclusters.addAditionalBiclusterMethodInformation("BIC", m.group(8));
            	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
	     }
		
		if(lines.size()>1){
			
			boolean started=false;
			int tag=0;
			ArrayList<String> genes=null;
			ArrayList<String> geneeffects=null;
			ArrayList<String> conds=null;
			ArrayList<String> condeffects=null;
			boolean save=false;
			String bicmaineffect=null;
			
			Pattern tp=Pattern.compile("(\\d+)\\s+(.*)\\s+((-)*\\d+(.\\d+))");
			
			for (int i = 1; i < lines.size(); i++) {
				String line=lines.get(i);
				
				if(line.matches("row\\s+genes\\s+names\\s+gene\\s+effects$")){
					tag=1;
					genes=new ArrayList<>();
					geneeffects=new ArrayList<>();
				}
				else if(line.matches("col\\s+condition\\s+names\\s+condition\\s+effects$")){
					tag=2;
					conds=new ArrayList<>();
					condeffects=new ArrayList<>();
				}
				else if(line.matches("bicluster main effect:\\s+((-)*\\d+(.\\d+))")){
					
					Pattern bicefpat=Pattern.compile("bicluster main effect:\\s+((-)*\\d+(.\\d+))");
					Matcher bicefmatch=bicefpat.matcher(line);
					bicefmatch.find();
					bicmaineffect=bicefmatch.group(1);
					
					tag=-1;

				}
				else if(line.matches("bicluster(\\d+)$")){
					if(!started)
						started=true;
					else
						save=true;

					tag=0;
				}
				else if(i==lines.size()-1){
					save=true;
					tag=0;
				}
				else if(tag==1){
					Matcher mg=tp.matcher(line);
					if(mg.find()){
						String genename=mg.group(2);
						String geneeffect=genename+": "+mg.group(3);
						genes.add(genename);
						geneeffects.add(geneeffect);
	
					}
				}
				else if(tag==2){
					Matcher mc=tp.matcher(line);
					if(mc.find()){
						String condname=mc.group(2);
						String condeffect=condname+": "+mc.group(3);
						conds.add(condname);
						condeffects.add(condeffect);
					}
				}
				if(save && tag==0){
					if(genes!=null && genes.size()>0 && conds!=null && conds.size()>0){
						BiclusterResult res=new BiclusterResult(expressionset, genes, conds);
						if(bicmaineffect!=null)
							res.appendAdditionalInfo("bicluster main effect", bicmaineffect);
						if(geneeffects!=null)
							res.appendAdditionalInfo("Gene effects", geneeffects);
						if(condeffects!=null)
							res.appendAdditionalInfo("Condition effects", condeffects);
						
						listofbiclusters.add(res);

						tag=-1;
					}
				}

			}
			
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
	 */
	@Override
	protected String getRunningTime() {
		return runningtime;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		LinkedHashMap<String, String> parameters=new LinkedHashMap<>();
		parameters.put(NUMBERBICLUSTERS, String.valueOf(nbics));
		parameters.put(NORMALIZATIONMETHOD, normmethod.getName());
		if(normmethod.equals(BBCNormalizationMethod.IQRN) || normmethod.equals(BBCNormalizationMethod.SQRN))
			parameters.put(NORMALIZATIONALPHAVALUE, String.valueOf(normalpha));
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new BBCMethod(nbics, normmethod, normalpha);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return tempfolder;
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
