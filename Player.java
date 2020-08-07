import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;


interface Iterator {
	   public boolean hasNextToken();
	   public Object getNextToken();
	}

interface Container {
	   public Iterator getIterator();
	}

public class Player implements Runnable,Container {
	
	private int playerID;
	private GameData gameData;
	
	ArrayList<Integer> tokens = new ArrayList<Integer>(); //List of Tokens
	
	//Player logic
	public void run(){
		PrintStream myout = new PrintStream(new FileOutputStream(FileDescriptor.out));
		
		synchronized(gameData.lock){
			
			if((gameData.gameStatus=="INTRO")){
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				printTokens();
				gameData.playerChanceFlag[playerID]=true;
				gameData.playersChancesCount++;
				gameData.lock.notify();
			}
			

			//executes till game status is not equal to END
			while((gameData.gameStatus!="END")) 
				{
					try {
						gameData.lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if((gameData.noAnnouncedFlag==true)&&(gameData.playerChanceFlag[playerID]==false)) 
						{
						gameData.playersChancesCount++;
						
						int j=0;
						j= gameData.list.get(gameData.list.size()-1);
						if(tokens.contains(j)==true){
							tokens.remove(new Integer(j));
							gameData.playerScores[playerID]++;
							myout.println("   Matched with Player"+playerID);
							if ((gameData.playerScores[playerID]==3)&&(gameData.gameStatus!="END")) 
								{gameData.gameStatus="END";
								gameData.winner="Player"+playerID;}
						}
						
						//updating player has taken his chance
						gameData.playerChanceFlag[playerID]=true;
						gameData.lock.notifyAll();
								
						}
					
				}//while loop ends
			gameData.lock.notifyAll();
			
			}//synchronized block ends
			
		}//run ends
		
	//Player Constructor
	public Player(GameData gameData, int id){
	
		this.playerID=id;
		this.gameData=gameData;
		
		
		for(int i = 0; i < 10; i++) {
			int p = randInt(0,50);
			tokens.add(p);
		}

	}

	@Override
	public Iterator getIterator() {
	      return new tokenIterator();
	   }
	
	private class tokenIterator implements Iterator {
		int i;//index
	      @Override
	      public boolean hasNextToken() {
	         if(i < tokens.size()){
	            return true;
	         }
	         return false;
	      }

	      @Override
	      public Object getNextToken() {
	         if(this.hasNextToken()){
	            return tokens.get(i++);
	         }
	         return null;
	      }			
	
	}
	
	//Function to generate Random integer
	private static int randInt(int min, int max) {	
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	//Function to print tokens
	private void printTokens() {	
		
		PrintStream myout = new PrintStream(new FileOutputStream(FileDescriptor.out));
		myout.print("Player"+playerID+" tokens are = [ ");
		
		
		for(Iterator iter = this.getIterator(); iter.hasNextToken();){
	         int j = (Integer)iter.getNextToken();
	         myout.print(j+"  ");
			}
		myout.print("] \n");
			}		
	
	}



