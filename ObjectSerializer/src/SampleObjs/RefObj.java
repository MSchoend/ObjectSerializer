package SampleObjs;

public class RefObj {

	private int anInt;
	private RefObj anObj;

	public RefObj(int anInt, RefObj anObj) {
		this.anInt = anInt;
		this.anObj = anObj;
	}
	
	public RefObj(int anInt){
		this.anInt = anInt;
		this.anObj = null;
	}
	
	public RefObj(){
		anObj = null;
	}

	public String toString() {
		if (anObj == null) {
			return "Int: " + anInt + " | RefObj: null";
		} else {
			return "Int: " + anInt + " | RefObj: " + System.identityHashCode(anObj);
		}
	}
	
	public void setReference(RefObj anObj){
		this.anObj = anObj;
	}

}
