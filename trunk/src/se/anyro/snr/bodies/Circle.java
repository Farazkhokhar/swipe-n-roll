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
import android.graphics.drawable.Drawable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public abstract class Circle extends Body {

	protected Fixture mFixture;
	protected Drawable mDrawable;
	private Point mScreenPos = new Point();
	private int mScreenRadius;
	
	public Circle(float x, float y, float radius, boolean isHole) {
		super(x, y);
		
		// Init physics
		CircleShape circleShape = new CircleShape();
		if (isHole) {
			// Smaller physical radius to prevent early collision
			circleShape.setRadius(Math.max(0.1f, radius - 1));
		} else {
			circleShape.setRadius(radius);
		}
		mFixture = mBody.createFixture(circleShape, 0.1f);		
		circleShape.dispose();

		// Init graphics
		mScreenRadius = SizeUtil.toScreen(radius);
	}

	@Override
	public void draw(Canvas canvas) {
		Vector2 bodyPos = mBody.getPosition();
		SizeUtil.toScreen(bodyPos.x, bodyPos.y, mScreenPos);
		
		mDrawable.setBounds(mScreenPos.x - mScreenRadius, mScreenPos.y - mScreenRadius, mScreenPos.x + mScreenRadius,
				mScreenPos.y + mScreenRadius);
		mDrawable.draw(canvas);
	}
}
