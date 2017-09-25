import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PartieJoueurBourreau implements Runnable {

	private Socket socket;
	private Conversation conversation;
	private String nomJoueur;

	public PartieJoueurBourreau(Socket socket, Conversation conversation, String nomJoueur){
		this.socket = socket;
		this.conversation = conversation;
		this.nomJoueur = nomJoueur;
	}
	
	@Override
	public void run() {
		PrintWriter out;
	try {
			out = new PrintWriter(socket.getOutputStream());
			out.println("Vous etes le bourreau");
			out.flush();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String reponse = in.readLine();
		} 
	catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}

}
