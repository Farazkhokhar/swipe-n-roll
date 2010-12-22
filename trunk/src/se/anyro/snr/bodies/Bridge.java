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
		GradientDrawable gradient = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] {0x00ffffff, 0x33ffffff, 0x00ffffff});
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		gradient.setSize(mHalfWidth * 2, mHalfHeight * 2);
		mDrawable = gradient;
	}

	public void collision(Body ball) {
		float posY = ball.getPosition().y;
		mForce.y = (posY - getPosition().y) * 4;
		ball.applyforce(mForce);
	}
}
