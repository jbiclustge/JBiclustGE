/**
 * 	Copyright 2014 Thierry Chekouo
 *  copying permission
 *  The program is distributed under the terms of the GNU General Public License
 *  URL sourcecode: http://www.dms.umontreal.ca/~murua/software/penalizedplaid.zip
 */

package jbiclustge.methods.algorithms.java.penalizedplaid;

import java.util.ArrayList;
import java.util.Random;

public class mcmcMethods {
	
	static Random gen = new Random(720);
	
	public static double[] NORMALiid(int n){
		
		double[] r = new double[n];
		for (int i=0;i<n;i++){
			r[i] = (double) gen.nextGaussian();
		}
		return r;
	}


	public static double[] MULTINORMAL1(double[] moy, double var) {
		int n0=moy.length;
		double[] norm=NORMALiid(n0);
		double[] result=new double[n0];
		result[0]=moy[0]+Math.sqrt(var)*norm[0]*Math.sqrt((double)n0/(n0+1));
		for (int i=1;i<n0;i++){
			double a=0;
			for (int j=0;j<i;j++){
				a=a+(1.0/(n0-j))*norm[j]*Math.sqrt((double)(n0-j)/(n0-j+1));}
			result[i]=moy[i]+Math.sqrt(var)*(norm[i]*Math.sqrt((double)(n0-i)/(n0-i+1))-a);
		}
	
		return result;
	}
	
	
	public static double sumIJ(int n,int p,double[][] data, int[] rho, int[] kappa){
		double sumIJ=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				sumIJ+=rho[i]*kappa[j]*data[i][j];
			}
		}
		return sumIJ;
	}
	
	
	public static int [] IndicesLabelsNonNuls(int[] Labels){
		ArrayList<Integer> IndicesLabels = new ArrayList<Integer>();
		for (int i=0;i<Labels.length;i++){
			if (Labels[i]==1) {IndicesLabels.add(i);}
			
		}
		Object Indices[]= IndicesLabels.toArray();
		int[] IndicesLabelsNonNul=new int[Indices.length];
		for(int i=0;i<Indices.length;i++)
			IndicesLabelsNonNul[i]=((Integer)Indices[i]).intValue();
		return  IndicesLabelsNonNul;
	}
	
	
	
	public static double[] alpha(int n, int p,double[][]data,double sigma2, int[]rho, int[]kappa, double sigma2alpha){	
		double[] alpha=new double[n];
		int I=IndicesLabelsNonNuls(rho).length;
		int J=IndicesLabelsNonNuls(kappa).length;
		int[] LabelsNonNulsRho=new int [I];
		double var=1.0/((J/sigma2)+(1.0/sigma2alpha));
		LabelsNonNulsRho=IndicesLabelsNonNuls(rho);
		double x=0;
		if ((I>=2)&(J>=2)){
			double[] moy=muAlpha(n,p,data,sigma2,rho,kappa,sigma2alpha);
			if(I==2){
				double moy0=muAlpha(n,p,data,sigma2,rho,kappa,sigma2alpha)[0];
				double sigm2=(1-(1.0/I))/((J/sigma2)+1/sigma2alpha);
				alpha[LabelsNonNulsRho[0]]=moy0+Math.sqrt(sigm2)*gen.nextGaussian();
				alpha[LabelsNonNulsRho[1]]=-alpha[LabelsNonNulsRho[0]];
			}
			else{
				double[] mult=MULTINORMAL1(moy,var);
				for (int i=0;i<I-1;i++){
					double multi=mult[i];
					alpha[LabelsNonNulsRho[i]]=multi;
					x+=multi;
				}
				
				alpha[LabelsNonNulsRho[I-1]]=-x;
			}
		}
        return alpha;	

	}


	public static double[] beta(int n, int p,double[][]data,double sigma2, int[]rho, int[]kappa, double sigma2beta){
        double[] beta =new double[kappa.length];
        int J=IndicesLabelsNonNuls(kappa).length;
        int I=IndicesLabelsNonNuls(rho).length;
        int[] LabelsNonNulsKappa=new int [J];
        double var=1.0/((I/sigma2)+1/sigma2beta);
        LabelsNonNulsKappa=IndicesLabelsNonNuls(kappa);
        
        if ((J>=2)&&(I>=2)){
        	double[] moy=muBeta(n,p,data,sigma2,rho,kappa,sigma2beta);
                if(J==2){
                        double moy0=moy[0];
                        double sigm2=(1-(1.0/J))/((I/sigma2)+1/sigma2beta);
                        beta[LabelsNonNulsKappa[0]]=moy0+Math.sqrt(sigm2)*gen.nextGaussian();
                        beta[LabelsNonNulsKappa[1]]=-beta[LabelsNonNulsKappa[0]];
                }
                else{
                	double[] mult=MULTINORMAL1(moy,var);
                	double x=0;
                	for (int j=0;j<J-1;j++){
                		double multj=mult[j];
                		beta[LabelsNonNulsKappa[j]]=multj;
                		x+=multj;
                	}
                	
                	beta[LabelsNonNulsKappa[J-1]]=-x;}
                }

        	return beta;
	}





	
	//parametres des distributions a posteriori
	public static double[] muAlpha(int n,int p,double[][] data, double sigma2,int[]rho,int[]kappa, double sigma2alpha){
		int I=IndicesLabelsNonNuls(rho).length;
        int J=IndicesLabelsNonNuls(kappa).length;
        double[] moyalpha =new double[I-1];
        double yIJ=sumIJ(n,p,data,rho,kappa)/(I*J);
        double co=(J/sigma2)/((J/sigma2)+(1/sigma2alpha));
        for (int i=0;i<I-1;i++){
                double yiJ=0;
                int i1=IndicesLabelsNonNuls(rho)[i];
                
                for (int j=0;j<p;j++){
                	yiJ+=kappa[j]*data[i1][j];
                }
                
                yiJ=yiJ/J;
                moyalpha[i]=(yiJ-yIJ)*co;
         }
         return moyalpha;
    }




	public static double[] muBeta(int n,int p,double[][] data, double sigma2,int[]rho,int[]kappa, double sigma2beta){
        
		int I=IndicesLabelsNonNuls(rho).length;
        int J=IndicesLabelsNonNuls(kappa).length;
        double[] moybeta =new double[J-1];
        double yIJ=sumIJ(n,p,data,rho,kappa)/(I*J);
        double co=(I/sigma2)/((I/sigma2)+(1/sigma2beta));
       
        for (int j=0;j<J-1;j++){
                double yIj=0;
                int j1=IndicesLabelsNonNuls(kappa)[j];
                for (int i=0;i<n;i++){
                	yIj+=rho[i]*data[i][j1];
                }
                yIj=yIj/I;
                //moybeta[j]=(yIj-yIJ)*(I/sigma2)/((I/sigma2)+(1/sigma2beta));
                moybeta[j]=(yIj-yIJ)*co;
        }
        return moybeta;
	}


	


