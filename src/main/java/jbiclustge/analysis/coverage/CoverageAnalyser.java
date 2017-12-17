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
package jbiclustge.analysis.coverage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.javatuples.Triplet;

import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class CoverageAnalyser.
 */
public class CoverageAnalyser {
	
	
	 /** The listresults. */
 	private BiclusterList listresults;
	 
 	/** The individualgenescoverage. */
 	private LinkedHashMap<Integer, Double> individualgenescoverage;
	 
 	/** The individualconditionscoverage. */
 	private LinkedHashMap<Integer, Double> individualconditionscoverage;
	 
 	/** The totalgenescoverage. */
 	private double totalgenescoverage=0.0;
	 
 	/** The totalconditionscoverage. */
 	private double totalconditionscoverage=0.0;
	 
 	/** The totalmatrixcoverage. */
 	private double totalmatrixcoverage=0.0;
	 
 	/** The totalgenes. */
 	private ArrayList<String> totalgenes;
	 
 	/** The totalconditions. */
 	private ArrayList<String> totalconditions;
	 
 	/** The matrixdimension. */
 	private int matrixdimension=0;
	 
	
	 /**
 	 * Instantiates a new coverage analyser.
 	 *
 	 * @param resultset the resultset
 	 */
 	public CoverageAnalyser(BiclusterList resultset){
		 this.listresults=resultset;
		 this.totalgenes=listresults.getAnalysedDataset().getGeneNamesList();
		 this.totalconditions=listresults.getAnalysedDataset().getConditionsList();
		 this.matrixdimension=totalgenes.size()*totalconditions.size();
	 }
	 
	 
	 /**
 	 * Gets the totalgenescoverage.
 	 *
 	 * @return the totalgenescoverage
 	 */
 	public double getTotalgenescoverage(){
		 if(totalgenescoverage==0.0)
			 processGenesCoverage();
		return totalgenescoverage;
	}

	/**
	 * Gets the totalconditionscoverage.
	 *
	 * @return the totalconditionscoverage
	 */
	public double getTotalconditionscoverage() {
		if(totalconditionscoverage==0.0)
			processConditionsCoverage();
		return totalconditionscoverage;
	}


	/**
	 * Gets the totalmatrixcoverage.
	 *
	 * @return the totalmatrixcoverage
	 */
	public double getTotalmatrixcoverage() {
		if(totalmatrixcoverage==0.0)
			processMatrixCoverage();
		return totalmatrixcoverage;
	}

	/**
	 * Process genes coverage.
	 */
	public void processGenesCoverage(){
		 
		 HashSet<String> covered=new HashSet<>();
		 individualgenescoverage=new LinkedHashMap<>();
		 ArrayList<ArrayList<String>> genespercluster=listresults.getBiclustersGeneNameList();
		 
		 for (int i = 0; i < genespercluster.size(); i++) {
			ArrayList<String> incluster=genespercluster.get(i);
			double partcov=(double)incluster.size()/totalgenes.size();
			individualgenescoverage.put(i, partcov);
			covered.addAll(incluster);
		 }
		
		 totalgenescoverage=(double)covered.size()/totalgenes.size();
	 }
	
	 
	 /**
 	 * Process conditions coverage.
 	 */
 	public void processConditionsCoverage(){
		 
		 HashSet<String> covered=new HashSet<>();
		 individualconditionscoverage=new LinkedHashMap<>();
		 ArrayList<ArrayList<String>> conditionspercluster=listresults.getBiclustersConditionsNameList();
		 
		 for (int i = 0; i < conditionspercluster.size(); i++) {
			individualconditionscoverage.put(i, ((double)conditionspercluster.get(i).size()/totalconditions.size()));
			covered.addAll(conditionspercluster.get(i));
		 }
		 
		 totalconditionscoverage=(double)covered.size()/totalconditions.size();	 
	 }
	 
	 
	 
