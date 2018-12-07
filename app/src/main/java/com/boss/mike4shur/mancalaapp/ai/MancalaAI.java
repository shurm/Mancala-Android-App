package com.boss.mike4shur.mancalaapp.ai;

import com.boss.mike4shur.mancalaapp.board.MancalaBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mike 4 Shur on 1/31/2018.
 */

public class MancalaAI
{
    private static final int MAXIMIZING_PLAYER = 0;
    @SuppressWarnings("unused")
    private static final int MINIMIZING_PLAYER = 1;

    //how many moves ahead in the future the minimax will check
    private static final int DEPTH = 8;

    private AIDifficulty aiDifficulty;

    public MancalaAI(AIDifficulty aiDifficulty)
    {
        this.aiDifficulty = aiDifficulty;
    }


    /* Minimax (recursive) at level of depth for maximizing or minimizing player
    with alpha-beta pruning.  */
    private RatingPackage minimax(MancalaBoard board, int depth, int alpha, int beta)
    {
        // Generate possible next moves in a list
        List<Integer> nextMoves = board.generateLegalMoves();

        if (nextMoves.isEmpty() || depth == 0)
        {
            // Gameover or depth reached, evaluate score
            int score = Rating.evaluateBoard(board, nextMoves.isEmpty());
            return new RatingPackage(score ,-1, board);
        }
        else
        {
            RatingPackage bestResult = new RatingPackage(0,-1,null);
            for (int move : nextMoves)
            {
                MancalaBoard nextBoard = board.clone();

                //try this move for the current "player"
                nextBoard.aiMove(move);

                RatingPackage score = minimax(nextBoard,depth - 1, alpha, beta);

                if (board.getCurrentTurn() == MAXIMIZING_PLAYER) {  // is maximizing player

                    if (score.score > alpha) {
                        alpha = score.score;
                        bestResult.move = move;
                        bestResult.board = score.board;
                    }
                } else {  // is minimizing player

                    if (score.score < beta) {
                        beta = score.score;
                        bestResult.move = move;
                        bestResult.board = score.board;
                    }
                }


                // Prune game tree
                if (alpha >= beta)
                    break;
            }
            bestResult.score = (board.getCurrentTurn() == MAXIMIZING_PLAYER) ? alpha : beta;
            return bestResult;
        }
    }



    /**
     *
     * Generates and rates the possible moves that the current player is able
     * to make using the minimax with alpha-beta pruning algorithm
     * @param board the provided MancalaBoard
     * @return returns a sorted list of (best to worst) moves, along with each
     * move's resulting future "rational" board and that board's score,
     * that the current player may perform
     */
    public List<RatingPackage> generateAndRateLegalMoves(MancalaBoard board)
    {
        List<RatingPackage> ratedLegalMoves = new ArrayList<>();

        List<Integer> legalMoves = board.generateLegalMoves();

        final int alpha = -1 * Rating.MAX;
        final int beta = Rating.MAX;

        for (int move :legalMoves)
        {
            MancalaBoard nextBoard = board.clone();
            nextBoard.aiMove(move);

            RatingPackage ratedMove = minimax(nextBoard, DEPTH-1, alpha,  beta);
            ratedMove.move = move;
            ratedLegalMoves.add(ratedMove);
        }
        if(board.getCurrentTurn() == 0)
            Collections.sort(ratedLegalMoves, Rating.PLAYER_1_COMPARATOR);
        else
            Collections.sort(ratedLegalMoves, Rating.PLAYER_2_COMPARATOR);

        return ratedLegalMoves;

    }

    /**
     * Generates and rates the possible moves that the current player is able
     * to make using the minimax with alpha-beta pruning algorithm
     *
     * @param mancalaBoard the provided MancalaBoard
     * @return returns the move chosen by the AI player
     */
    public Integer computeMove(MancalaBoard mancalaBoard)
    {
        List<RatingPackage> ratedPossibleMoves = generateAndRateLegalMoves(mancalaBoard);
        if(ratedPossibleMoves.isEmpty())
            return null;

        int range = (aiDifficulty.getRangeEnd() - aiDifficulty.getRangeStart() + 1);

        int randomMoveWithinRange = (int)(Math.random()*range) + aiDifficulty.getRangeStart();

        if(randomMoveWithinRange>=ratedPossibleMoves.size())
            return ratedPossibleMoves.get(ratedPossibleMoves.size()-1).move;
        return ratedPossibleMoves.get(randomMoveWithinRange).move;
    }
}