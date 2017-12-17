
/**
 * Algorithm developed by  Thierry Chekouo, Copyright 2014
 * The Algorithm is distributed under the terms of the GNU General Public License
 * 
 * Modifications to run algorithm in JbiclustGE: Orlando Rocha (ornrocha@gmail.com)
 * 
 */
package jbiclustge.methods.algorithms.java.penalizedplaid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.properties.AlgorithmProperties;
import pt.ornrocha.ioutils.readers.MTUReadUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.timeutils.MTUTimeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class PenalizedPlaidMethod.
 */
public class PenalizedPlaidMethod extends AbstractBiclusteringAlgorithmCaller{
	
	
	/** The Constant NAME. */
	public static final String NAME="PenalizedPlaid";
	
	/** The numberbiclustersestimate. */
	public static String NUMBERBICLUSTERSESTIMATE="number_of_biclusters_to_estimate";
	
	/** The modeltodata. */
	public static String MODELTODATA="model_used_for_the_data";
	
	/** The mcmcsamples. */
	public static String MCMCSAMPLES="number_of_MCMC_samples_after_the_burn_in";
	
	/** The burninsamples. */
	public static String BURNINSAMPLES="number_of_burn_in_samples";
	
	/** The pplambda. */
	public static String PPLAMBDA="value_of_lambda";
	
	/** The aicparameter. */
	public static String AICPARAMETER="AIC";
	
	/** The dicparamter. */
	public static String DICPARAMTER="DIC";
	
	/** The alphaestimate. */
	public static String ALPHAESTIMATE="AlphaEstimate";
	
	/** The betaestimate. */
	public static String BETAESTIMATE="BetaEstimate";
	
	/** The muestimate. */
	public static String MUESTIMATE="MuEstimate";
	
	/** The lambdaestimate. */
	public static String LAMBDAESTIMATE="LambdaEstimate";
	
	/** The f1measureparameter. */
	public static String F1MEASUREPARAMETER="F1-measure";
	
	/** The nu 0. */
	static int nu0=1;
	
	/** The s 20 sigma 2. */
	static double s20_sigma2=0.05;
	
	/** The s 20 sigma 20. */
	static double s20_sigma20=0.05;
	
	/** The sigma 2 alpha. */
	static double sigma2alpha=0.5;
	
	/** The sigma 2 beta. */
	static double sigma2beta=0.5;
	
	/** The sigma 2 mu 0. */
	static double sigma2mu0=0.5;
	
	/** The sigma 2 mu. */
	static double sigma2mu=0.5;
	
	/** The k. */
	private int K=0; // number biclusters to estimate
	
	/** The model. */
	private PPlaidModelData model=PPlaidModelData.GPE;
	
	/** The sample. */
	private int sample=1000; // number samples after burn in
	
	/** The burninsample. */
	private int burninsample=1000; // burn in samples
	
	/** The lambda. */
	private double lambda=0.0;
	
	/** The n. */
	private int n;// number genes
	
	/** The p. */
	private int p; // number conditions
	
	/** The data. */
	private double[][] data;
	
	/** The kappa output. */
	private int kappa_output[][];
	
	/** The rho output. */
	private int rho_output[][];
	
	/** The alpha max. */
	private double[][] alpha_max;
	
	/** The beta max. */
	private double[][] beta_max;
	
	/** The mu max. */
	private double[][] mu_max;
	
	/** The aic. */
	private double AIC;
	
	/** The DI cc. */
	private double DICc;
	
	/** The moylambda. */
	private double moylambda = 0;
	
	/** The saveadditionalinfo. */
	private boolean saveadditionalinfo=true;
	
	/** The rho sim. */
	private int[][] rhoSim; //known row biclusters
	
	/** The kappa sim. */
	private int[][] kappaSim; // known columns biclusters
	
	/** The k1. */
	private int K1=0;
	
	/** The mean F 1 max. */
	private double meanF1Max = 0;
	
	private boolean run=true;
	
	
	/**
	 * Instantiates a new penalized plaid method.
	 */
	public PenalizedPlaidMethod(){
		super();
	}
	
	/**
	 * Instantiates a new penalized plaid method.
	 *
	 * @param exprs the exprs
	 */
	public PenalizedPlaidMethod(ExpressionData exprs){
		super(exprs);
	}
	
	
	/**
	 * Instantiates a new penalized plaid method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public PenalizedPlaidMethod(ExpressionData exprs, Properties props){
		super(exprs,props);
	}
	
	/**
	 * Instantiates a new penalized plaid method.
	 *
	 * @param props the props
	 */
	public PenalizedPlaidMethod(Properties props){
		super(props);
	}
	
