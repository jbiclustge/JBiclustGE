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
package jbiclustge.enrichmentanalysistools.topgo.components;

import jbiclustge.datatools.goannotation.components.GOAspect;

// TODO: Auto-generated Javadoc
/**
 * The Enum TopgoOntology.
 */
public enum TopgoOntology {
	
	/** The bp. */
	BP{
		
		@Override
		public GOAspect getGOAspect() {
			return GOAspect.BIOLOGICALPROCESS;
		}
		
		@Override
		public String getName() {
			return GOAspect.BIOLOGICALPROCESS.getName();
		}
	},
	
	/** The mf. */
	MF{
		
		@Override
		public GOAspect getGOAspect() {
			return GOAspect.MOLECULARFUNCTION;
		}
		
		@Override
		public String getName() {
			return GOAspect.MOLECULARFUNCTION.getName();
		}
	},
	
	/** The cc. */
	CC{
		
		@Override
		public GOAspect getGOAspect() {
			return GOAspect.CELLULARCOMPONENT;
		}
		
		@Override
		public String getName() {
			return GOAspect.CELLULARCOMPONENT.getName();
		}
	};
	
	
	public GOAspect getGOAspect(){
    	return getGOAspect();
    }
	
	@Override
	public String toString() {
	    return getName();
	}
	    
	public String getName() {
		return getName();
	}
	
	public static TopgoOntology getTopgoOntologyFromString(String aspect){
		if(aspect!=null && !aspect.isEmpty())
		for (TopgoOntology a : TopgoOntology.values()) {
			if(aspect.toLowerCase().equals(a.getName().toLowerCase()))
				return a;
			else if(aspect.toLowerCase().equals(a.getGOAspect().getGOType().toLowerCase()))
				return a;
		}
		
		return TopgoOntology.BP;
	}

}
