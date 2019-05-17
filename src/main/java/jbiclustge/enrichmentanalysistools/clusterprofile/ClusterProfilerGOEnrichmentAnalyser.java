package jbiclustge.enrichmentanalysistools.clusterprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import jbiclustge.annotation.goannotation.components.GOAspect;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.BitrConverterInfoContainer;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.ClusterProfilerBitr;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.executer.ClusterProfilerEnrichGOExecuter;
import jbiclustge.enrichmentanalysistools.clusterprofile.components.props.ClusterProfileEnrichGOPropsContainer;
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
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;

public class ClusterProfilerGOEnrichmentAnalyser extends ClusterProfilerEnrichmentAnalyser{

	

	private String keytype;
	private GOAspect goaspect=GOAspect.BIOLOGICALPROCESS;
    
    
	
	
	public ClusterProfilerGOEnrichmentAnalyser() {}
	
	
	public ClusterProfilerGOEnrichmentAnalyser(BiclusterList listbiclusters) {
		super(listbiclusters);
	}
	
	

	private ClusterProfilerGOEnrichmentAnalyser(String organismid, 
			String keytype,
			GOAspect goaspect,
			pvaluesAdjustMethod adjustmethod, 
			double pvalcutoff, 
			double qvalcutoff,
			int mingsize, 
			int maxgsize,
			BitrConverterInfoContainer geneidsinfoconverter) {

		this.organnotationdatabase = organismid;
		this.keytype = keytype;
		this.goaspect=goaspect;
		this.adjustmethod = adjustmethod;
		this.pvalcutoff = pvalcutoff;
		this.qvalcutoff = qvalcutoff;
		this.mingsize = mingsize;
		this.maxgsize = maxgsize;
		this.geneidsinfoconverter=geneidsinfoconverter;
	}


	public ClusterProfilerGOEnrichmentAnalyser setOrgDatabase(String id) {
		this.organnotationdatabase=id;
		return this;
	}
	
	
	public ClusterProfilerGOEnrichmentAnalyser setKeyType(String keytype) {
		this.keytype=keytype;
		return this;
	}
	
	public ClusterProfilerGOEnrichmentAnalyser setGOAspect(GOAspect goaspect) {
		this.goaspect=goaspect;
		return this;
	}
	

	@Override
	protected boolean executeRgseaProcess() throws Exception {


		if(listofbiclusters.size()>0) {

			

			if(organnotationdatabase==null) {
				changesupport.firePropertyChange(FIREPROPERTYGSEAANALYSERERROR,null,"Please define the org.xx.xx database.");
				throw new IOException("Please define the org.xx.xx database.");
			}
			else {

				ArrayList<String> universegenes=listofbiclusters.getAnalysedDataset().getGeneNamesList();
				
				if(geneidsinfoconverter!=null) {
					changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Converting identifiers of the genes...");
					ClusterProfilerBitr converter=(ClusterProfilerBitr) geneidsinfoconverter.getInstance(rsession, universegenes);
					converter.convertIdentifiers();
					mapofprobset2geneid=converter.getAllIdentifiers();
					
					System.out.println("Converted: "+mapofprobset2geneid.size()+" IDS");
					
					ArrayList<String> newunivgenes=new ArrayList<>(mapofprobset2geneid.values());
					universegenes=newunivgenes;
				}

				changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Executing GO enrichment analysis...");

				int n=1;
				for (BiclusterResult bicres : listofbiclusters) {

					LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Analyzing bicluster "+n+" of "+listofbiclusters.size());
					changesupport.firePropertyChange(FIREPROPERTYGSEACHANGESUBTASKSTATUS, null, "Processing result "+n+" of "+listofbiclusters.size());

					if(!stopproc) {
						ClusterProfilerEnrichGOExecuter proc=new ClusterProfilerEnrichGOExecuter(rsession, bicres);
						proc.setOrganismDatabase(organnotationdatabase)
						.setKeyType(keytype)
						.setGOAspect(goaspect)
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
					LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("GO enrichment analysis were successfully performed to "+ listofbiclusters.getUsedmethod()+" method");
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
		if(organnotationdatabase!=null && !organnotationdatabase.isEmpty()) {
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
			organnotationdatabase=PropertiesUtilities.getStringPropertyValue(props, ClusterProfileEnrichGOPropsContainer.GOANOTORGDATABASE, null, getClass());
			keytype=PropertiesUtilities.getStringPropertyValue(props, ClusterProfileEnrichGOPropsContainer.KEYTYPE, null, getClass());
			goaspect=GOAspect.getAspectFromString(PropertiesUtilities.getStringPropertyValue(props, ClusterProfileEnrichGOPropsContainer.GOASPECT, GOAspect.BIOLOGICALPROCESS.getGOType(), getClass()));
		}

	}


	@Override
	public HashSet<String> getUnannotatedGeneNames() {
		return null;
	}


	@Override
	public EnrichmentAnalyserProcessor copyWorkingInstance() {
		return new ClusterProfilerGOEnrichmentAnalyser(organnotationdatabase, keytype,goaspect, adjustmethod, pvalcutoff, qvalcutoff, mingsize, maxgsize,geneidsinfoconverter);
	}


	@Override
	public GSEAAnalyserType getTypeAnalyserProcessor() {
		return GSEAAnalyserType.ClusterProfilerGO;
	}


	@Override
	protected ArrayList<String> getRObjectsToRemove() {
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {

	LogMessageCenter.getLogger().setLogLevel(MTULogLevel.INFO);
	

	
	
	/*BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
			"/home/orocha/Nextcloud/Data_jbiclustge_development/testes/dataset_GO_Filtered.csv");*/
	
	/*BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/varios/Blood1/Biclustering_results1/bimineplus_config_1/BiMineplus_1hbaxumcrrn79_2018-07-27_00-48-21/JBiclustGE_csv_overlap_0.25_filtered.bicge", 
			"/home/orocha/discodados/ApenasTrabalho/GeneExpressionProfiles/REsultados/varios/Blood1/dataset.csv");
*/
	
	BiclusterList listbics=BiclusterList.importBiclusterListFromJBiclustGEFormat("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/blood1/JBiclustGE_csv.bicge", 
			"/home/orocha/Nextcloud/Data_jbiclustge_development/testes/blood1/dataset.csv");

	
	BitrConverterInfoContainer convinfo=new BitrConverterInfoContainer("hgu95av2.db", OrgDatabaseItem.PROBEID.toString(),OrgDatabaseItem.ENTREZID.toString(), false);
	
	ClusterProfilerGOEnrichmentAnalyser analy=new ClusterProfilerGOEnrichmentAnalyser(listbics);
	analy.setOrgDatabase("hgu95av2.db").setKeyType("PROBEID").setPvalueAdjustMethod(pvaluesAdjustMethod.BH);
	//analy.setOrganismKeggID("hsa").setPvalueAdjustMethod(pvaluesAdjustMethod.BH).setBitrConvertInformation(convinfo);
	//analy.setOrganismKeggID("sce").setKeyType(ClusterProfileKeyType.ncbigeneid).setPvalueAdjustMethod(pvaluesAdjustMethod.BONFERRONI);

	analy.run();
	
	EnrichmentAnalysisResultList res=analy.getEnrichmentAnalysisResults();
	MTUWriterUtils.writeDataTofile("/home/orocha/Nextcloud/Data_jbiclustge_development/testes/results/teste5_GO.txt", res.toString());

}
	

}
