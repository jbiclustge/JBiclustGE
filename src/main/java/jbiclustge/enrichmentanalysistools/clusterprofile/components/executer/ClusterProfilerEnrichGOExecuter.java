package jbiclustge.enrichmentanalysistools.clusterprofile.components.executer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.javatuples.Pair;
import org.math.R.Rsession;

import jbiclustge.annotation.goannotation.components.GOAspect;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.props.ClusterProfileEnrichKeggPropsContainer;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultsContainer;
import jbiclustge.enrichmentanalysistools.common.OrgDatabaseItem;
import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

public class ClusterProfilerEnrichGOExecuter extends ClusterProfilerExecuter{

	

	private static String MAINID="CPGo";
	private String orgdb;
	private String keytype;
	private GOAspect goaspect=GOAspect.BIOLOGICALPROCESS;
	private pvaluesAdjustMethod pvaladjmethod=pvaluesAdjustMethod.NONE;
	private ArrayList<String> universegenes=null;
	private double pvalcutoff=0.05;
	private double qvalcutoff=0.2;
	private int mingsize=10;
	private int maxgsize=500;
	private String[] inputgeneset;
	
	
	
	public ClusterProfilerEnrichGOExecuter(Rsession rsession) {
		super(rsession);
	}


	
	public ClusterProfilerEnrichGOExecuter(Rsession rsession, BiclusterResult res) {
		super(rsession, res);
	}


	protected void setGeneSetToAnalyse(ArrayList<String> genes) {
		inputgeneset=genes.toArray(new String[genes.size()]);
	}

	
	public ClusterProfilerEnrichGOExecuter setOrganismDatabase(String orgdb) {
		this.orgdb=orgdb;
		return this;
	}
	
	public ClusterProfilerEnrichGOExecuter setKeyType(String keytype) {
		this.keytype=keytype;
		return this;
	}
	
	public ClusterProfilerEnrichGOExecuter setGOAspect(GOAspect goaspect) {
		this.goaspect=goaspect;
		return this;
	}
	
	public ClusterProfilerEnrichGOExecuter setKeyType(OrgDatabaseItem keytype) {
		return setKeyType(keytype.toString());
	}
	
	
	public ClusterProfilerEnrichGOExecuter setPvalueAdjustMethod(pvaluesAdjustMethod pvaladj) {
		this.pvaladjmethod=pvaladj;
		return this;
	}
	
	
	public ClusterProfilerEnrichGOExecuter setUniversalGeneSet(ArrayList<String> genelist) {
		this.universegenes=genelist;
		return this;
	}
	
	public ClusterProfilerEnrichGOExecuter setMinGSSize(int value) {
		this.mingsize=value;
		return this;
	}
	
	public ClusterProfilerEnrichGOExecuter setmaxGSSize(int value) {
		this.maxgsize=value;
		return this;
	}
	
	
	public ClusterProfilerEnrichGOExecuter setPvalueCutOFF(double value) {
		this.pvalcutoff=value;
		return this;
	}
	
	public ClusterProfilerEnrichGOExecuter setQvalueCutOFF(double value) {
		this.qvalcutoff=value;
		return this;
	}
	
	
	
	/*public ClusterProfilerEnrichGOExecuter setEnrichKeggProperties(Properties props) {

		ClusterProfileEnrichKeggPropsContainer castprops=null;

		if(!(props instanceof ClusterProfileEnrichKeggPropsContainer)) {
			castprops=ClusterProfileEnrichKeggPropsContainer.loadFromOtherProperties(props);
		}

		orgdb=castprops.getKeggOrganism();
		pvaladjmethod=pvaluesAdjustMethod.getMTCMethodFromString(castprops.getPvalueAdjustMethod());
		pvalcutoff=castprops.getPvalueCutOff();
		mingsize=castprops.getminGSSize();
		maxgsize=castprops.getmaxGSSize();
		qvalcutoff=castprops.getQvalueCutOff();


		return this;
	}*/
	


