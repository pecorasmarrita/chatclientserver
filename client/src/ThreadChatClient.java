package src;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * La classe ThreadChatClient implementa runnable, contiene il costruttore e il metodo run(). Si occupa della gestione dei messaggi server-client.
 * @author Leonardo Giustiniani
 *
 */

public class ThreadChatClient implements Runnable
{
	private Socket socketserver;
	private BufferedReader bufferedreader;

	
	/**
	 * Costruttore della classe ThreadChatClient, assegna alle variabili della classe quelle passate durante l'inizializzazione del thread.
	 * @param socket
	 * @throws Exception - in caso di errori con il socket oppure l'IO
	 */
	
	public ThreadChatClient(Socket socket) throws Exception
	{
		socketserver = socket; // socket del server
		bufferedreader = new BufferedReader(new InputStreamReader(socketserver.getInputStream())); // reader input
	}

	/**
	 * Metodo run della classe ThreadChatClient, si occupa della gestione e lettura messaggi server-client.
	 * 
	 */
	
	@Override
	public void run() // override methodo run
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
				System.out.println(input);
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