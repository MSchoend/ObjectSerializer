package SampleObjs;

public class RefObj {

	private SimpObj anObj;
	
	public RefObj(SimpObj anObj){
		this.anObj = anObj;
	}
	
	public String toString(){
		return "SimpObj: {" + anObj.toString() + "}";
	}
	
}
