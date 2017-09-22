import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.portable.InputStream;

public class JeuUtilitaire {
	
	
	private TypeJoueur[] TypeJoueurEnum = TypeJoueur.values();
    private String inputDuJoueur;
    private static Scanner scanner = new Scanner(System.in);
    
	//-------------------------
	
    public static String getInputJoueur() {
       
    	System.out.println("Entre un mot ou une lettre: "); 
    	System.out.println(""); 
		String s = scanner.nextLine();
        if (s == null || s.isEmpty()) {
            s = "";
        }
       
        // reset scanner
       //  scanner.nextLine();
       
        
        return s;
    }
   
    
     //------------------------
	
     
     public String GetTypeJoueur() {
    	 
    	String leType;
    	 
    	int rand = (int) (Math.random()*TypeJoueurEnum.length);
         leType = String.valueOf(TypeJoueurEnum[rand]);
         

        
        return leType;
		
	}


	public JeuUtilitaire() {
	 
	}
    //=========================================
	
	public String getDashedWord(String secret){
		
		 String  visible = "";
		 
		 int wordLength = secret.length();
	        
	   	       
	        for ( int i = 0; i < secret.length(); i++)
	        {
	            char c = secret.charAt(i);
	            
	            if ( c == ' ' )
	                visible += c;
	            else
	                visible += '_'+" ";
	        }
			return visible;
		
	}
     
	//=======================================
	
	
	public static String GetMotSecret(List<String> motsDict) {
		
		
		  //motsDict = new ArrayList<>();

	        
	        int sizeW = motsDict.size();
	        
	        int rand;
	        rand = (int) (Math.random()*sizeW);
	        String secret = motsDict.get(rand);
	        
		return secret;
    }
	
	//======================================
	
	public static List<String> LoadMotsDict(String fileName) {
		
		 List<String> motsDict = new ArrayList<>();
		
			//Get file from resources folder
		
			try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					motsDict.add(line);
				}

				scanner.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return motsDict;

		  }
	
	
	
}
	
