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

import se.anyro.snr.R;
import se.anyro.snr.SwipeNRoll;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ball extends Circle {
	
	private static final float RADIUS = 1;
	
	public Ball(float x, float y) {
		super(x, y, RADIUS, false);
		
		mDrawable = SwipeNRoll.sResources.getDrawable(R.drawable.ball);

		mBody.setType(BodyType.DynamicBody);
		mFixture.setRestitution(0.4f);
	}
}