package com.boss.mike4shur.mancalaapp.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.boss.mike4shur.mancalaapp.ui.animation.CounterClockwiseMovementAnimation;
import com.boss.mike4shur.mancalaapp.ui.animation.EndgameAnimation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * The type Movement animation manager.
 * @author Michael Shur
 */
public class MovementAnimationManager
{
    private static final Map<Integer, List<Integer>> specialDirectionalPitsMap = createPitsMap();
    private final Map<Integer, MancalaPitAndScoreContainer> mancalaPitsMap;

    //private final List<Map<MancalaPitAndScoreContainer, MancalaPitAndScoreContainer>> mancalaPitsMap2;


    private final UIMancalaBoard uiMancalaBoard;
    private final AtomicBoolean animationIsPlaying = new AtomicBoolean(false);

    //the imageView that will actually be moving during the animation
    private final ImageView defaultAnimationImageView;


    private int turnAtTheStart;


    /**
     * Instantiates a new Movement animation manager.
     *
     * @param uIMancalaBoard the u i mancala board
     * @param movingImage    the moving image
     */
    MovementAnimationManager(UIMancalaBoard uIMancalaBoard, ImageView movingImage)
    {
        this.uiMancalaBoard = uIMancalaBoard;
        this.defaultAnimationImageView = movingImage;
        this.mancalaPitsMap = createMancalaMap(uIMancalaBoard);
    }
/*
    private static List<Map<MancalaPitAndScoreContainer, MancalaPitAndScoreContainer>> createMancalaMap2(UIMancalaBoard uiMancalaBoard) {

        List<Map<MancalaPitAndScoreContainer, MancalaPitAndScoreContainer>> list = new ArrayList<>();
        for(int player = 0; player<UIMancalaBoard.PLAYERS; player++)
        {
            Map<MancalaPitAndScoreContainer, MancalaPitAndScoreContainer> map = new TreeMap<>();
            for(int)

            list.add(Collections.unmodifiableMap(map));
        }
        Map<MancalaPitAndScoreContainer, MancalaPitAndScoreContainer> mancalaPitsMap2 = new TreeMap<>();

        return Collections.unmodifiableList(list);
    }
*/
    private static Map<Integer, MancalaPitAndScoreContainer> createMancalaMap(UIMancalaBoard uiMancalaBoard) {
        Map<Integer, MancalaPitAndScoreContainer> specialDirectionalPitsMap = new TreeMap<>();
        specialDirectionalPitsMap.put(0, uiMancalaBoard.getMancala(0));
        specialDirectionalPitsMap.put(UIMancalaBoard.COLUMNS - 1, uiMancalaBoard.getMancala(1));

        return Collections.unmodifiableMap(specialDirectionalPitsMap);
    }

    private static Map<Integer, List<Integer>> createPitsMap() {
        Map<Integer, List<Integer>> specialDirectionalPitsMap = new TreeMap<>();
        specialDirectionalPitsMap.put(1, Arrays.asList(0, UIMancalaBoard.COLUMNS + 1));
        specialDirectionalPitsMap.put(2 * UIMancalaBoard.COLUMNS - 2, Arrays.asList(UIMancalaBoard.COLUMNS - 2, UIMancalaBoard.COLUMNS - 1));

        return Collections.unmodifiableMap(specialDirectionalPitsMap);
    }


    /**
     * Generate and start marble animations.
     *
     * @param marbleStack the marble stack
     * @param r           the r
     * @param c           the c
     */
    public void generateAndStartMarbleAnimations(Stack<Marble> marbleStack, int r, int c)
    {
        animationIsPlaying.set(true);
        //defaultAnimationImageView.setVisibility(View.VISIBLE);

        turnAtTheStart = r;
        //System.out.println(r+" , "+c);
        Marble firstMarbleImage = marbleStack.peek();

        int source = r * UIMancalaBoard.COLUMNS + (c + 1);

        int destination = getNextPitLocation(r, source);

        //HoppingAnimationSet movement = createMarbleAnimation(marbleStack, source, destination);

        CounterClockwiseMovementAnimation movement = new CounterClockwiseMovementAnimation(marbleStack, source, destination,defaultAnimationImageView,this);
        // System.out.println(marbleStack.size());
        defaultAnimationImageView.setImageBitmap(UITools.convertToBitmap(firstMarbleImage.getImageView()));
        //movingImage.setImageBitmap(UITools.convertToBitmap(stationaryImage));
        defaultAnimationImageView.setVisibility(View.VISIBLE);

        movement.start();
        //defaultAnimationImageView.startAnimation(movement);
    }

