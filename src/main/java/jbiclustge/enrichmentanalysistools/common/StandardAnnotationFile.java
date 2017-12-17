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

import org.apache.commons.io.FilenameUtils;

// TODO: Auto-generated Javadoc
/**
 * The Enum StandardAnnotationFile.
 */
public enum StandardAnnotationFile {
	
	
	/** The Escherichiacoli. */
	Escherichiacoli{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.ecocyc.gz";
		}
		@Override
		public String getOrganismName() {
			return "Escherichia coli";
		}
		
		
	},
	
	/** The Saccharomycescerevisiae. */
	Saccharomycescerevisiae{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.sgd.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Saccharomyces cerevisiae";
		}
		
	},
	
	/** The Leishmaniamajor. */
	Leishmaniamajor{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.GeneDB_Lmajor.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Leishmania major";
		}
		
	},
	
	/** The Plasmodiumfalciparum. */
	Plasmodiumfalciparum{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.GeneDB_Pfalciparum.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Plasmodium falciparum";
		}
		
	},
	
	/** The Trypanosomabrucei. */
	Trypanosomabrucei{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.GeneDB_Tbrucei.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Trypanosoma brucei";
		}
		
	},
	
	/** The Agrobacteriumtumefaciensstr C 58. */
	AgrobacteriumtumefaciensstrC58{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.PAMGO_Atumefaciens.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Agrobacterium tumefaciens str. C58";
		}
		
	},
	
	/** The Dickeyadadantii. */
	Dickeyadadantii{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.PAMGO_Ddadantii.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Dickeya dadantii";
		}
		
	},
	
	/** The Magnaporthegrisea. */
	Magnaporthegrisea{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.PAMGO_Mgrisea.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Magnaporthe grisea";
		}
		
	},
	
	/** The Oomycetes. */
	Oomycetes{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.PAMGO_Oomycetes.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Oomycetes";
		}
		
	},
	
	/** The Aspergillusnidulans. */
	Aspergillusnidulans{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.aspgd.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Aspergillus nidulans";
		}
		
	},
	
	/** The Candidaalbicans. */
	Candidaalbicans{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.cgd.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Candida albicans";
		}
		
	},
	
	/** The Dictyosteliumdiscoideum. */
	Dictyosteliumdiscoideum{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.dictyBase.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Dictyostelium discoideum";
		}
		
	},
	
	/** The Drosophilamelanogaster. */
	Drosophilamelanogaster{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.fb.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Drosophila melanogaster";
		}
		
	},
	
	/** The Gallusgallus. */
	Gallusgallus{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_chicken.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Gallus gallus";
		}
		
	},
	
	/** The Gallusgalluscomplex. */
	Gallusgalluscomplex{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_chicken_complex.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Gallus gallus (complex)";
		}
		
	},
	
	/** The Gallusgallusrna. */
	Gallusgallusrna{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_chicken_rna.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Gallus gallus (rna)";
		}
		
	},
	
	/** The Bostaurus. */
	Bostaurus{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_cow.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Bos taurus";
		}
		
	},
	
	/** The Bostauruscomplex. */
	Bostauruscomplex{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_cow_complex.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Bos taurus (complex)";
		}
		
	},
	
	/** The Bostaurusrna. */
	Bostaurusrna{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_cow_rna.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Bos taurus (rna)";
		}
		
	},
	
	/** The Canislupusfamiliaris. */
	Canislupusfamiliaris{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_dog.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Canis lupus familiaris";
		}
		
	},
	
	/** The Canislupusfamiliariscomplex. */
	Canislupusfamiliariscomplex{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_dog_complex.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Canis lupus familiaris (complex)";
		}
		
	},
	
	/** The Canislupusfamiliarisrna. */
	Canislupusfamiliarisrna{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_dog_rna.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Canis lupus familiaris (rna)";
		}
		
	},
	
	/** The Homosapiens. */
	Homosapiens{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_human.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Homo sapiens";
		}
		
	},
	
	/** The Homosapienscomplex. */
	Homosapienscomplex{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_human_complex.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Homo sapiens (complex)";
		}
		
	},
	
	/** The Homosapiensrna. */
	Homosapiensrna{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_human_rna.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Homo sapiens (rna)";
		}
		
	},
	
	/** The Susscrofa. */
	Susscrofa{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_pig.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Sus scrofa";
		}
		
	},
	

	/** The Susscrofacomplex. */
	Susscrofacomplex{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_pig_complex.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Sus scrofa (complex)";
		}
		
	},
	
	/** The Susscrofarna. */
	Susscrofarna{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_pig_rna.gaf.gz";
		}
		
		@Override
		public String getOrganismName() {
			return "Sus scrofa (rna)";
		}
		
	},
	
	/** The Uni protmultispeciesno IE aannotations. */
	UniProtmultispeciesnoIEAannotations{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/goa_uniprot_all_noiea.gaf.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "UniProt [multispecies], no IEA annotations";
		}
	},
	
	/** The Uni protmultispecies. */
	UniProtmultispecies{
		@Override
		public String getDownloadURL() {
			return "ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/UNIPROT/goa_uniprot_all.gaf.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "UniProt [multispecies]";
		}
	},
	
	/** The Oryzasativa. */
	Oryzasativa{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.gramene_oryza.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Oryza sativa";
		}
	},
	
	/** The Comprehensive microbial resource. */
	ComprehensiveMicrobialResource{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.jcvi.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Comprehensive Microbial Resource";
		}
	},
	
	/** The Musmusculus. */
	Musmusculus{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.mgi.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Mus musculus";
		}
	},
	
	/** The Schizosaccharomycespombe. */
	Schizosaccharomycespombe{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.pombase.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Schizosaccharomyces pombe";
		}
	},
	
	/** The Pseudomonasaeruginosa PAO 1. */
	PseudomonasaeruginosaPAO1{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.pseudocap.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Pseudomonas aeruginosa PAO1";
		}
	},
	
	/** The Rattusnorvegicus. */
	Rattusnorvegicus{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.rgd.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Rattus norvegicus";
		}
	},
	
	/** The Solanaceae. */
	Solanaceae{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.sgn.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Solanaceae";
		}
	},
	
	/** The Arabidopsisthaliana. */
	Arabidopsisthaliana{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.tair.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Arabidopsis thaliana";
		}
	},
	
	/** The Caenorhabditiselegans. */
	Caenorhabditiselegans{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.wb.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Caenorhabditis elegans";
		}
	},
	
	/** The Daniorerio. */
	Daniorerio{
		@Override
		public String getDownloadURL() {
			return "http://geneontology.org/gene-associations/gene_association.zfin.gz";
		}
		
		
		@Override
		public String getOrganismName() {
			return "Danio rerio";
		}
	};
	
	
	/**
	 * Gets the download URL.
	 *
	 * @return the download URL
	 */
	public String getDownloadURL(){
		return getDownloadURL();
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName(){
		String url=getDownloadURL();
		return FilenameUtils.getBaseName(url);
	}
	
	/**
	 * Gets the organism name.
	 *
	 * @return the organism name
	 */
	public String getOrganismName() {
		return getOrganismName();
	}
	
	@Override
	public String toString() {
		return getOrganismName();
	}

}