	/**
	 * Instantiates a new penalized plaid method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public PenalizedPlaidMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	
	
	
	/**
	 * Instantiates a new penalized plaid method.
	 *
	 * @param K the k
	 * @param model the model
	 * @param sample the sample
	 * @param burninsample the burninsample
	 * @param lambda the lambda
	 */
	private PenalizedPlaidMethod(int K, PPlaidModelData model, int sample, int burninsample, double lambda) {
		super();
		this.K = K;
		this.model = model;
		this.sample = sample;
		this.burninsample = burninsample;
		this.lambda = lambda;
	}

	/**
	 * Sets the number biclusters to find.
	 *
	 * @param number the new number biclusters to find
	 */
	public void setNumberBiclustersToFind(int number){
		this.K=number;
	}
	
	/**
	 * Adds the number biclusters to find.
	 *
	 * @param number the number
	 * @return the penalized plaid method
	 */
	public PenalizedPlaidMethod addNumberBiclustersToFind(int number){
		this.K=number;
		return this;
	}
	
	/**
	 * Sets the MCMC sample.
	 *
	 * @param sample the new MCMC sample
	 */
	public void setMCMCSample(int sample){
		this.sample=sample;
	}
	
	/**
	 * Adds the MCMC sample.
	 *
	 * @param sample the sample
	 * @return the penalized plaid method
	 */
	public PenalizedPlaidMethod addMCMCSample(int sample){
		setMCMCSample(sample);
		return this;
	}
	
	/**
	 * Sets the burn in sample.
	 *
	 * @param burnin the new burn in sample
	 */
	public void setBurnInSample(int burnin){
		this.burninsample=burnin;
	}
	
	/**
	 * Adds the burn in sample.
	 *
	 * @param burnin the burnin
	 * @return the penalized plaid method
	 */
	public PenalizedPlaidMethod addBurnInSample(int burnin){
		setBurnInSample(burnin);
		return this;
	}
	
	/**
	 * Sets the lambda.
	 *
	 * @param lambda the new lambda
	 */
	public void setlambda(double lambda){
		this.lambda=lambda;
	}
	
