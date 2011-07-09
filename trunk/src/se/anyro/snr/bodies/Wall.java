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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

public class Wall extends Rectangle {
	
	private WallOrientation mOrientation;
	private String mText;
	protected Drawable mNormalImage, mPressedImage;
	private Paint mTextPaint;

	public enum WallOrientation {
		HORIZONTAL,
		VERTICAL,
		NONE
	}

	public Wall(float x, float y, float width, float height) {
		super(x, y, width, height, false);
		
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffff00);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextAlign(Align.CENTER);
		mTextPaint.setTextSize(20);
		
		if (width > height) {
			mOrientation = WallOrientation.HORIZONTAL;
		} else if (height > width) {
			mOrientation = WallOrientation.VERTICAL;
		} else {
			mOrientation = WallOrientation.NONE;
		}
		
		// Init graphics
		mNormalImage = SwipeNRoll.sResources.getDrawable(R.drawable.wall_normal);
		mPressedImage = SwipeNRoll.sResources.getDrawable(R.drawable.wall_pressed);
		mDrawable = mNormalImage;
	}
	
	public Wall(float x, float y, float width, float height, String text) {
		this(x, y, width, height);
		mText = text;
	}
	
	public boolean contains(int x, int y) {
		return mDrawable.getBounds().contains(x, y);
	}
	
	public void onTouchStart() {
		mPressedImage.setBounds(mNormalImage.getBounds());
		mDrawable = mPressedImage;
	}
	
	public void onTouchEnd() {
		mNormalImage.setBounds(mPressedImage.getBounds());
		mDrawable = mNormalImage;
	}

	public WallOrientation getOrientation() {
		return mOrientation;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mText != null) {
			canvas.drawText(mText, mScreenPos.x, mScreenPos.y + 5, mTextPaint);
		}
	}
}
