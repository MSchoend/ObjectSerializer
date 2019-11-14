package Sender;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import SampleObjs.*;
import org.jdom2.*;

public class Driver {

	public static final int PORT = 1081;
	private static Scanner input;
	private static Document xml;
	private static ArrayList<RefObj> references;

	public static void main(String args[]) {

		Serializer s = new Serializer(PORT);
		input = new Scanner(System.in);
		ArrayList<Object> allObjs = new ArrayList<Object>();
		references = new ArrayList<RefObj>();
		boolean willSend = runMenu(allObjs);
		if(willSend) {
			serialize(s, allObjs);
			s.send();
		}
		
	}
	

	public static boolean runMenu(ArrayList<Object> list) {
		int command = -1;
		boolean result = false;

		while (command != 7) {
			System.out.println("Please input a number corresponding to an object or command: ");
			System.out.println("1. Simple Object");
			System.out.println("2. Reference Object");
			System.out.println("3. Primitive Array Object");
			System.out.println("4. Reference Array Object");
			System.out.println("5. Collection Object");
			System.out.println("6. Serialize, Send and Exit");
			System.out.println("7. Exit without sending");
			
			command = input.nextInt();

			switch (command) {
			case 1:
				list.add(makeSimple());
				break;
			case 2:
				list.add(makeRef());
				break;
			case 3:
				list.add(makeArr());
				break;
			case 4:
				list.add(makeRefArr());
				break;
			case 5:
				list.add(makeColl());
				break;
			case 6:
				result = true;
				command = 7;
				break;
			case 7:
				result = false;
				break;
			default:
				System.out.println("Command not recognized, try again.");
			}
		}
		return result;
	}

	private static void serialize(Serializer sender, ArrayList<Object> list) {
		
		xml = sender.serialize(list);
			
	}

	private static CollectObj makeColl() {
		System.out.print("Please input collection length: ");
		int len = input.nextInt();
		ArrayList<SimpObj> list = new ArrayList<SimpObj>();
		for(int i = 0; i < len; i++) {
			System.out.println("--Object " + (i+1) + "--");
			list.add(makeSimple());
		}
		return new CollectObj(list);
	}

	private static RefArrayObj makeRefArr() {
		System.out.print("Please input the array length: ");
		int len = input.nextInt();
		SimpObj arr[] = new SimpObj[len];
		for(int i = 0; i < len; i++) {
			System.out.println("--Object " + (i+1) + "--");
			arr[i] = makeSimple();
		}
		return new RefArrayObj(arr);
	}

	private static ArrayObj makeArr() {
		System.out.print("Please input the array length: ");
		int len = input.nextInt();
		int arr[] = new int[len];
		for(int i = 0; i < len; i++) {
			System.out.print("Value " + (i+1) + ": ");
			arr[i] = input.nextInt();
		}
		return new ArrayObj(arr);
	}

	private static RefObj makeRef() {
		System.out.print("Please input an Integer: ");
		int a = input.nextInt();
		System.out.print("Make this object contain a reference? (else will be null) (y/n): ");
		char choice = input.next().charAt(0);
		RefObj b;
		switch(choice){
		case 'y':
			b = makeCircRef();
			break;
		case 'n':
			b = null;
			break;
		default:
			System.out.println("Token not recognized; setting to null");
			b = null;
			break;
		}
		return new RefObj(a, b);
	}
	
	private static RefObj makeCircRef(){
		RefObj toReturn;
		System.out.println("Make a new object (1) or choose from existing objects? (2)");
		switch(input.nextInt()){
		case 1:
			toReturn = makeRef();
			break;
		case 2:
			System.out.println("Choose an index (1-n):");
			System.out.println(Arrays.toString(references.toArray()));
			int idx = input.nextInt();
			toReturn = references.get(idx-1);
			break;
		default:
			System.out.println("Token not recognized; constructing new object");
			toReturn = makeRef();
			break;
		}
		return toReturn;
	}

	private static SimpObj makeSimple() {
		System.out.print("Please input an integer: ");
		int a = input.nextInt();
		System.out.print("Please input a double: ");
		double b = input.nextDouble();
		return new SimpObj(a, b);
	}

}