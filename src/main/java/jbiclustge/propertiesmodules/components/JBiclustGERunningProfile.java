package jbiclustge.propertiesmodules.components;

import java.io.IOException;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;

public class JBiclustGERunningProfile {
	
	
	protected ExpressionData expressiondata=null;
	protected String expressiondatafilepath=null;

	
	
	
	
	
	
	
	
	
	
	public ExpressionData getExpressionData() throws Exception {
		if(expressiondata!=null)
			return expressiondata;
		else if(expressiondatafilepath!=null)
			return ExpressionData.loadDataset(expressiondatafilepath, null);
		else 
			throw new IOException("Missing expression datatset file in the JBiclustGERunningProfile");
	}

}
