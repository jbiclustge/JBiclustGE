package jbiclustge.enrichmentanalysistools.clusterprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.BitrConverterInfoContainer;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.ClusterProfilerBitr;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.ClusterProfilerEnrichKEGGExecuter;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.props.ClusterProfileEnrichKeggPropsContainer;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.GSEAAnalyserType;
import jbiclustge.enrichmentanalysistools.common.OrgDatabaseItem;
import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.results.biclusters.enriched.EnrichedBiclusterList;
import pt.ornrocha.logutils.MTULogLevel;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

public class ClusterProfilerKeggPathwayEnrichmentAnalyser extends ClusterProfilerEnrichmentAnalyser{


	protected String organismid;
	protected ClusterProfileKeyType keytype=ClusterProfileKeyType.kegg;




	public ClusterProfilerKeggPathwayEnrichmentAnalyser() {}


	public ClusterProfilerKeggPathwayEnrichmentAnalyser(BiclusterList listbiclusters) {
		super(listbiclusters);
	}
	
	public ClusterProfilerKeggPathwayEnrichmentAnalyser(BiclusterList listbiclusters, Properties settings) {
		super(listbiclusters, settings);
		readProperties();
	}



	protected ClusterProfilerKeggPathwayEnrichmentAnalyser(String organismid, 
			ClusterProfileKeyType keytype,
			pvaluesAdjustMethod adjustmethod, 
			double pvalcutoff, 
			double qvalcutoff,
			int mingsize, 
			int maxgsize,
			BitrConverterInfoContainer geneidsinfoconverter) {

		this.organismid = organismid;
		this.keytype = keytype;
		this.adjustmethod = adjustmethod;
		this.pvalcutoff = pvalcutoff;
		this.qvalcutoff = qvalcutoff;
		this.mingsize = mingsize;
		this.maxgsize = maxgsize;
		this.geneidsinfoconverter=geneidsinfoconverter;
	}


	public ClusterProfilerKeggPathwayEnrichmentAnalyser setOrganismKeggID(String id) {
		this.organismid=id;
		return this;
	}


	public ClusterProfilerKeggPathwayEnrichmentAnalyser setKeyType(ClusterProfileKeyType keytype) {
		this.keytype=keytype;
		return this;
	}


	@Override
	protected boolean executeRgseaProcess() throws Exception {


		if(listofbiclusters.size()>0) {



			if(organismid==null) {
				changesupport.firePropertyChange(FIREPROPERTYGSEAANALYSERERROR,null,"Please define the organism kegg identifier.");
				throw new IOException("Please define the organism kegg identifier.");
			}
			else {

				ArrayList<String> universegenes=listofbiclusters.getAnalysedDataset().getGeneNamesList();
				
				
				if(geneidsinfoconverter!=null) {
					changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Converting identifiers of the genes...");
					ClusterProfilerBitr converter=(ClusterProfilerBitr) geneidsinfoconverter.getInstance(rsession, universegenes);
					converter.convertIdentifiers();
					mapofprobset2geneid=converter.getAllIdentifiers();

					ArrayList<String> newunivgenes=new ArrayList<>(mapofprobset2geneid.values());
					universegenes=newunivgenes;
				}

				changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Executing Kegg enrichment analysis...");

				int n=1;
				for (BiclusterResult bicres : listofbiclusters) {

					LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Analyzing bicluster "+n+" of "+listofbiclusters.size());
					changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Processing result "+n+" of "+listofbiclusters.size());

					if(!stopproc) {
						ClusterProfilerEnrichKEGGExecuter proc=new ClusterProfilerEnrichKEGGExecuter(rsession, bicres);
						proc.setOrganismKeggID(organismid)
						.setKeyType(keytype)
						.setUniversalGeneSet(universegenes)
						.setPvalueAdjustMethod(adjustmethod)
						.setPvalueCutOFF(pvalcutoff)
						.setQvalueCutOFF(qvalcutoff)
						.setMinGSSize(mingsize)
						.setmaxGSSize(maxgsize)
						.setProbset2GeneidMapping(mapofprobset2geneid);


						tempresultlist.add(proc.run());

						float progress = (float)n/listofbiclusters.size();
						changesupport.firePropertyChange(FIREPROPERTYGSEACHANGEPROGRESS, null, progress);

						/*	System.out.println("BIC "+n+"\n");
					printgenes(bicres);
					System.out.println("\n\n");*/
						n++;
					}
				}

				if(!stopproc) {
					LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Kegg enrichment analysis were successfully performed to "+ listofbiclusters.getUsedmethod()+" method");
					return true;
				}
			}


		}

		return false;

	}


