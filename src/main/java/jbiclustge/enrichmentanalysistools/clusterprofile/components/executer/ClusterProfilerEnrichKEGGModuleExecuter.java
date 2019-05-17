package jbiclustge.enrichmentanalysistools.clusterprofile.components.executer;

import java.io.IOException;
import java.util.Properties;

import org.math.R.Rsession;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.props.ClusterProfileEnrichKeggModulePropsContainer;
import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.results.biclusters.containers.BiclusterResult;

public class ClusterProfilerEnrichKEGGModuleExecuter extends ClusterProfilerEnrichKEGGExecuter{

	

	private static String MAINID="CPkeggModule";
	
	
	
	public ClusterProfilerEnrichKEGGModuleExecuter(Rsession rsession) {
		super(rsession);
	}


	
	public ClusterProfilerEnrichKEGGModuleExecuter(Rsession rsession, BiclusterResult res) {
		super(rsession, res);
	}



	
	
	
	public ClusterProfilerEnrichKEGGModuleExecuter setEnrichKeggProperties(Properties props) {

		ClusterProfileEnrichKeggModulePropsContainer castprops=null;

		if(!(props instanceof ClusterProfileEnrichKeggModulePropsContainer)) {
			castprops=(ClusterProfileEnrichKeggModulePropsContainer) ClusterProfileEnrichKeggModulePropsContainer.loadFromOtherProperties(props);
		}

		organismid=castprops.getKeggOrganism();
		keytype=ClusterProfileKeyType.getKeyTypeFromString(castprops.getKeyType());
		pvaladjmethod=pvaluesAdjustMethod.getMTCMethodFromString(castprops.getPvalueAdjustMethod());
		pvalcutoff=castprops.getPvalueCutOff();
		mingsize=castprops.getminGSSize();
		maxgsize=castprops.getmaxGSSize();
		qvalcutoff=castprops.getQvalueCutOff();


		return this;
	}
	
    @Override
	protected String getMainID() {
		return MAINID;
	}

	
	@Override
	protected String getRCmd(String outputid) throws IOException {

		StringBuilder str=new StringBuilder();

		str.append(outputid+" <- enrichMKEGG(gene ="+ loadGeneSet()+", ");
		str.append("organism = '"+organismid+"',");
		str.append("keyType = \""+keytype.toString()+"\", ");
		str.append("pAdjustMethod = \""+pvaladjmethod.toString()+"\", ");
		if(universegenes!=null)
			str.append("universe = names("+loadUniversalGeneSet()+"), ");
		if(mingsize!=10 && mingsize<maxgsize && mingsize>0)
			str.append("minGSSize = "+mingsize+", ");
		if(maxgsize!=500 && maxgsize>mingsize && maxgsize>0)
			str.append("maxGSSize = "+maxgsize+", ");
		if(qvalcutoff!=0.2)
			str.append("qvalueCutoff = "+qvalcutoff+", ");
		str.append("pvalueCutoff = "+pvalcutoff);
		str.append(")");

		return str.toString();
	}

	

	

}
