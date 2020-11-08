package src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ChatClientGui extends JFrame {

	public JPanel contentPane;
	public static JLabel labelarea;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println("Benvenuto in Beatiful Chat!");
		Socket socket = new Socket ("127.0.0.1", 8888); // Connessione socket
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));  
		PrintWriter printwriter = new PrintWriter(socket.getOutputStream(), true); // input
		BufferedReader serverreader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // reader input
		String servermessage = ""; String nameinput = "";
		String listapartecipanti = serverreader.readLine();
		do {
			try {
				serverreader.readLine();
				nameinput = JOptionPane.showInputDialog("Inserire nome utente:");
				printwriter.println(nameinput);
				Thread.sleep(1000);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Input non valido.", "Attenzione!",  JOptionPane.ERROR_MESSAGE);
			}
			servermessage = serverreader.readLine();
			if (!servermessage.equals("Nome utente impostato correttamente"))
			{
				JOptionPane.showMessageDialog(null, servermessage, "Attenzione!",  JOptionPane.ERROR_MESSAGE);
			}
		} while (!(servermessage.equals("Nome utente impostato correttamente")));
		ChatClientGui frame = new ChatClientGui();
		frame.setVisible(true);
		frame.setTitle("Beatiful Chat - Benvenuto " + nameinput);
		ThreadChatClientGUI threadchatclient = new ThreadChatClientGUI (socket, frame, labelarea);
		Thread threadclientchat = new Thread (threadchatclient); // Thread client
		threadclientchat.start();
		System.out.println("Caricamento completato! Per favore apri la finestra che è apparsa.");
		System.out.println("Inserire messaggio desiderato, usare 'disconnect' per disconnettersi, usare '@nomeutente' per inviare un messaggio privato");
		for (;;)
		{
			System.out.println("> ");
			String input = bufferedreader.readLine(); // read input
			if(input.contains("disconnect")) // comando per la disconnessione
			{
				System.out.println("Fine della chat");
				break;
			}
			if (input.startsWith("@"))
			{
				System.out.println("Inserire messaggio privato da inviare a " + input.substring(1));
			}
			printwriter.println(input);
		}
		socket.close(); // Chiusura socket
		System.exit(0);
		
	}

	/**
	 * Create the frame.
	 */
	public ChatClientGui() {
		setTitle("Beatiful Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 553, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JTextArea txtrInserireMessaggio = new JTextArea();
		labelarea = new JLabel();
		txtrInserireMessaggio.setToolTipText("Inserire messaggio");
		contentPane.add(txtrInserireMessaggio, BorderLayout.SOUTH);
		contentPane.add(labelarea);
	}

}
