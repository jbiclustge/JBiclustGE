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
package jbiclustge.utils.statistics.multithread;

import jbiclustge.utils.statistics.CorrelationTest;

// TODO: Auto-generated Javadoc
/**
 * The Interface MultiThreadCorrelationTest.
 */
public interface MultiThreadCorrelationTest extends CorrelationTest{
	
	/**
	 * Sets the number simultaneous processes.
	 *
	 * @param np the new number simultaneous processes
	 */
	public void setNumberSimultaneousProcesses(int np);

}
