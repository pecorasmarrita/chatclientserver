package src;

import java.util.*;
import java.io.*;
import java.net.*;


public class ChatClient 
{
	
	public static void main(String[] args) throws Exception
	{
		Socket socket = new Socket ("127.0.0.1", 8888); // Connessione socket
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));  
		PrintWriter printwriter = new PrintWriter(socket.getOutputStream(), true); // input
		ThreadChatClient threadchatclient = new ThreadChatClient (socket);
		Thread threadclientchat = new Thread (threadchatclient); // Thread client
		threadclientchat.start();
		String name = getName(); 
		for (;;)
		{
			System.out.println(name + ": "); 
			String input = bufferedreader.readLine(); // read input
			if(input.contains("disconnect")) // comando per la disconnessione
			{
				System.out.println("Fine della chat");
				break;
			}
			printwriter.println(input); 
		}
		socket.close(); // Chiusura socket
	}
	
	public static String getName() throws Exception
	{
		System.out.println("Inserire nome: ");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String name = input.readLine();
		return name;
	}
	
}