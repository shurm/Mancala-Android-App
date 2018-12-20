package com.boss.mike4shur.mancalaapp.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.boss.mike4shur.mancalaapp.PlayingMancalaActivity;
import com.boss.mike4shur.mancalaapp.R;
import com.boss.mike4shur.mancalaapp.board.MancalaBoard;

import java.util.Stack;

/**
 * The type Ui mancala board.
 */
public class UIMancalaBoard
{

    private final MovementAnimationManager movementAnimationManager;

    /**
     * The constant COLUMNS.
     */
    public static final int COLUMNS = 8;

    /**
     * The Mancala pit and score containers.
     */
    public MancalaPitAndScoreContainer [] [] mancalaPitAndScoreContainers = new MancalaPitAndScoreContainer[MancalaBoard.PLAYERS][6];

    private MancalaPitAndScoreContainer [] mancalas = new MancalaPitAndScoreContainer[MancalaBoard.PLAYERS];

    private final ImageView [] turnLabels = new ImageView[MancalaBoard.PLAYERS];

    private final PlayingMancalaActivity activity;


    /**
     * Instantiates a new Ui mancala board.
     *
     * @param activity   the activity
     * @param gridLayout the grid layout
     */
    public UIMancalaBoard(PlayingMancalaActivity activity, GridLayout gridLayout)
    {
        this.activity=activity;

        int boardWidth = UITools.getScreenWidth(activity);

        int boardHeight = UITools.getScreenHeight(activity)*7/(12);

        int turnLabelHeight = UITools.getScreenHeight(activity)/(11);


        int pitWidth = UITools.getScreenWidth(activity)*2/(19);
        int pitHeight = (UITools.getScreenHeight(activity)*26)/100;
        int paddingBetweenPits = UITools.getScreenHeight(activity)/60;
        int pitScoreSize = UITools.getScreenHeight(activity)/20;
        int mancalaScoreSize = UITools.getScreenHeight(activity)/10;
        int spaceHeightInMiddle = UITools.getScreenHeight(activity)/25;
        int mancalaWidth = (UITools.getScreenWidth(activity) - 7*pitWidth)/2;
        int mancalaHeight = UITools.getScreenHeight(activity)*5/9;
        int marbleSize = pitWidth/3;


        int [] marbleImageIDs = {R.drawable.marble_light_blue,R.drawable.marble_orange,
                R.drawable.marble_light_green,R.drawable.marble_purple,
                R.drawable.marble_red,R.drawable.marble_yellow};

        Bitmap [] marbleImages = new Bitmap[marbleImageIDs.length];

        for(int a =0; a<marbleImageIDs.length; a++)
        {
            marbleImages[a] = ((BitmapDrawable)UITools.createImageDrawable(activity,marbleImageIDs[a],marbleSize,marbleSize)).getBitmap();
        }


        gridLayout.setBackgroundDrawable(UITools.createImageDrawable(activity,R.drawable.board, boardWidth, boardHeight));

        gridLayout.setColumnCount(COLUMNS);
        Drawable mancalaPitImage = UITools.createImageDrawable(activity,R.drawable.pit, pitWidth, pitHeight);
        Drawable mancalaImage = UITools.createImageDrawable(activity,R.drawable.mancala, mancalaWidth, mancalaHeight);

        Drawable divider = UITools.createImageDrawable(activity,R.drawable.divider, 6*(pitWidth+paddingBetweenPits)+paddingBetweenPits, spaceHeightInMiddle);

        for(int r=0;r<gridLayout.getRowCount();r+=2)
        {
            int c;
            for ( c = 1; c < gridLayout.getColumnCount() - 1; c++)
            {
                GridLayout.LayoutParams itemLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
                itemLayoutParams.leftMargin=paddingBetweenPits;

                int index = c-1;
                Bitmap marbleImage = marbleImages[index];

                int marbleCount =  getActivity().getBoard().getPit(r/2,index);


                MancalaPitAndScoreContainer pit = new MancalaPitAndScoreContainer(
                        activity, mancalaPitImage, marbleImage,
                        pitWidth, pitHeight, pitScoreSize,
                        marbleSize, r, marbleCount,
                        true,
                        new PitTouchListener(this,r/2,index));

                gridLayout.addView(pit, itemLayoutParams);
                mancalaPitAndScoreContainers[r/2][index] = pit;

               // System.out.println("marbleCount "+marbleCount);
            }
        }

        GridLayout.LayoutParams itemLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(1), GridLayout.spec(1,6));

        ImageView dividerImageView = new ImageView(activity);
        Bitmap dividerImage = ((BitmapDrawable) divider).getBitmap();
        dividerImageView.setImageBitmap(dividerImage);

        gridLayout.addView(dividerImageView, itemLayoutParams);

        int r =0;
        for (int c = 0; c < gridLayout.getColumnCount(); c+=(gridLayout.getColumnCount()-1))
        {
            itemLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(0, 3), GridLayout.spec(c));

