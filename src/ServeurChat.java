
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

	/**
	 * Execution du serveur
	 */
	public void run()
	{
		ServerSocket socketDuServeur = null;// Le socket qui écoute sur le port 8888 et accepte les connexions.
		ExecutorService service = null;
		boolean premierJoueur = true;

		try 
		{
			socketDuServeur = new ServerSocket(PORT);
			System.out.println("Le serveur est à l'écoute du port " + socketDuServeur.getLocalPort());

			service = Executors.newFixedThreadPool(NB_CLIENTS);

			conversation.addObserver(this);

			while(true)
			{
				// Connexion d'un client
				//*a faire
				//chaque connexion ajout� a une liste 
				if(premierJoueur == true)
				{
					Socket socketVersLeClient1 = socketDuServeur.accept();
					System.out.println("Un client s'est connecte au serveur");
					System.out.println(socketVersLeClient1);

					String role = "bourreau";

					service.submit(new ConnexionClient(socketVersLeClient1, conversation, role));
					premierJoueur = false;
				}
				else{
					Socket socketVersLeClient2 = socketDuServeur.accept();
					System.out.println("Un client s'est connecte au serveur");
					System.out.println(socketVersLeClient2);

					String role = "temoin";

					service.submit(new ConnexionClient(socketVersLeClient2, conversation, role));
					premierJoueur = true;
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
