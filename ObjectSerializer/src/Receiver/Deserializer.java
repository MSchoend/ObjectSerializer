package Receiver;

import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import SampleObjs.SimpObj;
import Sender.Driver;

public class Deserializer {

	private static ArrayList<Object> allObjs;
	private static ArrayList<Integer> allIDs;

	public static void main(String args[]) {

		allObjs = new ArrayList<Object>();
		allIDs = new ArrayList<Integer>();

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
			for(Object o : allObjs){
				inspect(o);
			}
			clientSock.close();
			sock.close();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException
				| JDOMException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void deserialize(Document xml)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Element root = xml.getRootElement();
		List<Element> elems = root.getChildren();
		for (Element e : elems) {
			Class eClass = Class.forName(e.getAttributeValue("class"));
			Integer id = Integer.getInteger(e.getAttributeValue("id"));
			allIDs.add(id);
			List<Element> fields = e.getChildren();
			if (eClass.equals(SimpObj.class)) {
				SimpObj newObj = (SimpObj) eClass.newInstance();
				for(Element f: fields){
					String fName = f.getAttributeValue("name");
					System.err.println(fName);
					Field eField = eClass.getDeclaredField(fName);
					eField.setAccessible(true);
					Class fieldType = eField.getType();
					if(fieldType.equals(int.class))
						eField.set(newObj, Integer.parseInt(f.getValue()));
					else
						eField.set(newObj, Double.parseDouble(f.getValue()));
				}
				allObjs.add(newObj);
			}
		}
	}
	
	private static void inspect(Object o){
		try {
			PrintWriter outputter = new PrintWriter("inspected.txt");
			Class clazz = o.getClass();
			outputter.append("Class: " + clazz.getName() + "\n");
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				f.setAccessible(true);
				outputter.append(" Field: " + f.getName() + " - " + f.get(o) + "\n");
			}
			outputter.close();
		} catch (FileNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
