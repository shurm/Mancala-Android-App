package com.boss.mike4shur.mancalaapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.boss.mike4shur.mancalaapp.ui.UITools;

/**
 *
 * @author Michael Shur
 */
public class MainActivity extends AppCompatActivity
{

    private int aiProperties_Dialog_WIDTH;
    private int aiProperties_Dialog_HEIGHT;

    private Dialog aiPropertiesPopUp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        aiProperties_Dialog_WIDTH = UITools.getScreenWidth(this)*8/10;
        aiProperties_Dialog_HEIGHT = UITools.getScreenHeight(this)*8/10;

        //final int MENU_TITLE_WIDTH = UITools.getScreenWidth(this)*5/7;

        final int MENU_TITLE_HEIGHT =  UITools.getScreenHeight(this)*1/7;
        final int BUTTON_HEIGHT =( UITools.getScreenHeight(this) - MENU_TITLE_HEIGHT )/5;

        ViewGroup buttonPanel = findViewById(R.id.buttonPanel);


        int buttonWidth = 0;
        for(int a = 0 ; a<buttonPanel.getChildCount();a++ )
        {
            View button = buttonPanel.getChildAt(a);

            if(button instanceof ImageView)
            {
                ImageView imageViewButton = (ImageView)button;
                Bitmap buttonImage = UITools.drawableToBitmap(imageViewButton.getDrawable());

                double buttonWidthFactor = ((double) BUTTON_HEIGHT) / buttonImage.getHeight();

                buttonWidth = (int) (buttonImage.getWidth() * buttonWidthFactor);
                buttonImage = Bitmap.createScaledBitmap(buttonImage, buttonWidth, BUTTON_HEIGHT, true);

                imageViewButton.setImageBitmap(buttonImage);
            }
        }


        Drawable background = buttonPanel.getBackground();

        Bitmap properlyScaledBackgoundImage = Bitmap.createScaledBitmap(UITools.drawableToBitmap(background),buttonWidth*9/8,BUTTON_HEIGHT*buttonPanel.getChildCount(),true);

        buttonPanel.setBackground(new BitmapDrawable(getResources(),properlyScaledBackgoundImage));


        buttonPanel = findViewById(R.id.left);

        for(int a = 0 ; a<buttonPanel.getChildCount();a++ )
        {
            ImageView button = (ImageView)buttonPanel.getChildAt(a);

            Bitmap buttonImage = UITools.drawableToBitmap(button.getDrawable());

            double buttonWidthFactor = ((double)BUTTON_HEIGHT)/buttonImage.getHeight();

            buttonWidth = (int)(buttonImage.getWidth()*buttonWidthFactor);
            buttonImage = Bitmap.createScaledBitmap(buttonImage,buttonWidth,BUTTON_HEIGHT,true);

            button.setImageBitmap(buttonImage);
        }


        buttonPanel = findViewById(R.id.right);

        for(int a = 0 ; a<buttonPanel.getChildCount();a++ )
        {
            ImageView button = (ImageView)buttonPanel.getChildAt(a);

            Bitmap buttonImage = UITools.drawableToBitmap(button.getDrawable());

            double buttonWidthFactor = ((double)BUTTON_HEIGHT)/buttonImage.getHeight();

            buttonWidth = (int)(buttonImage.getWidth()*buttonWidthFactor);
            buttonImage = Bitmap.createScaledBitmap(buttonImage,buttonWidth,BUTTON_HEIGHT,true);

            button.setImageBitmap(buttonImage);
        }




    }


    /**
     * If Vs Computer was clicked.
     *
     * @param v the button that was clicked
     */

    public void vsComputerClicked(View v)
    {
        createAndShowAITurnDialog(null);
    }

    //Displays the Input Dialog for obtaining the properties of the ai player (i.e. its turn and difficultly)
    private void createAndShowAITurnDialog(final Boolean testing)
    {
        aiPropertiesPopUp = new Dialog(MainActivity.this);

        aiPropertiesPopUp.setContentView(R.layout.ai_properties_input_dialog);

        aiPropertiesPopUp.getWindow().setLayout(aiProperties_Dialog_WIDTH, aiProperties_Dialog_HEIGHT);

        aiPropertiesPopUp.show();
    }

    /**
     * Starts the MancalaPlayingActivity and passes the properties obtaining from the ai properties dialog
     *
     * @param v the button that was clicked
     */
    public void startVsComputerActivity(View v)
    {
        RadioGroup turn_options = (RadioGroup) aiPropertiesPopUp.findViewById(R.id.turn_options);

        RadioGroup difficulty_options = (RadioGroup) aiPropertiesPopUp.findViewById(R.id.difficulty_options);

        View radioButton;
        int turnOfHuman = turn_options.getCheckedRadioButtonId();

        if(turnOfHuman == -1)
        {
            Toast.makeText(this,"Please select a turn", Toast.LENGTH_SHORT).show();
            return;
        }

        radioButton = turn_options.findViewById(turnOfHuman);
        turnOfHuman = turn_options.indexOfChild(radioButton);

        int difficulty = difficulty_options.getCheckedRadioButtonId();
        if(difficulty == -1)
        {
            Toast.makeText(this,"Please select a difficulty", Toast.LENGTH_SHORT).show();
            return;
        }

        radioButton = difficulty_options.findViewById(difficulty);
        difficulty = difficulty_options.indexOfChild(radioButton);


        //close the dialog
        aiPropertiesPopUp.dismiss();

        int aisTurn = (turnOfHuman+1)%2;
        startAIActivity(aisTurn, difficulty);

    }

    private void startAIActivity(int turnOfAI, int aiDifficulty)
    {
        Intent intent = new Intent(this, PlayingMancalaActivity.class);
        intent.putExtra("turnOfAI", turnOfAI);
        intent.putExtra("difficultyOfAI", aiDifficulty);
        startActivity(intent);
    }

    /**
     * If Pass N Play was clicked.
     *
     * @param v the button that was clicked
     */
    public void passNPlayClicked(View v)
    {
        Intent intent = new Intent(this, PlayingMancalaActivity.class);
        startActivity(intent);
    }


    /**
     * If how to play was clicked.
     *
     * @param v the button that was clicked
     */
    public void howToPlayClicked(View v)
    {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }

    /*public void testingButtonClicked(View v)
    {
        createAndShowAITurnDialog(true);
    }
*/
}
