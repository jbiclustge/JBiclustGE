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

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.progress.COALESCEResultsFileWriter;
import jbiclustge.results.progress.COALESCEResultsProcessor;
import jbiclustge.utils.osystem.SystemFolderTools;
import jbiclustge.utils.props.AlgorithmProperties;
import jbiclustge.utils.props.CommandsProcessList;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.stringutils.MTUStringUtils;
import pt.ornrocha.stringutils.ReusableInputStream;
import pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker;
import pt.ornrocha.swingutils.progress.GeneralProcessProgressionChecker;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class COALESCEMethod.
 */
public class COALESCEMethod extends AbstractBiclusteringAlgorithmCaller implements IBiclustWrapper{

	/** The Constant NAME. */
	public static final String NAME="COALESCE";
	
	
	/** The resultprocessor. */
	private AbstractProcessProgressionChecker resultprocessor;
	
	/** The cmds. */
	protected CommandsProcessList cmds;
	
	/** The outputfolder. */
	private String outputfolder;
	
	/** The pclinputfilepath. */
	private String pclinputfilepath;
	

	/** The mapofprobidstonames. */
	private Map<String, String> mapofprobidstonames;
	
	/** The fastafilepath. */
	private String fastafilepath;
	
	/** The conditiongroupblocks. */
	private String conditiongroupblocks;
	
	/** The knownmotiffile. */
	private String knownmotiffile;
	
	/** The wigfilepaths. */
	private ArrayList<String> wigfilepaths;
	
	/** The probgene. */
	private double probgene=0.95;
	
	/** The pvalueconds. */
	private double pvalueconds=0.05;
	
	/** The pvaluemotif. */
	private double pvaluemotif=0.05;
	
	/** The zscorecond. */
	private double zscorecond=0.05;
	
	/** The zscoremotif. */
	private double zscoremotif=0.05;
	
	
	/** The pvaluecorrel. */
	// Performance Parameters
	private double pvaluecorrel=0.05;
	
	/** The numbercorrel. */
	private int numbercorrel=100000;
	
	/** The resolutionbases. */
	private int resolutionbases=2500;
	
	/** The mingenecountforcluster. */
	private int mingenecountforcluster=5;
	
	/** The maxmotifcountmerge. */
	private int maxmotifcountmerge=100;
	
	/** The maxmotifcountcluster. */
	private int maxmotifcountcluster=1000;
	
	/** The kmerlenght. */
	// Sequence Parameters
	private int kmerlenght=7;
	
	/** The pvaluemerge. */
	private double pvaluemerge=0.05;
	
	/** The cutoffmerge. */
	private double cutoffmerge=2.5;
	
	/** The penaltygap. */
	private double penaltygap=1.0;
	
	/** The penaltymismatch. */
	private double penaltymismatch=2.1;
	
	/** The knownmotifcutoff. */
	// Postprocess
	private double knownmotifcutoff=0.05;
	
	/** The knownmotifmatch. */
	private String knownmotifmatch="pvalue";
	
	/** The cutoffpostprocess. */
	private double cutoffpostprocess=1;
	
	/** The fractionpostprocess. */
	private double fractionpostprocess=0.5;
	
	/** The cutoff trim. */
	private double cutoff_trim=1;
	
	/** The uninformativemotifthreshold. */
	private double uninformativemotifthreshold=0.3;
	
	/** The maxmergemotifs. */
	private int maxmergemotifs=2500;
	
	
	/** The normalize. */
	private boolean normalize=false;
	
	/** The use 2 pass. */
	private boolean use2pass=false;
	
	/** The start time. */
	private Instant start;
	
	/** The usefastafile. */
	// Temporary imput parameters
	private String usefastafile;
	
	/** The useconditiongroupsfile. */
	private String useconditiongroupsfile;
	
	/** The useknownmotifsfile. */
	private String useknownmotifsfile;
	
	/** The usepreprocessfile. */
	private String usepreprocessfile;
	
	/** The usewigfiles. */
	private String[] usewigfiles;
	
	/** The supportedby OS. */
	private boolean supportedbyOS=true;
	
	private Thread stop;
	
	/**
	 * Instantiates a new COALESCE method.
	 */
	public COALESCEMethod() {
		super();
	}
	
	/**
	 * Instantiates a new COALESCE method.
	 *
	 * @param exprs the exprs
	 */
	public COALESCEMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	
	/**
	 * Instantiates a new COALESCE method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public COALESCEMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}

    /**
     * Instantiates a new COALESCE method.
     *
     * @param properties the properties
     */
    public COALESCEMethod(Properties properties){
    	super(properties);
    }
    
    /**
     * Instantiates a new COALESCE method.
     *
     * @param propertiesfile the propertiesfile
     */
    public COALESCEMethod(String propertiesfile){
    	super(propertiesfile);
    }
    
    

