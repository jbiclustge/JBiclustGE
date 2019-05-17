/************************************************************************** 
 * Copyright 2012 - 2017, Orlando Rocha (ornrocha@gmail.com)
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
 */
package jbiclustge.methods.algorithms;


import java.util.ArrayList;

import jbiclustge.methods.IBiclusterAlgorithm;
import jbiclustge.methods.algorithms.java.bibit.BibitMethod;
import jbiclustge.methods.algorithms.java.bicat.opsm.OPSMMethod;
import jbiclustge.methods.algorithms.java.penalizedplaid.PenalizedPlaidMethod;
import jbiclustge.methods.algorithms.r.bicarepackage.RBicAREMethod;
import jbiclustge.methods.algorithms.r.biclic.RBiclicMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RBCCCMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RPlaidMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RQuestMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RQuestmetMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RQuestordMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RSpectralMethod;
import jbiclustge.methods.algorithms.r.biclustpackage.RXMOTIFSMethod;
import jbiclustge.methods.algorithms.r.fabiapackage.RFabiaMethod;
import jbiclustge.methods.algorithms.r.fabiapackage.RFabiaPMethod;
import jbiclustge.methods.algorithms.r.fabiapackage.RFabiaSMethod;
import jbiclustge.methods.algorithms.r.isapackage.RIsaMethod;
import jbiclustge.methods.algorithms.wrappers.BBCMethod;
import jbiclustge.methods.algorithms.wrappers.BiMinePlusMethod;
import jbiclustge.methods.algorithms.wrappers.BicFinderMethod;
import jbiclustge.methods.algorithms.wrappers.BimaxMethod;
import jbiclustge.methods.algorithms.wrappers.COALESCEMethod;
import jbiclustge.methods.algorithms.wrappers.CPBMethod;
import jbiclustge.methods.algorithms.wrappers.QuBicMethod;
import jbiclustge.methods.algorithms.wrappers.UBClustMethod;
import jbiclustge.methods.algorithms.wrappers.UnibicMethod;
import jbiclustge.methods.algorithms.wrappers.debi.DebiMethod;
import pt.ornrocha.systemutils.OSystemUtils;

// TODO: Auto-generated Javadoc
/**
 * The Enum BiclusteringMethod.
 */
