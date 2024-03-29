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

public class EvilWall extends Wall {

	public EvilWall(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		mCollider = true;
		
		// Init graphics
		mNormalImage = SwipeNRoll.sResources.getDrawable(R.drawable.wall_evil_normal);
		mPressedImage = SwipeNRoll.sResources.getDrawable(R.drawable.wall_evil_pressed);
		mDrawable = mNormalImage;
	}

}