	/**
	 * Instantiates a new COALESCE method.
	 *
	 * @param mapofprobidstonames the mapofprobidstonames
	 * @param fastafilepath the fastafilepath
	 * @param conditiongroupblocks the conditiongroupblocks
	 * @param knownmotiffile the knownmotiffile
	 * @param wigfilepaths the wigfilepaths
	 * @param probgene the probgene
	 * @param pvalueconds the pvalueconds
	 * @param pvaluemotif the pvaluemotif
	 * @param zscorecond the zscorecond
	 * @param zscoremotif the zscoremotif
	 * @param pvaluecorrel the pvaluecorrel
	 * @param numbercorrel the numbercorrel
	 * @param resolutionbases the resolutionbases
	 * @param mingenecountforcluster the mingenecountforcluster
	 * @param maxmotifcountmerge the maxmotifcountmerge
	 * @param maxmotifcountcluster the maxmotifcountcluster
	 * @param kmerlenght the kmerlenght
	 * @param pvaluemerge the pvaluemerge
	 * @param cutoffmerge the cutoffmerge
	 * @param penaltygap the penaltygap
	 * @param penaltymismatch the penaltymismatch
	 * @param knownmotifcutoff the knownmotifcutoff
	 * @param knownmotifmatch the knownmotifmatch
	 * @param cutoffpostprocess the cutoffpostprocess
	 * @param fractionpostprocess the fractionpostprocess
	 * @param cutoff_trim the cutoff trim
	 * @param uninformativemotifthreshold the uninformativemotifthreshold
	 * @param maxmergemotifs the maxmergemotifs
	 * @param normalize the normalize
	 */
	private COALESCEMethod(Map<String, String> mapofprobidstonames, String fastafilepath, String conditiongroupblocks,
			String knownmotiffile, ArrayList<String> wigfilepaths, double probgene, double pvalueconds,
			double pvaluemotif, double zscorecond, double zscoremotif, double pvaluecorrel, int numbercorrel,
			int resolutionbases, int mingenecountforcluster, int maxmotifcountmerge, int maxmotifcountcluster,
			int kmerlenght, double pvaluemerge, double cutoffmerge, double penaltygap, double penaltymismatch,
			double knownmotifcutoff, String knownmotifmatch, double cutoffpostprocess, double fractionpostprocess,
			double cutoff_trim, double uninformativemotifthreshold, int maxmergemotifs, boolean normalize) {
		super();
		this.mapofprobidstonames = mapofprobidstonames;
		this.fastafilepath = fastafilepath;
		this.conditiongroupblocks = conditiongroupblocks;
		this.knownmotiffile = knownmotiffile;
		this.wigfilepaths = wigfilepaths;
		this.probgene = probgene;
		this.pvalueconds = pvalueconds;
		this.pvaluemotif = pvaluemotif;
		this.zscorecond = zscorecond;
		this.zscoremotif = zscoremotif;
		this.pvaluecorrel = pvaluecorrel;
		this.numbercorrel = numbercorrel;
		this.resolutionbases = resolutionbases;
		this.mingenecountforcluster = mingenecountforcluster;
		this.maxmotifcountmerge = maxmotifcountmerge;
		this.maxmotifcountcluster = maxmotifcountcluster;
		this.kmerlenght = kmerlenght;
		this.pvaluemerge = pvaluemerge;
		this.cutoffmerge = cutoffmerge;
		this.penaltygap = penaltygap;
		this.penaltymismatch = penaltymismatch;
		this.knownmotifcutoff = knownmotifcutoff;
		this.knownmotifmatch = knownmotifmatch;
		this.cutoffpostprocess = cutoffpostprocess;
		this.fractionpostprocess = fractionpostprocess;
		this.cutoff_trim = cutoff_trim;
		this.uninformativemotifthreshold = uninformativemotifthreshold;
		this.maxmergemotifs = maxmergemotifs;
		this.normalize = normalize;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME.toLowerCase();
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryName()
	 */
	@Override
	public String getBinaryName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.wrappers.IBiclustWrapper#getBinaryExecutablePath()
	 */
	@Override
	public String getBinaryExecutablePath() throws IOException {
        String path=SystemFolderTools.getBinaryFilePathFromPropertiesFile(NAME);
        if(path==null){
        	if(OSystemUtils.isLinux())
        		path=SystemFolderTools.getMethodExePath(NAME);
        	else{
        		LogMessageCenter.getLogger().addCriticalErrorMessage("Coalesce is not supported by JBiclustGE in Windows OS");
        		supportedbyOS=false;
        	}
        }
        
		return path;
	}


	/**
	 * Sets the fasta file.
	 *
	 * @param filepath the new fasta file
	 */
	public void setFastaFile(String filepath){
		this.fastafilepath=filepath;
	}
	
	/**
	 * Adds the fasta file.
	 *
	 * @param filepath the filepath
	 * @return the COALESCE method
	 */
	public COALESCEMethod addFastaFile(String filepath){
		setFastaFile(filepath);
		return this;
	}
	
	
	/**
	 * Sets the condition grouping file.
	 *
	 * @param filepath the new condition grouping file
	 */
	public void setConditionGroupingFile(String filepath){
		this.conditiongroupblocks=filepath;
	}
	
	/**
	 * Adds the condition grouping file.
	 *
	 * @param filepath the filepath
	 * @return the COALESCE method
	 */
	public COALESCEMethod addConditionGroupingFile(String filepath){
		setConditionGroupingFile(filepath);
		return this;
	}
	
	/**
	 * Sets the known motif file.
	 *
	 * @param filepath the new known motif file
	 */
	public void setKnownMotifFile(String filepath){
		this.knownmotiffile=filepath;
	}
	
	/**
	 * Adds the known motif file.
	 *
	 * @param filepath the filepath
	 * @return the COALESCE method
	 */
	public COALESCEMethod addKnownMotifFile(String filepath){
		setKnownMotifFile(filepath);
		return this;
	}
	
	
	/*public void setOutputfolder(String outputfolder) {
		this.outputfolder = outputfolder;
	}*/

	
	/**
	 * Append wig file.
	 *
	 * @param filepath the filepath
	 * @return the COALESCE method
	 */
	public COALESCEMethod appendWigFile(String filepath){
		addWigFile(filepath);
		return this;
	}
	
	/**
	 * Sets the wigfiles.
	 *
	 * @param wigfiles the new wigfiles
	 */
	public void setWigfiles(String[] wigfiles) {
		this.wigfilepaths=new ArrayList<>(Arrays.asList(wigfiles));
	}
	
	/**
	 * Adds the wig file.
	 *
	 * @param filepath the filepath
	 */
	public void addWigFile(String filepath){
		if(wigfilepaths==null)
			wigfilepaths=new ArrayList<>();
		wigfilepaths.add(filepath);
	}
	
	/**
	 * Sets the wig files.
	 *
	 * @param wigfiles the wigfiles
	 * @return the COALESCE method
	 */
	public COALESCEMethod setWigFiles(String[] wigfiles){
		setWigfiles(wigfiles);
		return this;
	}
	

	/**
	 * Sets the gene probability threshhold.
	 *
	 * @param probgene the new gene probability threshhold
	 */
	public void setGeneProbabilityThreshhold(double probgene) {
		this.probgene = probgene;
	}
	
	/**
	 * Adds the gene probability threshold.
	 *
	 * @param probgene the probgene
	 * @return the COALESCE method
	 */
	public COALESCEMethod addGeneProbabilityThreshold(double probgene) {
		setGeneProbabilityThreshhold(probgene);
		return this;
	}

	
	/**
	 * Sets the condition pvalue threshold.
	 *
	 * @param pvalueconds the new condition pvalue threshold
	 */
	public void setConditionPvalueThreshold(double pvalueconds) {
		this.pvalueconds = pvalueconds;
	}
	
	/**
	 * Adds the condition pvalue threshold.
	 *
	 * @param pvalueconds the pvalueconds
	 * @return the COALESCE method
	 */
	public COALESCEMethod addConditionPvalueThreshold(double pvalueconds) {
		this.pvalueconds = pvalueconds;
		return this;
	}
	
	
	/**
	 * Sets the motif pvalue threshold.
	 *
	 * @param pvaluemotif the new motif pvalue threshold
	 */
	public void setMotifPvalueThreshold(double pvaluemotif) {
		this.pvaluemotif = pvaluemotif;
	}
	
	/**
	 * Adds the motif pvalue threshold.
	 *
	 * @param pvaluemotif the pvaluemotif
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMotifPvalueThreshold(double pvaluemotif) {
		this.pvaluemotif = pvaluemotif;
		return this;
	}
	

	/**
	 * Sets the condition zscore threshold.
	 *
	 * @param zscorecond the new condition zscore threshold
	 */
	public void setConditionZscoreThreshold(double zscorecond) {
		this.zscorecond = zscorecond;
	}
	
	/**
	 * Adds the condition zscore threshold.
	 *
	 * @param zscorecond the zscorecond
	 * @return the COALESCE method
	 */
	public COALESCEMethod addConditionZscoreThreshold(double zscorecond) {
		this.zscorecond = zscorecond;
		return this;
	}
	
	

	/**
	 * Sets the motif zscore threshold.
	 *
	 * @param zscoremotif the new motif zscore threshold
	 */
	public void setMotifZscoreThreshold(double zscoremotif) {
		this.zscoremotif = zscoremotif;
	}
	
	/**
	 * Adds the motif zscore threshold.
	 *
	 * @param zscoremotif the zscoremotif
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMotifZscoreThreshold(double zscoremotif) {
		this.zscoremotif = zscoremotif;
		return this;
	}

	
	/*
	 *  Performance Parameters
	 */
	
	/**
	 * Sets the significant correlation pvalue threshold.
	 *
	 * @param pvaluecorrel the new significant correlation pvalue threshold
	 */
	public void setSignificantCorrelationPvalueThreshold(double pvaluecorrel) {
		this.pvaluecorrel = pvaluecorrel;
	}
	
	/**
	 * Adds the significant correlation pvalue threshold.
	 *
	 * @param pvaluecorrel the pvaluecorrel
	 * @return the COALESCE method
	 */
	public COALESCEMethod addSignificantCorrelationPvalueThreshold(double pvaluecorrel) {
		this.pvaluecorrel = pvaluecorrel;
		return this;
	}

	
	
	/**
	 * Sets the max numb pairs significant correlation.
	 *
	 * @param numbercorrel the new max numb pairs significant correlation
	 */
	public void setMaxNumbPairsSignificantCorrelation(int numbercorrel) {
		this.numbercorrel = numbercorrel;
	}
	
	/**
	 * Adds the max numb pairs significant correlation.
	 *
	 * @param numbercorrel the numbercorrel
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMaxNumbPairsSignificantCorrelation(int numbercorrel) {
		this.numbercorrel = numbercorrel;
		return this;
	}

	
	/**
	 * Sets the resolution bases per motif.
	 *
	 * @param resolutionbases the new resolution bases per motif
	 */
	public void setResolutionBasesPerMotif(int resolutionbases) {
		this.resolutionbases = resolutionbases;
	}
	
	/**
	 * Adds the resolution bases per motif.
	 *
	 * @param resolutionbases the resolutionbases
	 * @return the COALESCE method
	 */
	public COALESCEMethod addResolutionBasesPerMotif(int resolutionbases) {
		this.resolutionbases = resolutionbases;
		return this;
	}


	/**
	 * Sets the min gene for clusters.
	 *
	 * @param mingenecountforcluster the new min gene for clusters
	 */
	public void setMinGeneForClusters(int mingenecountforcluster) {
		this.mingenecountforcluster = mingenecountforcluster;
	}
	
	
	/**
	 * Adds the min gene for clusters.
	 *
	 * @param mingenecountforcluster the mingenecountforcluster
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMinGeneForClusters(int mingenecountforcluster) {
		this.mingenecountforcluster = mingenecountforcluster;
		return this;
	}

	/**
	 * Sets the max motif for realtime merge.
	 *
	 * @param maxmotifcountmerge the new max motif for realtime merge
	 */
	public void setMaxMotifForRealtimeMerge(int maxmotifcountmerge) {
		this.maxmotifcountmerge = maxmotifcountmerge;
	}
	
	
	/**
	 * Adds the max motif for realtime merge.
	 *
	 * @param maxmotifcountmerge the maxmotifcountmerge
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMaxMotifForRealtimeMerge(int maxmotifcountmerge) {
		this.maxmotifcountmerge = maxmotifcountmerge;
		return this;
	}

	/**
	 * Sets the maximum motif to consider cluster.
	 *
	 * @param maxmotifcountcluster the new maximum motif to consider cluster
	 */
	public void setMaximumMotifToConsiderCluster(int maxmotifcountcluster) {
		this.maxmotifcountcluster = maxmotifcountcluster;
	}
	
	/**
	 * Adds the maximum motif to consider cluster.
	 *
	 * @param maxmotifcountcluster the maxmotifcountcluster
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMaximumMotifToConsiderCluster(int maxmotifcountcluster) {
		this.maxmotifcountcluster = maxmotifcountcluster;
		return this;
	}

	
	/*
	 * Sequence Parameters
	 */
	
	/**
	 * Sets the sequence kmer length.
	 *
	 * @param kmerlenght the new sequence kmer length
	 */
	public void setSequenceKmerLength(int kmerlenght) {
		this.kmerlenght = kmerlenght;
	}
	
	/**
	 * Adds the sequence kmer length.
	 *
	 * @param kmerlenght the kmerlenght
	 * @return the COALESCE method
	 */
	public COALESCEMethod addSequenceKmerLength(int kmerlenght) {
		this.kmerlenght = kmerlenght;
		return this;
	}

	/**
	 * Sets the pvalue threshold motif merging.
	 *
	 * @param pvaluemerge the new pvalue threshold motif merging
	 */
	public void setPvalueThresholdMotifMerging(double pvaluemerge) {
		this.pvaluemerge = pvaluemerge;
	}
	
	/**
	 * Adds the pvalue threshold motif merging.
	 *
	 * @param pvaluemerge the pvaluemerge
	 * @return the COALESCE method
	 */
	public COALESCEMethod addPvalueThresholdMotifMerging(double pvaluemerge) {
		this.pvaluemerge = pvaluemerge;
		return this;
	}

	/**
	 * Sets the distance cutoff motif merging.
	 *
	 * @param cutoffmerge the new distance cutoff motif merging
	 */
	public void setDistanceCutoffMotifMerging(double cutoffmerge) {
		this.cutoffmerge = cutoffmerge;
	}
	
	/**
	 * Adds the distance cutoff motif merging.
	 *
	 * @param cutoffmerge the cutoffmerge
	 * @return the COALESCE method
	 */
	public COALESCEMethod addDistanceCutoffMotifMerging(double cutoffmerge) {
		this.cutoffmerge = cutoffmerge;
		return this;
	}

	/**
	 * Sets the distance penalty gaps.
	 *
	 * @param penaltygap the new distance penalty gaps
	 */
	public void setDistancePenaltyGaps(double penaltygap) {
		this.penaltygap = penaltygap;
	}
	
	/**
	 * Adds the distance penalty gaps.
	 *
	 * @param penaltygap the penaltygap
	 * @return the COALESCE method
	 */
	public COALESCEMethod addDistancePenaltyGaps(double penaltygap) {
		this.penaltygap = penaltygap;
		return this;
	}

	/**
	 * Sets the distance penalty mismatches.
	 *
	 * @param penaltymismatch the new distance penalty mismatches
	 */
	public void setDistancePenaltyMismatches(double penaltymismatch) {
		this.penaltymismatch = penaltymismatch;
	}
	
	/**
	 * Adds the distance penalty mismatches.
	 *
	 * @param penaltymismatch the penaltymismatch
	 * @return the COALESCE method
	 */
	public COALESCEMethod addDistancePenaltyMismatches(double penaltymismatch) {
		this.penaltymismatch = penaltymismatch;
		return this;
	}
	
	/*
	 * Postprocess
	 */

	/**
	 * Sets the known motif cutoff.
	 *
	 * @param knownmotifcutoff the new known motif cutoff
	 */
	public void setKnownMotifCutoff(double knownmotifcutoff) {
		this.knownmotifcutoff = knownmotifcutoff;
	}
	
	/**
	 * Adds the known motif cutoff.
	 *
	 * @param knownmotifcutoff the knownmotifcutoff
	 * @return the COALESCE method
	 */
	public COALESCEMethod addKnownMotifCutoff(double knownmotifcutoff) {
		this.knownmotifcutoff = knownmotifcutoff;
		return this;
	}

	/**
	 * Sets the similarity cutoff cluster merging.
	 *
	 * @param cutoffpostprocess the new similarity cutoff cluster merging
	 */
	public void setSimilarityCutoffClusterMerging(double cutoffpostprocess) {
		this.cutoffpostprocess = cutoffpostprocess;
	}
	
	/**
	 * Adds the similarity cutoff cluster merging.
	 *
	 * @param cutoffpostprocess the cutoffpostprocess
	 * @return the COALESCE method
	 */
	public COALESCEMethod addSimilarityCutoffClusterMerging(double cutoffpostprocess) {
		this.cutoffpostprocess = cutoffpostprocess;
		return this;
	}

	/**
	 * Sets the overlap fraction postprocessing.
	 *
	 * @param fractionpostprocess the new overlap fraction postprocessing
	 */
	public void setOverlapFractionPostprocessing(double fractionpostprocess) {
		this.fractionpostprocess = fractionpostprocess;
	}
	
	/**
	 * Adds the overlap fraction postprocessing.
	 *
	 * @param fractionpostprocess the fractionpostprocess
	 * @return the COALESCE method
	 */
	public COALESCEMethod addOverlapFractionPostprocessing(double fractionpostprocess) {
		this.fractionpostprocess = fractionpostprocess;
		return this;
	}

	/**
	 * Sets the cocluster stdev cutoff.
	 *
	 * @param cutoff_trim the new cocluster stdev cutoff
	 */
	public void setCoclusterStdevCutoff(double cutoff_trim) {
		this.cutoff_trim = cutoff_trim;
	}
	
	/**
	 * Adds the cocluster stdev cutoff.
	 *
	 * @param cutoff_trim the cutoff trim
	 * @return the COALESCE method
	 */
	public COALESCEMethod addCoclusterStdevCutoff(double cutoff_trim) {
		this.cutoff_trim = cutoff_trim;
		return this;
	}

	/**
	 * Sets the uninformative motif threshold.
	 *
	 * @param uninformativemotifthreshold the new uninformative motif threshold
	 */
	public void setUninformativeMotifThreshold(double uninformativemotifthreshold) {
		this.uninformativemotifthreshold = uninformativemotifthreshold;
	}
	
	/**
	 * Adds the uninformative motif threshold.
	 *
	 * @param uninformativemotifthreshold the uninformativemotifthreshold
	 * @return the COALESCE method
	 */
	public COALESCEMethod addUninformativeMotifThreshold(double uninformativemotifthreshold) {
		this.uninformativemotifthreshold = uninformativemotifthreshold;
		return this;
	}

	/**
	 * Sets the max motifs to merge.
	 *
	 * @param maxmergemotifs the new max motifs to merge
	 */
	public void setMaxMotifsToMerge(int maxmergemotifs) {
		this.maxmergemotifs = maxmergemotifs;
	}
	
	/**
	 * Adds the max motifs to merge.
	 *
	 * @param maxmergemotifs the maxmergemotifs
	 * @return the COALESCE method
	 */
	public COALESCEMethod addMaxMotifsToMerge(int maxmergemotifs) {
		this.maxmergemotifs = maxmergemotifs;
		return this;
	}

	/**
	 * Sets the automatic normalization.
	 *
	 * @param normalize the new automatic normalization
	 */
	public void setAutomaticNormalization(boolean normalize) {
		this.normalize = normalize;
	}
	
	/**
	 * Adds the automatic normalization.
	 *
	 * @param normalize the normalize
	 * @return the COALESCE method
	 */
	public COALESCEMethod addAutomaticNormalization(boolean normalize) {
		this.normalize = normalize;
		return this;
	}



	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		
		
		 configureInputData();
		 
		 //if(!use2pass)
		 start = Instant.now();

		 if(fastafilepath!=null){
			 usefastafile=fastafilepath;
			 if(knownmotiffile!=null){
			    use2pass=true;
			    LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Configuring Coalesce 1st stage..."); 
			 }
		 }
		 if(conditiongroupblocks!=null)
			 useconditiongroupsfile=conditiongroupblocks;
		 
		 if(wigfilepaths!=null && knownmotiffile==null)
			 usewigfiles=wigfilepaths.toArray(new String[wigfilepaths.size()]);
		
		configurealgorithmruninput();
		
		if(use2pass){
			boolean outputstate=runCurrentAlgorithmCommands(false);
			if(outputstate){
			   resetInputParameters();
			   LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Configuring Coalesce 2st stage..."); 
			   usepreprocessfile=(String) resultprocessor.getResultsObject();
			   useknownmotifsfile=knownmotiffile;
			   configurealgorithmruninput();
			   return runCurrentAlgorithmCommands(true);
			}
			return outputstate;
			
		}
		else
			return runCurrentAlgorithmCommands(true);
		
		
	}
	
	
	/**
	 * Run current algorithm commands.
	 *
	 * @param savebiclusters the savebiclusters
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	protected boolean runCurrentAlgorithmCommands(boolean savebiclusters) throws Exception{
		
		ProcessBuilder build= new ProcessBuilder(cmds);
		final Process p =build.start();
		
		InputStream inputstr =p.getInputStream();
		ReusableInputStream errorstr =new ReusableInputStream(p.getErrorStream());
		
		if(!savebiclusters)
		    resultprocessor=new COALESCEResultsFileWriter(inputstr, outputfolder);
		else
		    resultprocessor=new COALESCEResultsProcessor(inputstr, expressionset);
		Thread stdout=new Thread(resultprocessor);
		stdout.start();
		
		
		Thread sterror=new Thread(new GeneralProcessProgressionChecker(errorstr));
		sterror.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			  @Override
			  public void run() {
			    p.destroy();
			    SystemFolderTools.deleteTempDir(outputfolder);
			  }
			});
		
		stop=new Thread(new Runnable() {
			
			@Override
			public void run() {
				 p.destroy();
				 SystemFolderTools.deleteTempDir(outputfolder);
				 LogMessageCenter.getLogger().addInfoMessage(NAME+" was forced to stop");
				
			}
		});
		
	  
		int exitval=p.waitFor();
		if(exitval==0){

			saveElapsedTime(start);
			
			while (!resultprocessor.isResultsprocessed()) {
				Thread.sleep(1000);
			}
			
			return true;
			
		}
		else{
			String out =IOUtils.toString(errorstr);
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": "+out);
			
		}
			
			
		return false;
	}
	
	 /**
 	 * Reset input parameters.
 	 */
 	private void resetInputParameters(){
		usefastafile=null;
		useconditiongroupsfile=null;
		useknownmotifsfile=null;
		usepreprocessfile=null;
		usewigfiles=null;
	 }
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		this.listofbiclusters=(BiclusterList) resultprocessor.getResultsObject();
		
