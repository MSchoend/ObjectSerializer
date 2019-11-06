package Sender;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Scanner;
import SampleObjs.*;

public class Driver {

	public static final int PORT = 1081;
	private static Scanner input;

	public static void main(String args[]) {

		Serializer s = new Serializer(PORT);
		input = new Scanner(System.in);
		//runMenu();
		
	}
	

	public static void runMenu() {
		int input = -1;

		while (input != 7) {
			System.out.println("Please input a number corresponding to an object or command: ");
			System.out.println("1. Simple Object");
			System.out.println("2. Reference Object");
			System.out.println("3. Primitive Array Object");
			System.out.println("4. Reference Array Object");
			System.out.println("5. Collection Object");
			System.out.println("6. Serialize and Send");
			System.out.println("7. Exit");

			switch (input) {
			case 1:
				makeSimple();
				break;
			case 2:
				makeRef();
				break;
			case 3:
				makeArr();
				break;
			case 4:
				makeRefArr();
				break;
			case 5:
				makeColl();
				break;
			case 6:
				serialize();
				break;
			case 7:
				break;
			default:
				System.out.println("Command not recognized, try again:");
			}
		}
	}

	private static void serialize() {
		// TODO Auto-generated method stub

	}

	private static CollectObj makeColl() {
		System.out.print("Please input collection length");
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
		char choice = (char)input.nextByte();
		RefObj b;
		switch(choice){
		case 'y':
			b = makeRef();
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

	private static SimpObj makeSimple() {
		System.out.print("Please input an integer: ");
		int a = input.nextInt();
		System.out.print("Please input a double: ");
		double b = input.nextDouble();
		return new SimpObj(a, b);
	}

}