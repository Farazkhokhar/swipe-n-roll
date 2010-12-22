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

import se.anyro.snr.Physics;
import se.anyro.snr.R;
import se.anyro.snr.SizeUtil;
import se.anyro.snr.SwipeNRoll;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Laser extends Body {

	private Drawable mCannonDrawable;
	private Drawable mBeamDrawable;
	private float mWidth, mHeight;
	private Rect mBeamBounds = new Rect();
	private Vector2 mPosition;
	private Point mScreenPos;
	private boolean mBlocked = false;

	public Laser(float y) {
		super(0, y);
		
		mCollider = true;
		
		mPosition = new Vector2(0, y);
		mWidth = 17f/320f * Physics.GAME_WIDTH;
		mHeight = 30f/480 * Physics.GAME_HEIGHT;

		// Init graphics
		mCannonDrawable = SwipeNRoll.resources.getDrawable(R.drawable.laser_cannon);
		mBeamDrawable = SwipeNRoll.resources.getDrawable(R.drawable.laser);
		
		// Calculate cannon bounds
		int right = SizeUtil.getScreenWidth();
		int left = right - SizeUtil.toScreen(mWidth);
		mScreenPos = new Point();
		SizeUtil.toScreen(0, y, mScreenPos);
		int halfHeight = SizeUtil.toScreen(mHeight/2f);
		int top = mScreenPos.y - halfHeight;
		int bottom = mScreenPos.y + halfHeight;		
		mCannonDrawable.setBounds(left, top, right, bottom);
		
		// Calculate beam bounds
		mBeamBounds.right = left;
		mBeamBounds.left = 0;
		mHeight = 6f/480f * Physics.GAME_HEIGHT;
		halfHeight = SizeUtil.toScreen(mHeight/2f);
		mBeamBounds.top = mScreenPos.y - halfHeight;
		mBeamBounds.bottom = mScreenPos.y + halfHeight;		
		mBeamDrawable.setBounds(mBeamBounds);
		
		// Init physics
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsBox(Physics.HALF_WIDTH, mHeight / 2f);
		
		Fixture mFixture = mBody.createFixture(polyShape, 0.1f);
		mFixture.setRestitution(0.2f);		
		
		polyShape.dispose();
	}

	@Override
	public void draw(Canvas canvas) {
		mBeamDrawable.draw(canvas);
		mCannonDrawable.draw(canvas);
	}

	public void block(float x) {
		if (mBlocked)
			return;
		mBlocked = true;
		
		// Block physics
		mPosition.x = x + Physics.HALF_WIDTH;
		mBody.setTransform(mPosition, 0);
		
		// Block graphics
		SizeUtil.toScreen(x, 0, mScreenPos);
		mBeamBounds.left = mScreenPos.x;
		mBeamDrawable.setBounds(mBeamBounds);
	}

	public void unBlock() {
		if (!mBlocked)
			return;
		mBlocked = false;
		
		// Unblock physics
		mPosition.x = 0;
		mBody.setTransform(mPosition, 0);
		
		// Unblock graphics
		mBeamBounds.left = 0;
		mBeamDrawable.setBounds(mBeamBounds);
	}
}
