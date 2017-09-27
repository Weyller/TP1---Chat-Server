
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Encapsule un serveur qui permet a un maximum de NB_CLIENTS clients a la fois
 * de participer a une conversation sous forme de protocole TCP sur port PORT
 * 
 * @author rebecca
 *
 */
public class ServeurChat implements Observer{

	Conversation conversation = new Conversation();

	final int NB_CLIENTS = 30;
	final int PORT = 8888;
	final int NB_MAX_CONNECTIONS = 2;
	private int countConnections = 0;
	
	public static List<Socket> listSockets = new ArrayList();
	public static List<BufferedReader> listBufferedReader = new ArrayList();
	//=============================================================================
	
	public boolean gameReady(List<String> clients){
		
		return clients.size() == NB_MAX_CONNECTIONS;
	}
	
	
	public boolean socketsReady(int countConnections){
		
		return countConnections == NB_MAX_CONNECTIONS;
	}
	
	//============================================================================
	/**
	 * Execution du serveur
	 */
	public void run()
	{
		ServerSocket socketDuServeur = null;// Le socket qui écoute sur le port 8888 et accepte les connexions.
		ExecutorService service = null;
		boolean premierJoueur = true;
		boolean joueursPrets = false;
		boolean socketsPrets = false;
		
		

		try 
		{
			socketDuServeur = new ServerSocket(PORT);
			System.out.println("Le serveur est à l'écoute du port " + socketDuServeur.getLocalPort());

			service = Executors.newFixedThreadPool(NB_CLIENTS);
					
						
			conversation.addObserver(this);

			// Connexion d'un client
			//==========================================
			while(true)
			{
					if(premierJoueur){
						
						Socket socketVersLeClient1 = socketDuServeur.accept();
						BufferedReader reader1 = new BufferedReader(new InputStreamReader(socketVersLeClient1.getInputStream()));
						String role = "bourreau";
						countConnections++;
						 
						socketsPrets = socketsReady(countConnections);
						System.out.println("Un client s'est connecte au serveur");
						System.out.println("Socket 1er joueur: " + socketVersLeClient1.getPort());
						
						listSockets.add(socketVersLeClient1);
						listBufferedReader.add(reader1);
						
						service.submit(new ConnexionClient(socketVersLeClient1, conversation, role));
						
						premierJoueur = false;
						
					}
					else 
						{
						
							Socket socketVersLeClient2 = socketDuServeur.accept();
							BufferedReader reader2 = new BufferedReader(new InputStreamReader(socketVersLeClient2.getInputStream()));
							String role = "temoin";
							
							System.out.println("Un client s'est connecte au serveur");
							System.out.println("Socket 2eme joueur: " + socketVersLeClient2.getPort());
							
							listSockets.add(socketVersLeClient2);
							listBufferedReader.add(reader2);

							service.submit(new ConnexionClient(socketVersLeClient2, conversation, role));
							
							premierJoueur = true;
						}
			
				if(socketsReady(countConnections)){
					
					System.out.println("Le jeu est pret");
					socketDuServeur.close();  
					break;
				}
				
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				socketDuServeur.close();  
				shutdownAndAwaitTermination(service);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Utilitaire pour fermer le pool de thread.
	 * Calqué de la documentation Oracle pour la classe ExecutorService
	 * @param pool
	 */
	static void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Lorsqu'un client envoie un message a la conversation
	 * Le serveur est alerte. Ceci sert uniquemet a afficher
	 * la conversation a la console.
	 */
	public void update(Observable arg0, Object arg1) 
	{		
		System.out.println(conversation.getLastMessage());
	}
}
