package SampleObjs;

public class SimpObj {

	public int aNum;
	private double aDouble;
	
	public SimpObj(int a, double b){
		aNum = a;
		aDouble = b;
	}
	
	public double getDouble(){
		return aDouble;
	}
	
	public String toString(){
		return "Int: " + aNum + " | Double: " + aDouble;
	}
}
