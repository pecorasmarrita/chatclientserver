package src;

import java.util.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * La classe ThreadChatClientGUI implementa runnable, contiene il costruttore e il metodo run(). Si occupa della gestione dei messaggi server-client.
 * @author Leonardo Giustiniani
 *
 */

public class ThreadChatClientGUI extends ThreadChatClient implements Runnable
{
	private Socket socketserver;
	private BufferedReader bufferedreader;
	public ChatClientGui frame;
	public JLabel messaggi;
	public JLabel partecipanti;
	
	/**
	 * Costruttore della classe ThreadChatClient, assegna alle variabili della classe quelle passate durante l'inizializzazione del thread.
	 * @param socket Socket connessione
	 * @throws Exception - in caso di errori con il socket oppure l'IO
	 */
	
	public ThreadChatClientGUI(Socket socket, ChatClientGui frame, JLabel messaggi, JLabel partecipanti) throws Exception
	{
		super(socket);
		socketserver = socket; // socket del server
		this.frame = frame;
		this.messaggi = messaggi;
		this.partecipanti = partecipanti;
		bufferedreader = new BufferedReader(new InputStreamReader(socketserver.getInputStream())); // reader input
	}

	/**
	 * Metodo run della classe ThreadChatClient, si occupa della gestione e lettura messaggi server-client.
	 * 
	 */
	
	@Override
	public void run() // override methodo run
	{
			String guitext = "";
			TrayIcon trayIcon = getWindows10Tray();
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
				if (input.startsWith("Partecipanti: ") || input.equals("Al momento non è connesso nessuno"))
				{
					ChatClientGui.partecipanti.setText(input);
				}
				else if (input.equals("Impossibile inviare il messaggio: il nome utente selezionato non è valido"))
				{
					JOptionPane.showMessageDialog(null, "Impossibile inviare il messaggio: il nome utente selezionato non è valido", "Attenzione!",  JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					SendWindows10Notification(input, trayIcon);
					guitext = guitext + "<br>" + input;
					ChatClientGui.messaggi.setText("<html>"+guitext+"<html>");
				}
				
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