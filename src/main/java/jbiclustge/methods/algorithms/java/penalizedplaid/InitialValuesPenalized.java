/**
 * 	Copyright 2014 Thierry Chekouo
 *  copying permission
 *  The program is distributed under the terms of the GNU General Public License
 *  URL sourcecode: http://www.dms.umontreal.ca/~murua/software/penalizedplaid.zip
 *  
 *  Code adaptations to JBiclust by Orlando Rocha
 */
package jbiclustge.methods.algorithms.java.penalizedplaid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

public class InitialValuesPenalized {
	
	
	
	public static int maxIndice(double[] array){
		double max=-Double.MAX_VALUE;
		int j=0;
		for (int i = 0; i < array.length; i++){
			while(array[i]>max){
				max=array[i];
				j=i;
			}
		}
		return j;
		}	
	
	
	public static int[][] RhoInitials(int n,int K, String fileRho0){
		int rho[][] =new int[n][K];	
		
		String linerho;
		ArrayList <String> contentsrho = new ArrayList<String>();
	    try{   
	    	BufferedReader rh = new BufferedReader(new FileReader(fileRho0));
	    	if (!rh.ready())
	            throw new IOException();
	    	while ((linerho = rh.readLine()) != null)
	    	        contentsrho.add(linerho);
	    }
	    catch (FileNotFoundException e){
	    	System.out.println("The file '" + fileRho0 +"' was not found.");
	    	System.exit(1);}  
	     catch (IOException e)
	     {
	    	 System.out.println(e);
	    	 System.exit(1);
	     }
	  
	     for (int i=0;i<n;i++){
			String[] tokrho = contentsrho.get(i).split("\\s+");
			for (int k=0;k<K;k++){
				rho[i][k]=(int) Double.parseDouble(tokrho[k]);
			}
		}
		 
	     return rho;
		}     

	public static double[][] ReadArrayText(int n, int K,String file) throws FileNotFoundException{
		double[][] array=new double[n][K];
		String linedat;
		ArrayList <String> contentskappa = new ArrayList<String>();
		try
		{
			BufferedReader dat = new BufferedReader(new FileReader(file));
			if  (!dat.ready())
            throw new IOException();
			while ((linedat = dat.readLine()) != null)
                contentskappa.add(linedat);

		} catch (FileNotFoundException e){
			System.out.println("The file '" + file +"' was not found.");    
			System.exit(1);	
		} catch (IOException er) {
			System.out.println(er);
			System.exit(1);
		}
		
		for (int i=0;i<n;i++){
			String[] tokdat = contentskappa.get(i).split("\\s+");
			for (int j=0;j<K;j++){
				array[i][j]= Double.parseDouble(tokdat[j]);
			}
        }

		return array;
	}    
 
    
    
    public static double[] muAlphaIni(int n,int p,double[][] data,int[]rho,int[]kappa){
    	int[] indrho=mcmcMethods.IndicesLabelsNonNuls(rho);
    	int I=indrho.length;
    	int J=mcmcMethods.IndicesLabelsNonNuls(kappa).length;
    	double[] moyalpha =new double[I];
    	double yIJ=mcmcMethods.sumIJ(n,p,data,rho,kappa)/(I*J);
    	for (int i=0;i<I;i++){
    		double yiJ=0;
    		int i1=indrho[i];
    		for (int j=0;j<p;j++){
    		yiJ=yiJ+kappa[j]*data[i1][j];	
    		}
    		yiJ=yiJ/J;
    		moyalpha[i]=(yiJ-yIJ);
    		}
    		return moyalpha;
    	}
    
    public static double[] muBetaIni(int n, int p,double[][] data,int[]rho,int[]kappa){
    	int[] indkappa=mcmcMethods.IndicesLabelsNonNuls(kappa);
    	int J=indkappa.length;
    	int I=mcmcMethods.IndicesLabelsNonNuls(rho).length;
    	double[] moybeta =new double[J];
    	double yIJ=mcmcMethods.sumIJ(n,p,data,rho,kappa)/(I*J);
    	for (int j=0;j<J;j++){
    		double yIj=0;
    		int j1=indkappa[j];
    		for (int i=0;i<n;i++){
    			yIj=yIj+rho[i]*data[i][j1]/I;	
    		}
    		moybeta[j]=(yIj-yIJ);
    		}
    	return moybeta;
    	}
    
    	public static double muInit(int n, int p,double[][] data,int[]rho,int[]kappa){
    		double mu=0;
    		int I=mcmcMethods.IndicesLabelsNonNuls(rho).length;
    		int J=mcmcMethods.IndicesLabelsNonNuls(kappa).length;
    			for (int i=0;i<n;i++){
    			for (int j=0;j<p;j++){
    			     mu+= rho[i]*kappa[j]*data[i][j];
    			}
    			}
    			if (I*J>1){
    			mu=mu/(I*J);}
    		
    		return mu;
    	}
    	
