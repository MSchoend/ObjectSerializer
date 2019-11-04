package SampleObjs;

import java.util.ArrayList;
import java.util.Arrays;

public class CollectObj {

	private ArrayList<SimpObj> aList = new ArrayList<SimpObj>();
	
	public CollectObj(SimpObj anArray[]){
		for(SimpObj o : anArray){
			aList.add(o);
		}
	}
	
	public String toString(){
		return "SimpObj ArrayList: " + Arrays.toString(aList.toArray());
	}
	
}
