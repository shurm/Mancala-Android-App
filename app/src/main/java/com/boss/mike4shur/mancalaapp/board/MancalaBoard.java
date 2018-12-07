package com.boss.mike4shur.mancalaapp.board;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MancalaBoard
{
        public static final int PLAYERS = 2;

        public static final int COLUMNS = 6;

        public static final int STARTING_MARBLES = 4;

        public final PitRow [] pitsForEachPlayers = new PitRow[PLAYERS];

        private Integer captureThatWasJustPerformed = null;

        private AtomicInteger currentTurn = new AtomicInteger(0);

        private MancalaBoard previousBoard = null;


        public MancalaBoard()
        {
            for(int r = 0 ; r<pitsForEachPlayers.length; r++)
            {
                pitsForEachPlayers[r] = new PitRow(r);
            }

        }

        public MancalaBoard(int[] player1Pits, int[] player2Pits, int player1Mancala, int player2Mancala)
        {
            reverseArray(player1Pits);

            pitsForEachPlayers[0] = new PitRow(0, player1Pits , player1Mancala);

            pitsForEachPlayers[1] = new PitRow(1,player2Pits, player2Mancala);

        }

        private void reverseArray(int [] validData)
        {
            for(int i = 0; i < validData.length / 2; i++)
            {
                int temp = validData[i];
                validData[i] = validData[validData.length - i - 1];
                validData[validData.length - i - 1] = temp;
            }
        }
        public int getCurrentTurn()
        {
            return currentTurn.get();
        }

        public int getNextTurn()
        {
            return getNextRow(currentTurn.get());
        }


        /**
         * @param playerNumber the desired player
         *
         * @return the number of marbles currently in the spcified player's mancala pit
         */
        public int getMancalaCount(int playerNumber)
        {
            return pitsForEachPlayers[playerNumber].mancalaPit.marbleCount;
        }

        public void clickMove(Integer move)
        {
            previousBoard = this.clone();

            if(currentTurn.get()==0)
                move = COLUMNS-1-move;
            aiMove(move);

            //System.out.println(toString());

            //System.out.println("turn "+ currentTurn.get());
        }

        public void aiMove(Integer move)
        {
            IntPointer currentRow = new IntPointer(currentTurn.get());
            int nextPlayerNumber = getNextTurn();
            int marbles = pitsForEachPlayers[currentRow.getValue()].pits[move].emptyPit();



            IntPointer position = new IntPointer(move+1);
            if(move+1>=COLUMNS)
                position.setValue(null);


            Pit currentPit = null;

            while(marbles>0)
            {
                currentPit = getNextPit(currentRow, position);

                currentPit.incrementMarbleCount();

                marbles--;

                changeVariablesForNextPit(currentRow, position);
            }
            captureThatWasJustPerformed=null;

            //the last marble did not go into a mancala
            if(currentPit!=null && !currentPit.isMancala)
            {
                int row =  currentTurn.get();
                int nextRow = getNextRow(row);
                int correspondingColumn = COLUMNS - 1 - currentPit.getPosition();
                Pit correspondingPit = pitsForEachPlayers[nextRow].pits[correspondingColumn];

                //if the last marble was placed in the currentPlayer's row, and if that pit has only 1 marble in it and if there are marbles in the opponents pit
                if(currentPit.isInRow(row) && currentPit.marbleCount==1 && correspondingPit.marbleCount>0)
                {
                    pitsForEachPlayers[row].mancalaPit.takeAllFrom(correspondingPit);
                    pitsForEachPlayers[row].mancalaPit.takeAllFrom(currentPit);

                    captureThatWasJustPerformed = currentPit.getPosition()+1;

                    //System.out.println("captureThatWasJustPerformed is "+captureThatWasJustPerformed);
                }
                //changes the current turn
                currentTurn.set(nextPlayerNumber);
            }

            if(gameIsOver())
            {
                for(PitRow rowOfPits : pitsForEachPlayers)
                    rowOfPits.putPitMarblesIntoMancala();
            }
        }

        private void changeVariablesForNextPit(IntPointer row, IntPointer column)
        {
            //gets the next pit
            if(column.getValue()==null)
            {
                column.setValue(0);
                row.setValue(getNextRow(row.getValue()));
            }
            else if (column.getValue()>=COLUMNS-1)
            {
                if(row.getValue()==currentTurn.get())
                    column.setValue(null);
                else
                {
                    column.setValue(0);
                    row.setValue(getNextRow(row.getValue()));
                }
            }
            else
                column.setValue(column.getValue() + 1);
        }

        private Pit getNextPit(IntPointer row, IntPointer column)
        {
            //gets the next pit
            if(column.getValue()==null)
                return pitsForEachPlayers[row.getValue()].mancalaPit;

            return pitsForEachPlayers[row.getValue()].pits[column.getValue()];
        }



        /**
         * @return a copy of this MancalaBoard
         */
        public MancalaBoard clone()
        {
            MancalaBoard copy = new MancalaBoard();

            for(int a = 0 ; a<pitsForEachPlayers.length;a++)
                copy.pitsForEachPlayers[a] = pitsForEachPlayers[a].clone();


            copy.currentTurn.set(this.getCurrentTurn());
            copy.captureThatWasJustPerformed=captureThatWasJustPerformed;
            return copy;
        }

        /**
         *
         * @return a string representation of this MancalaBoard
         */
        public String toString()
        {
            StringBuilder thirdRow = new StringBuilder();

            StringBuilder secondRow = new StringBuilder(pitsForEachPlayers[0].mancalaPit+" ");

            StringBuilder firstRow = new StringBuilder();


            for(int a=0;a<secondRow.length();a++)
            {
                thirdRow.append(' ');
                firstRow.append(' ');
            }

            firstRow.append(pitsForEachPlayers[0].toStringDecendingOrder());

            thirdRow.append(pitsForEachPlayers[1].toStringAscendingOrder());



            final int spacesToAdd = thirdRow.length() - secondRow.length();
            for(int a=0;a<spacesToAdd;a++)
                secondRow.append(' ');
            secondRow.append(pitsForEachPlayers[1].mancalaPit);

            StringBuilder boardRepresentation = new StringBuilder();


            boardRepresentation.append(firstRow);

            boardRepresentation.append('\n');

            boardRepresentation.append(secondRow);

            boardRepresentation.append('\n');

            boardRepresentation.append(thirdRow);


            return boardRepresentation.toString();
        }

        /**
         * Checks if a row of pits is empty, which if true means the game is over
         *
         * @return true if the game is over, false if the game is not over yet
         */
        public boolean gameIsOver()
        {
            for(PitRow rowOfPits : pitsForEachPlayers)
            {
                if(rowOfPits.hasNoMarbles())
                    return true;
            }
            return false;
        }


        private static int getNextRow(int row)
        {
            return (row+1)%PLAYERS;
        }

        @SuppressWarnings("unused")
        private static int getPreviousRow(int row)
        {
            return Math.abs(row-1)%PLAYERS;
        }

        public Integer getCaptureThatWasJustPerformed()
        {
            return captureThatWasJustPerformed;
        }

        public int getPit(int playerNumber, int pitNumber)
        {
            return  pitsForEachPlayers[playerNumber].pits[pitNumber].marbleCount;
        }

        public List<Integer> generateLegalMoves()
        {
            List<Integer> legalMoves = new LinkedList<>();
            if(this.gameIsOver())
                return legalMoves;

            int currentTurn = getCurrentTurn();
            PitRow currentPlayersPits = pitsForEachPlayers[currentTurn];
            for (int a = 0; a<currentPlayersPits.pits.length;a++)
            {
                if(currentPlayersPits.pits[a].marbleCount>0)
                    legalMoves.add(a);
            }
            return legalMoves;
        }



        public void undoLastClickMove()
        {
            for(int a = 0 ; a<pitsForEachPlayers.length;a++)
                pitsForEachPlayers[a] = previousBoard.pitsForEachPlayers[a];

            currentTurn.set(previousBoard.getCurrentTurn());
            captureThatWasJustPerformed= previousBoard.captureThatWasJustPerformed;

            previousBoard = null;

        }

    public void setCurrentTurn(int currentTurn)
    {
        this.currentTurn.set(currentTurn);
    }
}
