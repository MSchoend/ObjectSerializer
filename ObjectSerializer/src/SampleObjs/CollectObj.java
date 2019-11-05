package SampleObjs;

import java.util.ArrayList;
import java.util.Arrays;

public class CollectObj {

	private ArrayList<SimpObj> aList = new ArrayList<SimpObj>();
	
	public CollectObj(ArrayList<SimpObj> aList){
		this.aList = aList;
	}
	
	public String toString(){
		return "SimpObj ArrayList: " + Arrays.toString(aList.toArray());
	}
	
}
