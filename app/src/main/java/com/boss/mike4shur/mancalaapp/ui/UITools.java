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
 *  UI Library used to convert between different types of image Objects (Drawables, Bitmaps, etc) as well as obtaining
 *  @author Michael Shur
 */
public class UITools
{
    /**
     * Creates an image drawable object using the given imageID, imageWidth and imageHeight .
     *
     * @param activity  the activity
     * @param imageID   the imageID used in the android R library
     * @param imageWidth  the desired width of the image
     * @param imageHeight the desired height of the image
     * @return the new image drawable object
     */
    public static Drawable createImageDrawable(Activity activity, int imageID, int imageWidth, int imageHeight)
    {
        Bitmap image = BitmapFactory.decodeResource(activity.getResources(), imageID);

        image = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);

        return new BitmapDrawable(activity.getResources(), image);
    }

    /**
     * Gets the width of the current device's screen .
     *
     * @param activity the activity
     * @return the screen's width
     */
    public static int getScreenWidth(Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }


    /**
     * Gets the height of the current device's screen.
     *
     * @param activity the activity
     * @return the screen's width
     */
    public static int getScreenHeight(Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    /**
     * Converts an ImageView to a Bitmap object.
     *
     * @param imageView the image view to be converted
     * @return a bitmap which contains the same image as the imageview parameter
     */
    public static Bitmap convertToBitmap(ImageView imageView)
    {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    /**
     * Converts an drawable to a Bitmap object.
     *
     * @param drawable the drawable to be converted
     * @return a bitmap which contains the same image as the drawable parameter
     */
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

    /**
     * Get coordinates of pit int [ ].
     *
     * @param image the image
     * @return the int [ ]
     */
    public static int[] getCoordinatesOfPit(MancalaPitAndScoreContainer image)
    {
        int[] currentCoordinates = new int[2];

        image.getPitLayout().getLocationOnScreen(currentCoordinates);

        return currentCoordinates;
    }

    /**
     * Get coordinates of image view int [ ].
     *
     * @param image the image
     * @return the int [ ]
     */
    public static int[] getCoordinatesOfImageView(ImageView image)
    {
        int[] currentCoordinates = new int[2];

        image.getLocationOnScreen(currentCoordinates);

        return currentCoordinates;
    }
}
