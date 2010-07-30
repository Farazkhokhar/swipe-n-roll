package se.anyro.snr.bodies;

import se.anyro.snr.R;
import se.anyro.snr.SwipeNRoll;


public class SquareHole extends Rectangle {

	public SquareHole(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		
		mCollider = true;
		
		// Init graphics
		mDrawable = SwipeNRoll.resources.getDrawable(R.drawable.hole_square);
	}
}
