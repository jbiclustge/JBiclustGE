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
package jbiclustge.methods.algorithms.r.isapackage;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.javatuples.Pair;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import jbiclustge.datatools.expressiondata.dataset.ExpressionData;
import jbiclustge.datatools.expressiondata.processdata.binarization.BinarizationDecision;
import jbiclustge.methods.algorithms.AbstractBiclusteringAlgorithmCaller;
import jbiclustge.methods.algorithms.RunningParametersReporter;
import jbiclustge.methods.algorithms.r.RBiclustAlgorithmCaller;
import jbiclustge.results.biclusters.containers.BiclusterList;
import jbiclustge.results.biclusters.containers.BiclusterResult;
import jbiclustge.utils.props.AlgorithmProperties;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.propertyutils.PropertiesUtilities;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.rtools.rfunctions.MTURfunctions;

// TODO: Auto-generated Javadoc
/**
 * The Class RIsaMethod.
 */
public class RIsaMethod extends RBiclustAlgorithmCaller{
	
	/** The Constant ISA_ROWTHRESHOLDS. */
	public static final String ISA_ROWTHRESHOLDS="isa_row_threshold_parameters";
	
	/** The Constant ISA_COLUMNTHRESHOLDS. */
	public static final String ISA_COLUMNTHRESHOLDS="isa_column_threshold_parameters";
	
	/** The Constant ISA_NUMBERSEEDS. */
	public static final String ISA_NUMBERSEEDS="isa_number_of_seeds_to_use";
	
	/** The Constant ISA_ROWDIRECTION. */
	public static final String ISA_ROWDIRECTION="isa_row_cutoff_direction";
	
	/** The Constant ISA_COLUMNDIRECTION. */
	public static final String ISA_COLUMNDIRECTION="isa_column_cutoff_direction";
	
	
	/** The Constant NAME. */
	public static final String NAME="ISA";
	
	
	
	/** The rowthresholds. */
	private double[] rowthresholds;
	private double singlerowthreshold=-1.0;
	
	/** The columnthresholds. */
	private double[] columnthresholds;
	private double singlecolumnthreshold=-1.0;
	
	
	/** The numberseeds. */
	private int numberseeds=100;
	
	/** The direction. */
	private Pair<BinarizationDecision, BinarizationDecision> direction=new Pair<BinarizationDecision, BinarizationDecision>(BinarizationDecision.UPREGULATED, BinarizationDecision.UPREGULATED);
	
	
	
	
	
	/**
	 * Instantiates a new r isa method.
	 */
	public RIsaMethod() {
		super();
	}

	/**
	 * Instantiates a new r isa method.
	 *
	 * @param exprs the exprs
	 */
	public RIsaMethod(ExpressionData exprs) {
		super(exprs);

	}
	
	/**
	 * Instantiates a new r isa method.
	 *
	 * @param exprs the exprs
	 * @param props the props
	 */
	public RIsaMethod(ExpressionData exprs, Properties props) {
		super(exprs, props);
		
	}

	
	/**
	 * Instantiates a new r isa method.
	 *
	 * @param props the props
	 */
	public RIsaMethod(Properties props) {
		super(props);
	}
	
	/**
	 * Instantiates a new r isa method.
	 *
	 * @param propertiesfile the propertiesfile
	 */
	public RIsaMethod(String propertiesfile) {
		super(propertiesfile);
	}
	
	

