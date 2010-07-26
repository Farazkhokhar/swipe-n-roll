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
import android.graphics.Canvas;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Base class for visual rigid bodies, a combination of the Body class of jbox2d
 * and ShapeDrawable.
 */
public abstract class Body {
	
	private Vector2 mStartPos;
	protected boolean mCollider = false;
	private static final Vector2 ZERO_VECTOR = new Vector2(0, 0);
	protected com.badlogic.gdx.physics.box2d.Body mBody;
	
	public Body(float x, float y) {
		
		// Init physics
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.allowSleep = false;
		bodyDef.position.set(x, y);
		bodyDef.fixedRotation = true;
		mBody = Physics.createBody(bodyDef);
		mBody.setUserData(this);
		
		mStartPos = new Vector2(x, y);
	}
	
	public Vector2 getPosition() {
		return mBody.getPosition();
	}
	
	public void setPosition(Vector2 position) {
		mBody.setTransform(position, 0);
		mBody.setLinearVelocity(ZERO_VECTOR);
	}
	
	public void reset() {
		setPosition(mStartPos);
	}
	
	public boolean isCollider() {
		return mCollider;
	}
	
	public abstract void draw(Canvas canvas);

	public void destroy() {
		Physics.destroyBody(mBody);
	}

	public void applyforce(Vector2 force) {
		mBody.applyForce(force, ZERO_VECTOR);
	}
}
