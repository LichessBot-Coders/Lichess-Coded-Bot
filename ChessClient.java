package chai;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import chesspresso.Chess;
import chesspresso.position.Position;

public class ChessClient extends Application {

	private static final int PIXELS_PER_SQUARE = 64;
	private static final String welcomeMessage = 
			"Welcome to CS 76 chess.  Moves can be made using algebraic notation;"
			+ " for example the command c2c3 would move the piece at c2 to c3.  \n";
		
	
	TextField commandField;
	TextArea logArea;
	
	BoardView boardView;
	ChessGame game;
	// RandomMoveSource[] playerMoveSources;

	MoveMaker[] moveMaker;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("CS 76 Chess");

                /* 
                 * Comment or uncomment the following lines to run varying
                 * test cases!
                 */

                /*
                 * ===========================================================
                 *                Set Up for a New Game
                 * ===========================================================
                 */ 
//                game = new ChessGame();              


                /*
                 * ===========================================================
                 *                Set Up for Test Cases
                 * ===========================================================
                 */ 

                /* Test Case 0: (Very Easy) */
//                game = new ChessGame("k7/r7/1r6/8/8/8/8/6K1 w - - 0 1");
                
                /* Test Case 1: (Easy) */
//                game = new ChessGame("r5k1/p3Qpbp/2p3p1/1p6/q3bN2/6PP/PP3P2/K2RR3 b - - 0 1");
//                game = new ChessGame("6k1/Q4pp1/2pq2rp/3p2n1/4r3/2PN4/P4PPP/2R2R1K b - - 0 1");               
//                game = new ChessGame("8/4B2p/7n/4pqpk/P2n1b2/R3P2P/2r3P1/1Q3R1K b - - 0 1"); 
//                game = new ChessGame("2kr3r/1bbp1p2/p3pp2/1p4q1/4P3/PNN1PBP1/1PP3KP/1R1Q1R2 b - - 0 1"); 
//                game = new ChessGame("5rk1/p4ppp/2b2q1P/7B/4p1Q1/5PR1/PPPb2R1/1K6 b - - 0 1"); 
//                game = new ChessGame("3q2k1/1pp2pp1/7p/5b2/1P6/2Q2P2/r5PP/2BrRK1R b - - 2 25"); 

                /* Test Case 2: (Medium) */
//                game = new ChessGame("5r2/p1p1p1k1/1p2prpp/4R3/3P2P1/P1P1Q3/2q1R2P/6K1 b - - 1 28");
//                game = new ChessGame("r2qk2r/pp6/2pbp3/2Pp1p2/3PBPp1/4PRp1/PP1BQ1P1/4R1K1 b kq - 0 20"); 
//                game = new ChessGame("2rr2k1/Qp4pp/4bp2/4n3/4B3/1P2B1P1/P3q2P/RK5R b - - 1 24");
//                game = new ChessGame("8/8/8/8/8/k1B5/BN6/K7 w - - 0 1"); 
//                game = new ChessGame("2b1r3/1p2qpkp/2p3p1/4r3/R4Q2/2P2RP1/P1B2P1P/6K1 b - - 0 1");
                
                /* Test Case 3: (Hard) */
                game = new ChessGame("2rr2k1/Qp4pp/4bp2/1q2n3/4B3/1P2B1P1/PK2P2P/R6R b - - 0 23"); 
//                game = new ChessGame("7r/kp1PQ3/p7/2p5/1q1np3/8/PP1R3P/2K2B2 b - - 6 34"); 

                /* Test Case 4: (Very Hard) */
//                game = new ChessGame("B2n4/7p/4p2p/4P1np/k1KP3p/8/1P2p3/4Bb2 w - - 0 1"); 


                // build the board
		boardView = new BoardView(game, PIXELS_PER_SQUARE);

		// build the text area for giving log info to user
		logArea = new TextArea();
	 	//logArea.setPrefColumnCount(50);
	 	logArea.setPrefRowCount(5);
	 	logArea.setEditable(false);
	 	logArea.setWrapText(true);
	 	log(welcomeMessage);
		
		// build the command entry text field
		commandField = new TextField();
		
		// request focus on the command field after the ui is built,
		//  to get a blinking cursor
		Platform.runLater(new Runnable() {
		    public void run() {
		        commandField.requestFocus();
		    }
		});

                /*
                 * ============================================================
                 *                  Movemakers for Different Tests
                 * ============================================================
                 */
		moveMaker = new MoveMaker[2];

                /* For Test Cases: White = Random, Black = Minimax */
//                moveMaker[Chess.WHITE] = new AIMoveMaker(new RandomAI());
//                moveMaker[Chess.BLACK] = new AIMoveMaker(new MinimaxAI(5, Chess.BLACK));

