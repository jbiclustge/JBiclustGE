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
package jbiclustge.methods.algorithms.r.components;

// TODO: Auto-generated Javadoc
/**
 * The Enum BCPlaidClusterType.
 */
public enum BCPlaidClusterType {
	
	
	/** The rows. */
	ROWS{
		@Override
		public String toString() {
			return "r";
		}
	},
	
	/** The columns. */
	COLUMNS{
		
		@Override
		public String toString() {
			return "c";
		}
	},
	
	/** The both. */
	BOTH{
		
		@Override
		public String toString() {
			return "b";
		}
	};
	
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		return toString();
	}

}
