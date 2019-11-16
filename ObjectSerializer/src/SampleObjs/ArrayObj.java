package SampleObjs;

import java.util.Arrays;

public class ArrayObj {

	private int[] anArray;
	
	public ArrayObj(int[] anArray){
		this.anArray = anArray;
	}
	
	public ArrayObj(){
		
	}
	
	public String toString(){
		return "Int Array: " + Arrays.toString(anArray);
	}
	
}