            MancalaPitAndScoreContainer container = new MancalaPitAndScoreContainer(activity ,mancalaImage,null,
                    mancalaWidth,mancalaHeight, marbleSize, mancalaScoreSize, r,0, false, null);

            mancalas[r] = container;
            itemLayoutParams.leftMargin = paddingBetweenPits;

            if(r==0)
            {
                container.setPaddingTop(paddingBetweenPits);
                itemLayoutParams.setGravity(Gravity.TOP);
            }
            else
            {
                container.setPaddingBottom(paddingBetweenPits);
                itemLayoutParams.setGravity(Gravity.BOTTOM);
            }

            gridLayout.addView(container, itemLayoutParams);

           // System.out.println(r+", "+c);
            r++;

        }
        ImageView animation = activity.findViewById(R.id.animationMarble);

        movementAnimationManager = new MovementAnimationManager(this, animation);

        turnLabels[0] = activity.findViewById(R.id.player1_turnLabel);

        turnLabels[1] = activity.findViewById(R.id.player2_turnLabel);

        for(ImageView turnLabel : turnLabels)
        {
            Bitmap turnLabelImage = UITools.drawableToBitmap(turnLabel.getDrawable());

            int scaledWidth = turnLabelImage.getWidth()*turnLabelHeight/turnLabelImage.getHeight();

            turnLabelImage = Bitmap.createScaledBitmap(turnLabelImage, scaledWidth ,turnLabelHeight,false);

            turnLabel.setImageBitmap(turnLabelImage);
        }
        displayTurnLabel();
    }

    /**
     * Gets pit.
     *
     * @param row    the row
     * @param column the column
     * @return the pit
     */
    public MancalaPitAndScoreContainer getPit(int row, int column)
    {
        return mancalaPitAndScoreContainers[row][column];
    }

    /**
     * Gets mancala.
     *
     * @param player the player
     * @return the mancala
     */
    public MancalaPitAndScoreContainer getMancala(int player)
    {
        return mancalas[player];
    }

    /**
     * Game is over boolean.
     *
     * @return the boolean
     */
    public boolean gameIsOver()
    {
        for(MancalaPitAndScoreContainer [] row : mancalaPitAndScoreContainers)
        {
            boolean empty = true;
            for(MancalaPitAndScoreContainer pit : row)
            {
                if(!pit.isEmpty())
                {
                    empty=false;
                    break;
                }
            }
            if(empty)
                return true;
        }
        return false;
    }


    /**
     * Checks if hopping animations are currently playing
     *
     * @return true if any hopping animations are currently playing, false if no animations are currently playing
     */
    public boolean animationIsPlaying()
    {
        return movementAnimationManager.isAnimationPlaying();
    }

    /**
     * Gets movement animation manager.
     *
     * @return the movement animation manager
     */
    public MovementAnimationManager getMovementAnimationManager() {
        return movementAnimationManager;
    }

    /**
     * Move marbles.
     *
     * @param columnSelected the column selected
     * @param ai             the ai
     */
    public void moveMarbles(int columnSelected, boolean ai)
    {
        int playerTurn = getCurrentTurn();
        int correspondingUIColumn = columnSelected;
        if(ai && playerTurn==0)
        {
            correspondingUIColumn = MancalaBoard.COLUMNS-1-columnSelected;
        }

        Stack<Marble> marblesInPit = mancalaPitAndScoreContainers[playerTurn][correspondingUIColumn].getMarblesInPit();

        movementAnimationManager.generateAndStartMarbleAnimations(marblesInPit,playerTurn,correspondingUIColumn);

        MancalaBoard mancalaBoard = getActivity().getBoard();

        if(ai)
            mancalaBoard.aiMove(columnSelected);
        else
            mancalaBoard.clickMove(columnSelected);

       // System.out.println(mancalaBoard.toString());

    }

    /**
     * Is a is turn boolean.
     *
     * @return the boolean
     */
    public boolean isAIsTurn()
    {
        return activity.isAIsTurn();
    }

    /**
     * Has ai boolean.
     *
     * @return the boolean
     */
    public boolean hasAI()
    {
        return activity.hasAI();
    }

    /**
     * Do ai.
     */
    public void doAI()
    {
        activity.doAI();
    }

    /**
     * Gets current turn.
     *
     * @return the current turn
     */
    public int getCurrentTurn() {
        return activity.getBoard().getCurrentTurn();
    }

    /**
     * Gets next turn.
     *
     * @return the next turn
     */
    public int getNextTurn() {
        return activity.getBoard().getNextTurn();
    }


    /**
     * Displays whose current turn it is
     */
    public void displayTurnLabel()
    {
        int currentTurn = getCurrentTurn();

        int otherTurn  = getNextTurn();

        turnLabels[currentTurn].setVisibility(View.VISIBLE);
        turnLabels[otherTurn].setVisibility(View.INVISIBLE);
    }

    /**
     * Sets ais turn to false.
     */
    public void setAisTurnToFalse() {
        activity.setAisTurnToFalse();
    }

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public PlayingMancalaActivity getActivity()
    {
        return activity;
    }
}