	@Override
	public EnrichmentAnalysisResultsContainer run() throws Exception {

		EnrichmentAnalysisResultsContainer result=new EnrichmentAnalysisResultsContainer(bicluster);
		
		try {
			
			ArrayList<String> analysegenes=null;
			if(mapofprobset2geneid!=null) {
				analysegenes=new ArrayList<>();
				for (int i = 0; i < bicluster.getGeneNames().size(); i++) {
					String geneid=bicluster.getGeneNames().get(i);
					if(mapofprobset2geneid.containsKey(geneid))
						analysegenes.add(mapofprobset2geneid.get(geneid));
					else
						analysegenes.add(geneid);
				}
			}
			else
				analysegenes=bicluster.getGeneNames();
			
			//System.out.println(analysegenes);
			
			setGeneSetToAnalyse(analysegenes);
			
			String outid=MAINID+objlabel;
			addObjectToRemoveFromREnv(outid);


			rsession.silentlyEval(getRCmd(outid), false);
            
			int nterms=0;
			try {
				nterms=rsession.eval("nrow("+outid+")").asInteger();
			} catch (Exception e) {
				LogMessageCenter.getLogger().toClass(getClass()).addWarnMessage("Rsession returned a null value concerning the number of rows of clusterprofiler object");
			}
			System.out.println(nterms);
			if(nterms>0) {
				
				ArrayList<String> termids=new ArrayList<>(Arrays.asList(rsession.eval(outid+"[,1]").asStrings()));
				ArrayList<String> termnames=new ArrayList<>(Arrays.asList(rsession.eval(outid+"[,2]").asStrings()));
				ArrayList<String> siggenes=new ArrayList<>(Arrays.asList(rsession.eval(outid+"[,3]").asStrings()));
				
				double[] tmppvals=rsession.eval(outid+"[,5]").asDoubles();
				ArrayList<Double> pvals=new ArrayList<>();
				for (Double d : tmppvals) pvals.add(d);
				
				double[] tmpadjpvals=rsession.eval(outid+"[,6]").asDoubles();
				ArrayList<Double> adjpval=new ArrayList<>();
				for (Double d : tmpadjpvals) adjpval.add(d);
				
				ArrayList<String> assocgenes=new ArrayList<>(Arrays.asList(rsession.eval(outid+"[,8]").asStrings()));
				
				
				for (int i = 0; i < termids.size(); i++) {
					
					String termid=termids.get(i);
					result.addMapTermIDWithTermName(termid, termnames.get(i));
					result.addTermPValue(termid, pvals.get(i));
					result.addTermAdjustedPValue(termid, adjpval.get(i));
					Pair<Integer, Integer> significantgenes=getSignificantAnnotatedNumberGenes(siggenes.get(i));
					result.addNumberSignificantAnnotatedGenesToTermID(termid, significantgenes.getValue0());
					result.addNumberAnnotatedGenesToGOTerm(termid, significantgenes.getValue1());
					result.addToMapTerm2GeneAssociation(termid, getAssociatedGenes(assocgenes.get(i)));
					
				}

			}
			
			removeCollectedObjects();
			
		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage(e);
		}
		
		return result;
	}
	
	
	/*private synchronized Pair<Integer, Integer> getSignificantAnnotatedNumberGenes(String value) {
		String[] values=value.split("/");
		return new Pair<Integer, Integer>(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
	}
	
	private synchronized String[] getAssociatedGenes(String genestr) {
		
		return genestr.split("/");
	}*/
	
	private String getRCmd(String outputid) throws IOException {

		StringBuilder str=new StringBuilder();

		str.append(outputid+" <- enrichGO(gene ="+ loadGeneSet()+", ");
		str.append("OrgDb = "+orgdb+",");
		str.append("keyType = \""+keytype+"\", ");
		str.append("ont = \""+goaspect.getGOType()+"\",");
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

	
	 
	private String loadGeneSet() throws IOException {

		if(inputgeneset!=null && inputgeneset.length>0) {
			String genesetid="ingenes"+objlabel;
			rsession.set(genesetid, inputgeneset);
			addObjectToRemoveFromREnv(genesetid);
			return genesetid;
		}
		else
			throw new IOException("Input gene set array its empty, please define a valid array with name of genes.");

	}
	
	
	private String loadUniversalGeneSet() throws IOException {
		if(universegenes!=null && universegenes.size()>0) {
			String univgenesetid="univgenes"+objlabel;
			rsession.set(univgenesetid, universegenes.toArray(new String[universegenes.size()]));
			addObjectToRemoveFromREnv(univgenesetid);
			return univgenesetid;
		}
		else
			throw new IOException("Input universal gene set array its empty, please define a valid array with name of genes.");

	}
	

}
