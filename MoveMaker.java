// a MoveMaker wraps the process that decides on a move given a position,
// whether the move is gotten from the UI, the server, or a local AI

package chai;

import javafx.concurrent.Worker;
import chesspresso.position.Position;

interface MoveMaker {

	public abstract void start(Position position);
	public void reset();   // set state to READY
	
	public Worker.State getState();
	public short getMove();
	
}
