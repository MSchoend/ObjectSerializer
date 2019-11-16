package Sender;

import java.util.ArrayList;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.lang.reflect.*;

public class Serializer {

	private Document objsToSend;
	private Element root;
	private ArrayList<Integer> serializedIDs;

	public Serializer() {
		objsToSend = new Document();
		root = new Element("serialized");
		objsToSend.addContent(root);
		serializedIDs = new ArrayList<Integer>();

	}

	public Document serialize(ArrayList<Object> list) {
		for (Object o : list) {
			add(convertToXML(o));
		}
		return objsToSend;
	}

	private Element convertToXML(Object o) {
		Element e = new Element("object");
		int id = System.identityHashCode(o);
		e.setAttribute("class", o.getClass().getName());
		e.setAttribute("id", Integer.toString(id));

		Class c = o.getClass();
		if (!c.isArray()) {
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
		} else {
			convertArray(o, e);
		}

		return e;
	}

	public void add(Element e) {
		root.addContent(e);
	}

	//For debugging purposes only
	public void send() {
		try {
			File loc = new File("output.txt");
			PrintWriter output = new PrintWriter(loc);
			XMLOutputter xmlWriter = new XMLOutputter(Format.getPrettyFormat());
			output.println(xmlWriter.outputString(objsToSend));
			output.close();
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
		if (t.isPrimitive()) {
			Element value = new Element("value");
			value.addContent(f.get(o).toString());
			e.addContent(value);
		} else if (t.isArray()) {
			Element reference = new Element("reference");
			Object value = f.get(o);
			reference.addContent(Integer.toString(System.identityHashCode(value)));
			root.addContent(convertToXML(value));
			e.addContent(reference);
			
		} else {
			Element reference = new Element("reference");
			Object value = f.get(o);
			if (value != null) {
				int hash = System.identityHashCode(value);
				reference.addContent(Integer.toString(hash));
			} else {
				reference.addContent("-1");
			}
			e.addContent(reference);
		}

		return e;
	}
	
	private void convertArray(Object o, Element e){
		Class arrType = o.getClass().getComponentType();
		if(arrType.equals(int.class)){
			int[] iArr = (int[])o;
			e.setAttribute("length", Integer.toString(iArr.length));
			for(int i : iArr){
				Element value = new Element("value");
				value.addContent(Integer.toString(i));
				e.addContent(value);
			}
		} else {
			Object[] oArr = (Object[])o;
			e.setAttribute("length", Integer.toString(oArr.length));
			for(Object v : oArr){
				Element reference = new Element("reference");
				reference.addContent(Integer.toString(System.identityHashCode(v)));
				e.addContent(reference);
			}
		}
	}

}
