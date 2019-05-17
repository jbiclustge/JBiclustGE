package jbiclustge.enrichmentanalysistools.clusterprofile.components;

public enum ClusterProfileKeyType {

	
	kegg{
		
		@Override
		public String getName() {
			return "kegg";
		}
	},
	ncbigeneid{
		
		@Override
		public String getName() {
			return "ncbi-geneid";
		}
	},
	ncibproteinid{
		
		@Override
		public String getName() {
			return "ncib-proteinid";
		}
	},
	uniprot{
		
		@Override
		public String getName() {
			return "uniprot";
		}
	};
	
	
	public String getName() {
		return getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public static ClusterProfileKeyType getKeyTypeFromString(String name) {
		
		for (ClusterProfileKeyType elem : ClusterProfileKeyType.values()) {
			if(name.toLowerCase().equals(elem.getName()))
				return elem;
		}
		return ClusterProfileKeyType.kegg;
	}
	

	public static boolean isValidKeyTypeString(String name) {
		
		for (ClusterProfileKeyType elem : ClusterProfileKeyType.values()) {
			if(name.toLowerCase().equals(elem.getName()))
				return true;
		}
		return false;
	}
}