    /**
     * Gets next pit location.
     *
     * @param currentTurn the current turn
     * @param pitLocation the pit location
     * @return the next pit location
     */
    public int getNextPitLocation(int currentTurn, int pitLocation)
    {
        if (specialDirectionalPitsMap.containsKey(pitLocation))
            return specialDirectionalPitsMap.get(pitLocation).get(currentTurn);

        if (pitLocation == 0)
            return UIMancalaBoard.COLUMNS + 1;

        if (pitLocation > UIMancalaBoard.COLUMNS)
            return pitLocation + 1;
        else
            return pitLocation - 1;
    }


    /**
     * Get screen location of pit int [ ].
     *
     * @param pitLocation the pit location
     * @return the int [ ]
     */
    public int[] getScreenLocationOfPit(int pitLocation)
    {
        // System.out.println("pitLocation : "+pitLocation);
        MancalaPitAndScoreContainer mancalaPitAndScoreContainer = getPitContainer(pitLocation);
        return UITools.getCoordinatesOfPit(mancalaPitAndScoreContainer);
    }

    /**
     * Gets pit container.
     *
     * @param pitLocation the pit location
     * @return the pit container
     */
    public MancalaPitAndScoreContainer getPitContainer(int pitLocation)
    {
        MancalaPitAndScoreContainer mancalaPitAndScoreContainer = null;
        if (mancalaPitsMap.containsKey(pitLocation))
            mancalaPitAndScoreContainer = mancalaPitsMap.get(pitLocation);
        if(mancalaPitAndScoreContainer==null)
        {
            int r = pitLocation/UIMancalaBoard.COLUMNS;
            int c = pitLocation%UIMancalaBoard.COLUMNS-1;
            mancalaPitAndScoreContainer = uiMancalaBoard.getPit(r,c);
        }
        return mancalaPitAndScoreContainer;
    }

    /**
     * Is animation playing boolean.
     *
     * @return the boolean
     */
    public boolean isAnimationPlaying()
    {
        return animationIsPlaying.get();
    }


    /**
     * Gets ui board.
     *
     * @return the ui board
     */
    public UIMancalaBoard getUIBoard()
    {
        return uiMancalaBoard;
    }

    /**
     * Get screen location of pit int [ ].
     *
     * @param mancalaPitAndScoreContainer the mancala pit and score container
     * @return the int [ ]
     */
    public int[] getScreenLocationOfPit(MancalaPitAndScoreContainer mancalaPitAndScoreContainer)
    {
        return UITools.getCoordinatesOfPit(mancalaPitAndScoreContainer);
    }

    /**
     * After marble animation.
     *
     * @param sourcePit the source pit
     */
    public void afterMarbleAnimation(MancalaPitAndScoreContainer sourcePit)
    {
        MancalaPitAndScoreContainer[] sourcePits = new MancalaPitAndScoreContainer[]{sourcePit};

        this.afterMarbleAnimation(sourcePits);
    }

    /**
     * After marble animation.
     *
     * @param sourcePits the source pits
     */
    public void afterMarbleAnimation(MancalaPitAndScoreContainer... sourcePits)
    {

        for (MancalaPitAndScoreContainer sourcePit : sourcePits)
            sourcePit.emptyPit();

        animationIsPlaying.set(false);

        if(uiMancalaBoard.getActivity().gameIsOver())
        {
            EndgameAnimation endgameAnimation = new EndgameAnimation(this);
            endgameAnimation.start();

            return;
        }

        if (uiMancalaBoard.hasAI())
        {
            if (uiMancalaBoard.isAIsTurn())
            {
                uiMancalaBoard.doAI();
            }
            else
            {
                uiMancalaBoard.setAisTurnToFalse();
            }
        }
        else
        {
            //System.out.println("doesnt have AI");
        }

        //shows whose current turn it is on the screen
        uiMancalaBoard.displayTurnLabel();

        //prompts the user he can perform another turn
        if (uiMancalaBoard.getCurrentTurn() == turnAtTheStart) {
            Toast.makeText(uiMancalaBoard.getActivity(), "GO AGAIN", Toast.LENGTH_SHORT).show();
        }
    }
}