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
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Block extends Rectangle {
	
	public Block(float x, float y, float width, float height) {
		super(x, y, width, height, false);
		
		// Init physics
		mBody.setFixedRotation(false);
		mBody.setType(BodyType.DynamicBody);
		
		// Init graphics
		GradientDrawable gradient = new GradientDrawable(Orientation.BL_TR, new int[] {0x99333322, 0x99334444, 0x99333322});
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		gradient.setSize(mHalfWidth * 2, mHalfHeight * 2);
		mDrawable = gradient;
	}
	
	@Override
	public void draw(Canvas canvas) {
		Vector2 bodyPos = mBody.getPosition();
		SizeUtil.toScreen(bodyPos.x, bodyPos.y, mScreenPos);
		canvas.save();
		canvas.rotate(-mBody.getAngle() / (float) Math.PI * 180, mScreenPos.x, mScreenPos.y);
		mDrawable.setBounds(mScreenPos.x - mHalfWidth, mScreenPos.y - mHalfHeight, mScreenPos.x + mHalfWidth, mScreenPos.y
				+ mHalfHeight);
		mDrawable.draw(canvas);
		canvas.restore();
	}
}