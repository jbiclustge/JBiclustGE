package jbiclustge.methods.algorithms.wrappers.components;

public class KillerProcess implements Runnable{

	
	private boolean killstate=false;
	
	
	@Override
	public void run() {
		killstate=true;
		
	}
	
	
	public boolean isToKill() {
		return killstate;
	}

}
