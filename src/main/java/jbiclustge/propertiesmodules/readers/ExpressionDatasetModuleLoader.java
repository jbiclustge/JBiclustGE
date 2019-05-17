/************************************************************************** 
 * Copyright 2015 - 2017
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 * Created by Orlando Rocha (ornrocha@gmail.com) inside BIOSYSTEMS Group (https://www.ceb.uminho.pt/BIOSYSTEMS)
 */
package jbiclustge.propertiesmodules.readers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.smiletools.ZeroValueImputation;
import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import pt.ornrocha.systemutils.OSystemUtils;
import smile.imputation.AverageImputation;
import smile.imputation.KMeansImputation;
import smile.imputation.KNNImputation;
import smile.imputation.LLSImputation;
import smile.imputation.MissingValueImputation;
import smile.imputation.SVDImputation;

// TODO: Auto-generated Javadoc
/**
 * The Class ExpressionDatasetModuleLoader.
 */
public class ExpressionDatasetModuleLoader extends PropertyModuleLoader{

	/** The dataset. */
	private ExpressionData dataset;
	
	/** The withimputationmissvalues. */
	private boolean withimputationmissvalues=false;
	
	/**
	 * Instantiates a new expression dataset module loader.
	 *
	 * @param props the props
	 * @throws Exception the exception
	 */
	public ExpressionDatasetModuleLoader(Properties props) throws Exception {
		super(props);
		loadProperties();
	}
	

	/**
	 * Gets the expression dataset.
	 *
	 * @return the expression dataset
	 */
	public ExpressionData getExpressionDataset() {
		return dataset;
	}
	
	
	
	
	/**
	 * Checks if is missing values imputation.
	 *
	 * @return true, if is missing values imputation
	 */
	public boolean isMissingValuesImputation() {
		return withimputationmissvalues;
	}

	/* (non-Javadoc)
	 * @see propertiesmodules.PropertyModuleLoader#loadProperties()
	 */
	@Override
	public void loadProperties() throws Exception {
		
		if(!props.containsKey(PropertyLabels.INPUTDATASETFILEPATH))
			throw new IOException("Missing expression dataset file");
		else{
			
			MissingValueImputation missvalmethod=null;
			
			if(props.getProperty(PropertyLabels.INPUTDATASETFILEPATH).isEmpty())
				throw new IOException("Missing expression dataset file");
			else{
				
				String datasetfilepath=props.getProperty(PropertyLabels.INPUTDATASETFILEPATH);

				datasetfilepath=OSystemUtils.validatePath(datasetfilepath);
				
				System.out.println(datasetfilepath);
				
				if(props.containsKey(PropertyLabels.MISSINGDATAIMPUTATIONMETHOD)){
					
					String method=props.getProperty(PropertyLabels.MISSINGDATAIMPUTATIONMETHOD);
					
					if(method!=null && !method.isEmpty()){
						
						withimputationmissvalues=true;
						if(method.toLowerCase().equals("averageimputation"))
							missvalmethod=new AverageImputation();
						else if(method.toLowerCase().equals("kmeansimputation")){
							if(props.containsKey(PropertyLabels.KMeansImputation)){
								String kmeanparam=props.getProperty(PropertyLabels.KMeansImputation);
								if(kmeanparam!=null && !kmeanparam.isEmpty()){
									String[] sp=kmeanparam.split(";");
									if(sp.length==2){
										try {
											int k=Integer.parseInt(sp[0]);
											int runs=Integer.parseInt(sp[1]);
											missvalmethod=new KMeansImputation(k,runs);
										} catch (Exception e) {
											missvalmethod=new KMeansImputation(4,5);
										}
									}
									else
										missvalmethod=new KMeansImputation(4,5);
								}
								else
									missvalmethod=new KMeansImputation(4,5);
							}
							else
								missvalmethod=new KMeansImputation(4,5);
						}
						else if(method.toLowerCase().equals("knnimputation")){
							if(props.containsKey(PropertyLabels.KNNImputation)){
								String knnparam=props.getProperty(PropertyLabels.KNNImputation);
								if(knnparam!=null && !knnparam.isEmpty()){
									try {
										int k=Integer.parseInt(knnparam);
										missvalmethod=new KNNImputation(k);
									} catch (Exception e) {
										missvalmethod=new KNNImputation(5);
									}
								}
								else
									missvalmethod=new KNNImputation(5);
							}
							else
								missvalmethod=new KNNImputation(5);
						}
						else if(method.toLowerCase().equals("llsimputation")){
							if(props.containsKey(PropertyLabels.LLSImputation)){
								String llsparam=props.getProperty(PropertyLabels.LLSImputation);
								if(llsparam!=null && !llsparam.isEmpty()){
									try {
										int k=Integer.parseInt(llsparam);
										missvalmethod=new LLSImputation(k);
									} catch (Exception e) {
										missvalmethod=new LLSImputation(5);
									}
								}
								else
									missvalmethod=new LLSImputation(5);
							}
							else
								missvalmethod=new LLSImputation(5);
						}
						else if(method.toLowerCase().equals("svdimputation")){
							if(props.containsKey(PropertyLabels.SVDImputation)){
								String svdparam=props.getProperty(PropertyLabels.SVDImputation);
								if(svdparam!=null && !svdparam.isEmpty()){
									try {
										int k=Integer.parseInt(svdparam);
										missvalmethod=new SVDImputation(k);
									} catch (Exception e) {
										missvalmethod=new SVDImputation(5);
									}
								}
								else
									missvalmethod=new SVDImputation(5);
							}
							else
								missvalmethod=new SVDImputation(5);
						}
						else if(method.toLowerCase().equals("zerovalueimputation")){
							missvalmethod=new ZeroValueImputation();
						}
						
						
					}
					
					
					
				}
				
			   dataset=ExpressionData.loadDataset(datasetfilepath, missvalmethod);

			}

		}

	}


	/**
	 * Load.
	 *
	 * @param props the props
	 * @return the expression dataset module loader
	 * @throws Exception the exception
	 */
	public static ExpressionDatasetModuleLoader load(Properties props) throws Exception{
		return new ExpressionDatasetModuleLoader(props);
	}


	@Override
	public HashMap<String, Object> getMapOfProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
