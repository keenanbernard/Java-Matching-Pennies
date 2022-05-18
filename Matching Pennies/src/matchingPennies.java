/**
  Name         : matchingPennies
  Description  : The following program implements the game matching pennies
  Amendments   :
   When           What
   ============   =================================================
   08-JULY-2021   Initial Creation
   11-JULY-2021   Updated program with menu options
   13-JULY-2021   Updated program with input validations (playerName & playerAmount)
   ============   =================================================
 */
import javax.swing.*;
import java.util.Random;
import java.text.MessageFormat;
import java.util.regex.*;


public class matchingPennies {
    public static void main (String [] args){
        //declaration of variables
        int menuChoice;
        String [] menu = {"Start Game", "Game Info", "Exit"};

        //do while loop to provide playerMenu (calling methods for starting game or reading game details) and exit option
        do {
            menuChoice = JOptionPane.showOptionDialog(null, "Select Start to play Matching Pennies",
                    "Welcome", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu,menu[0]);
            if (menuChoice == 0) {
                matchingPenniesGame();
            }
            else if (menuChoice == 1) {
                gameInfo();
            }
        } while (menuChoice != 2);//end game menu when exit is selected
    }//end main


    //method use to create the matching pennies game
    public static void matchingPenniesGame(){
        //declaration of variables
        boolean error = true;
        int playersPennies, playersParity, playerChoice, cpuChoice, rounds = 0, roundResult;
        String numeric = "^[a-zA-Z]+", decimal = "^[0-9]\\.[0-9]+", playerAmount = "", playerName, roundWinner, gameResults = "", victor;
        String [] parity = {"Even", "Odd"}, obverseReverse = {"Heads", "Tails"}, roundHistory = new String[2^31-1];
        Pattern numericPattern = Pattern.compile(numeric);//pattern function compiles the variable numeric as new pattern (REGEX)
        Pattern decimalPattern = Pattern.compile(decimal);//pattern function compiles the variable decimal as new pattern (REGEX)


            playerName = JOptionPane.showInputDialog(null,
                    "Welcome to the game Matching Pennies!!!!\n\nEnter your player name."
                    ,"Matching Pennies", JOptionPane.INFORMATION_MESSAGE);

            //input validation for playerName. playerName cannot be empty
            while (playerName == null || playerName.isEmpty()){
             playerName = JOptionPane.showInputDialog(null,
                     "Value entered was empty!!!!\n\nKindly enter your player name."
                     ,"Error: Player name invalid", JOptionPane.ERROR_MESSAGE);
             }

        //input validation for playerAmount. playerAmount can only contain positive integer values
        while(error){
               playerAmount =JOptionPane.showInputDialog(null, "Hi " + playerName
                       + ", how many pennies will you be playing with?", "Matching Pennies", JOptionPane.INFORMATION_MESSAGE);

               Matcher numerical = numericPattern.matcher(playerAmount); //matcher compares numeric regex pattern with playerAmount
               Matcher decimalisation = decimalPattern.matcher(playerAmount); //matcher compares decimal regex pattern with playerAmount

               if (playerAmount.isEmpty()) {
                   JOptionPane.showMessageDialog(null, """
                                    Value entered was empty!!!!

                                    Enter a valid number of pennies"""
                            , "Error: Invalid entry", JOptionPane.ERROR_MESSAGE);
               } else if (numerical.matches() || decimalisation.matches() || Integer.parseInt(playerAmount) < 1) {
                   JOptionPane.showMessageDialog(null, "Enter a valid number of pennies!!!!"
                        ,"Error: Invalid entry", JOptionPane.ERROR_MESSAGE);
               } else if (!numerical.matches() || !decimalisation.matches()) error = false;
        }

        playersPennies = Integer.parseInt(playerAmount);
        int[] playersPot = {playersPennies, playersPennies};

        playersParity = JOptionPane.showOptionDialog(null,
                "What parity will you be playing as " + playerName +"?", "Matching Pennies",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, parity, parity[0]);


        //do while loop handles all functions (round information, choice matching and penny accumulation)
        do {
            playerChoice = JOptionPane.showOptionDialog(null,
                    "Heads or Tails?",
                    "Matching Pennies: Round " + (rounds + 1),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    obverseReverse, obverseReverse[0]);
            cpuChoice = coinObverseReverse();

            //if statement compares both players selection for a match resulting in Even or Odd
            if (playerChoice == cpuChoice) {
                roundResult = 0;
                roundWinner = "Even";
            } else {
                roundResult = 1;
                roundWinner = "Odd";
            }

            //if statement determines penny accumulation using roundResult
            if ((roundResult == playersParity)) {
                roundHistory[rounds] = playerName;
                playersPot[0] -= 1;
                playersPot[1] += 1;
            } else {
                roundHistory[rounds] = "Computer";
                playersPot[0] += 1;
                playersPot[1] -= 1;
            }

            String roundInformation = roundHistory[rounds] + " wins Round " + (rounds + 1)
                    + "\n\n" +playerName +"'s Choice: " + obverseReverse[playerChoice] + "\nComputer's Choice: "
                    + obverseReverse[cpuChoice]
                    + "\n\nResults: " + roundWinner ;
            String playersPotInformation = "Current Pennies in the players' pot:\n\n" + playerName + " has: " + playersPot[1]
                    + "\nComputer has: " + playersPot[0];

            JOptionPane.showMessageDialog(null, roundInformation,
                    "Matching Pennies", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, playersPotInformation ,
                    "Matching Pennies", JOptionPane.INFORMATION_MESSAGE);

            rounds++;
        } while (playersPot[0] > 0 && playersPot[1] > 0);//repeats block until a players runs out of pennies


        //victor decided on the playersPot reaching 0
        if (playersPot[0] == 0) {
            victor = playerName;
        } else victor = "Computer";

        //variable is assigned the final roundWinner
        String team = roundWinner;

        //loop constructs gameResults of each roundHistory
        for (int loop = 0; loop < rounds; loop++) {
            gameResults = MessageFormat.format("{0}Round {1} Winner: {2}\n", gameResults, loop + 1, roundHistory[loop]);
        }

        String gameResultMessage = "GAME RESULTS\n\n" + gameResults + "\n\n"
                + "Victory belongs to: " + victor + " ("+ team + ")";
        String thankYouMessage = "THANK YOU FOR PLAYING!!!";

        JOptionPane.showMessageDialog(null, gameResultMessage, "Matching Pennies",
                JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, thankYouMessage, "Matching Pennies",
                JOptionPane.PLAIN_MESSAGE);
    }//end method


    //method use to return game details
    public static void gameInfo(){
        //declaration of variables
        String gameRules =
                """
                        Matching Pennies is a game played between two players, Even and Odd.
                        Each player has a penny and must secretly turn the penny to heads or tails.
                        The players then reveal their choices simultaneously.If the pennies match (both heads or both tails),
                        then Even keeps both pennies, so wins one from Odd (+1 for Even, -1 for Odd).
                        If the pennies do not  (one heads and one tails) Odd keeps both pennies,
                        so receives one from Even (-1 forEven, +1 for Odd).
                        Taking all of your opponents pennies determines the Victor!""";//detail game information
        JOptionPane.showMessageDialog(null , gameRules, "Game Information", JOptionPane.INFORMATION_MESSAGE);
    }//end method


    //method use to return the Obverse or Reverse of a coin (Heads or Tails)
    public static int coinObverseReverse(){
        //declaration of variables
        Random side = new Random(); //to invoke the random command
        return side.nextInt(2);//return random number
    }//end method
}