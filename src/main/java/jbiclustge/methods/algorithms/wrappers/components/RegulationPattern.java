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
 * The Enum RegulationPattern.
 */
public enum RegulationPattern {

	/** The both. */
	BOTH{
		@Override
		public String getName() {
			return "both";
		}
		@Override
		public String getShortForm() {
			return "b";
		}
	},
	
	/** The up. */
	UP{
		@Override
		public String getName() {
			return "up";
		}
		
		@Override
		public String getShortForm() {
			return "u";
		}
	},
	
	/** The down. */
	DOWN{
		@Override
		public String getName() {
			return "down";
		}
		
		@Override
		public String getShortForm() {
			return "d";
		}
	};
	
	/**
	 * Gets the short form.
	 *
	 * @return the short form
	 */
	public String getShortForm(){
		return getShortForm();
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return getName();
	}
	
	/**
	 * Gets the regulation pattern from string.
	 *
	 * @param regulationpat the regulationpat
	 * @return the regulation pattern from string
	 */
	public static RegulationPattern getRegulationPatternFromString(String regulationpat){
		if(regulationpat!=null){
			for (RegulationPattern pat : RegulationPattern.values()) {
				if(regulationpat.toLowerCase().equals(pat.getName()))
					return pat;
				if(regulationpat.toLowerCase().equals(pat.getShortForm()))
					return pat;
			}
		}
		return RegulationPattern.BOTH;
	}
}
