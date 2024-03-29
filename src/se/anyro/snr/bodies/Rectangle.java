/*
 * Copyright (C) 2010 Adam Nyb�ck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.anyro.snr.bodies;

import se.anyro.snr.SizeUtil;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public abstract class Rectangle extends Body {

	protected Point mScreenPos = new Point();
	protected int mHalfWidth, mHalfHeight;
	
	protected Drawable mDrawable;
	
	private float mWidth, mHeight;
	protected Fixture mFixture;

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
		
		mFixture = mBody.createFixture(polyShape, 0.1f);
		mFixture.setRestitution(0.2f);		
		
		polyShape.dispose();
		
		// Init graphics
		mHalfWidth = SizeUtil.toScreen(width / 2f);
		mHalfHeight = SizeUtil.toScreen(height / 2f);
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
