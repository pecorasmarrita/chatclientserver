package src;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;

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
	 * @param socket Socket connessione
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
				SendWindows10Notification(input);
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
	
	private void SendWindows10Notification(String message)
	{
	    SystemTray tray = SystemTray.getSystemTray(); // ottenere system tray
	    Image image = Toolkit.getDefaultToolkit().createImage("./logo.png");
	    TrayIcon trayIcon = new TrayIcon(image, "Beatiful chat");
	    trayIcon.setImageAutoSize(true);
	    trayIcon.setToolTip("Beatiful chat");
	    try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("Can't send notification");
		}
	    trayIcon.displayMessage("Beatiful chat", message, MessageType.INFO);
	}
}