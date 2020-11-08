package src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe ChatClientGui, si occupa di creare l'interfaccia utente dell'applicazione e di connettere il client al server
 */

public class ChatClientGui extends JFrame {

	private static String listapartecipanti;
	public JPanel contentPane;
	public static JLabel messaggi;
	public static JLabel partecipanti;
	public Socket socket;
	public PrintWriter printwriter;

	/**
	 * Metodo main della classe ChatClientGui, si occupa di stabilire la connessione al server e di visualizzare l'interfaccia utente.
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
		listapartecipanti = serverreader.readLine();
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
		ChatClientGui frame = new ChatClientGui(printwriter, socket);
		frame.setIconImage(ImageIO.read(new File("./img/logo.png")));
		frame.setVisible(true);
		frame.setTitle("Beatiful Chat - Benvenuto " + nameinput);
		ThreadChatClientGUI threadchatclient = new ThreadChatClientGUI (socket, frame, messaggi, partecipanti);
		Thread threadclientchat = new Thread (threadchatclient); // Thread client
		threadclientchat.start();
		System.out.println("Caricamento completato! Per favore apri la finestra che è apparsa.");
		System.out.println("Inserire messaggio desiderato, usare 'disconnect' per disconnettersi, usare '@nomeutente' per inviare un messaggio privato");
		for(;;) {}
	}

	/**
	 * Costruttore, si occupa di creare l'interfaccia utente dell'applicazione
	 */
	public ChatClientGui(PrintWriter printwriter, Socket socket) {
		setTitle("Beatiful Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JTextField txtrInserireMessaggio;
		txtrInserireMessaggio = new JTextField();
		messaggi = new JLabel();
		partecipanti = new JLabel(listapartecipanti);
		JScrollPane scroller = new JScrollPane(messaggi, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		txtrInserireMessaggio.setToolTipText("Vuoi mandare un messaggio privato? Menziona l'utente che vuoi contattare usando '@nomeutente', usa 'disconnect' per disconnettersi");
		contentPane.add(txtrInserireMessaggio, BorderLayout.SOUTH);
		contentPane.add(scroller);
		contentPane.add(partecipanti, BorderLayout.NORTH);
		txtrInserireMessaggio.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try {
						sendMessage(txtrInserireMessaggio, printwriter, socket);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * Metodo sendMessage, si occupa di mandare il messaggio inviato
	 * @param txtrInserireMessaggio campo di testo contentente il messaggio
	 * @param printwriter si occupa dell'invio
	 * @param socket socket
	 */
	
	public void sendMessage (JTextField txtrInserireMessaggio, PrintWriter printwriter, Socket socket) throws IOException
	{
		String input = txtrInserireMessaggio.getText();
		System.out.println(input);
		txtrInserireMessaggio.setText("");
		if(input.contains("disconnect")) // comando per la disconnessione
		{
			System.out.println("Fine della chat");
			socket.close(); // Chiusura socket
			System.exit(0);
		}
		else
		{
			printwriter.println(input);
		}
		
	}

}
