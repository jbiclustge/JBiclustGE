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
 * The Enum TopGOAnnotationFunction.
 */
public enum TopGOAnnotationFunction {
	
	/** The annfun DB. */
	annfunDB{
		@Override
		public String toString() {
			return "annFUN.db";
		}
		
		@Override
		public boolean isAlternativeFunction() {
			return false;
		}
	},
	
	annfunDB2{
		@Override
		public String toString() {
			return "annFUN2.db";
		}
		
		@Override
		public boolean isAlternativeFunction() {
			return true;
		}
		
		@Override
		public String getFunction() {
			return "annFUN2.db <- function(whichOnto, feasibleGenes = NULL, affyLib) {\n" + 
					"  \n" + 
					"  ## we add the .db ending if needed\n" + 
					"  affyLib <- paste(sub(\".db$\", \"\", affyLib), \".db\", sep = \"\")\n" + 
					"  require(affyLib, character.only = TRUE) || stop(paste(\"package\",\n" + 
					"                                                        affyLib, \"is required\", sep = \" \"))\n" + 
					"  affyLib <- sub(\".db$\", \"\", affyLib)\n" + 
					"  \n" + 
					"  orgFile <- get(paste(get(paste(affyLib, \"ORGPKG\", sep = \"\")),\n" + 
					"                       \"_dbfile\", sep = \"\"))\n" + 
					"  \n" + 
					"  try(dbGetQuery(get(paste(affyLib, \"dbconn\", sep = \"_\"))(),\n" + 
					"                 paste(\"ATTACH '\", orgFile(), \"' as org;\", sep =\"\")),\n" + 
					"      silent = TRUE)\n" + 
					"  \n" + 
					"  .sql <- paste(\"SELECT DISTINCT probe_id, go_id FROM probes INNER JOIN \",\n" + 
					"                \"(SELECT * FROM org.genes INNER JOIN org.go_\",\n" + 
					"                tolower(whichOnto),\" USING('_id')) USING('gene_id');\", sep = \"\")\n" + 
					"  \n" + 
					"  retVal <- dbGetQuery(get(paste(affyLib, \"dbconn\", sep = \"_\"))(), .sql)\n" + 
					"  \n" + 
					"  ## restric to the set of feasibleGenes\n" + 
					"  if(!is.null(feasibleGenes))\n" + 
					"    retVal <- retVal[retVal[[\"probe_id\"]] %in% feasibleGenes, ]\n" + 
					"  \n" + 
					"  ## split the table into a named list of GOs\n" + 
					"  retVal <- split(retVal[[\"probe_id\"]], retVal[[\"go_id\"]])\n" + 
					"  \n" + 
					"  ## return only the GOs mapped in GO.db\n" + 
					"  return(retVal[names(retVal) %in% ls(get(paste(\"GO\",toupper(whichOnto), \"Term\", sep = \"\")))])\n" + 
					"}";
		}
		
		
	},
	
	/** The annfun ORG. */
	annfunORG{
		@Override
		public String toString() {
			return "annFUN.org";
		}
		
		@Override
		public boolean isAlternativeFunction() {
			return false;
		}
		
	},
	
	annfunORG2{
		@Override
		public String toString() {
			return "annFUN2.org";
		}
		
		@Override
		public boolean isAlternativeFunction() {
			return true;
		}
		
		@Override
		public String getFunction() {
			return "annFUN2.org <- function(whichOnto, feasibleGenes = NULL, mapping, ID = \"entrez\") {\n" + 
					"  \n" + 
					"  tableName <- c(\"genes\", \"accessions\", \"alias\", \"ensembl\",\n" + 
					"                 \"gene_info\", \"gene_info\", \"unigene\")\n" + 
					"  keyName <- c(\"gene_id\", \"accessions\", \"alias_symbol\", \"ensembl_id\",\n" + 
					"               \"symbol\", \"gene_name\", \"unigene_id\")\n" + 
					"  names(tableName) <- names(keyName) <- c(\"entrez\", \"genbank\", \"alias\", \"ensembl\",\"symbol\", \"genename\", \"unigene\")\n" + 
					"  \n" + 
					"  \n" + 
					"  ## we add the .db ending if needed \n" + 
					"  mapping <- paste(sub(\".db$\", \"\", mapping), \".db\", sep = \"\")\n" + 
					"  require(mapping, character.only = TRUE) || stop(paste(\"package\", mapping, \"is required\", sep = \" \"))\n" + 
					"  mapping <- sub(\".db$\", \"\", mapping)\n" + 
					"  \n" + 
					"  geneID <- keyName[tolower(ID)]\n" + 
					"  .sql <- paste(\"SELECT DISTINCT \", geneID, \", go_id FROM \", tableName[tolower(ID)],\n" + 
					"                \" INNER JOIN \", paste(\"go\", tolower(whichOnto), sep = \"_\"),\n" + 
					"                \" USING(_id)\", sep = \"\")\n" + 
					"  retVal <- dbGetQuery(get(paste(mapping, \"dbconn\", sep = \"_\"))(), .sql)\n" + 
					"  \n" + 
					"  ## restric to the set of feasibleGenes\n" + 
					"  if(!is.null(feasibleGenes))\n" + 
					"    retVal <- retVal[retVal[[geneID]] %in% feasibleGenes, ]\n" + 
					"  \n" + 
					"  ## split the table into a named list of GOs\n" + 
					"  retVal <- split(retVal[[geneID]], retVal[[\"go_id\"]])\n" + 
					"  \n" + 
					"  ## return only the GOs mapped in GO.db\n" + 
					"  return(retVal[names(retVal) %in% ls(get(paste(\"GO\",toupper(whichOnto), \"Term\", sep = \"\")))])\n" + 
					"  \n" + 
					"  ## split the table into a named list of GOs\n" + 
					"  #return(split(retVal[[geneID]], retVal[[\"go_id\"]]))\n" + 
					"}";
		}
		
	},
	
	/** The annfun GENE 2 GO. */
	annfunGENE2GO{
		@Override
		public String toString() {
			return "annFUN.gene2GO";
		}
		
		@Override
		public boolean isAlternativeFunction() {
			return false;
		}
	},
	
	/** The annfun GO 2 GENE. */
	annfunGO2GENE{
		@Override
		public String toString() {
			return "annFUN.GO2gene";
		}
		
		@Override
		public boolean isAlternativeFunction() {
			return false;
		}
	};
	
	
	public boolean isAlternativeFunction() {
		return isAlternativeFunction();
	}
	
	
	public String getFunction() {
		return getFunction();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		return toString();
	}

}