    	public static double mu0Init(int n, int p,int K,double[][] data,int[][]rho,int[][]kappa){
    		double mu0=0;
    		int n0=0;
    			for (int i=0;i<n;i++){
    			for (int j=0;j<p;j++){
    				int gamma=1;
    				for (int k=0;k<K;k++){
    				gamma*=(1-rho[i][k]*kappa[j][k]);	
    				}
    			     mu0+=gamma*data[i][j];
    			     n0+=gamma;
    			}
    			
    		}
    		if (n0==0){return 0;}
    		else {
    		return mu0/n0;}
    	}
    
    	public static double sigma2Init(int n, int p,int K,double[][] data,double[][] alpha,double[][] beta,
    			double[] mu,double mu0,int[][]rho,int[][]kappa){
    		double s2=0;
    		
    		for (int i=0;i<n;i++){
    			for (int j=0;j<p;j++){
    				double theta=0;
    				int gamma=1;
    				for (int k=0;k<K;k++){
    					theta+=rho[i][k]*kappa[j][k]*(mu[k]+alpha[i][k]+beta[j][k]);
    					gamma*=(1-(rho[i][k]*kappa[j][k]));
    				}
    			     s2+= Math.pow(data[i][j]-mu0*gamma-theta, 2);
    			
    			}
    			
    		}
    		
    		return s2/(double)(n*p);
    	}

    	
    	public static double sigma2InitCC(int n, int p,double[][] data,double[] alpha,double[] beta,
    			double mu,int[]rho,int[]kappa){
    			double s2=0;
    			int I=mcmcMethods.IndicesLabelsNonNuls(rho).length;
    			int J=mcmcMethods.IndicesLabelsNonNuls(kappa).length;
    		for (int i=0;i<n;i++){
    			for (int j=0;j<p;j++){
    					s2+=rho[i]*kappa[j]*Math.pow(data[i][j]-mu-alpha[i]-beta[j],2);
    				}
    			}
    			s2=s2/(I*J);
    		
    		return  s2;
    	}
    	
    	public static double sigma20InitCC(int n, int p,int K,double[][] data, double mu0, 
    			int[][]rho,int[][]kappa){
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
    		
    		return s2/((n*p)-d);
    	}


    	

    	public static int[][][] RhoKappaInitials(int n,int p,int K, int seed,double[][]data) throws Exception{
                int label[][][]=new int[2][][];
                        label[0]=new int[n][K];
                        label[1]=new int[p][K];
                List< List<Double>> data1 = new ArrayList< List< Double >>(n);
                for (int i=0;i<n;i++){
                        List<Double> edg = new ArrayList< Double >(p);
                        for (int j=0;j<p;j++){
                                edg.add(data[i][j]);
                        }
                        data1.add(edg);
                }
                 Set<Integer> IndicesRho =  new HashSet<Integer>(n);
                 Set<Integer> IndicesKappa =  new HashSet<Integer>(p);
                for (int i=0;i<n;i++){IndicesRho.add(i);}
                for (int j=0;j<p;j++){IndicesKappa.add(j);}
                for (int k=0;k<K;k++){
                int isizeK=IndicesKappa.size();int isizeR=IndicesRho.size();
                        double[][] data2=new double[isizeR][isizeK];
                       
                        double[][] dat=new double[isizeK][isizeR];
                        Object[] IR=IndicesRho.toArray();
                        for (int i=0;i<isizeR;i++){
                                int i1=Integer.parseInt(IR[i].toString());
                                for (int j=0;j<isizeK;j++){
                                	data2[i][j]=data1.get(i1).get(j);                              
                                	dat[j][i]=data2[i][j];
                                }
                                
                        }
                        int kk1=K+1-k;
                        kk1=Math.min(kk1,Math.max(n/5,2));//The average number of genes must be greater than 5.



                        int[][] rho1=Kmeans2(kk1,seed,dat);
                        int kk2=K+1-k;
                        kk2=Math.min(kk2,Math.max(p/5,2));//The average number of conditions must be greater than 5.
                        int[][] kappa1=Kmeans2(kk2,seed,data2);
                        int m1=rho1[0].length; int m2=kappa1[0].length;
                        double[][] ratio=Ratio(rho1,kappa1,data2);
                        ArrayList<Double> ratiolist = new ArrayList<Double>();
                        for (int k1=0;k1<m1;k1++){
                        	for (int k2=0;k2<m2;k2++){
                        		ratiolist.add(ratio[k1][k2]);
                        	}
                        	
                        }
                        double[] ratiovec=new double[ratiolist.size()];
                        for (int k3=0;k3<ratiolist.size();k3++){
                        	ratiovec[k3]=ratiolist.get(k3);
                        }
                        
                        ArrayList<Integer> List = new ArrayList< Integer >();
                        for (int k1=0;k1<m1;k1++){
                        	for (int k2=0;k2<m2;k2++){
                        		//if (mcmcMethods.max(new Matrix(ratio).getColumnPackedCopy())==ratio[k1][k2]){
                        		if (mcmcMethods.max(ratiovec)==ratio[k1][k2]){
                        			for (int ind=0;ind<isizeR;ind++){
                        				int i=Integer.parseInt(IR[ind].toString());
                        				if (rho1[ind][k1]==1){label[0][i][k]=1;List.add(i);
                        				}
                        				
                        			}
                        			int p1=data[0].length;
                        			for (int j=0;j<p1;j++){
                        				if (kappa1[j][k2]==1){label[1][j][k]=1;}}
                        		}
                        		
                        	}
                        	
                        }
                        IndicesRho.removeAll(List);
                        if (isizeR<3){break;}
                	}
                return label;
    	}


