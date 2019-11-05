package Sender;

import java.io.PrintWriter;
import java.lang.reflect.*;

public class Driver {

	public static final int PORT = 1081;

	public static void main(String args[]) {

		Serializer s = new Serializer(PORT);
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

	private static void makeColl() {
		// TODO Auto-generated method stub

	}

	private static void makeRefArr() {
		// TODO Auto-generated method stub

	}

	private static void makeArr() {
		// TODO Auto-generated method stub

	}

	private static void makeRef() {
		// TODO Auto-generated method stub

	}

	private static void makeSimple() {
		// TODO Auto-generated method stub

	}

}