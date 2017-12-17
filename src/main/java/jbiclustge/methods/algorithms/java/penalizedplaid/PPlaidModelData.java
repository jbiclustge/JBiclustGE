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
package jbiclustge.methods.algorithms.java.penalizedplaid;

// TODO: Auto-generated Javadoc
/**
 * The Enum PPlaidModelData.
 */
public enum PPlaidModelData {
	
	
	/** The gpe. */
	GPE{
		@Override
		public String toString() {
			return "GPE";
		}
	
		@Override
		public boolean uselambda() {
			return false;
		}
		
		@Override
		public String getDescription() {
			return "Penalized plaid model with the sampling of the penalty parameter,lambda. The model is fitted with a Gibbs sampling";
		}
	},
	
	/** The gpf. */
	GPF{
		
		@Override
		public String toString() {
			return "GPF";
		}
		
		@Override
		public boolean uselambda() {
			return true;
		}
		
		@Override
		public double defaultlambda() {
			return 0.0;
		}
		
		@Override
		public String getDescription() {
			return "Penalized model with a fix value of the penalty parameter lambda and fitted with the  Gibbs sampling procedure";
		}
		
	},
	
	/** The mpe. */
	MPE{
		
		@Override
		public String toString() {
			return "MPE";
		}
		
		@Override
		public boolean uselambda() {
			return false;
		}
		
		@Override
		public String getDescription() {
			return "Penalized model fitted with a Metropolis-Hastings procedure";
		}
		
	},
	
	/** The mpf. */
	MPF{
		
		@Override
		public String toString() {
			return "MPF";
		}
		
		@Override
		public boolean uselambda() {
			return true;
		}
		
		@Override
		public double defaultlambda() {
			return 0.0;
		}
		
		@Override
		public String getDescription() {
			return "Penalized model with a fix value of lambda and fitted with a Metropolis-Hastings procedure";
		}
	};

	
	
	
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription(){
		return getDescription();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		return toString();
	}
	
	/**
	 * Uselambda.
	 *
	 * @return true, if successful
	 */
	public boolean uselambda(){
		return uselambda();
	}
	
	/**
	 * Defaultlambda.
	 *
	 * @return the double
	 */
	public double defaultlambda(){
		return defaultlambda();
	}
	
	
	/**
	 * Gets the penalized plaid model from string.
	 *
	 * @param model the model
	 * @return the penalized plaid model from string
	 */
	public static PPlaidModelData getPenalizedPlaidModelFromString(String model){
		if(model!=null){
			for (PPlaidModelData modeltype : PPlaidModelData.values()) {
				if(modeltype.toString().toLowerCase().equals(model.toLowerCase()))
					return modeltype;
			}
		}
		return PPlaidModelData.GPE;
	}
	
}
