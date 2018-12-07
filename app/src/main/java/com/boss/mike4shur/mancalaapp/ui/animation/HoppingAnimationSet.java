package com.boss.mike4shur.mancalaapp.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;

/**
 * Created by Mike on 3/20/2018.
 */

public class HoppingAnimationSet
{

    private AnimatorSet animation = new AnimatorSet();

    private HoppingAnimationSet()
    {}

    public static HoppingAnimationSet createSetThatPlaysTogether(HoppingAnimation... hoppingAnimations)
    {
        AnimatorSet animatorSet = new AnimatorSet();

        HoppingAnimation first = hoppingAnimations[0];

        AnimatorSet.Builder animatorSetBuilder = animatorSet.play(first.animation);

        for (int  i =1; i<hoppingAnimations.length;i++)
            animatorSetBuilder.with(hoppingAnimations[i].animation);


        HoppingAnimationSet setThatWillPlayTogether = new HoppingAnimationSet();
        setThatWillPlayTogether.animation = animatorSet;

        return setThatWillPlayTogether;
    }

    public static HoppingAnimationSet createSetThatPlaysSequentially(HoppingAnimation... hoppingAnimations)
    {
        AnimatorSet animatorSet = new AnimatorSet();

        HoppingAnimation first = hoppingAnimations[0];

        AnimatorSet.Builder animatorSetBuilder = animatorSet.play(first.animation);

        for (int  i =1; i<hoppingAnimations.length;i++)
        {
            Animator currentAnimator = hoppingAnimations[i].animation;
            animatorSetBuilder.before(currentAnimator);
            animatorSetBuilder=animatorSet.play(currentAnimator);
        }


        HoppingAnimationSet setThatPlaysSequentially = new HoppingAnimationSet();
        setThatPlaysSequentially.animation = animatorSet;

        return setThatPlaysSequentially;
    }

    public void start()
    {
        animation.start();
    }

    public void setAnimationListener(Animator.AnimatorListener listener)
    {
        animation.removeAllListeners();

        animation.addListener(listener);
    }
}
