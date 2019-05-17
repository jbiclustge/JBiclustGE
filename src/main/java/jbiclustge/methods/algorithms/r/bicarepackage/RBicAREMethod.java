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
package jbiclustge.methods.algorithms.r.bicarepackage;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class RBicAREMethod.
 */
public class RBicAREMethod extends RBiclustAlgorithmCaller{
	
	
	/** The bicare nclusters. */
	public static String BICARE_NCLUSTERS="number_of_biclusters_to_search";
	
	/** The bicare initgenesprob. */
	public static String BICARE_INITGENESPROB="genes_initial_probability";
	
	/** The bicare initcondsprob. */
	public static String BICARE_INITCONDSPROB="samples_initial_probability";
	
	/** The bicare residuethreshold. */
	public static String BICARE_RESIDUETHRESHOLD="residue_threshold";
	
	/** The bicare mingenesbic. */
	public static String BICARE_MINGENESBIC="minimal_number_of_gene_per_bicluster";
	
	/** The bicare mincondsbic. */
	public static String BICARE_MINCONDSBIC="minimal_number_of_conditions_per_bicluster";
	
	/** The bicare niter. */
	public static String BICARE_NITER="number_of_iterations";
	
	/** The Constant NAME. */
	public static final String NAME="BicARE";
	
	/** The numberbiclusters. */
	private int numberbiclusters=20;
	
	/** The genesinitprob. */
	private double genesinitprob=0.5;
	
	/** The condinitprob. */
	private double condinitprob=0.5;
	
	/** The residuethresh. */
	private double residuethresh=0.0;
	
	/** The mingenesbic. */
	private int mingenesbic=8;
	
	/** The mincondsbic. */
	private int mincondsbic=6;
	
	/** The numberiter. */
	private int numberiter=500;
	//private 
	
	
	/**
	 * Instantiates a new r bic ARE method.
	 */
	public RBicAREMethod() {
		super();
	}
	
	/**
	 * Instantiates a new r bic ARE method.
	 *
	 * @param exprs the exprs
	 */
	public RBicAREMethod(ExpressionData exprs) {
		super(exprs);
	}
	
	/**
	 * Instantiates a new r bic ARE method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RBicAREMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
	}
	
	/**
	 * Instantiates a new r bic ARE method.
	 *
	 * @param props the props
	 */
	public RBicAREMethod(Properties props) {
		super(props);
	}
	
	/**
	 * Instantiates a new r bic ARE method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RBicAREMethod(String propertiesfile) {
		super(propertiesfile);
	}
	
	
	
	
	
	/**
	 * Instantiates a new r bic ARE method.
	 *
	 * @param data the data
	 * @param numberbiclusters the numberbiclusters
	 * @param genesinitprob the genesinitprob
	 * @param condinitprob the condinitprob
	 * @param residuethresh the residuethresh
	 * @param mingenesbic the mingenesbic
	 * @param mincondsbic the mincondsbic
	 * @param numberiter the numberiter
	 */
	private RBicAREMethod(ExpressionData data,int numberbiclusters, double genesinitprob, double condinitprob, double residuethresh,
			int mingenesbic, int mincondsbic, int numberiter) {
		super(data);
		this.numberbiclusters = numberbiclusters;
		this.genesinitprob = genesinitprob;
		this.condinitprob = condinitprob;
		this.residuethresh = residuethresh;
		this.mingenesbic = mingenesbic;
		this.mincondsbic = mincondsbic;
		this.numberiter = numberiter;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param numberbiclusters the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int numberbiclusters) {
		this.numberbiclusters = numberbiclusters;
	}

	/**
	 * Sets the genes initial probability.
	 *
	 * @param genesinitprob the new genes initial probability
	 */
	public void setGenesInitialProbability(double genesinitprob) {
		this.genesinitprob = genesinitprob;
	}

	/**
	 * Sets the conditions initial probability.
	 *
	 * @param condinitprob the new conditions initial probability
	 */
	public void setConditionsInitialProbability(double condinitprob) {
		this.condinitprob = condinitprob;
	}

	/**
	 * Sets the residue threshold.
	 *
	 * @param residuethresh the new residue threshold
	 */
	public void setResidueThreshold(double residuethresh) {
		this.residuethresh = residuethresh;
	}

	/**
	 * Sets the min genesper bicluster.
	 *
	 * @param mingenesbic the new min genesper bicluster
	 */
	public void setMinGenesperBicluster(int mingenesbic) {
		this.mingenesbic = mingenesbic;
	}

	/**
	 * Sets the minconditionsper bicluster.
	 *
	 * @param mincondsbic the new minconditionsper bicluster
	 */
	public void setMinconditionsperBicluster(int mincondsbic) {
		this.mincondsbic = mincondsbic;
	}

