package src;

import java.util.*;
import java.io.*;
import java.net.*;


public class ThreadChatClient implements Runnable
{
	private Socket socketserver;
	private BufferedReader bufferedreader;

	
	public ThreadChatClient(Socket socket) throws Exception
	{
		socketserver = socket; // socket del server
		bufferedreader = new BufferedReader(new InputStreamReader(socketserver.getInputStream())); // reader input
	}

	
	@Override
	public void run()
	{
			for (;;)
			{
				String input = ""; // inizializzazione variabile input
				try {
					input = bufferedreader.readLine(); // lettura dal buffer
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(input == "") // messaggio vuoto
				{
					break;
				}
				System.out.println(": " + input);
			}
		try
		{
			bufferedreader.close(); // chiusura buffer
		}
		catch (Exception e)
		{
			System.out.println("Errore durante la disconnessione");
		}
	}
}