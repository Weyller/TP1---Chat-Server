import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PartieJoueurTemoin implements Runnable {


	private Socket socket;
	private Conversation conversation;
	private String nomJoueur;

	public PartieJoueurTemoin(Socket socket, Conversation conversation, String nomJoueur){
		this.socket = socket;
		this.conversation = conversation;
		this.nomJoueur = nomJoueur;
	}
	
	@Override
	public void run() {
		PrintWriter out;
	try {
			out = new PrintWriter(socket.getOutputStream());
			out.println("Vous etes le temoin");
			out.flush();
		} 
	catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}

}
