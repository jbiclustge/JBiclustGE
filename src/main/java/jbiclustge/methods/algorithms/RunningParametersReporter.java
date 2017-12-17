/************************************************************************** 
 * Copyright 2012 - 2017, Orlando Rocha (ornrocha@gmail.com)
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
 */
package jbiclustge.methods.algorithms;

import java.util.LinkedHashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class RunningParametersReporter.
 */
public class RunningParametersReporter extends LinkedHashMap<String, String>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Sets the keydouble value.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setKeydoubleValue(String key, double value){
		this.put(key, String.valueOf(value));
	}
	
	/**
	 * Sets the keyint value.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setKeyintValue(String key, int value){
		this.put(key, String.valueOf(value));
	}
	
	/**
	 * Sets the keyboolean value.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setKeybooleanValue(String key, boolean value){
		this.put(key, String.valueOf(value));
	}
	
	/**
	 * Sets the key string value.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setKeyStringValue(String key,String value){
		this.put(key, value);
	}

}
