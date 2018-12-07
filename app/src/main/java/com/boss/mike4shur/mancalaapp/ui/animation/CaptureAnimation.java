package com.boss.mike4shur.mancalaapp.ui.animation;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boss.mike4shur.mancalaapp.R;
import com.boss.mike4shur.mancalaapp.ui.MancalaPitAndScoreContainer;
import com.boss.mike4shur.mancalaapp.ui.Marble;
import com.boss.mike4shur.mancalaapp.ui.MovementAnimationManager;
import com.boss.mike4shur.mancalaapp.ui.UIMancalaBoard;
import com.boss.mike4shur.mancalaapp.ui.UITools;

import java.util.Stack;

/**
 * Created by Mike on 12/4/2018.
 */

public class CaptureAnimation
{
    private final HoppingAnimationSet hoppingAnimationSet;

    private final MovementAnimationManager movementAnimationManager;

    private final UIMancalaBoard uiMancalaBoard;

    public CaptureAnimation(int capturedColumn, MovementAnimationManager movementAnimationManager,
                            MancalaPitAndScoreContainer sourcePit)
    {
        this.movementAnimationManager = movementAnimationManager;
        this.uiMancalaBoard = movementAnimationManager.getUIBoard();

        MancalaPitAndScoreContainer capturedPit = uiMancalaBoard.getPit(uiMancalaBoard.getCurrentTurn(), capturedColumn);
        MancalaPitAndScoreContainer capturersPit = uiMancalaBoard.getPit(uiMancalaBoard.getNextTurn(), capturedColumn);

        int otherPlayer = uiMancalaBoard.getNextTurn();

        MancalaPitAndScoreContainer mancalaPitOfCapturer = uiMancalaBoard.getMancala(otherPlayer);


        //the frontmost layout, views added to it will appear in front of all of the other views
        ViewGroup frontMostLayout = uiMancalaBoard.getActivity().findViewById(R.id.frontMostLayout);


        Stack<ImageView> temporaryImages = new Stack<>();

        CaptureAnimationListener captureAnimationListener =  new CaptureAnimationListener(temporaryImages,
                frontMostLayout, capturedPit, capturersPit, mancalaPitOfCapturer);

        Stack<Marble> imageViewStack = capturedPit.getMarblesInPit();
        Stack<Marble> stackOfOne = capturersPit.getMarblesInPit();

        HoppingAnimation[] moveMarblesAroundTheBoard = new HoppingAnimation[imageViewStack.size()+1];


        if(stackOfOne.size()!=1)
            throw new RuntimeException("Error, wrong pit");

        for (int i =0; i < moveMarblesAroundTheBoard.length-1;i++)
        {
            moveMarblesAroundTheBoard[i] = computeHoppingAnimation(capturedPit, imageViewStack.pop(), captureAnimationListener, temporaryImages, frontMostLayout, mancalaPitOfCapturer);
        }
        //capture animations

        moveMarblesAroundTheBoard[moveMarblesAroundTheBoard.length-1] = computeHoppingAnimation(capturersPit, stackOfOne.pop(), captureAnimationListener, temporaryImages, frontMostLayout, mancalaPitOfCapturer);

        hoppingAnimationSet = HoppingAnimationSet.createSetThatPlaysTogether(moveMarblesAroundTheBoard);

        hoppingAnimationSet.setAnimationListener(captureAnimationListener);


        sourcePit.emptyPit();
    }

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
                                          MancalaPitAndScoreContainer origin1,
                                          MancalaPitAndScoreContainer origin2,
                                          MancalaPitAndScoreContainer destinationPit)
        {
            this.movingImages = movingImages;
            this.frontMostLayout = frontMostLayout;
            this.destinationPit = destinationPit;

            this.originPits = new MancalaPitAndScoreContainer[]{origin1,origin2};
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

            movementAnimationManager.afterMarbleAnimation(originPits);

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
