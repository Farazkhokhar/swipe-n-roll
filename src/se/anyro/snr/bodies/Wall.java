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
import android.graphics.drawable.GradientDrawable.Orientation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Wall extends Body {
		
	private Point screenPos = new Point();
	private int halfWidth;
	private int halfHeight;
	private GradientDrawable mDrawable;

	public Wall(float x, float y, float width, float hight) {
		super(x, y);

		// Init physics
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsBox(width / 2.0f, hight / 2.0f);
		
		mBody.setType(BodyType.StaticBody);
		
		Fixture fixture = mBody.createFixture(polyShape, 0.1f);
		fixture.setRestitution(0.2f);		
		
		polyShape.dispose();
		
		// Init graphics
		halfWidth = SizeUtil.toScreen(width / 2f);
		halfHeight = SizeUtil.toScreen(hight / 2f);

		mDrawable = new GradientDrawable(Orientation.BL_TR, new int[] {0x99333322, 0x99334444, 0x99333322});
		mDrawable.setShape(GradientDrawable.RECTANGLE);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setSize(halfWidth * 2, halfHeight * 2);
	}
	
	@Override
	public void draw(Canvas canvas) {
		Vector2 bodyPos = mBody.getPosition();
		SizeUtil.toScreen(bodyPos.x, bodyPos.y, screenPos);
		mDrawable.setBounds(screenPos.x - halfWidth, screenPos.y - halfHeight, screenPos.x + halfWidth, screenPos.y
				+ halfHeight);
		mDrawable.draw(canvas);
	}
	
	@Override
	public boolean contains(int x, int y) {
		return mDrawable.getBounds().contains(x, y);
	}
	
	@Override
	public void onTouchStart() {
		super.onTouchStart();
		mDrawable.setStroke(1, 0x66ffff00);
	}
	
	@Override
	public void onTouchEnd() {
		super.onTouchEnd();
		mDrawable.setStroke(0, 0);
	}
}
