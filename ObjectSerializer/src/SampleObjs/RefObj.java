package SampleObjs;

public class RefObj {

	private int anInt;
	private RefObj anObj;
	private boolean isNull;

	public RefObj(int anInt, RefObj anObj) {
		this.anInt = anInt;
		this.anObj = anObj;
		isNull = this.anObj == null;
	}

	public String toString() {
		if (isNull) {
			return "Int: " + anInt + " | RefObj: null";
		} else {
			return "Int: " + anInt + " | RefObj: {\n" + anObj.toString() + "\n}";
		}
	}

}
