package com.boss.mike4shur.mancalaapp.board;

class IntPointer
{
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public IntPointer(int value) {
        super();
        this.value = value;
    }

    public String toString()
    {
        return value+"";
    }

}