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
import android.graphics.drawable.GradientDrawable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Circle extends Body {

	protected Fixture mFixture;
	private GradientDrawable mDrawable;
	private Point screenPos = new Point();
	private int mScreenRadius;
	
	public Circle(float x, float y, float radius, boolean isHole, int innerColor, int outerColor) {
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
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[] { innerColor, outerColor });
		mDrawable.setShape(GradientDrawable.OVAL);
        mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		mDrawable.setGradientRadius(mScreenRadius * 2);
	}

	@Override
	public void draw(Canvas canvas) {
		Vector2 bodyPos = mBody.getPosition();
		SizeUtil.toScreen(bodyPos.x, bodyPos.y, screenPos);
		
		mDrawable.setBounds(screenPos.x - mScreenRadius, screenPos.y - mScreenRadius, screenPos.x + mScreenRadius,
				screenPos.y + mScreenRadius);
		mDrawable.draw(canvas);
	}
}
