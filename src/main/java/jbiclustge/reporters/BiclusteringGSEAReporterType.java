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
package jbiclustge.reporters;

import jbiclustge.reporters.interfaces.IGSEABiclusteringReporter;

// TODO: Auto-generated Javadoc
/**
 * The Enum BiclusteringGSEAReporterType.
 */
public enum BiclusteringGSEAReporterType {
	
	/** The csv. */
	CSV{
		@Override
		public String toString() {
			return "csv";
		}
		
		@Override
		public IGSEABiclusteringReporter getReporterInstance() {
			return new BiclusteringGSEATaskCSVReporter();
		}
	},
	
	/** The excel. */
	EXCEL{
		@Override
		public String toString() {
			return "excel";
		}
		
		@Override
		public IGSEABiclusteringReporter getReporterInstance() {
			return new BiclusteringTaskExcelReporter();
		}
	};
	
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		return toString();
	}
	
	
	/**
	 * Gets the reporter instance.
	 *
	 * @return the reporter instance
	 */
	public IGSEABiclusteringReporter getReporterInstance(){
		return getReporterInstance();
	}
	
	
	/**
	 * Gets the reporter type from string.
	 *
	 * @param name the name
	 * @return the reporter type from string
	 */
	public static BiclusteringGSEAReporterType getReporterTypeFromString(String name){
		if(name!=null){
			for (BiclusteringGSEAReporterType type : BiclusteringGSEAReporterType.values()) {
				if(type.toString().toLowerCase().equals(name.toLowerCase()))
					return type;
			} 
		}
		return null;
	}

}
