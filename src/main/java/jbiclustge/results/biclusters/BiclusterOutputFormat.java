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
package jbiclustge.results.biclusters;

// TODO: Auto-generated Javadoc
/**
 * The Enum BiclusterOutputFormat.
 */
public enum BiclusterOutputFormat {
	
	/** The J biclust G E CSV. */
	JBiclustGE_CSV{
		
		@Override
		public String getName() {
			return "JBiclustGE_csv";
		}
		
	},
	
	/** The J biclust G E JSON. */
	JBiclustGE_JSON{
		
		@Override
		public String getName() {
			return "JBiclustGE_json";
		}
	},
	
	/** The Rbiclust TXT. */
	Rbiclust_TXT{
		@Override
		public String getName() {
			return "biclust_file_format";
		}
	},
	
	/** The Bic overlapper. */
	BicOverlapper{
		@Override
		public String getName() {
			return "BicOverlapper_file_format";
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

}
