package jbiclustge.enrichmentanalysistools.clusterprofile.components.executer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface BitrConverterFeature {
	
	
	
	public void convertIdentifiers() throws Exception;
	public ArrayList<String> getIdentifiersNotconverted();
	public LinkedHashMap<String, String> getAllIdentifiers();
	public LinkedHashMap<String, String> getOnlyIdentifiersThatWereConverted();

}
