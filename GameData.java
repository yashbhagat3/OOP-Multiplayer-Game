import java.util.*;


//SINGLETON CLASS GAMEDATA
public class GameData {	
	public boolean noAnnouncedFlag = false; //True if a new number has been announced
	public String gameStatus="NONE";  //game status NONE, START, END
	public int numbersCount = 0;  //count of numbers announced till now
	public int playersCount;	//total number of players
	public int playersChancesCount=0;	//total number of players
	public String winner="No one";	//winner name
	
	public Object lock = new Object(); //lock for synchronization
	
	ArrayList<Integer> list= new ArrayList<Integer> ();     //list of numbers declared
	public boolean[] playerChanceFlag;		//flag for players chances
	public int[] playerScores;	//player scores
	
	
	private static GameData instance = null; // static instance
	
	//private constructor
	private GameData(int playersCount)
	{	this.playersCount=playersCount;
		playerChanceFlag = new boolean[playersCount];
		playerScores = new int[playersCount];
		
	}
	
	//getInstance function
	static public GameData getInstance(int playersCount) {
		if(instance==null)		instance= new GameData(playersCount);
		return instance;
	}
	

}