    	public static int[][] Kmeans2 (int K,int seed, double[][] data) throws Exception{

    		int numDimensions=data.length;
    		int numInstances=data[0].length;
    		FastVector atts = new FastVector();
    		List<Instance> instances = new ArrayList<Instance>();
    		for(int dim = 0; dim < numDimensions; dim++){
    			Attribute current = new Attribute("Attribute" + dim, dim);
    			if(dim == 0){
    				for(int obj = 0; obj < numInstances; obj++){
    					instances.add(new SparseInstance(numDimensions));
    				}
    			}

    			for(int obj = 0; obj < numInstances; obj++){
    				instances.get(obj).setValue(current, data[dim][obj]);
    			}

    			// atts.add(current);
    			atts.addElement(current);
    		}

    		Instances newDataset = new Instances("Dataset", atts, instances.size());

    		for(Instance inst : instances)
    			newDataset.add(inst);
    		
    		SimpleKMeans kmeans = new SimpleKMeans();

    		kmeans.setSeed(seed);

    		// This is the important parameter to set
    		kmeans.setPreserveInstancesOrder(true);
    		kmeans.setNumClusters(K);
    		kmeans.buildClusterer(newDataset);

    		int[] assignments = kmeans.getAssignments();
    		int[][] rho=new int[assignments.length][K];
    		for (int i=0;i<assignments.length;i++){
    			for (int k=0;k<K;k++){
    				if (assignments[i]==k){rho[i][k]=1;}
    			}
    		}
    		return rho;
    	} 


    	
    	public static double[][] Ratio(int [][] rho, int [][] kappa, double[][] data){
    		int K1=rho[0].length;
    		int K2=kappa[0].length;
    		int n=rho.length;
    		int p=kappa.length;
    		double[][] ratio=new double[K1][K2];
    		int[][] rho1=new int[K1][n];
    		int[][] kappa1=new int[K2][p];
    		for (int i=0;i<n;i++){
    			for (int k1=0;k1<K1;k1++){
    				rho1[k1][i]=rho[i][k1];
    			}
    		}

    		for (int i=0;i<p;i++){
    			for (int k1=0;k1<K2;k1++){
    				kappa1[k1][i]=kappa[i][k1];
    			}
    		}

    		for (int k1=0;k1<K1;k1++){
    			int[] indrho=mcmcMethods.IndicesLabelsNonNuls(rho1[k1]);
    			int I=indrho.length;
    			for (int k2=0;k2<K2;k2++){
    				int[] indkappa=mcmcMethods.IndicesLabelsNonNuls(kappa1[k2]);
    				int J=indkappa.length;
    				double[][] data1=new double[I][J];
    				for (int i=0;i<I;i++){
    					for (int j=0;j<J;j++){
    						data1[i][j]=data[indrho[i]][indkappa[j]];
    					}
    				}
                 
    				double M_alpha=0;
                    double M_beta=0;
                    double E=0;
                    double yIJ=0;
                    for (int i=0;i<I;i++){
                       for (int j=0;j<J;j++){
                           yIJ+=data1[i][j];
                                        
                       }
                    }
                        
                    yIJ=yIJ/(I*J);
               
                    if ((I>1)&(J>1)){
                        for (int j=0;j<J;j++){
                            double yIj=0;
                            for (int i=0;i<I;i++){
                                yIj+=data1[i][j];
                            }
                                
                            yIj=yIj/I; M_beta+=Math.pow(yIj-yIJ,2);
                        }
                        
                        for (int i=0;i<I;i++){
                            double yiJ=0;
                        
                            for (int j=0;j<J;j++){
                                yiJ+=data1[i][j];
                                        
                            }
                                
                            yiJ=yiJ/J;
                         }
                        
                        for (int i=0;i<I;i++){
                        	double yiJ=0;
                        	for (int j=0;j<J;j++){
                        		yiJ+=data1[i][j];
                        	}
                        	
                        	yiJ=yiJ/J;
                        	M_alpha+=Math.pow(yiJ-yIJ,2);
                        	for (int j=0;j<J;j++){
                                double yIj=0;
                                for (int i1=0;i1<I;i1++){
                                    yIj+=data1[i1][j];
                                }
                                yIj=yIj/I;
                                E+=Math.pow(data1[i][j]-yiJ-yIj+yIJ, 2);
                        	}
                        }
                //E=(I*J)*E/((I-1)*(J-1));
                        E=E/((I-1)*(J-1));
                        double sigma2alpha=(1.0/J)*((J*M_alpha/(I-1))-E);
                        double sigma2beta=(1.0/I)*((I*M_beta/(J-1))-E);
                
                        ratio[k1][k2]=(sigma2alpha+sigma2beta)/E;}

                    	else{ratio[k1][k2]=-Math.pow(10,10);}
                //System.out.println("ratio="+ratio[k]);
    			}
    			
    		}
                
    		return ratio;

    	}