//Parametres a posteriori de mu

	public static double sigma21(int[]rho,int[]kappa, double sigma2,double sigma2mu){
		int I=IndicesLabelsNonNuls(rho).length;
		int J=IndicesLabelsNonNuls(kappa).length;
		double sigm=1.0/((1.0/sigma2mu)+(I*J/sigma2));
		return sigm ;
	}

	public static double moymu(int n,int p,double[][] data, double sigma2,int[]rho,int[]kappa,double sigma2mu){
		double mu=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				mu+= rho[i]*kappa[j]*data[i][j];
		}
		
		}
		return	sigma21(rho,kappa,sigma2,sigma2mu)*(1.0/sigma2)*mu;
	
	}

	
	
	public static double mu(int n, int p,double[][] data,double mu1,double sigma2,int[]rho,int[]kappa,double sigma2mu){
		double mu=0;
		int I=IndicesLabelsNonNuls(rho).length;
		int J=IndicesLabelsNonNuls(kappa).length;
		if (I*J>1){
			mu=moymu(n,p,data,sigma2,rho,kappa,sigma2mu)+Math.sqrt(sigma21(rho,kappa,sigma2,sigma2mu))*gen.nextGaussian();}
		else{ mu=mu1;}
		return mu;	
	}

//variance a posteriori complete de mu0 

/*public static double sigma210(double sigma2){	
	double sigm=sigma2mu0*sigma2/(sigma2+(sigma2mu0*n*p));
	return sigm ;
	}*/
	public static double sigma210(int n,int p,int K,double sigma2, int[][]rho,int[][]kappa,double sigma2mu0){	
		int n0=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				int gamma=1;
				for (int k=0;k<K;k++){
					gamma*=(1-(rho[i][k]*kappa[j][k]));
				} 
				n0+=gamma;
			}
		}
		double sigm=sigma2mu0*sigma2/(sigma2+(sigma2mu0*n0));
		return sigm ;
	}
	
	

	
	public static double moymu0(int n,int p,int K,double[][] data, double sigma2,int[][]rho,int[][]kappa,double sigma2mu0){
		double mu0=0;
			for (int i=0;i<n;i++){
				for (int j=0;j<p;j++){
					int gamma=1;
					for (int k=0;k<K;k++){
						gamma*=(1-(rho[i][k]*kappa[j][k]));
					} 
					mu0+= (gamma*data[i][j]);
				}
		
			}
		
	   return mu0*sigma210(n,p,K,sigma2, rho,kappa,sigma2mu0)*(1.0/sigma2);
	}



	public static double mu0(int n,int p,int K,double[][] data, double sigma2,int[][]rho,int[][]kappa,double sigma2mu0){
		double mu0=0;
		mu0=moymu0(n,p,K,data,sigma2,rho,kappa,sigma2mu0)+Math.sqrt(sigma210(n,p,K,sigma2,rho,kappa,sigma2mu0))*gen.nextGaussian();
		return mu0;	
	}




