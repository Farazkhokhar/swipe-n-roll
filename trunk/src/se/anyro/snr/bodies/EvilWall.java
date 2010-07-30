package se.anyro.snr.bodies;

import se.anyro.snr.R;
import se.anyro.snr.SwipeNRoll;

public class EvilWall extends Wall {

	public EvilWall(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		mCollider = true;
		
		// Init graphics
		normalImage = SwipeNRoll.resources.getDrawable(R.drawable.wall_evil_normal);
		pressedImage = SwipeNRoll.resources.getDrawable(R.drawable.wall_evil_pressed);
		mDrawable = normalImage;
	}

}
