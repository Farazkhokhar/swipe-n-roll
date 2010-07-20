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

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

public class Wall extends Rectangle {
		
	private static final int HIGHLIGHT_COLOR = 0x66ffff00;
	private static final int STROKE_COLOR = 0xff333333;
	
	private WallOrientation mOrientation;

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
		mDrawable = new GradientDrawable(Orientation.BL_TR, new int[] {0x99333322, 0x99334444, 0x99333322});
		mDrawable.setShape(GradientDrawable.RECTANGLE);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setSize(mHalfWidth * 2, mHalfHeight * 2);
		mDrawable.setStroke(1, STROKE_COLOR);
	}
	
	public boolean contains(int x, int y) {
		return mDrawable.getBounds().contains(x, y);
	}
	
	public void onTouchStart() {
		mDrawable.setStroke(1, HIGHLIGHT_COLOR);
	}
	
	public void onTouchEnd() {
		mDrawable.setStroke(1, STROKE_COLOR);
	}

	public WallOrientation getOrientation() {
		return mOrientation;
	}
}
