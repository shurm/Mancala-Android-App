package com.boss.mike4shur.mancalaapp.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

import com.boss.mike4shur.mancalaapp.ui.UITools;

public class HoppingAnimation
{
    //how long the the individual hoppping animations will be
    private static final int DURATION = 600;

    AnimatorSet animation = new AnimatorSet();


    public HoppingAnimation(int[] locationWhereAnimationWillEnd, int[] locationWhereAnimationWillStart, ImageView movingImage)
    {

        int [] locationWhereHiddenImageViewIs = UITools.getCoordinatesOfImageView(movingImage);

        //IMPORTANT resolves a bug
        locationWhereHiddenImageViewIs[0]=0;
        locationWhereHiddenImageViewIs[1]=62;

        Animator translateAnimation = createTranslateAnimator(locationWhereAnimationWillEnd, locationWhereHiddenImageViewIs, locationWhereAnimationWillStart, movingImage);
        Animator scaleAnimation = createScaleAnimation(movingImage);

        //animations are now part of the same animationset so they will start at the same time
        animation.playTogether(translateAnimation, scaleAnimation);

        animation.setDuration(DURATION);
    }

    private Animator createTranslateAnimator(int[] locationWhereAnimationWillEnd, int[] locationWhereHiddenImageViewIs, int[] locationWhereAnimationWillStart, ImageView movingImage) {

        int startingX = locationWhereAnimationWillStart[0] - locationWhereHiddenImageViewIs[0];
        int startingY = locationWhereAnimationWillStart[1] - locationWhereHiddenImageViewIs[1];

        int endingX = (locationWhereAnimationWillEnd[0]) - locationWhereHiddenImageViewIs[0];
        int endingY = (locationWhereAnimationWillEnd[1]) - locationWhereHiddenImageViewIs[1];

/*
        System.out.println("startingX:"+startingX);
        System.out.println("endingX:"+endingX);
        System.out.println("startingY:"+startingY);
        System.out.println("endingY:"+endingY);
*/
        Animator animatorX = ObjectAnimator.ofFloat(movingImage, "x", startingX, endingX);
        Animator animatorY = ObjectAnimator.ofFloat(movingImage, "y", startingY, endingY);

        AnimatorSet translateAnimator = new AnimatorSet();
        translateAnimator.playTogether(animatorX, animatorY);

        return translateAnimator;
    }

    private Animator createScaleAnimation(ImageView movingImage) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(movingImage, "scaleX", 1.0f, 1.1f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(movingImage, "scaleY", 1.0f, 1.1f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(scaleX, scaleY);

        return animatorSet;
    }

    public void start() {
        animation.start();
    }


    public void setDuration(long duration) {
        animation.setDuration(duration);
    }

    public void setStartDelay(long delay) {
        animation.setStartDelay(delay);
    }


    public void setAnimationListener(Animator.AnimatorListener listener)
    {
        animation.removeAllListeners();

        animation.addListener(listener);
    }
}