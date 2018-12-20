package com.boss.mike4shur.mancalaapp.ui.animation;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boss.mike4shur.mancalaapp.R;
import com.boss.mike4shur.mancalaapp.board.MancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.MancalaPitAndScoreContainer;
import com.boss.mike4shur.mancalaapp.ui.Marble;
import com.boss.mike4shur.mancalaapp.ui.MovementAnimationManager;
import com.boss.mike4shur.mancalaapp.ui.UIMancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.UITools;


import java.util.Stack;


/**
 * The animation that occurs that the game's conclusion, when one row of pits contains no marbleswhich moves all of the marbles/stones in a particular row to its Mancala Pit Board
 *
 * @author Michael Shur
 */
public class EndgameAnimation
{

    private final MovementAnimationManager movementAnimationManager;

    private final UIMancalaBoard uiMancalaBoard;

    private HoppingAnimationSet hoppingAnimationSet;

    /**
     * Instantiates a new Endgame animation.
     *
     * @param movementAnimationManager the movement animation manager
     */
    public EndgameAnimation(MovementAnimationManager movementAnimationManager)
    {
        this.movementAnimationManager = movementAnimationManager;
        this.uiMancalaBoard = movementAnimationManager.getUIBoard();

        Integer nonEmptyRow = findNonEmptyRow();
        if(nonEmptyRow == null)
        {
            uiMancalaBoard.getActivity().displayWinner();
            return;
        }

        MancalaPitAndScoreContainer mancalaPit = uiMancalaBoard.getMancala(nonEmptyRow);


        //the frontmost layout, views added to it will appear in front of all of the other views
        ViewGroup frontMostLayout = uiMancalaBoard.getActivity().findViewById(R.id.frontMostLayout);


        Stack<ImageView> temporaryImages = new Stack<>();

        MancalaPitAndScoreContainer [] pits = new MancalaPitAndScoreContainer[MancalaBoard.COLUMNS];

        int total = 0;
        for(int col = 0; col<MancalaBoard.COLUMNS; col++)
        {
            MancalaPitAndScoreContainer pit = uiMancalaBoard.getPit(nonEmptyRow,col);
            pits[col] = pit;
            Stack<Marble> tempStack = pit.getMarblesInPit();
            total += tempStack.size();
        }

        CaptureAnimationListener captureAnimationListener =  new CaptureAnimationListener(temporaryImages,
                frontMostLayout, pits, mancalaPit);


        HoppingAnimation[] moveMarblesAroundTheBoard = new HoppingAnimation[total];

        int i = 0;
        for (MancalaPitAndScoreContainer pit : pits)
        {
            Stack<Marble> stack = pit.getMarblesInPit();
            while (!stack.isEmpty()) {
                moveMarblesAroundTheBoard[i] = computeHoppingAnimation(pit, stack.pop(),
                        captureAnimationListener, temporaryImages, frontMostLayout, mancalaPit);
                i++;
            }
        }


        hoppingAnimationSet = HoppingAnimationSet.createSetThatPlaysTogether(moveMarblesAroundTheBoard);

        hoppingAnimationSet.setAnimationListener(captureAnimationListener);


    }

    private Integer findNonEmptyRow()
    {
        for(int rowNumber=0;rowNumber<MancalaBoard.PLAYERS;rowNumber++)
        {
            boolean empty = true;
            for(int col = 0; col<MancalaBoard.COLUMNS; col++)
            {
                MancalaPitAndScoreContainer pit = uiMancalaBoard.getPit(rowNumber,col);
                if(!pit.getMarblesInPit().isEmpty())
                {
                    empty = false;
                    break;
                }
            }
            if(!empty)
                return rowNumber;
        }
        return null;
    }

    /**
     * Start.
     */
    public void start()
    {
        hoppingAnimationSet.start();
    }

