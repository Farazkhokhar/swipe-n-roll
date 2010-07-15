/*
 * Copyright (C) 2010 Adam Nybäck
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
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Block extends Body {
	
	private float mWidth, mHeight;
	private GradientDrawable mDrawable;
	private Point mScreenPos = new Point();
	private int mHalfWidth;
	private int mHalfHeight;
	
	public Block(float x, float y, float width, float height) {
		super(x, y);
		
		mWidth = width;
		mHeight = height;
		
		// Init physics
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsBox(width / 2.0f, height / 2.0f);
		
		Fixture fixture = mBody.createFixture(polyShape, 0.1f);
		fixture.setRestitution(0.2f);		
		polyShape.dispose();
		
		// Init graphics
		mHalfWidth = SizeUtil.toScreen(width / 2f);
		mHalfHeight = SizeUtil.toScreen(height / 2f);

		mDrawable = new GradientDrawable(Orientation.BL_TR, new int[] {0x99333322, 0x99334444, 0x99333322});
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

	@Override
	public float getWidth() {
		return mWidth;
	}

	@Override
	public float getHeight() {
		return mHeight;
	}
	
	@Override
	public Rect getScreenBounds() {
		return mDrawable.getBounds();
	}
}