	/**
	 * Sets the number iterations.
	 *
	 * @param numberiter the new number iterations
	 */
	public void setNumberIterations(int numberiter) {
		this.numberiter = numberiter;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
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
		setNumberBiclustersToFind(numberbics);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#requiredLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> requiredLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("BicARE", true));
		return list;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		
		ArrayList<String> objectstoremove=getBaseROjectsList();
		
		return objectstoremove.toArray(new String[objectstoremove.size()]);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.numberbiclusters=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BICARE_NCLUSTERS, 20, 2,true, this.getClass());
		this.genesinitprob=PropertiesUtilities.getDoublePropertyValueValidLimits(props, BICARE_INITGENESPROB, 0.5, 0, false, 1, true, getClass());
		this.condinitprob=PropertiesUtilities.getDoublePropertyValueValidLimits(props, BICARE_INITCONDSPROB, 0.5, 0, false, 1, true, getClass());
		this.residuethresh=PropertiesUtilities.getDoublePropertyValueValidLowerLimit(props, BICARE_RESIDUETHRESHOLD, 0.0, 0.0, true, getClass());
		this.mingenesbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BICARE_MINGENESBIC, 8, 2,true, this.getClass());
		this.mincondsbic=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BICARE_MINCONDSBIC, 6, 2,true, this.getClass());
		this.numberiter=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, BICARE_NITER, 500, 1,true, this.getClass());
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Starting "+getAlgorithmName()+"(FLOC) biclustering method...please wait");
		try {

			//loadExpressionMatrixInREnvironment();
			loadLabeledExpressionMatrixInREnvironment();
			Instant start = Instant.now();
			if(residuethresh==0){
				residuethresh=rsession.eval("residue("+inputmatrixname+")").asDouble();
				residuethresh=residuethresh/10;
				LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Using residue: "+residuethresh);
			
			}
		
			rsession.silentlyEval(getResultOutputName()+" <- FLOC("+inputmatrixname+", k="+String.valueOf(numberbiclusters)+","
				+ " pGene="+String.valueOf(genesinitprob)+", pSample="+String.valueOf(condinitprob)+", r="+String.valueOf(residuethresh)+", "
						+ ""+String.valueOf(mingenesbic)+", "+mincondsbic+", "+String.valueOf(numberiter)+getExtraInformation()+")");
		
			saveElapsedTime(start);

			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": ", e);
			return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * Gets the extra information.
	 *
	 * @return the extra information
	 */
	protected String getExtraInformation(){
		return "";
	}
	
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Saving "+getAlgorithmName()+" results...please wait");

		
		listofbiclusters=new BiclusterList();
		try {
			
		
			int nbics=rsession.eval("nrow("+getResultOutputName()+"$mat.resvol.bic)").asInteger();
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Bicare found "+nbics+" biclusters");
		
			for (int i = 0; i < nbics; i++) {
				String k=String.valueOf(i+1);
				LogMessageCenter.getLogger().toClass(getClass()).addDebugMessage("Saving Bicluster "+k+" of "+nbics);
				int[]genevalueslist=rsession.eval("which("+getResultOutputName()+"$bicRow["+k+",] == 1)").asIntegers();
				int[] condsvalueslist =rsession.eval("which("+getResultOutputName()+"$bicCol["+k+",] == 1)").asIntegers();
				double[] extrainfo=rsession.eval(getResultOutputName()+"$mat.resvol.bic["+k+",]").asDoubles();
			
				BiclusterResult res=buildBicluster(genevalueslist, condsvalueslist,extrainfo);
				if(res!=null)
					listofbiclusters.add(res);
			}
		
		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(getClass()).addCriticalErrorMessage("Error in processing the results of "+getAlgorithmName()+": ", e);
		    throw e;
		}
		
	}
	
	/**
	 * Builds the bicluster.
	 *
	 * @param geneindexes the geneindexes
	 * @param condindexes the condindexes
	 * @param extrainfo the extrainfo
	 * @return the bicluster result
	 */
	private BiclusterResult buildBicluster(int[] geneindexes, int[] condindexes,double[] extrainfo){
		
		if(geneindexes!=null && condindexes!=null){
			int[] genesmapindexes=new int[geneindexes.length];
			int[] condmapindexes=new int[condindexes.length];
		
			for (int i = 0; i < geneindexes.length; i++) {
				genesmapindexes[i]=geneindexes[i]-1;
			}
		
			for (int i = 0; i < condindexes.length; i++) {
				condmapindexes[i]=condindexes[i]-1;
			}
			
			BiclusterResult result=new BiclusterResult(expressionset, genesmapindexes, condmapindexes, true);
			if(extrainfo!=null){
				result.appendAdditionalInfo("residue", extrainfo[0]);
				result.appendAdditionalInfo("volume", (int)extrainfo[1]);
				result.appendAdditionalInfo("rowvar", extrainfo[4]);
			}
		
			return result;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		String[] propkeys=new String[]{
				BICARE_NCLUSTERS,
				BICARE_INITGENESPROB,
				BICARE_INITCONDSPROB,
				BICARE_RESIDUETHRESHOLD,
				BICARE_MINGENESBIC,
				BICARE_MINCONDSBIC,
				BICARE_NITER
	
		};
		String[] defaultvalues=new String[]{"20","0.5","0.5","0","8","6","500"};
		String[] comments=new String[] {
				"The number of biclusters searched",
				"Genes initial probability of membership to the biclusters",
				"Samples initial probability of membership to the biclusters",
				"The residue threshold, if 0 is calculated from data (default=0)",
				"Minimal number of gene per bicluster",
				"Minimal number of conditions per bicluster",
				"Number of iterations"
				
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: BicARE manual, url: https://www.bioconductor.org/packages/release/bioc/vignettes/BicARE/inst/doc/BicARE.pdf");
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		LinkedHashMap<String, String> param=new LinkedHashMap<>();
		param.put("Number biclusters", String.valueOf(numberbiclusters));
		param.put("Genes initial probability", String.valueOf(genesinitprob));
		param.put("Conditions initial probability", String.valueOf(condinitprob));
		param.put("Residue threshold", String.valueOf(residuethresh));
		param.put("Min genes bicluster", String.valueOf(mingenesbic));
		param.put("Min conditions bicluster", String.valueOf(mincondsbic));
		param.put("Number iterations", String.valueOf(numberiter));
		return param;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RBicAREMethod(expressionset, numberbiclusters, genesinitprob, condinitprob, residuethresh, mingenesbic, mincondsbic, numberiter);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		return null;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#supportedinoperatingsystem()
	 */
	@Override
	protected boolean supportedinoperatingsystem() {
		return true;
	}

	

	

}
