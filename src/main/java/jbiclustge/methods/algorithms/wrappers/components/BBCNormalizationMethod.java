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
package jbiclustge.methods.algorithms.wrappers.components;

// TODO: Auto-generated Javadoc
/**
 * The Enum BBCNormalizationMethod.
 */
public enum BBCNormalizationMethod {

	/** The none. */
	NONE{
		@Override
		public String getName() {
			return "none";
		}
		
		@Override
		public String getLongName() {
			return "Without normalization";
		}
	},
	
	/** The csn. */
	CSN{
		@Override
		public String getName() {
			return "csn";
		}
		
		@Override
		public String getLongName() {
			return "Column-wise standardization";
		}
		
	},
	
	/** The rsn. */
	RSN{
		@Override
		public String getName() {
			return "rsn";
		}
		
		@Override
		public String getLongName() {
			return "Row-wise standardization";
		}
		
	},
	
	/** The iqrn. */
	IQRN{
		@Override
		public String getName() {
			return "iqrn";
		}
		
		@Override
		public String getLongName() {
			return "Inter-quartile range normalization";
		}
	},
	
	/** The sqrn. */
	SQRN{
		@Override
		public String getName() {
			return "sqrn";
		}
		
		@Override
		public String getLongName() {
			return "Smallest range quartile normalization";
		}
		
	};
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return getName();
	}
	
	
	public String getLongName() {
		return getLongName();
	}
	
	
	/**
	 * Gets the BBC normalization method from string.
	 *
	 * @param name the name
	 * @return the BBC normalization method from string
	 */
	public static BBCNormalizationMethod getBBCNormalizationMethodFromString(String name){
		
		if(name!=null){
			for (BBCNormalizationMethod method : BBCNormalizationMethod.values()) {
				if(name.toLowerCase().equals(method.getName()))
					return method;
				else if(name.toLowerCase().equals(method.getLongName().toLowerCase()))
					return method;
			}
		}
		
		return BBCNormalizationMethod.NONE;
	}
}
