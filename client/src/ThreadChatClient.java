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
				System.out.println(input);
				SendWindows10Notification(input, trayIcon);
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
	
	/**
	 * Metodo SendWindows10Notification della classe ThreadChatClient, si occupa di inviare al client la notifica del messaggio.
	 * 
	 */
	
	public void SendWindows10Notification(String message, TrayIcon trayIcon)
	{
		if ("Windows 10".equals(System.getProperty("os.name")))
		{
			trayIcon.displayMessage("Beatiful chat", message, MessageType.INFO);
		}
	}
	
	/**
	 * Metodo getWindows10Tray della classe ThreadChatClient, si occupa di creare un'icona all'interno della tray di windows.
	 * 
	 */
	
	public TrayIcon getWindows10Tray()
	{
		if (!"Windows 10".equals(System.getProperty("os.name")))
		{
			System.out.println("Sistema di notifiche non supportato dal sistema operativo " + System.getProperty("os.name"));
		}
		else 
		{
			SystemTray tray = SystemTray.getSystemTray(); // ottenere system tray
		    Image image = Toolkit.getDefaultToolkit().createImage("./img/logo.png");
		    TrayIcon trayIcon = new TrayIcon(image, "Beatiful chat");
		    trayIcon.setImageAutoSize(true);
		    trayIcon.setToolTip("Beatiful chat");
		    try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.out.println("Can't send notification");
			}
		    return trayIcon;	
		}
		return null;
	}
}