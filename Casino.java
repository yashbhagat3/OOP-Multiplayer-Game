import java.io.*;
import java.util.*;

public class Casino {

	public static void main(String[] args) {
		
		int playersCount=3; //NUMBER OF PLAYERS
		
		PrintStream myout = new PrintStream(new FileOutputStream(FileDescriptor.out));
		
		//creating player threads
		final Player[] player=new Player[playersCount];
		Thread playerThread[]= new Thread[playersCount];
		
		//constructing player threads
		for(int i=0; i<playersCount; i++) {
		player[i] = new Player(GameData.getInstance(playersCount),i);
		playerThread[i] = new Thread(player[i]);}

		
		//moderator thread
		Thread modThread  = new Thread(Moderator.getInstance(GameData.getInstance(playersCount)));
		try{modThread.start();
		}
		catch(Exception e) {
			myout.print("Moderator thread could not be started.");
		}
		//starting all player threads
		for(int i=0; i<playersCount; i++) {
			try{playerThread[i].start();
			}
			catch(Exception e) {
				myout.print("Player thread could not be started.");
			}
		}
		
	}

}

