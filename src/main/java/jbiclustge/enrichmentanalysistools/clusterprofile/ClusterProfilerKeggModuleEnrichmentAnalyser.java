package jbiclustge.enrichmentanalysistools.clusterprofile;

import java.io.IOException;
import java.util.ArrayList;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.BitrConverterInfoContainer;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.ClusterProfilerBitr;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.ClusterProfilerEnrichKEGGModuleExecuter;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalyserProcessor;
import jbiclustge.enrichmentanalysistools.common.EnrichmentAnalysisResultList;
import jbiclustge.enrichmentanalysistools.common.GSEAAnalyserType;
import jbiclustge.enrichmentanalysistools.common.OrgDatabaseItem;
import jbiclustge.enrichmentanalysistools.common.pvaluesAdjustMethod;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;
import pt.ornrocha.logutils.MTULogLevel;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;

public class ClusterProfilerKeggModuleEnrichmentAnalyser extends ClusterProfilerKeggPathwayEnrichmentAnalyser{

	

	public ClusterProfilerKeggModuleEnrichmentAnalyser() {}
	
	
	public ClusterProfilerKeggModuleEnrichmentAnalyser(BiclusterList listbiclusters) {
		super(listbiclusters);
	}
	
	protected ClusterProfilerKeggModuleEnrichmentAnalyser(String organismid, 
			ClusterProfileKeyType keytype,
			pvaluesAdjustMethod adjustmethod, 
			double pvalcutoff, 
			double qvalcutoff,
			int mingsize, 
			int maxgsize) {

		this.organismid = organismid;
		this.keytype = keytype;
		this.adjustmethod = adjustmethod;
		this.pvalcutoff = pvalcutoff;
		this.qvalcutoff = qvalcutoff;
		this.mingsize = mingsize;
		this.maxgsize = maxgsize;
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
						ClusterProfilerEnrichKEGGModuleExecuter proc=new ClusterProfilerEnrichKEGGModuleExecuter(rsession, bicres);
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
	
	
	@Override
	public GSEAAnalyserType getTypeAnalyserProcessor() {
		return GSEAAnalyserType.ClusterProfilerKEGGModule;
	}
	
	@Override
	public EnrichmentAnalyserProcessor copyWorkingInstance() {
		return new ClusterProfilerKeggModuleEnrichmentAnalyser(organismid, keytype, adjustmethod, pvalcutoff, qvalcutoff, mingsize, maxgsize);
	}
	
	
	public static void main(String[] args) throws Exception {

		LogMessageCenter.getLogger().setLogLevel(MTULogLevel.INFO);




		/*BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
			"/home/orocha/Nextcloud/Data_jbiclustge_development/testes/dataset_GO_Filtered.csv");*/

		BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/varios/Blood1/Biclustering_results1/bimineplus_config_1/BiMineplus_1hbaxumcrrn79_2018-07-27_00-48-21/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
				"/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/varios/Blood1/dataset.csv");


		BitrConverterInfoContainer convinfo=new BitrConverterInfoContainer("hgu95av2.db", OrgDatabaseItem.PROBEID.toString(),OrgDatabaseItem.ENTREZID.toString(), false);

		ClusterProfilerKeggModuleEnrichmentAnalyser analy=new ClusterProfilerKeggModuleEnrichmentAnalyser(listbics);
		analy.setOrganismKeggID("hsa").setPvalueAdjustMethod(pvaluesAdjustMethod.BH).setBitrConvertInformation(convinfo);
		//analy.setOrganismKeggID("sce").setKeyType(ClusterProfileKeyType.ncbigeneid).setPvalueAdjustMethod(pvaluesAdjustMethod.BONFERRONI);

		analy.run();

		EnrichmentAnalysisResultList res=analy.getEnrichmentAnalysisResults();
		MTUWriterUtils.writeDataTofile("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/results/teste5_kegg_module.txt", res.toString());

	}
	
	
	
}
