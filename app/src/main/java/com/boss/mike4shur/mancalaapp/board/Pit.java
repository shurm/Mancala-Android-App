package com.boss.mike4shur.mancalaapp.board;

/**
 * Created by Mike on 8/1/2018.
 */

public class Pit
{
    public int marbleCount;

    private final Integer columnNumber;

    private final int rowNumber;

    public final boolean isMancala;

    public Pit(int initialMarbleCount, int rowNumber, Integer columnNumber)
    {
        this(initialMarbleCount,rowNumber, columnNumber, false);
    }


    public Pit(int initialMarbleCount, int rowNumber, Integer columnNumber, boolean isMancala )
    {
        this.marbleCount = initialMarbleCount;
        this.rowNumber=rowNumber;
        this.columnNumber=columnNumber;
        this.isMancala = isMancala;
    }

    public void incrementMarbleCount()
    {
        marbleCount++;
    }

    public String toString()
    {
        return marbleCount+"";
    }


    public int emptyPit()
    {
        int temp = marbleCount;
        marbleCount = 0;
        return temp;
    }


    public void takeAllFrom(Pit pit)
    {
        marbleCount+=pit.marbleCount;

        pit.emptyPit();
    }

    public boolean isInRow(int r)
    {
        return rowNumber==r;
    }

    public int getPosition() {
        return columnNumber;
    }
}
