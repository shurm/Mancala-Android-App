package com.boss.mike4shur.mancalaapp.ai;

/**
 *
 * @author Michael Shur
 */
public enum AIDifficulty
{

    EASY(1,4), MEDIUM(0,3), HARD(0,1), IMPOSSIBLE(0,0);

    private final int rangeStart, rangeEnd;

    AIDifficulty(int rangeStart, int rangeEnd)
    {
        if (rangeStart > rangeEnd) {
            throw new IllegalArgumentException("rangeEnd must be greater than min");
        }
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    public int getRangeStart() {
        return rangeStart;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

}
