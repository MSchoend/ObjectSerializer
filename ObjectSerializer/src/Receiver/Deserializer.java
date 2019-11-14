package Receiver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import Sender.Driver;

public class Deserializer {

	public static void main(String args[]) {		
		
		try {
			
			ServerSocket sock = new ServerSocket(Driver.PORT);
			System.out.println("Waiting for connection");
			Socket clientSock = sock.accept();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
			
			System.err.println(in.readLine());
			clientSock.close();
			sock.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
