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
package jbiclustge.execution.controlcenters;

import java.util.ArrayList;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.execution.tasks.BiclusteringTask;
import jbiclustge.results.biclusters.containers.BiclusterList;

// TODO: Auto-generated Javadoc
/**
 * The Class BiclusteringControlCenter.
 */
public class BiclusteringControlCenter extends AbstractBiclusteringExecutionControlCenter<BiclusteringTask, BiclusterList>{

	 /**
 	 * Instantiates a new biclustering control center.
 	 *
 	 * @param data the data
 	 */
 	public BiclusteringControlCenter(ExpressionData data) {
		super(data);
	}


	 
	 /* (non-Javadoc)
 	 * @see execution.controlcenters.AbstractBiclusteringExecutionControlCenter#getListOfTasks()
 	 */
 	@Override
	 protected ArrayList<BiclusteringTask> getListOfTasks() {
		 ArrayList<BiclusteringTask> tasks=new ArrayList<>();
			
			for (int i = 0; i <biclustmethods.size(); i++) {
				tasks.add(new BiclusteringTask(biclustmethods.get(i)));
			}
			
			return tasks;
	
	 }

	/* (non-Javadoc)
	 * @see execution.controlcenters.AbstractBiclusteringExecutionControlCenter#getResults()
	 */
	@Override
	public ArrayList<BiclusterList> getResults() {
		return results;
	}


	

	
	
	
	
	
	
	
	
	


}
