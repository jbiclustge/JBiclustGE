package jbiclustge.methods.algorithms.java.bicat.bimax;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.transformdata.binarization.IDiscretizationMethod;
import jbiclustge.datatools.expressiondata.transformdata.binarization.methods.BiMaxBinarizationMethod;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.utils.properties.AlgorithmProperties;

public class JBimaxMethod extends AbstractBiclusteringAlgorithmCaller{

	
	
	private int mingenes=40;
	private int minconditions=40;
	private IDiscretizationMethod binarizationmethod=BiMaxBinarizationMethod.newInstance();
	
	public static final String NAME="JBimax";

	public JBimaxMethod(){
		super();
	}
	
	public JBimaxMethod(ExpressionData exprs){
		super(exprs);
	}
	
	
	public JBimaxMethod(ExpressionData exprs, Properties props){
		super(exprs,props);
	}
	
	public JBimaxMethod(Properties props){
		super(props);
	}
	
	public JBimaxMethod(String propertiesfile){
		super(propertiesfile);
	}
	
	
	
	
	
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAlgorithmProperties(Properties props) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean runAlgorithm() throws Exception {
		
		int [][] inputdata=expressionset.getBinarizedMatrix(binarizationmethod);
		listofbiclusters=BiMaxBicAT.run(inputdata, inputdata.length, inputdata[0].length,mingenes,minconditions, expressionset);
		
		
		return true;
	}

	@Override
	protected void processResults() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getRunningTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTemporaryWorkingDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean supportedinoperatingsystem() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void stopProcess() {
		// TODO Auto-generated method stub
		
	}

}
