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
package jbiclustge.execution.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jbiclustge.utils.properties.JBiGePropertiesManager;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiThreadBiclusteringCallableTaskExecutor.
 */
public class MultiThreadBiclusteringTaskExecutor {
	
	
	/** The nprocs. */
	private static int nprocs = -1;
    /**
     * Thread pool.
     */
    private static ThreadPoolExecutor executor = null;
   
    
    
    /**
     * Creates the thread pool.
     *
     * @param numberprocesses the numberprocesses
     */
    private static void createThreadPool(Integer numberprocesses) {
        if (nprocs == -1) {
            int np = 0;
            int npfile=1;
            if(numberprocesses!=null)
            	np=numberprocesses;
            else{
            	try {
            		String nps=(String) JBiGePropertiesManager.getManager().getKeyValue("simultaneous.processes");
            		if (nps != null && !nps.isEmpty()) {
            			npfile = Integer.parseInt(nps);
            		}
            	} catch (Exception ex) {
                   npfile=1;
            	}
            }
            
            if(npfile>np)
            	np=npfile;
            
            int nprocessors = Runtime.getRuntime().availableProcessors();
            
            if (np < 1) {
                nprocs = nprocessors;
            } else if(np >nprocessors){
                nprocs =nprocessors;
            }
            else
            	nprocs=np;

            if (nprocs > 0) {
                executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nprocs);
            }
        }
    }
    
    /**
     * Gets the thread pool size.
     *
     * @return the thread pool size
     */
    public static int getThreadPoolSize() {
        createThreadPool(null);
        return nprocs;
    }
    
    /**
     * Run.
     *
     * @param <T> the generic type
     * @param numberprocesses the numberprocesses
     * @param tasks the tasks
     * @return the list
     * @throws Exception the exception
     */
    public static <T> List<T> run(Integer numberprocesses, Collection<? extends Callable<T>> tasks) throws Exception {
        createThreadPool(numberprocesses);

        List<T> results = new ArrayList<T>();
        if (executor == null) {
            for (Callable<T> task : tasks) {
                results.add(task.call());
            }
        } else {
            if (executor.getActiveCount() < nprocs) {
                List<Future<T>> futures = executor.invokeAll(tasks);
                for (Future<T> future : futures) {
                    results.add(future.get());
                }
            } else {
                // Thread pool is busy. Just run in the caller's thread.
                for (Callable<T> task : tasks) {
                    results.add(task.call());
                }
            }
        }
        
        close();
        return results;
   }
 
 
    /**
     * Run.
     *
     * @param <T> the generic type
     * @param tasks the tasks
     * @return the list
     * @throws Exception the exception
     */
    public static <T> List<T> run(Collection<? extends Callable<T>> tasks) throws Exception {
	 return run(null, tasks);
    }
    

 
 
 
    /**
     * Shutdown.
     */
    public static void shutdown() {
      if (executor != null) {
         executor.shutdown();
      }
    }
 
    /**
     * Close.
     */
    protected static void close(){
    	
    	executor.shutdown();
    	boolean waitclose=true;
    	while (waitclose) {
			try{
				waitclose=!executor.awaitTermination(5, TimeUnit.SECONDS);
				if(waitclose)
					LogMessageCenter.getLogger().addTraceMessage("Waiting to finish "+executor.getActiveCount()+" processes");
			}catch (InterruptedException e) {
    		LogMessageCenter.getLogger().addTraceMessage("Interruped while awaiting completion of callback threads - trying again...");
			
		   }
    	}
    }	

}
