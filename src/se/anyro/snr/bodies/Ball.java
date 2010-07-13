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

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ball extends Circle {
	
	private static final float RADIUS = 1;
	
	public Ball(int x, int y) {
		super(x, y, RADIUS, false, 0xff6666cc, 0xff111155);
		mBody.setType(BodyType.DynamicBody);
		mFixture.setRestitution(0.4f);
	}
}