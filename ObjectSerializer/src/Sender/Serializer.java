package Sender;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import org.jdom2.*;
import java.lang.reflect.*;

public class Serializer {

	private int port;
	private Socket clientSock;
	private ArrayList<Integer> storedIDs;
	
	public Serializer(int port) {
		storedIDs = new ArrayList();
		this.port = port;
		/*
		 * try { System.out.print("Enter IP: "); Scanner in = new Scanner(System.in);
		 * String ip = in.nextLine(); clientSock = new Socket(ip, Driver.PORT);
		 * System.out.println("connected!");
		 * 
		 * PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);
		 * out.println("Hello, there!"); clientSock.close(); } catch (IOException e) {
		 * System.out.println("Error connecting to client! Aborting...");
		 * e.printStackTrace(); }
		 */
	}
	
	public void convertToXML(Object o) {
		Element e = new Element("object");
		int id = System.identityHashCode(o);
		e.setAttribute("class", o.getClass().getName());
		e.setAttribute("id", Integer.toString(id));
		storedIDs.add(id);
		
		Class c = o.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			//managing for primitives, objects, arrays
			
		}
	}
	
	private Element convertObject(Object o) {
		Element e = new Element("object");
		e.setAttribute("class", o.getClass().getName());
		
		return e;
	}
	
	
}
