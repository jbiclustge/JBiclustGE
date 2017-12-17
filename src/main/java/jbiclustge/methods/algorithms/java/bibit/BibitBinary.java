package jbiclustge.methods.algorithms.java.bibit;

public class BibitBinary {
	
	private int value;
	private int numOfOnes;
	
	public BibitBinary(int v)
	{
		value=v;
	}
	public BibitBinary(int v, int n)
	{
		value=v;
		numOfOnes=n;
	}
	public void setValue(int v)
	{
		value=v;
	}
	public void setNumOfOnes(int n)
	{
		numOfOnes=n;
	}
	public int getValue()
	{
		return value;
	}
	public int getNumOfOnes()
	{
		return numOfOnes;
	}
	public boolean equals(Object x)
	{
		boolean result=false;
		BibitBinary b=(BibitBinary)x;
		if(this.value==b.value)
			result=true;
		return result;
	}
	public String toString()
	{
		String result;
		result=value+" "+numOfOnes;
		return result;
	}

}