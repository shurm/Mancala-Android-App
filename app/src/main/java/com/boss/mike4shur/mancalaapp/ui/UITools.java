package com.boss.mike4shur.mancalaapp.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Mike 4 Shur on 2/1/2018.
 */

public class UITools
{
    public static Drawable createImageDrawable(Activity activity, int imageid, int imageWidth, int imageHeight)
    {
        Bitmap image = BitmapFactory.decodeResource(activity.getResources(), imageid);

        image = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);

        return new BitmapDrawable(activity.getResources(), image);
    }

    public static int getScreenWidth(Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    public static Bitmap convertToBitmap(ImageView imageView)
    {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public static Bitmap drawableToBitmap (Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static int[] getCoordinatesOfPit(MancalaPitAndScoreContainer image)
    {
        int[] currentCoordinates=new int[2];

        image.getPitLayout().getLocationOnScreen(currentCoordinates);

        return currentCoordinates;
    }

    public static int[] getCoordinatesOfImageView(ImageView image)
    {
        int[] currentCoordinates=new int[2];

        image.getLocationOnScreen(currentCoordinates);

        return currentCoordinates;
    }
}