                /* For Test Cases: White = AlphaBeta w/ Defaul getMaterial, Black = AlphaBetaAI */
                moveMaker[Chess.WHITE] = new AIMoveMaker(new BadEvalAI(5, Chess.WHITE));
                moveMaker[Chess.BLACK] = new AIMoveMaker(new AlphaBetaAI(5, Chess.BLACK));

                /* For Having the Alpha Beta AI Play Against Itself */                               
//		  moveMaker[Chess.WHITE] = new AIMoveMaker(new AlphaBetaAI(5, Chess.WHITE));
//                moveMaker[Chess.BLACK]= new AIMoveMaker(new AlphaBetaAI(5, Chess.BLACK));

                /* To Play against the AI with a Text Field */
//                moveMaker[Chess.BLACK] = new AIMoveMaker(new AlphaBetaAI(5, Chess.BLACK));
//                moveMaker[Chess.WHITE] = new TextFieldMoveMaker();

		VBox vb = new VBox();
		vb.getChildren().addAll(boardView, logArea, commandField);
		vb.setSpacing(10);
		vb.setPadding(new Insets(10, 10, 10, 10));

		// add everything to a root stackpane, and then to the main window
		StackPane root = new StackPane();
		root.getChildren().add(vb);
		primaryStage.setScene(new Scene(root)); //, boardView.getPreferredWidth(), 600));
		primaryStage.show();

		// sets the game world's game loop (Timeline)
		Timeline timeline = new Timeline(1.0);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(.05), new GameHandler()));
		timeline.playFromStart();
		timeline.playFromStart();

		// moveMaker = new AIMoveMaker(new RandomAI());

	}

	private void log(String logText) {
		logArea.appendText(logText + "\n");
		
	}
	
	// As time passes, handle the state of the game
	private class GameHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			// System.out.println("timer fired");

			// System.out.println(boardView.ready());
			// setting activeMoveSource to null will cause a new one to be
			// created:

			MoveMaker mover = moveMaker[game.position.getToPlay()];

			if (mover.getState() == Worker.State.READY) {
				mover.start(game.position);
			} else if (mover.getState() == Worker.State.SUCCEEDED
					&& boardView.ready()) {
				short move = mover.getMove();
				boardView.doMove(move);
				mover.reset();

                                if (game.position.isMate()) {
                                  if (game.position.getToPlay() == Chess.WHITE) {
                                    System.out.println("Black wins!");
                                  } else {
                                    System.out.println("White wins!");
                                  }
                                }
                                  
			}

			// System.out.println("activeMoveSource state " +
			// activeMoveSource.getState());

			// short move =
			// playerMoveSources[0].getMove(game.position.toString());
			// System.out.println(move);
			// boardView.doMove(move);
		}

	}

	private class TextFieldMoveMaker implements MoveMaker,
			EventHandler<ActionEvent> {

		private Worker.State state;
		short move;

		public TextFieldMoveMaker() {
			this.state = Worker.State.READY;
			commandField.setOnAction(this);
			move = 0;
		}

		@Override
		public void start(Position position) {
			//String[] players = {"WHITE", "BLACK"};
			//commandField.setPromptText("Your move," + players[position.getToPlay()] + ".");
		}

		@Override
		public void reset() {
			commandField.setText("");
			this.state = Worker.State.READY;

		}

		@Override
		public State getState() {
			return state;
		}

		@Override
		public short getMove() {
			return move;
		}

		@Override
		public void handle(ActionEvent e) {
			String text = commandField.getText();
			if (text != null & text != "") {
				int fromSqi = Chess.strToSqi(text.charAt(0), text.charAt(1));
				int toSqi = Chess.strToSqi(text.charAt(2), text.charAt(3));

				move = game.findMove(fromSqi, toSqi);
				this.state = Worker.State.SUCCEEDED;

			}

		}

	}

	private class AIMoveMaker implements MoveMaker {
		ChessAI ai;
		AIMoveTask moveTask;

		public AIMoveMaker(ChessAI ai) {
			super();
			this.ai = ai;
			this.moveTask = null;
		}

		public void start(Position position) {
			Position duplicate = new Position(position);
                        moveTask = new AIMoveTask(ai, duplicate);
			new Thread(moveTask).start();
		}

		public Worker.State getState() {
			// short circuit if moveTask hasn't been initalized
			//  (threading bug fix by Yu-Han Lyu:)
			if (moveTask == null)
				return Worker.State.READY;
			if (moveTask.getState() == Worker.State.SUCCEEDED)
				return Worker.State.SUCCEEDED;
			return Worker.State.RUNNING;
		}

		public short getMove() {
			return moveTask.getValue();

		}

		public void reset() {
			this.moveTask = null;
		}

	
	}

	
}
