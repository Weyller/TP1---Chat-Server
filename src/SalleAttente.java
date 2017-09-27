import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SalleAttente implements Runnable{
	
	private static final int NB_MAX_CONNECTIONS = 2;
	

	ServerSocket socketDuServeur = null;
	
	public boolean gameReady(List<String> clients){
		
		return clients.size() == NB_MAX_CONNECTIONS;
	}
	
	
	public boolean socketsReady(int countConnections){
		
		return countConnections == NB_MAX_CONNECTIONS;
	}
	
//=============================================================
	private Socket socket;
	private Conversation conversation;
	private String nomJoueur;
	private BufferedReader in;
    private int coutConnection = 0;
	public SalleAttente(Socket socket, Conversation conversation, String nomJoueur, BufferedReader in){
		this.socket = socket;
		this.conversation = conversation;
		this.nomJoueur = nomJoueur;
		this.in = in;
		
	}
	
	@Override
	public void run() {
		PrintWriter out;
		coutConnection ++;
		
		System.out.println("Liste de sockets connectees: " + ServeurChat.listSockets);
		
	try {
		
			out = new PrintWriter(socket.getOutputStream());
			out.println("En attente d'un autre joueur...");
			out.flush();
		
			//Effectue la boucle tant que la salle d'attente comporte seulement 1 joueur
			//====================================================================
			while(ConnexionClient.listeSalleAttente.size() == 1)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
			//quand il y a deux joueur pr�sent dans la salle d'attente la partie commence
			//===============================
			out.println("La partie commence!");
			out.flush();
			
			
			Partie2Joueurs partie = new  Partie2Joueurs(ServeurChat.listSockets,conversation, conversation.getClients(), in);
			partie.run();
			
			socket.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
}
