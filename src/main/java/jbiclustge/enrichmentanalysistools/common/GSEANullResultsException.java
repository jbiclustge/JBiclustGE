package jbiclustge.enrichmentanalysistools.common;

public class GSEANullResultsException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public GSEANullResultsException() {
		super("Results of Gene Set Enrichment Analysis Were Null, please try to change the initial configurations of topGO.");
	}
	
	
	public GSEANullResultsException(String message) {
        super(message);
    }

}