	private void printgenes(BiclusterResult res) {

		ArrayList<String> genes=res.getGeneNames();
		StringBuilder str=new StringBuilder();

		str.append("c(");
		for (int i = 0; i < genes.size(); i++) {
			str.append("\""+genes.get(i)+"\"");
			if(i<(genes.size()-1))
				str.append(",");
		}
		str.append(")");

		System.out.println(str.toString());
	}



	private void printgenes(ArrayList<String> genes) {

		StringBuilder str=new StringBuilder();

		str.append("c(");
		for (int i = 0; i < genes.size(); i++) {
			str.append("\""+genes.get(i)+"\"");
			if(i<(genes.size()-1))
				str.append(",");
		}
		str.append(")");

		System.out.println(str.toString());
	}



	@Override
	protected boolean validToLoadExtraPackages() {
		if(geneidsinfoconverter!=null && !geneidsinfoconverter.useKeggBitrConverter()) {
			organnotationdatabase=geneidsinfoconverter.getOrganismReference();
			return true;
		}
		return false;
	}


	@Override
	protected ArrayList<RPackageInfo> requiredExtraLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define(organnotationdatabase, true));

		return list;
	}





	@Override
	protected boolean mandatoryontologyfile() {
		return false;
	}


	@Override
	protected boolean mandatoryannotationfile() {
		return false;
	}


	@Override
	protected String getDefaultOntologyFilePath() {
		return null;
	}


	@Override
	protected void readProperties() {

		if(props!=null) {
			super.readProperties();
			organismid=PropertiesUtilities.getStringPropertyValue(props, ClusterProfileEnrichKeggPropsContainer.KEGGORGANISMID, null, getClass());
			keytype=ClusterProfileKeyType.getKeyTypeFromString(PropertiesUtilities.getStringPropertyValue(props, ClusterProfileEnrichKeggPropsContainer.KEYTYPE, null, getClass()));
		}

	}


	@Override
	public HashSet<String> getUnannotatedGeneNames() {
		return null;
	}


	@Override
	public EnrichmentAnalyserProcessor copyWorkingInstance() {
		return new ClusterProfilerKeggPathwayEnrichmentAnalyser(organismid, keytype, adjustmethod, pvalcutoff, qvalcutoff, mingsize, maxgsize,geneidsinfoconverter);
	}


	@Override
	public GSEAAnalyserType getTypeAnalyserProcessor() {
		return GSEAAnalyserType.ClusterProfilerKEGGPathway;
	}


	@Override
	protected ArrayList<String> getRObjectsToRemove() {
		return null;
	}


	public static void main(String[] args) throws Exception {

		LogMessageCenter.getLogger().setLogLevel(MTULogLevel.INFO);

		//LogMessageCenter.getLogger().enableStackTrace();


		/*BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
			"/home/orocha/Nextcloud/Data_jbiclustge_development/testes/dataset_GO_Filtered.csv");*/

		/*BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/varios/Blood1/Biclustering_results1/bimineplus_config_1/BiMineplus_1hbaxumcrrn79_2018-07-27_00-48-21/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
				"/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/varios/Blood1/dataset.csv");*/
		
		BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/Nextcloud/Data_jbiclustge_development/gsea_testes/BBC_1h7zsq0y5jcxf_2018-07-31_00-36-37/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
				"/home/orocha/Nextcloud/Data_jbiclustge_development/gsea_testes/dataset.csv");


		//BitrConverterInfoContainer convinfo=new BitrConverterInfoContainer("hgu95av2.db", OrgDatabaseItem.PROBEID.toString(),OrgDatabaseItem.ENTREZID.toString(), false);
		
		BitrConverterInfoContainer convinfo=new BitrConverterInfoContainer("hgu95av2.db", OrgDatabaseItem.PROBEID.toString(),OrgDatabaseItem.ENTREZID.toString(), false);

		ClusterProfilerKeggPathwayEnrichmentAnalyser analy=new ClusterProfilerKeggPathwayEnrichmentAnalyser(listbics);
		analy.setOrganismKeggID("hsa").setPvalueAdjustMethod(pvaluesAdjustMethod.BH).setBitrConvertInformation(convinfo);
		//analy.setOrganismKeggID("sce").setKeyType(ClusterProfileKeyType.ncbigeneid).setPvalueAdjustMethod(pvaluesAdjustMethod.BONFERRONI);

		analy.run();

		EnrichmentAnalysisResultList res=analy.getEnrichmentAnalysisResults();
	    res.filterAndProcessResults(0.05, true);
		EnrichedBiclusterList l=res.getEnrichedBiclusterList();
		l.writeBiclusterListToJBiclustGEOutputFormat("/home/orocha/Nextcloud/Data_jbiclustge_development/gsea_testes", "Enriched_biclusters");
		
		//MTUWriterUtils.writeDataTofile("/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/Estudos_para_paper/varios/Blood1/Biclustering_results/teste5.txt", res.toString());

	}


}
