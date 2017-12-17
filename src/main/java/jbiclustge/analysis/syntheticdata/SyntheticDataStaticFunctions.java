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
package jbiclustge.analysis.syntheticdata;

import java.io.IOException;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jbiclustge.analysis.syntheticdata.distribution.NormalDistributionDataFactory;
import jbiclustge.analysis.syntheticdata.model.AbstractDataModelGeneratorMultiBiclustersType;
import jbiclustge.datatools.utils.nd4j.ND4JUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SyntheticDataStaticFunctions.
 */
public class SyntheticDataStaticFunctions {
	
	
	

	/**
	 * Make matrix.
	 *
	 * @param numberaxiselems the numberaxiselems
	 * @param elemssizevector the elemssizevector
	 * @param numboverlapelems the numboverlapelems
	 * @return the pair
	 */
	protected static Pair<INDArray, ArrayList<Pair<Integer, Integer>>> makeMatrix(int numberaxiselems, int[] elemssizevector, int numboverlapelems){

		
		int nbics=elemssizevector.length;

		INDArray initmat=Nd4j.zeros(numberaxiselems, nbics);
	    ArrayList<Pair<Integer, Integer>> ranges=new ArrayList<>();
	    
		int init=0;
		int end=0;
		for (int i = 0; i < nbics; i++) {
			if(i==0)
				init=0;
			else
				init=end-numboverlapelems;
			int nextrowsize=elemssizevector[i];
			end=init+nextrowsize;
			//System.out.println(init+"+ "+nextrowsize+" -->"+end);
			ranges.add(new Pair<Integer, Integer>(init, end));

			ND4JUtils.changeToDoubleFixValueInRange(initmat, i, true, init, end, 1);
		}
		//ND4JUtils.printINDArrayMatrix(initmat, "\t");
        return new Pair<INDArray, ArrayList<Pair<Integer, Integer>>>(initmat, ranges);
	}
	

	/**
	 * Make matrix asymmetric.
	 *
	 * @param numberaxiselems the numberaxiselems
	 * @param elemssizevector the elemssizevector
	 * @param numboverlapelems the numboverlapelems
	 * @return the pair
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static Pair<INDArray, ArrayList<Pair<Integer, Integer>>> makeMatrixAsymmetric(int numberaxiselems, int[] elemssizevector, int[] numboverlapelems) throws IOException{

		if(numboverlapelems.length<(elemssizevector.length-1))
			throw new IOException("Incorrect number of overlaps, the number of overlaps must be = "+(elemssizevector.length-1));
		else{
	  
			int nbics=elemssizevector.length;

			INDArray initmat=Nd4j.zeros(numberaxiselems, nbics);
			ArrayList<Pair<Integer, Integer>> ranges=new ArrayList<>();
	    
			int init=0;
			int end=0;
			for (int i = 0; i < nbics; i++) {
				if(i==0)
					init=0;
				else
					init=end-numboverlapelems[i-1];
				int nextrowsize=elemssizevector[i];
				end=init+nextrowsize;
			//System.out.println(init+"+ "+nextrowsize+" -->"+end);
				ranges.add(new Pair<Integer, Integer>(init, end));

				ND4JUtils.changeToDoubleFixValueInRange(initmat, i, true, init, end, 1);
			}
		//ND4JUtils.printINDArrayMatrix(initmat, "\t");
			return new Pair<INDArray, ArrayList<Pair<Integer, Integer>>>(initmat, ranges);
		}
	}
	
	
	
	
	/**
	 * Make row matrix.
	 *
	 * @param numbdatarows the numbdatarows
	 * @param shapebiclusters the shapebiclusters
	 * @param numboverlaprows the numboverlaprows
	 * @return the pair
	 */
	public static Pair<INDArray, ArrayList<Pair<Integer, Integer>>> makeRowMatrix(int numbdatarows, ArrayList<Pair<Integer, Integer>> shapebiclusters, int numboverlaprows){

		
		int nbics=shapebiclusters.size();
		
		int[] rowsperbicluster=new int[nbics];
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			rowsperbicluster[i]=shapebiclusters.get(i).getValue0();
			
		}
		
