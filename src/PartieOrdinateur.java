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
			
			
			String motMystere = "eclipse"; 		  //mot que la personne doit trouver
			int nbrLettre = motMystere.length(); //nombre de lettre du mot mystère
			String motAffiche = ""; 			//ce qui sera afficher à l'écran de l'usager
			
			//détermine le nombre de _ a afficher par rapport au mot mystère
			for(int i =0 ; i<nbrLettre;i++){
				motAffiche += "-";
			}
			
			out.println("Mot choisi: "+motAffiche + "(" + nbrLettre + " lettres)");
			out.flush();
	        
			out.println("Entrez une lettre. Il reste "+ nbrEssais + " essais" );
			out.flush();
			nbrEssais -= 1;
			
	        //vérifie si la lettre entrée est dans le mot mystère
			while(nbrEssais+1 > 0 && motAffiche.contains("-")){
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        String reponse = in.readLine();
		        
		        //**** http://www.cs.jhu.edu/~joanne/cs107/code/StaticMethods/hangmanMethods.java ****
		        
		       // int indiceDeRecherche = 0;
		       // while(motMystere.indexOf(reponse, indiceDeRecherche)>-1){
		        //	indiceDeRecherche += 1;
		        //	out.println("cette lettre est dans le mot!!");
		        	
		        //	out.flush();
		       // }
		        
		        char lettre;
		        lettre = reponse.charAt(0);
		        if(motMystere.indexOf(lettre) < 0){
		        	out.println("mauvaise reponse");
		        	out.println(motAffiche);
		        	out.println("Entrez une lettre. Il reste "+ nbrEssais + " essais" );
		        	out.flush();
		        	nbrEssais -= 1;
		        }
		        else{
		        	for(int index = 0;index<nbrLettre;index++){
		        		if(motMystere.charAt(index) == lettre){
		        			char[] motAfficheChar = motAffiche.toCharArray();
		        			motAfficheChar[index] = lettre;
		        			motAffiche = String.valueOf(motAfficheChar);
		        			out.println("bonne reponse");
		        			out.flush();
		        		}
		        	}
		        	out.println(motAffiche);
		        	out.flush();
		        	if(motAffiche.contains("-")){
		        		out.println("Entrez une lettre. Il reste "+ nbrEssais + " essais" );
		        		out.flush();
		        	}
		        }
		     
		        
		        
			}
			System.out.println(nbrEssais);
			if(nbrEssais == -1){
				out.println("Vous avez perdu!!!");
				out.println("\n_________");
				out.println("|       |");
				out.println("|       O");
				out.println("|     --|--");
				out.println("|      / \\");
				out.println("|________________");
				out.flush();
			}
			else{
				out.println("Vous avez gagner!!!");
				out.println("      _");
				out.println("     /(|");
				out.println("    (  :");
				out.println("   __\\  \\  _____");
				out.println(" (____)  `|");
				out.println("(____)|   |");
				out.println(" (____).__|");
				out.println("  (___)__.|_____");
				out.flush();
			}
			
			out.println("Bye xoxo");
			out.flush();
		
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	
	
	}
}
