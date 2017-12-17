package jbiclustge.methods.algorithms.java.bibit;

import java.io.IOException;

public class BibitMatrix {
	
	public int [][] mArray=null;
	public int [][] mArray_dis=null;
	public int [][] DAux=null; 
	
	public double[] value;
	public int nRows=0;
	public int nCols=0;
	public int nConditions=0;
	public int extraCols=0; 
	public int patternSize=0;
	
	public BibitMatrix(double[][] expressionmatrix, int patternsize){
		this.patternSize=patternsize;
		loadMatrix(expressionmatrix);
		
	}
	
	private void loadMatrix(double[][] expressionmatrix){
		
	
		this.nRows=expressionmatrix.length;
		this.nCols=expressionmatrix[0].length;
		this.nConditions=nCols;
		int d=(nCols/patternSize)+1;
		extraCols=d;
		
	 	nCols=nCols+d;
	 	
	 	mArray=new int[nRows][nCols];
	 	mArray_dis=new int[nRows][nCols];
	 	DAux=new int[nRows][3]; // 15/10/2010 CHEVI
	 	
	 	
	 	for (int i = 0; i < expressionmatrix.length; i++) {
			
	 		double[] row=expressionmatrix[i];
	 		for (int j = 0; j < row.length; j++) {
				mArray[i][j]=(int)row[j];
				
			}
	 		
	 		
		}
	 	

	 	
	 	 for(int k=0;k<nRows;k++){
		    	for(int l=0;l<d;l++){
		    		
		    		mArray[k][nCols-(1+l)]=-1;
		    	}
		    }

	}
	
	
	public void discretizeMatrix(int value) throws IOException{
       
		for(int k=0;k<nRows;k++){
			
			DAux[k][1]=0;
			DAux[k][2]=-1;
			int pow=0;
			int numExtraCol=extraCols;
			int numPatternSize=0;
			int contOnes=0; 

          	for(int l=(nConditions-1);l>=0;l--){
          		
          		if (numPatternSize >(patternSize-1)){
          		   
          			mArray_dis[k][(nConditions -1)+numExtraCol]=pow;
          			numPatternSize=0;
          			numExtraCol=numExtraCol-1;
          			pow=0;
          			
          		}
          		
          		if (mArray[k][l]< value){
          			mArray_dis[k][l]=0;
          			pow=pow+0;
          		}
	    		else{
	    			mArray_dis[k][l]=1;
	    			pow=pow+(int)Math.pow(2, (numPatternSize));
	    			contOnes=contOnes+1; 
	    			
	    		}
	    		
	    		
          		
          		numPatternSize=numPatternSize+1;	
          	}
          	
          	mArray_dis[k][(nConditions -1)+numExtraCol]=pow;
          	
          	if (DAux[k][0]==contOnes){	
          		DAux[k][1]=1;
          	}
          	DAux[k][0]=contOnes;
        }
		
		

		for(int k=0;k<nRows;k++){
			if (DAux[k][0]>0){
			
				for(int l=k+1;l<nRows;l++){
					if(DAux[l][2]==-1 && DAux[l][0]>0){	
					
						boolean coincidence=true;
				
						int p=0;
						while(coincidence && p<extraCols){
						
							if (mArray_dis[l][nCols-extraCols+p]!=mArray_dis[k][nCols-extraCols+p])
								coincidence=false;
							else
								p=p+1;
						}
					
						if (coincidence){
							DAux[l][2]=k;
						}
					}	
				}
			}
		}
		
		
	}

}
