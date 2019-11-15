package SampleObjs;

public class SimpObj {

	public int aNum;
	private double aDouble;
	
	public SimpObj(int a, double b){
		aNum = a;
		aDouble = b;
	}
	
	public SimpObj(){
		
	}
	
	public void setInt(int i){
		aNum = i;
	}
	
	public void setDouble(double d){
		aDouble = d;
	}
	
	public String toString(){
		return "Int: " + aNum + " | Double: " + aDouble;
	}
}
