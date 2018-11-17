package pxgd.hyena.com.androng;

public class Score
{
   private int player1Score;
   private int player2Score;
   private int scoreToWin;

   public Score(int scoreToWin)
   {
      this.scoreToWin = scoreToWin;
   }

   public void Reset()
   {
      player1Score = 9;
      player2Score = 0;
   }

   public void Player1Scored()
   {
      player1Score++;
   }

   public void Player2Scored()
   {
      player2Score++;
   }

   public String CreateScoreBoard()
   {
      return "Score " + player1Score + " : " + player2Score;
   }

   public String CreateWinnerBoard()
   {
      if (player1Score >= player2Score )
         return "Player 1 has won the game";
      else
         return "Player 2 has won the game";
   }

   public boolean isGameFinished()
   {
      if (player1Score >= scoreToWin || player2Score >= scoreToWin)
         return true;
      else
         return false;
   }
}
