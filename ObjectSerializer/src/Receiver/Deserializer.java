package Receiver;

import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import SampleObjs.RefObj;
import SampleObjs.SimpObj;
import Sender.Driver;

public class Deserializer {

	private static HashMap<Integer, Object> allObjs;
	private static PrintWriter outputter;


	public static void main(String args[]) {

		allObjs = new HashMap<Integer, Object>();

		try {
			ServerSocket sock = new ServerSocket(Driver.PORT);
			System.out.println("Waiting for connection");
			Socket clientSock = sock.accept();

			SAXBuilder builder = new SAXBuilder();
			Document xml = builder.build(clientSock.getInputStream());
			/*
			 * PrintWriter outputter = new PrintWriter(new File("output.txt"));
			 * XMLOutputter xmler = new XMLOutputter(Format.getPrettyFormat());
			 * outputter.print(xmler.outputString(xml)); outputter.close();
			 */

			deserialize(xml);
			
			outputter = new PrintWriter("inspected.txt");
			Set<Integer> keys = allObjs.keySet();
			for (Integer k : keys) {
				inspect(allObjs.get(k));
			}
			outputter.close();
			clientSock.close();
			sock.close();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | JDOMException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void deserialize(Document xml) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		Element root = xml.getRootElement();
		List<Element> elems = root.getChildren();
		for (Element e : elems) { // create all objects
			Integer id = Integer.parseInt(e.getAttributeValue("id"));
			if (allObjs.get(id) == null) {
				System.err.println("Adding " + id);
				allObjs.put(id, objectFromElement(e));
			}
		}
		for (Element e : elems) { // fill all fields
			assignFields(e);
		}
	}

	private static Object objectFromElement(Element e)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
		Class eClass = Class.forName(e.getAttributeValue("class"));
		return eClass.newInstance();
	}

	private static void assignFields(Element e) throws ClassNotFoundException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		System.err.println("Assigning fields for " + e.getAttributeValue("id"));
		System.err.flush();
		Object eObj = allObjs.get(Integer.parseInt(e.getAttributeValue("id")));
		Class eClass = eObj.getClass();
		List<Element> eFields = e.getChildren();

		if (!eClass.isArray()) {
			for (Element f : eFields) {
				Class declaring = Class.forName(f.getAttributeValue("declaringclass"));
				Field field = declaring.getDeclaredField(f.getAttributeValue("name"));
				field.setAccessible(true);
				field.set(eObj, getField(field.getType() , f));
			}
		} else {

		}
	}

	private static Object getField(Class fType, Element field) {
		if (fType.isPrimitive()) {
			String value = field.getValue();
			if (fType.equals(int.class)) {
				return Integer.parseInt(value);
			} else if (fType.equals(double.class)) {
				return Double.parseDouble(value);
			}
		} else {
			int refID = Integer.parseInt(field.getValue());
			return allObjs.get(refID);

		}
		return null;
	}

	private static void inspect(Object o) {
		try {
			Class clazz = o.getClass();
			outputter.append("Class: " + clazz.getName() + "\n");
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				outputter.append(" Field: " + f.getName() + " - " + f.get(o) + "\n");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
