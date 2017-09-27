import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class Partie2Joueurs implements Runnable {


	private List<Socket> listSocket;
	private Conversation conversation;
	private List<String> clients;
	private BufferedReader in;

	public Partie2Joueurs(List<Socket> listSocket, Conversation conversation, List<String> clients, BufferedReader in){
		
		this.listSocket = listSocket;
		this.conversation = conversation;
		this.clients = clients;
		this.in = in;
	}


	public void run() {
		PrintWriter outTemoin;
		PrintWriter outBourreau;
		int nbrEssais = 10;
		char lettre;
		Socket socketTemoin = listSocket.get(1);
		Socket socketBourreau = listSocket.get(0);		
		ArrayList<Character> lettresEssayees = new ArrayList<>();
		List<String> nomsJoueurs = new ArrayList<>();
		
		nomsJoueurs.addAll(conversation.getClients());
		
		//=========================================================
		try {
			
			outTemoin = new PrintWriter(socketTemoin.getOutputStream());
			outBourreau = new PrintWriter(socketBourreau.getOutputStream());
			
			//===========================================
			outTemoin.println("Vous etes le temoin. En attente du choix du mot de "+ nomsJoueurs.get(0));
			outTemoin.flush();

			//===========================================
			outBourreau.println("Vous etes le bourreau. Entrez un mot!");
			outBourreau.flush();
			
			//changement du in pour le bourreau
			//=========================================
			in = ServeurChat.listBufferedReader.get(0);
			String reponseBourreau = in.readLine();
			System.out.println(reponseBourreau);
			
			//mot que la personne doit trouver
			//=========================================== 
			String motMystere = UDPClient.envoyerMessage(reponseBourreau.toUpperCase()).toLowerCase();
			
			//Effectue la boucle tant que le mot n'est pas valide dans le dictionnaire
			//================================================
			while(!motMystere.equals(reponseBourreau)){
				
				outBourreau.println("Mot invalide. Entrez un nouveau mot!");
				outBourreau.flush();
				
				reponseBourreau = in.readLine();
				motMystere = UDPClient.envoyerMessage(reponseBourreau.toUpperCase()).toLowerCase();
			}
			
			//détermine le nombre de - a afficher par rapport au mot mystère
			//=========================================== 
			int nbrLettre = motMystere.length(); 
			String motAffiche = ""; 

			
			for(int i =0 ; i<nbrLettre;i++){
				motAffiche += "-";
			}
			
			outBourreau.println("mot valide! " + nbrLettre + " lettres");
			outBourreau.flush();			 

			//Affiche  le nombre de lettres du mot mystère
			//===================================================================
			outTemoin.println("Mot choisi: "+motAffiche + "(" + nbrLettre + " lettres)");
			outTemoin.flush();
       

			//vérifie si la lettre ou le mot entrée est dans le mot mystère
			//===================================================================
			while(nbrEssais > 0 && motAffiche.contains("-")){

				outBourreau.println("En attente de " + nomsJoueurs.get(1));
				outBourreau.flush();
				
				outTemoin.println("Entrez une lettre. Il reste "+ nbrEssais + " essais" );
				outTemoin.flush();

				in = ServeurChat.listBufferedReader.get(1);
				String reponse = in.readLine();
				System.out.println("reponse: " + reponse);
				
				//si l'utilisateur entre un mot
				//==========================================
				if(reponse.length() > 1)
				{
					int longeur = reponse.length();
					
					for(int i =0;i<longeur;i++)
					{
						lettre = reponse.charAt(i);
						
						if(!lettresEssayees.contains(lettre))
						{
							lettresEssayees.add(lettre);
							nbrEssais = DetermineEssais(motMystere, lettre, nbrEssais);
							motAffiche = DetermineLettre(motMystere, lettre, outTemoin, motAffiche, nbrEssais, nbrLettre,lettresEssayees);
							DetermineLettre(motMystere, lettre, outBourreau, motAffiche, nbrEssais, nbrLettre, lettresEssayees);
							
							if(nbrEssais == 0)
								break;
						}
						else
						{
							outTemoin.println("Lettre deja utilisee");
						}   
					}
				}
				
				// si l'utilisateur entre seulement 1 lettre
				//===========================================
				else 
				{
					lettre = reponse.charAt(0);
					
					if(!lettresEssayees.contains(lettre))
					{
						lettresEssayees.add(lettre);
						nbrEssais = DetermineEssais(motMystere, lettre, nbrEssais);
						motAffiche = DetermineLettre(motMystere, lettre, outTemoin, motAffiche, nbrEssais, nbrLettre,lettresEssayees);
						DetermineLettre(motMystere, lettre, outBourreau, motAffiche, nbrEssais, nbrLettre, lettresEssayees);
						
						if(nbrEssais == 0)
							break;
					}
					else
					{
						outTemoin.println("Lettre deja utilisee");
					}
    
				}
			}
			//===================================================================

			//Fin de la partie
			//===================================================================
			
			if(nbrEssais == 0){
				outTemoin.println("Vous avez perdu!!!");
				outTemoin.println("\n_________");
				outTemoin.println("|       |");
				outTemoin.println("|       O");
				outTemoin.println("|     --|--");
				outTemoin.println("|      / \\");
				outTemoin.println("|________________");
				outTemoin.flush();
				
				outBourreau.println("Vous avez gagner!!!");
				outBourreau.println("      _");
				outBourreau.println("     /(|");
				outBourreau.println("    (  :");
				outBourreau.println("   __\\  \\  _____");
				outBourreau.println(" (____)  `|");
				outBourreau.println("(____)|   |");
				outBourreau.println(" (____).__|");
				outBourreau.println("  (___)__.|_____");
				outBourreau.flush();
			}
			else{
				outTemoin.println("Vous avez gagner!!!");
				outTemoin.println("      _");
				outTemoin.println("     /(|");
				outTemoin.println("    (  :");
				outTemoin.println("   __\\  \\  _____");
				outTemoin.println(" (____)  `|");
				outTemoin.println("(____)|   |");
				outTemoin.println(" (____).__|");
				outTemoin.println("  (___)__.|_____");
				outTemoin.flush();
				
				outBourreau.println("Vous avez perdu!!!");
				outBourreau.println("\n_________");
				outBourreau.println("|       |");
				outBourreau.println("|       O");
				outBourreau.println("|     --|--");
				outBourreau.println("|      / \\");
				outBourreau.println("|________________");
				outBourreau.flush();
			}

			outTemoin.println("Bye xoxo");
			outTemoin.flush();
			outBourreau.println("Bye xoxo");
			outBourreau.flush();
			
			
			//===================================================================

		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
//===================================================================
/*
 * DetermineEssais
 * 
 * vérifie si la lettre entrée est dans le mot mystère et ajuste le nombre d'essais
 * Si la lettre n'est pas dans le mot mystère, le nombre d'essais diminue de 1
 * 
 * retourne le nombre d'essais
 * **/
//===================================================================
	private int DetermineEssais(String motMystere, char lettre, int nbrEssais) {
		if(motMystere.indexOf(lettre) < 0)
        {
			nbrEssais -= 1;
        }
        
        return nbrEssais;
	}
	//===================================================================
	/*
	 * DetermineLettre
	 * 
	 * détermine si la lettre entrée est une bonne ou une mauvaise réponse
	 * 
	 * Si la lettre n'est pas dans le mot mystère on affiche mauvaise réponse
	 * ainsi que le status du mot a trouver.
	 * 
	 * Si la lettre est dans le mot mystere, la lettre est révélé dans le status 
	 * du mot a trouver
	 * 
	 * retourne le status du mot a trouver
	 * **/
	//===================================================================
	private String DetermineLettre(String motMystere, char lettre, PrintWriter out, String motAffiche, int nbrEssais, int nbrLettre, ArrayList<Character> lettresEssayees) {
		if(motMystere.indexOf(lettre) < 0)
		{
			out.println("\nmauvaise reponse");
			out.println(motAffiche + " " + lettresEssayees );
			out.flush();
			nbrEssais -= 1;
		}
		else
		{
			for(int index = 0;index<nbrLettre;index++){
				if(motMystere.charAt(index) == lettre){
					char[] motAfficheChar = motAffiche.toCharArray();
					motAfficheChar[index] = lettre;
					motAffiche = String.valueOf(motAfficheChar);
					out.println("\nbonne reponse");
					out.flush();
				}
			}
			out.println(motAffiche + " " + lettresEssayees);
			out.flush();
		}
		return motAffiche;
	}
	//===================================================================

}
