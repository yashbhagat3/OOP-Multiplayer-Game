import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//SINGLETON CLASS MODERATOR
public class Moderator implements Runnable {

	private GameData gameData; //shared data 
	private static Moderator instance = null; //null static Instance
	
	//Private Constructor
	private Moderator (GameData gameData) { 
		this.gameData=gameData;
	}
	
	//Static method to get instance
	static public Moderator getInstance(GameData gameData) {
		if(instance==null)		instance= new Moderator(gameData);
		return instance;
	}
	
	//Moderator running logic
	public void run(){
		
		synchronized(gameData.lock){
			
			PrintStream myout = new PrintStream(new FileOutputStream(FileDescriptor.out));
			
			//starting Moderator
			if(gameData.gameStatus=="NONE"){
				myout.print("Moderator Started\n");
				gameData.gameStatus="INTRO";
				gameData.lock.notifyAll();
			}
			
			//waiting till all players have displayed their tokens
			while(gameData.playersChancesCount!=gameData.playersCount)
			{
				try {
					gameData.lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//setting all scores to zero
			for(int i=0; i<gameData.playersCount;i++) {
				gameData.playerScores[i]=0;	}
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Starting to announce numbers
			try {
				if(gameData.playersChancesCount==gameData.playersCount) {
					System.out.println("Moderater Announcing Numbers");
					gameData.gameStatus="START";	
				}
			} catch (Exception e) {
				myout.print("Exception occured. Moderator could not announce numbers.");;
			}
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//runs until game is finished
			while((gameData.numbersCount < 10)&&(gameData.gameStatus=="START")) {
				
				
				gameData.noAnnouncedFlag = false;
				gameData.playersChancesCount=0;
				
				//setting all players chance flags to false
				for(int i=0; i<gameData.playersCount;i++) {
					gameData.playerChanceFlag[i]=false;	}
						
				
				//generating random number and announcing
				announceNumber();
				
				gameData.lock.notifyAll();
				
				//wait till all players have had their chance
				while(gameData.playersChancesCount!=gameData.playersCount)
				{
					try {
						gameData.lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//Introducing Delay between announcements
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
				
			}
			
			gameData.gameStatus="END";
			for(int i=0; i<gameData.playersCount;i++) 
			{
				myout.println("Player"+i+" scored : "+gameData.playerScores[i]);	}
			
			myout.println("RESULT: "+gameData.winner+" has won.");
			gameData.lock.notifyAll();
		}
	
		
	}
	
	//Function for announcing number
	private void announceNumber() {
		PrintStream myout = new PrintStream(new FileOutputStream(FileDescriptor.out));
		
		try {
			int j =randInt(0,50);
			myout.print((gameData.numbersCount+1)+". Moderator generated : "+j+"\n");
			gameData.list.add(j);
		} catch (Exception e) {
			myout.print("Number could not be generated");;
		}
		myout.flush();
		
		gameData.numbersCount++;
		gameData.noAnnouncedFlag = true;
		
		
	}
	
	//Function to generate random number
	private static int randInt(int min, int max) {	
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}
