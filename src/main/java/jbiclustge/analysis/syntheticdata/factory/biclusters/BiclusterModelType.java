package jbiclustge.analysis.syntheticdata.factory.biclusters;

public enum BiclusterModelType {
	
	
	CONSTANT{
		
		@Override
		public String getName() {
			return "constant";
		}
	},
	CONSTANTUP{
		
		@Override
		public String getName() {
			return "constant-up";
		}
	},
	CONSTANTADDROWADJ{
		@Override
		public String getName() {
			return "constant-additive-row-adjustment";
		}
	},
	CONSTANTMULTROWADJ{
		@Override
		public String getName() {
			return "constant-multiplicative-row-adjustment";
		}
	},
	CONSTANTADDCOLUMNADJ{
		@Override
		public String getName() {
			return "constant-additive-column-adjustment";
		}
	},
	CONSTANTMULTCOLUMNADJ{
		@Override
		public String getName() {
			return "constant-multiplicative-column-adjustment";
		}
	},
	SCALE{
		@Override
		public String getName() {
			return "scale";
		}
	},
	SHIFT{
		@Override
		public String getName() {
			return "shift";
		}
	},
	SHIFTSCALE{
		@Override
		public String getName() {
			return "shift-scale";
		}
	},
	PLAID{
		@Override
		public String getName() {
			return "plaid";
		}
	};
	

	public String getName() {
		return getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
