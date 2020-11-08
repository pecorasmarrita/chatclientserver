package src;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * La classe ChatClient contiene il metodo main() e si occupa della connessione al server, della creazione dei thread ThreadChatClient, dell'impostazione del nome utente e dell'invio dei messaggi al server.
 * @author Leonardo Giustiniani
 *
 */

public class ChatClient 
{
	/**
	 * Metodo main della classe ChatClient, si occupa dell'avvio delle istruzioni da eseguire.
	 * @param args - I parametri passati all'avvio del programma.
	 * @throws Exception - nel caso di errori sul socket
	 */
	
	public static void main(String[] args) throws Exception
	{
		Socket socket = new Socket ("127.0.0.1", 8888); // Connessione socket
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));  
		PrintWriter printwriter = new PrintWriter(socket.getOutputStream(), true); // input
		ThreadChatClient threadchatclient = new ThreadChatClient (socket);
		Thread threadclientchat = new Thread (threadchatclient); // Thread client
		threadclientchat.start();
		System.out.println("Inserire messaggio desiderato, usare 'disconnect' per disconnettersi");
		for (;;)
		{
			System.out.println("> ");
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
	
	/**
	 * Metodo getName della classe ChatClient, si occupa dell'assegnazione del nome utente al client.
	 * 
	 */
	
	public static String getName() throws Exception
	{
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String name = input.readLine();
		return name;
	}
	
}