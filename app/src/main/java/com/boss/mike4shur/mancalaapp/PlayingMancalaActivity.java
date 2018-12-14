package com.boss.mike4shur.mancalaapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.boss.mike4shur.mancalaapp.ai.AIDifficulty;
import com.boss.mike4shur.mancalaapp.board.MancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.MancalaPitAndScoreContainer;
import com.boss.mike4shur.mancalaapp.ui.UIMancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.UITools;
import com.boss.mike4shur.mancalaapp.ui.animation.EndgameAnimation;

/**
 *
 * @author Michael Shur
 */
public class PlayingMancalaActivity extends AppCompatActivity
{

    private MancalaAIManager mancalaAIManager;

    private MancalaBoard mancalaBoard;

    private UIMancalaBoard uiMancalaBoard;

    private EditText player1Name, player2Name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing_mancala);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mancalaBoard = new MancalaBoard();

        player1Name = findViewById(R.id.player1_name);
        player2Name = findViewById(R.id.player2_name);

        int textSize = UITools.getScreenHeight(this)/12;

        player1Name.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        player2Name.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        final int paddingBottom = 15;
        player1Name.setPadding(0,0,0,paddingBottom);
        player2Name.setPadding(0,0,0,paddingBottom);


        Bundle bundleOfMainMenu = getIntent().getExtras();


        if (bundleOfMainMenu!=null)
        {
            Integer aisTurn = (Integer) bundleOfMainMenu.get("turnOfAI");
            Integer difficultyOfAI = (Integer) bundleOfMainMenu.get("difficultyOfAI");

            if(aisTurn!=null && difficultyOfAI!=null)
            {
                player2Name.setText("Computer");
                player2Name.setEnabled(false);
                if (aisTurn == 0) {
                    String temp = player1Name.getText().toString();
                    player1Name.setText(player2Name.getText());
                    player1Name.setEnabled(false);

                    player2Name.setText(temp);
                    player2Name.setEnabled(true);

                }
                AIDifficulty difficultySelected = AIDifficulty.values()[difficultyOfAI];

                mancalaAIManager = new MancalaAIManager(this, aisTurn, difficultySelected);
            }

        }

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        uiMancalaBoard = new UIMancalaBoard(this, gridLayout);

        if(isAIsTurn())
        {
            doAI();
        }

        //testUsingSpecificPitValue()

        //testEndGameAnimation();
    }

    //Test cases
    private int player1 [] = {0,1,0,2,0,0};
    private int player2 [] = {0,0,0,1,0,0};
    private int player1Mancala = 27;
    private int player2Mancala = 17;
    private int testTurn = 1;
    private void testUsingSpecificPitValue()
    {
        MancalaPitAndScoreContainer [] firstRow = uiMancalaBoard.mancalaPitAndScoreContainers[0];
        for( int i = 0;i<firstRow.length;i++)
        {
            firstRow[i].reduceToSize(player1[i]);
            mancalaBoard.pitsForEachPlayers[0].pits[firstRow.length -1 - i].marbleCount = player1[i];
        }
        MancalaPitAndScoreContainer [] secondRow = uiMancalaBoard.mancalaPitAndScoreContainers[1];
        for( int i = 0;i<firstRow.length;i++)
        {
            secondRow[i].reduceToSize(player2[i]);
            mancalaBoard.pitsForEachPlayers[1].pits[i].marbleCount = player2[i];
        }
        mancalaBoard.setCurrentTurn(testTurn);
        uiMancalaBoard.displayTurnLabel();
    }

    private void testEndGameAnimation()
    {
        Thread t = new Thread()
        {
            public void run()
            {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //
                        EndgameAnimation endgameAnimation = new EndgameAnimation(uiMancalaBoard.getMovementAnimationManager());

                        endgameAnimation.start();
                    }
                });

            }
        };

        t.start();
    }


    /**
     * Checks if this game has an ai player
     *
     * @return true if there is an ai player in this game, false if this game is: human vs human
     */
    public boolean hasAI()
    {
        return mancalaAIManager!=null;
    }


    /**
     * Do ai.
     */
    public void doAI()
    {
        mancalaAIManager.performMove();
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public MancalaBoard getBoard() {
        return mancalaBoard;
    }


    /**
     * Display winner.
     */
    public void displayWinner()
    {

        if(mancalaBoard.getMancalaCount(0)>mancalaBoard.getMancalaCount(1))
        {
            showGameOverPopup(player1Name.getText().toString()+" Wins", player1Name.getCurrentTextColor());
        }
        else if (mancalaBoard.getMancalaCount(0)<mancalaBoard.getMancalaCount(1))
        {
            showGameOverPopup(player2Name.getText().toString()+" Wins",  player2Name.getCurrentTextColor());
        }
        else
        {
            showGameOverPopup("It's a tie!",Color.GRAY);
            //showGameOverPopup(player2Name.getText().toString()+" Wins",  player2Name.getCurrentTextColor());

        }
    }

    /**
     * Displays a Dialog which states the outcome/winner of the game (example: tie, Player1 wins, etc)
     *
     * @param message      string detailing the outcome of the game
     * @param messageColor the color of the message text
     */
    public void showGameOverPopup(String message, int messageColor)
    {
        Dialog myDialog = new Dialog(this);

        int Dialog_WIDTH = UITools.getScreenWidth(this)*8/10;
        int Dialog_HEIGHT = UITools.getScreenHeight(this)*8/10;


        myDialog.setContentView(R.layout.game_outcome_dialog);

        myDialog.getWindow().setLayout(Dialog_WIDTH, Dialog_HEIGHT);

        myDialog.show();

        int textSize = UITools.getScreenWidth(this)/11;


        TextView txtclose = myDialog.findViewById(R.id.winnerLabel);

        txtclose.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        txtclose.setTextColor(messageColor);
        txtclose.setText(message);


    }

    private void createAndShowAlertDialog(String prompt, DialogInterface.OnClickListener yesListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(prompt);
        builder.setPositiveButton(android.R.string.yes, yesListener);

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * if the back button is pressed a dialog will show asking the user if he really wants to exit the current activity
     * and exits the current activity depending on his response
     */
    @Override
    public void onBackPressed()
    {
        createAndShowAlertDialog("Are you sure you want to exit this game?",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
                finish();
            }
        });

    }

    /**
     * Update ui board.
     *
     * @param result the result
     * @param ai     the ai
     */
    public void updateUIBoard(Integer result, boolean ai)
    {
        uiMancalaBoard.moveMarbles(result,ai);
    }


    /**
     * checks if it is currently the computer ai player's turn
     *
     * @return true it is currently the computer's turn, false if it is currently a human player's turn,
     */
    public boolean isAIsTurn() {
        return ( mancalaAIManager!=null && mancalaBoard.getCurrentTurn() == mancalaAIManager.getRowOfAI());
    }

    /**
     * Sets ais turn to false.
     */
    public void setAisTurnToFalse() {
        mancalaAIManager.setAisTurnToFalse();
    }


    /**
     * checks if the game is over
     *
     * @return true if the game is over, false if the game is still being played
     */
    public boolean gameIsOver()
    {
        return mancalaBoard.gameIsOver();
    }
}
