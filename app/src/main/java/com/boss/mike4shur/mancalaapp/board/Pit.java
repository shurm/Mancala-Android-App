package com.boss.mike4shur.mancalaapp.board;

/**
 *
 * @author Michael Shur
 */
public class Pit
{
    /**
     * the number osf marbles/stones currently in this pit
     */
    int marbleCount;

    /**
     * the column this pit is in
     */
    private final Integer columnNumber;

    /**
     * the row this pit is in
     */
    private final int row;

    /**
     * boolean indicating whether this pit is a mancala.
     */
    public final boolean isMancala;

    /**
     * Instantiates a new Pit.
     *
     * @param initialMarbleCount the initial marble count
     * @param row    the row this pit is in
     * @param columnNumber   the column this pit is in
     */
    public Pit(int initialMarbleCount, int row, Integer columnNumber)
    {
        this(initialMarbleCount, row, columnNumber, false);
    }


    /**
     * Instantiates a new Pit.
     *
     * @param initialMarbleCount the initial marble count
     * @param row  the row this pit is in
     * @param columnNumber   the column this pit is in
     * @param isMancala    boolean indicating whether or not this pit is a mancala.
     */
    public Pit(int initialMarbleCount, int row, Integer columnNumber, boolean isMancala )
    {
        this.marbleCount = initialMarbleCount;
        this.row = row;
        this.columnNumber=columnNumber;
        this.isMancala = isMancala;
    }

    /**
     * Increments the number of marbles/stones in this pit.
     */
    public void incrementMarbleCount()
    {
        marbleCount++;
    }

    public String toString()
    {
        return marbleCount+"";
    }


    /**
     * Empty pit int.
     *
     * @return the number of marbles/stones that were in this pit
     */
    public int emptyPit()
    {
        int temp = marbleCount;
        marbleCount = 0;
        return temp;
    }


    /**
     * Adds all the marbles/stones from the given pit to this pit object and then empties the given pit
     *
     * @param pit the whose marbles/stones will be taken and deposited into this pit
     */
    public void takeAllFrom(Pit pit)
    {
        marbleCount+=pit.marbleCount;

        pit.emptyPit();
    }

    /**
     * Check if this Pit is in the given row
     *
     * @param rowNumber the row to be check
     * @return true if this pit is located in rowNumber
     */
    public boolean isInRow(int rowNumber)
    {
        return row == rowNumber;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return columnNumber;
    }


    /**
     * Sets the number of marbles/stones in this pit.
     *
     * @param marbleCount the number of marbles/stones that will be in this pit
     */
    public void setMarbleCount(int marbleCount) {
        this.marbleCount = marbleCount;
    }
}