//Variable d'echelle de sigma2
	public static double s2Plaid(int n,int p,int K,double[][] data, double[] mu, double mu0, double[][] alpha,
		double[][] beta,int[][]rho,int[][]kappa,double s20_sigma2,int nu0){
		double s2=0;
	
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				double theta=0;
				int gamma=1;
				for (int k=0;k<K;k++){
					theta+=rho[i][k]*kappa[j][k]*(mu[k]+alpha[i][k]+beta[j][k]);
					gamma*=(1-(rho[i][k]*kappa[j][k]));
				}
		     s2+= Math.pow(data[i][j]-(gamma*mu0)-theta, 2);
		
			}
		
		}
	
		return (s2+(nu0*s20_sigma2))/(double)(nu0+(n*p));
	}


	public static double s2CC(int n,int p,double[][] data, double mu, double[] alpha,double[] beta,int[]rho,int[]kappa,double s20_sigma2,int nu0){
		double s2=0;
		int I=IndicesLabelsNonNuls(rho).length;
		int J=IndicesLabelsNonNuls(kappa).length;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				s2+= rho[i]*kappa[j]*Math.pow(data[i][j]-mu-alpha[i]-beta[j], 2);
		
			}
		
		}
	
		return (s2+nu0*s20_sigma2)/(double)(nu0+(I*J));
	}




	public static double sigma2Plaid(int n,int p,int K,double[][] data, double[] mu, double mu0,double[][] alpha,double[][] beta,
		int[][]rho,int[][]kappa,double s20_sigma2,int nu0){
		int nu=nu0+n*p;
		double s2=s2Plaid(n,p,K,data,mu,mu0,alpha,beta,rho,kappa,s20_sigma2,nu0);
	//double sigma2=inversegammadist(((double)nu)/2.0, (nu*s2)/2.0);	
		double sigma2=1.0/sampleGamma(((double)nu)/2.0, (nu*s2)/2.0);	
		return sigma2;
	
	}


	public static double sigma2CC(int n,int p,double[][] data, double mu,double[] alpha,double[] beta,int[]rho,int[]kappa,double s20_sigma2,int nu0){
		int I=IndicesLabelsNonNuls(rho).length;
		int J=IndicesLabelsNonNuls(kappa).length;
		int nu=nu0+(int)(I*J);
		double s2=s2CC(n,p,data,mu,alpha,beta,rho,kappa,s20_sigma2,nu0);
//	double sigma2=inversegammadist(((double)nu)/2.0, (nu*s2)/2.0);	
		double sigma2=1.0/sampleGamma(((double)nu)/2.0, (nu*s2)/2.0);
		return sigma2;
	
	}


	public static double s20CC(int n,int p,int K,double[][] data, double mu0,int[][]rho,int[][]kappa,double s20_sigma20,int nu0){
		double s2=0;
		int d=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				int rk=0;
				for (int k=0; k<K;k++){
					rk+=rho[i][k]*kappa[j][k];
				}
				d+=rk;
				s2+= (1-rk)*Math.pow(data[i][j]-mu0, 2);
	
			}
		
		}
		return (s2+nu0*s20_sigma20)/(nu0+n*p-d);
	}

	public static double sigma20CC(int n,int p,double sigma2_old, int K,double[][] data, double mu0,int[][]rho,int[][]kappa,double s20_sigma20,int nu0){
		int d=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				for (int k=0; k<K;k++){
					d+=rho[i][k]*kappa[j][k];
				}
			}
		
		}
		if (d>1){return sigma2_old;}
		else{
			int nu=nu0+n*p-d;
			double s=s20CC(n,p,K,data,mu0,rho,kappa,s20_sigma20,nu0);
			//double g=inversegammadist(((double)nu)/2.0, (nu*s)/2.0);
			double g=1.0/sampleGamma(((double)nu)/2.0, (nu*s)/2.0);	
			return g;	
			}
	}


