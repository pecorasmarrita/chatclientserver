package src;

import java.util.*;
import java.io.*;
import java.net.*;


public class ThreadChatServer implements Runnable
{
	private ArrayList<ThreadChatServer> clientlist;
	private Socket socket; 
	private BufferedReader bufferedreader; // input
	private PrintWriter printwriter; // output
	int i;
	
	public ThreadChatServer(Socket socketclient, ArrayList<ThreadChatServer> threadclient) throws Exception
	{
		this.socket = socketclient; // assegnazione valore socketclient a socket
		this.clientlist = threadclient; // assegnazione valore threadclient a client
		bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // input
		printwriter = new PrintWriter(socket.getOutputStream(), true); // output
	}
	
	
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
	
	
	private void output(String message)
	{
		for(int i = 0; i < clientlist.size(); i++)
		{
			clientlist.get(i).printwriter.println(message); // invio del messaggio
			System.out.println("Messaggio inviato");
		}
	}
	
	
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
