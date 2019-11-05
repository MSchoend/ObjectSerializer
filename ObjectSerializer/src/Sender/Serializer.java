package Sender;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Serializer {

	private Socket clientSock;
	
	public Serializer(int port) {
		try {
			System.out.print("Enter IP: ");
			Scanner in = new Scanner(System.in);
			String ip = in.nextLine();
			clientSock = new Socket(ip, Driver.PORT);
			System.out.println("connected!");
			
			PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);
			out.println("Hello, there!");
			clientSock.close();
		} catch (IOException e) {
			System.out.println("Error connecting to client! Aborting...");
			e.printStackTrace();
		}
	}
	
	
}
