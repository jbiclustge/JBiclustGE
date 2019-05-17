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
package jbiclustge.enrichmentanalysistools.common;

// TODO: Auto-generated Javadoc
/**
 * The Enum TopGopvaluesAdjustMethod.
 */
public enum pvaluesAdjustMethod {

	/** The none. */
	/*	"holm", "hochberg", "hommel", "bonferroni", "BH", "BY", "fdr", "none"*/
	NONE{
		@Override
		public String toString() {
			return "none";
		}
	},
	
	/** The bh. */
	BH{
		@Override
		public String toString() {
			return "BH";
		}
	},
	
	/** The by. */
	BY{
		@Override
		public String toString() {
			return "BY";
		}
	},
	
	/** The bonferroni. */
	BONFERRONI{
		@Override
		public String toString() {
			return "bonferroni";
		}
	},
	
	/** The fdr. */
	FDR{
		@Override
		public String toString() {
			return "fdr";
		}
	},
	
	/** The holm. */
	HOLM{
		@Override
		public String toString() {
			return "holm";
		}
	},
	
	/** The hochberg. */
	HOCHBERG{
		@Override
		public String toString() {
			return "hochberg";
		}
	},
	
	/** The hommel. */
	HOMMEL{
		@Override
		public String toString() {
			return "hommel";
		}
	};
	

	/**
	 * Gets the MTC method from string.
	 *
	 * @param mtcmethod the mtcmethod
	 * @return the MTC method from string
	 */
	public static pvaluesAdjustMethod getMTCMethodFromString(String mtcmethod){
		if(mtcmethod!=null){
			for (pvaluesAdjustMethod method : pvaluesAdjustMethod.values()) {
				if(mtcmethod.toLowerCase().equals(method.toString().toLowerCase()))
					return method;
			}
		}
		return pvaluesAdjustMethod.NONE;
	}
	
	
}
