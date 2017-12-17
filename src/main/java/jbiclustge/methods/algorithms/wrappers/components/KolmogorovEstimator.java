package jbiclustge.methods.algorithms.wrappers.components;

public enum KolmogorovEstimator {
	
	UNIFORMMODEL{
		
		@Override
		public String getName() {
			return "Uniform Model";
		}
		
		@Override
		public String getCodeTag() {
			return "0";
		}
		
	},
	CONSTANTROWSMODEL{
		
		@Override
		public String getName() {
			return "Constant Rows Model";
		}
		
		@Override
		public String getCodeTag() {
			return "1";
		}
		
	},
	ADDITIVEMODEL{
		
		@Override
		public String getName() {
			return "Additive Model";
		}
		
		@Override
		public String getCodeTag() {
			return "2";
		}
		
	},
	RELAXEDOPSM{
		
		@Override
		public String getName() {
			return "Relaxed OPSM";
		}
		
		@Override
		public String getCodeTag() {
			return "3";
		}
		
	};
	
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	public String getName() {
		return getName();
	}
	
	public String getCodeTag() {
		return getCodeTag();
	}
	

}
