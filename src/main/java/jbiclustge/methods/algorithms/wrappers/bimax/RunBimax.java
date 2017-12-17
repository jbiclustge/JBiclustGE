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
package jbiclustge.methods.algorithms.wrappers.bimax;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

// TODO: Auto-generated Javadoc
/**
 * The Class RunBimax.
 */
public class RunBimax {
	

	/**
	 * The Interface BimaxWrapper.
	 */
	public interface BimaxWrapper extends Library{
		
		/** The Instance. */
		BimaxWrapper Instance =(BimaxWrapper) Native.loadLibrary("bimax", BimaxWrapper.class);
		
		/**
		 * Bimax.
		 *
		 * @param matrixbyvector the matrixbyvector
		 * @param nrows the nrows
		 * @param ncols the ncols
		 * @param minrows the minrows
		 * @param mincols the mincols
		 * @param bicrows the bicrows
		 * @param biccols the biccols
		 * @param nbics the nbics
		 */
		void bimax(Pointer matrixbyvector, IntByReference nrows, IntByReference ncols, IntByReference minrows, IntByReference mincols, Pointer bicrows, Pointer biccols, IntByReference nbics);

	}
	
	/** The binarymatrix. */
	private int[][] binarymatrix;
	
	/** The nrows. */
	private IntByReference nrows;
	
	/** The ncols. */
	private IntByReference ncols;
	
	/** The minrows. */
	private IntByReference minrows;
	
	/** The mincols. */
	private IntByReference mincols;
	
	/** The nbics. */
	private IntByReference nbics;
	
	/** The matrixalloc. */
	private Pointer matrixalloc;
	
	/** The bicrowsalloc. */
	private Pointer bicrowsalloc;
	
	/** The biccolsalloc. */
	private Pointer biccolsalloc;
	
	/** The rowsvectorsize. */
	private int rowsvectorsize;
	
	/** The colsvectorsize. */
	private int colsvectorsize;
	
	/** The rowsinbiclusters. */
	private int[][] rowsinbiclusters;
	
	/** The columnsinbiclusters. */
	private int[][] columnsinbiclusters;

	
	/**
	 * Instantiates a new run bimax.
	 *
	 * @param matrix the matrix
	 * @param minrows the minrows
	 * @param mincols the mincols
	 * @param nbiclusters the nbiclusters
	 */
	public RunBimax(int[][] matrix, int minrows, int mincols, int nbiclusters){
		this.binarymatrix=matrix;
		nrows=new IntByReference(matrix.length);
		ncols=new IntByReference(matrix[0].length);
		this.minrows=new IntByReference(minrows);
		this.mincols=new IntByReference(mincols);
		this.nbics=new IntByReference(nbiclusters);
		allocateMemory();

	}

	
	/**
	 * Gets the rowsinbiclusters.
	 *
	 * @return the rowsinbiclusters
	 */
	public int[][] getRowsinbiclusters() {
		return rowsinbiclusters;
	}


	/**
	 * Gets the columnsinbiclusters.
	 *
	 * @return the columnsinbiclusters
	 */
	public int[][] getColumnsinbiclusters() {
		return columnsinbiclusters;
	}


    /**
     * Execute.
     *
     * @throws Exception the exception
     */
    public void execute() throws Exception{
    	if(nbics.getValue()>0){
    		BimaxWrapper.Instance.bimax(matrixalloc, nrows, ncols, minrows, mincols, bicrowsalloc, biccolsalloc, nbics);
    		collectBiclusterRows();
    		collectBiclustersColumns();
    	}
    	else{
    		throw new Exception("Invalid maximum number of bicluster, must be higher than 0");
    	}
    }
    
    /**
     * Collect bicluster rows.
     */
    private void collectBiclusterRows(){
    	int[] v=bicrowsalloc.getIntArray(0, rowsvectorsize);
    	rowsinbiclusters=new int[nrows.getValue()][nbics.getValue()];
		int n=0;
		for (int i = 0; i < nbics.getValue(); i++) {
			for (int j = 0; j < nrows.getValue(); j++) {
				rowsinbiclusters[j][i]=v[n];
				n++;
			}
		}
    }
    
    
    /**
     * Collect biclusters columns.
     */
    private void collectBiclustersColumns(){
    	int[] v=biccolsalloc.getIntArray(0, colsvectorsize);
    	columnsinbiclusters=new int[ncols.getValue()][nbics.getValue()];
		int n=0;
		for (int i = 0; i < ncols.getValue(); i++) {
			for (int j = 0; j < nbics.getValue(); j++) {
				columnsinbiclusters[i][j]=v[n];
				n++;
			}
		}
    }


	/**
	 * Allocate memory.
	 */
	private void allocateMemory(){
		allocateMemoryBiclusterRows();
		allocateMemoryBiclusterColumns();
		this.matrixalloc=getMatrixPointer(binarymatrix);
	}
	

	/**
	 * Gets the matrix pointer.
	 *
	 * @param mat the mat
	 * @return the matrix pointer
	 */
	private Pointer getMatrixPointer(int[][] mat){
		
		int s=mat.length*mat[0].length;
		
		Pointer p=new Memory(s*Native.getNativeSize(Integer.TYPE));
		int n=0;
		int[] vector=new int[s];
		
		for (int i = 0; i < mat[0].length; i++) {
			for (int j = 0; j < mat.length; j++) {
				vector[n]=mat[j][i];
				n++;
			}
		}

		
		for (int i = 0; i < vector.length; i++) {
			p.setInt(i*Native.getNativeSize(Integer.TYPE), vector[i]);
		}
		return p;
		
	}
	
	/**
	 * Allocate memory bicluster rows.
	 */
	private void allocateMemoryBiclusterRows(){
		int[] vector=new int[nrows.getValue()*nbics.getValue()];
		Pointer p=new Memory(vector.length*Native.getNativeSize(Integer.TYPE));
		for (int i = 0; i < vector.length; i++) {
			p.setInt(i*Native.getNativeSize(Integer.TYPE), 0);
		}
		rowsvectorsize=vector.length;
		bicrowsalloc=p;
	}
	
	
	/**
	 * Allocate memory bicluster columns.
	 */
	private void allocateMemoryBiclusterColumns(){
		int[] vector=new int[ncols.getValue()*nbics.getValue()];
		Pointer p=new Memory(vector.length*Native.getNativeSize(Integer.TYPE));
		for (int i = 0; i < vector.length; i++) {
			p.setInt(i*Native.getNativeSize(Integer.TYPE), 0);
		}
		colsvectorsize=vector.length;
		biccolsalloc=p;
	}
	
	
	/**
	 * Sets the and execute.
	 *
	 * @param matrix the matrix
	 * @param minrows the minrows
	 * @param mincols the mincols
	 * @param nbiclusters the nbiclusters
	 * @return the run bimax
	 * @throws Exception the exception
	 */
	public static RunBimax setAndExecute(int[][] matrix, int minrows, int mincols, int nbiclusters) throws Exception{
		RunBimax bimax=new RunBimax(matrix, minrows, mincols, nbiclusters);
	    bimax.execute();
	    return bimax;
	}
	

}
