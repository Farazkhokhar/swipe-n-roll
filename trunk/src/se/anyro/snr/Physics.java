/*
 * Copyright (C) 2010 Adam NybŠck
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

package se.anyro.snr;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Physics {
	private final float timeStep = (1f / 60f);
	private final int iterations = 5;
	private long lastTime = System.currentTimeMillis();

	private static Vector2 sGravity = new Vector2();
	private static World sWorld = new World(sGravity, true);
	private static Body sGround;
	
	public static int GAME_WIDTH = 20;
	public static int GAME_HEIGHT = 30;
	public static int HALF_WIDTH = GAME_WIDTH / 2;
	public static int HALF_HEIGHT = GAME_HEIGHT / 2;

	public Physics(ContactListener listener) {
		sWorld.setContactListener(listener);

		PolygonShape wallShape = new PolygonShape();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		
		// Upper wall
		wallShape.setAsEdge(new Vector2(-HALF_WIDTH, HALF_HEIGHT), new Vector2(HALF_WIDTH, HALF_HEIGHT));		
		com.badlogic.gdx.physics.box2d.Body body = sWorld.createBody(bodyDef);
		body.createFixture(wallShape, 0);
		
		// Right wall
		wallShape.setAsEdge(new Vector2(HALF_WIDTH, HALF_HEIGHT), new Vector2(HALF_WIDTH, -HALF_HEIGHT));		
		body = sWorld.createBody(bodyDef);
		body.createFixture(wallShape, 0);

		// Lower wall
		wallShape.setAsEdge(new Vector2(HALF_WIDTH, -HALF_HEIGHT), new Vector2(-HALF_WIDTH, -HALF_HEIGHT));		
		body = sWorld.createBody(bodyDef);
		body.createFixture(wallShape, 0);

		// Left wall
		wallShape.setAsEdge(new Vector2(-HALF_WIDTH, -HALF_HEIGHT), new Vector2(-HALF_WIDTH, HALF_HEIGHT));		
		body = sWorld.createBody(bodyDef);
		body.createFixture(wallShape, 0);

		wallShape.dispose();
		
		// Ground for joints		
		sGround = sWorld.createBody(bodyDef);
	}

	public void update() {
		// Calculate time diff
		long now = System.currentTimeMillis();
		float timeDiff = (now - lastTime) / 1000f;
		lastTime = now;
		if (timeDiff > timeStep) {
			timeDiff = timeStep;
		}
		
		sGravity.x = -Accelerometer.getX() * 15;
		sGravity.y = -Accelerometer.getY() * 15;
		sWorld.setGravity(sGravity);
		
		sWorld.step(timeDiff, iterations, iterations);
	}

	public static Body createBody(BodyDef bodyDef) {
		return sWorld.createBody(bodyDef);
	}
	
	public static void destroyBody(Body body) {
		sWorld.destroyBody(body);
	}

	public static Joint createJoint(JointDef jointDef) {
		return sWorld.createJoint(jointDef);
	}
	
	public static void destroyJoint(Joint joint) {
		sWorld.destroyJoint(joint);
	}

	public static Body getGround() {
		return sGround;
	}
}
