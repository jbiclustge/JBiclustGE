package jbiclustge.methods.algorithms.java.bibit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.javatuples.Pair;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;

public class BibitUtilities {
	
	
	
	
	    /**
	     * This procedures stores in an array the number of ones of all the binary numbers that
	     * can be generated with the number of bits used in the encoding phase
	     * @param value
	     * @param v
	     */
		public static void numberOfOnes(int value, int[]v)
		{
			for(int i=0;i<value;i++)
			{
				
				String s=Integer.toString(i,2);
				int index=0;
				int cont=-1;
				while (index!=-1)
				{
					cont=cont+1;
					index=s.indexOf('1');
					s=s.substring(index+1, s.length());
									
				}
				v[i]=cont;
				
			}
			
			
		}
		
		/**
		 * This is the procedure that extract the biclusters from each binarization level. The patterns are extracted and, for every pattern, the rows compatible with it are added to a bicluster
		 * @param matrix_Aux
		 * @param mingenes
		 * @param minconditions
		 * @param distance
		 * @param file
		 * @param value
		 * @param Genes
		 * @param Rows
		 * @param patternSize
		 * @param write
		 * @return
		 * @throws Exception
		 */
		public static Pair<ArrayList<ArrayList<Integer>>, ArrayList<ArrayList<Integer>>> generateBiclusters(BibitMatrix matrix_Aux, int mingenes, int minconditions, int[] distance,int patternSize) throws Exception
		{
			
			int rows=matrix_Aux.nRows;
			int cols=matrix_Aux.nCols;
			int extraCols=matrix_Aux.extraCols;
	
			//SortedSet<String> patterns=new TreeSet<String>();
			ObjectRBTreeSet<String> patterns=new ObjectRBTreeSet<>();
			ArrayList<ArrayList<Integer>> geneindexes=new ArrayList<>();
			ArrayList<ArrayList<Integer>> conditionindexes=new ArrayList<>();
			int cont=0;
			for(int i=0;i<rows;i++)
			{
				
				
				if (matrix_Aux.DAux[i][0]>=minconditions && matrix_Aux.DAux[i][2]==-1) 
				{
					Integer motifI=new Integer(i);
					
					for(int j=i+1;j<rows;j++)
					{
						if (matrix_Aux.DAux[j][0]>=minconditions) 
						{
							Integer motifJ=new Integer(j);
							int andR[]=new int[extraCols];
							
							String prodPattern="";
							int prod=0;
							int num1=0;
							for(int k=0;k<extraCols;k++)
							{
						
								prod=matrix_Aux.mArray_dis[i][cols-extraCols+k]&matrix_Aux.mArray_dis[j][cols-extraCols+k];
								num1=num1+distance[prod];
								andR[k]=prod;
								
								prodPattern=prodPattern+String.valueOf(prod);
						
							}
							
							//SortedSet<Integer> motifs=new TreeSet<Integer>();
							IntAVLTreeSet motifs= new IntAVLTreeSet();
							
							if(num1>=minconditions && !(patterns.contains(prodPattern)))
							{
						
								patterns.add(prodPattern);
								motifs.add(motifI);
								motifs.add(motifJ);
								for(int k=0;k<rows;k++)
								{
									if (k!=i && k!=j)
									{
										boolean coincidence=true;
								
										int p=0;
										while(coincidence && p<extraCols) 
										{
											int r=andR[p] & matrix_Aux.mArray_dis[k][cols-extraCols+p];
											if (r!=andR[p])
												coincidence=false;
											else
												p=p+1;
									
										}
								
						
										if (coincidence)
										{
											Integer elem=new Integer(k);
											motifs.add(elem);
										}
								
									}
								}
								if(motifs.size()>=mingenes && motifs.size()<(cols-extraCols))
								{
									
									cont=cont+1;
									
									geneindexes.add(getGeneIndexes(motifs));
									conditionindexes.add(getConditionsIndexes(matrix_Aux.nConditions, andR, extraCols, patternSize));
							
								}
						
							}								
					}
				}		
			} 
					
		}
			return new Pair<ArrayList<ArrayList<Integer>>, ArrayList<ArrayList<Integer>>>(geneindexes, conditionindexes);
			
		}
		
		/**
		 * This procedure stores in an array the number of 1's of every bit word of x bits, being x the number of bits used for the encoding phase
		 * @param prod
		 * @param s
		 * @return
		 */
		static int giveNumberOfOnes(int prod, List s)
		{
			
			int result=0;
			int aux=prod;
			BibitBinary bAux=new BibitBinary(prod);
		
			int index=s.indexOf(bAux);
			
			if (index!=-1)
			{
				bAux=(BibitBinary)s.get(index);
				result=bAux.getNumOfOnes();
			}
			else
			{
				
				while(aux>0)
				{
						
					int sqrt=(int)Math.sqrt(aux);
					int pow=(int)Math.pow(sqrt, 2);
					aux=aux-pow;
					result=result+1;
				
				}
				bAux.setNumOfOnes(result);
				s.add(bAux);
				
			}
			
		
			
			return result;
			
		}
		
		
		//static ArrayList<Integer> getGeneIndexes(SortedSet Motifs) throws IOException
		static ArrayList<Integer> getGeneIndexes(IntAVLTreeSet Motifs) throws IOException
		{

			ArrayList<Integer> res=new ArrayList<>(Motifs.size());
			int cont=0;
			Iterator i=Motifs.iterator();
			while(i.hasNext()){
				
				Integer aux=(Integer)i.next();
				res.add(aux);
				cont=cont+1;
			}
			
			return res;
					
		}
		
		
		static ArrayList<Integer> getConditionsIndexes(int numberconditions, int [] andR, int extraCols,int patternSize ){
			
			//TreeSet<Integer> result=new TreeSet<Integer>();
			IntAVLTreeSet result=new IntAVLTreeSet();

			for(int i=extraCols-1,j=0;i>=0;i--,j++){
				
				String s=Integer.toString(andR[i],2);
				int numChar=s.length();
				int op=numberconditions-numChar-(patternSize*j);
				int index=0;
				int cont=0;
				while(index<s.length())
				{
					char aux=s.charAt(index);
					if (aux=='1')
					{
						cont=cont +1;
						int pos=index+op;
						result.add(pos);
					}
					index=index+1;
				}
				
			}
			
			ArrayList<Integer> res=new ArrayList<>(result);
			return res;
		}
	
		

}
