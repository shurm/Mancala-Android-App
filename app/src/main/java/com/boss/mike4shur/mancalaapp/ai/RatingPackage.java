package com.boss.mike4shur.mancalaapp.ai;

import com.boss.mike4shur.mancalaapp.board.MancalaBoard;

/**
 *
 * @author Michael Shur
 */
public class RatingPackage
{
        int score;
        int move;
        MancalaBoard board;
        public RatingPackage(int alphaBetaValue, int move, MancalaBoard board)
        {
            this.score = alphaBetaValue;
            this.move = move;
            this.board = board;
        }

        public String toString()
        {
            if(board == null)
                return "\n null "+"\nscore: "+score+"\nmove: "+move+"\n";
            return "\n"+board.toString()+"\nscore: "+score+"\nmove: "+move+"\n";
        }

    public int getMove() {
        return move;
    }
}
