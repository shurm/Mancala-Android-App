package com.boss.mike4shur.mancalaapp.board;

public class PitRow
{
    public final Pit [] pits = new Pit [MancalaBoard.COLUMNS] ;

    final Pit mancalaPit;

    final int rowNumber;
    PitRow(int row)
    {
        this.rowNumber = row;
        for(int a = 0 ; a<pits.length; a++)
        {
            pits[a] = new Pit(MancalaBoard.STARTING_MARBLES, row, a);
        }
        mancalaPit = new Pit(0,row, null, true);
    }

    public PitRow(int row, int[] valueOfPits, int mancalaValue)
    {
        this.rowNumber = row;
        for(int a = 0 ; a<pits.length; a++)
        {
            pits[a] = new Pit(valueOfPits[a], row, a);
        }
        mancalaPit = new Pit(mancalaValue, row, null, true);
    }

    /**
     * @return a copy of this PitRow
     */
    public PitRow clone()
    {
        PitRow copy = new PitRow(this.rowNumber);
        copy.mancalaPit.marbleCount = this.mancalaPit.marbleCount;

        for(int a = 0 ; a<pits.length;a++)
            copy.pits[a].marbleCount = pits[a].marbleCount;

        return copy;
    }

    void copyValuesInto(PitRow pitRow)
    {
        this.mancalaPit.marbleCount = pitRow.mancalaPit.marbleCount;

        for(int a = 0 ; a<pits.length;a++)
            pits[a].marbleCount = pitRow.pits[a].marbleCount;
    }

    /**
     *
     * @return true if there are no marbles in any of the pits, false if marbles are in this row
     */
    boolean hasNoMarbles()
    {
        for( Pit pit : pits)
        {
            if(pit.marbleCount > 0)
                return false;
        }
        return true;
    }

    void putPitMarblesIntoMancala()
    {
        for(int a = 0 ; a < pits.length; a++ )
        {
            this.mancalaPit.marbleCount+=this.pits[a].marbleCount;
            this.pits[a].marbleCount=0;
        }
    }

    String toStringAscendingOrder()
    {
        StringBuilder row = new StringBuilder();

        for(int a = 0 ; a < pits.length ; a++)
            row.append(pits[a]+" ");

        return row.toString();
    }

    String toStringDecendingOrder()
    {
        StringBuilder row = new StringBuilder();

        for(int a = pits.length-1 ; a >=0 ; a--)
            row.append(pits[a]+" ");

        return row.toString();
    }

}
