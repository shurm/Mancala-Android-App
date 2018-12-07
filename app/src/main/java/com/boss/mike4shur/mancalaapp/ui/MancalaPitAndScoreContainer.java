package com.boss.mike4shur.mancalaapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boss.mike4shur.mancalaapp.R;

import java.util.Stack;

/**
 * Created by Mike 4 Shur on 2/1/2018.
 */

public class MancalaPitAndScoreContainer extends LinearLayout
{
    private RelativeLayout absoluteLayout;
    private TextView mancalaScoreLabel;

    private Stack<Marble> marblesInPit = new Stack<>();

    private final int pitWidth, pitHeight, marbleSize;

    private final Context context;

    /**
     *
     * @param context
     * @param backgroundImage
     * @param intialMarble
     * @param pitWidth
     * @param pitHeight
     * @param marbleSize
     * @param textSize
     * @param row
     * @param intialMarblesSize
     * @param showFontPadding
     * @param onClickListener
     */
    public MancalaPitAndScoreContainer(Context context, Drawable backgroundImage,
                                       Bitmap intialMarble, int pitWidth, int pitHeight, int marbleSize, int textSize, int row,
                                       int intialMarblesSize, boolean showFontPadding, OnClickListener onClickListener)
    {
        super(context);

        this.context=context;
        this.pitWidth=pitWidth;
        this.pitHeight=pitHeight;
        this.marbleSize=marbleSize;

        this.setOrientation(LinearLayout.VERTICAL);

        absoluteLayout = new RelativeLayout(context);
        absoluteLayout.setBackgroundColor(Color.BLACK);
        absoluteLayout.setBackground(backgroundImage);
        absoluteLayout.setOnClickListener(onClickListener);

        mancalaScoreLabel = new TextView(context);
        mancalaScoreLabel.setGravity(Gravity.CENTER);
        mancalaScoreLabel.setIncludeFontPadding(showFontPadding);
        mancalaScoreLabel.setTextAppearance(context, R.style.fontForScoreLabel);
        mancalaScoreLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        //System.out.println("textSize is "+mancalaScoreLabel.getTextSize());

        //problem here for testing
        if(intialMarblesSize>0)
        {
            final int totalRows = 2;
            final int totalColumns = 2;
            final int padding = 15;
            int startingX = pitWidth/2 - marbleSize-padding/2;
            int startingY = pitHeight/2 - marbleSize-padding/2;

            for(int r = 0;r < totalRows;r++)
            {
                for(int c = 0;c < totalColumns;c++)
                {
                    if(intialMarblesSize<=0)
                        break;
                    ImageView  marbleImageView = new ImageView(context);
                    marbleImageView.setImageBitmap(intialMarble);

                    final int x = startingX + c*(marbleSize+padding);
                    final int y = startingY + r*(marbleSize+padding);

                    addMarble(marbleImageView,x,y);
                    intialMarblesSize--;
                }
            }

            int x = startingX;
            final int y = startingY;
            while(intialMarblesSize>0)
            {
                ImageView  marbleImageView = new ImageView(context);
                marbleImageView.setImageBitmap(intialMarble);
                x+=5;
                addMarble(marbleImageView,x,y);
                intialMarblesSize--;
            }
        }

        if(row==0)
        {
            this.addView(absoluteLayout);
            this.addView(mancalaScoreLabel);
        }
        else
        {
            this.addView(mancalaScoreLabel);
            this.addView(absoluteLayout);
        }


        countAndDisplayMarbles();
    }

    private void countAndDisplayMarbles()
    {
        updateMarbleCountLabel();
    }

    public void updateMarbleCountLabel()
    {
        mancalaScoreLabel.setText(getMarbleCount()+"");
    }

    public void setPaddingTop(int paddingTop)
    {
        setPadding(getPaddingLeft(),paddingTop,getPaddingRight(),getPaddingBottom());
    }

    public void setPaddingBottom(int paddingBottom)
    {
        setPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight(),paddingBottom);
    }

    public boolean isEmpty()
    {
        return marblesInPit.size()==0;
    }

    public int getMarbleCount() {
        return marblesInPit.size();
    }

    public int getRandomXValue()
    {
        int paddingFromEdge = pitWidth*10/100;

        int x = (int)(Math.random()*(pitWidth-marbleSize-2*paddingFromEdge))+paddingFromEdge;
        return x;
    }

    public int getRandomYValue()
    {
        int paddingFromEdge = pitHeight*10/100;

        int y = (int)(Math.random()*(pitHeight-marbleSize-2*paddingFromEdge))+paddingFromEdge;
        return y;
    }

    public void addMarble(ImageView marbleFromOtherPit, int x, int y)
    {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.leftMargin=x;
        layoutParams.topMargin=y;

        Marble marble = new Marble(context, UITools.convertToBitmap(marbleFromOtherPit),x,y);

        absoluteLayout.addView(marble.getImageView(),layoutParams);

        marblesInPit.add(marble);

        countAndDisplayMarbles();
    }

    public void emptyPit()
    {
        while(!marblesInPit.isEmpty())
        {
            Marble imageView = marblesInPit.pop();
            absoluteLayout.removeView(imageView.getImageView());
        }

        countAndDisplayMarbles();
    }

    public void reduceToSize(int desiredSize)
    {
        while(marblesInPit.size()>desiredSize)
        {
            Marble imageView = marblesInPit.pop();
            absoluteLayout.removeView(imageView.getImageView());
        }

        countAndDisplayMarbles();
    }

    public Stack<Marble> getMarblesInPit()
    {
        return marblesInPit;
    }

    public View getPitLayout()
    {
        return absoluteLayout;
    }
}
