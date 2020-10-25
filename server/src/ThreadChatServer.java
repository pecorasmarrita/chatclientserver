package src;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * La classe ThreadChatServer implementa runnable e contiene il costruttore e i metodi run(), output() e endSession(). La classe si occupa dell'invio dei messaggi ai client e della gestione della sessione.
 * @author Leonardo Giustiniani
 *
 */

public class ThreadChatServer implements Runnable
{
	private ArrayList<ThreadChatServer> clientlist;
	private Socket socket; 
	private BufferedReader bufferedreader; // input
	private PrintWriter printwriter; // output
	int i;
	
	/**
	 * Costruttore della classe ThreadChatServer, assegna alle variabili della classe quelle passate durante l'inizializzazione del thread.
	 * @param socketclient
	 * @param threadclient 
	 * @throws Exception - in caso di errori con il socket oppure l'IO
	 */
	
	public ThreadChatServer(Socket socketclient, ArrayList<ThreadChatServer> threadclient) throws Exception
	{
		this.socket = socketclient; // assegnazione valore socketclient a socket
		this.clientlist = threadclient; // assegnazione valore threadclient a client
		bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // input
		printwriter = new PrintWriter(socket.getOutputStream(), true); // output
	}
	
	/**
	 * Metodo run della classe ThreadChatServer, si occupa dell'esecuzione dei metodi di invio dei messaggi e della chiusura della sessione.
	 * 
	 */
	
	@Override
	public void run()
	{
			for(;;) // ciclo continuo
			{
				String input = ""; 
				try {
					input = bufferedreader.readLine(); int index = input.indexOf(""); // lettura input
					output(input.substring(index)); // passa il messaggio al metodo output
					endSession(); // chiusura sessione
					bufferedreader.close(); // chiusura input
					printwriter.close(); // chiusura output
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * Metodo output della classe ThreadChatServer, si occupa dell'invio dei messaggi ai client destinatari.
	 * 
	 */
	
	private void output(String message)
	{
		for(int i = 0; i < clientlist.size(); i++)
		{
			clientlist.get(i).printwriter.println(message); // invio del messaggio
			System.out.println("Messaggio inviato");
		}
	}
	
	/**
	 * Metodo endSession della classe ThreadChatServer, si occupa della chiusura delle sessioni con i client.
	 * 
	 */
	
	public void endSession()
	{
		System.out.println("Disconnessione client");
		i = 0;
		for(ThreadChatServer thread : clientlist)
		{
			thread.printwriter.println("Destinatario disconnesso, ulteriori messaggi non verranno recapitati"); // invio informazione disconnessione
			i++;
			clientlist.remove(i);
		}
	}
}