	/**
	 * Sets the plaid model.
	 *
	 * @param model the model
	 * @param lambda the lambda
	 */
	public void setPlaidModel(PPlaidModelData model, double lambda ){
		setlambda(lambda);
		this.model=model;
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
	
	/**
	 * Sets the known biclusters binary files.
	 *
	 * @param mapgenestobiclustersfile the mapgenestobiclustersfile
	 * @param genefiledelimiter the genefiledelimiter
	 * @param mapconditionstobiclustersfile the mapconditionstobiclustersfile
	 * @param conditionfiledelimiter the conditionfiledelimiter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void setKnownBiclustersBinaryFiles(String mapgenestobiclustersfile, String genefiledelimiter, String mapconditionstobiclustersfile, String conditionfiledelimiter) throws IOException{
		
		if(mapgenestobiclustersfile!=null && mapconditionstobiclustersfile!=null){
			String splitrowfileby="\\s+";
			if(genefiledelimiter!=null)
				splitrowfileby=genefiledelimiter;
			String splitcolumnfileby="\\s+";
			if(conditionfiledelimiter!=null)
				splitcolumnfileby=conditionfiledelimiter;
			
			rhoSim=MTUReadUtils.readIntegerMatrixFromFile(mapgenestobiclustersfile, splitrowfileby);
			kappaSim=MTUReadUtils.readIntegerMatrixFromFile(mapconditionstobiclustersfile, splitcolumnfileby);
			if(rhoSim[0].length==kappaSim[0].length)
				K1=rhoSim[0].length;
			else
				throw new IOException("The number of bicluster must be the same in  binary files of the gene and condition files");
		}
	}
	
	/**
	 * Addlambda.
	 *
	 * @param lambda the lambda
	 * @return the penalized plaid method
	 */
	public PenalizedPlaidMethod addlambda(double lambda){
		setlambda(lambda);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				NUMBERBICLUSTERSESTIMATE,
				MODELTODATA,
				MCMCSAMPLES,
				BURNINSAMPLES,
				PPLAMBDA
		};
		
		String[] defaultvalues=new String[]{"5","GPE","1000","1000","0"};
		
		String[] comments=new String[] {
				"Is the number of biclusters to estimate",
				"Is the model used for the data. We have basically 4 methods: \n"
				+ "1) GPE (penalized plaid model with the sampling of the penalty parameter,lambda. The model is fitted with a Gibbs sampling); \n"
				+ "2) GPF (the penalized model with a fix value of the penalty parameter lambda and fitted with the  Gibbs sampling procedure. When  lambda=0, the plaid modeldoes not assume "
				+ "any constraint on the amount of overlapping genes and conditions between biclusters. When lambda tends to infinity (lambda>=10^3 is recommended to speed up the algorithm) "
				+ "Authors assume that biclusters do not overlap as with the Cheng and Church model); \n"
				+ "3) MPE (the penalized model fitted with a Metropolis-Hastings procedure); \n"
				+ "4) MPF (the penalized model with a fix value of lambda and fitted with a Metropolis-Hastings procedure)",
				
				
				"Is the number of MCMC samples after the burn in. Authors recommend more than 1000 samples",
				"Is the number of burn-in samples. Authors recommend a number more than 1000",
				"If the models are GPF or MPF, is necessary to specify a fix value of lambda"
				
		};
		
		String source="source:  http://www.dms.umontreal.ca/~murua/software/README.txt";
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments, source);
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		this.K=PropertiesUtilities.getIntegerPropertyValue(props,NUMBERBICLUSTERSESTIMATE,5,getClass());
		this.model=PPlaidModelData.getPenalizedPlaidModelFromString(PropertiesUtilities.getStringPropertyValue(props, MODELTODATA, PPlaidModelData.GPE.toString(), getClass()));
		this.sample=PropertiesUtilities.getIntegerPropertyValue(props,MCMCSAMPLES,1000,getClass());
		this.burninsample=PropertiesUtilities.getIntegerPropertyValue(props,BURNINSAMPLES,1000,getClass());
		this.lambda=PropertiesUtilities.getDoublePropertyValue(props, PPLAMBDA, 0.0, getClass());
		
	}
	
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {
		try {
			loadExpressionDataInfo();
			execute();
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error executing "+getAlgorithmName()+": ", e);
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		this.listofbiclusters=new BiclusterList();
		
		if(rho_output.length>0 && kappa_output.length>0){
			for (int i = 0; i < K; i++) {
				ArrayList<Integer> bicgeneindex=new ArrayList<>();
				for (int j = 0; j < rho_output.length; j++) {
					int gv=rho_output[j][i];
					if(gv==1)
						bicgeneindex.add(j);
				}
			
				ArrayList<Integer> biccondindex=new ArrayList<>();
				for (int e = 0; e < kappa_output.length; e++) {
					int cv=kappa_output[e][i];
					if(cv==1)
						biccondindex.add(e);
				}
				
				if(bicgeneindex.size()>0 && biccondindex.size()>0){
					BiclusterResult bicres=new BiclusterResult(expressionset, bicgeneindex, biccondindex, true);
					listofbiclusters.add(bicres);
				}
			}
		
			listofbiclusters.addAditionalBiclusterMethodInformation(AICPARAMETER, AIC);
			listofbiclusters.addAditionalBiclusterMethodInformation(DICPARAMTER, DICc);
		
			if (model.equals(PPlaidModelData.MPE) || model.equals(PPlaidModelData.GPE))
				listofbiclusters.addAditionalBiclusterMethodInformation(LAMBDAESTIMATE, moylambda);
		
			if(saveadditionalinfo){
				listofbiclusters.addAditionalBiclusterMethodInformation(ALPHAESTIMATE, alpha_max);
				listofbiclusters.addAditionalBiclusterMethodInformation(BETAESTIMATE, beta_max);
				listofbiclusters.addAditionalBiclusterMethodInformation(MUESTIMATE, mu_max);
				
				if(K1>0)
					listofbiclusters.addAditionalBiclusterMethodInformation(F1MEASUREPARAMETER, meanF1Max);
			
			}
		}
		else{
			if(!(rho_output.length>0))
				throw new Exception(getAlgorithmName()+" was unable to find biclusters for genes");
			else if(!(kappa_output.length>0))
				throw new Exception(getAlgorithmName()+" was unable to find biclusters for conditions");
				
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getRunningTime()
	 */
	@Override
	protected String getRunningTime() {
		return runningtime;
	}
	
	/**
	 * Load expression data info.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadExpressionDataInfo() throws IOException{
		if(expressionset!=null){
			
			this.n=expressionset.numberGenes();
			this.p=expressionset.numberConditions();
			this.data=expressionset.getexpressionDataMatrix();
		
		}
		else
			throw new IOException("Empty dataset");
	}
	
	
	/**
	 * Execute.
	 *
	 * @throws Exception the exception
	 */
	private void execute() throws Exception{
		
		Date starttime =Calendar.getInstance().getTime();
		
		double[][] data1 = new double[n][p];
		double[] lik = new double[sample + burninsample];
		double predict_lik = 0;
		rho_output = new int[n][K];
		kappa_output= new int[p][K];
		int[][] rho = new int[n][K];
		int[][] kappa = new int[p][K];
		double[] sigma2 = new double[K];

		double[][] alpha = new double[n][K];
		double[][] beta = new double[p][K];
		alpha_max = new double[n][K];
		beta_max = new double[p][K];
		double[][] alpha1 = new double[K][n];
		int[][] rho1 = new int[K][n];
		double[] mu = new double[K];
		mu_max = new double[K][1];
		double[][] beta1 = new double[K][p];
		int[][] kappa1 = new int[K][p];
		double sigma22 = 0;
		double mu0 = 0;
		double sigma20 = 0;
		int NumberSeedInit = 1;
		int[][][] RhoKappaInitial = InitialValuesPenalized.RhoKappaInitials(n, p, K, NumberSeedInit, data);
		for (int i = 0; i < n; i++) {
			rho[i] = Arrays.copyOf(RhoKappaInitial[0][i], K);
		}

		for (int j = 0; j < p; j++) {
			kappa[j] = Arrays.copyOf(RhoKappaInitial[1][j], K);
		}
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < n; i++) {
				rho1[k][i] = rho[i][k];
			}
			for (int j = 0; j < p; j++) {
				kappa1[k][j] = kappa[j][k];
			}
			int[] indrho = mcmcMethods.IndicesLabelsNonNuls(rho1[k]);
			int[] indkappa = mcmcMethods.IndicesLabelsNonNuls(kappa1[k]);
			if (indrho.length * indkappa.length > 0) {
				double[] mualpha = InitialValuesPenalized.muAlphaIni(n, p, data, rho1[k], kappa1[k]);
				double[] mubeta = InitialValuesPenalized.muBetaIni(n, p, data, rho1[k], kappa1[k]);
				for (int i = 0; i < indrho.length; i++) {
					alpha1[k][indrho[i]] = mualpha[i];
					alpha[indrho[i]][k] = mualpha[i];
				}
				for (int j = 0; j < indkappa.length; j++) {
					beta1[k][indkappa[j]] = mubeta[j];
					beta[indkappa[j]][k] = mubeta[j];
				}
			}

		}
		for (int k = 0; k < K; k++) {
			mu[k] = InitialValuesPenalized.muInit(n, p, data, rho1[k], kappa1[k]);
		}
		mu0 = InitialValuesPenalized.mu0Init(n, p, K, data, rho, kappa);
		sigma22 = InitialValuesPenalized.sigma2Init(n, p, K, data, alpha, beta, mu, mu0, rho, kappa);
		sigma20 = InitialValuesPenalized.sigma20InitCC(n, p, K, data, mu0, rho, kappa);
		for (int k = 0; k < K; k++) {
			sigma2[k] = InitialValuesPenalized.sigma2InitCC(n, p, data, alpha1[k], beta1[k], mu[k], rho1[k], kappa1[k]);
		}

		
		double alpha_lambda = 16;
		double beta_lambda = 8;
		double[][] moyrho = new double[n][K];
		double[][] moykappa = new double[p][K];
		double[][] moyalpha = new double[n][K];
		double[][] moybeta = new double[p][K];
		double[] moymu = new double[K];
		double moysigma2 = 0;
		double moysigma20 = 0;
		double moymu0 = 0;
		double[] logPrior = new double[sample + burninsample];
		double[] LogPosterior = new double[sample + burninsample];
		double maxlik = -Double.MAX_VALUE;
		double maxpost = -Double.MAX_VALUE;
		int indexMaxLik = 0;
		int indexMaximumPost = 0;
		int kappa_max[][] = new int[p][K];
		int rho_max[][] = new int[n][K];
		for (int t = 0; t < (sample + burninsample) && run; t++) {
			// if ((model.equals("Metropolis_CC"))||(model.equals("Gibbs_CC"))){
			if (lambda >= Math.pow(10, 3)) {
				sigma20 = mcmcMethods.sigma20CC(n, p, sigma20, K, data, mu0, rho, kappa, s20_sigma20, nu0);
				mu0 = mcmcMethods.mu0(n, p, K, data, sigma20, rho, kappa, sigma2mu0);
				if (t >= burninsample) {
					moysigma20 += sigma20 / sample;
				}
			} else {
				sigma22 = mcmcMethods.sigma2Plaid(n, p, K, data, mu, mu0, alpha, beta, rho, kappa, s20_sigma2, nu0);
				mu0 = mcmcMethods.mu0(n, p, K, data, sigma22, rho, kappa, sigma2mu0);
				if (t >= burninsample) {
					moysigma2 += sigma22 / sample;
				}
			}

			if (t >= burninsample) {
				moymu0 += mu0 / sample;
			}
			for (int k = 0; k < K && run; k++) {
				data1 = mcmcMethods.data1(lambda, n, p, K, k, data, mu, alpha, beta, rho, kappa);
				// if
				// ((model.equals("Metropolis_CC"))||(model.equals("Gibbs_CC"))){
				if (lambda >= Math.pow(10, 3)) {
					sigma2[k] = mcmcMethods.sigma2CC(n, p, data1, mu[k], alpha1[k], beta1[k], rho1[k], kappa1[k],
							s20_sigma2, nu0);
				} else {
					sigma2[k] = sigma22;
				}
				alpha1[k] = mcmcMethods.alpha(n, p, data1, sigma2[k], rho1[k], kappa1[k], sigma2alpha);
				beta1[k] = mcmcMethods.beta(n, p, data1, sigma2[k], rho1[k], kappa1[k], sigma2beta);
				mu[k] = mcmcMethods.mu(n, p, data1, mu[k], sigma2[k], rho1[k], kappa1[k], sigma2mu);
				for (int i = 0; i < n; i++) {
					int rho0 = rho1[k][i];
					if (model.equals(PPlaidModelData.MPF) || model.equals(PPlaidModelData.MPE)) {
						rho1[k][i] = mcmcMethods.proposalRho(model.toString(), p, K, i, k, lambda, data1, sigma2[k], sigma20, mu0,
								rho, kappa);
						rho1[k][i] = mcmcMethods.AcceptanceRho(p, data1, i, sigma2[k], mu[k], alpha1[k], beta1[k],
								rho1[k][i], rho0, kappa1[k]);
					} else {
						rho1[k][i] = mcmcMethods.GibbsRho(model.toString(), n, p, K, i, k, lambda, data1, sigma2[k], sigma20, mu0,
								mu[k], alpha1[k][i], beta1[k], rho, kappa);
					}

					rho[i][k] = rho1[k][i];
					alpha[i][k] = alpha1[k][i];

					if (t >= burninsample) {
						moyrho[i][k] += (double) rho[i][k] / sample;
						moyalpha[i][k] += alpha[i][k] / sample;
					}
				}
				for (int j = 0; j < p; j++) {
					int kappa0 = kappa1[k][j];
					if (model.equals(PPlaidModelData.MPF) || model.equals(PPlaidModelData.MPE)) {
						kappa1[k][j] = mcmcMethods.proposalKappa(model.toString(), n, K, j, k, lambda, data1, sigma2[k], sigma20,
								mu0, rho, kappa);
						kappa1[k][j] = mcmcMethods.AcceptanceKappa(n, data1, j, sigma2[k], mu[k], alpha1[k], beta1[k],
								rho1[k], kappa1[k][j], kappa0);
					} else {
						kappa1[k][j] = mcmcMethods.GibbsKappa(model.toString(), n, p, K, j, k, lambda, data1, sigma2[k], sigma20,
								mu0, mu[k], alpha1[k], beta1[k][j], rho, kappa);
					}

					if (t >= burninsample) {
						moymu[k] += mu[k] / sample;
					}
					kappa[j][k] = kappa1[k][j];
					beta[j][k] = beta1[k][j];
					if (t >= burninsample) {
						moykappa[j][k] += (double) kappa[j][k] / sample;
						moybeta[j][k] += beta[j][k] / sample;
					}
				}
			}
			if (model.equals(PPlaidModelData.MPE) || model.equals(PPlaidModelData.GPE)) {

				double lambda_old = lambda;

				// if (fixeLambda==0){
				if (model.equals(PPlaidModelData.GPE) || model.equals(PPlaidModelData.MPE)) {
					double lambda_proposal = mcmcMethods.lambda(n, p, K, alpha_lambda, beta_lambda, rho, kappa);
					lambda = mcmcMethods.acceptancelambda(n, p, K, lambda_old, lambda_proposal);
					if (t >= burninsample) {
						moylambda += lambda / sample;
					}
				}
				// else {
				// lambda=fixeLambda;}
			}
			int divsample = (sample + burninsample) / 5;
			if (t % divsample == 0) {
				LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Number of MCMC iterations is " + t);
				//System.out.println("Number of MCMC iterations is " + t);
			}
			// if ((model.equals("Metropolis_CC"))||(model.equals("Gibbs_CC"))){
			if (lambda >= Math.pow(10, 3)) {
				lik[t] = mcmcMethods.likelihoodCC(n, p, K, data, rho, kappa, alpha, beta, mu, mu0, sigma2, sigma20);
			} else {
				lik[t] = mcmcMethods.likelihoodPlaid(n, p, K, data, rho, kappa, alpha, beta, mu, mu0, sigma22);
				logPrior[t] = mcmcMethods.LogPriorDistribution(n, p, K, data, rho, kappa, alpha, beta, mu, mu0, sigma22,
						lambda, sigma2alpha, sigma2beta, sigma2mu, sigma2mu0, alpha_lambda, beta_lambda, nu0,
						s20_sigma2, model.toString());
			}
			LogPosterior[t] = lik[t] + logPrior[t];

			if (t >= burninsample) {
				predict_lik += lik[t] / sample;
			}

			if (t >= burninsample) {
				while (lik[t] > maxlik) {
					maxlik = lik[t];
					indexMaxLik = t;
				}
				while (LogPosterior[t] > maxpost) {
					maxpost = LogPosterior[t];
					indexMaximumPost = t;
				}
			}

			if (t == indexMaximumPost) {
				for (int k = 0; k < K; k++) {
					mu_max[k][0] = mu[k];
					for (int i = 0; i < n; i++) {
						rho_max[i][k] = rho1[k][i];
						alpha_max[i][k] = alpha[i][k];
					}

					for (int j = 0; j < p; j++) {
						kappa_max[j][k] = kappa1[k][j];
						beta_max[j][k] = beta[j][k];
					}
				}
			}
		} // end of the t=0 loop

		for (int k = 0; k < K; k++) {
			for (int i = 0; i < n; i++) {
				if (moyrho[i][k] >= 0.5) {
					rho_output[i][k] = 1;
				}
			}
			for (int j = 0; j < p; j++) {
				if (moykappa[j][k] >= 0.5) {
					kappa_output[j][k] = 1;
				}
			}
		}

		//String Namefile = "." + model.toString() + "." + "K" + String.valueOf(K) + ".txt";
		// double[] MinusLik=new double[sample];

		// for (int l=0;l<sample;l++){
		// MinusLik[l]=-lik[l+burninsample];}

		AIC = -2 * lik[indexMaxLik] + (n + p - 1) * K * 2;
		DICc = -4 * predict_lik + 2 * lik[indexMaximumPost];
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("AIC: "+ AIC);
		LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("DIC Conditional: "+ DICc);

		
		if (K1>0) {
			meanF1Max = mcmcMethods.meanF1(K, rho_max, kappa_max, K1, rhoSim, kappaSim);
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("F1-measure: " + meanF1Max);
			
		}
		if (model.equals(PPlaidModelData.MPE) || model.equals(PPlaidModelData.GPE)) {
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("LambdaEstimate: "+ moylambda);
			//System.out.println("LambdaEstimate" + Namefile + "= " + moylambda);
		}
		
		Date endtime=Calendar.getInstance().getTime();
		long runtime=endtime.getTime()-starttime.getTime();	
		runningtime=MTUTimeUtils.getTimeElapsed(runtime);
		
		
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return null;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(NUMBERBICLUSTERSESTIMATE, K);
		parameters.setKeyStringValue(MODELTODATA, model.toString());
		parameters.setKeyintValue(MCMCSAMPLES, sample);
		parameters.setKeyintValue(BURNINSAMPLES, burninsample);
		parameters.setKeydoubleValue(PPLAMBDA, lambda);
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new PenalizedPlaidMethod( K, model, sample, burninsample, lambda);
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
		run=false;
		
	}


	
	
	
	
	

}
