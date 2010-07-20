package se.anyro.snr.bodies;

import se.anyro.snr.SizeUtil;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public abstract class Rectangle extends Body {

	protected Point mScreenPos = new Point();
	protected int mHalfWidth, mHalfHeight;
	
	protected GradientDrawable mDrawable;
	
	private float mWidth, mHeight;

	public Rectangle(float x, float y, float width, float height, boolean innerCollision) {
		super(x, y);

		mWidth = width;
		mHeight = height;
		
		// Init physics
		PolygonShape polyShape = new PolygonShape();
		if (innerCollision)
			polyShape.setAsBox(width / 2f - 1, height / 2f - 1);
		else
			polyShape.setAsBox(width / 2f, height / 2f);
		
		Fixture fixture = mBody.createFixture(polyShape, 0.1f);
		fixture.setRestitution(0.2f);		
		
		polyShape.dispose();
		
		// Init graphics
		mHalfWidth = SizeUtil.toScreen(width / 2f);
		mHalfHeight = SizeUtil.toScreen(height / 2f);

		mDrawable = new GradientDrawable(Orientation.BL_TR, new int[] {0xff3333ff, 0xff3344ff, 0xff3333ff});
		mDrawable.setShape(GradientDrawable.RECTANGLE);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setSize(mHalfWidth * 2, mHalfHeight * 2);
	}

	@Override
	public void draw(Canvas canvas) {
		Vector2 bodyPos = mBody.getPosition();
		SizeUtil.toScreen(bodyPos.x, bodyPos.y, mScreenPos);
		mDrawable.setBounds(mScreenPos.x - mHalfWidth, mScreenPos.y - mHalfHeight, mScreenPos.x + mHalfWidth, mScreenPos.y
				+ mHalfHeight);
		mDrawable.draw(canvas);
	}

	public float getWidth() {
		return mWidth;
	}
	
	public float getHeight() {
		return mHeight;
	}

	public Rect getScreenBounds() {
		return mDrawable.getBounds();
	}
}
