package se.anyro.snr.bodies;

import se.anyro.snr.SizeUtil;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

public class Water extends Rectangle {

	public Water(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		
		mCollider = true;
		
		// Init graphics
		mDrawable = new GradientDrawable(Orientation.BL_TR, new int[] {0xff3333cc, 0xff5555cc, 0xff3333cc});
		mDrawable.setShape(GradientDrawable.RECTANGLE);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setSize(SizeUtil.getScreenWidth(), SizeUtil.getScreenHeight());
	}
}
