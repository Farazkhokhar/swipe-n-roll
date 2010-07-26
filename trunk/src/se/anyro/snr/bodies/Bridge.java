package se.anyro.snr.bodies;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.badlogic.gdx.math.Vector2;

public class Bridge extends Rectangle {

	Vector2 mForce = new Vector2();
	
	public Bridge(float x, float y, float width, float height) {
		super(x, y, width, height, false);
		
		mCollider = true;
		mFixture.setSensor(true);
		
		// Init graphics
		mDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] {0x00ffffff, 0x33ffffff, 0x00ffffff});
		mDrawable.setShape(GradientDrawable.RECTANGLE);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setSize(mHalfWidth * 2, mHalfHeight * 2);
	}

	public void collision(Body ball) {
		float posY = ball.getPosition().y;
		mForce.y = (posY - getPosition().y) * 4;
		ball.applyforce(mForce);
	}
}
