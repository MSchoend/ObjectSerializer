package Sender;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import SampleObjs.*;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;

import java.net.*;

public class Driver {

	public static final int PORT = 1081;
	private static Scanner input;
	private static Document xml;
	private static ArrayList<RefObj> references;
	private static ArrayList<Object> allObjs;

	public static void main(String args[]) {

		Serializer s = new Serializer();
		input = new Scanner(System.in);
		allObjs = new ArrayList<Object>();
		references = new ArrayList<RefObj>();
		boolean willSend = runMenu();
		System.err.println(allObjs.toString());
		if (willSend) {
			serialize(s);
			s.send();
			//send();
		}

	}

	private static void send() {

		try {
			System.out.print("Enter IP: ");
			Scanner in = new Scanner(System.in);
			String ip = in.nextLine();
			Socket clientSock = new Socket(ip, Driver.PORT);
			System.out.println("connected!");

			XMLOutputter outputter = new XMLOutputter();
			PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
			writer.print(outputter.outputString(xml));
			in.close();
			writer.close();
			clientSock.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean runMenu() {
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
				allObjs.add(makeSimple());
				break;
			case 2:
				allObjs.add(makeRef());
				break;
			case 3:
				allObjs.add(makeArr());
				break;
			case 4:
				allObjs.add(makeRefArr());
				break;
			case 5:
				allObjs.add(makeColl());
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

	private static void serialize(Serializer sender) {

		xml = sender.serialize(allObjs);

	}

	private static CollectObj makeColl() {
		System.out.print("Please input collection length: ");
		int len = input.nextInt();
		ArrayList<SimpObj> list = new ArrayList<SimpObj>();
		for (int i = 0; i < len; i++) {
			System.out.println("--Object " + (i + 1) + "--");
			list.add(makeSimple());
			allObjs.add(list.get(i));
		}
		return new CollectObj(list);
	}

	private static RefArrayObj makeRefArr() {
		System.out.print("Please input the array length: ");
		int len = input.nextInt();
		SimpObj arr[] = new SimpObj[len];
		for (int i = 0; i < len; i++) {
			System.out.println("--Object " + (i + 1) + "--");
			arr[i] = makeSimple();
			allObjs.add(arr[i]);
		}
		return new RefArrayObj(arr);
	}

	private static ArrayObj makeArr() {
		System.out.print("Please input the array length: ");
		int len = input.nextInt();
		int arr[] = new int[len];
		for (int i = 0; i < len; i++) {
			System.out.print("Value " + (i + 1) + ": ");
			arr[i] = input.nextInt();
		}
		return new ArrayObj(arr);
	}

	private static RefObj makeRef() {
		System.out.print("Please input an Integer: ");
		int a = input.nextInt();
		RefObj toReturn = new RefObj(a);
		references.add(toReturn);
		System.out.print("Make this object contain a reference (1) or null (2): ");
		int choice = input.nextInt();
		RefObj b;
		switch (choice) {
		case 1:
			b = makeCircRef();
			break;
		case 2:
			b = null;
			break;
		default:
			System.out.println("Token not recognized; setting to null");
			b = null;
			break;
		}
		toReturn.setReference(b);
		return toReturn;
	}

	private static RefObj makeCircRef() {
		RefObj toReturn;
		System.out.println("Make a new object (1) or choose from existing objects? (2)");
		switch (input.nextInt()) {
		case 1:
			toReturn = makeRef();
			allObjs.add(toReturn);
			break;
		case 2:
			System.out.println("Choose an index (1-n):");
			System.out.println(Arrays.toString(references.toArray()));
			int idx = input.nextInt();
			toReturn = references.get(idx - 1);
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