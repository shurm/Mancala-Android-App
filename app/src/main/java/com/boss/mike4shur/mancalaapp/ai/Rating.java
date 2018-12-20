package com.boss.mike4shur.mancalaapp.ai;

import com.boss.mike4shur.mancalaapp.board.MancalaBoard;

import java.util.Comparator;

/**
 *
 * @author Michael Shur
 */
public class Rating
{
    /**
     * The highest score a board may have (signals a victory/defeat)
     */
    public static final int MAX = 1000;

    /**
     * used to sort so the best moves for player 1 are in descending order
     */
    public static final Comparator<RatingPackage> PLAYER_1_COMPARATOR = new Comparator<RatingPackage>() {

        @Override
        public int compare(RatingPackage arg0, RatingPackage arg1) {

            return arg1.score - arg0.score;
        }
    };

    /**
     * used to sort so the best moves for player 2 are in descending order
     */
    public static final Comparator<RatingPackage> PLAYER_2_COMPARATOR = new Comparator<RatingPackage>() {

        @Override
        public int compare(RatingPackage arg0, RatingPackage arg1) {
            return arg0.score - arg1.score;
        }
    };

    /**
     *
     * Calculates a score for the provided MancalaBoard, which indicates
     * how good/bad it is for each player
     * @param mancalaBoard the provided MancalaBoard
     * @param isGameOver true if the game is over, false if otherwise
     * @return returns the score assigned to mancalaBoard
     */
    public static int evaluateBoard(MancalaBoard mancalaBoard, boolean isGameOver)
    {

        int mancalaCountDiff  = mancalaBoard.getMancalaCount(0)-mancalaBoard.getMancalaCount(1);

        //if game is over
        if(isGameOver)
        {
            int sign = (int) Math.signum(mancalaCountDiff);
            return sign * MAX;
        }

        int score = mancalaCountDiff;
        score *= 10;
        if(mancalaBoard.getCurrentTurn() == 0)
            score+=1;
        else
            score-=1;

        return score;

    }
}