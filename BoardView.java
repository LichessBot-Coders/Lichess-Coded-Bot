package chai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import chesspresso.Chess;
import chesspresso.move.Move;

public class BoardView extends Group {
	
	// the size of the font used to draw pieces,
	//   relative to the size of the square
	private final static double FONT_SCALE = .75;
	
	private int pixelsPerSquare;
	private ChessGame game;

	private int numCurrentAnimations;

	private Label[] pieceLabels; // an array of Pieces sorted by square index

	private static final Map<Short, String> unicodePiece;
	static {
		Map<Short, String> aMap = new HashMap<Short, String>();
		aMap.put(Chess.WHITE_KING, "\u2654");
		aMap.put(Chess.WHITE_QUEEN, "\u2655");
		aMap.put(Chess.WHITE_ROOK, "\u2656");
		aMap.put(Chess.WHITE_BISHOP, "\u2657");
		aMap.put(Chess.WHITE_KNIGHT, "\u2658");
		aMap.put(Chess.WHITE_PAWN, "\u2659");

		aMap.put(Chess.BLACK_KING, "\u265A");
		aMap.put(Chess.BLACK_QUEEN, "\u265B");
		aMap.put(Chess.BLACK_ROOK, "\u265C");
		aMap.put(Chess.BLACK_BISHOP, "\u265D");
		aMap.put(Chess.BLACK_KNIGHT, "\u265E");
		aMap.put(Chess.BLACK_PAWN, "\u265F");

		unicodePiece = Collections.unmodifiableMap(aMap);
	}

	public BoardView(ChessGame game, int pixelsPerSquare) {

		pieceLabels = new Label[64];

		this.game = game;
		this.pixelsPerSquare = pixelsPerSquare;

		Color colors[] = { Color.LIGHTGRAY, Color.WHITE };
		int color_index = 0; // alternating index to select tile color

		for (int r = 0; r < game.rows; r++) {
			for (int c = 0; c < game.columns; c++) {

				int x = c * pixelsPerSquare;
				int y = (game.rows - r - 1) * pixelsPerSquare;

				Rectangle square = new Rectangle(x, y, pixelsPerSquare,
						pixelsPerSquare);

				square.setFill(colors[color_index]);

				Text t = new Text(x, y + 12, "" + Chess.colToChar(c)
						+ Chess.rowToChar(r));

				this.getChildren().add(square);
				this.getChildren().add(t);

				// switch colors
				color_index = (color_index + 1) % 2;
			}
			// switch color back for a new row
			color_index = (color_index + 1) % 2;
		}

		// add pieces

		//int pixelsPerPiece = (pixelsPerSquare * 3) / 4;
		for (int r = 0; r < game.rows; r++) {
			for (int c = 0; c < game.columns; c++) {
				short stone = (short) game.getStone(c, r);

				if (stone != Chess.NO_STONE) {
					int x = c * pixelsPerSquare;
					int y = (game.rows - r - 1) * pixelsPerSquare;

					int sqi = Chess.coorToSqi(c, r);
					// System.out.println(sqi);

					pieceLabels[sqi] = new Label(unicodePiece.get(stone));

					pieceLabels[sqi].setTranslateX(x);
					pieceLabels[sqi].setTranslateY(y);

					pieceLabels[sqi].setMinWidth(pixelsPerSquare);
					pieceLabels[sqi].setPrefWidth(pixelsPerSquare);
					pieceLabels[sqi].setMaxWidth(pixelsPerSquare);

					pieceLabels[sqi].setFont(Font.font("Verdana",
							(int) (pixelsPerSquare * FONT_SCALE)));

					pieceLabels[sqi].setAlignment(Pos.CENTER);

					this.getChildren().add(pieceLabels[sqi]);
				}

				// System.out.println(unicodePiece.get(stone));
				// System.out.print(stone);
			}
			// System.out.println();
		}
		
		numCurrentAnimations = 0 ;

	}

