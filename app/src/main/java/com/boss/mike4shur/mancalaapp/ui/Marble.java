package com.boss.mike4shur.mancalaapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Mike on 12/3/2018.
 */

public class Marble
{
    private ImageView marbleImageView;
    private int xRelativeToPit;
    private int yRelativeToPit;



    public Marble(Context context, Bitmap bitmap, int xRelativeToPit, int yRelativeToPit)
    {
        marbleImageView = new ImageView((context));
        marbleImageView.setImageBitmap(bitmap);

        this.xRelativeToPit = xRelativeToPit;
        this.yRelativeToPit = yRelativeToPit;
    }


    public ImageView getImageView()
    {
        return marbleImageView;
    }

    public int getxRelativeToPit() {
        return xRelativeToPit;
    }

    public int getyRelativeToPit() {
        return yRelativeToPit;
    }

}
