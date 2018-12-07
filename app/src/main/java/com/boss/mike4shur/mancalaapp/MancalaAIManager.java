package com.boss.mike4shur.mancalaapp;


import android.os.AsyncTask;
import android.util.Log;

import com.boss.mike4shur.mancalaapp.ai.AIDifficulty;
import com.boss.mike4shur.mancalaapp.ai.MancalaAI;
import com.boss.mike4shur.mancalaapp.ai.RatingPackage;
import com.boss.mike4shur.mancalaapp.board.MancalaBoard;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class which acts as a intermediary between the Front End and AI Logic
 *
 * @author Michael Shur
 */
public class MancalaAIManager
{

    //the front end which interacts with the user and displays the chessboard
    private PlayingMancalaActivity playingChessActivity;

    private static int aiTurns = 1;

    //the task which prompts the opponent variable from the next aiMove the computer player should make
    private AIBackgroundTask task;

    //determines if the computer player is currently making a aiMove
    private AtomicBoolean aisTurn = new AtomicBoolean(false);

    Integer getAiRow() {
        return aiRow;
    }

    //the row which the pits the ai controls belong to
    private Integer aiRow;

    private MancalaAI mancalaAI;


    MancalaAIManager(PlayingMancalaActivity playingChessActivity, Integer aiRow, AIDifficulty aiDifficulty)
    {
        this.playingChessActivity = playingChessActivity;
        this.aiRow = aiRow;
        this.mancalaAI = new MancalaAI(aiDifficulty);
    }


    public void reset()
    {
        //cancels AI's aiMove if AI is trying to make a aiMove
        cancelAIsMoveIfPossible();
        aisTurn.set(false);
    }

    public boolean isAIMakingAMove()
    {
        return aisTurn.get();
    }

    public void performMove()
    {
        aisTurn.set(true);

        task = new AIBackgroundTask();

        task.execute();
    }


    public boolean isCancelled()
    {
        if(task==null)
            return true;
        return task.isCancelled();
    }

    public void finishMove()
    {
        aisTurn.set(false);
    }


    /*
     *  Enables the AI logic to properly affect the UI
     */
    private class AIBackgroundTask extends AsyncTask<Void,Object,Integer>
    {


        @Override
        protected Integer doInBackground(Void[] args)
        {
            MancalaBoard mancalaBoard = playingChessActivity.getBoard();
            //System.out.println("ai has gone "+aiTurns);

            Integer move = mancalaAI.computeMove(mancalaBoard);

            //System.out.println(ratedPossibleMoves.toString());
            return move;
        }

        /*
               when AI logic is complete affect UI accordingly
         */
        @Override
        protected void onPostExecute(Integer result)
        {
            super.onPostExecute(result);

            //ai cant make any moves

            if(result==null)
            {
                aisTurn.set(false);
                playingChessActivity.displayWinner();

                //Log.v("tag",playingChessActivity.getBoard().toString());

                throw new RuntimeException("Error result is null");
            }

            //Log.v(getClass().getName(), "ai went "+result);
            playingChessActivity.updateUIBoard(result,true);

            aiTurns++;

        }
    }


  public void setAisTurnToFalse()
    {
        aisTurn.set(false);
    }



    /**
     * cancels AI's aiMove if AI is trying to make a aiMove
     * @return true if the AI was forced to stop, false if was not currently the AI's turn
     */
    public boolean cancelAIsMoveIfPossible()
    {
        if(aisTurn.get())
        {
            task.cancel(true);
            aisTurn.set(false);
            return true;
        }
        return false;
    }


}