	 /**
 	 * Process matrix coverage.
 	 */
 	public void processMatrixCoverage(){
		 
		 HashSet<String> matrixcov=new HashSet<>();

		 ArrayList<Integer[]> genebiclusters=listresults.getBiclustersGeneIndexList();
		 ArrayList<Integer[]> condbiclusters=listresults.getBiclustersConditionIndexList();
		 
		 for (int i = 0; i < genebiclusters.size(); i++) {
			Integer[] geneindexes=genebiclusters.get(i);
			Integer[] condindexes=condbiclusters.get(i);
			
			for (int j = 0; j < condindexes.length; j++) {
				int cidx=condindexes[j];
				for (int k = 0; k < geneindexes.length; k++) {
					String cellid=String.valueOf(geneindexes[k])+String.valueOf(cidx);
					matrixcov.add(cellid);
				}
			} 
		}
		 
		 totalmatrixcoverage=(double)matrixcov.size()/matrixdimension;
		 
	 }
	 
	 
	 
	 /**
 	 * Calculate genes, conditions and matrix coverage.
 	 *
 	 * @return a triplet with genes coverage, conditions coverage and matrix coverage.
 	 */
	 
	 
	 public Triplet<Double, Double, Double> processandgetCoverageParameters(){
		 
		 HashSet<String> matrixcov=new HashSet<>();
		 HashSet<Integer> genecov=new HashSet<>();
		 HashSet<Integer> condcov=new HashSet<>();
		 
		 ArrayList<Integer[]> genebiclusters=listresults.getBiclustersGeneIndexList();
		 ArrayList<Integer[]> condbiclusters=listresults.getBiclustersConditionIndexList();
		 
		 for (int i = 0; i < genebiclusters.size(); i++) {
			Integer[] geneindexes=genebiclusters.get(i);
			Integer[] condindexes=condbiclusters.get(i);
			genecov.addAll(new ArrayList<>(Arrays.asList(geneindexes)));
			condcov.addAll(new ArrayList<>(Arrays.asList(condindexes)));
			
			for (int j = 0; j < condindexes.length; j++) {
				int cidx=condindexes[j];
				for (int k = 0; k < geneindexes.length; k++) {
					String cellid=String.valueOf(geneindexes[k])+String.valueOf(cidx);
					matrixcov.add(cellid);
				}
			} 
		}
		 
		 return new Triplet<Double, Double, Double>((double)genecov.size()/totalgenes.size(), 
				 (double)condcov.size()/totalconditions.size(), 
				 (double)matrixcov.size()/matrixdimension);
	 }
	 
	 /**
 	 * Gets the coverage parameters report.
 	 *
 	 * @return the coverage parameters report
 	 */
 	public String getCoverageParametersReport(){
		 StringBuilder str=new StringBuilder();
		 str.append("Gene Coverage percentage: "+getTotalgenescoverage()*100+"\n");
		 str.append("Conditions coverage percentage: "+getTotalconditionscoverage()*100+"\n");
		 str.append("Matrix coverage percentage: "+getTotalmatrixcoverage()*100+"\n\n");
		 return str.toString();
	 }
	 
	 
	 /**
 	 * Gets the coverage parameters.
 	 *
 	 * @param resultset the resultset
 	 * @return the coverage parameters
 	 */
 	public static Triplet<Double, Double, Double> getCoverageParameters(BiclusterList resultset){
		 CoverageAnalyser analyser=new CoverageAnalyser(resultset);
		 return analyser.processandgetCoverageParameters();
	 }
	 
	 /**
 	 * Gets the coverage parameters report.
 	 *
 	 * @param resultset the resultset
 	 * @return the coverage parameters report
 	 */
 	public static String getCoverageParametersReport(BiclusterList resultset){
		 CoverageAnalyser analyser=new CoverageAnalyser(resultset);
		 Triplet<Double, Double, Double> res=analyser.processandgetCoverageParameters();
		 StringBuilder str=new StringBuilder();
		 str.append("Gene Coverage percentage: "+res.getValue0()*100+"\n");
		 str.append("Conditions coverage percentage: "+res.getValue1()*100+"\n");
		 str.append("Matrix coverage percentage: "+res.getValue2()*100+"\n\n");
		 return str.toString();
	 }

}
