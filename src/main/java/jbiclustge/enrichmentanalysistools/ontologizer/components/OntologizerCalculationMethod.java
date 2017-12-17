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
 * The Enum OntologizerCalculationMethod.
 */
public enum OntologizerCalculationMethod {
	
	
	
	/** The Parent child union. */
	ParentChildUnion{
		@Override
		public String toString() {
			return "Parent-Child-Union";
		}
	},
	
	/** The Parent child intersection. */
	ParentChildIntersection{
		@Override
		public String toString() {
			return "Parent-Child-Intersection";
		}
	},
	
	/** The mgsa. */
	MGSA{
		@Override
		public String toString() {
			return "MGSA";
		}
	},
	
	/** The Term for term. */
	TermForTerm{
		@Override
		public String toString() {
			return "Term-For-Term";
		}
	},
	
	/** The Topology elim. */
	TopologyElim{
		@Override
		public String toString() {
			return "Topology-Elim";
		}
	},
	
	/** The Topology weighted. */
	TopologyWeighted{
		@Override
		public String toString() {
			return "Topology-Weighted";
		}
	};
	
	
	
    
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString(){
		return toString();
	}
	
	public static OntologizerCalculationMethod getCalculationmethodFromString(String name){
		if(name!=null){
			for (OntologizerCalculationMethod method : OntologizerCalculationMethod.values()) {
				if(name.toLowerCase().equals(method.toString().toLowerCase()))
					return method;
			}
		}
		return OntologizerCalculationMethod.ParentChildUnion;
	}
	
	
}
