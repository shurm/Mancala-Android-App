package com.boss.mike4shur.mancalaapp.ui;

import android.view.View;

/**
 * Created by Mike 4 Shur on 2/1/2018.
 */

public class PitTouchListener implements View.OnClickListener
{

    private int row;
    private int column;

    private UIMancalaBoard uiMancalaBoard;



    public PitTouchListener(UIMancalaBoard uiMancalaBoard, int r , int c)
    {
        this.uiMancalaBoard=uiMancalaBoard;
        row=r;
        column=c;
    }

    @Override
    public void onClick(View view)
    {

       // System.out.println("r : "+row+" , c : "+column );
        //user selects  pit he does not control
        if(row!=uiMancalaBoard.getCurrentTurn())
        {
            //System.out.println("Not your turn error");
            return;
        }

        //only allow aiMove while the game isn't over and while no movement animations are playing and while the ai is not thinking
        if (!uiMancalaBoard.gameIsOver() && !uiMancalaBoard.animationIsPlaying() && !uiMancalaBoard.isAIsTurn())
        {
            MancalaPitAndScoreContainer mancalaPitAndScoreContainer = uiMancalaBoard.getPit(row, column);

            // if the user selects a pit that is empty (its  not a valid aiMove)
            if(mancalaPitAndScoreContainer.isEmpty())
            {
                //Log.v(getClass().getSimpleName(),"Error, an empty pit was clicked.");
                return;
            }

            uiMancalaBoard.moveMarbles(column, false);

            //System.out.println("Animations Started");

        }
        else
        {
            //System.out.println("Game over: "+uiMancalaBoard.gameIsOver());
            //System.out.println("Animation: "+uiMancalaBoard.animationIsPlaying());
            //System.out.println("AI: "+uiMancalaBoard.isAIsTurn());
        }
    }
}