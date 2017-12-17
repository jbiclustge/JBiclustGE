package jbiclustge.execution.tasks;

import java.util.ArrayList;

import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;

public class BiclusteringTasksFunctions {
	
	
	
	
	public static ArrayList<BiclusteringTask> createListOfBiclusteringTasks(AbstractBiclusteringAlgorithmCaller...methods){
		
		ArrayList<BiclusteringTask> tasks=new ArrayList<>();
		for (int i = 0; i < methods.length; i++) {
			tasks.add(new BiclusteringTask(methods[i]));
		}
		return tasks;
	}

}
