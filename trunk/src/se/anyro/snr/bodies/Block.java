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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Block extends Body {
	
	private float width, hight;
	
	private Point screenPos = new Point();
	
	public Block(float x, float y, float width, float hight) {
		super(x, y);
		
		// Init physics
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsBox(width / 2.0f, hight / 2.0f);
		
		Fixture fixture = mBody.createFixture(polyShape, 0.1f);
		fixture.setRestitution(0.2f);		
		polyShape.dispose();
		
		this.width = width;
		this.hight = hight;
	}
	
	@Override
	public void draw(Canvas canvas) {
		Vector2 bodyPos = mBody.getPosition();
		SizeUtil.toScreen(bodyPos.x, bodyPos.y, screenPos);
		int halfWidth = SizeUtil.toScreen(width / 2f);
		int halfHight = SizeUtil.toScreen(hight / 2f);

		canvas.drawRect(screenPos.x - halfWidth, screenPos.y - halfHight, screenPos.x + halfWidth, screenPos.y
				+ halfHight, mPaint);
	}
}