	public boolean doMove(short move) {
		
		// Castling bugs fixed:
		//   rook could not move after castle.  Also,
		//   long castles animated improperly.
		//   Bugs found by Andrew Meier and Jon Preddy.
		//   Fix below by Jon Preddy (Jan 10, 2014).
		
		
		// bail out if the move isn't legal
		if (move == 0) {
			System.out.println("Illegal move attempted.");
			return false;
		}
	
		int fromSqi = Move.getFromSqi(move);
		int toSqi = Move.getToSqi(move);

		int r1 = Chess.sqiToRow(fromSqi);
		int c1 = Chess.sqiToCol(fromSqi);

		int r2 = Chess.sqiToRow(toSqi);
		int c2 = Chess.sqiToCol(toSqi);


		// System.out.println(fromsqi + " " + tosqi);

		//System.out.println("rc " + r1 + " " + c1);
		Label l = pieceLabels[fromSqi];
                if (Move.isPromotion(move)) {
                        int piece = Move.getPromotionPiece(move);
                        //this is what the pawn to be promoted to
                        short stone = (short)Chess.pieceToStone(piece,
                        this.game.position.getToPlay());
                        l.setText(unicodePiece.get(stone));
                }
		// animate captured piece (regular) 
		if (Move.isCapturing(move)) {
			animateCapture(pieceLabels[toSqi]);
			//this.getChildren().removeAll(pieceLabels[toSqi]);
		}

		// animate captured En Passant piece 
		if (Move.isEPMove(move)) {
			// capture column is c2, capture row is r1
			int captureSqi = Chess.coorToSqi(c2, r1);
			animateCapture(pieceLabels[captureSqi]);
		}

		// animate castle moves
		if(Move.isShortCastle(move) || Move.isLongCastle(move)) {
			int dx = -2;    // short castle
			int rookx = 7;
				
			// NEW CODE	
			int toRookSqi = 5;
			int fromRookSqi = 7;
					
			
			if(Move.isLongCastle(move)) {
				// NEW CODE
				dx = 3;
				rookx = 0;
						
				// NEW CODE
				toRookSqi = 3;
				fromRookSqi = 0;
			}
			Label rook = pieceLabels[Chess.coorToSqi(rookx, r1)];
			animateMove(rook, dx, 0);
					
			// NEW CODE		
			pieceLabels[toRookSqi] = pieceLabels[fromRookSqi];
			pieceLabels[fromRookSqi] = null;
		}
		
		// update the list of piece Labels
		pieceLabels[toSqi] = pieceLabels[fromSqi];
		pieceLabels[fromSqi] = null;

		//System.out.println(l.getText());

		Timeline timeline = new Timeline();

		if (timeline != null) {
			timeline.stop();
		}

		animateMove(l, c2 - c1, r2 - r1);

		this.game.doMove(move);

		return true;
		
	}
	
	public boolean doMove(int fromSqi, int toSqi) {

		short move = game.findMove(fromSqi, toSqi);
		return doMove(move);
		
	}

	// move the piece n by dx, dy cells
	private void animateMove(Node n, int dx, int dy) {
		numCurrentAnimations++;
		TranslateTransition tt = new TranslateTransition(Duration.millis(1000),
				n);
		tt.setByX(pixelsPerSquare * dx);
		tt.setByY(-pixelsPerSquare * dy);
		tt.setCycleCount(1);
		tt.setOnFinished(new animationFinished());

		tt.play();	
	
	}
	
	private void animateCapture(Node n) {
		numCurrentAnimations++;
		FadeTransition t = new FadeTransition(Duration.millis(1000), n);
		
		 t.setFromValue(1.0);
	     t.setToValue(0.0);
	     t.setOnFinished(new animationFinished());
	     t.play();
		
	}

	private class animationFinished implements EventHandler<ActionEvent> {
		   @Override
		    public void handle(ActionEvent event) {
		        numCurrentAnimations--;
		    }
	}
	
	// BoardView is "ready" if not currently animating
	public boolean ready() {
		return numCurrentAnimations == 0;
		
	}
	
}