	/**
	 * Instantiates a new r isa method.
	 *
	 * @param rowthresholds the rowthresholds
	 * @param columnthresholds the columnthresholds
	 * @param numberseeds the numberseeds
	 * @param direction the direction
	 */
	private RIsaMethod(double[] rowthresholds, double[] columnthresholds, int numberseeds,
			Pair<BinarizationDecision, BinarizationDecision> direction) {
		super();
		this.rowthresholds = rowthresholds;
		this.columnthresholds = columnthresholds;
		this.numberseeds = numberseeds;
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#requiredLibraries()
	 */
	@Override
	protected ArrayList<RPackageInfo> requiredLibraries() {
		ArrayList<RPackageInfo> list=new ArrayList<>();
		list.add(RPackageInfo.define("eisa", true));
		return list;
	}
	


	public void setRowSingleTreshold(double value) {
		if(value>0)
			this.singlerowthreshold=value;
	}
	
	/**
	 * Sets the row thresholds.
	 *
	 * @param rowthresholds the new row thresholds
	 */
	public void setRowThresholds(double[] rowthresholds) {
		this.rowthresholds = rowthresholds;
	}
	
	/**
	 * Sets the row thresholds.
	 *
	 * @param init the init
	 * @param end the end
	 * @param interval the interval
	 */
	public void setRowThresholds(double init, double end, double interval){
		try {
			initRsession();
			this.rowthresholds=MTURfunctions.getSequence(rsession,init, end, interval);
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the row thresholds.
	 *
	 * @param init the init
	 * @param end the end
	 * @param interval the interval
	 * @return the r isa method
	 */
	public RIsaMethod addRowThresholds(double init, double end, double interval){
		setRowThresholds(init, end, interval);
		return this;
	}
	
	
	public void setColumnSingleTreshold(double value) {
		if(value>0)
			this.singlecolumnthreshold=value;
	}
	
	
	/**
	 * Sets the column thresholds.
	 *
	 * @param columnthresholds the new column thresholds
	 */
	public void setColumnThresholds(double[] columnthresholds) {
		this.columnthresholds = columnthresholds;
	}
	
	/**
	 * Sets the column thresholds.
	 *
	 * @param init the init
	 * @param end the end
	 * @param interval the interval
	 */
	public void setColumnThresholds(double init, double end, double interval){
		try {
			initRsession();
			this.columnthresholds=MTURfunctions.getSequence(rsession,init, end, interval);
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the column thresholds.
	 *
	 * @param init the init
	 * @param end the end
	 * @param interval the interval
	 * @return the r isa method
	 */
	public RIsaMethod addColumnThresholds(double init, double end, double interval){
		setColumnThresholds(init, end, interval);
		return this;
	}
	

	/**
	 * Sets the number seeds.
	 *
	 * @param numberseeds the new number seeds
	 */
	public void setNumberSeeds(int numberseeds) {
		this.numberseeds = numberseeds;
	}
	
	/**
	 * Adds the number seeds.
	 *
	 * @param numberseeds the numberseeds
	 * @return the r isa method
	 */
	public RIsaMethod addNumberSeeds(int numberseeds) {
		this.numberseeds = numberseeds;
		return this;
	}

	/**
	 * Sets the direction.
	 *
	 * @param direction the direction
	 */
	public void setDirection(Pair<BinarizationDecision, BinarizationDecision> direction) {
		this.direction = direction;
	}
	
	/**
	 * Sets the row direction.
	 *
	 * @param direction the new row direction
	 */
	public void setRowDirection(BinarizationDecision direction){
		this.direction.setAt0(direction);
	}
	
	/**
	 * Adds the row direction.
	 *
	 * @param direction the direction
	 * @return the r isa method
	 */
	public RIsaMethod addRowDirection(BinarizationDecision direction){
		this.direction.setAt0(direction);
		return this;
	}
	
	/**
	 * Sets the column direction.
	 *
	 * @param direction the new column direction
	 */
	public void setColumnDirection(BinarizationDecision direction){
		this.direction.setAt1(direction);
	}
	
	/**
	 * Adds the column direction.
	 *
	 * @param direction the direction
	 * @return the r isa method
	 */
	public RIsaMethod addColumnDirection(BinarizationDecision direction){
		this.direction.setAt1(direction);
		return this;
	}
	

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#setAlgorithmProperties(java.util.Properties)
	 */
	@Override
	public void setAlgorithmProperties(Properties props) {
		
		this.numberseeds=PropertiesUtilities.getIntegerPropertyValueValidLowerLimit(props, ISA_NUMBERSEEDS, 100, 1, false, this.getClass());
		
		String[] dirallowed=new String[]{"down","up","updown"};
		String rowdir=PropertiesUtilities.getStringPropertyValue(props, ISA_ROWDIRECTION, "up", dirallowed, this.getClass());
		setRowDirection(getDirectionFromString(rowdir));
		String coldir=PropertiesUtilities.getStringPropertyValue(props, ISA_COLUMNDIRECTION, "up", dirallowed, this.getClass());
		setColumnDirection(getDirectionFromString(coldir));
		
		
		String rowthresh=PropertiesUtilities.getStringPropertyValue(props, ISA_ROWTHRESHOLDS, null, this.getClass());
		String colthresh=PropertiesUtilities.getStringPropertyValue(props, ISA_COLUMNTHRESHOLDS, null, this.getClass());
		try {
			double[] tmprowthresh=getThresholsFromString(rowthresh);
			double[] tmpcolthresh=getThresholsFromString(colthresh);
			if((tmprowthresh!=null && tmprowthresh.length==1) && (tmpcolthresh!=null && tmpcolthresh.length==1)) {
				singlerowthreshold=tmprowthresh[0];
				singlecolumnthreshold=tmpcolthresh[0];
			}
			else {
				this.rowthresholds=tmprowthresh;
			    this.columnthresholds=tmpcolthresh;
			}
		} catch (NumberFormatException | RserveException | REXPMismatchException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in loading properties of "+getAlgorithmName()+": ", e);
		}
	
	}
	
	/* (non-Javadoc)
	 * @see methods.IBiclusterAlgorithm#getAlgorithmAllowedProperties()
	 */
	@Override
	public AlgorithmProperties getAlgorithmAllowedProperties() throws IOException {
		
		String[] propkeys=new String[]{
				ISA_NUMBERSEEDS,
				ISA_ROWDIRECTION,
				ISA_COLUMNDIRECTION,
				ISA_ROWTHRESHOLDS,
				ISA_COLUMNTHRESHOLDS
		};
		String[] defaultvalues=new String[]{"100","up","up","",""};
		String[] comments=new String[] {
				"Integer scalar, the number of seeds to use",
				"It specifies whether we are interested in rows that are higher (up) than average, lower than average(down), or both (updown)",
				"It specifies whether we are interested in columns that are higher (up) than average, lower than average(down), or both (updown)",
				"Numeric vector, can be defined as following: (1) single number (e.g. 2 or 2.0) (2) numbers separated by semi-colon -> 1;1.5;3;4.5;6, (3) defining a range (1:6) with interval 3 as follows -> 1:6:3",
				"Numeric vector, can be defined as following: (1) single number (e.g. 2 or 2.0) (2) numbers separated by semi-colon -> 1;1.5;3;4.5;6, (3) defining a range (1:6) with interval 3 as follows -> 1:6:3"
		};
		
		return AlgorithmProperties.setupProperties(propkeys, defaultvalues, comments,"Source: isa2 manual, url: https://cran.r-project.org/web/packages/isa2/isa2.pdf");
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#runAlgorithm()
	 */
	@Override
	protected boolean runAlgorithm() throws Exception {



		try {
			//loadExpressionMatrixInREnvironment();
			loadLabeledExpressionMatrixInREnvironment();
			LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Starting ISA clustering method...please wait");

			//boolean usesingletreshold=false;

			if(singlerowthreshold!=-1 & singlecolumnthreshold!=-1) {
                 rsession.set(("rowthresholds"+getlabel()), singlerowthreshold);
                 rsession.set(("columnthresholds"+getlabel()), singlecolumnthreshold);
                 //usesingletreshold=true;
			}
			else {

				if(rowthresholds==null)
					rowthresholds=MTURfunctions.getSequence(rsession,1, 3, 0.5);
				rsession.set(("rowthresholds"+getlabel()), rowthresholds);

				if(columnthresholds==null)
					columnthresholds=MTURfunctions.getSequence(rsession,1, 3, 0.5);
				rsession.set(("columnthresholds"+getlabel()), columnthresholds);
			}

			rsession.set(("dirvector"+getlabel()), getDirection());

			 Instant start = Instant.now();
			
			/*if(usesingletreshold)
				rsession.silentlyEval(getResultOutputName()+" <- isa("+inputmatrixname+", thr.row=rowthresholds"+getlabel()+", thr.col=columnthresholds"+getlabel()+", no.seeds="
						+String.valueOf(numberseeds)+", direction=dirvector"+getlabel()+" )",true);
			else*/
				rsession.silentlyEval(getResultOutputName()+" <- isa("+inputmatrixname+", thr.row=rowthresholds"+getlabel()+", thr.col=columnthresholds"+getlabel()+", no.seeds="
					+String.valueOf(numberseeds)+", direction=dirvector"+getlabel()+" )",true);

			
			String convbicname="conv_"+getResultOutputName();
			rsession.silentlyEval(convbicname+" <- isa.biclust("+getResultOutputName()+")");


			saveElapsedTime(start);

			writeBiclusterResultsToFileWithOriginalAlgorithmMethod(convbicname);
			/* if(writeoriginalresultstofile!=null){
			    c.eval("writeBiclusterResults(\""+writeoriginalresultstofile+"\","+convbicname+",\"output_"+NAME+"\",rownames("+inputmatrixname+"),colnames("+inputmatrixname+"))");
	        }*/

		} catch (Exception e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error in execution of "+getAlgorithmName()+": ", e);
			return false;
		}


		return true;
	}
	
	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#removeRInputObjects()
	 */
	@Override
	protected String[] removeRInputObjects() {
		
		String[] baseobjects=getBaseROjects();
		
		String[] fabiaobjects=new String[]{("rowthresholds"+getlabel()),
				("columnthresholds"+getlabel()),
				("dirvector"+getlabel()),
				("conv_"+getResultOutputName())};
		return ArrayUtils.addAll(baseobjects, fabiaobjects);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#processResults()
	 */
	@Override
	protected void processResults() throws Exception {
		
	    //RConnection c =RConnector.getSession().connection;
		RConnection c =rsession.connection;
	    LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("Saving ISA results...please wait");
	    synchronized (c) {
	    	
	    	double[][] rowscores= c.eval("as.matrix("+getResultOutputName()+"[[1]])").asDoubleMatrix();
	        double[][] colscores= c.eval("as.matrix("+getResultOutputName()+"[[2]])").asDoubleMatrix();
	    
	    
	    	ArrayList<Integer>bicrowindex=null;
	        ArrayList<Double> bicrowscores=null;
	        ArrayList<Integer>biccolindex=null;
	        ArrayList<Double> biccolscores=null;
	    
	        listofbiclusters=new BiclusterList();
	    
	        for (int i = 0; i < rowscores[0].length; i++) {
	    	
	        	int clustnumber=i;
	        	bicrowindex=new ArrayList<>();
	    		biccolindex=new ArrayList<>();
	    		bicrowscores=new ArrayList<>();
	    		biccolscores=new ArrayList<>();
	    	 
	    		for (int j = 0; j < rowscores.length; j++) {
				
	    			double val=rowscores[j][clustnumber];
	    			if(val!=0.0){
	    				bicrowindex.add(j);
	    				bicrowscores.add(val);
	    			} 
	    		}
	    	 
	    		for (int h = 0; h < colscores.length; h++) {
	    		
	    			double val=colscores[h][clustnumber];
	    			if(val!=0.0){
	    				biccolindex.add(h);
	    				biccolscores.add(val);
	    			} 
				 }
	    	 
		    	BiclusterResult result=new BiclusterResult(expressionset, bicrowindex, biccolindex, true);
		    	result.appendAdditionalInfo("Gene_Scores", bicrowscores);
		    	result.appendAdditionalInfo("Condition_Scores", biccolscores);
		    	listofbiclusters.add(result);
	        }
	        
	       
		}
	    
	    LogMessageCenter.getLogger().toClass(getClass()).addInfoMessage("ISA results were successfully saved");
	}
	
	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	private String[] getDirection(){
		return new String[]{direction.getValue0().getISAFormat(), direction.getValue1().getISAFormat()};
	}
	
	/**
	 * Gets the direction from string.
	 *
	 * @param value the value
	 * @return the direction from string
	 */
	private BinarizationDecision getDirectionFromString(String value){
		
		for (BinarizationDecision dec : BinarizationDecision.values()) {
			if(dec.getISAFormat().equals(value.toLowerCase()))
				return dec;
		}
		return BinarizationDecision.UPREGULATED;
	}
	
	/**
	 * Gets the threshols from string.
	 *
	 * @param value the value
	 * @return the threshols from string
	 * @throws NumberFormatException the number format exception
	 * @throws RserveException the rserve exception
	 * @throws REXPMismatchException the REXP mismatch exception
	 */
	private double[] getThresholsFromString(String value) throws NumberFormatException, RserveException, REXPMismatchException{
		if(value!=null){
			if(value.contains(";")){
				String[] elems=value.split(";");
				double[] res =new double[elems.length];
				for (int i = 0; i < elems.length; i++) {
					try {
						res[i]=Double.parseDouble(elems[i]);
					} catch (Exception e) {
						LogMessageCenter.getLogger().addErrorMessage("Invalid input number: "+elems[i]+", using default values");
						return null;
					}
				}
				return res;
			}
			else if(value.contains(":")){
				String[] elems=value.split(":");
				if(elems.length!=3){
					LogMessageCenter.getLogger().addErrorMessage("Invalid number of input arguments: "+value+", using default values");
					return null;
				}
				else{
					initRsession();
					return MTURfunctions.getSequence(rsession,Double.parseDouble(elems[0]),
							                         Double.parseDouble(elems[1]), 
							                         Double.parseDouble(elems[2]));
				}
					
			}
			else {
				double[]res=null;
				try {
					double val=Double.parseDouble(value);
					res= new double[]{val};
				} catch (Exception e) {
					return null;
				}
				return res;
			}
			
		}
		return null;
	}

	 /**
 	 * New instance.
 	 *
 	 * @param data the data
 	 * @return the r isa method
 	 */
 	public static RIsaMethod newInstance(ExpressionData data){
			return new RIsaMethod(data);
		}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#getTemporaryWorkingDirectory()
	 */
	@Override
	protected String getTemporaryWorkingDirectory() {
		return null;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#getReportRunningParameters()
	 */
	@Override
	protected LinkedHashMap<String, String> getReportRunningParameters() {
		RunningParametersReporter parameters=new RunningParametersReporter();
		parameters.setKeyintValue(ISA_NUMBERSEEDS, numberseeds);
		parameters.setKeyStringValue(ISA_ROWDIRECTION, direction.getValue0().getISAFormat());
		parameters.setKeyStringValue(ISA_COLUMNDIRECTION, direction.getValue1().getISAFormat());
		if(singlerowthreshold!=-1 & singlecolumnthreshold!=-1) {
			parameters.setKeyStringValue(ISA_ROWTHRESHOLDS,String.valueOf(singlerowthreshold));
			parameters.setKeyStringValue(ISA_COLUMNTHRESHOLDS, String.valueOf(singlecolumnthreshold));
		}
		else {
			parameters.setKeyStringValue(ISA_ROWTHRESHOLDS,getThresholdstoString(rowthresholds));
			parameters.setKeyStringValue(ISA_COLUMNTHRESHOLDS, getThresholdstoString(columnthresholds));
		}
		return parameters;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#copyIntance()
	 */
	@Override
	public AbstractBiclusteringAlgorithmCaller copyIntance() {
		return new RIsaMethod(rowthresholds, columnthresholds, numberseeds, direction);
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#supportDefineNumberBiclustersToFind()
	 */
	@Override
	protected boolean supportDefineNumberBiclustersToFind() {
		return false;
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.AbstractBiclusteringAlgorithmCaller#changeNumberBiclusterToFind(int)
	 */
	@Override
	protected void changeNumberBiclusterToFind(int numberbics) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Gets the thresholdsto string.
	 *
	 * @param tresholds the tresholds
	 * @return the thresholdsto string
	 */
	private String getThresholdstoString(double[] tresholds){
		StringBuilder str=new StringBuilder();
		for (int i = 0; i < tresholds.length; i++) {
			str.append(tresholds[i]);
			if(i<tresholds.length-1)
				str.append(";");
		}
		return str.toString();
	}

	/* (non-Javadoc)
	 * @see methods.algorithms.r.RBiclustAlgorithmCaller#loadSources()
	 */
	@Override
	protected ArrayList<String> loadSources() {
		return null;
	}
	

}