//variable d'echelle pour la variance sigma20 du non bicluster


	public static int GibbsRho(String model,int n,int p,int K,int i, int k,double lambda, double[][] data,
		double sigma2,double sigma20, 
		double mu0, double mu, double alpha, double[] beta, int[][]rho, int[][] kappa ){
		int rho1=0;
//	if ((model.equals("Gibbs_plaid"))||(model.equals("Gibbs_plaid_lambda"))){
		if ((model.equals("GPF"))||(model.equals("GPE"))){
			sigma20=sigma2;}
		int J=0;
		for (int j=0;j<p;j++){
			J+=kappa[j][k];
		}
		double B=0;
		double C=0;
		double A=0;
		for (int j=0;j<p;j++){
			int gamma=1;
			for (int k1=0;k1<K;k1++){
				if (k1!=k){
					gamma*=(1-(rho[i][k1]*kappa[j][k1]));}
			} 
			A+=kappa[j][k]*Math.pow(data[i][j]-mu-alpha-beta[j], 2);
			//if ((model.equals("Gibbs_plaid"))||(model.equals("Gibbs_plaid_lambda"))){
			if ((model.equals("GPF"))||(model.equals("GPE"))){	     
				B+=kappa[j][k]*((1-gamma)*Math.pow(data[i][j], 2)+gamma*Math.pow(data[i][j]-mu0,2));}
			//if (model.equals("Gibbs_CC")){
			if ((model.equals("GPF"))&&(lambda>=Math.pow(10,3))){		 
				B+=kappa[j][k]*(Math.pow(data[i][j]-mu0,2)); 
			}
			C+=lambda*kappa[j][k]*(1-gamma);
		}
	
		double B1=Math.exp((B/(2*sigma20))-C);
		double A1=Math.exp(-A/(2*sigma2));
		double prob=1.0/(1+((Math.pow(Math.sqrt(sigma20/sigma2), J))*A1*B1));
		//Random u = new Random( 19580427+i );
		if (gen.nextDouble()>prob){rho1=1;}
	
		return rho1;
	}


	public static int GibbsKappa(String model,int n,int p,int K,int j, int k,double lambda, double[][] data, 
		double sigma2, double sigma20, 
		double mu0, double mu, double[] alpha, double beta, int[][]rho, int[][] kappa ){
		int kappa1=0;
		//	if ((model.equals("Gibbs_plaid"))||(model.equals("Gibbs_plaid_lambda")))
		if ((model.equals("GPF"))||(model.equals("GPE")))
		{sigma20=sigma2;}
		int r=0;
		for (int i=0;i<n;i++){
			r+=rho[i][k];
		}
		double B=0;
		double C=0;
		double A=0;
		for (int i=0;i<n;i++){
			int gamma=1;
			for (int k1=0;k1<K;k1++){
				if (k1!=k){
					gamma*=(1-(rho[i][k1]*kappa[j][k1]));}
			} 
			A+=rho[i][k]*Math.pow(data[i][j]-mu-alpha[i]-beta, 2);
			// if ((model.equals("Gibbs_plaid"))||(model.equals("Gibbs_plaid_lambda"))){
			if ((model.equals("GPF"))||(model.equals("GPE"))){
				B+=rho[i][k]*((1-gamma)*Math.pow(data[i][j], 2)+gamma*Math.pow(data[i][j]-mu0,2));}
			//if (model.equals("Gibbs_CC")){
			if ((model.equals("GPF"))&&(lambda>=Math.pow(10,3))){	    
				B+=rho[i][k]*(Math.pow(data[i][j]-mu0,2));
			}
			C+=lambda*rho[i][k]*(1-gamma);
		}
	
		double B1=Math.exp((B/(2*sigma20))-C);
		double A1=Math.exp(-A/(2*sigma2));
		double prob=1.0/(1+((Math.pow(Math.sqrt(sigma20/sigma2), r))*(A1*B1)));
		//Random u = new Random( 19580427+i );
		if (gen.nextDouble()>prob){kappa1=1;}
	
		return kappa1;
	}


	public static int proposalRho(String model,int p,int K,int i, int k,double lambda, double[][] data, double sigma2, 
		double sigma20,	double mu0, int[][]rho, int[][] kappa ){
		int rho1=0;
		//	if ((model.equals("Metropolis_plaid"))||(model.equals("Metropolis_plaid_lambda")))
		if ((model.equals("MPF"))||(model.equals("MPE")))
		{sigma20=sigma2;}
		int J=0;
		for (int j=0;j<p;j++){
			J+=kappa[j][k];
		}
	
			double B=0;
			for (int j=0;j<p;j++){
				int gamma=1;
				for (int k1=0;k1<K;k1++){
					if (k1!=k){
						gamma*=(1-(rho[i][k1]*kappa[j][k1]));}
				}
				//if (model.equals("Metropolis_CC")){
				if ((model.equals("MPF"))&&(lambda>=Math.pow(10,3))){
					B+=kappa[j][k]*(Math.pow(data[i][j]-mu0, 2)-lambda*(1-gamma));}
				//if ((model.equals("Metropolis_plaid"))||(model.equals("Metropolis_plaid_lambda"))){
				if ((model.equals("MPF"))||(model.equals("MPE"))){
					B+=kappa[j][k]*((1-gamma)*Math.pow(data[i][j], 2)+
							gamma*Math.pow(data[i][j]-mu0,2)-lambda*(1-gamma));}
			}
			double  B1=Math.exp((1.0/(2*sigma20))*B);
			double proposal=1.0/(1+(Math.pow(Math.sqrt(sigma20/sigma2), J))*Math.exp(-J/2.0)*B1);
			if (gen.nextDouble()>proposal){rho1=1;}	
			return rho1;
		}
	

	public static int AcceptanceRho(int p,double[][] data, int i, double sigma2,
			double mu,double[] alpha, 
			double[] beta, int proposalRho, int rho, int[] kappa ){
		
			int J=IndicesLabelsNonNuls(kappa).length;
		
			double A=0;
			for (int j=0;j<p;j++){
			     A+=kappa[j]*Math.pow(data[i][j]-mu-alpha[i]-beta[j], 2);
			 }
			double A1=Math.exp(-(1.0/(2*sigma2))*A);
			double acceptance =Math.min(1, Math.exp((1.0/2.0)*(rho-proposalRho)*(-2*Math.log(A1)-J)));
			if (gen.nextDouble()<acceptance){rho=proposalRho;}
		
		
			return rho;
	
	}
	
	
	public static int proposalKappa(String model,int n, int K,int j,int k,double lambda,double[][] data,
		double sigma2,double sigma20,double mu0, int[][] rho,int[][] kappa ){
		int kappa1=0;
		//	if((model.equals("Metropolis_plaid"))||(model.equals("Metropolis_plaid_lambda")))
		if ((model.equals("MPF"))||(model.equals("MPE")))
		{sigma20=sigma2;}
		int r=0;
		for (int i=0;i<n;i++){
			r+=rho[i][k];
		}
	
		double B=0;
		
		for (int i=0;i<n;i++){
			int gamma=1;
			for (int k1=0;k1<K;k1++){
				if (k1!=k){
				gamma*=(1-(rho[i][k1]*kappa[j][k1]));}
			}
			//if ((model.equals("Metropolis_plaid"))||(model.equals("Metropolis_plaid_lambda"))){
			if ((model.equals("MPF"))||(model.equals("MPE"))){
				B+=rho[i][k]*((1-gamma)*Math.pow(data[i][j], 2)+gamma*Math.pow(data[i][j]-mu0,2)-lambda*(1-gamma));}
		
			//	if (model=="Metropolis_CC"){
			if ((model.equals("MPF"))&&(lambda>=Math.pow(10,3))){		
				B+=rho[i][k]*(Math.pow(data[i][j]-mu0, 2)-lambda*(1-gamma));
			}
		}
		
		double B1=Math.exp((B/(2*sigma20)));
		double proposal=1.0/(1+(Math.pow(Math.sqrt(sigma20/sigma2), r))*(Math.exp(-r/2.0)*B1));
		if (gen.nextDouble()>proposal){kappa1=1;}
			return kappa1;
		}
	
	
	
	
	
	public static int AcceptanceKappa(int n,double[][] data,int j, double sigma2, 
			double mu,double[] alpha, 
			double[] beta, int[] rho, int proposalkappa, int kappa ){
		
			int I=IndicesLabelsNonNuls(rho).length;
		
			double A=0;
			for (int i=0;i<n;i++){
			     A+=rho[i]*Math.pow(data[i][j]-mu-alpha[i]-beta[j], 2);
			     }
			double A1=Math.exp(-(1.0/(2*sigma2))*A);
			double acceptance =Math.min(1, Math.exp((1.0/2.0)*(kappa-proposalkappa)*(-2*Math.log(A1)-I)));
			if (gen.nextDouble()<acceptance){kappa=proposalkappa;}
	
			return kappa;
	
	}
	
	
	
	public static double lambda(int n,int p,int K,double alpha, double beta,int[][] rho, int[][]kappa){
		double lambda=0;
		double x=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				double e=0;
				int gamma=1;
				for (int k=0;k<K;k++){	
					e+=rho[i][k]*kappa[j][k];
					gamma*=(1-(rho[i][k]*kappa[j][k]));
				}	
		
				x+=Math.abs(1-gamma-e);
			}
		}
		//lambda=1/inversegammadist(alpha, x+beta);
		lambda=sampleGamma(alpha, x+beta);
		return lambda;
	}


	public static double acceptancelambda(int n,int p,int K,double lambda_old,double proposal_lambda){
		double lambda=0;
		double ratioZ=0;
		double la1=Math.exp(-lambda_old);
		double la2=Math.exp(-proposal_lambda);
		ratioZ=n*p*(lambda_old-proposal_lambda)+n*p*(Math.log(Math.pow(1+la1,K)-1+la1)-Math.log(Math.pow(1+la2,K)-1+la2));
		double x=gen.nextDouble();
		if (x>Math.min(1,Math.exp(ratioZ))){lambda=lambda_old;}
		else{lambda=proposal_lambda;}
		return lambda;
	}


	public static double[][] data1(double lambda,int n,int p,int K,int k, double[][] data, double mu[],double[][] alpha, double[][] beta, int[][] rho, int[][] kappa  ){
		//	if ((model.equals("Metropolis_CC"))||(model.equals("Gibbs_CC"))){return data;}
		if (lambda>=Math.pow(10,3)){return data;}
		else {
			double [][] data1=new double[n][p];
			for (int i=0;i<n;i++){
				rho[i][k]=0;
				for (int j=0;j<p;j++){
					double theta=0;		
					for (int k1=0;k1<K;k1++){
						theta+=rho[i][k1]*kappa[j][k1]*(mu[k1]+alpha[i][k1]+beta[j][k1]);
					}
					data1[i][j]=data[i][j]-theta;
					}
			}
			return data1;
			}
	}
	
	

	public static double likelihoodPlaid(int n,int p,int K,double[][] data,int[][] rho, int[][] kappa,
		double[][] alpha,double[][]beta, double[] mu, double mu0, double sigma2){
		
		double x=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				double e=0;
				int gamma=1;
				for (int k=0;k<K;k++){	
					e+=(mu[k]+alpha[i][k]+beta[j][k])*rho[i][k]*kappa[j][k];
					gamma*=(1-(rho[i][k]*kappa[j][k]));
				}	
	
				x+=Math.pow(data[i][j]-e-(mu0*gamma),2);
			}
		}
	
		return ((-n*p/2.0)*Math.log(2*Math.PI*sigma2))-(x/(2*sigma2));
	}





	public static double likelihoodCC(int n,int p,int K,double[][] data,int[][] rho, int[][] kappa,
		double[][] alpha,double[][]beta, double[] mu, double mu0, double[] sigma2, double sigma20){
	
		double x=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				double e=0;
		
				int gamma=1;
				for (int k=0;k<K;k++){	
					e+=(1/(2*sigma2[k]))*Math.pow((data[i][j]-(mu[k]+alpha[i][k]+beta[j][k])),2)*rho[i][k]*kappa[j][k]+
							(rho[i][k]*kappa[j][k]/2.0)*Math.log(2*Math.PI*sigma2[k]);
					gamma*=(1-(rho[i][k]*kappa[j][k]));
				}	
				x+=(1/(2*sigma20))*gamma*Math.pow(data[i][j]-mu0,2)+(gamma/2.0)*Math.log(2*Math.PI*sigma20)+e;
 
			}
		}
	
		return -x;
	}




	public static double  probLabels(int i, int j, int K, int[][][] rho, int[][][]kappa, ArrayList<Integer> biclust){
		int sample1=rho.length;
		double prob=0;
		for (int l=0;l<sample1;l++){
			double rhkap=1.0;
			for (int x=0;x<biclust.size();x++){
				rhkap*=rho[l][i][biclust.get(x)]*kappa[l][j][biclust.get(x)];
			}
			prob+=rhkap;
		}
		prob=prob/sample1;
	
		return prob;
		}



	public static double LOGDENSITY(int i, int j,int K,double[][] data,int[][][] rho, int[][][]kappa,
		double[]mu,double mu0,double sigma2,double[][]alpha,double[][]beta,ArrayList<Integer> biclust){

		int[] Indices=new int[biclust.size()]; 
		double prob= probLabels(i, j, K, rho, kappa,  biclust);
		for (int l=0;l<biclust.size();l++){
			Indices[l]=(Integer)biclust.get(l).intValue();}
	
		double LOG=0;
			double theta=0;
			 for (int l=0;l<biclust.size();l++){
				 theta+=mu[biclust.get(l)]+alpha[i][biclust.get(l)]+beta[j][biclust.get(l)];
			 }		
			 LOG=(-1/2.0)*Math.log(2*sigma2*Math.PI)-(1/(2*sigma2))*Math.pow(data[i][j]-theta,2)+Math.log(prob);
			
					
		return LOG;
	
	}



	static public double LOG_inversegammadist(double x,double alpha, double lambda){
                return alpha*Math.log(lambda)-(alpha+1)*Math.log(x)-log_Gamma(alpha)-(lambda/x);
        }


	static public double LOG_gammadist(double x,double alpha, double lambda){
                return alpha*Math.log(lambda)+(alpha-1)*Math.log(x)-log_Gamma(alpha)-(lambda*x);
        }

      
	
	
	static public double log_Gamma(double x){

                double[] p = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,
                                  771.32342877765313, -176.61502916214059, 12.507343278686905,
                                  -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
                int g = 7;
                if(x < 0.5) return Math.log(Math.PI)-Math.log (Math.sin(Math.PI * x))-log_Gamma(1-x);
                        //return Math.PI / (Math.sin(Math.PI * x)*Gamma(1-x));
 
                x -= 1;
                double a = p[0];
                double t = x+g+0.5;
                for(int i = 1; i < p.length; i++){
                        a += p[i]/(x+i);
                }
 
                //return Math.sqrt(2*Math.PI)*Math.pow(t, x+0.5)*Math.exp(-t)*a;
                return (1.0/2)*Math.log(2*Math.PI) + (x+0.5)*Math.log(t)-t+Math.log(a);
        
     }


	public static double LOGNORMAL(double x,double moy, double Sigma2) {
                double l=-((1.0/2)*Math.log(Sigma2*2*Math.PI))-((1.0/(2*Sigma2))*Math.pow(x-moy,2));
                return l;
        }


	
	
	public static double LOGMULTINORMAL1( double[] x,double[] moy, double sigma2) {
		int I=1+(x.length);
		double product=0;
		for (int i=0;i<x.length-1;i++){
			product+=2*Math.pow(x[i]-moy[i],2);
			for (int j=i+1;j<x.length;j++){
				product+=2*(x[i]-moy[i])*(x[j]-moy[j]);
			}
		}
		product=product+2*Math.pow(x[x.length-1]-moy[x.length-1],2);

		double A=((1.0/2.0)*Math.log(I))-(((I-1)/2.0)*Math.log(2*Math.PI*sigma2));
		double y=(1.0/(2.0*sigma2))*product;
		return A-y;

     }



	public static double LogPriorDistribution(int n,int p,int K,double[][] data,int[][] rho, int[][] kappa,
		double[][] alpha,double[][]beta, double[] mu, double mu0, double sigma2, double lambda,double sigma2alpha, double sigma2beta, double sigma2mu,double sigma2mu0,double alpha_lambda, double beta_lambda, int nu0,double s20_sigma2,String model){
	
		double X=0;
		double sig=LOG_inversegammadist(sigma2, nu0/2.0, nu0*s20_sigma2/2);
		double m0=LOGNORMAL(mu0, 0, sigma2mu0);
		double RhoKappaPrior=0;
		double LambdaPrior=LOG_gammadist(lambda, alpha_lambda,beta_lambda);
		for (int i=0;i<n;i++){
			for (int j=0;j<p;j++){
				int gamma=1;
				double e=0;
				for (int k=0;k<K;k++){	
					e+=rho[i][k]*kappa[j][k];
					gamma*=(1-(rho[i][k]*kappa[j][k]));
				}	
				RhoKappaPrior+=-lambda*Math.abs(1-gamma-e);
			}
		}



		for (int k=0;k<K;k++){
			int[] rho1=new int[n];
			double[] alpha1=new double[n];
			int[] kappa1=new int[p];
			double[] beta1=new double[p];
			for (int i=0;i<n;i++){
				rho1[i]=rho[i][k];
				alpha1[i]=alpha[i][k];
			}
			for (int j=0;j<p;j++){
				kappa1[j]=kappa[j][k];
				beta1[j]=beta[j][k];
			}
	
			int I=IndicesLabelsNonNuls(rho1).length;
			int J=IndicesLabelsNonNuls(kappa1).length;
			double alp=0;
			if (I>1) {
				double[] mo1=arrayNonZero(alpha1,rho1);
				double [] moy=new double[I-1];

				alp=LOGMULTINORMAL1(mo1, moy,sigma2alpha);
				
			}
	
			double bet=0;
			if (J>1) {
				double[] mo1=arrayNonZero(beta1,kappa1);
				double [] moy=new double[J-1];
				bet=LOGMULTINORMAL1(mo1, moy, sigma2beta);
				
			}
			
			double m=LOGNORMAL(mu[k], 0.0, sigma2mu);
			X+=alp+bet+m;
		}
		//if (fixeLambda!=0){ return X+sig+m0+RhoKappaPrior;}
		if ((model.equals("GPF"))||(model.equals("MPF"))){ return X+sig+m0+RhoKappaPrior;}
		else {return X+sig+m0+RhoKappaPrior+LambdaPrior;
		
		}
	}


        
	public static double[] arrayNonZero(double[] array, int[] rho){
         
		ArrayList<Double> arrayNZ = new ArrayList<Double>();
                
		for (int i=0;i<array.length;i++){
           if (rho[i]!=0) {arrayNZ.add(array[i]);
           
           }
		}
                
		double[] arra=new double[arrayNZ.size()-1];
                
		for (int j=0;j<arrayNZ.size()-1;j++){
            arra[j]=arrayNZ.get(j);
        }
                
		return  arra;
        
	}




	public static double meanF1(int K,int[][]rho,int[][]kappa, int K1,int[][]rho_known,int[][]kappa_known){
		double S=0;
		double[][] F1=new double[K][K1];
		double [] NA=new double[K];
		double [] NB=new double[K1];//known bicluster B 
		for (int k=0;k<K;k++){
			double c=0;
			double d=0;
			for (int i=0;i<rho.length;i++){
				c+=rho[i][k];}
			for (int j=0;j<kappa.length;j++){
				d+=kappa[j][k];
			}
			NA[k]=c*d;}
			
			for (int k1=0;k1<K1;k1++){
				double c1=0;
				double d1=0;
				for (int i=0;i<rho.length;i++){
					c1+=(double)rho_known[i][k1];
					
				}
				for (int j=0;j<kappa.length;j++){
					d1+=(double)kappa_known[j][k1];
				}
			
			NB[k1]=c1*d1;
		}
			for (int k=0;k<K;k++){
			for (int k1=0;k1<K1;k1++){
				double a=0;
				double b=0;
				for (int i=0;i<rho.length;i++){
					a+=(double)rho[i][k]*rho_known[i][k1];
				}
				for (int j=0;j<kappa.length;j++){
					b+=(double)kappa[j][k]*kappa_known[j][k1];	
				}
				if ((NB[k1]>0)&(NA[k]>0)){
					double recall=a*b/NB[k1];
					double precision=a*b/NA[k];
					if ((recall>0)&(precision>0)){
					F1[k][k1]=2.0/((1/recall)+(1/precision));}
					
				}
				
			}
			S+=max(F1[k]);
			
		}
		S=S/K;
		
		
		return S;
	}

        public static double max(double[] array){
                double max=-Double.MAX_VALUE;
                for (int i = 0; i < array.length; i++){
               
                	while(array[i]>max){
                		max=array[i];
                	}
                }
                
                return max;
                
        }

	
        public static double sampleGamma(double k, double theta) {

        	//f(x)\propto x^{k-1}*exp(-theta*x);	    
        	boolean accept = false;
        	if (k < 1) {
        		// Weibull algorithm
        		double c = (1 / k);
        		double d = ((1 - k) * Math.pow(k, (k / (1 - k))));
        		double u, v, z, e, x;
        		do {
        			u = gen.nextDouble();
        			v = gen.nextDouble();
        			z = -Math.log(u);
        			e = -Math.log(v);
        			x = Math.pow(z, c);
        			if ((z + e) >= (d + x)) {
        				accept = true;
        			}
        		} while (!accept);
        		return (x/theta);
        	} 
        	else {
	 // Cheng's algorithm
        		double b = (k - Math.log(4));
        		double c = (k + Math.sqrt(2 * k - 1));
        		double lam = Math.sqrt(2 * k - 1);
        		double cheng = (1 + Math.log(4.5));
        		double u, v, x, y, z, r;
        		do {
        			u = gen.nextDouble();
        			v = gen.nextDouble();
        			y = ((1 / lam) * Math.log(v / (1 - v)));
        			x = (k * Math.exp(y));
        			z = (u * v * v);
        			r = (b + (c * y) - x);
        			if ((r >= ((4.5 * z) - cheng)) || (r >= Math.log(z))) {
        				accept = true;
        			}
        		} while (!accept);
        		
        		return (x/theta);
        	}
        }
        
}



