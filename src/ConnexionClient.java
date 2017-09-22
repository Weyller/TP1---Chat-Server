

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;

/**
 * Encapsule une connexion avec un client.
 * Un client roule dans un thread dedie.
 * 
 * Lorsqu'un message est envoye par un autre client, cet
 * objet est alertee.
 * 
 * @author rebecca
 */
public class ConnexionClient implements Observer, Runnable{
	private Socket socket;
	private Conversation conversation;
	
	/**
	 * Constructeur parametrique 
	 * 
	 * @param socket la connexion vers le client
	 * @param conversation la conversation
	 */
	public ConnexionClient(Socket socket, Conversation conversation)
	{
		this.socket = socket;
		this.conversation = conversation;
		conversation.addObserver(this);
	}
	
	/**
	 * Mise a jour de la conversation
	 */
	public void update(Observable arg0, Object arg1) {
		PrintWriter out;
		try {
						
			out = new PrintWriter(socket.getOutputStream());
			out.println("********** " + conversation.getLastMessage());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execution du thread pour un client
	 * 
	 * Le client doit donner son nom. Ce nom doit être unique et ne peut pas
	 * être un mot réservé (ni QUIT ni ?).
	 * 
	 * Pour quitter, il doit entrer "QUIT"
	 * Pour voir la liste des clients connectés, il doit entrer "?"
	 */
	public void run() {
		ExecutorService service = null;
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream());
			out.println("Bienvenue au jeu du pendu!(serveur de Antoine)");	        
	        out.println("Entrez votre nom.");
	        out.flush();
	        

	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String nom = in.readLine();
	        while (nom.equals("?") || nom.equals("QUIT") || !conversation.ajouterClient(nom))
	        {
		        out.println("S'il vous plait choisissez un nom unique et différent de ? et QUIT.");
		        out.flush();
		        nom = in.readLine();
	        } 
	        
	        out.println("Voulez-vous jouer contre un joueur ou l'ordinateur [j/o]");
	        out.flush();
	        
	        String choix = in.readLine();
	        while(!choix.equals("j") && !choix.equals("o"))
	        {
	        	out.println("Veuillez entrer [j] pour joueur ou [o] pour ordinateur");
	        	out.flush();
	        	choix = in.readLine();
	        }
	        
	        if(choix.equals("j")){
	        	
	        	//out.println("Vous avez choisi joueur");
	        	
	        	PartieJoueur joueur = new PartieJoueur(socket, conversation, nom);
	        	
	        }
	        else if(choix.equals("o")){
	        	PartieOrdinateur bot = new PartieOrdinateur(socket, conversation, nom);
	        	bot.run();
	        }
	    
	        
	        conversation.deleteObserver(this);  
	        conversation.retirerClient(nom);
	        socket.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
