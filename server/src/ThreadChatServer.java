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
	private String username;
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
		System.out.println(socket);
		this.clientlist = threadclient; // assegnazione valore threadclient a client
		System.out.println(clientlist);
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
		printwriter.println(nameList());
		setName();
		// visualizzare partecipanti connessi
		try {
			for(;;) // ciclo continuo
			{
				String input = "";
					input = bufferedreader.readLine(); int index = input.indexOf(""); // lettura input
					if (input.startsWith("@"))
					{
						String recipient = input.substring(1); int index2 = recipient.indexOf("");
						input = "";
						input = bufferedreader.readLine(); index = input.indexOf(""); // lettura input
						output(input.substring(index), recipient.substring(index2));
					}
					else 
					{
						output(input.substring(index)); // passa il messaggio al metodo output
					}
			}
		} catch (Exception e) {
			System.out.println("Connessione con il client chiusa");
		}
			endSession(); // chiusura sessione
			try {
				bufferedreader.close(); // chiusura input
			} catch (IOException e) {
				e.printStackTrace();
			}
			printwriter.close(); // chiusura output
	}
	
	/**
	 * Metodo output della classe ThreadChatServer, si occupa dell'invio dei messaggi ai client destinatari.
	 * 
	 */
	
	private void output(String message)
	{
		String messagetime = "" + java.time.LocalTime.now();
		messagetime = messagetime.substring(0, 5);
		for(ThreadChatServer thread : clientlist)
		{
			thread.printwriter.println(username + ": "+ message + "\t" + messagetime); // invio del messaggio
		}
		System.out.println("Messaggio inviato da " + username);
	}
	
	private void output(String message, String recipient)
	{
		String messagetime = "" + java.time.LocalTime.now();
		messagetime = messagetime.substring(0, 5);
		for(ThreadChatServer thread : clientlist)
		{
			if (recipient.equals(thread.getName()))
			{
				thread.printwriter.println("Messaggio privato da " + username + ": "+ message + "\t" + messagetime); // invio del messaggio
			}
		}
		System.out.println("Messaggio inviato da " + username);
	}
	
	/**
	 * Metodo getName della classe ThreadChatServer, si occupa di restituire il nome utente.
	 * 
	 */
	
	public String getName()
	{
		return username;
	}
	
	/**
	 * Metodo setName della classe ThreadChatServer, si occupa di impostare il nome utente.
	 * 
	 */
	
	private void setName()
	{
		String tmpstring = null;
		boolean verified = false;
		while (!(verified))
		{
			verified = true;
			printwriter.println("Inserire nome utente:");
			try 
			{
				tmpstring = bufferedreader.readLine();
				tmpstring = tmpstring.replace("\n", "").replace("\r", "");
				for (ThreadChatServer thread : clientlist)
				{
					if (tmpstring.equals(thread.getName()))
					{
						printwriter.println("Nome già presente all'interno della chat");
						verified = false;
						break;
					}
				}
			}
			catch (IOException e)
		    {
				printwriter.println("Errore durante l'input, si prega di riprovare");
			}
		}
		this.username = tmpstring;
		sendUpdate();
		printwriter.println("Nome utente impostato correttamente");
		System.out.println(socket + " ha impostato il nome utente " + username);
		for(ThreadChatServer thread : clientlist)
		{
			thread.printwriter.println(username + " si è connesso"); // invio informazione disconnessione
		}
		// aggiornare lista partecipanti
	}
	
	public String nameList()
	{
		String namelist = "";
		for(ThreadChatServer thread : clientlist)
		{
			namelist = namelist + thread.getName() + ", ";
		}
		String tmpstring = namelist.replace(", ", "").replace("null", "");
		if (tmpstring.length()<1)
		{
			namelist = "Al momento non è connesso nessuno";
		}
		else 
		{
			namelist = namelist.replace("null, ", "");
			namelist = namelist.substring(0, namelist.length() - 2);
			namelist = "Partecipanti: " + namelist;
		}
		return namelist;
	}

	
	/**
	 * Metodo endSession della classe ThreadChatServer, si occupa della chiusura delle sessioni con i client.
	 * 
	 */
	
	public void endSession()
	{
		System.out.println("Disconnessione client di " + username);
		clientlist.remove(this);
		for(ThreadChatServer thread : clientlist)
		{
			thread.printwriter.println(username + " si è disconnesso"); // invio informazione disconnessione
		}
		sendUpdate();
		if (clientlist.size()<2)
		{
			for(ThreadChatServer thread : clientlist) thread.printwriter.println("Sei rimasto da solo in chat, ulteriori messaggi saranno visibili solo a te"); // invio informazione disconnessione
		}
	}
	
	public void sendUpdate()
	{
		for(ThreadChatServer thread : clientlist) 
		{
			System.out.println("Aggiornamento della sessione");
			thread.updateSession();
		}
	}
	
	public void updateSession()
	{
		clientlist = ChatServer.threadlist;
	}
}
