package SampleObjs;

public class RefObj {

	private SimpObj anObj;
	
	public RefObj(int a, double b){
		anObj = new SimpObj(a, b);
	}
	
	public String toString(){
		return "SimpObj: {" + anObj.toString() + "}";
	}
	
}
