/*===========================================================================================================
TIC-TAC-TOE X
Bowen Sam
January 17, 2012
Java, Eclipse SDK 3.4.1
=============================================================================================================
Problem Definition - Required to develop a Tic-Tac-Toe game that determines if anyone has won based on the 
					 positions of the O's and X's entered by user.  Upon victory, the winner earns one point.
					 The program must also show the total points for each player, separate the boxes using a
					 grid, and allow the user to continue playing the game, start with new players or quit.

Input - The user's preference to read How-To-Play via mouse click on JOptionPane
	  - The user's choice of game mode via mouse click on JOptionPane
	  - The names of Player 1 and Player 2 via keyboard input in JOptionPane
	  - The O's and X's entered via mouse click on JButton
	  - The user's preference to continue playing or to start a new game
	  - The user's choice to quit the game or not

Output - Welcome message, read instruction prompt, game selection prompt, players' name prompts, who's first
 		 prompt, user interface (tic-tac-toe grid, backgrounds) (updated each time upon an event), player 1 status 
 		 (player 1's name, player 1's score), player 2 status (player 2's name, player 2's score), 
 		 background, current round indicator, options to continue/start with new players/quit, new high
 		 score prompt (if applicable), high score list, goodbye message

Processing - Use user's mouse click to determine whether to display How-To-Play or not
		   - Use user's mouse click to determine game mode, then set up the Tic-Tac-Toe grid
		   - Use the user keyboard input to determine:
		   		- Player 1's name
		   		- Player 2's name
		   - Set up Player 1's Status and Player 2's Status
		   - Store the O's and X's user inputs by setting the text of the JButton boxes array
		   		- Determine whether it is an O or an X that would be set
		   - Convert these "O"s and "X"s of the array into a "specific value" (1 or -1, respectively)
		   - Calculate whether the "specific values" of each rows, columns, diagonals adds up to a
		     "specific sum" (this value would be based on the O's, X's entered and the current game mode)
		   		- Determine if the following is true:
		   		 	- There is a horizontal win
		   		 	- There is a vertical win
		   		 	- There is a diagonal win
		   		- If none of the above is true, the game continues until there is no more "empty" boxes,
		   		  which would result in a tie
		   - Based on current game mode, increase the winner's score by a certain amount of point(s)
		   - Use user input to determine if they would like to continue playing, start with new players,
		     or quit the game
		     	- If they would like to continue playing, the steps above will be repeated
		     	- If they would like to start with new players or quit the game, the following would occur:
		     		- Adds up both players' scores
		     		- Determine if the high score file exist (or it would be created)
		     		- Convert the file into an array
		     		- Check if their scores are applicable to enter the high score list, which will then
		     		  return a specific value that signify a "new high score situation".  If that value
		     		  returns 0, none of the following would occur. Otherwise:
		     			- Their scores would be added to the high score array, sorted, then the top 20 
		     			  indices of the array would be written back to the file
		     			- A method would then search for player 1/player 2/both (this depends on the case)
		     			  in the top 20 indices of the array for matches, then prompt the user of their new 
		     			  rank(s) in the high score
		       		- If the user chose to start with new players, their scores would be cleared, and asked
		       		  for new names
		       		- If the user chose to quit the game, the Applet would terminate after a UFP message.

=============================================================================================================
List of Identifiers - Local variables and objects will be listed in each methods
 					- Instance variables and instance objects are listed in the following:
 						- let main represent the content pane that would be responsible for showing the UI 
 						  with all the components for the user to interact with (type JPanel)
 						- let defaultFont represent the font with the default font name, font style and font
 						  size; this would be based on the defaults of the font used in JPanel main (type Font)
 						- let rounds represent a counter that keeps track of the number of round that has 
 						  passed (this would be used to determine whether <O> or <X> should be set next and if
 						  the tie condition is met) (type byte)
 						- let fileLocation represent the file path of where the high score file is stored (type String) 
 					- Class variables, class arrays and class objects are listed in the following:
 						- let mode represent user's preference of which game mode he/she would like to play (type byte)
 						- let turn represent a randomly generated number between 0-1 that determine which 
 						  player can go first; this value is also used to determine whether <O> or <X> should 
 						  be set next (type byte)
 						- let boxes represent the Tic-Tac-Toe grid which indicates and "stores" the <O> and 
 						  <X> values (two-dimensional array of JButtons)
 						- let players represent the names of Player 1 and Player 2 (one-dimensional array of Strings)
 						- let scores represent the scores of Player 1 and Player 2 (one-dimensional array of long)
 						- let player1Status represent the status bar shown in the game screen which contains 
 						  Player 1's name and scores (type JLabel)
 						- let player2Status represent the status bar shown in the game screen which contains 
 						  Player 2's name and scores (type JLabel)
 						- let roundIndicator represent a user-friendly indicator which indicates who's turn 
 						  it is (type JLabel)
 						- let background represent the background of the user interface; the background is set
 						  randomly from the twelve image files each time a new game starts (type JLabel)
=============================================================================================================



import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class TicTacToeX extends JApplet implements ActionListener{
	JPanel main = new JPanel();
	Font defaultFont = main.getFont();
	byte rounds = 0;
	String fileLocation = "./USERDATA/GameRecord.txt";
	
	/* A static modifier is required to allow methods to modify these valuables' values depending on the events 
	 * that occurred  
	  (As one cannot send an argument to the actionPerformed method)*/
	static byte mode, turn; //mode - the value needs to be changed in the start() method the new value is needed in actionPerformed method
							//turn - this is incremented every time there is a click  
	static JButton boxes[][]; //boxes - the text on the JButton needs to be changed upon a click
	static String players [] = new String [2]; //players - the value needs to be changed in the start() method and the new value is needed in actionPerformed method
	static long scores [] = {0,0}; //scores - the value is changed by actionPerformed method upon certain mouse click
	static JLabel player1Status = new JLabel("", JLabel.LEFT); //player1Status is modified by actionPerformed method when scores had changed
	static JLabel player2Status = new JLabel("", JLabel.RIGHT); //player2Status is modified by actionPerformed method when scores had changed
	static JLabel roundIndicator = new JLabel(); //roundIndicator needs to be changed every time a button is clicked
	static JLabel background = new JLabel(); //background needs to be assigned/changed with an ImageIcon both the start method and the actionPerformed method

	/** init method
	 * This procedural method initialize the applet and construct the GUI.
	 * The following is performed:
	 *	 	1) Sets the size of the top level container to 800x800
	 *  	2) Sets the size of the JPanel main to 800x800
	 *  	3) Disable layout manager for the JPanel main
	 * @param none
	 * @return void
	 */
	public void init(){
		setSize(800,800);
		main.setSize(800,800);
		main.setLayout(null);
	}

	/** start method
	 * This procedural method performs tasks after initialization of the Applet and calls out various methods.
	 * The methods that could be called by this method are listed in the following:
	 * 		1) welcome method
	 *  	2) readHowToPlayPrompt method
	 *  	3) howToPlay method (if applicable)
	 *  	4) getGameModeSelection method
	 *  	5) getNamesAndPrompt method
	 *  	6) refreshPlayerStatus method
	 *  	7) whosFirst method
	 *  	8) setRandomBackground method
	 *  
	 * It also performs the following:
	 *  	1) Set up the Tic-Tac-Toe grid (composed of JButtons) based on the game mode
	 *  	2) Add an ActionListener to each of the boxes (JButton) on the grid
	 *  	3) Assign each box (JButton) with a certain ActionCommand
	 *  	4) Add the Tic-Tac-Toe grid to the JPanel main
	 *  	5) Set up the player1Status JLabel
	 *  	6) Set up the player2Status JLabel
	 *  	7) Add player1Status and player2Status to the JPanel main
	 *  	8) Set the text on the roundIndicator (JLabel) depending on which player is going first
	 *  	9) Add roundIndicator to the JPanel main
	 *  	10) Set up the background JLabel
	 *  	11) Add the background to the JPanel main
	 *  	12) Set the JPanel main as the content pane (Top-level container) - this shows the GUI to the user 
	 * 
	 * @param none
	 * @return void
	 *  
	 * ===========================================================================================================
	 * List of Identifiers - let tttx represent the object used to call other non-static methods (type Program1)
	 * 					   - let read represent the user's preference to read How-To-Play or not (type boolean)
	 * =========================================================================================================== 
	 */
	public void start(){
		TicTacToeX tttx = new TicTacToeX();
		boolean read;

		tttx.welcome();
		read = tttx.readHowToPlayPrompt();
		if (read == true)
			tttx.howToPlay();
		mode = tttx.getGameModeSelection();

		boxes = new JButton [mode][mode]; //NOTE: The reason for setting the array with this size is explained in the horWin method
		for (int i = 0; i < boxes.length; i++){
			for (int j = 0; j < boxes[i].length; j++){
				boxes[i][j] = new JButton();
				boxes[i][j].setBounds(j*((main.getSize().width-200)/mode)+100, i*((main.getSize().height-200)/mode)+100, (main.getSize().width-200)/mode, (main.getSize().height-200)/mode);
				boxes[i][j].addActionListener(tttx);
				boxes[i][j].setActionCommand(i+" "+j);
				boxes[i][j].setOpaque(false);
				main.add(boxes[i][j]);
			}
		}
		tttx.getNamesAndPrompt();
		tttx.refreshPlayersStatus();
		player1Status.setBounds(30, 0, main.getSize().width/2, 50);
		player2Status.setBounds(main.getSize().width/2-50, 0, main.getSize().width/2, 50);
		player1Status.setFont(new Font(defaultFont.getFontName(), Font.BOLD, 18));
		player2Status.setFont(new Font(defaultFont.getFontName(), Font.BOLD, 18));
		player1Status.setForeground(Color.white);
		player2Status.setForeground(Color.white);
		main.add(player1Status);
		main.add(player2Status);

		tttx.whosFirst(players);
		if (turn % 2 == 0) //O is first
			roundIndicator.setText("O");
		else if (turn % 2 != 0) //X is first
			roundIndicator.setText("X");
		roundIndicator.setBounds(main.getSize().width/2, 0, 50, 50);
		roundIndicator.setForeground(Color.YELLOW);
		roundIndicator.setFont(new Font(defaultFont.getFontName(), Font.BOLD, 40));
		main.add(roundIndicator);

		tttx.setRandomBackground();
		background.setBounds(0, 0, main.getSize().width, main.getSize().height);
		main.add(background);

		setContentPane(main);
	}

	/** actionPerformed method
	 * This procedural method serves as an event handler.
	 * Specific actions and specific methods will be called/performed based on the user's interactions
	 *  
	 * The methods that could be called by this method are listed in the following:
	 * 		1) checkHorWin method
	 * 		2) checkVerWin method
	 *  	3) checkDiagWin method
	 *  	4) lockGrid method
	 *  	5) addScore method
	 *  	6) endGame method
	 *  	7) getContinue method
	 *  	8) flushBox method
	 *  	9) setRandomBackground method
	 *  	10) whosFirst method
	 *  	11) checkFileExistence method
	 *  	12) convertFileToArray method
	 *  	13) checkHighScoreCase method
	 *  	14) addAndSortHighScore method
	 *  	15) locateAndRemoveFlag method
	 *  	16) writeHighScore method
	 *  	17) newHighScorePrompt method
	 *  	18) highScoreListOutput method
	 *  	19) getNamesAndPrompt method
	 *  	20) clearScores method
	 *  	21) refreshPlayersStatus method
	 *  	22) goodBye method 
	 *   
	 *   It also performs the following:
	 *   	1) Respond to mouse click on the Tic-Tac-Toe grid and sets that specific JButton clicked with a <O> or
	 *   	   <X> depending on the current round and who started the game first (indicated by the variables turn and rounds)
	 *   	2) Disable the JButton that was clicked 
	 *   	3) Set the roundIndicator to a new value (<X> or <O>) upon a mouse click on the JButton
	 *   	   *The new value that will be change into depends on the current values of the variables turn and rounds
	 *   	4) Check if there is a win by calling the horWin method, the verWin method, and the diagWin method
	 *   			If there is a win:
	 *   				- The lockGrid method is called
	 *   				- roundIndicator is set to hidden
	 *   				- The addScore method is called
	 *   				- The endGame method is called
	 *   			If there is not a win:
	 *   				- The variable rounds is increased by one
	 *   	5) Check if the tie condition is met (the game resulted in a tie)
	 *   			If there is a tie:
	 *   				- The lockGrid method is called
	 *   				- roundIndicator is set to hidden
	 *   				- The endGame method is called
	 *   	6) Check if the game has ended (regardless of tie or win)
	 *   			If it has ended:
	 *   				- The getContinue method is called
	 *   						Depending on the value getContinue returns, it will lead to one of the following:
	 *   						- (When user wants to continue playing) The flushBox method, the setRandomBackground
	 *   						  method and the whosFirst method will be called.  In addition, rounds will be reset
	 *   						  to 0 and roundIndicator will also be shown with the appropriate indicator again.
	 *   						- (When user wants to start with new players)  The checkFileExistence method, 
	 *   						  the convertFileToArray method, the checkHighScoreCase method, highScoreListPrompt 
	 *   						  method, getNamesPrompt method, clearScores method, refreshPlayersStatus method,
	 *   						  flushBox method, setRandomBackground method, and the whosFirst method is called.
	 *   						  In addition, rounds is reset to 0, and the roundIndicator will be shown with the 
	 *   						  appropriate text again.
	 *   							  - If the value that checkHighScoreCase return is not 0 (it shows that one or
	 *   								both player(s) has scored a score equal or higher than the ones of the high
	 *   								score list.  The addAndSortHighScore method, the locateAndRemoveFlag method 
	 *   								is then called.
	 *   							  - If one of the player has scored in the top 20 of the high score list, the
	 *   						 		writeHighScore method and newHighScorePrompt will be called. Else they would
	 *   							    not be called.
	 *   						- (When user wants to quit) The same steps will occur as when the user chose to 
	 *   						  start with new players, with the exception that some of the methods require to 
	 *   						  setup the game again will not be performed and the goodBye method is called instead.
	 * 
	 * @param event - an event, or an user's interaction (type ActionEvent)
	 * @return void
	 * 
	 * ==================================================================================================================
	 * List of Identifiers - let tttx represent the object used to call other non-static methods (type Program1)
	 * 					   - let newHighScoreCase represent the situation of the new high score (Which player scored
	 * 						 into the high score? How many players scored in the high score? The purpose of this will be 
	 * 						 further explained in the newHighScoreCase method) (type byte)
	 * 
	 * 					   - let horWin represent the condition of which a horizontal win has occurred or not (type byte)
	 * 					   - let verWin represent the condition of which a vertical win has occurred or not (type byte)
	 *  				   - let diagWin represent the condition of which a diagonal win has occurred or not (type byte)
	 *  
	 *  						Note: The reason for not using boolean type is because a numeric value can reflect upon
	 *  							  which player had won, wheras boolean cannot. For example, <1> can signify Player 1
	 *  							  (O) had won, <2> can signify Player 2 (X) had won, <0> can signify nobody had won.
	 *  
	 *  				   - let toContinue represent the user's preference to continue the game, start with new players,
	 *  					 or to quit the game (type int)
	 *  				   - let rank represent the ranks of the two players in the high score list (one-dimensional 
	 *  					 array of int)
	 *  				   - let highScoreRecord represent the high score record retrieved from the file (two-dimensional
	 *  					 array of String)
	 *  
	 *  				   - let flag1 represent a unique identifier used to locate Player 1 in the highScoreRecord array 
	 *  					 when they had been added (type String)
	 *  				   - let flag2 represent a unique identifier used to locate Player 2 in the highScoreRecord array
	 *  					 when they had been added (type String)
	 *  
	 *  						Note: The variable flag1 and flag2 is critical for prompting the players of their ranks
	 *  						      in the high score. As the newHighScorePrompt method works by searching through the 
	 *  							  highScoreRecord array for the players' names.  This can be misleading as Player 1
	 *  							  and Player 2 might have a same name as previous players in the high score list, 
	 *  							  hence might lead to prompting users with a rank which they did not score on.
	 * ==================================================================================================================
	 */
	public void actionPerformed (ActionEvent event){
		TicTacToeX tttx = new TicTacToeX();
		byte newHighScoreCase, horWin = 0, verWin = 0, diagWin = 0;
		int toContinue = 0;
		int ranks [] = new int [2];
		String highScoreRecord [][] = new String [22][2]; //A 22th Location is needed in case of when both players is eligible of entering the high score
		String flag1 = "\tP1"+Math.round(Math.random()*1000), flag2 = "\tP2"+Math.round(Math.random()*1000);

		for (int i = 0; i < mode; i++){
			for (int j = 0; j < mode; j++){
				if (event.getActionCommand().equals(i+" "+j)){
					if (turn == 0){ //O is first
						if (rounds %2 == 0){ //O's turn
							boxes[i][j].setText("O");
							roundIndicator.setText("X");
						}
						else if (rounds %2 != 0){ //X's turn
							boxes[i][j].setText("X");
							roundIndicator.setText("O");
						}
					}					
					else if (turn == 1){ //X is first
						if (rounds %2 == 0){ //X's turn
							boxes[i][j].setText("X");
							roundIndicator.setText("O");
						}
						else if (rounds %2 != 0){// O's turn
							boxes[i][j].setText("O");
							roundIndicator.setText("X");
						}
					}
					boxes[i][j].setFont(new Font(defaultFont.getFontName(), Font.BOLD, 100));
					boxes[i][j].setEnabled(false);

					horWin = tttx.checkHorWin();
					verWin = tttx.checkVerWin();
					diagWin = tttx.checkDiagWin();
					if (horWin != 0 || verWin != 0 || diagWin != 0){ //Win conditions
						tttx.lockGrid();
						roundIndicator.setVisible(false);
						tttx.addScore(horWin, verWin, diagWin);
						tttx.refreshPlayersStatus();
						tttx.endGame(horWin, verWin, diagWin);
					}
				}
			}
		}
		rounds++;
		if (rounds == mode*mode && horWin == 0 && verWin == 0 && diagWin == 0){ //Tie condition
			tttx.lockGrid();
			roundIndicator.setVisible(false);
			tttx.endGame(horWin, verWin, diagWin);
		}
		if (rounds == mode*mode || horWin != 0 || verWin != 0 || diagWin != 0){ //Game has ended (regardless of who won or tied)
			toContinue = tttx.getContinue();
			if (toContinue == 0){
				tttx.flushBox();
				tttx.setRandomBackground();
				rounds = 0;
				tttx.whosFirst(players);
				if (turn % 2 == 0) //O is first
					roundIndicator.setText("O");
				else if (turn % 2 != 0) //X is first
					roundIndicator.setText("X");
				roundIndicator.setVisible(true);
			}
			else if (toContinue != 0){
				while(true){
					try {
						tttx.checkFileExistence();
						highScoreRecord = tttx.convertFileToArray();
						newHighScoreCase = tttx.checkHighScoreCase(highScoreRecord);
						if (newHighScoreCase != 0){
							tttx.addAndSortHighScore(newHighScoreCase, highScoreRecord, flag1, flag2);
							ranks = tttx.locateAndRemoveFlag(highScoreRecord, flag1, flag2);
							if (ranks[0] != 0 || ranks[1] != 0){ //A player must had scored in the high score list to meet this condition  
								tttx.writeHighScore(highScoreRecord);
								tttx.newHighScorePrompt(newHighScoreCase, ranks);
							}
						}
						tttx.highScoreListOutput();
						break;

					}
					catch (IOException e){ //Not possible as this exception is caught in each of the above methods
					}
				}
				if (toContinue == 1){
					tttx.getNamesAndPrompt();
					tttx.clearScores();
					tttx.refreshPlayersStatus();
					tttx.flushBox();
					tttx.setRandomBackground();
					rounds = 0;
					tttx.whosFirst(players);
					if (turn % 2 == 0) //O is first
						roundIndicator.setText("O");
					else if (turn % 2 != 0) //X is first
						roundIndicator.setText("X");
					roundIndicator.setVisible(true);
				}
				else if (toContinue == 2){
					tttx.goodBye();
				}
			}			
		}
	}
	/** welcome method
	 * This procedural method output a message to welcome the user(s) to Tic-Tac-Toe X
	 * 
	 * @param none
	 * @return void
	 */
	private void welcome (){
		JOptionPane.showMessageDialog(null, "=x==o==x==o==x==o==x==o==x==o=  Welcome to Tic-Tac-Toe X  =o==x==o==x==o==x==o==x==o==x=", "Tic-Tac-Toe X", JOptionPane.PLAIN_MESSAGE);
	}

	/** readHowToPlayPrompt method
	 * This functional method asks for the user's preference to read How-To-Play or not.  The respond is then parse to a boolean value
	 * and send back to the calling block.
	 * 
	 * @param none
	 * @return the user's preference to read How-To-Play or not (type boolean)
	 * 
	 * ==================================================================================================================
	 * List of Identifiers - let read represent the user's response to whether he or she would like to read How-To-Play
	 * 						 or not (type int)
	 * ==================================================================================================================
	 */
	private boolean readHowToPlayPrompt(){
		int read; 

		read = JOptionPane.showConfirmDialog(null, "Would you like to read Instructions?", "Tic-Tac-Toe X", JOptionPane.YES_NO_OPTION);
		if (read == 0) 
			return true;
		else
			return false;
	}

	/** howToPlay method
	 * This procedural method the instructions to play Tic-Tac-Toe X to the user.
	 * 
	 * @param none
	 * @void
	 */
	private void howToPlay (){
		JOptionPane.showMessageDialog(null, "\n Playing Tic-Tac-Toe X is simple.\n" +
				"\n Each player takes turn placing X or O in a 3x3, 4x4 or 5x5 grid.\n" +
				" The first player to get three symbols in a row horizontally,\n" +
				" vertically, or diagonally wins.\n" +
				"\n If the grid is filled and no one has won, the game results in a tie.\n" +
				"\n\n Scoring System\n" +
				" 3x3 Win - 1 pt\n" +
				" 4x4 Win - 2 pts \n" +
				" 5x5 Win - 3 pts \n" +
				" Ties - 0 pt\n", "How To Play Tic-Tac-Toe X - Tic-Tac-Toe X", JOptionPane.INFORMATION_MESSAGE, null);
	}

	/** getGameModeSelection method
	 * This functional method prompts the user to choose a game mode to play in.  The user preference is then change to
	 * one of the three following values : 3, 4, or 5 as byte back to the calling block.
	 * 
	 * @param none
	 * @return the user's preference of game mode (type byte)
	 * 
	 * ==================================================================================================================
	 * List of Identifiers - let tttx represent the object used to call other non-static methods (type Program1)
	 * 					   - let options represent the three different game modes that the user can choose 
	 * 						 (one-dimensional array of Object)
	 * 					   - let choice represent the user's preference of game mode (type int)
	 * ==================================================================================================================
	 */
	private byte getGameModeSelection(){
		TicTacToeX tttx = new TicTacToeX();
		Object options[] = {"3x3 Mode", "4x4 Mode", "5x5 Mode"};
		int choice;

		while(true){
			choice = JOptionPane.showOptionDialog(null, "Please select Game Mode", "Game Mode Selection - Tic-Tac-Toe X", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./GRAPHICS/ICONS/3x3.jpg"), options, options[0]);
			if (choice == 0)
				return 3;
			else if (choice == 1)
				return 4;
			else if (choice == 2)
				return 5;
			else
				tttx.goodBye();
		}
	}

	/** getNamesAndPrompt method
	 * This functional method prompts the two users to enter their names. This method ensures a name is always entered 
	 * and also ensures Player 2 do not have the same name as Player 1. The users' names are stored in an array and is
	 * then send back to the calling block. In addition, if <Cancel> or <X> is clicked in the JOptionPane, this program
	 * would call the goodBye method.
	 * 
	 * @param none
	 * @return the two players' names (one-dimensional array of String) 
	 * 
	 * ==================================================================================================================
	 * List of Identifiers - let tttx be the object used to call other non-static methods (type Program1)
	 * ==================================================================================================================
	 */
	private String[] getNamesAndPrompt () {
		TicTacToeX tttx = new TicTacToeX();
		while(true){
			players[0] = JOptionPane.showInputDialog(null, "Who wants to play O? Please tell us your name", "Player 1");
			try{
				if (!players[0].equals(null) && !players[0].equals(""))
					break;
				else
					JOptionPane.showMessageDialog(null, "Sorry, we did not get that. What is your name Player 1? (O)", "Player 1 - Tic-Tac-Toe X", JOptionPane.ERROR_MESSAGE);
			}
			catch (NullPointerException e){
				tttx.goodBye();
			}
		}

		while(true){
			players[1] = JOptionPane.showInputDialog(null, "Who wants to play X? Please tell us your name", "Player 2");
			try{
				if (!players[1].equals(null) && !players[1].equals("")){
					if (players[1].equalsIgnoreCase(players[0]))
						JOptionPane.showConfirmDialog(null, "Sorry, name taken! Please choose another name.", "Player 2 - Tic-Tac-Toe X", JOptionPane.ERROR_MESSAGE);
					else 
						break;
				}
				else
					JOptionPane.showMessageDialog(null, "Sorry, we did not get that. What is your name Player 2? (X)", "Player 2 - Tic-Tac-Toe X", JOptionPane.ERROR_MESSAGE);	
			}
			catch (NullPointerException e){
				tttx.goodBye();
			}
		}
		return players;
	}

	/**	refreshPlayersStatus method
	 * This procedural method refresh the two JLabel, player1Status and player2Status with updated names and scores
	 * 
	 * @param none
	 * @return void
	 */
	private void refreshPlayersStatus (){
		player1Status.setText(players[0]+"   Score: "+scores[0]);
		player2Status.setText(players[1]+"   Score: "+scores[1]);
	}

	/**	whosFirst method
	 * This procedural method determines which player <O> or <X> goes first by using a random number generator, which 
	 * generates either <0> or <1>. <0> signifies Player 1 (O) would go first. <1> signifies Player 2 (X) would go first.
	 * Note: The long value is explicitly casted to a byte type in this method  
	 * 
	 * @param players
	 * @return void
	 */
	private void whosFirst (String players[]){
		turn = (byte)(Math.round(Math.random()));
		if (turn == 0) //Player1 (O)'s turn
			JOptionPane.showMessageDialog(null, players[0]+" (O) will go first!", "Tic-Tac-Toe X", JOptionPane.PLAIN_MESSAGE);
		else if (turn == 1) //Player2 (X)'s turn
			JOptionPane.showMessageDialog(null, players[1]+" (X) will go first!", "Tic-Tac-Toe X", JOptionPane.PLAIN_MESSAGE);
	}

	/** setRandomBackground method
	 * This procedural method randomly sets an image to the JLabel background by using a random number generator, which
	 * will generate a value ranging from 1 to 12.
	 * 
	 * @param none
	 * @return void
	 * 
	 * ==================================================================================================================
	 * List of Identifiers - let chooser be the random number generated by Math.random; the value of this variable will
	 * 						 always be within the range from 1 to 12; this value is used to determine which image to use
	 * 						 as the background (type long) 
	 * ==================================================================================================================
	 */
	private void setRandomBackground(){
		long chooser = Math.round(Math.random()*7)+5;
		background.setIcon(new ImageIcon("./GRAPHICS/BACKGROUNDS/Background "+chooser+".jpg"));
	}

	/* Note: The reason for not storing the sums of each row, column, and diagonal as part of the boxes is due to the fact that the array
	 * is a class array of an object type (type JButton). The extra locations will not be used in the majority of the program, but will
	 * constantly take up system resources.  For 5x5 game mode, one would require extra 11 JButtons and a variable to store the sum of
	 * the back diagonal, making the program very inefficient. Since the aforementioned reason, declaring three local variables in their
	 * respective methods to temporary store the sum for each rows, columns, and diagonals is proven to be more efficient. 	*/

	/** checkHorWin method
	 * This functional method checks if there is a horizontal win in the current grid.  It works by adding rowSum with a specific value 
	 * depending on the text of that specific button (<1> if there is an "O"; <-1> if there is a "X") At the end of each row, the "value
	 * of rowSum" is compared with "the current game mode multiplied by 1 (and -1)" - this value represents the number of boxes in each
	 * row.  If the two matches, then a horizontal win has occurred.  And the value <1> would be returned if it is an "O win" or the 
	 * value <2> would be returned if it is a "X win".  If there is not a horizontal win, the method will return a value of <0>   
	 * 
	 * @param none
	 * @return a value which represents if there is a horizontal win or not; this value also represents who has won (if applicable) 
	 * 		   (type byte)
	 * 
	 * =================================================================================================================================
	 * List of Identifiers - let rowSum represent the temporary storage used to store the sum of a row; the value is reset to 0 upon 
	 * 						 finish checking one row (type byte)
	 * =================================================================================================================================
	 */
	private byte checkHorWin() {
		byte rowSum = 0;

		for (byte i = 0; i < boxes.length; i++){
			for (byte j = 0; j < boxes[i].length; j++){
				if (boxes[i][j].getText().equals("O"))
					rowSum += 1;
				else if (boxes[i][j].getText().equals("X"))
					rowSum += -1;
			}
			if (rowSum == mode*1) //O had won
				return 1; //signifies player1 had won
			else if (rowSum == mode*-1) //X had won
				return 2; //signifies player2 had won
			else
				rowSum = 0;
		}
		return 0;
	}

	/** checkVerWin method
	 * This functional method checks if there is a vertical win in the current grid.  It works by adding colSum with a specific value 
	 * depending on the text of that specific button (<1> if there is an "O"; <-1> if there is a "X") At the end of each column, the
	 * "value of colSum" is compared with "the current game mode multiplied by 1 (and -1)" - this value represents the number of boxes
	 * in each column.  If the two matches, then a vertical win has occurred.  And the value <1> would be returned if it is an "O win"
	 * or the value <2> would be returned if it is a "X win".  If there is not a vertical win, the method will return a value of <0>   
	 * 
	 * @param none
	 * @return a value which represents if there is a vertical win or not; this value also represents who has won (if applicable)
	 * 		  (type byte)
	 * 
	 * =================================================================================================================================
	 * List of Identifiers - let colSum represent the temporary storage used to store the sum of a column; the value is reset to 0 upon
	 * 						 finish checking one column (type byte)
	 * =================================================================================================================================
	 */
	private byte checkVerWin() {
		byte colSum = 0;

		for (byte i = 0; i < boxes.length; i++){
			for (byte j = 0; j < boxes[i].length; j++){
				if (boxes[j][i].getText().equals("O"))
					colSum += 1;
				else if (boxes[j][i].getText().equals("X"))
					colSum += -1;
			}
			if (colSum == mode*1)
				return 1;
			else if (colSum == mode*-1)
				return 2;
			else
				colSum = 0;
		}
		return 0;
	}

	/** checkDiagWin method
	 * This functional method checks if there is a diagonal win in the current grid.  It works by adding diagSum with a specific value 
	 * depending on the text of that specific button (<1> if there is an "O"; <-1> if there is a "X") At the end of each diagonal, the
	 * "value of diagSum" is compared with "the current game mode multiplied by 1 (and -1)" - this value represents the number of boxes
	 * in each diagonal.  If the two matches, then a diagonal win has occurred.  And the value <1> would be returned if it is an "O win"
	 * or the value <2> would be returned if it is a "X win".  If there is not a diagonal win, the method will return a value of <0>   
	 * 
	 * @param none
	 * @return a value which represents if there is a diagonal win or not; this value also represents who has won (if applicable)
	 * 		  (type byte)
	 * 
	 * =================================================================================================================================
	 * List of Identifiers - let diagSum represent the temporary storage used to store the sum of a diagonal; the value is reset to 0 
	 * 						 upon finish checking one diagonal (type byte)
	 * =================================================================================================================================
	 */
	private byte checkDiagWin (){
		byte diagSum = 0;

		for (int i = 0; i < boxes.length; i++){
			if (boxes[i][i].getText().equals("O"))
				diagSum += 1;
			else if (boxes[i][i].getText().equals("X"))
				diagSum += -1;
		}	
		if (diagSum == mode*1)
			return 1;
		else if (diagSum == mode*-1)
			return 2;
		else
			diagSum = 0;

		for (int i = 0; i < boxes.length; i++){
			if (boxes[i][boxes.length-1-i].getText().equals("O"))
				diagSum += 1;
			else if (boxes[i][boxes.length-1-i].getText().equals("X"))
				diagSum += -1;
		}
		if (diagSum == mode*1)
			return 1;
		else if (diagSum == mode*-1)
			return 2;
		return 0;
	}
	
	/** lockGrid method
	 * This procedural method disables all the JButtons of the Tic-Tac-Toe grid
	 * 
	 * @param none
	 * @return void
	 */
	private void lockGrid (){
		for (byte i = 0; i < boxes.length; i++)
			for (byte j = 0; j < boxes[i].length; j++)
				boxes[i][j].setEnabled(false);
	}
	
	/** addScore method
	 * This procedural method increases the player's score by a specific number (depends on game mode) if they had won
	 * 
	 * @param horWin - a value that signifies whether a horizontal win has occurred or not and which player has won (type byte)
	 * @param verWin - a value that signifies whether a vertical win has occurred or not and which player has won (type byte)
	 * @param diagWin - a value that signifies whether a diagonal win has occurred or not and which player has won (type byte)
	 * @return void
	 */
	private void addScore (byte horWin, byte verWin, byte diagWin) {
		if (horWin == 1 || verWin == 1 || diagWin == 1) //Player 1 (O) has won
			scores[0] += mode*1-2;
		else if (horWin == 2 || verWin == 2 || diagWin == 2) //Player 2 (X) has won
			scores[1] += mode*1-2;
	}
	
	/** endGame method
	 * This procedural method prompts the user that game has ended. It then outputs one of the following:
	 * 		1) Player 1 has won
	 * 		2) Player 2 has won
	 * 		3) It is a tie
	 * 
	 * @param horWin - a value that signifies whether a horizontal win has occurred or not and which player has won (type byte)
	 * @param verWin - a value that signifies whether a vertical win has occurred or not and which player has won (type byte)
	 * @param diagWin - a value that signifies whether a diagonal win has occurred or not and which player has won (type byte)
	 * @return void
	 */
	private void endGame(byte horWin, byte verWin, byte diagWin) {
		if (horWin == 1 || verWin == 1 || diagWin == 1)
			JOptionPane.showMessageDialog(null, "Congradulation! "+players[0]+", you won!", "O Victory - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);
		else if (horWin == 2 || verWin == 2 || diagWin == 2)
			JOptionPane.showMessageDialog(null, "Congradulation! "+players[1]+", you won!", "X Victory - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);
		else if (horWin == 0 || verWin == 0 || diagWin == 0)
			JOptionPane.showMessageDialog(null, "Excellent game! Tie!", "Tie - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);	
	}
	
	/** checkFileExistence method
	 * This procedural method serve as a check for file existence by looking for the record file in the location specified by
	 * the String fileLocation. If the file is not found, a new one would be created based on the defaults specified below.
	 * 
	 * @param none
	 * @return void
	 * @throws IOException
	 * 
	 * =======================================================================================================================
	 * List of Identifiers - let test represent an object used to test whether the file exist in the target location or not 
	 * 						 (type BufferedReader)
	 * =======================================================================================================================
	 */
	private void checkFileExistence () throws IOException{
		try {
			@SuppressWarnings("unused")
			BufferedReader test = new BufferedReader(new FileReader(fileLocation));
		}
		catch (FileNotFoundException e){
			PrintWriter test = new PrintWriter(new FileWriter(fileLocation));
			test.println("Alpha\t73");
			test.println("Beta\t71");
			test.println("Gamma\t65");
			test.println("Delta\t58");
			test.println("Epsilon\t52");
			test.println("Zeta\t48");
			test.println("Eta\t42");
			test.println("Theta\t38");
			test.println("Iota\t36");
			test.println("Kappa\t34");
			test.println("Lambda\t32");
			test.println("Mu\t26");
			test.println("Nu\t24");
			test.println("Omega\t22");
			test.println("Omicron\t21");
			test.println("Pi\t20");
			test.println("Rho\t17");
			test.println("Sigma\t12");
			test.println("Tau\t8");
			test.println("Upsilon\t5");
			test.println(" ");
			test.close();
		}
	}
	
	/** convertFileToArray method
	 * This functional method temporary stores the data in the record file to a two-dimensional array (as the file will soon be overridden)
	 * and return it to the calling block.
	 * 
	 * @param none
	 * @return the contents of the record file (the high score list) (2-dimensional array of String)
	 * @throws IOException
	 * 
	 * =====================================================================================================================================
	 * List of Identifiers - let highScoreRecord represent the contents of the record file (2-dimensional array of String)
	 * 					   - let line represent an accumulator that specifies the index of the array for where each line will be stored
	 * 						 in (type byte)
	 * 					   - let file represent an object used to read the record file (type BufferedReader)
	 * 					   - let playerAndScore represent a single line of the file (which contains one player's name and that player's
	 * 						 score) (type String)
	 * =====================================================================================================================================
	 */
	private String[][] convertFileToArray () throws IOException{
		String highScoreRecord [][] = new String [22][2]; 
		byte line = 0;
		BufferedReader file = new BufferedReader(new FileReader(fileLocation));
		String playerAndScore = file.readLine();

		/* Note: " " is used as the condition in the following "while loop" to stop reading the file instead of "null". As Eclipse
		 * throws a NullPointerException when "null" is used as the condition. (This error persisted even after throwing a 
		 * NullPointerException clause)  
		 */
		while (!playerAndScore.equals("")){
			highScoreRecord[line][0] = playerAndScore;
			playerAndScore = file.readLine();
			line++;
		}

		/* The following codes store the substring before the character "\t" (player's name) and the substring after the character "\t" 
		 * (player's score) into two different locations
		 * (Function is to split the string of each location into two substrings and stores them into two different locations)
		 */
		for (int i = 0; i < highScoreRecord.length-2; i++){ //There will only be data for 20 players, so the last 2 location of the array is omitted in this loop
			highScoreRecord[i][1] = highScoreRecord[i][0].substring(highScoreRecord[i][0].indexOf("\t")+1);
			highScoreRecord[i][0] = highScoreRecord[i][0].substring(0,highScoreRecord[i][0].indexOf("\t"));
		}	
		return highScoreRecord;
	}
	
	/** checkHighScoreCase method
	 * This functional method compares the two players' scores with the ones in the highScoreRecord array (high score list)
	 * 		- If player 1's score is higher than the ones in highScoreRecord, then player1High is set to true.
	 * 		- If player 2's score is higher than the ones in highScoreRecord, then player2High is set to true.
	 * Depending on the values of player1High and player2High, a special numeric value will be returned to the calling block.
	 * The possible values and their significances is outlined below:
	 * 		<0> represents the case in which no player has a score higher than the ones in the highscore list
	 * 		<1> represents the case in which Player 1 has a score higher than the ones in the highscore list
	 * 		<2> represents the case in which Player 2 has a score higher than the ones in the highscore list
	 * 		<3> represents the case in which both players have a score higher than the ones in the highscore list
	 * 
	 * Note: These cases does not necessary mean that the player(s) and their score(s) will be entered in the highscore list.
	 *		 Since only the top 20 player is kept, there would be some policies that needs to be established:
	 *		 	- If 1 or 2 player(s) has the SAME score as the lowest score in the highscore list (three players got the 
	 *			  same score), the record would be kept in this order: 
	 *					1) The ones in the record will be kept first
	 *					2) The scores of Player 1 (O)
	 *					3) The scores of Player 2 (X)
	 * 			- After sorting, any player(s) whose rank is lower than 20 will be eliminated from the highscore list. 
	 *
	 * @param highScoreRecord - the current high score list (retrieved from the record file) (two-dimensional array of String)
	 * @return a numeric value that specifies a specific case of "new high score" (type byte)
	 * 
	 * =====================================================================================================================================
	 * List of Identifiers - let player1High represents whether Player 1 has a score higher than the ones in the high score list or not
	 * 					     (type boolean)
	 * 					   - let player2High represents whether Player 2 has a score higher than the ones in the high score list or not
	 * 						 (type boolean)
	 * =====================================================================================================================================
	 */
	private byte checkHighScoreCase (String highScoreRecord[][]){
		boolean player1High = false, player2High = false;

		for (byte i = 0; i < highScoreRecord.length-2; i++){
			if (scores[0] >= Long.parseLong(highScoreRecord[i][1]))				
				player1High = true;
			if (scores[1] >= Long.parseLong(highScoreRecord[i][1])){
				player2High = true;
				break;
			}
		}
		if (player1High == true && player2High == true)
			return 3; //this case signifies that both players has a score higher than the ones in the highscore list
		else if (player1High == true)
			return 1; //this case signifies that player 1 has a score higher than the ones in the highscore list
		else if (player2High == true)
			return 2; //this case signifies that player 2 has a score higher than the ones in the highscore list
		else //player1High and player2High == false
			return 0; //this case signifies that no player has a score higher than the ones in the highscore list
	}
	
	/** addAndSortHighScore method
	 * This functional method adds Player 1 and/or Player 2 (depends on newHighScoreCase) and their respectively ID flag into the 
	 * highScoreRecord array and sort the array in descending order according to the scores of each player.  Modified bubble sort
	 * is used in this sorting method. The general outline of how modified bubble sort works is as follow:
	 * 		1) In pass 1, the computer will compare the value of the current location to the value of the location one
	 * 		   larger to the current location.  If the value of the current location is smaller, the two value will swap.
	 * 		2) Step 1 is repeated for as many times as the data size subtracted by one
	 * 		3) The value at the largest index of the array is now sorted and will be omitted in the next pass
	 * 		4) The next pass will repeat Step 1-3 again
	 *      -  The condition to quit the sort is when swap = false, by default, if a swap did not occur, the value of swap is 
	 * 		   false and the sort will terminate.  However if a swap did occur, the value of swap is changed to true, and the
	 * 		   sort will continue
	 * 
	 * @param newHighScoreCase - a numeric value that specifies a specific case of "new high score" (type byte)
	 * @param highScoreRecord - the current high score list (retrieved from the record file) (two-dimensional array of String) 
	 * @param flag1 - a unique identifier used to locate Player 1 in the highScoreRecord array when they had been added (type String)
	 * @param flag2 - a unique identifier used to locate Player 2 in the highScoreRecord array when they had been added (type String)
	 * @return the sorted highScoreRecord (two-dimensional array of String)
	 * 
	 * ===============================================================================================================================
	 * List of Identifiers - let swap represent a flag which shows if a swap occurred or not (type boolean)
	 * 					   - let tempName represent a temporary storage for a player's name during sorting (type String)
	 * 					   - let tempScore represent a temporary storage for a player's score during sorting (type String)
	 * ===============================================================================================================================
	 */
	private String[][] addAndSortHighScore (byte newHighScoreCase, String highScoreRecord[][], String flag1, String flag2){
		boolean swap = true;
		String tempName, tempScore;

		//Different highscore cases require different way to sort, as they will have more (or less) data accordingly
		if (newHighScoreCase == 1){
			highScoreRecord[20][0] = players[0]+flag1; //Adds the ID to Player1
			highScoreRecord[20][1] = Long.toString(scores[0]);
			highScoreRecord[21][1] = "0"; //This line of code is needed so that the whole array can be sorted, since "null" cannot be sorted
		}
		else if (newHighScoreCase == 2){
			highScoreRecord[20][0] = players[1]+flag2; //Adds the ID to Player2
			highScoreRecord[20][1] = Long.toString(scores[1]);
			highScoreRecord[21][1] = "0"; //This line of code is needed so that the whole array can be sorted, since "null" cannot be sorted
		}
		else if (newHighScoreCase == 3){
			highScoreRecord[20][0] = players[0]+flag1; //Adds the ID to Player1
			highScoreRecord[20][1] = Long.toString(scores[0]);
			highScoreRecord[21][0] = players[1]+flag2; //Adds the ID to Player2
			highScoreRecord[21][1] = Long.toString(scores[1]);
		}
		while (swap){
			swap = false;
			for (int i = 0; i < highScoreRecord.length-1; i++){
				if (Long.parseLong(highScoreRecord[i][1]) < Long.parseLong(highScoreRecord[i+1][1])){
					tempName = highScoreRecord[i+1][0];
					tempScore = highScoreRecord[i+1][1];
					highScoreRecord[i+1][0] = highScoreRecord[i][0];
					highScoreRecord[i+1][1] = highScoreRecord[i][1];
					highScoreRecord[i][0] = tempName;
					highScoreRecord[i][1] = tempScore;
					swap = true;
				}
			}
		}
		return highScoreRecord;
	}
	
	/** locateAndRemoveFlag method
	 * This functional method locates that ID of Player 1 and Player 2 in the highScoreRecord array which had been previously added.
	 * By finding the ID, the method can locate the ranks that Player 1 and Player 2 had scored on in the high score (if applicable)
	 * (If one of the player did not score in the high score list, a default value of "0" will be assigned into the array). After
	 * storing the ranks in a separate array (called ranks), this method will remove the two unique ID in the highScoreRecord
	 * array. In order to prevent the two ID from being written into the record file later.
	 * 
	 * For explanation on the purpose of these ID, please refer to the actionPerformed method, where these variables are declared.
	 * 
	 * @param highScoreRecord - the current high score list (two-dimensional array of String) 
	 * @param flag1 - a unique identifier used to locate Player 1 in the highScoreRecord array when they had been added (type String)
	 * @param flag2 - a unique identifier used to locate Player 2 in the highScoreRecord array when they had been added (type String)
	 * @return the rank of the two players in the high score list (one-dimensional array of int)
	 * 
	 * ===============================================================================================================================
	 * List of Identifiers - let ranks represent the ranks of the two players in the high score list (one-dimensional array of int)
	 * ===============================================================================================================================
	 */
	private int[] locateAndRemoveFlag (String highScoreRecord [][], String flag1, String flag2){
		int ranks [] = new int [2];

		for (int i = 0; i < 20; i++){
			if (highScoreRecord[i][0].indexOf(flag1) != -1){
				ranks[0] = i+1;
				highScoreRecord[i][0] = highScoreRecord[i][0].substring(0, highScoreRecord[i][0].indexOf(flag1)); //removes the flag
			}
			if (highScoreRecord[i][0].indexOf(flag2) != -1){
				ranks[1] = i+1;
				highScoreRecord[i][0] = highScoreRecord[i][0].substring(0, highScoreRecord[i][0].indexOf(flag2)); //removes the flag
			}
		}
		return ranks;
	}
	
	/** writeHighScore method
	 * This procedural method writes the highScoreRecord array into the record file located at where fileLocation specified.
	 * 
	 * @param highScoreRecord - the current high score list (two-dimensional array of String)
	 * @return void
	 * @throws IOException
	 * 
	 * ===============================================================================================================================
	 * List of Identifiers - let gameRecord represent the object used to write data to a file (type PrintWriter)
	 * ===============================================================================================================================
	 */
	private void writeHighScore (String highScoreRecord[][]) throws IOException {
		PrintWriter gameRecord = new PrintWriter (new FileWriter(fileLocation));

		for (byte i = 0; i < highScoreRecord.length-2; i++) //this will only allow the top 20 players to be written to the GameRecord file
			gameRecord.println(highScoreRecord[i][0]+"\t"+highScoreRecord[i][1]);
		gameRecord.print(" "); //sets up the condition to quit the "while" loop next time this file is read
		gameRecord.close();
	}
	
	/** newHighScorePrompt method
	 * This procedural method prompts the eligible user(s) of their new rank(s) in the high score list, which is determined by the 
	 * value of the newHighScoreCase variable.
	 * 
	 * @param newHighScoreCase - a numeric value that specifies a specific case of "new high score" (type byte)
	 * @param ranks - contains the ranks of the players in the highscore list (one-dimensional array of int) 
	 * @return void
	 */
	private void newHighScorePrompt (byte newHighScoreCase, int ranks []){
		if (newHighScoreCase == 1){
			JOptionPane.showMessageDialog(null, "Congradulation "+players[0]+"! You scored in the highscore list with a rank of "+ranks[0]+"!!", "New High Score - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);
		}
		else if (newHighScoreCase == 2){
			JOptionPane.showMessageDialog(null, "Congradulation "+players[1]+"! You scored in the highscore list with a rank of "+ranks[1]+"!!", "New High Score - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);
		}
		else if (newHighScoreCase == 3){
			JOptionPane.showMessageDialog(null, "Congradulation "+players[0]+"! You scored in the highscore list with a rank of "+ranks[0]+"!!", "New High Score - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);
			if (ranks[1] != 0) //Prevents output of "rank 0"
				JOptionPane.showMessageDialog(null, "Congradulation "+players[1]+"! You scored in the highscore list with a rank of "+ranks[1]+"!!", "New High Score - Tic-Tac-Toe X", JOptionPane.WARNING_MESSAGE);	
		}
	}
	
	/** highScoreListOutput
	 * This procedural method outputs the current high score list to the user. The list is retrieved from the record file.
	 * 
	 * @param none
	 * @return void
	 * @throws IOException
	 * 
	 * ===============================================================================================================================
	 * List of Identifiers - let record represent an object used to read the record file (type BufferedReader)
	 * 					   - let tempText represent a temporary storage for the contents of the record file that had been formatted
	 * 					     (type String)
	 * 					   - let text represent the interface containing formatted high score list (type JTextArea)
	 * ===============================================================================================================================
	 */
	private void highScoreListOutput () throws IOException {
		BufferedReader record = new BufferedReader(new FileReader(fileLocation));
		String tempText = "";
		JTextArea text = new JTextArea();

		for (byte i = 0; i < 20 ; i++){
			tempText += record.readLine();
			tempText += "\n";
		}
		text.setText(tempText);
		text.setEditable(false);
		JOptionPane.showMessageDialog(null, text, "High Scores - Tic-Tac-Toe X", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
	/** getContinue method
	 * This functional method asks the user what they would like to do. The user is given the following three options:
	 * 		1) Continue (Player names and their respective scores will be kept and the game will continue)
	 * 		2) New Game (Player names and their respective scores will be erased and the game will start with new players)
	 * 				- If the user chose this option, they will be asked for confirmation
	 * 		3) Quit (Player names and their respective scores will be erased and the game will quit) 
	 * 		* If the user click <X>, they will be asked to click <Quit> instead
	 * 
	 * @param none
	 * @return a numeric value which signifies the user's choice to continue, start a new game, or quit (type int)
	 * 
	 * ====================================================================================================================
	 * List of Identifiers - let options represent the choices that the user can choose (one-dimensional array of String)
	 * 					   - let choice represent the choice that the user chose (type int)
	 * 					   - let confirmation represent the user's confirmation (type int)
	 * ====================================================================================================================
	 */
	private int getContinue (){
		String options [] = {"Continue", "New Game", "Quit"};
		int choice, confirmation;

		while (true){
			choice = JOptionPane.showOptionDialog(null, "Would you like to continue playing?", "Continue? - Tic-Tac-Toe X", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (choice == 0 || choice == 2)
				return choice;
			else if (choice == 1){
				confirmation = JOptionPane.showConfirmDialog(null, "Both players' scores will be erased, are you sure about this?", "Tic-Tac-Toe X", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (confirmation == 0)
					return choice;
			}
			else
				JOptionPane.showMessageDialog(null, "If you would like to quit, please click <Quit>.", "Tic-Tac-Toe X", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** flushBox method
	 * This procedural method clear out all the <X> and <O> set on the JButtons. All JButtons will also be re-enabled.
	 * 
	 * @param none
	 * @return void
	 */
	private void flushBox (){
		for (byte i = 0; i < boxes.length; i++){
			for (byte j = 0; j < boxes[i].length; j++){
				boxes[i][j].setText("");
				boxes[i][j].setEnabled(true);
			}
		}
	}

	/** clearScores method
	 * This procedural method clears the score of Player 1 and the score of Player 2
	 * 
	 * @param none
	 * @return void
	 */
	private void clearScores (){
		scores[0] = 0;
		scores[1] = 0;
	}
	
	/** goodBye method
	 * This procedural method outputs a user-friendly message that informs the user that the game will quit
	 * 
	 * @param none
	 * @return void
	 */
	private void goodBye (){
		JOptionPane.showMessageDialog(null, "=x==o==x==o==x==o==x==o==x==o=  Thanks For Playing Tic-Tac-Toe X !  =o==x==o==x==o==x==o==x==o==x=", "Tic-Tac-Toe X", JOptionPane.PLAIN_MESSAGE);
		System.exit(0);
	}
}






