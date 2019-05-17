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
package jbiclustge.utils.props;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandsProcessList.
 */
public class CommandsProcessList extends ArrayList<String>{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Insert double parameter.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void insertDoubleParameter(String key, double value){
		this.add(key);
		this.add(String.valueOf(value));
	}
	
	/**
	 * Insert float parameter.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void insertFloatParameter(String key, float value){
		this.add(key);
		this.add(String.valueOf(value));
	}
	
	/**
	 * Insert integer parameter.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void insertIntegerParameter(String key, int value){
		this.add(key);
		this.add(String.valueOf(value));
	}
	
	/**
	 * Insert string parameter.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void insertStringParameter(String key, String value){
		this.add(key);
		this.add(value);
	}
	
	/**
	 * Insert string parameter array.
	 *
	 * @param cmds the cmds
	 */
	public void insertStringParameterArray(String[] cmds){
		if(cmds!=null)
			for (String string : cmds) {
				this.add(string);
			}
	}
	

}
