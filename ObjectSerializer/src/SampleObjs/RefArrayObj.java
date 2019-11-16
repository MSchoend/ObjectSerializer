package SampleObjs;

import java.util.Arrays;

public class RefArrayObj {

	private SimpObj anArray[];
	
	public RefArrayObj(SimpObj[] anArray){
		this.anArray = anArray;
	}
	
	public RefArrayObj(){
		
	}
	
	public String toString(){
		return "SimpObj Array: " + Arrays.toString(anArray);
	}
	
}
