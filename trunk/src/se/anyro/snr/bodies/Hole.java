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

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Hole extends Circle {
	
	public Hole(float x, float y) {
		super(x, y, 1.1f, true, 0xff555555, 0xff111111);
		
		collider = true;
		
		mBody.setType(BodyType.StaticBody);
	}
}
