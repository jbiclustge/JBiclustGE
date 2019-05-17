package jbiclustge.enrichmentanalysistools.clusterprofile.components.executer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.math.R.Rsession;

import jbiclustge.enrichmentanalysistools.clusterprofile.components.ClusterProfileKeyType;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.stringutils.MTUStringUtils;


public class ClusterProfilerBitrKegg implements BitrConverterFeature{
	
	
	protected Rsession rsession;
	protected ArrayList<String> geneids;
	
	private String organismname;
	private  String fromidtype;
	private  String toidtype;
	private  ArrayList<String> notconvertedids;
	private  LinkedHashMap<String, String> allset;
	private  LinkedHashMap<String, String> onlymatch;
	//private  boolean areconverted=false;
	
	
	public ClusterProfilerBitrKegg(Rsession session, ArrayList<String> geneids) {
		this.rsession=session;
		this.geneids=geneids;
	}
	
	public ClusterProfilerBitrKegg setOrganismName(String orgname) {
		this.organismname=orgname;
		return this;
	}
	
	
	public ClusterProfilerBitrKegg setTypeOforiginalIdentifiers(ClusterProfileKeyType fromidtype) {
		this.fromidtype=fromidtype.toString();
		return this;
	}

	
	public ClusterProfilerBitrKegg setTypeOfIdentifierstoConvertto(ClusterProfileKeyType fromidtype) {
		this.toidtype=fromidtype.toString();
		return this;
	}
	

	
	public void convertIdentifiers() throws Exception{

		allset=new LinkedHashMap<>();
		onlymatch=new LinkedHashMap<>();
		notconvertedids=new ArrayList<>();



		String[] inputgenes=geneids.toArray(new String[geneids.size()]);
		String ingenesid="genes"+MTUStringUtils.shortUUID();
		rsession.set(ingenesid, inputgenes);

		String outid = "out"+MTUStringUtils.shortUUID();

		try {
			rsession.silentlyEval(outid+" <- bitr("+ingenesid+", fromType=\""+fromidtype+"\", toType=\""+toidtype+"\", organism=\""+organismname+"\")");
		} catch (Exception e) {
		}


		int nconv=0;


		String[] origids=null;
		String[] convids=null;


		try {
			origids=rsession.silentlyEval(outid+"[,1]").asStrings();
			convids=rsession.silentlyEval(outid+"[,2]").asStrings();
		} catch (Exception e) {
			// TODO: handle exception
		}


		if(origids!=null && convids!=null) {

			ArrayList<String> tmporigids=new ArrayList<>(Arrays.asList(origids));
			ArrayList<String> tmpconvids=new ArrayList<>(Arrays.asList(convids));


			for (int i = 0; i < geneids.size(); i++) {

				String gid=geneids.get(i);
				if(tmporigids.contains(gid)) {
					String newid=tmpconvids.get(tmporigids.indexOf(gid));
					allset.put(gid, newid);
					onlymatch.put(gid, newid);
					nconv++;
				}
				else {
					allset.put(gid, gid);
					notconvertedids.add(gid);
				}
			}
		}

		if(nconv==0)
			throw new IOException("In ["+getClass().getSimpleName()+"]. None of the identifiers entered are valid for '"+fromidtype+"'. Please use the key type according to the gene identifiers");
		//throw new Exception("None of the original identifiers was converted to the chosen type of identifiers, please choose other 'fromidtype'");
		else {
			if(nconv==geneids.size())
				LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("All original identifiers were converted");
			else
				LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Only "+nconv+" from "+geneids.size()+" of the original identifiers were converted");

			//areconverted=true;
		}

		rsession.unset(ingenesid,outid);

	}

	public ArrayList<String> getIdentifiersNotconverted() {
		return notconvertedids;
	}

	public LinkedHashMap<String, String> getAllIdentifiers() {
		return allset;
	}

	public LinkedHashMap<String, String> getOnlyIdentifiersThatWereConverted() {
		return onlymatch;
	}
	
	
	
	

}
