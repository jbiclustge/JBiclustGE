package jbiclustge.enrichmentanalysistools.clusterprofile.components.executer;

import java.io.IOException;
import java.util.ArrayList;

import org.math.R.Rsession;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;
import pt.ornrocha.arrays.MTUEnumUtils;

public class BitrConverterInfoContainer {
	
	
	private String organism;
	private String fromidtype;
	private String toidtype;
	private boolean bykegg=false;
	
	
	/**
	 * 
	 * @param organism is related to name of database or the kegg name of the organism (if option bykegg is true)
	 * @param fromidtype 
	 * @param toidtype
	 * @param bykegg 
	 */
	
	
	public BitrConverterInfoContainer(String organism, String fromidtype, String toidtype, boolean bykegg) {
		this.organism = organism;
		this.fromidtype = fromidtype;
		this.toidtype = toidtype;
		this.bykegg = bykegg;
	}
	
	
	
	public synchronized BitrConverterFeature getInstance(Rsession rsession, ArrayList<String> geneids) throws Exception {
		
		if(bykegg) {
			
			ClusterProfilerBitrKegg conv=new ClusterProfilerBitrKegg(rsession, geneids);
			if(!ClusterProfileKeyType.isValidKeyTypeString(fromidtype))
				throw new IOException("The key of 'fromidtype' its invalid, must be one of "+MTUEnumUtils.getEnumNamesString(ClusterProfileKeyType.class, ", "));
			else if(!ClusterProfileKeyType.isValidKeyTypeString(toidtype))
				throw new IOException("The key of 'toidtype' its invalid, must be one of "+MTUEnumUtils.getEnumNamesString(ClusterProfileKeyType.class, ", "));
			else {
				conv.setOrganismName(organism).setTypeOforiginalIdentifiers(ClusterProfileKeyType.getKeyTypeFromString(fromidtype)).setTypeOfIdentifierstoConvertto(ClusterProfileKeyType.getKeyTypeFromString(toidtype));
				return conv;
			}
		}
		else {
			ClusterProfilerBitr conv=new ClusterProfilerBitr(rsession, geneids);
		    conv.setOrganismDatabase(organism).setTypeOforiginalIdentifiers(fromidtype).setTypeOfIdentifierstoConvertto(toidtype);
		    return conv;
			
		}
	}
	
	
	public String getOrganismReference() {
		return organism;
	}
	
	public boolean useKeggBitrConverter() {
		return bykegg;
	}

}
