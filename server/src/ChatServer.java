package src;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * La classe ChatServer contiene il metodo main() e si occupa dell'apertura del socket, dell'attesa della connessione al socket da parte dei client, della creazione del pool dei thread ThreadChatServer e del loro lancio.
 * @author Leonardo Giustiniani
 *
 */

public class ChatServer
{

	
	private static ArrayList<ThreadChatServer> threadlist = new ArrayList<ThreadChatServer>(); // Dichiarazione ArrayList con ThreadChatServer
	private static ExecutorService threadpool = Executors.newFixedThreadPool(2); // Dichiarazione Pool di Thread

	/**
	 * Metodo main della classe ChatServer, si occupa dell'avvio delle istruzioni da eseguire.
	 * @param args I parametri passati all'avvio del programma.
	 * @throws Exception nel caso di errori sul socket
	 */
	
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