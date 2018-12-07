package com.boss.mike4shur.mancalaapp.ai;

import com.boss.mike4shur.mancalaapp.board.MancalaBoard;

/**
 * Created by Mike on 11/24/2018.
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
            return "\n"+board.toString()+"\nscore: "+score+"\nmove: "+move+"\n";
        }

    public int getMove() {
        return move;
    }
}