    private HoppingAnimation computeHoppingAnimation(MancalaPitAndScoreContainer pit, Marble marble, CaptureAnimationListener captureAnimationListener, Stack<ImageView> temporaryImages, ViewGroup frontMostLayout, MancalaPitAndScoreContainer mancalaPitOfCapturer)
    {
        ImageView imageView = new ImageView(uiMancalaBoard.getActivity());

        temporaryImages.add(imageView);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageView.setLayoutParams(params);

        frontMostLayout.addView(imageView);

        ImageView marbleImage = marble.getImageView();
        imageView.setImageBitmap(UITools.convertToBitmap(marbleImage));

        int[] locationWhereAnimationWillStart = UITools.getCoordinatesOfPit(pit);
        locationWhereAnimationWillStart[0]+=marble.getxRelativeToPit();
        locationWhereAnimationWillStart[1]+=marble.getyRelativeToPit();

        int[] locationWhereAnimationWillEnd = movementAnimationManager.getScreenLocationOfPit(mancalaPitOfCapturer);

        int x = mancalaPitOfCapturer.getRandomXValue(), y = mancalaPitOfCapturer.getRandomYValue();

        locationWhereAnimationWillEnd[0] += x;
        locationWhereAnimationWillEnd[1] += y;

        HoppingAnimation marbleAnimation = new HoppingAnimation(locationWhereAnimationWillEnd, locationWhereAnimationWillStart, imageView);

        Animator.AnimatorListener animatorListener = captureAnimationListener.new SingleHopListener(imageView,marbleImage,x,y);
        marbleAnimation.setAnimationListener(animatorListener);

        return marbleAnimation;
    }

    private class CaptureAnimationListener implements Animator.AnimatorListener
    {
        //the marble images that will animate across the screen
        private final Stack<ImageView> movingImages;

        //the frontmost layout, views added to it will appear in front of all of the other views
        private final ViewGroup frontMostLayout;


        //the mancala pit the marbles will go to
        private final MancalaPitAndScoreContainer destinationPit;

        //the pits whose marbles will be transferred to the destinationPit
        private final MancalaPitAndScoreContainer[] originPits;

        private CaptureAnimationListener( Stack<ImageView> movingImages,
                                          ViewGroup frontMostLayout,
                                          MancalaPitAndScoreContainer []  origins,
                                          MancalaPitAndScoreContainer destinationPit)
        {
            this.movingImages = movingImages;
            this.frontMostLayout = frontMostLayout;
            this.destinationPit = destinationPit;

            this.originPits = origins;
        }

        @Override
        public void onAnimationStart(Animator animator) {}

        @Override
        public void onAnimationEnd(Animator animator) {

            frontMostLayout.post(new Runnable() {
                public void run () {

                    //removes the temporary marble images that were created for the animations
                    //all UI updates must be done on the UI thread
                    uiMancalaBoard.getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            while(!movingImages.isEmpty())
                            {
                                //Log.v("debug","Removing captured Image");
                                ImageView imageToBeRemoved = movingImages.pop();

                                frontMostLayout.removeView(imageToBeRemoved);
                            }
                        }
                    });
                }
            });

            for (MancalaPitAndScoreContainer sourcePit : originPits)
                sourcePit.emptyPit();

            uiMancalaBoard.getActivity().displayWinner();

        }

        @Override
        public void onAnimationCancel(Animator animator) {}

        @Override
        public void onAnimationRepeat(Animator animator) {}




        private class SingleHopListener implements Animator.AnimatorListener
        {

            private ImageView marbleInOriginPit, marbleImageThatAnimates;

            //the position in
            private int futureX, futureY;

            private SingleHopListener(ImageView marbleImageThatAnimates, ImageView marbleInOriginPit,
                                      int futureX, int futureY) {

                this.marbleInOriginPit = marbleInOriginPit;
                this.marbleImageThatAnimates = marbleImageThatAnimates;
                this.futureX = futureX;
                this.futureY = futureY;
            }

            @Override
            public void onAnimationStart(Animator animation)
            {
                marbleInOriginPit.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {

                marbleImageThatAnimates.setVisibility(View.GONE);


                destinationPit.addMarble(marbleInOriginPit, futureX, futureY);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}

        }
    }
}
