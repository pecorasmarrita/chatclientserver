package src;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChatServer
{
	
	private static ArrayList<ThreadChatServer> threadlist = new ArrayList<ThreadChatServer>(); // Dichiarazione ArrayList con ThreadChatServer
	private static ExecutorService threadpool = Executors.newFixedThreadPool(2); // Dichiarazione Pool di Thread

	public static void main(String[] args) throws Exception
	{
		ServerSocket serversocket = new ServerSocket (8888); // Inizializzazione ServerSocket, in attesa di connessione 
		System.out.println("/nIn attesa della connessione del client sulla porta 8888");
		for (;;)
		{
			Socket client = serversocket.accept(); // Accettamento connessione
			System.out.println("/nConnessione con " + client + " completata");
			ThreadChatServer threadchatserver = new ThreadChatServer (client, threadlist); // Creazione ThreadChatServer
			threadlist.add (threadchatserver); // Aggiunta ThreadChatServer all'Arraylist
			threadpool.execute (threadchatserver); // Esecuzione thread
		}
	}
}