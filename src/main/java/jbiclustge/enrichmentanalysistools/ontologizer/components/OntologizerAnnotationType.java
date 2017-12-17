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

import jbiclustge.datatools.goannotation.components.GOAspect;

// TODO: Auto-generated Javadoc
/**
 * The Enum OntologizerAnnotationType.
 */
public enum OntologizerAnnotationType {
	
	
	/** The all. */
	ALL{
		
		@Override
		public GOAspect getGOAspect() {
			return null;
		}
		
		@Override
		public String getName() {
			return "All";
		}
		
	},
	
	/** The biologicalprocess. */
	BIOLOGICALPROCESS{
		
		@Override
		public GOAspect getGOAspect() {
			return GOAspect.BIOLOGICALPROCESS;
		}
		
		@Override
		public String getName() {
			return GOAspect.BIOLOGICALPROCESS.getName();
		}
	},
	
	/** The molecularfunction. */
	MOLECULARFUNCTION{
		
		@Override
		public GOAspect getGOAspect() {
			return GOAspect.MOLECULARFUNCTION;
		}
		
		@Override
		public String getName() {
			return GOAspect.MOLECULARFUNCTION.getName();
		}
		
	},
	
	/** The cellularcomponent. */
	CELLULARCOMPONENT{
		@Override
		public GOAspect getGOAspect() {
			return GOAspect.CELLULARCOMPONENT;
		}
		
		@Override
		public String getName() {
			return GOAspect.CELLULARCOMPONENT.getName();
		}
	};
	
	
    /**
     * Gets the GO aspect.
     *
     * @return the GO aspect
     */
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
    
    /**
     * Gets the annotation type from string.
     *
     * @param type the type
     * @return the annotation type from string
     */
    public static OntologizerAnnotationType getAnnotationTypeFromString(String type){
    	
    	if(type!=null){
    		GOAspect check=GOAspect.getAspectFromString(type);
    		if(check!=null)
    		for (OntologizerAnnotationType typeann : OntologizerAnnotationType.values()) {
				if(check!=null && check.equals(typeann.getGOAspect())){
						return typeann;
				}
			}
    	}
    	return OntologizerAnnotationType.ALL;
    }
}
