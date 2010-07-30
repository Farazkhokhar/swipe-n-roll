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

package se.anyro.snr.bodies;

import se.anyro.snr.R;
import se.anyro.snr.SwipeNRoll;
import android.graphics.drawable.Drawable;

public class Wall extends Rectangle {
	
	private WallOrientation mOrientation;
	
	protected Drawable normalImage, pressedImage;

	public enum WallOrientation {
		HORIZONTAL,
		VERTICAL,
		NONE
	}

	public Wall(float x, float y, float width, float height) {
		super(x, y, width, height, false);
		
		if (width > height) {
			mOrientation = WallOrientation.HORIZONTAL;
		} else if (height > width) {
			mOrientation = WallOrientation.VERTICAL;
		} else {
			mOrientation = WallOrientation.NONE;
		}
		
		// Init graphics
		normalImage = SwipeNRoll.resources.getDrawable(R.drawable.wall_normal);
		pressedImage = SwipeNRoll.resources.getDrawable(R.drawable.wall_pressed);
		mDrawable = normalImage;
	}
	
	public boolean contains(int x, int y) {
		return mDrawable.getBounds().contains(x, y);
	}
	
	public void onTouchStart() {
		pressedImage.setBounds(normalImage.getBounds());
		mDrawable = pressedImage;
	}
	
	public void onTouchEnd() {
		normalImage.setBounds(pressedImage.getBounds());
		mDrawable = normalImage;
	}

	public WallOrientation getOrientation() {
		return mOrientation;
	}
}
