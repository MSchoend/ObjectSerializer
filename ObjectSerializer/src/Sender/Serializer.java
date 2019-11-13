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
	private Document objsToSend;
	private Element root;

	public Serializer(int port) {
		storedIDs = new ArrayList();
		this.port = port;
		objsToSend = new Document();
		root = new Element("serialized");
		objsToSend.addContent(root);
		/*
		 * try { System.out.print("Enter IP: "); Scanner in = new
		 * Scanner(System.in); String ip = in.nextLine(); clientSock = new
		 * Socket(ip, Driver.PORT); System.out.println("connected!");
		 * 
		 * PrintWriter out = new PrintWriter(clientSock.getOutputStream(),
		 * true); out.println("Hello, there!"); clientSock.close(); } catch
		 * (IOException e) {
		 * System.out.println("Error connecting to client! Aborting...");
		 * e.printStackTrace(); }
		 */
	}

	public Element convertToXML(Object o) {
		Element e = new Element("object");
		int id = System.identityHashCode(o);
		e.setAttribute("class", o.getClass().getName());
		e.setAttribute("id", Integer.toString(id));
		storedIDs.add(id);

		Class c = o.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			// managing for primitives, objects, arrays
			try {
				e.addContent(convertField(f, o));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		return e;
	}

	public void add(Element e) {
		objsToSend.addContent(e);
	}

	private Element convertField(Field f, Object o) throws IllegalArgumentException, IllegalAccessException {
		Element e = new Element("field");
		f.setAccessible(true);
		e.setAttribute("name", f.getName());
		Class<?> t = f.getType();
		if (t.isPrimitive()) {
			e.setAttribute("value", f.get(o).toString());
		} else if (t.getComponentType() != null) {
			Element array = new Element("object");
			Object[] arr = (Object[])(f.get(o));
			array.setAttribute("name", arr.getClass().getName());
			array.setAttribute("length", Integer.toString(arr.length));
		} else {
			Element reference = new Element("reference");
			Object value = f.get(o);
			reference.addContent(Integer.toString(System.identityHashCode(value)));
			e.addContent(reference);
			add(convertToXML(o));
		}

		return e;
	}

}