		SystemFolderTools.deleteTempDir(outputfolder);
	}
	
	/**
	 * Configurealgorithmruninput.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void configurealgorithmruninput() throws IOException{
		
		
		cmds=new CommandsProcessList();
		String binarypath=getBinaryExecutablePath();
		
		cmds.add(binarypath);
		
		cmds.insertStringParameter("-i", pclinputfilepath);
		
		if(usefastafile!=null && useknownmotifsfile==null && usepreprocessfile==null)
			cmds.insertStringParameter("-f", usefastafile);
		
		if(useconditiongroupsfile!=null && useknownmotifsfile==null && usepreprocessfile==null)
			cmds.insertStringParameter("-d", useconditiongroupsfile);
		
		if(useknownmotifsfile!=null && usepreprocessfile!=null && usefastafile==null){
			cmds.insertStringParameter("-K", useknownmotifsfile);
			cmds.insertStringParameter("-j", usepreprocessfile);
		}
		
		if(usewigfiles!=null)
			cmds.insertStringParameterArray(usewigfiles);
		
		if(probgene!=0.95 && probgene>0 && probgene<=1)
			cmds.insertDoubleParameter("-p", probgene);
		if(pvalueconds!=0.05 && pvalueconds>0 && pvalueconds<=1)
			cmds.insertDoubleParameter("-c", pvalueconds);
		if(zscorecond!=0.05 && zscorecond>0 && zscorecond<=1)
			cmds.insertDoubleParameter("-C", zscorecond);
		if(fastafilepath!=null){
			if(pvaluemotif!=0.05 && pvaluemotif>0 && pvaluemotif<=1)
				cmds.insertDoubleParameter("-m", pvaluemotif);
			if(zscoremotif!=0.05 && zscoremotif>0 && zscoremotif<=1)
				cmds.insertDoubleParameter("-M", zscoremotif);
		}
		if(pvaluecorrel!=0.05 && pvaluecorrel>0 && pvaluecorrel<=1)
			cmds.insertDoubleParameter("-n", pvaluecorrel);
		if(numbercorrel!=100000 && numbercorrel>0)
			cmds.insertIntegerParameter("-N", numbercorrel);
		if(resolutionbases!=2500 && resolutionbases>0)
			cmds.insertIntegerParameter("-b", resolutionbases);
		if(mingenecountforcluster!=5 && mingenecountforcluster>0)
			cmds.insertIntegerParameter("-z", mingenecountforcluster);
		if(maxmotifcountmerge!=100 && maxmotifcountmerge>0)
			cmds.insertIntegerParameter("-E", maxmotifcountmerge);
		if(maxmotifcountcluster!=1000 && maxmotifcountcluster>0)
			cmds.insertIntegerParameter("-Z", maxmotifcountcluster);
		if(kmerlenght!=7 && kmerlenght>0)
			cmds.insertIntegerParameter("-k", kmerlenght);
		if(pvaluemerge!=0.05 && pvaluemerge>0 && pvaluemerge<=1)
			cmds.insertDoubleParameter("-g", pvaluemerge);
		if(cutoffmerge!=2.5 && cutoffmerge>=0)
			cmds.insertDoubleParameter("-G", cutoffmerge);
		if(penaltygap!=1 && penaltygap>0)
			cmds.insertDoubleParameter("-y", penaltygap);
		if(penaltymismatch!=2.1 && penaltymismatch>0)
			cmds.insertDoubleParameter("-Y", penaltymismatch);
		if(knownmotifcutoff!=0.05 && knownmotifcutoff>0)
			cmds.insertDoubleParameter("-F", knownmotifcutoff);
		if(cutoffpostprocess!=1 && cutoffpostprocess>0)
			cmds.insertDoubleParameter("-J", cutoffpostprocess);
		if(fractionpostprocess!=0.5 && fractionpostprocess<=1 && fractionpostprocess>0)
			cmds.insertDoubleParameter("-L", fractionpostprocess);
		if(cutoff_trim!=1 && cutoff_trim>0)
			cmds.insertDoubleParameter("-T", cutoff_trim);
		if(uninformativemotifthreshold!=0.3 && uninformativemotifthreshold>0)
			cmds.insertDoubleParameter("-u", uninformativemotifthreshold);
		if(maxmergemotifs!=2500 && maxmergemotifs>0)
			cmds.insertIntegerParameter("-x", maxmergemotifs);
			
		if(normalize)
			cmds.add("-e");
		
		LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Input CMDs "+getAlgorithmName()+": "+cmds);
	}
	
	/**
	 * Configure input data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void configureInputData() throws IOException{
		outputfolder=SystemFolderTools.createRandomTemporaryProcessFolderWithNamePrefix("COALESCE");
		pclinputfilepath=FilenameUtils.concat(outputfolder, MTUStringUtils.shortUUID()+".pcl");
		if(mapofprobidstonames!=null)
			expressionset.writeExpressionDatasetLabeledFormatToPCLFileFormat(pclinputfilepath, mapofprobidstonames);
			//expressionset.writeExpressionDatasetToPCLFileFormat(pclinputfilepath, mapofprobidstonames);
		else
			expressionset.writeExpressionDatasetLabeledFormatToPCLFileFormat(pclinputfilepath);
			//expressionset.writeExpressionDatasetToPCLFileFormat(pclinputfilepath);
	}
	
/*	protected void deleteTempFiles() throws IOException{
		FileUtils.deleteDirectory(new File(outputfolder));
	}*/

	/* (non-Javadoc)
 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
 */