		return makeMatrix(numbdatarows, rowsperbicluster, numboverlaprows);
	}
	
	

	/**
	 * Make row matrix asymmetric overlap.
	 *
	 * @param numbdatarows the numbdatarows
	 * @param shapebiclusters the shapebiclusters
	 * @param rowoverlap the rowoverlap
	 * @return the pair
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Pair<INDArray, ArrayList<Pair<Integer, Integer>>> makeRowMatrixAsymmetricOverlap(int numbdatarows, ArrayList<Pair<Integer, Integer>> shapebiclusters, ArrayList<Integer> rowoverlap) throws IOException{

		if(rowoverlap.size()<(shapebiclusters.size()-1))
			throw new IOException("Incorrect number of elements in overlap row list, must be of size ="+(shapebiclusters.size()-1));
		else{
		
			int nbics=shapebiclusters.size();
		
			int[] rowsperbicluster=new int[nbics];
			int[] rowoverlapbybothbiclusters=new int[nbics-1];
		
			for (int i = 0; i < shapebiclusters.size(); i++) {
				rowsperbicluster[i]=shapebiclusters.get(i).getValue0();
				if(i<(shapebiclusters.size()-1))
					rowoverlapbybothbiclusters[i]=rowoverlap.get(i);
			}
		
			return makeMatrixAsymmetric(numbdatarows, rowsperbicluster, rowoverlapbybothbiclusters);
		}
	}
	

	
	/**
	 * Make row matrix.
	 *
	 * @param numbdatarows the numbdatarows
	 * @param numbbics the numbbics
	 * @param numbbicrows the numbbicrows
	 * @param numboverlaprows the numboverlaprows
	 * @return the IND array
	 */
	public static INDArray makeRowMatrix(int numbdatarows, int numbbics, int numbbicrows, int numboverlaprows){

		INDArray initmat=Nd4j.zeros(numbdatarows, numbbics);
		
		if(numbbicrows==0){
			numbbicrows=Math.floorDiv(numbdatarows, numbbics);
			if(numbbicrows==numbdatarows)
				numbbicrows=(int)numbdatarows/2;
		}
		   
		
		//System.out.println(numbbicrows);
		//ND4JUtils.printINDArrayMatrix(initmat, "\t");

		int init=0;
		int end=0;

		for (int i = 0; i < numbbics; i++) {
			init=(numbbicrows*i)-(numboverlaprows*i);
			end=init+numbbicrows;
			ND4JUtils.changeToDoubleFixValueInRange(initmat, i, true, init, end, 1);
		}
		//ND4JUtils.printINDArrayMatrix(initmat, "\t");
        return initmat;
	}
	
	
	
	/**
	 * Make column matrix.
	 *
	 * @param numbdatacolumns the numbdatacolumns
	 * @param numbbics the numbbics
	 * @param numbbiccols the numbbiccols
	 * @param numboverlapcols the numboverlapcols
	 * @return the IND array
	 */
	public static INDArray makeColumnMatrix(int numbdatacolumns, int numbbics, int numbbiccols, int numboverlapcols){
         return makeRowMatrix(numbdatacolumns, numbbics, numbbiccols, numboverlapcols);
	}
	
	
	

	/**
	 * Make column matrix.
	 *
	 * @param numbdatacolumns the numbdatacolumns
	 * @param shapebiclusters the shapebiclusters
	 * @param numboverlapcols the numboverlapcols
	 * @return the pair
	 */
	public static Pair<INDArray, ArrayList<Pair<Integer, Integer>>> makeColumnMatrix(int numbdatacolumns, ArrayList<Pair<Integer, Integer>> shapebiclusters, int numboverlapcols){

		
		int nbics=shapebiclusters.size();
		
		int[] colsperbicluster=new int[nbics];
		
		for (int i = 0; i < shapebiclusters.size(); i++) {
			colsperbicluster[i]=shapebiclusters.get(i).getValue1();
		}
		
		return makeMatrix(numbdatacolumns, colsperbicluster, numboverlapcols);
	}
	
	

	/**
	 * Make column matrix asymmetric overlap.
	 *
	 * @param numbdatacolumns the numbdatacolumns
	 * @param shapebiclusters the shapebiclusters
	 * @param columnoverlap the columnoverlap
	 * @return the pair
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Pair<INDArray, ArrayList<Pair<Integer, Integer>>> makeColumnMatrixAsymmetricOverlap(int numbdatacolumns, ArrayList<Pair<Integer, Integer>> shapebiclusters, ArrayList<Integer> columnoverlap) throws IOException{

		if(columnoverlap.size()<(shapebiclusters.size()-1))
			throw new IOException("Incorrect number of elements in overlap column list, must be of size ="+(shapebiclusters.size()-1));
		else{
		
			int nbics=shapebiclusters.size();
		
			int[] colsperbicluster=new int[nbics];
			int[] coloverlapbybothbiclusters=new int[nbics-1];
		
			for (int i = 0; i < shapebiclusters.size(); i++) {
				colsperbicluster[i]=shapebiclusters.get(i).getValue1();
				if(i<(shapebiclusters.size()-1))
					coloverlapbybothbiclusters[i]=columnoverlap.get(i);
			}
		
			return makeMatrixAsymmetric(numbdatacolumns, colsperbicluster, coloverlapbybothbiclusters);
		}
	}
	
	
	/**
	 * Make bicluster.
	 *
	 * @param nbicrows the nbicrows
	 * @param nbiccolumns the nbiccolumns
	 * @param basearray the basearray
	 * @param scalearray the scalearray
	 * @param shiftarray the shiftarray
	 * @return the IND array
	 */
	public static INDArray makeBicluster(int nbicrows,int nbiccolumns, double[] basearray, double[] scalearray, double[] shiftarray){
		
		
		/* if(basevector!=null && scalevector==null && shiftvector!=null){
			 scalevector=new double[nbicrows];
			 for (int i = 0; i < nbicrows; i++) {
				scalevector[i]=1.0;
			 }
		  }*/
		//INDArray biclustermatrix=null;
		INDArray bicdist=null;
		
		if(basearray!=null){
		    INDArray basevector=Nd4j.create(basearray);
		    INDArray scalevector=null;
		   
		    if(scalearray==null){
		    	scalevector=makeVectorWithValue(nbicrows, 1.0);
		    }
		    else
		    	scalevector=Nd4j.create(scalearray);
		    
		    bicdist=scalevector.transpose().mmul(basevector);
		}
		
		if(shiftarray!=null){
			
			if(bicdist!=null){
				INDArray rowshiftvector=Nd4j.create(shiftarray);
				INDArray rowshiftvectorreshape =rowshiftvector.reshape(rowshiftvector.length(), 1);
				return bicdist.addColumnVector(rowshiftvectorreshape);
			}
			else 
				return null;
			
			
		}
		else if(bicdist!=null)
			return bicdist;
		else{
			return Nd4j.create(nbicrows, nbiccolumns);
		}
		
		
	}
	
	
	/**
	 * Make vector with value.
	 *
	 * @param size the size
	 * @param value the value
	 * @return the IND array
	 */
	public static INDArray makeVectorWithValue(int size, double value){
		double[] array=new double[size];
		for (int i = 0; i < size; i++) {
			array[i]=value;
		}
		return Nd4j.create(array);
	}
	
	
	/**
	 * Gets the row highest dimension.
	 *
	 * @param shapebiclusters the shapebiclusters
	 * @return the row highest dimension
	 */
	public static int getRowHighestDimension(ArrayList<Pair<Integer, Integer>> shapebiclusters){
		int high=0;
		for (int i = 0; i < shapebiclusters.size(); i++) {
			int rowsize=shapebiclusters.get(i).getValue0();
			if(rowsize>high)
				high=rowsize;
		}
		return high;
	}
	
	/**
	 * Gets the column highest dimension.
	 *
	 * @param shapebiclusters the shapebiclusters
	 * @return the column highest dimension
	 */
	public static int getColumnHighestDimension(ArrayList<Pair<Integer, Integer>> shapebiclusters){
		int high=0;
		for (int i = 0; i < shapebiclusters.size(); i++) {
			int columnsize=shapebiclusters.get(i).getValue1();
			if(columnsize>high)
				high=columnsize;
		}
		return high;
	}
	
	
	/**
	 * Adds the noise to data.
	 *
	 * @param matrixtoaddnoise the matrixtoaddnoise
	 * @param noise the noise
	 * @return the IND array
	 */
	public static INDArray addNoiseToData(INDArray matrixtoaddnoise, double noise){
		
		NormalDistributionDataFactory normdist=new NormalDistributionDataFactory();
		normdist.setStandardDeviation(noise);
		
		INDArray noisematrix=normdist.generateDataMatrix(matrixtoaddnoise.rows(), matrixtoaddnoise.columns());
		//System.out.println(noisematrix);
		return matrixtoaddnoise.add(noisematrix);
		
	}
	
	
	/**
	 * Replace bicluster values in matrix.
	 *
	 * @param biclustersmatrix the biclustersmatrix
	 * @param bicluster the bicluster
	 * @param initrow the initrow
	 * @param endrow the endrow
	 * @param initcol the initcol
	 * @param endcol the endcol
	 */
	public static void replaceBiclusterValuesInMatrix(INDArray biclustersmatrix, INDArray bicluster,int initrow, int endrow, int initcol, int endcol){
		 
		 
		 int rowinc=0;
		 int colinc=0;
		 for (int i = initrow; i < endrow; i++) {
			 
			 for (int j = initcol; j < endcol; j++) {
				 //System.out.println(i+" -> "+j+" -> "+bicluster.getDouble(rowinc, colinc));
				 biclustersmatrix.putScalar(i, j, bicluster.getDouble(rowinc, colinc));
				 colinc++; 
			 }
			 colinc=0;
			 rowinc++;
		 }
		// System.out.println("\n\n");
	}
	
	/**
	 * Concatenate data with biclusters.
	 *
	 * @param datamatrix the datamatrix
	 * @param biclustermatrix the biclustermatrix
	 * @return the IND array
	 * @throws Exception the exception
	 */
	public static INDArray concatenateDataWithBiclusters(INDArray datamatrix, INDArray biclustermatrix) throws Exception{
		
		if(datamatrix.shapeInfoToString().equals(biclustermatrix.shapeInfoToString())){
			
			INDArray res=Nd4j.create(biclustermatrix.rows(), biclustermatrix.columns());
			
			for (int i = 0; i < biclustermatrix.rows(); i++) {
				
				INDArray row=biclustermatrix.getRow(i);
				for (int j = 0; j < row.length(); j++) {
					
					double value=row.getDouble(j);
					if(value!=0.0)
						res.putScalar(i, j, value);
					else
						res.putScalar(i, j, datamatrix.getDouble(i, j));
					
				}	
			}
			return res;	
		}
		else
			throw new Exception("Missmatch shape between both matrix");
	}
	
	

	/**
	 * Concatenate data with biclusters.
	 *
	 * @param datamatrix the datamatrix
	 * @param biclustermatrix the biclustermatrix
	 * @param modelgen the modelgen
	 * @param plaidpos the plaidpos
	 * @return the IND array
	 * @throws Exception the exception
	 */
	public static INDArray concatenateDataWithBiclusters(INDArray datamatrix, INDArray biclustermatrix, AbstractDataModelGeneratorMultiBiclustersType modelgen, int[] plaidpos) throws Exception{
		
		if(datamatrix.shapeInfoToString().equals(biclustermatrix.shapeInfoToString())){
			
			ArrayList<Integer> plaidrows=new ArrayList<>();
			ArrayList<Integer> plaidcolums=new ArrayList<>();
			
			
			if(plaidpos!=null && plaidpos.length>0){
				
				for (int i = 0; i < plaidpos.length; i++) {
					int plaidbic=plaidpos[i];
					
					Pair<Integer, Integer>rowrange =modelgen.getRowMatrixModelInfo().getValue1().get(plaidbic);
					Pair<Integer, Integer>colrange =modelgen.getColumnMatrixModelInfo().getValue1().get(plaidbic);
					
					//System.out.println(rowrange);
					
					for (int j = rowrange.getValue0(); j < rowrange.getValue1()+1; j++) {
						plaidrows.add(j);
					}
					
					//System.out.println(plaidrows);
					for (int j = colrange.getValue0(); j < colrange.getValue1()+1; j++) {
						plaidcolums.add(j);
					}
					
				}
			}
			
			
			
			
			INDArray res=Nd4j.create(biclustermatrix.rows(), biclustermatrix.columns());
			
			for (int i = 0; i < biclustermatrix.rows(); i++) {
				
				INDArray row=biclustermatrix.getRow(i);
				for (int j = 0; j < row.length(); j++) {
					
					double value=row.getDouble(j);
					
					if(plaidrows.contains(i)&& plaidcolums.contains(j)){
						res.putScalar(i, j, (datamatrix.getDouble(i, j)+value));
						//System.out.println("Adding ("+i+","+j+")");
					}
					else if(value!=0.0)
						res.putScalar(i, j, value);
					else
						res.putScalar(i, j, datamatrix.getDouble(i, j));
					
				}	
			}
			return res;	
		}
		else
			throw new Exception("Missmatch shape between both matrix");
	}
	
	
	
	/*public static INDArray makeMatrixModel(int numbdatarows, int numbdatacolumns, int numbbics,int numbbicrows, int numbbiccols, int numboverlaprows, int numboverlapcols){
		
		INDArray identmat=Nd4j.eye(numbbics);
		
		INDArray rowmat=makeRowMatrix(numbdatarows, numbbics, numbbicrows, numboverlaprows);
		INDArray colmat=makeColumnMatrix(numbdatacolumns, numbbics, numbbiccols, numboverlapcols);
		
		INDArray matonlyrow=rowmat.mmul(identmat);
		
		INDArray matboth=matonlyrow.mmul(colmat.transpose());
		//ND4JUtils.printINDArrayMatrix(fi, "\t");
		return matboth;
	}*/
	
	/*public static INDArray makeMatrixModel(int numbdatarows, int numbdatacolumns, ArrayList<Pair<Integer, Integer>> shapebiclusters, int numboverlaprows, int numboverlapcols){
	
		INDArray identmat=Nd4j.eye(shapebiclusters.size());
	
		INDArray rowmat=makeRowMatrix(numbdatarows,shapebiclusters, numboverlaprows);
		INDArray colmat=makeColumnMatrix(numbdatacolumns,shapebiclusters, numboverlapcols);
	
		INDArray matonlyrow=rowmat.mmul(identmat);
	
		INDArray matboth=matonlyrow.mmul(colmat.transpose());
	//ND4JUtils.printINDArrayMatrix(fi, "\t");
	return matboth;

	}*/
	
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		
		
		/*INDArray mat=makeRowMatrix(80, 5, 0, 2);
		ND4JUtils.printINDArrayMatrix(mat, "\t");
		
		System.out.println("\n\n");
		
		INDArray matcolumn=makeRowMatrix(50, 5, 0, 0);
		ND4JUtils.printINDArrayMatrix(matcolumn, "\t");*/
		
		/*INDArray mat=makeMatrixModel(20, 10, 3, 0, 0, 2, 2);
		ND4JUtils.printINDArrayMatrix(mat, "\t");*/
		
		/*ArrayList<Pair<Integer, Integer>> shapebiclusters =new ArrayList<>();
		shapebiclusters.add(new Pair<Integer, Integer>(8, 3));
		shapebiclusters.add(new Pair<Integer, Integer>(4, 7));
		Pair<INDArray, ArrayList<Integer>> res=makeRowMatrix(20, shapebiclusters, 2);
		System.out.println(res.getValue1());
		INDArray res=makeMatrixModel(20, 15, shapebiclusters, 0, 0);
		ND4JUtils.printINDArrayMatrix(res.getValue0(), "\t");*/
		
		/*double[][] mat1=new double[3][5];
		mat1[0]=new double[]{1,2,3,4,5};
		mat1[1]=new double[]{6,7,8,9,10};
		mat1[2]=new double[]{11,12,13,14,15};
		
		double[][] mat2=new double[3][5];
		mat2[0]=new double[]{100,200,0,0,0};
		mat2[1]=new double[]{400,500,0,0,0};
		mat2[2]=new double[]{0,0,0,0,600};

        INDArray m1=Nd4j.create(mat1);
        INDArray m2=Nd4j.create(mat2);
		
        INDArray m3=concatenateDataWithBiclusters(m1, m2);
		//INDArray noisezz=addNoiseToData(matt, 0.01);
		ND4JUtils.printINDArrayMatrix(m3, "\t");*/
		
		
		ArrayList<Pair<Integer, Integer>> shapebiclusters =new ArrayList<>();
		shapebiclusters.add(new Pair<Integer, Integer>(8, 3));
		shapebiclusters.add(new Pair<Integer, Integer>(4, 7));
		shapebiclusters.add(new Pair<Integer, Integer>(6, 5));
		
		ArrayList<Integer> rowoverlap=new ArrayList<>();
		rowoverlap.add(2);
		rowoverlap.add(3);
		Pair<INDArray, ArrayList<Pair<Integer, Integer>>> res=makeColumnMatrixAsymmetricOverlap(20, shapebiclusters, rowoverlap);
		//System.out.println(res.getValue1());
		ND4JUtils.printINDArrayMatrix(res.getValue0(), "\t");
		
		/*int[] elemssize=new int[]{8,4,6};
		int[] ovelap=new int[]{2,3,4};
		
		Pair<INDArray, ArrayList<Pair<Integer, Integer>>> t=makeMatrixAsymmetric(20, elemssize, ovelap);
		ND4JUtils.printINDArrayMatrix(t.getValue0(), "\t");*/

	}
	

}
