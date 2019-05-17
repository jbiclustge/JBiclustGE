package jbiclustge.enrichmentanalysistools.clusterprofile.components.executer;

import java.util.ArrayList;
import java.util.Map;

import org.javatuples.Pair;
import org.math.R.Rsession;

import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultsContainer;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.stringutils.MTUStringUtils;

public abstract class ClusterProfilerExecuter {
	
	
	protected Rsession rsession;
	protected String objlabel;
	private ArrayList<String> removeobjects=new ArrayList<>();
	protected BiclusterResult bicluster;
	protected Map<String, String> mapofprobset2geneid;
	
	
	public ClusterProfilerExecuter(Rsession rsession) {
		this.rsession=rsession;
		this.objlabel=MTUStringUtils.shortUUID();
	}
	
	public ClusterProfilerExecuter(Rsession rsession, BiclusterResult res) {
		this(rsession);
		this.bicluster=res;

	}
	
	public ClusterProfilerExecuter setProbset2GeneidMapping(Map<String, String> mapofprobset2geneid) {
		this.mapofprobset2geneid=mapofprobset2geneid;
		return this;
	}
	
	
	public void setBiclusterToAnalyse(BiclusterResult res) {
		this.bicluster=res;
	}
	
	
	public abstract EnrichmentAnalysisResultsContainer run() throws Exception;
	
	
	protected void addObjectToRemoveFromREnv(String id) {
		removeobjects.add(id);
	}
	
	
	
	protected void removeCollectedObjects() {
		if(removeobjects.size()>0)
			rsession.unset(removeobjects);
	}

	protected synchronized Pair<Integer, Integer> getSignificantAnnotatedNumberGenes(String value) {
		String[] values=value.split("/");
		return new Pair<Integer, Integer>(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
	}
	
	protected synchronized String[] getAssociatedGenes(String genestr) {
		
		return genestr.split("/");
	}
}
