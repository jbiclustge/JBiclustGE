package jbiclustge.methods.algorithms.java.bicat.opsm.components;

import java.util.LinkedList;

import jbiclustge.methods.algorithms.java.bicat.auxtools.Bicluster;
import jbiclustge.methods.algorithms.java.bicat.auxtools.LinkListElement;
import jbiclustge.methods.algorithms.java.bicat.auxtools.ParetoModelRecord;




public class Pmr2listConverter {
	
	public LinkedList list = new LinkedList();
	
	
	/**
	 * Constructor.
	 */
	public Pmr2listConverter() {
		
	}

	public LinkedList convert(ParetoModelRecord pmr){
		//System.out.println("convert ComputedBiclusters list...");
		LinkListElement current = pmr.getRoot();
		for (int i = 0; i < pmr.getSize(); i++) {
			current = current.next;
			Bicluster currentBicluster = new Bicluster(i, current.model.arrayOfGeneIndices, current.model.arrayOfTissueIndices);
			list.add(currentBicluster);
		}
		//System.out.println("OPSM LinkedList: "+ list.toString());
		return list;
		
	}

	
}