@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{"fasta_file",
				"condition_groupings_dataset",
				"prob_gene",
				"pvalue_cond",
				"pvalue_motif",
				"zscore_cond",
				"zscore_motif",
				"sequence_kmer_length",
				"pvalue_merge",
				"cutoff_merge",
				"penalty_gap",
				"penalty_mismatch",
				"pvalue_correl",
				"number_correl",
				"sequences_types_to_use",
				"resolution_of_bases_per_motif",
				"minimum_gene_count",
				"maximum_motif_count_merging",
				"maximum_motif_count_cluster",
				"known_motifs_file",
				"known_cutoff",
				"type_of_known_motif_matching",
				"cutoff_postprocess",
				"fraction_postprocess",
				"cutoff_trim",
				"Convert_RCs_and_RC-like_PSTs_to_single_strand",
				"uninformative_motif_threshold",
				"maximum_motifs_to_merge",
				"normalize"};
		
		String[] defaultvalues=new String[]{
			"","","0.95","0.05","0.05","0.05","0.05","7","0.05","2.5",
			"1.0","2.1","0.05","100000","","2500","5","100","1000","",
			"0.05","pvalue","1","0.5","1","true","0.3","2500","false"
		};
		
		String[] comments=new String[] {
			"Input FASTA file",
			"Condition groupings into dataset blocks",
			"Probability threshhold for gene inclusion",
			"P-value threshold for condition inclusion",
			"P-value threshold for motif inclusion",
			"Z-score threshold for condition inclusion",
			"Z-score threshhold for motif inclusion",
			"Sequence kmer length",
			"P-value threshhold for motif merging",
			"Edit distance cutoff for motif merging", 
			"Edit distance penalty for gaps",
			"Edit distance penalty for mismatches",  
			"P-value threshhold for significant correlation",  
			"Maximum number of pairs to sample for significant correlation",
			"Sequence types to use (comma separated)",
			"Resolution of bases per motif match",
			"Minimum gene count for clusters of interest", 
			"Maximum motif count for realtime merging",  
			"Maximum motif count to consider a cluster", 
			"File containing known motifs",
			"Score cutoff for known motif labeling",  
			"Type of known motif matching  (possible values: pvalue, rmse, js)", 
			"Similarity cutoff for cluster merging",  
			"Overlap fraction for postprocessing gene/condition inclusion",
			"Cocluster stdev cutoff for cluster trimming",
			"Convert RCs and RC-like PSTs to single strand",
			"Uninformative motif threshold (bits)",
			"Maximum motifs to merge exactly",  
			"Automatically detect/normalize single channel data (false or true)"
         };
		
		
		String[] propsubkeys=new String[]{"-f","-d","-p","-c","-m","-C","-M","-k","-g","-G","-y","-Y","-n","-N","-q","-b","-z","-E","-Z","-K","-F","-S","-J","-L","-T","-R","-u","-x","-e" };
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues,comments,propsubkeys);
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		fastafilepath=PropertiesUtilities.getStringPropertyValue(props, "fasta_file", null, this.getClass());
		conditiongroupblocks=PropertiesUtilities.getStringPropertyValue(props, "condition_groupings_dataset", null, this.getClass());
		knownmotiffile=PropertiesUtilities.getStringPropertyValue(props, "known_motifs_file", null, this.getClass());
		
		
		probgene=PropertiesUtilities.getDoublePropertyValue(props, "prob_gene", 0.95, this.getClass());
		pvalueconds=PropertiesUtilities.getDoublePropertyValue(props, "pvalue_cond", 0.05, this.getClass());
		pvaluemotif=PropertiesUtilities.getDoublePropertyValue(props, "pvalue_motif", 0.05, this.getClass());
		zscorecond=PropertiesUtilities.getDoublePropertyValue(props, "zscore_cond", 0.05, this.getClass());
		zscoremotif=PropertiesUtilities.getDoublePropertyValue(props, "zscore_motif", 0.05, this.getClass());
		
		kmerlenght=PropertiesUtilities.getIntegerPropertyValue(props, "sequence_kmer_length", 7, this.getClass());
		pvaluemerge=PropertiesUtilities.getDoublePropertyValue(props, "pvalue_merge", 0.05, this.getClass());
		cutoffmerge=PropertiesUtilities.getDoublePropertyValue(props, "cutoff_merge", 2.5, this.getClass());
		penaltygap=PropertiesUtilities.getDoublePropertyValue(props, "penalty_gap", 1, this.getClass());
		penaltymismatch=PropertiesUtilities.getDoublePropertyValue(props, "penalty_mismatch", 2.1, this.getClass());;
		// Performance Parameters
		pvaluecorrel=PropertiesUtilities.getDoublePropertyValue(props, "pvalue_correl", 0.05, this.getClass());
		numbercorrel=PropertiesUtilities.getIntegerPropertyValue(props,"number_correl", 100000, this.getClass());
		resolutionbases=PropertiesUtilities.getIntegerPropertyValue(props,"resolution_of_bases_per_motif", 2500, this.getClass());
		mingenecountforcluster=PropertiesUtilities.getIntegerPropertyValue(props,"minimum_gene_count", 5, this.getClass());
		maxmotifcountmerge=PropertiesUtilities.getIntegerPropertyValue(props,"maximum_motif_count_merging", 100, this.getClass());
		maxmotifcountcluster=PropertiesUtilities.getIntegerPropertyValue(props,"maximum_motif_count_cluster", 1000, this.getClass());
		
		knownmotifcutoff=PropertiesUtilities.getDoublePropertyValue(props, "known_cutoff", 0.05, this.getClass());
		cutoffpostprocess=PropertiesUtilities.getDoublePropertyValue(props, "cutoff_postprocess", 1, this.getClass());
		fractionpostprocess=PropertiesUtilities.getDoublePropertyValue(props, "fraction_postprocess", 0.5, this.getClass());
		cutoff_trim=PropertiesUtilities.getDoublePropertyValue(props, "cutoff_trim", 1, this.getClass());
		uninformativemotifthreshold=PropertiesUtilities.getDoublePropertyValue(props, "uninformative_motif_threshold", 0.3, this.getClass());
		maxmergemotifs=PropertiesUtilities.getIntegerPropertyValue(props, "maximum_motifs_to_merge", 2500, this.getClass());
		normalize=PropertiesUtilities.getBooleanPropertyValue(props, "normalize", false, this.getClass());
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return outputfolder;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeydoubleValue("prob_gene", probgene);
		parameters.setKeydoubleValue("pvalue_cond", pvalueconds);
		parameters.setKeydoubleValue("pvalue_motif", pvaluemotif);
		parameters.setKeydoubleValue("zscore_cond", zscorecond);
		parameters.setKeydoubleValue("zscore_motif", zscoremotif);
		parameters.setKeyintValue("sequence_kmer_length", kmerlenght);
		parameters.setKeydoubleValue("pvalue_merge", pvaluemerge);
		parameters.setKeydoubleValue("cutoff_merge", cutoffmerge);
		parameters.setKeydoubleValue("penalty_gap", penaltygap);
		parameters.setKeydoubleValue("penalty_mismatch", penaltymismatch);
		parameters.setKeydoubleValue("pvalue_correl", pvaluecorrel);
		parameters.setKeyintValue("number_correl", numbercorrel);
		parameters.setKeyintValue("resolution_of_bases_per_motif", resolutionbases);
		parameters.setKeyintValue("minimum_gene_count", mingenecountforcluster);
		parameters.setKeyintValue("maximum_motif_count_merging", maxmotifcountmerge);
		parameters.setKeyintValue("maximum_motif_count_cluster", maxmotifcountcluster);
		parameters.setKeydoubleValue("known_cutoff", knownmotifcutoff);
		parameters.setKeydoubleValue("cutoff_postprocess", cutoffpostprocess);
		parameters.setKeydoubleValue("fraction_postprocess", fractionpostprocess);
		parameters.setKeydoubleValue("cutoff_trim", cutoff_trim);
		parameters.setKeydoubleValue("uninformative_motif_threshold", uninformativemotifthreshold);
		parameters.setKeyintValue("maximum_motifs_to_merge", maxmergemotifs);
		
		parameters.setKeybooleanValue("normalize", normalize);
		
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new COALESCEMethod(mapofprobidstonames, fastafilepath, conditiongroupblocks, knownmotiffile, wigfilepaths, probgene, pvalueconds, pvaluemotif, zscorecond, zscoremotif, pvaluecorrel, numbercorrel, resolutionbases, mingenecountforcluster, maxmotifcountmerge, maxmotifcountcluster, kmerlenght, pvaluemerge, cutoffmerge, penaltygap, penaltymismatch, knownmotifcutoff, knownmotifmatch, cutoffpostprocess, fractionpostprocess, cutoff_trim, uninformativemotifthreshold, maxmergemotifs, normalize);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportDefineNumberBiclustersToFind()
	 */
	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#changeNumberBiclusterToFind(int)
	 */
	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return supportedbyOS;
	}

	@Override
	public void stopProcess() {
		stop.start();
		
	}
	
	@Override
	public void reset() {
		use2pass=false;
	    this.listofbiclusters=null;
	}
	
	/*public static COALESCEMethod newInstance(ExpressionData data){
		return new COALESCEMethod(data);
	}*/

}
