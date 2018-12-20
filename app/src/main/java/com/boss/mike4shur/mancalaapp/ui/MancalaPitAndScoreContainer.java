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
 * The type Mancala pit and score container.
 */
public class MancalaPitAndScoreContainer extends LinearLayout
{
    private RelativeLayout absoluteLayout;
    private TextView mancalaScoreLabel;

    private Stack<Marble> marblesInPit = new Stack<>();

    private final int pitWidth, pitHeight, marbleSize;

    private final Context context;


    /**
     * Instantiates a new Mancala pit and score container.
     *
     * @param context           the context
     * @param backgroundImage   the background image
     * @param intialMarble      the intial marble
     * @param pitWidth          the pit width
     * @param pitHeight         the pit height
     * @param marbleSize        the marble size
     * @param textSize          the text size
     * @param row               the row
     * @param intialMarblesSize the intial marbles size
     * @param showFontPadding   the show font padding
     * @param onClickListener   the on click listener
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

    /**
     * Update marble count label.
     */
    public void updateMarbleCountLabel()
    {
        mancalaScoreLabel.setText(getMarbleCount()+"");
    }

    /**
     * Sets padding top.
     *
     * @param paddingTop the padding top
     */
    public void setPaddingTop(int paddingTop)
    {
        setPadding(getPaddingLeft(),paddingTop,getPaddingRight(),getPaddingBottom());
    }

    /**
     * Sets padding bottom.
     *
     * @param paddingBottom the padding bottom
     */
    public void setPaddingBottom(int paddingBottom)
    {
        setPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight(),paddingBottom);
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty()
    {
        return marblesInPit.size()==0;
    }

    /**
     * Gets marble count.
     *
     * @return the marble count
     */
    public int getMarbleCount() {
        return marblesInPit.size();
    }

    /**
     * Gets random x value.
     *
     * @return the random x value
     */
    public int getRandomXValue()
    {
        int paddingFromEdge = pitWidth*10/100;

        int x = (int)(Math.random()*(pitWidth-marbleSize-2*paddingFromEdge))+paddingFromEdge;
        return x;
    }

    /**
     * Gets random y value.
     *
     * @return the random y value
     */
    public int getRandomYValue()
    {
        int paddingFromEdge = pitHeight*10/100;

        int y = (int)(Math.random()*(pitHeight-marbleSize-2*paddingFromEdge))+paddingFromEdge;
        return y;
    }

    /**
     * Add marble.
     *
     * @param marbleFromOtherPit the marble from other pit
     * @param x                  the x
     * @param y                  the y
     */
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

    /**
     * Empty pit.
     */
    public void emptyPit()
    {
        while(!marblesInPit.isEmpty())
        {
            Marble imageView = marblesInPit.pop();
            absoluteLayout.removeView(imageView.getImageView());
        }

        countAndDisplayMarbles();
    }

    /**
     * Reduce to size.
     *
     * @param desiredSize the desired size
     */
    public void reduceToSize(int desiredSize)
    {
        while(marblesInPit.size()>desiredSize)
        {
            Marble imageView = marblesInPit.pop();
            absoluteLayout.removeView(imageView.getImageView());
        }

        countAndDisplayMarbles();
    }

    /**
     * Gets marbles in pit.
     *
     * @return the marbles in pit
     */
    public Stack<Marble> getMarblesInPit()
    {
        return marblesInPit;
    }

    /**
     * Gets pit layout.
     *
     * @return the pit layout
     */
    public View getPitLayout()
    {
        return absoluteLayout;
    }
}
