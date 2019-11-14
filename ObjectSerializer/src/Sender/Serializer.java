package Sender;

import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
	
	public Document serialize(ArrayList<Object> list){
		for(Object o : list){
			add(convertToXML(o));
		}
		return objsToSend;
	}

	private Element convertToXML(Object o) {
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
		root.addContent(e);
	}

	public void send() {
		try {
			File loc = new File("output.txt");
			PrintWriter output = new PrintWriter(loc);
			XMLOutputter xmlWriter = new XMLOutputter(Format.getPrettyFormat());
			output.println(xmlWriter.outputString(objsToSend));
			output.close();
			System.err.println(loc.getAbsolutePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Element convertField(Field f, Object o) throws IllegalArgumentException, IllegalAccessException {
		Element e = new Element("field");
		f.setAccessible(true);
		e.setAttribute("name", f.getName());
		e.setAttribute("declaringclass", f.getDeclaringClass().getName());
		Class t = f.getType();
		Type cType = t.getComponentType();
		if (t.isPrimitive()) {
			Element value = new Element("value");
			value.addContent(f.get(o).toString());
			e.addContent(value);
		} else if (t.equals(ArrayList.class)){
			ArrayList list = (ArrayList)f.get(o);
			Object[] array = list.toArray();
			Element collect = new Element("object");
			collect.setAttribute("class", list.getClass().getName());
			collect.setAttribute("id", Integer.toString(System.identityHashCode(list)));
			collect.setAttribute("length", Integer.toString(array.length));
			for(Object v : array){
				Element reference = new Element("reference");
				reference.addContent(Integer.toString(System.identityHashCode(v)));
				add(convertToXML(v));
				collect.addContent(reference);
			}
			e.addContent(collect);
		}
			else if (cType != null) {
			Element array = new Element("object");
			Object arr = f.get(o);
			array.setAttribute("class", arr.getClass().getName());
			array.setAttribute("id", Integer.toString(System.identityHashCode(arr)));
			int length = Array.getLength(arr);
			array.setAttribute("length", Integer.toString(length));
			if (cType.getTypeName().equals("int")) {
				for (int i = 0; i < length; i++) {
					Element value = new Element("value");
					value.addContent(Integer.toString((int)Array.get(arr, i)));
					array.addContent(value);
				}
			} else {
				for(int i = 0; i < length; i++){
					Element reference = new Element("reference");
					Object value = Array.get(arr, i);
					reference.addContent(Integer.toString(System.identityHashCode(value)));
					add(convertToXML(value));
					array.addContent(reference);
				}
			}
			e.addContent(array);
		} else {
			Element reference = new Element("reference");
			Object value = f.get(o);
			if (value != null) {
				reference.addContent(Integer.toString(System.identityHashCode(value)));
				e.addContent(reference);
				add(convertToXML(value));
			}
		}

		return e;
	}

}