    	public static void WriteArrayText(String fileLabel ,double[][]array ){

    		FileWriter monFichier1 = null;
    		BufferedWriter tampon1 = null;
    		try {
    			monFichier1 = new FileWriter(fileLabel);
    			tampon1 = new BufferedWriter(monFichier1);
    			int n=array.length;
    			int K=array[0].length;

    			for (int l=0;l<n;l++){
    				for (int i=0;i<K;i++){

    					tampon1.write(String.valueOf(array[l][i])+" ");}
    				tampon1.newLine();
    			}
    		}catch (IOException exception) {
              exception.printStackTrace();
    		} finally {
              try {
            	  tampon1.flush();
                  tampon1.close();
                  monFichier1.close();
              } catch (IOException e1) {
                 e1.printStackTrace();
                     
              }
                   
    		}
    	}

    	
    	public static void SaveIntArray(String fileLabel ,int[][]array ){

    		FileWriter monFichier1 = null;
    		BufferedWriter tampon1 = null;
    		try {
    			monFichier1 = new FileWriter(fileLabel);
    			tampon1 = new BufferedWriter(monFichier1);
    			int n=array.length;
    			int K=array[0].length;

    			for (int l=0;l<n;l++){
    				for (int i=0;i<K;i++){
    					tampon1.write(String.valueOf(array[l][i])+" ");}
    				tampon1.newLine();
    			}
    		}catch (IOException exception) {
              exception.printStackTrace();
    		} finally {
              try {
            	  tampon1.flush();
                  tampon1.close();
                  monFichier1.close();
              } catch (IOException e1) {
            	  e1.printStackTrace();
                     
              }
                    
    		}
    	}


    	public static void saveDouble(String fileLabel ,double x ){

    		FileWriter monFichier1 = null;
    		BufferedWriter tampon1 = null;
    		try {
    			monFichier1 = new FileWriter(fileLabel);
    			tampon1 = new BufferedWriter(monFichier1);
    			tampon1.write(String.valueOf(x)+" ");
    		}catch (IOException exception) {
              exception.printStackTrace();
    		} finally {
              try {
            	  tampon1.flush();
            	  tampon1.close();
            	  monFichier1.close();

              } catch (IOException e1) {
            	  e1.printStackTrace();
              }
    		}
    	}

//Read some inputs

    	public static int ExtractInt(String str){

    		try{
    			int i = 0;
    			while (!Character.isDigit(str.charAt(i))) i++;
    			int j = i;
    			while ((j<str.length())&&(Character.isDigit(str.charAt(j)))) 
    				j++;
    			return Integer.parseInt(str.substring(i, j));
    		}catch(StringIndexOutOfBoundsException siobe){
    			System.out.println("You entered an invalid input: "+str);
    			System.exit(1);    
    			return 0;       
    		}
    	}

    	
    	public static double ExtractDouble(String str){
    		try{
    			int i = 0;
    			while (!Character.isDigit(str.charAt(i))) i++;
    			int j = i;
    			while ((j<str.length())&&((Character.isDigit(str.charAt(j)))||(String.valueOf(str.charAt(j)).equals("."))))
    				j++;
    			return Double.parseDouble(str.substring(i, j));
    		}catch(StringIndexOutOfBoundsException siobe){
    			System.out.println("You entered an invalid input: "+str);
    			System.exit(1);
    			return 0;
    		}
    	} 
  
 

}
