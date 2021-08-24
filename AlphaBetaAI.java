package chai;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import chesspresso.position.Position;
import chesspresso.move.IllegalMoveException;
import chesspresso.Chess;
public class AlphaBetaAI implements ChessAI {


      static int winVal = 1000000;
      static int loseVal = -1000000;

      int maxDepth;
      int tempDepth;
      int IDdepth;
      int player;
      
        // Spot Precedence for White Pawns
      protected static int [] pawnTable = {
          0,  0,  0,  0,  0,  0,  0,  0,
          50, 50, 50, 50, 50, 50, 50, 50,
          10, 10, 20, 30, 30, 20, 10, 10,
          5,  5, 10, 25, 25, 10,  5,  5,
          0,  0,  0, 20, 20,  0,  0,  0,
          5, -5,-10,  0,  0,-10, -5,  5,
          5, 10, 10,-20,-20, 10, 10,  5,
          0,  0,  0,  0,  0,  0,  0,  0 };

        // Placement Precedence for all Knights
      protected static int [] knightTable = {
          -50,-40,-30,-30,-30,-30,-40,-50,
          -40,-20,  0,  0,  0,  0,-20,-40,
          -30,  0, 10, 15, 15, 10,  0,-30,
          -30,  5, 15, 20, 20, 15,  5,-30,
          -30,  0, 15, 20, 20, 15,  0,-30,
          -30,  5, 10, 15, 15, 10,  5,-30,
          -40,-20,  0,  5,  5,  0,-20,-40,
          -50,-40,-30,-30,-30,-30,-40,-50 };

      protected static int [] bishopPrecedence = {
          -20,-10,-10,-10,-10,-10,-10,-20,
          -10,  0,  0,  0,  0,  0,  0,-10,
          -10,  0,  5, 10, 10,  5,  0,-10,
          -10,  5,  5, 10, 10,  5,  5,-10,
          -10,  0, 10, 10, 10, 10,  0,-10,
          -10, 10, 10, 10, 10, 10, 10,-10,
          -10,  5,  0,  0,  0,  0,  5,-10,
          -20,-10,-10,-10,-10,-10,-10,-20 };