public enum BiclusteringMethod {
	
	
/** The bimax. */
/*	BIMAXCLIB{
		@Override
		public String getName() {
			return "bimaxclib";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BiMax";
		}
		
		@Override
		public String getNameInMethodClass() {
			return BimaxMethodCLib.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new BimaxMethodCLib();
		}
	},*/
	BIMAX{
		@Override
		public String getAlgorithmID() {
			return "bimax";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BiMax";
		}
		
		@Override
		public String getNameInMethodClass() {
			return BimaxMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new BimaxMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The bibit. */
	BIBIT{
		@Override
		public String getAlgorithmID() {
			return "bibit";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BiBit";
		}
		
		@Override
		public String getNameInMethodClass() {
			return BibitMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new BibitMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The biclic. */
	BICLIC{
		@Override
		public String getAlgorithmID() {
			return "biclic";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BICLIC";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RBiclicMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RBiclicMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The opsm. */
	OPSM{
		@Override
		public String getAlgorithmID() {
			return "opsm";
		}
		
		@Override
		public String getAlgorithmName() {
			return "OPSM";
		}
		
		@Override
		public String getNameInMethodClass() {
			return OPSMMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new OPSMMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The cc. */
	CC{
		@Override
		public String getAlgorithmID() {
			return "chengchurch";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Cheng & Church";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RBCCCMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RBCCCMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The plaid. */
	PLAID{
		@Override
		public String getAlgorithmID() {
			return "plaid";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Plaid";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RPlaidMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RPlaidMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The penalizedplaid. */
	PENALIZEDPLAID{
		@Override
		public String getAlgorithmID() {
			return "penalizedplaid";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Penalized Plaid";
		}
		
		@Override
		public String getNameInMethodClass() {
			return PenalizedPlaidMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new PenalizedPlaidMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The quest. */
	QUEST{
		@Override
		public String getAlgorithmID() {
			return "quest";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Quest";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RQuestMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RQuestMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The questord. */
	QUESTORD{
		@Override
		public String getAlgorithmID() {
			return "questord";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Questord";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RQuestordMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RQuestordMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The questmet. */
	QUESTMET{
		@Override
		public String getAlgorithmID() {
			return "questmet";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Questmet";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RQuestmetMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RQuestmetMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The spectral. */
	SPECTRAL{
		@Override
		public String getAlgorithmID() {
			return "spectral";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Spectral";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RSpectralMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RSpectralMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The xmotifs. */
	XMOTIFS{
		@Override
		public String getAlgorithmID() {
			return "xmotifs";
		}
		
		@Override
		public String getAlgorithmName() {
			return "xMOTIFs";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RXMOTIFSMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RXMOTIFSMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The fabia. */
	FABIA{
		@Override
		public String getAlgorithmID() {
			return "fabia";
		}
		
		@Override
		public String getAlgorithmName() {
			return "FABIA";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RFabiaMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RFabiaMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The fabiap. */
	FABIAP{
		@Override
		public String getAlgorithmID() {
			return "fabiap";
		}
		
		@Override
		public String getAlgorithmName() {
			return "FABIAp";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RFabiaPMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RFabiaPMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The fabias. */
	FABIAS{
		@Override
		public String getAlgorithmID() {
			return "fabias";
		}
		
		@Override
		public String getAlgorithmName() {
			return "FABIAs";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RFabiaSMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RFabiaSMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The isa. */
	ISA{
		@Override
		public String getAlgorithmID() {
			return "isa";
		}
		
		@Override
		public String getAlgorithmName() {
			return "ISA";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RIsaMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RIsaMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The bicfinder. */
	BICFINDER{
		@Override
		public String getAlgorithmID() {
			return "bicfinder";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BicFinder";
		}
		
		@Override
		public String getNameInMethodClass() {
			return BicFinderMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new BicFinderMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The bimineplus. */
	BIMINEPLUS{
		@Override
		public String getAlgorithmID() {
			return "bimineplus";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BiMine+";
		}
		
		@Override
		public String getNameInMethodClass() {
			return BiMinePlusMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new BiMinePlusMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The bicare. */
	BICARE{
		@Override
		public String getAlgorithmID() {
			return "bicare";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Bicare (FLOC)";
		}
		
		@Override
		public String getNameInMethodClass() {
			return RBicAREMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new RBicAREMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The debi. */
	DEBI{
		@Override
		public String getAlgorithmID() {
			return "debi";
		}
		
		@Override
		public String getAlgorithmName() {
			return "DeBi";
		}
		
		@Override
		public String getNameInMethodClass() {
			return DebiMethod.NAME;
		}
		
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new DebiMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The coalesce. */
	COALESCE{
		@Override
		public String getAlgorithmID() {
			return "coalesce";
		}
		
		@Override
		public String getAlgorithmName() {
			return "COALESCE";
		}
		
		@Override
		public String getNameInMethodClass() {
			return COALESCEMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new COALESCEMethod();
		}
		
		@Override
		public boolean isSupported() {
			if(OSystemUtils.isWindows())
				return false;
			return true;
		}
	},
	
	/** The cpb. */
	CPB{
		@Override
		public String getAlgorithmID() {
			return "cpb";
		}
		
		@Override
		public String getAlgorithmName() {
			return "CPB";
		}
		
		@Override
		public String getNameInMethodClass() {
			return CPBMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new CPBMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The qubic. */
	QUBIC{
		@Override
		public String getAlgorithmID() {
			return "qubic";
		}
		
		@Override
		public String getAlgorithmName() {
			return "QuBic";
		}
		
		@Override
		public String getNameInMethodClass() {
			return QuBicMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new QuBicMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The unibic. */
	UNIBIC{
		@Override
		public String getAlgorithmID() {
			return "unibic";
		}
		
		@Override
		public String getAlgorithmName() {
			return "UniBic";
		}
		
		@Override
		public String getNameInMethodClass() {
			return UnibicMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new UnibicMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The bbc. */
	BBC{
		@Override
		public String getAlgorithmID() {
			return "bbc";
		}
		
		@Override
		public String getAlgorithmName() {
			return "BBC";
		}
		
		@Override
		public String getNameInMethodClass() {
			return BBCMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new BBCMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	/** The ubclust. */
	UBCLUST{
		@Override
		public String getAlgorithmID() {
			return "ubclust";
		}
		
		@Override
		public String getAlgorithmName() {
			return "UBCLUST";
		}
		
		@Override
		public String getNameInMethodClass() {
			return UBClustMethod.NAME;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return new UBClustMethod();
		}
		
		@Override
		public boolean isSupported() {
			return true;
		}
	},
	
	UNKNOWN{
		@Override
		public String getAlgorithmID() {
			return "unknown";
		}
		
		@Override
		public String getAlgorithmName() {
			return "Unknown";
		}
		
		@Override
		public String getNameInMethodClass() {
			return null;
		}
		
		@Override
		public AbstractBiclusteringAlgorithmCaller getInstance() {
			return null;
		}
		
		@Override
		public boolean isSupported() {
			return false;
		}
	};
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getAlgorithmID(){
		return getAlgorithmID();
	}
	
	/**
	 * Gets the algorithm name.
	 *
	 * @return the algorithm name
	 */
	public String getAlgorithmName(){
		return getAlgorithmName();
	}
	
	/**
	 * Gets the name in method class.
	 *
	 * @return the name in method class
	 */
	public String getNameInMethodClass(){
		return getNameInMethodClass();
	}
	
	/**
	 * Gets the single instance of BiclusteringMethod.
	 *
	 * @return single instance of BiclusteringMethod
	 */
	public AbstractBiclusteringAlgorithmCaller getInstance(){
		return getInstance();
	}
	
	/**
	 * Checks if is supported.
	 *
	 * @return true, if is supported
	 */
	public boolean isSupported(){
		return isSupported();
	}
	
	@Override
	public String toString() {
		return getAlgorithmName();
	}
	
	/**
	 * Gets the all methods.
	 *
	 * @return the all methods
	 */
	public static ArrayList<BiclusteringMethod> getAllMethods(){
		ArrayList<BiclusteringMethod> methods=new ArrayList<>();
		for (BiclusteringMethod method : BiclusteringMethod.values()) {
			methods.add(method);
		}
		return methods;
	}
	
	public static BiclusteringMethod getMethodbyName(String name) {
		if(name!=null)
			for (BiclusteringMethod method : BiclusteringMethod.values()) {
			     if(method.getAlgorithmName().toLowerCase().equals(name.toLowerCase()))
			    	 return method;
			     else if(method.getNameInMethodClass().toLowerCase().equals(name.toLowerCase()))
			    	 return method;
			     else if(method.getAlgorithmID().equals(name.toLowerCase()))
			    	 return method;
			}
		return BiclusteringMethod.UNKNOWN;
	}
	
	public static String getAlgorithmIDFromMethodInstance(IBiclusterAlgorithm methodinstance) {
		
		if(methodinstance!=null) {
			
			for (BiclusteringMethod method : BiclusteringMethod.values()) {
				if(methodinstance.getAlgorithmName().toLowerCase().equals(method.getNameInMethodClass().toLowerCase()))
					return method.getAlgorithmID();
			}
		}
		return null;
	}
	

}
