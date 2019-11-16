package Receiver;

import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static ArrayList<Object> inspectedObjs;

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
			inspectedObjs = new ArrayList<Object>();
			Set<Integer> keys = allObjs.keySet();
			for (Integer k : keys) {
				inspect(allObjs.get(k), 0);
				inspectedObjs.clear();
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
		if(!eClass.isArray()){
			return eClass.newInstance();
		} else {
			return Array.newInstance(eClass.getComponentType(), Integer.parseInt(e.getAttributeValue("length")));
		}
	}

	private static void assignFields(Element e) throws ClassNotFoundException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Object eObj = allObjs.get(Integer.parseInt(e.getAttributeValue("id")));
		Class eClass = eObj.getClass();
		List<Element> eFields = e.getChildren();

		if (!eClass.isArray()) {
			for (Element f : eFields) {
				Class declaring = Class.forName(f.getAttributeValue("declaringclass"));
				Field field = declaring.getDeclaredField(f.getAttributeValue("name"));
				field.setAccessible(true);

				field.set(eObj, getFieldValue(field.getType(), f));
			}
		} else {
			Class arrType = eClass.getComponentType();
			List<Element> elements = e.getChildren(arrType.isPrimitive() ? "value" : "reference");
			for(int i = 0; i < Integer.parseInt(e.getAttributeValue("length")); i++){
				Array.set(eObj, i, getFieldValue(arrType, elements.get(i)));
			}
		}

		// for (Element f : eFields) {
		// Class declaring =
		// Class.forName(f.getAttributeValue("declaringclass"));
		// Field field =
		// declaring.getDeclaredField(f.getAttributeValue("name"));
		// field.setAccessible(true);
		// if (!field.getType().isArray()) {
		// field.set(eObj, getFieldValue(field.getType(), f));
		// } else {
		// Class arrType = field.getType();
		// Element arrayEl = f.getChild("object");
		// int length = Integer.parseInt(arrayEl.getAttributeValue("length"));
		// Object eArray = field.get(eObj);
		// eArray = arrType.newInstance();
		// List<Element> elements = arrayEl.getChildren((arrType.isPrimitive() ?
		// "value" : "reference"));
		// for (int i = 0; i < length; i++) {
		// Array.set(eArray, i, elements.get(i));
		// }
		// }
		// }
	}

	private static Object getFieldValue(Class fType, Element field) {
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

	private static void inspect(Object o, int depth) {
		String padding = "";
		for (int i = 0; i < depth; i++) {
			padding += "\t";
		}
		try {
			if (o != null) {
				Class clazz = o.getClass();
				outputter.append(padding + "Class: " + clazz.getName() + "\n");
				Field[] fields = clazz.getDeclaredFields();

				for (Field f : fields) {
					f.setAccessible(true);
					Class fType = f.getType();
					outputter.append(padding + "> " + fType.getSimpleName() + " " + f.getName() + " = ");

					if (fType.isPrimitive()) {
						outputter.append(f.get(o).toString() + "\n");
					} else if (fType.isArray()) {
						Object arr = f.get(o);
						Class cType = fType.getComponentType();
						if (cType.equals(int.class)) {
							outputter.append(Arrays.toString((int[]) arr));
						} else {
							outputter.append("\n");
							Object[] objArray = (Object[]) arr;
							for (Object e : objArray) {
								inspect(e, depth + 1);
							}
						}
						outputter.append("\n");
					} else {
						Object toInspect = f.get(o);
						if (!inspectedObjs.contains(toInspect)) {
							outputter.append("\n");
							inspectedObjs.add(toInspect);
							inspect(toInspect, depth + 1);
							outputter.append("\n");
						} else {
							outputter.append("Circular Reference");
						}
					}

				}
			} else {
				outputter.append(padding + "null");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
