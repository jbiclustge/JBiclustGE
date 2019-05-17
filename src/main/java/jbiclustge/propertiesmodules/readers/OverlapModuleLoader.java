package jbiclustge.propertiesmodules.readers;

import java.util.HashMap;
import java.util.Properties;

import jbiclustge.propertiesmodules.PropertyLabels;
import jbiclustge.propertiesmodules.PropertyModuleLoader;
import pt.ornrocha.propertyutils.PropertiesUtilities;

public class OverlapModuleLoader extends PropertyModuleLoader{
	
	private double overlapvalue=-1.0;
	private int numberbics=-1;
	private boolean performoverlapanalysis=false;

	public OverlapModuleLoader(Properties props) throws Exception {
		super(props);
		loadProperties();
	}

	@Override
	public void loadProperties() throws Exception {
		
		if(PropertiesUtilities.isValidProperty(props, PropertyLabels.OVERLAPFILTERING)) {
			overlapvalue=PropertiesUtilities.getDoublePropertyValueValidLimits(props, PropertyLabels.OVERLAPFILTERING, -1.0, 0.0, true, 1.0, true,getClass());
		    if(overlapvalue>=0.0 && overlapvalue<=1.0)
		    	performoverlapanalysis=true;
		    
		    if(PropertiesUtilities.isValidProperty(props, PropertyLabels.FILTEROVERLAPNUMBERBICS))
		    	numberbics=PropertiesUtilities.getIntegerPropertyValue(props, PropertyLabels.FILTEROVERLAPNUMBERBICS, -1, getClass());
		}
		
	}

	public double getOverlapValue() {
		return overlapvalue;
	}

	public boolean isOverlapAnalysis() {
		return performoverlapanalysis;
	}
	
	public static OverlapModuleLoader load(Properties props) throws Exception {
		return new OverlapModuleLoader(props);
	}

	@Override
	public HashMap<String, Object> getMapOfProperties() {
		HashMap<String, Object> res=new HashMap<>();
		res.put(PropertyLabels.OVERLAPFILTERING, overlapvalue);
		if(numberbics>0) {
			res.put(PropertyLabels.FILTEROVERLAPNUMBERBICS, numberbics);
		}
		return res;
	}

}