      protected static int [] rookTable = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
           -5,  0,  0,  0,  0,  0,  0, -5,
           -5,  0,  0,  0,  0,  0,  0, -5,
           -5,  0,  0,  0,  0,  0,  0, -5,
           -5,  0,  0,  0,  0,  0,  0, -5,
           -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  5,  5,  0,  0,  0 };
        
      protected static int [] queenTable = {
          -20,-10,-10, -5, -5,-10,-10,-20,
          -10,  0,  0,  0,  0,  0,  0,-10,
          -10,  0,  5,  5,  5,  5,  0,-10,
           -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
          -10,  5,  5,  5,  5,  5,  0,-10,
          -10,  0,  5,  0,  0,  0,  0,-10,
          -20,-10,-10, -5, -5,-10,-10,-20 };

      protected static int [] kingTable = {
          -30,-40,-40,-50,-50,-40,-40,-30,
          -30,-40,-40,-50,-50,-40,-40,-30,
          -30,-40,-40,-50,-50,-40,-40,-30,
          -30,-40,-40,-50,-50,-40,-40,-30,
          -20,-30,-30,-40,-40,-30,-30,-20,
          -10,-20,-20,-20,-20,-20,-20,-10,
           20, 20,  0,  0,  0,  0, 20, 20,
           20, 30, 10,  0,  0, 10, 30, 20 };


      public AlphaBetaAI(int d, int whiteOrBlack) {
        this.player = whiteOrBlack;
        this.maxDepth = d;
      }

      public short getMove(Position position) {
        return AlphaBetaDecision(position);
      }

      /*
       * Returns the maximum of the minimum possibilities
       */
      private synchronized short AlphaBetaDecision(Position position) {
        for (IDdepth = 1; IDdepth < maxDepth; IDdepth++) {
          short [] moves = position.getAllMoves();
          int bestIdx = -1;
          int bestVal = loseVal;
          int currVal = 0;
          this.tempDepth = this.IDdepth;

          // Loop through each move
          for (int i = 0; i < moves.length; i++) {
            try {
              position.doMove(moves[i]);

                // Do the test for each possible move
                if (position.isLegal()) {
                  currVal = minValue(position, 1, loseVal, winVal);
                } else {
                  currVal = loseVal; // Can't work on this move
                }
              position.undoMove();

              if (currVal >= bestVal) {
                bestIdx = i;
                bestVal = currVal;
              }
            } catch (Exception e) {
              System.out.println(e);
              System.out.println("Invalid move at start state...");
            }
          }

          if (bestVal == winVal)
            return(moves[bestIdx]);

          if ((IDdepth+1) == maxDepth) {
            return(moves[bestIdx]);
          }
        }
        // Return the move with the best utility at the highest depth
        //return moves[bestIdx];
        return -1;

      }

      private synchronized int maxValue(Position position, int currDepth, int alpha, int beta) {
        if (cutOffTest(position, currDepth)) {
          return utility(position, currDepth);
        }

        int util = loseVal;
        int curr = 0;
        short [] moves = position.getAllMoves();

        
        for (int i = 0; i < moves.length; i++) {
          try {
            position.doMove(moves[i]);

              if (position.isLegal()) {
                curr = minValue(position, currDepth + 1, alpha, beta);
              } else {
                curr = loseVal;
              }
            position.undoMove();
            if (curr > util)
              util = curr;

            if (util > beta)
              return util;
            
            if (util > alpha)
              alpha = util;

          } catch(Exception e) {
            System.out.println("Illegal move found...");
          }
        }

        return util;
      }

      private synchronized int minValue(Position position, int currDepth, int alpha, int beta) {
        if (cutOffTest(position, currDepth)) {
            return utility(position, currDepth);
        }

        int util = winVal;
        int curr = 0;

        short [] moves = position.getAllMoves();        
        
        for (int i = 0; i < moves.length; i++) {
          try {
            position.doMove(moves[i]);

              if (position.isLegal()) {
                curr = maxValue(position, currDepth + 1, alpha, beta);
              } else {
                curr = winVal;
              }
            position.undoMove();

            if (curr < util)
              util = curr;
            
            if (util < alpha)
              return util;

            if (util < beta)
              beta = util;
           } catch(Exception e) {
            System.out.println("Illegal move found...");
          } 
        }

        return util;
      }

      private boolean cutOffTest(Position position, int currDepth) {
        return (position.isTerminal() || (currDepth > this.tempDepth));
      }

      /*
       * Returns winVal or loseVal on a mate, 0 on a draw
       * otherwise, returns a random value in the range [loseVal, winVal]
       */
      private int utility(Position position, int d) {
        if (position.isTerminal()) {
          
          // Case of a mate
          if (position.isMate()) {
            if (position.getToPlay() == this.player) {
              return loseVal;
            } else {
              if (d <= this.tempDepth) {
                this.tempDepth = d;
                return winVal;
              } else {
                return loseVal;
              }
            }
          } else {
            return 0;
          }
        } else { // Non-terminal position
          return (getMaterials(position));
        }
      }

      /*
       * Returns material values of a position.
       * Uses the "Simplified Evaluation Function" from 
       *  https://chessprogramming.wikispaces.com/Simplified+evaluation+function
       * 
       */
      public int getMaterials(Position p) {
        int currStone = Chess.NO_STONE, row, col;
        int val = 0;
        int wPawns = 0, wRooks = 0, wBishops = 0, wQueens = 0, wKnights = 0, wKings = 0;
        int bPawns = 0, bRooks = 0, bBishops = 0, bQueens = 0, bKnights = 0, bKings = 0;

        for (int i = 0; i < Chess.NUM_OF_SQUARES; i++) {
          currStone = p.getStone(i);
          row = i / 8; // EG B3 = 17, 17%8 = 1, 1 = B
          col = i % 8; // With integer division => 17/8 = 2 (counting from zero)

          switch(currStone) {
            case Chess.WHITE_PAWN:
              wPawns++;
              val += (100 + (pawnTable[8*(7 - row) + col])); 
              break;

            case Chess.BLACK_PAWN:
              bPawns++;
              val -= (100 + (pawnTable[8 + (8* row) - (1 +col)]));
              break;


            case Chess.WHITE_ROOK:
              wRooks++;
              val += (500 + (rookTable[8*(7 - row) + col])); 
              break;

            case Chess.BLACK_ROOK:
              bRooks++;
              val -= (500 + (rookTable[8 + (8* row) - (1 +col)]));
              break;


            case Chess.WHITE_BISHOP:
              wBishops++;
              val += (330 + (rookTable[8*(7 - row) + col])); 
              break;

            case Chess.BLACK_BISHOP:
              bBishops++;
              val -= (330 + (rookTable[8 + (8* row) - (1 +col)]));
              break;


            case Chess.WHITE_KNIGHT:
              wKnights++;
              val += (320 + (knightTable[8*(7 - row) + col])); 
              break;

            case Chess.BLACK_KNIGHT:
              bKnights++;
              val -= (320 + (knightTable[8 + (8* row) - (1 +col)]));
              break;


            case Chess.WHITE_QUEEN:
              wQueens++;
              val += (900 + (queenTable[8*(7 - row) + col])); 
              break;

            case Chess.BLACK_QUEEN:
              bQueens++;
              val -= (900 + (queenTable[8 + (8* row) - (1 +col)]));
              break;


            case Chess.WHITE_KING:
              wKings++;
              val += (20000 + (kingTable[8*(7 - row) + col])); 
              break;

            case Chess.BLACK_KING:
              bKings++;
              val -= (20000 + (kingTable[8 + (8* row) - (1 +col)]));
              break;

          }
        }

        /* Customizations: */
        // Penalty for no-pawns
        if (wPawns == 0) {
          val -= 20;
        }

        if (bPawns == 0) {
          val += 20;
        }

        // Bonus for bishop pair
        if (wBishops == 2) {
          val += 40;
        }

        if (bBishops == 2) {
          val -= 40;
        }
        // Everything Calculated with respect to white
        return (this.player == Chess.WHITE ? val : -1 * val);
      }

}
