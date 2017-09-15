import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class PartieOrdinateur implements Runnable {

	private Socket socket;
	private Conversation conversation;
	private String nomJoueur;
	
	public PartieOrdinateur(Socket socket, Conversation conversation, String nomJoueur){
		this.socket = socket;
		this.conversation = conversation;
		this.nomJoueur = nomJoueur;
	}
	
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
	
	public void run() {
		PrintWriter out;
		int nbrEssais = 10;
		
		
		try {
			out = new PrintWriter(socket.getOutputStream());
			out.println("Vous etes le temoin!");
			out.flush();
			
			String motMystere = "eclipse";
			int nbrLettre = motMystere.length();
			String motAffiche = "";
			
			for(int i =0 ; i<nbrLettre;i++){
				motAffiche += "_ ";
			}
			
			out.println("Mot choisi: "+motAffiche);
			out.flush();
	        
			out.println("Entrez une lettre. Il reste "+ nbrEssais + " essais" );
			out.flush();
			nbrEssais -= 1;
			
	        
			while(nbrEssais > 0){
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        String reponse = in.readLine();
				
				out.println("Entrez une lettre. Il reste "+ nbrEssais + " essais" );
				out.flush();
		        
		        
		        
		        nbrEssais -= 1;
			}
		
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	
	
	}
}
