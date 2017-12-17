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

// TODO: Auto-generated Javadoc
/**
 * The Enum TopGOMappingType.
 */
public enum TopGOMappingType {
	
	
	/** The Entrez. */
	Entrez,
	
	/** The Gen bank. */
	GenBank,
	
	/** The Alias. */
	Alias,
	
	/** The Ensembl. */
	Ensembl,
	
	/** The Gene. */
	Gene,
	
	/** The Symbol. */
	Symbol, 
	
	/** The Gene name. */
	GeneName,
	
	/** The Uni gene. */
	UniGene;
	
	public static TopGOMappingType getTopGOMappingTypeFromString(String mappingtype) {
		if(mappingtype!=null) {
			for (TopGOMappingType element : TopGOMappingType.values()) {
				if(mappingtype.toLowerCase().equals(element.toString().toLowerCase()))
					return element;
				
			}
		}
		return TopGOMappingType.Symbol;
	}
	
	

}
