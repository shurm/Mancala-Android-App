package com.boss.mike4shur.mancalaapp.ui.animation;

import android.animation.Animator;
import android.view.View;
import android.widget.ImageView;

import com.boss.mike4shur.mancalaapp.board.MancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.MancalaPitAndScoreContainer;
import com.boss.mike4shur.mancalaapp.ui.Marble;
import com.boss.mike4shur.mancalaapp.ui.MovementAnimationManager;
import com.boss.mike4shur.mancalaapp.ui.UIMancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.UITools;


import java.util.Stack;


/**
 * Animation which moves a group of marbles/stones around the Mancala Board in a counter clock wise manner
 *
 * @author Michael Shur
 */
public class CounterClockwiseMovementAnimation
{
    private HoppingAnimationSet hoppingAnimationSet;

    private final UIMancalaBoard uiMancalaBoard;
    private final MovementAnimationManager movementAnimationManager;

    /**
     * Instantiates a new Counter clockwise movement animation.
     *
     * @param marbleImagesStack         the marble images stack
     * @param source                    the source
     * @param destination               the destination
     * @param defaultAnimationImageView the default animation image view
     * @param movementAnimationManager  the movement animation manager
     */
    public CounterClockwiseMovementAnimation( Stack<Marble> marbleImagesStack,
                                              final int source, int destination,
                                              ImageView defaultAnimationImageView,
                                              MovementAnimationManager movementAnimationManager)
    {

        this.uiMancalaBoard = movementAnimationManager.getUIBoard();
        this.movementAnimationManager = movementAnimationManager;
        // MancalaPitAndScoreContainer
        HoppingAnimation[] moveMarblesAroundTheBoard = new HoppingAnimation[marbleImagesStack.size()];

        SequentialHoppingAnimationListener singleMoveListener = new SequentialHoppingAnimationListener( source);

        MancalaPitAndScoreContainer sourcePit = movementAnimationManager.getPitContainer(source);

        for (int i =0; i < moveMarblesAroundTheBoard.length;i++)
        {

            Marble stationaryImage = marbleImagesStack.pop();

            MancalaPitAndScoreContainer destinationPit = movementAnimationManager.getPitContainer(destination);

            int[] locationWhereAnimationWillStart = UITools.getCoordinatesOfPit(sourcePit);
            locationWhereAnimationWillStart[0]+=stationaryImage.getxRelativeToPit();
            locationWhereAnimationWillStart[1]+=stationaryImage.getyRelativeToPit();

            int[] locationWhereAnimationWillEnd = movementAnimationManager.getScreenLocationOfPit(destination);

            int x = destinationPit.getRandomXValue(), y = destinationPit.getRandomYValue();

            locationWhereAnimationWillEnd[0] += x;
            locationWhereAnimationWillEnd[1] += y;

            HoppingAnimation marbleAnimation = new HoppingAnimation(locationWhereAnimationWillEnd,
                    locationWhereAnimationWillStart,
                    defaultAnimationImageView);

            marbleAnimation.setAnimationListener(singleMoveListener.new SingleHopListener(stationaryImage.getImageView(), destination, x, y, defaultAnimationImageView));

            moveMarblesAroundTheBoard[i] = marbleAnimation;

            destination = movementAnimationManager.getNextPitLocation(source / UIMancalaBoard.COLUMNS, destination);
        }

        hoppingAnimationSet = HoppingAnimationSet.createSetThatPlaysSequentially(moveMarblesAroundTheBoard);

        hoppingAnimationSet.setAnimationListener(singleMoveListener);

    }

    /**
     * Start the animation.
     */
    public void start()
    {
        hoppingAnimationSet.start();
    }

    private class SequentialHoppingAnimationListener implements Animator.AnimatorListener
    {

        private final MancalaPitAndScoreContainer sourcePit;

        private SequentialHoppingAnimationListener(int source)
        {

            int sourceColumn = source % UIMancalaBoard.COLUMNS - 1;
            int sourceRow = source / UIMancalaBoard.COLUMNS;

            sourcePit = uiMancalaBoard.getPit(sourceRow, sourceColumn);
        }


        @Override
        public void onAnimationStart(Animator animator)
        {}

        @Override
        public void onAnimationEnd(Animator animator)
        {
            //if a capture happened start the capture animations
            if (uiMancalaBoard.getActivity().getBoard().getCaptureThatWasJustPerformed()!=null)
            {
                int capturedColumn = uiMancalaBoard.getActivity().getBoard().getCaptureThatWasJustPerformed();
                if(uiMancalaBoard.getNextTurn()==0)
                    capturedColumn = MancalaBoard.COLUMNS-capturedColumn;
                else
                    capturedColumn--;

                if(!uiMancalaBoard.getPit(uiMancalaBoard.getCurrentTurn(),capturedColumn).getMarblesInPit().isEmpty())
                {
                    CaptureAnimation captureAnimation = new CaptureAnimation(capturedColumn, movementAnimationManager, sourcePit);
                    captureAnimation.start();
                    return;
                }
            }

            movementAnimationManager.afterMarbleAnimation(sourcePit);

        }




        @Override
        public void onAnimationCancel(Animator animator) {}

        @Override
        public void onAnimationRepeat(Animator animator) {}

        /**
         * The type Single hop listener.
         */
        public class SingleHopListener implements Animator.AnimatorListener
        {
            private ImageView stationaryImage;

            private int destination;
            private int futureX, futureY;

            private ImageView movingImage;
            private SingleHopListener(ImageView stationaryImage,
                                     int destination, int futureX, int futureY, ImageView movingImage) {
                this.stationaryImage = stationaryImage;
                this.movingImage=movingImage;
                this.destination = destination;
                this.futureX = futureX;
                this.futureY = futureY;

            }


            @Override
            public void onAnimationStart(Animator animation) {
                stationaryImage.setVisibility(View.INVISIBLE);
                movingImage.setImageBitmap(UITools.convertToBitmap(stationaryImage));
                movingImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                //System.out.println("movingImage is at: "+Arrays.toString(UITools.getCoordinatesOfPit(movingImage)));
                MancalaPitAndScoreContainer destinationPit = movementAnimationManager.getPitContainer(destination);
                destinationPit.addMarble(stationaryImage, futureX, futureY);


                movingImage.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) { movingImage.setLayerType(View.LAYER_TYPE_NONE, null);}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        }
    }


}
