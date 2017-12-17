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
package jbiclustge.enrichmentanalysistools.ontologizer.components;

// TODO: Auto-generated Javadoc
/**
 * The Enum OntologizerMTCMethod.
 */
public enum OntologizerMTCMethod {
	
	
	
	/** The None. */
	None{
		@Override
		public String toString() {
			return "None";
		}
	},
	
	/** The Benjamini hochberg. */
	BenjaminiHochberg{
		@Override
		public String toString() {
			return "Benjamini-Hochberg";
		}
	},
	
	/** The Benjamini yekutieli. */
	BenjaminiYekutieli{
		@Override
		public String toString() {
			return "Benjamini-Yekutieli";
		}
	},
	
	/** The Bonferroni. */
	Bonferroni{
		@Override
		public String toString() {
			return "Bonferroni";
		}
	},
	
	/** The Bonferroni holm. */
	BonferroniHolm{
		@Override
		public String toString() {
			return "Bonferroni-Holm";
		}
	},
	
	/** The Westfall young single step. */
	WestfallYoungSingleStep{
		@Override
		public String toString() {
			return "Westfall-Young-Single-Step";
		}
	},
	
	/** The Westfall young step down. */
	WestfallYoungStepDown{
		@Override
		public String toString() {
			return "Westfall-Young-Step-Down";
		}
	};
	
	
	
    
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString(){
		return toString();
	}
	
	public static OntologizerMTCMethod getMTCMethodFromString(String name){
		if(name!=null)
		for (OntologizerMTCMethod method : OntologizerMTCMethod.values()) {
			if(name.toLowerCase().equals(method.toString().toLowerCase()))
				return method;
		}
		return OntologizerMTCMethod.None;
	}
	

}
