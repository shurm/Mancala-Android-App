package com.boss.mike4shur.mancalaapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * The type Marble.
 */
public class Marble
{
    private ImageView marbleImageView;
    private int xRelativeToPit;
    private int yRelativeToPit;


    /**
     * Instantiates a new Marble.
     *
     * @param context        the context
     * @param bitmap         the bitmap
     * @param xRelativeToPit the x relative to pit
     * @param yRelativeToPit the y relative to pit
     */
    public Marble(Context context, Bitmap bitmap, int xRelativeToPit, int yRelativeToPit)
    {
        marbleImageView = new ImageView((context));
        marbleImageView.setImageBitmap(bitmap);

        this.xRelativeToPit = xRelativeToPit;
        this.yRelativeToPit = yRelativeToPit;
    }


    /**
     * Gets image view.
     *
     * @return the image view
     */
    public ImageView getImageView()
    {
        return marbleImageView;
    }

    /**
     * Gets relative to pit.
     *
     * @return the relative to pit
     */
    public int getxRelativeToPit() {
        return xRelativeToPit;
    }

    /**
     * Gets relative to pit.
     *
     * @return the relative to pit
     */
    public int getyRelativeToPit() {
        return yRelativeToPit;
    }

}
