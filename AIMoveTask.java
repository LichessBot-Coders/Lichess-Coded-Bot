package chai;

import javafx.concurrent.Task;
import chesspresso.position.Position;

public class AIMoveTask extends Task<Short> {
	
	private Position position = null;
	private ChessAI ai;
	
	public AIMoveTask(ChessAI ai, Position p) {
		super();
		position = p;	
		this.ai = ai;
		
	}

	@Override
	protected Short call() throws Exception {
		return ai.getMove(position);
	
	}
	
}
